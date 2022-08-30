package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

public class ModelEmplacement extends ModelIIBase
{
	public ModelRendererTurbo[] doorRightModel, doorLeftModel, platformModel;
	int textureX = 256;
	int textureY = 256;

	public ModelEmplacement() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[109];
		baseModel[0] = new ModelRendererTurbo(this, 206, 15, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 9, 23, textureX, textureY); // Box 0
		baseModel[2] = new ModelRendererTurbo(this, 39, 2, textureX, textureY); // Box 0
		baseModel[3] = new ModelRendererTurbo(this, 39, 2, textureX, textureY); // Box 0
		baseModel[4] = new ModelRendererTurbo(this, 39, 7, textureX, textureY); // Box 0
		baseModel[5] = new ModelRendererTurbo(this, 9, 18, textureX, textureY); // Box 0
		baseModel[6] = new ModelRendererTurbo(this, 26, 152, textureX, textureY); // Box 6
		baseModel[7] = new ModelRendererTurbo(this, 9, 18, textureX, textureY); // Box 0
		baseModel[8] = new ModelRendererTurbo(this, 9, 23, textureX, textureY); // Box 0
		baseModel[9] = new ModelRendererTurbo(this, 9, 23, textureX, textureY); // Box 0
		baseModel[10] = new ModelRendererTurbo(this, 9, 23, textureX, textureY); // Box 0
		baseModel[11] = new ModelRendererTurbo(this, 9, 28, textureX, textureY); // Box 0
		baseModel[12] = new ModelRendererTurbo(this, 154, 128, textureX, textureY); // Box 6
		baseModel[13] = new ModelRendererTurbo(this, 104, 13, textureX, textureY); // Box 6
		baseModel[14] = new ModelRendererTurbo(this, 154, 128, textureX, textureY); // Box 6
		baseModel[15] = new ModelRendererTurbo(this, 9, 28, textureX, textureY); // Box 0
		baseModel[16] = new ModelRendererTurbo(this, 9, 28, textureX, textureY); // Box 0
		baseModel[17] = new ModelRendererTurbo(this, 9, 28, textureX, textureY); // Box 0
		baseModel[18] = new ModelRendererTurbo(this, 9, 28, textureX, textureY); // Box 0
		baseModel[19] = new ModelRendererTurbo(this, 21, 33, textureX, textureY); // Box 0
		baseModel[20] = new ModelRendererTurbo(this, 11, 33, textureX, textureY); // Box 0
		baseModel[21] = new ModelRendererTurbo(this, 11, 33, textureX, textureY); // Box 0
		baseModel[22] = new ModelRendererTurbo(this, 11, 33, textureX, textureY); // Box 0
		baseModel[23] = new ModelRendererTurbo(this, 0, 103, textureX, textureY); // Box 0
		baseModel[24] = new ModelRendererTurbo(this, 0, 91, textureX, textureY); // Box 0
		baseModel[25] = new ModelRendererTurbo(this, 0, 91, textureX, textureY); // Box 0
		baseModel[26] = new ModelRendererTurbo(this, 37, 27, textureX, textureY); // Box 0
		baseModel[27] = new ModelRendererTurbo(this, 0, 79, textureX, textureY); // Box 0
		baseModel[28] = new ModelRendererTurbo(this, 55, 27, textureX, textureY); // Box 0
		baseModel[29] = new ModelRendererTurbo(this, 0, 91, textureX, textureY); // Box 0
		baseModel[30] = new ModelRendererTurbo(this, 0, 103, textureX, textureY); // Box 0
		baseModel[31] = new ModelRendererTurbo(this, 0, 91, textureX, textureY); // Box 0
		baseModel[32] = new ModelRendererTurbo(this, 0, 103, textureX, textureY); // Box 0
		baseModel[33] = new ModelRendererTurbo(this, 184, 128, textureX, textureY); // Box 6
		baseModel[34] = new ModelRendererTurbo(this, 184, 128, textureX, textureY); // Box 6
		baseModel[35] = new ModelRendererTurbo(this, 206, 15, textureX, textureY); // Box 0
		baseModel[36] = new ModelRendererTurbo(this, 104, 13, textureX, textureY); // Box 6
		baseModel[37] = new ModelRendererTurbo(this, 154, 128, textureX, textureY); // Box 6
		baseModel[38] = new ModelRendererTurbo(this, 26, 152, textureX, textureY); // Box 6
		baseModel[39] = new ModelRendererTurbo(this, 154, 128, textureX, textureY); // Box 6
		baseModel[40] = new ModelRendererTurbo(this, 0, 103, textureX, textureY); // Box 0
		baseModel[41] = new ModelRendererTurbo(this, 0, 91, textureX, textureY); // Box 0
		baseModel[42] = new ModelRendererTurbo(this, 0, 91, textureX, textureY); // Box 0
		baseModel[43] = new ModelRendererTurbo(this, 0, 103, textureX, textureY); // Box 0
		baseModel[44] = new ModelRendererTurbo(this, 148, 170, textureX, textureY); // Box 0
		baseModel[45] = new ModelRendererTurbo(this, 114, 170, textureX, textureY); // Box 0
		baseModel[46] = new ModelRendererTurbo(this, 0, 79, textureX, textureY); // Box 0
		baseModel[47] = new ModelRendererTurbo(this, 29, 19, textureX, textureY); // Box 0
		baseModel[48] = new ModelRendererTurbo(this, 29, 19, textureX, textureY); // Box 0
		baseModel[49] = new ModelRendererTurbo(this, 39, 12, textureX, textureY); // Box 0
		baseModel[50] = new ModelRendererTurbo(this, 39, 12, textureX, textureY); // Box 0
		baseModel[51] = new ModelRendererTurbo(this, 9, 18, textureX, textureY); // Box 0
		baseModel[52] = new ModelRendererTurbo(this, 9, 23, textureX, textureY); // Box 0
		baseModel[53] = new ModelRendererTurbo(this, 9, 23, textureX, textureY); // Box 0
		baseModel[54] = new ModelRendererTurbo(this, 9, 28, textureX, textureY); // Box 0
		baseModel[55] = new ModelRendererTurbo(this, 9, 28, textureX, textureY); // Box 0
		baseModel[56] = new ModelRendererTurbo(this, 39, 7, textureX, textureY); // Box 0
		baseModel[57] = new ModelRendererTurbo(this, 9, 28, textureX, textureY); // Box 0
		baseModel[58] = new ModelRendererTurbo(this, 0, 91, textureX, textureY); // Box 0
		baseModel[59] = new ModelRendererTurbo(this, 0, 91, textureX, textureY); // Box 0
		baseModel[60] = new ModelRendererTurbo(this, 0, 103, textureX, textureY); // Box 0
		baseModel[61] = new ModelRendererTurbo(this, 0, 103, textureX, textureY); // Box 0
		baseModel[62] = new ModelRendererTurbo(this, 0, 103, textureX, textureY); // Box 0
		baseModel[63] = new ModelRendererTurbo(this, 29, 9, textureX, textureY); // Box 0
		baseModel[64] = new ModelRendererTurbo(this, 0, 103, textureX, textureY); // Box 0
		baseModel[65] = new ModelRendererTurbo(this, 29, 9, textureX, textureY); // Box 0
		baseModel[66] = new ModelRendererTurbo(this, 0, 91, textureX, textureY); // Box 0
		baseModel[67] = new ModelRendererTurbo(this, 0, 79, textureX, textureY); // Box 0
		baseModel[68] = new ModelRendererTurbo(this, 34, 71, textureX, textureY); // Box 70
		baseModel[69] = new ModelRendererTurbo(this, 64, 48, textureX, textureY); // BaseBottom
		baseModel[70] = new ModelRendererTurbo(this, 34, 71, textureX, textureY); // Box 70
		baseModel[71] = new ModelRendererTurbo(this, 37, 11, textureX, textureY); // Box 70
		baseModel[72] = new ModelRendererTurbo(this, 37, 11, textureX, textureY); // Box 70
		baseModel[73] = new ModelRendererTurbo(this, 146, 171, textureX, textureY); // Box 70
		baseModel[74] = new ModelRendererTurbo(this, 76, 168, textureX, textureY); // Box 70
		baseModel[75] = new ModelRendererTurbo(this, 27, 30, textureX, textureY); // Box 70
		baseModel[76] = new ModelRendererTurbo(this, 15, 41, textureX, textureY); // Box 70
		baseModel[77] = new ModelRendererTurbo(this, 1, 33, textureX, textureY); // Box 70
		baseModel[78] = new ModelRendererTurbo(this, 1, 33, textureX, textureY); // Box 70
		baseModel[79] = new ModelRendererTurbo(this, 146, 171, textureX, textureY); // Box 70
		baseModel[80] = new ModelRendererTurbo(this, 76, 168, textureX, textureY); // Box 70
		baseModel[81] = new ModelRendererTurbo(this, 27, 30, textureX, textureY); // Box 70
		baseModel[82] = new ModelRendererTurbo(this, 15, 41, textureX, textureY); // Box 70
		baseModel[83] = new ModelRendererTurbo(this, 1, 33, textureX, textureY); // Box 70
		baseModel[84] = new ModelRendererTurbo(this, 1, 33, textureX, textureY); // Box 70
		baseModel[85] = new ModelRendererTurbo(this, 122, 168, textureX, textureY); // Box 70
		baseModel[86] = new ModelRendererTurbo(this, 64, 128, textureX, textureY); // Box 70
		baseModel[87] = new ModelRendererTurbo(this, 27, 24, textureX, textureY); // Box 70
		baseModel[88] = new ModelRendererTurbo(this, 21, 41, textureX, textureY); // Box 70
		baseModel[89] = new ModelRendererTurbo(this, 27, 27, textureX, textureY); // Box 70
		baseModel[90] = new ModelRendererTurbo(this, 27, 27, textureX, textureY); // Box 70
		baseModel[91] = new ModelRendererTurbo(this, 106, 163, textureX, textureY); // Box 70
		baseModel[92] = new ModelRendererTurbo(this, 0, 63, textureX, textureY); // Box 0
		baseModel[93] = new ModelRendererTurbo(this, 0, 63, textureX, textureY); // Box 0
		baseModel[94] = new ModelRendererTurbo(this, 0, 55, textureX, textureY); // Box 0
		baseModel[95] = new ModelRendererTurbo(this, 0, 55, textureX, textureY); // Box 0
		baseModel[96] = new ModelRendererTurbo(this, 80, 21, textureX, textureY); // Box 0
		baseModel[97] = new ModelRendererTurbo(this, 80, 9, textureX, textureY); // Box 0
		baseModel[98] = new ModelRendererTurbo(this, 80, 21, textureX, textureY); // Box 0
		baseModel[99] = new ModelRendererTurbo(this, 0, 55, textureX, textureY); // Box 0
		baseModel[100] = new ModelRendererTurbo(this, 0, 63, textureX, textureY); // Box 0
		baseModel[101] = new ModelRendererTurbo(this, 0, 71, textureX, textureY); // Box 0
		baseModel[102] = new ModelRendererTurbo(this, 80, 9, textureX, textureY); // Box 0
		baseModel[103] = new ModelRendererTurbo(this, 80, 9, textureX, textureY); // Box 0
		baseModel[104] = new ModelRendererTurbo(this, 80, 21, textureX, textureY); // Box 0
		baseModel[105] = new ModelRendererTurbo(this, 26, 157, textureX, textureY); // BaseBottom
		baseModel[106] = new ModelRendererTurbo(this, 26, 157, textureX, textureY); // BaseBottom
		baseModel[107] = new ModelRendererTurbo(this, 124, 208, textureX, textureY); // BaseBottom
		baseModel[108] = new ModelRendererTurbo(this, 124, 208, textureX, textureY); // BaseBottom

