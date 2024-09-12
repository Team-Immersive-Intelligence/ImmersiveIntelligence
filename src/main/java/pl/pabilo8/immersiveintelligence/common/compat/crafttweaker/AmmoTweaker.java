package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.compat.crafttweaker.CraftTweakerHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IVector3d;
import crafttweaker.api.world.IWorld;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * @author Pabilo8
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 05.01.2022
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".ammo.Ammo")
@ZenRegister
public class AmmoTweaker
{
	@ZenMethod
	public static void addShrapnel(String name, int color, String texture, int damage, float mass, float brightness)
	{
		ShrapnelHandler.addShrapnel(name, IIColor.fromPackedRGB(color), ResLoc.of(new ResourceLocation(texture)), damage, mass, brightness);
		CraftTweakerAPI.apply(new IAction()
		{
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
		CraftTweakerAPI.apply(new IAction()
		{
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

	@ZenClass("mods."+ImmersiveIntelligence.MODID+".ammo.CoreMaterialBuilder")
	@ZenRegister
	public static class CoreMaterialBuilder
	{
		private final String name;
		private IIColor color;
		private PenetrationHardness penHardness;
		private float density, dmgModifier, explosionModifier;
		private Object stack;

		private CoreMaterialBuilder(String name)
		{
			this.name = name;
		}

		@ZenMethod
		public static CoreMaterialBuilder create(String name)
		{
			return new CoreMaterialBuilder("core_"+IIStringUtil.toSnakeCase(name));
		}

		@ZenMethod
		public void setColor(int color)
		{
			this.color = IIColor.fromPackedRGB(color);
		}

		@ZenMethod
		public void setColor(int red, int green, int blue)
		{
			this.color = IIColor.fromRGB(red, green, blue);
		}

		@ZenMethod
		public void setColor(String hexColor)
		{
			this.color = IIColor.fromHex(hexColor);
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
		public void setPenHardness(String penHardness)
		{
			this.penHardness = IIUtils.enumValue(PenetrationHardness.class, penHardness);
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
				AmmoRegistry.registerCore(
						new AmmoCore(core.name, core.density, core.penHardness, core.explosionModifier, core.dmgModifier, core.color)
						{
							private final IngredientStack stack = ApiUtils.createIngredientStack(core.stack);

							@Override
							public IngredientStack getMaterial()
							{
								return stack;
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

	@ZenClass("mods."+ImmersiveIntelligence.MODID+".ammo.ComponentMaterialBuilder")
	@ZenRegister
	public static class ComponentMaterialBuilder
	{
		private final String name;
		private int color;
		private float density;
		private Object stack;
		private String role;
		private IComponentFunction function = null;

		private ComponentMaterialBuilder(String name)
		{
			this.name = name;
		}

		@ZenMethod
		public static ComponentMaterialBuilder create(String name)
		{
			return new ComponentMaterialBuilder(IIStringUtil.toSnakeCase(name));
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
		public void setStack(IIngredient stack)
		{
			this.stack = CraftTweakerHelper.toObject(stack);
		}

		@ZenMethod
		public void setComponentRole(String role)
		{
			this.role = role;
		}

		@ZenMethod
		public void setComponentEffect(IComponentFunction function)
		{
			this.function = function;
		}

		@ZenMethod
		public void register()
		{
			CraftTweakerAPI.apply(new Add(this));
		}

		private static class Add implements IAction
		{
			private final ComponentMaterialBuilder component;

			public Add(ComponentMaterialBuilder core)
			{
				this.component = core;
			}

			@Override
			public void apply()
			{
				final ComponentRole componentRole = IIUtils.enumValue(ComponentRole.class, component.role);

				AmmoRegistry.registerComponent(
						new AmmoComponent(component.name, component.density, componentRole, IIColor.fromPackedRGB(component.color))
						{
							private final IngredientStack stack = ApiUtils.createIngredientStack(component.stack);

							@Override
							public IngredientStack getMaterial()
							{
								return stack;
							}

							@Override
							public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentAmount, float multiplier, Entity owner)
							{
								if(component.function!=null)
									component.function.process(CraftTweakerMC.getIWorld(world),
											CraftTweakerMC.getIVector3d(pos),
											CraftTweakerMC.getIVector3d(dir),
											shape.getName(),
											multiplier,
											CraftTweakerMC.getIData(tag)
									);
							}
						}
				);
			}

			@Override
			public String describe()
			{
				return "Adding Bullet Core for material "+component.name;
			}
		}
	}

	@ZenClass("mods."+ImmersiveIntelligence.MODID+".ammo.IComponentFunction")
	@ZenRegister
	public interface IComponentFunction
	{
		void process(IWorld world, IVector3d pos, IVector3d dir, String effectShape, float amount, IData nbt);
	}

	@ZenMethod
	public static void removeCore(String name)
	{
		AmmoRegistry.unregisterCore(name);
	}

	@ZenMethod
	public static void removeComponent(String name)
	{
		AmmoRegistry.unregisterComponent(name);
	}
}
