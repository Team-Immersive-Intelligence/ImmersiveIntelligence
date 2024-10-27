package pl.pabilo8.immersiveintelligence.api.data.pol;

import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 17.04.2022
 */
public abstract class POLTerminal
{
	/**
	 * Used to log compilation error
	 */
	public abstract void error(String text);

	/**
	 * Prints out text onto the terminal
	 */
	public abstract void type(String text);

	/**
	 * Changes state of one of 16 colored lamps
	 */
	public abstract void lamp(int lamp, int color, boolean state);

	/**
	 * @param section section/block of the device hub
	 * @param id      id of the device
	 * @return the device
	 */
	@Nullable
	public abstract IDataDevice getDeviceAt(int section, int id);

	public abstract void sleep(int value);
}
