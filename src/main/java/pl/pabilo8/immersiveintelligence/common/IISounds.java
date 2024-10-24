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
import java.util.HashMap;

/**
 * <p>
 * Pls don't sue me, CoH BKMod.<br>
 * Most of the sounds provided by ZapSplat.<br>
 * Siren sound from Factorio.<br>
 * Explosion-related sounds provided by Carver (source unsure) or from CoH BKMod.<br>
 * Some sounds recorded by Pabilo8<br>
 * </p>
 * <p>
 * Naming convention:
 * <ul>
 *     <li>variable names are camelCase, but sound resource paths are snake case</li>
 *     <li>{@link ModSound} annotation allows character replacements (see {@link ModSound#sounds()}), always use them when possible</li>
 *     <li>if the sounds are both to be used as single sounds and MultiSounds, initialize the sounds separately and reference them with the variable</li>
 *     <li>
 *         if possible, start/mid/end {@link MultiSound}s with a common name part, like <pre>slidingDoor</pre>
 *         for <pre>slidingDoorStart, slidingDoorOpen, slidingDoorEnd</pre>
 *         should be named with the name of the middle sound +ing, f.e. <pre>slidingDoorOpening</pre>
 *     </li>
 * </ul>
 * </p>
 *
 * @author Pabilo8
 * @author Avalon
 * @ii-approved 0.3.1
 * @since 15-06-2019
 * @since 30.03.2023
 */
public class IISounds
{
	static HashMap<ResourceLocation, SoundEvent> registeredEvents = new HashMap<>();
	public static ArrayList<RangedSound> rangedSounds = new ArrayList<>();
	public static ArrayList<MultiSound> multiSounds = new ArrayList<>();

	//--- Devices ---//
	@ModSound(sounds = {"device/*"}, subtitle = "siren")
	public static SoundEvent siren = registerSound("siren");
	@ModSound(sounds = {"device/*"}, subtitle = "punchtape_reader")
	public static SoundEvent punchtapeReader = registerSound("punchtape_reader");
	@ModSound(sounds = {"paper_falling"}, subtitle = "paper_eject")
	public static SoundEvent paperEject = registerSound("paper_eject");

	@ModSound(sounds = {"device/radio/*{0..1}"}, subtitle = "radio_noise")
	public static SoundEvent radio_noise = registerSound("radio_noise");
	@ModSound(sounds = {"device/*"}, subtitle = "radio_beep")
	public static SoundEvent radio_beep = registerSound("radio_beep");

	@ModSound(sounds = {"device/electric_motor/start"}, subtitle = "inserter")
	public static SoundEvent inserterStart = registerSound("inserter_start");
	@ModSound(sounds = {"device/electric_motor/forward"}, subtitle = "inserter")
	public static SoundEvent inserterYaw = registerSound("inserter_forward");
	@ModSound(sounds = {"device/electric_motor/backward"}, subtitle = "inserter")
	public static SoundEvent inserterPitch = registerSound("inserter_backward");
	@ModSound(sounds = {"device/electric_motor/end"}, subtitle = "inserter")
	public static SoundEvent inserterEnd = registerSound("inserter_end");

	public static MultiSound inserterYawM = new MultiSound(inserterStart,
			inserterYaw, inserterEnd);
	public static MultiSound inserterPitchM = new MultiSound(inserterEnd,
			inserterPitch, inserterStart);

	//--- Machine Motion ---//

	//electric motor
	@ModSound(sounds = {"device/electric_motor/start"})
	public static SoundEvent electricMotorStart = registerSound("electric_motor_start");
	@ModSound(sounds = {"device/electric_motor/end"})
	public static SoundEvent electricMotorEnd = registerSound("electric_motor_end");
	@ModSound(sounds = {"device/electric_motor/forward"}, subtitle = "*")
	public static SoundEvent electricMotorForward = registerSound("electric_motor_forward");
	@ModSound(sounds = {"device/electric_motor/backward"}, subtitle = "*")
	public static SoundEvent electricMotorBackward = registerSound("electric_motor_backward");

	public static MultiSound electricMotorForwardM = new MultiSound(electricMotorStart,
			electricMotorForward, electricMotorBackward);
	public static MultiSound electricMotorBackwardM = new MultiSound(electricMotorStart,
			electricMotorEnd, electricMotorBackward);

