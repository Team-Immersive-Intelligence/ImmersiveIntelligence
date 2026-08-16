package pl.pabilo8.immersiveintelligence.api.ammo.penetration;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SoundEvent;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.HitEffect;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.common.util.sound.AdvancedSounds.HitSound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * Stores penetration properties for a block or entity material.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 14.08.2026
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
	@Nullable
	private Function<IBlockState, IBlockState> flammableVariant;

	/**
	 * @param hardness       penetration hardness level
	 * @param thickness      density (constant resistance multiplier)
	 * @param integrity      hit-points of the block
	 * @param debrisParticle the particle effect spawned when the block is destroyed, or {@code null} if none
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

	/**
	 * Sets the function that converts a block to its burnt variant.
	 *
	 * @param flammableVariant replacement function
	 * @return this handler
	 */
	public PenetrationHandler withFlammableVariant(@Nonnull Function<IBlockState, IBlockState> flammableVariant)
	{
		this.flammableVariant = flammableVariant;
		return this;
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

	@Override
	public boolean canRicochet()
	{
		return hardness.canRicochet();
	}

	@Override
	public boolean canBeDamaged()
	{
		return true;
	}

	@Override
	public String getDebrisParticle()
	{
		return debrisParticle;
	}

	@Override
	public boolean hasFlammableVariant()
	{
		return flammableVariant!=null;
	}

	@Nullable
	@Override
	public IBlockState getFlammableVariant(IBlockState state)
	{
		return flammableVariant==null?null: flammableVariant.apply(state);
	}
}
