package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import pl.pabilo8.immersiveintelligence.common.IILogger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author GabrielV (gabriel@iiteam.net)
 * @since 21/04/2024 - 7:26 PM
 * Class used for reflection aka hacky stuff to get something that forge does not give us peacefully
 */
public class Reflector
{
	private Reflector(){}

	private static ConcurrentHashMap<Object, ArrayList<IEventListener>> listeners = null;

	public static Field getField(Class cls, String name, boolean priv)
	{
		try{
			return priv ? cls.getDeclaredField(name) : cls.getField(name);
		} catch(Exception e)
		{
			IILogger.error("[Reflector/Error] Uh Oh! An reflection error! ("+e.getMessage()+")");
			return null;
		}
	}

	public static void getForgeEventListeners()
	{
		Field listenersFld = getField(MinecraftForge.EVENT_BUS.getClass(), "listeners", true);
		if (listenersFld == null) {
			IILogger.error("[Reflector/Error] Could not get event bus listeners");
			return;
		}
		listenersFld.setAccessible(true);
		if (ConcurrentHashMap.class.isAssignableFrom(listenersFld.getType()))
		{
			try {
				listeners = (ConcurrentHashMap<Object, ArrayList<IEventListener>>)listenersFld.get(MinecraftForge.EVENT_BUS);
			} catch(Exception e) {
				IILogger.error("[Reflector/Error] AAAAAA! Why? "+e.getMessage());
			}
		}
	}

	/**
	 * Hijack the original event handler to replace it with our own
	 * @param origEvent
	 * @param overrideEvent
	 */
	public static void overrideEventHandler(Class origEvent, Object overrideEvent)
	{
		if (listeners == null) {
			IILogger.error("[Reflector/Error] Listeners list is null");
			return;
		}

		for (Map.Entry<Object, ArrayList<IEventListener>> o : listeners.entrySet())
		{
			Object c1 = o.getKey();
			if (!c1.getClass().getName().equals(origEvent.getName())) continue;
			MinecraftForge.EVENT_BUS.unregister(c1);
			MinecraftForge.EVENT_BUS.register(overrideEvent);
		}
	}
}