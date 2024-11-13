package pl.pabilo8.immersiveintelligence.client.gui.block.data_input_machine;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import blusunrize.lib.manual.gui.GuiManual;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.IDataMachineGui;
import pl.pabilo8.immersiveintelligence.client.gui.ITabbedGui;
import pl.pabilo8.immersiveintelligence.client.gui.elements.GuiWidgetManualWrapper;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonTab;
import pl.pabilo8.immersiveintelligence.client.gui.elements.label.GuiLabelNoShadow;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.DataInputMachine;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageGuiNBT;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * <p>
 * Created by Pabilo8 on 30-06-2019.
 * Some code you write, some you steal, but in most cases you adapt the existing solutions...
 * </p>
 *
 * <p>
 * Edited by the same Pabilo8 on 30.08.2021
 * Most of the code you write, little you steal, but in most cases you improve on what you have created...
 * </p>
 */
public class GuiDataInputMachineBase extends GuiIEContainerBase implements ITabbedGui, IDataMachineGui
{
	protected static final ResourceLocation TEXTURE_STORAGE = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/gui/data_input_machine_inventory.png");
	protected static final ResourceLocation TEXTURE_EDIT = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/gui/data_input_machine_editing.png");
	protected static final ResourceLocation TEXTURE_VARIABLES = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/gui/data_input_machine.png");
	private final IIGuiList thisGui;
	public final LinkedHashMap<GuiButtonTab, IIGuiList> TABS = new LinkedHashMap<>();
	protected String title = I18n.format("tile.immersiveintelligence.metal_multiblock.data_input_machine.name");
	private final ResourceLocation thisTexture;

	protected ClientProxy proxy = (ClientProxy)ImmersiveIntelligence.proxy;
	protected TileEntityDataInputMachine tile;
	protected InventoryPlayer playerInv;

	private GuiManual trueManual;
	public GuiWidgetManualWrapper sideManual = null;
	protected GuiButtonState manualButton;

	private GuiButtonIE sendPacketButton;

	DataPacket list;
	//this is due to the machine sending a close door message to the server and producing an annoying sound
	protected boolean preparedForChange = false;

	public GuiDataInputMachineBase(EntityPlayer player, TileEntityDataInputMachine tile, IIGuiList gui)
	{
		super(new ContainerDataInputMachine(player, tile, gui==IIGuiList.GUI_DATA_INPUT_MACHINE_STORAGE));
		this.thisTexture = gui==IIGuiList.GUI_DATA_INPUT_MACHINE_STORAGE?TEXTURE_STORAGE: gui==IIGuiList.GUI_DATA_INPUT_MACHINE_VARIABLES?TEXTURE_VARIABLES: TEXTURE_EDIT;
		this.ySize = 222;
		this.playerInv = player.inventory;
		this.thisGui = gui;
		this.tile = tile;
		this.list = tile.storedData;
		refreshStoredData();
	}

	@Override
	public void initGui()
	{
		boolean isStorage = thisGui==IIGuiList.GUI_DATA_INPUT_MACHINE_STORAGE;
		IIPacketHandler.sendToServer(new MessageBooleanAnimatedPartsSync(isStorage, 0, tile.getPos()));
		IIPacketHandler.sendToServer(new MessageBooleanAnimatedPartsSync(!isStorage, 1, tile.getPos()));

		buttonList.clear();
		labelList.clear();
		TABS.clear();

		super.initGui();
		refreshStoredData();

		addLabel(4, 8, IIReference.COLOR_H1, title);

		addTab(IIGuiList.GUI_DATA_INPUT_MACHINE_STORAGE, "storage_module");
		addTab(IIGuiList.GUI_DATA_INPUT_MACHINE_VARIABLES, "variables_module");

		trueManual = ManualHelper.getManual().getGui();
		if(trueManual==null||trueManual instanceof GuiWidgetManualWrapper)
			trueManual = new GuiManual(ManualHelper.getManual(), ManualHelper.getManual().texture);

		sideManual = new GuiWidgetManualWrapper(trueManual, guiLeft+xSize-20, guiTop, proxy.storedGuiData.getBoolean("manual"));

		manualButton = addButton(new GuiButtonState(buttonList.size(),
				0, guiTop+56, 32, 18, "", proxy.storedGuiData.getBoolean("manual"), TEXTURE_STORAGE.toString(), 176, 96, -1));

		sendPacketButton = addButton(new GuiButtonIE(buttonList.size(), guiLeft-28, guiTop+98, 28, 24, "", TEXTURE_STORAGE.toString(), 176, 114))
				.setHoverOffset(28, 0);

	}

	@Override
	protected void actionPerformed(@Nonnull GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		if(button==sendPacketButton)
		{
			//nothing to send
			if(list.toNBT().hasNoTags())
				return;
			IIPacketHandler.sendToServer(new MessageIITileSync(tile, EasyNBT.newNBT().withBoolean("send_packet", true)));
		}
		else if(button==manualButton)
			saveBasicData();
		else if(button instanceof GuiButtonTab)
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
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);

		drawPunchtapeProgress();

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
		TABS.keySet().stream().filter(GuiButtonTab::isMouseOver).findFirst().ifPresent(tab -> tooltip.add(tab.displayString));
		if(this.sendPacketButton.isMouseOver())
			tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"variable_send_packet"));
		else if(this.manualButton.isMouseOver())
			tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+(manualButton.state?"hide_manual_widget": "show_manual_widget")));
		return tooltip;
	}

	/**
	 * Draw the punchcard progress bar
	 */
	private void drawPunchtapeProgress()
	{
		IIClientUtils.bindTexture(TEXTURE_STORAGE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableLighting();
		this.drawTexturedModalRect(guiLeft+5, guiTop+44, 176, 48, 16, Math.round(48*(tile.productionProgress/DataInputMachine.timePunchtapeProduction)));

	}

	@Override
	public void onGuiClosed()
	{
		syncDataToServer();
		//Close the hatches
		if(!preparedForChange)
		{
			IIPacketHandler.sendToServer(new MessageBooleanAnimatedPartsSync(false, 0, tile.getPos()));
			IIPacketHandler.sendToServer(new MessageBooleanAnimatedPartsSync(false, 1, tile.getPos()));
		}
		super.onGuiClosed();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
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
		this.list = tile.storedData;
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

		tile.storedData = list.clone();
		IIPacketHandler.sendToServer(new MessageIITileSync(tile, EasyNBT.newNBT().withTag("variables", this.list.toNBT())));
	}

	protected void addTab(IIGuiList gui, String name)
	{
		final int vOffset = TABS.size()*24;
		GuiButtonTab button = new GuiButtonTab(buttonList.size(), guiLeft-28, guiTop+4+vOffset, 28, 24, thisGui==gui?204: 176, vOffset,
				TEXTURE_STORAGE, I18n.format(IIReference.DESCRIPTION_KEY+name));
		TABS.put(button, gui);
		addButton(button);
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

	@Override
	public void editVariable(char c, DataType type)
	{
		if(!list.variables.containsKey(c)||list.getPacketVariable(c).getClass()!=type.getClass())
			list.setVariable(c, type);

		//Save gui scroll, tile pos for validation
		saveBasicData();
		syncDataToServer();

		proxy.storedGuiData.setString("variableToEdit", String.valueOf(c));
		//Set variable and change gui
		refreshStoredData();
		syncDataToServer();

		preparedForChange = true;
		IIPacketHandler.sendToServer(new MessageGuiNBT(IIGuiList.GUI_DATA_INPUT_MACHINE_EDIT, tile));
	}
}
