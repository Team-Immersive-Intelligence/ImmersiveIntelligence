package pl.pabilo8.immersiveintelligence.client.gui.data_input_machine;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.gui.ITabbedGui;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.TileEntityDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.gui.data_input_machine.ContainerDataInputMachineVariables;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.MessageGuiNBT;

import java.util.ArrayList;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.dataInputMachine;

/**
 * Created by Pabilo8 on 30-06-2019.
 * Some code you write, some you steal, but in most cases you adapt the existing solutions...
 */
public class GuiDataInputMachineVariables extends GuiIEContainerBase implements ITabbedGui
{
	public static final String texture_storage = ImmersiveIntelligence.MODID+":textures/gui/data_input_machine_inventory.png";
	public static final String texture_variables = ImmersiveIntelligence.MODID+":textures/gui/data_input_machine.png";

	public int scroll;
	public int maxScroll;

	public TileEntityDataInputMachine tile;
	public InventoryPlayer playerInv;
	private boolean wasDown = false;
	public ContainerDataInputMachineVariables container;

	DataPacket list;

	//It was necessary to make the Gui control the Container
	public GuiDataInputMachineVariables(InventoryPlayer inventoryPlayer, TileEntityDataInputMachine tile)
	{
		//Tricky, but definitely doable!
		super(new ContainerDataInputMachineVariables(inventoryPlayer, tile));

		this.ySize = 222;
		this.playerInv = inventoryPlayer;
		this.tile = tile;
		this.container = (ContainerDataInputMachineVariables)this.inventorySlots;

		this.list = tile.storedData;

		refreshStoredData();

		ClientProxy proxy = (ClientProxy)ImmersiveIntelligence.proxy;
		if(positionEqual(proxy, tile))
		{
			if(proxy.storedGuiData.hasKey("scrollPercent"))
				scroll = Math.round(proxy.storedGuiData.getFloat("scrollPercent")*maxScroll);
		}

		refreshStoredData();
	}

