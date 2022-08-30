package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * @author Pabilo8
 * @since 09.10.2021
 */
public class ModelChemicalPainter extends ModelIIBase
{
	public final ModelRendererTurbo[] conveyorLifterModel, lampsModel, glassModel, nozzleModel;
	int textureX = 256;
	int textureY = 128;

	public ModelChemicalPainter() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[179];
		baseModel[0] = new ModelRendererTurbo(this, 160, 88, textureX, textureY); // MAINFRAME01
		baseModel[1] = new ModelRendererTurbo(this, 28, 89, textureX, textureY); // MAINFRAME02
		baseModel[2] = new ModelRendererTurbo(this, 8, 116, textureX, textureY); // MAINFRAME03
		baseModel[3] = new ModelRendererTurbo(this, 8, 116, textureX, textureY); // MAINFRAME04
		baseModel[4] = new ModelRendererTurbo(this, 8, 116, textureX, textureY); // MAINFRAME05
		baseModel[5] = new ModelRendererTurbo(this, 8, 116, textureX, textureY); // MAINFRAME06
		baseModel[6] = new ModelRendererTurbo(this, 8, 116, textureX, textureY); // MAINFRAME07
		baseModel[7] = new ModelRendererTurbo(this, 8, 116, textureX, textureY); // MAINFRAME08
		baseModel[8] = new ModelRendererTurbo(this, 28, 89, textureX, textureY); // MAINFRAME09
		baseModel[9] = new ModelRendererTurbo(this, 8, 116, textureX, textureY); // MAINFRAME10
		baseModel[10] = new ModelRendererTurbo(this, 8, 116, textureX, textureY); // MAINFRAME11
		baseModel[11] = new ModelRendererTurbo(this, 8, 116, textureX, textureY); // MAINFRAME12
		baseModel[12] = new ModelRendererTurbo(this, 8, 116, textureX, textureY); // MAINFRAME13
		baseModel[13] = new ModelRendererTurbo(this, 28, 89, textureX, textureY); // MAINFRAME14
		baseModel[14] = new ModelRendererTurbo(this, 8, 116, textureX, textureY); // MAINFRAME15
		baseModel[15] = new ModelRendererTurbo(this, 8, 116, textureX, textureY); // MAINFRAME16
		baseModel[16] = new ModelRendererTurbo(this, 8, 116, textureX, textureY); // MAINFRAME17
		baseModel[17] = new ModelRendererTurbo(this, 8, 116, textureX, textureY); // MAINFRAME18
		baseModel[18] = new ModelRendererTurbo(this, 28, 89, textureX, textureY); // MAINFRAME19
		baseModel[19] = new ModelRendererTurbo(this, 8, 116, textureX, textureY); // MAINFRAME20
		baseModel[20] = new ModelRendererTurbo(this, 8, 116, textureX, textureY); // MAINFRAME21
		baseModel[21] = new ModelRendererTurbo(this, 82, 76, textureX, textureY); // Conveyorsandlamps12
		baseModel[22] = new ModelRendererTurbo(this, 82, 76, textureX, textureY); // Conveyorsandlamps13
		baseModel[23] = new ModelRendererTurbo(this, 72, 69, textureX, textureY); // Conveyorsandlamps14
		baseModel[24] = new ModelRendererTurbo(this, 81, 93, textureX, textureY); // Conveyorsandlamps15
		baseModel[25] = new ModelRendererTurbo(this, 54, 13, textureX, textureY); // Conveyorsandlamps16
		baseModel[26] = new ModelRendererTurbo(this, 0, 62, textureX, textureY); // MAINFRAME22
		baseModel[27] = new ModelRendererTurbo(this, 0, 62, textureX, textureY); // MAINFRAME23
		baseModel[28] = new ModelRendererTurbo(this, 128, 0, textureX, textureY); // MAINFRAME24
		baseModel[29] = new ModelRendererTurbo(this, 72, 86, textureX, textureY); // MAINFRAME25
		baseModel[30] = new ModelRendererTurbo(this, 45, 12, textureX, textureY); // MAINFRAME27
		baseModel[31] = new ModelRendererTurbo(this, 96, 4, textureX, textureY); // MAINFRAME28
		baseModel[32] = new ModelRendererTurbo(this, 96, 51, textureX, textureY); // MAINFRAME31
		baseModel[33] = new ModelRendererTurbo(this, 96, 51, textureX, textureY); // MAINFRAME32
		baseModel[34] = new ModelRendererTurbo(this, 158, 55, textureX, textureY); // MAINFRAME33
		baseModel[35] = new ModelRendererTurbo(this, 0, 95, textureX, textureY); // MAINFRAME34
		baseModel[36] = new ModelRendererTurbo(this, 0, 95, textureX, textureY); // MAINFRAME35
		baseModel[37] = new ModelRendererTurbo(this, 0, 95, textureX, textureY); // MAINFRAME36
		baseModel[38] = new ModelRendererTurbo(this, 0, 95, textureX, textureY); // MAINFRAME37
		baseModel[39] = new ModelRendererTurbo(this, 0, 95, textureX, textureY); // MAINFRAME38
		baseModel[40] = new ModelRendererTurbo(this, 72, 86, textureX, textureY); // MAINFRAME39
		baseModel[41] = new ModelRendererTurbo(this, 0, 95, textureX, textureY); // MAINFRAME40
		baseModel[42] = new ModelRendererTurbo(this, 0, 95, textureX, textureY); // MAINFRAME41
		baseModel[43] = new ModelRendererTurbo(this, 0, 95, textureX, textureY); // MAINFRAME42
		baseModel[44] = new ModelRendererTurbo(this, 0, 95, textureX, textureY); // MAINFRAME43
		baseModel[45] = new ModelRendererTurbo(this, 0, 95, textureX, textureY); // MAINFRAME44
		baseModel[46] = new ModelRendererTurbo(this, 18, 46, textureX, textureY); // Conveyorsandlamps18
		baseModel[47] = new ModelRendererTurbo(this, 40, 57, textureX, textureY); // Conveyorsandlamps19
		baseModel[48] = new ModelRendererTurbo(this, 18, 46, textureX, textureY); // Conveyorsandlamps21
		baseModel[49] = new ModelRendererTurbo(this, 18, 46, textureX, textureY); // Conveyorsandlamps23
		baseModel[50] = new ModelRendererTurbo(this, 136, 3, textureX, textureY); // MAINFRAME01
		baseModel[51] = new ModelRendererTurbo(this, 136, 3, textureX, textureY); // MAINFRAME01
		baseModel[52] = new ModelRendererTurbo(this, 136, 3, textureX, textureY); // MAINFRAME01
		baseModel[53] = new ModelRendererTurbo(this, 136, 3, textureX, textureY); // MAINFRAME01
		baseModel[54] = new ModelRendererTurbo(this, 160, 88, textureX, textureY); // MAINFRAME01
		baseModel[55] = new ModelRendererTurbo(this, 160, 88, textureX, textureY); // MAINFRAME01
		baseModel[56] = new ModelRendererTurbo(this, 160, 88, textureX, textureY); // MAINFRAME01
		baseModel[57] = new ModelRendererTurbo(this, 82, 76, textureX, textureY); // Conveyorsandlamps12
		baseModel[58] = new ModelRendererTurbo(this, 82, 76, textureX, textureY); // Conveyorsandlamps13
		baseModel[59] = new ModelRendererTurbo(this, 72, 69, textureX, textureY); // Conveyorsandlamps14
		baseModel[60] = new ModelRendererTurbo(this, 71, 93, textureX, textureY); // Conveyorsandlamps15
		baseModel[61] = new ModelRendererTurbo(this, 54, 13, textureX, textureY); // Conveyorsandlamps16
		baseModel[62] = new ModelRendererTurbo(this, 98, 69, textureX, textureY); // MAINFRAME22
		baseModel[63] = new ModelRendererTurbo(this, 98, 69, textureX, textureY); // MAINFRAME22
		baseModel[64] = new ModelRendererTurbo(this, 0, 95, textureX, textureY); // MAINFRAME25
		baseModel[65] = new ModelRendererTurbo(this, 0, 114, textureX, textureY); // MAINFRAME25
		baseModel[66] = new ModelRendererTurbo(this, 0, 95, textureX, textureY); // MAINFRAME34
		baseModel[67] = new ModelRendererTurbo(this, 0, 114, textureX, textureY); // MAINFRAME25
		baseModel[68] = new ModelRendererTurbo(this, 0, 95, textureX, textureY); // MAINFRAME34
		baseModel[69] = new ModelRendererTurbo(this, 0, 95, textureX, textureY); // MAINFRAME25
		baseModel[70] = new ModelRendererTurbo(this, 84, 90, textureX, textureY); // MAINFRAME01
		baseModel[71] = new ModelRendererTurbo(this, 84, 90, textureX, textureY); // MAINFRAME01
		baseModel[72] = new ModelRendererTurbo(this, 84, 90, textureX, textureY); // MAINFRAME01
		baseModel[73] = new ModelRendererTurbo(this, 84, 90, textureX, textureY); // MAINFRAME01
		baseModel[74] = new ModelRendererTurbo(this, 34, 40, textureX, textureY); // MAINFRAME25
		baseModel[75] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[76] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[77] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[78] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[79] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[80] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[81] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[82] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[83] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[84] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[85] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[86] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[87] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[88] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[89] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[90] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[91] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[92] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[93] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[94] = new ModelRendererTurbo(this, 8, 105, textureX, textureY); // Strips
		baseModel[95] = new ModelRendererTurbo(this, 82, 76, textureX, textureY); // Conveyorsandlamps12
		baseModel[96] = new ModelRendererTurbo(this, 82, 76, textureX, textureY); // Conveyorsandlamps13
		baseModel[97] = new ModelRendererTurbo(this, 72, 69, textureX, textureY); // Conveyorsandlamps14
		baseModel[98] = new ModelRendererTurbo(this, 82, 76, textureX, textureY); // Conveyorsandlamps12
		baseModel[99] = new ModelRendererTurbo(this, 82, 76, textureX, textureY); // Conveyorsandlamps13
		baseModel[100] = new ModelRendererTurbo(this, 72, 69, textureX, textureY); // Conveyorsandlamps14
		baseModel[101] = new ModelRendererTurbo(this, 10, 119, textureX, textureY); // MAINFRAME01
		baseModel[102] = new ModelRendererTurbo(this, 10, 119, textureX, textureY); // MAINFRAME01
		baseModel[103] = new ModelRendererTurbo(this, 10, 119, textureX, textureY); // MAINFRAME01
		baseModel[104] = new ModelRendererTurbo(this, 10, 119, textureX, textureY); // MAINFRAME01
		baseModel[105] = new ModelRendererTurbo(this, 12, 97, textureX, textureY); // MAINFRAME25
		baseModel[106] = new ModelRendererTurbo(this, 12, 97, textureX, textureY); // MAINFRAME25
		baseModel[107] = new ModelRendererTurbo(this, 12, 97, textureX, textureY); // MAINFRAME25
		baseModel[108] = new ModelRendererTurbo(this, 118, 18, textureX, textureY); // MAINFRAME01
		baseModel[109] = new ModelRendererTurbo(this, 118, 18, textureX, textureY); // MAINFRAME01
		baseModel[110] = new ModelRendererTurbo(this, 118, 18, textureX, textureY); // MAINFRAME01
		baseModel[111] = new ModelRendererTurbo(this, 94, 69, textureX, textureY); // Conveyorsandlamps19
		baseModel[112] = new ModelRendererTurbo(this, 40, 57, textureX, textureY); // Conveyorsandlamps19
		baseModel[113] = new ModelRendererTurbo(this, 94, 69, textureX, textureY); // Conveyorsandlamps19
		baseModel[114] = new ModelRendererTurbo(this, 186, 18, textureX, textureY); // MAINFRAME01
		baseModel[115] = new ModelRendererTurbo(this, 16, 104, textureX, textureY); // MAINFRAME01
		baseModel[116] = new ModelRendererTurbo(this, 156, 90, textureX, textureY); // MAINFRAME01
		baseModel[117] = new ModelRendererTurbo(this, 136, 67, textureX, textureY); // MAINFRAME01
		baseModel[118] = new ModelRendererTurbo(this, 72, 97, textureX, textureY); // MAINFRAME01
		baseModel[119] = new ModelRendererTurbo(this, 248, 88, textureX, textureY); // MAINFRAME01
		baseModel[120] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // MAINFRAME19
		baseModel[121] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // MAINFRAME19
		baseModel[122] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // MAINFRAME19
		baseModel[123] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // MAINFRAME19
		baseModel[124] = new ModelRendererTurbo(this, 40, 37, textureX, textureY); // MAINFRAME28
		baseModel[125] = new ModelRendererTurbo(this, 80, 39, textureX, textureY); // MAINFRAME28
		baseModel[126] = new ModelRendererTurbo(this, 80, 39, textureX, textureY); // MAINFRAME28
		baseModel[127] = new ModelRendererTurbo(this, 102, 25, textureX, textureY); // MAINFRAME28
		baseModel[128] = new ModelRendererTurbo(this, 102, 25, textureX, textureY); // MAINFRAME28
		baseModel[129] = new ModelRendererTurbo(this, 68, 0, textureX, textureY); // MAINFRAME28
		baseModel[130] = new ModelRendererTurbo(this, 134, 55, textureX, textureY); // MAINFRAME28
		baseModel[131] = new ModelRendererTurbo(this, 178, 31, textureX, textureY); // MAINFRAME28
		baseModel[132] = new ModelRendererTurbo(this, 157, 18, textureX, textureY); // MAINFRAME28
		baseModel[133] = new ModelRendererTurbo(this, 60, 57, textureX, textureY); // MAINFRAME28
		baseModel[134] = new ModelRendererTurbo(this, 52, 4, textureX, textureY); // MAINFRAME28
		baseModel[135] = new ModelRendererTurbo(this, 116, 41, textureX, textureY); // MAINFRAME28
		baseModel[136] = new ModelRendererTurbo(this, 44, 21, textureX, textureY); // MAINFRAME28
		baseModel[137] = new ModelRendererTurbo(this, 10, 38, textureX, textureY); // MAINFRAME28
		baseModel[138] = new ModelRendererTurbo(this, 116, 41, textureX, textureY); // MAINFRAME28
		baseModel[139] = new ModelRendererTurbo(this, 10, 38, textureX, textureY); // MAINFRAME28
		baseModel[140] = new ModelRendererTurbo(this, 116, 41, textureX, textureY); // MAINFRAME28
		baseModel[141] = new ModelRendererTurbo(this, 0, 36, textureX, textureY); // MAINFRAME28
		baseModel[142] = new ModelRendererTurbo(this, 44, 21, textureX, textureY); // MAINFRAME28
		baseModel[143] = new ModelRendererTurbo(this, 60, 57, textureX, textureY); // MAINFRAME28
		baseModel[144] = new ModelRendererTurbo(this, 116, 41, textureX, textureY); // MAINFRAME28
		baseModel[145] = new ModelRendererTurbo(this, 116, 41, textureX, textureY); // MAINFRAME28
		baseModel[146] = new ModelRendererTurbo(this, 10, 38, textureX, textureY); // MAINFRAME28
		baseModel[147] = new ModelRendererTurbo(this, 116, 41, textureX, textureY); // MAINFRAME28
		baseModel[148] = new ModelRendererTurbo(this, 164, 26, textureX, textureY); // MAINFRAME32
		baseModel[149] = new ModelRendererTurbo(this, 96, 39, textureX, textureY); // MAINFRAME32
		baseModel[150] = new ModelRendererTurbo(this, 0, 22, textureX, textureY); // MAINFRAME32
		baseModel[151] = new ModelRendererTurbo(this, 8, 9, textureX, textureY); // MAINFRAME32
		baseModel[152] = new ModelRendererTurbo(this, 14, 15, textureX, textureY); // Conveyorsandlamps16
		baseModel[153] = new ModelRendererTurbo(this, 18, 0, textureX, textureY); // Conveyorsandlamps16
		baseModel[154] = new ModelRendererTurbo(this, 74, 57, textureX, textureY); // Conveyorsandlamps16
		baseModel[155] = new ModelRendererTurbo(this, 74, 57, textureX, textureY); // Conveyorsandlamps16
		baseModel[156] = new ModelRendererTurbo(this, 0, 50, textureX, textureY); // Conveyorsandlamps16
		baseModel[157] = new ModelRendererTurbo(this, 0, 50, textureX, textureY); // Conveyorsandlamps16
		baseModel[158] = new ModelRendererTurbo(this, 44, 61, textureX, textureY); // Conveyorsandlamps16
		baseModel[159] = new ModelRendererTurbo(this, 114, 0, textureX, textureY); // Conveyorsandlamps16
		baseModel[160] = new ModelRendererTurbo(this, 44, 61, textureX, textureY); // Conveyorsandlamps16
		baseModel[161] = new ModelRendererTurbo(this, 80, 39, textureX, textureY); // MAINFRAME28
		baseModel[162] = new ModelRendererTurbo(this, 80, 39, textureX, textureY); // MAINFRAME28
		baseModel[163] = new ModelRendererTurbo(this, 102, 25, textureX, textureY); // MAINFRAME28
		baseModel[164] = new ModelRendererTurbo(this, 102, 25, textureX, textureY); // MAINFRAME28
		baseModel[165] = new ModelRendererTurbo(this, 134, 55, textureX, textureY); // MAINFRAME28
		baseModel[166] = new ModelRendererTurbo(this, 96, 4, textureX, textureY); // MAINFRAME28
		baseModel[167] = new ModelRendererTurbo(this, 80, 39, textureX, textureY); // MAINFRAME28
		baseModel[168] = new ModelRendererTurbo(this, 80, 39, textureX, textureY); // MAINFRAME28
		baseModel[169] = new ModelRendererTurbo(this, 0, 62, textureX, textureY); // MAINFRAME22
		baseModel[170] = new ModelRendererTurbo(this, 0, 62, textureX, textureY); // MAINFRAME23
		baseModel[171] = new ModelRendererTurbo(this, 158, 55, textureX, textureY); // MAINFRAME33
		baseModel[172] = new ModelRendererTurbo(this, 158, 55, textureX, textureY); // MAINFRAME33
		baseModel[173] = new ModelRendererTurbo(this, 164, 48, textureX, textureY); // NOZZLE01
		baseModel[174] = new ModelRendererTurbo(this, 82, 69, textureX, textureY); // NOZZLE02
		baseModel[175] = new ModelRendererTurbo(this, 71, 93, textureX, textureY); // stakan
		baseModel[176] = new ModelRendererTurbo(this, 71, 93, textureX, textureY); // stakan
		baseModel[177] = new ModelRendererTurbo(this, 71, 93, textureX, textureY); // stakan
		baseModel[178] = new ModelRendererTurbo(this, 71, 93, textureX, textureY); // stakan

