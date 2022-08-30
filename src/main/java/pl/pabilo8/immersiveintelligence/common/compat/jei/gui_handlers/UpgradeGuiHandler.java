package pl.pabilo8.immersiveintelligence.common.compat.jei.gui_handlers;

import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import pl.pabilo8.immersiveintelligence.client.gui.block.GuiUpgrade;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8
 * @since 24.03.2021
 */
public class UpgradeGuiHandler implements IAdvancedGuiHandler<GuiUpgrade>
{
	public UpgradeGuiHandler()
	{

	}

	@Override
	public Class<GuiUpgrade> getGuiContainerClass()
	{
		return GuiUpgrade.class;
	}

	/**
	 * Modeled after {@link GuiRecipeBook#render(int, int, float)}
	 */
	@Override
	public java.util.List<Rectangle> getGuiExtraAreas(GuiUpgrade guiContainer)
	{
		if(guiContainer.info)
		{
			List<Rectangle> areas = new ArrayList<>();
			areas.add(new Rectangle(guiContainer.getGuiLeft()+guiContainer.getXSize(), guiContainer.getGuiTop()+4, 80, 128));
			return areas;
		}
		return Collections.emptyList();
	}

	@Nullable
	@Override
	public Object getIngredientUnderMouse(GuiUpgrade guiContainer, int mouseX, int mouseY)
	{
		return guiContainer.getPreviewedItem(mouseX,mouseY);
	}
}
