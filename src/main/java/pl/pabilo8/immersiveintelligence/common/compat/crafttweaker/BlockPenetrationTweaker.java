package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.block.IMaterial;
import crafttweaker.api.minecraft.CraftTweakerMC;
import lombok.NonNull;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.PenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.PenetrationHandlerMetal;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;

/**
 * Adds CraftTweaker registration methods for penetration handlers.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 13.10.2026
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
		createBuilder(hardnessTier, thickness, integrity, sound).registerMaterial(material);
	}

	/**
	 * Registers a material with a fixed burnt block.
	 */
	@ZenMethod
	public static void addMaterial(IMaterial material, String hardnessTier, float thickness, float integrity,
								   IBlock flammableVariant, @Optional String sound)
	{
		PenetrationBuilder builder = createBuilder(hardnessTier, thickness, integrity, sound);
		builder.setFlammableVariant(flammableVariant);
		builder.registerMaterial(material);
	}

	/**
	 * Registers a material with a fixed burnt block state.
	 */
	@ZenMethod
	public static void addMaterial(IMaterial material, String hardnessTier, float thickness, float integrity,
								   IBlockState flammableVariant, @Optional String sound)
	{
		PenetrationBuilder builder = createBuilder(hardnessTier, thickness, integrity, sound);
		builder.setFlammableVariant(flammableVariant);
		builder.registerMaterial(material);
	}

	/**
	 * Registers a material with a burnt-state function.
	 */
	@ZenMethod
	public static void addMaterial(IMaterial material, String hardnessTier, float thickness, float integrity,
								   IFlammableVariantFunction flammableVariant, @Optional String sound)
	{
		PenetrationBuilder builder = createBuilder(hardnessTier, thickness, integrity, sound);
		builder.setFlammableVariant(flammableVariant);
		builder.registerMaterial(material);
	}

	@ZenMethod
	public static void addBlock(IBlockState state, String hardnessTier, float thickness, float integrity, @Optional String sound)
	{
		createBuilder(hardnessTier, thickness, integrity, sound).registerBlock(state);
	}

	/**
	 * Registers a block state with a fixed burnt block.
	 */
	@ZenMethod
	public static void addBlock(IBlockState state, String hardnessTier, float thickness, float integrity,
								IBlock flammableVariant, @Optional String sound)
	{
		PenetrationBuilder builder = createBuilder(hardnessTier, thickness, integrity, sound);
		builder.setFlammableVariant(flammableVariant);
		builder.registerBlock(state);
	}

	/**
	 * Registers a block state with a fixed burnt block state.
	 */
	@ZenMethod
	public static void addBlock(IBlockState state, String hardnessTier, float thickness, float integrity,
								IBlockState flammableVariant, @Optional String sound)
	{
		PenetrationBuilder builder = createBuilder(hardnessTier, thickness, integrity, sound);
		builder.setFlammableVariant(flammableVariant);
		builder.registerBlock(state);
	}

	/**
	 * Registers a block state with a burnt-state function.
	 */
	@ZenMethod
	public static void addBlock(IBlockState state, String hardnessTier, float thickness, float integrity,
								IFlammableVariantFunction flammableVariant, @Optional String sound)
	{
		PenetrationBuilder builder = createBuilder(hardnessTier, thickness, integrity, sound);
		builder.setFlammableVariant(flammableVariant);
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

	private static PenetrationBuilder createBuilder(String hardnessTier, float thickness, float integrity, @Nullable String sound)
	{
		PenetrationBuilder builder = PenetrationBuilder.create(hardnessTier, thickness, integrity);
		if(sound!=null)
			builder.setSound(sound);
		return builder;
	}

	/**
	 * Selects a burnt replacement for the current block state.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 13.10.2026
	 */
	@FunctionalInterface
	@ZenClass("mods."+ImmersiveIntelligence.MODID+".IFlammableVariantFunction")
	@ZenRegister
	public interface IFlammableVariantFunction
	{
		/**
		 * Returns the replacement state, or null to prevent burning this state.
		 */
		@Nullable
		@ZenMethod
		IBlockState getFlammableVariant(IBlockState previousState);
	}

	/**
	 * Builds and registers custom penetration handlers from CraftTweaker.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 24.08.2026
	 */
	@ZenClass("mods."+ImmersiveIntelligence.MODID+".BlockPenetrationBuilder")
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
		 * Sets the burnt replacement from a block and its metadata.
		 */
		@ZenMethod
		public void setFlammableVariant(@NonNull IBlock variant)
		{
			Block block = CraftTweakerMC.getBlock(variant);
			int metadata = variant.getMeta();
			setFlammableVariant(CraftTweakerMC.getBlockState(metadata < 0||metadata==OreDictionary.WILDCARD_VALUE?
					block.getDefaultState(): block.getStateFromMeta(metadata)));
		}

		/**
		 * Sets a fixed burnt replacement state.
		 */
		@ZenMethod
		public void setFlammableVariant(@NonNull IBlockState variant)
		{
			net.minecraft.block.state.IBlockState replacement = CraftTweakerMC.getBlockState(variant);
			builder.withFlammableVariant(state -> replacement);
		}

		/**
		 * Sets a function that selects the burnt replacement state.
		 */
		@ZenMethod
		public void setFlammableVariant(@NonNull IFlammableVariantFunction variant)
		{
			builder.withFlammableVariant(state -> CraftTweakerMC.getBlockState(
					variant.getFlammableVariant(CraftTweakerMC.getBlockState(state))));
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
