package pl.pabilo8.immersiveintelligence.client.gui.block.data_input_machine;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import blusunrize.lib.manual.IManualPage;
import blusunrize.lib.manual.ManualInstance.ManualEntry;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonDataLetterList;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonDataLetterList.ArrowsAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonII;
import pl.pabilo8.immersiveintelligence.client.gui.elements.data_editor.GuiDataEditor;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageGuiNBT;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Created by Pabilo8 on 30-06-2019.
 * Rework on 31.08.2021.
 */
public class GuiDataInputMachineEdit extends GuiDataInputMachineBase
{
	public char variableToEdit = 'a';
	public DataType dataType;
	public GuiButtonDataLetterList buttonLetter;
	public GuiButtonIE buttonApply;
	public GuiButtonIE buttonTypeNext, buttonTypePrev;
	private GuiButtonII buttonVariableHelp;
	@Nullable
	private GuiDataEditor<? extends DataType> editor = null;

	public GuiDataInputMachineEdit(EntityPlayer player, TileEntityDataInputMachine tile)
	{
		super(player, tile, IIGuiList.GUI_DATA_INPUT_MACHINE_EDIT);
		title = I18n.format("tile.immersiveintelligence.metal_multiblock.data_input_machine.edit");
		refreshStoredData();
	}

	@Override
	public void initGui()
	{
		super.initGui();

		//Properties
		addLabel(43, 40, 115, 0, false, IIReference.COLOR_H1, I18n.format("desc.immersiveintelligence.variable_properties")).setCentered();
		//Type:
		addLabel(61, 24, IIReference.COLOR_H1, I18n.format("desc.immersiveintelligence.variable_type"));
		//Variable Type
		addLabel(152-10-fontRenderer.getStringWidth(I18n.format(IIReference.DATA_KEY+"datatype."+dataType.getName())),
				24, dataType.getTypeColor().withBrightness(0.4f),
				I18n.format(IIReference.DATA_KEY+"datatype."+dataType.getName())
		);

		//Apply Button
		buttonApply = addButton(new GuiButtonIE(buttonList.size(), guiLeft+96, guiTop+121, 64, 12, I18n.format("desc.immersiveintelligence.variable_apply"), TEXTURE_EDIT.toString(), 0, 222).setHoverOffset(64, 0));

		//Displays Manual Page for Type
		buttonVariableHelp = addButton(new GuiButtonII(buttonList.size(), guiLeft+152-10, guiTop+15, 16, 16, String.format("immersiveintelligence:textures/gui/data_types/%s.png", dataType.getName()), 0, 0, 1, 1));

		buttonTypeNext = addButton(new GuiButtonIE(0, guiLeft+159, guiTop+14+2, 8, 6, "",
				ImmersiveIntelligence.MODID+":textures/gui/emplacement_icons.png", 128, 77)
				.setHoverOffset(8, 0));

		buttonTypePrev = addButton(new GuiButtonIE(0, guiLeft+159, guiTop+14+10, 8, 6, "",
				ImmersiveIntelligence.MODID+":textures/gui/emplacement_icons.png", 128, 77+6)
				.setHoverOffset(8, 0));

		editor = null;
		for(Entry<Class<? extends DataType>, BiFunction<Integer, DataType, GuiDataEditor<? extends DataType>>> entry : GuiDataEditor.editors.entrySet())
		{
			if(entry.getKey()==dataType.getClass())
			{
				this.editor = addButton(entry.getValue().apply(buttonList.size(), dataType));
				this.editor.setBounds(guiLeft+35, guiTop+46, 131, 80);
				break;
			}
		}

		//Letter Change Buttons
		buttonLetter = addButton(new GuiButtonDataLetterList(buttonList.size(), guiLeft+42-10, guiTop+14, false, variableToEdit, ArrowsAlignment.LEFT));
		buttonLetter.setAvoidGetter(() -> list);

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
		{
			if(!buttonLetter.keyTyped(typedChar, keyCode))
				super.keyTyped(typedChar, keyCode);
		}
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
		else if(button==buttonTypeNext||button==buttonTypePrev)
		{
			switchType(button==buttonTypeNext);
		}
		else if(button==buttonApply)
		{
			if(this.editor!=null)
				this.dataType = this.editor.outputType();
			saveBasicData();
			syncDataToServer();
			preparedForChange = true;
			IIPacketHandler.sendToServer(new MessageGuiNBT(IIGuiList.GUI_DATA_INPUT_MACHINE_VARIABLES, tile));
		}
		else if(button==buttonVariableHelp)
		{
			sideManual.selectedCategory = IIReference.CAT_DATA;
			sideManual.setSelectedEntry("data_variable_types");
			sideManual.page = 0;

			final String pp = "data_variable_types_"+dataType.getName();

			List<ManualEntry> entries = ManualHelper.getManual().manualContents.get(IIReference.CAT_DATA);
			Optional<ManualEntry> first = entries.stream().filter(manualEntry -> manualEntry.getName().equals("data_variable_types")).findFirst();
			if(first.isPresent())
			{
				IManualPage[] pages = first.get().getPages();
				int i = 0;
				for(IManualPage page : pages)
				{
					if(page instanceof ManualPages)
					{
						if(ReflectionHelper.getPrivateValue(ManualPages.class, ((ManualPages)page), "text").equals(pp))
						{
							sideManual.page = i;
							break;
						}
					}
					i++;
				}
			}

			manualButton.state = true;

			sideManual.initGui();

		}

	}

	@Override
	public ArrayList<String> getTooltip(int mx, int my)
	{
		ArrayList<String> tooltip = super.getTooltip(mx, my);
		if(editor!=null)
			editor.getTooltip(tooltip, mx, my);
		return tooltip;
	}

	//Used to refresh gui variables after one of the variables is changed
	void refreshStoredData()
	{
		super.refreshStoredData();
		this.list = tile.storedData;
		if(positionEqual(proxy, tile))
		{
			if(proxy.storedGuiData.hasKey("variableToEdit"))
				variableToEdit = proxy.storedGuiData.getString("variableToEdit").charAt(0);
		}
		this.dataType = list.getPacketVariable(variableToEdit);
	}

	void switchType(boolean forward)
	{
		try
		{
			ArrayList<Class<? extends DataType>> types = new ArrayList<>(GuiDataEditor.editors.keySet());
			int i = IIUtils.cycleInt(forward, types.indexOf(this.dataType.getClass()), 0, types.size()-1);
			list.setVariable(variableToEdit, new DataPacket().getVarInType(types.get(i), new DataTypeNull()));
			this.dataType = list.getPacketVariable(variableToEdit);
		} catch(Exception ignored)
		{

		}

		syncDataToServer();
		initGui();
	}

	void switchLetter()
	{
		if(!list.variables.containsKey(buttonLetter.selectedEntry))
		{
			list.setVariable(buttonLetter.selectedEntry, list.getPacketVariable(variableToEdit));
			list.removeVariable(variableToEdit);
			variableToEdit = buttonLetter.selectedEntry;
		}

		syncDataToServer();
		initGui();
	}

	@Override
	protected void syncDataToServer()
	{
		proxy.storedGuiData.setString("variableToEdit", String.valueOf(variableToEdit));
		tile.storedData.setVariable(variableToEdit, dataType);
		super.syncDataToServer();
	}
}
