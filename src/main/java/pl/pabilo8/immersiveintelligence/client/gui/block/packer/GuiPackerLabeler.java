package pl.pabilo8.immersiveintelligence.client.gui.block.packer;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.LogisticTag;
import pl.pabilo8.immersiveintelligence.api.PackerHandler.LabelingTask;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoIngredientStackPickerPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoIngredientStackPickerPanel.PickerPanelMode;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoTaskList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoTaskList.ListMode;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoItemStackDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPacker;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerPacker;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import javax.annotation.Nullable;

import static pl.pabilo8.immersiveintelligence.common.util.IIReference.GUI_LABEL_KEY;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.01.2026
 * @ii-approved 0.3.1
 * @since 25.08.2022
 */
@DecoTemplate(name = "packer_labeler", category = DecoGuiCategory.DATA_TILE)
public class GuiPackerLabeler extends DecoTileGui<TileEntityPacker, ContainerPacker>
{
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public EasyCollection<LabelingTask, NBTTagCompound> labels;
	@SyncNBT
	public ListMode mode = ListMode.JOBS;

	private DecoTaskList<LabelingTask> taskList;
	private DecoPanel panelDetails;
	private DecoCheckbox expiresCheckbox, serialStartCheckbox;
	private DecoTextField expiresTextField, serialStartTextField;
	private DecoIngredientStackPickerPanel panelFilterPicker, panelOutputPicker;

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
				.withBox(DecoTextures.BG_STEEL, 0, 0, 2*108+160+6+4, 144+16)
				.withTitleBar(tile)
				.withNextLayer()

