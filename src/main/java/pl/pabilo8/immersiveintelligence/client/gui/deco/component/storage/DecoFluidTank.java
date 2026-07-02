package pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.inventory.MultiFluidTank;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Displays a fluid tank in the Deco GUI system.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 12.01.2025
 **/
public class DecoFluidTank extends DecoTankBase<DecoFluidTank, FluidStack>
{
	@Nullable
	protected IFluidTank fluidTank;

	public DecoFluidTank(int x, int y)
	{
		super(x, y);
		this.width = 24;
		this.height = 64;
	}

	public DecoFluidTank withFluidTank(IFluidTank fluidTank)
	{
		this.fluidTank = fluidTank;
		cleanup();
		return this;
	}

	@Override
	protected int getResourceCapacity()
	{
		return fluidTank!=null?fluidTank.getCapacity(): 0;
	}

	@Nullable
	@Override
	protected List<FluidStack> getContents()
	{
		if(fluidTank!=null)
		{
			if(fluidTank instanceof MultiFluidTank)
				return ((MultiFluidTank)fluidTank).fluids;
			if(fluidTank.getFluid()!=null&&fluidTank.getFluid().amount > 0)
				return Collections.singletonList(fluidTank.getFluid());
		}
		return null;
	}

	@Override
	public int getResourceAmount(@Nonnull FluidStack fluidStack)
	{
		return fluidStack.amount;
	}

	@Nonnull
	@Override
	protected IIColor getResourceColor(FluidStack fluidStack)
	{
		return IIColor.fromPackedARGB(fluidStack.getFluid().getColor(fluidStack));
	}

	@Override
	public ResourceLocation getResourceTexture(@Nonnull FluidStack fluidStack)
	{
		return fluidStack.getFluid().getStill(fluidStack);
	}

	@Override
	protected void addResourceTooltip(List<String> tooltip, FluidStack fluidStack, int tankCapacity)
	{
		ClientUtils.addFluidTooltip(fluidStack, tooltip, tankCapacity);
	}
}
