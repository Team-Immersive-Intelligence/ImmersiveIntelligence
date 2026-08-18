package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.LogisticTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Stores an ingredient filter with lossless NBT and optional logistics-tag matching.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 12.08.2026
 */
public class IngredientReference extends IngredientStack implements INBTSerializable<NBTTagCompound>, Cloneable
{
	private static final String NBT_FLUID_TAG = "fluidNBT";
	private static final String NBT_LOGISTIC_TAG = "logisticTag";

	@Getter
	@Nullable
	private LogisticTag logisticTag;

	/**
	 * Creates a wildcard ingredient reference.
	 */
	public IngredientReference()
	{
		super("*");
	}

	/**
	 * Creates an item-stack ingredient reference.
	 */
	public IngredientReference(@Nonnull ItemStack stack)
	{
		super(stack.copy());
	}

	/**
	 * Creates an Ore Dictionary or wildcard ingredient reference.
	 */
	public IngredientReference(@Nonnull String oreName)
	{
		super(oreName);
	}

	/**
	 * Creates an Ore Dictionary or wildcard ingredient reference with an amount.
	 */
	public IngredientReference(@Nonnull String oreName, int inputSize)
	{
		super(oreName, inputSize);
	}

	/**
	 * Creates an item-list ingredient reference.
	 */
	public IngredientReference(@Nonnull List<ItemStack> stackList)
	{
		this(stackList, 1);
	}

	/**
	 * Creates an item-list ingredient reference with an amount.
	 */
	public IngredientReference(@Nonnull List<ItemStack> stackList, int inputSize)
	{
		super(copyStacks(stackList), inputSize);
	}

	/**
	 * Creates a fluid ingredient reference.
	 */
	public IngredientReference(@Nonnull FluidStack fluid)
	{
		super(fluid.copy());
		this.inputSize = Math.max(1, fluid.amount);
	}

	/**
	 * Creates a wildcard reference with a logistics-tag filter.
	 */
	public IngredientReference(@Nonnull LogisticTag logisticTag)
	{
		this();
		this.logisticTag = logisticTag.clone();
	}

	/**
	 * Creates a deep copy of an IE ingredient stack.
	 */
	public IngredientReference(@Nonnull IngredientStack ingredient)
	{
		super(ingredient);
		this.stack = ingredient.stack==null?null: ingredient.stack.copy();
		this.stackList = ingredient.stackList==null?null: copyStacks(ingredient.stackList);
		this.fluid = ingredient.fluid==null?null: ingredient.fluid.copy();
		this.inputSize = ingredient.inputSize;
		this.useNBT = ingredient.useNBT;
		if(ingredient instanceof IngredientReference)
		{
			LogisticTag tag = ((IngredientReference)ingredient).logisticTag;
			this.logisticTag = tag==null?null: tag.clone();
		}
	}

	/**
	 * Converts an IE ingredient stack into an IngredientReference.
	 */
	@Nonnull
	public static IngredientReference fromIngredientStack(@Nullable IngredientStack ingredient)
	{
		return ingredient==null?new IngredientReference(): new IngredientReference(ingredient);
	}

	/**
	 * Converts this reference to a plain IE ingredient stack.
	 * The optional logistics-tag predicate is not represented by IngredientStack.
	 */
	@Nonnull
	public IngredientStack toIngredientStack()
	{
		IngredientStack result;
		if(isWildcard())
			result = new IngredientStack("*", inputSize);
		else if(fluid!=null)
		{
			FluidStack copy = fluid.copy();
			copy.amount = inputSize;
			result = new IngredientStack(copy);
		}
		else if(oreName!=null)
			result = new IngredientStack(oreName, inputSize);
		else if(stackList!=null)
			result = new IngredientStack(copyStacks(stackList), inputSize);
		else
			result = new IngredientStack(stack==null?ItemStack.EMPTY: stack.copy());
		result.inputSize = inputSize;
		result.useNBT = useNBT;
		return result;
	}

	/**
	 * Sets whether NBT must match.
	 */
	@Override
	public IngredientReference setUseNBT(boolean useNBT)
	{
		this.useNBT = useNBT;
		return this;
	}

	/**
	 * Sets an optional logistics-tag predicate.
	 */
	public IngredientReference withLogisticTag(@Nullable LogisticTag logisticTag)
	{
		this.logisticTag = logisticTag==null?null: logisticTag.clone();
		return this;
	}

	/**
	 * Returns true for the native '*' ingredient mode.
	 */
	public boolean isWildcard()
	{
		return (stack==null||stack.isEmpty())&&(stackList==null||stackList.isEmpty())&&(fluid==null||fluid.amount==0)
				&&(oreName==null||"*".equals(oreName));
	}

	/**
	 * Returns true if this reference also checks a logistics tag.
	 */
	public boolean hasLogisticTag()
	{
		return logisticTag!=null;
	}

