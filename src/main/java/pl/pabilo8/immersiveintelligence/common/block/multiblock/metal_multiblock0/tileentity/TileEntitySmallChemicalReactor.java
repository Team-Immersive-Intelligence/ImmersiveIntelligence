package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.crafting.SmallChemicalReactorRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.SmallChemicalReactor;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockSmallChemicalReactor;
import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialCatalyst;
import pl.pabilo8.immersiveintelligence.common.util.IIEnergyStorage;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.fluid.FilteredFluidTank;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;

import javax.annotation.Nullable;
import java.util.Comparator;

/**
 * @author Avalon (avalon@iiteam.net)
 * @since 14.11.2026
 */
public class TileEntitySmallChemicalReactor extends TileEntityMultiblockProductionSingle<TileEntitySmallChemicalReactor, SmallChemicalReactorRecipe>
{
	@SyncNBT(name = "tank0", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FilteredFluidTank tankInput1;
	@SyncNBT(name = "tank1", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FilteredFluidTank tankInput2;
	@SyncNBT(name = "tank2", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FilteredFluidTank tankOutput1;
	private IItemHandler catalystInputHandler = getSingleInventoryHandler(MultiblockSmallChemicalReactor.SLOT_CATALYST, true, false);
	private IItemHandler itemInputHandler = getSingleInventoryHandler(MultiblockSmallChemicalReactor.SLOT_ITEM_INPUT, true, false);
	private IItemHandler itemOutputHandler = getSingleInventoryHandler(MultiblockSmallChemicalReactor.SLOT_ITEM_OUTPUT, false, true);

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
		catalystInputHandler = itemInputHandler = itemOutputHandler = null;
	}

	@Override
	protected void onUpdate()
	{
		if(!world.isRemote&&currentProcess!=null&&!hasInputs(currentProcess.recipe))
		{
			currentProcess = null;
			markDirty();
			updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
		}
		IIMultiblockProcess<SmallChemicalReactorRecipe> previous = currentProcess;
		float previousTicks = previous==null?0: previous.ticks;
		super.onUpdate();
		if(!world.isRemote&&(previous!=currentProcess||currentProcess!=null&&currentProcess.ticks!=previousTicks))
			markDirty();
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
			for(int output : getPOI("fluid_output"))
				update |= IIUtils.outputFluidToTank(tankOutput1, 100, getBlockPosForPos(output), world, facing.getOpposite());
			int previousOutputCount = inventory.get(MultiblockSmallChemicalReactor.SLOT_ITEM_OUTPUT).getCount();
			attemptStackOutput(itemOutputHandler, getDirection("item_output"), getPOI("item_output"));
			if(inventory.get(MultiblockSmallChemicalReactor.SLOT_ITEM_OUTPUT).getCount()!=previousOutputCount)
			{
				markDirty();
				updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
			}
			if(update)
			{
				markDirty();
				updateTileForEvent(SyncEvents.TILE_CUSTOM1);
			}
		}
	}

	@Override
	protected IFluidTank[] getFluidTanks(int pos, EnumFacing side)
	{
		return new IFluidTank[]{tankInput1, tankInput2, tankOutput1};
	}

	@Override
	protected boolean isTankAvailable(int pos, int tank)
	{
		switch(tank)
		{
			case 0:
				return multiblock.isPointOfInterest(pos, "fluid_input_1");
			case 1:
				return multiblock.isPointOfInterest(pos, "fluid_input_2");
			case 2:
				return multiblock.isPointOfInterest(pos, "fluid_output");
			default:
				return false;
		}
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&(isPOI("catalyst_input")||isPOI("item_input")||isPOI("item_output")))
		{
			TileEntitySmallChemicalReactor master = master();
			return master==null?null: CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(
					isPOI("catalyst_input")?master.catalystInputHandler:
							isPOI("item_input")?master.itemInputHandler: master.itemOutputHandler);
		}
		return super.getCapability(capability, side);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side)
	{
		return capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&isPOI("item_output")
				||super.hasCapability(capability, side);
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.SMALL_CHEMICAL_REACTOR;
	}

	@Override
	protected IIMultiblockProcess<SmallChemicalReactorRecipe> findNewProductionProcess()
	{
		if(getRedstoneAtPos(0))
			return null;
		return IIMultiblockRecipe.streamRecipes(SmallChemicalReactorRecipe.class)
				.filter(this::hasInputs)
				.max(Comparator.comparingInt(recipe ->
						(recipe.fluidInput2!=null?4: 0)
								+(recipe.itemInput!=null?2: 0)
								+(recipe.catalyst!=null?1: 0)))
				.map(IIMultiblockProcess::new).orElse(null);
	}

	@Override
	protected IIMultiblockProcess<SmallChemicalReactorRecipe> getProcessByName(String name)
	{
		SmallChemicalReactorRecipe recipe = IIMultiblockRecipe.getRecipe(SmallChemicalReactorRecipe.class, name);
		return recipe==null?null: new IIMultiblockProcess<>(recipe);
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<SmallChemicalReactorRecipe> process, boolean simulate)
	{
		if(getRedstoneAtPos(0)||!hasInputs(process.recipe))
			return 0;
		long tick = (long)process.ticks;
		int energy = process.recipe.getTotalProcessEnergy();
		int cost = (int)(((tick+1)*energy)/process.maxTicks-(tick*energy)/process.maxTicks);
		return energyStorage.tryConsumeEnergy(cost, simulate)?1: 0;
	}

