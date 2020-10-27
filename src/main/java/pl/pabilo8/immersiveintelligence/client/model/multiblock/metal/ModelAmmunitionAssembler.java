package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import pl.pabilo8.immersiveintelligence.client.model.ModelBlockBase;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 30.09.2020
 */
public class ModelAmmunitionAssembler extends ModelBlockBase
{
	int textureX = 128;
	int textureY = 128;

	public ModelAmmunitionAssembler() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[66];
		baseModel[0] = new ModelRendererTurbo(this, 0, 88, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 38, 47, textureX, textureY); // Box 0
		baseModel[2] = new ModelRendererTurbo(this, 64, 91, textureX, textureY); // Box 0
		baseModel[3] = new ModelRendererTurbo(this, 64, 91, textureX, textureY); // Box 0
		baseModel[4] = new ModelRendererTurbo(this, 64, 91, textureX, textureY); // Box 0
		baseModel[5] = new ModelRendererTurbo(this, 64, 91, textureX, textureY); // Box 0
		baseModel[6] = new ModelRendererTurbo(this, 0, 88, textureX, textureY); // Box 0
		baseModel[7] = new ModelRendererTurbo(this, 38, 47, textureX, textureY); // Box 0
		baseModel[8] = new ModelRendererTurbo(this, 64, 91, textureX, textureY); // Box 0
		baseModel[9] = new ModelRendererTurbo(this, 64, 91, textureX, textureY); // Box 0
		baseModel[10] = new ModelRendererTurbo(this, 64, 91, textureX, textureY); // Box 0
		baseModel[11] = new ModelRendererTurbo(this, 64, 91, textureX, textureY); // Box 0
		baseModel[12] = new ModelRendererTurbo(this, 50, 4, textureX, textureY); // Box 0
		baseModel[13] = new ModelRendererTurbo(this, 0, 4, textureX, textureY); // Box 0
		baseModel[14] = new ModelRendererTurbo(this, 52, 3, textureX, textureY); // Box 0
		baseModel[15] = new ModelRendererTurbo(this, 24, 53, textureX, textureY); // Box 0
		baseModel[16] = new ModelRendererTurbo(this, 96, 47, textureX, textureY); // Box 0
		baseModel[17] = new ModelRendererTurbo(this, 0, 47, textureX, textureY); // Box 0
		baseModel[18] = new ModelRendererTurbo(this, 118, 13, textureX, textureY); // Box 0
		baseModel[19] = new ModelRendererTurbo(this, 118, 13, textureX, textureY); // Box 0
		baseModel[20] = new ModelRendererTurbo(this, 96, 63, textureX, textureY); // Box 0
		baseModel[21] = new ModelRendererTurbo(this, 96, 120, textureX, textureY); // Box 0
		baseModel[22] = new ModelRendererTurbo(this, 28, 28, textureX, textureY); // Box 0
		baseModel[23] = new ModelRendererTurbo(this, 101, 1, textureX, textureY); // Box 0
		baseModel[24] = new ModelRendererTurbo(this, 101, 1, textureX, textureY); // Box 0
		baseModel[25] = new ModelRendererTurbo(this, 101, 1, textureX, textureY); // Box 0
		baseModel[26] = new ModelRendererTurbo(this, 101, 1, textureX, textureY); // Box 0
		baseModel[27] = new ModelRendererTurbo(this, 0, 72, textureX, textureY); // Box 0
		baseModel[28] = new ModelRendererTurbo(this, 0, 53, textureX, textureY); // Box 0
		baseModel[29] = new ModelRendererTurbo(this, 50, 4, textureX, textureY); // Box 0
		baseModel[30] = new ModelRendererTurbo(this, 50, 4, textureX, textureY); // Box 0
		baseModel[31] = new ModelRendererTurbo(this, 50, 4, textureX, textureY); // Box 0
		baseModel[32] = new ModelRendererTurbo(this, 50, 0, textureX, textureY); // Box 0
		baseModel[33] = new ModelRendererTurbo(this, 50, 0, textureX, textureY); // Box 0
		baseModel[34] = new ModelRendererTurbo(this, 50, 0, textureX, textureY); // Box 0
		baseModel[35] = new ModelRendererTurbo(this, 60, 0, textureX, textureY); // Box 0
		baseModel[36] = new ModelRendererTurbo(this, 60, 0, textureX, textureY); // Box 0
		baseModel[37] = new ModelRendererTurbo(this, 60, 0, textureX, textureY); // Box 0
		baseModel[38] = new ModelRendererTurbo(this, 34, 17, textureX, textureY); // Box 0
		baseModel[39] = new ModelRendererTurbo(this, 0, 106, textureX, textureY); // Box 0
		baseModel[40] = new ModelRendererTurbo(this, 96, 47, textureX, textureY); // Box 0
		baseModel[41] = new ModelRendererTurbo(this, 60, 53, textureX, textureY); // Box 0
		baseModel[42] = new ModelRendererTurbo(this, 18, 106, textureX, textureY); // Box 0
		baseModel[43] = new ModelRendererTurbo(this, 50, 9, textureX, textureY); // Box 0
		baseModel[44] = new ModelRendererTurbo(this, 96, 47, textureX, textureY); // Box 0
		baseModel[45] = new ModelRendererTurbo(this, 60, 53, textureX, textureY); // Box 0
		baseModel[46] = new ModelRendererTurbo(this, 18, 106, textureX, textureY); // Box 0
		baseModel[47] = new ModelRendererTurbo(this, 50, 9, textureX, textureY); // Box 0
		baseModel[48] = new ModelRendererTurbo(this, 96, 47, textureX, textureY); // Box 0
		baseModel[49] = new ModelRendererTurbo(this, 60, 53, textureX, textureY); // Box 0
		baseModel[50] = new ModelRendererTurbo(this, 18, 106, textureX, textureY); // Box 0
		baseModel[51] = new ModelRendererTurbo(this, 50, 9, textureX, textureY); // Box 0
		baseModel[52] = new ModelRendererTurbo(this, 64, 84, textureX, textureY); // Box 0
		baseModel[53] = new ModelRendererTurbo(this, 2, 0, textureX, textureY); // Box 0
		baseModel[54] = new ModelRendererTurbo(this, 0, 30, textureX, textureY); // Box 0
		baseModel[55] = new ModelRendererTurbo(this, 36, 67, textureX, textureY); // Box 0
		baseModel[56] = new ModelRendererTurbo(this, 108, 24, textureX, textureY); // Box 0
		baseModel[57] = new ModelRendererTurbo(this, 0, 19, textureX, textureY); // Box 0
		baseModel[58] = new ModelRendererTurbo(this, 0, 88, textureX, textureY); // Box 0
		baseModel[59] = new ModelRendererTurbo(this, 36, 67, textureX, textureY); // Box 0
		baseModel[60] = new ModelRendererTurbo(this, 36, 67, textureX, textureY); // Box 0
		baseModel[61] = new ModelRendererTurbo(this, 36, 67, textureX, textureY); // Box 0
		baseModel[62] = new ModelRendererTurbo(this, 36, 67, textureX, textureY); // Box 0
		baseModel[63] = new ModelRendererTurbo(this, 36, 67, textureX, textureY); // Box 0
		baseModel[64] = new ModelRendererTurbo(this, 0, 4, textureX, textureY); // Box 0
		baseModel[65] = new ModelRendererTurbo(this, 50, 4, textureX, textureY); // Box 0