	//heavy electric motor
	@ModSound(sounds = {"device/electric_motor_heavy/start"})
	public static SoundEvent electricMotorHeavyStart = registerSound("electric_motor_heavy_start");
	@ModSound(sounds = {"device/electric_motor_heavy/end"})
	public static SoundEvent electricMotorHeavyEnd = registerSound("electric_motor_heavy_end");
	@ModSound(sounds = {"device/electric_motor_heavy/forward"}, subtitle = "*")
	public static SoundEvent electricMotorHeavyForward = registerSound("electric_motor_heavy_forward");
	@ModSound(sounds = {"device/electric_motor_heavy/backward"}, subtitle = "*")
	public static SoundEvent electricMotorHeavyBackward = registerSound("electric_motor_heavy_backward");

	public static MultiSound electricMotorHeavyForwardM = new MultiSound(electricMotorHeavyStart,
			electricMotorHeavyForward, electricMotorHeavyEnd);
	public static MultiSound electricMotorHeavyBackwardM = new MultiSound(electricMotorHeavyStart,
			electricMotorHeavyBackward, electricMotorHeavyEnd);

	//turntable
	@ModSound(sounds = {"device/turntable/start"})
	public static SoundEvent turntableStart = registerSound("turntable_start");
	@ModSound(sounds = {"device/turntable/end"})
	public static SoundEvent turntableEnd = registerSound("turntable_end");
	@ModSound(sounds = {"device/turntable/forward"}, subtitle = "*")
	public static SoundEvent turntableForward = registerSound("turntable_forward");
	@ModSound(sounds = {"device/turntable/backward"}, subtitle = "*")
	public static SoundEvent turntableBackward = registerSound("turntable_backward");
	@ModSound(sounds = {"device/turntable/forward_alt"}, subtitle = "turntable_forward")
	public static SoundEvent turntableAltForward = registerSound("turntable_forward_alt");
	@ModSound(sounds = {"device/turntable/backward_alt"}, subtitle = "turntable_backward")
	public static SoundEvent turntableAltBackward = registerSound("turntable_backward_alt");

	public static MultiSound turntableForwardM = new MultiSound(turntableStart, turntableForward, turntableEnd);
	public static MultiSound turntableBackwardM = new MultiSound(turntableStart, turntableBackward, turntableEnd);
	public static MultiSound turntableAltForwardM = new MultiSound(turntableStart, turntableAltForward, turntableEnd);
	public static MultiSound turntableAltBackwardM = new MultiSound(turntableStart, turntableAltBackward, turntableEnd);

	//heavy turntable
	@ModSound(sounds = {"device/turntable_heavy/start"})
	public static SoundEvent turntableHeavyStart = registerSound("turntable_heavy_start");
	@ModSound(sounds = {"device/turntable_heavy/end"})
	public static SoundEvent turntableHeavyEnd = registerSound("turntable_heavy_end");
	@ModSound(sounds = {"device/turntable_heavy/forward"}, subtitle = "*")
	public static SoundEvent turntableHeavyForward = registerSound("turntable_heavy_forward");
	@ModSound(sounds = {"device/turntable_heavy/backward"}, subtitle = "*")
	public static SoundEvent turntableHeavyBackward = registerSound("turntable_heavy_backward");

	public static MultiSound turntableHeavyForwardM = new MultiSound(turntableHeavyStart, turntableHeavyForward, turntableHeavyEnd);
	public static MultiSound turntableHeavyBackwardM = new MultiSound(turntableHeavyStart, turntableHeavyBackward, turntableHeavyEnd);

	//light metal door
	@ModSound(sounds = {"device/turntable/start"})
	public static SoundEvent slidingDoorStart = registerSound("sliding_door_start");
	@ModSound(sounds = {"device/turntable/end"})
	public static SoundEvent slidingDoorEnd = registerSound("sliding_door_end");
	@ModSound(sounds = {"device/turntable/forward"}, subtitle = "*")
	public static SoundEvent slidingDoorOpen = registerSound("sliding_door_open");
	@ModSound(sounds = {"device/turntable/backward"}, subtitle = "*")
	public static SoundEvent slidingDoorClose = registerSound("sliding_door_close");

	//--- Sawmill ---//
	@ModSound(sounds = {"device/sawmill/start"}, subtitle = "sawmill")
	public static SoundEvent sawmillStart = registerSound("sawmill_start");
	@ModSound(sounds = {"device/sawmill/mid"}, subtitle = "sawmill")
	public static SoundEvent sawmillMid = registerSound("sawmill_mid");
	@ModSound(sounds = {"device/sawmill/end"}, subtitle = "sawmill")
	public static SoundEvent sawmillEnd = registerSound("sawmill_end");
	public static MultiSound sawmillRunning = new MultiSound(sawmillStart, sawmillMid, sawmillEnd);

