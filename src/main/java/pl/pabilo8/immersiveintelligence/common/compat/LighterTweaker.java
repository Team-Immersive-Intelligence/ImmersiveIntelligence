package pl.pabilo8.immersiveintelligence.common.compat;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.LighterFuelHandler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * Created by Pabilo8 on 2019-05-24.
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".Lighter")
@ZenRegister
public class LighterTweaker
{

	@ZenMethod
	public static void addFuel(ILiquidStack fuelEntry, int amountPerUse)
	{
		Fluid mcFluid;
		FluidStack mcFluidStack;
		if(fuelEntry==null)
		{
			CraftTweakerAPI.logError("Found null FluidStack in fuel entry");
		}
		else
		{
			mcFluidStack = CraftTweakerMC.getLiquidStack(fuelEntry);
			mcFluid = mcFluidStack.getFluid();
			LighterFuelHandler.addFuel(mcFluid, amountPerUse);
		}
	}

	@ZenMethod
	public static void setBurnQuantity(ILiquidStack fuelEntry, int amountPerUse)
	{
		Fluid mcFluid;
		FluidStack mcFluidStack;
		if(fuelEntry==null)
		{
			CraftTweakerAPI.logError("Found null FluidStack in fuel entry");
		}
		else
		{
			mcFluidStack = CraftTweakerMC.getLiquidStack(fuelEntry);
			mcFluid = mcFluidStack.getFluid();
			LighterFuelHandler.setBurnQuantity(mcFluid, amountPerUse);
		}
	}

	@ZenMethod
	public static void removeFuel(ILiquidStack fuelEntry)
	{
		Fluid mcFluid;
		FluidStack mcFluidStack;
		if(fuelEntry==null)
		{
			CraftTweakerAPI.logError("Found null FluidStack in fuel entry");
		}
		else
		{
			mcFluidStack = CraftTweakerMC.getLiquidStack(fuelEntry);
			mcFluid = mcFluidStack.getFluid();
			LighterFuelHandler.removeFuel(mcFluid);
		}
	}


}
