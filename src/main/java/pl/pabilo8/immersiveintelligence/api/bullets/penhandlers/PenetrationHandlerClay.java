package pl.pabilo8.immersiveintelligence.api.bullets.penhandlers;

import net.minecraft.util.SoundEvent;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.PenMaterialTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.HitEffect;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.common.IISounds;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 06-03-2020
 */
public class PenetrationHandlerClay implements IPenetrationHandler
{
	@Override
	public float getIntegrity()
	{
		return 175f;
	}

	@Override
	public float getDensity()
	{
		return 0.75f;
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
		return IISounds.hitDirt.getSpecialSound(effect);
	}
}
