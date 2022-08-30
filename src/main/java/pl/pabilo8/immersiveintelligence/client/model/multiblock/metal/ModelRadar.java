package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * @author Pabilo8
 * @since 25.07.2021
 */
public class ModelRadar extends ModelIIBase
{
	public ModelRendererTurbo[] radarModel, triangulatorsModel;
	int textureX = 256;
	int textureY = 256;

	public ModelRadar() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[150];
		baseModel[0] = new ModelRendererTurbo(this, 144, 72, textureX, textureY); // BASE01
		baseModel[1] = new ModelRendererTurbo(this, 44, 88, textureX, textureY); // BASE02
		baseModel[2] = new ModelRendererTurbo(this, 124, 50, textureX, textureY); // BASE03
		baseModel[3] = new ModelRendererTurbo(this, 124, 50, textureX, textureY); // BASE04
		baseModel[4] = new ModelRendererTurbo(this, 124, 50, textureX, textureY); // BASE05
		baseModel[5] = new ModelRendererTurbo(this, 124, 50, textureX, textureY); // BASE06
		baseModel[6] = new ModelRendererTurbo(this, 0, 161, textureX, textureY); // BASE07
		baseModel[7] = new ModelRendererTurbo(this, 0, 55, textureX, textureY); // BASE08
		baseModel[8] = new ModelRendererTurbo(this, 126, 128, textureX, textureY); // BASE09
		baseModel[9] = new ModelRendererTurbo(this, 0, 128, textureX, textureY); // BASE10
		baseModel[10] = new ModelRendererTurbo(this, 48, 15, textureX, textureY); // BASE11
		baseModel[11] = new ModelRendererTurbo(this, 0, 85, textureX, textureY); // BASE20
		baseModel[12] = new ModelRendererTurbo(this, 28, 189, textureX, textureY); // BASE21
		baseModel[13] = new ModelRendererTurbo(this, 0, 189, textureX, textureY); // BASE22
		baseModel[14] = new ModelRendererTurbo(this, 44, 1, textureX, textureY); // BASE23
		baseModel[15] = new ModelRendererTurbo(this, 20, 77, textureX, textureY); // BASE24
		baseModel[16] = new ModelRendererTurbo(this, 20, 77, textureX, textureY); // BASE27
		baseModel[17] = new ModelRendererTurbo(this, 20, 77, textureX, textureY); // BASE29
		baseModel[18] = new ModelRendererTurbo(this, 28, 93, textureX, textureY); // BASE30
		baseModel[19] = new ModelRendererTurbo(this, 28, 93, textureX, textureY); // BASE31
		baseModel[20] = new ModelRendererTurbo(this, 28, 93, textureX, textureY); // BASE32
		baseModel[21] = new ModelRendererTurbo(this, 140, 19, textureX, textureY); // BASE33
		baseModel[22] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE34
		baseModel[23] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE35
		baseModel[24] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE36
		baseModel[25] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE40
		baseModel[26] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE41
		baseModel[27] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE42
		baseModel[28] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE43
		baseModel[29] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE44
		baseModel[30] = new ModelRendererTurbo(this, 224, 46, textureX, textureY); // BASE45
		baseModel[31] = new ModelRendererTurbo(this, 21, 8, textureX, textureY); // BASE46
		baseModel[32] = new ModelRendererTurbo(this, 48, 15, textureX, textureY); // BASE47
		baseModel[33] = new ModelRendererTurbo(this, 104, 0, textureX, textureY); // BASE48
		baseModel[34] = new ModelRendererTurbo(this, 56, 0, textureX, textureY); // BASE49
		baseModel[35] = new ModelRendererTurbo(this, 104, 0, textureX, textureY); // BASE50
		baseModel[36] = new ModelRendererTurbo(this, 21, 8, textureX, textureY); // BASE51
		baseModel[37] = new ModelRendererTurbo(this, 48, 15, textureX, textureY); // BASE52
		baseModel[38] = new ModelRendererTurbo(this, 140, 19, textureX, textureY); // BASE53
		baseModel[39] = new ModelRendererTurbo(this, 142, 0, textureX, textureY); // BASE54
		baseModel[40] = new ModelRendererTurbo(this, 142, 0, textureX, textureY); // BASE55
		baseModel[41] = new ModelRendererTurbo(this, 92, 165, textureX, textureY); // BASE56
		baseModel[42] = new ModelRendererTurbo(this, 14, 41, textureX, textureY); // BASE57
		baseModel[43] = new ModelRendererTurbo(this, 14, 41, textureX, textureY); // BASE58
		baseModel[44] = new ModelRendererTurbo(this, 144, 52, textureX, textureY); // BASE59
		baseModel[45] = new ModelRendererTurbo(this, 144, 52, textureX, textureY); // BASE60
		baseModel[46] = new ModelRendererTurbo(this, 144, 52, textureX, textureY); // BASE61
		baseModel[47] = new ModelRendererTurbo(this, 197, 181, textureX, textureY); // BASE63
		baseModel[48] = new ModelRendererTurbo(this, 144, 52, textureX, textureY); // BASE64
		baseModel[49] = new ModelRendererTurbo(this, 228, 183, textureX, textureY); // BASE65
		baseModel[50] = new ModelRendererTurbo(this, 228, 183, textureX, textureY); // BASE66
		baseModel[51] = new ModelRendererTurbo(this, 228, 171, textureX, textureY); // BASE67
		baseModel[52] = new ModelRendererTurbo(this, 228, 171, textureX, textureY); // BASE68
		baseModel[53] = new ModelRendererTurbo(this, 5, 38, textureX, textureY); // BASE69
		baseModel[54] = new ModelRendererTurbo(this, 19, 21, textureX, textureY); // BASE70
		baseModel[55] = new ModelRendererTurbo(this, 22, 9, textureX, textureY); // BASE71
		baseModel[56] = new ModelRendererTurbo(this, 28, 0, textureX, textureY); // BASE72
		baseModel[57] = new ModelRendererTurbo(this, 94, 183, textureX, textureY); // BASE73
		baseModel[58] = new ModelRendererTurbo(this, 94, 190, textureX, textureY); // BASE74
		baseModel[59] = new ModelRendererTurbo(this, 104, 0, textureX, textureY); // Box 123
		baseModel[60] = new ModelRendererTurbo(this, 167, 128, textureX, textureY); // Box 124
		baseModel[61] = new ModelRendererTurbo(this, 218, 159, textureX, textureY); // Box 129
		baseModel[62] = new ModelRendererTurbo(this, 196, 169, textureX, textureY); // Box 130
		baseModel[63] = new ModelRendererTurbo(this, 26, 43, textureX, textureY); // Box 131
		baseModel[64] = new ModelRendererTurbo(this, 23, 36, textureX, textureY); // Box 132
		baseModel[65] = new ModelRendererTurbo(this, 29, 17, textureX, textureY); // Box 133
		baseModel[66] = new ModelRendererTurbo(this, 14, 41, textureX, textureY); // BASE57
		baseModel[67] = new ModelRendererTurbo(this, 14, 41, textureX, textureY); // BASE58
		baseModel[68] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // BASE03
		baseModel[69] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // BASE04
		baseModel[70] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // BASE05
		baseModel[71] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // BASE06
		baseModel[72] = new ModelRendererTurbo(this, 192, 46, textureX, textureY); // BASE02
		baseModel[73] = new ModelRendererTurbo(this, 192, 46, textureX, textureY); // BASE02
		baseModel[74] = new ModelRendererTurbo(this, 144, 25, textureX, textureY); // BASE02
		baseModel[75] = new ModelRendererTurbo(this, 140, 25, textureX, textureY); // BASE02
		baseModel[76] = new ModelRendererTurbo(this, 140, 25, textureX, textureY); // BASE02
		baseModel[77] = new ModelRendererTurbo(this, 144, 25, textureX, textureY); // BASE02
		baseModel[78] = new ModelRendererTurbo(this, 192, 46, textureX, textureY); // BASE02
		baseModel[79] = new ModelRendererTurbo(this, 140, 25, textureX, textureY); // BASE02
		baseModel[80] = new ModelRendererTurbo(this, 140, 25, textureX, textureY); // BASE02
		baseModel[81] = new ModelRendererTurbo(this, 140, 25, textureX, textureY); // BASE02
		baseModel[82] = new ModelRendererTurbo(this, 155, 25, textureX, textureY); // BASE02
		baseModel[83] = new ModelRendererTurbo(this, 192, 46, textureX, textureY); // BASE02
		baseModel[84] = new ModelRendererTurbo(this, 140, 25, textureX, textureY); // BASE02
		baseModel[85] = new ModelRendererTurbo(this, 140, 25, textureX, textureY); // BASE02
		baseModel[86] = new ModelRendererTurbo(this, 206, 54, textureX, textureY); // Box 124
		baseModel[87] = new ModelRendererTurbo(this, 206, 54, textureX, textureY); // Box 124
		baseModel[88] = new ModelRendererTurbo(this, 119, 137, textureX, textureY); // BASE23
		baseModel[89] = new ModelRendererTurbo(this, 88, 200, textureX, textureY); // BASE23
		baseModel[90] = new ModelRendererTurbo(this, 119, 137, textureX, textureY); // BASE23
		baseModel[91] = new ModelRendererTurbo(this, 44, 77, textureX, textureY); // BASE35
		baseModel[92] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE35
		baseModel[93] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE35
		baseModel[94] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE35
		baseModel[95] = new ModelRendererTurbo(this, 44, 77, textureX, textureY); // BASE35
		baseModel[96] = new ModelRendererTurbo(this, 44, 77, textureX, textureY); // BASE35
		baseModel[97] = new ModelRendererTurbo(this, 224, 46, textureX, textureY); // BASE43
		baseModel[98] = new ModelRendererTurbo(this, 2, 49, textureX, textureY); // BASE43
		baseModel[99] = new ModelRendererTurbo(this, 2, 49, textureX, textureY); // BASE43
		baseModel[100] = new ModelRendererTurbo(this, 212, 46, textureX, textureY); // BASE43
		baseModel[101] = new ModelRendererTurbo(this, 224, 46, textureX, textureY); // BASE35
		baseModel[102] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE35
		baseModel[103] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE35
		baseModel[104] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE35
		baseModel[105] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE43
		baseModel[106] = new ModelRendererTurbo(this, 17, 33, textureX, textureY); // BASE35
		baseModel[107] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE35
		baseModel[108] = new ModelRendererTurbo(this, 17, 33, textureX, textureY); // BASE35
		baseModel[109] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE45
		baseModel[110] = new ModelRendererTurbo(this, 44, 77, textureX, textureY); // BASE35
		baseModel[111] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE35
		baseModel[112] = new ModelRendererTurbo(this, 18, 26, textureX, textureY); // BASE35
		baseModel[113] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE35
		baseModel[114] = new ModelRendererTurbo(this, 224, 46, textureX, textureY); // BASE43
		baseModel[115] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE43
		baseModel[116] = new ModelRendererTurbo(this, 17, 33, textureX, textureY); // BASE43
		baseModel[117] = new ModelRendererTurbo(this, 44, 77, textureX, textureY); // BASE43
		baseModel[118] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE43
		baseModel[119] = new ModelRendererTurbo(this, 60, 56, textureX, textureY); // Box 123
		baseModel[120] = new ModelRendererTurbo(this, 104, 0, textureX, textureY); // Box 123
		baseModel[121] = new ModelRendererTurbo(this, 76, 161, textureX, textureY); // Box 124
		baseModel[122] = new ModelRendererTurbo(this, 167, 128, textureX, textureY); // Box 124
		baseModel[123] = new ModelRendererTurbo(this, 206, 54, textureX, textureY); // Box 124
		baseModel[124] = new ModelRendererTurbo(this, 104, 0, textureX, textureY); // Box 123
		baseModel[125] = new ModelRendererTurbo(this, 60, 56, textureX, textureY); // Box 123
		baseModel[126] = new ModelRendererTurbo(this, 156, 169, textureX, textureY); // Box 124
		baseModel[127] = new ModelRendererTurbo(this, 156, 169, textureX, textureY); // Box 124
		baseModel[128] = new ModelRendererTurbo(this, 224, 46, textureX, textureY); // BASE34
		baseModel[129] = new ModelRendererTurbo(this, 144, 72, textureX, textureY); // BASE01
		baseModel[130] = new ModelRendererTurbo(this, 144, 72, textureX, textureY); // BASE01
		baseModel[131] = new ModelRendererTurbo(this, 144, 72, textureX, textureY); // BASE01
		baseModel[132] = new ModelRendererTurbo(this, 44, 88, textureX, textureY); // BASE02
		baseModel[133] = new ModelRendererTurbo(this, 44, 88, textureX, textureY); // BASE02
		baseModel[134] = new ModelRendererTurbo(this, 44, 88, textureX, textureY); // BASE02
		baseModel[135] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE35
		baseModel[136] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE45
		baseModel[137] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE45
		baseModel[138] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE43
		baseModel[139] = new ModelRendererTurbo(this, 28, 93, textureX, textureY); // BASE30
		baseModel[140] = new ModelRendererTurbo(this, 28, 93, textureX, textureY); // BASE31
		baseModel[141] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE44
		baseModel[142] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE44
		baseModel[143] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE44
		baseModel[144] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE44
		baseModel[145] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE44
		baseModel[146] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE44
		baseModel[147] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE44
		baseModel[148] = new ModelRendererTurbo(this, 200, 46, textureX, textureY); // BASE44
		baseModel[149] = new ModelRendererTurbo(this, 183, 153, textureX, textureY); // TableKeyboardMain

