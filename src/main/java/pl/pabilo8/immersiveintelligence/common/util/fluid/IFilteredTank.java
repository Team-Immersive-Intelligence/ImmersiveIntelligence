package pl.pabilo8.immersiveintelligence.common.util.fluid;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * An extension of {@link IFluidTank} that allows filtering of fluids that can be inserted or extracted.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 07.10.2026
 * @since 21.09.2026
 */
public interface IFilteredTank<T extends IFilteredTank<T>> extends IFluidTank, IFluidHandler
{
	/**
	 * Attempts a transfer using the held stack. Returns false when normal item or GUI interaction must stop.
	 */
	default boolean interactWithItem(EntityPlayer player, EnumHand hand, ItemStack heldItem)
	{
		return interactWithItem(player, hand, heldItem, null);
	}

	/**
	 * The callback runs on the server after a successful transfer, for saving and synchronising the tank.
	 */
	default boolean interactWithItem(EntityPlayer player, EnumHand hand, ItemStack heldItem, @Nullable Runnable onTransfer)
	{
		if(heldItem.isEmpty()||FluidUtil.getFluidHandler(heldItem)==null)
			return true;
		if(player.world.isRemote&&Tools.fluidContainersBlockInteraction)
			return false;

		boolean transferred = FluidUtil.interactWithFluidHandler(player, hand, this);
		if(transferred&&onTransfer!=null&&!player.world.isRemote)
			onTransfer.run();
		return !transferred&&!Tools.fluidContainersBlockInteraction;
	}

	//--- Setters ---//

	default T withInputFilter(Predicate<FluidStack> inputFilter)
	{
		setInputFilter(inputFilter);
		//noinspection unchecked
		return ((T)this);
	}

	default T withOutputFilter(Predicate<FluidStack> outputFilter)
	{
		setOutputFilter(outputFilter);
		//noinspection unchecked
		return ((T)this);
	}

	void setInputFilter(Predicate<FluidStack> inputFilter);

	void setOutputFilter(Predicate<FluidStack> outputFilter);

	//--- Getters ---//

	default float getFillPercentage()
	{
		int cap = getCapacity();
		if(cap==0)
			return 0;
		return getFluidAmount()/(float)cap;
	}
}
