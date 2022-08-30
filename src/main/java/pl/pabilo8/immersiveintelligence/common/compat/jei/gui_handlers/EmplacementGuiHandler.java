package pl.pabilo8.immersiveintelligence.common.compat.jei.gui_handlers;

import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacement;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 24.03.2021
 */
public class EmplacementGuiHandler implements IAdvancedGuiHandler<GuiEmplacement>
{
	public EmplacementGuiHandler()
	{

	}

	@Override
	public Class<GuiEmplacement> getGuiContainerClass()
	{
		return GuiEmplacement.class;
	}

	/**
	 * Modeled after {@link GuiRecipeBook#render(int, int, float)}
	 */
	@Override
	public List<Rectangle> getGuiExtraAreas(GuiEmplacement guiContainer)
	{
		List<Rectangle> areas = new ArrayList<>();
		areas.add(new Rectangle(guiContainer.getGuiLeft()-28, guiContainer.getGuiTop()+4, 28, 72));
		return areas;
	}
}
