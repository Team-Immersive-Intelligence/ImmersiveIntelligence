package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.block.IMaterial;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.PenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.PenetrationHandlerMetal;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * Adds CraftTweaker registration methods for penetration handlers.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.08.2026
 * @ii-approved 0.3.1
 * @since 06.01.2022
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".BlockPenetration")
@ZenRegister
@SuppressWarnings("unused")
public class BlockPenetrationTweaker
{
	@ZenMethod
	public static void addMaterial(IMaterial material, String hardnessTier, float thickness, float integrity, @Optional String sound)
	{
		PenetrationBuilder builder = PenetrationBuilder.create(hardnessTier, thickness, integrity);
		if(sound!=null)
			builder.setSound(sound);
		builder.registerMaterial(material);
	}

	@ZenMethod
	public static void addBlock(IBlockState state, String hardnessTier, float thickness, float integrity, @Optional String sound)
	{
		PenetrationBuilder builder = PenetrationBuilder.create(hardnessTier, thickness, integrity);
		if(sound!=null)
			builder.setSound(sound);
		builder.registerBlock(state);
	}

	@ZenMethod
	public static void addMetal(String name, String hardnessTier, float thickness, float integrity)
	{
		PenetrationRegistry.registerMetalMaterial(PenetrationHandlerMetal.create(
				name,
				IIUtils.enumValue(PenetrationHardness.class, hardnessTier),
				thickness, integrity
		));
	}

	private static SoundEvent getSound(String sound)
	{
		return SoundEvent.REGISTRY.getObject(new ResourceLocation(sound));
	}

	/**
	 * Builds and registers custom penetration handlers from CraftTweaker.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 24.08.2026
	 */
	@ZenClass("mods."+ImmersiveIntelligence.MODID+".BlockPenetration.Builder")
	@ZenRegister
	public static class PenetrationBuilder
	{
		private final PenetrationHandler.PenetrationHandlerBuilder builder;

		private PenetrationBuilder(String hardnessTier, float thickness, float integrity)
		{
			builder = PenetrationHandler.builder(IIUtils.enumValue(PenetrationHardness.class, hardnessTier))
					.withThickness(thickness)
					.withIntegrity(integrity);
		}

		/**
		 * Creates a penetration handler builder.
		 *
		 * @param hardnessTier material hardness
		 * @param thickness    material thickness
		 * @param integrity    material integrity
		 * @return penetration handler builder
		 */
		@ZenMethod
		public static PenetrationBuilder create(String hardnessTier, float thickness, float integrity)
		{
			return new PenetrationBuilder(hardnessTier, thickness, integrity);
		}

		@ZenMethod
		public void setHardness(String hardnessTier)
		{
			builder.withPenetrationHardness(IIUtils.enumValue(PenetrationHardness.class, hardnessTier));
		}

		@ZenMethod
		public void setThickness(float thickness)
		{
			builder.withThickness(thickness);
		}

		@ZenMethod
		public void setIntegrity(float integrity)
		{
			builder.withIntegrity(integrity);
		}

		@ZenMethod
		public void setDebrisParticle(String particle)
		{
			builder.withDebrisParticle(particle);
		}

		@ZenMethod
		public void setImpactParticle(String particle)
		{
			builder.withImpactParticle(particle);
		}

		@ZenMethod
		public void setRicochetParticle(String particle)
		{
			builder.withRicochetParticle(particle);
		}

		@ZenMethod
		public void setSound(String sound)
		{
			SoundEvent event = getSound(sound);
			builder.withImpactSound(event).withRicochetSound(event);
		}

		@ZenMethod
		public void setImpactSound(String sound)
		{
			builder.withImpactSound(getSound(sound));
		}

		@ZenMethod
		public void setRicochetSound(String sound)
		{
			builder.withRicochetSound(getSound(sound));
		}

		@ZenMethod
		public void setDamageable(boolean damageable)
		{
			builder.withDamageable(damageable);
		}

		/**
		 * Registers this handler for a block material.
		 *
		 * @param material material to register
		 */
		@ZenMethod
		public void registerMaterial(IMaterial material)
		{
			PenetrationRegistry.registerMaterial(CraftTweakerMC.getMaterial(material), builder.build());
		}

		/**
		 * Registers this handler for a block state.
		 *
		 * @param state block state to register
		 */
		@ZenMethod
		public void registerBlock(IBlockState state)
		{
			PenetrationRegistry.registerState(blockState -> state.compare(CraftTweakerMC.getBlockState(blockState))==0, builder.build());
		}

		/**
		 * Registers this handler for an Ore Dictionary entry.
		 *
		 * @param oreName Ore Dictionary entry
		 */
		@ZenMethod
		public void registerOre(String oreName)
		{
			PenetrationRegistry.registerOre(oreName, builder.build());
		}
	}
}
