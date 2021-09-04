package pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.operators.arithmetic.DataOperationAdd;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.gui.ITabbedGui;
import pl.pabilo8.immersiveintelligence.client.gui.elements.GuiButtonItemAdvanced;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.gui.arithmetic_logic_machine.ContainerArithmeticLogicMachineEdit;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIFunctionalCircuit;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.MessageGuiNBT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Pabilo8 on 30-06-2019.
 * Some code you write, some you steal, but in most cases you adapt the existing solutions...
 */
public class GuiArithmeticLogicMachineEdit extends GuiIEContainerBase implements ITabbedGui
{
	public static final String texture_storage = ImmersiveIntelligence.MODID+":textures/gui/arithmetic_logic_machine_storage.png";
	public static final String texture_edit = ImmersiveIntelligence.MODID+":textures/gui/arithmetic_logic_machine_editing.png";

	public TileEntityArithmeticLogicMachine tile;
	public InventoryPlayer playerInv;
	public char expressionToEdit = 'a';
	public int editingPage = 0;
	public ContainerIEBase container;
	public IDataType currentlyEditeddataType;
	IItemHandler handler;
	int page;
	DataPacket list;
	IDataType mainType, type1, type2;
	private boolean wasDown = false;
	private GuiTextField valueEdit;
	private boolean editedstate = true;
	char expressionAllowed = ' ';

	public GuiArithmeticLogicMachineEdit(InventoryPlayer inventoryPlayer, TileEntityArithmeticLogicMachine tile, int page)
	{
		//Tricky, but definitely doable!
		super(new ContainerArithmeticLogicMachineEdit(inventoryPlayer, tile));

		this.ySize = 222;
		this.playerInv = inventoryPlayer;
		this.tile = tile;
		this.container = (ContainerIEBase)this.inventorySlots;

		this.handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		this.page = page;
		if(!handler.getStackInSlot(page).isEmpty())
			this.list = ((ItemIIFunctionalCircuit)handler.getStackInSlot(page).getItem()).getStoredData(handler.getStackInSlot(page));
		else
			this.list = new DataPacket();

		ClientProxy proxy = (ClientProxy)ImmersiveIntelligence.proxy;
		if(positionEqual(proxy, tile))
		{
			if(proxy.storedGuiData.hasKey("expressionToEdit"))
				expressionToEdit = proxy.storedGuiData.getString("expressionToEdit").charAt(0);

			if(this.list.getPacketVariable(expressionToEdit).getName().equals("expression"))
			{
				mainType = this.list.getPacketVariable(expressionToEdit);
				type1 = ((DataPacketTypeExpression)mainType).getType1();
				type2 = ((DataPacketTypeExpression)mainType).getType2();
				expressionAllowed = ((DataPacketTypeExpression)mainType).getRequiredVariable();
			}
		}

		refreshStoredData();
		editingPage = 0;
		currentlyEditeddataType = mainType;

	}

