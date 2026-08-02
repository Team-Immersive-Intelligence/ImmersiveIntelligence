package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecisionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IPrecisionTool;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.PrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIAssemblyScheme;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 */
public class TileEntityPrecisionAssembler extends TileEntityMultiblockProductionSingle<TileEntityPrecisionAssembler, PrecisionAssemblerRecipe> implements IBooleanAnimatedPartsBlock
{
	@SyncNBT
	public MultiblockInteractablePart drawer1, drawer2;
	@SyncNBT
	public String toolHash = "";
	private IEInventoryHandler outputMainHandler, outputSecondaryHandler, inputHandler;
	private IEInventoryHandler[] toolInputHandlers;

	public TileEntityPrecisionAssembler()
	{
		super(MultiblockPrecisionAssembler.INSTANCE);
		this.inventory = NonNullList.withSize(10, ItemStack.EMPTY);
		this.energyStorage = new FluxStorageAdvanced(PrecisionAssembler.energyCapacity);
		this.drawer1 = new MultiblockInteractablePart(0, 8, 0.75f);
		this.drawer2 = new MultiblockInteractablePart(1, 8, 0.75f);

		this.outputMainHandler = getSingleInventoryHandler(MultiblockPrecisionAssembler.SLOT_OUTPUT, true, true);
		this.outputSecondaryHandler = getSingleInventoryHandler(MultiblockPrecisionAssembler.SLOT_OUTPUT_TRASH, true, true);
		this.inputHandler = new IEInventoryHandler(4, this, MultiblockPrecisionAssembler.SLOT_INGREDIENT1, true, false);
		this.toolInputHandlers = new IEInventoryHandler[3];
		this.toolInputHandlers[0] = getSingleInventoryHandler(MultiblockPrecisionAssembler.SLOT_TOOL1, true, true);
		this.toolInputHandlers[1] = getSingleInventoryHandler(MultiblockPrecisionAssembler.SLOT_TOOL2, true, true);
		this.toolInputHandlers[2] = getSingleInventoryHandler(MultiblockPrecisionAssembler.SLOT_TOOL3, true, true);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		this.drawer1 = this.drawer2 = null;
		this.outputMainHandler = this.outputSecondaryHandler = this.inputHandler = null;
		this.toolInputHandlers = null;
	}

