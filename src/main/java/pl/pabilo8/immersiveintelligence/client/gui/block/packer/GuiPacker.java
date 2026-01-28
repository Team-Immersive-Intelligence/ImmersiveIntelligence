package pl.pabilo8.immersiveintelligence.client.gui.block.packer;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.PackerHandler;
import pl.pabilo8.immersiveintelligence.api.PackerHandler.PackerActionType;
import pl.pabilo8.immersiveintelligence.api.PackerHandler.PackerTask;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoIngredientStackPickerPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoItemStackDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPacker;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerPacker;
import pl.pabilo8.immersiveintelligence.common.util.*;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import javax.annotation.Nullable;
import java.util.stream.Collectors;

import static pl.pabilo8.immersiveintelligence.common.util.IIReference.GUI_LABEL_KEY;
import static pl.pabilo8.immersiveintelligence.common.util.IIReference.RES_II;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.01.2026
 * @ii-approved 0.3.1
 * @since 25.08.2022
 */
@DecoTemplate(name = "packer", category = DecoGuiCategory.DATA_TILE)
public class GuiPacker extends DecoGui<TileEntityPacker, ContainerPacker>
{
	@DecoResource
	public static ResourceLocation ICON_TASKS = ResLoc.of(RES_II, "gui/tab_icons/tasks");
	@DecoResource
	public static ResourceLocation ICON_LABELER = ResLoc.of(RES_II, "gui/upgrade/packer_naming");

	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public boolean repeatActions = false;
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public EasyCollection<PackerTask, NBTTagCompound> tasks;
	@SyncNBT
	public ListMode mode = ListMode.TASKS;
	private PackerActionType actionType;

	private DecoList<PackerTask> list;
	private DecoPanel panelDetails;
	private DecoCheckbox expiresCheckbox;
	private DecoTextField expiresTextField;
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

		addLinkTab(IIGUI.PACKER, ICON_TASKS, "tasks_module");
		addLinkTab(IIGUI.PACKER_LABELER, ICON_LABELER, "labeler_module");

		//Mode tabs
		addComponents(
				new DecoButton(0, 4)
						.withSize(54, 18)
						.withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_TAB_VERTICAL)
						.withText(GUI_LABEL_KEY+"task_editor.tasks")
						.withTranslatedTooltip(GUI_LABEL_KEY+"task_editor.tasks.tooltip")
						.withOnLMBPressed(() -> {
							mode = ListMode.TASKS;
							refreshListEntries();
						}),
				new DecoButton(54, 4)
						.withSize(54, 18)
						.withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_TAB_VERTICAL)
						.withText(GUI_LABEL_KEY+"task_editor.jobs")
						.withTranslatedTooltip(GUI_LABEL_KEY+"task_editor.jobs.tooltip")
						.withOnLMBPressed(() -> {
							mode = ListMode.JOBS;
							refreshListEntries();
						})
		);

		//Task list (moved down to make room for mode tabs)
		list = addComponent(
				new DecoList<PackerTask>(0, 8+18-4)
						.withSize(108, 116+12-8)
						.withEntries(tasks)
						.withDisplayFunction(new DecoEntryPanelBuilder<PackerTask>()
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
									panel.label("type").withText(task.actionType.getFullLocaleKey()+(task.unpack?".in": ".out"));

									IngredientStack ing = task.stack;
									boolean wildcard = ing==null||"*".equals(ing.oreName);
									panel.label("wild").visible = wildcard;

									DecoItemStackDisplay icon = panel.component("icon", DecoItemStackDisplay.class);
									icon.visible = icon.enabled = !wildcard;
									icon.withStack(!wildcard?ing.getExampleStack(): ItemStack.EMPTY);
								})
						)
						.withOnEntryClicked(task -> {
							selected = task;
							refreshDetails();
						})
		);
		//Apply mode filtering
		refreshListEntries();

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
	}

	private void refreshListEntries()
	{
		//tasks: expirationAmount == -1
		//jobs: expirationAmount != -1
		list.withEntries(tasks.stream()
				.filter(t -> (mode==ListMode.TASKS)==(t.expirationAmount==-1))
				.collect(Collectors.toList())
		);

		//if selection moved out of current view, clear it
		if(selected!=null)
		{
			boolean inView = (mode==ListMode.TASKS)==(selected.expirationAmount==-1);
			if(!inView)
				selected = null;
		}
	}

	private void refreshDetails()
	{
		panelDetails.cleanup();
		if(!(panelDetails.visible = (selected!=null)))
			return;
		final PackerTask task = selected;

		panelDetails.addLabel(GUI_LABEL_KEY+"packer.task_editor", 4, 4)
				.withSize(panelDetails.width-8, 10)
				.withAlign(DecoAlignment.CENTER);

		int yy = 16;

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
		panelDetails.addComponent(new DecoDropdown<PackerHandler.PackerPutMode>(6+48, 16+18*2))
				.withSize(panelDetails.width-48-6-4, 16)
				.withEntries(PackerHandler.PackerPutMode.values())
				.withSelectedEntry(task.mode)
				.withOnSelectedEntry((o, n) -> {
					task.mode = n;
					updateExpiresFields();
				});
		yy += 17;

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
		yy += 17;

		panelDetails.addComponent(new DecoIngredientStackPickerPanel(4, panelDetails.height-56-4)
				.withFluidMode(actionType==PackerActionType.FLUID)
				.withLogisticTagMode(true)
				.withIngredientStack(task.stack)
				.withOnStackChanged(is -> {
					task.stack = is;
					if(task.stack.getExampleStack().isEmpty())
						task.stack = new IngredientStack("*", task.stack.inputSize);
				})
				.withSize(panelDetails.width-8, 56)
		);

		refreshListEntries();
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

	// --- Buttons ---

	private void onAddPressed()
	{
		PackerTask created = new PackerTask(PackerHandler.PackerPutMode.ALL_POSSIBLE, actionType, new IngredientStack("*"));
		//set new jobs default to be expirable (1 time) depending on mode
		created.expirationAmount = (mode==ListMode.TASKS)?-1: 1;
		tasks.add(created);
		selected = created;
		refreshListEntries();
		refreshDetails();
	}

	private void onRemovePressed()
	{
		if(selected==null)
			return;
		tasks.remove(selected);
		selected = null;
		list.withEntries(tasks);
		refreshListEntries();
		refreshDetails();
	}

	private void onDuplicatePressed()
	{
		if(selected==null)
			return;
		PackerTask copy = new PackerTask(selected.serializeNBT());
		tasks.add(selected = copy);

		//Ensure duplicate is visible in current mode, otherwise switch mode to match it
		mode = (copy.expirationAmount==-1)?ListMode.TASKS: ListMode.JOBS;

		refreshListEntries();
		refreshDetails();
	}

	private void onClearPressed()
	{
		//Clear only current mode
		tasks.removeIf(t -> (mode==ListMode.TASKS)==(t.expirationAmount==-1));
		selected = null;
		refreshListEntries();
		refreshDetails();
	}

	public enum ListMode implements ISerializableEnum
	{
		TASKS,
		JOBS;
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
