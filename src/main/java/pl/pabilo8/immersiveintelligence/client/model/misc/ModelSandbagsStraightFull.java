package pl.pabilo8.immersiveintelligence.client.model.misc;

import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * Created by Pabilo8 on 15-06-2019.
 */
public class ModelSandbagsStraightFull extends ModelSandbagsStraight
{
	int textureX = 64;
	int textureY = 64;

	public ModelSandbagsStraightFull() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[45];
		baseModel[0] = new ModelRendererTurbo(this, 10, 21, textureX, textureY); // SandbagMain
		baseModel[1] = new ModelRendererTurbo(this, 0, 21, textureX, textureY); // SandbagSide
		baseModel[2] = new ModelRendererTurbo(this, 0, 21, textureX, textureY); // SandbagSide
		baseModel[3] = new ModelRendererTurbo(this, 10, 7, textureX, textureY); // SandbagMain
		baseModel[4] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // SandbagSide
		baseModel[5] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // SandbagSide
		baseModel[6] = new ModelRendererTurbo(this, 10, 14, textureX, textureY); // SandbagMain
		baseModel[7] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // SandbagSide
		baseModel[8] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // SandbagSide
		baseModel[9] = new ModelRendererTurbo(this, 10, 21, textureX, textureY); // SandbagMain
		baseModel[10] = new ModelRendererTurbo(this, 0, 21, textureX, textureY); // SandbagSide
		baseModel[11] = new ModelRendererTurbo(this, 0, 21, textureX, textureY); // SandbagSide
		baseModel[12] = new ModelRendererTurbo(this, 10, 7, textureX, textureY); // SandbagMain
		baseModel[13] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // SandbagSide
		baseModel[14] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // SandbagSide
		baseModel[15] = new ModelRendererTurbo(this, 10, 21, textureX, textureY); // SandbagMain
		baseModel[16] = new ModelRendererTurbo(this, 0, 21, textureX, textureY); // SandbagSide
		baseModel[17] = new ModelRendererTurbo(this, 0, 21, textureX, textureY); // SandbagSide
		baseModel[18] = new ModelRendererTurbo(this, 10, 7, textureX, textureY); // SandbagMain
		baseModel[19] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // SandbagSide
		baseModel[20] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // SandbagSide
		baseModel[21] = new ModelRendererTurbo(this, 10, 7, textureX, textureY); // SandbagMain
		baseModel[22] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // SandbagSide
		baseModel[23] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // SandbagSide
		baseModel[24] = new ModelRendererTurbo(this, 10, 14, textureX, textureY); // SandbagMain
		baseModel[25] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // SandbagSide
		baseModel[26] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // SandbagSide
		baseModel[27] = new ModelRendererTurbo(this, 10, 7, textureX, textureY); // SandbagMain
		baseModel[28] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // SandbagSide
		baseModel[29] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // SandbagSide
		baseModel[30] = new ModelRendererTurbo(this, 10, 14, textureX, textureY); // SandbagMain
		baseModel[31] = new ModelRendererTurbo(this, 10, 14, textureX, textureY); // SandbagSide
		baseModel[32] = new ModelRendererTurbo(this, 10, 14, textureX, textureY); // SandbagSide
		baseModel[33] = new ModelRendererTurbo(this, 10, 21, textureX, textureY); // SandbagMain
		baseModel[34] = new ModelRendererTurbo(this, 0, 21, textureX, textureY); // SandbagSide
		baseModel[35] = new ModelRendererTurbo(this, 0, 21, textureX, textureY); // SandbagSide

		baseModel[36] = new ModelRendererTurbo(this, 40, 20, textureX, textureY); // Box 62
		baseModel[37] = new ModelRendererTurbo(this, 30, 20, textureX, textureY); // Box 66
		baseModel[38] = new ModelRendererTurbo(this, 30, 20, textureX, textureY); // Box 67
		baseModel[39] = new ModelRendererTurbo(this, 30, 4, textureX, textureY); // Box 70
		baseModel[40] = new ModelRendererTurbo(this, 40, 4, textureX, textureY); // Box 71
		baseModel[41] = new ModelRendererTurbo(this, 30, 4, textureX, textureY); // Box 72
		baseModel[42] = new ModelRendererTurbo(this, 30, 20, textureX, textureY); // Box 73
		baseModel[43] = new ModelRendererTurbo(this, 40, 20, textureX, textureY); // Box 74
		baseModel[44] = new ModelRendererTurbo(this, 30, 20, textureX, textureY); // Box 75

