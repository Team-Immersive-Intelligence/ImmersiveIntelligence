package pl.pabilo8.immersiveintelligence.api.data.radio;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;

/**
 * Created by Pabilo8 on 23-06-2019.
 */
public interface IRadioDevice
{
	void onRadioSend(DataPacket packet);

	void onRadioReceive(DataPacket packet);

	int getFrequency();

	void setFrequency(int value);

	boolean isBasicRadio();

	float getRange();

	float getWeatherRangeDecrease();
}
