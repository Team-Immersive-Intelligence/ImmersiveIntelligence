package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 06.04.2021
 */
public class ModelVehicleWorkshop extends ModelIIBase
{
	public final ModelRendererTurbo[] platformModel, scissor1Model, scissor2Model, drawer1Model, drawer2Model, engineModel, craneShaftModel, engineShaftModel, doorLeftModel, doorRightModel, railModel, winchModel;
	int textureX = 512;
	int textureY = 256;

	public ModelVehicleWorkshop() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[140];
		baseModel[0] = new ModelRendererTurbo(this, 258, 0, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[2] = new ModelRendererTurbo(this, 44, 162, textureX, textureY); // Box 0
		baseModel[3] = new ModelRendererTurbo(this, 200, 154, textureX, textureY); // Box 4
		baseModel[4] = new ModelRendererTurbo(this, 15, 228, textureX, textureY); // Box 4
		baseModel[5] = new ModelRendererTurbo(this, 0, 67, textureX, textureY); // Box 4
		baseModel[6] = new ModelRendererTurbo(this, 0, 67, textureX, textureY); // Box 4
		baseModel[7] = new ModelRendererTurbo(this, 152, 104, textureX, textureY); // Box 4
		baseModel[8] = new ModelRendererTurbo(this, 0, 67, textureX, textureY); // Box 4
		baseModel[9] = new ModelRendererTurbo(this, 110, 219, textureX, textureY); // Box 4
		baseModel[10] = new ModelRendererTurbo(this, 108, 202, textureX, textureY); // Box 4
		baseModel[11] = new ModelRendererTurbo(this, 226, 71, textureX, textureY); // Box 4
		baseModel[12] = new ModelRendererTurbo(this, 0, 106, textureX, textureY); // Box 4
		baseModel[13] = new ModelRendererTurbo(this, 0, 106, textureX, textureY); // Box 4
		baseModel[14] = new ModelRendererTurbo(this, 262, 174, textureX, textureY); // Box 4
		baseModel[15] = new ModelRendererTurbo(this, 262, 174, textureX, textureY); // Box 4
		baseModel[16] = new ModelRendererTurbo(this, 0, 144, textureX, textureY); // Box 4
		baseModel[17] = new ModelRendererTurbo(this, 96, 60, textureX, textureY); // Box 4
		baseModel[18] = new ModelRendererTurbo(this, 188, 204, textureX, textureY); // Box 4
		baseModel[19] = new ModelRendererTurbo(this, 8, 179, textureX, textureY); // Box 4
		baseModel[20] = new ModelRendererTurbo(this, 8, 179, textureX, textureY); // Box 4
		baseModel[21] = new ModelRendererTurbo(this, 96, 104, textureX, textureY); // Box 4
		baseModel[22] = new ModelRendererTurbo(this, 96, 104, textureX, textureY); // Box 4
		baseModel[23] = new ModelRendererTurbo(this, 128, 104, textureX, textureY); // Box 35
		baseModel[24] = new ModelRendererTurbo(this, 96, 64, textureX, textureY); // Box 35
		baseModel[25] = new ModelRendererTurbo(this, 0, 96, textureX, textureY); // Box 35
		baseModel[26] = new ModelRendererTurbo(this, 0, 48, textureX, textureY); // Box 35
		baseModel[27] = new ModelRendererTurbo(this, 110, 182, textureX, textureY); // Box 0
		baseModel[28] = new ModelRendererTurbo(this, 110, 182, textureX, textureY); // Box 0
		baseModel[29] = new ModelRendererTurbo(this, 110, 182, textureX, textureY); // Box 0
		baseModel[30] = new ModelRendererTurbo(this, 110, 182, textureX, textureY); // Box 0
		baseModel[31] = new ModelRendererTurbo(this, 132, 182, textureX, textureY); // Box 0
		baseModel[32] = new ModelRendererTurbo(this, 108, 206, textureX, textureY); // Box 0
		baseModel[33] = new ModelRendererTurbo(this, 16, 2, textureX, textureY); // Box 0
		baseModel[34] = new ModelRendererTurbo(this, 4, 144, textureX, textureY); // Box 0
		baseModel[35] = new ModelRendererTurbo(this, 24, 96, textureX, textureY); // Box 0
		baseModel[36] = new ModelRendererTurbo(this, 119, 189, textureX, textureY); // Box 0
		baseModel[37] = new ModelRendererTurbo(this, 202, 145, textureX, textureY); // Box 0
		baseModel[38] = new ModelRendererTurbo(this, 190, 144, textureX, textureY); // Box 0
		baseModel[39] = new ModelRendererTurbo(this, 96, 14, textureX, textureY); // Box 0
		baseModel[40] = new ModelRendererTurbo(this, 4, 96, textureX, textureY); // Box 0
		baseModel[41] = new ModelRendererTurbo(this, 96, 18, textureX, textureY); // Box 0
		baseModel[42] = new ModelRendererTurbo(this, 96, 22, textureX, textureY); // Box 0
		baseModel[43] = new ModelRendererTurbo(this, 111, 44, textureX, textureY); // Box 0
		baseModel[44] = new ModelRendererTurbo(this, 168, 144, textureX, textureY); // Box 0
		baseModel[45] = new ModelRendererTurbo(this, 96, 64, textureX, textureY); // Box 0
		baseModel[46] = new ModelRendererTurbo(this, 96, 60, textureX, textureY); // Box 0
		baseModel[47] = new ModelRendererTurbo(this, 182, 144, textureX, textureY); // Box 0
		baseModel[48] = new ModelRendererTurbo(this, 0, 52, textureX, textureY); // Box 0
		baseModel[49] = new ModelRendererTurbo(this, 92, 182, textureX, textureY); // Box 0
		baseModel[50] = new ModelRendererTurbo(this, 254, 15, textureX, textureY); // Box 0
		baseModel[51] = new ModelRendererTurbo(this, 226, 64, textureX, textureY); // Box 0
		baseModel[52] = new ModelRendererTurbo(this, 226, 68, textureX, textureY); // Box 0
		baseModel[53] = new ModelRendererTurbo(this, 226, 59, textureX, textureY); // Box 0
		baseModel[54] = new ModelRendererTurbo(this, 226, 64, textureX, textureY); // Box 0
		baseModel[55] = new ModelRendererTurbo(this, 226, 68, textureX, textureY); // Box 0
		baseModel[56] = new ModelRendererTurbo(this, 226, 59, textureX, textureY); // Box 0
		baseModel[57] = new ModelRendererTurbo(this, 60, 144, textureX, textureY); // Box 0
		baseModel[58] = new ModelRendererTurbo(this, 60, 144, textureX, textureY); // Box 0
		baseModel[59] = new ModelRendererTurbo(this, 0, 48, textureX, textureY); // Box 0
		baseModel[60] = new ModelRendererTurbo(this, 0, 48, textureX, textureY); // Box 0
		baseModel[61] = new ModelRendererTurbo(this, 2, 44, textureX, textureY); // Box 0
		baseModel[62] = new ModelRendererTurbo(this, 2, 44, textureX, textureY); // Box 0
		baseModel[63] = new ModelRendererTurbo(this, 168, 144, textureX, textureY); // Box 0
		baseModel[64] = new ModelRendererTurbo(this, 96, 64, textureX, textureY); // Box 0
		baseModel[65] = new ModelRendererTurbo(this, 182, 144, textureX, textureY); // Box 0
		baseModel[66] = new ModelRendererTurbo(this, 168, 144, textureX, textureY); // Box 0
		baseModel[67] = new ModelRendererTurbo(this, 96, 64, textureX, textureY); // Box 0
		baseModel[68] = new ModelRendererTurbo(this, 182, 144, textureX, textureY); // Box 0
		baseModel[69] = new ModelRendererTurbo(this, 2, 18, textureX, textureY); // Box 0
		baseModel[70] = new ModelRendererTurbo(this, 2, 18, textureX, textureY); // Box 0
		baseModel[71] = new ModelRendererTurbo(this, 24, 61, textureX, textureY); // Box 0
		baseModel[72] = new ModelRendererTurbo(this, 5, 116, textureX, textureY); // Box 0
		baseModel[73] = new ModelRendererTurbo(this, 18, 10, textureX, textureY); // Box 0
		baseModel[74] = new ModelRendererTurbo(this, 226, 14, textureX, textureY); // Box 4
		baseModel[75] = new ModelRendererTurbo(this, 222, 174, textureX, textureY); // Box 4
		baseModel[76] = new ModelRendererTurbo(this, 222, 174, textureX, textureY); // Box 4
		baseModel[77] = new ModelRendererTurbo(this, 124, 182, textureX, textureY); // Box 4
		baseModel[78] = new ModelRendererTurbo(this, 12, 106, textureX, textureY); // Box 4
		baseModel[79] = new ModelRendererTurbo(this, 60, 152, textureX, textureY); // Box 4
		baseModel[80] = new ModelRendererTurbo(this, 12, 106, textureX, textureY); // Box 4
		baseModel[81] = new ModelRendererTurbo(this, 60, 152, textureX, textureY); // Box 4
		baseModel[82] = new ModelRendererTurbo(this, 156, 144, textureX, textureY); // Box 4
		baseModel[83] = new ModelRendererTurbo(this, 156, 144, textureX, textureY); // Box 4
		baseModel[84] = new ModelRendererTurbo(this, 96, 5, textureX, textureY); // Box 4
		baseModel[85] = new ModelRendererTurbo(this, 106, 60, textureX, textureY); // Box 4
		baseModel[86] = new ModelRendererTurbo(this, 106, 60, textureX, textureY); // Box 4
		baseModel[87] = new ModelRendererTurbo(this, 0, 18, textureX, textureY); // Box 0
		baseModel[88] = new ModelRendererTurbo(this, 144, 127, textureX, textureY); // Box 0
		baseModel[89] = new ModelRendererTurbo(this, 144, 127, textureX, textureY); // Box 0
		baseModel[90] = new ModelRendererTurbo(this, 96, 104, textureX, textureY); // Box 4
		baseModel[91] = new ModelRendererTurbo(this, 116, 63, textureX, textureY); // Box 4
		baseModel[92] = new ModelRendererTurbo(this, 192, 39, textureX, textureY); // Box 4
		baseModel[93] = new ModelRendererTurbo(this, 131, 191, textureX, textureY); // Box 4
		baseModel[94] = new ModelRendererTurbo(this, 131, 191, textureX, textureY); // Box 4
		baseModel[95] = new ModelRendererTurbo(this, 131, 191, textureX, textureY); // Box 4
		baseModel[96] = new ModelRendererTurbo(this, 131, 191, textureX, textureY); // Box 4
		baseModel[97] = new ModelRendererTurbo(this, 192, 39, textureX, textureY); // Box 4
		baseModel[98] = new ModelRendererTurbo(this, 26, 190, textureX, textureY); // Box 4
		baseModel[99] = new ModelRendererTurbo(this, 26, 190, textureX, textureY); // Box 4
		baseModel[100] = new ModelRendererTurbo(this, 128, 0, textureX, textureY); // Box 35
		baseModel[101] = new ModelRendererTurbo(this, 60, 206, textureX, textureY); // Box 35
		baseModel[102] = new ModelRendererTurbo(this, 96, 19, textureX, textureY); // Box 4
		baseModel[103] = new ModelRendererTurbo(this, 8, 61, textureX, textureY); // Box 4
		baseModel[104] = new ModelRendererTurbo(this, 92, 162, textureX, textureY); // Box 4
		baseModel[105] = new ModelRendererTurbo(this, 6, 44, textureX, textureY); // Box 4
		baseModel[106] = new ModelRendererTurbo(this, 96, 44, textureX, textureY); // Box 0
		baseModel[107] = new ModelRendererTurbo(this, 6, 7, textureX, textureY); // Box 0
		baseModel[108] = new ModelRendererTurbo(this, 111, 73, textureX, textureY); // Box 0
		baseModel[109] = new ModelRendererTurbo(this, 76, 144, textureX, textureY); // Box 4
		baseModel[110] = new ModelRendererTurbo(this, 176, 0, textureX, textureY); // Box 4
		baseModel[111] = new ModelRendererTurbo(this, 224, 92, textureX, textureY); // Box 4
		baseModel[112] = new ModelRendererTurbo(this, 224, 92, textureX, textureY); // Box 4
		baseModel[113] = new ModelRendererTurbo(this, 4, 96, textureX, textureY); // Box 4
		baseModel[114] = new ModelRendererTurbo(this, 18, 44, textureX, textureY); // Box 4
		baseModel[115] = new ModelRendererTurbo(this, 18, 44, textureX, textureY); // Box 4
		baseModel[116] = new ModelRendererTurbo(this, 300, 47, textureX, textureY); // Box 4
		baseModel[117] = new ModelRendererTurbo(this, 314, 47, textureX, textureY); // Box 4
		baseModel[118] = new ModelRendererTurbo(this, 310, 47, textureX, textureY); // Box 4
		baseModel[119] = new ModelRendererTurbo(this, 300, 47, textureX, textureY); // Box 4
		baseModel[120] = new ModelRendererTurbo(this, 314, 47, textureX, textureY); // Box 4
		baseModel[121] = new ModelRendererTurbo(this, 310, 47, textureX, textureY); // Box 4
		baseModel[122] = new ModelRendererTurbo(this, 274, 61, textureX, textureY); // Box 4
		baseModel[123] = new ModelRendererTurbo(this, 290, 61, textureX, textureY); // Box 4
		baseModel[124] = new ModelRendererTurbo(this, 290, 73, textureX, textureY); // Box 4
		baseModel[125] = new ModelRendererTurbo(this, 297, 89, textureX, textureY); // Box 4
		baseModel[126] = new ModelRendererTurbo(this, 310, 73, textureX, textureY); // Box 4
		baseModel[127] = new ModelRendererTurbo(this, 318, 81, textureX, textureY); // Box 4
		baseModel[128] = new ModelRendererTurbo(this, 286, 73, textureX, textureY); // Box 4
		baseModel[129] = new ModelRendererTurbo(this, 309, 86, textureX, textureY); // Box 4
		baseModel[130] = new ModelRendererTurbo(this, 309, 95, textureX, textureY); // Box 4
		baseModel[131] = new ModelRendererTurbo(this, 285, 90, textureX, textureY); // Box 4
		baseModel[132] = new ModelRendererTurbo(this, 318, 81, textureX, textureY); // Box 4
		baseModel[133] = new ModelRendererTurbo(this, 324, 73, textureX, textureY); // Box 4
		baseModel[134] = new ModelRendererTurbo(this, 256, 104, textureX, textureY); // Box 4
		baseModel[135] = new ModelRendererTurbo(this, 226, 80, textureX, textureY); // Box 4
		baseModel[136] = new ModelRendererTurbo(this, 192, 87, textureX, textureY); // Box 4
		baseModel[137] = new ModelRendererTurbo(this, 226, 77, textureX, textureY); // Box 4
		baseModel[138] = new ModelRendererTurbo(this, 306, 0, textureX, textureY); // Box 4
		baseModel[139] = new ModelRendererTurbo(this, 26, 44, textureX, textureY); // Box 4

		baseModel[0].addBox(0F, 0F, 0F, 8, 12, 32, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, -4F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 32, 12, 32, 0F); // Box 0
		baseModel[1].setRotationPoint(16F, -4F, 0F);

		baseModel[2].addBox(0F, 0F, 0F, 8, 12, 32, 0F); // Box 0
		baseModel[2].setRotationPoint(56F, -4F, 0F);

		baseModel[3].addBox(0F, 0F, 0F, 32, 4, 16, 0F); // Box 4
		baseModel[3].setRotationPoint(0F, -4F, 32F);

		baseModel[4].addBox(0F, 0F, 0F, 32, 8, 16, 0F); // Box 4
		baseModel[4].setRotationPoint(32F, -8F, 32F);

		baseModel[5].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 4
		baseModel[5].setRotationPoint(32F, -15F, 32F);

		baseModel[6].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 4
		baseModel[6].setRotationPoint(62F, -15F, 32F);

		baseModel[7].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 4
		baseModel[7].setRotationPoint(32F, -15F, 46F);

		baseModel[8].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 4
		baseModel[8].setRotationPoint(62F, -15F, 46F);

		baseModel[9].addBox(0F, 0F, 0F, 30, 7, 14, 0F); // Box 4
		baseModel[9].setRotationPoint(33F, -15F, 33F);

		baseModel[10].addBox(0F, 0F, 0F, 32, 1, 16, 0F); // Box 4
		baseModel[10].setRotationPoint(32F, -16F, 32F);

		baseModel[11].addBox(0F, 0F, 0F, 16, 3, 16, 0F); // Box 4
		baseModel[11].setRotationPoint(0F, -16F, 32F);

		baseModel[12].addBox(0F, 0F, 0F, 3, 9, 3, 0F); // Box 4
		baseModel[12].setRotationPoint(1F, -13F, 33F);

		baseModel[13].addBox(0F, 0F, 0F, 3, 9, 3, 0F); // Box 4
		baseModel[13].setRotationPoint(1F, -13F, 44F);

		baseModel[14].addBox(0F, 0F, 0F, 12, 1, 12, 0F); // Box 4
		baseModel[14].setRotationPoint(34F, -16F, 48F);

		baseModel[15].addBox(0F, 0F, 0F, 12, 1, 12, 0F); // Box 4
		baseModel[15].setRotationPoint(50F, -16F, 48F);

		baseModel[16].addBox(0F, 0F, 0F, 22, 15, 16, 0F); // Box 4
		baseModel[16].setRotationPoint(1F, -16F, 64F);

		baseModel[17].addBox(0F, 0F, 0F, 1, 8, 8, 0F); // Box 4
		baseModel[17].setRotationPoint(0F, -12F, 68F);

		baseModel[18].addBox(0F, 0F, 0F, 16, 1, 16, 0F); // Box 4
		baseModel[18].setRotationPoint(16F, -16F, 32F);
		baseModel[18].rotateAngleX = 0.06981317F;

		baseModel[19].addBox(0F, 0F, 0F, 1, 11, 16, 0F); // Box 4
		baseModel[19].setRotationPoint(16F, -15F, 32F);

		baseModel[20].addBox(0F, 0F, 0F, 1, 11, 16, 0F); // Box 4
		baseModel[20].setRotationPoint(31F, -15F, 32F);

		baseModel[21].addBox(0F, 0F, 0F, 14, 1, 16, 0F); // Box 4
		baseModel[21].setRotationPoint(17F, -5F, 32F);

		baseModel[22].addBox(0F, 0F, 0F, 14, 1, 16, 0F); // Box 4
		baseModel[22].setRotationPoint(17F, -10F, 32F);

		baseModel[23].addBox(0F, 0F, 0F, 32, 8, 32, 0F); // Box 35
		baseModel[23].setRotationPoint(0F, 8F, 0F);

		baseModel[24].addBox(0F, 0F, 0F, 32, 8, 32, 0F); // Box 35
		baseModel[24].setRotationPoint(32F, 8F, 0F);

		baseModel[25].addBox(0F, 0F, 0F, 32, 16, 32, 0F); // Box 35
		baseModel[25].setRotationPoint(0F, 0F, 32F);

		baseModel[26].addBox(0F, 0F, 0F, 32, 16, 32, 0F); // Box 35
		baseModel[26].setRotationPoint(32F, 0F, 32F);

		baseModel[27].addBox(0F, 0F, 0F, 8, 4, 3, 0F); // Box 0
		baseModel[27].setRotationPoint(8F, -2F, 0F);

		baseModel[28].addBox(0F, 0F, 0F, 8, 4, 3, 0F); // Box 0
		baseModel[28].setRotationPoint(8F, -2F, 29F);

		baseModel[29].addBox(0F, 0F, 0F, 8, 4, 3, 0F); // Box 0
		baseModel[29].setRotationPoint(48F, -2F, 0F);

		baseModel[30].addBox(0F, 0F, 0F, 8, 4, 3, 0F); // Box 0
		baseModel[30].setRotationPoint(48F, -2F, 29F);

		baseModel[31].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // Box 0
		baseModel[31].setRotationPoint(11F, 4F, 12F);

		baseModel[32].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[32].setRotationPoint(12F, 4F, 12F);

		baseModel[33].addBox(0F, 0F, 0F, 3, 3, 5, 0F); // Box 0
		baseModel[33].setRotationPoint(12F, 4F, 7F);

		baseModel[34].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[34].setRotationPoint(12F, 4F, 4F);

		baseModel[35].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // Box 0
		baseModel[35].setRotationPoint(15F, 4F, 4F);

		baseModel[36].addShapeBox(0F, 0F, 0F, 3, 2, 3, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[36].setRotationPoint(8F, 0F, 12F);

		baseModel[37].addShapeBox(0F, 0F, 0F, 3, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // Box 0
		baseModel[37].setRotationPoint(8F, 5F, 12F);

		baseModel[38].addBox(0F, 0F, 0F, 3, 3, 3, 0F); // Box 0
		baseModel[38].setRotationPoint(8F, 2F, 12F);

		baseModel[39].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[39].setRotationPoint(9F, 0F, 8F);

		baseModel[40].addBox(0F, 0F, 0F, 2, 6, 2, 0F); // Box 0
		baseModel[40].setRotationPoint(9F, 2F, 8F);

		baseModel[41].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[41].setRotationPoint(8F, 0F, 5F);

		baseModel[42].addBox(0F, 0F, 0F, 2, 3, 2, 0F); // Box 0
		baseModel[42].setRotationPoint(8F, 2F, 5F);

		baseModel[43].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F); // Box 0
		baseModel[43].setRotationPoint(8F, 5F, 5F);

		baseModel[44].addBox(0F, 0F, 0F, 5, 2, 2, 0F); // Box 0
		baseModel[44].setRotationPoint(10F, 5F, 1F);

		baseModel[45].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[45].setRotationPoint(8F, 5F, 1F);

		baseModel[46].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // Box 0
		baseModel[46].setRotationPoint(8F, 5F, 3F);

		baseModel[47].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // Box 0
		baseModel[47].setRotationPoint(15F, 4.5F, 0.5F);

		baseModel[48].addBox(0F, 0F, 0F, 1, 2, 2, 0F); // Box 0
		baseModel[48].setRotationPoint(8F, 0F, 8F);

		baseModel[49].addBox(0F, 0F, 0F, 6, 5, 3, 0F); // Box 0
		baseModel[49].setRotationPoint(8F, 2F, 16F);

		baseModel[50].addBox(0F, 0F, 0F, 6, 1, 12, 0F); // Box 0
		baseModel[50].setRotationPoint(8F, 7F, 19F);

		baseModel[51].addBox(-1.5F, -4F, -1.5F, 3, 1, 3, 0F); // Box 0
		baseModel[51].setRotationPoint(10F, 7F, 22F);
		baseModel[51].rotateAngleZ = -0.38397244F;

		baseModel[52].addBox(-2F, -3F, -2F, 4, 3, 4, 0F); // Box 0
		baseModel[52].setRotationPoint(10F, 7F, 22F);
		baseModel[52].rotateAngleZ = -0.38397244F;

		baseModel[53].addBox(-2F, -5F, -2F, 4, 1, 4, 0F); // Box 0
		baseModel[53].setRotationPoint(10F, 7F, 22F);
		baseModel[53].rotateAngleZ = -0.38397244F;

		baseModel[54].addBox(-1.5F, -4F, -1.5F, 3, 1, 3, 0F); // Box 0
		baseModel[54].setRotationPoint(10F, 7F, 27F);
		baseModel[54].rotateAngleZ = -0.38397244F;

		baseModel[55].addBox(-2F, -3F, -2F, 4, 3, 4, 0F); // Box 0
		baseModel[55].setRotationPoint(10F, 7F, 27F);
		baseModel[55].rotateAngleZ = -0.38397244F;

		baseModel[56].addBox(-2F, -5F, -2F, 4, 1, 4, 0F); // Box 0
		baseModel[56].setRotationPoint(10F, 7F, 27F);
		baseModel[56].rotateAngleZ = -0.38397244F;

		baseModel[57].addBox(0F, 0F, 0F, 5, 4, 4, 0F); // Box 0
		baseModel[57].setRotationPoint(50F, 4F, 25F);

		baseModel[58].addBox(0F, 0F, 0F, 5, 4, 4, 0F); // Box 0
		baseModel[58].setRotationPoint(50F, 4F, 20F);

		baseModel[59].addBox(0F, 0F, 0F, 1, 2, 2, 0F); // Box 0
		baseModel[59].setRotationPoint(55F, 5F, 26F);

		baseModel[60].addBox(0F, 0F, 0F, 1, 2, 2, 0F); // Box 0
		baseModel[60].setRotationPoint(55F, 5F, 21F);

		baseModel[61].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // Box 0
		baseModel[61].setRotationPoint(48F, 5F, 26F);

		baseModel[62].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // Box 0
		baseModel[62].setRotationPoint(48F, 5F, 21F);

		baseModel[63].addBox(0F, 0F, 0F, 5, 2, 2, 0F); // Box 0
		baseModel[63].setRotationPoint(50F, 5F, 14F);

		baseModel[64].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[64].setRotationPoint(48F, 5F, 14F);

		baseModel[65].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // Box 0
		baseModel[65].setRotationPoint(55F, 4.5F, 13.5F);

		baseModel[66].addBox(0F, 0F, 0F, 5, 2, 2, 0F); // Box 0
		baseModel[66].setRotationPoint(50F, 1F, 14F);

		baseModel[67].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[67].setRotationPoint(48F, 1F, 14F);

		baseModel[68].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // Box 0
		baseModel[68].setRotationPoint(55F, 0.5F, 13.5F);

		baseModel[69].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F); // Box 0
		baseModel[69].setRotationPoint(48F, 5F, 18F);

		baseModel[70].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F); // Box 0
		baseModel[70].setRotationPoint(48F, 1F, 23F);

