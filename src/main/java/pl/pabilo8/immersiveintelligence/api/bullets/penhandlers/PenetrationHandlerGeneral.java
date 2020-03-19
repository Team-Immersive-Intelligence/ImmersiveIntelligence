package pl.pabilo8.immersiveintelligence.api.bullets.penhandlers;

import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.IPenetrationHandler;

/**
 * Created by Pabilo8 on 06-03-2020.
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
}
