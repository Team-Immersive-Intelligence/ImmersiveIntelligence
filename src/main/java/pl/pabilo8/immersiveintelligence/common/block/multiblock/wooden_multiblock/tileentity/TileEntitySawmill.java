package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.api.rotary.CapabilityRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotationalEnergyBlock;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISawblade;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Sawmill;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSawmill;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageRotaryPowerSync;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 13-04-2020
 */
public class TileEntitySawmill extends TileEntityMultiblockProductionSingle<TileEntitySawmill, SawmillRecipe> implements IRotationalEnergyBlock, IBooleanAnimatedPartsBlock
{
	//Inventory Slots
	public static final int SLOT_INPUT = 0, SLOT_SAWBLADE = 1, SLOT_OUTPUT = 2, SLOT_SAWDUST = 3;

	//Inventory Handlers
	IItemHandler insertionHandler = getSingleInventoryHandler(SLOT_INPUT, true, false);
	IItemHandler dustExtractionHandler = getSingleInventoryHandler(SLOT_SAWDUST, false, true);
	//Recipe Output Handlers
	IItemHandler outputHandler = getSingleInventoryHandler(SLOT_OUTPUT), sawdustOutputHandler = getSingleInventoryHandler(SLOT_SAWDUST);

	public MultiblockInteractablePart vice;

	//Rotary Power
	public RotaryStorage rotation = new RotaryStorage(0, 0)
	{
		@Override
		public RotationSide getSide(@Nullable EnumFacing facing)
		{
			return facing==getFacing()?RotationSide.INPUT: RotationSide.NONE;
		}
	};

	public TileEntitySawmill()
	{
		super(MultiblockSawmill.INSTANCE);

		energyStorage = new FluxStorageAdvanced(0);
		inventory = NonNullList.withSize(4, ItemStack.EMPTY);
		vice = new MultiblockInteractablePart(20);
	}

