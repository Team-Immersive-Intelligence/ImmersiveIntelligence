package pl.pabilo8.immersiveintelligence.api.ammo.penetration;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SoundEvent;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.HitEffect;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;

import javax.annotation.Nullable;

/**
 * Defines penetration and optional burnt-state behaviour for a material.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 14.08.2026
 * @since 27.03.2024
 */
public interface IPenetrationHandler
{
	/**
	 * @return hardness - the block's resistance to penetration
	 */
	PenetrationHardness getPenetrationHardness();

	/**
	 * @return integrity - the block's hp
	 */
	float getIntegrity();

	/**
	 * @return density (constant resistance multiplier)
	 */
	float getThickness();

	@Nullable
	default SoundEvent getSpecialSound(HitEffect effect)
	{
		return null;
	}

	boolean canRicochet();

	boolean canBeDamaged();

	String getDebrisParticle();

	/**
	 * @return true if this material can change to a burnt block state
	 */
	default boolean hasFlammableVariant()
	{
		return false;
	}

	/**
	 * Gets the burnt replacement for a block state.
	 *
	 * @param state current block state
	 * @return replacement state, or {@code null} if this state has no replacement
	 */
	@Nullable
	default IBlockState getFlammableVariant(IBlockState state)
	{
		return null;
	}
}
