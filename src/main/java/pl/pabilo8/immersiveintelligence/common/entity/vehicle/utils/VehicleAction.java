package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleControls.MouseBinding;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * Represents an action for a vehicle, like shooting a gun or opening a hatch or door.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 29.05.2026
 */
@SideOnly(Side.CLIENT)
public class VehicleAction
{
	private final String name;
	private final ResLoc icon;
	private final KeyBinding keyBinding;
	private final MouseBinding mouseBinding;

	public VehicleAction(String name, ResLoc icon, KeyBinding keyBinding)
	{
		this.name = name;
		this.icon = icon;
		this.keyBinding = keyBinding;
		this.mouseBinding = null;
	}

	public VehicleAction(String name, ResLoc icon, MouseBinding mouseBinding)
	{
		this.name = name;
		this.icon = icon;
		this.keyBinding = null;
		this.mouseBinding = mouseBinding;
	}

	//--- Getters ---//

	public String getName()
	{
		return name;
	}

	public ResLoc getIcon()
	{
		return icon;
	}

	public KeyBinding getKeyBinding()
	{
		return keyBinding;
	}

	public MouseBinding getMouseBinding()
	{
		return mouseBinding;
	}
}
