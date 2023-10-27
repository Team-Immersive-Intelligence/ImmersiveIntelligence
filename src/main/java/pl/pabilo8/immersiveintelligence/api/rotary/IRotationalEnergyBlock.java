package pl.pabilo8.immersiveintelligence.api.rotary;

/**
 * @author Pabilo8
 * @since 02-07-2019
 */
public interface IRotationalEnergyBlock
{
	void updateRotationStorage(float rpm, float torque, int part);
}
