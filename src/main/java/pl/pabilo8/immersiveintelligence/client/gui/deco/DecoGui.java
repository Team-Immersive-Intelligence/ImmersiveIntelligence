package pl.pabilo8.immersiveintelligence.client.gui.deco;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.fml.common.Loader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoBase;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoBase.DecoGuiEvent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoBase.MouseButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.widget.DecoComponentWidgetBase;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundTile;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoResource;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageGuiNBT;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * Introducing Deco, Immersive Intelligence's new GUI framework designed to be
 *     <ul>
 *         <li>Dynamic</li>
 *         <li>Elegant</li>
 *         <li>Compact</li>
 *         <li>Optimized</li>
 *     </ul>
 *     Offering a variety of {@link pl.pabilo8.immersiveintelligence.client.gui.deco.component components},
 *     {@link pl.pabilo8.immersiveintelligence.client.gui.deco.widget widgets} and a tile-based
 *     {@link DecoBackgroundBuilder} to make GUI creation easier and more efficient
 * </p>
 * Class for advanced GUIs that store data in the client proxy NBT and use components for their display<br>
 * Use annotation {@link DecoTemplate} to specify traits
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.01.2025
 */
public abstract class DecoGui<T extends TileEntityIEBase & IIEInventory, C extends ContainerIIBase<T>> extends GuiContainer
{
	private static final int MAX_WIDGET_TIME = 40;

	//Gui basics
	protected final String name;
	protected final T tile;
	protected final C container;
	protected final InventoryPlayer playerContainer;

	//Components
	protected final List<DecoTab> tabList = new ArrayList<>();
	protected final List<DecoTab> widgetTabList = new ArrayList<>();
	private final List<DecoComponentWidgetBase<?>> widgetList = new ArrayList<>();

	//Background
	private DecoBackgroundBuilder<T, C> backgroundBuilder;
	private List<Rectangle> takenSpace;
	private GuiComponentDecoBase<?> focusedElement;
	private GuiComponentDecoBase<?> hoveredElement;

	//Widgets
	private DecoComponentWidgetBase<?> previousWidget, currentWidget;
	private int widgetTime = 0;

	//Help framework
	private boolean helpMode = false;

	public DecoGui(EntityPlayer player, T tile, IIGUI iigui)
	{
		super(iigui.containerFromTile.apply(player, tile));
		this.tile = tile;
		//noinspection unchecked
		this.container = ((C)this.inventorySlots);
		this.playerContainer = player.inventory;

		AtomicReference<String> guiName = new AtomicReference<>("deco");
		Optional.ofNullable(this.getClass().getAnnotation(DecoTemplate.class))
				.ifPresent(template -> guiName.set(template.name()));
		name = guiName.get();
	}

	@Override
	public final void initGui()
	{
		//Sync GUI NBT
		EasyNBT nbt = this.loadGuiData();
		NBTSerialisation.synchroniseFor(this, (tag, gui) ->
				tag.deserializeAll(gui, nbt.unwrap(), true));

		//Clean GUI
		this.buttonList.clear();
		this.labelList.clear();
		this.tabList.clear();
		this.fontRenderer = IIClientUtils.fontRegular;
		//JEI Compatibility
		this.takenSpace = null;

		//Fire initialization event for the extending Deco GUI class
		onInit();

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
			xSize = ySize = 64;

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
			if(button instanceof GuiComponentDecoBase)
				((GuiComponentDecoBase<?>)button).setParentGUI(this);
		for(DecoComponentWidgetBase<?> widget : widgetList)
			widget.setParentGUI(this);
	}

	/**
	 * Called upon Deco GUI initialization.
	 */
	public abstract void onInit();

	/**
	 * Called after {@link #onInit()} for GUI JEI compatibility initialization.
	 * Overriding methods should be annotated with @{@link net.minecraftforge.fml.common.Optional.Method} to not cause a crash if JEI is not present.
	 */
	public void onInitJEICompat()
	{

	}

	//--- GUI Component Methods ---//

	/**
	 * @deprecated use {@link #addComponent(GuiComponentDecoBase)} instead
	 */
	@Override
	@Deprecated
	protected final <B extends GuiButton> B addButton(B button)
	{
		return super.addButton(button);
	}

	/**
	 * Adds a component to the GUI and returns it
	 *
	 * @param component The component to add
	 * @param <B>       The type of the component
	 * @return The added component
	 */
	protected final <B extends GuiComponentDecoBase<B>> B addComponent(B component)
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

