package pl.pabilo8.immersiveintelligence.api.rotary;

import blusunrize.immersiveengineering.common.Config.IEConfig.Machines;
import mysticalmechanics.tileentity.TileEntityAxle;
import net.minecraft.tileentity.TileEntity;

import static pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.MechanicalDevices.rofConversionRatio;

/**
 * @author GabrielV (gabriel@iiteam.net)
 * @since 21.04.2024
 */
public class IIRotaryMath
{
	// Disable the constructor
	private IIRotaryMath()
	{
	}

	/**
	 * Calculate from IE dynamo output to II's rotary units
	 *
	 * @param rotation the rotation value of the IE dynamo
	 * @param device   the IE device to calculate the torque for
	 * @return an array containing the speed and torque in II's rotary units
	 */
	public static float[] IEToII(double rotation, TileEntity device)
	{
		float torque = IIRotaryUtils.getTorqueForIEDevice(device, 1);
		int output = (int)(20*Machines.dynamo_output*rotation*rofConversionRatio);
		float speed = output/torque;
		torque = output/speed;

		return new float[]{speed, torque};
	}

	public static double IIToIE(float energy)
	{
		return (energy/rofConversionRatio/Machines.dynamo_output);
	}

	public static double IEToMM(double rotation)
	{
		return rotation*rofConversionRatio;
	}

	public static double MMToIE(double power)
	{
		return power/rofConversionRatio;
	}

	public static float[] MMToII(double power)
	{
		double ii = MMToIE(power);
		return IEToII(ii, new TileEntityAxle());
	}
}
