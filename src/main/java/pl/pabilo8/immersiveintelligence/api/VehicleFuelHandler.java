package pl.pabilo8.immersiveintelligence.api;

import net.minecraft.entity.Entity;
import net.minecraftforge.fluids.Fluid;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Pabilo8
 * @since 08.04.2021
 */
public class VehicleFuelHandler
{
	static HashMap<Class<? extends Entity>, Fluid[]> allowedFluids = new HashMap<>();

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
			allowedFluids.put(clazz, fluids);
	}

	public static void removeVehicle(Fluid fluid)
	{
		allowedFluids.remove(fluid);
	}
}