		baseModel[0].addBox(0F, 0F, 0F, 16, 24, 4, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, -24F, 1F);

		baseModel[1].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 0
		baseModel[1].setRotationPoint(0F, -24F, 0F);

		baseModel[2].addBox(0F, 0F, 0F, 16, 4, 1, 0F); // Box 0
		baseModel[2].setRotationPoint(8F, -16F, 0F);

		baseModel[3].addBox(0F, 0F, 0F, 16, 4, 1, 0F); // Box 0
		baseModel[3].setRotationPoint(8F, -8F, 0F);

		baseModel[4].addBox(0F, 0F, 0F, 16, 4, 1, 0F); // Box 0
		baseModel[4].setRotationPoint(0F, -4F, 0F);

		baseModel[5].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 0
		baseModel[5].setRotationPoint(4F, -20F, 0F);

		baseModel[6].addBox(0F, 0F, 0F, 15, 24, 4, 0F); // Box 6
		baseModel[6].setRotationPoint(-15F, -24F, 1F);

		baseModel[7].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 0
		baseModel[7].setRotationPoint(-8F, -16F, 0F);

		baseModel[8].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 0
		baseModel[8].setRotationPoint(-8F, -8F, 0F);

		baseModel[9].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 0
		baseModel[9].setRotationPoint(-12F, -20F, 0F);

