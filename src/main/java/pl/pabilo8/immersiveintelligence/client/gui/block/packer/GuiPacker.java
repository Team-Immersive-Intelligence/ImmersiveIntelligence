package pl.pabilo8.immersiveintelligence.client.gui.block.packer;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.PackerHandler;
import pl.pabilo8.immersiveintelligence.api.PackerHandler.PackerActionType;
import pl.pabilo8.immersiveintelligence.api.PackerHandler.PackerTask;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoIngredientStackPickerPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoIngredientStackPickerPanel.PickerPanelMode;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoTaskJobList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoTaskJobList.ListMode;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoItemStackDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoScrollableItemSlots;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Packer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPacker;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerPacker;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.ILocalizedEnum;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import javax.annotation.Nullable;

import static pl.pabilo8.immersiveintelligence.common.util.IIReference.GUI_LABEL_KEY;
import static pl.pabilo8.immersiveintelligence.common.util.IIReference.RES_II;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.01.2026
 * @ii-approved 0.3.1
 * @since 25.08.2022
 */
@DecoTemplate(name = "packer", category = DecoGuiCategory.DATA_TILE)
public class GuiPacker extends DecoTileGui<TileEntityPacker, ContainerPacker>
{
	@DecoResource
	public static ResourceLocation ICON_LABELER = ResLoc.of(RES_II, "gui/upgrade/packer_naming");

	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public EasyCollection<PackerTask, NBTTagCompound> tasks;
	@SyncNBT
	public ListMode mode = ListMode.TASKS;

	private PackerActionType actionType;
	private DecoTaskJobList<PackerTask> taskJobList;
	private DecoPanel panelDetails, panelResources;
	private DecoCheckbox expiresCheckbox;
	private DecoTextField expiresTextField;
	private DecoIngredientStackPickerPanel panelContainerFilterPicker, panelStackFilterPicker;
	@Nullable
	private PackerTask selected;

