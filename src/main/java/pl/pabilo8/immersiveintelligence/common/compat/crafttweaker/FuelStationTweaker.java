package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.Entity;
import net.minecraftforge.fluids.Fluid;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.VehicleFuelHandler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 2019-05-24
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".FuelStation")
@ZenRegister
public class FuelStationTweaker
{
	@ZenMethod
	public static void addVehicle(String classPath, ILiquidStack[] fluids)
	{
		try
		{
			Fluid[] f = Arrays.stream(fluids).map(l -> CraftTweakerMC.getFluid(l.getDefinition())).toArray(Fluid[]::new);

			Class<?> c = Class.forName(classPath);
			if(c.isAssignableFrom(Entity.class))
				VehicleFuelHandler.addVehicle(((Class<Entity>)c), f);
			CraftTweakerAPI.getLogger().logInfo("The Fuel Station will now recognise "+classPath+" as a supported vehicle");
		}
		catch(ClassNotFoundException e)
		{
			CraftTweakerAPI.getLogger().logError("Couldn't add "+classPath+" as a supported vehicle, class doesn't exist");
		}
	}

}