		baseModel[0].addBox(0F, 0F, 0F, 16, 8, 32, 0F); // Box 0
		baseModel[0].setRotationPoint(16F, -8F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 14, 7, 30, 0F); // Box 0
		baseModel[1].setRotationPoint(17F, -15F, 1F);

		baseModel[2].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 0
		baseModel[2].setRotationPoint(16F, -15F, 0F);

		baseModel[3].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 0
		baseModel[3].setRotationPoint(30F, -15F, 0F);

		baseModel[4].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 0
		baseModel[4].setRotationPoint(16F, -15F, 30F);

		baseModel[5].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 0
		baseModel[5].setRotationPoint(30F, -15F, 30F);

		baseModel[6].addBox(0F, 0F, 0F, 16, 8, 32, 0F); // Box 0
		baseModel[6].setRotationPoint(-16F, -8F, 0F);

		baseModel[7].addBox(0F, 0F, 0F, 14, 7, 30, 0F); // Box 0
		baseModel[7].setRotationPoint(-15F, -15F, 1F);

		baseModel[8].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 0
		baseModel[8].setRotationPoint(-16F, -15F, 0F);

		baseModel[9].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 0
		baseModel[9].setRotationPoint(-2F, -15F, 0F);

		baseModel[10].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 0
		baseModel[10].setRotationPoint(-16F, -15F, 30F);

		baseModel[11].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 0
		baseModel[11].setRotationPoint(-2F, -15F, 30F);

		baseModel[12].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[12].setRotationPoint(31F, -12F, 4F);

		baseModel[13].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[13].setRotationPoint(31.01F, -12F, 19F);