	@ModSound(sounds = {"device/sawmill/inserter_start"}, subtitle = "sawmill")
	public static SoundEvent sawmillInserterStart = registerSound("sawmill_inserter_start");
	@ModSound(sounds = {"device/sawmill/inserter_end"}, subtitle = "sawmill")
	public static SoundEvent sawmillInserterEnd = registerSound("sawmill_inserter_end");

	@ModSound(sounds = {"device/sawmill/wood_tumble{0..4}"}, subtitle = "sawmill")
	public static SoundEvent sawmillWoodTumble = registerSound("sawmill_wood_tumble");

	//--- Printing Press---//
	@ModSound(sounds = {"device/printingpress/start"}, subtitle = "press_start")
	public static SoundEvent pressStart = registerSound("Printing Press");
	@ModSound(sounds = {"device/printingpress/mid"}, subtitle = "press_mid")
	public static SoundEvent pressMid = registerSound("Printing Press");
	@ModSound(sounds = {"device/printingpress/end"}, subtitle = "press_end")
	public static SoundEvent pressEnd = registerSound("Printing Press");
	public static MultiSound printingPressRunning = new MultiSound(pressStart, pressMid, pressEnd);

	//--- Packer ---//
	@ModSound(sounds = {"device/packer/start"}, subtitle = "packer_start")
	public static SoundEvent packerStart = registerSound("Packer");
	@ModSound(sounds = {"device/packer/mid"}, subtitle = "packer_mid")
	public static SoundEvent packerMid = registerSound("Packer");
	@ModSound(sounds = {"device/packer/end"}, subtitle = "packer_end")
	public static SoundEvent packerEnd = registerSound("Packer");

	//--- Vulcanizer ---//
	@ModSound(sounds = {"devices/vulcanizer/start"}, subtitle = "vulcanizer_pull_start")
	public static SoundEvent vulcanizerPullStart = registerSound("vulcanizer");
	@ModSound(sounds = {"devices/vulcanizer/mid"}, subtitle = "vulcanizer_pull_mid")
	public static SoundEvent vulcanizerPullMid = registerSound("vulcanizer");
	@ModSound(sounds = {"devices/vulcanizer/end"}, subtitle = "vulcanizer_pull_end")
	public static SoundEvent vulcanizerPullEnd = registerSound("vulcanizer");
	public static MultiSound vulcanizerRunning = new MultiSound(vulcanizerPullStart, vulcanizerPullMid, vulcanizerPullEnd);

	//--- Packer ---//
	public static MultiSound packerRunning = new MultiSound(packerStart, packerMid, packerEnd);

	public static MultiSound slidingDoorOpenM = new MultiSound(slidingDoorStart, slidingDoorOpen, slidingDoorEnd);
	public static MultiSound slidingDoorCloseM = new MultiSound(slidingDoorStart, slidingDoorClose, slidingDoorClose);

	//--- Gate Sounds---//
	@ModSound(sounds = {"device/hatch/gate_wooden/open"}, subtitle = "gate_open")
	public static SoundEvent gateWoodenOpen = registerSound("gate_wooden_open");
	@ModSound(sounds = {"device/hatch/gate_wooden/close"}, subtitle = "gate_close")
	public static SoundEvent gateWoodenClose = registerSound("gate_wooden_close");
	@ModSound(sounds = {"device/hatch/gate_metal/open"}, subtitle = "gate_open")
	public static SoundEvent gateMetalOpen = registerSound("gate_metal_open");
	@ModSound(sounds = {"device/hatch/gate_metal/close"}, subtitle = "gate_close")
	public static SoundEvent gateMetalClose = registerSound("gate_metal_close");

	//--- Machines ---//
	@ModSound(sounds = {"device/*"}, subtitle = "device_*")
	public static SoundEvent rolling = registerSound("rolling");
	@ModSound(sounds = {"device/*"}, subtitle = "device_*")
	public static SoundEvent chain = registerSound("chain");
	@ModSound(sounds = {"device/*"}, subtitle = "device_*")
	public static SoundEvent lamp = registerSound("lamp");
	@ModSound(sounds = {"device/*"}, subtitle = "device_*")
	public static SoundEvent heating = registerSound("heating");

	public static MultiSound chainM = new MultiSound(electricMotorStart, chain, electricMotorEnd);

	@ModSound(sounds = {"device/welding/start"}, subtitle = "welding")
	public static SoundEvent weldingStart = registerSound("welding_start");
	@ModSound(sounds = {"device/welding/mid"}, subtitle = "welding")
	public static SoundEvent weldingMid = registerSound("welding_mid");
	@ModSound(sounds = {"device/welding/end"}, subtitle = "welding")
	public static SoundEvent weldingEnd = registerSound("welding_end");

