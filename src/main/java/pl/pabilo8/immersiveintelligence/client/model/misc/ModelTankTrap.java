package pl.pabilo8.immersiveintelligence.client.model.misc;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 15-06-2019
 */
public class ModelTankTrap extends ModelIIBase
{
	int textureX = 32;
	int textureY = 32;

	public ModelTankTrap() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[9];

		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY);
		baseModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY);
		baseModel[2] = new ModelRendererTurbo(this, 4, 0, textureX, textureY);
		baseModel[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY);
		baseModel[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY);
		baseModel[5] = new ModelRendererTurbo(this, 4, 0, textureX, textureY);
		baseModel[6] = new ModelRendererTurbo(this, 0, 0, textureX, textureY);
		baseModel[7] = new ModelRendererTurbo(this, 0, 0, textureX, textureY);
		baseModel[8] = new ModelRendererTurbo(this, 4, 0, textureX, textureY);

		baseModel[0].addBox(0.2557F, -16.9843F, 1.1856F, 1, 23, 1);
		baseModel[0].setRotationPoint(-5.0F, -18.0F, -1.0F);
		baseModel[0].setRotationAngle(-0.829F, 1.2217F, 0.0F);

		baseModel[1].addBox(2.2557F, -16.9843F, 1.1856F, 1, 23, 1);
		baseModel[1].setRotationPoint(-5.0F, -18.0F, -1.0F);
		baseModel[1].setRotationAngle(-0.829F, 1.2217F, 0.0F);

		baseModel[2].addBox(0.2557F, -16.9843F, 2.1856F, 3, 23, 1);
		baseModel[2].setRotationPoint(-5.0F, -18.0F, -1.0F);
		baseModel[2].setRotationAngle(-0.829F, 1.2217F, 0.0F);

		baseModel[3].addBox(-1.0F, -20.8126F, 3.98F, 1, 23, 1);
		baseModel[3].setRotationPoint(2.0F, -19.0F, 9.0F);
		baseModel[3].setRotationAngle(-0.6981F, 3.1416F, 0.0F);

		baseModel[4].addBox(1.0F, -20.8126F, 3.98F, 1, 23, 1);
		baseModel[4].setRotationPoint(2.0F, -19.0F, 9.0F);
		baseModel[4].setRotationAngle(-0.6981F, 3.1416F, 0.0F);

		baseModel[5].addBox(-1.0F, -20.8126F, 4.98F, 3, 23, 1);
		baseModel[5].setRotationPoint(2.0F, -19.0F, 9.0F);
		baseModel[5].setRotationAngle(-0.6981F, 3.1416F, 0.0F);

		baseModel[6].addBox(-5.0516F, -20.9144F, 4.0336F, 1, 23, 1);
		baseModel[6].setRotationPoint(11.0F, -19.0F, -4.0F);
		baseModel[6].setRotationAngle(-0.7418F, -0.8727F, 0.0F);

		baseModel[7].addBox(-3.0516F, -20.9144F, 4.0336F, 1, 23, 1);
		baseModel[7].setRotationPoint(11.0F, -19.0F, -4.0F);
		baseModel[7].setRotationAngle(-0.7418F, -0.8727F, 0.0F);

		baseModel[8].addBox(-5.0516F, -20.9144F, 5.0336F, 3, 23, 1);
		baseModel[8].setRotationPoint(11.0F, -19.0F, -4.0F);
		baseModel[8].setRotationAngle(-0.7418F, -0.8727F, 0.0F);

		flipAll();
	}

}
