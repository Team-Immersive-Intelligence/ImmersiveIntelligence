package pl.pabilo8.immersiveintelligence.api.bullets.penhandlers;

import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.PenMaterialTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.IPenetrationHandler;

/**
 * @author Pabilo8
 * @since 06-03-2020
 */
public class PenetrationHandlerGeneral implements IPenetrationHandler
{
	@Override
	public float getIntegrity()
	{
		return 150f;
	}

	@Override
	public float getDensity()
	{
		return 0.75f;
	}

	@Override
	public PenMaterialTypes getPenetrationType()
	{
		return PenMaterialTypes.SOLID;
	}
}