	@ModSound(sounds = {"device/fuel_station/start"}, subtitle = "fuel_station")
	public static SoundEvent fuelStationStart = registerSound("fuel_station_start");
	@ModSound(sounds = {"device/fuel_station/mid"}, subtitle = "fuel_station")
	public static SoundEvent fuelStationMid = registerSound("fuel_station_mid");
	@ModSound(sounds = {"device/fuel_station/end"}, subtitle = "fuel_station")
	public static SoundEvent fuelStationEnd = registerSound("fuel_station_end");

	public static SoundEvent chemicalPainterLights = registerSound("chemical_painter_lights");
	public static SoundEvent chemicalPainterLiftUp = registerSound("chemical_painter_lift_up");
	public static SoundEvent chemicalPainterLiftDown = registerSound("chemical_painter_lift_down");

	//--- Hatches ---//
	@ModSound(sounds = {"device/hatch/drawer/open{0..1}"}, subtitle = "drawer_open")
	public static SoundEvent drawerOpen = registerSound("drawer_open");
	@ModSound(sounds = {"device/hatch/drawer/close{0..3}"}, subtitle = "drawer_close")
	public static SoundEvent drawerClose = registerSound("drawer_close");

	@ModSound(sounds = {"device/hatch/locker/open{0..2}"}, subtitle = "metal_locker_open")
	public static SoundEvent metalLockerOpen = registerSound("metal_locker_open");
	@ModSound(sounds = {"device/hatch/locker/close{0..2}"}, subtitle = "metal_locker_close")
	public static SoundEvent metalLockerClose = registerSound("metal_locker_close");
	@ModSound(sounds = {"device/hatch/breadbox/open{0..1}"}, subtitle = "metal_hatch_open")
	public static SoundEvent metalBreadboxOpen = registerSound("metal_breadbox_open");
	@ModSound(sounds = {"device/hatch/breadbox/close{0..1}"}, subtitle = "metal_hatch_close")
	public static SoundEvent metalBreadboxClose = registerSound("metal_breadbox_close");
	@ModSound(sounds = {"device/hatch/slide/open{0..0}"}, subtitle = "metal_hatch_open")
	public static SoundEvent metalSlideOpen = registerSound("metal_slide_open");
	@ModSound(sounds = {"device/hatch/slide/close{0..0}"}, subtitle = "metal_hatch_close")
	public static SoundEvent metalSlideClose = registerSound("metal_slide_close");

	@ModSound(sounds = {"device/hatch/vise/open0"}, subtitle = "*")
	public static SoundEvent viseOpen = registerSound("vise_open");
	@ModSound(sounds = {"device/hatch/vise/close0"}, subtitle = "*")
	public static SoundEvent viseClose = registerSound("vise_close");

	//--- Motor Belts ---//
	@ModSound(sounds = {"device/motor_belt/*"}, subtitle = "motor_belt_break")
	public static SoundEvent motorBeltBreak = registerSound("motor_belt_break");
	@ModSound(sounds = {"device/motor_belt/*"}, subtitle = "motor_belt")
	public static SoundEvent motorBeltLoop = registerSound("motor_belt_loop");
	public static MultiSound motorBeltRunning = new MultiSound(motorBeltLoop);

	//--- Tracks ---//
	@ModSound(sounds = {"device/motor_belt/*"}, subtitle = "track_break")
	public static SoundEvent trackBreak = registerSound("track_break");
	@ModSound(sounds = {"device/motor_belt/*"}, subtitle = "track")
	public static SoundEvent trackLoop = registerSound("track_loop");
	public static MultiSound trackRunning = new MultiSound(trackLoop);

	//--- Tools ---//
	@ModSound(sounds = {"device/*"}, subtitle = "mine_detector")
	public static SoundEvent mineDetector = registerSound("mine_detector");
	@ModSound(sounds = {"construction/hammer/hammer{0..5}"}, subtitle = "construction")
	public static SoundEvent constructionHammer = registerSound("construction_hammer");

	//--- Guns ---//

	//Ammo Pickup
	@ModSound(sounds = {"weapons/rifle/load{0..2}"}, subtitle = "*")
	public static SoundEvent casingPickup = registerSound("casing_pickup");

	//Machinegun
	@ModSound(sounds = {"weapons/dryfire{0..1}"}, subtitle = "dryfire")
	public static SoundEvent machinegunShotDry = registerSound("machinegun_shot_dry");

