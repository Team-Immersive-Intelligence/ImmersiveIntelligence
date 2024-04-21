package pl.pabilo8.immersiveintelligence.api.rotary;

import blusunrize.immersiveengineering.common.Config.IEConfig.Machines;
import mysticalmechanics.tileentity.TileEntityAxle;
import net.minecraft.tileentity.TileEntity;

import static pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.MechanicalDevices.rofConversionRatio;

/**
 * @author GabrielV (gabriel@iiteam.net)
 * @since 21/04/2024 - 7:53 PM
 */
public class RotaryMath
{
	// Disable the constructor
	private RotaryMath() {}

	/**
	 * Calculate from IE dynamo output to II's rotary unit
	 * @param rotation
	 * @param device
	 * @return
	 */
	public static float[] IEToRoF(double rotation, TileEntity device)
	{
		float torque = RotaryUtils.getTorqueForIEDevice(device, 1);
		int output = (int)(20*Machines.dynamo_output*rotation*rofConversionRatio);
		float speed = output/torque;
		torque = output/speed;

		return new float[] {speed, torque};
	}

	public static double RoFToIE(float energy)
	{
		return (energy/rofConversionRatio/Machines.dynamo_output);
	}

	public static double IEToMM(double rotation)
	{
		return rotation * rofConversionRatio;
	}

	public static double MMToIE(double power)
	{
		return power / rofConversionRatio;
	}

	public static float[] MMToRoF(double power)
	{
		double ii = MMToIE(power);
		return IEToRoF(ii, new TileEntityAxle());
	}
}
