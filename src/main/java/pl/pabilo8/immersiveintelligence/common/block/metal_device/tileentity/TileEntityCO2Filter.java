package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity;

import blusunrize.immersiveengineering.api.crafting.FermenterRecipe;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHasDummyBlocks;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityFermenter;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal.MultiblockProcess;
import blusunrize.immersiveengineering.common.blocks.stone.TileEntityBlastFurnaceAdvanced;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.CO2Collector;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon
 * @since 19.05.2021
 * @since 15.12.2024
 */
public class TileEntityCO2Filter extends TileEntityIIDirectional implements ITickable, IBlockBounds, IHasDummyBlocks
{
	public static final HashMap<Class<?>, CO2Handler> handlerMap = new HashMap<>();
	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.SIDE_CLICKED);

	static
	{
		handlerMap.put(TileEntityFermenter.class,
				new CO2Handler()
				{
					@Override
					public int getOutput(TileEntity tile)
					{
						if(!(tile instanceof TileEntityFermenter))
							return 0;
						if(((TileEntityFermenter)tile).pos!=0)
							return 0;

						TileEntityFermenter fermenter = ((TileEntityFermenter)tile).master();
						if(fermenter==null)
							return 0;
						int i = 0;
						for(MultiblockProcess<FermenterRecipe> process : fermenter.processQueue)
							if(process.canProcess(fermenter)&&process.processTick%CO2Collector.fermenterCollectTime==0)
								i += CO2Collector.fermenterCollectAmount;
						return i;
					}
				}
		);

		handlerMap.put(TileEntityBlastFurnaceAdvanced.class,
				new CO2Handler()
				{
					@Override
					public int getOutput(TileEntity tile)
					{
						if(!(tile instanceof TileEntityBlastFurnaceAdvanced))
							return 0;
						if(((TileEntityBlastFurnaceAdvanced)tile).pos!=31)
							return 0;

						TileEntityBlastFurnaceAdvanced furnace = (TileEntityBlastFurnaceAdvanced)(((TileEntityBlastFurnaceAdvanced)tile).master());

						if(furnace==null)
							return 0;
						return (furnace.burnTime%CO2Collector.blastFurnaceCollectTime==0&&furnace.active)?CO2Collector.blastFurnaceCollectAmount: 0;
					}
				}
		);
	}

	@SyncNBT(name = "dummy")
	public int subBlockID = 0;
	public FluidTank tank = new FluidTank(1000);
	FluidWrapper fluidWrapper = new FluidWrapper(this);
	IItemHandler insertionHandler = new CO2ItemHandler(this);

	@Override
	public void update()
	{
		if(!isDummy())
		{
			CO2Handler handlerBelow = getHandlerBelow();
			if(handlerBelow!=null)
			{
				int output = handlerBelow.getOutput(world.getTileEntity(pos.offset(facing.getOpposite())));
				if(output > 0)
				{
					EnumFacing ff = (facing==EnumFacing.UP)?EnumFacing.NORTH: facing;
					TileEntity tile = world.getTileEntity(pos.up().offset(ff));
					if(tile!=null&&tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, ff.getOpposite()))
					{
						IFluidHandler capability = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, ff.getOpposite());
						if(capability!=null)
							capability.fill(new FluidStack(IIContent.gasCO2, output), true);
					}
				}
			}

		}
	}

	@Override
	public void placeDummies(BlockPos pos, IBlockState state, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		for(int i = 1; i <= 2; i++)
		{
			world.setBlockState(pos.add(0, i, 0), state);
			((TileEntityCO2Filter)world.getTileEntity(pos.add(0, i, 0))).subBlockID = i;
			((TileEntityCO2Filter)world.getTileEntity(pos.add(0, i, 0))).facing = this.facing;
		}
	}

	@Override
	public void breakDummies(BlockPos pos, IBlockState state)
	{
		for(int i = 0; i <= 2; i++)
			if(world.getTileEntity(getPos().add(0, -subBlockID, 0).add(0, i, 0)) instanceof TileEntityCO2Filter)
				world.setBlockToAir(getPos().add(0, -subBlockID, 0).add(0, i, 0));
	}

	@Override
	public boolean isDummy()
	{
		return subBlockID > 0;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(subBlockID==1&&facing==(this.getFacing()==EnumFacing.UP?EnumFacing.NORTH: this.facing)&&capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return true;
		if(!isDummy()&&(facing==null||facing.getAxis()!=Axis.Y)&&capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(subBlockID==1&&facing==(this.getFacing()==EnumFacing.UP?EnumFacing.NORTH: this.facing)&&capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return ((T)fluidWrapper);
		if(!isDummy()&&(facing==null||facing.getAxis()!=Axis.Y)&&capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T)insertionHandler;
		return super.getCapability(capability, facing);
	}

	@Nullable
	private CO2Handler getHandlerBelow()
	{
		TileEntity te = getWorld().getTileEntity(pos.offset(facing.getOpposite()));
		return (te!=null)?handlerMap.get(te.getClass()): null;
	}

	public static class FluidWrapper implements IFluidHandler
	{
		final TileEntityCO2Filter tile;

		public FluidWrapper(TileEntityCO2Filter tile)
		{
			this.tile = tile;
		}

		@Override
		public IFluidTankProperties[] getTankProperties()
		{
			return tile.tank.getTankProperties();
		}

		@Override
		public int fill(FluidStack resource, boolean doFill)
		{
			return 0;
		}

		@Nullable
		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain)
		{
			return null;
		}

		@Nullable
		@Override
		public FluidStack drain(int maxDrain, boolean doDrain)
		{
			return null;
		}
	}

	public static class CO2ItemHandler implements IItemHandlerModifiable
	{
		TileEntityCO2Filter tile;

		public CO2ItemHandler(TileEntityCO2Filter tile)
		{
			this.tile = tile;
		}

		@Override
		public int getSlots()
		{
			IItemHandler handlerBelow = getHandlerBelow();
			return handlerBelow!=null?handlerBelow.getSlots(): 1;
		}

		@Override
		public ItemStack getStackInSlot(int slot)
		{
			IItemHandler handlerBelow = getHandlerBelow();
			return handlerBelow!=null?handlerBelow.getStackInSlot(slot): ItemStack.EMPTY;
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
		{
			IItemHandler handlerBelow = getHandlerBelow();
			if(handlerBelow!=null)
				return handlerBelow.insertItem(slot, stack, simulate);
			return stack;  // Return full stack if no valid handler
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate)
		{
			IItemHandler handlerBelow = getHandlerBelow();
			return handlerBelow!=null?handlerBelow.extractItem(slot, amount, simulate): ItemStack.EMPTY;
		}

		@Override
		public int getSlotLimit(int slot)
		{
			IItemHandler handlerBelow = getHandlerBelow();
			return handlerBelow!=null?handlerBelow.getSlotLimit(slot): 64;
		}

		@Override
		public void setStackInSlot(int slot, ItemStack stack)
		{
			IItemHandlerModifiable handlerBelow = (IItemHandlerModifiable)getHandlerBelow();
			if(handlerBelow!=null)
				handlerBelow.setStackInSlot(slot, stack);
		}

		@Nullable
		private IItemHandler getHandlerBelow()
		{
			TileEntity te = tile.getWorld().getTileEntity(tile.pos.down());
			if(te!=null&&te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP))
				return te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
			return null;
		}
	}

	//--- IBlockBounds ---//

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0f, 0, 0f, 1f, 1f, 1f};
	}

	//--- Facing ---//

	@Nonnull
	@Override
	protected FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS;
	}

	public EnumFacing getFacingForPlacement(EntityLivingBase placer, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		//All but not down
		return (side==EnumFacing.DOWN)?EnumFacing.UP: side;
	}

	public static abstract class CO2Handler
	{
		public abstract int getOutput(TileEntity tile);
	}
}
