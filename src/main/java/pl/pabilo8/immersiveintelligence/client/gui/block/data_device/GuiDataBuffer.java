package pl.pabilo8.immersiveintelligence.client.gui.block.data_device;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoDropdownDataLetters;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.DataDeviceUtils;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataBuffer;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataBuffer.BufferCondition;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataBuffer.Comparison;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataBuffer.ConditionType;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerSmallDataDevice;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 * Edits the conditions and lifetime of queued data packets.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 25.10.2026
 * @since 24.10.2026
 */
@DecoTemplate(name = "data_buffer", category = DecoGuiCategory.DATA_TILE)
public class GuiDataBuffer extends DecoTileGui<TileEntityDataBuffer, ContainerSmallDataDevice<TileEntityDataBuffer>>
{
	private static final String KEY = "ii.gui.small_data_devices.";

	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public EasyCollection<BufferCondition, NBTTagCompound> conditions = new EasyCollection<>(BufferCondition::new);
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public boolean matchAll = true, queueUnmatched = true;
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public int terminationTime = 1200;
	@SyncNBT
	public int editedRule = -1;

	public GuiDataBuffer(EntityPlayer player, TileEntityDataBuffer tile)
	{
		super(player, tile, IIGUI.DATA_BUFFER);
		if(tile!=null)
		{
			conditions = tile.conditions.clone();
			matchAll = tile.matchAll;
			queueUnmatched = tile.queueUnmatched;
			terminationTime = tile.terminationTime;
		}
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 176, 168)
				.withTitleBar(tile)
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 168, 176, 92)
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.withNextLayer()
				.withBox(DecoTextures.BG_PAPER, DecoTextures.TEMPLATE_PAPER, 4, 8, 168, 16)
				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 0, 8+152-24, 168+8, 32)
				.build();

		addLabel(KEY+"conditions", 8, 16-4);
		addComponent(new DecoDropdown<String>(102+4, 12-2)
				.withSize(62, 12)
				.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
				.withEntries("AND", "OR")
				.withSelectedEntry(matchAll?"AND": "OR")
				.withOnSelectedEntry((oldValue, newValue) -> matchAll = "AND".equals(newValue)));
		addComponent(new DecoList<BufferCondition>(4, 32-8+2)
				.withSize(160+8, 90+14+4)
				.withEntries(conditions)
				.withCreateLaterAction(() -> {
					if(conditions.size() < DataDeviceUtils.MAX_RULES)
						conditions.add(new BufferCondition());
					refreshGUI();
				})
				.withDisplayFunction(new DecoEntryPanelBuilder<BufferCondition>()
						.withHeight(44)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
						.withComponent("type", p -> new DecoDropdown<ConditionType>(3, 2)
								.withSize(p.width-24, 18)
								.withDropdownWidth(168)
								.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
								.withEntries(ConditionType.values())
								.withSelectedEntry(p.getCurrentElement().type)
								.withOnSelectedEntry((oldType, newType) -> {
									p.getCurrentElement().type = newType;
									refreshGUI();
								}))
						.withComponent(p -> new DecoButton(p.width-19, 2)
								.withTemplate(DecoTemplates.ACTION_BUTTON_REMOVE)
								.withOnLMBPressed(() -> {
									conditions.remove(p.getCurrentElement());
									refreshGUI();
								}))
						.withComponent("comparison", p -> new DecoDropdown<Comparison>(3, 23)
								.withSize(48, 16)
								.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
								.withEntries(Comparison.values())
								.withSelectedEntry(p.getCurrentElement().comparison)
								.withOnSelectedEntry((oldValue, newValue) -> p.getCurrentElement().comparison = newValue))
						.withComponent("signal", p -> new DecoTextField(54, 23)
								.withSize(48, 16)
								.withFilter(TextFilter.DECIMAL)
								.withText(Integer.toString(p.getCurrentElement().signal))
								.withOnTextChanged(text -> p.getCurrentElement().signal = MathHelper.clamp(IIStringUtil.parseInt(text), 0, 16)))
						.withComponent("ticks", p -> new DecoTextField(25, 23)
								.withSize(72, 16)
								.withFilter(TextFilter.DECIMAL)
								.withText(Integer.toString(p.getCurrentElement().ticks))
								.withOnTextChanged(text -> p.getCurrentElement().ticks = MathHelper.clamp(IIStringUtil.parseInt(text), 0, DataDeviceUtils.MAX_TICKS)))
						.withComponent("accessor", p -> new DecoButton(3, 23)
								.withSize(18, 16)
								.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
								.withRawText("@")
								.withTranslatedTooltip(KEY+"accessor")
								.withOnLMBPressed(() -> {
									BufferCondition condition = p.getCurrentElement();
									condition.useAccessor = !condition.useAccessor;
									refreshGUI();
								}))
						.withComponent("variable", p -> new DecoDropdownDataLetters(p.getCurrentElement().type==ConditionType.TIME_DELAY?25: 3, 23)
								.withConstraints(new DataPacket())
								.withBackground(DecoTextures.COMPONENT_DROPDOWN_DATA_LETTER_PAPER)
								.withDropdownSymbol(DecoTextures.COMPONENT_DROPDOWN_SYMBOL_PAPER)
								.withSelectedEntry((Character)p.getCurrentElement().variable)
								.withOnSelectedEntry((oldChar, newChar) -> p.getCurrentElement().variable = newChar))
						.withComponent("edit", p -> new DecoButton(27, 23)
								.withTemplate(DecoTemplates.ACTION_BUTTON_EDIT)
								.withOnLMBPressed(() -> {
									editedRule = conditions.indexOf(p.getCurrentElement());
									changeGUI(IIGUI.DATA_BUFFER_EDIT);
								}))
						.withElementApplyMethod((condition, panel) -> {
							boolean redstone = condition.type==ConditionType.REDSTONE_SIGNAL;
							boolean timeDelay = condition.type==ConditionType.TIME_DELAY;
							@SuppressWarnings("unchecked")
							DecoDropdown<Comparison> comparison = panel.component("comparison", DecoDropdown.class);
							comparison.visible = comparison.enabled = redstone;
							DecoTextField signal = panel.component("signal", DecoTextField.class);
							signal.visible = signal.enabled = redstone;
							DecoTextField ticks = panel.component("ticks", DecoTextField.class);
							ticks.visible = ticks.enabled = condition.type==ConditionType.LAST_RECEIVED_DELAY||(timeDelay&&!condition.useAccessor);
							DecoButton accessor = panel.component("accessor", DecoButton.class);
							accessor.visible = accessor.enabled = timeDelay;
							DecoDropdownDataLetters variable = panel.component("variable", DecoDropdownDataLetters.class);
							variable.visible = variable.enabled = condition.type==ConditionType.VARIABLE_PRESENT
									||condition.type==ConditionType.VARIABLE_MATCHING||(timeDelay&&condition.useAccessor);
							DecoButton edit = panel.component("edit", DecoButton.class);
							edit.visible = edit.enabled = condition.type==ConditionType.VARIABLE_MATCHING;
						})));
		addComponent(new DecoCheckbox(4, 131+4+2)
				.withSize(154, 12)
				.withText(KEY+"queue_unmatched")
				.withChecked(queueUnmatched)
				.withOnToggle(value -> queueUnmatched = value));
		addLabel(KEY+"termination", 4, 146+2)
				.withSize(86, 18)
				.withAlign(DecoAlignment.LEFT)
				.withWrapping(true);
		addComponent(new DecoTextField(100+8, 147+2)
				.withSize(64, 16)
				.withFilter(TextFilter.DECIMAL)
				.withText(Integer.toString(terminationTime))
				.withOnTextChanged(text -> terminationTime = MathHelper.clamp(IIStringUtil.parseInt(text), 0, DataDeviceUtils.MAX_TICKS)));
	}
}