		baseModel[0].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMain
		baseModel[0].setRotationPoint(5F, -3F, 0F);

		baseModel[1].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagSide
		baseModel[1].setRotationPoint(11F, -3F, 0F);

		baseModel[2].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagSide
		baseModel[2].setRotationPoint(4F, -3F, 0F);

		baseModel[3].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMain
		baseModel[3].setRotationPoint(9F, -6F, 0F);

		baseModel[4].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagSide
		baseModel[4].setRotationPoint(15F, -6F, 0F);

		baseModel[5].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagSide
		baseModel[5].setRotationPoint(8F, -6F, 0F);

		baseModel[6].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMain
		baseModel[6].setRotationPoint(1F, -6F, 0F);

		baseModel[7].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagSide
		baseModel[7].setRotationPoint(7F, -6F, 0F);

		baseModel[8].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagSide
		baseModel[8].setRotationPoint(0F, -6F, 0F);

		baseModel[9].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMain
		baseModel[9].setRotationPoint(9F, -3F, 4F);

		baseModel[10].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagSide
		baseModel[10].setRotationPoint(15F, -3F, 4F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagSide
		baseModel[11].setRotationPoint(8F, -3F, 4F);

		baseModel[12].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMain
		baseModel[12].setRotationPoint(1F, -3F, 4F);

		baseModel[13].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagSide
		baseModel[13].setRotationPoint(7F, -3F, 4F);

		baseModel[14].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagSide
		baseModel[14].setRotationPoint(0F, -3F, 4F);

		baseModel[15].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMain
		baseModel[15].setRotationPoint(9F, -9F, 4F);

		baseModel[16].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagSide
		baseModel[16].setRotationPoint(15F, -9F, 4F);

		baseModel[17].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagSide
		baseModel[17].setRotationPoint(8F, -9F, 4F);

		baseModel[18].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMain
		baseModel[18].setRotationPoint(1F, -9F, 4F);

		baseModel[19].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagSide
		baseModel[19].setRotationPoint(7F, -9F, 4F);

		baseModel[20].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagSide
		baseModel[20].setRotationPoint(0F, -9F, 4F);

		baseModel[21].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMain
		baseModel[21].setRotationPoint(9F, -12F, 0F);

		baseModel[22].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagSide
		baseModel[22].setRotationPoint(15F, -12F, 0F);

		baseModel[23].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagSide
		baseModel[23].setRotationPoint(8F, -12F, 0F);

		baseModel[24].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMain
		baseModel[24].setRotationPoint(1F, -12F, 0F);

		baseModel[25].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagSide
		baseModel[25].setRotationPoint(7F, -12F, 0F);

		baseModel[26].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagSide
		baseModel[26].setRotationPoint(0F, -12F, 0F);

		baseModel[27].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMain
		baseModel[27].setRotationPoint(5F, -9F, 0F);

		baseModel[28].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagSide
		baseModel[28].setRotationPoint(11F, -9F, 0F);

		baseModel[29].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagSide
		baseModel[29].setRotationPoint(4F, -9F, 0F);

		baseModel[30].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMain
		baseModel[30].setRotationPoint(5F, -6F, 4F);

		baseModel[31].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagSide
		baseModel[31].setRotationPoint(11F, -6F, 4F);

		baseModel[32].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagSide
		baseModel[32].setRotationPoint(4F, -6F, 4F);

		baseModel[33].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMain
		baseModel[33].setRotationPoint(5F, -12F, 4F);

		baseModel[34].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagSide
		baseModel[34].setRotationPoint(11F, -12F, 4F);

		baseModel[35].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagSide
		baseModel[35].setRotationPoint(4F, -12F, 4F);

		baseModel[36].addShapeBox(0F, 0F, 0F, 6, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 62
		baseModel[36].setRotationPoint(5F, -16F, 0F);

		baseModel[37].addShapeBox(0F, 0F, 0F, 1, 4, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // Box 66
		baseModel[37].setRotationPoint(11F, -16F, 0F);

		baseModel[38].addShapeBox(0F, 0F, 0F, 1, 4, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // Box 67
		baseModel[38].setRotationPoint(4F, -16F, 0F);

		baseModel[39].addShapeBox(0F, 0F, 0F, 1, 4, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // Box 70
		baseModel[39].setRotationPoint(0F, -16F, 4F);

		baseModel[40].addShapeBox(0F, 0F, 0F, 6, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 71
		baseModel[40].setRotationPoint(1F, -16F, 4F);

		baseModel[41].addShapeBox(0F, 0F, 0F, 1, 4, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // Box 72
		baseModel[41].setRotationPoint(7F, -16F, 4F);

		baseModel[42].addShapeBox(0F, 0F, 0F, 1, 4, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // Box 73
		baseModel[42].setRotationPoint(8F, -16F, 4F);

		baseModel[43].addShapeBox(0F, 0F, 0F, 6, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 74
		baseModel[43].setRotationPoint(9F, -16F, 4F);

		baseModel[44].addShapeBox(0F, 0F, 0F, 1, 4, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // Box 75
		baseModel[44].setRotationPoint(15F, -16F, 4F);

		rightModel = new ModelRendererTurbo[15];
		rightModel[0] = new ModelRendererTurbo(this, 10, 7, textureX, textureY); // SandbagMainAddonRight
		rightModel[1] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // SandbagMainAddonRight
		rightModel[2] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // SandbagMainAddonRight
		rightModel[3] = new ModelRendererTurbo(this, 10, 21, textureX, textureY); // SandbagMainAddonRight
		rightModel[4] = new ModelRendererTurbo(this, 0, 21, textureX, textureY); // SandbagMainAddonRight
		rightModel[5] = new ModelRendererTurbo(this, 0, 21, textureX, textureY); // SandbagMainAddonRight
		rightModel[6] = new ModelRendererTurbo(this, 10, 21, textureX, textureY); // SandbagMainAddonRight
		rightModel[7] = new ModelRendererTurbo(this, 10, 21, textureX, textureY); // SandbagMainAddonRight
		rightModel[8] = new ModelRendererTurbo(this, 10, 21, textureX, textureY); // SandbagMainAddonRight
		rightModel[9] = new ModelRendererTurbo(this, 10, 7, textureX, textureY); // SandbagMainAddonRight
		rightModel[10] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // SandbagMainAddonRight
		rightModel[11] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // SandbagMainAddonRight
		rightModel[12] = new ModelRendererTurbo(this, 40, 12, textureX, textureY); // Box 61
		rightModel[13] = new ModelRendererTurbo(this, 30, 12, textureX, textureY); // Box 68
		rightModel[14] = new ModelRendererTurbo(this, 30, 12, textureX, textureY); // Box 69

		rightModel[0].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMainAddonRight
		rightModel[0].setRotationPoint(-3F, -3F, 0F);

		rightModel[1].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagMainAddonRight
		rightModel[1].setRotationPoint(3F, -3F, 0F);

		rightModel[2].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagMainAddonRight
		rightModel[2].setRotationPoint(-4F, -3F, 0F);

		rightModel[3].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMainAddonRight
		rightModel[3].setRotationPoint(-3F, -9F, 0F);

		rightModel[4].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagMainAddonRight
		rightModel[4].setRotationPoint(3F, -9F, 0F);

		rightModel[5].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagMainAddonRight
		rightModel[5].setRotationPoint(-4F, -9F, 0F);

		rightModel[6].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMainAddonRight
		rightModel[6].setRotationPoint(-3F, -6F, 4F);

		rightModel[7].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagMainAddonRight
		rightModel[7].setRotationPoint(3F, -6F, 4F);

		rightModel[8].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagMainAddonRight
		rightModel[8].setRotationPoint(-4F, -6F, 4F);

		rightModel[9].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMainAddonRight
		rightModel[9].setRotationPoint(-3F, -12F, 4F);

		rightModel[10].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagMainAddonRight
		rightModel[10].setRotationPoint(3F, -12F, 4F);

		rightModel[11].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagMainAddonRight
		rightModel[11].setRotationPoint(-4F, -12F, 4F);

		rightModel[12].addShapeBox(0F, 0F, 0F, 6, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 61
		rightModel[12].setRotationPoint(-3F, -16F, 0F);

		rightModel[13].addShapeBox(0F, 0F, 0F, 1, 4, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // Box 68
		rightModel[13].setRotationPoint(3F, -16F, 0F);

		rightModel[14].addShapeBox(0F, 0F, 0F, 1, 4, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // Box 69
		rightModel[14].setRotationPoint(-4F, -16F, 0F);

		leftModel = new ModelRendererTurbo[15];
		leftModel[0] = new ModelRendererTurbo(this, 10, 7, textureX, textureY); // SandbagMainAddonLeft
		leftModel[1] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // SandbagMainAddonLeft
		leftModel[2] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // SandbagMainAddonLeft
		leftModel[3] = new ModelRendererTurbo(this, 10, 14, textureX, textureY); // SandbagMainAddonLeft
		leftModel[4] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // SandbagMainAddonLeft
		leftModel[5] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // SandbagMainAddonLeft
		leftModel[6] = new ModelRendererTurbo(this, 10, 7, textureX, textureY); // SandbagMainAddonLeft
		leftModel[7] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // SandbagMainAddonLeft
		leftModel[8] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // SandbagMainAddonLeft
		leftModel[9] = new ModelRendererTurbo(this, 10, 14, textureX, textureY); // SandbagMainAddonLeft
		leftModel[10] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // SandbagMainAddonLeft
		leftModel[11] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // SandbagMainAddonLeft

		leftModel[12] = new ModelRendererTurbo(this, 40, 4, textureX, textureY); // Box 63
		leftModel[13] = new ModelRendererTurbo(this, 30, 4, textureX, textureY); // Box 64
		leftModel[14] = new ModelRendererTurbo(this, 30, 4, textureX, textureY); // Box 65

		leftModel[0].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMainAddonLeft
		leftModel[0].setRotationPoint(13F, -3F, 0F);

		leftModel[1].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagMainAddonLeft
		leftModel[1].setRotationPoint(19F, -3F, 0F);

		leftModel[2].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagMainAddonLeft
		leftModel[2].setRotationPoint(12F, -3F, 0F);

		leftModel[3].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMainAddonLeft
		leftModel[3].setRotationPoint(13F, -9F, 0F);

		leftModel[4].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagMainAddonLeft
		leftModel[4].setRotationPoint(19F, -9F, 0F);

		leftModel[5].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagMainAddonLeft
		leftModel[5].setRotationPoint(12F, -9F, 0F);

		leftModel[6].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMainAddonLeft
		leftModel[6].setRotationPoint(13F, -6F, 4F);

		leftModel[7].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagMainAddonLeft
		leftModel[7].setRotationPoint(19F, -6F, 4F);

		leftModel[8].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagMainAddonLeft
		leftModel[8].setRotationPoint(12F, -6F, 4F);

		leftModel[9].addBox(0F, 0F, 0F, 6, 3, 4, 0F); // SandbagMainAddonLeft
		leftModel[9].setRotationPoint(13F, -12F, 4F);

		leftModel[10].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // SandbagMainAddonLeft
		leftModel[10].setRotationPoint(19F, -12F, 4F);

		leftModel[11].addShapeBox(0F, 0F, 0F, 1, 3, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // SandbagMainAddonLeft
		leftModel[11].setRotationPoint(12F, -12F, 4F);

		leftModel[12].addShapeBox(0F, 0F, 0F, 6, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 63
		leftModel[12].setRotationPoint(13F, -16F, 0F);

		leftModel[13].addShapeBox(1F, 0F, 0F, 1, 4, 4, 0F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F); // Box 64
		leftModel[13].setRotationPoint(11F, -16F, 0F);

		leftModel[14].addShapeBox(0F, 0F, 0F, 1, 4, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F); // Box 65
		leftModel[14].setRotationPoint(19F, -16F, 0F);

		parts.put("base", baseModel);
		parts.put("right", rightModel);
		parts.put("left", leftModel);
		flipAll();
	}

}
