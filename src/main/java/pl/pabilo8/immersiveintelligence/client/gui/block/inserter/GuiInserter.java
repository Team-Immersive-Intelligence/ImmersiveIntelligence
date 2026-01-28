package pl.pabilo8.immersiveintelligence.client.gui.block.inserter;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoElementDisplays;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoIngredientStackPickerPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoItemStackDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityInserterBase;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityInserterBase.InserterTask;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerInserter;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 20.01.2026
 */
@DecoTemplate(name = "inserter", category = DecoGuiCategory.DATA_TILE)
public class GuiInserter extends DecoGui<TileEntityInserterBase, ContainerInserter>
{
	private static final String TASK_EDITOR_KEY = IIReference.GUI_LABEL_KEY+"task_editor.";
	private static final String INSERTER_KEY = IIReference.GUI_LABEL_KEY+"inserter.";
	private ListMode mode = ListMode.TASKS;
	private DecoList<InserterTask> list;
	private DecoPanel panelDetails;

	/**
	 * Local, editable copy of tasks (copied from tile on init).
	 */
	private final ArrayList<InserterTask> localTasks = new ArrayList<>();

	@Nullable
	private InserterTask selected;

	public GuiInserter(EntityPlayer player, TileEntityInserterBase tile)
	{
		super(player, tile, IIGUI.INSERTER);
		if(tile!=null)
		{
			//Deep copy tasks from tile to a local editable list
			localTasks.clear();
			for(InserterTask t : tile.getTasks())
				localTasks.add(t.copy(tile));
		}
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.GUI_BG_STEEL, 0, 0, 152+96, 152+8)
				.withTitleBar(tile)
				.withNextLayer()
				.withBox(DecoTextures.GUI_BG_WOODEN, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND_WOODEN, 32, 152+8, 176, 92)
				.withFrame(DecoTextures.GUI_FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.withNextLayer()
				.withBox(DecoTextures.GUI_BG_STEEL, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_SQUARE, 108, 8+3, 120+16, 130+16-2)
				.build();

		//Mode tabs
		addComponents(
				new DecoButton(0, 8)
						.withSize(54, 18)
						.withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_TAB_VERTICAL)
						.withText(TASK_EDITOR_KEY+"tasks")
						.withTranslatedTooltip(TASK_EDITOR_KEY+"tasks.tooltip")
						.withOnLMBPressed(() -> {
							mode = ListMode.TASKS;
							refreshListEntries();
						}),
				new DecoButton(54, 8)
						.withSize(54, 18)
						.withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_TAB_VERTICAL)
						.withText(TASK_EDITOR_KEY+"jobs")
						.withTranslatedTooltip(TASK_EDITOR_KEY+"jobs.tooltip")
						.withOnLMBPressed(() -> {
							mode = ListMode.JOBS;
							refreshListEntries();
						})
		);

		//Task list
		list = addComponent(
				new DecoList<InserterTask>(0, 8+18)
						.withSize(108, 116)
						.withDisplayFunction(new DecoEntryPanelBuilder<InserterTask>()
								.withBackground(DecoTextures.GUI_BG_PAPER)
								.withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_TICKET)
								.withComponent("icon", new DecoItemStackDisplay(3, 2).withSize(16, 16))
								.withLabel("wild", new DecoLabel(fontRenderer, 3, 2)
										.withSize(16, 16)
										.withAlign(DecoAlignment.CENTER)
										.withRawText("*")
										.withTextColor(IIReference.COLOR_IMMERSIVE_ORANGE)
								)
								.withLabel("type", new DecoLabel(fontRenderer, 3+16+4, 2)
										.withSize(96-3-16-6, 16)
										.withAlign(DecoAlignment.LEFT)
										.withRawText("task")
								)
								.withElementApplyMethod((task, panel) -> {
									IngredientStack stack = task.stack;
									panel.label("type").withText(INSERTER_KEY+"tasks."+task.getName());

									boolean wildcard = isWildcard(stack);
									panel.label("wild").visible = wildcard;

									panel.component("icon", DecoItemStackDisplay.class).visible = panel.component("icon", DecoItemStackDisplay.class).enabled = !wildcard;
									if(!wildcard)
										panel.component("icon", DecoItemStackDisplay.class).withStack(stack.getExampleStack());
									else
										panel.component("icon", DecoItemStackDisplay.class).withStack(ItemStack.EMPTY);
								})
						)
						.withOnEntryClicked(task -> {
							selected = task;
							refreshDetails();
						})
		);
		//Add filtered list entries
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
						.withOnLMBPressed(this::onClearPressed),
				new DecoBar(128-16-8+2, 128+32-8+2+2)
						.withSize(96, 12)
						.withHorizontalMode(true)
						.withTemplate(DecoGuiUtils.BAR_ELECTRIC_ENERGY_BASE)
						.withLimits(0, tile.getEnergyCapacity(), () -> tile.energyStorage)
		);

		//Details panel, hidden at first
		panelDetails = addComponent(new DecoPanel(108, 8))
				.withSize(120+16, 130+16+2)
				.withBackground(DecoTextures.GUI_BG_PAPER)
				.withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_PAPER);
		refreshDetails();
	}

	private void refreshListEntries()
	{
		//Only show tasks/jobs depending on mode
		list.withEntries(localTasks.stream()
				.filter(t -> mode==ListMode.TASKS^t.isJob())
				.collect(Collectors.toList())
		);
	}

	private void refreshDetails()
	{
		panelDetails.cleanup();
		//Hide panel if nothing is selected
		if(!(panelDetails.visible = (selected!=null)))
			return;
		final InserterTask thisTask = selected;

		panelDetails.addLabel(INSERTER_KEY+"task_editor", 4, 4)
				.withSize(panelDetails.width-8, 10)
				.withAlign(DecoAlignment.CENTER);

		panelDetails.addLabel(INSERTER_KEY+"type", 6, 20-2);
		panelDetails.addComponent(new DecoDropdown<String>(42-4, 20-8+2))
				.withSize(panelDetails.width-42, 16)
				.withEntries(tile.getAvailableTasks().keySet())
				.withSelectedEntry(thisTask.getName())
				.withDisplayFunction(DecoElementDisplays.getSimpleTextDisplay(taskName ->
						I18n.format(INSERTER_KEY+"tasks."+taskName)))
				.withOnSelectedEntry((oldType, newType) -> {
					int index = this.localTasks.indexOf(thisTask);
					this.localTasks.remove(thisTask);
					this.localTasks.add(index, this.selected = thisTask.copy(tile, newType));
					this.refreshListEntries();
					this.refreshDetails();
				});

		panelDetails.addLabel(INSERTER_KEY+"input", 6, 34)
				.withSize(42, 16)
				.withAlign(DecoAlignment.LEFT);
		panelDetails.addComponents(
				new DecoDropdown<EnumFacing>(6+42, 34)
						.withSize(64, 16)
						.withEntries(EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST)
						.withSelectedEntry(thisTask.facingIn==null?tile.defaultInputFacing: thisTask.facingIn)
						.withOnSelectedEntry((oldV, newV) -> thisTask.facingIn = newV)
						.withTranslatedTooltip(INSERTER_KEY+"input.facing.tooltip"),
				new DecoTextField(74+42, 34)
						.withSize(16, 16)
						.withFilter(TextFilter.DECIMAL)
						.withText(thisTask.distanceIn==-1?String.valueOf(tile.defaultInputDistance): String.valueOf(thisTask.distanceIn))
						.withOnTextChanged(string -> thisTask.distanceIn = IIStringUtil.parseInt(string))
						.withTranslatedTooltip(INSERTER_KEY+"input.distance.tooltip")
		);

		panelDetails.addLabel(INSERTER_KEY+"output", 6, 34+18)
				.withSize(42, 16)
				.withAlign(DecoAlignment.LEFT);
		panelDetails.addComponents(
				new DecoDropdown<EnumFacing>(6+42, 34+18)
						.withSize(64, 16)
						.withEntries(EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST)
						.withSelectedEntry(thisTask.facingOut==null?tile.defaultOutputFacing: thisTask.facingOut)
						.withOnSelectedEntry((oldV, newV) -> thisTask.facingOut = newV)
						.withTranslatedTooltip(INSERTER_KEY+"output.facing.tooltip"),
				new DecoTextField(74+42, 34+18)
						.withSize(16, 16)
						.withFilter(TextFilter.DECIMAL)
						.withText(thisTask.distanceOut==-1?String.valueOf(tile.defaultOutputDistance): String.valueOf(thisTask.distanceOut))
						.withOnTextChanged(string -> thisTask.distanceOut = IIStringUtil.parseInt(string))
						.withTranslatedTooltip(INSERTER_KEY+"output.distance.tooltip")
		);

		panelDetails.addLabel(INSERTER_KEY+"items_per_step", 6, 34+18+18)
				.withSize(64, 16)
				.withAlign(DecoAlignment.LEFT);
		panelDetails.addComponents(
				new DecoTextField(6+64, 34+18+18)
						.withSize(panelDetails.width-6-64-4, 16)
						.withText(thisTask.overrideTakeAmount==-1?String.valueOf(tile.takeAmount): String.valueOf(thisTask.overrideTakeAmount))
						.withOnTextChanged(string -> thisTask.overrideTakeAmount = IIStringUtil.parseInt(string))
						.withTranslatedTooltip(INSERTER_KEY+"items_per_step.tooltip")
						.withDisabled(!thisTask.areDetailsEditable()),
				new DecoIngredientStackPickerPanel(6-2, 34+18+18+18)
						.withFluidMode(thisTask.getName().contains("fluid"))
						.withLogisticTagMode(true)
						.withOnStackChanged(stack -> {
							thisTask.stack = stack;
							refreshListEntries();
						})
						.withIngredientStack(thisTask.stack)
						.withSize(panelDetails.width-6-2, 56)
						.withDisabled(!thisTask.areDetailsEditable())
		);

	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		return super.onSaveTileData()
				.withList("tasks", InserterTask::toNBT, localTasks);
	}

	//--- Buttons ---

	private void onAddPressed()
	{
		//Pick first available task type as default
		Optional<Function<NBTTagCompound, InserterTask>> function =
				tile.getAvailableTasks().values().stream().findFirst();

		if(!function.isPresent())
			return;
		//Create the task
		InserterTask created = function.get().apply(new NBTTagCompound());
		created.isJob = mode==ListMode.JOBS;
		localTasks.add(created);
		selected = created;

		refreshListEntries();
		refreshDetails();
	}

	private void onRemovePressed()
	{
		if(selected==null)
			return;
		localTasks.remove(selected);
		selected = null;
		refreshListEntries();
		refreshDetails();
	}

	private void onDuplicatePressed()
	{
		if(selected==null)
			return;
		localTasks.add(selected = selected.copy(tile));
		refreshListEntries();
		refreshDetails();
	}

	private void onClearPressed()
	{
		localTasks.removeIf(inserterTask -> inserterTask.isJob==(mode==ListMode.JOBS));
		selected = null;
		refreshListEntries();
		refreshDetails();
	}

	//--- Task helpers ---

	private static boolean isWildcard(IngredientStack ing)
	{
		ItemStack ex = ing.getExampleStack();
		return ex==null||ex.isEmpty();
	}

	private enum ListMode
	{
		TASKS,
		JOBS
	}
}