	@ModSound(name = "machinegun_shot_muffled", sounds = {"weapons/machinegun/mg_muffled{0..1}"}, subtitle = "machinegun_shot_distant")
	public static SoundEvent machinegunShotMuffled = registerSound("machinegun_shot_muffled");
	@ModSound(name = "machinegun_shot_distant", sounds = {"weapons/machinegun/mg_distant"}, subtitle = "distant_gunshot")
	public static SoundEvent machinegunShotDistant = registerSound("machinegun_shot_distant");


	@ModSound(name = "machinegun_shot", sounds = {"weapons/machinegun/mg"}, subtitle = "machinegun_shot")
	public static RangedSound machinegunShot = new RangedSound(
			new Tuple<>(0.0, registerSound("machinegun_shot")),
			new Tuple<>(0.7, machinegunShotMuffled),
			new Tuple<>(0.9, machinegunShotDistant)
	);

	@ModSound(name = "machinegun_shot_heavybarrel", sounds = {"weapons/machinegun/mg_heavybarrel"}, subtitle = "machinegun_shot")
	public static RangedSound machinegunShotHeavyBarrel = new RangedSound(
			new Tuple<>(0.0, registerSound("machinegun_shot_heavybarrel")),
			new Tuple<>(0.7, machinegunShotMuffled),
			new Tuple<>(0.9, machinegunShotDistant)
	);

	@ModSound(name = "machinegun_shot_watercooled", sounds = {"weapons/machinegun/mg_watercooled"}, subtitle = "machinegun_shot")
	public static RangedSound machinegunShotWaterCooled = new RangedSound(
			new Tuple<>(0.0, registerSound("machinegun_shot_watercooled")),
			new Tuple<>(0.7, machinegunShotMuffled),
			new Tuple<>(0.9, machinegunShotDistant)
	);

	@ModSound(sounds = {"weapons/machinegun/reload"}, subtitle = "machinegun_reload")
	public static SoundEvent machinegunReload = registerSound("machinegun_reload");

	@ModSound(sounds = {"weapons/machinegun/unload"}, subtitle = "machinegun_reload")
	public static SoundEvent machinegunUnload = registerSound("machinegun_unload");

	//Submachinegun
	@ModSound(sounds = {"weapons/submachinegun/reload"}, subtitle = "*")
	public static SoundEvent submachinegunReload = registerSound("submachinegun_reload");

	@ModSound(sounds = {"weapons/submachinegun/unload"}, subtitle = "*")
	public static SoundEvent submachinegunUnload = registerSound("submachinegun_unload");

	@ModSound(sounds = {"weapons/dryfire{0..1}"}, subtitle = "dryfire")
	public static SoundEvent submachinegunShotDry = registerSound("submachinegun_shot_dry");

	@ModSound(name = "submachinegun_shot_close", sounds = {"weapons/submachinegun/smg{0..2}"}, subtitle = "submachinegun_shot")
	@ModSound(name = "submachinegun_shot_muffled", sounds = {"weapons/submachinegun/smg_muffled{0..2}"}, subtitle = "submachinegun_shot_distant")
	@ModSound(name = "submachinegun_shot_distant", sounds = {"weapons/submachinegun/smg_distant{0..2}"}, subtitle = "distant_gunshot")
	public static RangedSound submachinegunShot = new RangedSound(
			new Tuple<>(0.0, registerSound("submachinegun_shot_close")),
			new Tuple<>(0.5, registerSound("submachinegun_shot_muffled")),
			new Tuple<>(0.75, registerSound("submachinegun_shot_distant"))
	);

	//Assault Rifle
	@ModSound(sounds = {"weapons/assault_rifle/reload"}, subtitle = "*")
	public static SoundEvent assaultRifleReload = registerSound("assault_rifle_reload");

	@ModSound(sounds = {"weapons/assault_rifle/unload"}, subtitle = "*")
	public static SoundEvent assaultRifleUnload = registerSound("assault_rifle_unload");

	@ModSound(sounds = {"weapons/dryfire{0..1}"}, subtitle = "dryfire")
	public static SoundEvent assaultRifleShotDry = registerSound("assault_rifle_shot_dry");

	@ModSound(name = "assault_rifle_shot", sounds = {"weapons/assault_rifle/stg{0..1}"}, subtitle = "assault_rifle_shot")
	@ModSound(name = "assault_rifle_shot_muffled", sounds = {"weapons/assault_rifle/stg_muffled{0..1}"}, subtitle = "assault_rifle_shot_muffled")
	@ModSound(name = "assault_rifle_shot_distant", sounds = {"weapons/assault_rifle/stg_distant{0..1}"}, subtitle = "distant_gunshot")
	public static RangedSound assaultRifleShot = new RangedSound(
			new Tuple<>(0.0, registerSound("assault_rifle_shot")),
			new Tuple<>(0.5, registerSound("assault_rifle_shot_muffled")),
			new Tuple<>(0.75, registerSound("assault_rifle_shot_distant"))
	);
	@ModSound(sounds = {"weapons/assault_rifle/load_grenade"}, subtitle = "*")
	public static SoundEvent assaultRifleLoadGrenade = registerSound("assault_rifle_load_grenade");

