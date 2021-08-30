package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.compat.crafttweaker.CraftTweakerHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCore;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * @author Pabilo8
 * @since 09.08.2021
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".bullet.BulletTweaker")
@ZenRegister
public class BulletTweaker
{
	// TODO: 17.08.2021 custom components

	@ZenMethod
	public static void addShrapnel(String name, int color, String texture, int damage, float mass, float brightness)
	{
		ShrapnelHandler.addShrapnel(name, color, texture, damage, mass, brightness);
		CraftTweakerAPI.apply(new IAction(){

			@Override
			public void apply()
			{

			}

			@Override
			public String describe()
			{
				return "Added shrapnel for "+name;
			}
		});
	}

	@ZenMethod
	public static void removeShrapnel(String shrapnel)
	{
		ShrapnelHandler.removeShrapnel(shrapnel);
		CraftTweakerAPI.apply(new IAction(){

			@Override
			public void apply()
			{

			}

			@Override
			public String describe()
			{
				return "Removed shrapnel for "+shrapnel;
			}
		});
	}

	@ZenClass("mods."+ImmersiveIntelligence.MODID+".bullet.CoreMaterialBuilder")
	@ZenRegister
	public static class CoreMaterialBuilder
	{
		private final String name;
		private int color;
		private float density, dmgModifier, explosionModifier, penHardness;
		private Object stack;

		private CoreMaterialBuilder(String name)
		{
			this.name = name;
		}

		@ZenMethod
		public static CoreMaterialBuilder create(String name)
		{
			return new CoreMaterialBuilder("core_"+Utils.toSnakeCase(name));
		}

		@ZenMethod
		public void setColor(int color)
		{
			this.color = color;
		}

		@ZenMethod
		public void setDensity(float density)
		{
			this.density = density;
		}

		@ZenMethod
		public void setDmgModifier(float dmgModifier)
		{
			this.dmgModifier = dmgModifier;
		}

		@ZenMethod
		public void setExplosionModifier(float explosionModifier)
		{
			this.explosionModifier = explosionModifier;
		}

		@ZenMethod
		public void setPenHardness(float penHardness)
		{
			this.penHardness = penHardness;
		}

		@ZenMethod
		public void setStack(IIngredient stack)
		{
			this.stack = CraftTweakerHelper.toObject(stack);
		}

		@ZenMethod
		public void register()
		{
			CraftTweakerAPI.apply(new Add(this));
		}

		private static class Add implements IAction
		{
			private final CoreMaterialBuilder core;

			public Add(CoreMaterialBuilder core)
			{
				this.core = core;
			}

			@Override
			public void apply()
			{
				final IngredientStack s = ApiUtils.createIngredientStack(core.stack);
				BulletRegistry.INSTANCE.registerBulletCore(
						new IBulletCore()
						{
							@Override
							public String getName()
							{
								return core.name;
							}

							@Override
							public IngredientStack getMaterial()
							{
								return s;
							}

							@Override
							public float getDensity()
							{
								return core.density;
							}

							@Override
							public float getDamageModifier()
							{
								return core.dmgModifier;
							}

							@Override
							public float getExplosionModifier()
							{
								return core.explosionModifier;
							}

							@Override
							public float getPenetrationHardness()
							{
								return core.penHardness;
							}

							@Override
							public int getColour()
							{
								return core.color;
							}
						}
				);
			}

			@Override
			public String describe()
			{
				return "Adding Bullet Core for material "+core.name;
			}
		}
	}
}
