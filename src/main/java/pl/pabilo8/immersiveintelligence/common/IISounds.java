package pl.pabilo8.immersiveintelligence.common;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Pabilo8 on 15-06-2019.
 */
public class IISounds
{
	static Set<SoundEvent> registeredEvents = new HashSet();
	public static SoundEvent siren = registerSound("siren");
	public static SoundEvent printing_press = registerSound("printing_press");
	public static SoundEvent paper_eject = registerSound("paper_eject");

	public static SoundEvent machinegun_shot = registerSound("machinegun_shot");
	public static SoundEvent machinegun_shot_heavybarrel = registerSound("machinegun_shot_heavybarrel");
	public static SoundEvent machinegun_shot_watercooled = registerSound("machinegun_shot_watercooled");
	public static SoundEvent machinegun_reload = registerSound("machinegun_reload");
	public static SoundEvent machinegun_unload = registerSound("machinegun_unload");

	public static SoundEvent howitzer_shot = registerSound("howitzer_shot");
	public static SoundEvent howitzer_load = registerSound("howitzer_load");
	public static SoundEvent howitzer_unload = registerSound("howitzer_unload");
	public static SoundEvent howitzer_rotation_h = registerSound("howitzer_rotation_h");
	public static SoundEvent howitzer_rotation_v = registerSound("howitzer_rotation_v");
	public static SoundEvent howitzer_door_open = registerSound("howitzer_door_open");
	public static SoundEvent howitzer_door_close = registerSound("howitzer_door_close");

	public static SoundEvent inserter_forward = registerSound("inserter_forward");
	public static SoundEvent inserter_backward = registerSound("inserter_backward");

	public static SoundEvent penetration_metal = registerSound("penetration_metal");
	public static SoundEvent ricochet_metal = registerSound("ricochet_metal");

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
