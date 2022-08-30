package pl.pabilo8.immersiveintelligence.common;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

import java.util.HashSet;
import java.util.Set;

/**
 * Pls don't sue me, CoH BKMod.
 * Most of the sounds provided by ZapSplat.
 * Printing press uses a sound from a youtube video.
 * Siren sound from Factorio.
 * Explosion-related sounds provided by Carver (source unsure) or from CoH BKMod.
 *
 * @author Pabilo8
 * @since 15-06-2019
 */
public class IISounds
{
	static Set<SoundEvent> registeredEvents = new HashSet<>();

	public static SoundEvent siren = registerSound("siren");
	public static SoundEvent printingPress = registerSound("printing_press");
	public static SoundEvent paperEject = registerSound("paper_eject");

	public static SoundEvent machinegunShot = registerSound("machinegun_shot");
	public static SoundEvent machinegunShotHeavybarrel = registerSound("machinegun_shot_heavybarrel");
	public static SoundEvent machinegunShotWatercooled = registerSound("machinegun_shot_watercooled");
	public static SoundEvent machinegunReload = registerSound("machinegun_reload");
	public static SoundEvent machinegunUnload = registerSound("machinegun_unload");

	public static SoundEvent submachinegunShot = registerSound("submachinegun_shot");
	public static SoundEvent submachinegunReload = registerSound("submachinegun_reload");
	public static SoundEvent submachinegunUnload = registerSound("submachinegun_unload");

	public static SoundEvent howitzerChain = registerSound("howitzer_chain");
	public static SoundEvent howitzerShot = registerSound("howitzer_shot");
	public static SoundEvent howitzerShellPick = registerSound("howitzer_shell_pick");
	public static SoundEvent howitzerShellPut = registerSound("howitzer_shell_put");
	public static SoundEvent howitzerRotationH = registerSound("howitzer_rotation_h");
	public static SoundEvent howitzerRotationV = registerSound("howitzer_rotation_v");
	public static SoundEvent howitzerPlatformStart = registerSound("howitzer_platform_start");
	public static SoundEvent howitzerPlatformEnd = registerSound("howitzer_platform_end");
	public static SoundEvent howitzerPlatformRaise = registerSound("howitzer_platform_raise");
	public static SoundEvent howitzerPlatformLower = registerSound("howitzer_platform_lower");
	public static SoundEvent howitzerDoorOpen = registerSound("howitzer_door_open");
	public static SoundEvent howitzerDoorClose = registerSound("howitzer_door_close");

	public static SoundEvent inserterForward = registerSound("inserter_forward");
	public static SoundEvent inserterBackward = registerSound("inserter_backward");

	public static SoundEvent radio_noise = registerSound("radio_noise");
	public static SoundEvent radio_beep = registerSound("radio_beep");

	public static SoundEvent motorbikeStart = registerSound("motorbike_start");
	public static SoundEvent motorbikeStartNoFuel = registerSound("motorbike_start_no_fuel");
	public static SoundEvent motorbikeEngine = registerSound("motorbike_engine");
	public static SoundEvent motorbikeHorn = registerSound("motorbike_horn");

	public static SoundEvent weldingStart = registerSound("welding_start");
	public static SoundEvent weldingMid = registerSound("welding_mid");
	public static SoundEvent weldingEnd = registerSound("welding_end");

	public static SoundEvent fuelStationStart = registerSound("fuel_station_start");
	public static SoundEvent fuelStationMid = registerSound("fuel_station_mid");
	public static SoundEvent fuelStationEnd = registerSound("fuel_station_end");

	public static SoundEvent vulcanizerHeating = registerSound("vulcanizer_heating");
	public static SoundEvent vulcanizerPullStart = registerSound("vulcanizer_pull_start");
	public static SoundEvent vulcanizerPullEnd = registerSound("vulcanizer_pull_end");

	public static SoundEvent autocannonFiring = registerSound("autocannon_firing");
	public static SoundEvent autocannonReload = registerSound("autocannon_reload");
	public static SoundEvent autocannonUnload = registerSound("autocannon_unload");

