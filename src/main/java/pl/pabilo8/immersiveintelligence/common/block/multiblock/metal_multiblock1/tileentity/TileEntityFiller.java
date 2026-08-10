package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.api.DustTank;
import pl.pabilo8.immersiveintelligence.api.crafting.DustStack;
import pl.pabilo8.immersiveintelligence.api.crafting.DustUtils;
import pl.pabilo8.immersiveintelligence.api.crafting.FillerRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Filler;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockFiller;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionMulti;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.03.2021
 */
public class TileEntityFiller extends TileEntityMultiblockProductionMulti<TileEntityFiller, FillerRecipe> implements IConveyorAttachable
{
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.ENTITY_CUSTOM1})
	public DustTank dustStorage;
	private IItemHandler insertionHandlerDust, insertionHandlerStack;

	public TileEntityFiller()
	{
		super(MultiblockFiller.INSTANCE);
		this.energyStorage = new FluxStorageAdvanced(Filler.energyCapacity);
		this.inventory = NonNullList.withSize(2, ItemStack.EMPTY);
		this.dustStorage = new DustTank(Filler.dustCapacity);
		this.insertionHandlerDust = getSingleInventoryHandler(MultiblockFiller.SLOT_DUST, true, false);
		this.insertionHandlerStack = getSingleInventoryHandler(MultiblockFiller.SLOT_INPUT, true, false);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		dustStorage = null;
		insertionHandlerDust = insertionHandlerStack = null;
	}

	@Override
	protected void onUpdate()
	{
		super.onUpdate();

		//Insert dust into the tank
		if(world.getTotalWorldTime()%4==0&&!inventory.get(MultiblockFiller.SLOT_DUST).isEmpty())
		{
			DustStack dustStack = DustUtils.fromItemStack(ItemHandlerHelper.copyStackWithSize(inventory.get(MultiblockFiller.SLOT_DUST), 1));
			if(!dustStack.isEmpty()&&dustStorage.fill(dustStack, true) > 0)
			{
				inventory.get(MultiblockFiller.SLOT_DUST).shrink(1);
				updateTileForEvent(SyncEvents.ENTITY_CUSTOM1);
			}
		}
	}


	private EnumFacing getOutFacing()
	{
		return this.mirrored?this.facing.rotateYCCW(): this.facing.rotateY();
	}

	@Override
	public void onEntityCollision(World world, Entity entity)
	{
		if(!world.isRemote&&entity instanceof EntityItem)
		{
			ItemStack stack = ((EntityItem)entity).getItem();
			if(stack.isEmpty())
				return;

			if(isPOI("dust_input"))
				((EntityItem)entity).setItem(master().insertionHandlerDust.insertItem(0, stack, false));
			else if(isPOI("conveyor_in"))
				((EntityItem)entity).setItem(master().insertionHandlerStack.insertItem(0, stack, false));
		}
	}

	@Override
	public EnumFacing[] sigOutputDirections()
	{
		if(isPOI("conveyor_out")) return new EnumFacing[]{getOutFacing()};
		return new EnumFacing[0];
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntityFiller master = master();
			if(master==null) return false;
			if(isPOI("dust_input")&&facing==EnumFacing.UP) return true;
			return isPOI("conveyor_in")&&facing==getOutFacing().getOpposite();
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability!=CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return super.getCapability(capability, facing);

		if(isPOI("dust_input")&&facing==EnumFacing.UP)
			return (T)master().insertionHandlerDust;
		if(isPOI("conveyor_in")&&facing==getOutFacing().getOpposite())
			return (T)master().insertionHandlerStack;
		return null;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return slot==MultiblockFiller.SLOT_INPUT?1: super.getSlotLimit(slot);
	}

	@Override
	public boolean isStackValid(int i, ItemStack stack)
	{
		if(i==MultiblockFiller.SLOT_INPUT)
			return FillerRecipe.streamRecipes(FillerRecipe.class)
					.anyMatch(recipe -> recipe.itemInput.matchesItemStackIgnoringSize(stack));
		return i!=MultiblockFiller.SLOT_DUST||DustUtils.isDustStack(stack);
	}

	@Override
	public NonNullList<ItemStack> getDroppedItems()
	{
		NonNullList<ItemStack> droppedItems = super.getDroppedItems();
		if(!isDummy())
			droppedItems.addAll(dustStorage.turnIntoItems());
		return droppedItems;
	}

	@Override
	public float getMinProductionOffset()
	{
		return 0.66f;
	}

	@Override
	public int getMaxProductionQueue()
	{
		return 2;
	}

	@Override
	protected IIMultiblockProcess<FillerRecipe> findNewProductionProcess()
	{
		return FillerRecipe.streamRecipes(FillerRecipe.class)
				.filter(recipe -> recipe.itemInput.matchesItemStack(inventory.get(MultiblockFiller.SLOT_INPUT))&&
						!dustStorage.drain(recipe.dust, false).isEmpty())
				.findFirst().map(recipe -> {
					//Consume item and dust
					inventory.get(MultiblockFiller.SLOT_INPUT).shrink(recipe.itemInput.inputSize);
					dustStorage.drain(recipe.dust, true);

					//Sync with clients
					updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
					return new IIMultiblockProcess<>(recipe);
				}).orElse(null);
	}

	@Override
	protected IIMultiblockProcess<FillerRecipe> getProcessByName(String name)
	{
		FillerRecipe recipe = IIMultiblockRecipe.getRecipe(FillerRecipe.class, name);
		return recipe==null?null: new IIMultiblockProcess<>(recipe);
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<FillerRecipe> process, boolean simulate)
	{
		int perTick = process.recipe.getTotalProcessEnergy()/process.maxTicks;
		return energyStorage.extractEnergy(perTick, simulate)==perTick?1: 0;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<FillerRecipe> process)
	{
		return true;
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<FillerRecipe> process)
	{
		outputOrDrop(process.recipe.itemOutput.copy(), null, getOutFacing().getOpposite(), getPOI(MultiblockPOI.ITEM_OUTPUT));
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.FILLER;
	}
}