		baseModel[0].addShapeBox(0F, 0F, 0F, 40, 32, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME01
		baseModel[0].setRotationPoint(-1F, 0F, 1F);
		baseModel[0].rotateAngleX = 1.57079633F;

		baseModel[1].addShapeBox(0F, 0F, 0F, 14, 25, 14, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME02
		baseModel[1].setRotationPoint(0F, -42F, 50F);

		baseModel[2].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME03
		baseModel[2].setRotationPoint(1F, -17F, 51F);

		baseModel[3].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME04
		baseModel[3].setRotationPoint(11F, -17F, 51F);

		baseModel[4].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME05
		baseModel[4].setRotationPoint(11F, -17F, 61F);

		baseModel[5].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME06
		baseModel[5].setRotationPoint(1F, -17F, 61F);

		baseModel[6].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME07
		baseModel[6].setRotationPoint(17F, -17F, 61F);

		baseModel[7].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME08
		baseModel[7].setRotationPoint(17F, -17F, 51F);

		baseModel[8].addShapeBox(0F, 0F, 0F, 14, 25, 14, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME09
		baseModel[8].setRotationPoint(16F, -42F, 50F);

		baseModel[9].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME10
		baseModel[9].setRotationPoint(27F, -17F, 61F);

		baseModel[10].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME11
		baseModel[10].setRotationPoint(27F, -17F, 51F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME12
		baseModel[11].setRotationPoint(65F, -17F, 61F);

		baseModel[12].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME13
		baseModel[12].setRotationPoint(65F, -17F, 51F);

		baseModel[13].addShapeBox(0F, 0F, 0F, 14, 25, 14, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME14
		baseModel[13].setRotationPoint(64F, -42F, 50F);

		baseModel[14].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME15
		baseModel[14].setRotationPoint(75F, -17F, 61F);

		baseModel[15].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME16
		baseModel[15].setRotationPoint(75F, -17F, 51F);

		baseModel[16].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME17
		baseModel[16].setRotationPoint(59F, -17F, 61F);

		baseModel[17].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME18
		baseModel[17].setRotationPoint(59F, -17F, 51F);

		baseModel[18].addShapeBox(0F, 0F, 0F, 14, 25, 14, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME19
		baseModel[18].setRotationPoint(48F, -42F, 50F);

		baseModel[19].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME20
		baseModel[19].setRotationPoint(49F, -17F, 51F);

		baseModel[20].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME21
		baseModel[20].setRotationPoint(49F, -17F, 61F);

		baseModel[21].addShapeBox(0F, 0F, 0F, 4, 9, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps12
		baseModel[21].setRotationPoint(71F, -27F, 17F);

		baseModel[22].addShapeBox(0F, 0F, 0F, 4, 9, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps13
		baseModel[22].setRotationPoint(71F, -27F, 32F);

		baseModel[23].addShapeBox(0F, 0F, 0F, 4, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps14
		baseModel[23].setRotationPoint(71F, -27F, 17F);
		baseModel[23].rotateAngleX = 1.57079633F;

		baseModel[24].addShapeBox(0F, 0F, 0F, 4, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps15
		baseModel[24].setRotationPoint(71F, -31F, 27F);
		baseModel[24].rotateAngleY = -1.57079633F;

		baseModel[25].addShapeBox(0F, 0F, 0F, 16, 16, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps16
		baseModel[25].setRotationPoint(63F, -31F, 33F);
		baseModel[25].rotateAngleY = -1.57079633F;

		baseModel[26].addShapeBox(0F, 0F, 0F, 16, 32, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME22
		baseModel[26].setRotationPoint(-1F, -15F, 1F);
		baseModel[26].rotateAngleX = 1.57079633F;

		baseModel[27].setFlipped(true);
		baseModel[27].addShapeBox(0F, 0F, 0F, 16, 32, 1, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F); // MAINFRAME23
		baseModel[27].setRotationPoint(63F, -15F, 1F);
		baseModel[27].rotateAngleX = 1.57079633F;

		baseModel[28].addShapeBox(0F, 0F, 0F, 48, 1, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME24
		baseModel[28].setRotationPoint(15F, -16F, 49F);

		baseModel[29].addShapeBox(0F, 0F, 0F, 44, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME25
		baseModel[29].setRotationPoint(17F, -17F, 3F);

		baseModel[30].addShapeBox(0F, 0F, 0F, 8, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME27
		baseModel[30].setRotationPoint(35F, -48F, 29F);
		baseModel[30].rotateAngleX = -1.57079633F;

		baseModel[31].addShapeBox(0F, 0F, 0F, 16, 5, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[31].setRotationPoint(31F, -43F, 41F);

		baseModel[32].addShapeBox(0F, 0F, 0F, 16, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME31
		baseModel[32].setRotationPoint(15F, -45.5F, 49F);
		baseModel[32].rotateAngleY = -1.57079633F;

		baseModel[33].addShapeBox(0F, 0F, 0F, 16, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME32
		baseModel[33].setRotationPoint(61F, -45.5F, 49.02F);
		baseModel[33].rotateAngleY = -1.57079633F;

		baseModel[34].addShapeBox(0F, 0F, 0F, 48, 32, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME33
		baseModel[34].setRotationPoint(15F, -47.5F, 49F);

		baseModel[35].addShapeBox(0F, 0F, 0F, 2, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME34
		baseModel[35].setRotationPoint(15F, -29.5F, 33F);

		baseModel[36].addShapeBox(0F, 0F, 0F, 2, 31, 2, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME35
		baseModel[36].setRotationPoint(15F, -44.8F, 15F);

		baseModel[37].addShapeBox(0F, 0F, 0F, 2, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME36
		baseModel[37].setRotationPoint(15F, -29.5F, 3F);

		baseModel[38].addShapeBox(0F, 0F, 0F, 2, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME37
		baseModel[38].setRotationPoint(61F, -29.5F, 3F);

		baseModel[39].addShapeBox(0F, 0F, 0F, 2, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME38
		baseModel[39].setRotationPoint(61F, -29.5F, 33F);

		baseModel[40].addShapeBox(0F, 0F, 0F, 44, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME39
		baseModel[40].setRotationPoint(17F, -29.5F, 3F);

		baseModel[41].addShapeBox(0F, 0F, 0F, 2, 23, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME40
		baseModel[41].setRotationPoint(14.98F, -47.5F, 17F);
		baseModel[41].rotateAngleX = -0.66322512F;

		baseModel[42].addShapeBox(0F, 0F, 0F, 2, 23, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME41
		baseModel[42].setRotationPoint(61.01F, -47.5F, 17F);
		baseModel[42].rotateAngleX = -0.66322512F;

		baseModel[43].addShapeBox(0F, 0F, 0F, 2, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME42
		baseModel[43].setRotationPoint(15F, -28.5F, 17F);
		baseModel[43].rotateAngleX = 1.57079633F;

		baseModel[44].addShapeBox(0F, 0F, 0F, 2, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME43
		baseModel[44].setRotationPoint(61F, -28.5F, 17F);
		baseModel[44].rotateAngleX = 1.57079633F;

		baseModel[45].addShapeBox(0F, 0F, 0F, 2, 31, 2, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME44
		baseModel[45].setRotationPoint(61F, -44.8F, 15F);

		baseModel[46].addShapeBox(0F, 0F, 0F, 6, 14, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F); // Conveyorsandlamps18
		baseModel[46].setRotationPoint(20F, -43F, 19F);
		baseModel[46].rotateAngleX = 1.57079633F;

		baseModel[47].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps19
		baseModel[47].setRotationPoint(22F, -45F, 21F);
		baseModel[47].rotateAngleZ = 1.57079633F;

		baseModel[48].addShapeBox(0F, 0F, 0F, 6, 14, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F); // Conveyorsandlamps21
		baseModel[48].setRotationPoint(35.5F, -43F, 19F);
		baseModel[48].rotateAngleX = 1.57079633F;

		baseModel[49].addShapeBox(0F, 0F, 0F, 6, 14, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F); // Conveyorsandlamps23
		baseModel[49].setRotationPoint(51F, -43F, 19F);
		baseModel[49].rotateAngleX = 1.57079633F;

		baseModel[50].addShapeBox(0F, 0F, 0F, 2, 7, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME01
		baseModel[50].setRotationPoint(-1F, -15F, 1F);

		baseModel[51].addShapeBox(0F, 0F, 0F, 2, 7, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME01
		baseModel[51].setRotationPoint(-1F, -15F, 63F);

		baseModel[52].addShapeBox(0F, 0F, 0F, 2, 7, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME01
		baseModel[52].setRotationPoint(77F, -15F, 1F);

		baseModel[53].addShapeBox(0F, 0F, 0F, 2, 7, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME01
		baseModel[53].setRotationPoint(77F, -15F, 63F);

		baseModel[54].setFlipped(true);
		baseModel[54].addShapeBox(0F, 0F, 0F, 40, 32, 8, 0F, -40F, 0F, 0F, -40F, 0F, 0F, -40F, 0F, 0F, -40F, 0F, 0F, -40F, 0F, 0F, -40F, 0F, 0F, -40F, 0F, 0F, -40F, 0F, 0F); // MAINFRAME01
		baseModel[54].setRotationPoint(39F, 0F, 1F);
		baseModel[54].rotateAngleX = 1.57079633F;

		baseModel[55].setFlipped(true);
		baseModel[55].addShapeBox(0F, 0F, 0F, 40, 32, 8, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F); // MAINFRAME01
		baseModel[55].setRotationPoint(-1F, 0F, 33F);
		baseModel[55].rotateAngleX = 1.57079633F;

		baseModel[56].addShapeBox(0F, 0F, 0F, 40, 32, 8, 0F, -40F, -32F, 0F, -40F, -32F, 0F, -40F, -32F, 0F, -40F, -32F, 0F, -40F, -32F, 0F, -40F, -32F, 0F, -40F, -32F, 0F, -40F, -32F, 0F); // MAINFRAME01
		baseModel[56].setRotationPoint(39F, 0F, 33F);
		baseModel[56].rotateAngleX = 1.57079633F;

		baseModel[57].addShapeBox(0F, 0F, 0F, 4, 9, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps12
		baseModel[57].setRotationPoint(3F, -27F, 17F);

		baseModel[58].addShapeBox(0F, 0F, 0F, 4, 9, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps13
		baseModel[58].setRotationPoint(3F, -27F, 32F);

		baseModel[59].addShapeBox(0F, 0F, 0F, 4, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps14
		baseModel[59].setRotationPoint(3F, -27F, 17F);
		baseModel[59].rotateAngleX = 1.57079633F;

		baseModel[60].addShapeBox(0F, 0F, 0F, 4, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps15
		baseModel[60].setRotationPoint(6F, -31F, 27F);
		baseModel[60].rotateAngleY = -1.57079633F;

		baseModel[61].addShapeBox(0F, 0F, 0F, 16, 16, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps16
		baseModel[61].setRotationPoint(7F, -31F, 33F);
		baseModel[61].rotateAngleY = -1.57079633F;

		baseModel[62].addShapeBox(0F, 0F, 0F, 16, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME22
		baseModel[62].setRotationPoint(-1.05F, -16.05F, 49F);
		baseModel[62].rotateAngleY = -1.57079633F;

		baseModel[63].addShapeBox(0F, 0F, 0F, 16, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME22
		baseModel[63].setRotationPoint(78.05F, -16.05F, 49F);
		baseModel[63].rotateAngleY = -1.57079633F;

		baseModel[64].addShapeBox(0F, 0F, 0F, 2, 10, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME25
		baseModel[64].setRotationPoint(15F, -15F, 5F);
		baseModel[64].rotateAngleX = 1.57079633F;

		baseModel[65].addShapeBox(0F, 0F, 0F, 2, 12, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME25
		baseModel[65].setRotationPoint(15F, -15F, 35F);
		baseModel[65].rotateAngleX = 1.57079633F;

		baseModel[66].addShapeBox(0F, 0F, 0F, 2, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME34
		baseModel[66].setRotationPoint(15F, -29.5F, 47F);

		baseModel[67].addShapeBox(0F, 0F, 0F, 2, 12, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME25
		baseModel[67].setRotationPoint(61F, -15F, 35F);
		baseModel[67].rotateAngleX = 1.57079633F;

		baseModel[68].addShapeBox(0F, 0F, 0F, 2, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME34
		baseModel[68].setRotationPoint(61F, -29.5F, 47F);

		baseModel[69].addShapeBox(0F, 0F, 0F, 2, 10, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME25
		baseModel[69].setRotationPoint(61F, -15F, 5F);
		baseModel[69].rotateAngleX = 1.57079633F;

		baseModel[70].addShapeBox(0F, 0F, 0F, 31, 31, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME01
		baseModel[70].setRotationPoint(0F, -8F, 2F);
		baseModel[70].rotateAngleX = 1.57079633F;

		baseModel[71].setFlipped(true);
		baseModel[71].addShapeBox(0F, 0F, 0F, 31, 31, 7, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F); // MAINFRAME01
		baseModel[71].setRotationPoint(47F, -8F, 2F);
		baseModel[71].rotateAngleX = 1.57079633F;

		baseModel[72].setFlipped(true);
		baseModel[72].addShapeBox(0F, 0F, 0F, 31, 31, 7, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F); // MAINFRAME01
		baseModel[72].setRotationPoint(0F, -8F, 33F);
		baseModel[72].rotateAngleX = 1.57079633F;

		baseModel[73].addShapeBox(0F, 0F, 0F, 31, 31, 7, 0F, -31F, -31F, 0F, -31F, -31F, 0F, -31F, -31F, 0F, -31F, -31F, 0F, -31F, -31F, 0F, -31F, -31F, 0F, -31F, -31F, 0F, -31F, -31F, 0F); // MAINFRAME01
		baseModel[73].setRotationPoint(47F, -8F, 33F);
		baseModel[73].rotateAngleX = 1.57079633F;

		baseModel[74].addShapeBox(0F, 0F, 0F, 1, 48, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME25
		baseModel[74].setRotationPoint(15F, -15F, 1F);
		baseModel[74].rotateAngleZ = 1.57079633F;

		baseModel[75].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[75].setRotationPoint(5F, -27F, 17.5F);

		baseModel[76].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[76].setRotationPoint(5F, -27F, 20.5F);

		baseModel[77].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[77].setRotationPoint(5F, -27F, 23.5F);

		baseModel[78].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[78].setRotationPoint(5F, -27F, 26.5F);

		baseModel[79].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[79].setRotationPoint(5F, -27F, 29.5F);

		baseModel[80].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[80].setRotationPoint(72F, -27F, 17.5F);

		baseModel[81].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[81].setRotationPoint(72F, -27F, 20.5F);

		baseModel[82].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[82].setRotationPoint(72F, -27F, 23.5F);

		baseModel[83].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[83].setRotationPoint(72F, -27F, 26.5F);

		baseModel[84].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[84].setRotationPoint(72F, -27F, 29.5F);

		baseModel[85].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[85].setRotationPoint(16F, -27F, 17.5F);

		baseModel[86].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[86].setRotationPoint(16F, -27F, 20.5F);

		baseModel[87].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[87].setRotationPoint(16F, -27F, 23.5F);

		baseModel[88].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[88].setRotationPoint(16F, -27F, 26.5F);

		baseModel[89].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[89].setRotationPoint(16F, -27F, 29.5F);

		baseModel[90].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[90].setRotationPoint(61F, -27F, 17.5F);

		baseModel[91].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[91].setRotationPoint(61F, -27F, 20.5F);

		baseModel[92].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[92].setRotationPoint(61F, -27F, 23.5F);

		baseModel[93].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[93].setRotationPoint(61F, -27F, 26.5F);

		baseModel[94].addShapeBox(0F, 0F, 0F, 1, 8, 3, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, -0.125F); // Strips
		baseModel[94].setRotationPoint(61F, -27F, 29.5F);

		baseModel[95].addShapeBox(0F, 0F, 0F, 4, 9, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps12
		baseModel[95].setRotationPoint(16F, -27F, 17F);

		baseModel[96].addShapeBox(0F, 0F, 0F, 4, 9, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps13
		baseModel[96].setRotationPoint(16F, -27F, 32F);

		baseModel[97].addShapeBox(0F, 0F, 0F, 4, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps14
		baseModel[97].setRotationPoint(16F, -27F, 17F);
		baseModel[97].rotateAngleX = 1.57079633F;

		baseModel[98].addShapeBox(0F, 0F, 0F, 4, 9, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps12
		baseModel[98].setRotationPoint(58F, -27F, 17F);

		baseModel[99].addShapeBox(0F, 0F, 0F, 4, 9, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps13
		baseModel[99].setRotationPoint(58F, -27F, 32F);

		baseModel[100].addShapeBox(0F, 0F, 0F, 4, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps14
		baseModel[100].setRotationPoint(58F, -27F, 17F);
		baseModel[100].rotateAngleX = 1.57079633F;

		baseModel[101].addShapeBox(0F, 0F, 0F, 8, 8, 1, 0F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F); // MAINFRAME01
		baseModel[101].setRotationPoint(3F, -12F, 64F);

		baseModel[102].addShapeBox(0F, 0F, 0F, 8, 8, 1, 0F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F); // MAINFRAME01
		baseModel[102].setRotationPoint(19F, -12F, 64F);

		baseModel[103].addShapeBox(0F, 0F, 0F, 8, 8, 1, 0F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F); // MAINFRAME01
		baseModel[103].setRotationPoint(51F, -12F, 64F);

		baseModel[104].addShapeBox(0F, 0F, 0F, 8, 8, 1, 0F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F); // MAINFRAME01
		baseModel[104].setRotationPoint(67F, -12F, 64F);

		baseModel[105].addShapeBox(0F, 0F, 0F, 12, 6, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME25
		baseModel[105].setRotationPoint(18F, -15.05F, 6F);
		baseModel[105].rotateAngleX = 1.57079633F;

		baseModel[106].addShapeBox(0F, 0F, 0F, 12, 6, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME25
		baseModel[106].setRotationPoint(48F, -15.05F, 6F);
		baseModel[106].rotateAngleX = 1.57079633F;

		baseModel[107].addShapeBox(0F, 0F, 0F, 12, 6, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME25
		baseModel[107].setRotationPoint(33F, -15.05F, 6F);
		baseModel[107].rotateAngleX = 1.57079633F;

		baseModel[108].addShapeBox(0F, 0F, 0F, 16, 15, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME01
		baseModel[108].setRotationPoint(31F, -8F, 2F);
		baseModel[108].rotateAngleX = 1.57079633F;

		baseModel[109].setFlipped(true);
		baseModel[109].addShapeBox(0F, 0F, 0F, 16, 15, 7, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F); // MAINFRAME01
		baseModel[109].setRotationPoint(31F, -8F, 33F);
		baseModel[109].rotateAngleX = 1.57079633F;

		baseModel[110].addShapeBox(0F, 0F, 0F, 16, 15, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME01
		baseModel[110].setRotationPoint(31F, -8F, 49F);
		baseModel[110].rotateAngleX = 1.57079633F;

		baseModel[111].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps19
		baseModel[111].setRotationPoint(38F, -45F, 21F);
		baseModel[111].rotateAngleZ = 1.57079633F;

		baseModel[112].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps19
		baseModel[112].setRotationPoint(22F, -45F, 30F);
		baseModel[112].rotateAngleZ = 1.57079633F;

		baseModel[113].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps19
		baseModel[113].setRotationPoint(38F, -45F, 30F);
		baseModel[113].rotateAngleZ = 1.57079633F;

		baseModel[114].addBox(0F, 0F, 0F, 6, 4, 6, 0F); // MAINFRAME01
		baseModel[114].setRotationPoint(36F, -12F, 22F);

		baseModel[115].addBox(0F, 0F, 0F, 3, 12, 3, 0F); // MAINFRAME01
		baseModel[115].setRotationPoint(43F, -8F, 21F);
		baseModel[115].rotateAngleX = 1.57079633F;

		baseModel[116].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME01
		baseModel[116].setRotationPoint(43F, -11F, 18F);

		baseModel[117].addBox(0F, 0F, 0F, 8, 3, 3, 0F); // MAINFRAME01
		baseModel[117].setRotationPoint(35F, -11F, 18F);

		baseModel[118].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F); // MAINFRAME01
		baseModel[118].setRotationPoint(32F, -11F, 18F);

		baseModel[119].addBox(0F, 0F, 0F, 3, 3, 1, 0F); // MAINFRAME01
		baseModel[119].setRotationPoint(32F, -11F, 17F);

		baseModel[120].addShapeBox(0F, 0F, 0F, 8, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME19
		baseModel[120].setRotationPoint(51F, -16F, 52F);
		baseModel[120].rotateAngleX = 1.57079633F;

		baseModel[121].addShapeBox(0F, 0F, 0F, 8, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME19
		baseModel[121].setRotationPoint(67F, -16F, 52F);
		baseModel[121].rotateAngleX = 1.57079633F;

		baseModel[122].addShapeBox(0F, 0F, 0F, 8, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME19
		baseModel[122].setRotationPoint(3F, -16F, 52F);
		baseModel[122].rotateAngleX = 1.57079633F;

		baseModel[123].addShapeBox(0F, 0F, 0F, 8, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME19
		baseModel[123].setRotationPoint(19F, -16F, 52F);
		baseModel[123].rotateAngleX = 1.57079633F;

		baseModel[124].addShapeBox(0F, 0F, 0F, 16, 16, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[124].setRotationPoint(31F, -31F, 45F);

		baseModel[125].addShapeBox(0F, 0F, 0F, 6, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[125].setRotationPoint(31F, -29F, 45F);
		baseModel[125].rotateAngleX = 1.57079633F;
		baseModel[125].rotateAngleY = -1.57079633F;

		baseModel[126].setFlipped(true);
		baseModel[126].addShapeBox(0F, 0F, 0F, 6, 16, 2, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F); // MAINFRAME28
		baseModel[126].setRotationPoint(31F, -15F, 45F);
		baseModel[126].rotateAngleX = 1.57079633F;
		baseModel[126].rotateAngleY = -1.57079633F;

		baseModel[127].addShapeBox(0F, 0F, 0F, 6, 12, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[127].setRotationPoint(31F, -29F, 45F);
		baseModel[127].rotateAngleY = -1.57079633F;

		baseModel[128].setFlipped(true);
		baseModel[128].addShapeBox(0F, 0F, 0F, 6, 12, 2, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F); // MAINFRAME28
		baseModel[128].setRotationPoint(45F, -29F, 45F);
		baseModel[128].rotateAngleY = -1.57079633F;

		baseModel[129].addShapeBox(0F, 0F, 0F, 16, 10, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[129].setRotationPoint(31F, -31F, 39F);
		baseModel[129].rotateAngleX = 1.57079633F;

		baseModel[130].addShapeBox(0F, 0F, 0F, 12, 12, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[130].setRotationPoint(33F, -29F, 40F);

		baseModel[131].addShapeBox(0F, 0F, 0F, 8, 16, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[131].setRotationPoint(47F, -31F, 41F);

		baseModel[132].addShapeBox(0F, 0F, 0F, 6, 1, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[132].setRotationPoint(48F, -32F, 42F);

		baseModel[133].addShapeBox(0F, 0F, 0F, 6, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[133].setRotationPoint(47F, -43F, 48F);
		baseModel[133].rotateAngleY = -1.57079633F;

		baseModel[134].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[134].setRotationPoint(49F, -42F, 43F);

		baseModel[135].addShapeBox(0F, 0F, 0F, 4, 6, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[135].setRotationPoint(49F, -38F, 43F);

		baseModel[136].addShapeBox(0F, 0F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[136].setRotationPoint(48F, -42F, 47F);
		baseModel[136].rotateAngleY = -1.57079633F;

		baseModel[137].addShapeBox(0F, 0F, 0F, 6, 2, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[137].setRotationPoint(48F, -36F, 42F);

		baseModel[138].addShapeBox(0F, 0F, 0F, 4, 6, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[138].setRotationPoint(25F, -34F, 43F);

		baseModel[139].addShapeBox(0F, 0F, 0F, 6, 2, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[139].setRotationPoint(24F, -35F, 42F);

		baseModel[140].addShapeBox(0F, 0F, 0F, 4, 6, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[140].setRotationPoint(25F, -40F, 43F);

		baseModel[141].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[141].setRotationPoint(25F, -44F, 43F);

		baseModel[142].setFlipped(true);
		baseModel[142].addShapeBox(0F, 0F, 0F, 4, 4, 1, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F); // MAINFRAME28
		baseModel[142].setRotationPoint(29F, -44F, 47F);
		baseModel[142].rotateAngleY = -1.57079633F;

		baseModel[143].addShapeBox(0F, 0F, 0F, 6, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[143].setRotationPoint(30F, -45F, 48F);
		baseModel[143].rotateAngleY = -1.57079633F;

		baseModel[144].addShapeBox(0F, 0F, 0F, 4, 6, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[144].setRotationPoint(25F, -28F, 43F);

		baseModel[145].addShapeBox(0F, 0F, 0F, 4, 6, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[145].setRotationPoint(25F, -22F, 43F);

		baseModel[146].addShapeBox(0F, 0F, 0F, 6, 2, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[146].setRotationPoint(24F, -23F, 42F);

		baseModel[147].addShapeBox(0F, 0F, 0F, 4, 6, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[147].setRotationPoint(25F, -16F, 43F);

		baseModel[148].addShapeBox(0F, 0F, 0F, 10, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME32
		baseModel[148].setRotationPoint(63.5F, -46.5F, 35F);
		baseModel[148].rotateAngleX = -0.05235988F;
		baseModel[148].rotateAngleY = 1.57079633F;

		baseModel[149].addShapeBox(0F, 0F, 0F, 9, 11, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME32
		baseModel[149].setRotationPoint(66F, -36.5F, 49.5F);
		baseModel[149].rotateAngleX = -0.03490659F;
		baseModel[149].rotateAngleZ = 0.06981317F;

		baseModel[150].addShapeBox(0F, 0F, 0F, 1, 8, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME32
		baseModel[150].setRotationPoint(62.5F, -32.5F, 34F);
		baseModel[150].rotateAngleX = 0.03490659F;

		baseModel[151].addShapeBox(0F, 0F, 0F, 1, 10, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME32
		baseModel[151].setRotationPoint(62.5F, -32.5F, 42F);
		baseModel[151].rotateAngleX = -0.08726646F;

		baseModel[152].addShapeBox(0F, 0F, 0F, 8, 10, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps16
		baseModel[152].setRotationPoint(65F, -26F, 5F);
		baseModel[152].rotateAngleY = -0.31415927F;

		baseModel[153].addShapeBox(0F, 0F, 0F, 10, 10, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps16
		baseModel[153].setRotationPoint(2F, -16F, 5F);
		baseModel[153].rotateAngleX = 1.57079633F;
		baseModel[153].rotateAngleY = -0.31415927F;

		baseModel[154].addShapeBox(0F, 0F, 0F, 10, 11, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps16
		baseModel[154].setRotationPoint(2F, -28F, 5F);
		baseModel[154].rotateAngleY = -0.31415927F;

		baseModel[155].addShapeBox(0F, 0F, 9F, 10, 11, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps16
		baseModel[155].setRotationPoint(2F, -28F, 5F);
		baseModel[155].rotateAngleY = -0.31415927F;

		baseModel[156].addShapeBox(1F, 0F, -1F, 8, 11, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps16
		baseModel[156].setRotationPoint(2F, -28F, 5F);
		baseModel[156].rotateAngleY = 1.25663706F;

		baseModel[157].addShapeBox(1F, 0F, -10F, 8, 11, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps16
		baseModel[157].setRotationPoint(2F, -28F, 5F);
		baseModel[157].rotateAngleY = 1.25663706F;

		baseModel[158].addShapeBox(0F, 0F, -0.5F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps16
		baseModel[158].setRotationPoint(2.5F, -27.5F, 10F);
		baseModel[158].rotateAngleX = -0.64577182F;
		baseModel[158].rotateAngleY = -0.31415927F;

		baseModel[159].addShapeBox(-0.5F, 0F, -2F, 10, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps16
		baseModel[159].setRotationPoint(2F, -19.25F, 4F);
		baseModel[159].rotateAngleY = -0.31415927F;

		baseModel[160].addShapeBox(11F, 0F, -0.5F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Conveyorsandlamps16
		baseModel[160].setRotationPoint(2.5F, -27.5F, 10F);
		baseModel[160].rotateAngleX = -0.64577182F;
		baseModel[160].rotateAngleY = -0.31415927F;

		baseModel[161].addShapeBox(0F, 0F, 0F, 6, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[161].setRotationPoint(31F, -38F, 56F);
		baseModel[161].rotateAngleX = 1.57079633F;
		baseModel[161].rotateAngleY = -1.57079633F;

		baseModel[162].setFlipped(true);
		baseModel[162].addShapeBox(0F, 0F, 0F, 6, 16, 2, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F); // MAINFRAME28
		baseModel[162].setRotationPoint(31F, -24F, 56F);
		baseModel[162].rotateAngleX = 1.57079633F;
		baseModel[162].rotateAngleY = -1.57079633F;

		baseModel[163].addShapeBox(0F, 0F, 0F, 6, 12, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[163].setRotationPoint(31F, -38F, 56F);
		baseModel[163].rotateAngleY = -1.57079633F;

		baseModel[164].setFlipped(true);
		baseModel[164].addShapeBox(0F, 0F, 0F, 6, 12, 2, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F); // MAINFRAME28
		baseModel[164].setRotationPoint(45F, -38F, 56F);
		baseModel[164].rotateAngleY = -1.57079633F;

		baseModel[165].addShapeBox(0F, 0F, 0F, 12, 12, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[165].setRotationPoint(33F, -38F, 55F);

		baseModel[166].setFlipped(false);
		baseModel[166].addShapeBox(0F, 0F, 0F, 16, 5, 8, 0F, -16F, -5F, 0F, -16F, -5F, 0F, -16F, -5F, 0F, -16F, -5F, 0F, -16F, -5F, 0F, -16F, -5F, 0F, -16F, -5F, 0F, -16F, -5F, 0F); // MAINFRAME28
		baseModel[166].setRotationPoint(31F, -38F, 41F);

		baseModel[167].addShapeBox(0F, 0F, 0F, 6, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[167].setRotationPoint(31F, -43F, 45F);
		baseModel[167].rotateAngleX = 1.57079633F;
		baseModel[167].rotateAngleY = -1.57079633F;

		baseModel[168].addShapeBox(0F, 0F, 0F, 6, 16, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME28
		baseModel[168].setRotationPoint(31F, -45F, 45F);
		baseModel[168].rotateAngleX = 1.57079633F;
		baseModel[168].rotateAngleY = -1.57079633F;

		baseModel[169].setFlipped(true);
		baseModel[169].addShapeBox(0F, 0F, 0F, 16, 32, 1, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F); // MAINFRAME22
		baseModel[169].setRotationPoint(-1F, -15F, 33F);
		baseModel[169].rotateAngleX = 1.57079633F;

		baseModel[170].addShapeBox(0F, 0F, 0F, 16, 32, 1, 0F, -16F, -32F, 0F, -16F, -32F, 0F, -16F, -32F, 0F, -16F, -32F, 0F, -16F, -32F, 0F, -16F, -32F, 0F, -16F, -32F, 0F, -16F, -32F, 0F); // MAINFRAME23
		baseModel[170].setRotationPoint(63F, -15F, 33F);
		baseModel[170].rotateAngleX = 1.57079633F;

		baseModel[171].addShapeBox(0F, 0F, 0F, 48, 32, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME33
		baseModel[171].setRotationPoint(15F, -46.5F, 17F);
		baseModel[171].rotateAngleX = 1.57079633F;

		baseModel[172].addShapeBox(0F, 0F, 0F, 48, 32, 1, 0F, -48F, -32F, 0F, -48F, -32F, 0F, -48F, -32F, 0F, -48F, -32F, 0F, -48F, -32F, 0F, -48F, -32F, 0F, -48F, -32F, 0F, -48F, -32F, 0F); // MAINFRAME33
		baseModel[172].setRotationPoint(15F, -45.5F, 17F);
		baseModel[172].rotateAngleX = 1.57079633F;

		baseModel[173].addShapeBox(0F, 0F, 0F, 6, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // NOZZLE01
		baseModel[173].setRotationPoint(36F, -42F, 40F);

		baseModel[174].addShapeBox(0F, 0F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // NOZZLE02
		baseModel[174].setRotationPoint(37F, -41F, 39F);

		baseModel[175].addShapeBox(0F, 0F, 0F, 4, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // stakan
		baseModel[175].setRotationPoint(69F, -15F, 64F);

		baseModel[176].addShapeBox(0F, 0F, 0F, 4, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // stakan
		baseModel[176].setRotationPoint(53F, -15F, 64F);

		baseModel[177].addShapeBox(0F, 0F, 0F, 4, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // stakan
		baseModel[177].setRotationPoint(21F, -15F, 64F);

		baseModel[178].addShapeBox(0F, 0F, 0F, 4, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // stakan
		baseModel[178].setRotationPoint(5F, -15F, 64F);


		nozzleModel = new ModelRendererTurbo[3];
		nozzleModel[0] = new ModelRendererTurbo(this, 164, 39, textureX, textureY); // NOZZLE02
		nozzleModel[1] = new ModelRendererTurbo(this, 109, 17, textureX, textureY); // NOZZLE02
		nozzleModel[2] = new ModelRendererTurbo(this, 48, 57, textureX, textureY); // NOZZLE02

		nozzleModel[0].addShapeBox(-1F, -1F, -5F, 2, 2, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // NOZZLE02
		nozzleModel[0].setRotationPoint(39F, -39F, 39F);

		nozzleModel[1].addShapeBox(-2F, -2F, -9F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // NOZZLE02
		nozzleModel[1].setRotationPoint(39F, -39F, 39F);

		nozzleModel[2].addShapeBox(-1.5F, -1.5F, -8F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // NOZZLE02
		nozzleModel[2].setRotationPoint(39F, -39F, 39F);


		glassModel = new ModelRendererTurbo[13];
		glassModel[0] = new ModelRendererTurbo(this, 210, 19, textureX, textureY); // GLASS01
		glassModel[1] = new ModelRendererTurbo(this, 210, 33, textureX, textureY); // GLASS02
		glassModel[2] = new ModelRendererTurbo(this, 132, 24, textureX, textureY); // GLASS03
		glassModel[3] = new ModelRendererTurbo(this, 134, 27, textureX, textureY); // GLASS04
		glassModel[4] = new ModelRendererTurbo(this, 132, 24, textureX, textureY); // GLASS09
		glassModel[5] = new ModelRendererTurbo(this, 134, 27, textureX, textureY); // GLASS10
		glassModel[6] = new ModelRendererTurbo(this, 40, 74, textureX, textureY); // LAMP
		glassModel[7] = new ModelRendererTurbo(this, 40, 74, textureX, textureY); // LAMP
		glassModel[8] = new ModelRendererTurbo(this, 40, 74, textureX, textureY); // LAMP
		glassModel[9] = new ModelRendererTurbo(this, 52, 64, textureX, textureY); // Shape 152
		glassModel[10] = new ModelRendererTurbo(this, 52, 64, textureX, textureY); // Shape 152
		glassModel[11] = new ModelRendererTurbo(this, 210, 33, textureX, textureY); // GLASS02
		glassModel[12] = new ModelRendererTurbo(this, 210, 19, textureX, textureY); // GLASS01

		glassModel[0].addShapeBox(0F, 0F, 0F, 23, 11, 0, 0F, 0F, -11F, 0F, 0F, -11F, 0F, 0F, -11F, 0F, 0F, -11F, 0F, 0F, -11F, 0F, 0F, -11F, 0F, 0F, -11F, 0F, 0F, -11F, 0F); // GLASS01
		glassModel[0].setRotationPoint(16F, -27.5F, 4F);

		glassModel[1].addShapeBox(0F, 0F, 0F, 23, 22, 0, 0F, 0F, -22F, 0F, 0F, -22F, 0F, 0F, -22F, 0F, 0F, -22F, 0F, 0F, -22F, 0F, 0F, -22F, 0F, 0F, -22F, 0F, 0F, -22F, 0F); // GLASS02
		glassModel[1].setRotationPoint(16F, -46.5F, 17F);
		glassModel[1].rotateAngleX = -0.65449847F;

		glassModel[2].addShapeBox(0F, 0F, 0F, 0, 15, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GLASS03
		glassModel[2].setRotationPoint(16F, -45.5F, 17F);

		glassModel[3].addShapeBox(0F, 0F, 0F, 0, 14, 14, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GLASS04
		glassModel[3].setRotationPoint(16F, -29.5F, 35F);

		glassModel[4].addShapeBox(0F, 0F, 0F, 0, 15, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GLASS09
		glassModel[4].setRotationPoint(62F, -45.5F, 17F);

		glassModel[5].addShapeBox(0F, 0F, 0F, 0, 14, 14, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GLASS10
		glassModel[5].setRotationPoint(62F, -29.5F, 35F);

		glassModel[6].addShapeBox(0F, 0F, 0F, 4, 13, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // LAMP
		glassModel[6].setRotationPoint(21F, -41.5F, 19.5F);
		glassModel[6].rotateAngleX = 1.57079633F;

		glassModel[7].addShapeBox(0F, 0F, 0F, 4, 13, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // LAMP
		glassModel[7].setRotationPoint(36.5F, -41.5F, 19.5F);
		glassModel[7].rotateAngleX = 1.57079633F;

		glassModel[8].addShapeBox(0F, 0F, 0F, 4, 13, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // LAMP
		glassModel[8].setRotationPoint(52F, -41.5F, 19.5F);
		glassModel[8].rotateAngleX = 1.57079633F;

		glassModel[9].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(10, 0, 10, 0), new Coord2D(10, 25, 10, 25), new Coord2D(0, 12, 0, 12)}), 0, 10, 25, 64, 0, ModelRendererTurbo.MR_FRONT, new float[]{12, 17, 25, 10}); // Shape 152
		glassModel[9].setRotationPoint(16F, -17F, 5F);
		glassModel[9].rotateAngleY = -1.57079633F;

		glassModel[10].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(10, 0, 10, 0), new Coord2D(10, 25, 10, 25), new Coord2D(0, 12, 0, 12)}), 0, 10, 25, 64, 0, ModelRendererTurbo.MR_FRONT, new float[]{12, 17, 25, 10}); // Shape 152
		glassModel[10].setRotationPoint(62F, -17F, 5F);
		glassModel[10].rotateAngleY = -1.57079633F;

		glassModel[11].addShapeBox(0F, 0F, 0F, 23, 22, 0, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F); // GLASS02
		glassModel[11].setRotationPoint(39F, -46.5F, 17F);
		glassModel[11].rotateAngleX = -0.65449847F;

		glassModel[12].addShapeBox(0F, 0F, 0F, 23, 11, 0, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F); // GLASS01
		glassModel[12].setRotationPoint(39F, -27.5F, 4F);


		lampsModel = new ModelRendererTurbo[6];
		lampsModel[0] = new ModelRendererTurbo(this, 36, 90, textureX, textureY); // Conveyorsandlamps21
		lampsModel[1] = new ModelRendererTurbo(this, 36, 90, textureX, textureY); // Conveyorsandlamps21
		lampsModel[2] = new ModelRendererTurbo(this, 36, 90, textureX, textureY); // Conveyorsandlamps21
		lampsModel[3] = new ModelRendererTurbo(this, 36, 90, textureX, textureY); // Conveyorsandlamps21
		lampsModel[4] = new ModelRendererTurbo(this, 36, 90, textureX, textureY); // Conveyorsandlamps21
		lampsModel[5] = new ModelRendererTurbo(this, 36, 90, textureX, textureY); // Conveyorsandlamps21

		lampsModel[0].addShapeBox(0F, 0F, 0F, 2, 12, 1, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // Conveyorsandlamps21
		lampsModel[0].setRotationPoint(21F, -42F, 20F);
		lampsModel[0].rotateAngleX = 1.57079633F;

		lampsModel[1].addShapeBox(0F, 0F, 0F, 2, 12, 1, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // Conveyorsandlamps21
		lampsModel[1].setRotationPoint(23F, -42F, 20F);
		lampsModel[1].rotateAngleX = 1.57079633F;

		lampsModel[2].addShapeBox(0F, 0F, 0F, 2, 12, 1, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // Conveyorsandlamps21
		lampsModel[2].setRotationPoint(52F, -42F, 20F);
		lampsModel[2].rotateAngleX = 1.57079633F;

		lampsModel[3].addShapeBox(0F, 0F, 0F, 2, 12, 1, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // Conveyorsandlamps21
		lampsModel[3].setRotationPoint(54F, -42F, 20F);
		lampsModel[3].rotateAngleX = 1.57079633F;

		lampsModel[4].addShapeBox(0F, 0F, 0F, 2, 12, 1, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // Conveyorsandlamps21
		lampsModel[4].setRotationPoint(36.5F, -42F, 20F);
		lampsModel[4].rotateAngleX = 1.57079633F;

		lampsModel[5].addShapeBox(0F, 0F, 0F, 2, 12, 1, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F); // Conveyorsandlamps21
		lampsModel[5].setRotationPoint(38.5F, -42F, 20F);
		lampsModel[5].rotateAngleX = 1.57079633F;


		conveyorLifterModel = new ModelRendererTurbo[2];
		conveyorLifterModel[0] = new ModelRendererTurbo(this, 132, 73, textureX, textureY); // MAINFRAME01
		conveyorLifterModel[1] = new ModelRendererTurbo(this, 240, 3, textureX, textureY); // MAINFRAME01

		conveyorLifterModel[0].addShapeBox(-6F, -6F, 0, 12, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAINFRAME01
		conveyorLifterModel[0].setRotationPoint(33F+6, -21F, 19F+6);
		conveyorLifterModel[0].rotateAngleX = 1.57079633F;

		conveyorLifterModel[1].addBox(-2F, 0F, -2F, 4, 9, 4, 0F); // MAINFRAME01
		conveyorLifterModel[1].setRotationPoint(37F+2, -21F, 23F+2);

		parts.put("base",baseModel);
		parts.put("conveyorLifter",conveyorLifterModel);
		parts.put("lamps",lampsModel);
		parts.put("glass",glassModel);
		parts.put("nozzle",nozzleModel);

		translateAll(1F, 0F, -1F);
		translate(conveyorLifterModel,0,7,0);

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
				GlStateManager.translate(mirrored?-2:3, 0f, 2f);

			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(0, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?-3f:2f, 0f, 1f);
			}
			break;
			case EAST:
			{
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?-2f:3, 0f, 1f);

			}
			break;
			case WEST:
			{
				GlStateManager.rotate(-90F, 0F, 1F, 0F);

				GlStateManager.translate(mirrored?-3:2, 0f, 2f);

			}
			break;
		}
	}
}