	@ModSound(sounds = {"weapons/assault_rifle/mode_change"}, subtitle = "*")
	public static SoundEvent assaultRifleModeChange = registerSound("assault_rifle_mode_change");
	public static RangedSound assaultRifleRailgunShot = new RangedSound(
			new Tuple<>(0.0, IESounds.railgunFire)
	);
	public static SoundEvent assaultRifleRailgunCharge = IESounds.chargeSlow;

	//Rifle
	@ModSound(sounds = {"weapons/rifle/load_start"}, subtitle = "rifle_load")
	public static SoundEvent rifleLoadStart = registerSound("rifle_load_start");

	@ModSound(sounds = {"weapons/rifle/load{0..2}"}, subtitle = "*")
	public static SoundEvent rifleLoad = registerSound("rifle_load");

	@ModSound(sounds = {"weapons/rifle/load_end"}, subtitle = "rifle_load")
	public static SoundEvent rifleLoadEnd = registerSound("rifle_load_end");

	@ModSound(sounds = {"weapons/rifle/reload_magazine"}, subtitle = "*")
	public static SoundEvent rifleReloadMagazine = registerSound("rifle_reload_magazine");

	@ModSound(sounds = {"weapons/rifle/unload_magazine"}, subtitle = "*")
	public static SoundEvent rifleUnloadMagazine = registerSound("rifle_unload_magazine");

	@ModSound(sounds = {"weapons/dryfire{0..1}"}, subtitle = "dryfire")
	public static SoundEvent rifleShotDry = registerSound("rifle_shot_dry");

	@ModSound(name = "rifle_shot", sounds = {"weapons/rifle/rifle{0..0}"}, subtitle = "rifle_shot")
	@ModSound(name = "rifle_shot_muffled", sounds = {"weapons/rifle/rifle_muffled{0..0}"}, subtitle = "rifle_shot_muffled")
	@ModSound(name = "rifle_shot_distant", sounds = {"weapons/rifle/rifle_distant{0..0}"}, subtitle = "distant_gunshot")
	public static RangedSound rifleShot = new RangedSound(
			new Tuple<>(0.0, registerSound("rifle_shot")),
			new Tuple<>(0.5, registerSound("rifle_shot_muffled")),
			new Tuple<>(0.75, registerSound("rifle_shot_distant"))
	);


	@ModSound(name = "rifle_bolt_shot", sounds = {"weapons/rifle/rifle_bolt"}, subtitle = "rifle_shot")
	public static RangedSound rifleBoltShot = new RangedSound(
			new Tuple<>(0.0, registerSound("rifle_bolt_shot")),
			new Tuple<>(0.5, registerSound("rifle_shot_muffled")),
			new Tuple<>(0.75, registerSound("rifle_shot_distant"))

	);

	//Artillery Howitzer
	@ModSound(name = "howitzer_shot", sounds = {"weapons/howitzer/howitzer{0..2}"}, subtitle = "howitzer_shot")
	@ModSound(name = "howitzer_shot_muffled", sounds = {"weapons/howitzer/howitzer_muffled{0..2}"}, subtitle = "howitzer_shot_distant")
	@ModSound(name = "howitzer_shot_distant", sounds = {"weapons/howitzer/howitzer_distant{0..2}"}, subtitle = "distant_artillery")
	public static RangedSound howitzerShot = new RangedSound(
			new Tuple<>(0.0, registerSound("howitzer_shot")),
			new Tuple<>(0.45, registerSound("howitzer_shot_muffled")),
			new Tuple<>(0.75, registerSound("howitzer_shot_distant"))
	);

	//TODO: 23.04.2023 replace with universal sounds
	public static SoundEvent howitzerPlatformStart = registerSound("howitzer_platform_start");
	public static SoundEvent howitzerPlatformEnd = registerSound("howitzer_platform_end");
	public static SoundEvent howitzerPlatformRaise = registerSound("howitzer_platform_raise");
	public static SoundEvent howitzerPlatformLower = registerSound("howitzer_platform_lower");

