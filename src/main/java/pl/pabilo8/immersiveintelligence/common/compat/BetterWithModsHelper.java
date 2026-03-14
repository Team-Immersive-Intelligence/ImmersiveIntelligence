package pl.pabilo8.immersiveintelligence.common.compat;

import betterwithmods.api.BWMAPI;
import betterwithmods.api.capabilities.CapabilityMechanicalPower;
import betterwithmods.api.tile.IMechanicalPower;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.DustUtils;
import pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy.RotationSide;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.MechanicalDevices;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityTransmissionBox;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 08.12.2025
 */
public class BetterWithModsHelper extends IICompatModule
{
	public static final ResourceLocation CAPABILITY_RES = new ResourceLocation(ImmersiveIntelligence.MODID, "bwm_transmision");

	@Override
	public void preInit()
	{
		DustUtils.registerDust(new IngredientStack("pileSand", 25), "sand");
		DustUtils.registerDust(new IngredientStack("pileGravel", 25), "gravel");
	}

	@Override
	public void registerRecipes()
	{
	}

	@Override
	public void init()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public String getName()
	{
		return "betterwithmods";
	}

	@Override
	public void postInit()
	{
		IIRotaryUtils.TORQUE_TILES.put(te -> te instanceof IMechanicalPower, this::handleBWMTorque);
	}

	private void handleBWMTorque(TileEntityTransmissionBox box, TileEntity tileEntity)
	{
		EnumFacing facing = box.getFacing();
		RotationSide side = box.energy.getSide(facing);
		if(side!=RotationSide.INPUT)
			return;

		int power = BWMAPI.IMPLEMENTATION.getPowerOutput(box.getWorld(), tileEntity.getPos(), facing.getOpposite());
		if(power > 0)
		{
			box.energy.grow(10*power, MechanicalDevices.dynamoBWMAxleTorque, 0.95f);
			box.tick = 40;
		}

	}

	@SubscribeEvent
	public void onAttachCapabilities(AttachCapabilitiesEvent<TileEntity> event)
	{
		if(event.getObject() instanceof TileEntityTransmissionBox)
			if(!event.getCapabilities().containsKey(CAPABILITY_RES))
				event.addCapability(CAPABILITY_RES, new BWMTransmissionBoxHandler((TileEntityTransmissionBox)event.getObject()));
	}

	private static class BWMTransmissionBoxHandler implements IMechanicalPower, ICapabilityProvider
	{
		TileEntityTransmissionBox box;

		BWMTransmissionBoxHandler(TileEntityTransmissionBox box)
		{
			this.box = box;
		}

		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing enumFacing)
		{
			return capability==CapabilityMechanicalPower.MECHANICAL_POWER&&box.energy.getSide(enumFacing)!=RotationSide.NONE;
		}

		@Nullable
		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing enumFacing)
		{
			return capability==CapabilityMechanicalPower.MECHANICAL_POWER?(T)this: null;
		}

		@Override
		public int getMechanicalOutput(EnumFacing facing)
		{
			RotationSide side = box.energy.getSide(facing);
			if(side==RotationSide.OUTPUT)
				return (int)(box.energy.getTorque()/MechanicalDevices.dynamoDefaultTorque);
			return 0;
		}

		@Override
		public int getMechanicalInput(EnumFacing facing)
		{
			RotationSide side = box.energy.getSide(facing);
			if(side==RotationSide.INPUT)
				return BWMAPI.IMPLEMENTATION.getPowerOutput(getBlockWorld(), getBlockPos().offset(facing), facing.getOpposite());
			return 0;
		}

		@Override
		public int getMaximumInput(EnumFacing facing)
		{
			return 6;
		}

		@Override
		public int getMinimumInput(EnumFacing facing)
		{
			return 0;
		}

		@Override
		public Block getBlock()
		{
			return IIContent.blockMechanicalDevice;
		}

		@Override
		public World getBlockWorld()
		{
			return box.getWorld();
		}

		@Override
		public BlockPos getBlockPos()
		{
			return box.getPos();
		}
	}
}