	private boolean hasInputs(SmallChemicalReactorRecipe recipe)
	{
		ItemStack catalyst = inventory.get(MultiblockSmallChemicalReactor.SLOT_CATALYST);
		if(firstInputTank(recipe)==null)
			return false;
		if(recipe.fluidInput2!=null&&!recipe.fluidInput2.isFluidStackIdentical(tankInput2.drain(recipe.fluidInput2, false)))
			return false;
		if(recipe.itemInput!=null&&!recipe.itemInput.matchesItemStack(inventory.get(MultiblockSmallChemicalReactor.SLOT_ITEM_INPUT)))
			return false;
		if(recipe.catalyst==null)
			return true;

		return !catalyst.isEmpty()
				&&catalyst.getItem() instanceof ItemIIMaterialCatalyst
				&&recipe.catalyst.matchesItemStack(catalyst)
				&&(Tools.infiniteCatalystDurability
						||((ItemIIMaterialCatalyst)catalyst.getItem()).getToolDamage(catalyst) > 0);
	}

	@Nullable
	private FilteredFluidTank firstInputTank(SmallChemicalReactorRecipe recipe)
	{
		if(recipe.fluidInput1.isFluidStackIdentical(tankInput1.drain(recipe.fluidInput1, false)))
			return tankInput1;
		if(recipe.fluidInput2==null&&recipe.fluidInput1.isFluidStackIdentical(tankInput2.drain(recipe.fluidInput1, false)))
			return tankInput2;
		return null;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<SmallChemicalReactorRecipe> process)
	{
		SmallChemicalReactorRecipe recipe = process.recipe;
		if(getRedstoneAtPos(0)||!hasInputs(recipe))
			return false;
		if(recipe.fluidOutput!=null&&tankOutput1.fill(recipe.fluidOutput, false)!=recipe.fluidOutput.amount)
			return false;

		ItemStack existing = inventory.get(MultiblockSmallChemicalReactor.SLOT_ITEM_OUTPUT);
		ItemStack output = recipe.itemOutput;
		int limit = getSlotLimit(MultiblockSmallChemicalReactor.SLOT_ITEM_OUTPUT);
		if(output.isEmpty())
			return true;
		if(existing.isEmpty())
			return output.getCount() <= Math.min(output.getMaxStackSize(), limit);

		return ItemStack.areItemsEqual(existing, output)
				&&ItemStack.areItemStackTagsEqual(existing, output)
				&&existing.getCount()+output.getCount() <= Math.min(existing.getMaxStackSize(), limit);
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<SmallChemicalReactorRecipe> process)
	{
		SmallChemicalReactorRecipe recipe = process.recipe;
		firstInputTank(recipe).drain(recipe.fluidInput1, true);
		if(recipe.fluidInput2!=null)
			tankInput2.drain(recipe.fluidInput2, true);
		if(recipe.itemInput!=null)
			inventory.get(MultiblockSmallChemicalReactor.SLOT_ITEM_INPUT).shrink(recipe.itemInput.inputSize);
		if(recipe.catalyst!=null)
		{
			ItemStack catalyst = inventory.get(MultiblockSmallChemicalReactor.SLOT_CATALYST);
			ItemIIMaterialCatalyst catalystItem = (ItemIIMaterialCatalyst)catalyst.getItem();
			catalystItem.damageTool(catalyst, 1);
			if(!Tools.infiniteCatalystDurability&&catalystItem.getToolDamage(catalyst) <= 0)
				inventory.set(MultiblockSmallChemicalReactor.SLOT_CATALYST, ItemStack.EMPTY);
		}
		if(recipe.fluidOutput!=null)
			tankOutput1.fill(recipe.fluidOutput, true);
		if(!recipe.itemOutput.isEmpty())
		{
			ItemStack existing = inventory.get(MultiblockSmallChemicalReactor.SLOT_ITEM_OUTPUT);
			if(existing.isEmpty())
				inventory.set(MultiblockSmallChemicalReactor.SLOT_ITEM_OUTPUT, recipe.itemOutput.copy());
			else
				existing.grow(recipe.itemOutput.getCount());
		}
		markDirty();
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		switch(slot)
		{
			case MultiblockSmallChemicalReactor.SLOT_ITEM_INPUT:
			case MultiblockSmallChemicalReactor.SLOT_TANK_INPUT_1_BUCKET_OUTPUT:
			case MultiblockSmallChemicalReactor.SLOT_TANK_INPUT_2_BUCKET_OUTPUT:
			case MultiblockSmallChemicalReactor.SLOT_TANK_OUTPUT_1_BUCKET_OUTPUT:
				return true;
			case MultiblockSmallChemicalReactor.SLOT_CATALYST:
				return stack.getItem() instanceof ItemIIMaterialCatalyst;
			case MultiblockSmallChemicalReactor.SLOT_TANK_INPUT_1_BUCKET_INPUT:
			case MultiblockSmallChemicalReactor.SLOT_TANK_INPUT_2_BUCKET_INPUT:
			case MultiblockSmallChemicalReactor.SLOT_TANK_OUTPUT_1_BUCKET_INPUT:
				return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			default:
				return false;
		}
	}
}
