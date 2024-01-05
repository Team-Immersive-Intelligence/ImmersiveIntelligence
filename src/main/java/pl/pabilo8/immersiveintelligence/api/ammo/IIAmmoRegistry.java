package pl.pabilo8.immersiveintelligence.api.ammo;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmo;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoItem;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.ammo.cores.AmmoCoreMissingNo;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class IIAmmoRegistry
{
	/**
	 * Special case, used when the core is missing
	 */
	public static final AmmoCoreMissingNo MISSING_CORE = new AmmoCoreMissingNo();

	/**
	 * Ammo registry
	 */
	private static final LinkedHashMap<String, IAmmo<?>> REGISTERED_AMMO_TYPES = new LinkedHashMap<>();
	private static final LinkedHashMap<String, IAmmoItem<?>> REGISTERED_AMMO_TYPE_ITEMS = new LinkedHashMap<>();
	/**
	 * Ammo parts registry
	 */
	private static final LinkedHashMap<String, IAmmoComponent> REGISTERED_COMPONENTS = new LinkedHashMap<>();
	private static final LinkedHashMap<String, IAmmoCore> REGISTERED_BULLET_CORES = new LinkedHashMap<>();
	@SideOnly(Side.CLIENT)
	private static final HashMap<String, IBulletModel> REGISTERED_MODELS = new HashMap<>();

	/**
	 * Registers a new ammo component
	 *
	 * @param component The ammo component to register
	 * @return true if the registration was successful
	 */
	public static boolean registerComponent(IAmmoComponent component)
	{
		String name = component.getName();
		if(!REGISTERED_COMPONENTS.containsKey(name))
		{
			REGISTERED_COMPONENTS.put(name, component);
			return true;
		}
		return false;
	}

	/**
	 * Registers a new ammo core
	 *
	 * @param core The ammo core to register
	 * @return true if the registration was successful
	 */
	public static boolean registerBulletCore(IAmmoCore core)
	{
		String name = core.getName();
		if(!REGISTERED_BULLET_CORES.containsKey(name))
		{
			REGISTERED_BULLET_CORES.put(name, core);
			return true;
		}
		return false;
	}

	/**
	 * Registers a new ammo type
	 *
	 * @param ammo The ammo type to register
	 * @return true if the registration was successful
	 */
	@SuppressWarnings("all")
	public static <T extends EntityAmmoBase> boolean registerAmmoType(IAmmo<T> ammo)
	{
		String name = ammo.getName();
		if(!REGISTERED_AMMO_TYPE_ITEMS.containsKey(name))
		{
			//Register ammo
			REGISTERED_AMMO_TYPES.put(name, ammo);
			if(ammo instanceof IAmmoItem)
				REGISTERED_AMMO_TYPE_ITEMS.put(name, ((IAmmoItem<T>)ammo));

			//Load model
			if(FMLCommonHandler.instance().getSide().isClient())
			{
				try
				{
					IBulletModel iBulletModel = ammo.getModel().newInstance();
					iBulletModel.subscribeToList(ammo.getName());
					REGISTERED_MODELS.put(ammo.getName(), iBulletModel);
				} catch(InstantiationException|IllegalAccessException e)
				{
					IILogger.error("Failed to load model for ammo type "+ammo.getName());
					IILogger.error("Reason: "+e);
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * @param name of the ammo type
	 * @return ammo component with the given name
	 */
	@Nullable
	public static IAmmoComponent getComponent(String name)
	{
		return REGISTERED_COMPONENTS.get(name);
	}

	/**
	 * @param name of the ammo type
	 * @return ammo type item with the given name
	 */
	@Nullable
	public static IAmmoItem<?> getBulletItem(String name)
	{
		return REGISTERED_AMMO_TYPE_ITEMS.get(name);
	}

	/**
	 * @param name core name
	 * @return ammo type with the given name or {@link #MISSING_CORE} if not found
	 */
	@Nonnull
	public static IAmmoCore getCore(String name)
	{
		return REGISTERED_BULLET_CORES.getOrDefault(name, MISSING_CORE);
	}

	/**
	 * @param bullet ammo type
	 * @return 3D model of this ammo type
	 */
	@Nullable
	@SideOnly(Side.CLIENT)
	public static IBulletModel getModel(IAmmoItem<?> bullet)
	{
		return REGISTERED_MODELS.get(bullet.getName());
	}

	//--- Getters ---//

	/**
	 * @return all registered ammo items
	 */
	public static Collection<IAmmoItem<?>> getAllBulletItems()
	{
		return REGISTERED_AMMO_TYPE_ITEMS.values();
	}

	/**
	 * @return all registered ammo types
	 */
	public static Collection<IAmmo<?>> getAllAmmoTypes()
	{
		return REGISTERED_AMMO_TYPES.values();
	}

	/**
	 * @return all registered ammo cores
	 */
	public static Collection<IAmmoCore> getAllCores()
	{
		return REGISTERED_BULLET_CORES.values();
	}

	/**
	 * @return all registered ammo components
	 */
	public static Collection<IAmmoComponent> getAllComponents()
	{
		return REGISTERED_COMPONENTS.values();
	}

	/**
	 * @return all registered ammo models
	 */
	@SideOnly(Side.CLIENT)
	public static Collection<IBulletModel> getAllModels()
	{
		return REGISTERED_MODELS.values();
	}
}
