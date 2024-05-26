package pl.pabilo8.immersiveintelligence.api.ammo;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
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
 * @updated 16.02.2024
 * @ii-approved 0.3.1
 * @since 30-08-2019
 */
public class AmmoRegistry
{
	/**
	 * Special case, used when the core is missing
	 */
	public static final AmmoCoreMissingNo MISSING_CORE = new AmmoCoreMissingNo();

	/**
	 * Ammo registry
	 */
	private static final LinkedHashMap<String, IAmmoType<?, ?>> REGISTERED_AMMO_TYPES = new LinkedHashMap<>();
	private static final LinkedHashMap<String, IAmmoTypeItem<?, ?>> REGISTERED_AMMO_TYPE_ITEMS = new LinkedHashMap<>();
	/**
	 * Ammo parts registry
	 */
	private static final LinkedHashMap<String, AmmoComponent> REGISTERED_COMPONENTS = new LinkedHashMap<>();
	private static final LinkedHashMap<String, AmmoCore> REGISTERED_CORES = new LinkedHashMap<>();
	@SideOnly(Side.CLIENT)
	private static final HashMap<IAmmoType<?, ?>, IAmmoModel<?, ?>> REGISTERED_MODELS = new HashMap<>();

	//--- Registration ---//

	/**
	 * Registers a new ammo component
	 *
	 * @param component The ammo component to register
	 * @return true if the registration was successful
	 */
	public static boolean registerComponent(AmmoComponent component)
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
	 * Removes a component from the registry
	 *
	 * @param name of the component
	 */
	public static void unregisterComponent(String name)
	{
		REGISTERED_COMPONENTS.remove(name);
	}

	/**
	 * Registers a new ammo core
	 *
	 * @param core The ammo core to register
	 * @return true if the registration was successful
	 */
	public static boolean registerCore(AmmoCore core)
	{
		String name = core.getName();
		if(!REGISTERED_CORES.containsKey(name))
		{
			REGISTERED_CORES.put(name, core);
			return true;
		}
		return false;
	}

	/**
	 * Removes a core from the registry
	 *
	 * @param name of the the core material
	 */
	public static void unregisterCore(String name)
	{
		REGISTERED_CORES.remove(name);
	}

	/**
	 * Registers a new ammo type
	 *
	 * @param ammo The ammo type to register
	 * @return true if the registration was successful
	 */
	@SuppressWarnings("all")
	public static <T extends IAmmoType<T, E>, E extends EntityAmmoBase<? super E>> boolean registerAmmoType(IAmmoType<T, E> ammo)
	{
		String name = ammo.getName();
		if(!REGISTERED_AMMO_TYPE_ITEMS.containsKey(name))
		{
			//Register ammo
			REGISTERED_AMMO_TYPES.put(name, ammo);
			if(ammo instanceof IAmmoTypeItem)
				REGISTERED_AMMO_TYPE_ITEMS.put(name, ((IAmmoTypeItem<T, E>)ammo));
			return true;
		}
		return false;
	}

	/**
	 * Registers 3D models of all ammo types, called only on client side
	 */
	@SideOnly(Side.CLIENT)
	public static void registerAmmoModels()
	{
		REGISTERED_AMMO_TYPES.values().forEach(AmmoRegistry::registerSingleModel);
	}

	private static <T extends IAmmoType<T, E>, E extends EntityAmmoBase<? super E>> void registerSingleModel(IAmmoType<?, ?> ammo)
	{
		IAmmoType<T, E> generic = (IAmmoType<T, E>)ammo;
		IAmmoModel<T, E> model = generic.get3DModel().apply((T)generic);
		if(model==null)
		{
			IILogger.error("Failed to load model for ammo type "+ammo.getName());
			return;
		}
		REGISTERED_MODELS.put(ammo, model);
	}

	//--- Getters ---//

	/**
	 * @param name of the ammo type
	 * @return ammo component with the given name
	 */
	@Nullable
	public static AmmoComponent getComponent(String name)
	{
		return REGISTERED_COMPONENTS.get(name);
	}

	/**
	 * @param name of the ammo type
	 * @return ammo type item with the given name
	 */
	@Nullable
	public static IAmmoTypeItem<?, ?> getAmmoItem(String name)
	{
		return REGISTERED_AMMO_TYPE_ITEMS.get(name);
	}

	/**
	 * @param ammo ammo item
	 * @return ammo type of the given ammo item
	 */
	@Nullable
	public static IAmmoTypeItem<?, ?> getAmmoItem(ItemStack ammo)
	{
		if(ammo.getItem() instanceof IAmmoTypeItem<?, ?>)
			return ((IAmmoTypeItem<?, ?>)ammo.getItem());
		return null;
	}

	/**
	 * @param name core name
	 * @return ammo type with the given name or {@link #MISSING_CORE} if not found
	 */
	@Nonnull
	public static AmmoCore getCore(String name)
	{
		return REGISTERED_CORES.getOrDefault(name, MISSING_CORE);
	}

	/**
	 * @param ammo ammo type
	 * @return 3D model of this ammo type
	 */
	@Nullable
	@SideOnly(Side.CLIENT)
	public static <T extends IAmmoType<T, E>, E extends EntityAmmoBase<? super E>> IAmmoModel<T, E> getModel(IAmmoType<T, E> ammo)
	{
		return (IAmmoModel<T, E>)REGISTERED_MODELS.get(ammo);
	}

	//--- Registry Getters ---//

	/**
	 * @return all registered ammo items
	 */
	public static Collection<IAmmoTypeItem<?, ?>> getAllAmmoItems()
	{
		return REGISTERED_AMMO_TYPE_ITEMS.values();
	}

	/**
	 * @return all registered ammo types
	 */
	public static Collection<IAmmoType<?, ?>> getAllAmmoTypes()
	{
		return REGISTERED_AMMO_TYPES.values();
	}

	/**
	 * @return all registered ammo cores
	 */
	public static Collection<AmmoCore> getAllCores()
	{
		return REGISTERED_CORES.values();
	}

	/**
	 * @return all registered ammo components
	 */
	public static Collection<AmmoComponent> getAllComponents()
	{
		return REGISTERED_COMPONENTS.values();
	}

	/**
	 * @return all registered ammo models
	 */
	@SideOnly(Side.CLIENT)
	public static Collection<IAmmoModel<?, ?>> getAllModels()
	{
		return REGISTERED_MODELS.values();
	}

	/**
	 * Replaces the model of the given ammo type
	 *
	 * @param ammo  name of the model to replace
	 * @param model new model
	 */
	public static void renewModel(IAmmoType<?, ?> ammo, IAmmoModel model)
	{
		REGISTERED_MODELS.remove(ammo);
		REGISTERED_MODELS.put(ammo, model);
	}
}
