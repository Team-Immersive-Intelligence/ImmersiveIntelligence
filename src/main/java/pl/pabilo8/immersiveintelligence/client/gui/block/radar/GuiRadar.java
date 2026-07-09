package pl.pabilo8.immersiveintelligence.client.gui.block.radar;

import blusunrize.immersiveengineering.common.entities.EntityRailgunShot;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoMapDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.DecoMapDefaultColorMapper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.scanners.EntityScanner;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.scanners.RadarDirectionScanner;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Radar;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRadar;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoArtilleryProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoMissile;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityDrone;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerRadar;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.04.2023
 */
@DecoTemplate(name = "radar", category = DecoGuiCategory.TERRITORY_CONTROL_TILE)
public class GuiRadar extends DecoTileGui<TileEntityRadar, ContainerRadar>
{
	@DecoResource
	public static ResourceLocation ICON_RADAR = ResLoc.of(IIReference.RES_II, "gui/tab_icons/radar");

	private EntityScanner scanner;
	@SyncNBT
	public boolean filterPlayers = true, filterMobs = true, filterAnimals = true, filterItems = true, filterVehicles = true, filterAircraft = true, filterMissiles = true, filterArtillery = true, filterBullets = true;


	public GuiRadar(EntityPlayer player, TileEntityRadar tile)
	{
		super(player, tile, IIGUI.RADAR);
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
				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 152-4, 0, 96+8, 152)
				.build();

		//Tabs
		addLinkTab(IIGUI.RADAR, GuiRadar.ICON_RADAR, "radar_module");
		addLinkTab(IIGUI.RADAR_CONFIG, DecoTextures.ICON_CONFIG, "configuration_module");
		addLinkTab(IIGUI.RADAR_TARGETS, DecoTextures.ICON_TARGETS, "targets_module");

		BlockPos pos = tile.getPos();
		addComponents(
				new DecoMapDisplay(4, 4)
						.withSize(152-8, 152-8)
						.withRegion(pos.getX(), pos.getZ(), 128)
						.withColorMapper(DecoMapDefaultColorMapper.GRAYSCALE)
						.withPanning(true)
						.withZoomScrolling(1, 4)
						.withScanner((this.scanner = new EntityScanner("entities"))
								.withUpdateInterval(2, () -> tile.active)
								.withMarkerStyle(DecoTextures.MAP_MARKER_ENTITY, 3, IIReference.COLOR_IMMERSIVE_ORANGE)
								.withNoiseShader()
								.withScanRange(Radar.detectionRadius)
						)
						.withScanner(new RadarDirectionScanner("radar", () -> tile.dishRotation, () -> tile.active))
		);
		registerFilters();

		addLabel("Shown Markers", 152+4, 4)
				.withSize(96-8, 12)
				.withAlign(DecoAlignment.TOP);
		addComponents(
				new DecoCheckbox(152+4, 12)
						.withSize(96-8, 12)
						.withText(IIReference.GUI_LABEL_KEY+"map_display.marker.items")
						.withChecked(filterItems)
						.withOnToggle(newValue -> {
							this.filterItems = newValue;
							registerFilters();
						}),
				new DecoCheckbox(152+4, 12+12)
						.withSize(96-8, 12)
						.withText(IIReference.GUI_LABEL_KEY+"map_display.marker.players")
						.withChecked(filterPlayers)
						.withOnToggle(newValue -> {
							this.filterPlayers = newValue;
							registerFilters();
						}),
				new DecoCheckbox(152+4, 12+12*2)
						.withSize(96-8, 12)
						.withText(IIReference.GUI_LABEL_KEY+"map_display.marker.mobs")
						.withChecked(filterMobs)
						.withOnToggle(newValue -> {
							this.filterMobs = newValue;
							registerFilters();
						}),
				new DecoCheckbox(152+4, 12+12*3)
						.withSize(96-8, 12)
						.withText(IIReference.GUI_LABEL_KEY+"map_display.marker.animals")
						.withChecked(filterAnimals)
						.withOnToggle(newValue -> {
							this.filterAnimals = newValue;
							registerFilters();
						}),
				new DecoCheckbox(152+4, 12+12*4)
						.withSize(96-8, 12)
						.withText(IIReference.GUI_LABEL_KEY+"map_display.marker.aircraft")
						.withChecked(filterAircraft)
						.withOnToggle(newValue -> {
							this.filterAircraft = newValue;
							registerFilters();
						}),
				new DecoCheckbox(152+4, 12+12*5)
						.withSize(96-8, 12)
						.withText(IIReference.GUI_LABEL_KEY+"map_display.marker.vehicles")
						.withChecked(filterVehicles)
						.withOnToggle(newValue -> {
							this.filterVehicles = newValue;
							registerFilters();
						}),
				new DecoCheckbox(152+4, 12+12*6)
						.withSize(96-8, 12)
						.withText(IIReference.GUI_LABEL_KEY+"map_display.marker.missiles")
						.withChecked(filterMissiles)
						.withOnToggle(newValue -> {
							this.filterMissiles = newValue;
							registerFilters();
						}),
				new DecoCheckbox(152+4, 12+12*7)
						.withSize(96-8, 12)
						.withText(IIReference.GUI_LABEL_KEY+"map_display.marker.artillery")
						.withChecked(filterArtillery)
						.withOnToggle(newValue -> {
							this.filterArtillery = newValue;
							registerFilters();
						}),
				new DecoCheckbox(152+4, 12+12*8)
						.withSize(96-8, 12)
						.withText(IIReference.GUI_LABEL_KEY+"map_display.marker.bullets")
						.withChecked(this.filterBullets)
						.withOnToggle(newValue -> {
							this.filterBullets = newValue;
							registerFilters();
						})
		);