				//Task background
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_PAPER, 104+6, 8-4, 160, 132+16)
				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 108+160+6-4, 8-4, 108+4+4, 132+16)
				.withNextLayer()

				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 104+6-8, 144+16, 160+16, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		//Change GUI into normal packer if the upgrade is removed while in labeler GUI
		addValueListener(() -> !tile.isUpgradeInstalled(IIContent.UPGRADE_PACKER_NAMING))
				.addObserver(notInstalled -> {
					if(notInstalled)
						this.changeGUI(IIGUI.PACKER);
				});

		//Title label above list
		addLinkTab(IIGUI.PACKER, DecoTextures.ICON_TASKS, "tasks_module");
		addLinkTab(IIGUI.PACKER_LABELER, GuiPacker.ICON_LABELER, "labeler_module");

		// Replace mode tabs + list + action buttons with a standardized component
		addComponent((taskList = new DecoTaskList<>(0, 0))
				.withSize(108, 116+12-8)
				.withEntries(labels)
				.withIsJobPredicate(t -> t.expirationAmount==-1)
				.withModeHandling(mode, m -> mode = m)
				.withBlankTaskSupplier(() -> {
					LabelingTask created = new LabelingTask();
					created.filter = new IngredientStack("*");
					created.expirationAmount = (taskList.getMode()==ListMode.JOBS)?-1: 1;
					created.serialBatch = 0;
					return created;
				})
				.withOnSelectedChanged(task -> {
					selected = task;
					refreshDetails();
				})
				.withDisplayFunction(new DecoEntryPanelBuilder<LabelingTask>()
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_TICKET)
						.withComponent("icon", new DecoItemStackDisplay(3, 2).withSize(16, 16))
						.withLabel("type", new DecoLabel(fontRenderer, 23, 2)
								.withSize(59, 16)
								.withAlign(DecoAlignment.LEFT)
						)
						.withLabel("expires", new DecoLabel(fontRenderer, 23, 12)
								.withSize(82, 8)
								.withAlign(DecoAlignment.LEFT)
								.withTextColor(IIReference.COLOR_IMMERSIVE_ORANGE)
						)
						.withElementApplyMethod((task, panel) -> {
							IngredientStack ing = task.filter;
							boolean wildcard = ing==null||"*".equals(ing.oreName);

							DecoItemStackDisplay icon = panel.component("icon", DecoItemStackDisplay.class);
							icon.visible = icon.enabled = !wildcard;
							icon.withStack(!wildcard?ing.getExampleStack(): ItemStack.EMPTY);

							panel.label("type").withText(GUI_LABEL_KEY+"packer.labeler.task");

							String exp = task.expirationAmount==-1?GUI_LABEL_KEY+"packer.expires.never"
									: (GUI_LABEL_KEY+"packer.expires.after"+" "+task.expirationAmount);
							panel.label("expires").withText(exp);
						})
				)
		);

		//Details panel
		panelDetails = addComponent(new DecoPanel(104+4+2, 8-4))
				.withSize(160, 132+12+8)
				.withBackground(DecoTextures.BG_PAPER)
				.withBackgroundMask(DecoTextures.TEMPLATE_PAPER);

		refreshDetails();
	}

	private void refreshDetails()
	{
		panelDetails.cleanup();
		if(!(panelDetails.visible = (selected!=null)))
			return;
		final LabelingTask task = selected;
		int yy = 4;

		//Header
		panelDetails.addLabel(GUI_LABEL_KEY+"packer.labeler.header", 6, yy)
				.withSize(panelDetails.width-12, 16)
				.withAlign(DecoAlignment.LEFT);
		yy += 11;

		//Name
		panelDetails.addComponent(new DecoTextField(4, yy))
				.withSize(panelDetails.width-8, 16)
				.withFilter(TextFilter.DECIMAL)
				.withOnTextChanged(s -> task.name = s);
		yy += 17;

		//Expire after...
		expiresCheckbox = panelDetails.addComponent(new DecoCheckbox(6, yy)
				.withSize(64, 16)
				.withText(GUI_LABEL_KEY+"packer.expires")
				.withOnToggle(checked -> {
					if(!checked)
						task.expirationAmount = -1;
					else if(task.expirationAmount==-1)
						task.expirationAmount = 1;
					updateExpiresFields();
				})
		);
		expiresTextField = panelDetails.addComponent(new DecoTextField(panelDetails.width-32-4, yy))
				.withSize(32, 16)
				.withFilter(TextFilter.DECIMAL)
				.withOnTextChanged(s -> {
					task.expirationAmount = s==null||s.trim().isEmpty()?-1: IIStringUtil.parseInt(s.trim());
					updateExpiresFields();
				});
		updateExpiresFields();
		yy += 17;

		//Serial batching
		serialStartCheckbox = panelDetails.addComponent(new DecoCheckbox(6, yy))
				.withText(GUI_LABEL_KEY+"packer.labeler.serial_start")
				.withSize(panelDetails.width-6-4-32, 16)
				.withTranslatedTooltip(GUI_LABEL_KEY+"packer.labeler.serial_start.tooltip")
				.withChecked(task.serialBatch!=-1)
				.withOnToggle(checked -> {
					if(!checked)
					{
						task.serialBatch = -1;
						updateSerialBatching();
					}
					else if(task.expirationAmount==-1)
					{
						task.serialBatch = 0;
						updateSerialBatching();
					}
				});
		serialStartTextField = panelDetails.addComponent(new DecoTextField(panelDetails.width-32-4, yy))
				.withSize(32, 16)
				.withFilter(TextFilter.DECIMAL)
				.withText(String.valueOf(task.serialBatch))
				.withOnTextChanged(s -> {
					task.serialBatch = IIStringUtil.parseInt(s==null?"0": s.trim());
					updateSerialBatching();
				});
		yy += 17;

		//Filter + (optional) tags
		panelDetails.addComponent((panelFilterPicker = new DecoIngredientStackPickerPanel(4, panelDetails.height-56-4))
				.withMode(PickerPanelMode.ITEM_LOGISTIC_TAG)
				.withIngredientStack(task.filter)
				.withOnStackChanged(is -> {
					//Prefer LogiTags
					if(LogisticTag.hasLogisticsTag(is.getExampleStack()))
					{
						task.logiTagIn = LogisticTag.getLogisticsTagFromStack(is.getExampleStack());
						task.filter = new IngredientStack("*", task.filter.inputSize);
					}
					else
					{
						task.filter = is;
						if(task.filter.getExampleStack().isEmpty())
							task.filter = new IngredientStack("*", task.filter.inputSize);
					}
				})
				.withSize(panelDetails.width-8, 56)
		);
		panelDetails.addComponent((panelOutputPicker = new DecoIngredientStackPickerPanel(4, panelDetails.height-56-4))
				.withMode(PickerPanelMode.ITEM)
				.withIngredientStack(new IngredientStack(IIContent.itemLogisticTag.getStack(task.logiTagOut, 1)))
				.withOnStackChanged(is -> {
					if(LogisticTag.hasLogisticsTag(is.getExampleStack()))
					{
						task.logiTagOut = LogisticTag.getLogisticsTagFromStack(is.getExampleStack());
						task.filter = new IngredientStack("*", task.filter.inputSize);
					}
				})
				.withSize(panelDetails.width-8, 56)
		);

		panelDetails.addComponents(
				new DecoButton(4, panelDetails.height-56-4-14)
						.withSize((panelDetails.width-8)/2, 16)
						.withBackground(DecoTextures.COMPONENT_TAB_VERTICAL)
						.withText(GUI_LABEL_KEY+"packer.picker.container")
						.withTranslatedTooltip(GUI_LABEL_KEY+"packer.picker.container.tooltip")
						.withOnLMBPressed(() -> {
							panelFilterPicker.visible = panelFilterPicker.enabled = true;
							panelOutputPicker.visible = panelOutputPicker.enabled = false;
						}),
				new DecoButton(4+(panelDetails.width-8)/2, panelDetails.height-56-4-14)
						.withSize((panelDetails.width-8)/2, 16)
						.withBackground(DecoTextures.COMPONENT_TAB_VERTICAL)
						.withText(GUI_LABEL_KEY+"packer.picker.logitag")
						.withTranslatedTooltip(GUI_LABEL_KEY+"packer.picker.logitag.tooltip")
						.withOnLMBPressed(() -> {
							panelFilterPicker.visible = panelFilterPicker.enabled = false;
							panelOutputPicker.visible = panelOutputPicker.enabled = true;
						})
		);

		panelFilterPicker.visible = panelFilterPicker.enabled = false;
		panelOutputPicker.visible = panelOutputPicker.enabled = true;
	}

	private void updateSerialBatching()
	{
		if(selected==null)
			return;
		if(serialStartCheckbox!=null)
			serialStartCheckbox.withChecked(selected.serialBatch!=-1);
		if(serialStartTextField!=null)
			serialStartTextField.withText(selected.serialBatch==-1?"": String.valueOf(selected.serialBatch));
	}

	private void updateExpiresFields()
	{
		if(selected==null)
			return;
		if(expiresCheckbox!=null)
			expiresCheckbox.withChecked(selected.expirationAmount!=-1);
		if(expiresTextField!=null)
			expiresTextField.withText(selected.expirationAmount==-1?"": String.valueOf(selected.expirationAmount));
		if(taskList!=null)
			taskList.setMode(selected.expirationAmount==-1?ListMode.JOBS: ListMode.REQUESTS);
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		tile.labels = labels;
		return super.onSaveTileData();
	}
}
