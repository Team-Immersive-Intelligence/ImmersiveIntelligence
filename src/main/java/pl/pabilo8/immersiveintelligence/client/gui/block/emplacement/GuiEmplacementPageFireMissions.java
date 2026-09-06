package pl.pabilo8.immersiveintelligence.client.gui.block.emplacement;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSwitch;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoTaskList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.EmplacementTargetManager.EmplacementFireMission;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.EmplacementTargetManager.FireMissionEdit;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.EmplacementTargetManager.FireMissionTargetType;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetingLimits;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;

/**
 * Edits revision-checked Emplacement Fire Mission Requests.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 31.08.2026
 * @since 16.07.2021
 */
@DecoTemplate(name = "emplacement_fire_missions", category = DecoGuiCategory.TERRITORY_CONTROL_TILE)
public class GuiEmplacementPageFireMissions extends GuiEmplacement
{
	private static final String FIRE_KEY = "ii.gui.emplacement.fire_mission.";

	private FireMissionEdit missionEdit;
	private DecoTaskList<EmplacementFireMission> taskList;
	private DecoPanel detailsPanel;
	@Nullable
	private EmplacementFireMission selected;
	private boolean dirty;

	public GuiEmplacementPageFireMissions(EntityPlayer player, TileEntityEmplacement tile)
	{
		super(player, tile, IIGUI.EMPLACEMENT_FIRE_MISSIONS);
		if(tile!=null)
			missionEdit = tile.taskManager.createFireMissionEdit();
	}

	@Override
	public void onInit()
	{
		super.onInit();
		if(missionEdit==null)
			missionEdit = tile.taskManager.createFireMissionEdit();

		addComponent(taskList = new DecoTaskList<EmplacementFireMission>(0, 0)
				.withSize(96, 110)
				.withShowJobsTab(false)
				.withEntries(missionEdit.getMissions())
				.withIsJobPredicate(EmplacementFireMission::isJob)
				.withCanModifyPredicate(EmplacementFireMission::isPositionMission)
				.withBlankTaskSupplier(() -> missionEdit.getMissions().size() >= TargetingLimits.MAX_FIRE_MISSIONS?null:
						new EmplacementFireMission(() -> tile.getWorld()).withPosition(tile.getPos()))
				.withDuplicateFunction(mission -> !mission.isPositionMission()||missionEdit.getMissions().size() >= TargetingLimits.MAX_FIRE_MISSIONS?null:
						mission.copyWithNewId())
				.withOnEntriesChanged(this::markDirty)
				.withOnSelectedChanged(mission -> {
					selected = mission;
					refreshDetails();
				})
				.withDisplayFunction(createMissionDisplay()));

		detailsPanel = addComponent(new DecoPanel(98, 10)
				.withSize(148, 144)
				.withBackground(DecoTextures.BG_PAPER)
				.withBackgroundMask(DecoTextures.TEMPLATE_PAPER));
		refreshDetails();
	}

	private DecoEntryPanelBuilder<EmplacementFireMission> createMissionDisplay()
	{
		return new DecoEntryPanelBuilder<EmplacementFireMission>()
				.withHeight(26)
				.withBackground(DecoTextures.BG_PAPER)
				.withBackgroundMask(DecoTextures.TEMPLATE_TICKET)
				.withLabel("name", () -> new DecoLabel(fontRenderer, 3, 2)
						.withSize(86, 10)
						.withAlign(DecoAlignment.LEFT))
				.withLabel("target", () -> new DecoLabel(fontRenderer, 3, 13)
						.withSize(86, 10)
						.withAlign(DecoAlignment.LEFT))
				.withElementApplyMethod((mission, panel) -> {
					panel.label("name").withRawText(getMissionDisplayName(mission));
					panel.label("target").withRawText(getMissionTargetText(mission));
				})
				.withRefreshInterval(1);
	}

