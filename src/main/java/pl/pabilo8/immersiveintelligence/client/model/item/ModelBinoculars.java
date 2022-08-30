package pl.pabilo8.immersiveintelligence.client.model.item;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 20.01.2021
 */
public class ModelBinoculars extends ModelIIBase
{
	int textureX = 16;
	int textureY = 16;

	public ModelBinoculars() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[6];
		baseModel[0] = new ModelRendererTurbo(this, 8, 6, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 8, 6, textureX, textureY); // Box 0
		baseModel[2] = new ModelRendererTurbo(this, 4, 10, textureX, textureY); // Box 0
		baseModel[3] = new ModelRendererTurbo(this, 4, 10, textureX, textureY); // Box 0
		baseModel[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[5] = new ModelRendererTurbo(this, 0, 8, textureX, textureY); // Box 0

		baseModel[0].addBox(1F, -5F, -6F, 2, 2, 2, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, 0F, 0F);

		baseModel[1].addBox(-3F, -5F, -6F, 2, 2, 2, 0F); // Box 0
		baseModel[1].setRotationPoint(0F, 0F, 0F);

		baseModel[2].addShapeBox(0F, -6F, -10F, 4, 4, 2, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F); // Box 0
		baseModel[2].setRotationPoint(0F, 0F, 0F);

		baseModel[3].addShapeBox(-4F, -6F, -10F, 4, 4, 2, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F); // Box 0
		baseModel[3].setRotationPoint(0F, 0F, 0F);

		baseModel[4].addBox(-2.5F, -5F, -8F, 6, 2, 2, 0F); // Box 0
		baseModel[4].setRotationPoint(0F, 0F, 0F);

		baseModel[5].addBox(-3.5F, -5F, -8F, 1, 2, 2, 0F); // Box 0
		baseModel[5].setRotationPoint(0F, 0F, 0F);


	}
}
