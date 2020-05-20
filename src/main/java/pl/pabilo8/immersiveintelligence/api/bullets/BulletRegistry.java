package pl.pabilo8.immersiveintelligence.api.bullets;

import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * Created by Pabilo8 on 30-08-2019.
 */
public class BulletRegistry
{
	public static BulletRegistry INSTANCE = new BulletRegistry();
	public LinkedHashMap<String, IBulletComponent> registeredComponents = new LinkedHashMap<>();
	public LinkedHashMap<String, IBulletCoreType> registeredBulletCores = new LinkedHashMap<>();
	public LinkedHashMap<String, IBulletCasingType> registeredCasings = new LinkedHashMap<>();

	public HashMap<String, IBulletModel> registeredModels = new HashMap<>();

	public boolean registerComponent(IBulletComponent component, String name)
	{
		if(!registeredComponents.containsKey(name))
		{
			registeredComponents.put(name, component);
			return true;
		}
		return false;
	}

	public boolean registerBulletCore(IBulletCoreType core, String name)
	{
		if(!registeredBulletCores.containsKey(name))
		{
			registeredBulletCores.put(name, core);
			return true;
		}
		return false;
	}

	public boolean registerCasing(IBulletCasingType casing, String name)
	{
		if(!registeredCasings.containsKey(name))
		{
			registeredCasings.put(name, casing);

			if(FMLCommonHandler.instance().getSide().isClient())
			{
				try
				{
					registeredModels.put(casing.getName(), casing.getModel().newInstance());
				} catch(InstantiationException e)
				{
					e.printStackTrace();
				} catch(IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}

	@Nullable
	public IBulletComponent getComponent(String name)
	{
		return registeredComponents.get(name);
	}

	@Nullable
	public IBulletCasingType getCasing(String name)
	{
		return registeredCasings.get(name);
	}

	@Nullable
	public IBulletCoreType getCore(String name)
	{
		return registeredBulletCores.get(name);
	}

	public enum EnumComponentRole implements IStringSerializable
	{
		SHRAPNEL,
		PIERCING,
		EXPLOSIVE,
		INCENDIARY,
		TRACER,
		FLARE,
		CHEMICAL,
		SPECIAL;

		@Override
		public String getName()
		{
			return this.toString().toLowerCase(Locale.ENGLISH);
		}
	}
}
