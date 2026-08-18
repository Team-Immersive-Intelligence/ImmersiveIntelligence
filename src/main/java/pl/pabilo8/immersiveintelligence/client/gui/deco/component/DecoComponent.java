package pl.pabilo8.immersiveintelligence.client.gui.deco.component;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
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
public abstract class DecoComponent<TYPE extends DecoComponent<? super TYPE>> extends GuiButton
{
	@Nullable
	protected DecoGui<?, ?> parentGui;
	protected List<DecoComponent<?>> children = new ArrayList<>();
	protected int pressTime = 0;
	protected boolean initialized;
	@SuppressWarnings("unused")
	private String displayString;
	private boolean focused;

	private DecoMouseEvent<TYPE> onPressed;
	private DecoMouseEvent<TYPE> onDragged;
	private DecoMouseEvent<TYPE> onReleased;
	private DecoMouseScrollEvent<TYPE> onScroll;
	private DecoMouseEvent<TYPE> onHovered;
	private DecoKeyboardEvent<TYPE> onKeyTyped;
	private Function<TYPE, Collection<String>> onTooltip;
	private Consumer<TYPE> onGuiSave;

	public DecoComponent(int x, int y)
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
		if(!children.isEmpty())
			return children.stream()
					.filter(DecoComponent::isMouseOver)
					.map(DecoComponent::getTooltip)
					.flatMap(Collection::stream)
					.collect(Collectors.toList());
		return new ArrayList<>();
	}

	@Nullable
	public DecoGui<?, ?> getParentGui()
	{
		return parentGui;
	}

	//--- Property Setters ---//

	public <T, C extends Container> void setParentGUI(DecoGui<T, C> parent)
	{
		this.parentGui = parent;
		this.x += parent.guiLeft;
		this.y += parent.guiTop;
		children.forEach(child -> child.setParentGUI(parent));
	}

	public TYPE withPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
		return (TYPE)this;
	}

	public TYPE withSize(int width, int height)
	{
		return (TYPE)this.withWidth(width).withHeight(height);
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

	public TYPE withTemplate(DecoComponentTemplate<TYPE> template)
	{
		return template.apply((TYPE)this);
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
			this.hovered = canBeClicked(mouseX, mouseY);
			if(onHovered!=null)
				onHovered.onMouse((TYPE)this, null, mouseX, mouseY);

			draw(mouseX, mouseY, partialTicks);

			for(DecoComponent<?> child : children)
				child.drawButton(mc, mouseX, mouseY, partialTicks);

			if(pressTime > 0)
				this.pressTime--;
		}
		else
			updateInvisibleTree();
	}

	public final void drawButtonUpperLayer(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if(!initialized||!visible)
		{
			updateInvisibleTree();
			return;
		}

		drawUpperLayer(mouseX, mouseY, partialTicks);

		for(DecoComponent<?> child : children)
			child.drawButtonUpperLayer(mc, mouseX, mouseY, partialTicks);
	}

	protected void updateInvisibleComponent()
	{

	}

	/**
	 * Notifies this component and all descendants that their containing tree is hidden.
	 * This is separate from cleanup: components may retain their state while releasing or
	 * moving external resources that would otherwise remain interactive on screen.
	 */
	private void updateInvisibleTree()
	{
		updateInvisibleComponent();
		for(DecoComponent<?> child : children)
			child.updateInvisibleTree();
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
		return false;
	}


	@Override
	public final void mouseReleased(int mouseX, int mouseY)
	{

	}

	@Override
	public final void mouseDragged(Minecraft mc, int mouseX, int mouseY)
	{

	}

	@Nullable
	public final DecoMouseCapture decoMousePressed(Minecraft mc, int mouseX, int mouseY, MouseButton button)
	{
		if(!visible||!enabled||!canBeClicked(mouseX, mouseY))
			return null;

		for(int i = children.size()-1; i >= 0; i--)
		{
			DecoMouseCapture capture = children.get(i).decoMousePressed(mc, mouseX, mouseY, button);
			if(capture!=null)
				return capture.withAncestor(this);
		}

		DecoMouseCapture capture = decoMousePressedVirtualChild(mc, mouseX, mouseY, button);
		if(capture!=null)
			return capture.withAncestor(this);

		if(onPressed!=null&&onPressed.onMouse((TYPE)this, button, mouseX, mouseY))
		{
			this.pressTime = 10;
			playPressSound(mc.getSoundHandler());
			return DecoMouseCapture.of(this);
		}
		return null;
	}

	@Nullable
	protected DecoMouseCapture decoMousePressedVirtualChild(Minecraft mc, int mouseX, int mouseY, MouseButton button)
	{
		return null;
	}

	protected boolean ownsVirtualChild(DecoComponent<?> component)
	{
		return false;
	}

	private boolean ownsInputChild(DecoComponent<?> component)
	{
		return children.contains(component)||ownsVirtualChild(component);
	}

	private void decoMouseReleased(int mouseX, int mouseY, MouseButton mouseButton)
	{
		if(enabled&&onReleased!=null)
			onReleased.onMouse((TYPE)this, mouseButton, mouseX, mouseY);
		this.pressTime = 0;
	}

	private void decoMouseDragged(Minecraft mc, int mouseX, int mouseY, MouseButton button)
	{
		if(visible&&enabled&&onDragged!=null)
			onDragged.onMouse((TYPE)this, button, mouseX, mouseY);
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

	/**
	 * Called when a key is typed (over a focused component in {@link DecoGui}, implementation dependent)
	 *
	 * @param typedChar The character that was typed
	 * @param keyCode   The key code of the key that was typed
	 * @return Whether the key was handled
	 */
	public final boolean keyTyped(char typedChar, int keyCode)
	{
		if(onKeyTyped!=null&&onKeyTyped.onKeyTyped((TYPE)this, typedChar, keyCode))
			return true;
		for(DecoComponent<?> child : children)
			if(child.keyTyped(typedChar, keyCode))
				return true;
		return false;
	}

	//--- Event Methods ---//

	/**
	 * Called when a GUI event is triggered, override for custom behavior
	 *
	 * @param event The event that was triggered
	 */
	public void onGuiEvent(DecoGuiEvent event)
	{

	}

	/**
	 * Adds an onPressed event handler to the component, triggered when the mouse is pressed on the component
	 *
	 * @param onPressed The event handler
	 * @return this
	 */
	public final TYPE withOnPressed(DecoMouseEvent<TYPE> onPressed)
	{
		this.onPressed = onPressed;
		return (TYPE)this;
	}

	/**
	 * An extension of {@link #withOnPressed(DecoMouseEvent)} that works only with the left mouse button.
	 *
	 * @param onPressed The event handler
	 * @return this
	 */
	public final TYPE withOnLMBPressed(Runnable onPressed)
	{
		this.onPressed = (gui, mouseButton, mouseX, mouseY) ->
		{
			if(mouseButton==MouseButton.LEFT)
			{
				onPressed.run();
				return true;
			}
			return false;
		};
		return (TYPE)this;
	}

	/**
	 * Adds an onReleased event handler to the component, triggered when the mouse is released on the component
	 *
	 * @param onReleased The event handler
	 * @return this
	 */
	public final TYPE withOnReleased(DecoMouseEvent<TYPE> onReleased)
	{
		this.onReleased = onReleased;
		return (TYPE)this;
	}

	/**
	 * Adds an onDragged event handler to the component, triggered when the mouse is dragged over the component
	 *
	 * @param onDragged The event handler
	 * @return this
	 */
	public final TYPE withOnDragged(DecoMouseEvent<TYPE> onDragged)
	{
		this.onDragged = onDragged;
		return (TYPE)this;
	}

	/**
	 * Adds an onHovered event handler to the component, triggered when the mouse is hovered over the component
	 *
	 * @param onHovered The event handler
	 * @return this
	 */
	public final TYPE withOnHovered(DecoMouseEvent<TYPE> onHovered)
	{
		this.onHovered = onHovered;
		return (TYPE)this;
	}

	/**
	 * Adds an onKeyTyped event handler to the component,
	 * triggered when a keyboard key is typed while the component is focused
	 *
	 * @param onKeyTyped The event handler
	 * @return this
	 */
	public final TYPE withOnKeyTyped(DecoKeyboardEvent<TYPE> onKeyTyped)
	{
		this.onKeyTyped = onKeyTyped;
		return (TYPE)this;
	}

	/**
	 * Adds an onScroll event handler to the component, triggered when the mouse is scrolled while the component is hovered
	 *
	 * @param onScroll The event handler
	 * @return this
	 */
	public final TYPE withOnScroll(DecoMouseScrollEvent<TYPE> onScroll)
	{
		this.onScroll = onScroll;
		return (TYPE)this;
	}

	/**
	 * Adds an onTooltip event handler to the component, triggered when the mouse is hovered over the component
	 *
	 * @param onTooltip The tooltip
	 * @return this
	 */
	public final TYPE withOnTooltip(Function<TYPE, Collection<String>> onTooltip)
	{
		this.onTooltip = onTooltip;
		return (TYPE)this;
	}

	/**
	 * Adds a tooltip to the component to be displayed when hovered
	 *
	 * @param tooltip The tooltip
	 * @return this
	 */
	public final TYPE withTranslatedTooltip(String... tooltip)
	{
		final List<String> collect = Arrays.stream(tooltip)
				.filter(Objects::nonNull)
				.map(s -> {
					String text = TextFormatting.getTextWithoutFormattingCodes(s);
					assert text!=null;
					String translated = I18n.format(text);
					return s.replace(text, translated);
				})
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toList());

		this.onTooltip = (component) -> collect;
		return (TYPE)this;
	}

	public final TYPE withGuiSaveAction(Consumer<TYPE> onGuiSave)
	{
		this.onGuiSave = onGuiSave;
		return (TYPE)this;
	}

	public void onGuiSave()
	{
		if(onGuiSave!=null)
			onGuiSave.accept(((TYPE)this));
	}

	public TYPE withDisabled(boolean disabled)
	{
		this.enabled = !disabled;
		return (TYPE)this;
	}

	/**
	 * Changes both visibility and interaction state. Hiding a component immediately
	 * propagates to descendants so external resources, such as real container slots,
	 * cannot remain active until the next render pass.
	 */
	public final void setActive(boolean active)
	{
		this.visible = active;
		this.enabled = active;
		if(!active)
			updateInvisibleTree();
	}

	/**
	 * Provides an ingredient that can be used by JEI compat.
	 *
	 * @return an ingredient, like an {@link net.minecraft.item.ItemStack},
	 * a {@link net.minecraftforge.fluids.FluidStack}, or an {@link net.minecraft.item.crafting.Ingredient} associated with this component
	 */
	@Nullable
	public Object getProvidedIngredient()
	{
		return null;
	}

	public boolean isFocused()
	{
		return focused;
	}

	public void setFocused(boolean focused)
	{
		this.focused = focused;
	}

	/**
	 * Called when the mouse is scrolled over the component
	 *
	 * @param mouseX   The x position of the mouse
	 * @param mouseY   The y position of the mouse
	 * @param scrolled The amount the mouse was scrolled
	 * @return Whether the scroll was handled
	 */
	public final boolean onComponentScroll(int mouseX, int mouseY, float scrolled)
	{
		return visible&&enabled&&onScroll!=null&&canBeClicked(mouseX, mouseY)
				&&onScroll.onMouse((TYPE)this, (int)scrolled, mouseX, mouseY);
	}

	/**
	 * Checks whether a component belongs to this ordinary child tree.
	 */
	public final boolean containsComponent(DecoComponent<?> component)
	{
		if(this==component)
			return true;
		for(DecoComponent<?> child : children)
			if(child.containsComponent(component))
				return true;
		return false;
	}

	/**
	 * Immutable pointer context produced by mouse hit-testing.
	 * Besides the final target, it stores the coordinate transform used by virtual
	 * children such as cached list entry panels and the ancestor path used to
	 * invalidate focus when a containing panel disappears.
	 */
	public static final class DecoMouseCapture
	{
		private final DecoComponent<?> component;
		private final List<DecoComponent<?>> path;
		private final int offsetX, offsetY;

		private DecoMouseCapture(DecoComponent<?> component, List<DecoComponent<?>> path, int offsetX, int offsetY)
		{
			this.component = component;
			this.path = path;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
		}

		public static DecoMouseCapture of(DecoComponent<?> component)
		{
			return new DecoMouseCapture(component, Collections.singletonList(component), 0, 0);
		}

		private DecoMouseCapture withAncestor(DecoComponent<?> ancestor)
		{
			if(path.contains(ancestor))
				return this;
			List<DecoComponent<?>> expanded = new ArrayList<>(path);
			expanded.add(ancestor);
			return new DecoMouseCapture(component, expanded, offsetX, offsetY);
		}

		public DecoMouseCapture translated(int offsetX, int offsetY)
		{
			return new DecoMouseCapture(component, path, this.offsetX+offsetX, this.offsetY+offsetY);
		}

		public DecoComponent<?> getComponent()
		{
			return component;
		}

		public boolean isValid()
		{
			for(int i = 0; i < path.size(); i++)
			{
				DecoComponent<?> element = path.get(i);
				if(!element.visible||!element.enabled)
					return false;

				//A capture is stale when a rebuilt panel no longer owns the child that
				//originally produced it, even if the detached objects remain enabled.
				if(i+1 < path.size()&&!path.get(i+1).ownsInputChild(element))
					return false;
			}
			return true;
		}

		public boolean belongsTo(DecoComponent<?> root)
		{
			return path.contains(root)||root.containsComponent(component);
		}

		@Nullable
		public DecoMouseCapture press(Minecraft mc, int mouseX, int mouseY, MouseButton button)
		{
			if(!isValid())
				return null;

			DecoMouseCapture result = component.decoMousePressed(mc, mouseX+offsetX, mouseY+offsetY, button);
			if(result==null)
				return null;

			result = result.translated(offsetX, offsetY);
			for(int i = 1; i < path.size(); i++)
				result = result.withAncestor(path.get(i));
			return result;
		}

		public void release(int mouseX, int mouseY, MouseButton button)
		{
			component.decoMouseReleased(mouseX+offsetX, mouseY+offsetY, button);
		}

		public void drag(Minecraft mc, int mouseX, int mouseY, MouseButton button)
		{
			component.decoMouseDragged(mc, mouseX+offsetX, mouseY+offsetY, button);
		}

		public boolean scroll(int mouseX, int mouseY, float amount)
		{
			return isValid()&&component.onComponentScroll(mouseX+offsetX, mouseY+offsetY, amount);
		}

		public List<String> getTooltip()
		{
			return isValid()&&component.isMouseOver()?component.getTooltip(): Collections.emptyList();
		}
	}

	/**
	 * A set of GUI events that can be triggered on any {@link DecoComponent} by a {@link DecoGui}
	 */
	public enum DecoGuiEvent
	{
		COPY,
		PASTE,
		CUT,
		UNDO,
		REDO,
		SELECT_ALL,
		JEI_RECIPE,
		JEI_USES,
		JEI_BOOKMARK
	}

	public enum MouseButton
	{
		LEFT,
		RIGHT,
		MIDDLE,
		FORWARD,
		BACKWARD
	}

	@FunctionalInterface
	public interface DecoMouseEvent<TYPE extends DecoComponent<? super TYPE>>
	{
		boolean onMouse(TYPE gui, MouseButton button, int mouseX, int mouseY);
	}

	@FunctionalInterface
	public interface DecoMouseScrollEvent<TYPE extends DecoComponent<? super TYPE>>
	{
		boolean onMouse(TYPE gui, int mouseScroll, int mouseX, int mouseY);
	}

	@FunctionalInterface
	public interface DecoKeyboardEvent<TYPE extends DecoComponent<? super TYPE>>
	{
		boolean onKeyTyped(TYPE gui, char typedChar, int keyCode);
	}

	@FunctionalInterface
	public interface DecoComponentTemplate<TYPE extends DecoComponent<? super TYPE>>
	{
		TYPE apply(TYPE component);

		default DecoComponentTemplate<TYPE> and(DecoComponentTemplate<TYPE> base)
		{
			return (TYPE component) -> base.apply(apply(component));
		}
	}
}
