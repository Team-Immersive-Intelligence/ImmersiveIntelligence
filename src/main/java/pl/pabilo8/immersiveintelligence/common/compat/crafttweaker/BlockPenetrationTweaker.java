package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.block.IMaterial;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoUtils;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.PenMaterialTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.HitEffect;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.bullets.penhandlers.PenetrationHandlerMetals.PenetrationHandlerMetal;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 06.01.2022
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".BlockPenetration")
@ZenRegister
public class BlockPenetrationTweaker
{
	@ZenMethod
	public static void addMaterial(IMaterial material, float integrity, float density, String penetrationType, String sound)
	{
		final Material mat = CraftTweakerMC.getMaterial(material);
		final CTPenetrationHandler pen = new CTPenetrationHandler(integrity, density, sound, penetrationType);
		PenetrationRegistry.registeredMaterials.put(m -> m==mat, pen);
	}

	@ZenMethod
	public static void addBlock(IBlockState state, float integrity, float density, String penetrationType, String sound)
	{
		final CTPenetrationHandler pen = new CTPenetrationHandler(integrity, density, sound, penetrationType);

		PenetrationRegistry.registeredBlocks.put(b -> state.compare(CraftTweakerMC.getBlockState(b))==0, pen);
	}

	@ZenMethod
	public static void addMetal(String name, float integrity, float density)
	{
		final PenetrationHandlerMetal pen = new PenetrationHandlerMetal()
		{
			@Override
			public float getIntegrity()
			{
				return integrity;
			}

			@Override
			public float getDensity()
			{
				return density;
			}
		};
		AmmoUtils.registerMetalMaterial(pen, name);
	}

	private static class CTPenetrationHandler implements IPenetrationHandler
	{
		float integrity, density;
		SoundEvent sound;
		PenMaterialTypes penMat;

		public CTPenetrationHandler(float integrity, float density, @Nullable String sound, String penMat)
		{
			this.integrity = integrity;
			this.density = density;
			this.sound = sound==null?null: SoundEvent.REGISTRY.getObject(new ResourceLocation(sound));
			this.penMat = PenMaterialTypes.v(penMat);
		}

		@Override
		public float getIntegrity()
		{
			return integrity;
		}

		@Override
		public float getDensity()
		{
			return density;
		}

		@Nullable
		@Override
		public SoundEvent getSpecialSound(HitEffect effect)
		{
			return sound;
		}

		@Override
		public PenMaterialTypes getPenetrationType()
		{
			return penMat;
		}
	}
}
