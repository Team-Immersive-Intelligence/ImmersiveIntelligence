package pl.pabilo8.immersiveintelligence.client.gui.deco;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoBase;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoRectangle;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;
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
 * @author Pabilo8
 * @since 04.01.2025
 */
public abstract class DecoGui<T extends TileEntityIEBase & IIEInventory, C extends ContainerIIBase<T>> extends GuiContainer
{
	//Gui basics
	protected final String name;
	protected final T tile;
	protected final C container;
	protected final InventoryPlayer playerContainer;

	protected ResLoc fallbackBackground = IIReference.GUI_BG_STEEL;

	//Components
	protected final List<DecoTab> tabList = new ArrayList<>();

	//Help framework
	private boolean helpMode = false;
	private DecoBackgroundBuilder<T, C> backgroundBuilder;
	private List<Rectangle> takenSpace;
	private GuiComponentDecoBase<?> focusedElement;

	public DecoGui(EntityPlayer player, T tile, IIGuiList guiList)
	{
		super(guiList.containerFromTile.apply(player, tile));
		this.tile = tile;
		this.container = ((C)this.inventorySlots);
		this.playerContainer = player.inventory;

		AtomicReference<String> guiName = new AtomicReference<>("deco");
		Optional.ofNullable(this.getClass().getAnnotation(DecoTemplate.class))
				.ifPresent(template -> {
					fallbackBackground = ResLoc.of(template.style());
					guiName.set(template.name());
				});
		name = guiName.get();
	}

	@Override
	public final void initGui()
	{
		//Sync GUI NBT
		this.loadGuiData();

		//Clean GUI
		this.buttonList.clear();
		this.labelList.clear();
		this.tabList.clear();
		this.fontRenderer = IIClientUtils.fontRegular;
		//JEI Compatibility
		this.takenSpace = null;

		//Fire initialization event for the extending Deco GUI class
		onInit();

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

		//Apply label and component position corrections
		for(GuiLabel guiLabel : labelList)
		{
			guiLabel.x += guiLeft;
			guiLabel.y += guiTop;
		}
		for(GuiButton button : buttonList)
			if(button instanceof GuiComponentDecoBase)
				((GuiComponentDecoBase<?>)button).setParentGUI(this);
	}

	/**
	 * Called upon Deco GUI initialization.
	 */
	public abstract void onInit();

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

	//Buttons and other components

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
			tab.y = 5+tabList.size()*24;
			tabList.add(tab);
		}

		return component;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	protected final void addComponents(GuiComponentDecoBase... components)
	{
		for(GuiComponentDecoBase component : components)
			addComponent(component);
	}

	//Labels
	protected final DecoLabel addLabel(String text, int x, int y)
	{
		return addLabel(new DecoLabel(fontRenderer, x, y)).withText(text);
	}

	protected final DecoLabel addLabel(DecoLabel label)
	{
		labelList.add(label);
		label.id = labelList.size();
		return label;
	}

	//--- GUI Background Methods ---//

	protected DecoBackgroundBuilder<T, C> startBackground()
	{
		return this.backgroundBuilder = new DecoBackgroundBuilder<>(this);
	}

	//--- GUI Rendering Methods ---//

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		if(backgroundBuilder!=null)
			backgroundBuilder.draw();

		//TODO: 23.01.2024 draw background boxes, tabs
	}

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
			for(GuiButton b : buttonList)
				if(b instanceof GuiComponentDecoBase)
					((GuiComponentDecoBase<?>)b).onComponentScroll(mouseX, mouseY, scroll);

		//Draw the upper layer of buttons
		for(GuiButton b : buttonList)
			if(b instanceof GuiComponentDecoBase)
				((GuiComponentDecoBase<?>)b).drawButtonUpperLayer(mc, mouseX, mouseY, partialTicks);
		//Draw tooltip
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY)
	{
		List<String> tooltip = getTooltip();
		if(tooltip.isEmpty())
			super.renderHoveredToolTip(mouseX, mouseY);
		else
			drawHoveringText(tooltip, mouseX, mouseY, fontRenderer);
	}

	//--- GUI Handling Methods ---//

	@Override
	protected final void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);
	}

	@Override
	public final void keyTyped(char typedChar, int keyCode) throws IOException
	{
		//Process key typed for the currently focused component
		if(focusedElement!=null)
			if(focusedElement.keyTyped(typedChar, keyCode))
				return;

		//Result to super if no component handled the key
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected final void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		//Lose element focus
		focusedElement = null;
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		saveGuiData();
		//Cleanup background builder
		if(backgroundBuilder!=null)
			backgroundBuilder.cleanup();
		//Cleanup components
		for(GuiButton b : buttonList)
			if(b instanceof GuiComponentDecoBase)
				((GuiComponentDecoBase<?>)b).cleanup();
		//Labels shouldn't create VBOs, so no cleanup needed
	}

	//--- Component Events ---//

	protected List<String> getTooltip()
	{
		//Buttons
		for(GuiButton guiButton : buttonList)
			if(guiButton instanceof GuiComponentDecoBase&&guiButton.isMouseOver())
				return ((GuiComponentDecoBase)guiButton).getTooltip();

		return Collections.emptyList();
	}

	public T getTile()
	{
		return tile;
	}

	//--- NBT ---//

	/**
	 * Called upon start and synchronisation, loads the data from the client proxy
	 *
	 * @return The NBT compound containing the data
	 */
	protected EasyNBT loadGuiData()
	{
		assert ImmersiveIntelligence.proxy instanceof ClientProxy;
		ClientProxy proxy = (ClientProxy)ImmersiveIntelligence.proxy;
		EasyNBT nbt = proxy.getStoredGuiData();

		//Return valid compound or an empty one
		if(!nbt.hasKey("pos")||!new DimensionBlockPos(tile).equals(nbt.getDimPos("pos")))
			return proxy.setStoredGuiData();
		return nbt;
	}

	/**
	 * Called upon closing the GUI, saves the data to the client proxy
	 *
	 * @return The NBT compound containing the data
	 */
	protected EasyNBT saveGuiData()
	{
		assert ImmersiveIntelligence.proxy instanceof ClientProxy;
		EasyNBT nbt = ((ClientProxy)ImmersiveIntelligence.proxy).setStoredGuiData()
				.withDimPos("pos", new DimensionBlockPos(tile));

		NBTSerialisation.synchroniseFor(this, (tag, tile) -> tag.serializeAll(tile, nbt.unwrap()));

		return nbt;
	}

	public void requestFocus(GuiComponentDecoBase<?> component)
	{
		this.focusedElement = component;
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
			IReloadableModelContainer.super.registerSprites(map);
			resources.forEach(map::registerSprite);
		}
	}

	public final List<Rectangle> getTakenSpace()
	{
		//Return cached value if available
		if(takenSpace!=null)
			return takenSpace;

		List<Rectangle> takenSpace = new ArrayList<>();
		//Add rectangles from the background builder
		if(backgroundBuilder!=null)
			for(DecoRectangle rect : backgroundBuilder.getTakenSpace())
				takenSpace.add(new Rectangle(guiLeft+rect.x, guiTop+rect.y, rect.width, rect.height));

		//Add rectangles for each tab
		for(DecoTab tab : tabList)
			takenSpace.add(new Rectangle(tab.x, tab.y, tab.width, tab.height));

		return this.takenSpace = takenSpace;
	}
}
