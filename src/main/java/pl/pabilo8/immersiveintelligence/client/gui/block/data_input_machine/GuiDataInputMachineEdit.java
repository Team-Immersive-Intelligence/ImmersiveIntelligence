package pl.pabilo8.immersiveintelligence.client.gui.block.data_input_machine;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoDropdownDataLetters;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor.GuiDataEditor;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityDataInputMachine;

import javax.annotation.Nullable;
import java.util.Map.Entry;
import java.util.function.BiFunction;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 31.08.2021.
 * @since 30.06.2019
 */
@DecoTemplate(name = "data_input_machine_edit")
public class GuiDataInputMachineEdit extends GuiDataInputMachine
{
	public char variableToEdit = 'a';
	public DataType dataType;
	public DecoDropdownDataLetters buttonLetter;
	public DecoButton buttonApply;
	public DecoButton buttonTypeNext, buttonTypePrev;
	private DecoButton buttonVariableHelp;
	@Nullable
	private GuiDataEditor<? extends DataType> editor = null;

	public GuiDataInputMachineEdit(EntityPlayer player, TileEntityDataInputMachine tile)
	{
		super(player, tile, IIGUI.DATA_INPUT_MACHINE_EDIT);
	}

	@Override
	public void onInit()
	{
		super.onInit();
		//Properties
		addLabel("desc.immersiveintelligence.variable_properties", 43, 40)
				.withAlign(DecoAlignment.CENTER);
		//Type:
		addLabel("desc.immersiveintelligence.variable_type", 61, 24);
		//Variable Type
		addLabel("desc.immersiveintelligence.variable_type", 152-10, 24)
				.withAlign(DecoAlignment.CENTER)
				.withTextColor(dataType.getTypeColor().withBrightness(0.4f));

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
		addComponents(
				//Apply Button
				buttonApply = new DecoButton(96, 121).withTranslatedTooltip("desc.immersiveintelligence.variable_apply"),
				//Displays Manual Page for Type
				buttonVariableHelp = new DecoButton(guiLeft+152-10, guiTop+15)
						.withSize(16, 16)
						.withIcon(dataType.getTextureLocation()),
				//Type scroll buttons
				buttonTypeNext = new DecoButton(159, 14+2).withRawText(">"),
				buttonTypePrev = new DecoButton(159, 14+10).withRawText("<")
				//Letter Change Buttons
//				buttonLetter = new DecoDropdownDataLetters(buttonList.size(), guiLeft+42-10, guiTop+14, false, variableToEdit, ArrowsAlignment.LEFT)
		);
//		buttonLetter.setAvoidGetter(() -> list);
	}

	/*@Override
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
			saveBasicData(tile);
			syncDataToServer();
			soundPlayed = true;
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

//			manualButton.state = true;

			sideManual.initGui();

		}

	}*/

	//Used to refresh gui variables after one of the variables is changed
	/*void refreshStoredData()
	{
		super.refreshStoredData();
		this.list = tile.storedData;
		if(positionEqual(tile))
		{
			if(proxy.getStoredGuiData().hasKey("variableToEdit"))
				variableToEdit = proxy.getStoredGuiData().getString("variableToEdit").charAt(0);
		}
		this.dataType = list.getPacketVariable(variableToEdit);
	}*/

	/*void switchType(boolean forward)
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
	}*/
}
