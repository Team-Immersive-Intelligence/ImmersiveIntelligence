package pl.pabilo8.immersiveintelligence.api.bullets;

import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.function.Function;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class BulletRegistry
{
	public static BulletRegistry INSTANCE = new BulletRegistry();
	public LinkedHashMap<String, IBulletComponent> registeredComponents = new LinkedHashMap<>();
	public LinkedHashMap<String, IBulletCore> registeredBulletCores = new LinkedHashMap<>();
	public LinkedHashMap<String, IBullet> registeredCasings = new LinkedHashMap<>();

	public HashMap<String, IBulletModel> registeredModels = new HashMap<>();

	public boolean registerComponent(IBulletComponent component)
	{
		String name = component.getName();
		if(!registeredComponents.containsKey(name))
		{
			registeredComponents.put(name, component);
			return true;
		}
		return false;
	}

	public boolean registerBulletCore(IBulletCore core)
	{
		String name = core.getName();
		if(!registeredBulletCores.containsKey(name))
		{
			registeredBulletCores.put(name, core);
			return true;
		}
		return false;
	}

	public boolean registerCasing(IBullet casing)
	{
		String name = casing.getName();
		if(!registeredCasings.containsKey(name))
		{
			registeredCasings.put(name, casing);

			if(FMLCommonHandler.instance().getSide().isClient())
			{
				try
				{
					registeredModels.put(casing.getName(), casing.getModel().newInstance());
				} catch(InstantiationException|IllegalAccessException e)
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
	public IBullet getCasing(String name)
	{
		return registeredCasings.get(name);
	}

	@Nullable
	public IBulletCore getCore(String name)
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

	public enum EnumCoreTypes implements IStringSerializable
	{
		SOFTPOINT(1, 0.5f, 1f, 1f)
				{
					@Override
					public float getDamageMod(PenMaterialTypes p)
					{
						return p==PenMaterialTypes.FLESH?1.65f: 1f;
					}
				},
		SHAPED(2, 0.5f, 1.25f, 1f)
				{
					@Override
					public float getPenMod(PenMaterialTypes p)
					{
						return p==PenMaterialTypes.METAL?1.5f: 0.5f;
					}

					@Override
					public float getDamageMod(PenMaterialTypes p)
					{
						return p==PenMaterialTypes.METAL?1.85f: 1f;
					}
				},
		PIERCING(1, 1.35f, 0.75f, 1f),
		PIERCING_FIN_STABILIZED(0, 2f, 0f, 0.85f)
				{
					@Override
					public float getPenMod(PenMaterialTypes p)
					{
						return p==PenMaterialTypes.GROUND||p==PenMaterialTypes.SOLID?1.15f: 2f;
					}
				},
		CANISTER(2, 0.125f, 1.25f, 0.125f)
				{
					@Override
					public float getPenMod(PenMaterialTypes p)
					{
						return p==PenMaterialTypes.LIGHT?1f: 0.125f;
					}
				},
		DOUBLE_CANISTER(4, 0.125f, 1.5f, 0.125f)
				{
					@Override
					public float getPenMod(PenMaterialTypes p)
					{
						return p==PenMaterialTypes.LIGHT?1f: 0.125f;
					}
				};

		private final int componentSlots;
		private final float componentEffectivenessMod;
		private final Function<PenMaterialTypes, Float> getPenEffectiveness;
		private final Function<PenMaterialTypes, Float> getDamageMod;

		EnumCoreTypes(int componentSlots, float penHardnessMod, float componentEffectivenessMod, float damageMod)
		{
			this.componentSlots = componentSlots;
			this.getPenEffectiveness = penMaterialTypes -> penHardnessMod;
			this.componentEffectivenessMod = componentEffectivenessMod;
			this.getDamageMod = penMaterialTypes -> damageMod;
		}

		public float getPenMod(PenMaterialTypes p)
		{
			return getPenEffectiveness.apply(p);
		}

		public float getDamageMod(PenMaterialTypes p)
		{
			return getDamageMod.apply(p);
		}

		public int getComponentSlots()
		{
			return componentSlots;
		}

		public float getComponentEffectivenessMod()
		{
			return componentEffectivenessMod;
		}

		@Override
		public String getName()
		{
			return this.toString().toLowerCase(Locale.ENGLISH);
		}

		public static EnumCoreTypes v(String s)
		{
			return valueOf(s.toUpperCase());
		}
	}

	public enum PenMaterialTypes
	{
		//all the metals
		METAL(true),
		//dirt, sand, etc.
		GROUND(false),
		//stone, bricks
		SOLID(true),
		//unarmored mobs
		FLESH(false),
		//glass, wool, leaves, cloth
		LIGHT(false);

		boolean ricochet;

		PenMaterialTypes(boolean ricochet)
		{
			this.ricochet = ricochet;
		}

		public boolean canRicochetOff()
		{
			return ricochet;
		}
	}
}