	/**
	 * Returns true if the fluid matches this reference and required amount.
	 */
	public boolean matchesFluidStack(@Nullable FluidStack input)
	{
		return logisticTag==null&&input!=null&&input.amount >= Math.max(1, inputSize)&&matchesFluidValue(input);
	}

	/**
	 * Returns true if the fluid matches this reference without checking amount.
	 */
	public boolean matchesFluidStackIgnoringSize(@Nullable FluidStack input)
	{
		return logisticTag==null&&matchesFluidValue(input);
	}

	@Override
	public boolean matches(Object input)
	{
		if(input instanceof IngredientStack)
		{
			IngredientReference other = fromIngredientStack((IngredientStack)input);
			return equals(other)&&inputSize <= other.inputSize;
		}
		if(input instanceof FluidStack)
			return matchesFluidStack((FluidStack)input);
		return super.matches(input);
	}

	@Override
	public boolean matchesItemStack(@Nullable ItemStack input)
	{
		if(input==null||input.isEmpty())
			return false;
		if(fluid!=null)
		{
			FluidStack contained = FluidUtil.getFluidContained(input);
			return contained!=null&&contained.amount >= Math.max(1, inputSize)
					&&matchesFluidValue(contained)&&matchesLogisticTag(input);
		}
		return input.getCount() >= Math.max(1, inputSize)&&matchesItemStackIgnoringSize(input);
	}

	@Override
	public boolean matchesItemStackIgnoringSize(@Nullable ItemStack input)
	{
		if(input==null||input.isEmpty())
			return false;

		boolean matchesIngredient;
		if(isWildcard())
			matchesIngredient = true;
		else if(fluid!=null)
			matchesIngredient = matchesFluidValue(FluidUtil.getFluidContained(input));
		else if(oreName!=null)
			matchesIngredient = super.matchesItemStackIgnoringSize(input);
		else if(stackList!=null)
		{
			matchesIngredient = false;
			for(ItemStack listed : stackList)
				if(matchesListedStack(listed, input))
				{
					matchesIngredient = true;
					break;
				}
		}
		else
			matchesIngredient = matchesListedStack(stack, input);
		return matchesIngredient&&matchesLogisticTag(input);
	}

	@Override
	public List<ItemStack> getStackList()
	{
		return isWildcard()?Collections.emptyList(): super.getStackList();
	}

	@Override
	public ItemStack getRandomizedExampleStack(long rand)
	{
		if(isWildcard())
			return ItemStack.EMPTY;
		ItemStack example = super.getRandomizedExampleStack(rand);
		return example==null?ItemStack.EMPTY: example;
	}

	@Override
	public ItemStack getExampleStack()
	{
		if(isWildcard())
			return ItemStack.EMPTY;
		if(fluid!=null)
			return FluidUtil.getFilledBucket(fluid);
		ItemStack example = super.getExampleStack();
		return example==null?ItemStack.EMPTY: example;
	}

	/**
	 * Creates a copy with a different required amount.
	 */
	@Override
	public IngredientReference copyWithSize(int size)
	{
		IngredientReference copy = clone();
		copy.inputSize = size;
		return copy;
	}

	/**
	 * Creates a copy with a multiplied required amount.
	 */
	@Override
	public IngredientReference copyWithMultipliedSize(double multiplier)
	{
		return copyWithSize((int)Math.floor(inputSize*multiplier));
	}

	/**
	 * Writes the IE-compatible ingredient format and II extensions.
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(isWildcard()&&oreName==null)
			new IngredientStack("*", inputSize).writeToNBT(nbt);
		else
			super.writeToNBT(nbt);
		// Keep the shared amount authoritative for legacy readers of fluid references.
		if(fluid!=null)
			nbt.setInteger("fluidAmount", inputSize);
		// IE only stores useNBT for item-stack mode. Keep it for all reference modes.
		nbt.setBoolean("useNBT", useNBT);
		if(fluid!=null&&fluid.tag!=null)
			nbt.setTag(NBT_FLUID_TAG, fluid.tag.copy());
		if(logisticTag!=null)
			nbt.setTag(NBT_LOGISTIC_TAG, logisticTag.serializeNBT());
		return nbt;
	}

	/**
	 * Serializes this reference to NBT.
	 */
	@Override
	public NBTTagCompound serializeNBT()
	{
		return writeToNBT(new NBTTagCompound());
	}

