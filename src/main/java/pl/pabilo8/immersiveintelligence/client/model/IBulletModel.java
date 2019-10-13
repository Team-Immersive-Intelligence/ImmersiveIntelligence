package pl.pabilo8.immersiveintelligence.client.model;

/**
 * Created by Pabilo8 on 04-10-2019.
 */
public interface IBulletModel
{

	void renderBullet(int coreColour, int paintColour);

	void renderCasing(boolean core, int coreColour, float gunpowderPercentage);

	void renderCore(int coreColour);

	void getConveyorOffset();
}
