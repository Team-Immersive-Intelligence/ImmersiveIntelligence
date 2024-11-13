package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorage;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.ImmutableSet;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.common.compat.BaublesHelper;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 22-06-2019
 */
@SuppressWarnings("unused")
public class IIUtils
{
	public static final Vec3d ONE = new Vec3d(1, 1, 1);

	public static <T extends TileEntity & IImmersiveConnectable> Set<Connection> genConnectableBlockstate(T te)
	{
		Set<Connection> conns = ImmersiveNetHandler.INSTANCE.getConnections(te.getWorld(), te.getPos());
		if(conns==null)
			return ImmutableSet.of();
		Set<Connection> ret = new HashSet<Connection>()
		{
			@Override
			public boolean equals(Object o)
			{
				if(o==this)
					return true;
				if(!(o instanceof HashSet))
					return false;
				HashSet<Connection> other = (HashSet<Connection>)o;
				if(other.size()!=this.size())
					return false;
				for(Connection c : this)
					if(!other.contains(c))
						return false;
				return true;
			}
		};
		for(Connection c : conns)
		{
			IImmersiveConnectable end = ApiUtils.toIIC(c.end, te.getWorld(), false);
			if(end==null)
				continue;
			c.getSubVertices(te.getWorld());
			ret.add(c);
		}
		return ret;
	}

