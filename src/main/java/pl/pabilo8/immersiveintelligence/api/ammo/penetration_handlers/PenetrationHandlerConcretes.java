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
public class PenetrationHandlerConcretes
{
	public static class PenetrationHandlerConcrete implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 345f;
		}

		@Override
		public float getReduction()
		{
			return 1f;
		}

		@Override
		public PenMaterialTypes getPenetrationType()
		{
			return PenMaterialTypes.SOLID;
		}

		@Nullable
		@Override
		public SoundEvent getSpecialSound(HitEffect effect)
		{
			return IISounds.hitStone.getSpecialSound(effect);
		}
	}

	public static class PenetrationHandlerLeadedConcrete implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 540f;
		}

		@Override
		public float getReduction()
		{
			return 1.55f;
		}

		@Override
		public PenMaterialTypes getPenetrationType()
		{
			return PenMaterialTypes.SOLID;
		}

		@Nullable
		@Override
		public SoundEvent getSpecialSound(HitEffect effect)
		{
			return IISounds.hitStone.getSpecialSound(effect);
		}
	}

	public static class PenetrationHandlerConcreteBricks implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 400f;
		}

		@Override
		public float getReduction()
		{
			return 1.25f;
		}

		@Override
		public PenMaterialTypes getPenetrationType()
		{
			return PenMaterialTypes.SOLID;
		}

		@Nullable
		@Override
		public SoundEvent getSpecialSound(HitEffect effect)
		{
			return IISounds.hitStone.getSpecialSound(effect);
		}
	}

	public static class PenetrationHandlerPanzerConcrete implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 540f;
		}

		@Override
		public float getReduction()
		{
			return 1.75f;
		}

		@Override
		public PenMaterialTypes getPenetrationType()
		{
			return PenMaterialTypes.SOLID;
		}

		@Nullable
		@Override
		public SoundEvent getSpecialSound(HitEffect effect)
		{
			return IISounds.hitStone.getSpecialSound(effect);
		}
	}

	public static class PenetrationHandlerUberConcrete implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 700f;
		}

		@Override
		public float getReduction()
		{
			return 2.5f;
		}

		@Override
		public PenMaterialTypes getPenetrationType()
		{
			return PenMaterialTypes.SOLID;
		}

		@Nullable
		@Override
		public SoundEvent getSpecialSound(HitEffect effect)
		{
			return IISounds.hitStone.getSpecialSound(effect);
		}
	}
}