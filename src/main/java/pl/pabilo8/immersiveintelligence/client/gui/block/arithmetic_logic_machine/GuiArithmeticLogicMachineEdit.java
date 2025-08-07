package pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor.DecoDataEditorExpression;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerArithmeticLogicMachine;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 03.08.2025
 * @ii-approved 0.3.1
 * @since 30.06.2019
 */
@DecoTemplate(name = "arithmetic_logic_machine_edit", category = DecoGuiCategory.DATA_TILE)
public class GuiArithmeticLogicMachineEdit extends DecoGui<TileEntityArithmeticLogicMachine, ContainerArithmeticLogicMachine>
{
	private DecoDataEditorExpression editor;

	public GuiArithmeticLogicMachineEdit(EntityPlayer player, TileEntityArithmeticLogicMachine tile)
	{
		super(player, tile, IIGUI.ARITHMETIC_LOGIC_MACHINE_EDIT);
	}

	@Override
	public void onInit()
	{
		syncAnimatedParts(tile.door, true);
		syncAnimatedParts(tile.drawer, false);
		syncAnimatedParts(tile.keyboard, true);
	}

	/*@Override
	public void initGui()
	{
		super.initGui();

		//Properties
		addLabel(43, 40, 115, 0, false, IIReference.COLOR_H1, I18n.format("desc.immersiveintelligence.variable_properties")).setCentered();
		//Type:
		addLabel(61, 24, IIReference.COLOR_H1, I18n.format("desc.immersiveintelligence.variable_type"));
		//Variable Type
		addLabel(152-fontRenderer.getStringWidth(I18n.format(IIReference.DATA_KEY+"datatype."+dataType.getName())),
				24, dataType.getTypeColor().withBrightness(0.4f),
				I18n.format(IIReference.DATA_KEY+"datatype."+dataType.getName())
		);

		//Apply Button
		buttonApply = addButton(new GuiButtonIE(buttonList.size(), guiLeft+96, guiTop+121, 64, 12, I18n.format("desc.immersiveintelligence.variable_apply"), TEXTURE_EDIT.toString(), 0, 222).setHoverOffset(64, 0));

		//Displays Manual Page for Type
		buttonVariableHelp = addButton(
				new DecoButton(guiLeft+152, guiTop+15)
						.withSize(16, 16)
						.withIcon(IIReference.RES_TEXTURES_GUI.with("data_types/expression").withExtension(ResLoc.EXT_PNG))
		);

		*//*this.editor = addButton(new DecoDataEditorExpression(buttonList.size(),
				this.editor!=null?this.editor.outputType(): new DataPacket().getVarInType(DataTypeExpression.class, dataType), handler.getStackInSlot(page)));
		this.editor.setBounds(guiLeft+35, guiTop+46, 131, 80);

		//Letter Change Buttons
		buttonLetter = addButton(new DecoDropdownDataLetters(buttonList.size(), guiLeft+42-10, guiTop+14, false, variableToEdit, ArrowsAlignment.LEFT));
		buttonLetter.setAvoidGetter(this::getPacketFromPage);*//*

	}*/

}
