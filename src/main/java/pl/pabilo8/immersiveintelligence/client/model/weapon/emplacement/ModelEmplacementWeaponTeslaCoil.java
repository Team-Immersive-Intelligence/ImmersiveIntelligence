package pl.pabilo8.immersiveintelligence.client.model.weapon.emplacement;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

import java.util.Arrays;

public class ModelEmplacementWeaponTeslaCoil extends ModelIIBase
{
	int textureX = 256;
	int textureY = 128;

	public ModelEmplacementWeaponTeslaCoil(boolean doOffsets) //Same as Filename
	{
		baseModel = new ModelRendererTurbo[61];
		baseModel[0] = new ModelRendererTurbo(this, 0, 62, textureX, textureY); // Box 3
		baseModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 4
		baseModel[2] = new ModelRendererTurbo(this, 163, 33, textureX, textureY); // Box 5
		baseModel[3] = new ModelRendererTurbo(this, 48, 15, textureX, textureY); // Box 6
		baseModel[4] = new ModelRendererTurbo(this, 74, 44, textureX, textureY); // Box 7
		baseModel[5] = new ModelRendererTurbo(this, 98, 63, textureX, textureY); // Box 8
		baseModel[6] = new ModelRendererTurbo(this, 163, 49, textureX, textureY); // Box 9
		baseModel[7] = new ModelRendererTurbo(this, 96, 23, textureX, textureY); // Box 11
		baseModel[8] = new ModelRendererTurbo(this, 98, 55, textureX, textureY); // Box 13
		baseModel[9] = new ModelRendererTurbo(this, 74, 27, textureX, textureY); // Box 14
		baseModel[10] = new ModelRendererTurbo(this, 154, 0, textureX, textureY); // Box 15
		baseModel[11] = new ModelRendererTurbo(this, 3, 13, textureX, textureY); // Box 16
		baseModel[12] = new ModelRendererTurbo(this, 2, 62, textureX, textureY); // Box 17
		baseModel[13] = new ModelRendererTurbo(this, 130, 23, textureX, textureY); // Box 18
		baseModel[14] = new ModelRendererTurbo(this, 6, 47, textureX, textureY); // Box 20
		baseModel[15] = new ModelRendererTurbo(this, 40, 44, textureX, textureY); // Box 23
		baseModel[16] = new ModelRendererTurbo(this, 40, 44, textureX, textureY); // Box 25
		baseModel[17] = new ModelRendererTurbo(this, 146, 70, textureX, textureY); // Box 27
		baseModel[18] = new ModelRendererTurbo(this, 146, 70, textureX, textureY); // Box 28
		baseModel[19] = new ModelRendererTurbo(this, 82, 32, textureX, textureY); // Box 29
		baseModel[20] = new ModelRendererTurbo(this, 78, 28, textureX, textureY); // Box 30
		baseModel[21] = new ModelRendererTurbo(this, 74, 24, textureX, textureY); // Box 31
		baseModel[22] = new ModelRendererTurbo(this, 14, 13, textureX, textureY); // NAMEPLATE01
		baseModel[23] = new ModelRendererTurbo(this, 114, 0, textureX, textureY); // Box 36
		baseModel[24] = new ModelRendererTurbo(this, 96, 8, textureX, textureY); // Box 37
		baseModel[25] = new ModelRendererTurbo(this, 121, 16, textureX, textureY); // Box 38
		baseModel[26] = new ModelRendererTurbo(this, 114, 0, textureX, textureY); // Box 39
		baseModel[27] = new ModelRendererTurbo(this, 76, 0, textureX, textureY); // Box 40
		baseModel[28] = new ModelRendererTurbo(this, 76, 0, textureX, textureY); // Box 41
		baseModel[29] = new ModelRendererTurbo(this, 96, 8, textureX, textureY); // Box 42
		baseModel[30] = new ModelRendererTurbo(this, 58, 0, textureX, textureY); // Box 43
		baseModel[31] = new ModelRendererTurbo(this, 58, 0, textureX, textureY); // Box 44
		baseModel[32] = new ModelRendererTurbo(this, 168, 0, textureX, textureY); // Box 45
		baseModel[33] = new ModelRendererTurbo(this, 168, 0, textureX, textureY); // Box 46
		baseModel[34] = new ModelRendererTurbo(this, 121, 16, textureX, textureY); // Box 47
		baseModel[35] = new ModelRendererTurbo(this, 150, 62, textureX, textureY); // Box 48
		baseModel[36] = new ModelRendererTurbo(this, 138, 1, textureX, textureY); // Box 49
		baseModel[37] = new ModelRendererTurbo(this, 80, 30, textureX, textureY); // Box 50
		baseModel[38] = new ModelRendererTurbo(this, 98, 49, textureX, textureY); // Box 52
		baseModel[39] = new ModelRendererTurbo(this, 138, 1, textureX, textureY); // Box 53
		baseModel[40] = new ModelRendererTurbo(this, 98, 49, textureX, textureY); // Box 54
		baseModel[41] = new ModelRendererTurbo(this, 96, 14, textureX, textureY); // Box 55
		baseModel[42] = new ModelRendererTurbo(this, 92, 43, textureX, textureY); // Box 58
		baseModel[43] = new ModelRendererTurbo(this, 162, 79, textureX, textureY); // Box 59
		baseModel[44] = new ModelRendererTurbo(this, 92, 43, textureX, textureY); // Box 58
		baseModel[45] = new ModelRendererTurbo(this, 162, 79, textureX, textureY); // Box 59
		baseModel[46] = new ModelRendererTurbo(this, 96, 14, textureX, textureY); // Box 61
		baseModel[47] = new ModelRendererTurbo(this, 82, 32, textureX, textureY); // Box 29
		baseModel[48] = new ModelRendererTurbo(this, 78, 28, textureX, textureY); // Box 30
		baseModel[49] = new ModelRendererTurbo(this, 74, 24, textureX, textureY); // Box 31
		baseModel[50] = new ModelRendererTurbo(this, 80, 30, textureX, textureY); // Box 50
		baseModel[51] = new ModelRendererTurbo(this, 162, 79, textureX, textureY); // Box 59
		baseModel[52] = new ModelRendererTurbo(this, 162, 79, textureX, textureY); // Box 59
		baseModel[53] = new ModelRendererTurbo(this, 162, 79, textureX, textureY); // Box 59
		baseModel[54] = new ModelRendererTurbo(this, 162, 79, textureX, textureY); // Box 59
		baseModel[55] = new ModelRendererTurbo(this, 162, 79, textureX, textureY); // Box 59
		baseModel[56] = new ModelRendererTurbo(this, 162, 79, textureX, textureY); // Box 59
		baseModel[57] = new ModelRendererTurbo(this, 153, 22, textureX, textureY); // Shape 72
		baseModel[58] = new ModelRendererTurbo(this, 153, 22, textureX, textureY); // Shape 72
		baseModel[59] = new ModelRendererTurbo(this, 153, 22, textureX, textureY); // Shape 72
		baseModel[60] = new ModelRendererTurbo(this, 153, 22, textureX, textureY); // Shape 72

		baseModel[0].addShapeBox(0F, 0F, 0F, 26, 3, 22, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F); // Box 3
		baseModel[0].setRotationPoint(3F, -3F, 3F);

		baseModel[1].addShapeBox(0F, 0F, 0F, 10, 2, 10, 0F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		baseModel[1].setRotationPoint(11F, -55F, 11F);

		baseModel[2].addShapeBox(0F, 0F, 0F, 10, 6, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 5
		baseModel[2].setRotationPoint(11F, -53F, 11F);

		baseModel[3].addShapeBox(0F, 0F, 0F, 10, 2, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F); // Box 6
		baseModel[3].setRotationPoint(11F, -47F, 11F);

		baseModel[4].addShapeBox(0F, 0F, 0F, 6, 34, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 7
		baseModel[4].setRotationPoint(13F, -45F, 13F);

		baseModel[5].addShapeBox(0F, 0F, 0F, 16, 8, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 8
		baseModel[5].setRotationPoint(8F, -11F, 8F);

		baseModel[6].addShapeBox(0F, 0F, 0F, 12, 6, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 9
		baseModel[6].setRotationPoint(10F, -8F, 24F);

		baseModel[7].addShapeBox(0F, 0F, 0F, 2, 5, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 11
		baseModel[7].setRotationPoint(22F, -7F, 25F);

		baseModel[8].addShapeBox(0F, 0F, 0F, 26, 2, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F); // Box 13
		baseModel[8].setRotationPoint(3F, -2F, 25F);

		baseModel[9].addShapeBox(0F, 0F, 0F, 3, 5, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 14
		baseModel[9].setRotationPoint(7F, -7F, 25F);

		baseModel[10].addShapeBox(0F, 0F, 0F, 5, 3, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 15
		baseModel[10].setRotationPoint(2F, -3F, 25F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 5, 4, 5, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		baseModel[11].setRotationPoint(2F, -7F, 25F);

		baseModel[12].addShapeBox(0F, 0F, 0F, 5, 5, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F); // Box 17
		baseModel[12].setRotationPoint(24F, -7F, 25F);

		baseModel[13].addShapeBox(0F, 0F, 0F, 5, 5, 13, 0F, 0F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0F, 0F, 0F); // Box 18
		baseModel[13].setRotationPoint(24F, -7F, 12F);

		baseModel[14].addShapeBox(0F, 0F, 0F, 10, 8, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 20
		baseModel[14].setRotationPoint(11F, -8F, 0F);

		baseModel[15].setFlipped(true);
		baseModel[15].addShapeBox(0F, 0F, 0F, 7, 8, 10, 0F, -7F, 0F, 0F, -7F, 0F, 0F, -7F, 0F, 0F, -7F, 0F, 0F, -7F, 0F, 0F, -7F, 0F, 0F, -7F, 0F, 0F, -7F, 0F, 0F); // Box 23
		baseModel[15].setRotationPoint(0F, -8F, 11F);

		baseModel[16].addShapeBox(0F, 0F, 0F, 7, 8, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 25
		baseModel[16].setRotationPoint(25F, -8F, 11F);

		baseModel[17].addShapeBox(0F, 0F, 0F, 1, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		baseModel[17].setRotationPoint(11F, -8F, 31F);

		baseModel[18].addShapeBox(0F, 0F, 0F, 1, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 28
		baseModel[18].setRotationPoint(20F, -8F, 31F);

		baseModel[19].addShapeBox(0F, 0F, 0F, 3, 1, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 29
		baseModel[19].setRotationPoint(14.5F, -40F, 12F);

		baseModel[20].addShapeBox(0F, 0F, 0F, 3, 1, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 30
		baseModel[20].setRotationPoint(14.5F, -28F, 10F);

		baseModel[21].addShapeBox(0F, 0F, 0F, 3, 1, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 31
		baseModel[21].setRotationPoint(14.5F, -23F, 8F);

		baseModel[22].addShapeBox(0F, 0.5F, 0F, 1, 4, 9, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // NAMEPLATE01
		baseModel[22].setRotationPoint(28F, -12F, 11.5F);
		baseModel[22].rotateAngleZ = 0.90757121F;

		baseModel[23].addShapeBox(0F, 0F, 0F, 14, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 36
		baseModel[23].setRotationPoint(9F, -41F, 9F);

		baseModel[24].addShapeBox(0F, 0F, 0F, 18, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 37
		baseModel[24].setRotationPoint(7F, -29F, 7F);

		baseModel[25].addShapeBox(0F, 0F, 0F, 22, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 38
		baseModel[25].setRotationPoint(5F, -24F, 5F);

		baseModel[26].setFlipped(true);
		baseModel[26].addShapeBox(0F, 0F, 0F, 14, 3, 3, 0F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F); // Box 39
		baseModel[26].setRotationPoint(9F, -41F, 20F);

		baseModel[27].addShapeBox(0F, 0F, 0F, 3, 3, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 40
		baseModel[27].setRotationPoint(9F, -41F, 12F);

		baseModel[28].addShapeBox(0F, 0F, 0F, 3, 3, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 41
		baseModel[28].setRotationPoint(20F, -41F, 12F);

		baseModel[29].setFlipped(true);
		baseModel[29].addShapeBox(0F, 0F, 0F, 18, 3, 3, 0F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F); // Box 42
		baseModel[29].setRotationPoint(7F, -29F, 22F);

		baseModel[30].addShapeBox(0F, 0F, 0F, 3, 3, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 43
		baseModel[30].setRotationPoint(7F, -29F, 10F);

		baseModel[31].addShapeBox(0F, 0F, 0F, 3, 3, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 44
		baseModel[31].setRotationPoint(22F, -29F, 10F);

		baseModel[32].addShapeBox(0F, 0F, 0F, 3, 3, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 45
		baseModel[32].setRotationPoint(24F, -24F, 8F);

		baseModel[33].addShapeBox(0F, 0F, 0F, 3, 3, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 46
		baseModel[33].setRotationPoint(5F, -24F, 8F);

		baseModel[34].setFlipped(true);
		baseModel[34].addShapeBox(0F, 0F, 0F, 22, 3, 3, 0F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F); // Box 47
		baseModel[34].setRotationPoint(5F, -24F, 24F);

		baseModel[35].addShapeBox(0F, 0F, 0F, 12, 5, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 48
		baseModel[35].setRotationPoint(10F, -16F, 10F);

		baseModel[36].addShapeBox(0F, 0F, 0F, 3, 3, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 49
		baseModel[36].setRotationPoint(21F, -35F, 11F);

		baseModel[37].addShapeBox(0F, 0F, 0F, 3, 1, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 50
		baseModel[37].setRotationPoint(14.5F, -34F, 11F);

		baseModel[38].setFlipped(true);
		baseModel[38].addShapeBox(0F, 0F, 0F, 16, 3, 3, 0F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F); // Box 52
		baseModel[38].setRotationPoint(8F, -35F, 21F);

		baseModel[39].addShapeBox(0F, 0F, 0F, 3, 3, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 53
		baseModel[39].setRotationPoint(8F, -35F, 11F);

		baseModel[40].addShapeBox(0F, 0F, 0F, 16, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 54
		baseModel[40].setRotationPoint(8F, -35F, 8F);

		baseModel[41].addShapeBox(0F, 0F, 0F, 3, 3, 19, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 55
		baseModel[41].setRotationPoint(12.5F, -15F, 6.5F);

		baseModel[42].addShapeBox(0F, 0F, 0F, 19, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 58
		baseModel[42].setRotationPoint(6.5F, -15F, 16.5F);

		baseModel[43].addShapeBox(0F, 0F, 0F, 2, 3, 3, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 59
		baseModel[43].setRotationPoint(25.5F, -15F, 16.5F);

		baseModel[44].addShapeBox(0F, 0F, 0F, 19, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 58
		baseModel[44].setRotationPoint(6.5F, -15F, 12.5F);

		baseModel[45].addShapeBox(0F, 0F, 0F, 2, 3, 3, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 59
		baseModel[45].setRotationPoint(25.5F, -15F, 12.5F);

		baseModel[46].addShapeBox(0F, 0F, 0F, 3, 3, 19, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 61
		baseModel[46].setRotationPoint(16.5F, -15F, 6.5F);

		baseModel[47].addShapeBox(0F, 0F, 0F, 3, 1, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 29
		baseModel[47].setRotationPoint(11.5F, -40F, 17.5F);
		baseModel[47].rotateAngleY = -1.57079633F;

		baseModel[48].addShapeBox(0F, 0F, 0F, 3, 1, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 30
		baseModel[48].setRotationPoint(9.5F, -28F, 17.5F);
		baseModel[48].rotateAngleY = -1.57079633F;

		baseModel[49].addShapeBox(0F, 0F, 0F, 3, 1, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 31
		baseModel[49].setRotationPoint(7.5F, -23F, 17.5F);
		baseModel[49].rotateAngleY = -1.57079633F;

		baseModel[50].addShapeBox(0F, 0F, 0F, 3, 1, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 50
		baseModel[50].setRotationPoint(10.5F, -34F, 17.5F);
		baseModel[50].rotateAngleY = -1.57079633F;

		baseModel[51].addShapeBox(0F, 0F, 0F, 2, 3, 3, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 59
		baseModel[51].setRotationPoint(19.5F, -15F, 25.5F);
		baseModel[51].rotateAngleY = 1.57079633F;

		baseModel[52].addShapeBox(0F, 0F, 0F, 2, 3, 3, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 59
		baseModel[52].setRotationPoint(15.5F, -15F, 25.5F);
		baseModel[52].rotateAngleY = 1.57079633F;

		baseModel[53].addShapeBox(0F, 0F, 0F, 2, 3, 3, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 59
		baseModel[53].setRotationPoint(6.5F, -15F, 19.5F);
		baseModel[53].rotateAngleY = 3.14159265F;

		baseModel[54].addShapeBox(0F, 0F, 0F, 2, 3, 3, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 59
		baseModel[54].setRotationPoint(6.5F, -15F, 15.5F);
		baseModel[54].rotateAngleY = 3.14159265F;

		baseModel[55].addShapeBox(0F, 0F, 0F, 2, 3, 3, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 59
		baseModel[55].setRotationPoint(12.5F, -15F, 6.5F);
		baseModel[55].rotateAngleY = 4.71238898F;

		baseModel[56].addShapeBox(0F, 0F, 0F, 2, 3, 3, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 59
		baseModel[56].setRotationPoint(16.5F, -15F, 6.5F);
		baseModel[56].rotateAngleY = 4.71238898F;

		baseModel[57].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(7, 0, 7, 0), new Coord2D(7, 4, 7, 4), new Coord2D(4, 4, 4, 4), new Coord2D(0, 1, 0, 1) }), 10, 7, 4, 20, 10, ModelRendererTurbo.MR_FRONT, new float[] {1 ,5 ,3 ,4 ,7}); // Shape 72
		baseModel[57].setRotationPoint(32F, -8F, 21F);

		baseModel[58].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(7, 0, 7, 0), new Coord2D(7, 4, 7, 4), new Coord2D(4, 4, 4, 4), new Coord2D(0, 1, 0, 1) }), 10, 7, 4, 20, 10, ModelRendererTurbo.MR_FRONT, new float[] {1 ,5 ,3 ,4 ,7}); // Shape 72
		baseModel[58].setRotationPoint(11F, -8F, 31F);
		baseModel[58].rotateAngleY = 1.57079633F;

		baseModel[59].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(7, 0, 7, 0), new Coord2D(7, 4, 7, 4), new Coord2D(4, 4, 4, 4), new Coord2D(0, 1, 0, 1) }), 10, 7, 4, 20, 10, ModelRendererTurbo.MR_FRONT, new float[] {1 ,5 ,3 ,4 ,7}); // Shape 72
		baseModel[59].setRotationPoint(0F, -8F, 11F);
		baseModel[59].rotateAngleY = 3.14159265F;

		baseModel[60].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(7, 0, 7, 0), new Coord2D(7, 4, 7, 4), new Coord2D(4, 4, 4, 4), new Coord2D(0, 1, 0, 1) }), 10, 7, 4, 20, 10, ModelRendererTurbo.MR_FRONT, new float[] {1 ,5 ,3 ,4 ,7}); // Shape 72
		baseModel[60].setRotationPoint(21F, -8F, 0F);
		baseModel[60].rotateAngleY = 4.71238898F;

		parts.put("base", baseModel);

		translateAll(-16,0,-16);

		baseModel = Arrays.stream(baseModel).
				sorted((o1, o2) -> Float.compare(o1.rotationPointY, o2.rotationPointY)).
				toArray(ModelRendererTurbo[]::new);

		flipAll();
	}
}
