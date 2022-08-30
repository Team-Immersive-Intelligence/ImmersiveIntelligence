package pl.pabilo8.immersiveintelligence.common.compat.jei.gui_handlers;

import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import pl.pabilo8.immersiveintelligence.client.gui.block.GuiDataRedstoneInterfaceData;
import pl.pabilo8.immersiveintelligence.client.gui.block.GuiDataRedstoneInterfaceRedstone;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 24.03.2021
 */
public abstract class RedstoneInterfaceGuiHandler<T extends GuiContainer> implements IAdvancedGuiHandler<T>
{
	public RedstoneInterfaceGuiHandler()
	{

	}

	/**
	 * Modeled after {@link GuiRecipeBook#render(int, int, float)}
	 */
	@Override
	public List<Rectangle> getGuiExtraAreas(T guiContainer)
	{
		List<Rectangle> areas = new ArrayList<>();
		areas.add(new Rectangle(guiContainer.getGuiLeft()-28, guiContainer.getGuiTop()+4, 28, 48));
		return areas;
	}

	public static class Redstone extends RedstoneInterfaceGuiHandler<GuiDataRedstoneInterfaceRedstone>
	{
		@Override
		public Class<GuiDataRedstoneInterfaceRedstone> getGuiContainerClass()
		{
			return GuiDataRedstoneInterfaceRedstone.class;
		}

	}

	public static class Data extends RedstoneInterfaceGuiHandler<GuiDataRedstoneInterfaceData>
	{
		@Override
		public Class<GuiDataRedstoneInterfaceData> getGuiContainerClass()
		{
			return GuiDataRedstoneInterfaceData.class;
		}

	}
}
