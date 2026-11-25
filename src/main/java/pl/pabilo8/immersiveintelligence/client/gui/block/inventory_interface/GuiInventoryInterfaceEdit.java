package pl.pabilo8.immersiveintelligence.client.gui.block.inventory_interface;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityInventoryInterface;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.inventory_interface.InventoryInterfaceRule;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.inventory_interface.InventoryInterfaceRule.*;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

import java.util.Arrays;
import java.util.function.IntConsumer;

/**
 * Edits an inventory rule without applying its draft until confirmation.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 27.10.2026
 */
@DecoTemplate(name = "inventory_interface_edit", category = DecoGuiCategory.DATA_TILE)
public class GuiInventoryInterfaceEdit extends GuiInventoryInterface
{
	@SyncNBT
	public InventoryInterfaceRule draft = new InventoryInterfaceRule();
	@SyncNBT
	public boolean draftLoaded;
	@SyncNBT
	public int selectedOutput = -1, outputScroll;

	private boolean apply;
	private DecoList<Output> outputList;

	public GuiInventoryInterfaceEdit(EntityPlayer player, TileEntityInventoryInterface tile)
	{
		this(player, tile, IIGUI.INVENTORY_INTERFACE_EDIT);
	}

	protected GuiInventoryInterfaceEdit(EntityPlayer player, TileEntityInventoryInterface tile, IIGUI gui)
	{
		super(player, tile, gui);
	}

