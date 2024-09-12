package pl.pabilo8.immersiveintelligence.common.compat;

import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8
 * @since 27.07.2021
 */
public class ImmersiveEnergyHelper extends IICompatModule
{
	@Override
	public void preInit()
	{
		ShrapnelHandler.addShrapnel("thorium", IIColor.fromPackedRGB(0x867b75), ResLoc.of(new ResourceLocation("immersive_energy:textures/blocks/sheetmetal_thorium")), 7, 0.45f, 8f);
		// TODO: 07.01.2022 Wait for The Swede to finish the battery (I'm too busy with II) ^^
	}

	@Override
	public String getName()
	{
		return "ImmersiveEnergy";
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
