package pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoTaskList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoTaskList.ListMode;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine.MemoryTransferRule;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * Arithmetic Logic Machine memory transfer rule list.
 * The same screen is used for memory input and memory output rules.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 01.07.2026
 */
@DecoTemplate(name = "arithmetic_logic_machine_memory", category = DecoGuiCategory.DATA_TILE)
public class GuiArithmeticLogicMachineMemory extends DecoTileGui<TileEntityArithmeticLogicMachine, ContainerArithmeticLogicMachine>
{
	private static final String KEY = IIReference.GUI_LABEL_KEY+"arithmetic_logic_machine.memory.";
	public EasyCollection<MemoryTransferRule, NBTTagCompound> rules;
	@SyncNBT
	public int editedRule = -1;
	@SyncNBT
	public boolean editLoadRules = true;
	@SyncNBT
	public ListMode mode = ListMode.JOBS;

	private GuiArithmeticLogicMachineMemory(EntityPlayer player, TileEntityArithmeticLogicMachine tile, IIGUI gui)
	{
		super(player, tile, gui);
	}

	public static GuiArithmeticLogicMachineMemory getMemoryInputGui(EntityPlayer player, TileEntityArithmeticLogicMachine te)
	{
		return new GuiArithmeticLogicMachineMemory(player, te, IIGUI.ARITHMETIC_LOGIC_MACHINE_MEMORY_IN);
	}

	public static GuiArithmeticLogicMachineMemory getMemoryOutputGui(EntityPlayer player, TileEntityArithmeticLogicMachine te)
	{
		return new GuiArithmeticLogicMachineMemory(player, te, IIGUI.ARITHMETIC_LOGIC_MACHINE_MEMORY_OUT);
	}

	@Override
	public void onInit()
	{
		syncAnimatedParts(tile.door, true);
		syncAnimatedParts(tile.drawer, false);
		syncAnimatedParts(tile.keyboard, false);

		this.editLoadRules = gui==IIGUI.ARITHMETIC_LOGIC_MACHINE_MEMORY_IN;
		this.rules = (this.editLoadRules?tile.memoryLoadRules: tile.memorySaveRules).clone();

		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 176, 128+8)
				.withTitleBar(tile)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 128+8, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		DecoTaskList<MemoryTransferRule> ruleList;
		addComponent((ruleList = new DecoTaskList<>(0, 8-4))
				.withSize(176, 120-16-8-4)
				.withEntries(rules)
				.withIsJobPredicate(MemoryTransferRule::isJob)
				.withModeHandling(mode, m -> mode = m)
				.withBlankTaskSupplier(() -> {
					MemoryTransferRule rule = new MemoryTransferRule();
					rule.usageLimit = ruleList.getMode()==ListMode.REQUESTS?1: -1;
					return rule;
				})
				.withDisplayFunction(new DecoEntryPanelBuilder<MemoryTransferRule>()
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_TICKET)
						.withComponent("source", p -> new DecoButton(3, 2)
								.withSize(16, 16)
								.withDisabled(true)
								.withTextDisabledColor(IIColor.fromPackedRGB(0xafafaf))
						)
						.withLabel("route", p -> new DecoLabel(fontRenderer, 3+16, 5)
								.withRawText(" into ")
								.withSize(24, 12)
								.withAlign(DecoAlignment.CENTER)
						)
						.withComponent("destination", p -> new DecoButton(3+16+24, 2)
								.withSize(16, 16)
								.withDisabled(true)
								.withTextDisabledColor(IIColor.fromPackedRGB(0xafafaf))
						)
						.withLabel("behavior", p -> new DecoLabel(fontRenderer, 3+16+24+16+3, 5)
								.withSize(24, 12)
								.withAlign(DecoAlignment.LEFT)
						)
						.withComponent(p -> new DecoButton(p.width-17+1-14, 2)
								.withTemplate(DecoTemplates.ACTION_BUTTON_EDIT)
								.withOnLMBPressed(() -> {
									editedRule = rules.indexOf(p.getCurrentElement());
									if(editedRule >= 0)
										changeGUI(IIGUI.ARITHMETIC_LOGIC_MACHINE_MEMORY_EDIT);
								})
						)
						.withComponent(p -> new DecoButton(p.width-17+1, 2)
								.withTemplate(DecoTemplates.ACTION_BUTTON_REMOVE)
								.withOnLMBPressed(() -> {
									int index = rules.indexOf(p.getCurrentElement());
									if(index >= 0)
									{
										rules.remove(index);
										refreshGUI();
									}
								})
						)
						.withElementApplyMethod((rule, panel) -> {
							panel.component("source", DecoButton.class)
									.withRawText(String.valueOf(rule.sourceVariable))
									.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
									.withBackgroundColor(editLoadRules?DecoColors.IN: DecoColors.OUT);
							panel.component("destination", DecoButton.class)
									.withRawText(String.valueOf(rule.targetVariable))
									.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
									.withBackgroundColor(editLoadRules?DecoColors.OUT: DecoColors.IN);
							panel.label("behavior")
									.withText(KEY+"mode."+(rule.overwriteExisting?"overwrite": "supplement"));
						})
				)
		);

		addLinkTab(IIGUI.ARITHMETIC_LOGIC_MACHINE_STORAGE, DecoTextures.ICON_STORAGE, "storage_module");
		addLinkTab(IIGUI.ARITHMETIC_LOGIC_MACHINE_MEMORY_IN, DecoTextures.ICON_MEMORY, "memory_in_module");

		//Circuit tabs
		NonNullList<ItemStack> inventory = tile.inventory;
		for(int i = 0, inventorySize = inventory.size(); i < inventorySize; i++)
		{
			ItemStack circuit = inventory.get(i);
			if(!circuit.isEmpty()&&circuit.getItem() instanceof ItemIIFunctionalCircuit)
			{
				int circuitIndex = i;
				addComponent(new DecoTab()
						.withOnPressed((gui, button, mouseX, mouseY) ->
								changeGUI(IIGUI.ARITHMETIC_LOGIC_MACHINE_VARIABLES,
										EasyNBT.newNBT().withInt("editedCircuit", circuitIndex), null))
						.withIcon(circuit)
						.withTranslatedTooltip(circuit.getDisplayName(),
								TextFormatting.GRAY.toString()+TextFormatting.ITALIC+I18n.format(IIReference.DESCRIPTION_KEY+"variables_module"))
				);
			}
		}

		addLinkTab(IIGUI.ARITHMETIC_LOGIC_MACHINE_MEMORY_OUT, DecoTextures.ICON_MEMORY, "memory_out_module");
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

	@Override
	protected EasyNBT onSaveTileData()
	{
		if(editLoadRules)
			tile.memoryLoadRules = rules;
		else
			tile.memorySaveRules = rules;
		return super.onSaveTileData()
				.withSerializable(editLoadRules?"memory_load_rules": "memory_save_rules", rules);
	}
}
