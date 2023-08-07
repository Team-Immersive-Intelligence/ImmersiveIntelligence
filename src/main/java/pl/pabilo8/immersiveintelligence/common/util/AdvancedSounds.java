package pl.pabilo8.immersiveintelligence.common.util;

import com.google.common.collect.Sets;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.HitEffect;
import pl.pabilo8.immersiveintelligence.common.IISounds;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

import static pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.HitEffect.RICOCHET;

/**
 * Despite being sounds, inner classes are NOT client side only
 *
 * @author Pabilo8
 * @since 23.09.2022
 */
public class AdvancedSounds
{
	/**
	 * A sound consisting of 3 parts - played on start, looped and played on finish/stop
	 */
	public static class MultiSound
	{
		public final int id;
		@Nullable
		private final SoundEvent soundBegin, soundEnd;
		@Nonnull
		private final SoundEvent soundMid;


		public MultiSound(SoundEvent soundBegin, SoundEvent soundMid, SoundEvent soundEnd)
		{
			this.id = IISounds.rangedSounds.size();
			IISounds.multiSounds.add(this);

			this.soundBegin = soundBegin;
			this.soundMid = soundMid;
			this.soundEnd = soundEnd;
		}

		@Nullable
		public SoundEvent getSoundBegin()
		{
			return soundBegin;
		}

		@Nonnull
		public SoundEvent getSoundMid()
		{
			return soundMid;
		}

		@Nullable
		public SoundEvent getSoundEnd()
		{
			return soundEnd;
		}
	}

	/**
	 * A class consisting of 2 sounds - impact and ricochet
	 */
	public static class HitSound
	{
		private final SoundEvent soundImpact, soundRicochet;

		public HitSound(SoundEvent impact, @Nullable SoundEvent ricochet)
		{
			this.soundImpact = impact;
			this.soundRicochet = ricochet;
		}

		public SoundEvent getSoundImpact()
		{
			return soundImpact;
		}

		public SoundEvent getSoundRicochet()
		{
			return soundRicochet;
		}

		@Nullable
		public SoundEvent getSpecialSound(HitEffect effect)
		{
			return effect==RICOCHET?soundRicochet: soundImpact;
		}
	}

	public static class RangedSound
	{
		public final int id;
		private final Set<Tuple<Double, SoundEvent>> sounds;

		@SafeVarargs
		public RangedSound(Tuple<Double, SoundEvent>... sounds)
		{
			this.id = IISounds.rangedSounds.size();
			IISounds.rangedSounds.add(this);

			this.sounds = Sets.newHashSet(sounds);
		}

		public Set<Tuple<Double, SoundEvent>> getSounds()
		{
			return sounds;
		}
	}
}