		baseModel[10].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 0
		baseModel[10].setRotationPoint(-16F, -12F, 0F);

		baseModel[11].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 0
		baseModel[11].setRotationPoint(-16F, -4F, 0F);

		baseModel[12].addBox(0F, 0F, 0F, 4, 24, 11, 0F); // Box 6
		baseModel[12].setRotationPoint(-15F, -24F, 5F);

		baseModel[13].addBox(0F, 0F, 0F, 15, 24, 4, 0F); // Box 6
		baseModel[13].setRotationPoint(16F, -24F, 1F);

		baseModel[14].addBox(0F, 0F, 0F, 4, 24, 11, 0F); // Box 6
		baseModel[14].setRotationPoint(27F, -24F, 5F);

		baseModel[15].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 0
		baseModel[15].setRotationPoint(17F, -24F, 0F);

		baseModel[16].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 0
		baseModel[16].setRotationPoint(24F, -12F, 0F);

		baseModel[17].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 0
		baseModel[17].setRotationPoint(24F, -4F, 0F);

		baseModel[18].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 0
		baseModel[18].setRotationPoint(20F, -20F, 0F);

		baseModel[19].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // Box 0
		baseModel[19].setRotationPoint(-16F, -12F, 1F);

		baseModel[20].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // Box 0
		baseModel[20].setRotationPoint(-16F, -4F, 1F);

