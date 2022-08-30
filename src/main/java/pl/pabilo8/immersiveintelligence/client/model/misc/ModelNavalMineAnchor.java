package pl.pabilo8.immersiveintelligence.client.model.misc;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 13.03.2021
 */
public class ModelNavalMineAnchor extends ModelIIBase
{
	int textureX=64;
	int textureY=32;

	public ModelNavalMineAnchor()
	{
		baseModel = new ModelRendererTurbo[13];
		baseModel[0] = new ModelRendererTurbo(this, 0, 6, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 0, 6, textureX, textureY); // Box 1
		baseModel[2] = new ModelRendererTurbo(this, 0, 6, textureX, textureY); // Box 3
		baseModel[3] = new ModelRendererTurbo(this, 0, 6, textureX, textureY); // Box 5
		baseModel[4] = new ModelRendererTurbo(this, 0, 6, textureX, textureY); // Box 6
		baseModel[5] = new ModelRendererTurbo(this, 19, 0, textureX, textureY); // Box 7
		baseModel[6] = new ModelRendererTurbo(this, 8, 2, textureX, textureY); // Box 8
		baseModel[7] = new ModelRendererTurbo(this, 6, 9, textureX, textureY); // Box 9
		baseModel[8] = new ModelRendererTurbo(this, 8, 2, textureX, textureY); // Box 10
		baseModel[9] = new ModelRendererTurbo(this, 6, 9, textureX, textureY); // Box 11
		baseModel[10] = new ModelRendererTurbo(this, 48, 0, textureX, textureY); // Box 12
		baseModel[11] = new ModelRendererTurbo(this, 31, 0, textureX, textureY); // Box 13
		baseModel[12] = new ModelRendererTurbo(this, 48, 7, textureX, textureY); // Box 14

		baseModel[0].addShapeBox(0F, 0F, 0F, 16, 10, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, -3F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 2, 4, 2, 0F); // Box 1
		baseModel[1].setRotationPoint(6F, 7F, 2F);
		baseModel[1].rotateAngleZ = -1.57079633F;

		baseModel[2].addBox(0F, 0F, 0F, 2, 4, 2, 0F); // Box 3
		baseModel[2].setRotationPoint(14F, 7F, 2F);
		baseModel[2].rotateAngleZ = -1.57079633F;

		baseModel[3].addBox(0F, 0F, 0F, 2, 4, 2, 0F); // Box 5
		baseModel[3].setRotationPoint(6F, 7F, 12F);
		baseModel[3].rotateAngleZ = -1.57079633F;

		baseModel[4].addBox(0F, 0F, 0F, 2, 4, 2, 0F); // Box 6
		baseModel[4].setRotationPoint(14F, 7F, 12F);
		baseModel[4].rotateAngleZ = -1.57079633F;

		baseModel[5].addBox(0F, 0F, 0F, 5, 5, 1, 0F); // Box 7
		baseModel[5].setRotationPoint(5.5F, -3F, 5.5F);
		baseModel[5].rotateAngleX = 1.57079633F;

		baseModel[6].addShapeBox(0F, 0F, 0F, 2, 5, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 8
		baseModel[6].setRotationPoint(-1F, -7F, 11F);
		baseModel[6].rotateAngleZ = 0.27925268F;

		baseModel[7].addShapeBox(0F, 0F, 0F, 2, 10, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 9
		baseModel[7].setRotationPoint(16F, -10F, 10F);
		baseModel[7].rotateAngleZ = -0.20943951F;

		baseModel[8].addShapeBox(0F, 0F, 0F, 2, 5, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 10
		baseModel[8].setRotationPoint(-1F, -7F, 3F);
		baseModel[8].rotateAngleZ = 0.27925268F;

		baseModel[9].addShapeBox(0F, 0F, 0F, 2, 10, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 11
		baseModel[9].setRotationPoint(16F, -10F, 3F);
		baseModel[9].rotateAngleZ = -0.20943951F;

		baseModel[10].addShapeBox(0F, 0F, 0F, 3, 4, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 12
		baseModel[10].setRotationPoint(-3.8F, 1F, 6F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 6, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 13
		baseModel[11].setRotationPoint(0F, 0F, 4.5F);
		baseModel[11].rotateAngleY = -1.57079633F;

		baseModel[12].addShapeBox(0F, 0F, 0F, 4, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 14
		baseModel[12].setRotationPoint(-3F, 6F, 1F);
		baseModel[12].rotateAngleX = 1.57079633F;

		flipAll();
		translateAll(-8,8,8);
	}
}
