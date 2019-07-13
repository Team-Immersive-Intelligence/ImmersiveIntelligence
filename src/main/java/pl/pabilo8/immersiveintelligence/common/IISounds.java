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