	private void refreshDetails()
	{
		if(detailsPanel==null)
			return;
		detailsPanel.cleanup();
		if(!(detailsPanel.visible = selected!=null))
			return;

		EmplacementFireMission mission = selected;
		detailsPanel.addLabel(FIRE_KEY+"editor", 4, 4)
				.withSize(detailsPanel.width-8, 10)
				.withAlign(DecoAlignment.CENTER);
		if(!mission.isPositionMission())
		{
			detailsPanel.addLabel(FIRE_KEY+"entity_not_editable", 8, 28)
					.withSize(detailsPanel.width-16, 42)
					.withAlign(DecoAlignment.CENTER);
			return;
		}

		detailsPanel.addLabel(FIRE_KEY+"name", 4, 18).withSize(42, 14).withAlign(DecoAlignment.LEFT);
		detailsPanel.addComponent(new DecoTextField(48, 18)
				.withSize(detailsPanel.width-52, 14)
				.withMaxStringLength(TargetingLimits.MAX_STRING_LENGTH)
				.withText(mission.name)
				.withTranslatedTooltip(FIRE_KEY+"name.tooltip")
				.withOnTextChanged(text -> {
					mission.withName(text);
					markDirty();
				}));

		addPositionTargetEditor(mission, 36);

		detailsPanel.addComponent(new DecoSwitch(4, 85)
				.withRawText(I18n.format(FIRE_KEY+"finite_shots"))
				.withSize(detailsPanel.width-8, 11)
				.withTranslatedTooltip(FIRE_KEY+"finite_shots.tooltip")
				.withCurrentState(mission.target.hasFiniteShots())
				.withOnToggle(finite -> {
					if(finite)
						mission.withShotLimit(Math.max(1, mission.target.getShotsRemaining()));
					else
						mission.withInfiniteShots();
					markDirty();
					refreshDetails();
				}));

		detailsPanel.addLabel(FIRE_KEY+"shots", 4, 102).withSize(42, 14).withAlign(DecoAlignment.LEFT);
		detailsPanel.addComponent(new DecoTextField(48, 102)
				.withSize(detailsPanel.width-52, 14)
				.withFilter(TextFilter.DECIMAL)
				.withText(mission.target.hasFiniteShots()?mission.target.getShotsRemaining(): 0)
				.withTranslatedTooltip(FIRE_KEY+"shots.tooltip")
				.withDisabled(!mission.target.hasFiniteShots())
				.withOnTextChanged(text -> {
					int shots = TextFilter.DECIMAL.parseInt(text, mission.target.getShotsRemaining());
					mission.withShotLimit(Math.max(1, Math.min(TargetingLimits.MAX_SHOTS, shots)));
					markDirty();
				}));
	}

	private void addPositionTargetEditor(EmplacementFireMission mission, int y)
	{
		BlockPos position = mission.target.getPosition();
		if(position==null)
			position = tile.getPos();
		detailsPanel.addLabel(FIRE_KEY+"position", 4, y).withSize(detailsPanel.width-8, 10).withAlign(DecoAlignment.LEFT);
		addCoordinateField(mission, 4, y+12, position.getX(), 'x');
		addCoordinateField(mission, 49, y+12, position.getY(), 'y');
		addCoordinateField(mission, 94, y+12, position.getZ(), 'z');
	}

	private void addCoordinateField(EmplacementFireMission mission, int x, int y, int value, char axis)
	{
		detailsPanel.addComponent(new DecoTextField(x, y)
				.withSize(41, 14)
				.withFilter(TextFilter.DECIMAL)
				.withText(value)
				.withTranslatedTooltip(FIRE_KEY+"coordinate."+axis+".tooltip")
				.withOnTextChanged(text -> updateCoordinate(mission, axis,
						TextFilter.DECIMAL.parseInt(text, value))));
	}

	private void updateCoordinate(EmplacementFireMission mission, char axis, int value)
	{
		BlockPos old = mission.target.getPosition();
		if(old==null)
			old = tile.getPos();
		int x = axis=='x'?value: old.getX();
		int y = axis=='y'?value: old.getY();
		int z = axis=='z'?value: old.getZ();
		mission.withPosition(new BlockPos(x, y, z));
		markDirty();
	}

	private String getMissionDisplayName(EmplacementFireMission mission)
	{
		return mission.name==null||mission.name.isEmpty()?I18n.format(FIRE_KEY+"unnamed"): mission.name;
	}

	private String getMissionTargetText(EmplacementFireMission mission)
	{
		String target;
		if(mission.type==FireMissionTargetType.ENTITY)
			target = I18n.format(FIRE_KEY+"entity_value", mission.target.getEntityID());
		else
		{
			BlockPos pos = mission.target.getPosition();
			target = pos==null?I18n.format(FIRE_KEY+"position_unset"):
					I18n.format(FIRE_KEY+"position_value", pos.getX(), pos.getY(), pos.getZ());
		}
		String shots = mission.target.hasFiniteShots()?I18n.format(FIRE_KEY+"shots_left", mission.target.getShotsRemaining()):
				I18n.format(FIRE_KEY+"until_done");
		return target+" - "+shots;
	}

	private void markDirty()
	{
		dirty = true;
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		if(!dirty||missionEdit==null)
			return EasyNBT.newNBT();
		dirty = false;
		return EasyNBT.newNBT().withTag("tasks", missionEdit.serializeNBT());
	}
}
