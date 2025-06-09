package pl.pabilo8.immersiveintelligence.api.data.device;

import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 01.06.2019
 */
public interface IDataDevice
{
	/**
	 * Called when received a packet.
	 *
	 * @param packet which has been received
	 */
	void onReceive(DataPacket packet, @Nullable EnumFacing side);
}
