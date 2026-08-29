package pl.pabilo8.immersiveintelligence.api.ammo.penetration;

import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;

/**
 * Defines penetration behavior for blocks that cannot be damaged, like Bedrock.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.08.2026
 * @ii-approved 0.3.1
 * @since 29.03.2024
 */
public class PenetrationHandlerInvulnerable extends PenetrationHandler
{
	public PenetrationHandlerInvulnerable()
	{
		super(PenetrationHardness.BEDROCK, Integer.MAX_VALUE, 1f,
				null, null, null,
				null, null,
				null, false);
	}
}