		baseModel[0].addShapeBox(0F, 0F, 0F, 48, 48, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE01
		baseModel[0].setRotationPoint(0F, 12F, 0F);
		baseModel[0].rotateAngleX = 1.57079633F;

		baseModel[1].addShapeBox(0F, 0F, 0F, 32, 32, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE02
		baseModel[1].setRotationPoint(17F, 4F, 16F);
		baseModel[1].rotateAngleX = 1.57079633F;

		baseModel[2].addShapeBox(1F, 0F, -1F, 5, 73, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE03
		baseModel[2].setRotationPoint(44F, -71F, 48F);
		baseModel[2].rotateAngleX = -0.43633231F;
		baseModel[2].rotateAngleY = -0.78539816F;

		baseModel[3].addShapeBox(1F, 0F, -1F, 5, 73, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE04
		baseModel[3].setRotationPoint(49F, -71F, 43F);
		baseModel[3].rotateAngleX = -0.43633231F;
		baseModel[3].rotateAngleY = 0.78539816F;

		baseModel[4].addShapeBox(1F, 0F, -1F, 5, 73, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE05
		baseModel[4].setRotationPoint(54F, -71F, 48F);
		baseModel[4].rotateAngleX = -0.43633231F;
		baseModel[4].rotateAngleY = 2.35619449F;

		baseModel[5].addShapeBox(1F, 0F, -1F, 5, 73, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE06
		baseModel[5].setRotationPoint(49F, -71F, 53F);
		baseModel[5].rotateAngleX = -0.43633231F;
		baseModel[5].rotateAngleY = 3.92699082F;

		baseModel[6].addShapeBox(0F, 0F, 0F, 25, 3, 25, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE07
		baseModel[6].setRotationPoint(36F, -70F, 35F);

		baseModel[7].addShapeBox(0F, 0F, 0F, 6, 24, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE08
		baseModel[7].setRotationPoint(46F, -69F, 45F);

		baseModel[8].addShapeBox(0F, 0F, 0F, 14, 5, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE09
		baseModel[8].setRotationPoint(32F, -57F, 46F);

		baseModel[9].addShapeBox(0F, 0F, 0F, 6, 2, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F); // BASE10
		baseModel[9].setRotationPoint(46F, -45F, 45F);

		baseModel[10].addShapeBox(0F, 0F, 0F, 2, 12, 2, 0F, -4F, 0F, 0F, 4F, 0F, 0F, 4F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE11
		baseModel[10].setRotationPoint(35F, -69F, 47F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 6, 18, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE20
		baseModel[11].setRotationPoint(32F, -4F, 52F);
		baseModel[11].rotateAngleZ = 1.57079633F;

		baseModel[12].addShapeBox(0F, 0F, 0F, 12, 12, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE21
		baseModel[12].setRotationPoint(50F, -20F, 94F);

		baseModel[13].addShapeBox(0F, 0F, 0F, 12, 12, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE22
		baseModel[13].setRotationPoint(66F, -20F, 94F);

		baseModel[14].addShapeBox(0F, 0F, 0F, 12, 12, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE23
		baseModel[14].setRotationPoint(82F, -20F, 94F);

		baseModel[15].addBox(0F, 0F, 0F, 8, 8, 8, 0F); // BASE24
		baseModel[15].setRotationPoint(52F, -18F, 86F);

		baseModel[16].addBox(0F, 0F, 0F, 8, 8, 8, 0F); // BASE27
		baseModel[16].setRotationPoint(68F, -18F, 86F);

		baseModel[17].addBox(0F, 0F, 0F, 8, 8, 8, 0F); // BASE29
		baseModel[17].setRotationPoint(84F, -18F, 86F);

		baseModel[18].addBox(0F, 0F, 0F, 5, 5, 1, 0F); // BASE30
		baseModel[18].setRotationPoint(32.5F, -9F, 60F);

		baseModel[19].addBox(0F, 0F, 0F, 5, 5, 1, 0F); // BASE31
		baseModel[19].setRotationPoint(38.5F, -9F, 60F);

		baseModel[20].addBox(0F, 0F, 0F, 5, 5, 1, 0F); // BASE32
		baseModel[20].setRotationPoint(44.5F, -9F, 60F);

		baseModel[21].addShapeBox(0F, 0F, 0F, 1, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE33
		baseModel[21].setRotationPoint(31F, -7.5F, 53F);

		baseModel[22].addShapeBox(0F, 0F, 0F, 3, 12, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE34
		baseModel[22].setRotationPoint(48.5F, -4F, 64F);
		baseModel[22].rotateAngleZ = 1.57079633F;

		baseModel[23].addShapeBox(0F, 0F, 0F, 3, 9, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE35
		baseModel[23].setRotationPoint(60.5F, -4F, 67F);
		baseModel[23].rotateAngleX = 1.57079633F;

		baseModel[24].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE36
		baseModel[24].setRotationPoint(45.5F, -4F, 61F);
		baseModel[24].rotateAngleX = 1.57079633F;

		baseModel[25].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE40
		baseModel[25].setRotationPoint(39.5F, -4F, 61F);
		baseModel[25].rotateAngleX = 1.57079633F;

		baseModel[26].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE41
		baseModel[26].setRotationPoint(33.5F, -4F, 61F);
		baseModel[26].rotateAngleX = 1.57079633F;

		baseModel[27].addShapeBox(0F, 0F, 0F, 3, 6, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE42
		baseModel[27].setRotationPoint(39.5F, -4F, 64F);
		baseModel[27].rotateAngleX = 1.57079633F;

		baseModel[28].addShapeBox(0F, 0F, 0F, 3, 6, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F, -1F, 0F, 0F, -1F); // BASE43
		baseModel[28].setRotationPoint(42.5F, -4F, 70F);
		baseModel[28].rotateAngleZ = 1.57079633F;

		baseModel[29].addShapeBox(0F, 0F, 0F, 3, 13, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 1F, 0F, 0F); // BASE44
		baseModel[29].setRotationPoint(33.5F, -4F, 64F);
		baseModel[29].rotateAngleX = 1.57079633F;

		baseModel[30].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // BASE45
		baseModel[30].setRotationPoint(32.5F, -7F, 77F);

		baseModel[31].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F); // BASE46
		baseModel[31].setRotationPoint(29F, -7F, 53.5F);

		baseModel[32].addBox(0F, 0F, 0F, 2, 12, 2, 0F); // BASE47
		baseModel[32].setRotationPoint(17F, -4F, 53.5F);
		baseModel[32].rotateAngleZ = 1.57079633F;

		baseModel[33].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE48
		baseModel[33].setRotationPoint(30F, -7F, 53.5F);

		baseModel[34].addShapeBox(0F, 0F, 0F, 16, 32, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE49
		baseModel[34].setRotationPoint(0F, 4F, 48F);
		baseModel[34].rotateAngleX = 1.57079633F;

		baseModel[35].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE50
		baseModel[35].setRotationPoint(30F, -7F, 57F);

		baseModel[36].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F); // BASE51
		baseModel[36].setRotationPoint(29F, -7F, 57F);

		baseModel[37].addBox(0F, 0F, 0F, 2, 12, 2, 0F); // BASE52
		baseModel[37].setRotationPoint(17F, -4F, 57F);
		baseModel[37].rotateAngleZ = 1.57079633F;

		baseModel[38].addShapeBox(0F, 0F, 0F, 1, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE53
		baseModel[38].setRotationPoint(31F, -7.5F, 56.5F);

		baseModel[39].addShapeBox(0F, 0F, 0F, 1, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE54
		baseModel[39].setRotationPoint(16F, -6.5F, 53F);

		baseModel[40].addShapeBox(0F, 0F, 0F, 1, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE55
		baseModel[40].setRotationPoint(16F, -6.5F, 56.5F);

		baseModel[41].addShapeBox(0F, 0F, 0F, 16, 3, 32, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE56
		baseModel[41].setRotationPoint(-16F, -2F, 48F);

		baseModel[42].addShapeBox(0F, 0F, 0F, 3, 11, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE57
		baseModel[42].setRotationPoint(-15F, 1F, 49F);

		baseModel[43].addShapeBox(0F, 0F, 0F, 3, 11, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE58
		baseModel[43].setRotationPoint(-15F, 1F, 76F);

		baseModel[44].addShapeBox(0F, 0F, 0F, 1, 6, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE59
		baseModel[44].setRotationPoint(-1F, -17.5F, 56F);

		baseModel[45].addShapeBox(0F, 0F, 0F, 1, 6, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE60
		baseModel[45].setRotationPoint(-1F, -11F, 56F);

		baseModel[46].addShapeBox(0F, 0F, 0F, 1, 6, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE61
		baseModel[46].setRotationPoint(-1F, -11F, 49F);

		baseModel[47].addShapeBox(0F, 0F, 0F, 8, 6, 15, 0F, 0F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE63
		baseModel[47].setRotationPoint(0F, -18F, 48F);

		baseModel[48].addShapeBox(0F, 0F, 0F, 1, 6, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE64
		baseModel[48].setRotationPoint(-1F, -17.5F, 49F);

		baseModel[49].addShapeBox(0F, 0F, 0F, 1, 1, 11, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE65
		baseModel[49].setRotationPoint(-15.5F, -3.5F, 67.5F);
		baseModel[49].rotateAngleZ = 0.17453293F;

		baseModel[50].addShapeBox(0F, 0F, 0F, 1, 1, 11, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE66
		baseModel[50].setRotationPoint(-12.5F, -5F, 67.5F);
		baseModel[50].rotateAngleZ = 0.17453293F;

		baseModel[51].addShapeBox(0F, 0F, 0F, 1, 1, 11, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE67
		baseModel[51].setRotationPoint(-14F, -4.3F, 67.5F);
		baseModel[51].rotateAngleZ = 0.17453293F;

		baseModel[52].addShapeBox(0F, 0F, 0F, 1, 1, 11, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE68
		baseModel[52].setRotationPoint(-11F, -5.66F, 67.5F);
		baseModel[52].rotateAngleZ = 0.17453293F;

		baseModel[53].addShapeBox(0F, 0F, 0F, 1, 1, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE69
		baseModel[53].setRotationPoint(-9F, -7F, 70.5F);

		baseModel[54].addShapeBox(0F, 0F, 0F, 3, 1, 11, 0F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE70
		baseModel[54].setRotationPoint(-9.2F, -6.5F, 67.5F);

		baseModel[55].addShapeBox(0F, 0F, 0F, 2, 1, 11, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE71
		baseModel[55].setRotationPoint(-3.2F, -7F, 67.5F);

		baseModel[56].addShapeBox(0F, 0F, 0F, 8, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE72
		baseModel[56].setRotationPoint(-8.5F, -7F, 72.5F);
		baseModel[56].rotateAngleZ = 0.48869219F;

		baseModel[57].addShapeBox(0F, 0F, -0.1F, 8, 1, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE73
		baseModel[57].setRotationPoint(-8.5F, -7.5F, 73.2F);
		baseModel[57].rotateAngleX = 0.08726646F;
		baseModel[57].rotateAngleZ = 0.48869219F;

		baseModel[58].addShapeBox(0F, -0.5F, 0.1F, 8, 1, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE74
		baseModel[58].setRotationPoint(-8.5F, -7.5F, 67F);
		baseModel[58].rotateAngleX = -0.08726646F;
		baseModel[58].rotateAngleZ = 0.48869219F;

		baseModel[59].addShapeBox(0F, 0F, 0F, 15, 5, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 123
		baseModel[59].setRotationPoint(7F, -1F, 80F);
		baseModel[59].rotateAngleY = 0.83775804F;

		baseModel[60].addShapeBox(0F, 0F, 0F, 12, 10, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 124
		baseModel[60].setRotationPoint(1F, -6F, 28F);
		baseModel[60].rotateAngleY = -1.13446401F;

		baseModel[61].addShapeBox(0F, 0F, 0F, 6, 2, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 129
		baseModel[61].setRotationPoint(4F, -14F, 67F);
		baseModel[61].rotateAngleY = -0.26179939F;

		baseModel[62].addShapeBox(0F, 0F, 0F, 6, 2, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 130
		baseModel[62].setRotationPoint(5F, -16F, 66F);
		baseModel[62].rotateAngleY = -0.01745329F;

		baseModel[63].addShapeBox(0F, 0F, 0F, 14, 17, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F); // Box 131
		baseModel[63].setRotationPoint(-15F, -2F, 49F);
		baseModel[63].rotateAngleX = 1.57079633F;

		baseModel[64].addShapeBox(0F, 0F, 0F, 1, 1, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 132
		baseModel[64].setRotationPoint(-12F, -3.5F, 53F);
		baseModel[64].rotateAngleY = -0.2443461F;

		baseModel[65].addShapeBox(0F, 0F, 6F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F); // Box 133
		baseModel[65].setRotationPoint(-12F, -3.5F, 53F);
		baseModel[65].rotateAngleY = -0.2443461F;

		baseModel[66].addShapeBox(0F, 0F, 0F, 3, 11, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE57
		baseModel[66].setRotationPoint(-4F, 1F, 49F);

		baseModel[67].addShapeBox(0F, 0F, 0F, 3, 11, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE58
		baseModel[67].setRotationPoint(-4F, 1F, 76F);

		baseModel[68].addShapeBox(0F, 66F, -2F, 7, 10, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE03
		baseModel[68].setRotationPoint(44F, -71F, 48F);
		baseModel[68].rotateAngleX = -0.43633231F;
		baseModel[68].rotateAngleY = -0.78539816F;

		baseModel[69].addShapeBox(0F, 66F, -2F, 7, 10, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE04
		baseModel[69].setRotationPoint(49F, -71F, 43F);
		baseModel[69].rotateAngleX = -0.43633231F;
		baseModel[69].rotateAngleY = 0.78539816F;

		baseModel[70].addShapeBox(0F, 66F, -2F, 7, 10, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE05
		baseModel[70].setRotationPoint(54F, -71F, 48F);
		baseModel[70].rotateAngleX = -0.43633231F;
		baseModel[70].rotateAngleY = 2.35619449F;

		baseModel[71].addShapeBox(0F, 66F, -2F, 7, 10, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE06
		baseModel[71].setRotationPoint(49F, -71F, 53F);
		baseModel[71].rotateAngleX = -0.43633231F;
		baseModel[71].rotateAngleY = 3.92699082F;

		baseModel[72].addBox(0F, 0F, 0F, 2, 16, 2, 0F); // BASE02
		baseModel[72].setRotationPoint(18F, -20F, 19F);

		baseModel[73].addBox(0F, 0F, 0F, 2, 16, 2, 0F); // BASE02
		baseModel[73].setRotationPoint(76F, -20F, 17F);

		baseModel[74].addBox(1F, 0F, 0F, 12, 16, 0, 0F); // BASE02
		baseModel[74].setRotationPoint(19F, -20F, 20F);
		baseModel[74].rotateAngleY = -0.03490659F;

		baseModel[75].addBox(13F, 0F, 0F, 16, 16, 0, 0F); // BASE02
		baseModel[75].setRotationPoint(19F, -20F, 20F);
		baseModel[75].rotateAngleY = -0.03490659F;

		baseModel[76].addBox(29F, 0F, 0F, 16, 16, 0, 0F); // BASE02
		baseModel[76].setRotationPoint(19F, -20F, 20F);
		baseModel[76].rotateAngleY = -0.03490659F;

		baseModel[77].addBox(45F, 0F, 0F, 12, 16, 0, 0F); // BASE02
		baseModel[77].setRotationPoint(19F, -20F, 20F);
		baseModel[77].rotateAngleY = -0.03490659F;

		baseModel[78].addBox(0F, 0F, 0F, 2, 16, 2, 0F); // BASE02
		baseModel[78].setRotationPoint(78F, -20F, 68F);

		baseModel[79].addBox(1F, 0F, 0F, 16, 16, 0, 0F); // BASE02
		baseModel[79].setRotationPoint(77F, -20F, 18F);
		baseModel[79].rotateAngleY = 1.53588974F;

		baseModel[80].addBox(17F, 0F, 0F, 16, 16, 0, 0F); // BASE02
		baseModel[80].setRotationPoint(77F, -20F, 18F);
		baseModel[80].rotateAngleY = 1.53588974F;

		baseModel[81].addBox(33F, 0F, 0F, 16, 16, 0, 0F); // BASE02
		baseModel[81].setRotationPoint(77F, -20F, 18F);
		baseModel[81].rotateAngleY = 1.53588974F;

		baseModel[82].addBox(49F, 0F, 0F, 1, 16, 0, 0F); // BASE02
		baseModel[82].setRotationPoint(77F, -20F, 18F);
		baseModel[82].rotateAngleY = 1.53588974F;

		baseModel[83].addBox(0F, 0F, 0F, 2, 16, 2, 0F); // BASE02
		baseModel[83].setRotationPoint(22F, -20F, 50F);

		baseModel[84].addBox(0F, 0F, 0F, 16, 16, 0, 0F); // BASE02
		baseModel[84].setRotationPoint(19F, -20F, 20F);
		baseModel[84].rotateAngleY = 1.43116999F;

		baseModel[85].addBox(16F, 0F, 0F, 16, 16, 0, 0F); // BASE02
		baseModel[85].setRotationPoint(19F, -20F, 20F);
		baseModel[85].rotateAngleY = 1.43116999F;

		baseModel[86].addShapeBox(0F, 0F, 0F, 10, 8, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 124
		baseModel[86].setRotationPoint(5F, -4F, 46F);
		baseModel[86].rotateAngleY = -1.85004901F;

		baseModel[87].addShapeBox(0F, 0F, -2F, 10, 8, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 124
		baseModel[87].setRotationPoint(12F, -12F, 41F);
		baseModel[87].rotateAngleX = 0.20943951F;
		baseModel[87].rotateAngleY = -2.60054059F;

		baseModel[88].addShapeBox(0F, 0F, 0F, 16, 12, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE23
		baseModel[88].setRotationPoint(80F, -8F, 80F);

		baseModel[89].addShapeBox(0F, 0F, 0F, 16, 12, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE23
		baseModel[89].setRotationPoint(64F, -8F, 80F);

		baseModel[90].setFlipped(true);
		baseModel[90].addShapeBox(0F, 0F, 0F, 16, 12, 16, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F); // BASE23
		baseModel[90].setRotationPoint(48F, -8F, 80F);

		baseModel[91].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE35
		baseModel[91].setRotationPoint(70.5F, -15F, 82F);

		baseModel[92].addShapeBox(0F, 0F, 0F, 3, 6, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE35
		baseModel[92].setRotationPoint(54.5F, -12F, 80F);
		baseModel[92].rotateAngleX = 1.57079633F;

		baseModel[93].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE35
		baseModel[93].setRotationPoint(48.5F, -8F, 73F);

		baseModel[94].addShapeBox(0F, 0F, 0F, 3, 5, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE35
		baseModel[94].setRotationPoint(54.5F, -12F, 77F);

		baseModel[95].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE35
		baseModel[95].setRotationPoint(48.5F, -11F, 73F);

		baseModel[96].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE35
		baseModel[96].setRotationPoint(54.5F, -15F, 77F);

		baseModel[97].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // BASE43
		baseModel[97].setRotationPoint(39.5F, -7F, 70F);

		baseModel[98].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE43
		baseModel[98].setRotationPoint(60.5F, -7F, 64F);

		baseModel[99].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE43
		baseModel[99].setRotationPoint(48.5F, -7F, 69F);

		baseModel[100].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // BASE43
		baseModel[100].setRotationPoint(48.5F, -7F, 73F);

		baseModel[101].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // BASE35
		baseModel[101].setRotationPoint(48.5F, -11F, 82F);

		baseModel[102].addShapeBox(0F, 0F, 0F, 3, 6, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE35
		baseModel[102].setRotationPoint(48.5F, -8F, 76F);
		baseModel[102].rotateAngleX = 1.57079633F;

		baseModel[103].addShapeBox(0F, 0F, 0F, 3, 10, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE35
		baseModel[103].setRotationPoint(60.5F, -8F, 82F);
		baseModel[103].rotateAngleZ = 1.57079633F;

		baseModel[104].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE35
		baseModel[104].setRotationPoint(70.5F, -12F, 85F);
		baseModel[104].rotateAngleX = 1.57079633F;

		baseModel[105].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE43
		baseModel[105].setRotationPoint(48.5F, -4F, 72F);
		baseModel[105].rotateAngleX = 1.57079633F;

		baseModel[106].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F); // BASE35
		baseModel[106].setRotationPoint(70.5F, -11F, 82F);

		baseModel[107].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE35
		baseModel[107].setRotationPoint(70.5F, -12F, 82F);

		baseModel[108].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE35
		baseModel[108].setRotationPoint(54.5F, -7F, 77F);

		baseModel[109].addShapeBox(0F, 0F, 0F, 3, 10, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE45
		baseModel[109].setRotationPoint(35.5F, -4F, 77F);
		baseModel[109].rotateAngleZ = 1.57079633F;

		baseModel[110].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE35
		baseModel[110].setRotationPoint(86.5F, -15F, 82F);

		baseModel[111].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE35
		baseModel[111].setRotationPoint(86.5F, -12F, 85F);
		baseModel[111].rotateAngleX = 1.57079633F;

		baseModel[112].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // BASE35
		baseModel[112].setRotationPoint(86.5F, -11F, 82F);

		baseModel[113].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE35
		baseModel[113].setRotationPoint(86.5F, -12F, 82F);

		baseModel[114].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // BASE43
		baseModel[114].setRotationPoint(60.5F, -7F, 76F);

		baseModel[115].addShapeBox(0F, 0F, 0F, 3, 15, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE43
		baseModel[115].setRotationPoint(63.5F, -4F, 76F);
		baseModel[115].rotateAngleZ = 1.57079633F;

		baseModel[116].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F); // BASE43
		baseModel[116].setRotationPoint(86.5F, -7F, 77F);

		baseModel[117].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE43
		baseModel[117].setRotationPoint(86.5F, -11F, 77F);

		baseModel[118].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE43
		baseModel[118].setRotationPoint(86.5F, -8F, 77F);

		baseModel[119].addShapeBox(0F, 0F, 0F, 16, 16, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 123
		baseModel[119].setRotationPoint(25F, -12F, 79F);
		baseModel[119].rotateAngleY = 0.17453293F;

		baseModel[120].addShapeBox(0F, 0F, 0F, 15, 5, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 123
		baseModel[120].setRotationPoint(58F, -1F, 5F);
		baseModel[120].rotateAngleY = 0.20943951F;

		baseModel[121].addShapeBox(0F, 0F, 0F, 12, 8, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 124
		baseModel[121].setRotationPoint(41F, -4F, 11F);
		baseModel[121].rotateAngleY = -1.13446401F;

		baseModel[122].addShapeBox(1F, 0F, 1F, 12, 10, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 124
		baseModel[122].setRotationPoint(31F, -6F, 18F);
		baseModel[122].rotateAngleY = -1.97222205F;

		baseModel[123].addShapeBox(-2F, 0F, 2F, 10, 8, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 124
		baseModel[123].setRotationPoint(43F, -12F, 17F);
		baseModel[123].rotateAngleY = -2.60054059F;
		baseModel[123].rotateAngleZ = 0.2443461F;

		baseModel[124].addShapeBox(0F, 0F, 0F, 15, 5, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 123
		baseModel[124].setRotationPoint(85F, -1F, 76F);
		baseModel[124].rotateAngleY = -1.76278254F;

		baseModel[125].addShapeBox(0F, 0F, 0F, 16, 16, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 123
		baseModel[125].setRotationPoint(83F, -12F, 42F);
		baseModel[125].rotateAngleY = 0.17453293F;

		baseModel[126].addShapeBox(0F, 0F, 0F, 12, 6, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 124
		baseModel[126].setRotationPoint(82F, -2F, 42F);
		baseModel[126].rotateAngleY = -1.6406095F;

		baseModel[127].addShapeBox(0F, 0F, 0F, 12, 6, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 124
		baseModel[127].setRotationPoint(82F, -8F, 42F);
		baseModel[127].rotateAngleY = -1.90240888F;

		baseModel[128].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // BASE34
		baseModel[128].setRotationPoint(45.5F, -7F, 64F);

		baseModel[129].setFlipped(true);
		baseModel[129].addShapeBox(0F, 0F, 0F, 48, 48, 8, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F); // BASE01
		baseModel[129].setRotationPoint(48F, 12F, 0F);
		baseModel[129].rotateAngleX = 1.57079633F;

		baseModel[130].setFlipped(true);
		baseModel[130].addShapeBox(0F, 0F, 0F, 48, 48, 8, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F, 0F, -48F, 0F); // BASE01
		baseModel[130].setRotationPoint(0F, 12F, 48F);
		baseModel[130].rotateAngleX = 1.57079633F;

		baseModel[131].addShapeBox(0F, 0F, 0F, 48, 48, 8, 0F, -48F, -48F, 0F, -48F, -48F, 0F, -48F, -48F, 0F, -48F, -48F, 0F, -48F, -48F, 0F, -48F, -48F, 0F, -48F, -48F, 0F, -48F, -48F, 0F); // BASE01
		baseModel[131].setRotationPoint(48F, 12F, 48F);
		baseModel[131].rotateAngleX = 1.57079633F;

		baseModel[132].setFlipped(true);
		baseModel[132].addShapeBox(0F, 0F, 0F, 32, 32, 8, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F); // BASE02
		baseModel[132].setRotationPoint(49F, 4F, 16F);
		baseModel[132].rotateAngleX = 1.57079633F;

		baseModel[133].setFlipped(true);
		baseModel[133].addShapeBox(0F, 0F, 0F, 32, 32, 8, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F); // BASE02
		baseModel[133].setRotationPoint(17F, 4F, 48F);
		baseModel[133].rotateAngleX = 1.57079633F;

		baseModel[134].addShapeBox(0F, 0F, 0F, 32, 32, 8, 0F, -32F, -32F, 0F, -32F, -32F, 0F, -32F, -32F, 0F, -32F, -32F, 0F, -32F, -32F, 0F, -32F, -32F, 0F, -32F, -32F, 0F, -32F, -32F, 0F); // BASE02
		baseModel[134].setRotationPoint(49F, 4F, 48F);
		baseModel[134].rotateAngleX = 1.57079633F;

		baseModel[135].addShapeBox(0F, 0F, 0F, 3, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE35
		baseModel[135].setRotationPoint(86.5F, -8F, 80F);
		baseModel[135].rotateAngleX = 1.57079633F;

		baseModel[136].addShapeBox(0F, 0F, 0F, 3, 9, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE45
		baseModel[136].setRotationPoint(45.5F, -4F, 77F);
		baseModel[136].rotateAngleZ = 1.57079633F;

		baseModel[137].addShapeBox(0F, 0F, 0F, 3, 9, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE45
		baseModel[137].setRotationPoint(51.5F, -8F, 82F);
		baseModel[137].rotateAngleZ = 1.57079633F;

		baseModel[138].addShapeBox(0F, 0F, 0F, 3, 8, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 1F, 0F, 0F, 1F); // BASE43
		baseModel[138].setRotationPoint(78.5F, -4F, 76F);
		baseModel[138].rotateAngleZ = 1.57079633F;

		baseModel[139].addBox(0F, 0F, 0F, 5, 5, 1, 0F); // BASE30
		baseModel[139].setRotationPoint(33.5F, -10F, 53.5F);
		baseModel[139].rotateAngleX = 1.57079633F;

		baseModel[140].addBox(0F, 0F, 0F, 5, 5, 1, 0F); // BASE31
		baseModel[140].setRotationPoint(39.5F, -10F, 53.5F);
		baseModel[140].rotateAngleX = 1.57079633F;

		baseModel[141].addShapeBox(0F, 0F, 0F, 3, 10, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 1F, 0F, 0F); // BASE44
		baseModel[141].setRotationPoint(35.5F, -20F, 53F);
		baseModel[141].rotateAngleX = 0.19198622F;

		baseModel[142].addShapeBox(0F, 0F, -3F, 3, 15, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE44
		baseModel[142].setRotationPoint(35.5F, -35F, 54.5F);
		baseModel[142].rotateAngleX = 0.10471976F;

		baseModel[143].addShapeBox(0F, 0F, -5F, 3, 9, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F, -1F, 0F, 0F); // BASE44
		baseModel[143].setRotationPoint(34.5F, -44F, 54.5F);
		baseModel[143].rotateAngleX = 0.19198622F;

		baseModel[144].addShapeBox(0F, -1F, -3F, 3, 10, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE44
		baseModel[144].setRotationPoint(34.5F, -52F, 49.25F);
		baseModel[144].rotateAngleX = 0.34906585F;

		baseModel[145].addShapeBox(0F, 0F, 0F, 3, 10, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 1F, 0F, 0F); // BASE44
		baseModel[145].setRotationPoint(40.5F, -20F, 53F);
		baseModel[145].rotateAngleX = 0.19198622F;

		baseModel[146].addShapeBox(0F, 0F, -3F, 3, 15, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE44
		baseModel[146].setRotationPoint(40.5F, -35F, 54.5F);
		baseModel[146].rotateAngleX = 0.10471976F;

		baseModel[147].addShapeBox(0F, 0F, -5F, 3, 9, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F, -1F, 0F, 0F); // BASE44
		baseModel[147].setRotationPoint(39.5F, -44F, 54.5F);
		baseModel[147].rotateAngleX = 0.19198622F;

		baseModel[148].addShapeBox(0F, -1F, -3F, 3, 10, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE44
		baseModel[148].setRotationPoint(39.5F, -52F, 49.25F);
		baseModel[148].rotateAngleX = 0.34906585F;

		baseModel[149].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(15, 0, 15, 0), new Coord2D(15, 4, 15, 4), new Coord2D(6, 4, 6, 4) }), 12, 15, 4, 36, 12, ModelRendererTurbo.MR_FRONT, new float[] {8 ,9 ,4 ,15}); // TableKeyboardMain
		baseModel[149].setRotationPoint(-15.5F, -2F, 67F);
		baseModel[149].rotateAngleY = -3.14159265F;


		radarModel = new ModelRendererTurbo[59];
		radarModel[0] = new ModelRendererTurbo(this, 0, 161, textureX, textureY); // DISHROTATe01
		radarModel[1] = new ModelRendererTurbo(this, 0, 128, textureX, textureY); // DISHROTATe02
		radarModel[2] = new ModelRendererTurbo(this, 83, 128, textureX, textureY); // DISHROTATe05
		radarModel[3] = new ModelRendererTurbo(this, 144, 50, textureX, textureY); // DISHROTATe06
		radarModel[4] = new ModelRendererTurbo(this, 150, 0, textureX, textureY); // DISHROTATe08
		radarModel[5] = new ModelRendererTurbo(this, 150, 0, textureX, textureY); // DISHROTATe09
		radarModel[6] = new ModelRendererTurbo(this, 150, 0, textureX, textureY); // DISHROTATe10
		radarModel[7] = new ModelRendererTurbo(this, 120, 13, textureX, textureY); // DISHROTATe12
		radarModel[8] = new ModelRendererTurbo(this, 57, 48, textureX, textureY); // DISHROTATe13
		radarModel[9] = new ModelRendererTurbo(this, 112, 55, textureX, textureY); // DISHROTATe14
		radarModel[10] = new ModelRendererTurbo(this, 172, 0, textureX, textureY); // DISHROTATe15
		radarModel[11] = new ModelRendererTurbo(this, 171, 1, textureX, textureY); // DISHROTATe16
		radarModel[12] = new ModelRendererTurbo(this, 171, 0, textureX, textureY); // DISHROTATe17
		radarModel[13] = new ModelRendererTurbo(this, 171, 0, textureX, textureY); // DISHROTATe18
		radarModel[14] = new ModelRendererTurbo(this, 172, 0, textureX, textureY); // DISHROTATe19
		radarModel[15] = new ModelRendererTurbo(this, 171, 0, textureX, textureY); // DISHROTATe20
		radarModel[16] = new ModelRendererTurbo(this, 52, 58, textureX, textureY); // DISHROTATe23
		radarModel[17] = new ModelRendererTurbo(this, 203, 128, textureX, textureY); // DISHROTATe24
		radarModel[18] = new ModelRendererTurbo(this, 248, 36, textureX, textureY); // DISHROTATe15
		radarModel[19] = new ModelRendererTurbo(this, 236, 35, textureX, textureY); // DISHROTATe16
		radarModel[20] = new ModelRendererTurbo(this, 236, 35, textureX, textureY); // DISHROTATe17
		radarModel[21] = new ModelRendererTurbo(this, 236, 35, textureX, textureY); // DISHROTATe18
		radarModel[22] = new ModelRendererTurbo(this, 248, 36, textureX, textureY); // DISHROTATe19
		radarModel[23] = new ModelRendererTurbo(this, 236, 35, textureX, textureY); // DISHROTATe20
		radarModel[24] = new ModelRendererTurbo(this, 248, 36, textureX, textureY); // DISHROTATe15
		radarModel[25] = new ModelRendererTurbo(this, 236, 35, textureX, textureY); // DISHROTATe16
		radarModel[26] = new ModelRendererTurbo(this, 236, 35, textureX, textureY); // DISHROTATe17
		radarModel[27] = new ModelRendererTurbo(this, 236, 35, textureX, textureY); // DISHROTATe18
		radarModel[28] = new ModelRendererTurbo(this, 248, 36, textureX, textureY); // DISHROTATe19
		radarModel[29] = new ModelRendererTurbo(this, 236, 35, textureX, textureY); // DISHROTATe20
		radarModel[30] = new ModelRendererTurbo(this, 248, 36, textureX, textureY); // DISHROTATe15
		radarModel[31] = new ModelRendererTurbo(this, 236, 35, textureX, textureY); // DISHROTATe16
		radarModel[32] = new ModelRendererTurbo(this, 236, 35, textureX, textureY); // DISHROTATe17
		radarModel[33] = new ModelRendererTurbo(this, 126, 42, textureX, textureY); // DISHROTATe10
		radarModel[34] = new ModelRendererTurbo(this, 124, 46, textureX, textureY); // DISHROTATe16
		radarModel[35] = new ModelRendererTurbo(this, 124, 46, textureX, textureY); // DISHROTATe20
		radarModel[36] = new ModelRendererTurbo(this, 126, 42, textureX, textureY); // DISHROTATe09
		radarModel[37] = new ModelRendererTurbo(this, 124, 46, textureX, textureY); // DISHROTATe17
		radarModel[38] = new ModelRendererTurbo(this, 124, 46, textureX, textureY); // DISHROTATe18
		radarModel[39] = new ModelRendererTurbo(this, 126, 42, textureX, textureY); // DISHROTATe09
		radarModel[40] = new ModelRendererTurbo(this, 124, 46, textureX, textureY); // DISHROTATe17
		radarModel[41] = new ModelRendererTurbo(this, 124, 46, textureX, textureY); // DISHROTATe18
		radarModel[42] = new ModelRendererTurbo(this, 126, 42, textureX, textureY); // DISHROTATe09
		radarModel[43] = new ModelRendererTurbo(this, 124, 46, textureX, textureY); // DISHROTATe17
		radarModel[44] = new ModelRendererTurbo(this, 124, 46, textureX, textureY); // DISHROTATe18
		radarModel[45] = new ModelRendererTurbo(this, 126, 42, textureX, textureY); // DISHROTATe09
		radarModel[46] = new ModelRendererTurbo(this, 124, 46, textureX, textureY); // DISHROTATe17
		radarModel[47] = new ModelRendererTurbo(this, 124, 46, textureX, textureY); // DISHROTATe18
		radarModel[48] = new ModelRendererTurbo(this, 52, 58, textureX, textureY); // DISHROTATe23
		radarModel[49] = new ModelRendererTurbo(this, 52, 58, textureX, textureY); // DISHROTATe23
		radarModel[50] = new ModelRendererTurbo(this, 52, 58, textureX, textureY); // DISHROTATe23
		radarModel[51] = new ModelRendererTurbo(this, 28, 102, textureX, textureY); // DISHROTATe23
		radarModel[52] = new ModelRendererTurbo(this, 28, 102, textureX, textureY); // DISHROTATe23
		radarModel[53] = new ModelRendererTurbo(this, 24, 61, textureX, textureY); // DISHROTATe24
		radarModel[54] = new ModelRendererTurbo(this, 203, 128, textureX, textureY); // DISHROTATe24
		radarModel[55] = new ModelRendererTurbo(this, 120, 13, textureX, textureY); // DISHROTATe12
		radarModel[56] = new ModelRendererTurbo(this, 120, 13, textureX, textureY); // DISHROTATe12
		radarModel[57] = new ModelRendererTurbo(this, 150, 0, textureX, textureY); // DISHROTATe08
		radarModel[58] = new ModelRendererTurbo(this, 40, 189, textureX, textureY); // DISHROTATe01

		radarModel[0].addShapeBox(0F, 0F, 0F, 25, 3, 25, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe01
		radarModel[0].setRotationPoint(36F, -75F, 35F);

		radarModel[1].addShapeBox(0F, 0F, 0F, 29, 8, 25, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe02
		radarModel[1].setRotationPoint(34F, -83F, 37F);

		radarModel[2].addShapeBox(0F, -2F, -2F, 17, 8, 9, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe05
		radarModel[2].setRotationPoint(40.01F, -82.8F, 18.5F);
		radarModel[2].rotateAngleX = -0.31415927F;

		radarModel[3].addShapeBox(0F, 0F, 0F, 17, 8, 14, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe06
		radarModel[3].setRotationPoint(40F, -83F, 23F);

		radarModel[4].addShapeBox(0F, 0F, 0F, 53, 22, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe08
		radarModel[4].setRotationPoint(22F, -142F, 60F);

		radarModel[5].addShapeBox(0F, 0F, 0F, 53, 25, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe09
		radarModel[5].setRotationPoint(22F, -100F, 60F);
		radarModel[5].rotateAngleX = -0.29670597F;

		radarModel[6].addShapeBox(0F, -24F, 0F, 53, 25, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe10
		radarModel[6].setRotationPoint(22F, -142F, 60F);
		radarModel[6].rotateAngleX = 0.29670597F;

		radarModel[7].addBox(0F, 0F, 0F, 5, 24, 5, 0F); // DISHROTATe12
		radarModel[7].setRotationPoint(46F, -149F, 60F);

		radarModel[8].addShapeBox(0F, 0F, 0F, 5, 1, 5, 0F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe13
		radarModel[8].setRotationPoint(46F, -150F, 60F);

		radarModel[9].addShapeBox(0F, 0F, 0F, 3, 14, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe14
		radarModel[9].setRotationPoint(47F, -164F, 61F);

		radarModel[10].addShapeBox(-1F, 0F, 0F, 32, 42, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe15
		radarModel[10].setRotationPoint(75F, -142F, 60F);
		radarModel[10].rotateAngleY = -0.2443461F;

		radarModel[11].addShapeBox(-1F, 0F, -7F, 32, 25, 0, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe16
		radarModel[11].setRotationPoint(75F, -167F, 60F);
		radarModel[11].rotateAngleX = 0.27925268F;
		radarModel[11].rotateAngleY = -0.2443461F;

		radarModel[12].addShapeBox(-1F, 0F, 0F, 32, 25, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F); // DISHROTATe17
		radarModel[12].setRotationPoint(75F, -100F, 60F);
		radarModel[12].rotateAngleX = -0.29670597F;
		radarModel[12].rotateAngleY = -0.2443461F;

		radarModel[13].addShapeBox(1F, 0F, 0F, 32, 25, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe18
		radarModel[13].setRotationPoint(-9F, -100F, 52F);
		radarModel[13].rotateAngleX = -0.29670597F;
		radarModel[13].rotateAngleY = 0.2443461F;

		radarModel[14].addShapeBox(1F, 0F, 0F, 32, 42, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe19
		radarModel[14].setRotationPoint(-9F, -142F, 52F);
		radarModel[14].rotateAngleY = 0.2443461F;

		radarModel[15].addShapeBox(1F, 0F, -7F, 32, 25, 0, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe20
		radarModel[15].setRotationPoint(-9F, -167F, 52F);
		radarModel[15].rotateAngleX = 0.27925268F;
		radarModel[15].rotateAngleY = 0.2443461F;

		radarModel[16].addShapeBox(0F, 0F, -6F, 6, 8, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe23
		radarModel[16].setRotationPoint(41F, -87F, 10F);
		radarModel[16].rotateAngleX = 1.23918377F;

		radarModel[17].addTrapezoid(0F, 0F, 0F, 8, 1, 8, 0F, -1.00F, ModelRendererTurbo.MR_TOP); // DISHROTATe24
		radarModel[17].setRotationPoint(45F, -100.5F, -23F);

		radarModel[18].addShapeBox(-1F, 0F, 0F, 2, 42, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe15
		radarModel[18].setRotationPoint(75F, -142F, 59F);
		radarModel[18].rotateAngleY = -0.2443461F;

		radarModel[19].addBox(-1F, 0F, -7F, 2, 25, 2, 0F); // DISHROTATe16
		radarModel[19].setRotationPoint(75F, -167F, 59F);
		radarModel[19].rotateAngleX = 0.27925268F;
		radarModel[19].rotateAngleY = -0.2443461F;

		radarModel[20].addBox(-1F, 0F, 0F, 2, 25, 2, 0F); // DISHROTATe17
		radarModel[20].setRotationPoint(75F, -100F, 59F);
		radarModel[20].rotateAngleX = -0.29670597F;
		radarModel[20].rotateAngleY = -0.2443461F;

		radarModel[21].addBox(1F, 0F, 0F, 2, 25, 2, 0F); // DISHROTATe18
		radarModel[21].setRotationPoint(21F, -100F, 58F);
		radarModel[21].rotateAngleX = -0.29670597F;
		radarModel[21].rotateAngleY = 0.2443461F;

		radarModel[22].addBox(1F, 0F, 0F, 2, 42, 2, 0F); // DISHROTATe19
		radarModel[22].setRotationPoint(21F, -142F, 58F);
		radarModel[22].rotateAngleY = 0.2443461F;

		radarModel[23].addBox(1F, 0F, -7F, 2, 25, 2, 0F); // DISHROTATe20
		radarModel[23].setRotationPoint(21F, -167F, 58F);
		radarModel[23].rotateAngleX = 0.27925268F;
		radarModel[23].rotateAngleY = 0.2443461F;

		radarModel[24].addShapeBox(-1F, 0F, 0F, 2, 42, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe15
		radarModel[24].setRotationPoint(48F, -142F, 59F);

		radarModel[25].addBox(-1F, 0F, -7F, 2, 25, 2, 0F); // DISHROTATe16
		radarModel[25].setRotationPoint(48F, -167F, 59F);
		radarModel[25].rotateAngleX = 0.27925268F;

		radarModel[26].addBox(-1F, 0F, 0F, 2, 25, 2, 0F); // DISHROTATe17
		radarModel[26].setRotationPoint(48F, -100F, 59F);
		radarModel[26].rotateAngleX = -0.29670597F;

		radarModel[27].addBox(-30F, 0F, 0F, 2, 25, 2, 0F); // DISHROTATe18
		radarModel[27].setRotationPoint(21F, -100F, 58F);
		radarModel[27].rotateAngleX = -0.29670597F;
		radarModel[27].rotateAngleY = 0.2443461F;

		radarModel[28].addBox(-30F, 0F, 0F, 2, 42, 2, 0F); // DISHROTATe19
		radarModel[28].setRotationPoint(21F, -142F, 58F);
		radarModel[28].rotateAngleY = 0.2443461F;

		radarModel[29].addBox(-30F, 0F, -7F, 2, 25, 2, 0F); // DISHROTATe20
		radarModel[29].setRotationPoint(21F, -167F, 58F);
		radarModel[29].rotateAngleX = 0.27925268F;
		radarModel[29].rotateAngleY = 0.2443461F;

		radarModel[30].addShapeBox(30F, 0F, 0F, 2, 42, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe15
		radarModel[30].setRotationPoint(75F, -142F, 59F);
		radarModel[30].rotateAngleY = -0.2443461F;

		radarModel[31].addBox(30F, 0F, -7F, 2, 25, 2, 0F); // DISHROTATe16
		radarModel[31].setRotationPoint(75F, -167F, 59F);
		radarModel[31].rotateAngleX = 0.27925268F;
		radarModel[31].rotateAngleY = -0.2443461F;

		radarModel[32].addBox(30F, 0F, 0F, 2, 25, 2, 0F); // DISHROTATe17
		radarModel[32].setRotationPoint(75F, -100F, 59F);
		radarModel[32].rotateAngleX = -0.29670597F;
		radarModel[32].rotateAngleY = -0.2443461F;

		radarModel[33].addShapeBox(0F, -24F, 0F, 53, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe10
		radarModel[33].setRotationPoint(22F, -142F, 59F);
		radarModel[33].rotateAngleX = 0.29670597F;

		radarModel[34].addShapeBox(-1F, 0F, -7F, 32, 2, 2, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe16
		radarModel[34].setRotationPoint(75F, -167F, 59F);
		radarModel[34].rotateAngleX = 0.27925268F;
		radarModel[34].rotateAngleY = -0.2443461F;

		radarModel[35].addShapeBox(1F, 0F, -7F, 32, 2, 2, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe20
		radarModel[35].setRotationPoint(-9F, -167F, 51F);
		radarModel[35].rotateAngleX = 0.27925268F;
		radarModel[35].rotateAngleY = 0.2443461F;

		radarModel[36].addShapeBox(0F, 24F, 0F, 53, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe09
		radarModel[36].setRotationPoint(22F, -100F, 59F);
		radarModel[36].rotateAngleX = -0.29670597F;

		radarModel[37].addShapeBox(-1F, 24F, 0F, 32, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F); // DISHROTATe17
		radarModel[37].setRotationPoint(75F, -100F, 59F);
		radarModel[37].rotateAngleX = -0.29670597F;
		radarModel[37].rotateAngleY = -0.2443461F;

		radarModel[38].addShapeBox(1F, 24F, 0F, 32, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe18
		radarModel[38].setRotationPoint(-9F, -100F, 51F);
		radarModel[38].rotateAngleX = -0.29670597F;
		radarModel[38].rotateAngleY = 0.2443461F;

		radarModel[39].addShapeBox(0F, -2F, -1F, 53, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe09
		radarModel[39].setRotationPoint(22F, -100F, 59F);
		radarModel[39].rotateAngleX = -0.29670597F;

		radarModel[40].addShapeBox(-1F, -2F, -1F, 32, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F); // DISHROTATe17
		radarModel[40].setRotationPoint(75F, -100F, 59F);
		radarModel[40].rotateAngleX = -0.29670597F;
		radarModel[40].rotateAngleY = -0.2443461F;

		radarModel[41].addShapeBox(1F, -2F, -1F, 32, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe18
		radarModel[41].setRotationPoint(-9F, -100F, 51F);
		radarModel[41].rotateAngleX = -0.29670597F;
		radarModel[41].rotateAngleY = 0.2443461F;

		radarModel[42].addShapeBox(0F, -2F, -1F, 53, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe09
		radarModel[42].setRotationPoint(22F, -120F, 59F);
		radarModel[42].rotateAngleX = -0.29670597F;

		radarModel[43].addShapeBox(-1F, -2F, -1F, 32, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F); // DISHROTATe17
		radarModel[43].setRotationPoint(75F, -120F, 59F);
		radarModel[43].rotateAngleX = -0.29670597F;
		radarModel[43].rotateAngleY = -0.2443461F;

		radarModel[44].addShapeBox(1F, -2F, -1F, 32, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe18
		radarModel[44].setRotationPoint(-9F, -120F, 51F);
		radarModel[44].rotateAngleX = -0.29670597F;
		radarModel[44].rotateAngleY = 0.2443461F;

		radarModel[45].addShapeBox(0F, -2F, -1F, 53, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe09
		radarModel[45].setRotationPoint(22F, -141F, 59F);
		radarModel[45].rotateAngleX = -0.29670597F;

		radarModel[46].addShapeBox(-1F, -2F, -1F, 32, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F); // DISHROTATe17
		radarModel[46].setRotationPoint(75F, -141F, 59F);
		radarModel[46].rotateAngleX = -0.29670597F;
		radarModel[46].rotateAngleY = -0.2443461F;

		radarModel[47].addShapeBox(1F, -2F, -1F, 32, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe18
		radarModel[47].setRotationPoint(-9F, -141F, 51F);
		radarModel[47].rotateAngleX = -0.29670597F;
		radarModel[47].rotateAngleY = 0.2443461F;

		radarModel[48].addShapeBox(0F, 0F, -6F, 6, 8, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe23
		radarModel[48].setRotationPoint(50F, -87F, 10F);
		radarModel[48].rotateAngleX = 1.23918377F;

		radarModel[49].addShapeBox(0F, 0F, -6F, 6, 8, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe23
		radarModel[49].setRotationPoint(41F, -97F, -18F);
		radarModel[49].rotateAngleX = 1.23918377F;

		radarModel[50].addShapeBox(0F, 0F, -6F, 6, 8, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe23
		radarModel[50].setRotationPoint(50F, -97F, -18F);
		radarModel[50].rotateAngleX = 1.23918377F;
		radarModel[50].rotateAngleZ = 0.03490659F;

		radarModel[51].addShapeBox(0F, 0F, -4.75F, 4, 22, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe23
		radarModel[51].setRotationPoint(42F, -94F, -11F);
		radarModel[51].rotateAngleX = 1.23918377F;

		radarModel[52].addShapeBox(0F, 0F, -4.75F, 4, 22, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe23
		radarModel[52].setRotationPoint(51F, -94F, -11F);
		radarModel[52].rotateAngleX = 1.23918377F;

		radarModel[53].addShapeBox(0F, 0F, 0F, 6, 8, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe24
		radarModel[53].setRotationPoint(45F, -93.5F, -23F);
		radarModel[53].rotateAngleZ = 1.57079633F;

		radarModel[54].addTrapezoid(0F, 0F, 0F, 8, 1, 8, 0F, -1.00F, ModelRendererTurbo.MR_BOTTOM); // DISHROTATe24
		radarModel[54].setRotationPoint(45F, -93.5F, -23F);

		radarModel[55].addBox(0F, 0F, 0F, 5, 24, 5, 0F); // DISHROTATe12
		radarModel[55].setRotationPoint(46F, -125F, 60F);

		radarModel[56].addBox(0F, 0F, 0.5F, 5, 24, 5, 0F); // DISHROTATe12
		radarModel[56].setRotationPoint(46F, -102F, 60F);
		radarModel[56].rotateAngleX = -0.2443461F;

		radarModel[57].addShapeBox(0F, 21F, 0F, 53, 20, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe08
		radarModel[57].setRotationPoint(22F, -142F, 60F);

		radarModel[58].addShapeBox(0F, 0F, 0F, 16, 2, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DISHROTATe01
		radarModel[58].setRotationPoint(40F, -72F, 40F);


		triangulatorsModel = new ModelRendererTurbo[37];
		triangulatorsModel[0] = new ModelRendererTurbo(this, 213, 146, textureX, textureY); // Radio23
		triangulatorsModel[1] = new ModelRendererTurbo(this, 144, 72, textureX, textureY); // Radio24
		triangulatorsModel[2] = new ModelRendererTurbo(this, 244, 39, textureX, textureY); // Radio25
		triangulatorsModel[3] = new ModelRendererTurbo(this, 213, 146, textureX, textureY); // Radio23
		triangulatorsModel[4] = new ModelRendererTurbo(this, 144, 72, textureX, textureY); // Radio24
		triangulatorsModel[5] = new ModelRendererTurbo(this, 244, 39, textureX, textureY); // Radio25
		triangulatorsModel[6] = new ModelRendererTurbo(this, 213, 146, textureX, textureY); // Radio23
		triangulatorsModel[7] = new ModelRendererTurbo(this, 144, 72, textureX, textureY); // Radio24
		triangulatorsModel[8] = new ModelRendererTurbo(this, 244, 39, textureX, textureY); // Radio25
		triangulatorsModel[9] = new ModelRendererTurbo(this, 37, 14, textureX, textureY); // Radio23
		triangulatorsModel[10] = new ModelRendererTurbo(this, 33, 6, textureX, textureY); // Radio23
		triangulatorsModel[11] = new ModelRendererTurbo(this, 48, 15, textureX, textureY); // Radio23
		triangulatorsModel[12] = new ModelRendererTurbo(this, 36, 2, textureX, textureY); // Radio23
		triangulatorsModel[13] = new ModelRendererTurbo(this, 37, 34, textureX, textureY); // Radio23
		triangulatorsModel[14] = new ModelRendererTurbo(this, 48, 15, textureX, textureY); // Radio23
		triangulatorsModel[15] = new ModelRendererTurbo(this, 36, 2, textureX, textureY); // Radio23
		triangulatorsModel[16] = new ModelRendererTurbo(this, 19, 4, textureX, textureY); // Radio23
		triangulatorsModel[17] = new ModelRendererTurbo(this, 25, 10, textureX, textureY); // Radio23
		triangulatorsModel[18] = new ModelRendererTurbo(this, 25, 6, textureX, textureY); // Radio23
		triangulatorsModel[19] = new ModelRendererTurbo(this, 33, 6, textureX, textureY); // Radio23
		triangulatorsModel[20] = new ModelRendererTurbo(this, 72, 48, textureX, textureY); // Radio23
		triangulatorsModel[21] = new ModelRendererTurbo(this, 37, 14, textureX, textureY); // Radio23
		triangulatorsModel[22] = new ModelRendererTurbo(this, 37, 14, textureX, textureY); // Radio23
		triangulatorsModel[23] = new ModelRendererTurbo(this, 5, 32, textureX, textureY); // Radio23
		triangulatorsModel[24] = new ModelRendererTurbo(this, 28, 2, textureX, textureY); // Radio23
		triangulatorsModel[25] = new ModelRendererTurbo(this, 48, 15, textureX, textureY); // Radio23
		triangulatorsModel[26] = new ModelRendererTurbo(this, 36, 2, textureX, textureY); // Radio23
		triangulatorsModel[27] = new ModelRendererTurbo(this, 20, 0, textureX, textureY); // Radio23
		triangulatorsModel[28] = new ModelRendererTurbo(this, 37, 14, textureX, textureY); // Radio23
		triangulatorsModel[29] = new ModelRendererTurbo(this, 37, 14, textureX, textureY); // Radio23
		triangulatorsModel[30] = new ModelRendererTurbo(this, 33, 6, textureX, textureY); // Radio23
		triangulatorsModel[31] = new ModelRendererTurbo(this, 37, 14, textureX, textureY); // Radio23
		triangulatorsModel[32] = new ModelRendererTurbo(this, 36, 2, textureX, textureY); // Radio23
		triangulatorsModel[33] = new ModelRendererTurbo(this, 28, 2, textureX, textureY); // Radio23
		triangulatorsModel[34] = new ModelRendererTurbo(this, 48, 15, textureX, textureY); // Radio23
		triangulatorsModel[35] = new ModelRendererTurbo(this, 36, 2, textureX, textureY); // Radio23
		triangulatorsModel[36] = new ModelRendererTurbo(this, 36, 2, textureX, textureY); // Radio23

		triangulatorsModel[0].addBox(0F, 0F, 0F, 7, 4, 6, 0F); // Radio23
		triangulatorsModel[0].setRotationPoint(6F, -5F, 85F);

		triangulatorsModel[1].addBox(0F, 0F, 0F, 2, 3, 2, 0F); // Radio24
		triangulatorsModel[1].setRotationPoint(7F, -8F, 88F);

		triangulatorsModel[2].addBox(0F, 0F, 0F, 1, 22, 1, 0F); // Radio25
		triangulatorsModel[2].setRotationPoint(7.5F, -30F, 88.5F);

		triangulatorsModel[3].addBox(0F, 0F, 0F, 7, 4, 6, 0F); // Radio23
		triangulatorsModel[3].setRotationPoint(61F, -5F, 8F);

		triangulatorsModel[4].addBox(0F, 0F, 0F, 2, 3, 2, 0F); // Radio24
		triangulatorsModel[4].setRotationPoint(62F, -8F, 11F);

		triangulatorsModel[5].addBox(0F, 0F, 0F, 1, 22, 1, 0F); // Radio25
		triangulatorsModel[5].setRotationPoint(62.5F, -30F, 11.5F);

		triangulatorsModel[6].addBox(0F, 0F, 0F, 7, 4, 6, 0F); // Radio23
		triangulatorsModel[6].setRotationPoint(84F, -5F, 66F);

		triangulatorsModel[7].addBox(0F, 0F, 0F, 2, 3, 2, 0F); // Radio24
		triangulatorsModel[7].setRotationPoint(85F, -8F, 67F);

		triangulatorsModel[8].addBox(0F, 0F, 0F, 1, 22, 1, 0F); // Radio25
		triangulatorsModel[8].setRotationPoint(85.5F, -30F, 67.5F);

		triangulatorsModel[9].addBox(1F, 0.5F, -1F, 3, 3, 1, 0F); // Radio23
		triangulatorsModel[9].setRotationPoint(61F, -5F, 8F);

		triangulatorsModel[10].addShapeBox(1.5F, 1F, -2F, 2, 2, 1, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F); // Radio23
		triangulatorsModel[10].setRotationPoint(61F, -5F, 8F);

		triangulatorsModel[11].addBox(1.5F, 3F, -4F, 2, 4, 2, 0F); // Radio23
		triangulatorsModel[11].setRotationPoint(61F, -5F, 8F);

		triangulatorsModel[12].addShapeBox(1.5F, 7F, -4F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F); // Radio23
		triangulatorsModel[12].setRotationPoint(61F, -5F, 8F);

		triangulatorsModel[13].addBox(0F, 1F, 0F, 2, 2, 7, 0F); // Radio23
		triangulatorsModel[13].setRotationPoint(75F, -4F, 8F);

		triangulatorsModel[14].addBox(0F, 3F, -2F, 2, 3, 2, 0F); // Radio23
		triangulatorsModel[14].setRotationPoint(75F, -4F, 8F);

		triangulatorsModel[15].addShapeBox(0F, 7F, -2F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F); // Radio23
		triangulatorsModel[15].setRotationPoint(75F, -5F, 8F);

		triangulatorsModel[16].addShapeBox(1.5F, 7F, -7F, 2, 2, 2, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio23
		triangulatorsModel[16].setRotationPoint(61F, -5F, 8F);

		triangulatorsModel[17].addShapeBox(0F, 7F, -6F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio23
		triangulatorsModel[17].setRotationPoint(75F, -5F, 8F);

		triangulatorsModel[18].addBox(0F, 7F, -4F, 2, 2, 2, 0F); // Radio23
		triangulatorsModel[18].setRotationPoint(75F, -5F, 8F);

		triangulatorsModel[19].addBox(1.5F, 7F, -5F, 2, 2, 1, 0F); // Radio23
		triangulatorsModel[19].setRotationPoint(61F, -5F, 8F);

		triangulatorsModel[20].addShapeBox(3.5F, 7F, -7F, 11, 2, 2, 0F, 0F, 0F, 0F, -0.5F, 0F, -1F, -0.5F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, -1F, -0.5F, 0F, 1F, 0F, 0F, 0F); // Radio23
		triangulatorsModel[20].setRotationPoint(61F, -5F, 8F);

		triangulatorsModel[21].addBox(0F, 0F, 0F, 3, 3, 1, 0F); // Radio23
		triangulatorsModel[21].setRotationPoint(74.5F, -3.5F, 15F);

		triangulatorsModel[22].addBox(0F, 0F, 0F, 3, 3, 1, 0F); // Radio23
		triangulatorsModel[22].setRotationPoint(84.5F, -4.5F, 72F);

		triangulatorsModel[23].addShapeBox(0F, 0F, 0F, 2, 2, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, 1F, 0F); // Radio23
		triangulatorsModel[23].setRotationPoint(85F, -4F, 73F);

		triangulatorsModel[24].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio23
		triangulatorsModel[24].setRotationPoint(85F, -3F, 77F);

		triangulatorsModel[25].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio23
		triangulatorsModel[25].setRotationPoint(85F, -1F, 77F);

		triangulatorsModel[26].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F); // Radio23
		triangulatorsModel[26].setRotationPoint(85F, 1F, 77F);

		triangulatorsModel[27].addShapeBox(0F, 0F, 0F, 3, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio23
		triangulatorsModel[27].setRotationPoint(82F, 1F, 77F);

		triangulatorsModel[28].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio23
		triangulatorsModel[28].setRotationPoint(82F, 0.5F, 76.5F);
		triangulatorsModel[28].rotateAngleY = 1.57079633F;

		triangulatorsModel[29].addBox(1F, 0.5F, -1F, 3, 3, 1, 0F); // Radio23
		triangulatorsModel[29].setRotationPoint(5F, -5F, 85F);

		triangulatorsModel[30].addBox(1.5F, 1F, -2F, 2, 2, 1, 0F); // Radio23
		triangulatorsModel[30].setRotationPoint(5F, -5F, 85F);

		triangulatorsModel[31].addBox(1F, 0.5F, -1F, 3, 3, 1, 0F); // Radio23
		triangulatorsModel[31].setRotationPoint(5F, -12F, 81F);

		triangulatorsModel[32].addShapeBox(1.5F, 1F, -2F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio23
		triangulatorsModel[32].setRotationPoint(5F, -5F, 83F);

		triangulatorsModel[33].addShapeBox(1.5F, 1F, -2F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio23
		triangulatorsModel[33].setRotationPoint(5F, -12F, 83F);

		triangulatorsModel[34].addBox(1.5F, 1F, -2F, 2, 5, 2, 0F); // Radio23
		triangulatorsModel[34].setRotationPoint(5F, -10F, 83F);

		triangulatorsModel[35].addShapeBox(1.5F, 7F, -4F, 2, 2, 2, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -1.5F, 0F, 0F, -1.5F, 0F, 0F, -2F, 0F, 0F, -2F, 0F); // Radio23
		triangulatorsModel[35].setRotationPoint(61F, -11F, 8F);

		triangulatorsModel[36].addShapeBox(0F, 7F, -2F, 2, 2, 2, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -1.5F, 0F, 0F, -1.5F, 0F, 0F, -2F, 0F, 0F, -2F, 0F); // Radio23
		triangulatorsModel[36].setRotationPoint(75F, -10F, 8F);
		
		parts.put("base",baseModel);
		parts.put("radar",radarModel);
		parts.put("triangulators",triangulatorsModel); //for the upgrade
		translateAll(0, -12F, -48F-16f);

		translate(radarModel,-36-12.5f,0,12.5f);

		flipAll();
	}

	public void getBlockRotation(EnumFacing facing, boolean mirrored)
	{
		switch(facing)
		{
			case NORTH:
			{
				if(mirrored)
					GlStateManager.translate(0, 0f, 1f);
			}
			break;
			case SOUTH:
			{
				GlStateManager.translate(1, 0f, 0);
				if(!mirrored)
					GlStateManager.translate(0, 0f, 1);
				GlStateManager.rotate(180F, 0F, 1F, 0F);

			}
			break;
			case EAST:
			{
				GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(0f, 0f, mirrored?0f:-1f);

			}
			break;
			case WEST:
			{
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-1f, 0f, 0f);
				if(mirrored)
					GlStateManager.translate(0, 0f, 1f);
			}
			break;
		}
	}
}
