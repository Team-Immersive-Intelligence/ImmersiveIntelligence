package pl.pabilo8.immersiveintelligence.api;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Pabilo8
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

	public enum PackerPutMode implements IStringSerializable
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

		@Nonnull
		@Override
		public String getName()
		{
			return this.toString().toLowerCase();
		}

		public static PackerPutMode fromName(String name)
		{
			String ss = name.toUpperCase();
			return Arrays.stream(values())
					.filter(e -> e.name().equals(ss))
					.findFirst()
					.orElse(ALL_POSSIBLE);
		}
	}

	public enum PackerActionType
	{
		ITEM,
		FLUID,
		ENERGY;

		public String getActionName(boolean unpacker)
		{
			switch(this)
			{
				default:
				case ITEM:
					return unpacker?"unpack": "pack";
				case FLUID:
					return unpacker?"drain": "fill";
				case ENERGY:
					return unpacker?"discharge": "charge";
			}
		}

		public static PackerActionType fromName(String name)
		{
			switch(name.toLowerCase())
			{
				default:
				case "item":
					return ITEM;
				case "fluid":
					return FLUID;
				case "energy":
					return ENERGY;
			}
		}
	}

	@ParametersAreNonnullByDefault
	public static class PackerTask
	{
		/**
		 * How much should be inserted
		 */
		public PackerPutMode mode;
		/**
		 * Item, Fluid or Energy
		 */
		public PackerActionType actionType;
		/**
		 * Filter for item and fluid tasks
		 */
		public IngredientStack stack;
		/**
		 * Amount of items/fluid/energy transferred after which this task expires<br>
		 * -1 Means task will never expire
		 */
		public int expirationAmount = -1;
		/**
		 * Whether the task is reversed
		 */
		public boolean unpack = false;

		public PackerTask(PackerPutMode mode, PackerActionType actionType, IngredientStack stack)
		{
			this.actionType = actionType;
			this.mode = mode;
			this.stack = stack;
		}

		public PackerTask(NBTTagCompound nbt)
		{
			this(
					PackerPutMode.valueOf(nbt.getString("mode").toUpperCase()),
					PackerActionType.valueOf(nbt.getString("action_type").toUpperCase()),
					IngredientStack.readFromNBT(nbt.getCompoundTag("stack"))
			);

			if(nbt.hasKey("expiration_amount"))
				expirationAmount = nbt.getInteger("expiration_amount");
			unpack = nbt.getBoolean("unpack");
		}

		public NBTTagCompound toNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("mode", mode.getName());
			nbt.setString("action_type", actionType.name().toLowerCase());
			nbt.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
			if(expirationAmount!=-1)
				nbt.setInteger("expiration_amount", expirationAmount);
			nbt.setBoolean("unpack", unpack);
			return nbt;
		}
	}
}
