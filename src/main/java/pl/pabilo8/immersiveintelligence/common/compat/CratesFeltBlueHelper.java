package pl.pabilo8.immersiveintelligence.common.compat;

import pl.pabilo8.immersiveintelligence.common.IILogger;

/**
 * @author Avalon (avalon@iiteam.net)
 * @since 03.03.2026
 */
public class CratesFeltBlueHelper extends IICompatModule
{
	@Override
	public String getName()
	{
		return "cfb";
	}

	@Override
	public void preInit()
	{

	}

	@Override
	public void registerRecipes()
	{

	}

	@Override
	public void init()
	{
		IILogger.info("Crates Felt Blue detected, crates will drop their items individually when broken");
		cfb = true;
	}

	@Override
	public void postInit()
	{

	}
}