	@Override
	public void initGui()
	{
		valueEdit = null;

		IIPacketHandler.INSTANCE.sendToServer(new MessageBooleanAnimatedPartsSync(true, 0, tile.getPos()));

		super.initGui();
		Keyboard.enableRepeatEvents(true);
		refreshStoredData();
		this.buttonList.clear();

		this.buttonList.add(new GuiButtonIE(0, guiLeft-28, guiTop+4, 28, 24, "", texture_storage, 204, 0));
		if(!handler.getStackInSlot(0).isEmpty())
			this.buttonList.add(new GuiButtonItemAdvanced(1, guiLeft-28, guiTop+28, 28, 24, texture_storage, 176, 24, handler.getStackInSlot(0), 6, 2).setHoverOffset(28, 0));
		if(!handler.getStackInSlot(1).isEmpty())
			this.buttonList.add(new GuiButtonItemAdvanced(2, guiLeft-28, guiTop+52, 28, 24, texture_storage, 176, 24, handler.getStackInSlot(1), 6, 2).setHoverOffset(28, 0));
		if(!handler.getStackInSlot(2).isEmpty())
			this.buttonList.add(new GuiButtonItemAdvanced(3, guiLeft-28, guiTop+76, 28, 24, texture_storage, 176, 24, handler.getStackInSlot(2), 6, 2).setHoverOffset(28, 0));
		if(!handler.getStackInSlot(3).isEmpty())
			this.buttonList.add(new GuiButtonItemAdvanced(4, guiLeft-28, guiTop+100, 28, 24, texture_storage, 176, 24, handler.getStackInSlot(3), 6, 2).setHoverOffset(28, 0));

		this.buttonList.add(new GuiButtonIE(5, guiLeft+96, guiTop+121, 64, 12, I18n.format(CommonProxy.DESCRIPTION_KEY+"variable_apply"), texture_edit, 0, 222).setHoverOffset(64, 0));

		//Type Change Buttons
		this.buttonList.add(new GuiButtonIE(6, guiLeft+10, guiTop+52, 8, 6, "", texture_edit, 128, 222).setHoverOffset(8, 0));
		this.buttonList.add(new GuiButtonIE(7, guiLeft+10, guiTop+80, 8, 6, "", texture_edit, 128, 228).setHoverOffset(8, 0));

		this.buttonList.add(new GuiButtonIE(8, guiLeft+64, guiTop+121, 12, 12, "", texture_edit, 96, 234).setHoverOffset(12, 0));
		this.buttonList.add(new GuiButtonIE(9, guiLeft+80, guiTop+121, 12, 12, "", texture_edit, 120, 234).setHoverOffset(12, 0));

		GuiButtonState gbs1 = new GuiButtonState(10, guiLeft+48, guiTop+121, 12, 12, "@", currentlyEditeddataType.getName().equals("accessor"), texture_edit, 168, 234, 0);
		gbs1.textOffset = new int[]{(gbs1.width/2)-(fontRenderer.getStringWidth(gbs1.displayString)/2), gbs1.height/2-4};
		this.buttonList.add(gbs1);

		if(handler.getStackInSlot(page).isEmpty())
		{
			IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE, tile.getPos(), playerInv.player));
		}

		//Type dependant variable buttons / text fields
		if(editingPage==0)
		{

			this.buttonList.add(new GuiButtonIE(11, guiLeft+42+fontRenderer.getStringWidth(I18n.format(CommonProxy.DESCRIPTION_KEY+"operation")), guiTop+48, 8, 6, "", texture_edit, 128, 222).setHoverOffset(8, 0));
			this.buttonList.add(new GuiButtonIE(12, guiLeft+42+fontRenderer.getStringWidth(I18n.format(CommonProxy.DESCRIPTION_KEY+"operation")), guiTop+54, 8, 6, "", texture_edit, 128, 228).setHoverOffset(8, 0));

			this.buttonList.add(new GuiButtonIE(13, guiLeft+42+fontRenderer.getStringWidth(I18n.format(CommonProxy.DESCRIPTION_KEY+"conditional_variable")), guiTop+64, 8, 6, "", texture_edit, 128, 222).setHoverOffset(8, 0));
			this.buttonList.add(new GuiButtonIE(14, guiLeft+42+fontRenderer.getStringWidth(I18n.format(CommonProxy.DESCRIPTION_KEY+"conditional_variable")), guiTop+70, 8, 6, "", texture_edit, 128, 228).setHoverOffset(8, 0));

		}
		else
		{
			switch(currentlyEditeddataType.getName())
			{
				case "null":
				{

				}
				break;
				case "boolean":
				{
					editedstate = ((DataPacketTypeBoolean)currentlyEditeddataType).value;
					this.buttonList.add(new GuiButtonState(11, guiLeft+42+fontRenderer.getStringWidth(I18n.format(CommonProxy.DESCRIPTION_KEY+"variable_value")), guiTop+48, 48, 12, I18n.format(CommonProxy.DATA_KEY+"datatype.boolean.true"), !editedstate, texture_edit, 0, 234, 0).setHoverOffset(48, 0));
					this.buttonList.add(new GuiButtonState(12, guiLeft+90+fontRenderer.getStringWidth(I18n.format(CommonProxy.DESCRIPTION_KEY+"variable_value")), guiTop+48, 48, 12, I18n.format(CommonProxy.DATA_KEY+"datatype.boolean.false"), editedstate, texture_edit, 0, 234, 0).setHoverOffset(48, 0));

					GuiButtonState b1 = ((GuiButtonState)this.getGuiButtonById(11));
					GuiButtonState b2 = ((GuiButtonState)this.getGuiButtonById(12));

					b1.textOffset = new int[]{(b1.width/2)-(fontRenderer.getStringWidth(b1.displayString)/2), b1.height/2-4};
					b2.textOffset = new int[]{(b2.width/2)-(fontRenderer.getStringWidth(b1.displayString)/2), b2.height/2-4};
				}
				break;
				case "integer":
				{
					this.valueEdit = new GuiTextField(11, this.fontRenderer, guiLeft+42+fontRenderer.getStringWidth(I18n.format(CommonProxy.DESCRIPTION_KEY+"variable_value")), guiTop+48, 121-fontRenderer.getStringWidth(I18n.format(CommonProxy.DESCRIPTION_KEY+"variable_value")), 20);
					this.valueEdit.setFocused(true);
					this.valueEdit.setText(currentlyEditeddataType.valueToString());
				}
				break;
				case "string":
				{
					this.valueEdit = new GuiTextField(11, this.fontRenderer, guiLeft+36, guiTop+60, 128, 60);
					this.valueEdit.setFocused(true);
					this.valueEdit.setMaxStringLength(512);
					this.valueEdit.setText(currentlyEditeddataType.valueToString());
				}
				break;
				case "accessor":
				{
					this.buttonList.add(new GuiButtonIE(11, guiLeft+42+fontRenderer.getStringWidth(I18n.format(CommonProxy.DESCRIPTION_KEY+"operation")), guiTop+48, 8, 6, "", texture_edit, 128, 222).setHoverOffset(8, 0));
					this.buttonList.add(new GuiButtonIE(12, guiLeft+42+fontRenderer.getStringWidth(I18n.format(CommonProxy.DESCRIPTION_KEY+"operation")), guiTop+54, 8, 6, "", texture_edit, 128, 228).setHoverOffset(8, 0));
				}
				break;
			}

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
		if(button.id==0)
		{
			syncDataToServer();
			IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE, tile.getPos(), playerInv.player));
		}
		else if(button.id==1)
		{
			syncDataToServer();
			IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_1, tile.getPos(), playerInv.player));
		}
		else if(button.id==2)
		{
			syncDataToServer();
			IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_2, tile.getPos(), playerInv.player));
		}
		else if(button.id==3)
		{
			syncDataToServer();
			IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_3, tile.getPos(), playerInv.player));
		}
		else if(button.id==4)
		{
			syncDataToServer();
			IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_4, tile.getPos(), playerInv.player));
		}
		else if(button.id==5)
		{
			/*switch(mainType.getName())
			{

			}*/
			changeDataTypePage(true);

			list.setVariable(expressionToEdit, new DataPacketTypeExpression(type1, type2, ((DataPacketTypeExpression)mainType).getOperation(), expressionAllowed));

			syncDataToServer();

			if(page==0)
				IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_1, tile.getPos(), playerInv.player));
			else if(page==1)
				IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_2, tile.getPos(), playerInv.player));
			else if(page==2)
				IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_3, tile.getPos(), playerInv.player));
			else if(page==3)
				IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_4, tile.getPos(), playerInv.player));

		}
		else if(button.id==6)
		{
			switchLetter(true, false);
		}
		else if(button.id==7)
		{
			switchLetter(false, false);
		}
		else if(button.id==8)
		{
			changeDataTypePage(true);
		}
		else if(button.id==9)
		{
			changeDataTypePage(false);
		}
		else if(button.id==10)
		{
			switchType();
		}

		if(currentlyEditeddataType.getName().equals("expression"))
		{
			if(button.id==11)
			{
				changeOperation(true);
			}
			if(button.id==12)
			{
				changeOperation(false);
			}
			else if(button.id==13)
			{
				changeRequiredVariable(true);
			}
			else if(button.id==14)
			{
				changeRequiredVariable(false);
			}
		}
		else if(currentlyEditeddataType.getName().equals("accessor"))
		{
			if(button.id==11)
			{
				switchLetter(true, true);
			}
			else if(button.id==12)
			{
				switchLetter(false, true);
			}
		}
		else if(currentlyEditeddataType.getName().equals("boolean"))
		{
			if(button.id==11)
			{
				((GuiButtonState)this.getGuiButtonById(11)).state = false;
				((GuiButtonState)this.getGuiButtonById(12)).state = true;
				editedstate = true;
			}
			else if(button.id==12)
			{
				((GuiButtonState)this.getGuiButtonById(11)).state = true;
				((GuiButtonState)this.getGuiButtonById(12)).state = false;
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
		this.fontRenderer.drawString(I18n.format("tile."+ImmersiveIntelligence.MODID+".metal_multiblock.arithmetic_logic_machine.edit"), 4, 6, 0x0a0a0a);
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
			tooltip.add(I18n.format(CommonProxy.DESCRIPTION_KEY+"storage_module"));

		if(!handler.getStackInSlot(0).isEmpty()&&mx >= guiLeft-28&&mx < guiLeft&&my >= guiTop+28&&my < guiTop+52)
			tooltip.add(handler.getStackInSlot(0).getDisplayName());

		if(!handler.getStackInSlot(1).isEmpty()&&mx >= guiLeft-28&&mx < guiLeft&&my >= guiTop+52&&my < guiTop+76)
			tooltip.add(handler.getStackInSlot(1).getDisplayName());

		if(!handler.getStackInSlot(2).isEmpty()&&mx >= guiLeft-28&&mx < guiLeft&&my >= guiTop+76&&my < guiTop+100)
			tooltip.add(handler.getStackInSlot(2).getDisplayName());

		if(!handler.getStackInSlot(3).isEmpty()&&mx >= guiLeft-28&&mx < guiLeft&&my >= guiTop+100&&my < guiTop+124)
			tooltip.add(handler.getStackInSlot(3).getDisplayName());


		wasDown = Mouse.isButtonDown(0);

		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		GlStateManager.enableAlpha();

		ClientUtils.bindTexture(currentlyEditeddataType.textureLocation());

		this.drawTexturedModalRect(guiLeft+152, guiTop+15, 40, currentlyEditeddataType.getFrameOffset()*20, 16, 16);

		ClientUtils.bindTexture(texture_edit);

		//Variable name
		this.fontRenderer.drawString(String.valueOf(expressionToEdit), guiLeft+11, guiTop+64, Lib.COLOUR_I_ImmersiveOrange, true);

		//Type:
		this.fontRenderer.drawString(I18n.format(CommonProxy.DESCRIPTION_KEY+"variable_type"), guiLeft+62, guiTop+19, 0x0a0a0a, false);

		//Variable type
		this.fontRenderer.drawString(" "+I18n.format(CommonProxy.DATA_KEY+"datatype."+currentlyEditeddataType.getName()), guiLeft+62+fontRenderer.getStringWidth(I18n.format(CommonProxy.DESCRIPTION_KEY+"variable_type")), guiTop+19, currentlyEditeddataType.getTypeColour(), true);

		//Title
		this.fontRenderer.drawString(I18n.format(CommonProxy.DESCRIPTION_KEY+"variable_properties"), guiLeft+100-(fontRenderer.getStringWidth(I18n.format(CommonProxy.DESCRIPTION_KEY+"variable_properties"))/2), guiTop+33, 0x0a0a0a, false);

		//Type Dependant
		switch(currentlyEditeddataType.getName())
		{
			case "null":
			{

			}
			break;
			case "boolean":
			{
				this.fontRenderer.drawString(I18n.format(CommonProxy.DESCRIPTION_KEY+"variable_value"), guiLeft+40, guiTop+50, 0x0a0a0a, false);
			}
			break;
			case "integer":
			case "string":
			{
				this.fontRenderer.drawString(I18n.format(CommonProxy.DESCRIPTION_KEY+"variable_value"), guiLeft+40, guiTop+50, 0x0a0a0a, false);
				this.valueEdit.drawTextBox();
			}
			break;
			case "expression":
			{
				this.fontRenderer.drawString(I18n.format(CommonProxy.DESCRIPTION_KEY+"operation"), guiLeft+40, guiTop+50, 0x0a0a0a, false);
				this.fontRenderer.drawString(I18n.format(CommonProxy.DESCRIPTION_KEY+"conditional_variable"), guiLeft+40, guiTop+62, 0x0a0a0a, false);

				this.fontRenderer.drawString(I18n.format(CommonProxy.DATA_KEY+"function."+((DataPacketTypeExpression)currentlyEditeddataType).getOperation().name), guiLeft+52+fontRenderer.getStringWidth(I18n.format(CommonProxy.DESCRIPTION_KEY+"operation")), guiTop+50, Lib.COLOUR_I_ImmersiveOrange, false);
				this.fontRenderer.drawString(expressionAllowed==' '?I18n.format(CommonProxy.DESCRIPTION_KEY+"no_variable"): String.valueOf(expressionAllowed), guiLeft+52+fontRenderer.getStringWidth(I18n.format(CommonProxy.DESCRIPTION_KEY+"conditional_variable")), guiTop+62, Lib.COLOUR_I_ImmersiveOrange, true);
			}
			break;
			case "accessor":
			{
				this.fontRenderer.drawString(I18n.format(CommonProxy.DESCRIPTION_KEY+"variable"), guiLeft+40, guiTop+50, 0x0a0a0a, false);
				this.fontRenderer.drawString(currentlyEditeddataType.valueToString(), guiLeft+50+fontRenderer.getStringWidth(I18n.format(CommonProxy.DESCRIPTION_KEY+"variable")), guiTop+50, Lib.COLOUR_I_ImmersiveOrange, false);
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
		super.onGuiClosed();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if(currentlyEditeddataType.getName().equals("string")||currentlyEditeddataType.getName().equals("integer"))
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

		tile = (TileEntityArithmeticLogicMachine)tile.getWorld().getTileEntity(tile.getPos());

		if(this.mainType==null||!Objects.equals(this.mainType.getName(), "expression"))
		{
			this.mainType = list.getPacketVariable(expressionToEdit);

			this.mainType = new DataOperationAdd().getVarInType(DataPacketTypeExpression.class, mainType, list);

			ArrayList<String> ops = new ArrayList<>(((ItemIIFunctionalCircuit)handler.getStackInSlot(page).getItem()).getOperationsList(handler.getStackInSlot(page)));
			try
			{
				((DataPacketTypeExpression)mainType).setOperation((DataOperator)(DataOperation.operations.get(ops.get(0))).newInstance());
			} catch(InstantiationException e)
			{
				e.printStackTrace();
			} catch(IllegalAccessException e)
			{
				e.printStackTrace();
			}

			type1 = ((DataPacketTypeExpression)mainType).getType1();
			type2 = ((DataPacketTypeExpression)mainType).getType2();
			if(!Objects.equals(type1.getName(), "accessor")||type1.getClass()!=((DataPacketTypeExpression)mainType).getOperation().allowedType1)
			{
				type1 = new DataOperationAdd().getVarInType(((DataPacketTypeExpression)mainType).getOperation().allowedType1, mainType, list);
			}
			if(!Objects.equals(type2.getName(), "accessor")||type2.getClass()!=((DataPacketTypeExpression)mainType).getOperation().allowedType2)
			{
				type2 = new DataOperationAdd().getVarInType(((DataPacketTypeExpression)mainType).getOperation().allowedType2, mainType, list);
			}

		}


	}

	void changeDataTypePage(boolean forward)
	{
		setTypeButtonValue();
		switch(editingPage)
		{
			case 0:
			{
				mainType = currentlyEditeddataType;
			}
			break;
			case 1:
			{
				type2 = currentlyEditeddataType;
			}
			break;
			case 2:
			{
				type1 = currentlyEditeddataType;
			}
			break;
		}
		this.editingPage += forward?1: -1;
		if(editingPage > 2)
			editingPage = 0;
		if(editingPage < 0)
			editingPage = 2;
		switch(editingPage)
		{
			case 0:
			{
				currentlyEditeddataType = mainType;
			}
			break;
			case 1:
			{
				currentlyEditeddataType = type2;
			}
			break;
			case 2:
			{
				currentlyEditeddataType = type1;
			}
			break;
		}

		initGui();
	}

	void setTypeButtonValue()
	{
		switch(currentlyEditeddataType.getName())
		{
			case "boolean":
			{
				((DataPacketTypeBoolean)currentlyEditeddataType).value = editedstate;
			}
			break;
			case "integer":
			{
				if(NumberUtils.isParsable(valueEdit.getText()))
				{
					currentlyEditeddataType = new DataPacketTypeInteger((int)Math.round(Double.parseDouble(valueEdit.getText())));
				}
				else
				{
					currentlyEditeddataType.setDefaultValue();
				}

			}
			break;
			case "string":
			{
				((DataPacketTypeString)currentlyEditeddataType).value = valueEdit.getText();
			}
			break;
			//case "accessor": {((DataPacketTypeAccessor)currentlyEditeddataType).variable='a';} break;
		}
	}

	void syncDataToServer()
	{
		NBTTagCompound tag = new NBTTagCompound();

		tag.setTag("expressions", new NBTTagCompound());

		tag.getCompoundTag("expressions").setInteger("page", page);
		tag.getCompoundTag("expressions").setTag("list", list.toNBT());

		ImmersiveEngineering.packetHandler.sendToServer(new MessageTileSync(tile, tag));
	}

	void changeOperation(boolean forward)
	{
		ArrayList<String> ops = new ArrayList<>(((ItemIIFunctionalCircuit)handler.getStackInSlot(page).getItem()).getOperationsList(handler.getStackInSlot(page)));
		int index = 0;

		if(ops.contains(((DataPacketTypeExpression)mainType).getOperation().name))
		{
			index = ops.indexOf(((DataPacketTypeExpression)mainType).getOperation().name);
			index += forward?1: -1;
			if(index < 0)
				index = ops.size()-1;
			else if(index >= ops.size())
				index = 0;
		}

		try
		{
			((DataPacketTypeExpression)mainType).setOperation((DataOperator)(DataOperation.operations.get(ops.get(index))).newInstance());

			if(type1.getName()!="accessor")
				type1 = new DataOperationAdd().getVarInType(((DataPacketTypeExpression)mainType).getOperation().allowedType1, type1, list);
			if(type2.getName()!="accessor")
				type2 = new DataOperationAdd().getVarInType(((DataPacketTypeExpression)mainType).getOperation().allowedType2, type2, list);

		} catch(InstantiationException e)
		{
			e.printStackTrace();
		} catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	void switchType()
	{
		if(editingPage!=0)
		{
			if(Objects.equals(currentlyEditeddataType.getName(), "accessor"))
			{
				if(editingPage==1)
				{
					currentlyEditeddataType = new DataOperationAdd().getVarInType(((DataPacketTypeExpression)mainType).getOperation().allowedType1, type1, list);
					type1 = currentlyEditeddataType;
				}
				if(editingPage==2)
				{
					currentlyEditeddataType = new DataOperationAdd().getVarInType(((DataPacketTypeExpression)mainType).getOperation().allowedType2, type2, list);
					type2 = currentlyEditeddataType;
				}
			}
			else
			{
				currentlyEditeddataType = new DataPacketTypeAccessor();
			}
			currentlyEditeddataType.setDefaultValue();
		}
		syncDataToServer();
		initGui();
	}

	void switchLetter(boolean forward, boolean accessor)
	{
		boolean done = false;

		int current_char;

		if(!accessor)
			current_char = ArrayUtils.indexOf(DataPacket.varCharacters, expressionToEdit);
		else
			current_char = ArrayUtils.indexOf(DataPacket.varCharacters, ((DataPacketTypeAccessor)currentlyEditeddataType).variable);

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

				if(!list.variables.containsKey(c)&&!accessor)
				{
					done = true;
					list.setVariable(c, list.getPacketVariable(expressionToEdit));
					list.removeVariable(expressionToEdit);
					expressionToEdit = c;
				}
				else if(accessor)
				{
					done = true;
					((DataPacketTypeAccessor)currentlyEditeddataType).variable = c;
				}
			}
		}

		syncDataToServer();
		initGui();
	}

	void changeRequiredVariable(boolean forward)
	{
		if(expressionAllowed==' ')
		{
			if(forward)
				expressionAllowed = DataPacket.varCharacters[0];
			else
				expressionAllowed = DataPacket.varCharacters[DataPacket.varCharacters.length-1];
		}
		else
		{
			int current_char;

			current_char = ArrayUtils.indexOf(DataPacket.varCharacters, expressionAllowed);
			current_char += forward?1: -1;

			if(current_char >= DataPacket.varCharacters.length||current_char < 0)
				expressionAllowed = ' ';
			else
				expressionAllowed = DataPacket.varCharacters[current_char];
		}
	}

	private GuiButton getGuiButtonById(int targetId) {
		for (GuiButton button:this.buttonList) {
			if (button.id == targetId) {
				return button;
			}
		}

		// Probably should be an exception
		return null;
	}

}
