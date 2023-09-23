package pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.fluid.IFluidPipe;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IConfigurableSides;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHasDummyBlocks;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityFluidPipe;
import blusunrize.immersiveengineering.common.util.ChatUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.MechanicalPump;
import pl.pabilo8.immersiveintelligence.api.rotary.CapabilityRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotationalEnergyBlock;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIMechanicalDevice1.IIBlockTypes_MechanicalDevice1;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageRotaryPowerSync;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Pabilo8
 * @since 03.10.2020
 */
public class TileEntityMechanicalPump extends TileEntityIEBase implements ITickable, IBlockBounds, IHasDummyBlocks, IConfigurableSides,
		IFluidPipe, IAdvancedTextOverlay, IRotationalEnergyBlock, IDirectionalTile
{
	public int[] sideConfig = new int[]{0, -1, -1, -1, -1, -1};
	public boolean dummy = false;
	public FluidTank tank = new FluidTank(1000);
	public boolean placeCobble = true;
	public EnumFacing facing = EnumFacing.NORTH;

	boolean checkingArea = false;
	Fluid searchFluid = null;
	ArrayList<BlockPos> openList = new ArrayList<>();
	ArrayList<BlockPos> closedList = new ArrayList<>();
	ArrayList<BlockPos> checked = new ArrayList<>();

	public RotaryStorage rotation = new RotaryStorage(0, 0)
	{
		@Override
		public RotationSide getSide(@Nullable EnumFacing facing)
		{
			return facing==getFacing()?RotationSide.INPUT: RotationSide.NONE;
		}
	};

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		ApiUtils.checkForNeedlessTicking(this);
		if(dummy||world.isRemote)
			return;
		if(tank.getFluidAmount() > 0)
		{
			int i = outputFluid(tank.getFluid(), false);
			tank.drain(i, true);
		}

		handleRotation();

		if(world.isBlockIndirectlyGettingPowered(getPos()) > 0||world.isBlockIndirectlyGettingPowered(getPos().add(0, 1, 0)) > 0)
		{
			for(EnumFacing f : EnumFacing.values())
				if(sideConfig[f.ordinal()]==0)
				{
					BlockPos output = getPos().offset(f);
					TileEntity tile = Utils.getExistingTileEntity(world, output);
					if(tile!=null&&tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, f.getOpposite()))
					{
						IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, f.getOpposite());
						FluidStack drain = handler.drain(500, false);
						if(drain==null||drain.amount <= 0)
							continue;
						int out = this.outputFluid(drain, false);
						handler.drain(out, true);
					}
					else if(world.getTotalWorldTime()%20==((getPos().getX()^getPos().getZ())&19)&&world.getBlockState(getPos().offset(f)).getBlock()==Blocks.WATER&&IEConfig.Machines.pump_infiniteWater&&tank.fill(new FluidStack(FluidRegistry.WATER, 1000), false)==1000&&this.hasEnoughPower())
					{
						int connectedSources = 0;
						for(EnumFacing f2 : EnumFacing.HORIZONTALS)
						{
							IBlockState waterState = world.getBlockState(getPos().offset(f).offset(f2));
							if(waterState.getBlock()==Blocks.WATER&&Blocks.WATER.getMetaFromState(waterState)==0)
								connectedSources++;
						}
						if(connectedSources > 1)
						{
							this.tank.fill(new FluidStack(FluidRegistry.WATER, 1000), true);
						}
					}
				}
			if(world.getTotalWorldTime()%40==(((getPos().getX()^getPos().getZ()))%40+40)%40)
			{
				if(closedList.isEmpty())
					prepareAreaCheck();
				else
				{
					int target = closedList.size()-1;
					BlockPos pos = closedList.get(target);
					FluidStack fs = Utils.drainFluidBlock(world, pos, false);
					if(fs==null)
						closedList.remove(target);
					else if(tank.fill(fs, false)==fs.amount&&hasEnoughPower())
					{
						fs = Utils.drainFluidBlock(world, pos, true);
						if(IEConfig.Machines.pump_placeCobble&&placeCobble)
							world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
						this.tank.fill(fs, true);
						closedList.remove(target);
					}
				}
			}
		}

		if(checkingArea)
			checkAreaTick();
	}

	private boolean handleRotation()
	{
		boolean b = false;
		if(rotation.getRotationSpeed() > MechanicalPump.rpmBreakingMax||rotation.getTorque() > MechanicalPump.torqueBreakingMax)
		{
			selfDestruct();
			return false;
		}

		TileEntity te = world.getTileEntity(getPos().up().offset(facing.getOpposite()));
		if(te!=null&&te.hasCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, facing))
		{
			IRotaryEnergy cap = te.getCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, facing);
			if(rotation.handleRotation(cap, facing))
			{
				IIPacketHandler.INSTANCE.sendToAllAround(new MessageRotaryPowerSync(rotation, 0, getPos()), IIPacketHandler.targetPointFromTile(this, 24));
			}
		}
		else
			b = true;

		if(b)
			if((rotation.getTorque() > 0||rotation.getRotationSpeed() > 0))
			{
				{
					rotation.grow(0, 0, 0.98f);
				}
				IIPacketHandler.INSTANCE.sendToAllAround(new MessageRotaryPowerSync(rotation, 0, getPos()), IIPacketHandler.targetPointFromTile(this, 24));
			}
		return b;
	}

	public void prepareAreaCheck()
	{
		openList.clear();
		closedList.clear();
		checked.clear();
		for(EnumFacing f : EnumFacing.values())
			if(sideConfig[f.ordinal()]==0)
			{
				openList.add(getPos().offset(f));
				checkingArea = true;
			}
	}

	public void checkAreaTick()
	{
		BlockPos next;
		final int closedListMax = 2048;
		int timeout = 0;
		while(timeout < 64&&closedList.size() < closedListMax&&!openList.isEmpty())
		{
			timeout++;
			next = openList.get(0);
			if(!checked.contains(next))
			{
				Fluid fluid = Utils.getRelatedFluid(world, next);
				if(fluid!=null&&(fluid!=FluidRegistry.WATER||!IEConfig.Machines.pump_infiniteWater)&&(searchFluid==null||fluid==searchFluid))
				{
					if(searchFluid==null)
						searchFluid = fluid;

					if(Utils.drainFluidBlock(world, next, false)!=null)
						closedList.add(next);
					for(EnumFacing f : EnumFacing.values())
					{
						BlockPos pos2 = next.offset(f);
						fluid = Utils.getRelatedFluid(world, pos2);
						if(!checked.contains(pos2)&&!closedList.contains(pos2)&&!openList.contains(pos2)&&fluid!=null&&(fluid!=FluidRegistry.WATER||!IEConfig.Machines.pump_infiniteWater)&&(searchFluid==null||fluid==searchFluid))
							openList.add(pos2);
					}
				}
				checked.add(next);
			}
			openList.remove(0);
		}
		if(closedList.size() >= closedListMax||openList.isEmpty())
			checkingArea = false;
	}

	public int outputFluid(FluidStack fs, boolean simulate)
	{
		if(fs==null)
			return 0;

		int canAccept = fs.amount;
		if(canAccept <= 0)
			return 0;

		final int fluidForSort = canAccept;
		int sum = 0;
		HashMap<DirectionalFluidOutput, Integer> sorting = new HashMap<>();
		for(EnumFacing f : EnumFacing.values())
			if(sideConfig[f.ordinal()]==1)
			{
				TileEntity tile = Utils.getExistingTileEntity(world, getPos().offset(f));
				if(tile!=null&&tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, f.getOpposite()))
				{
					IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, f.getOpposite());
					FluidStack insertResource = Utils.copyFluidStackWithAmount(fs, fs.amount, true);
					if(tile instanceof TileEntityFluidPipe&&hasEnoughPower())
					{
						insertResource.tag = new NBTTagCompound();
						insertResource.tag.setBoolean("pressurized", true);
					}
					int temp = handler.fill(insertResource, false);
					if(temp > 0)
					{
						sorting.put(new DirectionalFluidOutput(handler, tile, f), temp);
						sum += temp;
					}
				}
			}
		if(sum > 0)
		{
			int f = 0;
			int i = 0;
			for(DirectionalFluidOutput output : sorting.keySet())
			{
				float prio = sorting.get(output)/(float)sum;
				int amount = (int)(fluidForSort*prio);
				if(i++==sorting.size()-1)
					amount = canAccept;
				FluidStack insertResource = Utils.copyFluidStackWithAmount(fs, amount, true);
				if(output.containingTile instanceof TileEntityFluidPipe&&hasEnoughPower())
				{
					insertResource.tag = new NBTTagCompound();
					insertResource.tag.setBoolean("pressurized", true);
				}
				int r = output.output.fill(insertResource, !simulate);
				f += r;
				canAccept -= r;
				if(canAccept <= 0)
					break;
			}
			return f;
		}
		return 0;
	}


	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		sideConfig = nbt.getIntArray("sideConfig");
		if(sideConfig==null||sideConfig.length!=6)
			sideConfig = new int[]{0, -1, -1, -1, -1, -1};
		dummy = nbt.getBoolean("dummy");
		if(nbt.hasKey("placeCobble"))
			placeCobble = nbt.getBoolean("placeCobble");
		tank.readFromNBT(nbt.getCompoundTag("tank"));
		facing = EnumFacing.getFront(nbt.getInteger("facing"));

		if(descPacket)
			this.markContainingBlockForUpdate(null);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		nbt.setIntArray("sideConfig", sideConfig);
		nbt.setBoolean("dummy", dummy);
		nbt.setBoolean("placeCobble", placeCobble);
		nbt.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
		nbt.setInteger("facing", facing.ordinal());
	}

	@Override
	public SideConfig getSideConfig(int side)
	{
		return (side >= 0&&side < 6)?SideConfig.values()[this.sideConfig[side]+1]: SideConfig.NONE;
	}

	@Override
	public boolean toggleSide(int side, EntityPlayer p)
	{
		if(side!=1&&!dummy)
		{
			sideConfig[side]++;
			if(sideConfig[side] > 1)
				sideConfig[side] = -1;
			this.markDirty();
			this.markContainingBlockForUpdate(null);
			world.addBlockEvent(getPos(), this.getBlockType(), 0, 0);
			return true;
		}
		else if(p.isSneaking())
		{
			TileEntityMechanicalPump master = this;
			if(dummy)
			{
				TileEntity tmp = world.getTileEntity(pos.down());
				if(tmp instanceof TileEntityMechanicalPump)
					master = (TileEntityMechanicalPump)tmp;
			}
			master.placeCobble = !master.placeCobble;
			ChatUtils.sendServerNoSpamMessages(p, new TextComponentTranslation(Lib.CHAT_INFO+"pump.placeCobble."+master.placeCobble));
			return true;
		}
		return false;
	}

	SidedFluidHandler[] sidedFluidHandler = new SidedFluidHandler[6];

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY&&facing!=null&&!dummy)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY&&facing!=null&&!dummy)
		{
			if(sidedFluidHandler[facing.ordinal()]==null)
				sidedFluidHandler[facing.ordinal()] = new SidedFluidHandler(this, facing);
			return (T)sidedFluidHandler[facing.ordinal()];
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		if(Utils.isHammer(player.getHeldItem(EnumHand.MAIN_HAND))&&IEConfig.colourblindSupport&&!dummy)
		{
			int i = sideConfig[Math.min(sideConfig.length-1, mop.sideHit.ordinal())];
			int j = sideConfig[Math.min(sideConfig.length-1, mop.sideHit.getOpposite().ordinal())];
			return new String[]{
					I18n.format(Lib.DESC_INFO+"blockSide.facing")
							+": "+I18n.format(Lib.DESC_INFO+"blockSide.connectFluid."+i),
					I18n.format(Lib.DESC_INFO+"blockSide.opposite")
							+": "+I18n.format(Lib.DESC_INFO+"blockSide.connectFluid."+j)
			};
		}
		return null;
	}


	@Override
	public void updateRotationStorage(float rpm, float torque, int part)
	{
		if(world.isRemote&&!isDummy())
		{
			if(part==0)
			{
				rotation.setRotationSpeed(rpm);
				rotation.setTorque(torque);
			}
		}
	}

	@Override
	public EnumFacing getFacing()
	{
		return facing;
	}

	@Override
	public void setFacing(EnumFacing facing)
	{
		if(facing.getAxis().isHorizontal())
			this.facing = facing;
		else
			this.facing = EnumFacing.NORTH;

		if(isDummy())
		{
			TileEntity tileEntity = world.getTileEntity(pos.down());
			if(tileEntity instanceof TileEntityMechanicalPump)
				((TileEntityMechanicalPump)tileEntity).setFacing(facing);
		}
	}

	@Override
	public int getFacingLimitation()
	{
		return 2;
	}

	@Override
	public boolean mirrorFacingOnPlacement(EntityLivingBase placer)
	{
		return false;
	}

	@Override
	public boolean canHammerRotate(EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase entity)
	{
		return isDummy();
	}

	@Override
	public boolean canRotate(EnumFacing axis)
	{
		return isDummy();
	}

	static class SidedFluidHandler implements IFluidHandler
	{
		TileEntityMechanicalPump pump;
		EnumFacing facing;

		SidedFluidHandler(TileEntityMechanicalPump pump, EnumFacing facing)
		{
			this.pump = pump;
			this.facing = facing;
		}

		@Override
		public int fill(FluidStack resource, boolean doFill)
		{
			if(resource==null||pump.sideConfig[facing.ordinal()]!=0)
				return 0;
			return pump.tank.fill(resource, doFill);
		}

		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain)
		{
			if(resource==null)
				return null;
			return this.drain(resource.amount, doDrain);
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain)
		{
			if(pump.sideConfig[facing.ordinal()]!=1)
				return null;
			return pump.tank.drain(maxDrain, doDrain);
		}

		@Override
		public IFluidTankProperties[] getTankProperties()
		{
			return pump.tank.getTankProperties();
		}
	}

	@Override
	public boolean isDummy()
	{
		return dummy;
	}

	@Override
	public void placeDummies(BlockPos pos, IBlockState state, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		world.setBlockState(pos.add(0, 1, 0), state);
		((TileEntityMechanicalPump)world.getTileEntity(pos.add(0, 1, 0))).dummy = true;
	}

	@Override
	public void breakDummies(BlockPos pos, IBlockState state)
	{
		for(int i = 0; i <= 1; i++)
			if(Utils.isBlockAt(world, getPos().add(0, dummy?-1: 0, 0).add(0, i, 0), IIContent.blockMechanicalDevice1, IIBlockTypes_MechanicalDevice1.MECHANICAL_PUMP.getMeta()))
				world.setBlockToAir(getPos().add(0, dummy?-1: 0, 0).add(0, i, 0));
	}

	@Override
	public float[] getBlockBounds()
	{
		if(!dummy)
			return null;
		return new float[]{.1875f, 0, .1875f, .8125f, 1, .8125f};
	}

	@Override
	public boolean canOutputPressurized(boolean consumePower)
	{
		int accelPower = IEConfig.Machines.pump_consumption_accelerate;
		return hasEnoughPower();
	}

	private boolean hasEnoughPower()
	{
		return this.rotation.getRotationSpeed() >= MechanicalPump.rpmMin&&this.rotation.getTorque() >= MechanicalPump.torqueMin;
	}

	@Override
	public boolean hasOutputConnection(EnumFacing side)
	{
		return side!=null&&this.sideConfig[side.ordinal()]==1;
	}

	public static class DirectionalFluidOutput
	{
		IFluidHandler output;
		EnumFacing direction;
		TileEntity containingTile;

		public DirectionalFluidOutput(IFluidHandler output, TileEntity containingTile, EnumFacing direction)
		{
			this.output = output;
			this.direction = direction;
			this.containingTile = containingTile;
		}
	}

	private void selfDestruct()
	{
		world.createExplosion(null, getPos().getX(), getPos().getY(), getPos().getZ(), 1, true);
		world.setBlockToAir(this.pos);
	}
}