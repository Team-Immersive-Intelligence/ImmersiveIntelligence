package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * @author Pabilo8
 * @since 31.10.2021
 */
public class ModelCoagulator extends ModelIIBase
{
	public ModelRendererTurbo[] tankRightModel, valveRightModel, pistonsRightModel, tankLeftModel, valveLeftModel, pistonsLeftModel, mixerModel, starterSlideModel, bucketHolderModel, bucketRailModel, bucketTimerModel, cartModel, cartWheelsModel,bucketModel, bucketHandleModel;
	final int textureX = 256;
	final int textureY = 256;

	public ModelCoagulator()
	{
		baseModel = new ModelRendererTurbo[172];
		baseModel[0] = new ModelRendererTurbo(this, 190, 88, textureX, textureY); // MainShit01
		baseModel[1] = new ModelRendererTurbo(this, 74, 128, textureX, textureY); // MainShit02
		baseModel[2] = new ModelRendererTurbo(this, 74, 128, textureX, textureY); // MainShit03
		baseModel[3] = new ModelRendererTurbo(this, 214, 11, textureX, textureY); // MainShit13
		baseModel[4] = new ModelRendererTurbo(this, 182, 88, textureX, textureY); // MainShit16
		baseModel[5] = new ModelRendererTurbo(this, 191, 53, textureX, textureY); // MainShit17
		baseModel[6] = new ModelRendererTurbo(this, 191, 53, textureX, textureY); // MainShit18
		baseModel[7] = new ModelRendererTurbo(this, 182, 88, textureX, textureY); // MainShit19
		baseModel[8] = new ModelRendererTurbo(this, 191, 53, textureX, textureY); // MainShit20
		baseModel[9] = new ModelRendererTurbo(this, 191, 53, textureX, textureY); // MainShit21
		baseModel[10] = new ModelRendererTurbo(this, 94, 92, textureX, textureY); // MainShit22
		baseModel[11] = new ModelRendererTurbo(this, 94, 92, textureX, textureY); // MainShit23
		baseModel[12] = new ModelRendererTurbo(this, 94, 92, textureX, textureY); // MainShit24
		baseModel[13] = new ModelRendererTurbo(this, 94, 92, textureX, textureY); // MainShit25
		baseModel[14] = new ModelRendererTurbo(this, 162, 32, textureX, textureY); // MainShit26
		baseModel[15] = new ModelRendererTurbo(this, 162, 32, textureX, textureY); // MainShit27
		baseModel[16] = new ModelRendererTurbo(this, 182, 6, textureX, textureY); // MainShit31
		baseModel[17] = new ModelRendererTurbo(this, 182, 6, textureX, textureY); // MainShit32
		baseModel[18] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit34
		baseModel[19] = new ModelRendererTurbo(this, 7, 128, textureX, textureY); // MainShit35
		baseModel[20] = new ModelRendererTurbo(this, 182, 6, textureX, textureY); // MainShit36
		baseModel[21] = new ModelRendererTurbo(this, 0, 218, textureX, textureY); // MainShit40
		baseModel[22] = new ModelRendererTurbo(this, 90, 102, textureX, textureY); // MainShit43
		baseModel[23] = new ModelRendererTurbo(this, 178, 0, textureX, textureY); // MainShit45
		baseModel[24] = new ModelRendererTurbo(this, 178, 0, textureX, textureY); // MainShit46
		baseModel[25] = new ModelRendererTurbo(this, 178, 0, textureX, textureY); // MainShit47
		baseModel[26] = new ModelRendererTurbo(this, 178, 0, textureX, textureY); // MainShit48
		baseModel[27] = new ModelRendererTurbo(this, 186, 233, textureX, textureY); // MainShit49
		baseModel[28] = new ModelRendererTurbo(this, 192, 193, textureX, textureY); // MainShit50
		baseModel[29] = new ModelRendererTurbo(this, 42, 128, textureX, textureY); // MainShit51
		baseModel[30] = new ModelRendererTurbo(this, 35, 196, textureX, textureY); // MainShit52
		baseModel[31] = new ModelRendererTurbo(this, 113, 233, textureX, textureY); // MainShit53
		baseModel[32] = new ModelRendererTurbo(this, 186, 49, textureX, textureY); // MainShit54
		baseModel[33] = new ModelRendererTurbo(this, 240, 131, textureX, textureY); // MainShit55
		baseModel[34] = new ModelRendererTurbo(this, 100, 26, textureX, textureY); // MainShit56
		baseModel[35] = new ModelRendererTurbo(this, 0, 55, textureX, textureY); // MainShit57
		baseModel[36] = new ModelRendererTurbo(this, 48, 74, textureX, textureY); // MainShit58
		baseModel[37] = new ModelRendererTurbo(this, 188, 148, textureX, textureY); // MainShit59
		baseModel[38] = new ModelRendererTurbo(this, 53, 47, textureX, textureY); // MainShit60
		baseModel[39] = new ModelRendererTurbo(this, 182, 6, textureX, textureY); // MainShit61
		baseModel[40] = new ModelRendererTurbo(this, 182, 6, textureX, textureY); // MainShit62
		baseModel[41] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit64
		baseModel[42] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit66
		baseModel[43] = new ModelRendererTurbo(this, 170, 128, textureX, textureY); // MainShit70
		baseModel[44] = new ModelRendererTurbo(this, 202, 129, textureX, textureY); // MainShit72
		baseModel[45] = new ModelRendererTurbo(this, 98, 17, textureX, textureY); // MIXER01
		baseModel[46] = new ModelRendererTurbo(this, 34, 100, textureX, textureY); // MIXER03
		baseModel[47] = new ModelRendererTurbo(this, 23, 47, textureX, textureY); // MIXER04
		baseModel[48] = new ModelRendererTurbo(this, 146, 5, textureX, textureY); // MIXER05
		baseModel[49] = new ModelRendererTurbo(this, 146, 5, textureX, textureY); // MIXER09
		baseModel[50] = new ModelRendererTurbo(this, 34, 100, textureX, textureY); // MIXER03
		baseModel[51] = new ModelRendererTurbo(this, 83, 184, textureX, textureY); // MIXER03
		baseModel[52] = new ModelRendererTurbo(this, 146, 5, textureX, textureY); // MIXER09
		baseModel[53] = new ModelRendererTurbo(this, 26, 168, textureX, textureY); // MainShit60
		baseModel[54] = new ModelRendererTurbo(this, 202, 129, textureX, textureY); // MainShit72
		baseModel[55] = new ModelRendererTurbo(this, 190, 88, textureX, textureY); // MainShit01
		baseModel[56] = new ModelRendererTurbo(this, 190, 88, textureX, textureY); // MainShit01
		baseModel[57] = new ModelRendererTurbo(this, 190, 88, textureX, textureY); // MainShit01
		baseModel[58] = new ModelRendererTurbo(this, 170, 128, textureX, textureY); // MainShit70
		baseModel[59] = new ModelRendererTurbo(this, 170, 128, textureX, textureY); // MainShit70
		baseModel[60] = new ModelRendererTurbo(this, 170, 128, textureX, textureY); // MainShit70
		baseModel[61] = new ModelRendererTurbo(this, 170, 128, textureX, textureY); // MainShit70
		baseModel[62] = new ModelRendererTurbo(this, 170, 128, textureX, textureY); // MainShit70
		baseModel[63] = new ModelRendererTurbo(this, 6, 110, textureX, textureY); // MainShit70
		baseModel[64] = new ModelRendererTurbo(this, 6, 110, textureX, textureY); // MainShit70
		baseModel[65] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[66] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[67] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[68] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[69] = new ModelRendererTurbo(this, 6, 110, textureX, textureY); // MainShit70
		baseModel[70] = new ModelRendererTurbo(this, 6, 110, textureX, textureY); // MainShit70
		baseModel[71] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[72] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[73] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[74] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[75] = new ModelRendererTurbo(this, 6, 110, textureX, textureY); // MainShit70
		baseModel[76] = new ModelRendererTurbo(this, 6, 110, textureX, textureY); // MainShit70
		baseModel[77] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[78] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[79] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[80] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[81] = new ModelRendererTurbo(this, 6, 110, textureX, textureY); // MainShit70
		baseModel[82] = new ModelRendererTurbo(this, 6, 110, textureX, textureY); // MainShit70
		baseModel[83] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[84] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[85] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[86] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[87] = new ModelRendererTurbo(this, 6, 110, textureX, textureY); // MainShit70
		baseModel[88] = new ModelRendererTurbo(this, 6, 110, textureX, textureY); // MainShit70
		baseModel[89] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[90] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[91] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[92] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[93] = new ModelRendererTurbo(this, 6, 110, textureX, textureY); // MainShit70
		baseModel[94] = new ModelRendererTurbo(this, 6, 110, textureX, textureY); // MainShit70
		baseModel[95] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[96] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[97] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[98] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // MainShit70
		baseModel[99] = new ModelRendererTurbo(this, 182, 88, textureX, textureY); // MainShit16
		baseModel[100] = new ModelRendererTurbo(this, 182, 88, textureX, textureY); // MainShit19
		baseModel[101] = new ModelRendererTurbo(this, 192, 50, textureX, textureY); // MainShit01
		baseModel[102] = new ModelRendererTurbo(this, 192, 50, textureX, textureY); // MainShit01
		baseModel[103] = new ModelRendererTurbo(this, 192, 50, textureX, textureY); // MainShit01
		baseModel[104] = new ModelRendererTurbo(this, 192, 50, textureX, textureY); // MainShit01
		baseModel[105] = new ModelRendererTurbo(this, 112, 88, textureX, textureY); // MainShit01
		baseModel[106] = new ModelRendererTurbo(this, 112, 88, textureX, textureY); // MainShit01
		baseModel[107] = new ModelRendererTurbo(this, 112, 88, textureX, textureY); // MainShit01
		baseModel[108] = new ModelRendererTurbo(this, 112, 88, textureX, textureY); // MainShit01
		baseModel[109] = new ModelRendererTurbo(this, 116, 49, textureX, textureY); // MainShit01
		baseModel[110] = new ModelRendererTurbo(this, 116, 49, textureX, textureY); // MainShit01
		baseModel[111] = new ModelRendererTurbo(this, 116, 49, textureX, textureY); // MainShit01
		baseModel[112] = new ModelRendererTurbo(this, 116, 49, textureX, textureY); // MainShit01
		baseModel[113] = new ModelRendererTurbo(this, 182, 6, textureX, textureY); // MainShit31
		baseModel[114] = new ModelRendererTurbo(this, 87, 3, textureX, textureY); // MainShit13
		baseModel[115] = new ModelRendererTurbo(this, 214, 11, textureX, textureY); // MainShit13
		baseModel[116] = new ModelRendererTurbo(this, 87, 3, textureX, textureY); // MainShit13
		baseModel[117] = new ModelRendererTurbo(this, 214, 11, textureX, textureY); // MainShit13
		baseModel[118] = new ModelRendererTurbo(this, 87, 3, textureX, textureY); // MainShit13
		baseModel[119] = new ModelRendererTurbo(this, 214, 11, textureX, textureY); // MainShit13
		baseModel[120] = new ModelRendererTurbo(this, 87, 3, textureX, textureY); // MainShit13
		baseModel[121] = new ModelRendererTurbo(this, 98, 17, textureX, textureY); // MIXER01
		baseModel[122] = new ModelRendererTurbo(this, 98, 17, textureX, textureY); // MIXER01
		baseModel[123] = new ModelRendererTurbo(this, 133, 4, textureX, textureY); // MainShit54
		baseModel[124] = new ModelRendererTurbo(this, 162, 32, textureX, textureY); // MainShit26
		baseModel[125] = new ModelRendererTurbo(this, 162, 32, textureX, textureY); // MainShit27
		baseModel[126] = new ModelRendererTurbo(this, 162, 32, textureX, textureY); // MainShit27
		baseModel[127] = new ModelRendererTurbo(this, 162, 32, textureX, textureY); // MainShit27
		baseModel[128] = new ModelRendererTurbo(this, 162, 32, textureX, textureY); // MainShit27
		baseModel[129] = new ModelRendererTurbo(this, 162, 32, textureX, textureY); // MainShit27
		baseModel[130] = new ModelRendererTurbo(this, 38, 103, textureX, textureY); // MainShit54
		baseModel[131] = new ModelRendererTurbo(this, 118, 49, textureX, textureY); // MainShit54
		baseModel[132] = new ModelRendererTurbo(this, 112, 49, textureX, textureY); // MainShit54
		baseModel[133] = new ModelRendererTurbo(this, 182, 6, textureX, textureY); // MainShit61
		baseModel[134] = new ModelRendererTurbo(this, 182, 6, textureX, textureY); // MainShit36
		baseModel[135] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit34
		baseModel[136] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit34
		baseModel[137] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit34
		baseModel[138] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit34
		baseModel[139] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit34
		baseModel[140] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit34
		baseModel[141] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit34
		baseModel[142] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit34
		baseModel[143] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit34
		baseModel[144] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit34
		baseModel[145] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit34
		baseModel[146] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit34
		baseModel[147] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit34
		baseModel[148] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit34
		baseModel[149] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit34
		baseModel[150] = new ModelRendererTurbo(this, 83, 70, textureX, textureY); // MIXER02
		baseModel[151] = new ModelRendererTurbo(this, 146, 5, textureX, textureY); // MIXER09
		baseModel[152] = new ModelRendererTurbo(this, 146, 5, textureX, textureY); // MIXER09
		baseModel[153] = new ModelRendererTurbo(this, 90, 102, textureX, textureY); // MainShit43
		baseModel[154] = new ModelRendererTurbo(this, 90, 102, textureX, textureY); // MainShit43
		baseModel[155] = new ModelRendererTurbo(this, 90, 102, textureX, textureY); // MainShit43
		baseModel[156] = new ModelRendererTurbo(this, 87, 6, textureX, textureY); // Shape 240
		baseModel[157] = new ModelRendererTurbo(this, 87, 6, textureX, textureY); // Shape 240
		baseModel[158] = new ModelRendererTurbo(this, 87, 6, textureX, textureY); // Shape 240
		baseModel[159] = new ModelRendererTurbo(this, 87, 6, textureX, textureY); // Shape 240
		baseModel[160] = new ModelRendererTurbo(this, 136, 212, textureX, textureY); // MainShit53
		baseModel[161] = new ModelRendererTurbo(this, 104, 212, textureX, textureY); // MainShit53
		baseModel[162] = new ModelRendererTurbo(this, 186, 49, textureX, textureY); // MainShit54
		baseModel[163] = new ModelRendererTurbo(this, 133, 4, textureX, textureY); // MainShit54
		baseModel[164] = new ModelRendererTurbo(this, 191, 191, textureX, textureY); // MainShit49
		baseModel[165] = new ModelRendererTurbo(this, 176, 154, textureX, textureY); // MainShit49
		baseModel[166] = new ModelRendererTurbo(this, 176, 154, textureX, textureY); // MainShit49
		baseModel[167] = new ModelRendererTurbo(this, 176, 154, textureX, textureY); // MainShit49
		baseModel[168] = new ModelRendererTurbo(this, 186, 233, textureX, textureY); // MainShit49
		baseModel[169] = new ModelRendererTurbo(this, 0, 218, textureX, textureY); // MainShit40
		baseModel[170] = new ModelRendererTurbo(this, 16, 83, textureX, textureY); // MainShit66
		baseModel[171] = new ModelRendererTurbo(this, 153, 184, textureX, textureY); // MainShit02

		baseModel[0].addShapeBox(0F, 0F, 0F, 25, 32, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit01
		baseModel[0].setRotationPoint(0F, 16F, 0F);
		baseModel[0].rotateAngleX = 1.57079633F;

		baseModel[1].setFlipped(true);
		baseModel[1].addShapeBox(0F, 0F, 0F, 32, 24, 32, 0F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F); // MainShit02
		baseModel[1].setRotationPoint(0F, -24F, 32F);

		baseModel[2].setFlipped(true);
		baseModel[2].addShapeBox(0F, 0F, 0F, 32, 24, 32, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F); // MainShit03
		baseModel[2].setRotationPoint(80F, -24F, 32F);

		baseModel[3].addShapeBox(0F, 0F, 0F, 20, 38, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit13
		baseModel[3].setRotationPoint(6F, -62F, 32F);

		baseModel[4].addShapeBox(0F, 0F, 0F, 5, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit16
		baseModel[4].setRotationPoint(93.5F, -59F, 31F);

		baseModel[5].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit17
		baseModel[5].setRotationPoint(94.5F, -58F, 30F);

		baseModel[6].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit18
		baseModel[6].setRotationPoint(94.5F, -58F, 65F);

		baseModel[7].addShapeBox(0F, 0F, 0F, 5, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit19
		baseModel[7].setRotationPoint(13.5F, -59F, 31F);

		baseModel[8].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit20
		baseModel[8].setRotationPoint(14.5F, -58F, 30F);

		baseModel[9].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit21
		baseModel[9].setRotationPoint(14.5F, -58F, 65F);

		baseModel[10].addShapeBox(0F, 0F, 0F, 5, 6, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit22
		baseModel[10].setRotationPoint(107F, -24F, 38F);
		baseModel[10].rotateAngleX = 1.57079633F;

		baseModel[11].addShapeBox(0F, 0F, 0F, 5, 6, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit23
		baseModel[11].setRotationPoint(107F, -24F, 52F);
		baseModel[11].rotateAngleX = 1.57079633F;

		baseModel[12].addShapeBox(0F, 0F, 0F, 5, 6, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit24
		baseModel[12].setRotationPoint(0F, -24F, 52F);
		baseModel[12].rotateAngleX = 1.57079633F;

		baseModel[13].addShapeBox(0F, 0F, 0F, 5, 6, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit25
		baseModel[13].setRotationPoint(0F, -24F, 38F);
		baseModel[13].rotateAngleX = 1.57079633F;

		baseModel[14].addShapeBox(0F, 0F, 0F, 24, 15, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit26
		baseModel[14].setRotationPoint(32F, -28F, 16F);

		baseModel[15].setFlipped(true);
		baseModel[15].addShapeBox(0F, 0F, 0F, 24, 15, 2, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F); // MainShit27
		baseModel[15].setRotationPoint(32F, -28F, 62F);

		baseModel[16].addShapeBox(0F, 0F, 0F, 2, 7, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit31
		baseModel[16].setRotationPoint(0F, 1F, 0F);

		baseModel[17].addShapeBox(0F, 0F, 0F, 2, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit32
		baseModel[17].setRotationPoint(0F, 0F, 62F);

		baseModel[18].addShapeBox(0F, 0F, 0F, 1, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit34
		baseModel[18].setRotationPoint(111F, 1F, 18F);
		baseModel[18].rotateAngleX = 1.57079633F;

		baseModel[19].addShapeBox(0F, 0F, 0F, 1, 16, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit35
		baseModel[19].setRotationPoint(111.01F, 0.1F, 0F);

		baseModel[20].addShapeBox(0F, 0F, 0F, 2, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit36
		baseModel[20].setRotationPoint(110F, 0F, 62F);

		baseModel[21].addShapeBox(0F, 0F, 0F, 38, 24, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit40
		baseModel[21].setRotationPoint(32F, -2F, 59F);
		baseModel[21].rotateAngleX = 1.57079633F;
		baseModel[21].rotateAngleY = -1.57079633F;

		baseModel[22].addShapeBox(0F, 0F, 0F, 9, 24, 2, 0F, 0F, 0F, -5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 5F, 0F, 0F, -5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 5F); // MainShit43
		baseModel[22].setRotationPoint(32F, -4F, 16F);
		baseModel[22].rotateAngleZ = 1.57079633F;

		baseModel[23].addShapeBox(0F, 0F, 0F, 3, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit45
		baseModel[23].setRotationPoint(39F, -2F, 24F);

		baseModel[24].addShapeBox(0F, 0F, 0F, 3, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit46
		baseModel[24].setRotationPoint(69F, -2F, 24F);

		baseModel[25].addShapeBox(0F, 0F, 0F, 3, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit47
		baseModel[25].setRotationPoint(69F, -2F, 53F);

		baseModel[26].addShapeBox(0F, 0F, 0F, 3, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit48
		baseModel[26].setRotationPoint(39F, -2F, 53F);

		baseModel[27].addShapeBox(0F, 0F, 0F, 16, 7, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit49
		baseModel[27].setRotationPoint(0F, -8F, 16F);

		baseModel[28].addShapeBox(0F, 0F, 0F, 16, 24, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit50
		baseModel[28].setRotationPoint(96F, -24F, 16F);

		baseModel[29].addShapeBox(0F, 0F, 0F, 16, 16, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit51
		baseModel[29].setRotationPoint(96F, -16F, 0F);

		baseModel[30].addShapeBox(0F, 0F, 0F, 16, 6, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit52
		baseModel[30].setRotationPoint(96F, -6F, -16F);

		baseModel[31].addShapeBox(0F, 0F, 0F, 4, 1, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F); // MainShit53
		baseModel[31].setRotationPoint(102F, -7F, -16F);

		baseModel[32].addShapeBox(0F, 0F, 0F, 1, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit54
		baseModel[32].setRotationPoint(102F, -9F, -13F);
		baseModel[32].rotateAngleY = -1.57079633F;
		baseModel[32].rotateAngleZ = 1.57079633F;

		baseModel[33].addShapeBox(0F, 0F, 0F, 2, 2, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit55
		baseModel[33].setRotationPoint(102F, -18F, -4F);
		baseModel[33].rotateAngleX = -0.38397244F;
		baseModel[33].rotateAngleZ = 0.01745329F;

		baseModel[34].addShapeBox(0F, -1.5F, -1F, 5, 5, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit56
		baseModel[34].setRotationPoint(100.5F, -18F, -4F);
		baseModel[34].rotateAngleX = -0.38397244F;
		baseModel[34].rotateAngleZ = 0.01745329F;

		baseModel[35].addShapeBox(0F, 0F, 0F, 3, 10, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit57
		baseModel[35].setRotationPoint(109F, -29F, 3F);

		baseModel[36].addShapeBox(0F, 0F, 0F, 11, 14, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit58
		baseModel[36].setRotationPoint(97F, -29F, 2F);

		baseModel[37].addShapeBox(0F, 0F, 0F, 1, 6, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit59
		baseModel[37].setRotationPoint(108F, -28F, 5F);

		baseModel[38].addShapeBox(0F, 0F, 0F, 15, 7, 15, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit60
		baseModel[38].setRotationPoint(96F, 1F, -15F);

		baseModel[39].addShapeBox(0F, 0F, 0F, 2, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit61
		baseModel[39].setRotationPoint(110F, 0F, -16F);

		baseModel[40].addShapeBox(0F, 0F, 0F, 2, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit62
		baseModel[40].setRotationPoint(96F, 0F, -16F);

		baseModel[41].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit64
		baseModel[41].setRotationPoint(111F, 1F, -14F);
		baseModel[41].rotateAngleX = 1.57079633F;

		baseModel[42].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit66
		baseModel[42].setRotationPoint(98F, 1F, -16F);
		baseModel[42].rotateAngleZ = 1.57079633F;

		baseModel[43].addShapeBox(0F, 0F, 0F, 16, 16, 1, 0F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F); // MainShit70
		baseModel[43].setRotationPoint(0F, 1F, 0F);
		baseModel[43].rotateAngleX = 1.57079633F;

		baseModel[44].addShapeBox(0F, 0F, 0F, 11, 48, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit72
		baseModel[44].setRotationPoint(0F, 16F, -16F);
		baseModel[44].rotateAngleZ = 1.57079633F;

		baseModel[45].addShapeBox(0F, 0F, 0F, 16, 16, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER01
		baseModel[45].setRotationPoint(80F, -48F, 16F);

		baseModel[46].addShapeBox(0F, 0F, 0F, 16, 16, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER03
		baseModel[46].setRotationPoint(49F, -64F, 18F);

		baseModel[47].addShapeBox(0F, 0F, 0F, 9, 12, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER04
		baseModel[47].setRotationPoint(51.5F, -62F, 30F);

		baseModel[48].addShapeBox(0F, 0F, 0F, 11, 14, 11, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER05
		baseModel[48].setRotationPoint(50.5F, -63F, 36F);

		baseModel[49].addShapeBox(0F, 0F, 0F, 11, 16, 11, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER09
		baseModel[49].setRotationPoint(82.5F, -48F, 18F);

		baseModel[50].addShapeBox(0F, 0F, 0F, 16, 16, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER03
		baseModel[50].setRotationPoint(80F, -64F, 18F);

		baseModel[51].addShapeBox(0F, 0F, 0F, 15, 16, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER03
		baseModel[51].setRotationPoint(65F, -64F, 18F);

		baseModel[52].addShapeBox(0F, 0F, 0F, 11, 16, 11, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER09
		baseModel[52].setRotationPoint(80F, -50.5F, 18.5F);
		baseModel[52].rotateAngleX = 1.57079633F;
		baseModel[52].rotateAngleY = 1.57079633F;

		baseModel[53].addShapeBox(0F, 0F, 0F, 16, 8, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit60
		baseModel[53].setRotationPoint(96F, 8F, -16F);

		baseModel[54].setFlipped(true);
		baseModel[54].addShapeBox(0F, 0F, 0F, 11, 48, 16, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F); // MainShit72
		baseModel[54].setRotationPoint(48F, 16F, -16F);
		baseModel[54].rotateAngleZ = 1.57079633F;

		baseModel[55].setFlipped(true);
		baseModel[55].addShapeBox(0F, 0F, 0F, 25, 32, 8, 0F, -25F, 0F, 0F, -25F, 0F, 0F, -25F, 0F, 0F, -25F, 0F, 0F, -25F, 0F, 0F, -25F, 0F, 0F, -25F, 0F, 0F, -25F, 0F, 0F); // MainShit01
		baseModel[55].setRotationPoint(87F, 16F, 0F);
		baseModel[55].rotateAngleX = 1.57079633F;

		baseModel[56].setFlipped(true);
		baseModel[56].addShapeBox(0F, 0F, 0F, 25, 32, 8, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F); // MainShit01
		baseModel[56].setRotationPoint(0F, 16F, 32F);
		baseModel[56].rotateAngleX = 1.57079633F;

		baseModel[57].addShapeBox(0F, 0F, 0F, 25, 32, 8, 0F, -25F, -32F, 0F, -25F, -32F, 0F, -25F, -32F, 0F, -25F, -32F, 0F, -25F, -32F, 0F, -25F, -32F, 0F, -25F, -32F, 0F, -25F, -32F, 0F); // MainShit01
		baseModel[57].setRotationPoint(87F, 16F, 32F);
		baseModel[57].rotateAngleX = 1.57079633F;

		baseModel[58].addShapeBox(0F, 0F, 0F, 16, 16, 1, 0F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F); // MainShit70
		baseModel[58].setRotationPoint(16F, 1F, 0F);
		baseModel[58].rotateAngleX = 1.57079633F;

		baseModel[59].addShapeBox(0F, 0F, 0F, 16, 16, 1, 0F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F); // MainShit70
		baseModel[59].setRotationPoint(32F, 1F, 0F);
		baseModel[59].rotateAngleX = 1.57079633F;

		baseModel[60].addShapeBox(0F, 0F, 0F, 16, 16, 1, 0F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F); // MainShit70
		baseModel[60].setRotationPoint(48F, 1F, 0F);
		baseModel[60].rotateAngleX = 1.57079633F;

		baseModel[61].addShapeBox(0F, 0F, 0F, 16, 16, 1, 0F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F); // MainShit70
		baseModel[61].setRotationPoint(64F, 1F, 0F);
		baseModel[61].rotateAngleX = 1.57079633F;

		baseModel[62].addShapeBox(0F, 0F, 0F, 16, 16, 1, 0F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F); // MainShit70
		baseModel[62].setRotationPoint(80F, 1F, 0F);
		baseModel[62].rotateAngleX = 1.57079633F;

		baseModel[63].addShapeBox(0F, 0F, 0F, 5, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[63].setRotationPoint(0F, 0F, 8F);
		baseModel[63].rotateAngleX = 1.57079633F;
		baseModel[63].rotateAngleY = -1.57079633F;

		baseModel[64].addShapeBox(0F, 0F, 0F, 5, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[64].setRotationPoint(0F, 0F, 14F);
		baseModel[64].rotateAngleX = 1.57079633F;
		baseModel[64].rotateAngleY = -1.57079633F;

		baseModel[65].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[65].setRotationPoint(0F, -2F, 3F);
		baseModel[65].rotateAngleZ = 1.57079633F;

		baseModel[66].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[66].setRotationPoint(0F, -2F, 9F);
		baseModel[66].rotateAngleZ = 1.57079633F;

		baseModel[67].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[67].setRotationPoint(0F, -2F, 7F);
		baseModel[67].rotateAngleZ = 1.57079633F;

		baseModel[68].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[68].setRotationPoint(0F, -2F, 13F);
		baseModel[68].rotateAngleZ = 1.57079633F;

		baseModel[69].addShapeBox(0F, 0F, 0F, 5, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[69].setRotationPoint(16F, 0F, 8F);
		baseModel[69].rotateAngleX = 1.57079633F;
		baseModel[69].rotateAngleY = -1.57079633F;

		baseModel[70].addShapeBox(0F, 0F, 0F, 5, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[70].setRotationPoint(16F, 0F, 14F);
		baseModel[70].rotateAngleX = 1.57079633F;
		baseModel[70].rotateAngleY = -1.57079633F;

		baseModel[71].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[71].setRotationPoint(16F, -2F, 3F);
		baseModel[71].rotateAngleZ = 1.57079633F;

		baseModel[72].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[72].setRotationPoint(16F, -2F, 9F);
		baseModel[72].rotateAngleZ = 1.57079633F;

		baseModel[73].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[73].setRotationPoint(16F, -2F, 7F);
		baseModel[73].rotateAngleZ = 1.57079633F;

		baseModel[74].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[74].setRotationPoint(16F, -2F, 13F);
		baseModel[74].rotateAngleZ = 1.57079633F;

		baseModel[75].addShapeBox(0F, 0F, 0F, 5, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[75].setRotationPoint(32F, 0F, 8F);
		baseModel[75].rotateAngleX = 1.57079633F;
		baseModel[75].rotateAngleY = -1.57079633F;

		baseModel[76].addShapeBox(0F, 0F, 0F, 5, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[76].setRotationPoint(32F, 0F, 14F);
		baseModel[76].rotateAngleX = 1.57079633F;
		baseModel[76].rotateAngleY = -1.57079633F;

		baseModel[77].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[77].setRotationPoint(32F, -2F, 3F);
		baseModel[77].rotateAngleZ = 1.57079633F;

		baseModel[78].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[78].setRotationPoint(32F, -2F, 9F);
		baseModel[78].rotateAngleZ = 1.57079633F;

		baseModel[79].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[79].setRotationPoint(32F, -2F, 7F);
		baseModel[79].rotateAngleZ = 1.57079633F;

		baseModel[80].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[80].setRotationPoint(32F, -2F, 13F);
		baseModel[80].rotateAngleZ = 1.57079633F;

		baseModel[81].addShapeBox(0F, 0F, 0F, 5, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[81].setRotationPoint(48F, 0F, 8F);
		baseModel[81].rotateAngleX = 1.57079633F;
		baseModel[81].rotateAngleY = -1.57079633F;

		baseModel[82].addShapeBox(0F, 0F, 0F, 5, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[82].setRotationPoint(48F, 0F, 14F);
		baseModel[82].rotateAngleX = 1.57079633F;
		baseModel[82].rotateAngleY = -1.57079633F;

		baseModel[83].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[83].setRotationPoint(48F, -2F, 3F);
		baseModel[83].rotateAngleZ = 1.57079633F;

		baseModel[84].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[84].setRotationPoint(48F, -2F, 9F);
		baseModel[84].rotateAngleZ = 1.57079633F;

		baseModel[85].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[85].setRotationPoint(48F, -2F, 7F);
		baseModel[85].rotateAngleZ = 1.57079633F;

		baseModel[86].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[86].setRotationPoint(48F, -2F, 13F);
		baseModel[86].rotateAngleZ = 1.57079633F;

		baseModel[87].addShapeBox(0F, 0F, 0F, 5, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[87].setRotationPoint(64F, 0F, 8F);
		baseModel[87].rotateAngleX = 1.57079633F;
		baseModel[87].rotateAngleY = -1.57079633F;

		baseModel[88].addShapeBox(0F, 0F, 0F, 5, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[88].setRotationPoint(64F, 0F, 14F);
		baseModel[88].rotateAngleX = 1.57079633F;
		baseModel[88].rotateAngleY = -1.57079633F;

		baseModel[89].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[89].setRotationPoint(64F, -2F, 3F);
		baseModel[89].rotateAngleZ = 1.57079633F;

		baseModel[90].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[90].setRotationPoint(64F, -2F, 9F);
		baseModel[90].rotateAngleZ = 1.57079633F;

		baseModel[91].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[91].setRotationPoint(64F, -2F, 7F);
		baseModel[91].rotateAngleZ = 1.57079633F;

		baseModel[92].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[92].setRotationPoint(64F, -2F, 13F);
		baseModel[92].rotateAngleZ = 1.57079633F;

		baseModel[93].addShapeBox(0F, 0F, 0F, 5, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[93].setRotationPoint(80F, 0F, 8F);
		baseModel[93].rotateAngleX = 1.57079633F;
		baseModel[93].rotateAngleY = -1.57079633F;

		baseModel[94].addShapeBox(0F, 0F, 0F, 5, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[94].setRotationPoint(80F, 0F, 14F);
		baseModel[94].rotateAngleX = 1.57079633F;
		baseModel[94].rotateAngleY = -1.57079633F;

		baseModel[95].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[95].setRotationPoint(80F, -2F, 3F);
		baseModel[95].rotateAngleZ = 1.57079633F;

		baseModel[96].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[96].setRotationPoint(80F, -2F, 9F);
		baseModel[96].rotateAngleZ = 1.57079633F;

		baseModel[97].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[97].setRotationPoint(80F, -2F, 7F);
		baseModel[97].rotateAngleZ = 1.57079633F;

		baseModel[98].addShapeBox(0F, 0F, 0F, 2, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		baseModel[98].setRotationPoint(80F, -2F, 13F);
		baseModel[98].rotateAngleZ = 1.57079633F;

		baseModel[99].addShapeBox(0F, 0F, 0F, 5, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit16
		baseModel[99].setRotationPoint(93.5F, -59F, 64F);

		baseModel[100].addShapeBox(0F, 0F, 0F, 5, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit19
		baseModel[100].setRotationPoint(13.5F, -59F, 64F);

		baseModel[101].addShapeBox(0F, 0F, 0F, 25, 31, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit01
		baseModel[101].setRotationPoint(1F, 8F, 1F);
		baseModel[101].rotateAngleX = 1.57079633F;

		baseModel[102].setFlipped(true);
		baseModel[102].addShapeBox(0F, 0F, 0F, 25, 31, 7, 0F, -25F, 0F, 0F, -25F, 0F, 0F, -25F, 0F, 0F, -25F, 0F, 0F, -25F, 0F, 0F, -25F, 0F, 0F, -25F, 0F, 0F, -25F, 0F, 0F); // MainShit01
		baseModel[102].setRotationPoint(86F, 8F, 1F);
		baseModel[102].rotateAngleX = 1.57079633F;

		baseModel[103].setFlipped(true);
		baseModel[103].addShapeBox(0F, 0F, 0F, 25, 31, 7, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F); // MainShit01
		baseModel[103].setRotationPoint(1F, 8F, 32F);
		baseModel[103].rotateAngleX = 1.57079633F;

		baseModel[104].addShapeBox(0F, 0F, 0F, 25, 31, 7, 0F, -25F, -31F, 0F, -25F, -31F, 0F, -25F, -31F, 0F, -25F, -31F, 0F, -25F, -31F, 0F, -25F, -31F, 0F, -25F, -31F, 0F, -25F, -31F, 0F); // MainShit01
		baseModel[104].setRotationPoint(86F, 8F, 32F);
		baseModel[104].rotateAngleX = 1.57079633F;

		baseModel[105].setFlipped(true);
		baseModel[105].addShapeBox(0F, 0F, 0F, 31, 32, 8, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F); // MainShit01
		baseModel[105].setRotationPoint(25F, 16F, 0F);
		baseModel[105].rotateAngleX = 1.57079633F;

		baseModel[106].addShapeBox(0F, 0F, 0F, 31, 32, 8, 0F, -31F, -32F, 0F, -31F, -32F, 0F, -31F, -32F, 0F, -31F, -32F, 0F, -31F, -32F, 0F, -31F, -32F, 0F, -31F, -32F, 0F, -31F, -32F, 0F); // MainShit01
		baseModel[106].setRotationPoint(25F, 16F, 32F);
		baseModel[106].rotateAngleX = 1.57079633F;

		baseModel[107].addShapeBox(0F, 0F, 0F, 31, 32, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit01
		baseModel[107].setRotationPoint(56F, 16F, 0F);
		baseModel[107].rotateAngleX = 1.57079633F;

		baseModel[108].setFlipped(true);
		baseModel[108].addShapeBox(0F, 0F, 0F, 31, 32, 8, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F); // MainShit01
		baseModel[108].setRotationPoint(56F, 16F, 32F);
		baseModel[108].rotateAngleX = 1.57079633F;

		baseModel[109].addShapeBox(0F, 0F, 0F, 30, 31, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit01
		baseModel[109].setRotationPoint(26F, 8F, 1F);
		baseModel[109].rotateAngleX = 1.57079633F;

		baseModel[110].setFlipped(true);
		baseModel[110].addShapeBox(0F, 0F, 0F, 30, 31, 8, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F); // MainShit01
		baseModel[110].setRotationPoint(26F, 8F, 32F);
		baseModel[110].rotateAngleX = 1.57079633F;

		baseModel[111].addShapeBox(0F, 0F, 0F, 30, 31, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit01
		baseModel[111].setRotationPoint(56F, 8F, 1F);
		baseModel[111].rotateAngleX = 1.57079633F;

		baseModel[112].setFlipped(true);
		baseModel[112].addShapeBox(0F, 0F, 0F, 30, 31, 8, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F); // MainShit01
		baseModel[112].setRotationPoint(56F, 8F, 32F);
		baseModel[112].rotateAngleX = 1.57079633F;

		baseModel[113].addShapeBox(0F, 0F, 0F, 2, 7, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit31
		baseModel[113].setRotationPoint(94F, 1F, 0F);

		baseModel[114].addShapeBox(0F, 0F, 0F, 20, 2, 1, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit13
		baseModel[114].setRotationPoint(6F, -64F, 32F);

		baseModel[115].addShapeBox(0F, 0F, 0F, 20, 38, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit13
		baseModel[115].setRotationPoint(6F, -62F, 63F);

		baseModel[116].addShapeBox(0F, 0F, 0F, 20, 2, 1, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit13
		baseModel[116].setRotationPoint(6F, -64F, 63F);

		baseModel[117].addShapeBox(0F, 0F, 0F, 20, 38, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit13
		baseModel[117].setRotationPoint(86F, -62F, 32F);

		baseModel[118].addShapeBox(0F, 0F, 0F, 20, 2, 1, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit13
		baseModel[118].setRotationPoint(86F, -64F, 32F);

		baseModel[119].addShapeBox(0F, 0F, 0F, 20, 38, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit13
		baseModel[119].setRotationPoint(86F, -62F, 63F);

		baseModel[120].addShapeBox(0F, 0F, 0F, 20, 2, 1, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit13
		baseModel[120].setRotationPoint(86F, -64F, 63F);

		baseModel[121].addShapeBox(0F, 0F, 0F, 16, 16, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER01
		baseModel[121].setRotationPoint(80F, -32F, 16F);

		baseModel[122].addShapeBox(0F, 0F, 0F, 16, 16, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER01
		baseModel[122].setRotationPoint(80F, -16F, 16F);

		baseModel[123].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit54
		baseModel[123].setRotationPoint(102.5F, -9F, -14F);
		baseModel[123].rotateAngleY = -1.57079633F;

		baseModel[124].setFlipped(true);
		baseModel[124].addShapeBox(0F, 0F, 0F, 24, 15, 2, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F); // MainShit26
		baseModel[124].setRotationPoint(56F, -28F, 16F);

		baseModel[125].addShapeBox(0F, 0F, 0F, 24, 15, 2, 0F, -24F, 0F, -2F, -24F, 0F, -2F, -24F, 0F, -2F, -24F, 0F, -2F, -24F, 0F, -2F, -24F, 0F, -2F, -24F, 0F, -2F, -24F, 0F, -2F); // MainShit27
		baseModel[125].setRotationPoint(56F, -28F, 62F);

		baseModel[126].addShapeBox(0F, 0F, 0F, 24, 15, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit27
		baseModel[126].setRotationPoint(32.05F, -28F, 63.95F);
		baseModel[126].rotateAngleY = -1.57079633F;

		baseModel[127].setFlipped(true);
		baseModel[127].addShapeBox(0F, 0F, 0F, 24, 15, 2, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F); // MainShit27
		baseModel[127].setRotationPoint(32.05F, -28F, 40.05F);
		baseModel[127].rotateAngleY = -1.57079633F;

		baseModel[128].setFlipped(true);
		baseModel[128].addShapeBox(0F, 0F, 0F, 24, 15, 2, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F); // MainShit27
		baseModel[128].setRotationPoint(77.95F, -28F, 63.95F);
		baseModel[128].rotateAngleY = -1.57079633F;

		baseModel[129].addShapeBox(0F, 0F, 0F, 24, 15, 2, 0F, -24F, 0F, -2F, -24F, 0F, -2F, -24F, 0F, -2F, -24F, 0F, -2F, -24F, 0F, -2F, -24F, 0F, -2F, -24F, 0F, -2F, -24F, 0F, -2F); // MainShit27
		baseModel[129].setRotationPoint(77.95F, -28F, 40.05F);
		baseModel[129].rotateAngleY = -1.57079633F;

		baseModel[130].addShapeBox(0F, 0F, 0F, 1, 6, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit54
		baseModel[130].setRotationPoint(102.5F, -7F, -2F);
		baseModel[130].rotateAngleY = -1.57079633F;
		baseModel[130].rotateAngleZ = 1.57079633F;

		baseModel[131].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit54
		baseModel[131].setRotationPoint(102.5F, -8F, -5.5F);
		baseModel[131].rotateAngleY = -1.57079633F;
		baseModel[131].rotateAngleZ = 1.57079633F;

		baseModel[132].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit54
		baseModel[132].setRotationPoint(102.5F, -8F, -2.5F);
		baseModel[132].rotateAngleY = -1.57079633F;
		baseModel[132].rotateAngleZ = 1.57079633F;

		baseModel[133].addShapeBox(0F, 0F, 0F, 2, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit61
		baseModel[133].setRotationPoint(110F, 0F, -2F);

		baseModel[134].addShapeBox(0F, 0F, 0F, 2, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit36
		baseModel[134].setRotationPoint(110F, 0F, 16F);

		baseModel[135].addShapeBox(0F, 0F, 0F, 1, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit34
		baseModel[135].setRotationPoint(111F, 1F, 32F);
		baseModel[135].rotateAngleX = 1.57079633F;

		baseModel[136].addShapeBox(0F, 0F, 0F, 1, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit34
		baseModel[136].setRotationPoint(111F, 1F, 46F);
		baseModel[136].rotateAngleX = 1.57079633F;

		baseModel[137].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit34
		baseModel[137].setRotationPoint(111F, 1F, 60F);
		baseModel[137].rotateAngleX = 1.57079633F;

		baseModel[138].addShapeBox(0F, 0F, 0F, 1, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit34
		baseModel[138].setRotationPoint(0F, 1F, 16F);
		baseModel[138].rotateAngleX = 1.57079633F;

		baseModel[139].addShapeBox(0F, 0F, 0F, 1, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit34
		baseModel[139].setRotationPoint(0F, 1F, 30F);
		baseModel[139].rotateAngleX = 1.57079633F;

		baseModel[140].addShapeBox(0F, 0F, 0F, 1, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit34
		baseModel[140].setRotationPoint(0F, 1F, 44F);
		baseModel[140].rotateAngleX = 1.57079633F;

		baseModel[141].addShapeBox(0F, 0F, 0F, 1, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit34
		baseModel[141].setRotationPoint(0F, 1F, 58F);
		baseModel[141].rotateAngleX = 1.57079633F;

		baseModel[142].addShapeBox(0F, 0F, 0F, 1, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit34
		baseModel[142].setRotationPoint(110F, 1F, 63F);
		baseModel[142].rotateAngleX = 1.57079633F;
		baseModel[142].rotateAngleY = 1.57079633F;

		baseModel[143].addShapeBox(0F, 0F, 0F, 1, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit34
		baseModel[143].setRotationPoint(96F, 1F, 63F);
		baseModel[143].rotateAngleX = 1.57079633F;
		baseModel[143].rotateAngleY = 1.57079633F;

		baseModel[144].addShapeBox(0F, 0F, 0F, 1, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit34
		baseModel[144].setRotationPoint(82F, 1F, 63F);
		baseModel[144].rotateAngleX = 1.57079633F;
		baseModel[144].rotateAngleY = 1.57079633F;

		baseModel[145].addShapeBox(0F, 0F, 0F, 1, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit34
		baseModel[145].setRotationPoint(68F, 1F, 63F);
		baseModel[145].rotateAngleX = 1.57079633F;
		baseModel[145].rotateAngleY = 1.57079633F;

		baseModel[146].addShapeBox(0F, 0F, 0F, 1, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit34
		baseModel[146].setRotationPoint(54F, 1F, 63F);
		baseModel[146].rotateAngleX = 1.57079633F;
		baseModel[146].rotateAngleY = 1.57079633F;

		baseModel[147].addShapeBox(0F, 0F, 0F, 1, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit34
		baseModel[147].setRotationPoint(40F, 1F, 63F);
		baseModel[147].rotateAngleX = 1.57079633F;
		baseModel[147].rotateAngleY = 1.57079633F;

		baseModel[148].addShapeBox(0F, 0F, 0F, 1, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit34
		baseModel[148].setRotationPoint(26F, 1F, 63F);
		baseModel[148].rotateAngleX = 1.57079633F;
		baseModel[148].rotateAngleY = 1.57079633F;

		baseModel[149].addShapeBox(0F, 0F, 0F, 1, 10, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit34
		baseModel[149].setRotationPoint(12F, 1F, 63F);
		baseModel[149].rotateAngleX = 1.57079633F;
		baseModel[149].rotateAngleY = 1.57079633F;

		baseModel[150].addShapeBox(0F, 0F, 0F, 8, 4, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER02
		baseModel[150].setRotationPoint(52F, -49F, 37.5F);

		baseModel[151].addShapeBox(0F, 0F, 0F, 11, 16, 11, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER09
		baseModel[151].setRotationPoint(82.5F, -32F, 18F);

		baseModel[152].addShapeBox(0F, 0F, 0F, 11, 16, 11, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER09
		baseModel[152].setRotationPoint(82.5F, -16F, 18F);

		baseModel[153].setFlipped(true);
		baseModel[153].addShapeBox(0F, 0F, 0F, 9, 24, 2, 0F, 0F, -24F, -5F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 5F, 0F, -24F, -5F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 5F); // MainShit43
		baseModel[153].setRotationPoint(56F, -4F, 16F);
		baseModel[153].rotateAngleZ = 1.57079633F;

		baseModel[154].addShapeBox(0F, 0F, 0F, 9, 24, 2, 0F, 0F, 0F, 5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -5F, 0F, 0F, 5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -5F); // MainShit43
		baseModel[154].setRotationPoint(32F, -4F, 62F);
		baseModel[154].rotateAngleZ = 1.57079633F;

		baseModel[155].setFlipped(true);
		baseModel[155].addShapeBox(0F, 0F, 0F, 9, 24, 2, 0F, 0F, -24F, 5F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, -5F, 0F, -24F, 5F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, -5F); // MainShit43
		baseModel[155].setRotationPoint(56F, -4F, 62F);
		baseModel[155].rotateAngleZ = 1.57079633F;

		baseModel[156].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(5, 0, 5, 0), new Coord2D(22, 0, 22, 0), new Coord2D(22, 9, 22, 9), new Coord2D(0, 9, 0, 9)}), 2, 22, 9, 59, 2, ModelRendererTurbo.MR_FRONT, new float[]{11, 22, 9, 17}); // Shape 240
		baseModel[156].setRotationPoint(34.05F, -4F, 18F);
		baseModel[156].rotateAngleY = -1.57079633F;

		baseModel[157].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(5, 0, 5, 0), new Coord2D(22, 0, 22, 0), new Coord2D(22, 9, 22, 9), new Coord2D(0, 9, 0, 9)}), 2, 22, 9, 59, 2, ModelRendererTurbo.MR_FRONT, new float[]{11, 22, 9, 17}); // Shape 240
		baseModel[157].setRotationPoint(32.05F, -4F, 62F);
		baseModel[157].rotateAngleY = 1.57079633F;

		baseModel[158].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(5, 0, 5, 0), new Coord2D(22, 0, 22, 0), new Coord2D(22, 9, 22, 9), new Coord2D(0, 9, 0, 9)}), 2, 22, 9, 59, 2, ModelRendererTurbo.MR_FRONT, new float[]{11, 22, 9, 17}); // Shape 240
		baseModel[158].setRotationPoint(79.95F, -4F, 18F);
		baseModel[158].rotateAngleY = -1.57079633F;

		baseModel[159].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(5, 0, 5, 0), new Coord2D(22, 0, 22, 0), new Coord2D(22, 9, 22, 9), new Coord2D(0, 9, 0, 9)}), 2, 22, 9, 59, 2, ModelRendererTurbo.MR_FRONT, new float[]{11, 22, 9, 17}); // Shape 240
		baseModel[159].setRotationPoint(77.95F, -4F, 62F);
		baseModel[159].rotateAngleY = 1.57079633F;

		baseModel[160].addShapeBox(0F, 0F, 0F, 6, 2, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit53
		baseModel[160].setRotationPoint(95F, -8F, -16F);
		baseModel[160].rotateAngleY = 0.10471976F;

		baseModel[161].addShapeBox(0F, 0F, 0F, 6, 2, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit53
		baseModel[161].setRotationPoint(95F, -10F, -16F);
		baseModel[161].rotateAngleY = -0.03490659F;

		baseModel[162].addShapeBox(0F, 0F, 0F, 1, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit54
		baseModel[162].setRotationPoint(103.5F, -9F, -9F);
		baseModel[162].rotateAngleY = -1.57079633F;
		baseModel[162].rotateAngleZ = 1.57079633F;

		baseModel[163].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit54
		baseModel[163].setRotationPoint(104F, -9F, -10F);
		baseModel[163].rotateAngleY = -1.57079633F;

		baseModel[164].addShapeBox(0F, 0F, 0F, 2, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit49
		baseModel[164].setRotationPoint(0F, -24F, 16F);

		baseModel[165].addShapeBox(0F, 0F, 0F, 2, 2, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit49
		baseModel[165].setRotationPoint(0F, -24F, 12F);

		baseModel[166].addShapeBox(0F, 0F, 0F, 2, 2, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit49
		baseModel[166].setRotationPoint(94F, -24F, 12F);

		baseModel[167].addShapeBox(0F, 0F, 0F, 2, 2, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit49
		baseModel[167].setRotationPoint(54F, -24F, 12F);

		baseModel[168].addShapeBox(0F, 0F, 0F, 16, 1, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit49
		baseModel[168].setRotationPoint(0F, -1F, 16F);

		baseModel[169].setFlipped(true);
		baseModel[169].addShapeBox(0F, 0F, 0F, 38, 24, 2, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F); // MainShit40
		baseModel[169].setRotationPoint(56F, -2F, 59F);
		baseModel[169].rotateAngleX = 1.57079633F;
		baseModel[169].rotateAngleY = -1.57079633F;

		baseModel[170].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit66
		baseModel[170].setRotationPoint(97F, 1F, -14F);
		baseModel[170].rotateAngleY = 1.57079633F;
		baseModel[170].rotateAngleZ = 1.57079633F;

		baseModel[171].addShapeBox(0F, 0F, 0F, 18, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit02
		baseModel[171].setRotationPoint(7F, -19F, 64F);


		tankRightModel = new ModelRendererTurbo[7];
		tankRightModel[0] = new ModelRendererTurbo(this, 8, 2, textureX, textureY); // BIGbucketOne01
		tankRightModel[1] = new ModelRendererTurbo(this, 190, 1, textureX, textureY); // BIGbucketOne02
		tankRightModel[2] = new ModelRendererTurbo(this, 133, 3, textureX, textureY); // BIGbucketOne03
		tankRightModel[3] = new ModelRendererTurbo(this, 98, 20, textureX, textureY); // BIGbucketOne04
		tankRightModel[4] = new ModelRendererTurbo(this, 98, 20, textureX, textureY); // BIGbucketOne05
		tankRightModel[5] = new ModelRendererTurbo(this, 8, 2, textureX, textureY); // BIGbucketOne01
		tankRightModel[6] = new ModelRendererTurbo(this, 87, 0, textureX, textureY); // ValveOne02

		tankRightModel[0].addShapeBox(0F, 0F, 0F, 30, 30, 15, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BIGbucketOne01
		tankRightModel[0].setRotationPoint(1F, -64F, 33F);

		tankRightModel[1].addShapeBox(0F, 0F, 0F, 8, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BIGbucketOne02
		tankRightModel[1].setRotationPoint(0F, -60F, 61F);
		tankRightModel[1].rotateAngleY = -1.57079633F;

		tankRightModel[2].addShapeBox(0F, 0F, 0F, 8, 8, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BIGbucketOne03
		tankRightModel[2].setRotationPoint(31F, -60F, 52F);
		tankRightModel[2].rotateAngleY = -1.57079633F;

		tankRightModel[3].addShapeBox(0F, 0F, 0F, 4, 2, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BIGbucketOne04
		tankRightModel[3].setRotationPoint(0.5F, -34F, 39F);

		tankRightModel[4].addShapeBox(0F, 0F, 0F, 4, 2, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BIGbucketOne05
		tankRightModel[4].setRotationPoint(0.5F, -34F, 53F);

		tankRightModel[5].setFlipped(true);
		tankRightModel[5].addShapeBox(0F, 0F, 0F, 30, 30, 15, 0F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F); // BIGbucketOne01
		tankRightModel[5].setRotationPoint(1F, -64F, 48F);

		tankRightModel[6].addShapeBox(0F, 0F, 0F, 5, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // ValveOne02
		tankRightModel[6].setRotationPoint(31.5F, -61F, 47F);


		valveRightModel = new ModelRendererTurbo[1];
		valveRightModel[0] = new ModelRendererTurbo(this, 236, 1, textureX, textureY); // ValveOne01

		valveRightModel[0].addShapeBox(0F, 0F, 0F, 8, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // ValveOne01
		valveRightModel[0].setRotationPoint(35F, -60F, 52F);
		valveRightModel[0].rotateAngleY = -1.57079633F;


		pistonsRightModel = new ModelRendererTurbo[2];
		pistonsRightModel[0] = new ModelRendererTurbo(this, 78, 100, textureX, textureY); // PistonsOne01
		pistonsRightModel[1] = new ModelRendererTurbo(this, 78, 100, textureX, textureY); // PistonsOne02

		pistonsRightModel[0].addShapeBox(0F, 0F, 0F, 3, 9, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // PistonsOne01
		pistonsRightModel[0].setRotationPoint(1F, -34F, 39.5F);

		pistonsRightModel[1].addShapeBox(0F, 0F, 0F, 3, 9, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // PistonsOne02
		pistonsRightModel[1].setRotationPoint(1F, -34F, 53.5F);


		tankLeftModel = new ModelRendererTurbo[7];
		tankLeftModel[0] = new ModelRendererTurbo(this, 8, 2, textureX, textureY); // BIGbucketTwo01
		tankLeftModel[1] = new ModelRendererTurbo(this, 98, 20, textureX, textureY); // BIGbucketTwo02
		tankLeftModel[2] = new ModelRendererTurbo(this, 98, 20, textureX, textureY); // BIGbucketTwo03
		tankLeftModel[3] = new ModelRendererTurbo(this, 190, 1, textureX, textureY); // BIGbucketTwo04
		tankLeftModel[4] = new ModelRendererTurbo(this, 133, 3, textureX, textureY); // BIGbucketTwo05
		tankLeftModel[5] = new ModelRendererTurbo(this, 8, 2, textureX, textureY); // BIGbucketTwo01
		tankLeftModel[6] = new ModelRendererTurbo(this, 87, 0, textureX, textureY); // ValveTwo02

		tankLeftModel[0].addShapeBox(0F, 0F, 0F, 30, 30, 15, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BIGbucketTwo01
		tankLeftModel[0].setRotationPoint(81F, -64F, 33F);

		tankLeftModel[1].addShapeBox(0F, 0F, 0F, 4, 2, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BIGbucketTwo02
		tankLeftModel[1].setRotationPoint(107.5F, -34F, 53F);

		tankLeftModel[2].addShapeBox(0F, 0F, 0F, 4, 2, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BIGbucketTwo03
		tankLeftModel[2].setRotationPoint(107.5F, -34F, 39F);

		tankLeftModel[3].addShapeBox(0F, 0F, 0F, 8, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BIGbucketTwo04
		tankLeftModel[3].setRotationPoint(111F, -60F, 61F);
		tankLeftModel[3].rotateAngleY = -1.57079633F;

		tankLeftModel[4].setFlipped(true);
		tankLeftModel[4].addShapeBox(0F, 0F, 0F, 8, 8, 4, 0F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F); // BIGbucketTwo05
		tankLeftModel[4].setRotationPoint(77F, -60F, 52F);
		tankLeftModel[4].rotateAngleY = -1.57079633F;

		tankLeftModel[5].setFlipped(true);
		tankLeftModel[5].addShapeBox(0F, 0F, 0F, 30, 30, 15, 0F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F); // BIGbucketTwo01
		tankLeftModel[5].setRotationPoint(81F, -64F, 48F);

		tankLeftModel[6].addShapeBox(0F, 0F, 0F, 5, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // ValveTwo02
		tankLeftModel[6].setRotationPoint(75.5F, -61F, 47F);


		valveLeftModel = new ModelRendererTurbo[1];
		valveLeftModel[0] = new ModelRendererTurbo(this, 236, 1, textureX, textureY); // ValveTwo01

		valveLeftModel[0].setFlipped(true);
		valveLeftModel[0].addShapeBox(0F, 0F, -2F, 8, 8, 2, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F); // ValveTwo01
		valveLeftModel[0].setRotationPoint(77F, -60F, 52F);
		valveLeftModel[0].rotateAngleY = -1.57079633F;


		pistonsLeftModel = new ModelRendererTurbo[2];
		pistonsLeftModel[0] = new ModelRendererTurbo(this, 78, 100, textureX, textureY); // PistonsTwo01
		pistonsLeftModel[1] = new ModelRendererTurbo(this, 78, 100, textureX, textureY); // PistonsTwo02

		pistonsLeftModel[0].addShapeBox(0F, 0F, 0F, 3, 9, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // PistonsTwo01
		pistonsLeftModel[0].setRotationPoint(108F, -34F, 53.5F);

		pistonsLeftModel[1].addShapeBox(0F, 0F, 0F, 3, 9, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // PistonsTwo02
		pistonsLeftModel[1].setRotationPoint(108F, -34F, 39.5F);

		mixerModel = new ModelRendererTurbo[7];
		mixerModel[0] = new ModelRendererTurbo(this, 14, 92, textureX, textureY); // MIXER02
		mixerModel[1] = new ModelRendererTurbo(this, 20, 74, textureX, textureY); // MIXER06
		mixerModel[2] = new ModelRendererTurbo(this, 190, 10, textureX, textureY); // MIXER08
		mixerModel[3] = new ModelRendererTurbo(this, 190, 10, textureX, textureY); // MIXER08
		mixerModel[4] = new ModelRendererTurbo(this, 190, 10, textureX, textureY); // MIXER08
		mixerModel[5] = new ModelRendererTurbo(this, 190, 10, textureX, textureY); // MIXER08
		mixerModel[6] = new ModelRendererTurbo(this, 14, 92, textureX, textureY); // MIXER02

		mixerModel[0].addShapeBox(-3F, 0F, -3F, 6, 12, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER02
		mixerModel[0].setRotationPoint(56F, -45F, 41F);

		mixerModel[1].addShapeBox(-3.5F, 0F, -3.5F, 7, 11, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER06
		mixerModel[1].setRotationPoint(56F, -21F, 41F);

		mixerModel[2].addShapeBox(-14.5F, 0F, 0F, 11, 9, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER08
		mixerModel[2].setRotationPoint(56F, -20F, 41F);

		mixerModel[3].addShapeBox(-14.5F, 0F, 0F, 11, 9, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER08
		mixerModel[3].setRotationPoint(56F, -20F, 41F);
		mixerModel[3].rotateAngleY = -1.57079633F;

		mixerModel[4].addShapeBox(-14.5F, 0F, 0F, 11, 9, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER08
		mixerModel[4].setRotationPoint(56F, -20F, 41F);
		mixerModel[4].rotateAngleY = -3.14159265F;

		mixerModel[5].addShapeBox(-14.5F, 0F, 0F, 11, 9, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER08
		mixerModel[5].setRotationPoint(56F, -20F, 41F);
		mixerModel[5].rotateAngleY = -4.71238898F;

		mixerModel[6].addShapeBox(-3F, 0F, -3F, 6, 12, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MIXER02
		mixerModel[6].setRotationPoint(56F, -33F, 41F);


		starterSlideModel = new ModelRendererTurbo[1];
		starterSlideModel[0] = new ModelRendererTurbo(this, 194, 88, textureX, textureY); // GaugeARROW

		starterSlideModel[0].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GaugeARROW
		starterSlideModel[0].setRotationPoint(102.5F, -19F, -5.3F);
		starterSlideModel[0].rotateAngleX = -0.38397244F;
		starterSlideModel[0].rotateAngleZ = 0.01745329F;


		bucketHolderModel = new ModelRendererTurbo[1];
		bucketHolderModel[0] = new ModelRendererTurbo(this, 190, 20, textureX, textureY); // BucketHolder01

		bucketHolderModel[0].addShapeBox(0F, 0F, 0F, 11, 11, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BucketHolder01
		bucketHolderModel[0].setRotationPoint(2.5F, 3F, -14F);
		bucketHolderModel[0].rotateAngleX = 1.57079633F;


		bucketRailModel = new ModelRendererTurbo[4];
		bucketRailModel[0] = new ModelRendererTurbo(this, 20, 110, textureX, textureY); // MainShit73
		bucketRailModel[1] = new ModelRendererTurbo(this, 2, 83, textureX, textureY); // MainShit74
		bucketRailModel[2] = new ModelRendererTurbo(this, 20, 110, textureX, textureY); // MainShit75
		bucketRailModel[3] = new ModelRendererTurbo(this, 218, 2, textureX, textureY); // MainShit76

		bucketRailModel[0].addShapeBox(0F, 0F, 0F, 5, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit73
		bucketRailModel[0].setRotationPoint(5.5F, 5F, -16F);
		bucketRailModel[0].rotateAngleX = 1.57079633F;

		bucketRailModel[1].addShapeBox(0F, 0F, 0F, 5, 10, 2, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit74
		bucketRailModel[1].setRotationPoint(5.5F, 3F, -18F);

		bucketRailModel[2].addShapeBox(0F, 0F, 0F, 5, 5, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit75
		bucketRailModel[2].setRotationPoint(5.5F, -2F, -2F);

		bucketRailModel[3].addShapeBox(0F, -4F, -1F, 7, 7, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit76
		bucketRailModel[3].setRotationPoint(4.5F, -2F, -2F);
		bucketRailModel[3].rotateAngleX = -0.17453293F;

		bucketTimerModel = new ModelRendererTurbo[1];
		bucketTimerModel[0] = new ModelRendererTurbo(this, 101, 1, textureX, textureY); // MainShit76

		bucketTimerModel[0].addShapeBox(0F, -0.5F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit76
		bucketTimerModel[0].setRotationPoint(7.5F, -2.5F, -3.25F);
		bucketTimerModel[0].rotateAngleX = -0.17453293F;


		cartModel = new ModelRendererTurbo[26];
		cartModel[0] = new ModelRendererTurbo(this, 50, 76, textureX, textureY); // MainShit70
		cartModel[1] = new ModelRendererTurbo(this, 50, 76, textureX, textureY); // MainShit70
		cartModel[2] = new ModelRendererTurbo(this, 50, 76, textureX, textureY); // MainShit70
		cartModel[3] = new ModelRendererTurbo(this, 50, 76, textureX, textureY); // MainShit70
		cartModel[4] = new ModelRendererTurbo(this, 6, 160, textureX, textureY); // MainShit70
		cartModel[5] = new ModelRendererTurbo(this, 43, 128, textureX, textureY); // MainShit70
		cartModel[6] = new ModelRendererTurbo(this, 43, 128, textureX, textureY); // MainShit70
		cartModel[7] = new ModelRendererTurbo(this, 27, 192, textureX, textureY); // MainShit70
		cartModel[8] = new ModelRendererTurbo(this, 38, 160, textureX, textureY); // MainShit70
		cartModel[9] = new ModelRendererTurbo(this, 38, 160, textureX, textureY); // MainShit70
		cartModel[10] = new ModelRendererTurbo(this, 43, 133, textureX, textureY); // MainShit70
		cartModel[11] = new ModelRendererTurbo(this, 43, 133, textureX, textureY); // MainShit70
		cartModel[12] = new ModelRendererTurbo(this, 25, 138, textureX, textureY); // MainShit70
		cartModel[13] = new ModelRendererTurbo(this, 90, 128, textureX, textureY); // MainShit70
		cartModel[14] = new ModelRendererTurbo(this, 90, 134, textureX, textureY); // MainShit70
		cartModel[15] = new ModelRendererTurbo(this, 37, 139, textureX, textureY); // MainShit70
		cartModel[16] = new ModelRendererTurbo(this, 64, 160, textureX, textureY); // MainShit70
		cartModel[17] = new ModelRendererTurbo(this, 52, 160, textureX, textureY); // MainShit70
		cartModel[18] = new ModelRendererTurbo(this, 11, 139, textureX, textureY); // MainShit70
		cartModel[19] = new ModelRendererTurbo(this, 11, 133, textureX, textureY); // MainShit70
		cartModel[20] = new ModelRendererTurbo(this, 6, 136, textureX, textureY); // MainShit70
		cartModel[21] = new ModelRendererTurbo(this, 90, 184, textureX, textureY); // MainShit70
		cartModel[22] = new ModelRendererTurbo(this, 90, 184, textureX, textureY); // MainShit70
		cartModel[23] = new ModelRendererTurbo(this, 20, 182, textureX, textureY); // MainShit70
		cartModel[24] = new ModelRendererTurbo(this, 90, 184, textureX, textureY); // MainShit70
		cartModel[25] = new ModelRendererTurbo(this, 90, 184, textureX, textureY); // MainShit70

		cartModel[0].addShapeBox(0F, 0F, 0F, 3, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[0].setRotationPoint(0F, -4F, 13F);
		cartModel[0].rotateAngleX = 1.57079633F;
		cartModel[0].rotateAngleY = -1.57079633F;

		cartModel[1].addShapeBox(0F, 0F, 0F, 3, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[1].setRotationPoint(8F, -4F, 13F);
		cartModel[1].rotateAngleX = 1.57079633F;
		cartModel[1].rotateAngleY = -1.57079633F;

		cartModel[2].addShapeBox(0F, 0F, 0F, 3, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[2].setRotationPoint(0F, -4F, 7F);
		cartModel[2].rotateAngleX = 1.57079633F;
		cartModel[2].rotateAngleY = -1.57079633F;

		cartModel[3].addShapeBox(0F, 0F, 0F, 3, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[3].setRotationPoint(8F, -4F, 7F);
		cartModel[3].rotateAngleX = 1.57079633F;
		cartModel[3].rotateAngleY = -1.57079633F;

		cartModel[4].addShapeBox(0F, 0F, 0F, 10, 14, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[4].setRotationPoint(1F, -6F, 12F);
		cartModel[4].rotateAngleX = 1.57079633F;
		cartModel[4].rotateAngleY = -1.57079633F;

		cartModel[5].addShapeBox(0F, 0F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[5].setRotationPoint(2F, -12F, 12F);

		cartModel[6].addShapeBox(0F, 0F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[6].setRotationPoint(8F, -12F, 12F);

		cartModel[7].addShapeBox(0F, 0F, 0F, 8, 8, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[7].setRotationPoint(4F, -14F, 11F);
		cartModel[7].rotateAngleX = 1.57079633F;
		cartModel[7].rotateAngleY = -1.57079633F;

		cartModel[8].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[8].setRotationPoint(1F, -14.5F, 10.5F);
		cartModel[8].rotateAngleX = 1.57079633F;
		cartModel[8].rotateAngleY = -1.57079633F;

		cartModel[9].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[9].setRotationPoint(1F, -14.5F, 6.5F);
		cartModel[9].rotateAngleX = 1.57079633F;
		cartModel[9].rotateAngleY = -1.57079633F;

		cartModel[10].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[10].setRotationPoint(-2F, -14.5F, 10.5F);
		cartModel[10].rotateAngleX = 1.57079633F;
		cartModel[10].rotateAngleY = -1.57079633F;

		cartModel[11].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[11].setRotationPoint(-2F, -14.5F, 6.5F);
		cartModel[11].rotateAngleX = 1.57079633F;
		cartModel[11].rotateAngleY = -1.57079633F;

		cartModel[12].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[12].setRotationPoint(-2F, -11.5F, 10.5F);
		cartModel[12].rotateAngleX = 1.57079633F;
		cartModel[12].rotateAngleY = -1.57079633F;

		cartModel[13].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[13].setRotationPoint(-2F, -8.5F, 10.5F);
		cartModel[13].rotateAngleX = 1.57079633F;
		cartModel[13].rotateAngleY = -1.57079633F;

		cartModel[14].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[14].setRotationPoint(-2F, -9.5F, 6.5F);
		cartModel[14].rotateAngleX = 1.57079633F;
		cartModel[14].rotateAngleY = -1.57079633F;

		cartModel[15].addShapeBox(0F, 0F, 0F, 3, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[15].setRotationPoint(-2F, -12.5F, 6.5F);
		cartModel[15].rotateAngleX = 1.57079633F;
		cartModel[15].rotateAngleY = -1.57079633F;

		cartModel[16].addShapeBox(0F, 0F, 0F, 2, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[16].setRotationPoint(-2F, -9.5F, 3.5F);
		cartModel[16].rotateAngleX = 1.57079633F;
		cartModel[16].rotateAngleY = -1.57079633F;

		cartModel[17].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[17].setRotationPoint(-2F, -9.5F, 1.5F);
		cartModel[17].rotateAngleX = 1.57079633F;
		cartModel[17].rotateAngleY = -1.57079633F;

		cartModel[18].addShapeBox(0F, 0F, 0F, 3, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[18].setRotationPoint(1F, -9.5F, 1.5F);
		cartModel[18].rotateAngleX = 1.57079633F;
		cartModel[18].rotateAngleY = -1.57079633F;

		cartModel[19].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[19].setRotationPoint(3F, -9.5F, 1.5F);
		cartModel[19].rotateAngleX = 1.57079633F;
		cartModel[19].rotateAngleY = -1.57079633F;

		cartModel[20].addShapeBox(0F, 0F, 0F, 1, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[20].setRotationPoint(3F, -9.5F, 2.5F);
		cartModel[20].rotateAngleX = 1.57079633F;
		cartModel[20].rotateAngleY = -1.57079633F;

		cartModel[21].addShapeBox(0F, 0F, 0F, 1, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[21].setRotationPoint(6F, -15.5F, 11F);
		cartModel[21].rotateAngleX = 2.39110108F;

		cartModel[22].addShapeBox(0F, 0F, 0F, 1, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[22].setRotationPoint(9F, -15.5F, 11F);
		cartModel[22].rotateAngleX = 2.39110108F;

		cartModel[23].addShapeBox(0F, 0F, 0F, 2, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[23].setRotationPoint(5F, -19.5F, 14.5F);
		cartModel[23].rotateAngleZ = 1.57079633F;

		cartModel[24].addShapeBox(0F, 0F, 0F, 1, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[24].setRotationPoint(6F, -21.5F, 17.5F);
		cartModel[24].rotateAngleY = -1.57079633F;
		cartModel[24].rotateAngleZ = 1.57079633F;

		cartModel[25].addShapeBox(0F, 0F, 0F, 1, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartModel[25].setRotationPoint(9F, -21.5F, 17.5F);
		cartModel[25].rotateAngleY = -1.57079633F;
		cartModel[25].rotateAngleZ = 1.57079633F;


		cartWheelsModel = new ModelRendererTurbo[4];
		cartWheelsModel[0] = new ModelRendererTurbo(this, 33, 128, textureX, textureY); // MainShit70
		cartWheelsModel[1] = new ModelRendererTurbo(this, 33, 128, textureX, textureY); // MainShit70
		cartWheelsModel[2] = new ModelRendererTurbo(this, 33, 128, textureX, textureY); // MainShit70
		cartWheelsModel[3] = new ModelRendererTurbo(this, 33, 128, textureX, textureY); // MainShit70

		cartWheelsModel[0].addShapeBox(-1.5F, -1.5F, 0F, 3, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartWheelsModel[0].setRotationPoint(2.5F, -3.5F, 10.5F);

		cartWheelsModel[1].addShapeBox(-1.5F, -1.5F, 0F, 3, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartWheelsModel[1].setRotationPoint(12.5F, -3.5F, 10.5F);

		cartWheelsModel[2].addShapeBox(-1.5F, -1.5F, 0F, 3, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartWheelsModel[2].setRotationPoint(2.5F, -3.5F, 4.5F);

		cartWheelsModel[3].addShapeBox(-1.5F, -1.5F, 0F, 3, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainShit70
		cartWheelsModel[3].setRotationPoint(12.5F, -3.5F, 4.5F);

		bucketModel = new ModelRendererTurbo[5];

		bucketModel[0] = new ModelRendererTurbo(this, 137, 197, textureX, textureY); // Bucket2
		bucketModel[1] = new ModelRendererTurbo(this, 125, 184, textureX, textureY); // Bucket3
		bucketModel[2] = new ModelRendererTurbo(this, 125, 184, textureX, textureY); // Bucket4
		bucketModel[3] = new ModelRendererTurbo(this, 137, 197, textureX, textureY); // Bucket5
		bucketModel[4] = new ModelRendererTurbo(this, 159, 201, textureX, textureY); // Bucket6

		bucketModel[0].setFlipped(true);
		bucketModel[0].addShapeBox(1F, 0F, -10F, 8, 11, 1, 0F, -8F, 0F, 0F, -8F, 0F, 0F, -8F, 0F, 0F, -8F, 0F, 0F, -8F, 0F, 0F, -8F, 0F, 0F, -8F, 0F, 0F, -8F, 0F, 0F); // Bucket2
		bucketModel[0].setRotationPoint(3F, -10F, -13.5F);
		bucketModel[0].rotateAngleY = 1.57079633F;

		bucketModel[1].setFlipped(true);
		bucketModel[1].addShapeBox(0F, 0F, 9F, 10, 11, 1, 0F, -10F, 0F, 0F, -10F, 0F, 0F, -10F, 0F, 0F, -10F, 0F, 0F, -10F, 0F, 0F, -10F, 0F, 0F, -10F, 0F, 0F, -10F, 0F, 0F); // Bucket3
		bucketModel[1].setRotationPoint(3F, -10F, -13.5F);

		bucketModel[2].addShapeBox(0F, 0F, 0F, 10, 11, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Bucket4
		bucketModel[2].setRotationPoint(3F, -10F, -13.5F);

		bucketModel[3].addShapeBox(1F, 0F, -1F, 8, 11, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Bucket5
		bucketModel[3].setRotationPoint(3F, -10F, -13.5F);
		bucketModel[3].rotateAngleY = 1.57079633F;

		bucketModel[4].addShapeBox(0F, 0F, 0F, 10, 10, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Bucket6
		bucketModel[4].setRotationPoint(3F, 2F, -13.5F);
		bucketModel[4].rotateAngleX = 1.57079633F;


		bucketHandleModel = new ModelRendererTurbo[3];

		bucketHandleModel[0] = new ModelRendererTurbo(this, 155, 197, textureX, textureY); // Bucket0
		bucketHandleModel[1] = new ModelRendererTurbo(this, 159, 197, textureX, textureY); // Bucket1
		bucketHandleModel[2] = new ModelRendererTurbo(this, 155, 197, textureX, textureY); // Bucket7

		bucketHandleModel[0].addShapeBox(0F, 0F, -0.5F, 1, 7, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Bucket0
		bucketHandleModel[0].setRotationPoint(2F, -9.5F, -8.5F);
		bucketHandleModel[0].rotateAngleX = -1.151917f;

		bucketHandleModel[1].addShapeBox(-0.5F, 0F, -2F, 10, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Bucket1
		bucketHandleModel[1].setRotationPoint(3.5F, -7.75F, -14F);

		bucketHandleModel[2].addShapeBox(11F, 0F, -0.5F, 1, 7, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Bucket7
		bucketHandleModel[2].setRotationPoint(1.5F, -9.5F, -8.5F);
		bucketHandleModel[2].rotateAngleX = -1.151917f;

		parts.put("base", baseModel);
		parts.put("tank_right", tankRightModel);
		parts.put("valve_right", valveRightModel);
		parts.put("pistons_right", pistonsRightModel);
		parts.put("tank_left", tankLeftModel);
		parts.put("valve_left", valveLeftModel);
		parts.put("pistons_left", pistonsLeftModel);
		parts.put("mixer", mixerModel);
		parts.put("starter_slide", starterSlideModel);
		parts.put("bucket_holder", bucketHolderModel);
		parts.put("bucket_rail", bucketRailModel);
		parts.put("bucket_timer", bucketTimerModel);
		parts.put("cart", cartModel);
		parts.put("cart_wheels", cartWheelsModel);
		parts.put("bucket", bucketModel);
		parts.put("bucket_handle", bucketHandleModel);

		translate(mixerModel,-56F,0,-41f);
		translate(tankRightModel,-14F, 56.5f,0);
		translate(valveRightModel,-14F, 56.5f,0);
		translate(valveRightModel,-22f, 3,0);

		translate(tankLeftModel,-93F, 56.5f,0);
		translate(valveLeftModel,-93F, 56.5f,0);
		translate(valveLeftModel,16f, 3,0);

		translate(bucketModel,-8f, 0f, -2.5f);
		translate(bucketRailModel,-8f, 0f, 0);
		translate(bucketTimerModel,-8f, 0f, -0);
		translate(bucketHolderModel,-8f, 0f, 0);

		translate(bucketHandleModel,-8f, 0, -2.5f);
		translate(bucketHandleModel,0, 9f, 11.5f);

		flipAll();
	}

	@Override
	public void getBlockRotation(EnumFacing facing, boolean mirrored)
	{
		switch(facing)
		{
			case NORTH:
			{
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?-4:3, 0f, 2f);

			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(0, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?-3f:4f, 0f, 3f);
			}
			break;
			case EAST:
			{
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?-4f:3, 0f, 3f);

			}
			break;
			case WEST:
			{
				GlStateManager.rotate(-90F, 0F, 1F, 0F);

				GlStateManager.translate(mirrored?-3:4, 0f, 2f);

			}
			break;
		}
	}
}
