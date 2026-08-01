package pl.pabilo8.immersiveintelligence.client.gui.deco;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional.Method;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent.DecoGuiEvent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent.MouseButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.widget.DecoComponentWidgetBase;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.widget.DecoManualWidget;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageGuiNBT;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Common base for Deco GUIs.
 * <p>
 * This class owns the reusable GUI mechanics: component lifecycle, widgets, labels, tooltips,
 * value listeners, background drawing, JEI taken-space data and GUI screenshot export.
 * Context-specific behaviour is supplied by subclasses such as {@link DecoTileGui},
 * {@link DecoEntityGui} and {@link DecoItemGui}.
 * </p>
 * Use annotation {@link DecoTemplate} to specify traits.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.01.2025
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class DecoGui<T, C extends Container> extends GuiContainer
{
	private static final int MAX_WIDGET_TIME = 40;
	private static final String NBT_GUI_NAME = "decoGui";

	//Gui basics
	protected final String name;
	protected final T context;
	protected final C container;
	protected final DecoGuiCategory category;
	protected final InventoryPlayer playerContainer;
	protected final IIGUI gui;

	//Components
	protected final List<DecoTab> tabList = new ArrayList<>();
	protected final List<DecoTab> widgetTabList = new ArrayList<>();
	private final List<DecoComponentWidgetBase<?>> widgetList = new ArrayList<>();
	private final List<ValueListener<?>> valueListeners = new ArrayList<>();
	/**
	 * if true, the GUI won't perform default closing cleanup during {@link #onGuiClosed()}, used for transitions between GUIs
	 */
	protected boolean changeGUIFlag = false, refreshGUIFlag = false;
	//Background
	private DecoBackgroundBuilder backgroundBuilder;
	private List<Rectangle> takenSpace;
	private DecoComponent<?> focusedElement;
	private DecoComponent<?> hoveredElement;
	//Widgets
	private DecoComponentWidgetBase<?> previousWidget, currentWidget;
	private int widgetTime = 0;
	/**
	 * Screenshot mode, changes the GL scissor method
	 */
	private boolean screenshotMode = false;

	protected DecoGui(EntityPlayer player, @Nullable C container, @Nullable T context, IIGUI iigui)
	{
		//The player can be null ONLY for the GUI's resource annotation loading
		//In a normal scenario the player is never null
		super(container);
		this.gui = iigui;
		this.context = context;
		this.container = container;
		this.playerContainer = player==null?null: player.inventory;

		//Get meta data from annotation
		DecoTemplate annotation = this.getClass().getAnnotation(DecoTemplate.class);
		if(annotation!=null)
		{
			name = annotation.name();
			category = annotation.category();
		}
		else
		{
			name = "deco";
			category = DecoGuiCategory.GENERIC_TILE;
		}
	}

	@Override
	public final void initGui()
	{
		//Sync GUI NBT
		EasyNBT nbt = this.loadGuiData();

		//Clean GUI
		this.buttonList.clear();
		this.labelList.clear();
		this.tabList.clear();
		this.widgetTabList.clear();
		this.widgetList.clear();
		this.focusedElement = null;
		this.hoveredElement = null;
		this.previousWidget = null;
		this.currentWidget = null;
		this.valueListeners.clear();

		this.fontRenderer = IIClientUtils.fontRegular;
		//JEI Compatibility
		this.takenSpace = null;

		//Fire initialization event for the extending Deco GUI class
		onInit();
		if(category!=null)
			onInitStandardAddons();

		//Load the last widget
		nbt.checkSetString("currentWidget", s -> {
			for(DecoComponentWidgetBase<?> widget : widgetList)
				if(widget.getName().equals(s))
				{
					setCurrentWidget(widget);
					widgetTime = 0;
					for(DecoTab decoTab : widgetTabList)
						decoTab.x += widget.getWidgetWidth();
					return;
				}
		});

		//Build background and generate gui size
		if(backgroundBuilder!=null)
		{
			backgroundBuilder.build();
			//Add title labels, if specified
			Arrays.stream(backgroundBuilder.getTitleLabel()).forEach(this::addLabel);
		}
		else
			onNoBackgroundBuilder();

		//Resize using vanilla method
		super.initGui();

		if(Loader.isModLoaded("jei"))
			onInitJEICompat();

		//Apply label and component position corrections
		for(GuiLabel guiLabel : labelList)
		{
			guiLabel.x += guiLeft;
			guiLabel.y += guiTop;
		}
		for(GuiButton button : buttonList)
			if(button instanceof DecoComponent)
				((DecoComponent<?>)button).setParentGUI(this);
		for(DecoComponentWidgetBase<?> widget : widgetList)
			widget.setParentGUI(this);
	}

	/**
	 * Called upon Deco GUI initialization.
	 */
	public abstract void onInit();

	/**
	 * Called after {@link #onInit()} for standard Deco GUI addons initialization.
	 */
	protected void onInitStandardAddons()
	{

	}

	/**
	 * Called when no {@link DecoBackgroundBuilder} was supplied.
	 */
	protected void onNoBackgroundBuilder()
	{
		xSize = ySize = 64;
	}

	/**
	 * Called after {@link #onInit()} for GUI JEI compatibility initialization.
	 * Overriding methods should be annotated with @{@link Method} to not cause a crash if JEI is not present.
	 */
	public void onInitJEICompat()
	{

	}

	//--- GUI Component Methods ---//

	/**
	 * @deprecated use {@link #addComponent(DecoComponent)} instead
	 */
	@Override
	@Deprecated
	protected final <B extends GuiButton> B addButton(B button)
	{
		return super.addButton(button);
	}

	/**
	 * Adds a {@link DecoComponent} to the GUI and returns it
	 *
	 * @param component The component to add
	 * @param <B>       The type of the component
	 * @return The added component
	 */
	protected final <B extends DecoComponent<B>> B addComponent(B component)
	{
		buttonList.add(component);
		component.id = buttonList.size();

		//Special handling for tabs – they should be placed in a separate list and aligned to the left side of GUI
		if(component instanceof DecoTab)
		{
			DecoTab tab = (DecoTab)component;
			tab.withSize(28, 24);
			tab.x = -28;
			tab.y = 5+tabList.stream().mapToInt(d -> d.height).sum();
			tabList.add(tab);
		}

		return component;
	}

	protected final DecoTab addLinkTab(IIGUI gui, ResourceLocation tabIcon, String moduleName)
	{
		return (DecoTab)addComponent(new DecoTab()
				.withLink(gui)
				.withIcon(tabIcon)
				.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+moduleName)
		);
	}

	protected final DecoTab addLinkTab(IIGUI gui, ItemStack tabIcon, String moduleName)
	{
		return (DecoTab)addComponent(new DecoTab()
				.withLink(gui)
				.withIcon(tabIcon)
				.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+moduleName)
		);
	}

	/**
	 * Adds a {@link DecoComponentWidgetBase} to the GUI and returns it
	 *
	 * @param widget the widget to add
	 * @param <W>    the type of the widget
	 * @return the added widget
	 */
	protected final <W extends DecoComponentWidgetBase<W>> W addWidget(W widget)
	{
		//Add the widget tab
		DecoTab tab = widget.provideTab();
		buttonList.add(0, tab);
		tab.id = buttonList.size();
		tab.withSize(32, 18);
		tab.withOnPressed((gui, mouseButton, mouseX, mouseY) -> setCurrentWidget(widget));
		tab.x = xSize;
		tab.y = ySize/2-10-widgetTabList.stream().mapToInt(d -> d.height).sum();
		widgetTabList.add(tab);

		//Add the widget itself
		widget.x = xSize;
		widget.y = 0;
		widgetList.add(widget);
		return widget;
	}

	private boolean setCurrentWidget(DecoComponentWidgetBase<?> widget)
	{
		previousWidget = currentWidget;
		currentWidget = currentWidget==widget?null: widget;
		widgetTime = MAX_WIDGET_TIME;
		return true;
	}

	/**
	 * Adds multiple components to the GUI
	 *
	 * @param components The components to add
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	protected final void addComponents(DecoComponent... components)
	{
		for(DecoComponent component : components)
			addComponent(component);
	}

	/**
	 * Adds a {@link ValueListener} to the GUI, which will be notified of value changes.
	 *
	 * @param supplier the value supplier of the new listener
	 */
	protected final <V> ValueListener<V> addValueListener(Supplier<V> supplier)
	{
		ValueListener<V> valueListener = new ValueListener<>(supplier);
		valueListeners.add(valueListener);
		return valueListener;
	}

	//--- GUI Label Methods ---//

	/**
	 * Adds a label to the GUI and returns it
	 *
	 * @param text The text of the label
	 * @param x    The x position of the label
	 * @param y    The y position of the label
	 * @return The added label
	 */
	protected final DecoLabel addLabel(String text, int x, int y)
	{
		return addLabel(new DecoLabel(fontRenderer, x, y)).withText(text);
	}

	/**
	 * Adds a dynamically updated label to the GUI and returns it
	 *
	 * @param textFormat The language key of the label's text
	 * @param listener   A supplier that provides the text of the label
	 * @param x          The x position of the label
	 * @param y          The y position of the label
	 * @return The added label
	 */
	protected final DecoLabel addLabel(String textFormat, Supplier<String[]> listener, int x, int y)
	{
		return addLabel(new DecoLabel(fontRenderer, x, y)).withFormattedTextListener(textFormat, listener);
	}

	/**
	 * Adds a label to the GUI and returns it
	 *
	 * @param label The label to add
	 * @return The added label
	 */
	protected final DecoLabel addLabel(DecoLabel label)
	{
		labelList.add(label);
		label.id = labelList.size();
		return label;
	}

	//--- GUI Background Methods ---//

	/**
	 * Starts the background builder for this GUI
	 *
	 * @return The background builder
	 */
	protected DecoBackgroundBuilder<T, C> startBackground()
	{
		return this.backgroundBuilder = new DecoBackgroundBuilder(this);
	}

	//--- GUI Rendering Methods ---//

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		//Draw widgets
		drawWidgets(mouseX, mouseY, partialTicks);

		//Draw the tiled GUI background
		if(backgroundBuilder!=null)
			backgroundBuilder.draw();
	}

	/**
	 * Draws the GUI
	 *
	 * @param mouseX       The x position of the mouse cursor
	 * @param mouseY       The y position of the mouse cursor
	 * @param partialTicks The partial render ticks
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		if(refreshGUIFlag)
		{
			cleanupDecoGui();
			initGui();
			refreshGUIFlag = false;
		}

		//Draw the dark background
		this.drawDefaultBackground();

		//Update value listeners before GUI is displayed
		valueListeners.forEach(ValueListener::update);

		//Draw tiled background, labels, and buttons
		super.drawScreen(mouseX, mouseY, partialTicks);
		//Check scroll on components
		float scroll = Mouse.getDWheel();
		if(scroll!=0)
		{
			if(currentWidget==null||!currentWidget.onComponentScroll(mouseX, mouseY, scroll))
				if(focusedElement==null||!focusedElement.onComponentScroll(mouseX, mouseY, scroll))
					for(int i = buttonList.size()-1; i >= 0; i--)
					{
						GuiButton b = buttonList.get(i);
						if(b instanceof DecoComponent)
							((DecoComponent<?>)b).onComponentScroll(mouseX, mouseY, scroll);
					}
		}

		//Draw the upper layer of buttons
		GlStateManager.pushMatrix();
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		for(GuiButton b : buttonList)
			if(b instanceof DecoComponent)
				((DecoComponent<?>)b).drawButtonUpperLayer(mc, mouseX, mouseY, partialTicks);
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();

		//Draw tooltip
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	private void drawWidgets(int mouseX, int mouseY, float partialTicks)
	{
		//Calculate show/hide progress
		float progress = AMTUtils.getAnimationProgress(widgetTime--, MAX_WIDGET_TIME, true, partialTicks);

		//Draw the previous (hiding) widget
		if(previousWidget!=null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(-(progress)*previousWidget.getWidgetWidth(), 0, 0);
			if(progress < 1)
				previousWidget.drawButton(mc, mouseX, mouseY, partialTicks);
			GlStateManager.popMatrix();
		}
		//Draw the current widget
		if(currentWidget!=null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(-(1f-progress)*currentWidget.getWidgetWidth(), 0, 0);
			currentWidget.drawButton(mc, mouseX, mouseY, partialTicks);
			currentWidget.drawButtonUpperLayer(mc, mouseX, mouseY, partialTicks);
			GlStateManager.popMatrix();
		}

		//Update widget tabs position
		int wSize = (int)(currentWidget!=null?currentWidget.getWidgetWidth()*progress:
				(previousWidget!=null?previousWidget.getWidgetWidth()*(1f-progress): 0));
		for(DecoTab decoTab : widgetTabList)
		{
			decoTab.x = this.guiLeft+this.xSize+wSize;
			decoTab.initialize();
		}
	}

	protected void openManualWidget(String pageName, int index)
	{
		widgetList.stream()
				.filter(widget -> widget instanceof DecoManualWidget)
				.map(widget -> (DecoManualWidget)widget)
				.findFirst().ifPresent(widget -> {
					if(currentWidget!=widget)
						setCurrentWidget(widget);
					widget.setCurrentPage(pageName, index);
					requestFocus(widget);
				});
	}

	/**
	 * Renders the tooltip for a hovered-over component or itemstack.
	 *
	 * @param mouseX The x position of the mouse cursor
	 * @param mouseY The y position of the mouse cursor
	 */
	@Override
	protected final void renderHoveredToolTip(int mouseX, int mouseY)
	{
		List<String> tooltip = getTooltip();
		if(tooltip.isEmpty())
			super.renderHoveredToolTip(mouseX, mouseY);
		else
			drawHoveringText(tooltip, mouseX, mouseY, fontRenderer);
	}

	//--- GUI Handling Methods ---//

	/**
	 * Handles mouse input.
	 */
	@Override
	protected final void actionPerformed(@Nonnull GuiButton button) throws IOException
	{
		super.actionPerformed(button);
	}

	/**
	 * Handles keyboard input.
	 */
	@Override
	public final void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_F5))
		{
			refreshGUI();
			return;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_F6))
			exportCurrentGui();

		//Process key typed for the currently focused component
		if(focusedElement!=null)
		{
			if(keyCode==Keyboard.KEY_ESCAPE)
			{
				requestFocus(null);
				return;
			}

			//Common keys are turned into events for unified handling
			if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)||Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
				switch(keyCode)
				{
					case Keyboard.KEY_C:
						focusedElement.onGuiEvent(DecoGuiEvent.COPY);
						return;
					case Keyboard.KEY_V:
						focusedElement.onGuiEvent(DecoGuiEvent.PASTE);
						return;
					case Keyboard.KEY_X:
						focusedElement.onGuiEvent(DecoGuiEvent.CUT);
						return;
					case Keyboard.KEY_Z:
						focusedElement.onGuiEvent(DecoGuiEvent.UNDO);
						return;
					case Keyboard.KEY_Y:
						focusedElement.onGuiEvent(DecoGuiEvent.REDO);
						return;
				}
			//Other cases
			if(focusedElement.keyTyped(typedChar, keyCode))
				return;
		}

		//Result to super if no component handled the key
		super.keyTyped(typedChar, keyCode);
	}

	/**
	 * Handles mouse input.
	 */
	@Override
	protected final void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		MouseButton mouseButtonEnum = MouseButton.values()[mouseButton%MouseButton.values().length];

		//Widgets are not a part of the button list, so we need to check them separately
		boolean anyPressed = false;

		if(focusedElement!=null)
			anyPressed = focusedElement.decoMousePressed(this.mc, mouseX, mouseY, mouseButtonEnum);
		if(!anyPressed)
		{
			if(currentWidget!=null&&currentWidget.decoMousePressed(this.mc, mouseX, mouseY, mouseButtonEnum))
			{
				anyPressed = true;
				Pre event = new Pre(this, currentWidget, this.buttonList);
				if(MinecraftForge.EVENT_BUS.post(event))
					return;
				this.selectedButton = currentWidget;
				if(this.equals(this.mc.currentScreen))
					MinecraftForge.EVENT_BUS.post(new Post(this, event.getButton(), this.buttonList));
			}

			if(!anyPressed)
			{
				for(int i = this.buttonList.size()-1; i >= 0; i--)
				{
					GuiButton guiButton = this.buttonList.get(i);
					if(guiButton==focusedElement)
						continue;
					if(guiButton instanceof DecoComponent)
						anyPressed = ((DecoComponent<?>)guiButton).decoMousePressed(this.mc, mouseX, mouseY, mouseButtonEnum)||anyPressed;
					else if(mouseButtonEnum==MouseButton.LEFT)
						anyPressed = guiButton.mousePressed(this.mc, mouseX, mouseY)||anyPressed;
				}
			}
		}

		if(!anyPressed)
			requestFocus(null);

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected final void mouseReleased(int mouseX, int mouseY, int state)
	{
		if(focusedElement!=null)
		{
			MouseButton[] mouseButtons = MouseButton.values();
			if(state >= 0&&state < mouseButtons.length)
				focusedElement.decoMouseReleased(mouseX, mouseY, mouseButtons[state]);
		}
		super.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	protected final void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
	{
		if(focusedElement!=null)
		{
			MouseButton[] mouseButtons = MouseButton.values();
			if(clickedMouseButton >= 0&&clickedMouseButton < mouseButtons.length)
				focusedElement.decoMouseDragged(mc, mouseX, mouseY, mouseButtons[clickedMouseButton]);
		}
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}

	/**
	 * Triggered upon closing the GUI. Saves the GUI data to {@link ClientProxy} and delegates context-specific sync to subclasses.
	 */
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();

		if(!changeGUIFlag)
		{
			onGuiClosedWithoutTransition();
			clearStoredGuiData();
		}
		else
			saveGuiData();

		cleanupDecoGui();
	}

	protected void cleanupDecoGui()
	{
		if(backgroundBuilder!=null)
			backgroundBuilder.cleanup();
		for(GuiButton b : buttonList)
			if(b instanceof DecoComponent)
				((DecoComponent<?>)b).cleanup();
		//Labels shouldn't create VBOs, so no cleanup needed
		for(DecoComponentWidgetBase<?> widget : widgetList)
			widget.cleanup();
	}

	/**
	 * Called when the GUI is closed normally, not when it is changing to another GUI.
	 */
	protected void onGuiClosedWithoutTransition()
	{

	}

	//--- Component Events ---//

	protected List<String> getTooltip()
	{
		this.hoveredElement = null;
		//Widget
		if(currentWidget!=null&&currentWidget.isMouseOver())
			return currentWidget.getTooltip();
		//Buttons
		for(int i = buttonList.size()-1; i >= 0; i--)
		{
			GuiButton guiButton = buttonList.get(i);
			if(!guiButton.enabled)
				continue;
			if(guiButton instanceof DecoComponent&&guiButton.isMouseOver())
			{
				this.hoveredElement = (DecoComponent<?>)guiButton;
				return ((DecoComponent<?>)guiButton).getTooltip();
			}
		}
		//Labels
		for(GuiLabel guiLabel : labelList)
			if(guiLabel instanceof DecoLabel&&((DecoLabel)guiLabel).shouldDisplayTooltip())
				return ((DecoLabel)guiLabel).getTooltip();

		return Collections.emptyList();
	}

	public void requestFocus(DecoComponent<?> component)
	{
		if(this.focusedElement!=component)
		{
			if(this.focusedElement!=null)
				this.focusedElement.setFocused(false);
			if(component!=null)
				component.setFocused(true);
		}

		this.focusedElement = component;
	}

	//--- NBT ---//

	/**
	 * Called upon start and synchronisation, loads the data from the client proxy.
	 *
	 * @return The NBT compound containing the data
	 */
	protected EasyNBT loadGuiData()
	{
		ClientProxy proxy = getClientProxy();
		EasyNBT nbt = proxy.getStoredGuiData();

		if(!isStoredGuiDataValid(nbt))
			return proxy.setStoredGuiData();

		//Deserialize all SyncNBT fields
		NBTSerialisation.synchroniseFor(this, (tag, gui) ->
				tag.deserializeAll(gui, nbt.unwrap(), true));
		return nbt;
	}

	/**
	 * Checks whether stored GUI NBT belongs to this GUI instance.
	 */
	protected boolean isStoredGuiDataValid(EasyNBT nbt)
	{
		final boolean[] valid = {false};
		nbt.checkSetString(NBT_GUI_NAME, s -> valid[0] = s.equals(name));
		return valid[0];
	}

	/**
	 * Called upon closing the GUI, gathers and saves the data to the client proxy.
	 *
	 * @return The NBT compound containing the data
	 */
	protected EasyNBT saveGuiData()
	{
		EasyNBT nbt = createGuiDataTag();

		//Save component data
		for(GuiButton button : buttonList)
			if(button instanceof DecoComponent)
				((DecoComponent<?>)button).onGuiSave();

		//Save current widget data
		if(currentWidget!=null)
			nbt.withString("currentWidget", currentWidget.getName());

		//Save fields marked with @SyncNBT
		NBTSerialisation.synchroniseFor(this, (tag, tile) -> tag.serializeAll(tile, nbt.unwrap()));

		return nbt;
	}

	/**
	 * Creates the client-side GUI persistence tag. Subclasses add their own identity keys here.
	 */
	protected EasyNBT createGuiDataTag()
	{
		return getClientProxy().setStoredGuiData()
				.withString(NBT_GUI_NAME, name);
	}

	protected final void clearStoredGuiData()
	{
		getClientProxy().setStoredGuiData();
	}

	protected final ClientProxy getClientProxy()
	{
		assert ImmersiveIntelligence.proxy instanceof ClientProxy;
		return (ClientProxy)ImmersiveIntelligence.proxy;
	}

	@Nullable
	public Object getIngredientUnderMouse()
	{
		return hoveredElement==null?null: hoveredElement.getProvidedIngredient();
	}

	/**
	 * JEI Compat, returns a list of rectangles that are taken by the GUI elements, so the JEI overlay can adjust.
	 *
	 * @return a list of AWT rectangles
	 */
	public final List<Rectangle> getTakenSpace()
	{
		//Return cached value if available
		if(takenSpace!=null)
		{
			//Get the widget show animation progress
			float progress = AMTUtils.getAnimationProgress(widgetTime--, MAX_WIDGET_TIME, true, 0);

			//Set the previous widget rectangle
			Rectangle previousWidgetRectangle = takenSpace.get(takenSpace.size()-2);
			if(previousWidget!=null)
				previousWidgetRectangle.setBounds((int)(previousWidget.x-(progress*xSize)), previousWidget.y,
						previousWidget.getWidgetWidth(), previousWidget.height);
			else
				previousWidgetRectangle.setBounds(0, 0, 0, 0);

			//Set the current widget rectangle
			Rectangle widgetRectangle = takenSpace.get(takenSpace.size()-1);
			if(currentWidget!=null)
				widgetRectangle.setBounds((int)(currentWidget.x-(1f-progress)*xSize), currentWidget.y,
						currentWidget.getWidgetWidth(), currentWidget.height);
			else
				widgetRectangle.setBounds(0, 0, 0, 0);

			for(int i = takenSpace.size()-widgetTabList.size()-2; i < takenSpace.size()-2; i++)
				takenSpace.get(i).x = guiLeft+xSize+(int)(progress*(currentWidget!=null?currentWidget.getWidgetWidth(): 0));

			return takenSpace;
		}

		List<Rectangle> takenSpace = new ArrayList<>();
		//Add rectangles from the background builder
		if(backgroundBuilder!=null)
			for(Object object : backgroundBuilder.getTakenSpace())
			{
				DecoBackgroundTile rect = (DecoBackgroundTile)object;
				takenSpace.add(new Rectangle(guiLeft+rect.x, guiTop+rect.y, rect.width, rect.height));
			}

		//Add rectangles for each tab
		for(DecoTab tab : tabList)
			takenSpace.add(new Rectangle(tab.x, tab.y, tab.width, tab.height));

		//Add widget tabs
		for(DecoTab tab : widgetTabList)
			takenSpace.add(new Rectangle(tab.x, tab.y, tab.width, tab.height));

		//Previous widget
		takenSpace.add(new Rectangle(0, 0, 0, 0));
		//Current widget
		takenSpace.add(new Rectangle(0, 0, 0, 0));

		return this.takenSpace = takenSpace;
	}

	//--- Utilities ---//

	public final boolean refreshGUI()
	{
		return changeGUI(this.gui);
	}

	/**
	 * Closes the current GUI, ensuring data is saved and sent to the server.
	 */
	public final boolean closeGUI()
	{
		return changeGUI(null);
	}

	/**
	 * Changes the GUI to the specified one, ensuring data is saved and sent to the server
	 *
	 * @param newGUI the new GUI to change to, null will close the current GUI
	 */
	public final boolean changeGUI(@Nullable IIGUI newGUI)
	{
		return changeGUI(newGUI, null, null);
	}

	/**
	 * Changes the GUI to the specified one, ensuring data is saved and sent to the server.
	 *
	 * @param newGUI      the new GUI to change to, null will close the current GUI
	 * @param guiData     additional data to save for the GUI
	 * @param contextData additional data to save for the backing object
	 */
	public final boolean changeGUI(@Nullable IIGUI newGUI, @Nullable EasyNBT guiData, @Nullable EasyNBT contextData)
	{
		//Switch the Change GUI flag to prevent double saving
		this.changeGUIFlag = true;

		//Save context-specific data first
		onBeforeGuiChange(contextData);

		//Save GUI data
		saveGuiData().conditionally(guiData!=null, e -> e.mergeWith(guiData));

		return sendGuiChangeMessage(newGUI);
	}

	/**
	 * Gives subclasses a chance to sync their backing object before a GUI transition.
	 */
	protected void onBeforeGuiChange(@Nullable EasyNBT contextData)
	{

	}

	/**
	 * Sends the actual GUI transition message.
	 */
	protected boolean sendGuiChangeMessage(@Nullable IIGUI newGUI)
	{
		if(newGUI==null)
			IIPacketHandler.sendToServer(MessageGuiNBT.closeGuiMessage());
		else if(newGUI==gui)
			refreshGUIFlag = true;
		else
			return false;
		return true;
	}

	/**
	 * Starts the scissor function, enabling OpenGL scissor test.
	 * This is used to limit rendering to a specific area of the screen.
	 *
	 * @param x     The x position of the scissor box
	 * @param y     The y position of the scissor box
	 * @param xSize The width of the scissor box
	 * @param ySize The height of the scissor box
	 */
	public void scissorStart(int x, int y, int xSize, int ySize)
	{
		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		if(screenshotMode)
		{
			//When in screenshot mode, use coordinates relative to the framebuffer
			//Add offsets to account for the GUI position adjustment
			GL11.glScissor(
					x-guiLeft+16,  //Offset by the same amount as in exportCurrentGui
					height-(y-guiTop+16)-ySize,  //Flip Y coordinate for OpenGL
					xSize,
					ySize
			);
		}
		else
		{
			//Normal rendering with Minecraft scaling
			ScaledResolution res = new ScaledResolution(ClientUtils.mc());
			x = x*res.getScaleFactor();
			ySize = ySize*res.getScaleFactor();
			y = ClientUtils.mc().displayHeight-(y*res.getScaleFactor())-ySize;
			xSize = xSize*res.getScaleFactor();
			GL11.glScissor(x, y, xSize, ySize);
		}
	}

	/**
	 * Ends the scissor function, restoring the OpenGL state.
	 */
	public void scissorEnd()
	{
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	/**
	 * @return the itemstack currently being held by the player's cursor
	 */
	@Nonnull
	public ItemStack getMouseHeldItemStack()
	{
		return Minecraft.getMinecraft().player==null?ItemStack.EMPTY: Minecraft.getMinecraft().player.inventory.getItemStack();
	}

	private void exportCurrentGui()
	{
		try
		{
			//Create directory for exports if it doesn't exist
			File exportDir = new File("screenshots/ii_gui/");
			if(!exportDir.exists()&&!exportDir.mkdirs())
			{
				IILogger.error("Could not create screenshot directory.");
				return;
			}

			int width = xSize+32;
			int height = ySize+32;
			this.screenshotMode = true;

			//Set up framebuffer for rendering with transparency
			Framebuffer framebuffer = new Framebuffer(width, height, true);
			framebuffer.enableStencil();
			framebuffer.bindFramebuffer(true);

			GlStateManager.pushMatrix();
			//Clear with transparent background
			GlStateManager.clearColor(0, 0, 0, 0);
			GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);

			//Set up projection matrix for GUI rendering
			GlStateManager.matrixMode(GL11.GL_PROJECTION);
			GlStateManager.loadIdentity();
			GlStateManager.ortho(0, width, height, 0, 1000.0D, 3000.0D);
			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
			GlStateManager.loadIdentity();
			GlStateManager.translate(-guiLeft+16, -guiTop+16, -2000.0F);

			//Configure proper blending for transparency
			GlStateManager.enableAlpha();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F); // ~1/255
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA); // Pre-multiplied alpha
			GlStateManager.disableDepth();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			//Disable lighting and normals rescale
			GlStateManager.disableRescaleNormal();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();

			//Draw widgets
			drawWidgets(0, 0, 0);
			//Draw tiled background, if present
			if(backgroundBuilder!=null)
				backgroundBuilder.draw();

			GlStateManager.color(1f, 1f, 1f, 1f);
			GlStateManager.enableAlpha();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F); // ~1/255
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA); // Pre-multiplied alpha
			GlStateManager.disableDepth();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			//Draw labels and buttons
			for(GuiLabel guiButton : this.labelList)
				guiButton.drawLabel(mc, 0, 0);
			for(GuiButton guiButton : this.buttonList)
				guiButton.drawButton(mc, 0, 0, 0);

			//Draw the upper layer of buttons
			for(GuiButton b : buttonList)
				if(b instanceof DecoComponent)
					((DecoComponent<?>)b).drawButtonUpperLayer(mc, 0, 0, 0);

			GlStateManager.popMatrix();
			//Read pixels from framebuffer
			ByteBuffer buffer = BufferUtils.createByteBuffer(width*height*4);
			GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

			//Restore default framebuffer
			framebuffer.unbindFramebuffer();
			framebuffer.deleteFramebuffer();

			//Convert buffer to image and flip Y-axis (OpenGL vs Java image coordinates)
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			for(int x = 0; x < width; x++)
			{
				for(int y = 0; y < height; y++)
				{
					int i = (x+(height-y-1)*width)*4;
					int r = buffer.get(i)&0xFF;
					int g = buffer.get(i+1)&0xFF;
					int b = buffer.get(i+2)&0xFF;
					int a = buffer.get(i+3)&0xFF;
					image.setRGB(x, y, (a<<24)|(r<<16)|(g<<8)|b);
				}
			}

			//Generate file with GUI name and timestamp
			String filename = name+".png";
			File outputFile = new File(exportDir, filename);
			ImageIO.write(image, "PNG", outputFile);

			//Notify user
			ITextComponent itextcomponent = new TextComponentString(outputFile.getName());
			itextcomponent.getStyle().setClickEvent(new ClickEvent(Action.OPEN_FILE, outputFile.getCanonicalFile().getAbsolutePath()));
			itextcomponent.getStyle().setUnderlined(true);
			mc.player.sendMessage(new TextComponentTranslation("screenshot.success", itextcomponent));

		} catch(Exception e)
		{
			mc.player.sendMessage(new TextComponentTranslation("screenshot.failure", e.getMessage()));
			IILogger.error("Failed to export GUI: "+e.getMessage());
		} finally
		{
			this.screenshotMode = false;
		}
	}

	/**
	 * A resource loader for a Deco based GUI, created when one of its resource fields is marked by {@link DecoResource}
	 */
	public static class DecoResourcesLoader implements IReloadableModelContainer<DecoResourcesLoader>
	{
		private final List<ResLoc> resources;

		public DecoResourcesLoader(String name, List<ResLoc> resources)
		{
			this.resources = resources;
			subscribeToList("gui/"+name);
		}

		@Override
		public void reloadModels()
		{

		}

		@Override
		public void registerSprites(TextureMap map)
		{
			resources.forEach(resLoc -> ApiUtils.getRegisterSprite(map, resLoc));
		}
	}
}
