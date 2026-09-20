package pl.pabilo8.immersiveintelligence.api.ammo.penetration;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SoundEvent;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.HitEffect;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.common.util.sound.AdvancedSounds.HitSound;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * Stores immutable penetration properties for a block or entity material.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.08.2026
 * @since 27.03.2024
 */
@Builder(toBuilder = true, setterPrefix = "with")
public class PenetrationHandler implements IPenetrationHandler
{
	@Getter
	@NonNull
	private final PenetrationHardness penetrationHardness;
	@Getter
	private final float thickness, integrity;
	@Getter
	@Nullable
	private final String debrisParticle;
	@Nullable
	private final String impactParticle;
	@Getter
	@Nullable
	private final String ricochetParticle;
	@Nullable
	private final SoundEvent impactSound, ricochetSound;
	@Nullable
	private final Function<IBlockState, IBlockState> flammableVariant;
	@Builder.Default
	private final boolean damageable = true;

	@Nullable
	@Override
	public SoundEvent getSpecialSound(HitEffect effect)
	{
		return effect==HitEffect.IMPACT?impactSound: (ricochetSound==null?impactSound: ricochetSound);
	}

	@Override
	public boolean canRicochet()
	{
		return penetrationHardness.canRicochet();
	}

	@Override
	public boolean canBeDamaged()
	{
		return damageable;
	}

	@Nullable
	@Override
	public String getImpactParticle()
	{
		return impactParticle==null?"debris/generic_hit": impactParticle;
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

	//--- Builder ---//

	public static PenetrationHandlerBuilder builder(PenetrationHardness hardness)
	{
		return new PenetrationHandlerBuilder()
				.withPenetrationHardness(hardness);
	}

	public static PenetrationHandlerBuilder builder(PenetrationHardness hardness, float thickness, float integrity, String debrisParticle)
	{
		return builder(hardness)
				.withThickness(thickness)
				.withIntegrity(integrity)
				.withDebrisParticle(debrisParticle);
	}

	public static class PenetrationHandlerBuilder
	{
		public PenetrationHandlerBuilder withHitSound(@Nullable HitSound hitSound)
		{
			return withImpactSound(hitSound==null?null: hitSound.getImpactSound())
					.withRicochetSound(hitSound==null?null: hitSound.getRicochetSound());
		}
	}
}
