package pl.pabilo8.immersiveintelligence.common.compat.jei;

import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.inventory.Container;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.common.IIGUI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.03.2021
 */
public class DecoGuiJEIHandler<GUI extends DecoGui<T, C>, T, C extends Container> implements IAdvancedGuiHandler<GUI>
{
	private final Class<GUI> wrappedClass;

	public DecoGuiJEIHandler(IIGUI gui)
	{
		//noinspection unchecked
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

	@Nonnull
	@Override
	public Class<GUI> getGuiContainerClass()
	{
		return wrappedClass;
	}
}
