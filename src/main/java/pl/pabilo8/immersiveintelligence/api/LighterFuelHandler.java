package pl.pabilo8.immersiveintelligence.api;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.05.2019
 */
public class LighterFuelHandler
{

	static HashMap<Fluid, Integer> allowedFluids = new HashMap<>();

	public static boolean isValidFuel(FluidStack stack)
	{
		return stack!=null&&allowedFluids.containsKey(stack.getFluid());
	}

	public static int getBurnQuantity(FluidStack stack)
	{
		if(isValidFuel(stack))
			return allowedFluids.get(stack.getFluid());
		return Integer.MAX_VALUE;
	}

	public static void setBurnQuantity(Fluid fluid, int amount)
	{
		if(allowedFluids.containsKey(fluid))
			allowedFluids.replace(fluid, amount);
	}

	public static void addFuel(Fluid fluid, int amount)
	{
		if(!allowedFluids.containsKey(fluid))
			allowedFluids.put(fluid, amount);
	}

	public static void removeFuel(Fluid fluid)
	{
		allowedFluids.remove(fluid);
	}

	public static Fluid[] getAllowedFluids()
	{
		return allowedFluids.keySet().toArray(new Fluid[0]);
	}
}
