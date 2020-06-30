package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.MachinegunCoolantHandler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * @author Pabilo8
 * @since 2019-05-24
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".Machinegun")
@ZenRegister
public class MachinegunTweaker
{

	@ZenMethod
	public static void addCoolant(ILiquidStack fuelEntry, int amountPerUse)
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
			MachinegunCoolantHandler.addCoolant(mcFluid, amountPerUse);
		}
	}

	@ZenMethod
	public static void setCoolAmount(ILiquidStack fuelEntry, int amountPerUse)
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
			MachinegunCoolantHandler.setCoolAmount(mcFluid, amountPerUse);
		}
	}

	@ZenMethod
	public static void removeCoolant(ILiquidStack fuelEntry)
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
			MachinegunCoolantHandler.removeCoolant(mcFluid);
		}
	}


}
