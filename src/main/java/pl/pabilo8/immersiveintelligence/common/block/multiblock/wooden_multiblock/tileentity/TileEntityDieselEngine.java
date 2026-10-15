package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity;

import blusunrize.immersiveengineering.api.energy.DieselHandler;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.rotary.CapabilityRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.client.util.carversound.ConditionCompoundSound;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.DieselEngine;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockDieselEngine;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.WorkAnimationTimer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Vector2f;

/**
 * Converts IE {@link DieselHandler} fuels into II rotary power.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.2
 * @updated 15.10.2026
 * @since 14.10.2026
 */
public class TileEntityDieselEngine extends TileEntityMultiblockIIBase<TileEntityDieselEngine> implements IPlayerInteraction, IIIGuiMultiblockTile
{
	private static final IFluidTank[] NO_TANKS = new IFluidTank[0];

	@SyncNBT
	public FluidTank fuelTank = new FluidTank(DieselEngine.fuelCapacity);
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public MultiblockInteractablePart maintenanceHatch = new MultiblockInteractablePart(0, 20, 1);
	/**
	 * Controls the engine startup ramp and work animation.
	 */
	@SyncNBT(name = "active", events = SyncEvents.TILE_ENERGY_CHANGED)
	public WorkAnimationTimer workAnimator = new WorkAnimationTimer(6, 15);
	@SyncNBT(events = SyncEvents.TILE_ENERGY_CHANGED)
	public double fuelRemainder;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_CLIENT_MESSAGE})
	public float torqueSpeedRatio = 1;
	@SideOnly(Side.CLIENT)
	private ConditionCompoundSound<TileEntityDieselEngine> engineNoise;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_ENERGY_CHANGED})
	public RotaryStorage rotation = new RotaryStorage()
	{
		@Override
		public float getTorque()
		{
			return DieselEngine.producedTorque*workAnimator.getProgress(0);
		}

		@Override
		public float getRotationSpeed()
		{
			return DieselEngine.producedSpeed*workAnimator.getProgress(0);
		}

		@Override
		public float getOutputTorque()
		{
			return getTorque()*getTorqueSpeedRatio();
		}

		@Override
		public float getOutputRotationSpeed()
		{
			return getRotationSpeed()/getTorqueSpeedRatio();
		}

		@Override
		public RotationSide getSide(@Nullable EnumFacing side)
		{
			return side==null||side.getOpposite()==getDirection("rotary_output")?RotationSide.OUTPUT: RotationSide.NONE;
		}
	};
	private IFluidTank[] tanks = {fuelTank};

	public TileEntityDieselEngine()
	{
		super(MultiblockDieselEngine.INSTANCE);
	}

	@Override
	protected void dummyCleanup()
	{
		fuelTank = null;
		tanks = NO_TANKS;
		maintenanceHatch = null;
		rotation = null;
	}

	@Override
	protected void onUpdate()
	{
		maintenanceHatch.update();
		workAnimator.update();

		if(world.isRemote)
		{
			onClientUpdate();
			return;
		}

		//Handle active state
		if(workAnimator.setState(formed&&world.isBlockPowered(getPOIPos(MultiblockPOI.REDSTONE_INPUT))&&consumeFuel()))
			updateTileForEvent(SyncEvents.TILE_ENERGY_CHANGED);
	}

	@SideOnly(Side.CLIENT)
	private void onClientUpdate()
	{
		//Stop engine noise if inactive
		if(!workAnimator.getState())
		{
			if(engineNoise!=null)
				engineNoise.forceStop();
			engineNoise = null;
		}
		//Create or update engine noise
		else if(engineNoise==null||engineNoise.isDonePlaying())
		{
			engineNoise = new ConditionCompoundSound<>(IISounds.engineLightLoop,
					new Vec3d(getPos()).addVector(0.5, 0.5, 0.5), this,
					tile -> tile.isValid()&&tile.workAnimator.getState());
			engineNoise.setVolume(1f);
			engineNoise.setMaxRange(24);
		}

		//Create exhaust particles
		if(workAnimator.getState()&&world.getTotalWorldTime()%6==0)
		{
			Vec3d chimney = multiblock.getLocalVector(this, "chimney");
			if(chimney!=null)
				ParticleRegistry.spawnParticle("smoke/exhaust/diesel_medium", chimney.add(new Vec3d(getPos())), Vec3d.ZERO, new Vector2f(0, 0));
		}
	}

	public float getTorqueSpeedRatio()
	{
		return Float.isNaN(torqueSpeedRatio)||Float.isInfinite(torqueSpeedRatio)?1:
				MathHelper.clamp(torqueSpeedRatio, DieselEngine.minRatio, DieselEngine.maxRatio);
	}

	public void setTorqueSpeedRatio(float ratio)
	{
		if(Float.isNaN(ratio)||Float.isInfinite(ratio))
			return;
		ratio = MathHelper.clamp(ratio, DieselEngine.minRatio, DieselEngine.maxRatio);
		if(torqueSpeedRatio==ratio)
			return;
		torqueSpeedRatio = ratio;
		markDirty();
		if(hasWorld()&&!world.isRemote)
			updateTileForEvent(SyncEvents.TILE_CUSTOM2);
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		if(!isDummy()&&message.hasKey("torque_speed_ratio", 99))
			setTorqueSpeedRatio(message.getFloat("torque_speed_ratio"));
	}

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!isDummy())
			torqueSpeedRatio = getTorqueSpeedRatio();
	}

	@Override
	public boolean canOpenGui()
	{
		return formed;
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.DIESEL_ENGINE;
	}

	@Override
	public void onGuiOpened(@Nonnull EntityPlayer player, boolean clientside)
	{
		updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
	}

	boolean consumeFuel()
	{
		FluidStack fuel = fuelTank.getFluid();
		int burnTime = fuel==null?0: DieselHandler.getBurnTime(fuel.getFluid());
		if(burnTime <= 0||fuel.amount <= 0)
			return false;

		//Carry fractional mB between ticks and saves.
		double required = fuelRemainder+250.0/burnTime;
		int consumed = (int)Math.floor(required);
		if(consumed > fuel.amount)
			return false;
		if(consumed > 0)
			fuelTank.drain(consumed, true);
		fuelRemainder = required-consumed;
		return true;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing side)
	{
		if(capability==CapabilityRotaryEnergy.ROTARY_ENERGY)
			return getRotaryMaster(side)!=null;
		return super.hasCapability(capability, side);
	}

	@Nullable
	private TileEntityDieselEngine getRotaryMaster(@Nullable EnumFacing side)
	{
		if(!formed||!isPOI("rotary_output")||(side!=null&&side.getOpposite()!=getDirection("rotary_output")))
			return null;
		TileEntityDieselEngine master = master();
		return master!=null&&master.formed&&!master.isInvalid()?master: null;
	}

	@Nullable
	@SuppressWarnings({"unchecked", "NullableProblems"})
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing side)
	{
		if(capability==CapabilityRotaryEnergy.ROTARY_ENERGY)
		{
			TileEntityDieselEngine master = getRotaryMaster(side);
			return master==null?null: (T)master.rotation;
		}
		return super.getCapability(capability, side);
	}

	@Nonnull
	@Override
	protected IFluidTank[] getAccessibleFluidTanks(EnumFacing side)
	{
		if(!formed||!isPOI("input")||side.getOpposite()!=getDirection("input"))
			return NO_TANKS;
		TileEntityDieselEngine master = master();
		return master!=null&&master.formed&&!master.isInvalid()?master.tanks: NO_TANKS;
	}

	@Override
	protected boolean canFillTankFrom(int iTank, EnumFacing side, FluidStack resource)
	{
		return iTank==0&&DieselHandler.getBurnTime(resource.getFluid()) > 0
				&&getAccessibleFluidTanks(side).length > 0;
	}

	@Override
	public boolean interact(@Nonnull EnumFacing side, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		TileEntityDieselEngine master = master();
		if(master==null)
			return false;
		if(isPOI("input"))
		{
			IFluidHandler handler = getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
			return handler!=null&&FluidUtil.interactWithFluidHandler(player, hand, handler);
		}
		return false;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

}
