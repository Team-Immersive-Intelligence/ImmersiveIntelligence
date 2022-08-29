package pl.pabilo8.immersiveintelligence.client.animation;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Supplier;

/**
 * @author Pabilo8
 * @since 26.07.2022
 */
public class AMTFluid extends AMT
{
	private final Supplier<FluidStack> fluid;

	public AMTFluid(String name, Vec3d originPos, Supplier<FluidStack> fluid)
	{
		super(name, originPos);
		this.fluid = fluid;
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{

	}

	@Override
	public void disposeOf()
	{

	}
}
