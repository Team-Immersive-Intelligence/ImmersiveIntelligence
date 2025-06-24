package pl.pabilo8.immersiveintelligence.client.gui.block.data_input_machine;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.client.gui.IDataMachineGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoDropdownDataLetters;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor.GuiDataEditor;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

import javax.annotation.Nullable;
import java.util.Map.Entry;
import java.util.function.BiFunction;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 31.08.2021.
 * @since 30.06.2019
 */
@DecoTemplate(name = "data_input_machine_edit")
public class GuiDataInputMachineEdit extends DecoGui<TileEntityDataInputMachine, ContainerDataInputMachine> implements IDataMachineGui
{
	@SyncNBT
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
		this.dataType = tile.storedData.getPacketVariable(variableToEdit);

		//Build background
		startBackground()
				.withBox(IIReference.GUI_BG_STEEL, 0, 0, 176+64, 128+8+32)
				.withTitleBar(tile)
				.withBox(IIReference.GUI_BG_WOODEN, 0, 128+8, 176, 92)
				.withInventoryTitleBar()

				.withNextLayer()
				.withBox(IIReference.GUI_BG_STEEL, IIReference.RES_TEXTURES_DECO_TEMPLATE_SQUARE, 0, 8, 32, 120)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventorySlots(SlotStyle.IE_INPUT, container.dataInput)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.dataOutput)
				.build();

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
				/*this.editor = addButton(entry.getValue().apply(buttonList.size(), dataType));
				this.editor.setBounds(guiLeft+35, guiTop+46, 131, 80);*/
				break;
			}
		}
//		addComponent(editor);

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

	@Override
	public void editVariable(char c, DataType type)
	{

	}
}
