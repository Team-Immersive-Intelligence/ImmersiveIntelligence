package pl.pabilo8.immersiveintelligence.api.utils.tools;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 11.12.2025
 * @since 19.08.2019
 */
public interface ISawblade extends IMachineTool
{
	int getHardness(ItemStack stack);

	@Nonnull
	ItemStack getToolPresentationStack(String toolName);

	ResourceLocation getSawbladeTexture(ItemStack stack);
}
