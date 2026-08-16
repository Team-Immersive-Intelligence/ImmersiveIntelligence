package pl.pabilo8.immersiveintelligence.client.gui.block.flagpole;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSlider;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoMapDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.DecoMapDefaultColorMapper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.layers.MapLayerBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Flagpole;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFlagpole;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerFlagpole;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 * Configures Flagpole's loaded chunks and upgrade settings.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 15.08.2026
 * @since 14.08.2026
 */
@DecoTemplate(name = "flagpole_config", category = DecoGuiCategory.TERRITORY_CONTROL_TILE)
public class GuiFlagpoleConfig extends DecoTileGui<TileEntityFlagpole, ContainerFlagpole>
{
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public int chunkLoadingRange;
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public boolean displayFactionFlag;
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public boolean prioritizeFactionFlag;
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public boolean soundDistressAlarm;
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public boolean sendDistressPacket;
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public boolean teslaCoilActive;

	public GuiFlagpoleConfig(EntityPlayer player, TileEntityFlagpole tile)
	{
		super(player, tile, IIGUI.FLAGPOLE_CONFIG);
	}

	@Override
	public void onInit()
	{
		//Variables
		this.chunkLoadingRange = tile.getChunkLoadingRange();
		this.displayFactionFlag = tile.displayFactionFlag;
		this.prioritizeFactionFlag = tile.prioritizeFactionFlag;
		this.soundDistressAlarm = tile.soundDistressAlarm;
		this.sendDistressPacket = tile.sendDistressPacket;
		this.teslaCoilActive = tile.teslaCoilActive;

		//Background
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 152+96, 152)
				.withTitleBar(tile)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 32, 152, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withNextLayer()
				//Use the inverse proportions of the map page: narrow map, wide controls.
				.withBox(DecoTextures.BG_PAPER, DecoTextures.TEMPLATE_PAPER, 0, 0, 96, 152)
				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 96, 0, 152, 152)
				.build();

		//Tabs
		addLinkTab(IIGUI.FLAGPOLE, DecoTextures.ICON_MAP, "map_module");
		addLinkTab(IIGUI.FLAGPOLE_CONFIG, DecoTextures.ICON_CONFIG, "configuration_module");
		if(tile.getOwnerIdentity()!=DiplomacyHandler.NEUTRAL)
			addLinkTab(IIGUI.FLAGPOLE_FACTION, DecoTextures.ICON_FACTION_CONFIG, "faction_module");

		//Calculate chunk center
		BlockPos chunkCenterPos = new BlockPos(((tile.getPos().getX()>>4)<<4)+8, tile.getPos().getY(), ((tile.getPos().getZ()>>4)<<4)+8);
		int maxChunkLoadingRange = Math.max(0, Math.min(Flagpole.maxChunksLoadedRadius, Flagpole.chunkClaimRadius));

		//Add map display
		DecoMapDisplay mapDisplay = addComponent(new DecoMapDisplay(4, 0)
				.withSize(88, 144)
				.withRegion(tile.getWorld(), chunkCenterPos.getX(), chunkCenterPos.getZ(), 256)
				.withColorMapper(DecoMapDefaultColorMapper.TERRAIN)
				.withBaseZoom(1f)
				.withPanning(false)
				.withScanner(DecoTemplates.getFlagpoleScanner(() -> true))
				.withScanner(DecoTemplates.getEmplacementScanner(() -> true))
				.withScanner(DecoTemplates.getArtilleryHowitzerScanner(() -> true))
				.withScanner(DecoTemplates.getRadarScanner(() -> true))
				.withScanner(DecoTemplates.getRadioStationScanner(() -> true))
		);
		updateRangePreview(mapDisplay, chunkCenterPos, chunkLoadingRange);

		int controlsX = 100;
		int controlsWidth = 144;

		//Base settings
		addLabel(IIReference.GUI_LABEL_KEY+"flagpole.config.chunk_loading_range", controlsX, 8)
				.withSize(controlsWidth, 12)
				.withAlign(DecoAlignment.CENTER);
		addLabel(IIReference.GUI_LABEL_KEY+"flagpole.config.chunks", () -> new String[]{String.valueOf(this.chunkLoadingRange)}, controlsX+controlsWidth-32-24, 8+6)
				.withSize(32+24, 12);
		addComponents(
				//Chunk loading range slider.
				new DecoSlider(controlsX, 17)
						.withSize(controlsWidth-32-2-24, 12)
						.withRange(0, Math.max(1, maxChunkLoadingRange))
						.withIntegersOnly(true)
						.withValue(chunkLoadingRange)
						.withDisabled(maxChunkLoadingRange==0)
						.withOnValueChanged(value -> setChunkLoadingRange(Math.round(value), mapDisplay, chunkCenterPos))
						.withBarColors(IIColor.fromPackedRGB(0x663f26), IIColor.fromPackedRGB(0xb37e28)),

				new DecoCheckbox(controlsX, 36)
						.withSize(controlsWidth, 12)
						.withText(IIReference.GUI_LABEL_KEY+"flagpole.config.display_faction_flag")
						.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"flagpole.config.display_faction_flag.tooltip")
						.withChecked(displayFactionFlag)
						.withOnToggle(value -> displayFactionFlag = value),
				new DecoCheckbox(controlsX, 50)
						.withSize(controlsWidth, 12)
						.withText(IIReference.GUI_LABEL_KEY+"flagpole.config.prioritize_faction_flag")
						.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"flagpole.config.prioritize_faction_flag.tooltip")
						.withChecked(prioritizeFactionFlag)
						.withOnToggle(value -> prioritizeFactionFlag = value)
		);

		//Distress Signal upgrade settings
		int upgradeY = 64;
		if(tile.isUpgradeInstalled(IIContent.UPGRADE_FLAGPOLE_DISTRESS_SIGNAL))
		{
			addLabel(IIReference.GUI_LABEL_KEY+"flagpole.config.distress_signal", controlsX, upgradeY)
					.withSize(controlsWidth, 12)
					.withAlign(DecoAlignment.CENTER);
			addComponents(
					new DecoCheckbox(controlsX, upgradeY+13)
							.withSize(controlsWidth, 12)
							.withText(IIReference.GUI_LABEL_KEY+"flagpole.config.sound_alarm")
							.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"flagpole.config.sound_alarm.tooltip")
							.withChecked(soundDistressAlarm)
							.withOnToggle(value -> soundDistressAlarm = value),
					new DecoCheckbox(controlsX, upgradeY+27)
							.withSize(controlsWidth, 12)
							.withText(IIReference.GUI_LABEL_KEY+"flagpole.config.send_distress_packet")
							.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"flagpole.config.send_distress_packet.tooltip")
							.withChecked(sendDistressPacket)
							.withOnToggle(value -> sendDistressPacket = value)
			);
			upgradeY += 46;
		}

		//Tesla Coil upgrade settings
		if(tile.isUpgradeInstalled(IIContent.UPGRADE_FLAGPOLE_TASER_LOCKS))
		{
			addLabel(IIReference.GUI_LABEL_KEY+"flagpole.config.tesla_coil", controlsX, upgradeY)
					.withSize(controlsWidth, 12)
					.withAlign(DecoAlignment.CENTER);

			addComponent(new DecoCheckbox(controlsX, upgradeY+13)
					.withSize(controlsWidth, 12)
					.withText(IIReference.GUI_LABEL_KEY+"flagpole.config.tesla_coil_active")
					.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"flagpole.config.tesla_coil_active.tooltip")
					.withChecked(teslaCoilActive)
					.withOnToggle(value -> teslaCoilActive = value));
		}
	}

	private void setChunkLoadingRange(int range, DecoMapDisplay mapDisplay, BlockPos pos)
	{
		this.chunkLoadingRange = MathHelper.clamp(range, 0, Math.max(0, Math.min(Flagpole.chunkClaimRadius, Flagpole.maxChunksLoadedRadius)));
		this.updateRangePreview(mapDisplay, pos, chunkLoadingRange);
	}

	private void updateRangePreview(DecoMapDisplay mapDisplay, BlockPos pos, int range)
	{
		int chunkX = pos.getX()>>4;
		int chunkZ = pos.getZ()>>4;
		//Add a box around the loaded chunk area
		MapLayerBuilder builder = mapDisplay.withLayer("chunk_loading")
				.clear();
		if(range > 0)
			builder.addRectangleFrame((chunkX-(range-1))<<4, (chunkZ-(range-1))<<4,
					(chunkX+(range-1)+1)<<4, (chunkZ+(range-1)+1)<<4,
					IIReference.COLOR_IMMERSIVE_ORANGE.withAlpha(48), IIReference.COLOR_IMMERSIVE_ORANGE
			);
	}
}
