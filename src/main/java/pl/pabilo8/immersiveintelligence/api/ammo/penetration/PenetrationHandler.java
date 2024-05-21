package pl.pabilo8.immersiveintelligence.api.ammo.penetration;

import net.minecraft.util.SoundEvent;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.HitEffect;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.HitSound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 27.03.2024
 */
public class PenetrationHandler implements IPenetrationHandler
{
	private final PenetrationHardness hardness;
	private final float integrity, thickness;
	@Nullable
	private final String debrisParticle;
	@Nullable
	private final SoundEvent impactSound, ricochetSound;

	/**
	 * @param hardness       penetration hardness level
	 * @param thickness      density (constant resistance multiplier)
	 * @param integrity      hit-points of the block
	 * @param debrisParticle
	 * @param impactSound    the sound played when the block is hit
	 * @param ricochetSound  the sound played when the bullet ricochets off the block
	 */
	public PenetrationHandler(PenetrationHardness hardness,
							  float thickness, float integrity,
							  @Nullable String debrisParticle,
							  @Nullable SoundEvent impactSound, @Nullable SoundEvent ricochetSound)
	{
		this.hardness = hardness;
		this.integrity = integrity;
		this.thickness = thickness;
		this.debrisParticle = debrisParticle;
		this.impactSound = impactSound;
		this.ricochetSound = ricochetSound;
	}

	public PenetrationHandler(PenetrationHardness hardness,
							  float thickness, float integrity,
							  @Nullable String debrisParticle,
							  @Nonnull HitSound hitSound)
	{
		this.hardness = hardness;
		this.integrity = integrity;
		this.thickness = thickness;
		this.debrisParticle = debrisParticle;
		this.impactSound = hitSound.getImpactSound();
		this.ricochetSound = hitSound.getRicochetSound();
	}

	@Override
	public PenetrationHardness getPenetrationHardness()
	{
		return hardness;
	}

	@Override
	public float getIntegrity()
	{
		return integrity;
	}

	@Override
	public float getThickness()
	{
		return thickness;
	}

	@Nullable
	@Override
	public SoundEvent getSpecialSound(HitEffect effect)
	{
		return effect==HitEffect.IMPACT?impactSound: (ricochetSound==null?impactSound: ricochetSound);
	}

	public boolean canRicochet()
	{
		return hardness.canRicochet();
	}

	public boolean canBeDamaged()
	{
		return true;
	}

	@Override
	public String getDebrisParticle()
	{
		return debrisParticle;
	}
}
