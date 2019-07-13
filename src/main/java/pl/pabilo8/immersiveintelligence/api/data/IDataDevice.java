package pl.pabilo8.immersiveintelligence.api.data;

/**
 * Created by Pabilo8 on 2019-06-01.
 */
public interface IDataDevice
{
	void onReceive(DataPacket packet);

	void onSend();
}
