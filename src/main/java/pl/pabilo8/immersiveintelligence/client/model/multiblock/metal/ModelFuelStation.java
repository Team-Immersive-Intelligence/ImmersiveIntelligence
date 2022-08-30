package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 06.04.2021
 */
public class ModelFuelStation extends ModelIIBase
{
	int textureX = 256;
	int textureY = 64;

	public ModelFuelStation() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[43];
		baseModel[0] = new ModelRendererTurbo(this, 8, 13, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 8, 13, textureX, textureY); // Box 0
		baseModel[2] = new ModelRendererTurbo(this, 8, 13, textureX, textureY); // Box 0
		baseModel[3] = new ModelRendererTurbo(this, 8, 13, textureX, textureY); // Box 0
		baseModel[4] = new ModelRendererTurbo(this, 160, 0, textureX, textureY); // Box 0
		baseModel[5] = new ModelRendererTurbo(this, 84, 0, textureX, textureY); // Box 0
		baseModel[6] = new ModelRendererTurbo(this, 130, 20, textureX, textureY); // Box 0
		baseModel[7] = new ModelRendererTurbo(this, 130, 20, textureX, textureY); // Box 0
		baseModel[8] = new ModelRendererTurbo(this, 120, 0, textureX, textureY); // Box 0
		baseModel[9] = new ModelRendererTurbo(this, 132, 17, textureX, textureY); // Box 0
		baseModel[10] = new ModelRendererTurbo(this, 120, 0, textureX, textureY); // Box 0
		baseModel[11] = new ModelRendererTurbo(this, 120, 6, textureX, textureY); // Box 0
		baseModel[12] = new ModelRendererTurbo(this, 120, 6, textureX, textureY); // Box 0
		baseModel[13] = new ModelRendererTurbo(this, 124, 50, textureX, textureY); // Box 0
		baseModel[14] = new ModelRendererTurbo(this, 102, 13, textureX, textureY); // Box 0
		baseModel[15] = new ModelRendererTurbo(this, 144, 0, textureX, textureY); // Box 0
		baseModel[16] = new ModelRendererTurbo(this, 208, 19, textureX, textureY); // Box 0
		baseModel[17] = new ModelRendererTurbo(this, 20, 2, textureX, textureY); // Box 0
		baseModel[18] = new ModelRendererTurbo(this, 20, 5, textureX, textureY); // Box 0
		baseModel[19] = new ModelRendererTurbo(this, 112, 28, textureX, textureY); // Box 0
		baseModel[20] = new ModelRendererTurbo(this, 112, 49, textureX, textureY); // Box 0
		baseModel[21] = new ModelRendererTurbo(this, 112, 55, textureX, textureY); // Box 0
		baseModel[22] = new ModelRendererTurbo(this, 121, 45, textureX, textureY); // Box 0
		baseModel[23] = new ModelRendererTurbo(this, 124, 50, textureX, textureY); // Box 0
		baseModel[24] = new ModelRendererTurbo(this, 112, 49, textureX, textureY); // Box 0
		baseModel[25] = new ModelRendererTurbo(this, 112, 55, textureX, textureY); // Box 0
		baseModel[26] = new ModelRendererTurbo(this, 121, 45, textureX, textureY); // Box 0
		baseModel[27] = new ModelRendererTurbo(this, 0, 20, textureX, textureY); // Box 0
		baseModel[28] = new ModelRendererTurbo(this, 0, 20, textureX, textureY); // Box 0
		baseModel[29] = new ModelRendererTurbo(this, 0, 20, textureX, textureY); // Box 0
		baseModel[30] = new ModelRendererTurbo(this, 0, 20, textureX, textureY); // Box 0
		baseModel[31] = new ModelRendererTurbo(this, 20, 20, textureX, textureY); // Box 0
		baseModel[32] = new ModelRendererTurbo(this, 20, 20, textureX, textureY); // Box 0
		baseModel[33] = new ModelRendererTurbo(this, 20, 20, textureX, textureY); // Box 0
		baseModel[34] = new ModelRendererTurbo(this, 20, 20, textureX, textureY); // Box 0
		baseModel[35] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[36] = new ModelRendererTurbo(this, 126, 7, textureX, textureY); // Box 0
		baseModel[37] = new ModelRendererTurbo(this, 112, 31, textureX, textureY); // Box 0
		baseModel[38] = new ModelRendererTurbo(this, 126, 28, textureX, textureY); // Box 0
		baseModel[39] = new ModelRendererTurbo(this, 17, 8, textureX, textureY); // Box 0
		baseModel[40] = new ModelRendererTurbo(this, 112, 44, textureX, textureY); // Box 0
		baseModel[41] = new ModelRendererTurbo(this, 112, 22, textureX, textureY); // Box 0
		baseModel[42] = new ModelRendererTurbo(this, 208, 33, textureX, textureY); // Box 0

