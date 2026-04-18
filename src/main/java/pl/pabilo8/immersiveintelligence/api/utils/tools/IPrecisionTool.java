package pl.pabilo8.immersiveintelligence.api.utils.tools;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecisionAssemblerRecipe.PrecisionToolInfo;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 11.12.2025
 * @since 19.08.2019
 */
public interface IPrecisionTool extends IMachineTool
{
	PrecisionToolInfo getInfo(ItemStack toolStack);
}
