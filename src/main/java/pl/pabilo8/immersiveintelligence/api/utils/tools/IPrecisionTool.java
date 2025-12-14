package pl.pabilo8.immersiveintelligence.api.utils.tools;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 11.12.2025
 * @since 19.08.2019
 */
public interface IPrecisionTool extends IMachineTool
{
	int getWorkTime(String toolName);

	@Nonnull
	ItemStack getToolPresentationStack(String toolName);

	@Nonnull
	ResLoc getToolModelRes(String toolName);
}