	/**
	 * Loads this reference from IE-compatible NBT.
	 */
	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		copyFrom(readFromNBT(nbt));
	}

	/**
	 * Reads an IngredientReference from IE-compatible NBT.
	 */
	@Nonnull
	public static IngredientReference readFromNBT(@Nullable NBTTagCompound nbt)
	{
		if(nbt==null||!nbt.hasKey("nbtType"))
			return new IngredientReference();

		IngredientStack legacy = IngredientStack.readFromNBT(nbt);
		if(legacy==null)
			return new IngredientReference();

		IngredientReference result = new IngredientReference(legacy);
		if(nbt.hasKey("inputSize"))
			result.inputSize = nbt.getInteger("inputSize");
		if(nbt.hasKey("useNBT"))
			result.useNBT = nbt.getBoolean("useNBT");
		if(result.fluid!=null&&nbt.hasKey(NBT_FLUID_TAG, NBT.TAG_COMPOUND))
			result.fluid.tag = nbt.getCompoundTag(NBT_FLUID_TAG).copy();
		if(nbt.hasKey(NBT_LOGISTIC_TAG, NBT.TAG_COMPOUND))
			result.logisticTag = new LogisticTag(nbt.getCompoundTag(NBT_LOGISTIC_TAG));
		return result;
	}

	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@Override
	public IngredientReference clone()
	{
		return new IngredientReference(this);
	}

	@Override
	public boolean equals(Object object)
	{
		if(this==object)
			return true;
		if(!(object instanceof IngredientReference))
			return false;

		IngredientReference other = (IngredientReference)object;
		String thisOre = isWildcard()?"*": oreName;
		String otherOre = other.isWildcard()?"*": other.oreName;
		return useNBT==other.useNBT
				&&Objects.equals(thisOre, otherOre)
				&&stacksEqualReference(stack, other.stack, useNBT)
				&&stackListsEqualReference(stackList, other.stackList, useNBT)
				&&fluidsEqualReference(fluid, other.fluid, useNBT)
				&&Objects.equals(logisticTag, other.logisticTag);
	}

	@Override
	public int hashCode()
	{
		int result = Objects.hash(useNBT, isWildcard()?"*": oreName, logisticTag);
		result = 31*result+stackHashReference(stack, useNBT);
		if(stackList!=null)
			for(ItemStack listed : stackList)
				result = 31*result+stackHashReference(listed, useNBT);
		if(fluid!=null)
			result = 31*result+Objects.hash(fluid.getFluid(), useNBT?fluid.tag: null);
		return result;
	}

	private void copyFrom(@Nonnull IngredientReference other)
	{
		this.stack = other.stack==null?null: other.stack.copy();
		this.stackList = other.stackList==null?null: copyStacks(other.stackList);
		this.oreName = other.oreName;
		this.fluid = other.fluid==null?null: other.fluid.copy();
		this.inputSize = other.inputSize;
		this.useNBT = other.useNBT;
		this.logisticTag = other.logisticTag==null?null: other.logisticTag.clone();
	}

	private boolean matchesFluidValue(@Nullable FluidStack input)
	{
		if(input==null)
			return false;
		if(isWildcard())
			return true;
		if(fluid==null||input.getFluid()!=fluid.getFluid())
			return false;
		return !useNBT||Objects.equals(fluid.tag, input.tag);
	}

	private boolean matchesLogisticTag(ItemStack input)
	{
		return logisticTag==null||logisticTag.itemMatches(input);
	}

	private static List<ItemStack> copyStacks(List<ItemStack> stacks)
	{
		List<ItemStack> copy = new ArrayList<>(stacks.size());
		for(ItemStack stack : stacks)
			if(stack!=null)
				copy.add(stack.copy());
		return copy;
	}

	private boolean matchesListedStack(@Nullable ItemStack listed, @Nonnull ItemStack input)
	{
		return listed!=null&&!listed.isEmpty()
				&&OreDictionary.itemMatches(listed, input, false)
				&&(!useNBT||ItemStack.areItemStackTagsEqual(listed, input));
	}

	private static boolean stackListsEqualReference(@Nullable List<ItemStack> first, @Nullable List<ItemStack> second, boolean useNBT)
	{
		if(first==second)
			return true;
		if(first==null||second==null||first.size()!=second.size())
			return false;
		for(int i = 0; i < first.size(); i++)
			if(!stacksEqualReference(first.get(i), second.get(i), useNBT))
				return false;
		return true;
	}

	private static boolean stacksEqualReference(@Nullable ItemStack first, @Nullable ItemStack second, boolean useNBT)
	{
		if(first==second)
			return true;
		if(first==null||second==null)
			return false;
		if(first.isEmpty()||second.isEmpty())
			return first.isEmpty()&&second.isEmpty();
		return ItemStack.areItemsEqual(first, second)
				&&(!useNBT||ItemStack.areItemStackTagsEqual(first, second));
	}

	private static boolean fluidsEqualReference(@Nullable FluidStack first, @Nullable FluidStack second, boolean useNBT)
	{
		if(first==second)
			return true;
		return first!=null&&second!=null
				&&first.getFluid()==second.getFluid()
				&&(!useNBT||Objects.equals(first.tag, second.tag));
	}

	private static int stackHashReference(@Nullable ItemStack stack, boolean useNBT)
	{
		if(stack==null||stack.isEmpty())
			return 0;
		return Objects.hash(stack.getItem().getRegistryName(), stack.getMetadata(), useNBT?stack.getTagCompound(): null);
	}


}
