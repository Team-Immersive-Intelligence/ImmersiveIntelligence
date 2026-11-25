package pl.pabilo8.immersiveintelligence.client.gui.block.inventory_interface;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.api.crafting.IngredientReference;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoDropdownDataLetters;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoIngredientStackPickerPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityInventoryInterface;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.inventory_interface.InventoryInterfaceRule.Output;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.inventory_interface.InventoryInterfaceRule.ValueType;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * Edits one output variable without changing the rule draft until confirmation.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 27.10.2026
 */
@DecoTemplate(name = "inventory_interface_output", category = DecoGuiCategory.DATA_TILE)
public class GuiInventoryInterfaceOutput extends GuiInventoryInterfaceEdit
{
	@SyncNBT
	public Output output = new Output();
	@SyncNBT
	public boolean outputLoaded;
	private DecoIngredientStackPickerPanel filterEditor;

	public GuiInventoryInterfaceOutput(EntityPlayer player, TileEntityInventoryInterface tile)
	{
		super(player, tile, IIGUI.INVENTORY_INTERFACE_OUTPUT);
	}

	@Override
	public void onInit()
	{
		changeGUIFlag = false;
		filterEditor = null;
		if(selectedOutput < 0||selectedOutput > draft.outputs.size()||
				(selectedOutput==draft.outputs.size()&&firstFreeLetter()==0))
		{
			changeGUI(IIGUI.INVENTORY_INTERFACE_EDIT);
			return;
		}
		if(!outputLoaded)
		{
			if(selectedOutput < draft.outputs.size())
				output = draft.outputs.copyEntry(draft.outputs.get(selectedOutput));
			else
				output.variable = firstFreeLetter();
			outputLoaded = true;
		}
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 240, 144)
				.withTitleBar(KEY+"output_settings")
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 32, 144, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		addComponent(new DecoDropdownDataLetters(8, 13)
		{
			@Override
			public boolean isSelectable(Character letter)
			{
				return super.isSelectable(letter)&&letterAvailable(letter);
			}
		}
				.withConstraints(new DataPacket())
				.withSize(18, 18)
				.withSelectedEntry((Character)output.variable)
				.withOnSelectedEntry((oldValue, newValue) -> output.variable = newValue));

		addComponent(new DecoDropdown<ValueType>(32, 14)
				.withSize(200, 16)
				.withMaxDisplayedEntries(6)
				.withEntries(ValueType.values())
				.withSelectedEntry(output.type)
				.withOnSelectedEntry((oldValue, newValue) -> {
					output.type = newValue;
					refreshGUI();
				}));

		if(usesSlot(output.type))
		{
			addLabel(KEY+"slot", 8, 42).withTranslatedTooltip(KEY+"slot.tooltip");
			addNumber(120, 40, 112, output.slot, 0, value -> output.slot = value);
		}
		else if(output.type==ValueType.ITEM_COUNT||output.type==ValueType.ITEM_STACKS)
			addComponent(createFilterEditor()).withSize(224, 90);
		else
			addLabel(KEY+"no_settings", 8, 42);
		addActionButtons(240, 144, () -> {
			if(applyOutput())
				changeGUI(IIGUI.INVENTORY_INTERFACE_EDIT);
		}, IIGUI.INVENTORY_INTERFACE_EDIT);
	}

	protected DecoIngredientStackPickerPanel createFilterEditor()
	{
		return filterEditor = new DecoIngredientStackPickerPanel(8, 40)
				.withOreDictSelection(false)
				.withIngredientReference(new IngredientReference(output.filter).setUseNBT(output.matchNBT));
	}

	protected boolean applyOutput()
	{
		captureDraft();
		if(selectedOutput < 0||selectedOutput > draft.outputs.size()||!letterAvailable(output.variable))
			return false;
		if(selectedOutput < draft.outputs.size())
			draft.outputs.set(selectedOutput, draft.outputs.copyEntry(output));
		else
			draft.outputs.add(draft.outputs.copyEntry(output));
		return true;
	}

	@Override
	protected void captureDraft()
	{
		super.captureDraft();
		if(filterEditor!=null)
		{
			output.filter = filterEditor.getItemStack();
			output.matchNBT = filterEditor.getIngredientReference().useNBT;
		}
	}

	private boolean letterAvailable(char letter)
	{
		for(int index = 0; index < draft.outputs.size(); index++)
			if(index!=selectedOutput&&draft.outputs.get(index).variable==letter)
				return false;
		return DataPacket.isValidVariable(letter);
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		return EasyNBT.newNBT();
	}
}
