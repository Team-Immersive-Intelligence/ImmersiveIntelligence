package pl.pabilo8.immersiveintelligence.api.bullets.penhandlers;

import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.IPenetrationHandler;

/**
 * Created by Pabilo8 on 06-03-2020.
 */
public class PenetrationHandlerMetals
{
	public static class PenetrationHandlerIron implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 295f;
		}

		@Override
		public float getDensity()
		{
			return 1.25f;
		}
	}

	public static class PenetrationHandlerSteel implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 365f;
		}

		@Override
		public float getDensity()
		{
			return 1.25f;
		}
	}

	public static class PenetrationHandlerCopper implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 165f;
		}

		@Override
		public float getDensity()
		{
			return 1f;
		}
	}

	public static class PenetrationHandlerAluminium implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 75;
		}

		@Override
		public float getDensity()
		{
			return 0.85f;
		}
	}

	public static class PenetrationHandlerGold implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 165f;
		}

		@Override
		public float getDensity()
		{
			return 0.85f;
		}
	}

	public static class PenetrationHandlerTungsten implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 235f;
		}

		@Override
		public float getDensity()
		{
			return 1f;
		}
	}

	public static class PenetrationHandlerBronze implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 250f;
		}

		@Override
		public float getDensity()
		{
			return 1f;
		}
	}

	public class PenetrationHandlerMisc implements IPenetrationHandler
	{
		@Override
		public float getIntegrity()
		{
			return 275f;
		}

		@Override
		public float getDensity()
		{
			return 0.85f;
		}
	}

}
