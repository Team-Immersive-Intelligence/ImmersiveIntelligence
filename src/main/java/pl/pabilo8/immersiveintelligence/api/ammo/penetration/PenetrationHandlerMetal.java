package pl.pabilo8.immersiveintelligence.api.ammo.penetration;

import lombok.Getter;
import pl.pabilo8.immersiveintelligence.api.ammo.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.HitEffect;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * Stores and caches penetration properties for registered metals.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.08.2026
 * @since 27.03.2024
 */
public class PenetrationHandlerMetal extends PenetrationHandler
{
	private static final HashMap<String, PenetrationHandlerMetal> REGISTRY = new HashMap<>();
	@Getter
	private final String name;

	private PenetrationHandlerMetal(String name, PenetrationHardness hardness, float thickness, float integrity)
	{
		super(hardness, thickness, integrity,
				PenetrationRegistry.PARTICLE_DEBRIS_METAL, "debris/metal_hit", "debris/metal_ricochet",
				IISounds.hitMetal.getSpecialSound(HitEffect.IMPACT),
				IISounds.hitMetal.getSpecialSound(HitEffect.RICOCHET),
				null, true);
		this.name = IIStringUtil.toCamelCase(name.toLowerCase(), false);
	}

	/**
	 * Gets or creates a penetration handler for the given metal.
	 *
	 * @param name      metal name
	 * @param hardness  material hardness
	 * @param thickness material thickness
	 * @param integrity material integrity
	 * @return metal penetration handler
	 */
	@Nonnull
	public static PenetrationHandlerMetal create(String name, PenetrationHardness hardness, float thickness, float integrity)
	{
		return REGISTRY.computeIfAbsent(name,
				n -> new PenetrationHandlerMetal(name, hardness, thickness, integrity));
	}

	/**
	 * Gets the penetration handler for the given metal.
	 *
	 * @param name metal name
	 * @return penetration handler, or {@code null} if it does not exist
	 */
	@Nullable
	public static PenetrationHandlerMetal get(String name)
	{
		return REGISTRY.get(name);
	}
}
