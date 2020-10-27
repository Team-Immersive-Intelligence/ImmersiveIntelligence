package pl.pabilo8.immersiveintelligence.client.model.metal_device;

import pl.pabilo8.immersiveintelligence.client.model.ModelBlockBase;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 30.08.2020
 */
public class ModelPunchtapeReader extends ModelBlockBase
{
	int textureX = 64;
	int textureY = 32;

	public ModelPunchtapeReader()
	{
		baseModel = new ModelRendererTurbo[11];
		baseModel[0] = new ModelRendererTurbo(this, 0, 2, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 40, 0, textureX, textureY); // Box 1
		baseModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 1
		baseModel[3] = new ModelRendererTurbo(this, 40, 0, textureX, textureY); // Box 1
		baseModel[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 1
		baseModel[5] = new ModelRendererTurbo(this, 6, 2, textureX, textureY); // Box 1
		baseModel[6] = new ModelRendererTurbo(this, 10, 2, textureX, textureY); // Box 1
		baseModel[7] = new ModelRendererTurbo(this, 6, 2, textureX, textureY); // Box 1
		baseModel[8] = new ModelRendererTurbo(this, 10, 2, textureX, textureY); // Box 1
		baseModel[9] = new ModelRendererTurbo(this, 18, 0, textureX, textureY); // Box 1
		baseModel[10] = new ModelRendererTurbo(this, 18, 0, textureX, textureY); // Box 1

		baseModel[0].addBox(0F, 0F, 0F, 16, 16, 14, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, -16F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 10, 1, 1, 0F); // Box 1
		baseModel[1].setRotationPoint(3F, -12F, 14F);

		baseModel[2].addBox(0F, 0F, 0F, 8, 1, 1, 0F); // Box 1
		baseModel[2].setRotationPoint(4F, -14F, 14F);

		baseModel[3].addBox(0F, 0F, 0F, 10, 1, 1, 0F); // Box 1
		baseModel[3].setRotationPoint(3F, -6F, 14F);

		baseModel[4].addBox(0F, 0F, 0F, 8, 1, 1, 0F); // Box 1
		baseModel[4].setRotationPoint(4F, -8F, 14F);

		baseModel[5].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		baseModel[5].setRotationPoint(12F, -8F, 14F);

		baseModel[6].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		baseModel[6].setRotationPoint(12F, -14F, 14F);

		baseModel[7].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		baseModel[7].setRotationPoint(3F, -8F, 14F);

		baseModel[8].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		baseModel[8].setRotationPoint(3F, -14F, 14F);

		baseModel[9].addBox(0F, 0F, 0F, 10, 1, 1, 0F); // Box 1
		baseModel[9].setRotationPoint(3F, -12F, 15F);

		baseModel[10].addBox(0F, 0F, 0F, 10, 1, 1, 0F); // Box 1
		baseModel[10].setRotationPoint(3F, -6F, 15F);


		translateAll(0F, 0F, 0F);


		flipAll();
	}
}
