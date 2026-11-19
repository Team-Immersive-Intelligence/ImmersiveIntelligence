package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.SmallChemicalReactor;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockSmallChemicalReactor;
import pl.pabilo8.immersiveintelligence.common.util.IIEnergyStorage;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.fluid.FilteredFluidTank;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;

/**
 * @author Avalon (avalon@iiteam.net)
 * @since 14.11.2026
 */
public class TileEntitySmallChemicalReactor extends TileEntityMultiblockIIGeneric<TileEntitySmallChemicalReactor>
		implements IIIGuiMultiblockTile
{
	@SyncNBT(name = "tank0", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FilteredFluidTank tankInput1;
	@SyncNBT(name = "tank1", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FilteredFluidTank tankInput2;
	@SyncNBT(name = "tank2", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FilteredFluidTank tankOutput1;

	public TileEntitySmallChemicalReactor()
	{
		super(MultiblockSmallChemicalReactor.INSTANCE);
		this.inventory = NonNullList.withSize(9, ItemStack.EMPTY);
		this.tankInput1 = new FilteredFluidTank(SmallChemicalReactor.fluidCapacity);
		this.tankInput2 = new FilteredFluidTank(SmallChemicalReactor.fluidCapacity);
		this.tankOutput1 = new FilteredFluidTank(SmallChemicalReactor.fluidCapacity);
		this.energyStorage = new IIEnergyStorage(SmallChemicalReactor.energyCapacity);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		tankInput1 = tankInput2 = tankOutput1 = null;
	}

	@Override
	protected void onUpdate()
	{
		if(!world.isRemote&&world.getTotalWorldTime()%10==0)
		{
			boolean update = IIUtils.handleBucketTankInteraction(tankInput1, inventory,
					MultiblockSmallChemicalReactor.SLOT_TANK_INPUT_1_BUCKET_INPUT,
					MultiblockSmallChemicalReactor.SLOT_TANK_INPUT_1_BUCKET_OUTPUT, true);
			update |= IIUtils.handleBucketTankInteraction(tankInput2, inventory,
					MultiblockSmallChemicalReactor.SLOT_TANK_INPUT_2_BUCKET_INPUT,
					MultiblockSmallChemicalReactor.SLOT_TANK_INPUT_2_BUCKET_OUTPUT, true);
			update |= IIUtils.handleBucketTankInteraction(tankOutput1, inventory,
					MultiblockSmallChemicalReactor.SLOT_TANK_OUTPUT_1_BUCKET_INPUT,
					MultiblockSmallChemicalReactor.SLOT_TANK_OUTPUT_1_BUCKET_OUTPUT, true);
			if(update)
				updateTileForEvent(SyncEvents.TILE_CUSTOM1);
		}
	}

	@Override
	protected IFluidTank[] getFluidTanks(int pos, EnumFacing side)
	{
		return new IFluidTank[]{tankInput1, tankInput2, tankOutput1};
	}

	@Override
	public boolean canOpenGui()
	{
		return formed;
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.SMALL_CHEMICAL_REACTOR;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		switch(slot)
		{
			case MultiblockSmallChemicalReactor.SLOT_ITEM_INPUT:
			case MultiblockSmallChemicalReactor.SLOT_CATALYST:
			case MultiblockSmallChemicalReactor.SLOT_TANK_INPUT_1_BUCKET_OUTPUT:
			case MultiblockSmallChemicalReactor.SLOT_TANK_INPUT_2_BUCKET_OUTPUT:
			case MultiblockSmallChemicalReactor.SLOT_TANK_OUTPUT_1_BUCKET_OUTPUT:
				return true;
			case MultiblockSmallChemicalReactor.SLOT_TANK_INPUT_1_BUCKET_INPUT:
			case MultiblockSmallChemicalReactor.SLOT_TANK_INPUT_2_BUCKET_INPUT:
			case MultiblockSmallChemicalReactor.SLOT_TANK_OUTPUT_1_BUCKET_INPUT:
				return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			default:
				return false;
		}
	}
}