	protected final <W extends DecoComponentWidgetBase<W>> W addWidget(W widget)
	{
		//Add the widget tab
		DecoTab tab = widget.provideTab();
		buttonList.add(tab);
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
	protected final void addComponents(GuiComponentDecoBase... components)
	{
		for(GuiComponentDecoBase component : components)
			addComponent(component);
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
		return this.backgroundBuilder = new DecoBackgroundBuilder<>(this);
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
		//Draw the dark background
		this.drawDefaultBackground();

		//Draw tiled background, labels, and buttons
		super.drawScreen(mouseX, mouseY, partialTicks);
		//Check scroll on components
		float scroll = Mouse.getDWheel();
		if(scroll!=0)
		{
			if(currentWidget!=null&&!currentWidget.onComponentScroll(mouseX, mouseY, scroll))
				for(GuiButton b : buttonList)
					if(b instanceof GuiComponentDecoBase)
						((GuiComponentDecoBase<?>)b).onComponentScroll(mouseX, mouseY, scroll);
		}

		//Draw the upper layer of buttons
		for(GuiButton b : buttonList)
			if(b instanceof GuiComponentDecoBase)
				((GuiComponentDecoBase<?>)b).drawButtonUpperLayer(mc, mouseX, mouseY, partialTicks);

		//Draw tooltip
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	private void drawWidgets(int mouseX, int mouseY, float partialTicks)
	{
		//Calculate show/hide progress
		float progress = IIAnimationUtils.getAnimationProgress(widgetTime--, MAX_WIDGET_TIME, true, partialTicks);

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
		//Process key typed for the currently focused component
		if(focusedElement!=null)
		{
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
		//Lose element focus
		focusedElement = null;

		MouseButton mouseButtonEnum = MouseButton.values()[mouseButton%MouseButton.values().length];

		//Widgets are not a part of the button list, so we need to check them separately
		if(currentWidget!=null&&currentWidget.decoMousePressed(this.mc, mouseY, mouseX, mouseButtonEnum))
		{
			ActionPerformedEvent.Pre event = new ActionPerformedEvent.Pre(this, currentWidget, this.buttonList);
			if(net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
				return;
			this.selectedButton = currentWidget;
			if(this.equals(this.mc.currentScreen))
				net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new ActionPerformedEvent.Post(this, event.getButton(), this.buttonList));
		}

		for(GuiButton guiButton : this.buttonList)
			if(guiButton instanceof GuiComponentDecoBase)
				((GuiComponentDecoBase<?>)guiButton).decoMousePressed(this.mc, mouseY, mouseX, mouseButtonEnum);

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected final void mouseReleased(int mouseX, int mouseY, int state)
	{
		if(focusedElement!=null)
			focusedElement.decoMouseReleased(mouseX, mouseY, MouseButton.values()[state]);
		super.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	protected final void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
	{
		if(focusedElement!=null)
			focusedElement.decoMouseDragged(mc, mouseX, mouseY, MouseButton.values()[clickedMouseButton]);
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}

	/**
	 * Triggered upon closing the GUI. Saves the GUI data to {@link ClientProxy} and (optionally) sends a sync message to the tile entity.
	 */
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();

		//Save GUI data
		saveGuiData();

		//Send a sync message to the tile entity
		EasyNBT nbt = onSaveTileData();
		if(!nbt.isEmpty())
			IIPacketHandler.sendToServer(new MessageIITileSync(tile, nbt));

		//Cleanup background builder
		if(backgroundBuilder!=null)
			backgroundBuilder.cleanup();

		//Cleanup components
		for(GuiButton b : buttonList)
			if(b instanceof GuiComponentDecoBase)
				((GuiComponentDecoBase<?>)b).cleanup();

		//Labels shouldn't create VBOs, so no cleanup needed

		//Cleanup widgets
		for(DecoComponentWidgetBase<?> widget : widgetList)
			widget.cleanup();
	}

	//--- Component Events ---//

	protected List<String> getTooltip()
	{
		this.hoveredElement = null;
		//Widget
		if(currentWidget!=null&&currentWidget.isMouseOver())
			return currentWidget.getTooltip();
		//Buttons
		for(GuiButton guiButton : buttonList)
			if(guiButton instanceof GuiComponentDecoBase&&guiButton.isMouseOver())
			{
				this.hoveredElement = (GuiComponentDecoBase<?>)guiButton;
				return ((GuiComponentDecoBase<?>)guiButton).getTooltip();
			}

		return Collections.emptyList();
	}

	public void requestFocus(GuiComponentDecoBase<?> component)
	{
		this.focusedElement = component;
	}

	//--- NBT ---//

	/**
	 * Called upon start and synchronisation, loads the data from the client proxy
	 *
	 * @return The NBT compound containing the data
	 */
	private EasyNBT loadGuiData()
	{
		//Load GUI data from the proxy
		assert ImmersiveIntelligence.proxy instanceof ClientProxy;
		ClientProxy proxy = (ClientProxy)ImmersiveIntelligence.proxy;
		EasyNBT nbt = proxy.getStoredGuiData();

		//Return a valid compound or an empty one if location mismatch
		if(!nbt.hasKey("pos")||!new DimensionBlockPos(tile).equals(nbt.getDimPos("pos")))
			return proxy.setStoredGuiData();
		return nbt;
	}

	/**
	 * Called upon closing the GUI, gathers and saves the data to the client proxy.
	 *
	 * @return The NBT compound containing the data
	 */
	private EasyNBT saveGuiData()
	{
		//Save basic identification
		assert ImmersiveIntelligence.proxy instanceof ClientProxy;
		EasyNBT nbt = ((ClientProxy)ImmersiveIntelligence.proxy).setStoredGuiData()
				.withDimPos("pos", new DimensionBlockPos(tile));

		//Save component data
		for(GuiButton button : buttonList)
			if(button instanceof GuiComponentDecoBase)
				((GuiComponentDecoBase<?>)button).onGuiSave();

		//Save current widget data
		if(currentWidget!=null)
			nbt.withString("currentWidget", currentWidget.getName());

		//Save fields marked with @SyncNBT
		NBTSerialisation.synchroniseFor(this, (tag, tile) -> tag.serializeAll(tile, nbt.unwrap()));

		return nbt;
	}

	/**
	 * Called upon closing the GUI, collects data to be passed in a {@link MessageIITileSync} to the tile entity.
	 *
	 * @return The NBT compound containing the data
	 */
	protected EasyNBT onSaveTileData()
	{
		return EasyNBT.newNBT();
	}

	@Nullable
	public Object getIngredientUnderMouse()
	{
		return hoveredElement==null?null: hoveredElement.getProvidedIngredient();
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
			float progress = IIAnimationUtils.getAnimationProgress(widgetTime--, MAX_WIDGET_TIME, true, 0);

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
			for(DecoBackgroundTile rect : backgroundBuilder.getTakenSpace())
				takenSpace.add(new Rectangle(guiLeft+rect.x, guiTop+rect.y, rect.width, rect.height));

		//Add rectangles for each tab
		for(DecoTab tab : tabList)
			takenSpace.add(new Rectangle(tab.x, tab.y, tab.width, tab.height));

		//Add widget tabs
		for(DecoTab tab : widgetTabList)
			takenSpace.add(new Rectangle(tab.x, tab.y, tab.width, tab.height));

		//Previous widget
		takenSpace.add(new Rectangle(0, 0, 0, 0));
		//Current wigdet
		takenSpace.add(new Rectangle(0, 0, 0, 0));

		return this.takenSpace = takenSpace;
	}

	//--- Utilities ---//

	/**
	 * @return the tile entity associated with this GUI
	 */
	public T getTile()
	{
		return tile;
	}

	public void syncAnimatedParts(int id, boolean state)
	{
		IIPacketHandler.sendToServer(new MessageBooleanAnimatedPartsSync(id, state, tile.getPos()));
	}

	/**
	 * Changes the GUI to the specified one, ensuring data is saved and sent to the server
	 *
	 * @param newGUI the new GUI to change to
	 */
	public final boolean changeGUI(@Nonnull IIGUI newGUI)
	{
		return changeGUI(newGUI, null, null);
	}

	/**
	 * Changes the GUI to the specified one, ensuring data is saved and sent to the server
	 *
	 * @param newGUI   the new GUI to change to
	 * @param guiData  additional data to save for the GUI
	 * @param tileData additional data to save for the tile entity
	 */
	public final boolean changeGUI(@Nonnull IIGUI newGUI, @Nullable EasyNBT guiData, @Nullable EasyNBT tileData)
	{
		//Save Tile Entity data
		EasyNBT nbt = onSaveTileData().conditionally(tileData!=null, e -> e.mergeWith(tileData));
		if(!nbt.isEmpty())
			IIPacketHandler.sendToServer(new MessageIITileSync(tile, nbt));

		//Save GUI data
		saveGuiData().conditionally(guiData!=null, e -> e.mergeWith(guiData));

		//Send change GUI message
		IIPacketHandler.sendToServer(new MessageGuiNBT(newGUI, tile));
		return true;
	}
}
