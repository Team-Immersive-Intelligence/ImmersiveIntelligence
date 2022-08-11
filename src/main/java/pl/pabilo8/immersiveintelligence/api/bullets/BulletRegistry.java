package pl.pabilo8.immersiveintelligence.api.bullets;

import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
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
	public LinkedHashMap<String, IBullet> registeredBulletItems = new LinkedHashMap<>();

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

	public boolean registerBulletItem(IBullet casing)
	{
		String name = casing.getName();
		if(!registeredBulletItems.containsKey(name))
		{
			registeredBulletItems.put(name, casing);

			if(FMLCommonHandler.instance().getSide().isClient())
			{
				try
				{
					IBulletModel iBulletModel = casing.getModel().newInstance();
					iBulletModel.subscribeToList(casing.getName());
					registeredModels.put(casing.getName(), iBulletModel);
				}
				catch(InstantiationException|IllegalAccessException e)
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
	public IBullet getBulletItem(String name)
	{
		return registeredBulletItems.get(name);
	}

	@Nullable
	public IBulletCore getCore(String name)
	{
		return registeredBulletCores.get(name);
	}

	@Nullable
	@SideOnly(Side.CLIENT)
	public IBulletModel getModel(IBullet bullet)
	{
		return registeredModels.get(bullet.getName());
	}

	public enum EnumComponentRole implements IStringSerializable
	{
		GENERAL_PURPOSE,
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

		@Nonnull
		public static EnumComponentRole v(String s)
		{
			String ss = s.toUpperCase();
			return Arrays.stream(values()).filter(e -> e.name().equals(ss)).findFirst().orElse(GENERAL_PURPOSE);
		}
	}

	public enum EnumCoreTypes implements IStringSerializable
	{
		SOFTPOINT(2, 0.5f, 1f, 1f, EnumComponentRole.GENERAL_PURPOSE)
				{
					@Override
					public float getDamageMod(PenMaterialTypes p)
					{
						return p==PenMaterialTypes.FLESH?1.65f: 1f;
					}
				},
		SHAPED(3, 0.5f, 1.25f, 1f, EnumComponentRole.GENERAL_PURPOSE)
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
		PIERCING(1, 1.35f, 0.75f, 1f, EnumComponentRole.PIERCING),
		PIERCING_SABOT(0, 2f, 0f, 0.85f, EnumComponentRole.PIERCING)
				{
					@Override
					public float getPenMod(PenMaterialTypes p)
					{
						return p==PenMaterialTypes.GROUND||p==PenMaterialTypes.SOLID?1.15f: 2f;
					}
				},
		CANISTER(4, 0.125f, 1.25f, 0.125f, EnumComponentRole.GENERAL_PURPOSE)
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
		@Nullable
		private final EnumComponentRole role;

		EnumCoreTypes(int componentSlots, float penHardnessMod, float componentEffectivenessMod, float damageMod, @Nullable EnumComponentRole role)
		{
			this.componentSlots = componentSlots;
			this.getPenEffectiveness = penMaterialTypes -> penHardnessMod;
			this.componentEffectivenessMod = componentEffectivenessMod;
			this.getDamageMod = penMaterialTypes -> damageMod;
			this.role = role;
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

		@Nullable
		public EnumComponentRole getRole()
		{
			return role;
		}

		@Nonnull
		public static EnumCoreTypes v(String s)
		{
			String ss = s.toUpperCase();
			return Arrays.stream(values()).filter(e -> e.name().equals(ss)).findFirst().orElse(SOFTPOINT);
		}
	}

	public enum EnumFuseTypes implements IStringSerializable
	{
		CONTACT,
		TIMED,
		PROXIMITY;

		@Override
		public String getName()
		{
			return this.toString().toLowerCase(Locale.ENGLISH);
		}

		@Nonnull
		public static EnumFuseTypes v(String s)
		{
			String ss = s.toUpperCase();
			return Arrays.stream(values()).filter(e -> e.name().equals(ss)).findFirst().orElse(CONTACT);
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

		public static PenMaterialTypes v(String s)
		{
			String ss = s.toUpperCase();
			return Arrays.stream(values()).filter(e -> e.name().equals(ss)).findFirst().orElse(METAL);
		}
	}
}
