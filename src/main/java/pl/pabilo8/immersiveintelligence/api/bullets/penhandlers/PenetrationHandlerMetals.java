package pl.pabilo8.immersiveintelligence.api.bullets.penhandlers;

import net.minecraft.util.SoundEvent;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.PenMaterialTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.HitEffect;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.common.IISounds;

import javax.annotation.Nullable;

import static pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.HitEffect.RICOCHET;

/**
 * @author Pabilo8
 * @since 06-03-2020
 */
public class PenetrationHandlerMetals
{
	public static class PenetrationHandlerIron extends PenetrationHandlerMetal
	{
		@Override
		public float getIntegrity()
		{
			return 295f;
		}

		@Override
		public float getDensity()
		{
			return 1.25f;
		}
	}

	public static class PenetrationHandlerSteel extends PenetrationHandlerMetal
	{
		@Override
		public float getIntegrity()
		{
			return 365f;
		}

		@Override
		public float getDensity()
		{
			return 1.25f;
		}
	}

	public static class PenetrationHandlerCopper extends PenetrationHandlerMetal
	{
		@Override
		public float getIntegrity()
		{
			return 165f;
		}

		@Override
		public float getDensity()
		{
			return 1f;
		}
	}

	public static class PenetrationHandlerAluminium extends PenetrationHandlerMetal
	{
		@Override
		public float getIntegrity()
		{
			return 75;
		}

		@Override
		public float getDensity()
		{
			return 0.85f;
		}
	}

	public static class PenetrationHandlerGold extends PenetrationHandlerMetal
	{
		@Override
		public float getIntegrity()
		{
			return 165f;
		}

		@Override
		public float getDensity()
		{
			return 0.85f;
		}
	}

	public static class PenetrationHandlerTungsten extends PenetrationHandlerMetal
	{
		@Override
		public float getIntegrity()
		{
			return 235f;
		}

		@Override
		public float getDensity()
		{
			return 1f;
		}
	}

	public static class PenetrationHandlerBronze extends PenetrationHandlerMetal
	{
		@Override
		public float getIntegrity()
		{
			return 250f;
		}

		@Override
		public float getDensity()
		{
			return 1f;
		}
	}

	public static abstract class PenetrationHandlerMetal implements IPenetrationHandler
	{
		@Nullable
		@Override
		public SoundEvent getSpecialSound(HitEffect effect)
		{
			return IISounds.hitMetal.getSpecialSound(effect);
		}

		@Override
		public PenMaterialTypes getPenetrationType()
		{
			return PenMaterialTypes.METAL;
		}
	}

}
