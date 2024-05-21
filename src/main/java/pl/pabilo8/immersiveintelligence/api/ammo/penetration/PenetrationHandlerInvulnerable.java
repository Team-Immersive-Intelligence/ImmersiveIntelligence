package pl.pabilo8.immersiveintelligence.api.ammo.penetration;

import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;

/**
 * For cases like bedrock and fluid blocks
 *
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 29.03.2024
 */
public class PenetrationHandlerInvulnerable extends PenetrationHandler
{
	public PenetrationHandlerInvulnerable(PenetrationHardness hardness, float thickness)
	{
		super(hardness, thickness, 1f, null, null, null);
	}

	@Override
	public boolean canBeDamaged()
	{
		return false;
	}
}
