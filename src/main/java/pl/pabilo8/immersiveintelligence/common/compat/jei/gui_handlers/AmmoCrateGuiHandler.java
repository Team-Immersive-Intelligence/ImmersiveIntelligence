package pl.pabilo8.immersiveintelligence.common.compat.jei.gui_handlers;

import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import pl.pabilo8.immersiveintelligence.client.gui.block.GuiAmmunitionCrate;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8
 * @since 24.03.2021
 */
public class AmmoCrateGuiHandler implements IAdvancedGuiHandler<GuiAmmunitionCrate>
{
	public AmmoCrateGuiHandler()
	{

	}

	@Override
	public Class<GuiAmmunitionCrate> getGuiContainerClass()
	{
		return GuiAmmunitionCrate.class;
	}

	/**
	 * Modeled after {@link GuiRecipeBook#render(int, int, float)}
	 */
	@Override
	public List<Rectangle> getGuiExtraAreas(GuiAmmunitionCrate guiContainer)
	{
		if(guiContainer.upgraded)
		{
			List<Rectangle> areas = new ArrayList<>();
			areas.add(new Rectangle(guiContainer.getGuiLeft()+guiContainer.getXSize(), guiContainer.getGuiTop(), 49, 133));
			return areas;
		}
		return Collections.emptyList();
	}
}
