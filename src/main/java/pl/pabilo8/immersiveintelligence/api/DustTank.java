package pl.pabilo8.immersiveintelligence.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.api.crafting.DustStack;
import pl.pabilo8.immersiveintelligence.api.crafting.DustUtils;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 21.08.2025
 */
public class DustTank implements INBTSerializable<NBTTagCompound>
{
	protected DustStack dustStack = DustStack.getEmptyStack();
	protected int capacity;

	public DustTank(int capacity)
	{
		this.capacity = capacity;
	}

	/**
	 * @return Current amount of fluid in the tank.
	 */
	public DustStack getDustStack()
	{
		return dustStack;
	}

	/**
	 * @return Capacity of this fluid tank.
	 */
	public int getCapacity()
	{
		return capacity;
	}

	/**
	 *
	 * @param resource DustStack attempting to fill the tank.
	 * @param doFill   If false, the fill will only be simulated.
	 * @return Amount of dust that was accepted by the tank.
	 */
	public int fill(DustStack resource, boolean doFill)
	{
		if(resource.isEmpty()||resource.amount <= 0)
			return 0;

		if(!dustStack.canMergeWith(resource))
			return 0;

		int accepted = capacity-Math.min(dustStack.amount+resource.amount, capacity);
		if(accepted <= 0)
			return 0;

		if(doFill)
			dustStack = dustStack.mergeWith(resource);
		return accepted;
	}

	/**
	 *
	 * @param maxDrain Maximum amount of dust to be removed from the container.
	 * @param doDrain  If false, the drain will only be simulated.
	 * @return Amount of dust that was removed from the tank.
	 */
	@Nonnull
	public DustStack drain(int maxDrain, boolean doDrain)
	{
		if(dustStack.isEmpty()||maxDrain <= 0)
			return DustStack.getEmptyStack();

		int drainedAmount = Math.min(maxDrain, dustStack.amount);
		DustStack drainedDust = new DustStack(dustStack.name, drainedAmount);

		if(doDrain)
		{
			dustStack = dustStack.subtract(drainedDust);
			if(dustStack.isEmpty())
				dustStack = DustStack.getEmptyStack();
		}

		return drainedDust;
	}

	public DustStack drain(DustStack resource, boolean doDrain)
	{
		DustStack drained = drain(resource.amount, false);
		if(drained.equals(resource)&&drained.amount==resource.amount)
			return drain(resource.amount, doDrain);
		return DustStack.getEmptyStack();
	}

	/**
	 * @return a (most accurate) equivalent of this dust tank as a list of dust ItemStacks
	 */
	public List<ItemStack> turnIntoItems()
	{
		List<ItemStack> list = new ArrayList<>();
		Collections.addAll(Arrays.asList(DustUtils.fromDustStack(dustStack)));
		return list;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT()
				.withTag("contents", dustStack.serializeNBT())
				.unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		dustStack.deserializeNBT(nbt.getCompoundTag("contents"));
	}
}