	@ModSound(sounds = {"weapons/howitzer/shell/pick"}, subtitle = "*")
	public static SoundEvent artilleryShellPick = registerSound("artillery_shell_pick");
	@ModSound(sounds = {"weapons/howitzer/shell/place{0..1}"}, subtitle = "*")
	public static SoundEvent artilleryShellPlace = registerSound("artillery_shell_place");

	//Emplacement
	//TODO: 23.04.2023 replace with universal sounds
	@ModSound(sounds = {"device/emplacement/r_h"}, subtitle = "emplacement_rotation")
	public static SoundEvent emplacementRotationH = registerSound("emplacement_rotation_h");
	@ModSound(sounds = {"device/emplacement/r_v"}, subtitle = "emplacement_rotation")
	public static SoundEvent emplacementRotationV = registerSound("emplacement_rotation_v");
	@ModSound(sounds = {"device/emplacement/platform"}, subtitle = "emplacement_platform")
	public static SoundEvent emplacementPlatform = registerSound("emplacement_platform");
	@ModSound(sounds = {"device/emplacement/door_open"}, subtitle = "emplacement_door")
	public static SoundEvent emplacementDoorOpen = registerSound("emplacement_door_open");
	@ModSound(sounds = {"device/emplacement/door_close"}, subtitle = "emplacement_door")
	public static SoundEvent emplacementDoorClose = registerSound("emplacement_door_close");


	public static SoundEvent emplacementDoorPull = registerSound("emplacement_door_pull");
	public static SoundEvent emplacementDoorPush = registerSound("emplacement_door_push");

	//Emplacement Weapons

	@ModSound(name = "autocannon_firing", sounds = {"weapons/autocannon/flak{0..4}"}, subtitle = "autocannon_shot")
	@ModSound(name = "autocannon_firing_muffled", sounds = {"weapons/autocannon/flak_muffled{0..2}"}, subtitle = "autocannon_shot_distant")
	@ModSound(name = "autocannon_firing_distant", sounds = {"weapons/autocannon/flak_distant{0..2}"}, subtitle = "distant_gunshot")
	public static RangedSound autocannonShot = new RangedSound(
			new Tuple<>(0.0, registerSound("autocannon_firing")),
			new Tuple<>(0.65, registerSound("autocannon_firing_muffled")),
			new Tuple<>(0.95, registerSound("autocannon_firing_distant"))
	);
	public static SoundEvent autocannonReload = registerSound("autocannon_reload");
	public static SoundEvent autocannonUnload = registerSound("autocannon_unload");

	//Mortar
	@ModSound(sounds = {"weapons/mortar/fire{0..1}"}, subtitle = "*")
	public static SoundEvent mortarShot = registerSound("mortar_shot");
	@ModSound(sounds = {"weapons/mortar/load"}, subtitle = "*")
	public static SoundEvent mortarLoad = registerSound("mortar_load");

	//Grenade throwing
	@ModSound(sounds = {"weapons/*"}, subtitle = "*")
	public static SoundEvent grenadeThrow = registerSound("grenade_throw");

	//--- Explosions ---//
	@ModSound(sounds = {"explosion/flare/flare{0..4}"}, subtitle = "explosion_incendiary")
	public static SoundEvent explosionFlare = registerSound("explosion_flare");

	@ModSound(name = "explosion_incendiary_high", sounds = {"explosion/nuke/high{0..2}"}, subtitle = "explosion_incendiary")
	@ModSound(name = "explosion_incendiary_low", sounds = {"explosion/nuke/low{0..4}"}, subtitle = "explosion_incendiary")
	public static RangedSound explosionIncendiary = new RangedSound(
			new Tuple<>(0.0, registerSound("explosion_incendiary_high")),
			new Tuple<>(0.55, registerSound("explosion_incendiary_low"))
	);
	@ModSound(name = "explosion_low", sounds = {"explosion/normal/low{0..1}"}, subtitle = "subtitles.entity.generic.explode")
	public static RangedSound explosion = new RangedSound(
			new Tuple<>(0.0, SoundEvents.ENTITY_GENERIC_EXPLODE),
			new Tuple<>(0.55, registerSound("explosion_low"))
	);
	@ModSound(name = "explosion_nuke_high", sounds = {"explosion/nuke/high{0..2}"}, subtitle = "explosion_nuke")
	@ModSound(name = "explosion_nuke_low", sounds = {"explosion/nuke/low{0..4}"}, subtitle = "explosion_nuke")
	public static RangedSound explosionNuke = new RangedSound(
			new Tuple<>(0.0, registerSound("explosion_nuke_high")),
			new Tuple<>(0.65, registerSound("explosion_nuke_low"))
	);

