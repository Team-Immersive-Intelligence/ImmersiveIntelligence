package pl.pabilo8.immersiveintelligence.client.gui.deco.component;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Base of all Deco GUI components
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 07.01.2025
 **/
@SuppressWarnings("unchecked")
public abstract class GuiComponentDecoBase<TYPE extends GuiComponentDecoBase<? super TYPE>> extends GuiButton
{
	@Nullable
	protected DecoGui<?, ?> parentGui;
	@Nullable
	protected GuiComponentDecoBase<?> parentComponent;

	@SuppressWarnings("unused")
	private String displayString;

	protected List<GuiComponentDecoBase<?>> children = new ArrayList<>();
	protected boolean pressed;
	protected boolean initialized;

	private DecoMouseEvent<TYPE> onPressed;
	private DecoMouseEvent<TYPE> onReleased;
	private DecoMouseScrollEvent<TYPE> onScroll;
	private DecoMouseEvent<TYPE> onHovered;
	private DecoKeyboardEvent<TYPE> onKeyTyped;
	private Function<TYPE, Collection<String>> onTooltip;

	public GuiComponentDecoBase(int x, int y)
	{
		super(-1, x, y, 20, 20, "");
		this.parentGui = null;
		this.enabled = true;
		this.visible = true;
		this.hovered = false;
	}

	protected abstract boolean initialize();

	protected abstract void draw(int mouseX, int mouseY, float partialTicks);

	public abstract void cleanup();

	public List<String> getTooltip()
	{
		if(onTooltip!=null)
			return new ArrayList<>(onTooltip.apply((TYPE)this));
		return new ArrayList<>();
	}

	@Nullable
	public DecoGui<?, ?> getParentGui()
	{
		return parentGui;
	}

	//--- Property Setters ---//

	public <T extends TileEntityIEBase & IIEInventory, C extends ContainerIIBase<T>> void setParentGUI(DecoGui<T, C> parent)
	{
		this.parentGui = parent;
		this.x += parent.guiLeft;
		this.y += parent.guiTop;
	}

	public TYPE withChild(GuiComponentDecoBase<?> child)
	{
		children.add(child);
		child.parentComponent = this;
		return (TYPE)this;
	}

	public TYPE withSize(int width, int height)
	{
		this.width = width;
		this.height = height;
		return (TYPE)this;
	}

	public TYPE withWidth(int width)
	{
		this.width = width;
		return (TYPE)this;
	}

	public TYPE withHeight(int height)
	{
		this.height = height;
		return (TYPE)this;
	}

	//--- Draw Methods ---//

	@Override
	public final void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if(!initialized)
		{
			initialized = initialize();
			if(!initialized)
				return;
		}

		if(this.visible)
		{
			this.hovered = IIMath.isPointInRectangle(x, y, x+width, y+height, mouseX, mouseY);
			if(onHovered!=null)
				onHovered.onMouse((TYPE)this, mouseX, mouseY);

			draw(mouseX, mouseY, partialTicks);

			for(GuiComponentDecoBase<?> child : children)
				child.drawButton(mc, mouseX, mouseY, partialTicks);
		}
	}

	public final void drawButtonUpperLayer(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if(!initialized||!visible)
			return;

		drawUpperLayer(mouseX, mouseY, partialTicks);

		for(GuiComponentDecoBase<?> child : children)
			child.drawButtonUpperLayer(mc, mouseX, mouseY, partialTicks);
	}

	public void drawUpperLayer(int mouseX, int mouseY, float partialTicks)
	{

	}

	protected void bindAtlas()
	{
		ClientUtils.bindAtlas();
	}

	@Override
	public final boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		if(this.enabled&&canBeClicked(mouseX, mouseY))
		{
			pressed = onPressed==null||onPressed.onMouse((TYPE)this, mouseX, mouseY);
			if(pressed)
				playPressSound(mc.getSoundHandler());
			if(parentGui!=null)
				parentGui.requestFocus(this);
			return pressed;
		}
		return false;
	}

	@Override
	public final void mouseReleased(int mouseX, int mouseY)
	{
		if(this.enabled&&canBeClicked(mouseX, mouseY))
			pressed = !(onReleased==null||onReleased.onMouse((TYPE)this, mouseX, mouseY));
	}

	/**
	 * Separate from checking the hover state, as some components like checkboxes can't be clicked on their text part
	 *
	 * @param mouseX The x position of the mouse
	 * @param mouseY The y position of the mouse
	 * @return Whether the component can be clicked
	 */
	protected boolean canBeClicked(int mouseX, int mouseY)
	{
		return IIMath.isPointInRectangle(x, y, x+width, y+height, mouseX, mouseY);
	}

	@Override
	public final boolean isMouseOver()
	{
		return visible&&hovered;
	}

	@Override
	@Deprecated
	protected final int getHoverState(boolean mouseOver)
	{
		return super.getHoverState(mouseOver);
	}

	//--- Input Methods ---//

	public final boolean keyTyped(char typedChar, int keyCode)
	{
		if(onKeyTyped!=null&&onKeyTyped.onKeyTyped((TYPE)this, typedChar, keyCode))
			return true;
		for(GuiComponentDecoBase<?> child : children)
			if(child.keyTyped(typedChar, keyCode))
				return true;
		return true;
	}

	/**
	 * Adds
	 *
	 * @param onPressed
	 * @return
	 */
	public TYPE withOnPressed(DecoMouseEvent<TYPE> onPressed)
	{
		this.onPressed = onPressed;
		return (TYPE)this;
	}

	public TYPE withOnReleased(DecoMouseEvent<TYPE> onReleased)
	{
		this.onReleased = onReleased;
		return (TYPE)this;
	}

	public TYPE withOnHovered(DecoMouseEvent<TYPE> onHovered)
	{
		this.onHovered = onHovered;
		return (TYPE)this;
	}

	public TYPE withOnKeyTyped(DecoKeyboardEvent<TYPE> onKeyTyped)
	{
		this.onKeyTyped = onKeyTyped;
		return (TYPE)this;
	}

	public TYPE withOnScroll(DecoMouseScrollEvent<TYPE> onScroll)
	{
		this.onScroll = onScroll;
		return (TYPE)this;
	}

	public TYPE withOnTooltip(Function<TYPE, Collection<String>> onTooltip)
	{
		this.onTooltip = onTooltip;
		return (TYPE)this;
	}

	public TYPE withTranslatedTooltip(String... tooltip)
	{
		final List<String> collect = Arrays.stream(tooltip)
				.map(I18n::format)
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toList());

		this.onTooltip = (component) -> collect;
		return (TYPE)this;
	}

	public boolean onComponentScroll(int mouseX, int mouseY, float scrolled)
	{
		return onScroll==null||(canBeClicked(mouseX, mouseY)&&onScroll.onMouse((TYPE)this, (int)scrolled, mouseX, mouseY));
	}

	@FunctionalInterface
	public interface DecoMouseEvent<TYPE extends GuiComponentDecoBase<? super TYPE>>
	{
		boolean onMouse(TYPE gui, int mouseX, int mouseY);
	}

	@FunctionalInterface
	public interface DecoMouseScrollEvent<TYPE extends GuiComponentDecoBase<? super TYPE>>
	{
		boolean onMouse(TYPE gui, int mouseScroll, int mouseX, int mouseY);
	}

	@FunctionalInterface
	public interface DecoKeyboardEvent<TYPE extends GuiComponentDecoBase<? super TYPE>>
	{
		boolean onKeyTyped(TYPE gui, char typedChar, int keyCode);
	}
}