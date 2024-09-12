package pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import blusunrize.lib.manual.gui.GuiManual;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.ITabbedGui;
import pl.pabilo8.immersiveintelligence.client.gui.elements.GuiWidgetManualWrapper;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonItemAdvanced;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonTab;
import pl.pabilo8.immersiveintelligence.client.gui.elements.label.GuiLabelNoShadow;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerArithmeticLogicMachine.CircuitSlot;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageGuiNBT;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * @author Pabilo8
 */
public class GuiArithmeticLogicMachineBase extends GuiIEContainerBase implements ITabbedGui
{
	protected static final ResourceLocation TEXTURE_STORAGE = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/gui/arithmetic_logic_machine_storage.png");
	protected static final ResourceLocation TEXTURE_EDIT = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/gui/arithmetic_logic_machine_editing.png");
	protected static final ResourceLocation TEXTURE_VARIABLES = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/gui/arithmetic_logic_machine_variables.png");

	private final IIGuiList thisGui;
	public final LinkedHashMap<GuiButton, IIGuiList> TABS = new LinkedHashMap<>();
	protected String title = I18n.format("tile.immersiveintelligence.metal_multiblock.arithmetic_logic_machine.name");
	private final ResourceLocation thisTexture;

	protected ClientProxy proxy = (ClientProxy)ImmersiveIntelligence.proxy;
	protected TileEntityArithmeticLogicMachine tile;
	public IItemHandler handler;
	protected InventoryPlayer playerInv;

	private GuiManual trueManual;
	public GuiWidgetManualWrapper sideManual = null;
	protected GuiButtonState manualButton;
	//this is due to the machine sending a close door message to the server and producing an annoying sound
	protected boolean preparedForChange = false;

	public GuiArithmeticLogicMachineBase(EntityPlayer player, TileEntityArithmeticLogicMachine tile, IIGuiList gui)
	{
		super(gui.containerFromTile.apply(player, tile));
		this.thisTexture = gui==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE?TEXTURE_STORAGE: gui==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_EDIT?TEXTURE_EDIT: TEXTURE_VARIABLES;
		this.ySize = 222;
		this.playerInv = player.inventory;
		this.thisGui = gui;
		this.tile = tile;
		this.handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		refreshStoredData();
	}

	@Override
	public void initGui()
	{
		IIPacketHandler.sendToServer(new MessageBooleanAnimatedPartsSync(true, 0, tile.getPos()));

		buttonList.clear();
		labelList.clear();
		TABS.clear();

		super.initGui();
		refreshStoredData();

		addLabel(4, 8, IIReference.COLOR_H1, title);

		addTab(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE, "storage_module");
		addItemTab(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_0, 0);
		addItemTab(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_1, 1);
		addItemTab(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_2, 2);
		addItemTab(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_3, 3);

		trueManual = ManualHelper.getManual().getGui();
		if(trueManual==null||trueManual instanceof GuiWidgetManualWrapper)
			trueManual = new GuiManual(ManualHelper.getManual(), ManualHelper.getManual().texture);

		sideManual = new GuiWidgetManualWrapper(trueManual, guiLeft+xSize-20, guiTop, proxy.storedGuiData.getBoolean("manual"));

		manualButton = addButton(new GuiButtonState(buttonList.size(),
				0, guiTop+56, 32, 18, "", proxy.storedGuiData.getBoolean("manual"), TEXTURE_STORAGE.toString(), 176, 48, -1));

	}