	public static <T extends IFluidTank & IFluidHandler> boolean handleBucketTankInteraction(T tank, NonNullList<ItemStack> inventory, int bucketInputSlot, int bucketOutputSlot, boolean fillBucket, Predicate<FluidStack> filter)
	{
		if(inventory.get(bucketInputSlot).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
		{
			IFluidHandlerItem capability = inventory.get(bucketInputSlot).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if(!filter.test(capability.getTankProperties()[0].getContents()))
				return false;

			int amount_prev = tank.getFluidAmount();
			ItemStack emptyContainer;

			if(fillBucket)
			{
				if(tank.getTankProperties()[0].getContents()==null)
					return false;
				emptyContainer = blusunrize.immersiveengineering.common.util.Utils.fillFluidContainer(tank, inventory.get(bucketInputSlot), inventory.get(bucketOutputSlot), null);
			}
			else
			{
				if(capability.getTankProperties()[0].getContents()==null)
					return false;
				emptyContainer = blusunrize.immersiveengineering.common.util.Utils.drainFluidContainer(tank, inventory.get(bucketInputSlot), inventory.get(bucketOutputSlot), null);
			}

			if(amount_prev!=tank.getFluidAmount())
			{
				if(!inventory.get(bucketOutputSlot).isEmpty()&&OreDictionary.itemMatches(inventory.get(bucketOutputSlot), emptyContainer, true))
					inventory.get(bucketOutputSlot).grow(emptyContainer.getCount());
				else if(inventory.get(bucketOutputSlot).isEmpty())
					inventory.set(bucketOutputSlot, emptyContainer.copy());
				inventory.get(bucketInputSlot).shrink(1);
				if(inventory.get(bucketInputSlot).getCount() <= 0)
					inventory.set(bucketInputSlot, ItemStack.EMPTY);

				return true;
			}
		}
		return false;
	}

	public static <T extends IFluidTank & IFluidHandler> boolean handleBucketTankInteraction(T[] tanks, NonNullList<ItemStack> inventory, int bucketInputSlot, int bucketOutputSlot, int tank, boolean fillBucket)
	{
		return handleBucketTankInteraction(tanks, inventory, bucketInputSlot, bucketOutputSlot, tank, fillBucket, fluidStack -> true);
	}

	public static <T extends IFluidTank & IFluidHandler> boolean handleBucketTankInteraction(T[] tanks, NonNullList<ItemStack> inventory, int bucketInputSlot, int bucketOutputSlot, int tank, boolean fillBucket, Predicate<FluidStack> filter)
	{
		if(inventory.get(bucketInputSlot).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
		{
			IFluidHandlerItem capability = inventory.get(bucketInputSlot).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if(!filter.test(capability.getTankProperties()[0].getContents()))
				return false;

			int amount_prev = tanks[tank].getFluidAmount();
			ItemStack emptyContainer;

			if(fillBucket)
			{
				if(tanks[tank].getTankProperties()[0].getContents()==null)
					return false;
				emptyContainer = blusunrize.immersiveengineering.common.util.Utils.fillFluidContainer(tanks[tank], inventory.get(bucketInputSlot), inventory.get(bucketOutputSlot), null);
			}
			else
			{
				if(capability.getTankProperties()[0].getContents()==null)
					return false;
				emptyContainer = blusunrize.immersiveengineering.common.util.Utils.drainFluidContainer(tanks[tank], inventory.get(bucketInputSlot), inventory.get(bucketOutputSlot), null);
			}

			if(amount_prev!=tanks[tank].getFluidAmount())
			{
				if(!inventory.get(bucketOutputSlot).isEmpty()&&OreDictionary.itemMatches(inventory.get(bucketOutputSlot), emptyContainer, true))
					inventory.get(bucketOutputSlot).grow(emptyContainer.getCount());
				else if(inventory.get(bucketOutputSlot).isEmpty())
					inventory.set(bucketOutputSlot, emptyContainer.copy());
				inventory.get(bucketInputSlot).shrink(1);
				if(inventory.get(bucketInputSlot).getCount() <= 0)
					inventory.set(bucketInputSlot, ItemStack.EMPTY);

				return true;
			}
		}
		return false;
	}

	public static boolean outputFluidToTank(FluidTank tank, int amount, BlockPos pos, World world, EnumFacing side)
	{
		if(tank.getFluidAmount() > 0)
		{

			FluidStack out = tank.drain(Math.min(tank.getFluidAmount(), amount), false);
			IFluidHandler output = FluidUtil.getFluidHandler(world, pos.offset(side), side.getOpposite());
			if(output!=null)
			{
				int accepted = output.fill(out, true);
				if(accepted > 0)
				{
					int drained = output.fill(blusunrize.immersiveengineering.common.util.Utils.copyFluidStackWithAmount(out, Math.min(out.amount, accepted), false), true);
					tank.drain(drained, true);
					return true;
				}
			}
		}
		return false;
	}

	public static char cycleDataPacketChars(char current, boolean forward, boolean hasEmpty)
	{
		if(hasEmpty)
			if(current==' ')
				if(forward)
					current = DataPacket.varCharacters[0];
				else
					current = DataPacket.varCharacters[DataPacket.varCharacters.length-1];
			else
			{
				int current_char;

				current_char = ArrayUtils.indexOf(DataPacket.varCharacters, current);
				current_char += forward?1: -1;

				if(current_char >= DataPacket.varCharacters.length||current_char < 0)
					current = ' ';
				else
					current = DataPacket.varCharacters[current_char];
			}
		else
		{
			int current_char;

			current_char = ArrayUtils.indexOf(DataPacket.varCharacters, current);
			current_char += forward?1: -1;

			if(current_char >= DataPacket.varCharacters.length)
				current = DataPacket.varCharacters[0];
			else if(current_char < 0)
				current = DataPacket.varCharacters[DataPacket.varCharacters.length-1];
			else
				current = DataPacket.varCharacters[current_char];
		}
		return current;
	}

	public static char cyclePacketCharsAvoiding(char current, boolean forward, boolean hasEmpty, DataPacket packet)
	{
		int current_char = ArrayUtils.indexOf(DataPacket.varCharacters, current);
		int repeats = DataPacket.varCharacters.length+(hasEmpty?1: 0);

		for(int i = 0; i < repeats; i++)
		{
			current_char += forward?1: -1;
			if(current_char >= repeats)
				current_char = 0;
			if(current_char < 0)
				current_char = repeats-1;

			char c = (hasEmpty&&current_char==DataPacket.varCharacters.length)?' ': DataPacket.varCharacters[current_char];

			if(!packet.hasVariable(c))
				return c;
		}
		return current; //¯\_(ツ)_/¯
	}


	public static void unlockIIAdvancement(EntityPlayer player, String name)
	{
		if(player instanceof EntityPlayerMP)
		{
			//Can't unlock the same advancement twice
			if(hasUnlockedIIAdvancement(player, name))
				return;

			PlayerAdvancements advancements = ((EntityPlayerMP)player).getAdvancements();
			AdvancementManager manager = ((WorldServer)player.getEntityWorld()).getAdvancementManager();
			Advancement advancement = manager.getAdvancement(new ResourceLocation(ImmersiveIntelligence.MODID, name));
			if(advancement!=null)
				advancements.grantCriterion(advancement, "code_trigger");
		}
	}

	public static boolean hasUnlockedIIAdvancement(EntityPlayer player, String name)
	{
		if(player instanceof EntityPlayerMP)
		{
			PlayerAdvancements advancements = ((EntityPlayerMP)player).getAdvancements();
			AdvancementManager manager = ((WorldServer)player.getEntityWorld()).getAdvancementManager();
			Advancement advancement = manager.getAdvancement(new ResourceLocation(ImmersiveIntelligence.MODID, name));
			if(advancement!=null)
				return advancements.getProgress(advancement).isDone();
		}
		return false;
	}


	public static int cycleInt(boolean forward, int current, int min, int max)
	{
		current += forward?1: -1;
		if(current > max)
			return min;
		else if(current < min)
			return max;
		return current;
	}

	public static int cycleIntAvoid(boolean forward, int current, int min, int max, int avoid)
	{
		int i = cycleInt(forward, current, min, max);
		if(i==avoid)
			return cycleInt(forward, i, min, max);
		else
			return i;
	}

	public static <E extends Enum<E>> E cycleEnum(boolean forward, Class<E> enumType, E current)
	{
		return enumType.getEnumConstants()[cycleInt(forward, current.ordinal(), 0, enumType.getEnumConstants().length-1)];
	}

	public static boolean compareBlockstateOredict(IBlockState state, String oreName)
	{
		ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
		return blusunrize.immersiveengineering.common.util.Utils.compareToOreName(stack, oreName);
	}


	public static float getMaxClientProgress(float current, float required, int parts)
	{
		return current-(current%(required/parts));
	}


	@Deprecated
	public static String getPowerLevelString(TileEntityMultiblockMetal<?, ?> tile)
	{
		return getPowerLevelString(tile.getEnergyStored(null), tile.getMaxEnergyStored(null));
	}

	public static String getPowerLevelString(FluxStorage storage)
	{
		return getPowerLevelString(storage.getEnergyStored(), storage.getMaxEnergyStored());
	}

	public static String getPowerLevelString(int min, int max)
	{
		return String.format("%s/%s IF", min, max);
	}

	public static IngredientStack ingredientFromData(DataType dataType)
	{
		if(dataType instanceof DataTypeItemStack)
			return new IngredientStack((((DataTypeItemStack)dataType).value.copy()));
		else if(dataType instanceof DataTypeString)
			return new IngredientStack(dataType.toString());
		else
			return new IngredientStack("*");
	}

	public static DataPacket getSimpleCallbackMessage(DataPacket packet, String parameter, DataType value)
	{
		packet.setVariable('c', new DataTypeString(parameter));
		packet.setVariable('g', value);
		return packet;
	}

	public static void giveOrDropCasingStack(@Nonnull Entity entity, ItemStack stack)
	{
		//attempt to give the item
		if(entity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
		{
			IItemHandler capability = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(capability!=null)
				for(int i = 0; i < 10; i++)
				{
					if(stack.isEmpty())
						break;

					ItemStack pouchStack;
					if(i==0)
						pouchStack = (IICompatModule.baubles&&entity instanceof EntityPlayer)?
								BaublesHelper.getWornPouch(((EntityPlayer)entity)):
								ItemStack.EMPTY;
					else
						pouchStack = capability.getStackInSlot(i-1);

					if(!pouchStack.getItem().equals(IIContent.itemCasingPouch))
						continue;

					IItemHandler pouchCap = pouchStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					if(pouchCap==null)
						continue;

					stack = ItemHandlerHelper.insertItem(pouchCap, stack, false);
					if(entity instanceof EntityPlayer)
						((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
				}
		}

		if(!stack.isEmpty())
			giveOrDropStack(entity, stack);
	}

	public static void giveOrDropStack(@Nonnull Entity entity, ItemStack stack)
	{
		//attempt to give the item
		if(entity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
		{
			IItemHandler capability = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			stack = ItemHandlerHelper.insertItem(capability, stack, false);
		}
		//if can't do that, drop it on the entity's position
		if(!stack.isEmpty())
			Utils.dropStackAtPos(entity.world, entity.getPosition(), stack);
	}

	/**
	 * <i>Trust me, I'm an Engineer!</i><br>
	 * Returns a value of an annotation for an enum extending {@link ISerializableEnum}<br>
	 * Generally safe to use, but slow. Cache the results.
	 */
	@Nullable
	public static <T extends Annotation> T getEnumAnnotation(Class<T> annotationClass, ISerializableEnum e)
	{
		try
		{
			Field field = e.getClass().getDeclaredField(e.getName().toUpperCase());
			if(field.isAnnotationPresent(annotationClass))
				return field.getAnnotation(annotationClass);
		} catch(NoSuchFieldException ignored) {}
		return null;
	}

	/**
	 * <i>Trust me, I'm an Engineer!</i><br>
	 * Returns a value of an annotation<br>
	 * Generally safe to use, but slow. Cache the results.
	 */
	@Nullable
	public static <T extends Annotation> T getAnnotation(Class<T> annotationClass, Object o)
	{
		if(o.getClass().isAnnotationPresent(annotationClass))
			return o.getClass().getAnnotation(annotationClass);
		return null;
	}

	//REFACTOR: 27.03.2024 replace lambda variant of this in project

	/**
	 * @param en   enum class
	 * @param name name of the enum value
	 * @param <T>  enum type
	 * @return enum value with name, case insensitive
	 */
	@Nonnull
	public static <T extends Enum<T> & ISerializableEnum> T enumValue(Class<T> en, String name)
	{
		return Enum.valueOf(en, name.toUpperCase());
	}

	public static void fixupItem(Item item, String itemName)
	{
		// First, get the item out of IE's registries.
		Item rItem = IEContent.registeredIEItems.remove(IEContent.registeredIEItems.size()-1);
		if(rItem!=item)
			throw new IllegalStateException("fixupItem was not called at the appropriate time");

		// Now, reconfigure the block to match our mod.
		item.setUnlocalizedName(ImmersiveIntelligence.MODID+"."+itemName);
		item.setCreativeTab(IIContent.II_CREATIVE_TAB);

		// And add it to our registries.
		IIContent.ITEMS.add(item);
	}

	public static void sendToolbarMessage(EntityPlayer player, String messageFormat, Object... args)
	{
		player.sendStatusMessage(new TextComponentTranslation(messageFormat, args), true);
	}

}