	@Override
	public void onInit()
	{
		changeGUIFlag = false;
		if(!draftLoaded)
		{
			draft = editedRule >= 0&&editedRule < rules.size()?rules.get(editedRule).copy(): new InventoryInterfaceRule();
			draftLoaded = true;
		}
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 240, 176)
				.withTitleBar(KEY+"rule_properties")
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 32, 176, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		addComponent(new DecoCheckbox(8, 12).withSize(148, 12)
				.withText(KEY+"enabled").withChecked(draft.enabled)
				.withOnToggle(value -> draft.enabled = value));
		addLabel(KEY+"trigger", 8, 32-4-2);
		addComponent(new DecoDropdown<Trigger>(8, 44-4-2-2)

				.withSize(114, 16)
				.withDropdownWidth(148)
				.withMaxDisplayedEntries(5)
				.withEntries(Trigger.values())
				.withSelectedEntry(draft.trigger)
				.withOnSelectedEntry((oldValue, newValue) -> {
					draft.trigger = newValue;
					refreshGUI();
				}));
		if(usesCondition())
		{
			addLabel(KEY+"send_mode", 124, 32-4-2);
			addComponent(new DecoDropdown<SendMode>(124, 44-4-2-2)

					.withSize(114, 16)
					.withDropdownWidth(148)
					.withEntries(SendMode.values())
					.withSelectedEntry(draft.sendMode)
					.withOnSelectedEntry((oldValue, newValue) -> {
						draft.sendMode = newValue;
						refreshGUI();
					}));
		}
		if(draft.trigger==Trigger.PERIODIC||(usesCondition()&&draft.sendMode==SendMode.WHILE_TRUE))
		{
			addLabel(KEY+"interval", 124, 14).withTranslatedTooltip(KEY+"interval.tooltip");
			addNumber(184, 12, 48, draft.interval, TileEntityInventoryInterface.SAMPLE_INTERVAL,
					value -> draft.interval = Math.min(72000, value));
		}
		addTriggerControls();
		if(draft.usesItemFilter())
			addComponent(new DecoButton(124, draft.trigger==Trigger.CONDITION?82-6: 42-6)
					.withSize(114, 16)
					.withText(KEY+"set_item")
					.withOnLMBPressed(() -> changeGUI(IIGUI.INVENTORY_INTERFACE_VALUE))
			);
		addOutputs(draft.trigger==Trigger.CONDITION?108: 76);
		addActionButtons(240, 176, () -> {
			if(editedRule >= 0&&editedRule < rules.size())
				rules.set(editedRule, draft.copy());
			else if(rules.size() < MAX_RULES)
				rules.add(draft.copy());
			else
				return;
			apply = true;
			changeGUI(IIGUI.INVENTORY_INTERFACE);
		}, IIGUI.INVENTORY_INTERFACE);
	}

	private void addTriggerControls()
	{
		if(draft.trigger==Trigger.CONDITION)
		{
			addComponent(new DecoDropdown<ValueType>(8, 64-8)

					.withSize(114, 16)
					.withDropdownWidth(148)
					.withTranslatedTooltip(KEY+"measured_value")
					.withMaxDisplayedEntries(5)
					.withEntries(Arrays.stream(ValueType.values()).filter(ValueType::isNumeric).toArray(ValueType[]::new))
					.withSelectedEntry(draft.measuredValue)
					.withOnSelectedEntry((oldValue, newValue) -> {
						draft.measuredValue = newValue;
						refreshGUI();
					}));
			addComponent(new DecoDropdown<Comparison>(124, 64-8)
					.withSize(114, 16)
					.withTranslatedTooltip(KEY+"comparison")
					.withMaxDisplayedEntries(5).withEntries(Comparison.values()).withSelectedEntry(draft.comparison)
					.withOnSelectedEntry((oldValue, newValue) -> draft.comparison = newValue));
			addLabel(KEY+"threshold", 8, 84-8+2);
			addNumber(68, 82-8, 48, draft.threshold, 0, value -> draft.threshold = value);
		}
		if(draft.trigger==Trigger.SLOT_CHANGED||(draft.trigger==Trigger.CONDITION&&usesSlot(draft.measuredValue)))
		{
			int y = draft.trigger==Trigger.CONDITION?76: 24;
			addLabel(KEY+"slot", 124, y+3).withTranslatedTooltip(KEY+"slot.tooltip");
			addNumber(184-16, y-1, 64, draft.slot, 0, value -> draft.slot = value);
		}
	}

	private void addOutputs(int y)
	{
		addLabel(KEY+"outputs", 8, y-12);
		outputList = addComponent(new DecoList<Output>(8, y).withSize(224, 168-y)
				.withEntries(draft.outputs)
				.withCreateLaterAction(firstFreeLetter()!=0?() -> {
					selectedOutput = draft.outputs.size();
					changeGUI(IIGUI.INVENTORY_INTERFACE_OUTPUT);
				}: null)
				.withDisplayFunction(new DecoEntryPanelBuilder<Output>().withHeight(20)
						.withBackground(DecoTextures.BG_PAPER)
						.withComponent(p -> new DecoButton(2, 2)
								.withSize(18, 16)
								.withDisabled(true)
								.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
								.withRawText(String.valueOf(p.getCurrentElement().variable)))
						.withLabel("value", p -> new DecoLabel(fontRenderer, 24, 5).withSize(p.width-62, 12)
								.withAlign(DecoAlignment.LEFT)
								.withRawText(p.getCurrentElement().type.getLocalizedName())
								.withTranslatedTooltip(p.getCurrentElement().type==ValueType.ITEM_STACKS?
										KEY+"item_stacks.tooltip": p.getCurrentElement().type.getFullLocaleKey()))
						.withComponent(p -> new DecoButton(p.width-34, 2).withTemplate(DecoTemplates.ACTION_BUTTON_EDIT)
								.withOnLMBPressed(() -> {
									selectedOutput = draft.outputs.indexOf(p.getCurrentElement());
									changeGUI(IIGUI.INVENTORY_INTERFACE_OUTPUT);
								}))
						.withComponent(p -> new DecoButton(p.width-18, 2).withTemplate(DecoTemplates.ACTION_BUTTON_REMOVE)
								.withOnLMBPressed(() -> {
									draft.outputs.remove(p.getCurrentElement());
									refreshGUI();
								})))
				.withScroll(outputScroll));
	}

	protected void addNumber(int x, int y, int width, int value, int minimum, IntConsumer consumer)
	{
		addComponent(new DecoTextField(x, y)
				.withSize(width, 16)
				.withFilter(TextFilter.DECIMAL)
				.withText(Integer.toString(value))
				.withOnTextChanged(text -> consumer.accept(Math.max(minimum, TextFilter.DECIMAL.parseInt(text, value))))
		);
	}

	protected void addActionButtons(int width, int height, Runnable onApply, IIGUI parent)
	{
		addComponent(new DecoButton(width-104, height-6).withSize(48, 12)
				.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
				.withText("ii.gui.button.cancel")
				.withOnLMBPressed(() -> changeGUI(parent)));
		addComponent(new DecoButton(width-52, height-6).withSize(48, 12)
				.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
				.withText("ii.gui.button.apply")
				.withOnLMBPressed(onApply));
	}

	private boolean usesCondition()
	{
		return draft.trigger==Trigger.CONDITION||draft.trigger==Trigger.INVENTORY_EMPTY||
				draft.trigger==Trigger.INVENTORY_FULL||draft.trigger==Trigger.CONTAINS_ITEM||draft.trigger==Trigger.MISSING_ITEM;
	}

	protected static boolean usesSlot(ValueType type)
	{
		return type==ValueType.SLOT_ITEMS||type==ValueType.SLOT_STACK||type==ValueType.SLOT_LIMIT;
	}

	protected char firstFreeLetter()
	{
		for(char letter : DataPacket.VARIABLE_NAMES)
			if(draft.outputs.stream().noneMatch(output -> output.variable==letter))
				return letter;
		return 0;
	}

	@Override
	protected void captureDraft()
	{
		super.captureDraft();
		if(outputList!=null)
			outputScroll = outputList.getScroll();
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		return EasyNBT.newNBT().conditionally(apply, nbt -> nbt.withSerializable("rules", rules)
				.withInt("colorIn", colorIn).withInt("colorOut", colorOut));
	}
}