	@Override
	protected void actionPerformed(@Nonnull GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		if(button==manualButton)
		{
			saveBasicData();
		}
		else if(TABS.containsKey(button))
		{
			syncDataToServer();
			saveBasicData();
			preparedForChange = true;
			IIPacketHandler.sendToServer(new MessageGuiNBT(TABS.get(button), tile));
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	@Override
	public void handleInput() throws IOException
	{
		boolean handled = false;
		if(sideManual.manualTime==100)
		{
			int mouseX = Mouse.getX()*width/mc.displayWidth;
			int mouseZ = height-Mouse.getY()*height/mc.displayHeight-1;
			if(IIMath.isPointInRectangle(guiLeft+xSize, sideManual.y, sideManual.x+146, sideManual.y+198, mouseX, mouseZ))
			{
				sideManual.handleInput();
				handled = true;
			}
		}
		if(!handled)
			super.handleInput();
	}

	@Override
	protected void handleMouseClick(@Nullable Slot slotIn, int slotId, int mouseButton, @Nullable ClickType type)
	{
		super.handleMouseClick(slotIn, slotId, mouseButton, type);
		if(slotIn instanceof CircuitSlot)
			initGui();
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);

		ArrayList<String> tooltip = getTooltip(mx, my);
		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, -1, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}

	public ArrayList<String> getTooltip(int mx, int my)
	{
		ArrayList<String> tooltip = new ArrayList<>();
		TABS.keySet().stream().filter(GuiButton::isMouseOver).findFirst().ifPresent(tab -> tooltip.add(tab.displayString));

		if(this.manualButton.isMouseOver())
			tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+(manualButton.state?"hide_manual_widget": "show_manual_widget")));
		return tooltip;
	}

	@Override
	public void onGuiClosed()
	{
		syncDataToServer();
		if(!preparedForChange)
			IIPacketHandler.sendToServer(new MessageBooleanAnimatedPartsSync(false, 0, tile.getPos()));
		super.onGuiClosed();
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		sideManual.setOpened(manualButton.state);
		manualButton.x = guiLeft+176+(int)(146*(sideManual.manualTime/100f));

		sideManual.drawScreen(mx, my, f);

		IIClientUtils.bindTexture(thisTexture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);


	}

	void refreshStoredData()
	{

	}

	protected void syncDataToServer()
	{
		if(tile==null)
			return;
		sideManual.onGuiClosed();
		GuiManual.activeManual = trueManual;
		trueManual.setSelectedEntry(sideManual.getSelectedEntry());
		trueManual.selectedCategory = sideManual.selectedCategory;
		trueManual.previousSelectedEntry = sideManual.previousSelectedEntry;
		trueManual.page = sideManual.page;
	}

	protected void addTab(IIGuiList gui, String name)
	{
		final int vOffset = TABS.size()*24;
		GuiButtonTab button = new GuiButtonTab(buttonList.size(), guiLeft-28, guiTop+4+vOffset, 28, 24, thisGui==gui?204: 176, vOffset,
				TEXTURE_STORAGE, I18n.format(IIReference.DESCRIPTION_KEY+name));
		TABS.put(button, gui);
		addButton(button);
	}

	protected void addItemTab(IIGuiList gui, int slot)
	{
		final int vOffset = TABS.size()*24;
		if(!handler.getStackInSlot(slot).isEmpty())
		{
			GuiButtonItemAdvanced button = new GuiButtonItemAdvanced(buttonList.size(), guiLeft-28, guiTop+4+vOffset, 28, 24, TEXTURE_STORAGE,
					thisGui==gui?204: 176, 24, handler.getStackInSlot(slot), 6, 2);
			TABS.put(button, gui);
			addButton(button);
		}
	}

	protected GuiLabel addLabel(int x, int y, IIColor textColor, String... text)
	{
		return addLabel(x, y, 0, 0, false, textColor, text);
	}

	protected GuiLabel addLabel(int x, int y, int w, int h, boolean shadow, IIColor textColor, String... text)
	{
		GuiLabel guiLabel =
				shadow?new GuiLabel(this.fontRenderer, labelList.size(), guiLeft+x, guiTop+y, w, h, textColor.getPackedRGB()):
						new GuiLabelNoShadow(this.fontRenderer, labelList.size(), guiLeft+x, guiTop+y, w, h, textColor);
		Arrays.stream(text).forEachOrdered(guiLabel::addLine);
		labelList.add(guiLabel);
		return guiLabel;
	}

	public void saveBasicData()
	{
		ITabbedGui.super.saveBasicData(proxy, tile);
		proxy.storedGuiData.setBoolean("manual", manualButton.state);
	}

	protected static IIGuiList getPage(int page)
	{
		switch(page)
		{
			default:
			case 0:
				return IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_0;
			case 1:
				return IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_1;
			case 2:
				return IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_2;
			case 3:
				return IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_3;
		}
	}
}
