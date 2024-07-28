package pl.pabilo8.immersiveintelligence.api.ammo.penetration;

import pl.pabilo8.immersiveintelligence.api.ammo.enums.HitEffect;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticles;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * @author Pabilo8
 * @since 27.03.2024
 */
public class PenetrationHandlerMetal extends PenetrationHandler
{
	private static final HashMap<String, PenetrationHandlerMetal> REGISTRY = new HashMap<>();
	private final String name;

	private PenetrationHandlerMetal(String name, PenetrationHardness hardness, float thickness, float integrity)
	{
		super(hardness, thickness, integrity, IIParticles.PARTICLE_DEBRIS_METAL,
				IISounds.hitMetal.getSpecialSound(HitEffect.IMPACT),
				IISounds.hitMetal.getSpecialSound(HitEffect.RICOCHET));
		this.name = IIStringUtil.toCamelCase(name.toLowerCase(), false);
	}


	public String getName()
	{
		return name;
	}

	/**
	 * Gets the penetration handler for the given metal name. If the penetration handler does not exist, it will be created.
	 *
	 * @param name      The name of the penetration handler
	 * @param hardness  The hardness of the material
	 * @param thickness The thickness of the material
	 * @param integrity The integrity of the material
	 * @return The penetration handler
	 */
	@Nonnull
	public static PenetrationHandlerMetal create(String name, PenetrationHardness hardness, float thickness, float integrity)
	{
		return REGISTRY.computeIfAbsent(name,
				n -> new PenetrationHandlerMetal(name, hardness, thickness, integrity));
	}

	/**
	 * Gets the penetration handler for the given metal name.
	 *
	 * @param name The name of the metal
	 * @return The penetration handler, or null if it does not exist
	 */
	@Nullable
	public static PenetrationHandlerMetal get(String name)
	{
		return REGISTRY.get(name);
	}
}
