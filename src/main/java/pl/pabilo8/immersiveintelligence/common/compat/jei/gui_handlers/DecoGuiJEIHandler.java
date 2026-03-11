package pl.pabilo8.immersiveintelligence.common.compat.jei.gui_handlers;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.03.2021
 */
public class DecoGuiJEIHandler<GUI extends DecoGui<T, C>, T extends TileEntityIEBase & IIEInventory, C extends ContainerIIBase<T>> implements IAdvancedGuiHandler<GUI>
{
	Class<GUI> wrappedClass;

	public DecoGuiJEIHandler(IIGUI gui)
	{
		this.wrappedClass = (Class<GUI>)gui.guiClass;
	}

	@Nullable
	@Override
	public List<Rectangle> getGuiExtraAreas(GUI guiContainer)
	{
		return guiContainer.getTakenSpace();
	}

	@Nullable
	@Override
	public Object getIngredientUnderMouse(GUI guiContainer, int mouseX, int mouseY)
	{
		return guiContainer.getIngredientUnderMouse();
	}

	@Override
	public Class<GUI> getGuiContainerClass()
	{
		return wrappedClass;
	}
}
