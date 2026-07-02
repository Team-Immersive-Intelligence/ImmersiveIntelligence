package pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.DataVariable;
import pl.pabilo8.immersiveintelligence.api.data.IIDataOperationUtils;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation.DataOperationMeta;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation.DataOperationNull;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoArrows;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoDropdownDataLetters;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSwitch;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoElementDisplays.DecoElementDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor.DecoCodeEditor;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor.DecoDataEditor;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor.DecoDataEditorExpression;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 03.08.2025
 * @ii-approved 0.3.1
 * @since 30.06.2019
 */
@DecoTemplate(name = "arithmetic_logic_machine_edit", category = DecoGuiCategory.DATA_TILE)
public class GuiArithmeticLogicMachineEdit extends DecoTileGui<TileEntityArithmeticLogicMachine, ContainerArithmeticLogicMachine>
{
	@SyncNBT
	public DataVariable variableToEdit;
	@SyncNBT(name = "page")
	public int editedCircuit;

	private Collection<DataOperationMeta> circuitOperations;
	private DataPacket packet;
	private DataTypeExpression edited;
	private ItemStack circuitStack;
	private boolean cancel = false;
	private Character originalVariableName = null;

	@Nullable
	private DecoDataEditor<?> editor;
	private boolean codeEditMode;

	public GuiArithmeticLogicMachineEdit(EntityPlayer player, TileEntityArithmeticLogicMachine tile)
	{
		super(player, tile, IIGUI.ARITHMETIC_LOGIC_MACHINE_EDIT);
	}

	@Override
	public void onInit()
	{
		//Set animated part states
		syncAnimatedParts(tile.door, true);
		syncAnimatedParts(tile.drawer, false);
		syncAnimatedParts(tile.keyboard, true);

		//Get circuit and list of all allowed operations
		this.circuitStack = editedCircuit >= 0&&editedCircuit < tile.getInventory().size()?tile.getInventory().get(editedCircuit): ItemStack.EMPTY;
		this.packet = this.packet==null?getCircuitPacket().clone(): this.packet;
		if(originalVariableName==null)
			originalVariableName = variableToEdit.getName();
		this.circuitOperations = IIContent.itemCircuit.getOperationsList(this.circuitStack).stream()
				.map(IIDataOperationUtils::getOperationMeta)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

		//Get currently edited operation
		DataType value = variableToEdit.getValue();
		this.edited = value instanceof DataTypeExpression?(DataTypeExpression)value.clone(): new DataTypeExpression();

		//If the current operation is not valid for this circuit, set it to the first valid one
		if(!isOperationAllowed(this.edited.getMeta()))
		{
			DataOperationMeta firstOperation = circuitOperations.isEmpty()?DataOperationNull.INSTANCE_META: circuitOperations.iterator().next();
			this.edited.setOperation(IIDataOperationUtils.getOperationInstance(firstOperation.name()));
		}

		//A cloned packet without the currently edited variable is required, so the selector knows which variable names are unavailable
		DataPacket constraints = packet.clone();
		constraints.remove(variableToEdit.getName());

		//Build background
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 176+64-16, 128+8+32+16+4)
				.withTitleBar(tile)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 16+8, 128+8+32+20+4, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()

