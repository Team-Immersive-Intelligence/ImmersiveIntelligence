package pl.pabilo8.immersiveintelligence.common.util.multiblock.production;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIIMultiblockRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;


/**
 * A standard II production multiblock.<br>
 * Counterpart to {@link TileEntityMultiblockMetal}<br>
 *
 * @author Pabilo8
 * @since 13.04.2023
 */

public abstract class TileEntityMultiblockProductionBase<T extends TileEntityMultiblockProductionBase<T, R>, R extends IIIMultiblockRecipe>
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
			Utils.dropStackAtPos(world, getBlockPosForPos(outputPos[0]).offset(facing.getOpposite()), output, facing.getOpposite());

	}

	//--- Production Abstracts ---//

	/**
	 * @return minimal offset between production processes
	 */
	public abstract float getMinProductionOffset();

	/**
	 * @return max parallel processes in machine
	 */
	public abstract int getMaxProductionQueue();

	/**
	 * @return a new production process to be added
	 */
	protected abstract IIMultiblockProcess<R> findNewProductionProcess();

	/**
	 * @param nbt NBT data of a saved production process
	 * @return a new production process from NBT
	 */
	protected abstract IIMultiblockProcess<R> getProcessFromNBT(EasyNBT nbt);

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
		return Optional.ofNullable(getGUI()).map(Enum::ordinal).orElse(-1);
	}

	@Nullable
	public abstract IIGuiList getGUI();

	public abstract float getProductionProgress(IIMultiblockProcess<R> process, float partialTicks);

	public static class IIMultiblockProcess<R extends IIIMultiblockRecipe> implements INBTSerializable<NBTTagCompound>
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
		/**
		 * Additional process data
		 */
		public EasyNBT processData;

		public IIMultiblockProcess(R recipe)
		{
			this.recipe = recipe;
			this.ticks = 0;
			this.maxTicks = recipe.getTotalProcessTime();
			this.processData = recipe.writeToNBT();
		}

		public IIMultiblockProcess<R> withNBT(Consumer<EasyNBT> consumer)
		{
			consumer.accept(processData);
			return this;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			return processData
					.withFloat("ticks", ticks)
					.withInt("maxTicks", maxTicks)
					.unwrap();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			processData = EasyNBT.wrapNBT(nbt)
					.checkSetFloat("ticks", f -> ticks = f)
					.checkSetInt("maxTicks", i -> maxTicks = i)
					.without("ticks", "maxTicks");
		}
	}

	public interface IIIMultiblockRecipe
	{
		int getTotalProcessTime();

		int getTotalProcessEnergy();

		int getMultipleProcessTicks();

		EasyNBT writeToNBT();
	}

	/**
	 * Used for storing a queue of production processes and serializing their NBT.
	 *
	 * @param <T> TileEntity type
	 * @param <R> Recipe type
	 */
	public static class ProcessQueue<T extends TileEntityMultiblockProductionBase<T, R>, R extends IIIMultiblockRecipe>
			extends ArrayList<IIMultiblockProcess<R>> implements INBTSerializable<NBTTagList>
	{
		TileEntityMultiblockProductionBase<T, R> tile;

		public ProcessQueue(TileEntityMultiblockProductionBase<T, R> tile)
		{
			super();
			this.tile = tile;
		}

		@Override
		public NBTTagList serializeNBT()
		{
			NBTTagList list = new NBTTagList();
			for(IIMultiblockProcess<R> process : this)
				list.appendTag(process.serializeNBT());
			return list;
		}

		@Override
		public void deserializeNBT(NBTTagList nbt)
		{
			clear();
			for(NBTBase nbtBase : nbt)
				if(nbtBase instanceof NBTTagCompound)
				{
					//Get an empty process
					IIMultiblockProcess<R> process = tile.getProcessFromNBT(EasyNBT.wrapNBT(((NBTTagCompound)nbtBase)));
					if(process==null)
						continue;

					//Fill it with data from nbt
					process.deserializeNBT((NBTTagCompound)nbtBase);
					add(process);
				}
		}
	}
}
