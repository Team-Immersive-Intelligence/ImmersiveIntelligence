package pl.pabilo8.immersiveintelligence.common.compat.jei.gui_handlers;

import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import pl.pabilo8.immersiveintelligence.client.gui.block.GuiVulcanizer;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8
 * @since 24.03.2021
 */
public class VulcanizerGuiHandler implements IAdvancedGuiHandler<GuiVulcanizer>
{
	public VulcanizerGuiHandler()
	{

	}

	@Override
	public Class<GuiVulcanizer> getGuiContainerClass()
	{
		return GuiVulcanizer.class;
	}

	/**
	 * Modeled after {@link GuiRecipeBook#render(int, int, float)}
	 */
	@Override
	public List<Rectangle> getGuiExtraAreas(GuiVulcanizer guiContainer)
	{
		return Collections.emptyList();
	}

	@Nullable
	@Override
	public Object getIngredientUnderMouse(GuiVulcanizer guiContainer, int mouseX, int mouseY)
	{
		return guiContainer.getPreviewedItem(mouseX,mouseY);
	}
}
