package pl.pabilo8.immersiveintelligence.common;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

/**
 * Initialized in {@link pl.pabilo8.immersiveintelligence.ImmersiveIntelligence#preInit}
 *
 * @author Pabilo8
 * @since 30.08.2022
 */
public class IILogger
{
	public static boolean debug = false;
	public static Logger logger;

	public static void log(Level logLevel, Object object)
	{
		logger.log(logLevel, String.valueOf(object));
	}

	public static void error(Object object)
	{
		log(Level.ERROR, object);
	}

	public static void info(Object object)
	{
		log(Level.INFO, object);
	}

	public static void warn(Object object)
	{
		log(Level.WARN, object);
	}

	public static void error(String message, Object... params)
	{
		logger.log(Level.ERROR, message, params);
	}

	public static void info(String message, Object... params)
	{
		logger.log(Level.INFO, message, params);
	}

	public static void warn(String message, Object... params)
	{
		logger.log(Level.WARN, message, params);
	}
}
