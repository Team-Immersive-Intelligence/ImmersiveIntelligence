package pl.pabilo8.immersiveintelligence.api.ammo.penetration_handlers;

import net.minecraft.util.SoundEvent;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.HitEffect;
import pl.pabilo8.immersiveintelligence.common.IISounds;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 06-03-2020
 */
public class PenetrationHandlerGrass extends PenetrationHandlerDirt
{
	@Nullable
	@Override
	public SoundEvent getSpecialSound(HitEffect effect)
	{
		return IISounds.hitGrass.getSpecialSound(effect);
	}
}
