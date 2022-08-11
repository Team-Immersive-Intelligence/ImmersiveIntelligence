package pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonDataLetterList;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonDataLetterList.ArrowsAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonII;
import pl.pabilo8.immersiveintelligence.client.gui.elements.data_editor.GuiDataEditorExpression;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageGuiNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Pabilo8 on 30-06-2019.
 * Some code you write, some you steal, but in most cases you adapt the existing solutions...
 */
public class GuiArithmeticLogicMachineEdit extends GuiArithmeticLogicMachineBase
{
	private int page;
	public char variableToEdit = 'a';
	public IDataType dataType;
	public GuiButtonDataLetterList buttonLetter;
	public GuiButtonIE buttonApply;
	public GuiButtonIE buttonVariableHelp;

	@Nullable
	private GuiDataEditorExpression editor = null;

	public GuiArithmeticLogicMachineEdit(InventoryPlayer inventoryPlayer, TileEntityArithmeticLogicMachine tile)
	{
		super(inventoryPlayer, tile, IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_EDIT);
		refreshStoredData();
	}

	@Override
	public void initGui()
	{
		super.initGui();

		//Properties
		addLabel(43, 40, 115, 0, false, Utils.COLOR_H1, I18n.format("desc.immersiveintelligence.variable_properties")).setCentered();
		//Type:
		addLabel(61, 24, Utils.COLOR_H1, I18n.format("desc.immersiveintelligence.variable_type"));
		//Variable Type
		addLabel(152-fontRenderer.getStringWidth(I18n.format(CommonProxy.DATA_KEY+"datatype."+dataType.getName())),
				24, MathHelper.multiplyColor(dataType.getTypeColour(), 0xcacaca),
				I18n.format(CommonProxy.DATA_KEY+"datatype."+dataType.getName())
		);

		//Apply Button
		buttonApply = addButton(new GuiButtonIE(buttonList.size(), guiLeft+96, guiTop+121, 64, 12, I18n.format("desc.immersiveintelligence.variable_apply"), TEXTURE_EDIT.toString(), 0, 222).setHoverOffset(64, 0));

		//Displays Manual Page for Type
		buttonVariableHelp = addButton(new GuiButtonII(buttonList.size(), guiLeft+152, guiTop+15, 16, 16, "immersiveintelligence:textures/gui/data_types/expression.png", 0, 0, 1, 1));

		this.editor = addButton(new GuiDataEditorExpression(buttonList.size(),
				this.editor!=null?this.editor.outputType(): new DataPacket().getVarInType(DataTypeExpression.class, dataType), handler.getStackInSlot(page)));
		this.editor.setBounds(guiLeft+35, guiTop+46, 131, 80);

		//Letter Change Buttons
		buttonLetter = addButton(new GuiButtonDataLetterList(buttonList.size(), guiLeft+42-10, guiTop+14, false, variableToEdit, ArrowsAlignment.LEFT));
		buttonLetter.setAvoidGetter(this::getPacketFromPage);

	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		if(editor!=null)
			editor.update();
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(editor!=null&&editor.isFocused())
			editor.keyTyped(typedChar, keyCode);
		else
			super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void actionPerformed(@Nonnull GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		if(button==buttonLetter)
		{
			if(buttonLetter.selectedEntry!=variableToEdit)
				switchLetter();
		}
		else if(button==buttonApply)
		{
			if(this.editor!=null)
				this.dataType = this.editor.outputType();
			saveBasicData();
			syncDataToServer();
			preparedForChange=true;
			IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(getPage(page), tile.getPos(), playerInv.player));
		}

	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		super.drawGuiContainerBackgroundLayer(f, mx, my);

	}

	//Used to refresh gui variables after one of the variables is changed
	void refreshStoredData()
	{
		super.refreshStoredData();
		if(positionEqual(proxy, tile))
		{
			if(proxy.storedGuiData.hasKey("variableToEdit"))
			{
				page = proxy.storedGuiData.getInteger("circuitToEdit");
				variableToEdit = proxy.storedGuiData.getString("variableToEdit").charAt(0);
			}
		}
		DataPacket list = IIContent.itemCircuit.getStoredData(handler.getStackInSlot(page));
		if(list.variables.containsKey(variableToEdit))
			this.dataType = list.getPacketVariable(variableToEdit);
		else
			this.dataType = new DataTypeExpression();
	}

	public DataPacket getPacketFromPage()
	{
		return IIContent.itemCircuit.getStoredData(handler.getStackInSlot(page));
	}


	void switchLetter()
	{
		if(handler.getSlots() > page&&handler.getStackInSlot(page).isEmpty())
			return;

		DataPacket list = IIContent.itemCircuit.getStoredData(handler.getStackInSlot(page));
		if(!list.variables.containsKey(buttonLetter.selectedEntry))
		{
			list.setVariable(buttonLetter.selectedEntry, list.getPacketVariable(variableToEdit));
			list.removeVariable(variableToEdit);
			variableToEdit = buttonLetter.selectedEntry;
		}
		IIContent.itemCircuit.writeDataToItem(list, handler.getStackInSlot(page));

		syncDataToServer();
		initGui();
	}

	@Override
	public ArrayList<String> getTooltip(int mx, int my)
	{
		ArrayList<String> tooltip = super.getTooltip(mx, my);
		if(editor!=null)
			editor.getTooltip(tooltip,mx,my);
		return tooltip;
	}

	@Override
	protected void syncDataToServer()
	{
		proxy.storedGuiData.setString("variableToEdit", String.valueOf(variableToEdit));
		if(editor!=null)
		{
			DataPacket storedData = IIContent.itemCircuit.getStoredData(handler.getStackInSlot(page));
			storedData.setVariable(variableToEdit, editor.outputType());
			NBTTagCompound tag = new NBTTagCompound();
			NBTTagCompound expTag = new NBTTagCompound();

			expTag.setInteger("page", page);
			expTag.setTag("list", storedData.toNBT());
			tag.setTag("expressions", expTag);
			ImmersiveEngineering.packetHandler.sendToServer(new MessageTileSync(tile, tag));
		}

		super.syncDataToServer();
	}
}
