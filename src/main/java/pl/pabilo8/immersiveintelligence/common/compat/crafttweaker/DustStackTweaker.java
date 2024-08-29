package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import blusunrize.immersiveengineering.common.util.compat.crafttweaker.CraftTweakerHelper;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.DustUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * @author Pabilo8
 * @since 05.01.2022
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".Dusts")
@ZenRegister
public class DustStackTweaker
{
	@ZenMethod
	public static void registerDust(IIngredient stack, String name, int color)
	{
		DustUtils.registerDust(CraftTweakerHelper.toIEIngredientStack(stack), name, IIColor.fromPackedRGB(color));
	}

	@ZenMethod
	public static void registerDust(IIngredient stack, String name)
	{
		DustUtils.registerDust(CraftTweakerHelper.toIEIngredientStack(stack), name);
	}
}
