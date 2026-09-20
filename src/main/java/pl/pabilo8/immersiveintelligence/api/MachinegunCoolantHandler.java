package pl.pabilo8.immersiveintelligence.api;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.HashMap;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.05.2019
 */
public class MachinegunCoolantHandler
{
	static HashMap<Fluid, Float> allowedFluids = new HashMap<>();

	public static boolean isValidCoolant(FluidStack stack)
	{
		return stack!=null&&allowedFluids.containsKey(stack.getFluid());
	}

	public static float getCoolAmount(FluidStack stack)
	{
		if(isValidCoolant(stack))
			return allowedFluids.get(stack.getFluid());
		return Float.MAX_VALUE;
	}

	public static void addCoolant(@Nonnull Fluid fluid, float amount)
	{
		allowedFluids.put(fluid, MathHelper.clamp(amount, 0, Float.MAX_VALUE));
	}

	public static void removeCoolant(@Nonnull Fluid fluid)
	{
		allowedFluids.remove(fluid);
	}
}
