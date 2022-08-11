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
	public static SoundEvent printing_press = registerSound("printing_press");
	public static SoundEvent paper_eject = registerSound("paper_eject");

	public static SoundEvent machinegun_shot = registerSound("machinegun_shot");
	public static SoundEvent machinegun_shot_heavybarrel = registerSound("machinegun_shot_heavybarrel");
	public static SoundEvent machinegun_shot_watercooled = registerSound("machinegun_shot_watercooled");
	public static SoundEvent machinegun_reload = registerSound("machinegun_reload");
	public static SoundEvent machinegun_unload = registerSound("machinegun_unload");

	public static SoundEvent submachinegun_shot = registerSound("submachinegun_shot");
	public static SoundEvent submachinegun_reload = registerSound("submachinegun_reload");
	public static SoundEvent submachinegun_unload = registerSound("submachinegun_unload");

	public static SoundEvent howitzer_chain = registerSound("howitzer_chain");
	public static SoundEvent howitzer_shot = registerSound("howitzer_shot");
	public static SoundEvent howitzer_shell_pick = registerSound("howitzer_shell_pick");
	public static SoundEvent howitzer_shell_put = registerSound("howitzer_shell_put");
	public static SoundEvent howitzer_rotation_h = registerSound("howitzer_rotation_h");
	public static SoundEvent howitzer_rotation_v = registerSound("howitzer_rotation_v");
	public static SoundEvent howitzer_platform_start = registerSound("howitzer_platform_start");
	public static SoundEvent howitzer_platform_end = registerSound("howitzer_platform_end");
	public static SoundEvent howitzer_platform_raise = registerSound("howitzer_platform_raise");
	public static SoundEvent howitzer_platform_lower = registerSound("howitzer_platform_lower");
	public static SoundEvent howitzer_door_open = registerSound("howitzer_door_open");
	public static SoundEvent howitzer_door_close = registerSound("howitzer_door_close");

	public static SoundEvent inserter_forward = registerSound("inserter_forward");
	public static SoundEvent inserter_backward = registerSound("inserter_backward");

	public static SoundEvent radio_noise = registerSound("radio_noise");
	public static SoundEvent radio_beep = registerSound("radio_beep");

	public static SoundEvent motorbike_start = registerSound("motorbike_start");
	public static SoundEvent motorbike_start_no_fuel = registerSound("motorbike_start_no_fuel");
	public static SoundEvent motorbike_engine = registerSound("motorbike_engine");
	public static SoundEvent motorbike_horn = registerSound("motorbike_horn");

	public static SoundEvent welding_start = registerSound("welding_start");
	public static SoundEvent welding_mid = registerSound("welding_mid");
	public static SoundEvent welding_end = registerSound("welding_end");

	public static SoundEvent fuel_station_start = registerSound("fuel_station_start");
	public static SoundEvent fuel_station_mid = registerSound("fuel_station_mid");
	public static SoundEvent fuel_station_end = registerSound("fuel_station_end");

	public static SoundEvent vulcanizer_heating = registerSound("vulcanizer_heating");
	public static SoundEvent vulcanizer_pull_start = registerSound("vulcanizer_pull_start");
	public static SoundEvent vulcanizer_pull_end = registerSound("vulcanizer_pull_end");

	public static SoundEvent autocannon_firing = registerSound("autocannon_firing");
	public static SoundEvent autocannon_reload = registerSound("autocannon_reload");
	public static SoundEvent autocannon_unload = registerSound("autocannon_unload");

	public static SoundEvent explosion_flare = registerSound("explosion_flare");
	public static SoundEvent explosion_incendiary_high = registerSound("explosion_incendiary_high");
	public static SoundEvent explosion_incendiary_low = registerSound("explosion_incendiary_low");
	public static SoundEvent explosion_nuke_high = registerSound("explosion_nuke_high");
	public static SoundEvent explosion_nuke_low = registerSound("explosion_nuke_low");

	public static SoundEvent mine_detector = registerSound("mine_detector");

	public static SoundEvent mortar_shot = registerSound("mortar_shot");
	public static SoundEvent mortar_load = registerSound("mortar_load");

	public static SoundEvent bullet_wind = registerSound("bullet_wind");
	public static SoundEvent grenade_throw = registerSound("grenade_throw");
	
	public static SoundEvent impact_grass = registerSound("impact_grass");
	public static SoundEvent ricochet_grass = registerSound("ricochet_grass");
	public static SoundEvent impact_metal = registerSound("impact_metal");
	public static SoundEvent ricochet_metal = registerSound("ricochet_metal");
	public static SoundEvent impact_stone = registerSound("impact_stone");
	public static SoundEvent ricochet_stone = registerSound("ricochet_stone");
	public static SoundEvent impact_sand = registerSound("impact_sand");
	public static SoundEvent ricochet_sand = registerSound("ricochet_sand");
	public static SoundEvent impact_dirt = registerSound("impact_dirt");
	public static SoundEvent ricochet_dirt = registerSound("ricochet_dirt");
	public static SoundEvent impact_foliage = registerSound("impact_foliage");
	//public static SoundEvent ricochet_foliage = registerSound("ricochet_foliage");
	public static SoundEvent impact_wood = registerSound("impact_wood");
	//public static SoundEvent ricochet_wood = registerSound("ricochet_wood");
	public static SoundEvent impact_flesh = registerSound("impact_flesh");
	//public static SoundEvent ricochet_flesh = registerSound("ricochet_flesh");

	public static SoundEvent construction_hammer = registerSound("construction_hammer");

	public static SoundEvent emplacement_rotation_h = registerSound("emplacement_rotation_h");
	public static SoundEvent emplacement_rotation_v = registerSound("emplacement_rotation_v");
	public static SoundEvent emplacement_platform = registerSound("emplacement_platform");
	public static SoundEvent emplacement_door_open = registerSound("emplacement_door_open");
	public static SoundEvent emplacement_door_close = registerSound("emplacement_door_close");
	public static SoundEvent emplacement_door_pull = registerSound("emplacement_door_pull");
	public static SoundEvent emplacement_door_push = registerSound("emplacement_door_push");

	public static SoundEvent chemical_painter_lights = registerSound("chemical_painter_lights");
	public static SoundEvent chemical_painter_lift_up = registerSound("chemical_painter_lift_up");
	public static SoundEvent chemical_painter_lift_down = registerSound("chemical_painter_lift_down");
	//public static SoundEvent hans_test_pl = registerSound("hans_test_pl");
	//public static SoundEvent hans_test_de = registerSound("hans_test_de");

	public static SoundEvent drawer_open = registerSound("drawer_open");
	public static SoundEvent drawer_close = registerSound("drawer_close");
	public static SoundEvent metal_locker_open = registerSound("metal_locker_open");
	public static SoundEvent metal_locker_close = registerSound("metal_locker_close");
	public static SoundEvent metal_breadbox_open = registerSound("metal_breadbox_open");
	public static SoundEvent metal_breadbox_close = registerSound("metal_breadbox_close");

	public static SoundEvent punchtape_reader = registerSound("punchtape_reader");

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