		baseModel[21].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // Box 0
		baseModel[21].setRotationPoint(31F, -12F, 1F);

		baseModel[22].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // Box 0
		baseModel[22].setRotationPoint(31F, -4F, 1F);

		baseModel[23].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[23].setRotationPoint(31F, -24F, 5F);

		baseModel[24].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[24].setRotationPoint(31F, -20F, 13F);

		baseModel[25].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[25].setRotationPoint(31F, -4F, 13F);

		baseModel[26].addBox(0F, 0F, 0F, 1, 12, 8, 0F); // Box 0
		baseModel[26].setRotationPoint(31F, -16F, 5F);

		baseModel[27].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[27].setRotationPoint(31F, -12F, 13F);

		baseModel[28].addBox(0F, 0F, 0F, 1, 12, 8, 0F); // Box 0
		baseModel[28].setRotationPoint(-16F, -24F, 4F);

		baseModel[29].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[29].setRotationPoint(-16F, -20F, 12F);

		baseModel[30].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[30].setRotationPoint(-16F, -4F, 12F);

		baseModel[31].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[31].setRotationPoint(-16F, -12F, 12F);

		baseModel[32].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[32].setRotationPoint(-16F, -8F, 5F);

		baseModel[33].addBox(0F, 0F, 0F, 4, 24, 16, 0F); // Box 6
		baseModel[33].setRotationPoint(-15F, -24F, 16F);