		baseModel[0].addBox(0F, 0F, 0F, 3, 12, 3, 0F); // Box 0
		baseModel[0].setRotationPoint(1F, -12F, 1F);

		baseModel[1].addBox(0F, 0F, 0F, 3, 12, 3, 0F); // Box 0
		baseModel[1].setRotationPoint(1F, -12F, 12F);

		baseModel[2].addBox(0F, 0F, 0F, 3, 12, 3, 0F); // Box 0
		baseModel[2].setRotationPoint(28F, -12F, 1F);

		baseModel[3].addBox(0F, 0F, 0F, 3, 12, 3, 0F); // Box 0
		baseModel[3].setRotationPoint(28F, -12F, 12F);

		baseModel[4].addBox(0F, 0F, 0F, 32, 3, 16, 0F); // Box 0
		baseModel[4].setRotationPoint(0F, -15F, 0F);

		baseModel[5].addBox(0F, 0F, 0F, 12, 1, 12, 0F); // Box 0
		baseModel[5].setRotationPoint(18F, -16F, 2F);

		baseModel[6].addBox(0F, 0F, 0F, 1, 3, 4, 0F); // Box 0
		baseModel[6].setRotationPoint(18F, -20F, 3F);

		baseModel[7].addBox(0F, 0F, 0F, 1, 3, 4, 0F); // Box 0
		baseModel[7].setRotationPoint(18F, -20F, 9F);

		baseModel[8].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[8].setRotationPoint(15F, -19.5F, 9.5F);

		baseModel[9].addBox(0F, 0F, 0F, 2, 1, 10, 0F); // Box 0
		baseModel[9].setRotationPoint(18F, -17F, 3F);