		baseModel[71].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // Box 0
		baseModel[71].setRotationPoint(48F, 5F, 16F);

		baseModel[72].addBox(0F, 0F, 0F, 2, 2, 7, 0F); // Box 0
		baseModel[72].setRotationPoint(48F, 1F, 16F);

		baseModel[73].addBox(0F, 0F, 0F, 5, 6, 2, 0F); // Box 0
		baseModel[73].setRotationPoint(51F, 2F, 17F);

		baseModel[74].addBox(0F, 0F, 0F, 10, 3, 10, 0F); // Box 4
		baseModel[74].setRotationPoint(51F, -15F, 49F);

		baseModel[75].addBox(0F, 0F, 0F, 6, 5, 6, 0F); // Box 4
		baseModel[75].setRotationPoint(49F, -14F, 51F);

		baseModel[76].addBox(0F, 0F, 0F, 6, 5, 6, 0F); // Box 4
		baseModel[76].setRotationPoint(57F, -14F, 51F);

		baseModel[77].addBox(0F, 0F, 0F, 32, 4, 16, 0F); // Box 4
		baseModel[77].setRotationPoint(32F, -4F, 48F);

		baseModel[78].addShapeBox(0F, 0F, 0F, 4, 4, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // Box 4
		baseModel[78].setRotationPoint(58F, -8F, 56F);

		baseModel[79].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		baseModel[79].setRotationPoint(58F, -8F, 52F);

		baseModel[80].addShapeBox(0F, 0F, 0F, 4, 4, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // Box 4
		baseModel[80].setRotationPoint(50F, -8F, 56F);

		baseModel[81].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		baseModel[81].setRotationPoint(50F, -8F, 52F);

		baseModel[82].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // Box 4
		baseModel[82].setRotationPoint(58F, -9F, 52F);

		baseModel[83].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // Box 4
		baseModel[83].setRotationPoint(50F, -9F, 52F);

		baseModel[84].addBox(0F, 0F, 0F, 16, 8, 1, 0F); // Box 4
		baseModel[84].setRotationPoint(48F, -12F, 63F);

		baseModel[85].addBox(0F, 0F, 0F, 6, 6, 1, 0F); // Box 4
		baseModel[85].setRotationPoint(57F, -10F, 62F);

		baseModel[86].addBox(0F, 0F, 0F, 6, 6, 1, 0F); // Box 4
		baseModel[86].setRotationPoint(49F, -10F, 62F);

		baseModel[87].addBox(0F, 0F, 0F, 6, 4, 10, 0F); // Box 0
		baseModel[87].setRotationPoint(49F, 4F, 3F);

		baseModel[88].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // Box 0
		baseModel[88].setRotationPoint(50F, 3F, 3.5F);

		baseModel[89].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // Box 0
		baseModel[89].setRotationPoint(50F, 3F, 8.5F);

		baseModel[90].addBox(0F, 0F, 0F, 14, 1, 16, 0F); // Box 4
		baseModel[90].setRotationPoint(17F, -15F, 32F);

		baseModel[91].addBox(0F, 0F, 0F, 1, 3, 4, 0F); // Box 4
		baseModel[91].setRotationPoint(0F, -15F, 70F);

		baseModel[92].addBox(0F, 0F, 0F, 30, 17, 1, 0F); // Box 4
		baseModel[92].setRotationPoint(1F, -17F, 48F);

		baseModel[93].addBox(0F, 0F, 0F, 2, 1, 2, 0F); // Box 4
		baseModel[93].setRotationPoint(2F, -1F, 65F);

		baseModel[94].addBox(0F, 0F, 0F, 2, 1, 2, 0F); // Box 4
		baseModel[94].setRotationPoint(2F, -1F, 77F);

		baseModel[95].addBox(0F, 0F, 0F, 2, 1, 2, 0F); // Box 4
		baseModel[95].setRotationPoint(20F, -1F, 65F);

		baseModel[96].addBox(0F, 0F, 0F, 2, 1, 2, 0F); // Box 4
		baseModel[96].setRotationPoint(20F, -1F, 77F);

		baseModel[97].addBox(0F, 0F, 0F, 30, 17, 1, 0F); // Box 4
		baseModel[97].setRotationPoint(1F, -17F, 63F);

		baseModel[98].addBox(0F, 0F, 0F, 1, 17, 16, 0F); // Box 4
		baseModel[98].setRotationPoint(0F, -17F, 48F);

		baseModel[99].addBox(0F, 0F, 0F, 1, 17, 16, 0F); // Box 4
		baseModel[99].setRotationPoint(31F, -17F, 48F);

		baseModel[100].addBox(0F, 0F, 0F, 16, 46, 16, 0F); // Box 35
		baseModel[100].setRotationPoint(48F, -30F, 64F);

		baseModel[101].addShapeBox(0F, 0F, 0F, 16, 2, 16, 0F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 35
		baseModel[101].setRotationPoint(48F, -32F, 64F);

		baseModel[102].addBox(0F, 0F, 0F, 6, 5, 8, 0F); // Box 4
		baseModel[102].setRotationPoint(40F, -15F, 48F);

		baseModel[103].addBox(0F, 0F, 0F, 4, 11, 8, 0F); // Box 4
		baseModel[103].setRotationPoint(34F, -15F, 48F);

		baseModel[104].addBox(0F, 0F, 0F, 25, 4, 16, 0F); // Box 4
		baseModel[104].setRotationPoint(23F, -4F, 64F);

		baseModel[105].addBox(0F, 0F, 0F, 1, 8, 4, 0F); // Box 4
		baseModel[105].setRotationPoint(26F, -12F, 71F);

		baseModel[106].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // Box 0
		baseModel[106].setRotationPoint(23F, -8F, 71F);

		baseModel[107].addBox(0F, 0F, 0F, 3, 3, 3, 0F); // Box 0
		baseModel[107].setRotationPoint(23F, -8F, 68F);

		baseModel[108].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[108].setRotationPoint(23F, -8F, 65F);

		baseModel[109].addBox(0F, 0F, 0F, 32, 2, 16, 0F); // Box 4
		baseModel[109].setRotationPoint(0F, -48F, 48F);

		baseModel[110].addBox(0F, 0F, 0F, 32, 8, 1, 0F); // Box 4
		baseModel[110].setRotationPoint(0F, -25F, 63F);

		baseModel[111].addBox(0F, 0F, 0F, 1, 29, 15, 0F); // Box 4
		baseModel[111].setRotationPoint(0F, -46F, 48F);

		baseModel[112].addBox(0F, 0F, 0F, 1, 29, 15, 0F); // Box 4
		baseModel[112].setRotationPoint(31F, -46F, 48F);

		baseModel[113].addBox(0F, 0F, 0F, 6, 2, 8, 0F); // Box 4
		baseModel[113].setRotationPoint(40F, -6F, 48F);

		baseModel[114].addBox(0F, 0F, 0F, 1, 8, 6, 0F); // Box 4
		baseModel[114].setRotationPoint(43F, -12F, 70F);

		baseModel[115].addBox(0F, 0F, 0F, 1, 8, 6, 0F); // Box 4
		baseModel[115].setRotationPoint(47F, -12F, 70F);

		baseModel[116].addBox(0F, 0F, -1F, 4, 9, 1, 0F); // Box 4
		baseModel[116].setRotationPoint(23F, -9F, 63F);

		baseModel[117].addBox(0F, 0F, 0F, 1, 9, 1, 0F); // Box 4
		baseModel[117].setRotationPoint(23F, -9F, 61F);

		baseModel[118].addBox(0F, 0F, 0F, 1, 9, 1, 0F); // Box 4
		baseModel[118].setRotationPoint(26F, -9F, 61F);

		baseModel[119].addBox(0F, 0F, -1F, 4, 9, 1, 0F); // Box 4
		baseModel[119].setRotationPoint(5F, -9F, 63F);

		baseModel[120].addBox(0F, 0F, 0F, 1, 9, 1, 0F); // Box 4
		baseModel[120].setRotationPoint(5F, -9F, 61F);

		baseModel[121].addBox(0F, 0F, 0F, 1, 9, 1, 0F); // Box 4
		baseModel[121].setRotationPoint(8F, -9F, 61F);

		baseModel[122].addBox(0F, 0F, 0F, 2, 6, 6, 0F); // Box 4
		baseModel[122].setRotationPoint(29F, -46F, 49F);

		baseModel[123].addBox(0F, 0F, 0F, 1, 6, 6, 0F); // Box 4
		baseModel[123].setRotationPoint(2F, -46F, 49F);

		baseModel[124].addBox(0F, 0F, 0F, 4, 8, 8, 0F); // Box 4
		baseModel[124].setRotationPoint(32F, -46F, 49F);

		baseModel[125].addBox(0F, 0F, 0F, 2, 8, 4, 0F); // Box 4
		baseModel[125].setRotationPoint(32F, -41F, 58F);

		baseModel[126].addShapeBox(0F, 0F, 0F, 2, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		baseModel[126].setRotationPoint(32F, -45F, 58F);

		baseModel[127].addBox(0F, 0F, 0F, 2, 4, 1, 0F); // Box 4
		baseModel[127].setRotationPoint(32F, -45F, 57F);

		baseModel[128].addShapeBox(0F, 0F, 0F, 2, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // Box 4
		baseModel[128].setRotationPoint(32F, -33F, 58F);

		baseModel[129].addBox(0F, 0F, 0F, 2, 4, 5, 0F); // Box 4
		baseModel[129].setRotationPoint(32F, -33F, 53F);

		baseModel[130].addShapeBox(0F, 0F, 0F, 2, 4, 4, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		baseModel[130].setRotationPoint(32F, -33F, 49F);

		baseModel[131].addShapeBox(0F, 0F, 0F, 2, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // Box 4
		baseModel[131].setRotationPoint(32F, -12F, 49F);

		baseModel[132].addBox(0F, 0F, 0F, 2, 4, 1, 0F); // Box 4
		baseModel[132].setRotationPoint(32F, -12F, 48F);

		baseModel[133].addBox(0F, 0F, 0F, 2, 17, 4, 0F); // Box 4
		baseModel[133].setRotationPoint(32F, -29F, 49F);

		baseModel[134].addBox(0F, 0F, 0F, 32, 19, 1, 0F); // Box 4
		baseModel[134].setRotationPoint(0F, -46F, 63F);

		baseModel[135].addBox(0F, 0F, 0F, 4, 2, 1, 0F); // Box 4
		baseModel[135].setRotationPoint(0F, -27F, 63F);

		baseModel[136].addBox(0F, 0F, 0F, 12, 2, 1, 0F); // Box 4
		baseModel[136].setRotationPoint(10F, -27F, 63F);

		baseModel[137].addBox(0F, 0F, 0F, 4, 2, 1, 0F); // Box 4
		baseModel[137].setRotationPoint(28F, -27F, 63F);

		baseModel[138].addShapeBox(0F, 0F, 0F, 48, 32, 0, 0F, -9F, -6F, 0F, -9F, -6F, 0F, -9F, -6F, 0F, -9F, -6F, 0F, -9F, -6F, 0F, -9F, -6F, 0F, -9F, -6F, 0F, -9F, -6F, 0F); // Box 4
		baseModel[138].setRotationPoint(-10F, -48F, 64.05F);
		baseModel[138].rotateAngleZ = 0.15707963F;

		baseModel[139].addBox(0F, 0F, -0.5F, 24, 2, 2, 0F); // Box 4
		baseModel[139].setRotationPoint(0F, -48F, 46.5F);


		platformModel = new ModelRendererTurbo[2];
		platformModel[0] = new ModelRendererTurbo(this, 222, 174, textureX, textureY); // Box 0
		platformModel[1] = new ModelRendererTurbo(this, 222, 174, textureX, textureY); // Box 0

		platformModel[0].addBox(0F, 0F, 0F, 8, 1, 24, 0F); // Box 0
		platformModel[0].setRotationPoint(48F, -4F, 4F);

		platformModel[1].addBox(0F, 0F, 0F, 8, 1, 24, 0F); // Box 0
		platformModel[1].setRotationPoint(8F, -4F, 4F);


		scissor1Model = new ModelRendererTurbo[4];
		scissor1Model[0] = new ModelRendererTurbo(this, 196, 178, textureX, textureY); // Box 0
		scissor1Model[1] = new ModelRendererTurbo(this, 196, 178, textureX, textureY); // Box 0
		scissor1Model[2] = new ModelRendererTurbo(this, 196, 178, textureX, textureY); // Box 0
		scissor1Model[3] = new ModelRendererTurbo(this, 196, 178, textureX, textureY); // Box 0

		scissor1Model[0].addBox(-1F, -1F, 0F, 1, 2, 24, 0F); // Box 0
		scissor1Model[0].setRotationPoint(10F, 0F, 29F);
		scissor1Model[0].rotateAngleX = 0.10471976F;
		scissor1Model[0].rotateAngleY = -3.14159265F;

		scissor1Model[1].addBox(-1F, -1F, 0F, 1, 2, 24, 0F); // Box 0
		scissor1Model[1].setRotationPoint(13F, 0F, 29F);
		scissor1Model[1].rotateAngleX = 0.10471976F;
		scissor1Model[1].rotateAngleY = -3.14159265F;

		scissor1Model[2].addBox(-1F, -1F, 0F, 1, 2, 24, 0F); // Box 0
		scissor1Model[2].setRotationPoint(50F, 0F, 29F);
		scissor1Model[2].rotateAngleX = 0.10471976F;
		scissor1Model[2].rotateAngleY = -3.14159265F;

		scissor1Model[3].addBox(-1F, -1F, 0F, 1, 2, 24, 0F); // Box 0
		scissor1Model[3].setRotationPoint(53F, 0F, 29F);
		scissor1Model[3].rotateAngleX = 0.10471976F;
		scissor1Model[3].rotateAngleY = -3.14159265F;


		scissor2Model = new ModelRendererTurbo[4];
		scissor2Model[0] = new ModelRendererTurbo(this, 196, 178, textureX, textureY); // Box 0
		scissor2Model[1] = new ModelRendererTurbo(this, 196, 178, textureX, textureY); // Box 0
		scissor2Model[2] = new ModelRendererTurbo(this, 196, 178, textureX, textureY); // Box 0
		scissor2Model[3] = new ModelRendererTurbo(this, 196, 178, textureX, textureY); // Box 0

		scissor2Model[0].addBox(-1F, -1F, 0F, 1, 2, 24, 0F); // Box 0
		scissor2Model[0].setRotationPoint(10F, 0F, 2F);
		scissor2Model[0].rotateAngleX = 0.10471976F;

		scissor2Model[1].addBox(-1F, -1F, 0F, 1, 2, 24, 0F); // Box 0
		scissor2Model[1].setRotationPoint(15F, 0F, 2F);
		scissor2Model[1].rotateAngleX = 0.10471976F;

		scissor2Model[2].addBox(-1F, -1F, 0F, 1, 2, 24, 0F); // Box 0
		scissor2Model[2].setRotationPoint(50F, 0F, 2F);
		scissor2Model[2].rotateAngleX = 0.10471976F;

		scissor2Model[3].addBox(-1F, -1F, 0F, 1, 2, 24, 0F); // Box 0
		scissor2Model[3].setRotationPoint(55F, 0F, 2F);
		scissor2Model[3].rotateAngleX = 0.10471976F;


		drawer1Model = new ModelRendererTurbo[5];
		drawer1Model[0] = new ModelRendererTurbo(this, 229, 0, textureX, textureY); // Box 4
		drawer1Model[1] = new ModelRendererTurbo(this, 2, 44, textureX, textureY); // Box 4
		drawer1Model[2] = new ModelRendererTurbo(this, 2, 44, textureX, textureY); // Box 4
		drawer1Model[3] = new ModelRendererTurbo(this, 96, 0, textureX, textureY); // Box 4
		drawer1Model[4] = new ModelRendererTurbo(this, 9, 4, textureX, textureY); // Box 4

		drawer1Model[0].addBox(0F, 0F, 0F, 14, 1, 13, 0F); // Box 4
		drawer1Model[0].setRotationPoint(17F, -11F, 34F);
		drawer1Model[0].rotateAngleX = 0.19198622F;

		drawer1Model[1].addBox(0F, 0F, 0F, 1, 3, 14, 0F); // Box 4
		drawer1Model[1].setRotationPoint(17F, -14F, 34F);

		drawer1Model[2].addBox(0F, 0F, 0F, 1, 3, 14, 0F); // Box 4
		drawer1Model[2].setRotationPoint(30F, -14F, 34F);

		drawer1Model[3].addBox(0F, 0F, 0F, 14, 4, 1, 0F); // Box 4
		drawer1Model[3].setRotationPoint(17F, -14F, 33F);

		drawer1Model[4].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // Box 4
		drawer1Model[4].setRotationPoint(23F, -13F, 32F);


		drawer2Model = new ModelRendererTurbo[5];
		drawer2Model[0] = new ModelRendererTurbo(this, 229, 0, textureX, textureY); // Box 4
		drawer2Model[1] = new ModelRendererTurbo(this, 2, 44, textureX, textureY); // Box 4
		drawer2Model[2] = new ModelRendererTurbo(this, 2, 44, textureX, textureY); // Box 4
		drawer2Model[3] = new ModelRendererTurbo(this, 96, 0, textureX, textureY); // Box 4
		drawer2Model[4] = new ModelRendererTurbo(this, 9, 4, textureX, textureY); // Box 4

		drawer2Model[0].addBox(0F, 0F, 0F, 14, 1, 13, 0F); // Box 4
		drawer2Model[0].setRotationPoint(17F, -6F, 34F);

		drawer2Model[1].addBox(0F, 0F, 0F, 1, 3, 14, 0F); // Box 4
		drawer2Model[1].setRotationPoint(17F, -9F, 34F);

		drawer2Model[2].addBox(0F, 0F, 0F, 1, 3, 14, 0F); // Box 4
		drawer2Model[2].setRotationPoint(30F, -9F, 34F);

		drawer2Model[3].addBox(0F, 0F, 0F, 14, 4, 1, 0F); // Box 4
		drawer2Model[3].setRotationPoint(17F, -9F, 33F);

		drawer2Model[4].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // Box 4
		drawer2Model[4].setRotationPoint(23F, -8F, 32F);


		engineModel = new ModelRendererTurbo[20];
		engineModel[0] = new ModelRendererTurbo(this, 160, 150, textureX, textureY); // Box 4
		engineModel[1] = new ModelRendererTurbo(this, 111, 240, textureX, textureY); // Box 4
		engineModel[2] = new ModelRendererTurbo(this, 226, 64, textureX, textureY); // Box 0
		engineModel[3] = new ModelRendererTurbo(this, 226, 68, textureX, textureY); // Box 0
		engineModel[4] = new ModelRendererTurbo(this, 226, 59, textureX, textureY); // Box 0
		engineModel[5] = new ModelRendererTurbo(this, 226, 64, textureX, textureY); // Box 0
		engineModel[6] = new ModelRendererTurbo(this, 226, 68, textureX, textureY); // Box 0
		engineModel[7] = new ModelRendererTurbo(this, 226, 59, textureX, textureY); // Box 0
		engineModel[8] = new ModelRendererTurbo(this, 226, 64, textureX, textureY); // Box 0
		engineModel[9] = new ModelRendererTurbo(this, 226, 68, textureX, textureY); // Box 0
		engineModel[10] = new ModelRendererTurbo(this, 226, 59, textureX, textureY); // Box 0
		engineModel[11] = new ModelRendererTurbo(this, 226, 64, textureX, textureY); // Box 0
		engineModel[12] = new ModelRendererTurbo(this, 226, 68, textureX, textureY); // Box 0
		engineModel[13] = new ModelRendererTurbo(this, 226, 59, textureX, textureY); // Box 0
		engineModel[14] = new ModelRendererTurbo(this, 226, 64, textureX, textureY); // Box 0
		engineModel[15] = new ModelRendererTurbo(this, 226, 68, textureX, textureY); // Box 0
		engineModel[16] = new ModelRendererTurbo(this, 226, 59, textureX, textureY); // Box 0
		engineModel[17] = new ModelRendererTurbo(this, 226, 64, textureX, textureY); // Box 0
		engineModel[18] = new ModelRendererTurbo(this, 226, 68, textureX, textureY); // Box 0
		engineModel[19] = new ModelRendererTurbo(this, 226, 59, textureX, textureY); // Box 0

		engineModel[0].addBox(0F, 0F, 0F, 16, 8, 12, 0F); // Box 4
		engineModel[0].setRotationPoint(27F, -12F, 67F);

		engineModel[1].addBox(0F, 0F, 0F, 16, 2, 8, 0F); // Box 4
		engineModel[1].setRotationPoint(27F, -14F, 69F);

		engineModel[2].addBox(-1.5F, -4F, -1.5F, 3, 1, 3, 0F); // Box 0
		engineModel[2].setRotationPoint(40F, -11F, 78F);
		engineModel[2].rotateAngleX = -0.38397244F;

		engineModel[3].addBox(-2F, -3F, -2F, 4, 5, 4, 0F); // Box 0
		engineModel[3].setRotationPoint(40F, -11F, 78F);
		engineModel[3].rotateAngleX = -0.38397244F;

		engineModel[4].addBox(-2F, -5F, -2F, 4, 1, 4, 0F); // Box 0
		engineModel[4].setRotationPoint(40F, -11F, 78F);
		engineModel[4].rotateAngleX = -0.38397244F;

		engineModel[5].addBox(-1.5F, -4F, -1.5F, 3, 1, 3, 0F); // Box 0
		engineModel[5].setRotationPoint(35F, -11F, 78F);
		engineModel[5].rotateAngleX = -0.38397244F;

		engineModel[6].addBox(-2F, -3F, -2F, 4, 5, 4, 0F); // Box 0
		engineModel[6].setRotationPoint(35F, -11F, 78F);
		engineModel[6].rotateAngleX = -0.38397244F;

		engineModel[7].addBox(-2F, -5F, -2F, 4, 1, 4, 0F); // Box 0
		engineModel[7].setRotationPoint(35F, -11F, 78F);
		engineModel[7].rotateAngleX = -0.38397244F;

		engineModel[8].addBox(-1.5F, -4F, -1.5F, 3, 1, 3, 0F); // Box 0
		engineModel[8].setRotationPoint(30F, -11F, 78F);
		engineModel[8].rotateAngleX = -0.38397244F;

		engineModel[9].addBox(-2F, -3F, -2F, 4, 5, 4, 0F); // Box 0
		engineModel[9].setRotationPoint(30F, -11F, 78F);
		engineModel[9].rotateAngleX = -0.38397244F;

		engineModel[10].addBox(-2F, -5F, -2F, 4, 1, 4, 0F); // Box 0
		engineModel[10].setRotationPoint(30F, -11F, 78F);
		engineModel[10].rotateAngleX = -0.38397244F;

		engineModel[11].addBox(-1.5F, -4F, -1.5F, 3, 1, 3, 0F); // Box 0
		engineModel[11].setRotationPoint(40F, -11F, 68F);
		engineModel[11].rotateAngleX = 0.38397244F;

		engineModel[12].addBox(-2F, -3F, -2F, 4, 5, 4, 0F); // Box 0
		engineModel[12].setRotationPoint(40F, -11F, 68F);
		engineModel[12].rotateAngleX = 0.38397244F;

		engineModel[13].addBox(-2F, -5F, -2F, 4, 1, 4, 0F); // Box 0
		engineModel[13].setRotationPoint(40F, -11F, 68F);
		engineModel[13].rotateAngleX = 0.38397244F;

		engineModel[14].addBox(-1.5F, -4F, -1.5F, 3, 1, 3, 0F); // Box 0
		engineModel[14].setRotationPoint(35F, -11F, 68F);
		engineModel[14].rotateAngleX = 0.38397244F;

		engineModel[15].addBox(-2F, -3F, -2F, 4, 5, 4, 0F); // Box 0
		engineModel[15].setRotationPoint(35F, -11F, 68F);
		engineModel[15].rotateAngleX = 0.38397244F;

		engineModel[16].addBox(-2F, -5F, -2F, 4, 1, 4, 0F); // Box 0
		engineModel[16].setRotationPoint(35F, -11F, 68F);
		engineModel[16].rotateAngleX = 0.38397244F;

		engineModel[17].addBox(-1.5F, -4F, -1.5F, 3, 1, 3, 0F); // Box 0
		engineModel[17].setRotationPoint(30F, -11F, 68F);
		engineModel[17].rotateAngleX = 0.38397244F;

		engineModel[18].addBox(-2F, -3F, -2F, 4, 5, 4, 0F); // Box 0
		engineModel[18].setRotationPoint(30F, -11F, 68F);
		engineModel[18].rotateAngleX = 0.38397244F;

		engineModel[19].addBox(-2F, -5F, -2F, 4, 1, 4, 0F); // Box 0
		engineModel[19].setRotationPoint(30F, -11F, 68F);
		engineModel[19].rotateAngleX = 0.38397244F;


		craneShaftModel = new ModelRendererTurbo[3];
		craneShaftModel[0] = new ModelRendererTurbo(this, 4, 61, textureX, textureY); // Box 4
		craneShaftModel[1] = new ModelRendererTurbo(this, 22, 18, textureX, textureY); // Box 4
		craneShaftModel[2] = new ModelRendererTurbo(this, 22, 23, textureX, textureY); // Box 4

		craneShaftModel[0].addBox(-2F, 0F, 0F, 4, 4, 2, 0F); // Box 4
		craneShaftModel[0].setRotationPoint(43F, -10F, 52F);

		craneShaftModel[1].addShapeBox(-2F, 0F, -1F, 4, 4, 1, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		craneShaftModel[1].setRotationPoint(43F, -10F, 52F);

		craneShaftModel[2].addShapeBox(-2F, 0F, 2F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F); // Box 4
		craneShaftModel[2].setRotationPoint(43F, -10F, 52F);


		engineShaftModel = new ModelRendererTurbo[3];
		engineShaftModel[0] = new ModelRendererTurbo(this, 16, 117, textureX, textureY); // Box 4
		engineShaftModel[1] = new ModelRendererTurbo(this, 4, 13, textureX, textureY); // Box 4
		engineShaftModel[2] = new ModelRendererTurbo(this, 142, 121, textureX, textureY); // Box 4

		engineShaftModel[0].addBox(0F, -1F, -2F, 3, 2, 4, 0F); // Box 4
		engineShaftModel[0].setRotationPoint(44F, -9F, 73F);

		engineShaftModel[1].addShapeBox(0F, -2F, -2F, 3, 1, 4, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		engineShaftModel[1].setRotationPoint(44F, -9F, 73F);

		engineShaftModel[2].addShapeBox(0F, 1F, -2F, 3, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F); // Box 4
		engineShaftModel[2].setRotationPoint(44F, -9F, 73F);


		doorLeftModel = new ModelRendererTurbo[1];
		doorLeftModel[0] = new ModelRendererTurbo(this, 192, 57, textureX, textureY); // Box 4

		doorLeftModel[0].addBox(0F, 0F, -0.5F, 16, 29, 1, 0F); // Box 4
		doorLeftModel[0].setRotationPoint(0F, -46F, 47.5F);




		doorRightModel = new ModelRendererTurbo[8];
		doorRightModel[0] = new ModelRendererTurbo(this, 192, 9, textureX, textureY); // Box 4
		doorRightModel[1] = new ModelRendererTurbo(this, 256, 44, textureX, textureY); // Box 4
		doorRightModel[2] = new ModelRendererTurbo(this, 256, 44, textureX, textureY); // Box 4
		doorRightModel[3] = new ModelRendererTurbo(this, 256, 44, textureX, textureY); // Box 4
		doorRightModel[4] = new ModelRendererTurbo(this, 256, 44, textureX, textureY); // Box 4
		doorRightModel[5] = new ModelRendererTurbo(this, 256, 44, textureX, textureY); // Box 4
		doorRightModel[6] = new ModelRendererTurbo(this, 256, 44, textureX, textureY); // Box 4
		doorRightModel[7] = new ModelRendererTurbo(this, 256, 46, textureX, textureY); // Box 4

		doorRightModel[0].addBox(0F, 0F, -0.5F, 16, 29, 1, 0F); // Box 4
		doorRightModel[0].setRotationPoint(16F, -46F, 47.5F);

		doorRightModel[1].addBox(2F, 1F, -1.5F, 12, 1, 1, 0F); // Box 4
		doorRightModel[1].setRotationPoint(16F, -46F, 47.5F);

		doorRightModel[2].addBox(2F, 3F, -1.5F, 12, 1, 1, 0F); // Box 4
		doorRightModel[2].setRotationPoint(16F, -46F, 47.5F);

		doorRightModel[3].addBox(2F, 5F, -1.5F, 12, 1, 1, 0F); // Box 4
		doorRightModel[3].setRotationPoint(16F, -46F, 47.5F);

		doorRightModel[4].addBox(2F, 7F, -1.5F, 12, 1, 1, 0F); // Box 4
		doorRightModel[4].setRotationPoint(16F, -46F, 47.5F);

		doorRightModel[5].addBox(2F, 9F, -1.5F, 12, 1, 1, 0F); // Box 4
		doorRightModel[5].setRotationPoint(16F, -46F, 47.5F);

		doorRightModel[6].addBox(2F, 11F, -1.5F, 12, 1, 1, 0F); // Box 4
		doorRightModel[6].setRotationPoint(16F, -46F, 47.5F);

		doorRightModel[7].addBox(12F, 15F, -1.5F, 2, 2, 1, 0F); // Box 4
		doorRightModel[7].setRotationPoint(16F, -46F, 47.5F);


		railModel = new ModelRendererTurbo[8];
		railModel[0] = new ModelRendererTurbo(this, 282, 44, textureX, textureY); // Box 4
		railModel[1] = new ModelRendererTurbo(this, 292, 44, textureX, textureY); // Box 4
		railModel[2] = new ModelRendererTurbo(this, 296, 44, textureX, textureY); // Box 4
		railModel[3] = new ModelRendererTurbo(this, 282, 44, textureX, textureY); // Box 4
		railModel[4] = new ModelRendererTurbo(this, 292, 44, textureX, textureY); // Box 4
		railModel[5] = new ModelRendererTurbo(this, 296, 44, textureX, textureY); // Box 4
		railModel[6] = new ModelRendererTurbo(this, 300, 44, textureX, textureY); // Box 4
		railModel[7] = new ModelRendererTurbo(this, 300, 44, textureX, textureY); // Box 4

		railModel[0].addBox(0F, 0F, -1F, 4, 16, 1, 0F); // Box 4
		railModel[0].setRotationPoint(5F, -25F, 63F);

		railModel[1].addBox(0F, 0F, -2F, 1, 16, 1, 0F); // Box 4
		railModel[1].setRotationPoint(5F, -25F, 63F);

		railModel[2].addBox(3F, 0F, -2F, 1, 16, 1, 0F); // Box 4
		railModel[2].setRotationPoint(5F, -25F, 63F);

		railModel[3].addBox(0F, 0F, -1F, 4, 16, 1, 0F); // Box 4
		railModel[3].setRotationPoint(23F, -25F, 63F);

		railModel[4].addBox(0F, 0F, -2F, 1, 16, 1, 0F); // Box 4
		railModel[4].setRotationPoint(23F, -25F, 63F);

		railModel[5].addBox(3F, 0F, -2F, 1, 16, 1, 0F); // Box 4
		railModel[5].setRotationPoint(23F, -25F, 63F);

		railModel[6].addBox(0F, -1F, -2F, 4, 1, 2, 0F); // Box 4
		railModel[6].setRotationPoint(5F, -25F, 63F);

		railModel[7].addBox(0F, -1F, -2F, 4, 1, 2, 0F); // Box 4
		railModel[7].setRotationPoint(23F, -25F, 63F);


		winchModel = new ModelRendererTurbo[4];
		winchModel[0] = new ModelRendererTurbo(this, 271, 59, textureX, textureY); // Box 4
		winchModel[1] = new ModelRendererTurbo(this, 300, 57, textureX, textureY); // Box 4
		winchModel[2] = new ModelRendererTurbo(this, 271, 59, textureX, textureY); // Box 4
		winchModel[3] = new ModelRendererTurbo(this, 304, 65, textureX, textureY); // Box 4

		winchModel[0].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // Box 4
		winchModel[0].setRotationPoint(28F, -43F, 52F);

		winchModel[1].addBox(0F, -2F, -2F, 24, 4, 4, 0F); // Box 4
		winchModel[1].setRotationPoint(4F, -43F, 52F);

		winchModel[2].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // Box 4
		winchModel[2].setRotationPoint(3F, -43F, 52F);

		winchModel[3].addShapeBox(0F, -2F, -2F, 24, 4, 4, 0F, 0.125F, 0.25F, 0.25F, 0.125F, 0.25F, 0.25F, 0.125F, 0.25F, 0.25F, 0.125F, 0.25F, 0.25F, 0.125F, 0.25F, 0.25F, 0.125F, 0.25F, 0.25F, 0.125F, 0.25F, 0.25F, 0.125F, 0.25F, 0.25F); // Box 4
		winchModel[3].setRotationPoint(4F, -43F, 52F);

		parts.put("base",baseModel);
		parts.put("platform",platformModel);
		parts.put("scissor1",scissor1Model);
		parts.put("scissor2",scissor2Model);
		parts.put("drawer1",drawer1Model);
		parts.put("drawer2",drawer2Model);
		parts.put("engine",engineModel);
		parts.put("craneShaft",craneShaftModel);
		parts.put("engineShaft",engineShaftModel);
		parts.put("doorLeft",doorLeftModel);
		parts.put("doorRight",doorRightModel);
		parts.put("rail",railModel);
		parts.put("winch",winchModel);

		flipAll();
	}

	@Override
	public void getBlockRotation(EnumFacing facing, boolean mirrored)
	{
		switch(facing)
		{
			case NORTH:
			{
				GlStateManager.translate(mirrored?3:-2, 0f, 3f);
			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?2:-3, 0f, 2f);
			}
			break;
			case EAST:
			{
				GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?3f:-2f, 0f, 2f);

			}
			break;
			case WEST:
			{
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?2f:-3f, 0f, 3f);

			}
			break;
		}
	}
}
