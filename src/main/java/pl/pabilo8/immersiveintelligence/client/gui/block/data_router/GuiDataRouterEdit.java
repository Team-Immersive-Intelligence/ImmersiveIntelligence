package pl.pabilo8.immersiveintelligence.client.gui.block.data_router;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoDropdownDataLetters;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoElementDisplays;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor.DecoDataEditor;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoTaskList.ListMode;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataRouter;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataRouter.DataRoutingRule;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataRouter.RoutingAction;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerDataRouter;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import javax.annotation.Nullable;

/**
 * Data Router rule editor.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 02.06.2026
 */
@DecoTemplate(name = "data_router_edit", category = DecoGuiCategory.DATA_TILE)
public class GuiDataRouterEdit extends DecoTileGui<TileEntityDataRouter, ContainerDataRouter>
{
	private static final String KEY = IIReference.GUI_LABEL_KEY+"data_router.";

	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public EasyCollection<DataRoutingRule, NBTTagCompound> rules;
	@SyncNBT
	public int editedRule = -1;
	@SyncNBT
	public ListMode mode = ListMode.JOBS;
	@SyncNBT
	public DataRoutingRule edited = new DataRoutingRule();
	@Nullable
	private DecoDataEditor<? extends DataType> valueEditor;
	private boolean cancel = true, loaded = false;

	public GuiDataRouterEdit(EntityPlayer player, TileEntityDataRouter tile)
	{
		super(player, tile, IIGUI.DATA_ROUTER_EDIT);
		if(tile!=null)
			rules = tile.routingRules.clone();
	}