	public static SoundEvent explosionFlare = registerSound("explosion_flare");
	public static SoundEvent explosionIncendiaryHigh = registerSound("explosion_incendiary_high");
	public static SoundEvent explosionIncendiaryLow = registerSound("explosion_incendiary_low");
	public static SoundEvent explosionNukeHigh = registerSound("explosion_nuke_high");
	public static SoundEvent explosionNukeLow = registerSound("explosion_nuke_low");

	public static SoundEvent mineDetector = registerSound("mine_detector");

	public static SoundEvent mortarShot = registerSound("mortar_shot");
	public static SoundEvent mortarLoad = registerSound("mortar_load");

	public static SoundEvent bulletWind = registerSound("bullet_wind");
	public static SoundEvent grenadeThrow = registerSound("grenade_throw");
	
	public static SoundEvent impactGrass = registerSound("impact_grass");
	public static SoundEvent ricochetGrass = registerSound("ricochet_grass");
	public static SoundEvent impactMetal = registerSound("impact_metal");
	public static SoundEvent ricochetMetal = registerSound("ricochet_metal");
	public static SoundEvent impactStone = registerSound("impact_stone");
	public static SoundEvent ricochetStone = registerSound("ricochet_stone");
	public static SoundEvent impactSand = registerSound("impact_sand");
	public static SoundEvent ricochetSand = registerSound("ricochet_sand");
	public static SoundEvent impactDirt = registerSound("impact_dirt");
	public static SoundEvent ricochetDirt = registerSound("ricochet_dirt");
	public static SoundEvent impactFoliage = registerSound("impact_foliage");
	public static SoundEvent impactWood = registerSound("impact_wood");
	public static SoundEvent impactFlesh = registerSound("impact_flesh");

	public static SoundEvent constructionHammer = registerSound("construction_hammer");

	public static SoundEvent emplacementRotationH = registerSound("emplacement_rotation_h");
	public static SoundEvent emplacementRotationV = registerSound("emplacement_rotation_v");
	public static SoundEvent emplacementPlatform = registerSound("emplacement_platform");
	public static SoundEvent emplacementDoorOpen = registerSound("emplacement_door_open");
	public static SoundEvent emplacementDoorClose = registerSound("emplacement_door_close");
	public static SoundEvent emplacementDoorPull = registerSound("emplacement_door_pull");
	public static SoundEvent emplacementDoorPush = registerSound("emplacement_door_push");

	public static SoundEvent chemicalPainterLights = registerSound("chemical_painter_lights");
	public static SoundEvent chemicalPainterLiftUp = registerSound("chemical_painter_lift_up");
	public static SoundEvent chemicalPainterLiftDown = registerSound("chemical_painter_lift_down");
	//public static SoundEvent hans_test_pl = registerSound("hans_test_pl");
	//public static SoundEvent hans_test_de = registerSound("hans_test_de");

	public static SoundEvent drawerOpen = registerSound("drawer_open");
	public static SoundEvent drawerClose = registerSound("drawer_close");
	public static SoundEvent metalLockerOpen = registerSound("metal_locker_open");
	public static SoundEvent metalLockerClose = registerSound("metal_locker_close");
	public static SoundEvent metalBreadboxOpen = registerSound("metal_breadbox_open");
	public static SoundEvent metalBreadboxClose = registerSound("metal_breadbox_close");

	public static SoundEvent punchtapeReader = registerSound("punchtape_reader");

	private static SoundEvent registerSound(String name)
	{
		ResourceLocation location = new ResourceLocation(ImmersiveIntelligence.MODID, name);
		SoundEvent event = new SoundEvent(location);
		registeredEvents.add(event.setRegistryName(location));
		return event;
	}

	public static void init()
	{
		for(SoundEvent event : registeredEvents)
			ForgeRegistries.SOUND_EVENTS.register(event);
	}
}