	@Override
	public void initGui()
	{
		IIPacketHandler.INSTANCE.sendToServer(new MessageBooleanAnimatedPartsSync(false, 0, tile.getPos()));
		IIPacketHandler.INSTANCE.sendToServer(new MessageBooleanAnimatedPartsSync(true, 1, tile.getPos()));

		super.initGui();
		refreshStoredData();
		this.buttonList.clear();

		this.buttonList.add(new GuiButtonIE(0, guiLeft-28, guiTop+4, 28, 24, "", texture_storage, 176, 0).setHoverOffset(28, 0));
		this.buttonList.add(new GuiButtonIE(1, guiLeft-28, guiTop+28, 28, 24, "", texture_storage, 204, 24));
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		NBTTagCompound tag = new NBTTagCompound();

		if(button.id==0)
		{
			syncDataToServer();
			IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_DATA_INPUT_MACHINE_STORAGE, tile.getPos(), playerInv.player));
		}
		else if(button.id==1)
		{
			this.initGui();
		}
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRenderer.drawString(I18n.format("tile."+ImmersiveIntelligence.MODID+".metal_multiblock.data_input_machine.programming"), 4, 2, 0x0a0a0a);
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);

		ArrayList<String> tooltip = new ArrayList<String>();

		if(mx >= guiLeft-28&&mx < guiLeft&&my >= guiTop+4&&my < guiTop+28)
			tooltip.add(I18n.format(CommonProxy.description_key+"storage_module"));

		if(mx >= guiLeft-28&&mx < guiLeft&&my >= guiTop+28&&my < guiTop+56)
			tooltip.add(I18n.format(CommonProxy.description_key+"variables_module"));

		//Draw the punchcard progress bar
		GlStateManager.pushMatrix();

		mc.getTextureManager().bindTexture(new ResourceLocation(texture_storage));
		//ClientUtils.bindTexture();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableLighting();

		this.drawTexturedModalRect(guiLeft+5, guiTop+44, 176, 48, 16, Math.round(48*(tile.productionProgress/dataInputMachine.timePunchtapeProduction)));

		GlStateManager.popMatrix();


		GlStateManager.pushMatrix();

		//GLScissor forces the drawing functions to draw only inside given coordinates
		//https://www.khronos.org/registry/OpenGL-Refpages/es2.0/xhtml/glScissor.xml

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		scissor(guiLeft+32, guiTop+12, 128, 114);

		//GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int i = 0;
		for(char c : DataPacket.varCharacters)
		{
			if(list.variables.containsKey(c))
			{
				IDataType data = list.getPacketVariable(c);
				//Base
				int drawx = guiLeft+32;
				int drawy = guiTop+12+(i*24)-scroll;

				GL11.glPushMatrix();
				GlStateManager.disableLighting();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

				GlStateManager.enableAlpha();

				ClientUtils.bindTexture(texture_variables);

				this.drawTexturedModalRect(drawx, drawy, 0, 222, 128, 20);

				ClientUtils.bindTexture(data.textureLocation());

				//Variable type based effects

				this.drawTexturedModalRect(drawx, drawy, 0, data.getFrameOffset()*20, 8, 20);
				this.drawTexturedModalRect(drawx+52, drawy, 8, data.getFrameOffset()*20, 24, 20);
				this.drawTexturedModalRect(drawx+120, drawy, 32, data.getFrameOffset()*20, 8, 20);

				//Variable Type Symbol
				this.drawTexturedModalRect(drawx+22, drawy+2, 40, data.getFrameOffset()*20, 16, 16);

				ClientUtils.bindTexture(texture_variables);

				boolean hovered = isPointInRegion(drawx+93-guiLeft, drawy+5-guiTop, 32, 12, mx, my);

				//'Set' Button
				this.drawTexturedModalRect(drawx+93, drawy+5, hovered?32: 0, 242, 32, 12);
				this.fontRenderer.drawString(I18n.format(CommonProxy.description_key+(isShiftKeyDown()?"variable_remove": "variable_set")), drawx+96, drawy+7, hovered?Lib.COLOUR_I_ImmersiveOrange: 0xffffff, true);
				if(hovered)
				{
					if(!isShiftKeyDown())
					{
						onVariableEditButtonClick(c, true);
						tooltip.add(I18n.format(CommonProxy.description_key+(isShiftKeyDown()?"variable_remove_desc": "variable_set_desc")));
					}
					else
					{
						onVariableRemoveButtonClick(c, true);
					}
				}

				GL11.glPopMatrix();

				this.fontRenderer.drawString(I18n.format(CommonProxy.data_key+"datatype."+data.getName()), drawx+38, drawy+6, data.getTypeColour(), true);

				//Draw variable name (single character)
				this.fontRenderer.drawString(String.valueOf(c), drawx+8, drawy+5, Lib.COLOUR_I_ImmersiveOrange, true);


				i += 1;
			}
		}
		GL11.glPushMatrix();

		//Draw 'add' button
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(texture_variables);

		this.drawTexturedModalRect(guiLeft+87, guiTop+12+(i*24)-scroll, 137, 222, 18, 18);
		GL11.glPopMatrix();
		boolean hovered = isPointInRegion(87, 12+(i*24)-scroll, 18, 18, mx, my);

		this.fontRenderer.drawString("+", guiLeft+93, guiTop+17+(i*24)-scroll, hovered?Lib.COLOUR_I_ImmersiveOrange: 0xffffff, true);

		if(hovered)
		{
			tooltip.add(I18n.format(CommonProxy.description_key+"variable_add_desc"));
		}

		//Check for button click
		if(hovered&&!wasDown&&Mouse.isButtonDown(0))
		{
			boolean done = false;
			for(char c : DataPacket.varCharacters)
			{
				if(!list.variables.containsKey(c))
				{
					//Save gui scroll, tile pos for validation
					ClientProxy proxy = (ClientProxy)ImmersiveIntelligence.proxy;
					saveGuiData(proxy);
					proxy.storedGuiData.setString("variableToEdit", String.valueOf(c));
					//Set variable and change gui
					refreshStoredData();
					syncDataToServer();

					IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_DATA_INPUT_MACHINE_EDIT, tile.getPos(), playerInv.player));

					break;
				}
			}

		}

		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		GlStateManager.popMatrix();

		wasDown = Mouse.isButtonDown(0);

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, -1, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}

	@Override
	public void onGuiClosed()
	{
		syncDataToServer();
		IIPacketHandler.INSTANCE.sendToServer(new MessageBooleanAnimatedPartsSync(false, 0, tile.getPos()));
		IIPacketHandler.INSTANCE.sendToServer(new MessageBooleanAnimatedPartsSync(false, 1, tile.getPos()));
		super.onGuiClosed();
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(texture_variables);

		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		int mouseChange = Mouse.getDWheel();
		scroll -= Integer.signum(mouseChange)*40;

		if((isPointInRegion(161, 12, 9, 114, mx, my))&&Mouse.isButtonDown(0))
		{
			scroll = (int)((my-(15./2)-guiTop-10.)*maxScroll)/114;
		}

		scroll = Math.min(scroll, maxScroll);
		scroll = Math.max(0, scroll);

		this.drawTexturedModalRect(guiLeft+161, guiTop+12+(int)(100*(scroll*1./maxScroll)), 128, 222, 9, 14);

	}

	//Used to refresh gui variables after one of the variables is changed
	void refreshStoredData()
	{
		maxScroll = (Math.max(list.variables.size()-4, 0))*24;
		wasDown = false;

		tile = (TileEntityDataInputMachine)tile.getWorld().getTileEntity(tile.getPos());
		this.list = tile.storedData;
	}

	void syncDataToServer()
	{
		NBTTagCompound tag = new NBTTagCompound();

		tag.setTag("variables", this.list.toNBT());

		if(tile==null)
		{
			return;
		}
		if(!list.toNBT().hasNoTags())
		{
			ImmersiveEngineering.packetHandler.sendToServer(new MessageTileSync(tile, tag));
		}
	}

	//Stolen from Flaxbeard (https://github.com/Flaxbeard/QuestionablyImmersive/blob/dev/src/main/java/flaxbeard/questionablyimmersive/client/gui/GuiCokeOvenBattery.java)
	private void scissor(int x, int y, int xSize, int ySize)
	{
		ScaledResolution res = new ScaledResolution(mc);
		x = x*res.getScaleFactor();
		ySize = ySize*res.getScaleFactor();
		y = mc.displayHeight-(y*res.getScaleFactor())-ySize;
		xSize = xSize*res.getScaleFactor();
		GL11.glScissor(x, y, xSize, ySize);
	}

	void onVariableEditButtonClick(char variable, boolean hovered)
	{
		//Check for button click
		if(hovered&&!wasDown&&Mouse.isButtonDown(0)&&list.variables.containsKey(variable))
		{
			refreshStoredData();
			syncDataToServer();

			ClientProxy proxy = (ClientProxy)ImmersiveIntelligence.proxy;
			saveGuiData(proxy);
			proxy.storedGuiData.setString("variableToEdit", String.valueOf(variable));

			//Set variable and change gui
			refreshStoredData();
			syncDataToServer();

			IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_DATA_INPUT_MACHINE_EDIT, tile.getPos(), playerInv.player));

		}
	}

	void onVariableRemoveButtonClick(char variable, boolean hovered)
	{
		//Check for button click
		if(hovered&&!wasDown&&Mouse.isButtonDown(0)&&list.variables.containsKey(variable))
		{
			list.removeVariable(variable);
			syncDataToServer();
			maxScroll = (Math.max(list.variables.size()-4, 0))*24;
			wasDown = false;
		}
	}

	void saveGuiData(ClientProxy proxy)
	{
		//I am absolutely 100% sure its client. How would a server even come here after all this rendering and proxy doohickeys?!
		//But this is actually forge, so i should be kind of cautious... everything can happen ^^.

		proxy.storedGuiData = new NBTTagCompound();
		saveBasicData(proxy, tile);
		if(maxScroll!=0)
			proxy.storedGuiData.setFloat("scrollPercent", scroll/maxScroll);
	}
}
