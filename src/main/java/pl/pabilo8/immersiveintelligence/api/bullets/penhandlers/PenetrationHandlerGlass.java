package pl.pabilo8.immersiveintelligence.api.bullets.penhandlers;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.PenMaterialTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.HitEffect;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.IPenetrationHandler;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 06-03-2020
 */
public class PenetrationHandlerGlass implements IPenetrationHandler
{
	@Override
	public float getIntegrity()
	{
		return 4f;
	}

	@Override
	public float getDensity()
	{
		return 1f;
	}

	@Override
	public PenMaterialTypes getPenetrationType()
	{
		return PenMaterialTypes.LIGHT;
	}

	@Nullable
	@Override
	public SoundEvent getSpecialSound(HitEffect effect)
	{
		return SoundEvents.BLOCK_GLASS_BREAK;
	}
}
