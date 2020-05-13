package pl.pabilo8.immersiveintelligence.client.gui.data_input_machine;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.DataInputMachine;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.gui.ITabbedGui;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.gui.data_input_machine.ContainerDataInputMachineVariables;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.MessageGuiNBT;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Pabilo8 on 30-06-2019.
 * Some code you write, some you steal, but in most cases you adapt the existing solutions...
 */
public class GuiDataInputMachineEdit extends GuiIEContainerBase implements ITabbedGui
{
	public static final String texture_storage = ImmersiveIntelligence.MODID+":textures/gui/data_input_machine_inventory.png";
	public static final String texture_edit = ImmersiveIntelligence.MODID+":textures/gui/data_input_machine_editing.png";

	public TileEntityDataInputMachine tile;
	public InventoryPlayer playerInv;
	private boolean wasDown = false;

	public char variableToEdit = 'a';
	public IDataType dataType;

	private GuiTextField valueEdit;
	private boolean editedstate = true;
	public ContainerDataInputMachineVariables container;

	DataPacket list;

	//It was necessary to make the Gui control the Container
	public GuiDataInputMachineEdit(InventoryPlayer inventoryPlayer, TileEntityDataInputMachine tile)
	{
		//Tricky, but definitely doable!
		super(new ContainerDataInputMachineVariables(inventoryPlayer, tile));

		this.ySize = 222;
		this.playerInv = inventoryPlayer;
		this.tile = tile;

		this.list = tile.storedData;
		this.container = (ContainerDataInputMachineVariables)this.inventorySlots;

		ClientProxy proxy = (ClientProxy)ImmersiveIntelligence.proxy;
		if(positionEqual(proxy, tile))
		{
			if(proxy.storedGuiData.hasKey("variableToEdit"))
				variableToEdit = proxy.storedGuiData.getString("variableToEdit").charAt(0);
		}

		refreshStoredData();
	}

