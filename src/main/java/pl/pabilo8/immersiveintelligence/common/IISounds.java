package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.common.util.IESounds;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.HitSound;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.MultiSound;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.RangedSound;
import pl.pabilo8.modworks.annotations.sound.ModSound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
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
	public static ArrayList<RangedSound> rangedSounds = new ArrayList<>();
	public static ArrayList<MultiSound> multiSounds = new ArrayList<>();

	//--- Devices ---//
	public static SoundEvent siren = registerSound("siren");
	public static SoundEvent punchtapeReader = registerSound("punchtape_reader");
	public static SoundEvent paperEject = registerSound("paper_eject");

	public static SoundEvent radio_noise = registerSound("radio_noise");
	public static SoundEvent radio_beep = registerSound("radio_beep");

	public static SoundEvent inserterForward = registerSound("inserter_forward");
	public static SoundEvent inserterBackward = registerSound("inserter_backward");

	//--- Machines ---//
	public static SoundEvent rolling = registerSound("device_rolling");
	public static SoundEvent chain = registerSound("device_chain");
	public static SoundEvent lamp = registerSound("device_lamp");
	public static SoundEvent heating = registerSound("device_heating");

	public static SoundEvent weldingStart = registerSound("welding_start");
	public static SoundEvent weldingMid = registerSound("welding_mid");
	public static SoundEvent weldingEnd = registerSound("welding_end");

	public static SoundEvent fuelStationStart = registerSound("fuel_station_start");
	public static SoundEvent fuelStationMid = registerSound("fuel_station_mid");
	public static SoundEvent fuelStationEnd = registerSound("fuel_station_end");

	public static SoundEvent vulcanizerPullStart = registerSound("vulcanizer_pull_start");
	public static SoundEvent vulcanizerPullEnd = registerSound("vulcanizer_pull_end");

	public static SoundEvent chemicalPainterLights = registerSound("chemical_painter_lights");
	public static SoundEvent chemicalPainterLiftUp = registerSound("chemical_painter_lift_up");
	public static SoundEvent chemicalPainterLiftDown = registerSound("chemical_painter_lift_down");

	public static SoundEvent drawerOpen = registerSound("drawer_open");
	public static SoundEvent drawerClose = registerSound("drawer_close");
	public static SoundEvent metalLockerOpen = registerSound("metal_locker_open");
	public static SoundEvent metalLockerClose = registerSound("metal_locker_close");
	public static SoundEvent metalBreadboxOpen = registerSound("metal_breadbox_open");
	public static SoundEvent metalBreadboxClose = registerSound("metal_breadbox_close");

	public static SoundEvent artilleryShellPick = registerSound("howitzer_shell_pick");
	public static SoundEvent artilleryShellPut = registerSound("howitzer_shell_put");

	//--- Tools ---//
	public static SoundEvent mineDetector = registerSound("mine_detector");
	public static SoundEvent constructionHammer = registerSound("construction_hammer");

	//--- Guns ---//
	public static SoundEvent machinegunShotDry = registerSound("machinegun_shot_dry");
	public static RangedSound machinegunShot = new RangedSound(
			new Tuple<>(0.0, registerSound("machinegun_shot")),
			new Tuple<>(0.6, registerSound("submachinegun_shot_muffled")),
			new Tuple<>(0.75, registerSound("submachinegun_shot_distant"))
	);
	public static RangedSound machinegunShotHeavyBarrel = new RangedSound(
			new Tuple<>(0.0, registerSound("machinegun_shot_heavybarrel")),
			new Tuple<>(0.6, registerSound("submachinegun_shot_muffled")),
			new Tuple<>(0.75, registerSound("submachinegun_shot_distant"))
	);
	public static RangedSound machinegunShotWaterCooled = new RangedSound(
			new Tuple<>(0.0, registerSound("machinegun_shot_watercooled")),
			new Tuple<>(0.6, registerSound("submachinegun_shot_muffled")),
			new Tuple<>(0.75, registerSound("submachinegun_shot_distant"))
	);

	public static SoundEvent machinegunReload = registerSound("machinegun_reload");
	public static SoundEvent machinegunUnload = registerSound("machinegun_unload");


	public static SoundEvent submachinegunReload = registerSound("submachinegun_reload");
	public static SoundEvent submachinegunUnload = registerSound("submachinegun_unload");
	public static SoundEvent submachinegunShotDry = registerSound("submachinegun_shot_dry");
	public static RangedSound submachinegunShot = new RangedSound(
			new Tuple<>(0.0, registerSound("submachinegun_shot_close")),
			new Tuple<>(0.5, registerSound("submachinegun_shot_muffled")),
			new Tuple<>(0.75, registerSound("submachinegun_shot_distant"))
	);

	public static SoundEvent assaultRifleReload = registerSound("assault_rifle_reload");
	public static SoundEvent assaultRifleUnload = registerSound("assault_rifle_unload");
	public static SoundEvent assaultRifleShotDry = registerSound("assault_rifle_shot_dry");
	public static RangedSound assaultRifleShot = new RangedSound(
			new Tuple<>(0.0, registerSound("assault_rifle_shot")),
			new Tuple<>(0.5, registerSound("assault_rifle_shot_muffled")),
			new Tuple<>(0.75, registerSound("assault_rifle_shot_distant"))
	);
	public static SoundEvent assaultRifleLoadGrenade = registerSound("assault_rifle_load_grenade");
	public static SoundEvent assaultRifleModeChange = registerSound("assault_rifle_mode_change");
	public static RangedSound assaultRifleRailgunShot = new RangedSound(
			new Tuple<>(0.0, IESounds.railgunFire)
	);
	public static SoundEvent assaultRifleRailgunCharge = IESounds.chargeSlow;

	public static SoundEvent rifleLoadStart = registerSound("rifle_load_start");
	public static SoundEvent rifleLoad = registerSound("rifle_load");
	public static SoundEvent rifleLoadEnd = registerSound("rifle_load_end");
	public static SoundEvent rifleReloadMagazine = registerSound("rifle_reload_magazine");
	public static SoundEvent rifleUnloadMagazine = registerSound("rifle_unload_magazine");
	public static SoundEvent rifleShotDry = registerSound("rifle_shot_dry");
	public static RangedSound rifleShot = new RangedSound(
			new Tuple<>(0.0, registerSound("rifle_shot")),
			new Tuple<>(0.5, registerSound("rifle_shot_muffled")),
			new Tuple<>(0.75, registerSound("rifle_shot_distant"))
	);

	public static SoundEvent howitzerShot = registerSound("howitzer_shot");
	public static SoundEvent howitzerRotationH = registerSound("howitzer_rotation_h");
	public static SoundEvent howitzerRotationV = registerSound("howitzer_rotation_v");
	public static SoundEvent howitzerPlatformStart = registerSound("howitzer_platform_start");
	public static SoundEvent howitzerPlatformEnd = registerSound("howitzer_platform_end");
	public static SoundEvent howitzerPlatformRaise = registerSound("howitzer_platform_raise");
	public static SoundEvent howitzerPlatformLower = registerSound("howitzer_platform_lower");
	public static SoundEvent howitzerDoorOpen = registerSound("howitzer_door_open");
	public static SoundEvent howitzerDoorClose = registerSound("howitzer_door_close");

	public static SoundEvent emplacementRotationH = registerSound("emplacement_rotation_h");
	public static SoundEvent emplacementRotationV = registerSound("emplacement_rotation_v");
	public static SoundEvent emplacementPlatform = registerSound("emplacement_platform");
	public static SoundEvent emplacementDoorOpen = registerSound("emplacement_door_open");
	public static SoundEvent emplacementDoorClose = registerSound("emplacement_door_close");
	public static SoundEvent emplacementDoorPull = registerSound("emplacement_door_pull");
	public static SoundEvent emplacementDoorPush = registerSound("emplacement_door_push");

	public static SoundEvent autocannonFiring = registerSound("autocannon_firing");
	public static SoundEvent autocannonReload = registerSound("autocannon_reload");
	public static SoundEvent autocannonUnload = registerSound("autocannon_unload");

	public static SoundEvent mortarShot = registerSound("mortar_shot");
	public static SoundEvent mortarLoad = registerSound("mortar_load");

	public static SoundEvent grenadeThrow = registerSound("grenade_throw");

	//--- Explosions ---//
	public static SoundEvent explosionFlare = registerSound("explosion_flare");
	public static RangedSound explosionIncendiary = new RangedSound(
			new Tuple<>(0.0, registerSound("explosion_incendiary_high")),
			new Tuple<>(0.55, registerSound("explosion_incendiary_low"))
	);
	public static RangedSound explosion = new RangedSound(
			new Tuple<>(0.0, SoundEvents.ENTITY_GENERIC_EXPLODE),
			new Tuple<>(0.55, registerSound("explosion_low"))
	);
	public static RangedSound explosionNuke = new RangedSound(
			new Tuple<>(0.0, registerSound("explosion_nuke_high")),
			new Tuple<>(0.65, registerSound("explosion_nuke_low"))
	);

	public static SoundEvent bulletWind = registerSound("bullet_wind");

	//--- Impact and Ricochet ---//
	public static HitSound hitGrass = registerHitSound("impact_grass", "ricochet_grass");
	public static HitSound hitMetal = registerHitSound("impact_metal", "ricochet_metal");
	public static HitSound hitStone = registerHitSound("impact_stone", "ricochet_stone");
	public static HitSound hitSand = registerHitSound("impact_sand", "ricochet_sand");
	public static HitSound hitDirt = registerHitSound("impact_dirt", "ricochet_dirt");
	public static HitSound hitWood = registerHitSound("impact_wood", "ricochet_wood");
	public static SoundEvent impactFoliage = registerSound("impact_foliage");
	public static SoundEvent impactFlesh = registerSound("impact_flesh");

	//--- Vehicle ---//
	public static SoundEvent motorbikeStart = registerSound("motorbike_start");
	public static SoundEvent motorbikeStartNoFuel = registerSound("motorbike_start_no_fuel");
	public static SoundEvent motorbikeEngine = registerSound("motorbike_engine");
	public static SoundEvent motorbikeHorn = registerSound("motorbike_horn");

	//--- Hans ---//
	//public static SoundEvent hans_test_pl = registerSound("hans_test_pl");
	//public static SoundEvent hans_test_de = registerSound("hans_test_de");

	//--- End ---//

	public static void init()
	{
		for(SoundEvent event : registeredEvents)
			ForgeRegistries.SOUND_EVENTS.register(event);
	}

	//--- Internal Methods ---//

	private static SoundEvent registerSound(@Nonnull String name)
	{
		ResourceLocation location = new ResourceLocation(ImmersiveIntelligence.MODID, name);
		Optional<SoundEvent> first = registeredEvents.stream()
				.filter(soundEvent -> soundEvent.getSoundName().equals(location))
				.findFirst();
		if(first.isPresent())
			return first.get();

		SoundEvent event = new SoundEvent(location);
		registeredEvents.add(event.setRegistryName(location));
		return event;
	}

	private static HitSound registerHitSound(String impact, @Nullable String ricochet)
	{
		return new HitSound(registerSound(impact), ricochet==null?null: registerSound(ricochet));
	}
}