		baseModel[10].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[10].setRotationPoint(15F, -19.5F, 3.5F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 3, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[11].setRotationPoint(15F, -16.5F, 9.5F);

		baseModel[12].addShapeBox(0F, 0F, 0F, 3, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[12].setRotationPoint(15F, -16.5F, 3.5F);

		baseModel[13].addBox(0F, 0F, 0F, 6, 3, 3, 0F); // Box 0
		baseModel[13].setRotationPoint(9F, -17.5F, 3.5F);

		baseModel[14].addBox(0F, 0F, 0F, 8, 1, 8, 0F); // Box 0
		baseModel[14].setRotationPoint(4F, -32F, 4F);

		baseModel[15].addBox(0F, 0F, 0F, 8, 8, 8, 0F); // Box 0
		baseModel[15].setRotationPoint(4F, -30F, 4F);

		baseModel[16].addBox(0F, 0F, 0F, 12, 2, 12, 0F); // Box 0
		baseModel[16].setRotationPoint(2F, -22F, 2F);

		baseModel[17].addBox(0F, 0F, 0F, 2, 1, 2, 0F); // Box 0
		baseModel[17].setRotationPoint(7F, -31F, 9F);

		baseModel[18].addBox(0F, 0F, 0F, 2, 1, 2, 0F); // Box 0
		baseModel[18].setRotationPoint(7F, -31F, 5F);

		baseModel[19].addBox(0F, 0F, 0F, 32, 4, 32, 0F); // Box 0
		baseModel[19].setRotationPoint(0F, -4F, 16F);

		baseModel[20].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // Box 0
		baseModel[20].setRotationPoint(6F, -17.5F, 3.5F);

		baseModel[21].addBox(0F, 0F, 0F, 3, 2, 3, 0F); // Box 0
		baseModel[21].setRotationPoint(6F, -19.5F, 3.5F);

		baseModel[22].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // Box 0
		baseModel[22].setRotationPoint(5.5F, -20.5F, 3F);

		baseModel[23].addBox(0F, 0F, 0F, 6, 3, 3, 0F); // Box 0
		baseModel[23].setRotationPoint(9F, -17.5F, 9.5F);

		baseModel[24].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // Box 0
		baseModel[24].setRotationPoint(6F, -17.5F, 9.5F);

		baseModel[25].addBox(0F, 0F, 0F, 3, 2, 3, 0F); // Box 0
		baseModel[25].setRotationPoint(6F, -19.5F, 9.5F);

		baseModel[26].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // Box 0
		baseModel[26].setRotationPoint(5.5F, -20.5F, 9F);

		baseModel[27].addBox(0F, 0F, -2F, 2, 6, 2, 0F); // Box 0
		baseModel[27].setRotationPoint(3F, -20F, 4F);
		baseModel[27].rotateAngleX = -0.20943951F;

		baseModel[28].addBox(0F, 0F, -2F, 2, 6, 2, 0F); // Box 0
		baseModel[28].setRotationPoint(11F, -20F, 4F);
		baseModel[28].rotateAngleX = -0.20943951F;

		baseModel[29].addBox(0F, 0F, 2F, 2, 6, 2, 0F); // Box 0
		baseModel[29].setRotationPoint(3F, -20F, 10F);
		baseModel[29].rotateAngleX = 0.20943951F;

		baseModel[30].addBox(0F, 0F, 2F, 2, 6, 2, 0F); // Box 0
		baseModel[30].setRotationPoint(11F, -20F, 10F);
		baseModel[30].rotateAngleX = 0.20943951F;

		baseModel[31].addBox(0F, 0F, 0F, 2, 6, 2, 0F); // Box 0
		baseModel[31].setRotationPoint(2F, -10F, 20F);

		baseModel[32].addBox(0F, 0F, 0F, 2, 6, 2, 0F); // Box 0
		baseModel[32].setRotationPoint(2F, -10F, 44F);

		baseModel[33].addBox(0F, 0F, 0F, 2, 6, 2, 0F); // Box 0
		baseModel[33].setRotationPoint(26F, -10F, 20F);

		baseModel[34].addBox(0F, 0F, 0F, 2, 6, 2, 0F); // Box 0
		baseModel[34].setRotationPoint(26F, -10F, 44F);

		baseModel[35].addBox(0F, 0F, 0F, 28, 36, 28, 0F); // Box 0
		baseModel[35].setRotationPoint(1F, -46F, 19F);

		baseModel[36].addBox(0F, 0F, 0F, 2, 6, 6, 0F); // Box 0
		baseModel[36].setRotationPoint(29F, -12F, 30F);

		baseModel[37].addShapeBox(0F, 0F, 0F, 3, 3, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // Box 0
		baseModel[37].setRotationPoint(26F, -10F, 31F);

		baseModel[38].addBox(0F, 0F, 0F, 1, 8, 8, 0F); // Box 0
		baseModel[38].setRotationPoint(0F, -28F, 36F);

		baseModel[39].addBox(0F, 0F, 0F, 1, 3, 4, 0F); // Box 0
		baseModel[39].setRotationPoint(0F, -31F, 38F);

		baseModel[40].addBox(0F, 0F, 0F, 4, 3, 1, 0F); // Box 0
		baseModel[40].setRotationPoint(6F, -31F, 47F);

		baseModel[41].addBox(0F, 0F, 0F, 8, 8, 1, 0F); // Box 0
		baseModel[41].setRotationPoint(4F, -28F, 47F);

		baseModel[42].addBox(0F, 0F, 0F, 1, 10, 16, 0F); // Box 0
		baseModel[42].setRotationPoint(29F, -36F, 25F);

		flipAll();
	}
}
