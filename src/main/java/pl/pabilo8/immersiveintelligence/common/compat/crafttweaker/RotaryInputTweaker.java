package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * Created by Pabilo8 on 2019-05-24.
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".RotaryInput")
@ZenRegister
public class RotaryInputTweaker
{

	@ZenMethod
	public static void addInput(String classPath, float torqueMod)
	{
		try
		{
			Class c = Class.forName(classPath);
			RotaryUtils.ie_rotational_blocks_torque.put(
					(tileEntity -> tileEntity.getClass().equals(c)),
					(aFloat -> aFloat*torqueMod)
			);
		} catch(ClassNotFoundException e)
		{

		}
	}

}