	@Override
	protected void onUpdate()
	{
		super.onUpdate();

		//Fix for scheme disappearing when process finishes and output is blocked
		if(currentProcess!=null&&inventory.get(MultiblockPrecisionAssembler.SLOT_SCHEME).isEmpty())
		{
			//Give back ingredients
			if(!world.isRemote)
				for(int i = 0; i < currentProcess.recipe.inputs.length; i++)
					Utils.dropStackAtPos(world, getPOIPos("item_in"), inputHandler.insertItem(i, currentProcess.recipe.inputs[i].getExampleStack(), false),
							getDirection("item_input")
					);
			currentProcess = null;
			if(!world.isRemote)
				updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
		}

		//Handle drawer animations
		this.drawer1.update();
		this.drawer2.update();

		//Handle output
		if(!world.isRemote)
		{
			attemptStackOutput(outputMainHandler, getDirection("item_output"), getPOI(MultiblockPOI.ITEM_OUTPUT));
			attemptStackOutput(outputSecondaryHandler, getDirection("item_output"), getPOI(MultiblockPOI.ITEM_OUTPUT));
		}
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case ENERGY_INPUT:
				return getPOI("energy");
			case ITEM_INPUT:
				return getPOI("all_item_inputs");
			case ITEM_OUTPUT:
				return getPOI("item_out");
			case REDSTONE:
				return getPOI("redstone");
			default:
				return new int[0];
		}
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		if(slot <= MultiblockPrecisionAssembler.SLOT_TOOL3)
			return stack.getItem() instanceof IPrecisionTool;
		else if(slot==MultiblockPrecisionAssembler.SLOT_SCHEME)
			return stack.getItem() instanceof ItemIIAssemblyScheme;
		else if(slot <= MultiblockPrecisionAssembler.SLOT_INGREDIENT4)
		{
			ItemStack scheme = inventory.get(MultiblockPrecisionAssembler.SLOT_SCHEME);
			if(scheme.getItem() instanceof ItemIIAssemblyScheme)
			{
				PrecisionAssemblerRecipe recipe = ((ItemIIAssemblyScheme)scheme.getItem()).getSchemeRecipe(scheme);
				if(recipe==null)
					return false;
				IngredientStack[] stacks = recipe.inputs;
				return stacks.length > slot-4&&stacks[slot-4].matchesItemStack(stack);
			}
		}
		return true;
	}

	@Override
	protected IIMultiblockProcess<PrecisionAssemblerRecipe> findNewProductionProcess()
	{
		//Check for assembly scheme in item slot
		ItemStack schemeStack = inventory.get(MultiblockPrecisionAssembler.SLOT_SCHEME);
		if(!(schemeStack.getItem() instanceof ItemIIAssemblyScheme))
			return null;

		//Get the recipe from the assembly scheme
		PrecisionAssemblerRecipe recipe = ((ItemIIAssemblyScheme)schemeStack.getItem()).getSchemeRecipe(schemeStack);
		if(recipe==null)
			return null;

		//Check tools
		if(!this.toolHash.contains(recipe.toolHash))
			return null;

		//Check ingredients
		for(int i = 0; i < recipe.inputs.length; i++)
		{
			IngredientStack ingredient = recipe.inputs[i];
			ItemStack inputStack = inventory.get(MultiblockPrecisionAssembler.SLOT_INGREDIENT1+i);
			if(!ingredient.matchesItemStack(inputStack))
				return null;
		}

		//Shrink ingredient stacks
		for(int i = 0; i < recipe.inputs.length; i++)
		{
			IngredientStack ingredient = recipe.inputs[i];
			inventory.get(MultiblockPrecisionAssembler.SLOT_INGREDIENT1+i).shrink(ingredient.inputSize);
		}

		return new IIMultiblockProcess<>(recipe);
	}

	@Override
	protected IIMultiblockProcess<PrecisionAssemblerRecipe> getProcessByName(String name)
	{
		PrecisionAssemblerRecipe recipe = IIMultiblockRecipe.getRecipe(PrecisionAssemblerRecipe.class, name);
		return recipe==null?null: new IIMultiblockProcess<>(recipe);
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<PrecisionAssemblerRecipe> process, boolean simulate)
	{
		if(energyStorage.extractEnergy(process.recipe.getEnergyPerTick(), true) < process.recipe.getEnergyPerTick())
			return 0;
		if(!this.toolHash.contains(process.recipe.toolHash))
			return 0;

		energyStorage.extractEnergy(process.recipe.getEnergyPerTick(), simulate);
		return 1f;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<PrecisionAssemblerRecipe> process)
	{
		//Attempt output
		return outputMainHandler.insertItem(0, process.recipe.output, true).isEmpty()&&
				outputSecondaryHandler.insertItem(0, process.recipe.trashOutput, true).isEmpty();
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<PrecisionAssemblerRecipe> process)
	{
		//Actually output items
		outputMainHandler.insertItem(0, process.recipe.output, false);
		outputSecondaryHandler.insertItem(0, process.recipe.trashOutput, false);

		//Bump up produced items count in scheme
		ItemStack schemeStack = inventory.get(MultiblockPrecisionAssembler.SLOT_SCHEME);
		((ItemIIAssemblyScheme)schemeStack.getItem()).increaseCreatedItems(schemeStack, process.recipe.trashOutput.getCount());

		//Damage tools
		for(int i = MultiblockPrecisionAssembler.SLOT_TOOL1; i <= MultiblockPrecisionAssembler.SLOT_TOOL3; i++)
		{
			ItemStack toolStack = inventory.get(i);
			if(toolStack.getItem() instanceof IPrecisionTool)
			{
				IPrecisionTool tool = (IPrecisionTool)toolStack.getItem();
				if(process.recipe.toolHash.contains(tool.getToolID(toolStack)))
					tool.damageTool(toolStack, 1);
			}
		}
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{
		if(slot >= MultiblockPrecisionAssembler.SLOT_TOOL1&&slot <= MultiblockPrecisionAssembler.SLOT_TOOL3)
		{
			//Rebuild tool hash
			ArrayList<String> toolList = new ArrayList<>();
			for(int i = MultiblockPrecisionAssembler.SLOT_TOOL1; i <= MultiblockPrecisionAssembler.SLOT_TOOL3; i++)
			{
				ItemStack toolStack = inventory.get(i);
				if(toolStack.getItem() instanceof IPrecisionTool)
					toolList.add(((IPrecisionTool)toolStack.getItem()).getToolID(toolStack));
			}
			this.toolHash = PrecisionAssemblerRecipe.buildToolHash(toolList.toArray(new String[0]));
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if(isPOI("item_in")||isPOI("tool1")||isPOI("tool2")||isPOI("tool3"))
				return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntityPrecisionAssembler master = master();
			assert master!=null;
			if(isPOI("item_in"))
				return (T)(master.inputHandler);
			else if(isPOI("tool1"))
				return (T)master.toolInputHandlers[0];
			else if(isPOI("tool2"))
				return (T)master.toolInputHandlers[1];
			else if(isPOI("tool3"))
				return (T)master.toolInputHandlers[2];

		}
		return super.getCapability(capability, facing);
	}

	@Nullable
	@Override
	public IIGUI getGUI()
	{
		return IIGUI.PRECISION_ASSEMBLER;
	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		MultiblockInteractablePart.setStates(state, part, drawer1, drawer2);
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		MultiblockInteractablePart changed = MultiblockInteractablePart.setStates(state, part, drawer1, drawer2);
		if(changed!=null)
		{
			world.playSound(null, getPos(), state?IISounds.drawerOpen: IISounds.drawerClose, SoundCategory.BLOCKS, 0.25F, 1f);
			IIPacketHandler.sendToClient(this, new MessageBooleanAnimatedPartsSync(changed, this));
		}
	}
}
