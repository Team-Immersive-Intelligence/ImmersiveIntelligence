package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.crafting.ChemicalReactorRecipe;

import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ChemicalReactor;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockChemicalReactor;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionMulti;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import static pl.pabilo8.immersiveintelligence.common.IIUtils.handleBucketTankInteraction;
import static pl.pabilo8.immersiveintelligence.common.IIUtils.outputFluidToTank;
import static pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockChemicalPainter.*;
import static pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockChemicalPainter.BLACK;

/**
 * @author Avalon (avalon@iiteam.net)
 * @since 17.10.2026
 */
public class TileEntityChemicalReactor extends TileEntityMultiblockProductionMulti<TileEntityChemicalReactor, ChemicalReactorRecipe> implements IPlayerInteraction, IAdvancedTextOverlay
{
	//Set 1
	@SyncNBT(name = "tank_set1_in1", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FluidTank tankSet1Input1;
	@SyncNBT(name = "tank_set1_in2", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FluidTank tankSet1Input2;
	@SyncNBT(name = "tank_set1_out", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FluidTank tankSet1Output;

	//Set 2
	@SyncNBT(name = "tank_set2_in1", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FluidTank tankSet2Input1;
	@SyncNBT(name = "tank_set2_in2", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FluidTank tankSet2Input2;
	@SyncNBT(name = "tank_set2_out", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FluidTank tankSet2Output;

	//Set 3
	@SyncNBT(name = "tank_set3_in1", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FluidTank tankSet3Input1;
	@SyncNBT(name = "tank_set3_in2", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FluidTank tankSet3Input2;
	@SyncNBT(name = "tank_set3_out", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CUSTOM1})
	public FluidTank tankSet3Output;



	private IEInventoryHandler outputHandlerSet1, outputHandlerSet2, outputHandlerSet3;


	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public IIColor color = IIColor.fromPackedRGB(0xff00ff);


	public TileEntityChemicalReactor()
	{
		super(MultiblockChemicalReactor.INSTANCE);

		this.inventory = NonNullList.withSize(18, ItemStack.EMPTY);

		//Set 1 tanks
		this.tankSet1Input1 = new FluidTank(ChemicalReactor.fluidCapacity);
		this.tankSet1Input2 = new FluidTank(ChemicalReactor.fluidCapacity);
		this.tankSet1Output = new FluidTank(ChemicalReactor.fluidCapacity);

		//Set 2 tanks
		this.tankSet2Input1 = new FluidTank(ChemicalReactor.fluidCapacity);
		this.tankSet2Input2 = new FluidTank(ChemicalReactor.fluidCapacity);
		this.tankSet2Output = new FluidTank(ChemicalReactor.fluidCapacity);

		//Set 3 tanks
		this.tankSet3Input1 = new FluidTank(ChemicalReactor.fluidCapacity);
		this.tankSet3Input2 = new FluidTank(ChemicalReactor.fluidCapacity);
		this.tankSet3Output = new FluidTank(ChemicalReactor.fluidCapacity);

		this.outputHandlerSet1 = getSingleInventoryHandler(MultiblockChemicalReactor.SLOT_SET1_ITEM_OUTPUT, true, true);
		this.outputHandlerSet2 = getSingleInventoryHandler(MultiblockChemicalReactor.SLOT_SET2_ITEM_OUTPUT, true, true);
		this.outputHandlerSet3 = getSingleInventoryHandler(MultiblockChemicalReactor.SLOT_SET3_ITEM_OUTPUT, true, true);

		this.energyStorage = new FluxStorageAdvanced(ChemicalReactor.energyCapacity);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		tankSet1Input1 = tankSet1Input2 = tankSet1Output = null;
		tankSet2Input1 = tankSet2Input2 = tankSet2Output = null;
		tankSet3Input1 = tankSet3Input2 = tankSet3Output = null;
		outputHandlerSet1 = outputHandlerSet2 = outputHandlerSet3 = null;
	}

	@Override
	protected IFluidTank[] getFluidTanks(int pos, EnumFacing side)
	{
		return new FluidTank[]{
				tankSet1Input1, tankSet1Input2, tankSet1Output,
				tankSet2Input1, tankSet2Input2, tankSet2Output,
				tankSet3Input1, tankSet3Input2, tankSet3Output
		};
	}

	@Override
	protected boolean isTankAvailable(int pos, int tank)
	{
		String[] poiNames = {
			"input_fluid_1.1", "input_fluid_1.2", "output_fluid_1",
			"input_fluid_2.1", "input_fluid_2.2", "output_fluid_2",
			"input_fluid_3.1", "input_fluid_3.2", "output_fluid_3"
		};
		if(tank >= 0 && tank < poiNames.length)
			return multiblock.isPointOfInterest(pos, poiNames[tank]);
		return false;
	}

	@Override
	protected void onUpdate()
	{
		super.onUpdate();

		if(!world.isRemote&&world.getTotalWorldTime()%10==0)
		{
			//Handle fluid transfers between sets (output to input)
			transferFluidBetweenSets();

			//Handle item output for all sets
			attemptStackOutput(outputHandlerSet1, getDirection("output_item_1"), getPOI(MultiblockPOI.ITEM_OUTPUT));
			attemptStackOutput(outputHandlerSet2, getDirection("output_item_2"), getPOI(MultiblockPOI.ITEM_OUTPUT));
			attemptStackOutput(outputHandlerSet3, getDirection("output_item_3"), getPOI(MultiblockPOI.ITEM_OUTPUT));
		}
	}

	@Override
	public int getMaxProductionQueue()
	{
		return 3;
	}

	@Override
	public float getMinProductionOffset()
	{
		return 0.5f;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return true;
	}

	private void transferFluidBetweenSets()
	{
		//Transfer from Set 1 output to Set 2/3 inputs
		if(tankSet1Output.getFluidAmount() > 0)
		{
			transferFluid(tankSet1Output, tankSet2Input1);
			transferFluid(tankSet1Output, tankSet2Input2);
			transferFluid(tankSet1Output, tankSet3Input1);
			transferFluid(tankSet1Output, tankSet3Input2);
		}
		//Transfer from Set 2 output to Set 1/3 inputs
		if(tankSet2Output.getFluidAmount() > 0)
		{
			transferFluid(tankSet2Output, tankSet1Input1);
			transferFluid(tankSet2Output, tankSet1Input2);
			transferFluid(tankSet2Output, tankSet3Input1);
			transferFluid(tankSet2Output, tankSet3Input2);
		}
		//Transfer from Set 3 output to Set 1/2 inputs
		if(tankSet3Output.getFluidAmount() > 0)
		{
			transferFluid(tankSet3Output, tankSet1Input1);
			transferFluid(tankSet3Output, tankSet1Input2);
			transferFluid(tankSet3Output, tankSet2Input1);
			transferFluid(tankSet3Output, tankSet2Input2);
		}
	}

	private void transferFluid(FluidTank source, FluidTank destination)
	{
		if(source.getFluidAmount() > 0 && (destination.getFluid() == null ||
				destination.getFluid().isFluidEqual(source.getFluid())))
		{
			int amountToTransfer = Math.min(source.getFluidAmount(), destination.getCapacity() - destination.getFluidAmount());
			if(amountToTransfer > 0)
			{
				destination.fill(source.drain(amountToTransfer, true), true);
			}
		}
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.CHEMICAL_REACTOR;
	}

	@Override
	protected IIMultiblockProcess<ChemicalReactorRecipe> findNewProductionProcess()
	{
		//Check which sets are not currently processing and have available resources
		boolean set1Processing = processQueue.stream().anyMatch(p -> p.processSet == 1);
		boolean set2Processing = processQueue.stream().anyMatch(p -> p.processSet == 2);
		boolean set3Processing = processQueue.stream().anyMatch(p -> p.processSet == 3);

		//Try Set 1
		if(!set1Processing && energyStorage.getEnergyStored() > 0)
		{
			IIMultiblockProcess<ChemicalReactorRecipe> process = findProcessForSet(1, MultiblockChemicalReactor.SLOT_SET1_ITEM_INPUT, MultiblockChemicalReactor.SLOT_SET1_CATALYST_INPUT, tankSet1Input1, tankSet1Input2);
			if(process != null)
				return process;
		}

		//Try Set 2
		if(!set2Processing && energyStorage.getEnergyStored() > 0)
		{
			IIMultiblockProcess<ChemicalReactorRecipe> process = findProcessForSet(2, MultiblockChemicalReactor.SLOT_SET2_ITEM_INPUT, MultiblockChemicalReactor.SLOT_SET2_CATALYST_INPUT, tankSet2Input1, tankSet2Input2);
			if(process != null)
				return process;
		}

		//Try Set 3
		if(!set3Processing && energyStorage.getEnergyStored() > 0)
		{
			IIMultiblockProcess<ChemicalReactorRecipe> process = findProcessForSet(3, MultiblockChemicalReactor.SLOT_SET3_ITEM_INPUT, MultiblockChemicalReactor.SLOT_SET3_CATALYST_INPUT, tankSet3Input1, tankSet3Input2);
			if(process != null)
				return process;
		}

		return null;
	}

	private IIMultiblockProcess<ChemicalReactorRecipe> findProcessForSet(int setNum, int itemInputSlot, int catalystInputSlot, FluidTank input1, FluidTank input2)
	{
		return ChemicalReactorRecipe.streamRecipes(ChemicalReactorRecipe.class)
				.filter(recipe -> canProcessRecipe(recipe, itemInputSlot, catalystInputSlot, input1, input2))
				.findFirst()
				.map(recipe -> new IIMultiblockProcess<>(recipe, setNum))
				.orElse(null);
	}

	private boolean canProcessRecipe(ChemicalReactorRecipe recipe, int itemInputSlot, int catalystInputSlot, FluidTank input1, FluidTank input2)
	{
		//Check fluid inputs
		if(recipe.fluidInput != null && (input1.getFluid() == null || !recipe.fluidInput.isFluidStackIdentical(input1.drain(recipe.fluidInput, false))))
			return false;
		if(recipe.fluidInput2 != null && (input2.getFluid() == null || !recipe.fluidInput2.isFluidStackIdentical(input2.drain(recipe.fluidInput2, false))))
			return false;

		//Check item inputs
		ItemStack itemInput = inventory.get(itemInputSlot);
		ItemStack catalystInput = inventory.get(catalystInputSlot);
		if(recipe.itemInput != null && (itemInput.isEmpty() || !recipe.itemInput.matchesItemStackIgnoringSize(itemInput)))
			return false;
		if(recipe.catalystInput != null && (catalystInput.isEmpty() || !recipe.catalystInput.matchesItemStackIgnoringSize(catalystInput)))
			return false;

		return true;
	}

	@Override
	protected IIMultiblockProcess<ChemicalReactorRecipe> getProcessByName(String name)
	{
		ChemicalReactorRecipe recipe = IIMultiblockRecipe.getRecipe(ChemicalReactorRecipe.class, name);
		return recipe==null?null: new IIMultiblockProcess<>(recipe);
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<ChemicalReactorRecipe> process, boolean simulate)
	{
		if(energyStorage.extractEnergy(process.recipe.getEnergyPerTick(), true)!=process.recipe.getEnergyPerTick())
			return 0;
		energyStorage.extractEnergy(process.recipe.getEnergyPerTick(), simulate);
		return 1f;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<ChemicalReactorRecipe> process)
	{
		ChemicalReactorRecipe recipe = process.recipe;
		FluidTank outputTank = getOutputTankForProcess(process);
		if(outputTank == null)
			return false;

		// Check fluid output
		if(recipe.fluidOutputs != null && recipe.fluidOutputs.length > 0)
			if(outputTank.fill(recipe.fluidOutputs[0], false)!=recipe.fluidOutputs[0].amount)
				return false;

		// Check item output
		if(recipe.itemOutput != null && !recipe.itemOutput.isEmpty())
		{
			int outputSlot = getItemOutputSlotForProcess(process);
			ItemStack outputItem = inventory.get(outputSlot);
			if(!outputItem.isEmpty())
				return false;
		}

		return true;
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<ChemicalReactorRecipe> process)
	{
		ChemicalReactorRecipe recipe = process.recipe;
		FluidTank inputTank1 = getInputTank1ForProcess(process);
		FluidTank inputTank2 = getInputTank2ForProcess(process);
		FluidTank outputTank = getOutputTankForProcess(process);

		// Consume fluid inputs
		if(inputTank1 != null && recipe.fluidInput != null)
			inputTank1.drain(recipe.fluidInput, true);
		if(inputTank2 != null && recipe.fluidInput2 != null)
			inputTank2.drain(recipe.fluidInput2, true);

		// Consume item inputs
		int itemInputSlot = getItemInputSlotForProcess(process);
		int catalystInputSlot = getCatalystInputSlotForProcess(process);
		if(recipe.itemInput != null)
			inventory.get(itemInputSlot).shrink(recipe.itemInput.inputSize);
		if(recipe.catalystInput != null)
			inventory.get(catalystInputSlot).shrink(recipe.catalystInput.inputSize);

		// Produce fluid output
		if(outputTank != null && recipe.fluidOutputs != null && recipe.fluidOutputs.length > 0)
			outputTank.fill(recipe.fluidOutputs[0], true);

		// Produce item output
		if(recipe.itemOutput != null && !recipe.itemOutput.isEmpty())
		{
			int outputSlot = getItemOutputSlotForProcess(process);
			if(inventory.get(outputSlot).isEmpty())
			{
				inventory.set(outputSlot, recipe.itemOutput.copy());
			}
		}
	}

	private FluidTank getInputTank1ForProcess(IIMultiblockProcess<ChemicalReactorRecipe> process)
	{
		if(process.processSet == 1) return tankSet1Input1;
		if(process.processSet == 2) return tankSet2Input1;
		if(process.processSet == 3) return tankSet3Input1;
		return null;
	}

	private FluidTank getInputTank2ForProcess(IIMultiblockProcess<ChemicalReactorRecipe> process)
	{
		if(process.processSet == 1) return tankSet1Input2;
		if(process.processSet == 2) return tankSet2Input2;
		if(process.processSet == 3) return tankSet3Input2;
		return null;
	}

	private FluidTank getOutputTankForProcess(IIMultiblockProcess<ChemicalReactorRecipe> process)
	{
		if(process.processSet == 1) return tankSet1Output;
		if(process.processSet == 2) return tankSet2Output;
		if(process.processSet == 3) return tankSet3Output;
		return null;
	}

	private int getItemInputSlotForProcess(IIMultiblockProcess<ChemicalReactorRecipe> process)
	{
		if(process.processSet == 1) return MultiblockChemicalReactor.SLOT_SET1_ITEM_INPUT;
		if(process.processSet == 2) return MultiblockChemicalReactor.SLOT_SET2_ITEM_INPUT;
		if(process.processSet == 3) return MultiblockChemicalReactor.SLOT_SET3_ITEM_INPUT;
		return -1;
	}

	private int getCatalystInputSlotForProcess(IIMultiblockProcess<ChemicalReactorRecipe> process)
	{
		if(process.processSet == 1) return MultiblockChemicalReactor.SLOT_SET1_CATALYST_INPUT;
		if(process.processSet == 2) return MultiblockChemicalReactor.SLOT_SET2_CATALYST_INPUT;
		if(process.processSet == 3) return MultiblockChemicalReactor.SLOT_SET3_CATALYST_INPUT;
		return -1;
	}

	private int getItemOutputSlotForProcess(IIMultiblockProcess<ChemicalReactorRecipe> process)
	{
		if(process.processSet == 1) return MultiblockChemicalReactor.SLOT_SET1_ITEM_OUTPUT;
		if(process.processSet == 2) return MultiblockChemicalReactor.SLOT_SET2_ITEM_OUTPUT;
		if(process.processSet == 3) return MultiblockChemicalReactor.SLOT_SET3_ITEM_OUTPUT;
		return -1;
	}

	//--- IPlayerInteraction ---//

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		TileEntityChemicalReactor master = master();
		if(master == null)
			return false;

		if(isPOI("input_fluid_1.1"))
			return FluidUtil.interactWithFluidHandler(player, hand, master.tankSet1Input1);
		if(isPOI("input_fluid_1.2"))
			return FluidUtil.interactWithFluidHandler(player, hand, master.tankSet1Input2);
		if(isPOI("input_fluid_2.1"))
			return FluidUtil.interactWithFluidHandler(player, hand, master.tankSet2Input1);
		if(isPOI("input_fluid_2.2"))
			return FluidUtil.interactWithFluidHandler(player, hand, master.tankSet2Input2);
		if(isPOI("input_fluid_3.1"))
			return FluidUtil.interactWithFluidHandler(player, hand, master.tankSet3Input1);
		if(isPOI("input_fluid_3.2"))
			return FluidUtil.interactWithFluidHandler(player, hand, master.tankSet3Input2);

		return false;
	}

	//--- IAdvancedTextOverlay ---//

	@SideOnly(Side.CLIENT)
	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		if(!Utils.isFluidRelatedItemStack(player.getHeldItem(EnumHand.MAIN_HAND)))
			return new String[0];

		TileEntityChemicalReactor master = master();
		if(master!=null)
		{
			if(isPOI("input_fluid_1.1"))
				return new String[]{IIUtils.getFluidNameOverlayText(master.tankSet1Input1.getFluid())};
			if(isPOI("input_fluid_1.2"))
				return new String[]{IIUtils.getFluidNameOverlayText(master.tankSet1Input2.getFluid())};
			if(isPOI("input_fluid_2.1"))
				return new String[]{IIUtils.getFluidNameOverlayText(master.tankSet2Input1.getFluid())};
			if(isPOI("input_fluid_2.2"))
				return new String[]{IIUtils.getFluidNameOverlayText(master.tankSet2Input2.getFluid())};
			if(isPOI("input_fluid_3.1"))
				return new String[]{IIUtils.getFluidNameOverlayText(master.tankSet3Input1.getFluid())};
			if(isPOI("input_fluid_3.2"))
				return new String[]{IIUtils.getFluidNameOverlayText(master.tankSet3Input2.getFluid())};
		}

		return new String[0];
	}
}
