package pl.pabilo8.immersiveintelligence.api;

import net.minecraft.entity.Entity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 08.04.2021
 */
public class VehicleFuelHandler
{
	private static HashMap<Class<? extends Entity>, Fluid[]> allowedFluids = new HashMap<>();
	private static ArrayList<Fluid> allowedFluidsList = new ArrayList<>();

	public static boolean isValidFluid(FluidStack fluidStack)
	{
		return allowedFluidsList.contains(fluidStack.getFluid());
	}

	public static boolean isValidFluid(Fluid fluid)
	{
		return allowedFluidsList.contains(fluid);
	}

	public static boolean isValidVehicle(Entity e)
	{
		return allowedFluids.keySet().stream().anyMatch(aClass -> aClass.isInstance(e));
	}

	public static boolean isFuelValidForVehicle(Entity e, Fluid f)
	{
		if(!allowedFluids.containsKey(e.getClass()))
			return false;
		return Arrays.stream(allowedFluids.get(e.getClass())).anyMatch(fluid -> fluid==f);
	}

	public static void addVehicle(Class<? extends Entity> clazz, Fluid... fluids)
	{
		if(!allowedFluids.containsKey(clazz))
		{
			allowedFluids.put(clazz, fluids);
			allowedFluidsList.addAll(Arrays.asList(fluids));
		}
	}
}
