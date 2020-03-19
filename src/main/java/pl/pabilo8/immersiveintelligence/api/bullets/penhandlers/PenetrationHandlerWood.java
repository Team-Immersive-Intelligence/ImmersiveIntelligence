package pl.pabilo8.immersiveintelligence.api.bullets.penhandlers;

import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.IPenetrationHandler;

/**
 * Created by Pabilo8 on 06-03-2020.
 */
public class PenetrationHandlerWood
{
	public static class PenetrationHandlerLog implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 165f;
		}

		@Override
		public float getDensity()
		{
			return 0.95f;
		}
	}

	public static class PenetrationHandlerPlanks implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 75f;
		}

		@Override
		public float getDensity()
		{
			return 0.65f;
		}
	}
}
