package pl.pabilo8.immersiveintelligence.common.compat;

import mysticalmechanics.api.IMechCapability;
import mysticalmechanics.tileentity.TileEntityAxle;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryMath;
import pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.MechanicalDevices;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityTransmissionBox;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageRotaryPowerSync;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static mysticalmechanics.api.MysticalMechanicsAPI.MECH_CAPABILITY;

/**
 * @author GabrielV (gabriel@iiteam.net)
 * @since 21/04/2024 - 7:50 PM
 */
public class MysticalMechanicsAPIHelper extends IICompatModule
{
	public static final ResourceLocation CAPABILITY_RES = new ResourceLocation(ImmersiveIntelligence.MODID, "nuclear_device");

	@Override
	public void preInit()
	{
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
		return "mysticalmechanics";
	}

	@Override
	public void postInit()
	{
		IIRotaryUtils.TORQUE_BLOCKS.put(tileEntity -> tileEntity instanceof TileEntityAxle,
				aFloat -> aFloat*MechanicalDevices.dynamoAxleTorque);
	}

	@SubscribeEvent
	public void onAttachCapabilities(AttachCapabilitiesEvent<TileEntity> event)
	{
		if(event.getObject() instanceof TileEntityTransmissionBox)
		{
			if(!event.getCapabilities().containsKey(CAPABILITY_RES))
				event.addCapability(CAPABILITY_RES, new NuclearDeviceHandler((TileEntityTransmissionBox)event.getObject()));
		}
	}

	static class NuclearDeviceHandler implements IMechCapability, ICapabilityProvider
	{
		TileEntityTransmissionBox box;
		double power = 0.0;

		NuclearDeviceHandler(TileEntityTransmissionBox box)
		{
			this.box = box;
		}

		@Override
		public boolean isOutput(EnumFacing from)
		{
			return false;
		}

		@Override
		public boolean isInput(EnumFacing from)
		{
			return true;
		}

		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing enumFacing)
		{
			return capability==MECH_CAPABILITY;
		}

		@Nullable
		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing enumFacing)
		{
			return capability==MECH_CAPABILITY?(T)this: null;
		}

		@Override
		public double getPower(EnumFacing enumFacing)
		{
			return this.power;
		}

		@Override
		public void setPower(double v, EnumFacing enumFacing)
		{
			if(enumFacing==box.getFacing().getOpposite()) return;
			IILogger.info("Setting power to: "+v);
			this.power = v;
			calculatePower(enumFacing);
			//if (oldPower!=v) this.onPowerChange();
		}

		private void calculatePower(@Nonnull EnumFacing facing)
		{
			float[] st = IIRotaryMath.MMToRoF(this.power);
			box.energy.grow(Math.round(st[0]), Math.round(st[1]), 0.98f);
			if(box.getWorld().getTotalWorldTime()%20==0)
			{
				IIPacketHandler.INSTANCE.sendToAllAround(new MessageRotaryPowerSync(box.energy, 0, box.getPos()), IIPacketHandler.targetPointFromTile(box, 24));
			}
		}

		public void readFromNBT(NBTTagCompound tag)
		{
			this.power = tag.getDouble("mech_power");
		}

		public void writeToNBT(NBTTagCompound tag)
		{
			tag.setDouble("mech_power", this.power);
		}

		@Override
		public void onPowerChange()
		{
		}
	}
}