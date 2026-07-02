package pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoDropdownDataLetters;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoTaskList.ListMode;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine.MemoryTransferRule;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 * Arithmetic Logic Machine memory transfer rule editor.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 01.07.2026
 */
@DecoTemplate(name = "arithmetic_logic_machine_memory_edit", category = DecoGuiCategory.DATA_TILE)
public class GuiArithmeticLogicMachineMemoryEdit extends DecoTileGui<TileEntityArithmeticLogicMachine, ContainerArithmeticLogicMachine>
{
	private static final String KEY = IIReference.GUI_LABEL_KEY+"arithmetic_logic_machine.memory.";

	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public EasyCollection<MemoryTransferRule, NBTTagCompound> rules;
	@SyncNBT
	public int editedRule = -1;
	@SyncNBT
	public boolean editLoadRules = true;
	@SyncNBT
	public ListMode mode = ListMode.JOBS;
	@SyncNBT
	public MemoryTransferRule edited = new MemoryTransferRule();
	private boolean cancel = true, loaded = false;

	public GuiArithmeticLogicMachineMemoryEdit(EntityPlayer player, TileEntityArithmeticLogicMachine tile)
	{
		super(player, tile, IIGUI.ARITHMETIC_LOGIC_MACHINE_MEMORY_EDIT);
	}

	@Override
	public void onInit()
	{
		syncAnimatedParts(tile.door, true);
		syncAnimatedParts(tile.drawer, false);
		syncAnimatedParts(tile.keyboard, false);

		if(rules==null)
			rules = (editLoadRules?tile.memoryLoadRules: tile.memorySaveRules).clone();
		if(editedRule < 0||editedRule >= rules.size())
		{
			cancel = true;
			changeGUI(editLoadRules?IIGUI.ARITHMETIC_LOGIC_MACHINE_MEMORY_IN: IIGUI.ARITHMETIC_LOGIC_MACHINE_MEMORY_OUT);
			return;
		}
		if(!loaded)
		{
			edited = rules.get(editedRule).copy();
			loaded = true;
		}

		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 176, 128+8)
				.withTitleBar(KEY+(editLoadRules?"load_rule_properties": "save_rule_properties"))
				.withNextLayer()

				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 128+8, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withInventoryTitleBar()
				.withNextLayer()

				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_ROUND, 4, 8+4, 168-4, 108)
				.withTitleBar(KEY+"transfer", DecoAlignment.TOP_LEFT)
				.build();

		DecoPanel panel = addComponent(new DecoPanel(12, 16))
				.withSize(152, 128)
				.withBackground(null);

		addTransferSection(panel, 0);

		addComponents(
				new DecoButton(xSize-48-4, 156+8-8-2-24)
						.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
						.withText("ii.gui.button.apply")
						.withSize(48, 12)
						.withOnPressed((gui, button, mouseX, mouseY) -> {
							rules.set(editedRule, edited.copy());
							cancel = false;
							return changeGUI(editLoadRules?IIGUI.ARITHMETIC_LOGIC_MACHINE_MEMORY_IN: IIGUI.ARITHMETIC_LOGIC_MACHINE_MEMORY_OUT);
						}),
				new DecoButton(xSize-48*2-4, 156+8-8-2-24)
						.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
						.withText("ii.gui.button.cancel")
						.withSize(48, 12)
						.withOnPressed((gui, button, mouseX, mouseY) -> {
							cancel = true;
							return changeGUI(editLoadRules?IIGUI.ARITHMETIC_LOGIC_MACHINE_MEMORY_IN: IIGUI.ARITHMETIC_LOGIC_MACHINE_MEMORY_OUT);
						})
		);
	}

	private void addTransferSection(DecoPanel panel, int yy)
	{
		panel.addLabel("From:", -4+2, yy+4+2);
		DecoPanel panelPaper = panel.addComponent(new DecoPanel(-4, yy+4+12)
				.withBackground(DecoTextures.BG_PAPER)
				.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
				.withSize(160, 18)
		);
		panelPaper.addLabel(KEY+(editLoadRules?"memory_variable": "packet_variable"), 4, 6)
				.withSize(112, 9)
				.withAlign(DecoAlignment.LEFT);
		panelPaper.addComponent(new DecoDropdownDataLetters(160-18, 0)
				.withConstraints(new DataPacket())
				.withSelectedEntry((Character)edited.sourceVariable)
				.withTextColor(DecoColors.H1, IIColor.fromPackedRGB(0x35322c))
				.withBackground(DecoTextures.COMPONENT_DROPDOWN_DATA_LETTER_PAPER)
				.withDropdownSymbol(DecoTextures.COMPONENT_DROPDOWN_SYMBOL_PAPER)
				.withOnSelectedEntry((oldChar, newChar) -> edited.sourceVariable = newChar)
		);

		panel.addLabel("Into:", -4+2, yy+28+8+1);
		DecoPanel panelPaperTarget = panel.addComponent(new DecoPanel(-4, yy+28+12+8-2+1)
				.withBackground(DecoTextures.BG_PAPER)
				.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
				.withSize(160, 18)
		);
		panelPaperTarget.addLabel(KEY+(editLoadRules?"packet_variable": "memory_variable"), 4, 6)
				.withSize(112, 9)
				.withAlign(DecoAlignment.LEFT);
		panelPaperTarget.addComponent(new DecoDropdownDataLetters(160-18, 0)
				.withConstraints(new DataPacket())
				.withSelectedEntry((Character)edited.targetVariable)
				.withTextColor(DecoColors.H1, IIColor.fromPackedRGB(0x35322c))
				.withBackground(DecoTextures.COMPONENT_DROPDOWN_DATA_LETTER_PAPER)
				.withDropdownSymbol(DecoTextures.COMPONENT_DROPDOWN_SYMBOL_PAPER)
				.withOnSelectedEntry((oldChar, newChar) -> edited.targetVariable = newChar)
		);

		panel.addComponent(new DecoCheckbox(-2, yy+56+12+2)
				.withSize(148, 12)
				.withText(KEY+"overwrite_existing")
				.withChecked(edited.overwriteExisting)
				.withOnToggle(checked -> edited.overwriteExisting = checked)
				.withTranslatedTooltip(KEY+"overwrite_existing.tooltip")
		);

		panel.addLabel(KEY+"usage_limit", 0, yy+76+3+12-4)
				.withSize(74, 9)
				.withAlign(DecoAlignment.LEFT)
				.withTranslatedTooltip(KEY+"usage_limit.tooltip");
		panel.addComponent(new DecoTextField(78, yy+76-1+12-4)
				.withSize(42, 16)
				.withFilter(TextFilter.DECIMAL)
				.withText(edited.usageLimit < 0?"": String.valueOf(edited.usageLimit))
				.withOnTextChanged(text -> edited.usageLimit = text==null||text.trim().isEmpty()?-1: IIStringUtil.parseInt(text.trim()))
				.withTranslatedTooltip(KEY+"usage_limit.tooltip")
		);
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		return super.onSaveTileData()
				.conditionally(!cancel&&rules!=null, easyNBT -> {
					if(editLoadRules)
						tile.memoryLoadRules = rules;
					else
						tile.memorySaveRules = rules;
					easyNBT.withSerializable(editLoadRules?"memory_load_rules": "memory_save_rules", rules);
				});
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
