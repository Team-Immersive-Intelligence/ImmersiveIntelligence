package pl.pabilo8.immersiveintelligence.api.ammo.penetration;

import net.minecraft.util.SoundEvent;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.HitEffect;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
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
}
