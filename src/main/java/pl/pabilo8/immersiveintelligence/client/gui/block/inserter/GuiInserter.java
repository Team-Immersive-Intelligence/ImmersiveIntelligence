package pl.pabilo8.immersiveintelligence.client.gui.block.inserter;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoElementDisplays;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoIngredientStackPickerPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoTaskJobList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoTaskJobList.ListMode;
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
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyMultiTypeCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 20.01.2026
 */
@DecoTemplate(name = "inserter", category = DecoGuiCategory.DATA_TILE)
public class GuiInserter extends DecoTileGui<TileEntityInserterBase, ContainerInserter>
{
	private static final String INSERTER_KEY = IIReference.GUI_LABEL_KEY+"inserter.";
	private ListMode mode = ListMode.TASKS;
	private DecoTaskJobList<InserterTask> taskJobList;
	private DecoPanel panelDetails;
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	private EasyMultiTypeCollection<InserterTask> tasks;
	@Nullable
	private InserterTask selected;

	public GuiInserter(EntityPlayer player, TileEntityInserterBase tile)
	{
		super(player, tile, IIGUI.INSERTER);
		if(tile!=null)
			tasks = tile.getTasks().clone();
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 152+96, 152+8)
				.withTitleBar(tile)
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 32, 152+8, 176, 92)
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 108, 8+3, 120+16, 130+16-2)
				.build();

		addComponent((taskJobList = new DecoTaskJobList<>(0, 2))
				.withSize(108, 116)
				.withEntries(tasks)
				.withIsJobPredicate(InserterTask::isJob)
				.withModeHandling(mode, m -> mode = m)
				.withBlankTaskSupplier(() -> {
					Optional<Supplier<InserterTask>> taskSupplier = tile.getAvailableTasks().values().stream().findFirst();
					if(!taskSupplier.isPresent())
						return null;
					InserterTask created = taskSupplier.get().get();
					created.isJob = (taskJobList.getMode()==ListMode.JOBS);
					return created;
				})
				.withOnSelectedChanged(task -> {
					selected = task;
					refreshDetails();
				})
				.withDisplayFunction(new DecoEntryPanelBuilder<InserterTask>()
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_TICKET)
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

							DecoItemStackDisplay icon = panel.component("icon", DecoItemStackDisplay.class);
							icon.visible = icon.enabled = !wildcard;
							icon.withStack(!wildcard?stack.getExampleStack(): ItemStack.EMPTY);
						})
				)
		);

		//Details panel, hidden at first
		panelDetails = addComponent(new DecoPanel(108, 8))
				.withSize(120+16, 130+16+2)
				.withBackground(DecoTextures.BG_PAPER)
				.withBackgroundMask(DecoTextures.TEMPLATE_PAPER);

		// Keep existing helper methods working
		addComponent(new DecoBar(128-16-8+2, 128+32-8+2+2)
				.withSize(96, 12)
				.withHorizontalMode(true)
				.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY_BASE)
				.withLimits(0, tile.getEnergyCapacity(), () -> tile.energyStorage)
		);
		refreshDetails();
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
					int index = this.tasks.indexOf(thisTask);
					this.tasks.remove(thisTask);
					this.selected = tile.getAvailableTasks().get(newType).get();
					this.selected.deserializeNBT(thisTask.serializeNBT());
					this.tasks.add(index, this.selected);
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
						.withLogisticTagMode(true)
						.withFluidMode(thisTask.getName().contains("fluid"))
						.withOnStackChanged(stack -> {
							thisTask.stack = stack;
							//refreshDetails();
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
				.withSerializable("tasks", tasks);
	}

	//--- Task helpers ---

	private static boolean isWildcard(IngredientStack ing)
	{
		ItemStack ex = ing.getExampleStack();
		return ex==null||ex.isEmpty();
	}
}
