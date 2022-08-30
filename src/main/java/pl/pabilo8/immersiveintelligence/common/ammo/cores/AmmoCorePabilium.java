package pl.pabilo8.immersiveintelligence.common.ammo.cores;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoCore;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class AmmoCorePabilium implements IAmmoCore
{
	@Override
	public String getName()
	{
		return "core_pabilium";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("nuggetPabilium");
	}

	@Override
	public float getDensity()
	{
		return 0.15f;
	}

	@Override
	public float getDamageModifier()
	{
		return 4f;
	}

	@Override
	public float getExplosionModifier()
	{
		return 6.5f;
	}

	@Override
	public float getPenetrationHardness()
	{
		return 999;
	}

	@Override
	public int getColour()
	{
		//Weird stuff here
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT)
		{
			float add = (Minecraft.getMinecraft().world.getTotalWorldTime()%60f)/60f;
			add = add > 0.5?1f-((add-0.5f)*2f): add*2f;
			return MathHelper.hsvToRGB(110f/255f, 0.75f*add, (0.5f+((1f-add)*0.45f)));
		}
		else
			return MathHelper.hsvToRGB(121f/255f, 0.75f, 0.88f);
	}
}
