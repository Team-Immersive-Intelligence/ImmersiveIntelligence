package pl.pabilo8.immersiveintelligence.client.gui.block.flagpole;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSwitch;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoMapDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.DecoMapDefaultColorMapper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.IDecoMapColorMapper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.scanners.BlockTypeScanner;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFlagpole;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRadar;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerFlagpole;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 27.12.2025
 */
@DecoTemplate(name = "flagpole", category = DecoGuiCategory.TERRITORY_CONTROL_TILE)
public class GuiFlagpole extends DecoTileGui<TileEntityFlagpole, ContainerFlagpole>
{
	@SyncNBT
	public boolean filterFlagpoles = true, filterWeapons = true, filterLogistics = true, filterIntelligence = true, filterWireNetworks = true;

	public GuiFlagpole(EntityPlayer player, TileEntityFlagpole tile)
	{
		super(player, tile, IIGUI.FLAGPOLE);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 152+96, 152)
				.withTitleBar(tile)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 32, 152, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withNextLayer()
				.withBox(DecoTextures.BG_PAPER, DecoTextures.TEMPLATE_PAPER, 0, 0, 152, 152)
				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 152, 0, 64+32, 152)
				.build();

		//Tabs
		addLinkTab(IIGUI.FLAGPOLE, DecoTextures.ICON_MAP, "map_module");
		if(tile.getOwnerIdentity()!=DiplomacyHandler.NEUTRAL)
			addLinkTab(IIGUI.FLAGPOLE_FACTION, DecoTextures.ICON_FACTION_CONFIG, "faction_module");

		BlockPos pos = tile.getPos();
		addLabel("Shown Markers", 152+4, 4)
				.withSize(96-8, 12)
				.withAlign(DecoAlignment.TOP);
		addLabel("Map Type", 152+4, 4+10+12*5)
				.withSize(96-8, 12)
				.withAlign(DecoAlignment.TOP);
		DecoMapDisplay mapDisplay;
		addComponents(
				mapDisplay = new DecoMapDisplay(4, 4)
						.withSize(152-8, 152-8)
						.withRegion(pos.getX(), pos.getZ(), 128)
						.withColorMapper(DecoMapDefaultColorMapper.TERRAIN)
						.withPanning(true)
						.withZoomScrolling(1, 4)
						.withScanner(new BlockTypeScanner("flagpoles")
								.withMultiblockFilter(TileEntityFlagpole.class)
								.withUpdateCondition(() -> this.filterFlagpoles)
								.withMarkerStyle(DecoTextures.MAP_MARKER_FLAGPOLE, 4, IIColor.WHITE)
						)
						.withScanner(new BlockTypeScanner("emplacement")
								.withMultiblockFilter(TileEntityEmplacement.class)
								.withUpdateCondition(() -> this.filterWeapons)
								.withMarkerStyle(DecoTextures.MAP_MARKER_EMPLACEMENT, 4, IIColor.WHITE)
						)
						.withScanner(new BlockTypeScanner("artillery_howitzer")
								.withMultiblockFilter(TileEntityArtilleryHowitzer.class)
								.withUpdateCondition(() -> this.filterWeapons)
								.withMarkerStyle(DecoTextures.MAP_MARKER_EMPLACEMENT, 6, IIColor.WHITE)
						)
						.withScanner(new BlockTypeScanner("intelligence")
								.withMultiblockFilter(TileEntityRadar.class)
								.withUpdateCondition(() -> this.filterFlagpoles)
								.withMarkerStyle(DecoTextures.MAP_MARKER_RADAR, 6, IIColor.WHITE)
						),

				new DecoCheckbox(152+4, 12)
						.withSize(96-8, 12)
						.withText(IIReference.GUI_LABEL_KEY+"map_display.marker.flagpoles")
						.withChecked(this.filterFlagpoles)
						.withOnToggle(newValue -> this.filterFlagpoles = newValue),
				new DecoCheckbox(152+4, 12+12)
						.withSize(96-8, 12)
						.withText(IIReference.GUI_LABEL_KEY+"map_display.marker.weapons")
						.withChecked(this.filterWeapons)
						.withOnToggle(newValue -> this.filterWeapons = newValue),
				new DecoCheckbox(152+4, 12+12*2)
						.withSize(96-8, 12)
						.withText(IIReference.GUI_LABEL_KEY+"map_display.marker.logistics")
						.withChecked(this.filterLogistics)
						.withOnToggle(newValue -> this.filterLogistics = newValue),
				new DecoCheckbox(152+4, 12+12*3)
						.withSize(96-8, 12)
						.withText(IIReference.GUI_LABEL_KEY+"map_display.marker.intelligence")
						.withChecked(this.filterIntelligence)
						.withOnToggle(newValue -> this.filterIntelligence = newValue),
				new DecoCheckbox(152+4, 12+12*4)
						.withSize(96-8, 12)
						.withText(IIReference.GUI_LABEL_KEY+"map_display.marker.wire_networks")
						.withChecked(this.filterWireNetworks)
						.withOnToggle(newValue -> this.filterWireNetworks = newValue),

				new DecoDropdown<IDecoMapColorMapper>(152+4, 4+10+12*5+10)
						.withSize(96-8, 12)
						.withEntries(DecoMapDefaultColorMapper.TERRAIN, DecoMapDefaultColorMapper.LIGHT, DecoMapDefaultColorMapper.TOPOGRAPHIC,
								DecoMapDefaultColorMapper.FACTIONS, DecoMapDefaultColorMapper.ALLIANCES)
						.withSelectedEntry(DecoMapDefaultColorMapper.TERRAIN)
						.withOnSelectedEntry((prev, next) -> mapDisplay.withColorMapper(next)),

				new DecoSwitch(152+4, 4+10+12*5+10+14)
						.withText("Show Grid")
						.withSize(96-8, 12)

		);

	}
}