	//--- Bullets ---//
	@ModSound(sounds = {"weapons/flyby/*{0..3}"}, subtitle = "*")
	public static RangedSound artilleryImpact = new RangedSound(
			new Tuple<>(0.0, registerSound("artillery_impact"))
	);
	@ModSound(sounds = {"weapons/flyby/*"}, subtitle = "*")
	public static SoundEvent bulletFlyby = registerSound("bullet_flyby");

	//--- Impact and Ricochet ---//
	@ModSound(name = "impact_grass", sounds = {"hit/grass/impact{0..2}"}, subtitle = "bullet_impact")
	@ModSound(name = "ricochet_grass", sounds = {"hit/grass/ricochet{0..2}"}, subtitle = "bullet_ricochet")
	public static HitSound hitGrass = registerHitSound("impact_grass", "ricochet_grass");

	@ModSound(name = "impact_metal", sounds = {"hit/metal/impact{0..4}"}, subtitle = "impact_metal")
	@ModSound(name = "ricochet_metal", sounds = {"hit/metal/ricochet{0..4}"}, subtitle = "ricochet_metal")
	public static HitSound hitMetal = registerHitSound("impact_metal", "ricochet_metal");

	@ModSound(name = "impact_stone", sounds = {"hit/stone/impact{0..4}"}, subtitle = "bullet_impact")
	@ModSound(name = "ricochet_stone", sounds = {"hit/stone/ricochet{0..4}"}, subtitle = "bullet_ricochet")
	public static HitSound hitStone = registerHitSound("impact_stone", "ricochet_stone");

	@ModSound(name = "impact_sand", sounds = {"hit/sand/impact{0..2}"}, subtitle = "bullet_impact")
	@ModSound(name = "ricochet_sand", sounds = {"hit/sand/ricochet{0..2}"}, subtitle = "bullet_ricochet")
	public static HitSound hitSand = registerHitSound("impact_sand", "ricochet_sand");

	@ModSound(name = "impact_dirt", sounds = {"hit/dirt/impact{0..4}"}, subtitle = "bullet_impact")
	@ModSound(name = "ricochet_dirt", sounds = {"hit/dirt/ricochet{0..4}"}, subtitle = "bullet_ricochet")
	public static HitSound hitDirt = registerHitSound("impact_dirt", "ricochet_dirt");

	@ModSound(name = "impact_wood", sounds = {"hit/wood/impact{0..4}"}, subtitle = "bullet_impact")
	@ModSound(name = "ricochet_wood", subtitle = "bullet_ricochet")
	public static HitSound hitWood = registerHitSound("impact_wood", "ricochet_wood");

	@ModSound(sounds = {"hit/foliage/impact{0..2}"}, subtitle = "bullet_impact")
	public static SoundEvent impactFoliage = registerSound("impact_foliage");
	@ModSound(sounds = {"hit/flesh/impact{0..4}"}, subtitle = "bullet_impact")
	public static SoundEvent impactFlesh = registerSound("impact_flesh");

	//--- Vehicle ---//
	@ModSound(sounds = {"motorbike/start"}, subtitle = "*")
	public static SoundEvent motorbikeStart = registerSound("motorbike_start");
	@ModSound(sounds = {"motorbike/start_no_fuel"}, subtitle = "motorbike_start")
	public static SoundEvent motorbikeStartNoFuel = registerSound("motorbike_start_no_fuel");
	@ModSound(sounds = {"motorbike/engine"}, subtitle = "*")
	public static SoundEvent motorbikeEngine = registerSound("motorbike_engine");
	@ModSound(sounds = {"motorbike/horn"}, subtitle = "*")
	public static SoundEvent motorbikeHorn = registerSound("motorbike_horn");

	//--- Hans ---//
	//public static SoundEvent hans_test_pl = registerSound("hans_test_pl");
	//public static SoundEvent hans_test_de = registerSound("hans_test_de");

	//--- End ---//

	public static void init()
	{
		for(SoundEvent event : registeredEvents.values())
			ForgeRegistries.SOUND_EVENTS.register(event);
	}

	//--- Internal Methods ---//

	private static SoundEvent registerSound(@Nonnull String name)
	{
		ResourceLocation location = new ResourceLocation(ImmersiveIntelligence.MODID, name);
		SoundEvent event = registeredEvents.get(location);
		if(event!=null)
			return event;

		event = new SoundEvent(location);
		registeredEvents.put(location, event.setRegistryName(location));
		return event;
	}

	private static HitSound registerHitSound(String impact, @Nullable String ricochet)
	{
		return new HitSound(registerSound(impact), ricochet==null?null: registerSound(ricochet));
	}
}
