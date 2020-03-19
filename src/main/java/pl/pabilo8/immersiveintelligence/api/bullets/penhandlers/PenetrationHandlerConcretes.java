package pl.pabilo8.immersiveintelligence.api.bullets.penhandlers;

import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.IPenetrationHandler;

/**
 * Created by Pabilo8 on 06-03-2020.
 */
public class PenetrationHandlerConcretes
{
	public static class PenetrationHandlerConcrete implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 345f;
		}

		@Override
		public float getDensity()
		{
			return 1f;
		}
	}

	public static class PenetrationHandlerLeadedConcrete implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 540f;
		}

		@Override
		public float getDensity()
		{
			return 1.55f;
		}
	}
}