		baseModel[14].addBox(0F, 0F, 0F, 16, 8, 16, 0F); // Box 0
		baseModel[14].setRotationPoint(-16F, -8F, -16F);

		baseModel[15].addBox(0F, 0F, 0F, 14, 6, 8, 0F); // Box 0
		baseModel[15].setRotationPoint(12F, -6F, -11F);
		baseModel[15].rotateAngleY = 0.29670597F;

		baseModel[16].addBox(0F, 0F, 0F, 8, 8, 8, 0F); // Box 0
		baseModel[16].setRotationPoint(16F, -8F, -13F);
		baseModel[16].rotateAngleY = -0.90757121F;

		baseModel[17].addBox(0F, 0F, 0F, 32, 4, 2, 0F); // Box 0
		baseModel[17].setRotationPoint(-16F, -21F, 0F);

		baseModel[18].addBox(0F, 0F, 0F, 2, 17, 2, 0F); // Box 0
		baseModel[18].setRotationPoint(0F, -17F, 0F);

		baseModel[19].addBox(0F, 0F, 0F, 2, 17, 2, 0F); // Box 0
		baseModel[19].setRotationPoint(14F, -17F, 0F);

		baseModel[20].addBox(0F, 0F, 0F, 12, 13, 0, 0F); // Box 0
		baseModel[20].setRotationPoint(2F, -17F, 1F);

		baseModel[21].addBox(0F, 0F, 0F, 12, 4, 2, 0F); // Box 0
		baseModel[21].setRotationPoint(2F, -4F, 0F);

		baseModel[22].addBox(0F, 0F, 0F, 32, 3, 16, 0F); // Box 0
		baseModel[22].setRotationPoint(0F, -15F, -16F);

		baseModel[23].addBox(0F, 0F, 0F, 3, 12, 3, 0F); // Box 0
		baseModel[23].setRotationPoint(28F, -12F, -15F);

		baseModel[24].addBox(0F, 0F, 0F, 3, 12, 3, 0F); // Box 0
		baseModel[24].setRotationPoint(28F, -12F, -4F);

		baseModel[25].addBox(0F, 0F, 0F, 3, 12, 3, 0F); // Box 0
		baseModel[25].setRotationPoint(1F, -12F, -15F);

		baseModel[26].addBox(0F, 0F, 0F, 3, 12, 3, 0F); // Box 0
		baseModel[26].setRotationPoint(1F, -12F, -4F);

		baseModel[27].addBox(0F, 0F, 0F, 12, 4, 12, 0F); // Box 0
		baseModel[27].setRotationPoint(2F, -4F, 4F);

		baseModel[28].addBox(0F, 0F, 0F, 6, 11, 6, 0F); // Box 0
		baseModel[28].setRotationPoint(2F, -15F, 4F);

		baseModel[29].addBox(0F, 0F, 0F, 2, 3, 2, 0F); // Box 0
		baseModel[29].setRotationPoint(10F, -8F, 5F);

		baseModel[30].addBox(0F, 0F, 0F, 2, 3, 2, 0F); // Box 0
		baseModel[30].setRotationPoint(10F, -8F, 8F);

		baseModel[31].addBox(0F, 0F, 0F, 2, 3, 2, 0F); // Box 0
		baseModel[31].setRotationPoint(10F, -8F, 11F);

		baseModel[32].addBox(0F, 0F, 0F, 3, 2, 2, 0F); // Box 0
		baseModel[32].setRotationPoint(12F, -10F, 5F);

		baseModel[33].addBox(0F, 0F, 0F, 3, 2, 2, 0F); // Box 0
		baseModel[33].setRotationPoint(12F, -10F, 8F);

		baseModel[34].addBox(0F, 0F, 0F, 3, 2, 2, 0F); // Box 0
		baseModel[34].setRotationPoint(12F, -10F, 11F);

		baseModel[35].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[35].setRotationPoint(10F, -10F, 11F);

		baseModel[36].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[36].setRotationPoint(10F, -10F, 8F);

		baseModel[37].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[37].setRotationPoint(10F, -10F, 5F);

		baseModel[38].addBox(0F, 0F, 0F, 4, 1, 10, 0F); // Box 0
		baseModel[38].setRotationPoint(9F, -5F, 4F);

		baseModel[39].addBox(0F, 0F, 0F, 2, 4, 10, 0F); // Box 0
		baseModel[39].setRotationPoint(15F, -11F, 4F);

		baseModel[40].addBox(0F, 0F, 0F, 3, 3, 1, 0F); // Box 0
		baseModel[40].setRotationPoint(3F, -15F, 10F);