	public GuiPacker(EntityPlayer player, TileEntityPacker tile)
	{
		super(player, tile, IIGUI.PACKER);
		if(tile!=null)
		{
			tasks = tile.tasks.clone();
			if(tile.isUpgradeInstalled(IIContent.UPGRADE_PACKER_ENERGY))
				actionType = PackerHandler.PackerActionType.ENERGY;
			else if(tile.isUpgradeInstalled(IIContent.UPGRADE_PACKER_FLUID))
				actionType = PackerHandler.PackerActionType.FLUID;
			else
				actionType = PackerActionType.ITEM;
		}
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
				.conditionally(actionType!=PackerActionType.ITEM, builder -> builder
						.withInventorySlots(SlotStyle.IE_INPUT, container.slotsInput)
						.withInventorySlots(SlotStyle.IE_OUTPUT, container.slotsOutput)
				)
				.withInventoryTitleBar()
				.build();

		if(tile.isUpgradeInstalled(IIContent.UPGRADE_PACKER_NAMING))
		{
			addLinkTab(IIGUI.PACKER, DecoTextures.ICON_TASKS, "tasks_module");
			addLinkTab(IIGUI.PACKER_LABELER, ICON_LABELER, "labeler_module");
		}

		// Replace mode tabs + list + action buttons with a single component
		addComponent((taskJobList = new DecoTaskJobList<>(0, 0))
				.withSize(108, 116+12-8)
				.withEntries(tasks)
				.withIsJobPredicate(t -> t.expirationAmount!=-1)
				.withModeHandling(mode, m -> mode = m)
				.withBlankTaskSupplier(() -> {
					PackerTask created = new PackerTask(PackerHandler.PackerPutMode.ALL_POSSIBLE, actionType, new IngredientStack("*"));
					created.expirationAmount = (taskJobList.getMode()==ListMode.TASKS)?-1: 1;
					return created;
				})
				.withOnSelectedChanged(task -> {
					selected = task;
					refreshDetails();
				})
				.withDisplayFunction(new DecoEntryPanelBuilder<PackerTask>()
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_TICKET)
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
							panel.label("type").withText(task.actionType.getFullLocaleKey()+(task.unpack?".in": ".out"));

							IngredientStack ing = task.stack;
							boolean wildcard = ing==null||"*".equals(ing.oreName);
							panel.label("wild").visible = wildcard;

							DecoItemStackDisplay icon = panel.component("icon", DecoItemStackDisplay.class);
							icon.visible = icon.enabled = !wildcard;
							icon.withStack(!wildcard?ing.getExampleStack(): ItemStack.EMPTY);
						})
				)
		);

		//Details panel
		panelDetails = addComponent(new DecoPanel(104+4+2, 8-4))
				.withSize(160, 132+12+8)
				.withBackground(DecoTextures.BG_PAPER)
				.withBackgroundMask(DecoTextures.TEMPLATE_PAPER);

		//Resources (items, fluid or energy storage in container)
		panelResources = addComponent(new DecoPanel(108+160+6-4, 8-4))
				.withSize(108+4+4+4, 132+12+8)
				.withBackground(null);

		refreshDetails();
		refreshResources();
	}

	private void refreshResources()
	{
		panelResources.cleanup();
		panelResources.addComponent(
				new DecoBar(4+4, 128+32-8+2+2-16-8+2)
						.withSize(panelResources.width-16, 12)
						.withHorizontalMode(true)
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage))
		);
		switch(actionType)
		{
			case ENERGY:
			{
				panelResources.addComponent(new DecoBar(4+4, 4+8+4+8+8+4+2))
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorageUpgrade))
						.withSize(14*2-4, panelResources.height-32-8-32-8-4);
				panelResources.addComponent(new DecoBar(4+32+2+4, 4+8+4))
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY_INPUT)
						.withLimits(0, tile.energyStorageUpgrade.getLimitReceive(), () -> tile.energyStorageUpgrade.getAverageInsertion())
						.withSize(14, panelResources.height-32-8);
				panelResources.addComponent(new DecoBar(4+32+2+32, 4+8+4))
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY_OUTPUT)
						.withLimits(0, tile.energyStorageUpgrade.getLimitExtract(), () -> tile.energyStorageUpgrade.getAverageExtraction())
						.withSize(14, panelResources.height-32-8);
			}
			break;
			case FLUID:
			{
				panelResources.addComponent(new DecoFluidTank(4+2, 4+18+8+2))
						.withFluidTank(tile.fluidTankUpgradeInput)
						.withSize(52, panelResources.height-8-32-32-12+4)
						.withColorMarker(IIReference.COLOR_ENGINEERS_BLUE);
				panelResources.addComponent(new DecoFluidTank(4+2+32+4+20, 4+18+8+2))
						.withFluidTank(tile.fluidTankUpgradeOutput)
						.withSize(52, panelResources.height-8-32-32-12+4)
						.withColorMarker(IIReference.COLOR_IMMERSIVE_ORANGE);
			}
			break;
			case ITEM:
			{
				//Add scrollable item lists
				final DecoScrollableItemSlots slotsInput = panelResources.addComponent(new DecoScrollableItemSlots(0, 8+4+2+4))
						.withSlots(container.slotsInput)
						.withColumns(6)
						.withHeight(panelResources.height-8-16-8-8);
				final DecoScrollableItemSlots slotsOutput = panelResources.addComponent(new DecoScrollableItemSlots(0, 8+4+2+4))
						.withSlots(container.slotsOutput)
						.withColumns(6)
						.withHeight(panelResources.height-8-16-8-8);
				slotsOutput.visible = false;

				//Add
				panelResources.addComponent(new DecoButton(0, -2+4))
						.withSize(panelResources.width/2, 16)
						.withBackground(DecoTextures.COMPONENT_TAB_VERTICAL)
						.withText(GUI_LABEL_KEY+"packer.item.input")
						.withTranslatedTooltip(GUI_LABEL_KEY+"packer.item.input.tooltip")
						.withOnLMBPressed(() -> {
							slotsInput.visible = true;
							slotsOutput.visible = false;
						});
				panelResources.addComponent(new DecoButton(panelResources.width/2, -2+4))
						.withSize(panelResources.width/2, 16)
						.withBackground(DecoTextures.COMPONENT_TAB_VERTICAL)
						.withText(GUI_LABEL_KEY+"packer.item.output")
						.withTranslatedTooltip(GUI_LABEL_KEY+"packer.item.output.tooltip")
						.withOnLMBPressed(() -> {
							slotsInput.visible = false;
							slotsOutput.visible = true;
						});
			}
			break;
		}
	}

	private void refreshDetails()
	{
		panelDetails.cleanup();
		if(!(panelDetails.visible = (selected!=null)))
			return;
		final PackerTask task = selected;
		int yy = 4;

		//Transfer:
		panelDetails.addLabel(GUI_LABEL_KEY+"packer.task", 6, yy)
				.withSize(54, 16)
				.withAlign(DecoAlignment.LEFT);
		//Items/Fluid/Energy
		panelDetails.addComponent(new DecoDropdown<PackerActionType>(6+54, yy))
				.withSize(panelDetails.width-6-54-4, 16)
				.withEntries(actionType)
				.withSelectedEntry(task.actionType)
				.withDisabled(true);
		yy += 17;

		//from
		panelDetails.addLabel(GUI_LABEL_KEY+"packer.direction", 6, yy)
				.withSize(32, 16)
				.withAlign(DecoAlignment.LEFT);
		//Packer to Contrainer / Container to Packer
		panelDetails.addComponent(new DecoDropdown<PackingDirection>(6+32, yy))
				.withSize(panelDetails.width-32-6-4, 16)
				.withEntries(PackingDirection.PACK, PackingDirection.UNPACK)
				.withSelectedEntry(task.unpack?PackingDirection.UNPACK: PackingDirection.PACK)
				.withOnSelectedEntry((o, n) -> task.unpack = PackingDirection.UNPACK.equals(n));
		yy += 17;

		//All possible/Stack/etc.
		panelDetails.addLabel(GUI_LABEL_KEY+"packer.mode", 6, yy)
				.withSize(48, 16)
				.withAlign(DecoAlignment.LEFT);
		panelDetails.addComponent(new DecoDropdown<PackerHandler.PackerPutMode>(6+48, yy))
				.withSize(panelDetails.width-48-6-4, 16)
				.withEntries(PackerHandler.PackerPutMode.values())
				.withSelectedEntry(task.mode)
				.withOnSelectedEntry((o, n) -> {
					task.mode = n;
					updateExpiresFields();
				});
		yy += 15;

		panelDetails.addComponent(new DecoCheckbox(6, yy))
				.withSize(48, 12)
				.withText(GUI_LABEL_KEY+"packer.repeat")
				.withChecked(task.repeat)
				.withOnToggle(checked -> {
					task.repeat = checked;
				});
		yy += 12;

		//And expire after...
		expiresCheckbox = panelDetails.addComponent(new DecoCheckbox(6, yy)
				.withSize(48, 16)
				.withText(GUI_LABEL_KEY+"packer.expires")
				.withOnToggle(checked -> {
					if(!checked)
					{
						task.expirationAmount = -1;
						updateExpiresFields();
					}
					else if(task.expirationAmount==-1)
					{
						task.expirationAmount = 1;
						updateExpiresFields();
					}
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

		//Itemstack picker panels
		PickerPanelMode panelMode = actionType==PackerActionType.ITEM?PickerPanelMode.ITEM_LOGISTIC_TAG:
				(actionType==PackerActionType.FLUID?PickerPanelMode.FLUID: PickerPanelMode.ENERGY);
		panelDetails.addComponent((panelStackFilterPicker = new DecoIngredientStackPickerPanel(4, panelDetails.height-56-4))
				.withMode(panelMode)
				.withIngredientStack(task.stack)
				.withOnStackChanged(is -> {
					task.stack = is;
					if(task.stack.getExampleStack().isEmpty())
						task.stack = new IngredientStack("*", task.stack.inputSize);
				})
				.withMaxEnergy(Packer.energyCapacityUpgradeMaxTransfer)
				.withSize(panelDetails.width-8, 56)
		);
		panelDetails.addComponent((panelContainerFilterPicker = new DecoIngredientStackPickerPanel(4, panelDetails.height-56-4))
				.withMode(panelMode)
				.withIngredientStack(task.containerFilter)
				.withOnStackChanged(is -> {
					task.containerFilter = is;
					if(task.containerFilter.getExampleStack().isEmpty())
						task.containerFilter = new IngredientStack("*");
				})
				.withMaxEnergy(Packer.energyCapacityUpgradeMaxTransfer)
				.withSize(panelDetails.width-8, 56)
		);

		//Itemstack panel picker buttons
		panelDetails.addComponents(
				new DecoButton(4, panelDetails.height-56-4-14)
						.withSize((panelDetails.width-8)/2, 16)
						.withBackground(DecoTextures.COMPONENT_TAB_VERTICAL)
						.withText(GUI_LABEL_KEY+"packer.picker.container")
						.withTranslatedTooltip(GUI_LABEL_KEY+"packer.picker.container.tooltip")
						.withOnLMBPressed(() -> {
							panelContainerFilterPicker.visible = panelContainerFilterPicker.enabled = true;
							panelStackFilterPicker.visible = panelStackFilterPicker.enabled = false;
						}),
				new DecoButton(4+(panelDetails.width-8)/2, panelDetails.height-56-4-14)
						.withSize((panelDetails.width-8)/2, 16)
						.withBackground(DecoTextures.COMPONENT_TAB_VERTICAL)
						.withText(GUI_LABEL_KEY+"packer.picker.stack")
						.withTranslatedTooltip(GUI_LABEL_KEY+"packer.picker.stack.tooltip")
						.withOnLMBPressed(() -> {
							panelContainerFilterPicker.visible = panelContainerFilterPicker.enabled = false;
							panelStackFilterPicker.visible = panelStackFilterPicker.enabled = true;
						})
		);

		panelContainerFilterPicker.visible = panelContainerFilterPicker.enabled = false;
		panelStackFilterPicker.visible = panelStackFilterPicker.enabled = true;
	}

	private void updateExpiresFields()
	{
		if(selected==null)
			return;
		if(expiresCheckbox!=null)
			expiresCheckbox.withChecked(selected.expirationAmount!=-1);
		if(expiresTextField!=null)
			expiresTextField.withText(selected.expirationAmount==-1?"": String.valueOf(selected.expirationAmount));

	}

	public enum PackingDirection implements ILocalizedEnum
	{
		PACK,
		UNPACK;

		@Override
		public String geLocaleKey()
		{
			return GUI_LABEL_KEY+"packer.direction.";
		}
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		tile.tasks = tasks;
		return super.onSaveTileData();
	}
}
