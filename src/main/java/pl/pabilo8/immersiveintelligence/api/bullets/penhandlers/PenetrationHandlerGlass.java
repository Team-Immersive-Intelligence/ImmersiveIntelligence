package pl.pabilo8.immersiveintelligence.api.bullets.penhandlers;

import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.PenMaterialTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.IPenetrationHandler;

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
}