				.withNextLayer()
				.withBox(DecoTextures.BG_PAPER, DecoTextures.TEMPLATE_PAPER, 16+8-4+4-16, 48-8-24-4, 176+64-16-32+8-4-8+16, 24)

				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_ROUND, 16+8-4+4-8-8, 48-8+24-20+4, 176+64-16-32+8-8+16, 128+8-48+32+16)
				.withTitleBar("desc.immersiveintelligence.variable_properties")

				.build();

		//Add top bar
		addComponents(
				//Variable name selector
				new DecoDropdownDataLetters(32+4+6+1-32, 4+8+2+1)
						.withConstraints(constraints)
						.withSelectedEntry((Character)variableToEdit.getName())
						.withTranslatedTooltip("desc.immersiveintelligence.variable_properties")
						.withOnSelectedEntry((oldChar, newChar) -> changeVariableName(newChar))
						.withTextColor(DecoColors.H1, IIColor.fromPackedRGB(0x35322c))
						.withBackground(DecoTextures.COMPONENT_DROPDOWN_DATA_LETTER_PAPER)
						.withDropdownSymbol(DecoTextures.COMPONENT_DROPDOWN_SYMBOL_PAPER),

				new DecoArrows(32+4+6+1+18+1-32, 4+8+2+1+2)
						.withSize(8, 14)
						.withBackground(DecoTextures.COMPONENT_ARROWS_PAPER)
						.withOnArrow(arrow -> {
							char cycled = IIUtils.cycleDataPacketCharsAvoiding(variableToEdit.getName(), arrow, false, constraints);
							changeVariableName(cycled);
						}),

				//Operation selector
				new DecoDropdown<DataOperationMeta>(16+4+10+32-12+6+1-24+8, 4+8+2+1)
						.withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER)
						.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
						.withListBackground(DecoTextures.COMPONENT_TEXT_FIELD)
						.withDropdownSymbol(DecoTextures.COMPONENT_DROPDOWN_SYMBOL_PAPER)
						.withSize(116+24-8, 18)
						.withDropdownWidth(116+24)
						.withMaxDisplayedEntries(5)
						.withEntries(circuitOperations)
						.withSelectedEntry(edited.getOperation().getMeta())
						.withDisplayFunction(getOperationDropdownDisplayFunction())
						.withOnSelectedEntry((oldMeta, newMeta) -> {
							cancel = true;
							storeEditorOutput();
							edited.setOperation(IIDataOperationUtils.getOperationInstance(newMeta.name()));
							variableToEdit = new DataVariable(variableToEdit.getName(), edited);
							refreshGUI();
						}),

				new DecoButton(162+16-4, 2+8+4+1)
						.withTemplate(DecoTemplates.ACTION_BUTTON_DUPLICATE)
						.withSize(18, 18)
						.withOnLMBPressed(this::duplicateVariable),
				new DecoButton(162+16-4+1+18, 2+8+4+1)
						.withTemplate(DecoTemplates.ACTION_BUTTON_REMOVE)
						.withSize(18, 18)
						.withOnLMBPressed(this::removeVariable)
		);

		//Add editor
		addComponent(this.editor = codeEditMode?new DecoCodeEditor(43-32, 43+4+4, edited): new DecoDataEditorExpression(43-32, 43+4+4, edited))
				.withSize(186+16, 106+24);

		addComponent(new DecoSwitch(43-32, 43-7)
				.withText("ii.gui.editor.editor_style")
				.withCurrentState(this.codeEditMode)
				.withOnToggle(result -> {
					cancel = true;
					storeEditorOutput();
					this.codeEditMode = result;
					refreshGUI();
				})
		);

		addComponents(
				new DecoButton(xSize-48-4-4-4-2, 128+8-16+32-2+3+16+2+4)
						.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
						.withText("ii.gui.button.apply")
						.withSize(48, 12)
						.withOnPressed((gui, button, mouseX, mouseY) -> {
							cancel = false;
							return changeGUI(IIGUI.ARITHMETIC_LOGIC_MACHINE_VARIABLES);
						}),
				new DecoButton(xSize-48*2-4-4-4-2, 128+8+32-16-2+3+16+2+4)
						.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
						.withText("ii.gui.button.cancel")
						.withSize(48, 12)
						.withOnPressed((gui, button, mouseX, mouseY) -> {
							cancel = true;
							return changeGUI(IIGUI.ARITHMETIC_LOGIC_MACHINE_VARIABLES);
						})
		);
	}

	private void changeVariableName(Character newName)
	{
		//Do nothing if the name remains the same
		if(newName==null||newName==variableToEdit.getName())
			return;

		cancel = true;
		storeEditorOutput();

		//Remove existing variable and place it in the packet with the new name
		packet.remove(variableToEdit.getName());
		variableToEdit = new DataVariable(newName, edited);
		refreshGUI();
	}

	private char findNextFreeVariableName()
	{
		if(packet.size() >= DataPacket.VARIABLE_NAMES.length)
			return '\0';
		return IIUtils.cycleDataPacketCharsAvoiding(variableToEdit.getName(), true, false, packet);
	}

	private void duplicateVariable()
	{
		if(editor==null)
			return;

		storeEditorOutput();
		packet.with(variableToEdit);
		savePacketToCircuit(packet);

		char name = findNextFreeVariableName();
		if(name=='\0')
			return;

		cancel = true;
		variableToEdit = new DataVariable(name, edited.clone());
		refreshGUI();
	}

	private void removeVariable()
	{
		if(originalVariableName!=null)
			packet.remove(originalVariableName);
		packet.remove(variableToEdit.getName());
		savePacketToCircuit(packet);

		cancel = true;
		changeGUI(IIGUI.ARITHMETIC_LOGIC_MACHINE_VARIABLES);
	}

	private boolean isOperationAllowed(DataOperationMeta meta)
	{
		return meta!=null&&circuitOperations.stream().anyMatch(m -> m.name().equals(meta.name()));
	}

	private DataPacket getCircuitPacket()
	{
		if(tile.getInventory().size() <= editedCircuit)
			return new DataPacket();
		ItemStack stack = tile.getInventory().get(editedCircuit);
		if(stack.isEmpty()||!(stack.getItem() instanceof ItemIIFunctionalCircuit))
			return new DataPacket();
		return ((ItemIIFunctionalCircuit)stack.getItem()).getStoredData(stack);
	}

	private void storeEditorOutput()
	{
		if(editor==null)
			return;
		DataType output = editor.outputType();
		if(output instanceof DataTypeExpression)
			edited = (DataTypeExpression)output;
		variableToEdit = new DataVariable(variableToEdit.getName(), edited);
	}

	private void updateLocalCircuitStack(DataPacket packet)
	{
		if(tile.getInventory().size() <= editedCircuit)
			return;
		ItemStack stack = tile.getInventory().get(editedCircuit);
		if(stack.isEmpty()||!(stack.getItem() instanceof ItemIIFunctionalCircuit))
			return;
		((ItemIIFunctionalCircuit)stack.getItem()).writeDataToItem(stack, packet);
		tile.getInventory().set(editedCircuit, stack);
	}

	private void savePacketToCircuit(DataPacket packet)
	{
		updateLocalCircuitStack(packet);
		NBTTagCompound expressions = EasyNBT.newNBT()
				.withInt("page", editedCircuit)
				.withSerializable("list", packet)
				.unwrap();
		IIPacketHandler.sendToServer(new MessageIITileSync(tile, EasyNBT.newNBT()
				.withTag("expressions", expressions)
		));
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		EasyNBT nbt = super.onSaveTileData();
		if(editor==null||cancel)
			return nbt;

		storeEditorOutput();
		DataPacket updatedPacket = packet.with(variableToEdit);
		updateLocalCircuitStack(updatedPacket);

		NBTTagCompound expressions = EasyNBT.newNBT()
				.withInt("page", editedCircuit)
				.withSerializable("list", updatedPacket)
				.unwrap();
		return nbt.withTag("expressions", expressions);
	}

	private DecoElementDisplay<DataOperationMeta> getOperationDropdownDisplayFunction()
	{
		return new DecoEntryPanelBuilder<DataOperationMeta>()
				.withHeight(18)
				.withBackground(DecoTextures.BG_PAPER)
				.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
				//Type Icon, Label, and Letter
				.withComponent("image", new DecoImage(3, 1)
						.withSize(16, 16))
				.withLabel("typeLabel",
						new DecoLabel(fontRenderer, 2+16+2, 1)
								.withSize(48, 18)
								.withAlign(DecoAlignment.LEFT)
								.withText("Addition")
				)
				.withElementApplyMethod((operation, panel) -> {
					TypeMetaInfo<?> metaInfo = IIDataTypeUtils.metaTypesByClass.get(operation.expectedResult());

					if(metaInfo==null)
						return;

					//type label (f.e. integer)
					panel.label("typeLabel")
							.withText("datasystem.immersiveintelligence.function."+operation.name())
							.withTextColor(metaInfo.color.withBrightness(0.4f));
					//type icon
					panel.component("image", DecoImage.class)
							.withImageLocation(metaInfo.getTextureLocation(), true);
				})
				.withElementTooltip(operation -> "datasystem.immersiveintelligence.function."+operation.name()+".desc");
	}

	@Override
	public void onGuiClosed()
	{
		if(!changeGUIFlag)
		{
			syncAnimatedParts(tile.door, false);
			syncAnimatedParts(tile.drawer, false);
			syncAnimatedParts(tile.keyboard, false);
		}
		super.onGuiClosed();
	}
}
