package pl.pabilo8.immersiveintelligence.common.util.multiblock.production;

import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;

import javax.annotation.Nullable;


/**
 * A standard II production multiblock.<br>
 * Counterpart to {@link TileEntityMultiblockMetal}<br>
 *
 * @author Pabilo8
 * @since 13.04.2023
 */

public abstract class TileEntityMultiblockProductionBase<T extends TileEntityMultiblockProductionBase<T, R>, R extends IMultiblockRecipe>
		extends TileEntityMultiblockIIGeneric<T>
		implements IGuiTile
{
	public TileEntityMultiblockProductionBase(MultiblockStuctureBase<T> multiblock)
	{
		super(multiblock);
	}


	//--- Production-related Utilities ---//

	public void outputOrDrop(ItemStack output, @Nullable IItemHandler itemHandler, EnumFacing facing, int... outputPos)
	{
		for(int p : outputPos)
		{
			BlockPos pos = getBlockPosForPos(p).offset(facing.getOpposite());
			TileEntity inventoryTile = this.world.getTileEntity(pos);
			if(inventoryTile!=null)
				output = Utils.insertStackIntoInventory(inventoryTile, output, facing, world.isRemote);
			if(output.isEmpty())
				return;
		}

		if(itemHandler!=null)
			output = ItemHandlerHelper.insertItemStacked(itemHandler, output, world.isRemote);

		if(outputPos.length > 0&&!world.isRemote)
			Utils.dropStackAtPos(world, getBlockPosForPos(outputPos[0]), output, facing.getOpposite());

	}

	public void attemptOutput(IEInventoryHandler handler, int slot, int outputPos, EnumFacing facing)
	{
		/*handler.extractItem(slot);
		BlockPos pos = getBlockPosForPos(outputPos).offset(facing.getOpposite());

		TileEntity inventoryTile = this.world.getTileEntity(pos);
		if(inventoryTile!=null)
			output = Utils.insertStackIntoInventory(inventoryTile, output, facing);
		inventory.set(slot, output);*/
	}

	//--- Production Abstracts ---//

	/**
	 * @param nbt recipe tag compound
	 * @return a recipe instance from nbt
	 */
	public abstract R loadRecipeFromNBT(NBTTagCompound nbt);

	/**
	 * @return minimal offset between production processes
	 */
	public abstract int getMinProductionOffset();

	/**
	 * @return max parallel processes in machine
	 */
	public abstract int getMaxProductionQueue();

	/**
	 * @return a new production process to be added
	 */
	protected abstract IIMultiblockProcess<R> findNewProductionProcess();

	/**
	 * @param process  the production process
	 * @param simulate whether resources should actually be used
	 * @return how many ticks should the current production process progress
	 */
	public abstract float getProductionStep(IIMultiblockProcess<R> process, boolean simulate);

	/**
	 * @param process the production process
	 * @return true if output was successful, false if it should be repeated in one more tick
	 */
	protected abstract boolean attemptProductionOutput(IIMultiblockProcess<R> process);

	/**
	 * Performed if {@link #attemptProductionOutput} was true<br>
	 * Consume crafting items/fluids there
	 *
	 * @param process the production process
	 */
	protected abstract void onProductionFinish(IIMultiblockProcess<R> process);


	//--- IGuiTile ---//


	@Override
	public final T getGuiMaster()
	{
		return master();
	}

	/**
	 * Not sure why overload a one parameter method...
	 */
	@Override
	public final boolean canOpenGui()
	{
		return false;
	}

	@Override
	public boolean canOpenGui(EntityPlayer player)
	{
		return formed;
	}

	@Override
	public void onGuiOpened(EntityPlayer player, boolean clientside)
	{
		if(!clientside)
			forceTileUpdate();
	}

	@Override
	public final int getGuiID()
	{
		return getGUI().ordinal();
	}

	public abstract IIGuiList getGUI();

	public abstract float getProductionProgress(IIMultiblockProcess<R> process, float partialTicks);

	public static class IIMultiblockProcess<R extends IMultiblockRecipe>
	{
		public R recipe;
		/**
		 * Time of current production process
		 */
		public float ticks;
		/**
		 * Total time of current production process
		 */
		public int maxTicks;

		public IIMultiblockProcess(R recipe)
		{
			this.recipe = recipe;
			this.ticks = 0;
			this.maxTicks = recipe.getTotalProcessTime();
		}
	}
}
