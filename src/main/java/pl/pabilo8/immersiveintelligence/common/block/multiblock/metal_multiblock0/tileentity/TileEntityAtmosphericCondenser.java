/**
 * @author Avalon (avalon@iiteam.net)
 * @since 04.11.2026
 */
package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.AtmosphericCondenser;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockAtmosphericCondenser;
import pl.pabilo8.immersiveintelligence.common.util.IIEnergyStorage;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.fluid.FilteredFluidTank;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import static pl.pabilo8.immersiveintelligence.common.IIUtils.handleBucketTankInteraction;
import static pl.pabilo8.immersiveintelligence.common.IIUtils.outputFluidToTank;

public class TileEntityAtmosphericCondenser extends TileEntityMultiblockIIGeneric<TileEntityAtmosphericCondenser>
		implements IIIGuiMultiblockTile
{
	@SyncNBT(name = "argon_tank", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FilteredFluidTank argonTank;
	@SyncNBT(name = "nitrogen_tank", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FilteredFluidTank nitrogenTank;
	@SyncNBT(name = "oxygen_tank", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FilteredFluidTank oxygenTank;
	@SyncNBT(name = "production_progress", time = 5, events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED})
	public int productionProgress;

	public TileEntityAtmosphericCondenser()
	{
		super(MultiblockAtmosphericCondenser.INSTANCE);
		inventory = NonNullList.withSize(6, ItemStack.EMPTY);
		energyStorage = new IIEnergyStorage(AtmosphericCondenser.energyCapacity);
		argonTank = new FilteredFluidTank(AtmosphericCondenser.fluidCapacity);
		nitrogenTank = new FilteredFluidTank(AtmosphericCondenser.fluidCapacity);
		oxygenTank = new FilteredFluidTank(AtmosphericCondenser.fluidCapacity);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		argonTank = nitrogenTank = oxygenTank = null;
	}

	@Override
	protected void onUpdate()
	{
		if(world.isRemote)
			return;

		if(world.getTotalWorldTime()%10==0)
		{
			boolean changed = handleBucketTankInteraction(argonTank, inventory,
					MultiblockAtmosphericCondenser.BUCKET_ARGON_IN, MultiblockAtmosphericCondenser.BUCKET_ARGON_OUT, true);
			changed |= handleBucketTankInteraction(nitrogenTank, inventory,
					MultiblockAtmosphericCondenser.BUCKET_NITROGEN_IN, MultiblockAtmosphericCondenser.BUCKET_NITROGEN_OUT, true);
			changed |= handleBucketTankInteraction(oxygenTank, inventory,
					MultiblockAtmosphericCondenser.BUCKET_OXYGEN_IN, MultiblockAtmosphericCondenser.BUCKET_OXYGEN_OUT, true);
			if(outputFluidToTank(argonTank, 100, getPOIPos("output_argon"), this.world, getDirection("output_argon")))
				changed = true;
			if(outputFluidToTank(nitrogenTank, 100, getPOIPos("output_nitrogen"), this.world, getDirection("output_nitrogen")))
				changed = true;
			if(outputFluidToTank(oxygenTank, 100, getPOIPos("output_oxygen"), this.world, getDirection("output_oxygen")))
				changed = true;
			if(changed)
			{
				markDirty();
				updateTileForEvent(SyncEvents.TILE_CUSTOM1);
			}
		}

		if(getRedstoneAtPos(0))
			return;

		if(argonTank.getFluidAmount()+AtmosphericCondenser.argonPerCycle > AtmosphericCondenser.fluidCapacity
				||nitrogenTank.getFluidAmount()+AtmosphericCondenser.nitrogenPerCycle > AtmosphericCondenser.fluidCapacity
				||oxygenTank.getFluidAmount()+AtmosphericCondenser.oxygenPerCycle > AtmosphericCondenser.fluidCapacity)
			return;

		if(energyStorage.tryConsumeEnergy(AtmosphericCondenser.energyPerTick, false))
		{
			if(++productionProgress >= AtmosphericCondenser.ticksPerCycle)
			{
				productionProgress = 0;
				argonTank.fill(new FluidStack(IIContent.gasArgon, AtmosphericCondenser.argonPerCycle), true);
				nitrogenTank.fill(new FluidStack(IIContent.gasNitrogen, AtmosphericCondenser.nitrogenPerCycle), true);
				oxygenTank.fill(new FluidStack(IIContent.gasOxygen, AtmosphericCondenser.oxygenPerCycle), true);
				markDirty();
				updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
			}
		}
	}

	@Override
	protected IFluidTank[] getFluidTanks(int pos, EnumFacing side)
	{
		return new FilteredFluidTank[]{argonTank, nitrogenTank, oxygenTank};
	}

	@Override
	protected boolean isTankAvailable(int pos, int tank)
	{
		switch(tank)
		{
			case 0:
				return multiblock.isPointOfInterest(pos, "output_argon");
			case 1:
				return multiblock.isPointOfInterest(pos, "output_nitrogen");
			case 2:
				return multiblock.isPointOfInterest(pos, "output_oxygen");
		}
		return false;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		switch(slot)
		{
			case MultiblockAtmosphericCondenser.BUCKET_ARGON_IN:
			case MultiblockAtmosphericCondenser.BUCKET_NITROGEN_IN:
			case MultiblockAtmosphericCondenser.BUCKET_OXYGEN_IN:
				if(!stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
					return false;
				IFluidHandlerItem fluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
				return fluidHandler!=null&&fluidHandler.getTankProperties().length > 0
						&&fluidHandler.getTankProperties()[0].getContents()==null;
			case MultiblockAtmosphericCondenser.BUCKET_ARGON_OUT:
			case MultiblockAtmosphericCondenser.BUCKET_NITROGEN_OUT:
			case MultiblockAtmosphericCondenser.BUCKET_OXYGEN_OUT:
				return true;
			default:
				return false;
		}
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.ATMOSPHERIC_CONDENSER;
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public void onGuiOpened(EntityPlayer player, boolean clientside)
	{
		if(!clientside)
			updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
	}
}