		baseModel[34].addBox(0F, 0F, 0F, 4, 24, 16, 0F); // Box 6
		baseModel[34].setRotationPoint(27F, -24F, 16F);

		baseModel[35].addBox(0F, 0F, 0F, 16, 24, 4, 0F); // Box 0
		baseModel[35].setRotationPoint(0F, -24F, 43F);

		baseModel[36].addBox(0F, 0F, 0F, 15, 24, 4, 0F); // Box 6
		baseModel[36].setRotationPoint(-15F, -24F, 43F);

		baseModel[37].addBox(0F, 0F, 0F, 4, 24, 11, 0F); // Box 6
		baseModel[37].setRotationPoint(-15F, -24F, 32F);

		baseModel[38].addBox(0F, 0F, 0F, 15, 24, 4, 0F); // Box 6
		baseModel[38].setRotationPoint(16F, -24F, 43F);

		baseModel[39].addBox(0F, 0F, 0F, 4, 24, 11, 0F); // Box 6
		baseModel[39].setRotationPoint(27F, -24F, 32F);

		baseModel[40].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[40].setRotationPoint(-16F, -8F, 27F);

		baseModel[41].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[41].setRotationPoint(-16F, -12F, 31F);

		baseModel[42].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[42].setRotationPoint(-16F, -16F, 27F);

		baseModel[43].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[43].setRotationPoint(-16F, -20F, 31F);

		baseModel[44].addBox(0F, 0F, 0F, 1, 4, 16, 0F); // Box 0
		baseModel[44].setRotationPoint(-16F, -24F, 16F);

		baseModel[45].addBox(0F, 0F, 0F, 1, 4, 16, 0F); // Box 0
		baseModel[45].setRotationPoint(-16F, -4F, 31F);

		baseModel[46].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[46].setRotationPoint(-16F, -16F, 39F);

		baseModel[47].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // Box 0
		baseModel[47].setRotationPoint(-16F, -16F, 47F);

		baseModel[48].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // Box 0
		baseModel[48].setRotationPoint(-16F, -4F, 47F);

		baseModel[49].addBox(0F, 0F, 0F, 16, 4, 1, 0F); // Box 0
		baseModel[49].setRotationPoint(8F, -16F, 47F);

		baseModel[50].addBox(0F, 0F, 0F, 16, 4, 1, 0F); // Box 0
		baseModel[50].setRotationPoint(8F, -8F, 47F);

		baseModel[51].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 0
		baseModel[51].setRotationPoint(4F, -20F, 47F);

		baseModel[52].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 0
		baseModel[52].setRotationPoint(-8F, -8F, 47F);

		baseModel[53].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 0
		baseModel[53].setRotationPoint(-12F, -20F, 47F);

		baseModel[54].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 0
		baseModel[54].setRotationPoint(17F, -24F, 47F);

		baseModel[55].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 0
		baseModel[55].setRotationPoint(20F, -20F, 47F);

		baseModel[56].addBox(0F, 0F, 0F, 16, 4, 1, 0F); // Box 0
		baseModel[56].setRotationPoint(0F, -4F, 47F);

		baseModel[57].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 0
		baseModel[57].setRotationPoint(-4F, -16F, 47F);

		baseModel[58].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[58].setRotationPoint(31F, -8F, 27F);

		baseModel[59].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[59].setRotationPoint(31F, -12F, 31F);

		baseModel[60].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[60].setRotationPoint(31F, -16F, 27F);

