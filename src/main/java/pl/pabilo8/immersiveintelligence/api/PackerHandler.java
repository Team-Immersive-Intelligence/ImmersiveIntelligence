package pl.pabilo8.immersiveintelligence.api;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.common.util.ILocalizedEnum;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 19.08.2022
 */
public class PackerHandler
{
	/**
	 * Used for handling item I/O
	 */
	private static HashMap<Predicate<ItemStack>, Function<ItemStack, IItemHandler>> itemHandleMap = new HashMap<>();
	/**
	 * Used for handling fluid I/O
	 */
	private static HashMap<Predicate<ItemStack>, Function<ItemStack, IFluidHandlerItem>> fluidHandleMap = new HashMap<>();

	public static void registerItem(Predicate<ItemStack> check, Function<ItemStack, IItemHandler> handler)
	{
		itemHandleMap.put(check, handler);
	}

	public static void registerFluid(Predicate<ItemStack> check, Function<ItemStack, IFluidHandlerItem> handler)
	{
		fluidHandleMap.put(check, handler);
	}

	public static Stream<Entry<Predicate<ItemStack>, Function<ItemStack, IItemHandler>>> streamItems()
	{
		return itemHandleMap.entrySet().stream();
	}

	public static Stream<Entry<Predicate<ItemStack>, Function<ItemStack, IFluidHandlerItem>>> streamFluids()
	{
		return fluidHandleMap.entrySet().stream();
	}

	public enum PackerPutMode implements ILocalizedEnum
	{
		//all possible
		ALL_POSSIBLE,
		//stack size * given amount
		SLOT,
		//given amount
		AMOUNT,
		//fill until x is inside container
		AT_MOST_CONTAINER,
		//fill until x is inside packer
		AT_LEAST_PACKER;

		@Override
		public String geLocaleKey()
		{
			return "ii.gui.packer.mode.";
		}
	}

	public enum PackerActionType implements ILocalizedEnum
	{
		ITEM,
		FLUID,
		ENERGY;

		@Override
		public String geLocaleKey()
		{
			return "ii.gui.packer.task.";
		}
	}

	@ParametersAreNonnullByDefault
	public static class PackerTask implements INBTSerializable<NBTTagCompound>
	{
		/**
		 * How much should be inserted
		 */
		public PackerPutMode mode = PackerPutMode.ALL_POSSIBLE;
		/**
		 * Item, Fluid or Energy
		 */
		public PackerActionType actionType = PackerActionType.ITEM;
		/**
		 * Filter for item and fluid tasks
		 */
		public IngredientStack stack = new IngredientStack("*");
		/**
		 * Filter for the container to be packed
		 */
		public IngredientStack containerFilter = new IngredientStack("*");
		/**
		 * Amount of items/fluid/energy transferred after which this task expires<br>
		 * -1 Means task will never expire
		 */
		public int expirationAmount = -1;
		/**
		 * Whether the task is reversed
		 */
		public boolean unpack = false;
		/**
		 * Logistic tag, acting as a filter for the container
		 */
		@Nullable
		public LogisticTag logiTag = null;

		public PackerTask()
		{

		}

		public PackerTask(PackerPutMode mode, PackerActionType actionType, IngredientStack stack)
		{
			this.mode = mode;
			this.actionType = actionType;
			this.stack = stack;
		}

		public PackerTask(NBTTagCompound nbt)
		{
			deserializeNBT(nbt);
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			return EasyNBT.newNBT()
					.withEnum("mode", mode)
					.withEnum("action_type", actionType)
					.withIngredientStack("stack", stack)
					.withIngredientStack("container_filter", containerFilter)
					.withInt("expiration_amount", expirationAmount)
					.withBoolean("unpack", unpack)
					.conditionally(logiTag!=null, e -> e.withSerializable("logi_tag", logiTag))
					.unwrap();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			EasyNBT enbt = EasyNBT.wrapNBT(nbt);
			this.mode = enbt.getEnum("mode", PackerPutMode.class);
			this.actionType = enbt.getEnum("action_type", PackerActionType.class);
			this.stack = enbt.getIngredientStack("stack");
			this.containerFilter = enbt.getIngredientStack("container_filter");
			this.expirationAmount = enbt.getInt("expiration_amount");
			this.unpack = enbt.getBoolean("unpack");
			this.logiTag = enbt.hasKey("logi_tag")?new LogisticTag(enbt.getCompound("logi_tag")): null;
		}
	}

	public static class LabelingTask implements INBTSerializable<NBTTagCompound>
	{
		public IngredientStack stack = new IngredientStack("*");
		public IngredientStack containerFilter = new IngredientStack("*");
		public int expirationAmount = -1;
		public int serialBatchStart = 0;
		@Nullable
		public LogisticTag logiTagIn = null;
		public LogisticTag logiTagOut = new LogisticTag();

		public LabelingTask()
		{

		}

		public LabelingTask(NBTTagCompound nbt)
		{
			deserializeNBT(nbt);
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			return EasyNBT.newNBT()
					.withIngredientStack("stack", stack)
					.withIngredientStack("container_filter", containerFilter)
					.withInt("expiration_amount", expirationAmount)
					.withInt("serial_batch_start", serialBatchStart)
					.conditionally(logiTagIn!=null, e -> e.withSerializable("logi_tag", logiTagIn))
					.withSerializable("logi_tag_out", logiTagOut)
					.unwrap();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			EasyNBT enbt = EasyNBT.wrapNBT(nbt);
			this.stack = enbt.getIngredientStack("stack");
			this.containerFilter = enbt.getIngredientStack("container_filter");
			this.expirationAmount = enbt.getInt("expiration_amount");
			this.serialBatchStart = enbt.getInt("serial_batch_start");
			this.logiTagIn = enbt.hasKey("logi_tag")?new LogisticTag(enbt.getCompound("logi_tag")): null;
			this.logiTagOut = new LogisticTag(enbt.getCompound("logi_tag_out"));
		}
	}
}