		baseModel[41].addBox(0F, 0F, 0F, 1, 1, 3, 0F); // Box 0
		baseModel[41].setRotationPoint(4F, -14F, 11F);

		baseModel[42].addBox(0F, 0F, 0F, 2, 2, 3, 0F); // Box 0
		baseModel[42].setRotationPoint(3.5F, -14.5F, 11F);

		baseModel[43].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[43].setRotationPoint(4F, -14F, 14F);

		baseModel[44].addBox(0F, 0F, 0F, 3, 3, 1, 0F); // Box 0
		baseModel[44].setRotationPoint(3F, -11F, 10F);

		baseModel[45].addBox(0F, 0F, 0F, 1, 1, 3, 0F); // Box 0
		baseModel[45].setRotationPoint(4F, -10F, 11F);

		baseModel[46].addBox(0F, 0F, 0F, 2, 2, 3, 0F); // Box 0
		baseModel[46].setRotationPoint(3.5F, -10.5F, 11F);

		baseModel[47].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[47].setRotationPoint(4F, -10F, 14F);

		baseModel[48].addBox(0F, 0F, 0F, 3, 3, 1, 0F); // Box 0
		baseModel[48].setRotationPoint(3F, -7F, 10F);

		baseModel[49].addBox(0F, 0F, 0F, 1, 1, 3, 0F); // Box 0
		baseModel[49].setRotationPoint(4F, -6F, 11F);

		baseModel[50].addBox(0F, 0F, 0F, 2, 2, 3, 0F); // Box 0
		baseModel[50].setRotationPoint(3.5F, -6.5F, 11F);

		baseModel[51].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[51].setRotationPoint(4F, -6F, 14F);

		baseModel[52].addBox(0F, 0F, 0F, 16, 20, 16, 0F); // Box 0
		baseModel[52].setRotationPoint(-16F, -31F, -16F);

		baseModel[53].addShapeBox(0F, 0F, 0F, 16, 1, 16, 0F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[53].setRotationPoint(-16F, -32F, -16F);

		baseModel[54].addBox(0F, 0F, 0F, 14, 6, 8, 0F); // Box 0
		baseModel[54].setRotationPoint(12F, -11F, -11F);
		baseModel[54].rotateAngleY = -0.17453293F;
		baseModel[54].rotateAngleZ = 0.15707963F;

		baseModel[55].addBox(0F, 0F, 0F, 8, 2, 8, 0F); // Box 0
		baseModel[55].setRotationPoint(-12F, -11F, -12F);

		baseModel[56].addBox(0F, 0F, 0F, 1, 12, 8, 0F); // Box 0
		baseModel[56].setRotationPoint(-17F, -25F, -12F);

		baseModel[57].addBox(0F, 0F, 0F, 10, 1, 10, 0F); // Box 0
		baseModel[57].setRotationPoint(2F, -16F, 4F);

		baseModel[58].addBox(0F, 0F, 0F, 8, 10, 8, 0F); // Box 0
		baseModel[58].setRotationPoint(3F, -10F, -2F);
		baseModel[58].rotateAngleY = -1.91986218F;

		baseModel[59].addBox(0F, 0F, 0F, 8, 1, 8, 0F); // Box 0
		baseModel[59].setRotationPoint(-12F, -9F, -12F);

		baseModel[60].addBox(0F, 0F, 0F, 2, 3, 2, 0F); // Box 0
		baseModel[60].setRotationPoint(-15F, -11F, -15F);

		baseModel[61].addBox(0F, 0F, 0F, 2, 3, 2, 0F); // Box 0
		baseModel[61].setRotationPoint(-3F, -11F, -15F);

		baseModel[62].addBox(0F, 0F, 0F, 2, 3, 2, 0F); // Box 0
		baseModel[62].setRotationPoint(-15F, -11F, -3F);

		baseModel[63].addBox(0F, 0F, 0F, 2, 3, 2, 0F); // Box 0
		baseModel[63].setRotationPoint(-3F, -11F, -3F);

		baseModel[64].addBox(-1F, -8F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[64].setRotationPoint(31.01F, -12F, 19F);
		baseModel[64].rotateAngleX = 3.14159265F;
		baseModel[64].rotateAngleY = -3.14159265F;

		baseModel[65].addBox(-1F, -8F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[65].setRotationPoint(31.01F, -12F, 4F);
		baseModel[65].rotateAngleX = 3.14159265F;
		baseModel[65].rotateAngleY = -3.14159265F;

		flipAll();
	}
}
