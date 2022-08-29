package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * @author Pabilo8
 * @since 2019-05-24
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".RotaryInput")
@ZenRegister
public class RotaryInputTweaker
{
	@ZenMethod
	public static void addInput(String classPath, float torque)
	{
		try
		{
			Class<?> c = Class.forName(classPath);
			RotaryUtils.ie_rotational_blocks_torque.put(
					(tileEntity -> tileEntity.getClass().equals(c)),
					(aFloat -> aFloat*torque)
			);
			CraftTweakerAPI.getLogger().logInfo("Transmission box will now recognise "+classPath+" as a rotary power source");
		} catch(ClassNotFoundException e)
		{
			CraftTweakerAPI.getLogger().logError("Couldn't add "+classPath+" as a rotary power source, class doesn't exist");
		}
	}

}