		baseModel[61].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[61].setRotationPoint(31F, -20F, 31F);

		baseModel[62].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[62].setRotationPoint(31F, -16F, 39F);

		baseModel[63].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // Box 0
		baseModel[63].setRotationPoint(28F, -16F, 47F);

		baseModel[64].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[64].setRotationPoint(31F, -4F, 39F);

		baseModel[65].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // Box 0
		baseModel[65].setRotationPoint(28F, -4F, 47F);

		baseModel[66].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[66].setRotationPoint(31F, -24F, 23F);

		baseModel[67].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 0
		baseModel[67].setRotationPoint(31F, -4F, 23F);

		baseModel[68].addBox(0F, 0F, 0F, 38, 24, 1, 0F); // Box 70
		baseModel[68].setRotationPoint(-11F, -24F, 5F);

		baseModel[69].addBox(0F, 0F, 0F, 48, 32, 48, 0F); // BaseBottom
		baseModel[69].setRotationPoint(-16F, 16F, 0F);

		baseModel[70].addBox(0F, 0F, 0F, 38, 24, 1, 0F); // Box 70
		baseModel[70].setRotationPoint(-11F, -24F, 42F);

		baseModel[71].addBox(0F, 0F, 0F, 1, 24, 36, 0F); // Box 70
		baseModel[71].setRotationPoint(-11F, -24F, 6F);

		baseModel[72].addBox(0F, 0F, 0F, 1, 24, 36, 0F); // Box 70
		baseModel[72].setRotationPoint(26F, -24F, 6F);

		baseModel[73].addBox(0F, 0F, 0F, 1, 1, 36, 0F); // Box 70
		baseModel[73].setRotationPoint(-11F, -28F, 6F);

		baseModel[74].addBox(0F, 0F, 0F, 1, 1, 36, 0F); // Box 70
		baseModel[74].setRotationPoint(-11F, -25F, 6F);

		baseModel[75].addBox(0F, 0F, 0F, 1, 2, 4, 0F); // Box 70
		baseModel[75].setRotationPoint(-11F, -27F, 6F);

		baseModel[76].addBox(0F, 0F, 0F, 1, 2, 4, 0F); // Box 70
		baseModel[76].setRotationPoint(-11F, -27F, 38F);

		baseModel[77].addBox(0F, 0F, 0F, 1, 2, 8, 0F); // Box 70
		baseModel[77].setRotationPoint(-11F, -27F, 26F);

		baseModel[78].addBox(0F, 0F, 0F, 1, 2, 8, 0F); // Box 70
		baseModel[78].setRotationPoint(-11F, -27F, 14F);

		baseModel[79].addBox(0F, 0F, 0F, 1, 1, 36, 0F); // Box 70
		baseModel[79].setRotationPoint(26F, -28F, 6F);

		baseModel[80].addBox(0F, 0F, 0F, 1, 1, 36, 0F); // Box 70
		baseModel[80].setRotationPoint(26F, -25F, 6F);

		baseModel[81].addBox(0F, 0F, 0F, 1, 2, 4, 0F); // Box 70
		baseModel[81].setRotationPoint(26F, -27F, 6F);

		baseModel[82].addBox(0F, 0F, 0F, 1, 2, 4, 0F); // Box 70
		baseModel[82].setRotationPoint(26F, -27F, 38F);

		baseModel[83].addBox(0F, 0F, 0F, 1, 2, 8, 0F); // Box 70
		baseModel[83].setRotationPoint(26F, -27F, 26F);

		baseModel[84].addBox(0F, 0F, 0F, 1, 2, 8, 0F); // Box 70
		baseModel[84].setRotationPoint(26F, -27F, 14F);

		baseModel[85].addBox(0F, 0F, 0F, 38, 1, 1, 0F); // Box 70
		baseModel[85].setRotationPoint(-11F, -28F, 42F);

