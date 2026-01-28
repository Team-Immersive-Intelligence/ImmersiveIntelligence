package pl.pabilo8.immersiveintelligence.client.gui.block.packer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.PackerHandler.LabelingTask;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoItemStackDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPacker;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerPacker;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.01.2026
 * @ii-approved 0.3.1
 * @since 25.08.2022
 */
@DecoTemplate(name = "packer_labeler", category = DecoGuiCategory.DATA_TILE)
public class GuiPackerLabeler extends DecoGui<TileEntityPacker, ContainerPacker>
{
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public EasyCollection<LabelingTask, NBTTagCompound> labels;

	private DecoList<LabelingTask> list;
	private DecoPanel panelDetails;
	@Nullable
	private LabelingTask selected;

	public GuiPackerLabeler(EntityPlayer player, TileEntityPacker tile)
	{
		super(player, tile, IIGUI.PACKER_LABELER);
		if(tile!=null)
			labels = tile.labels.clone();
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.GUI_BG_STEEL, 0, 0, 2*108+160+6+4, 144+16)
				.withTitleBar(tile)
				.withNextLayer()

				//Task background
				.withBox(DecoTextures.GUI_BG_STEEL, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_PAPER, 104+6, 8-4, 160, 132+16)
				.withNextLayer()
				.withBox(DecoTextures.GUI_BG_STEEL, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_SQUARE, 108+160+6-4, 8-4, 108+4+4, 132+16)
				.withNextLayer()

				.withBox(DecoTextures.GUI_BG_WOODEN, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND_WOODEN, 104+6-8, 144+16, 160+16, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		//Title label above list
		addLabel(IIReference.GUI_LABEL_KEY+"packer.labeling_tasks", 0, 6)
				.withSize(108, 10)
				.withAlign(DecoAlignment.CENTER);

		addLinkTab(IIGUI.PACKER, GuiPacker.ICON_TASKS, "tasks_module");
		addLinkTab(IIGUI.PACKER_LABELER, GuiPacker.ICON_LABELER, "labeler_module");

		//Task list (replaces GuiPackerTaskList)
		list = addComponent(
				new DecoList<LabelingTask>(0, 16)
						.withSize(108, 116+10)
						.withEntries(labels)
						.withDisplayFunction(new DecoEntryPanelBuilder<LabelingTask>()
								.withBackground(DecoTextures.GUI_BG_PAPER)
								.withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_TICKET)
								.withComponent("icon", new DecoItemStackDisplay(3, 2).withSize(16, 16))
								.withLabel("wild", new DecoLabel(fontRenderer, 3, 2)
										.withSize(16, 16)
										.withAlign(DecoAlignment.CENTER)
										.withRawText("*")
										.withTextColor(IIReference.COLOR_IMMERSIVE_ORANGE)
								)
								.withLabel("type", new DecoLabel(fontRenderer, 23, 2)
										.withSize(59, 16)
										.withAlign(DecoAlignment.LEFT)
								)
								.withElementApplyMethod((task, panel) -> {
									//panel.label("type").withText(task.actionType.getFullLocaleKey()+(task.unpack?".out": ".in"));


									boolean wildcard = "*".equals(task.stack.oreName);
									panel.label("wild").visible = wildcard;

									panel.component("icon", DecoItemStackDisplay.class).visible = panel.component("icon", DecoItemStackDisplay.class).enabled = !wildcard;
									if(!wildcard)
										panel.component("icon", DecoItemStackDisplay.class).withStack(task.stack);
									else
										panel.component("icon", DecoItemStackDisplay.class).withStack(ItemStack.EMPTY);
								})
						)
						.withOnEntryClicked(task -> {
							selected = task;
							refreshDetails();
						})
		);

		//Action buttons
		addComponents(
				new DecoButton(24-21, 20+116+4+3)
						.withTemplate(DecoGuiUtils.LIST_BUTTON_ADD_TEMPLATE)
						.withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_BUTTON)
						.withBackgroundColor(IIColor.fromHex("efefef"))
						.withSize(25, 14)
						.withOnLMBPressed(this::onAddPressed),
				new DecoButton(24+25+1-21, 20+116+4+3)
						.withTemplate(DecoGuiUtils.LIST_BUTTON_REMOVE_TEMPLATE)
						.withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_BUTTON)
						.withBackgroundColor(IIColor.fromHex("efefef"))
						.withSize(25, 14)
						.withOnLMBPressed(this::onRemovePressed),
				new DecoButton(24+2*(25+1)-21, 20+116+4+3)
						.withTemplate(DecoGuiUtils.LIST_BUTTON_DUPLICATE_TEMPLATE)
						.withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_BUTTON)
						.withBackgroundColor(IIColor.fromHex("efefef"))
						.withSize(25, 14)
						.withOnLMBPressed(this::onDuplicatePressed),
				new DecoButton(24+3*(25+1)-21, 20+116+4+3)
						.withTemplate(DecoGuiUtils.LIST_BUTTON_CLEAR_TEMPLATE)
						.withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_BUTTON)
						.withBackgroundColor(IIColor.fromHex("efefef"))
						.withSize(25, 14)
						.withOnLMBPressed(this::onClearPressed)
		);

		//Details panel
		panelDetails = addComponent(new DecoPanel(104+4+2, 8-4))
				.withSize(160, 132+12+8)
				.withBackground(DecoTextures.GUI_BG_PAPER)
				.withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_PAPER);

		refreshDetails();

		//Change GUI into normal packer if the upgrade is removed while in labeler GUI
		addValueListener(() -> !tile.isUpgradeInstalled(IIContent.UPGRADE_PACKER_NAMING))
				.addObserver(notInstalled -> {
					if(notInstalled)
						this.changeGUI(IIGUI.PACKER);
				});
	}

	private void refreshDetails()
	{
		panelDetails.cleanup();
		if(!(panelDetails.visible = (selected!=null)))
			return;

		final LabelingTask task = selected;

		panelDetails.addLabel("ii.gui.packer.task_editor", 4, 4)
				.withSize(panelDetails.width-8, 10)
				.withAlign(DecoAlignment.CENTER);

		// If the list entry text/icon depends on changes, refresh list view cheaply.
		list.withEntries(labels);
	}

	// --- Buttons ---

	private void onAddPressed()
	{
		LabelingTask created = new LabelingTask();
		labels.add(created);
		selected = created;
		list.withEntries(labels);
		refreshDetails();
	}

	private void onRemovePressed()
	{
		if(selected==null)
			return;
		labels.remove(selected);
		selected = null;
		list.withEntries(labels);
		refreshDetails();
	}

	private void onDuplicatePressed()
	{
		if(selected==null)
			return;
		labels.add(selected = new LabelingTask(selected.serializeNBT()));
		list.withEntries(labels);
		refreshDetails();
	}

	private void onClearPressed()
	{
		labels.clear();
		selected = null;
		list.withEntries(labels);
		refreshDetails();
	}
}
