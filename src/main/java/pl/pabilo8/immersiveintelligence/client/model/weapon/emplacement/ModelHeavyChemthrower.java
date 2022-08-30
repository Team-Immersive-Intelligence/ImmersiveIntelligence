package pl.pabilo8.immersiveintelligence.client.model.weapon.emplacement;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

public class ModelHeavyChemthrower extends ModelIIBase
{
	int textureX = 128;
	int textureY = 128;

	public ModelRendererTurbo[] turretModel, barrelStartModel,barrelMidModel,barrelEndModel;

	public ModelHeavyChemthrower(boolean doOffsets) //Same as Filename
	{
		baseModel = new ModelRendererTurbo[1];
		baseModel[0] = new ModelRendererTurbo(this, 64, 29, textureX, textureY); // Box 2

		baseModel[0].addBox(0F, 0F, 0F, 16, 2, 16, 0F); // Box 2
		baseModel[0].setRotationPoint(8F, -2.0f, 8F);


		turretModel = new ModelRendererTurbo[53];
		turretModel[0] = new ModelRendererTurbo(this, 37, 90, textureX, textureY); // Box 3
		turretModel[1] = new ModelRendererTurbo(this, 64, 66, textureX, textureY); // Box 4
		turretModel[2] = new ModelRendererTurbo(this, 66, 47, textureX, textureY); // Box 5
		turretModel[3] = new ModelRendererTurbo(this, 44, 53, textureX, textureY); // Box 6
		turretModel[4] = new ModelRendererTurbo(this, 41, 98, textureX, textureY); // Box 7
		turretModel[5] = new ModelRendererTurbo(this, 56, 105, textureX, textureY); // Box 9
		turretModel[6] = new ModelRendererTurbo(this, 48, 123, textureX, textureY); // Box 10
		turretModel[7] = new ModelRendererTurbo(this, 35, 119, textureX, textureY); // Box 11
		turretModel[8] = new ModelRendererTurbo(this, 44, 116, textureX, textureY); // Box 12
		turretModel[9] = new ModelRendererTurbo(this, 73, 84, textureX, textureY); // Box 14
		turretModel[10] = new ModelRendererTurbo(this, 26, 67, textureX, textureY); // Box 15
		turretModel[11] = new ModelRendererTurbo(this, 10, 69, textureX, textureY); // Box 16
		turretModel[12] = new ModelRendererTurbo(this, 48, 123, textureX, textureY); // Box 18
		turretModel[13] = new ModelRendererTurbo(this, 44, 107, textureX, textureY); // Box 19
		turretModel[14] = new ModelRendererTurbo(this, 52, 115, textureX, textureY); // Box 20
		turretModel[15] = new ModelRendererTurbo(this, 52, 115, textureX, textureY); // Box 21
		turretModel[16] = new ModelRendererTurbo(this, 43, 119, textureX, textureY); // Box 22
		turretModel[17] = new ModelRendererTurbo(this, 43, 119, textureX, textureY); // Box 23
		turretModel[18] = new ModelRendererTurbo(this, 58, 105, textureX, textureY); // Box 24
		turretModel[19] = new ModelRendererTurbo(this, 67, 47, textureX, textureY); // Box 25
		turretModel[20] = new ModelRendererTurbo(this, 67, 47, textureX, textureY); // Box 28
		turretModel[21] = new ModelRendererTurbo(this, 66, 23, textureX, textureY); // Box 31
		turretModel[22] = new ModelRendererTurbo(this, 92, 10, textureX, textureY); // Box 33
		turretModel[23] = new ModelRendererTurbo(this, 0, 82, textureX, textureY); // Box 56
		turretModel[24] = new ModelRendererTurbo(this, 100, 20, textureX, textureY); // Box 57
		turretModel[25] = new ModelRendererTurbo(this, 24, 105, textureX, textureY); // Box 58
		turretModel[26] = new ModelRendererTurbo(this, 18, 55, textureX, textureY); // Box 59
		turretModel[27] = new ModelRendererTurbo(this, 92, 0, textureX, textureY); // Box 60
		turretModel[28] = new ModelRendererTurbo(this, 76, 95, textureX, textureY); // Box 61
		turretModel[29] = new ModelRendererTurbo(this, 76, 95, textureX, textureY); // Box 62
		turretModel[30] = new ModelRendererTurbo(this, 18, 55, textureX, textureY); // Box 63
		turretModel[31] = new ModelRendererTurbo(this, 0, 105, textureX, textureY); // Box 64
		turretModel[32] = new ModelRendererTurbo(this, 92, 0, textureX, textureY); // Box 65
		turretModel[33] = new ModelRendererTurbo(this, 24, 105, textureX, textureY); // Box 66
		turretModel[34] = new ModelRendererTurbo(this, 100, 20, textureX, textureY); // Box 67
		turretModel[35] = new ModelRendererTurbo(this, 36, 55, textureX, textureY); // Box 68
		turretModel[36] = new ModelRendererTurbo(this, 52, 40, textureX, textureY); // Box 69
		turretModel[37] = new ModelRendererTurbo(this, 43, 72, textureX, textureY); // Box 70
		turretModel[38] = new ModelRendererTurbo(this, 51, 47, textureX, textureY); // Box 71
		turretModel[39] = new ModelRendererTurbo(this, 62, 21, textureX, textureY); // Box 73
		turretModel[40] = new ModelRendererTurbo(this, 62, 21, textureX, textureY); // Box 76
		turretModel[41] = new ModelRendererTurbo(this, 51, 47, textureX, textureY); // Box 77
		turretModel[42] = new ModelRendererTurbo(this, 32, 60, textureX, textureY); // Box 25
		turretModel[43] = new ModelRendererTurbo(this, 32, 60, textureX, textureY); // Box 28
		turretModel[44] = new ModelRendererTurbo(this, 52, 40, textureX, textureY); // Box 68
		turretModel[45] = new ModelRendererTurbo(this, 36, 55, textureX, textureY); // Box 68
		turretModel[46] = new ModelRendererTurbo(this, 66, 23, textureX, textureY); // Box 31
		turretModel[47] = new ModelRendererTurbo(this, 74, 20, textureX, textureY); // Box 68
		turretModel[48] = new ModelRendererTurbo(this, 52, 33, textureX, textureY); // Box 69
		turretModel[49] = new ModelRendererTurbo(this, 43, 72, textureX, textureY); // Box 70
		turretModel[50] = new ModelRendererTurbo(this, 52, 40, textureX, textureY); // Box 68
		turretModel[51] = new ModelRendererTurbo(this, 36, 55, textureX, textureY); // Box 68
		turretModel[52] = new ModelRendererTurbo(this, 39, 4, textureX, textureY); // Box 14

		turretModel[0].addBox(0F, 0F, 0F, 12, 2, 12, 0F); // Box 3
		turretModel[0].setRotationPoint(10F, -4.0f, 10F);

		turretModel[1].addBox(0F, 0F, 0F, 16, 1, 16, 0F); // Box 4
		turretModel[1].setRotationPoint(8F, -5.0f, 8F);

		turretModel[2].addBox(0F, 0F, 0F, 18, 6, 13, 0F); // Box 5
		turretModel[2].setRotationPoint(7F, -17.0f, 11F);

		turretModel[3].addShapeBox(0F, 0F, 0F, 2, 9, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 6
		turretModel[3].setRotationPoint(7F, -20.0f, 7F);

		turretModel[4].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 7
		turretModel[4].setRotationPoint(14.5F, -23.5f, 6F);

		turretModel[5].addShapeBox(0F, 0F, 0F, 3, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 9
		turretModel[5].setRotationPoint(14.5F, -20.0f, 15F);

		turretModel[6].addShapeBox(0F, 0F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 10
		turretModel[6].setRotationPoint(14F, -24.0f, 5F);

		turretModel[7].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 11
		turretModel[7].setRotationPoint(15F, -21.5f, 15.5F);

		turretModel[8].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5f, 0F, 0F, -0.5f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 12
		turretModel[8].setRotationPoint(15F, -22.5f, 15.5F);

		turretModel[9].addBox(0F, 0F, 0F, 18, 3, 5, 0F); // Box 14
		turretModel[9].setRotationPoint(7F, -20.0f, 11F);

		turretModel[10].addShapeBox(0F, 0F, 0F, 2, 9, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 15
		turretModel[10].setRotationPoint(23F, -20.0f, 7F);

		turretModel[11].addShapeBox(0F, 0F, 0F, 4, 9, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		turretModel[11].setRotationPoint(14F, -20.0f, 7F);

		turretModel[12].addShapeBox(0F, 0F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 18
		turretModel[12].setRotationPoint(14F, -24.0f, 7F);

		turretModel[13].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 19
		turretModel[13].setRotationPoint(14F, -24.0f, 12F);

		turretModel[14].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5f, -0.5f, 0F, -0.5f, -0.5f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5f, -0.5f, 0F, -0.5f, -0.5f, 0F); // Box 20
		turretModel[14].setRotationPoint(14.5F, -23.5f, 8F);

		turretModel[15].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5f, -0.5f, 0F, -0.5f, -0.5f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5f, -0.5f, 0F, -0.5f, -0.5f, 0F); // Box 21
		turretModel[15].setRotationPoint(14.5F, -23.5f, 10F);

		turretModel[16].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.5f, -0.5f, 0F, -0.5f, -0.5f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5f, -0.5f, 0F, -0.5f, -0.5f, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 22
		turretModel[16].setRotationPoint(14.5F, -23.5f, 11F);

		turretModel[17].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.5f, -0.5f, 0F, -0.5f, -0.5f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5f, -0.5f, 0F, -0.5f, -0.5f, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 23
		turretModel[17].setRotationPoint(14.5F, -23.5f, 9F);

		turretModel[18].addBox(0F, 0F, 0F, 18, 6, 17, 0F); // Box 24
		turretModel[18].setRotationPoint(7F, -11.0f, 7F);

		turretModel[19].addShapeBox(0F, 0F, 0F, 5, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 25
		turretModel[19].setRotationPoint(18F, -10.5f, 6F);

		turretModel[20].addShapeBox(0F, 0F, 0F, 5, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 28
		turretModel[20].setRotationPoint(9F, -10.5f, 6F);

		turretModel[21].addBox(0F, 0F, 0F, 1, 6, 6, 0F); // Box 31
		turretModel[21].setRotationPoint(6F, -13.0f, 9F);

		turretModel[22].addShapeBox(0F, 0F, 0F, 14, 1, 4, 0F, 0F, -1.0f, 0F, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 33
		turretModel[22].setRotationPoint(9F, -12.0f, 7F);

		turretModel[23].addBox(0F, 0F, 0F, 8, 15, 8, 0F); // Box 56
		turretModel[23].setRotationPoint(20F, -21.0f, 24F);

		turretModel[24].addBox(0F, 0F, 0F, 7, 2, 7, 0F); // Box 57
		turretModel[24].setRotationPoint(20.5F, -6.5f, 24.5F);

		turretModel[25].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // Box 58
		turretModel[25].setRotationPoint(23.5F, -22.0f, 24.5F);

		turretModel[26].addBox(0F, 0F, 0F, 1, 6, 6, 0F); // Box 59
		turretModel[26].setRotationPoint(28F, -13.0f, 25F);

		turretModel[27].addBox(0F, 0F, 0F, 9, 1, 9, 0F); // Box 60
		turretModel[27].setRotationPoint(19.5F, -16.0f, 23.5F);

		turretModel[28].addBox(0F, 0F, 0F, 9, 1, 9, 0F); // Box 61
		turretModel[28].setRotationPoint(19.5F, -7.01f, 23.5F);

		turretModel[29].addBox(0F, 0F, 0F, 9, 1, 9, 0F); // Box 62
		turretModel[29].setRotationPoint(3.5F, -7.01f, 23.5F);

		turretModel[30].addBox(0F, 0F, 0F, 1, 6, 6, 0F); // Box 63
		turretModel[30].setRotationPoint(3F, -13.0f, 25F);

		turretModel[31].addBox(0F, 0F, 0F, 8, 15, 8, 0F); // Box 64
		turretModel[31].setRotationPoint(4F, -21.0f, 24F);

		turretModel[32].addBox(0F, 0F, 0F, 9, 1, 9, 0F); // Box 65
		turretModel[32].setRotationPoint(3.5F, -16.0f, 23.5F);

		turretModel[33].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // Box 66
		turretModel[33].setRotationPoint(4.5F, -22.0f, 24.5F);

		turretModel[34].addBox(0F, 0F, 0F, 7, 2, 7, 0F); // Box 67
		turretModel[34].setRotationPoint(4.5F, -6.5f, 24.5F);

		turretModel[35].addShapeBox(0F, 0F, 0F, 3, 4, 1, 0F, 0F, 0F, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 68
		turretModel[35].setRotationPoint(6F, -8.0f, 10F);
		turretModel[35].rotateAngleZ = 3.14159265F;

		turretModel[36].addShapeBox(0F, 0F, 0F, 3, 4, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 69
		turretModel[36].setRotationPoint(3F, -12.0f, 11F);

		turretModel[37].addShapeBox(0F, 0F, 0F, 4, 4, 13, 0F, -0.25f, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F, -0.575f, 0F, 0F, 0.375F, -0.25f, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F, -0.575f, 0F, 0F, 0.375F); // Box 70
		turretModel[37].setRotationPoint(3F, -12.0f, 13F);
		turretModel[37].rotateAngleY = 0.2268928F;

		turretModel[38].addShapeBox(0F, 0F, 0F, 6, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 71
		turretModel[38].setRotationPoint(1F, -12.5f, 19F);

		turretModel[39].addShapeBox(0F, 0F, 0F, 1, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 73
		turretModel[39].setRotationPoint(2F, -14.0f, 17F);
		turretModel[39].rotateAngleZ = -0.6632251f;

		turretModel[40].addShapeBox(0F, 0F, 0F, 1, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 76
		turretModel[40].setRotationPoint(29F, -14.0f, 17F);
		turretModel[40].rotateAngleZ = 0.66322512F;

		turretModel[41].addShapeBox(0F, 0F, 0F, 6, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 77
		turretModel[41].setRotationPoint(31F, -12.5f, 20F);
		turretModel[41].rotateAngleY = -3.1415927f;

		turretModel[42].addShapeBox(0F, 0F, 0F, 4, 4, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 25
		turretModel[42].setRotationPoint(18.5F, -10.0f, 4F);

		turretModel[43].addShapeBox(0F, 0F, 0F, 4, 4, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 28
		turretModel[43].setRotationPoint(9.5F, -10.0f, 4F);

		turretModel[44].addBox(0F, 0F, 0F, 3, 4, 3, 0F); // Box 68
		turretModel[44].setRotationPoint(0F, -12.0f, 26F);

		turretModel[45].addShapeBox(0F, 0F, 0F, 3, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F); // Box 68
		turretModel[45].setRotationPoint(3F, -8.0f, 29F);
		turretModel[45].rotateAngleZ = 3.14159265F;

		turretModel[46].addBox(0F, 0F, 0F, 1, 6, 6, 0F); // Box 31
		turretModel[46].setRotationPoint(25F, -13.0f, 9F);

		turretModel[47].addShapeBox(0F, 0F, 0F, 3, 4, 1, 0F, 0F, 0F, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 68
		turretModel[47].setRotationPoint(26F, -12.0f, 10F);

		turretModel[48].addShapeBox(0F, 0F, 0F, 3, 4, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 69
		turretModel[48].setRotationPoint(26F, -12.0f, 11F);

		turretModel[49].addShapeBox(0F, 0F, 0F, 4, 4, 13, 0F, 0F, 0F, 0F, -0.25f, 0F, -1.0f, 0F, 0F, 0.375F, 0F, 0F, -0.575f, 0F, 0F, 0F, -0.25f, 0F, -1.0f, 0F, 0F, 0.375F, 0F, 0F, -0.575f); // Box 70
		turretModel[49].setRotationPoint(25.13F, -12.0f, 13.85F);
		turretModel[49].rotateAngleY = -0.2268928f;

		turretModel[50].addBox(0F, 0F, 0F, 3, 4, 3, 0F); // Box 68
		turretModel[50].setRotationPoint(29F, -12.0f, 26F);

		turretModel[51].addShapeBox(0F, 0F, 0F, 3, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F); // Box 68
		turretModel[51].setRotationPoint(29F, -12.0f, 29F);

		turretModel[52].addShapeBox(0F, 0F, 0F, 18, 3, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -3.0f, 0F, 0F, -3.0f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 14
		turretModel[52].setRotationPoint(7F, -20.0f, 16F);


		barrelStartModel = new ModelRendererTurbo[4];
		barrelStartModel[0] = new ModelRendererTurbo(this, 43, 53, textureX, textureY); // GUN101
		barrelStartModel[1] = new ModelRendererTurbo(this, 64, 35, textureX, textureY); // GUN102
		barrelStartModel[2] = new ModelRendererTurbo(this, 43, 53, textureX, textureY); // GUN103
		barrelStartModel[3] = new ModelRendererTurbo(this, 64, 35, textureX, textureY); // GUN104

		barrelStartModel[0].addShapeBox(-1.5f, -1.5f, 0F, 5, 5, 13, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN101
		barrelStartModel[0].setRotationPoint(10.5F, -15.0f, -1.0f);

		barrelStartModel[1].addShapeBox(-2.5f, -3.5f, 0F, 5, 7, 3, 0F, 0F, -1.0f, 0F, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1.0f, 0F, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN102
		barrelStartModel[1].setRotationPoint(11.5F, -14.5f, 9F);

		barrelStartModel[2].addShapeBox(-1.5f, -1.5f, 0F, 5, 5, 13, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN103
		barrelStartModel[2].setRotationPoint(19.5F, -15.0f, -1.0f);

		barrelStartModel[3].addShapeBox(-2.5f, -3.5f, 0F, 5, 7, 3, 0F, 0F, -1.0f, 0F, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1.0f, 0F, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN104
		barrelStartModel[3].setRotationPoint(20.5F, -14.5f, 9F);


		barrelMidModel = new ModelRendererTurbo[2];
		barrelMidModel[0] = new ModelRendererTurbo(this, 74, 16, textureX, textureY); // GUN201
		barrelMidModel[1] = new ModelRendererTurbo(this, 74, 16, textureX, textureY); // GUN202

		barrelMidModel[0].addShapeBox(-1.5f, -1.5f, 0F, 4, 4, 9, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN201
		barrelMidModel[0].setRotationPoint(11F, -14.5f, -10.0f);

		barrelMidModel[1].addShapeBox(-1.5f, -1.5f, 0F, 4, 4, 9, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN202
		barrelMidModel[1].setRotationPoint(20F, -14.5f, -10.0f);


		barrelEndModel = new ModelRendererTurbo[6];
		barrelEndModel[0] = new ModelRendererTurbo(this, 51, 110, textureX, textureY); // GUN301
		barrelEndModel[1] = new ModelRendererTurbo(this, 30, 72, textureX, textureY); // GUN304
		barrelEndModel[2] = new ModelRendererTurbo(this, 51, 110, textureX, textureY); // GUN309
		barrelEndModel[3] = new ModelRendererTurbo(this, 30, 72, textureX, textureY); // GUN312
		barrelEndModel[4] = new ModelRendererTurbo(this, 76, 4, textureX, textureY); // GUN304
		barrelEndModel[5] = new ModelRendererTurbo(this, 76, 4, textureX, textureY); // GUN304

		barrelEndModel[0].addShapeBox(-1.5f, -1.5f, 0F, 3, 3, 9, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN301
		barrelEndModel[0].setRotationPoint(11.5F, -14.0f, -19.0f);

		barrelEndModel[1].addShapeBox(-1.5f, -1.5f, 0F, 5, 5, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN304
		barrelEndModel[1].setRotationPoint(10.5F, -15.0f, -27.0f);

		barrelEndModel[2].addShapeBox(-1.5f, -1.5f, 0F, 3, 3, 9, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN309
		barrelEndModel[2].setRotationPoint(20.5F, -14.0f, -19.0f);

		barrelEndModel[3].addShapeBox(-1.5f, -1.5f, 0F, 5, 5, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN312
		barrelEndModel[3].setRotationPoint(19.5F, -15.0f, -27.0f);

		barrelEndModel[4].addShapeBox(-1.5f, -1.5f, 0F, 3, 3, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN304
		barrelEndModel[4].setRotationPoint(11.5F, -14.0f, -26.0f);

		barrelEndModel[5].addShapeBox(-1.5f, -1.5f, 0F, 3, 3, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN304
		barrelEndModel[5].setRotationPoint(20.5F, -14.0f, -26.0f);

		parts.put("base",  baseModel);
		parts.put("turret",turretModel);
		parts.put("barrel_start", barrelStartModel);
		parts.put("barrel_mid", barrelMidModel);
		parts.put("barrel_end", barrelEndModel);

		translateAll(-16,0,-16);
		if(doOffsets)
		{
			translate(barrelStartModel, -4.5f,14F, 3.0f);
			translate(barrelMidModel, -4.5f,14F, 3.0f);
			translate(barrelEndModel, -4.5f,14F, 3.0f);
		}

		flipAll();
	}
}
