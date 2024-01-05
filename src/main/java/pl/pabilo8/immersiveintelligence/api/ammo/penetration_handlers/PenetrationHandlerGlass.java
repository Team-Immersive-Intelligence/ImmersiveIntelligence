package pl.pabilo8.immersiveintelligence.api.ammo.penetration_handlers;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import pl.pabilo8.immersiveintelligence.api.ammo.IIPenetrationRegistry.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.HitEffect;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenMaterialTypes;

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
	public float getReduction()
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
