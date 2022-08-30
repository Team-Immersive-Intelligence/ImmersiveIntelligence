package pl.pabilo8.immersiveintelligence.api;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;

/**
 * @author Pabilo8
 * @since 2019-05-24
 */
public class MachinegunCoolantHandler
{
	static HashMap<Fluid, Integer> allowedFluids = new HashMap<>();

	public static boolean isValidCoolant(FluidStack stack)
	{
		return stack!=null&&allowedFluids.containsKey(stack.getFluid());
	}

	public static int getCoolAmount(FluidStack stack)
	{
		if(isValidCoolant(stack))
			return allowedFluids.get(stack.getFluid());
		return Integer.MAX_VALUE;
	}

	public static void setCoolAmount(Fluid fluid, int amount)
	{
		if(allowedFluids.containsKey(fluid))
			allowedFluids.replace(fluid, amount);
	}

	public static void addCoolant(Fluid fluid, int amount)
	{
		if(!allowedFluids.containsKey(fluid))
			allowedFluids.put(fluid, amount);
	}

	public static void removeCoolant(Fluid fluid)
	{
		allowedFluids.remove(fluid);
	}
}
