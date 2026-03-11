package pl.pabilo8.immersiveintelligence.common.util.sound;

import com.google.common.collect.Sets;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.HitEffect;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

import static pl.pabilo8.immersiveintelligence.api.ammo.enums.HitEffect.RICOCHET;

/**
 * Despite being sounds, inner classes are NOT client side only
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 23.09.2022
 */
public class AdvancedSounds
{
	/**
	 * A sound consisting of 3 parts - played on start, looped and played on finish/stop
	 */
	public static class MultiSound
	{
		public final ResourceLocation id;
		@Nullable
		private final SoundEvent soundBegin, soundEnd;
		@Nonnull
		private final SoundEvent soundMid;

		public MultiSound(String id, SoundEvent soundBegin, SoundEvent soundMid, SoundEvent soundEnd)
		{
			this(IIReference.RES_II.with(id), soundBegin, soundMid, soundEnd);
		}

		/**
		 * @param soundBegin start sound
		 * @param soundMid   middle/looped sound
		 * @param soundEnd   end sound
		 */
		public MultiSound(ResourceLocation id, SoundEvent soundBegin, SoundEvent soundMid, SoundEvent soundEnd)
		{
			IISounds.multiSounds.put(this.id = id, this);
			this.soundBegin = soundBegin;
			this.soundMid = soundMid;
			this.soundEnd = soundEnd;
		}

		/**
		 * It may make no sense, but it is here for convenience
		 *
		 * @param sound middle/looped sound
		 */
		public MultiSound(SoundEvent sound)
		{
			this(ResLoc.of(sound.getRegistryName()+"_loop"), null, sound, null);
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

		public SoundEvent getImpactSound()
		{
			return soundImpact;
		}

		public SoundEvent getRicochetSound()
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
		public final ResourceLocation id;
		private final Set<Tuple<Double, SoundEvent>> sounds;

		@SafeVarargs
		public RangedSound(String id, Tuple<Double, SoundEvent>... sounds)
		{
			this(IIReference.RES_II.with(id), sounds);
		}

		@SafeVarargs
		public RangedSound(ResourceLocation id, Tuple<Double, SoundEvent>... sounds)
		{
			IISounds.rangedSounds.put(this.id = id, this);
			this.sounds = Sets.newHashSet(sounds);
		}

		public Set<Tuple<Double, SoundEvent>> getSounds()
		{
			return sounds;
		}
	}
}