	@Override
	public void initGui()
	{
		valueEdit = null;

		IIPacketHandler.INSTANCE.sendToServer(new MessageBooleanAnimatedPartsSync(false, 0, tile.getPos()));
		IIPacketHandler.INSTANCE.sendToServer(new MessageBooleanAnimatedPartsSync(true, 1, tile.getPos()));

		super.initGui();
		Keyboard.enableRepeatEvents(true);
		refreshStoredData();
		this.buttonList.clear();

		this.buttonList.add(new GuiButtonIE(0, guiLeft-28, guiTop+4, 28, 24, "", texture_storage, 176, 0).setHoverOffset(28, 0));
		this.buttonList.add(new GuiButtonIE(1, guiLeft-28, guiTop+28, 28, 24, "", texture_storage, 204, 24));

		this.buttonList.add(new GuiButtonIE(2, guiLeft+96, guiTop+121, 64, 12, I18n.format(CommonProxy.description_key+"variable_apply"), texture_edit, 0, 222).setHoverOffset(64, 0));

		//Type Change Buttons
		this.buttonList.add(new GuiButtonIE(3, guiLeft+52, guiTop+15, 8, 6, "", texture_edit, 128, 222).setHoverOffset(8, 0));
		this.buttonList.add(new GuiButtonIE(4, guiLeft+52, guiTop+26, 8, 6, "", texture_edit, 128, 228).setHoverOffset(8, 0));

		//Type dependant variable buttons / text fields

		switch(dataType.getName())
		{
			case "null":
			{

			}
			break;
			case "boolean":
			{
				editedstate = ((DataPacketTypeBoolean)dataType).value;
				this.buttonList.add(new GuiButtonState(5, guiLeft+42+fontRenderer.getStringWidth(I18n.format(CommonProxy.description_key+"variable_value")), guiTop+48, 48, 12, I18n.format(CommonProxy.data_key+"datatype.boolean.true"), !editedstate, texture_edit, 0, 234, 0).setHoverOffset(48, 0));
				this.buttonList.add(new GuiButtonState(6, guiLeft+90+fontRenderer.getStringWidth(I18n.format(CommonProxy.description_key+"variable_value")), guiTop+48, 48, 12, I18n.format(CommonProxy.data_key+"datatype.boolean.false"), editedstate, texture_edit, 0, 234, 0).setHoverOffset(48, 0));
				GuiButtonState b1 = ((GuiButtonState)buttonList.get(5));
				GuiButtonState b2 = ((GuiButtonState)buttonList.get(6));

				b1.textOffset = new int[]{(b1.width/2)-(fontRenderer.getStringWidth(b1.displayString)/2), b1.height/2-4};
				b2.textOffset = new int[]{(b2.width/2)-(fontRenderer.getStringWidth(b1.displayString)/2), b2.height/2-4};
			}
			break;
			case "integer":
			{
				this.valueEdit = new GuiTextField(5, this.fontRenderer, guiLeft+42+fontRenderer.getStringWidth(I18n.format(CommonProxy.description_key+"variable_value")), guiTop+48, 121-fontRenderer.getStringWidth(I18n.format(CommonProxy.description_key+"variable_value")), 20);
				this.valueEdit.setFocused(true);
				this.valueEdit.setText(dataType.valueToString());
			}
			break;
			case "string":
			{
				this.valueEdit = new GuiTextField(5, this.fontRenderer, guiLeft+36, guiTop+60, 128, 60);
				this.valueEdit.setFocused(true);
				this.valueEdit.setMaxStringLength(512);
				this.valueEdit.setText(dataType.valueToString());
			}
			break;
		}

	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		if(valueEdit!=null)
			this.valueEdit.updateCursorCounter();
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
			syncDataToServer();
			IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_DATA_INPUT_MACHINE_VARIABLES, tile.getPos(), playerInv.player));
		}

		else if(button.id==2)
		{
			switch(dataType.getName())
			{
				case "null":
				{
					list.setVariable(variableToEdit, new DataPacketTypeNull());
				}
				break;
				case "boolean":
				{
					list.setVariable(variableToEdit, new DataPacketTypeBoolean(editedstate));
				}
				break;
				case "integer":
				{
					if(NumberUtils.isParsable(valueEdit.getText()))
					{
						list.setVariable(variableToEdit, new DataPacketTypeInteger((int)Math.round(Double.parseDouble(valueEdit.getText()))));
					}
					else
					{
						list.setVariable(variableToEdit, new DataPacketTypeInteger(0));
					}
				}
				break;
				case "string":
				{
					list.setVariable(variableToEdit, new DataPacketTypeString(valueEdit.getText()));
				}
				break;
			}

			syncDataToServer();
			initGui();
			IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_DATA_INPUT_MACHINE_VARIABLES, tile.getPos(), playerInv.player));
		}
		else if(button.id==3)
		{
			if(!isShiftKeyDown())
				switchType(true);
			else
			{
				switchLetter(true);
			}
		}
		else if(button.id==4)
		{
			if(!isShiftKeyDown())
				switchType(false);
			else
			{
				switchLetter(false);
			}
		}

		if(dataType.getName()=="boolean")
		{
			if(button.id==5)
			{
				((GuiButtonState)buttonList.get(5)).state = false;
				((GuiButtonState)buttonList.get(6)).state = true;
				editedstate = true;
			}
			else if(button.id==6)
			{
				((GuiButtonState)buttonList.get(5)).state = true;
				((GuiButtonState)buttonList.get(6)).state = false;
				editedstate = false;
			}
		}
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRenderer.drawString(I18n.format("tile."+ImmersiveIntelligence.MODID+".metal_multiblock.data_input_machine.edit"), 4, 2, 0x0a0a0a);
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException
	{
		if(valueEdit!=null)
		{
			this.valueEdit.textboxKeyTyped(par1, par2);
			if(!(par2==Keyboard.KEY_E&&this.valueEdit.isFocused()))
				super.keyTyped(par1, par2);
		}
		else
		{
			super.keyTyped(par1, par2);
		}
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

		this.drawTexturedModalRect(guiLeft+5, guiTop+44, 176, 48, 16, Math.round(48*(tile.productionProgress/DataInputMachine.timePunchtapeProduction)));

		GlStateManager.popMatrix();

		wasDown = Mouse.isButtonDown(0);

		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		GlStateManager.enableAlpha();

		ClientUtils.bindTexture(dataType.textureLocation());

		this.drawTexturedModalRect(guiLeft+152, guiTop+15, 40, dataType.getFrameOffset()*20, 16, 16);

		ClientUtils.bindTexture(texture_edit);

		//Variable name
		this.fontRenderer.drawString(String.valueOf(variableToEdit), guiLeft+38, guiTop+19, Lib.COLOUR_I_ImmersiveOrange, true);

		//Type:
		this.fontRenderer.drawString(I18n.format(CommonProxy.description_key+"variable_type"), guiLeft+62, guiTop+19, 0x0a0a0a, false);

		//Variable type
		this.fontRenderer.drawString(" "+I18n.format(CommonProxy.data_key+"datatype."+dataType.getName()), guiLeft+62+fontRenderer.getStringWidth(I18n.format(CommonProxy.description_key+"variable_type")), guiTop+19, dataType.getTypeColour(), true);

		//Title
		this.fontRenderer.drawString(I18n.format(CommonProxy.description_key+"variable_properties"), guiLeft+100-(fontRenderer.getStringWidth(I18n.format(CommonProxy.description_key+"variable_properties"))/2), guiTop+36, 0x0a0a0a, false);

		//Type Dependant
		switch(dataType.getName())
		{
			case "null":
			{

			}
			break;
			case "boolean":
			{
				this.fontRenderer.drawString(I18n.format(CommonProxy.description_key+"variable_value"), guiLeft+40, guiTop+50, 0x0a0a0a, false);
			}
			break;
			case "integer":
			case "string":
			{
				this.fontRenderer.drawString(I18n.format(CommonProxy.description_key+"variable_value"), guiLeft+40, guiTop+50, 0x0a0a0a, false);
				this.valueEdit.drawTextBox();
			}
			break;
		}

		GlStateManager.popMatrix();

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

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if(dataType.getName().equals("string")||dataType.getName().equals("integer"))
			this.valueEdit.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(texture_edit);

		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	//Used to refresh gui variables after one of the variables is changed
	void refreshStoredData()
	{
		wasDown = false;

		tile = (TileEntityDataInputMachine)tile.getWorld().getTileEntity(tile.getPos());
		this.list = tile.storedData;

		this.dataType = list.getPacketVariable(variableToEdit);
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

	void switchType(boolean forward)
	{
		if(forward)
		{
			if(dataType.getName()=="null")
				list.setVariable(variableToEdit, new DataPacketTypeBoolean());
			else if(dataType.getName()=="boolean")
				list.setVariable(variableToEdit, new DataPacketTypeInteger());
			else if(dataType.getName()=="integer")
				list.setVariable(variableToEdit, new DataPacketTypeString());
			else if(dataType.getName()=="string")
				list.setVariable(variableToEdit, new DataPacketTypeNull());
		}
		else
		{
			if(dataType.getName()=="null")
				list.setVariable(variableToEdit, new DataPacketTypeString());
			else if(dataType.getName()=="boolean")
				list.setVariable(variableToEdit, new DataPacketTypeNull());
			else if(dataType.getName()=="integer")
				list.setVariable(variableToEdit, new DataPacketTypeBoolean());
			else if(dataType.getName()=="string")
				list.setVariable(variableToEdit, new DataPacketTypeInteger());
		}

		list.getPacketVariable(variableToEdit).setDefaultValue();

		syncDataToServer();
		initGui();
	}

	void switchLetter(boolean forward)
	{
		boolean done = false;

		int current_char = ArrayUtils.indexOf(DataPacket.varCharacters, variableToEdit);
		int repeats = DataPacket.varCharacters.length;

		for(int i = 0; i < repeats; i++)
		{
			if(!done)
			{
				current_char += forward?1: -1;
				if(current_char >= repeats)
					current_char = 0;
				if(current_char < 0)
					current_char = repeats-1;

				char c = DataPacket.varCharacters[current_char];

				if(!list.variables.containsKey(c))
				{
					done = true;
					list.setVariable(c, list.getPacketVariable(variableToEdit));
					list.removeVariable(variableToEdit);
					variableToEdit = c;
				}
			}
		}

		syncDataToServer();
		initGui();
	}

}
