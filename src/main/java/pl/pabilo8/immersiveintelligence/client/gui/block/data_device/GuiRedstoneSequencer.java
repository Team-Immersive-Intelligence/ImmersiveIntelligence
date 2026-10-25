package pl.pabilo8.immersiveintelligence.client.gui.block.data_device;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSlider;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoTaskList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoTaskList.ListMode;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.DataDeviceUtils;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityRedstoneSequencer;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityRedstoneSequencer.SequenceTask;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerSmallDataDevice;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * Configures repeating and finite redstone sequence steps.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 25.10.2026
 * @since 24.10.2026
 */
@DecoTemplate(name = "redstone_sequencer", category = DecoGuiCategory.DATA_TILE)
public class GuiRedstoneSequencer extends DecoTileGui<TileEntityRedstoneSequencer, ContainerSmallDataDevice<TileEntityRedstoneSequencer>>
{
	private static final String KEY = "ii.gui.small_data_devices.";

	@SyncNBT
	public EasyCollection<SequenceTask, NBTTagCompound> tasks = new EasyCollection<>(SequenceTask::new);
	@SyncNBT
	public ListMode mode = ListMode.JOBS;
	@SyncNBT
	public boolean running = true, initialRunning = true, dirty = false;
	@SyncNBT
	public int expectedRevision = 0;

	public GuiRedstoneSequencer(EntityPlayer player, TileEntityRedstoneSequencer tile)
	{
		super(player, tile, IIGUI.REDSTONE_SEQUENCER);
		if(tile!=null)
		{
			tasks = tile.tasks.clone();
			running = tile.running;
			initialRunning = running;
			expectedRevision = tile.revision;
		}
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 240, 168)
				.withTitleBar(tile)
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 32, 168, 176, 92)
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 4, 16+4, 232, 152-8)
				.build();

		DecoTaskList<SequenceTask> list = new DecoTaskList<SequenceTask>(4, 6)
				.withSize(228+4, 88+16);
		list.withEntries(tasks)
				.withIsJobPredicate(SequenceTask::isJob)
				.withModeHandling(mode, value -> mode = value)
				.withBlankTaskSupplier(() -> {
					if(tasks.size() >= DataDeviceUtils.MAX_RULES)
						return null;
					SequenceTask task = new SequenceTask();
					task.isJob = list.getMode()==ListMode.JOBS;
					return task;
				})
				.withOnEntriesChanged(() -> dirty = true)
				.withDisplayFunction(new DecoEntryPanelBuilder<SequenceTask>()
						.withHeight(24)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_TICKET)
						.withComponent("strength", p -> new DecoSlider(4, 5)
								.withSize(100, 12)
								.withBarColors(DecoColors.GRADIENT1, DecoColors.GRADIENT2)
								.withRange(0, 15)
								.withIntegersOnly(true)
								.withValue(p.getCurrentElement().strength)
								.withTranslatedTooltip(KEY+"strength")
								.withOnValueChanged(value -> {
									p.getCurrentElement().strength = MathHelper.clamp(Math.round(value), 0, 15);
									p.label("strength").withRawText(Integer.toString(p.getCurrentElement().strength));
									dirty = true;
								}))
						.withLabel("strength", p -> new DecoLabel(fontRenderer, 108, 7)
								.withSize(16, 10)
								.withAlign(DecoAlignment.LEFT)
								.withRawText(Integer.toString(p.getCurrentElement().strength))
						)
						.withLabel("duration", p -> new DecoLabel(fontRenderer, 132, 7)
								.withSize(28, 10)
								.withAlign(DecoAlignment.LEFT)
								.withText(KEY+"duration"))
						.withComponent("duration", p -> new DecoTextField(166, 3)
								.withSize(p.width-170, 16)
								.withFilter(TextFilter.DECIMAL)
								.withText(Integer.toString(p.getCurrentElement().duration))
								.withOnTextChanged(text -> {
									p.getCurrentElement().duration = MathHelper.clamp(IIStringUtil.parseInt(text), 1, DataDeviceUtils.MAX_TICKS);
									dirty = true;
								}))
						.withLabel("repeats", p -> new DecoLabel(fontRenderer, 4, 26)
								.withSize(54, 10)
								.withAlign(DecoAlignment.LEFT)
								.withText(KEY+"repeats"))
						.withComponent("repeats", p -> new DecoTextField(62, 3)
								.withSize(60, 16)
								.withFilter(TextFilter.DECIMAL)
								.withText(Integer.toString(p.getCurrentElement().repeats))
								.withOnTextChanged(text -> {
									p.getCurrentElement().repeats = MathHelper.clamp(IIStringUtil.parseInt(text), 1, DataDeviceUtils.MAX_TICKS);
									dirty = true;
								}))
						.withElementApplyMethod((task, panel) -> {
							panel.height = task.isJob?24: 42;
							panel.label("repeats").visible = !task.isJob;
							DecoTextField repeats = panel.component("repeats", DecoTextField.class);
							repeats.visible = repeats.enabled = !task.isJob;
							repeats.withPosition(62, task.isJob?3: 22);
						}));
		addComponent(list);
		addComponent(new DecoCheckbox(10, 149)
				.withSize(218, 12)
				.withText(KEY+"running")
				.withChecked(running)
				.withOnToggle(value -> running = value));
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		return EasyNBT.newNBT()
				.conditionally(dirty, nbt -> nbt.withSerializable("tasks", tasks).withInt("expectedRevision", expectedRevision))
				.conditionally(running!=initialRunning, nbt -> nbt.withBoolean("running", running));
	}
}
