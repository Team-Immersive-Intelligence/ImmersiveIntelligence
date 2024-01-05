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
public class PenetrationHandlerClay implements IPenetrationHandler
{
	@Override
	public float getIntegrity()
	{
		return 175f;
	}

	@Override
	public float getReduction()
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