	//--- NBT ---//

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		rotation.fromNBT(nbt.getCompoundTag("rotation"));
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		nbt.setTag("rotation", rotation.toNBT());
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);

		if(message.hasKey("rotation"))
			rotation.fromNBT(message.getCompoundTag("rotation"));
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case ROTARY_INPUT:
				return getPOI("rotary");
			case ITEM_INPUT:
				return getPOI("all_item_input");
			case ITEM_OUTPUT:
				return getPOI("item_output");
		}
		return new int[0];
	}

	//--- Capabilities ---//

	//7 out
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(isPOI(MultiblockPOI.ITEM_INPUT))
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntitySawmill master = master();
			if(isPOI("item_input"))
				return (T)master.insertionHandler;
			else if(isPOI("sawdust"))
				return (T)master.dustExtractionHandler;
		}


		return super.getCapability(capability, facing);
	}

	//--- Handling ---//

	@Override
	protected void onUpdate()
	{
		vice.update();

		if(!world.isRemote)
		{
			boolean receivesPower = false;

			//Self destruct
			if(rotation.getRotationSpeed() > Sawmill.rpmBreakingMax||rotation.getTorque() > Sawmill.torqueBreakingMax)
			{
				selfDestruct();
				return;
			}

			//Wheel or mechanical device connected to multiblock
			TileEntity te = world.getTileEntity(getPOIPos(MultiblockPOI.ROTARY_INPUT).offset(facing));

			if(te!=null)
				if(te.hasCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, facing.getOpposite()))
				{
					//Increase internal rotation if powered
					IRotaryEnergy cap = te.getCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, facing.getOpposite());
					assert cap!=null;
					if(rotation.handleRotation(cap, facing.getOpposite()))
						IIPacketHandler.INSTANCE.sendToAllAround(new MessageRotaryPowerSync(rotation, 0, getPos()), IIPacketHandler.targetPointFromTile(this, 24));
					receivesPower = true;
				}

			if(rotation.getTorque() > 0||rotation.getRotationSpeed() > 0)
			{
				//Decrease internal rotation if not powered
				if(!receivesPower)
				{
					rotation.grow(0, 0, 0.98f);
					IIPacketHandler.INSTANCE.sendToAllAround(new MessageRotaryPowerSync(rotation, 0, getPos()), IIPacketHandler.targetPointFromTile(this, 24));
				}

				//Hurt entities stepping on sawblade
				ItemStack sawStack = inventory.get(SLOT_SAWBLADE);

				//REFACTOR: capabilities for saw blades, instead of interfaces
				if(sawStack.getItem() instanceof ISawblade)
				{
					//TODO: 14.04.2023 simplify
					if(world.getTotalWorldTime()%Math.ceil(4/MathHelper.clamp(rotation.getRotationSpeed()/360, 0, 1))==0)
					{
						int hardness = ((ISawblade)sawStack.getItem()).getHardness(sawStack);
						Vec3i v = facing.getDirectionVec();
						List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class,
								new AxisAlignedBB(getBlockPosForPos(2).offset(EnumFacing.UP)).offset(v.getX()*0.5, v.getY()*0.5, v.getZ()*0.5));
						for(EntityLivingBase l : entities)
							l.attackEntityFrom(IIDamageSources.SAWMILL_DAMAGE, hardness);
					}

				}

			}
		}

		super.onUpdate();

	}

	@SideOnly(Side.CLIENT)
	private void spawnDustParticleLast()
	{
		Vec3d pos = new Vec3d(getBlockPosForPos(2)).addVector(0.5, 0.75, 0.5);
		Vec3d facing = new Vec3d(getFacing().getDirectionVec());
		facing = facing.scale(0.65f);

		float mod = (float)(Math.random()*2f);
		float[] rgb = getCurrentProcessColor();

		ParticleRedstone particle = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.x+facing.x, pos.y+facing.y, pos.z+facing.z, 0, -4, 0);
		if(particle!=null)
		{
			//particle.setMaxAge(25);
			particle.reddustParticleScale = 3.25f;
			particle.setRBGColorF(rgb[0]*mod, rgb[1]*mod, rgb[2]*mod);
		}
	}

	@SideOnly(Side.CLIENT)
	private void spawnDustParticle()
	{
		Vec3d pos = new Vec3d(getBlockPosForPos(2)).addVector(0.5, 0, 0.5);
		Vec3d facing = new Vec3d(getFacing().getDirectionVec());
		facing = facing.scale(0.65f);

		float mod = (float)(Math.random()*2f);

		ParticleRedstone particle = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.x+facing.x, pos.y+1.125+facing.y, pos.z+facing.z, 0, -4, 0);
		ParticleRedstone particle2 = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.x+facing.x, pos.y+0.65+facing.y, pos.z+facing.z, 0, -4, 0);
		ParticleRedstone particle3 = (ParticleRedstone)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), pos.x+facing.x, pos.y+facing.y, pos.z+facing.z, 0, -4, 0);

		float[] rgb = getCurrentProcessColor();
		final float dmod = 1.3043479f;

		if(particle!=null)
		{
			particle.reddustParticleScale = 3;
			particle.setRBGColorF(rgb[0]*mod, rgb[1]*mod, rgb[2]*mod);
		}
		if(particle2!=null)
		{
			particle2.reddustParticleScale = 4;
			particle2.setRBGColorF(rgb[0]*dmod*mod, rgb[1]*dmod*mod, rgb[2]*dmod*mod);
		}
		if(particle3!=null)
		{
			particle3.reddustParticleScale = 3;
			particle3.setRBGColorF(rgb[0]*mod, rgb[1]*mod, rgb[2]*mod);
		}
	}

	private float[] getCurrentProcessColor()
	{
		if(currentProcess!=null)
			return currentProcess.recipe.getDustColor();
		return new float[]{1, 1, 1};
	}

	public float getCurrentEfficiency()
	{
		float e1, e2;
		e1 = MathHelper.clamp(this.rotation.getRotationSpeed()/(float)Sawmill.rpmMin, 0, 1);
		e2 = MathHelper.clamp(this.rotation.getTorque()/(float)Sawmill.torqueMin, 0, 1);
		return (e1+e2)/2f;
	}


	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return true;
	}

	//--- IGuiTile ---//

	@Override
	public IIGuiList getGUI()
	{
		return IIGuiList.GUI_SAWMILL;
	}

	//--- TileEntityMultiblockProduction ---//

	@Override
	public SawmillRecipe loadRecipeFromNBT(NBTTagCompound nbt)
	{
		return SawmillRecipe.loadFromNBT(nbt);
	}

	@Override
	protected IIMultiblockProcess<SawmillRecipe> findNewProductionProcess()
	{
		ItemStack stackSawblade = inventory.get(SLOT_SAWBLADE);

		if(stackSawblade.isEmpty())
			return null;

		SawmillRecipe recipe = SawmillRecipe.findRecipe(inventory.get(SLOT_INPUT));
		if(recipe!=null)
		{
			Item item = stackSawblade.getItem();
			if(!(item instanceof ISawblade))
				return null;
			ISawblade saw = (ISawblade)item;

			if(saw.getHardness(stackSawblade) < recipe.getHardness())
				return null;

			inventory.get(SLOT_INPUT).shrink(recipe.itemInput.inputSize);
			return new IIMultiblockProcess<>(recipe);
		}

		return null;
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<SawmillRecipe> process, boolean simulate)
	{
		if(inventory.get(1).getItem() instanceof ISawblade&&getCurrentEfficiency() > 0.95&&
				inventory.get(2).getCount()+process.recipe.itemOutput.getCount() <= getSlotLimit(2))
			return 1;
		return 0;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<SawmillRecipe> process)
	{
		ItemStack output = process.recipe.itemOutput.copy();
		ItemStack sawdust = process.recipe.itemSecondaryOutput.copy();

		outputOrDrop(output, outputHandler, facing, getPOI("item_output"));
		outputOrDrop(sawdust, sawdustOutputHandler, EnumFacing.DOWN, getPOI("sawdust"));

		return true;
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<SawmillRecipe> process)
	{
		ItemStack sawblade = inventory.get(SLOT_SAWBLADE);
		if(sawblade.getItem() instanceof ISawblade)
			((ISawblade)sawblade.getItem()).damageSawblade(sawblade, process.recipe.getHardness());
	}

	private void selfDestruct()
	{
		world.createExplosion(null, getPos().getX(), getPos().getY(), getPos().getZ(), 4, true);
	}

	//--- IRotationalEnergyBlock ---//

	@Override
	public void updateRotationStorage(float rpm, float torque, int part)
	{
		if(world.isRemote)
		{
			rotation.setRotationSpeed(rpm);
			rotation.setTorque(torque);
		}
	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		vice.setState(state);
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		vice.setState(state);
		IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(state, part, getPos()),
				IIPacketHandler.targetPointFromTile(this, 32));
	}
}