		baseModel[86].addBox(0F, 0F, 0F, 38, 1, 1, 0F); // Box 70
		baseModel[86].setRotationPoint(-11F, -25F, 42F);

		baseModel[87].addBox(0F, 0F, 0F, 5, 2, 1, 0F); // Box 70
		baseModel[87].setRotationPoint(-11F, -27F, 42F);

		baseModel[88].addBox(0F, 0F, 0F, 5, 2, 1, 0F); // Box 70
		baseModel[88].setRotationPoint(22F, -27F, 42F);

		baseModel[89].addBox(0F, 0F, 0F, 8, 2, 1, 0F); // Box 70
		baseModel[89].setRotationPoint(-2F, -27F, 42F);

		baseModel[90].addBox(0F, 0F, 0F, 8, 2, 1, 0F); // Box 70
		baseModel[90].setRotationPoint(10F, -27F, 42F);

		baseModel[91].addBox(0F, 0F, 0F, 38, 4, 1, 0F); // Box 70
		baseModel[91].setRotationPoint(-11F, -28F, 5F);

		baseModel[92].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[92].setRotationPoint(-3F, -27.99F, 43F);

		baseModel[93].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[93].setRotationPoint(12F, -27.99F, 43F);

		baseModel[94].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[94].setRotationPoint(20F, -27.99F, 43F);
		baseModel[94].rotateAngleY = -0.06981317F;

		baseModel[95].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[95].setRotationPoint(-15F, -27.99F, 43F);
		baseModel[95].rotateAngleY = -0.06981317F;

		baseModel[96].addBox(0F, 0F, 0F, 4, 4, 8, 0F); // Box 0
		baseModel[96].setRotationPoint(-15F, -27.99F, 28F);
		baseModel[96].rotateAngleY = -0.05235988F;

		baseModel[97].addBox(0F, 0F, 0F, 4, 4, 8, 0F); // Box 0
		baseModel[97].setRotationPoint(-15F, -27.99F, 15F);
		baseModel[97].rotateAngleY = 0.05235988F;

		baseModel[98].addBox(0F, 0F, 0F, 4, 4, 8, 0F); // Box 0
		baseModel[98].setRotationPoint(-15F, -27.99F, 7F);

		baseModel[99].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[99].setRotationPoint(-3F, -27.99F, 1F);

		baseModel[100].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[100].setRotationPoint(12F, -27.99F, 1F);

		baseModel[101].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[101].setRotationPoint(20F, -27.99F, 0F);
		baseModel[101].rotateAngleY = 0.06981317F;

		baseModel[102].addBox(0F, 0F, 0F, 4, 4, 8, 0F); // Box 0
		baseModel[102].setRotationPoint(27F, -27.99F, 34F);
		baseModel[102].rotateAngleY = -0.05235988F;

		baseModel[103].addBox(0F, 0F, 0F, 4, 4, 8, 0F); // Box 0
		baseModel[103].setRotationPoint(27F, -27.99F, 13F);
		baseModel[103].rotateAngleY = 0.05235988F;

		baseModel[104].addBox(0F, 0F, 0F, 4, 4, 8, 0F); // Box 0
		baseModel[104].setRotationPoint(27F, -27.99F, 5F);

		baseModel[105].addBox(0F, 0F, 0F, 1, 16, 48, 0F); // BaseBottom
		baseModel[105].setRotationPoint(-16F, 0F, 0F);

		baseModel[106].addBox(0F, 0F, 0F, 1, 16, 48, 0F); // BaseBottom
		baseModel[106].setRotationPoint(31F, 0F, 0F);

		baseModel[107].addBox(0F, 0F, 0F, 46, 16, 1, 0F); // BaseBottom
		baseModel[107].setRotationPoint(-15F, 0F, 0F);

		baseModel[108].addBox(0F, 0F, 0F, 46, 16, 1, 0F); // BaseBottom
		baseModel[108].setRotationPoint(-15F, 0F, 47F);