		addComponents(
				new DecoBar(152+4, 108+12+16)
						.withSize(96-8, 12)
						.withHorizontalMode(true)
						.withTemplate(DecoTemplates.BAR_STRUCTURAL_INTEGRITY.apply(tile)),
				new DecoBar(152+4, 108+12)
						.withSize(96-8, 12)
						.withHorizontalMode(true)
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage))
		);
	}

	private void registerFilters()
	{
		scanner.clearFilters();
		if(filterItems)
			scanner.withFilter(entity -> entity instanceof EntityItem, DecoTextures.MAP_MARKER_ENTITY, IIReference.COLOR_IMMERSIVE_ORANGE, 0.125f);
		if(filterPlayers)
			scanner.withFilter(entity -> entity instanceof EntityPlayer, DecoTextures.MAP_MARKER_ENTITY, IIReference.COLOR_IMMERSIVE_ORANGE, 1f);
		if(filterMobs)
			scanner.withFilter(entity -> entity instanceof EntityMob, DecoTextures.MAP_MARKER_ENTITY, IIReference.COLOR_IMMERSIVE_ORANGE, 1f);
		if(filterAnimals)
			scanner.withFilter(entity -> entity instanceof EntityAnimal, DecoTextures.MAP_MARKER_ENTITY, IIReference.COLOR_IMMERSIVE_ORANGE, 1f);
		if(filterAircraft)
			scanner.withFilter(entity -> entity instanceof EntityDrone, DecoTextures.MAP_MARKER_DRONE, IIReference.COLOR_IMMERSIVE_ORANGE, 2f);
		if(filterVehicles)
			scanner.withFilter(entity -> entity instanceof EntityVehicleBase, DecoTextures.MAP_MARKER_VEHICLE, IIReference.COLOR_IMMERSIVE_ORANGE, 3f);
		if(filterMissiles)
			scanner.withFilter(entity -> entity instanceof EntityAmmoMissile, DecoTextures.MAP_MARKER_MISSILE, IIReference.COLOR_IMMERSIVE_ORANGE, 1f);
		if(filterArtillery)
			scanner.withFilter(entity -> entity instanceof EntityAmmoArtilleryProjectile, DecoTextures.MAP_MARKER_BULLET, IIReference.COLOR_IMMERSIVE_ORANGE, 1f);
		if(filterBullets)
		{
			scanner.withFilter(entity -> entity instanceof EntityAmmoProjectile, DecoTextures.MAP_MARKER_BULLET, IIReference.COLOR_IMMERSIVE_ORANGE, 0.45f);
			scanner.withFilter(entity -> entity instanceof EntityRailgunShot, DecoTextures.MAP_MARKER_BULLET, IIReference.COLOR_IMMERSIVE_ORANGE, 0.45f);
		}
	}
}
