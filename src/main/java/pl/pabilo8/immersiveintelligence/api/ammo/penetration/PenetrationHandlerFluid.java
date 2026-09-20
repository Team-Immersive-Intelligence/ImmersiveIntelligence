package pl.pabilo8.immersiveintelligence.api.ammo.penetration;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.Fluid;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;

/**
 * Defines penetration behavior for fluid blocks.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.08.2026
 * @since 23.08.2026
 */
public class PenetrationHandlerFluid extends PenetrationHandler
{
	public PenetrationHandlerFluid(Fluid fluid)
	{
		super(PenetrationHardness.FOLIAGE, MathHelper.clamp(fluid.getDensity()/1000f*50f, 0, 100), 0f,
				null, getParticle(fluid), getParticle(fluid),
				fluid.getTemperature() > 400?SoundEvents.BLOCK_LAVA_EXTINGUISH: SoundEvents.ENTITY_GENERIC_SPLASH,
				SoundEvents.ENTITY_GENERIC_SPLASH,
				null, false);
	}

	private static final String getParticle(Fluid fluid)
	{
		return fluid.getDensity() > 1500?"debris/water_dense_hit": "debris/water_hit";
	}
}
