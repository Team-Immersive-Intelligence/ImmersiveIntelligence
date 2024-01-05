package pl.pabilo8.immersiveintelligence.api.ammo.penetration_handlers;

import net.minecraft.util.SoundEvent;
import pl.pabilo8.immersiveintelligence.api.ammo.IIPenetrationRegistry.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.HitEffect;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenMaterialTypes;
import pl.pabilo8.immersiveintelligence.common.IISounds;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 06-03-2020
 */
public class PenetrationHandlerWood
{
	public static class PenetrationHandlerLog implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 165f;
		}

		@Override
		public float getReduction()
		{
			return 0.95f;
		}

		@Override
		public PenMaterialTypes getPenetrationType()
		{
			return PenMaterialTypes.GROUND;
		}

		@Nullable
		@Override
		public SoundEvent getSpecialSound(HitEffect effect)
		{
			return IISounds.hitWood.getSpecialSound(effect);
		}
	}

	public static class PenetrationHandlerPlanks implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 75f;
		}

		@Override
		public float getReduction()
		{
			return 0.65f;
		}

		@Override
		public PenMaterialTypes getPenetrationType()
		{
			return PenMaterialTypes.GROUND;
		}

		@Nullable
		@Override
		public SoundEvent getSpecialSound(HitEffect effect)
		{
			return IISounds.hitWood.getSpecialSound(effect);
		}
	}
}