		doorRightModel = new ModelRendererTurbo[3];
		doorRightModel[0] = new ModelRendererTurbo(this, 50, 130, textureX, textureY); // Lid
		doorRightModel[1] = new ModelRendererTurbo(this, 128, 1, textureX, textureY); // Lid
		doorRightModel[2] = new ModelRendererTurbo(this, 128, 1, textureX, textureY); // Lid

		doorRightModel[0].addBox(0F, 0F, 0F, 38, 1, 14, 0F); // Lid
		doorRightModel[0].setRotationPoint(-11F, -29F, 5F);

		doorRightModel[1].addBox(0F, 0F, 14F, 14, 1, 5, 0F); // Lid
		doorRightModel[1].setRotationPoint(-11F, -29F, 5F);

		doorRightModel[2].addBox(24F, 0F, 14F, 14, 1, 5, 0F); // Lid
		doorRightModel[2].setRotationPoint(-11F, -29F, 5F);


		doorLeftModel = new ModelRendererTurbo[3];
		doorLeftModel[0] = new ModelRendererTurbo(this, 152, 0, textureX, textureY); // Lid
		doorLeftModel[1] = new ModelRendererTurbo(this, 128, 1, textureX, textureY); // Lid
		doorLeftModel[2] = new ModelRendererTurbo(this, 128, 1, textureX, textureY); // Lid

		doorLeftModel[0].addBox(0F, 0F, -13F, 38, 1, 14, 0F); // Lid
		doorLeftModel[0].setRotationPoint(-11F, -29F, 42F);

		doorLeftModel[1].addBox(0F, 0F, -18F, 14, 1, 5, 0F); // Lid
		doorLeftModel[1].setRotationPoint(-11F, -29F, 42F);

		doorLeftModel[2].addBox(24F, 0F, -18F, 14, 1, 5, 0F); // Lid
		doorLeftModel[2].setRotationPoint(-11F, -29F, 42F);


		platformModel = new ModelRendererTurbo[4];
		platformModel[0] = new ModelRendererTurbo(this, 80, 33, textureX, textureY); // PlatformHolder
		platformModel[1] = new ModelRendererTurbo(this, 232, 46, textureX, textureY); // PlatformHolderRod
		platformModel[2] = new ModelRendererTurbo(this, 110, 15, textureX, textureY); // Platform
		platformModel[3] = new ModelRendererTurbo(this, 101, 0, textureX, textureY); // PlatformHolderBottom

		platformModel[0].addBox(0F, 0F, 0F, 8, 2, 8, 0F); // PlatformHolder
		platformModel[0].setRotationPoint(4F, -48F, 20F);

		platformModel[1].addBox(0F, 0F, 0F, 6, 44, 6, 0F); // PlatformHolderRod
		platformModel[1].setRotationPoint(5F, -45F, 21F);

		platformModel[2].addBox(0F, 0F, 0F, 32, 1, 32, 0F); // Platform
		platformModel[2].setRotationPoint(-8F, -49F, 8F);

		platformModel[3].addShapeBox(0F, 0F, 0F, 8, 1, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F); // PlatformHolderBottom
		platformModel[3].setRotationPoint(4F, -46F, 20F);

		parts.put("base", baseModel);
		parts.put("door_left", doorLeftModel);
		parts.put("door_right", doorRightModel);
		parts.put("platform", platformModel);

		flipAll();
	}

	@Override
	public void getBlockRotation(EnumFacing facing, boolean mirrored)
	{
		switch(facing)
		{
			case NORTH:
				GlStateManager.translate(1, 0, -2);
				break;
			case SOUTH:
				GlStateManager.translate(2, 0, 1);
				break;
			case EAST:
				GlStateManager.translate(3, 0, -1);
				break;
			case WEST:
			default:
				break;
		}
		GlStateManager.rotate(360-facing.getHorizontalAngle(), 0F, 1F, 0F);
	}
}