	@Override
	public void onInit()
	{
		if(rules==null)
			rules = new EasyCollection<>(DataRoutingRule::new);
		if(editedRule < 0||editedRule >= rules.size())
		{
			cancel = true;
			changeGUI(IIGUI.DATA_ROUTER);
			return;
		}
		if(!loaded)
		{
			edited = rules.get(editedRule).copy();
			loaded = true;
		}

		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 240, 184+12)
				.withTitleBar(KEY+"rule_properties")
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 32, 184+12, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.withNextLayer()

				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_ROUND, 4, 8+4, 224+8, 48-8)
				.withTitleBar(KEY+"routing", DecoAlignment.TOP_LEFT)
				.withNextLayer()

				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_ROUND, 4, 8+4+48-4, 224+8, 128+12)
				.withTitleBar(KEY+"packet_filters", DecoAlignment.TOP_LEFT)
				.withNextLayer()
				.build();

		DecoPanel panel = addComponent(new DecoPanel(12, 16))
				.withSize(216, 8+4+48-4+128+4)
				.withBackground(null);

		int yy = 0;
		addRoutingSection(panel, yy);
		yy += 48;
		addPacketFilterSection(panel, yy);
		yy += 14;
		addValueFilterSection(panel, yy);

		addComponents(
				new DecoButton(xSize-48-4-4-4-2+2, 180+8-8-4+16)
						.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
						.withText("ii.gui.button.apply")
						.withSize(48, 12)
						.withOnPressed((gui, button, mouseX, mouseY) -> {
							storeEditorValue();
							rules.set(editedRule, edited.copy());
							cancel = false;
							return changeGUI(IIGUI.DATA_ROUTER);
						}),
				new DecoButton(xSize-48*2-4-4-4-2+2, 180+8-8-4+16)
						.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
						.withText("ii.gui.button.cancel")
						.withSize(48, 12)
						.withOnPressed((gui, button, mouseX, mouseY) -> {
							cancel = true;
							return changeGUI(IIGUI.DATA_ROUTER);
						})
		);
	}

	private void addRoutingSection(DecoPanel panel, int yy)
	{
		panel.addLabel(KEY+"incoming", 0, yy+5)
				.withSize(46, 9)
				.withAlign(DecoAlignment.LEFT);
		for(int i = 0; i < EnumFacing.VALUES.length; i++)
		{
			EnumFacing side = EnumFacing.VALUES[i];
			int x = 48+(i%3)*24;
			int y = yy+(i/3)*12+4;
			panel.addComponent(new DecoCheckbox(x, y)
					.withSize(24, 10)
					.withRawText(String.valueOf(side.getName().toUpperCase().charAt(0)))
					.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"side."+side.getName())
					.withChecked(edited.receivingSides[side.ordinal()])
					.withOnToggle(checked -> edited.receivingSides[side.ordinal()] = checked)
			);
		}

		panel.addLabel(KEY+"outgoing", 132, yy+3)
				.withSize(40, 9)
				.withAlign(DecoAlignment.LEFT);
		panel.addComponent(new DecoDropdown<EnumFacing>(172, yy)
				.withSize(42, 16)
				.withDropdownWidth(70)
				.withEntries(EnumFacing.VALUES)
				.withSelectedEntry(edited.outgoingSide)
				.withOnSelectedEntry((oldSide, newSide) -> edited.outgoingSide = newSide)
				.withDisplayFunction(DecoElementDisplays.getSimpleTextDisplay(side ->
						I18n.format(IIReference.DESCRIPTION_KEY+"side."+side.getName())
				))
		);

		panel.addLabel(KEY+"action", 132, yy+18)
				.withSize(40, 9)
				.withAlign(DecoAlignment.LEFT);
		panel.addComponent(new DecoDropdown<RoutingAction>(172, yy+15)
				.withSize(42, 16)
				.withDropdownWidth(70)
				.withEntries(RoutingAction.values())
				.withSelectedEntry(edited.action)
				.withOnSelectedEntry((oldAction, newAction) -> edited.action = newAction)
		);
	}

	private void addPacketFilterSection(DecoPanel panel, int yy)
	{
		panel.addComponent(new DecoCheckbox(0, yy)
				.withSize(56, 12)
				.withText(KEY+"filter.color")
				.withChecked(edited.useColorFilter)
				.withOnToggle(checked -> edited.useColorFilter = checked)
		);
		panel.addComponent(new DecoDropdown<EnumDyeColor>(58-8, yy)
				.withTemplate(DecoTemplates.DYE_COLOR_DROPDOWN)
				.withSize(56+8, 12)
				.withDropdownWidth(80)
				.withSelectedEntry(edited.packetColor)
				.withOnSelectedEntry((oldColor, newColor) -> edited.packetColor = newColor)
		);

		panel.addComponent(new DecoCheckbox(120, yy)
				.withSize(56, 12)
				.withText(KEY+"filter.address")
				.withChecked(edited.useAddressFilter)
				.withOnToggle(checked -> edited.useAddressFilter = checked)
		);
		panel.addComponent(new DecoTextField(178, yy-2)
				.withSize(36, 16)
				.withFilter(TextFilter.DECIMAL)
				.withText(edited.packetAddress < 0?"": String.valueOf(edited.packetAddress))
				.withOnTextChanged(text -> edited.packetAddress = text==null||text.trim().isEmpty()?-1: IIStringUtil.parseInt(text.trim()))
		);
	}

	private void addValueFilterSection(DecoPanel panel, int yy)
	{
		valueEditor = null;
		panel.addComponent(new DecoCheckbox(0, yy+2)
				.withSize(92, 12)
				.withText(KEY+"filter.value")
				.withChecked(edited.useValueFilter)
				.withOnToggle(checked -> {
					storeEditorValue();
					edited.useValueFilter = checked;
					refreshGUI();
				})
		);

		if(edited.useValueFilter)
		{
			panel.addComponent(new DecoDropdownDataLetters(96, yy)
					.withConstraints(new DataPacket())
					.withSelectedEntry((Character)edited.valueVariable)
					.withTextColor(DecoColors.H1, IIColor.fromPackedRGB(0x35322c))
					.withOnSelectedEntry((oldChar, newChar) -> edited.valueVariable = newChar)
			);
			panel.addComponent(new DecoDropdown<TypeMetaInfo<?>>(96+18, yy)
					.withSize(128-24, 18)
					.withDropdownWidth(116)
					.withMaxDisplayedEntries(5)
					.withEntries(DecoDataEditor.getEditorTypes(false))
					.withSelectedEntry(edited.expectedValue.getTypeMeta())
					.withDisplayFunction(DecoTemplates.getDataTypeEntryDisplay())
					.withSortFunction(DecoTemplates.getDataTypeEntrySorter())
					.withOnSelectedEntry((oldType, newType) -> {
						storeEditorValue();
						edited.expectedValue = newType==null?new DataTypeNull(): newType.supplier.get();
						refreshGUI();
					})
			);

			yy += 32;
			panel.addComponent(new DecoCheckbox(63, yy-16+1)
					.withSize(102, 12)
					.withText(KEY+"remove_control_variable")
					.withChecked(edited.removeControlVariable)
					.withOnToggle(checked -> edited.removeControlVariable = checked)
			);

			valueEditor = DecoDataEditor.getEditorFor(edited.expectedValue, 8, yy);
			int editorHeight = panel.height-yy;
			if(valueEditor!=null)
				addComponent(valueEditor)
						.withSize(214+12, editorHeight);
		}
	}

	private void storeEditorValue()
	{
		if(valueEditor!=null&&edited!=null&&edited.useValueFilter)
			edited.expectedValue = valueEditor.outputType();
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		return super.onSaveTileData()
				.conditionally(!cancel&&rules!=null, easyNBT -> easyNBT.withSerializable("rules", rules));
	}
}
