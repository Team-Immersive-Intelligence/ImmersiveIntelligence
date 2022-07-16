package pl.pabilo8.immersiveintelligence.common.compat;

import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;

/**
 * @author Pabilo8
 * @since 27.07.2021
 */
public class ImmersiveEnergyHelper extends IICompatModule
{
	@Override
	public void preInit()
	{
		ShrapnelHandler.addShrapnel("thorium", 0x867b75, "immersive_energy:textures/blocks/sheetmetal_thorium", 7, 0.45f, 8f);
		// TODO: 07.01.2022 Wait for The Swede to finish the battery (I'm too busy with II) ^^
	}

	@Override
	public void registerRecipes()
	{

	}

	@Override
	public void init()
	{

	}

	@Override
	public void postInit()
	{

	}
}
