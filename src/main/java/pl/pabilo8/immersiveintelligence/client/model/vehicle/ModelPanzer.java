package pl.pabilo8.immersiveintelligence.client.model.vehicle;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * @author Pabilo8
 * @since 25.04.2021
 */
public class ModelPanzer extends ModelIIBase
{
	public final ModelRendererTurbo[] bodyDoorOpenModel; //commander hatch
	public final ModelRendererTurbo[] bodyDoorCloseModel; //gunner hatch
	public final ModelRendererTurbo[] turretModel; //turret
	public final ModelRendererTurbo[] barrelModel; //barrel
	public final ModelRendererTurbo[] leftFrontWheelModel; //left wheels
	public final ModelRendererTurbo[] rightFrontWheelModel; //right wheels
	public final ModelRendererTurbo[] leftBackWheelModel; //left engine door
	public final ModelRendererTurbo[] rightBackWheelModel; //right engine door
	public final ModelRendererTurbo[] leftTrackWheelModels; //left back door
	public final ModelRendererTurbo[] rightTrackWheelModels; //right back door
	public final ModelRendererTurbo[] frontWheelModel; //gear shift lever
	public final ModelRendererTurbo[] backWheelModel; //driver pedals
	public final ModelRendererTurbo[] leftTrackModel; //driver hatch
	public final ModelRendererTurbo[] rightTrackModel; //mg turret
	public final ModelRendererTurbo[] trailerModel; //internals
	// TODO: 25.04.2021 check
	public final ModelRendererTurbo[] steeringWheelModel; //something on the turret
	public final ModelRendererTurbo[] drillHeadModel; //turret back door
	int textureX = 256;
	int textureY = 256;

	public ModelPanzer() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[158];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[5] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		baseModel[6] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[7] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[8] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[9] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[10] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[11] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[12] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[13] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[14] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[15] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[16] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[17] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[18] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[19] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[20] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[21] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		baseModel[22] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[23] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[24] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		baseModel[25] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		baseModel[26] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		baseModel[27] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		baseModel[28] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		baseModel[29] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[30] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[31] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[32] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		baseModel[33] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[34] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[35] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[36] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[37] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[38] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[39] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[40] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[41] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[42] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[43] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		baseModel[44] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		baseModel[45] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[46] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		baseModel[47] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[48] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[49] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[50] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[51] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[52] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[53] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[54] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[55] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		baseModel[56] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		baseModel[57] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[58] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[59] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[60] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[61] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[62] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[63] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[64] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[65] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[66] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[67] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[68] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[69] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[70] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[71] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[72] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[73] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[74] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[75] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[76] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		baseModel[77] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		baseModel[78] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		baseModel[79] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		baseModel[80] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		baseModel[81] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[82] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[83] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[84] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[85] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[86] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[87] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		baseModel[88] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		baseModel[89] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		baseModel[90] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		baseModel[91] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		baseModel[92] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		baseModel[93] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		baseModel[94] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[95] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[96] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[97] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[98] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[99] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[100] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[101] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[102] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[103] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[104] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[105] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[106] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[107] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[108] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[109] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[110] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[111] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[112] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[113] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[114] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[115] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[116] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[117] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[118] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[119] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[120] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[121] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[122] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[123] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[124] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[125] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[126] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[127] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[128] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[129] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[130] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[131] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[132] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[133] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[134] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[135] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[136] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[137] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[138] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[139] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[140] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[141] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[142] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[143] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[144] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[145] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[146] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[147] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[148] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[149] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[150] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[151] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[152] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[153] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[154] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[155] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[156] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[157] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0

		baseModel[0].addBox(0F, 0F, 0F, 32, 2, 32, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, -6F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 32, 2, 32, 0F); // Box 0
		baseModel[1].setRotationPoint(0F, -6F, -32F);

		baseModel[2].addBox(0F, 0F, 0F, 32, 2, 32, 0F); // Box 0
		baseModel[2].setRotationPoint(0F, -6F, 32F);

		baseModel[3].addBox(0F, 0F, 0F, 32, 2, 16, 0F); // Box 0
		baseModel[3].setRotationPoint(0F, -6F, -48F);

		baseModel[4].addShapeBox(0F, -2F, -1F, 32, 2, 16, 0F, 0F, -0.5F, 0.5F, 0F, -0.5F, 0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[4].setRotationPoint(0F, -6F, -48F);
		baseModel[4].rotateAngleX = 2.35619449F;

		baseModel[5].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(16, 0, 16, 0), new Coord2D(27, 11, 27, 11), new Coord2D(0, 11, 0, 11) }), 2, 27, 11, 70, 2, ModelRendererTurbo.MR_FRONT, new float[] {11 ,27 ,16 ,16}); // Shape 5
		baseModel[5].setRotationPoint(30F, -6F, -32F);
		baseModel[5].rotateAngleY = 1.57079633F;

		baseModel[6].addBox(0F, 0F, 0F, 32, 13, 2, 0F); // Box 0
		baseModel[6].setRotationPoint(0F, -28F, -60F);

		baseModel[7].addBox(0F, 0F, 0F, 2, 11, 26, 0F); // Box 0
		baseModel[7].setRotationPoint(30F, -28F, -58F);

		baseModel[8].addBox(0F, 0F, 0F, 2, 27, 32, 0F); // Box 0
		baseModel[8].setRotationPoint(30F, -33F, -32F);

		baseModel[9].addBox(0F, 0F, 0F, 2, 33, 32, 0F); // Box 0
		baseModel[9].setRotationPoint(30F, -39F, 0F);

		baseModel[10].addBox(0F, 0F, 0F, 2, 33, 32, 0F); // Box 0
		baseModel[10].setRotationPoint(30F, -39F, 32F);

		baseModel[11].addBox(0F, 0F, 0F, 32, 2, 32, 0F); // Box 0
		baseModel[11].setRotationPoint(-32F, -6F, 0F);

		baseModel[12].addBox(0F, 0F, 0F, 32, 2, 32, 0F); // Box 0
		baseModel[12].setRotationPoint(-32F, -6F, -32F);

		baseModel[13].addBox(0F, 0F, 0F, 32, 2, 32, 0F); // Box 0
		baseModel[13].setRotationPoint(-32F, -6F, 32F);

		baseModel[14].addBox(0F, 0F, 0F, 32, 2, 16, 0F); // Box 0
		baseModel[14].setRotationPoint(-32F, -6F, -48F);

		baseModel[15].addShapeBox(0F, -2F, -1F, 32, 2, 16, 0F, 0F, -0.5F, 0.5F, 0F, -0.5F, 0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[15].setRotationPoint(-32F, -6F, -48F);
		baseModel[15].rotateAngleX = 2.35619449F;

		baseModel[16].addBox(0F, 0F, 0F, 32, 13, 2, 0F); // Box 0
		baseModel[16].setRotationPoint(-32F, -28F, -60F);

		baseModel[17].addBox(0F, 0F, 0F, 2, 11, 26, 0F); // Box 0
		baseModel[17].setRotationPoint(-32F, -28F, -58F);

		baseModel[18].addBox(0F, 0F, 0F, 2, 27, 32, 0F); // Box 0
		baseModel[18].setRotationPoint(-32F, -33F, -32F);

		baseModel[19].addBox(0F, 0F, 0F, 2, 33, 32, 0F); // Box 0
		baseModel[19].setRotationPoint(-32F, -39F, 0F);

		baseModel[20].addBox(0F, 0F, 0F, 2, 33, 32, 0F); // Box 0
		baseModel[20].setRotationPoint(-32F, -39F, 32F);

		baseModel[21].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(16, 0, 16, 0), new Coord2D(27, 11, 27, 11), new Coord2D(0, 11, 0, 11) }), 2, 27, 11, 70, 2, ModelRendererTurbo.MR_FRONT, new float[] {11 ,27 ,16 ,16}); // Shape 5
		baseModel[21].setRotationPoint(-32F, -6F, -32F);
		baseModel[21].rotateAngleY = 1.57079633F;

		baseModel[22].addShapeBox(0F, -2F, -1F, 32, 2, 4, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[22].setRotationPoint(0F, -28F, -59F);
		baseModel[22].rotateAngleX = 0.43633231F;

		baseModel[23].addShapeBox(0F, -2F, -1F, 32, 2, 12, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[23].setRotationPoint(-32F, -28F, -59F);
		baseModel[23].rotateAngleX = 0.43633231F;

		baseModel[24].addBox(0F, 0F, 0F, 16, 1, 6, 0F); // Box 23
		baseModel[24].setRotationPoint(8F, -39F, -54F);

		baseModel[25].addBox(0F, 0F, 0F, 4, 3, 1, 0F); // Box 23
		baseModel[25].setRotationPoint(8F, -36F, -54F);

		baseModel[26].addBox(0F, 0F, 0F, 16, 2, 1, 0F); // Box 23
		baseModel[26].setRotationPoint(8F, -38F, -54F);

		baseModel[27].addBox(0F, 0F, 0F, 4, 3, 1, 0F); // Box 23
		baseModel[27].setRotationPoint(20F, -36F, -54F);

		baseModel[28].addBox(0F, 0F, 0F, 16, 4, 1, 0F); // Box 23
		baseModel[28].setRotationPoint(8F, -33F, -54F);

		baseModel[29].addBox(0F, -2F, 3F, 8, 2, 8, 0F); // Box 0
		baseModel[29].setRotationPoint(0F, -28F, -59F);
		baseModel[29].rotateAngleX = 0.43633231F;

		baseModel[30].addBox(0F, -2F, 3F, 8, 2, 8, 0F); // Box 0
		baseModel[30].setRotationPoint(24F, -28F, -59F);
		baseModel[30].rotateAngleX = 0.43633231F;

		baseModel[31].addBox(0F, -2F, 3F, 16, 2, 4, 0F); // Box 0
		baseModel[31].setRotationPoint(8F, -28F, -59F);
		baseModel[31].rotateAngleX = 0.43633231F;

		baseModel[32].addBox(0F, 0F, 0F, 8, 3, 0, 0F); // Box 23
		baseModel[32].setRotationPoint(12F, -36F, -53.5F);

		baseModel[33].addBox(1F, -3F, 0F, 2, 1, 2, 0F); // Box 0
		baseModel[33].setRotationPoint(-32F, -28F, -59F);
		baseModel[33].rotateAngleX = 0.43633231F;

		baseModel[34].addBox(29F, -3F, 0F, 2, 1, 2, 0F); // Box 0
		baseModel[34].setRotationPoint(-32F, -28F, -59F);
		baseModel[34].rotateAngleX = 0.43633231F;

		baseModel[35].addBox(1F, -3F, 8F, 2, 1, 2, 0F); // Box 0
		baseModel[35].setRotationPoint(-32F, -28F, -59F);
		baseModel[35].rotateAngleX = 0.43633231F;

		baseModel[36].addBox(29F, -3F, 8F, 2, 1, 2, 0F); // Box 0
		baseModel[36].setRotationPoint(-32F, -28F, -59F);
		baseModel[36].rotateAngleX = 0.43633231F;

		baseModel[37].addBox(33F, -3F, 0F, 2, 1, 2, 0F); // Box 0
		baseModel[37].setRotationPoint(-32F, -28F, -59F);
		baseModel[37].rotateAngleX = 0.43633231F;

		baseModel[38].addBox(33F, -3F, 8F, 2, 1, 2, 0F); // Box 0
		baseModel[38].setRotationPoint(-32F, -28F, -59F);
		baseModel[38].rotateAngleX = 0.43633231F;

		baseModel[39].addBox(61F, -3F, 0F, 2, 1, 2, 0F); // Box 0
		baseModel[39].setRotationPoint(-32F, -28F, -59F);
		baseModel[39].rotateAngleX = 0.43633231F;

		baseModel[40].addBox(61F, -3F, 8F, 2, 1, 2, 0F); // Box 0
		baseModel[40].setRotationPoint(-32F, -28F, -59F);
		baseModel[40].rotateAngleX = 0.43633231F;

		baseModel[41].addShapeBox(0F, 0F, 0F, 10, 2, 24, 0F, 0F, -0.5F, 1F, 0F, -0.5F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.375F, 0F, 0F, 0.375F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[41].setRotationPoint(-32F, -35F, -49F);

		baseModel[42].addShapeBox(0F, 0F, 0F, 10, 2, 24, 0F, 0F, -0.5F, 1F, 0F, -0.5F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.375F, 0F, 0F, 0.375F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[42].setRotationPoint(-2F, -35F, -49F);

		baseModel[43].addBox(0F, 0F, 0F, 16, 1, 6, 0F); // Box 23
		baseModel[43].setRotationPoint(8F, -39F, -31F);

		baseModel[44].addBox(0F, 0F, 0F, 1, 5, 28, 0F); // Box 23
		baseModel[44].setRotationPoint(8F, -38F, -53F);

		baseModel[45].addShapeBox(0F, 0F, 0F, 8, 2, 24, 0F, 0F, -0.5F, 1F, 0F, -0.5F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.375F, 0F, 0F, 0.375F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[45].setRotationPoint(24F, -35F, -49F);

		baseModel[46].addBox(0F, 0F, 0F, 1, 5, 28, 0F); // Box 23
		baseModel[46].setRotationPoint(23F, -38F, -53F);

		baseModel[47].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // Box 0
		baseModel[47].setRotationPoint(-31F, -28F, -61F);

		baseModel[48].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // Box 0
		baseModel[48].setRotationPoint(-31F, -18F, -61F);

		baseModel[49].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // Box 0
		baseModel[49].setRotationPoint(-3F, -28F, -61F);

		baseModel[50].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // Box 0
		baseModel[50].setRotationPoint(-3F, -18F, -61F);

		baseModel[51].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // Box 0
		baseModel[51].setRotationPoint(1F, -28F, -61F);

		baseModel[52].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // Box 0
		baseModel[52].setRotationPoint(1F, -18F, -61F);

		baseModel[53].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // Box 0
		baseModel[53].setRotationPoint(29F, -28F, -61F);

		baseModel[54].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // Box 0
		baseModel[54].setRotationPoint(29F, -18F, -61F);

		baseModel[55].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(27, 0, 27, 0), new Coord2D(17, 5, 17, 5), new Coord2D(0, 5, 0, 5) }), 2, 27, 5, 61, 2, ModelRendererTurbo.MR_FRONT, new float[] {5 ,17 ,12 ,27}); // Shape 5
		baseModel[55].setRotationPoint(30F, -28F, -32F);
		baseModel[55].rotateAngleY = 1.57079633F;

		baseModel[56].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(27, 0, 27, 0), new Coord2D(17, 5, 17, 5), new Coord2D(0, 5, 0, 5) }), 2, 27, 5, 61, 2, ModelRendererTurbo.MR_FRONT, new float[] {5 ,17 ,12 ,27}); // Shape 5
		baseModel[56].setRotationPoint(-32F, -28F, -32F);
		baseModel[56].rotateAngleY = 1.57079633F;

		baseModel[57].addBox(0F, 0F, 0F, 32, 6, 4, 0F); // Box 0
		baseModel[57].setRotationPoint(0F, -39F, -25F);

		baseModel[58].addBox(0F, 0F, 0F, 32, 2, 4, 0F); // Box 0
		baseModel[58].setRotationPoint(-32F, -39F, -25F);

		baseModel[59].addShapeBox(0F, -2F, -1F, 32, 2, 36, 0F, 0F, -0.5F, 0.25F, 0F, -0.5F, 0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[59].setRotationPoint(0F, -5F, 65.5F);
		baseModel[59].rotateAngleX = 1.46607657F;

		baseModel[60].addShapeBox(0F, 0F, 0F, 12, 2, 16, 0F, 0F, -1.5F, 1F, 0F, -1.5F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1.5F, -1F, 0F, 1.5F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[60].setRotationPoint(-44F, -36F, -54F);

		baseModel[61].addBox(0F, 0F, 0F, 12, 2, 16, 0F); // Box 0
		baseModel[61].setRotationPoint(-44F, -36F, -38F);

		baseModel[62].addBox(0F, 0F, 0F, 12, 2, 16, 0F); // Box 0
		baseModel[62].setRotationPoint(-44F, -36F, -22F);

		baseModel[63].addBox(0F, 0F, 0F, 12, 2, 16, 0F); // Box 0
		baseModel[63].setRotationPoint(-44F, -36F, -6F);

		baseModel[64].addBox(0F, 0F, 0F, 12, 2, 16, 0F); // Box 0
		baseModel[64].setRotationPoint(-44F, -36F, 10F);

		baseModel[65].addBox(0F, 0F, 0F, 12, 2, 16, 0F); // Box 0
		baseModel[65].setRotationPoint(-44F, -36F, 26F);

		baseModel[66].addBox(0F, 0F, 0F, 12, 2, 16, 0F); // Box 0
		baseModel[66].setRotationPoint(-44F, -36F, 42F);

		baseModel[67].addBox(0F, 0F, 0F, 2, 6, 21, 0F); // Box 0
		baseModel[67].setRotationPoint(30F, -39F, -21F);

		baseModel[68].addBox(0F, 0F, 0F, 2, 6, 21, 0F); // Box 0
		baseModel[68].setRotationPoint(-32F, -39F, -21F);

		baseModel[69].addShapeBox(0F, 0F, 0F, 12, 2, 16, 0F, 0F, -1.5F, 1F, 0F, -1.5F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1.5F, -1F, 0F, 1.5F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[69].setRotationPoint(32F, -36F, -54F);

		baseModel[70].addBox(0F, 0F, 0F, 12, 2, 16, 0F); // Box 0
		baseModel[70].setRotationPoint(32F, -36F, -38F);

		baseModel[71].addBox(0F, 0F, 0F, 12, 2, 16, 0F); // Box 0
		baseModel[71].setRotationPoint(32F, -36F, -22F);

		baseModel[72].addBox(0F, 0F, 0F, 12, 2, 16, 0F); // Box 0
		baseModel[72].setRotationPoint(32F, -36F, -6F);

		baseModel[73].addBox(0F, 0F, 0F, 12, 2, 16, 0F); // Box 0
		baseModel[73].setRotationPoint(32F, -36F, 10F);

		baseModel[74].addBox(0F, 0F, 0F, 12, 2, 16, 0F); // Box 0
		baseModel[74].setRotationPoint(32F, -36F, 26F);

		baseModel[75].addBox(0F, 0F, 0F, 12, 2, 16, 0F); // Box 0
		baseModel[75].setRotationPoint(32F, -36F, 42F);

		baseModel[76].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		baseModel[76].setRotationPoint(-33F, -6F, -28F);

		baseModel[77].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		baseModel[77].setRotationPoint(-33F, -6F, -10F);

		baseModel[78].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		baseModel[78].setRotationPoint(-33F, -6F, 8F);

		baseModel[79].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		baseModel[79].setRotationPoint(-33F, -6F, 26F);

		baseModel[80].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		baseModel[80].setRotationPoint(-33F, -6F, 44F);

		baseModel[81].addBox(0F, 0F, 0F, 8, 12, 32, 0F); // Box 0
		baseModel[81].setRotationPoint(-40F, -29F, 16F);

		baseModel[82].addBox(0F, 0F, 0F, 8, 12, 32, 0F); // Box 0
		baseModel[82].setRotationPoint(-40F, -29F, -16F);

		baseModel[83].addBox(0F, 0F, 0F, 8, 12, 16, 0F); // Box 0
		baseModel[83].setRotationPoint(-40F, -29F, -32F);

		baseModel[84].addBox(0F, 0F, 0F, 8, 12, 32, 0F); // Box 0
		baseModel[84].setRotationPoint(32F, -29F, 16F);

		baseModel[85].addBox(0F, 0F, 0F, 8, 12, 32, 0F); // Box 0
		baseModel[85].setRotationPoint(32F, -29F, -16F);

		baseModel[86].addBox(0F, 0F, 0F, 8, 12, 16, 0F); // Box 0
		baseModel[86].setRotationPoint(32F, -29F, -32F);

		baseModel[87].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		baseModel[87].setRotationPoint(32F, -6F, -28F);

		baseModel[88].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		baseModel[88].setRotationPoint(32F, -6F, -10F);

		baseModel[89].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		baseModel[89].setRotationPoint(32F, -6F, 8F);

		baseModel[90].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		baseModel[90].setRotationPoint(32F, -6F, 26F);

		baseModel[91].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		baseModel[91].setRotationPoint(32F, -6F, 44F);

		baseModel[92].addBox(0F, 0F, 0F, 1, 1, 17, 0F); // Box 23
		baseModel[92].setRotationPoint(8F, -39F, -48F);

		baseModel[93].addBox(0F, 0F, 0F, 2, 5, 24, 0F); // Box 23
		baseModel[93].setRotationPoint(24F, -39F, -49F);

		baseModel[94].addShapeBox(0F, -2F, -1F, 32, 2, 36, 0F, 0F, -0.5F, 0.25F, 0F, -0.5F, 0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[94].setRotationPoint(-32F, -5F, 65.5F);
		baseModel[94].rotateAngleX = 1.46607657F;

		baseModel[95].addShapeBox(0F, 0F, 0F, 2, 33, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.125F, 0F, 0F, 0.125F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -3F, 0F, 0F, -3F); // Box 0
		baseModel[95].setRotationPoint(30F, -39F, 64F);

		baseModel[96].addShapeBox(0F, 0F, 0F, 2, 33, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.125F, 0F, 0F, 0.125F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -3F, 0F, 0F, -3F); // Box 0
		baseModel[96].setRotationPoint(-32F, -39F, 64F);

		baseModel[97].addBox(0F, 0F, 0F, 6, 1, 23, 0F); // Box 0
		baseModel[97].setRotationPoint(-23F, -36F, -49F);

		baseModel[98].addBox(0F, 0F, 0F, 6, 1, 23, 0F); // Box 0
		baseModel[98].setRotationPoint(-6F, -36F, -49F);

		baseModel[99].addBox(0F, 0F, 0F, 20, 1, 1, 0F); // Box 0
		baseModel[99].setRotationPoint(-22F, -36F, -49F);

		baseModel[100].addBox(-2F, 0F, -1F, 4, 8, 4, 0F); // Box 0
		baseModel[100].setRotationPoint(-12F, -43F, -38F);

		baseModel[101].addBox(0F, 0F, 0F, 3, 4, 3, 0F); // Box 0
		baseModel[101].setRotationPoint(-13F, -35F, -35F);

		baseModel[102].addBox(0F, 0F, 0F, 3, 3, 2, 0F); // Box 0
		baseModel[102].setRotationPoint(-13F, -38F, -35F);

		baseModel[103].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[103].setRotationPoint(-13F, -38F, -33F);

		baseModel[104].addBox(0F, 0F, 0F, 9, 3, 3, 0F); // Box 0
		baseModel[104].setRotationPoint(-22F, -33F, -35F);

		baseModel[105].addBox(0F, 0F, 0F, 3, 3, 2, 0F); // Box 0
		baseModel[105].setRotationPoint(-22F, -33F, -32F);

		baseModel[106].addBox(0F, 0F, 0F, 3, 3, 2, 0F); // Box 0
		baseModel[106].setRotationPoint(-18F, -33F, -32F);

		baseModel[107].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[107].setRotationPoint(-13F, -31F, -35F);

		baseModel[108].addBox(1F, -2.5F, 0F, 2, 1, 2, 0F); // Box 0
		baseModel[108].setRotationPoint(-32F, -6F, -48F);
		baseModel[108].rotateAngleX = 2.35619449F;

		baseModel[109].addBox(1F, -2.5F, 12F, 2, 1, 2, 0F); // Box 0
		baseModel[109].setRotationPoint(-32F, -6F, -48F);
		baseModel[109].rotateAngleX = 2.35619449F;

		baseModel[110].addBox(29F, -2.5F, 0F, 2, 1, 2, 0F); // Box 0
		baseModel[110].setRotationPoint(-32F, -6F, -48F);
		baseModel[110].rotateAngleX = 2.35619449F;

		baseModel[111].addBox(29F, -2.5F, 12F, 2, 1, 2, 0F); // Box 0
		baseModel[111].setRotationPoint(-32F, -6F, -48F);
		baseModel[111].rotateAngleX = 2.35619449F;

		baseModel[112].addBox(33F, -2.5F, 0F, 2, 1, 2, 0F); // Box 0
		baseModel[112].setRotationPoint(-32F, -6F, -48F);
		baseModel[112].rotateAngleX = 2.35619449F;

		baseModel[113].addBox(33F, -2.5F, 12F, 2, 1, 2, 0F); // Box 0
		baseModel[113].setRotationPoint(-32F, -6F, -48F);
		baseModel[113].rotateAngleX = 2.35619449F;

		baseModel[114].addBox(61F, -2.5F, 0F, 2, 1, 2, 0F); // Box 0
		baseModel[114].setRotationPoint(-32F, -6F, -48F);
		baseModel[114].rotateAngleX = 2.35619449F;

		baseModel[115].addBox(61F, -2.5F, 12F, 2, 1, 2, 0F); // Box 0
		baseModel[115].setRotationPoint(-32F, -6F, -48F);
		baseModel[115].rotateAngleX = 2.35619449F;

		baseModel[116].addShapeBox(1F, 0F, 0F, 2, 1, 2, 0F, 0F, -0.5F, 0.25F, 0F, -0.5F, 0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[116].setRotationPoint(0F, -5F, 65.5F);
		baseModel[116].rotateAngleX = 1.46607657F;

		baseModel[117].addShapeBox(1F, 0F, 32F, 2, 1, 2, 0F, 0F, -0.5F, 0.25F, 0F, -0.5F, 0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[117].setRotationPoint(0F, -5F, 65.5F);
		baseModel[117].rotateAngleX = 1.46607657F;

		baseModel[118].addShapeBox(29F, 0F, 0F, 2, 1, 2, 0F, 0F, -0.5F, 0.25F, 0F, -0.5F, 0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[118].setRotationPoint(0F, -5F, 65.5F);
		baseModel[118].rotateAngleX = 1.46607657F;

		baseModel[119].addShapeBox(29F, 0F, 32F, 2, 1, 2, 0F, 0F, -0.5F, 0.25F, 0F, -0.5F, 0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[119].setRotationPoint(0F, -5F, 65.5F);
		baseModel[119].rotateAngleX = 1.46607657F;

		baseModel[120].addShapeBox(-31F, 0F, 0F, 2, 1, 2, 0F, 0F, -0.5F, 0.25F, 0F, -0.5F, 0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[120].setRotationPoint(0F, -5F, 65.5F);
		baseModel[120].rotateAngleX = 1.46607657F;

		baseModel[121].addShapeBox(-31F, 0F, 32F, 2, 1, 2, 0F, 0F, -0.5F, 0.25F, 0F, -0.5F, 0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[121].setRotationPoint(0F, -5F, 65.5F);
		baseModel[121].rotateAngleX = 1.46607657F;

		baseModel[122].addShapeBox(-3F, 0F, 0F, 2, 1, 2, 0F, 0F, -0.5F, 0.25F, 0F, -0.5F, 0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[122].setRotationPoint(0F, -5F, 65.5F);
		baseModel[122].rotateAngleX = 1.46607657F;

		baseModel[123].addShapeBox(-3F, 0F, 32F, 2, 1, 2, 0F, 0F, -0.5F, 0.25F, 0F, -0.5F, 0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[123].setRotationPoint(0F, -5F, 65.5F);
		baseModel[123].rotateAngleX = 1.46607657F;

		baseModel[124].addBox(0F, 0F, 0F, 40, 4, 1, 0F); // Box 0
		baseModel[124].setRotationPoint(-32F, -39F, -26F);

		baseModel[125].addBox(0F, 0F, 0F, 1, 2, 4, 0F); // Box 0
		baseModel[125].setRotationPoint(-32F, -37F, -25F);

		baseModel[126].addShapeBox(0F, 0F, 0F, 20, 2, 4, 0F, 0F, -0.5F, 1F, 0F, -0.5F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.375F, 0F, 0F, 0.375F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[126].setRotationPoint(-22F, -35F, -49F);

		baseModel[127].addBox(0F, 0F, 0F, 32, 1, 16, 0F); // Box 0
		baseModel[127].setRotationPoint(-32F, -40F, -25F);

		baseModel[128].addBox(0F, 0F, 0F, 32, 1, 16, 0F); // Box 0
		baseModel[128].setRotationPoint(0F, -40F, -25F);

		baseModel[129].addBox(0F, 0F, 0F, 32, 1, 16, 0F); // Box 0
		baseModel[129].setRotationPoint(-32F, -40F, 15F);

		baseModel[130].addBox(0F, 0F, 0F, 32, 1, 16, 0F); // Box 0
		baseModel[130].setRotationPoint(0F, -40F, 15F);

		baseModel[131].addBox(0F, 0F, 0F, 16, 1, 24, 0F); // Box 0
		baseModel[131].setRotationPoint(16F, -40F, -9F);

		baseModel[132].addBox(0F, 0F, 0F, 16, 1, 24, 0F); // Box 0
		baseModel[132].setRotationPoint(-32F, -40F, -9F);

		baseModel[133].addBox(0F, 0F, 0F, 16, 8, 14, 0F); // Box 0
		baseModel[133].setRotationPoint(-30F, -38F, 53F);

		baseModel[134].addBox(0F, 0F, 0F, 16, 16, 14, 0F); // Box 0
		baseModel[134].setRotationPoint(14F, -38F, 53F);

		baseModel[135].addBox(0F, 0F, 0F, 2, 1, 36, 0F); // Box 0
		baseModel[135].setRotationPoint(30F, -40F, 31F);

		baseModel[136].addBox(0F, 0F, 0F, 2, 1, 36, 0F); // Box 0
		baseModel[136].setRotationPoint(-32F, -40F, 31F);

		baseModel[137].addBox(0F, 0F, 0F, 14, 2, 2, 0F); // Box 0
		baseModel[137].setRotationPoint(-30F, -40F, 43F);

		baseModel[138].addBox(0F, 0F, 0F, 2, 2, 36, 0F); // Box 0
		baseModel[138].setRotationPoint(-16F, -40F, 31F);

		baseModel[139].addBox(0F, 0F, 0F, 14, 2, 2, 0F); // Box 0
		baseModel[139].setRotationPoint(16F, -40F, 43F);

		baseModel[140].addBox(0F, 0F, 0F, 2, 2, 36, 0F); // Box 0
		baseModel[140].setRotationPoint(14F, -40F, 31F);

		baseModel[141].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[141].setRotationPoint(40F, -24F, -10F);

		baseModel[142].addBox(0F, 0F, 0F, 1, 1, 12, 0F); // Box 0
		baseModel[142].setRotationPoint(41F, -24F, -10F);

		baseModel[143].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[143].setRotationPoint(40F, -24F, 1F);

		baseModel[144].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[144].setRotationPoint(40F, -19F, -10F);

		baseModel[145].addBox(0F, 0F, 0F, 1, 1, 12, 0F); // Box 0
		baseModel[145].setRotationPoint(41F, -19F, -10F);

		baseModel[146].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[146].setRotationPoint(40F, -19F, 1F);

		baseModel[147].addBox(0F, 0F, 0F, 2, 1, 48, 0F); // Box 0
		baseModel[147].setRotationPoint(23F, -41F, -21F);

		baseModel[148].addBox(0F, 0F, 0F, 2, 1, 48, 0F); // Box 0
		baseModel[148].setRotationPoint(-25F, -41F, -21F);

		baseModel[149].addBox(0F, 0F, 0F, 46, 1, 2, 0F); // Box 0
		baseModel[149].setRotationPoint(-23F, -41F, -21F);

		baseModel[150].addBox(0F, 0F, 0F, 46, 1, 2, 0F); // Box 0
		baseModel[150].setRotationPoint(-23F, -41F, 25F);

		baseModel[151].addBox(0F, 0F, 0F, 16, 16, 4, 0F); // Box 0
		baseModel[151].setRotationPoint(14F, -38F, 33F);

		baseModel[152].addBox(0F, 0F, 0F, 16, 16, 4, 0F); // Box 0
		baseModel[152].setRotationPoint(14F, -38F, 38F);

		baseModel[153].addBox(0F, 0F, 0F, 16, 16, 4, 0F); // Box 0
		baseModel[153].setRotationPoint(14F, -38F, 43F);

		baseModel[154].addBox(0F, 0F, 0F, 1, 1, 4, 0F); // Box 0
		baseModel[154].setRotationPoint(30F, -41F, 31F);

		baseModel[155].addBox(0F, 0F, 0F, 1, 1, 4, 0F); // Box 0
		baseModel[155].setRotationPoint(30F, -41F, 63F);

		baseModel[156].addBox(0F, 0F, 0F, 1, 1, 4, 0F); // Box 0
		baseModel[156].setRotationPoint(-31F, -41F, 31F);

		baseModel[157].addBox(0F, 0F, 0F, 1, 1, 4, 0F); // Box 0
		baseModel[157].setRotationPoint(-31F, -41F, 63F);


		bodyDoorOpenModel = new ModelRendererTurbo[6];
		bodyDoorOpenModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 630
		bodyDoorOpenModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 632
		bodyDoorOpenModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 633
		bodyDoorOpenModel[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 635
		bodyDoorOpenModel[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 637
		bodyDoorOpenModel[5] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 638

		bodyDoorOpenModel[0].addShapeBox(15F, -6F, 13F, 16, 2, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 630
		bodyDoorOpenModel[0].setRotationPoint(-15F, -71F, -5F);

		bodyDoorOpenModel[1].addShapeBox(0F, 0F, 0F, 8, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 632
		bodyDoorOpenModel[1].setRotationPoint(1F, -78.8F, 15F);
		bodyDoorOpenModel[1].rotateAngleZ = -0.17453293F;

		bodyDoorOpenModel[2].addShapeBox(16F, -6F, 14F, 10, 1, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 633
		bodyDoorOpenModel[2].setRotationPoint(-13F, -68.8F, -3F);

		bodyDoorOpenModel[3].addShapeBox(0F, 0F, 0F, 2, 1, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 635
		bodyDoorOpenModel[3].setRotationPoint(5F, -75F, 10.5F);
		bodyDoorOpenModel[3].rotateAngleY = 0.83775804F;

		bodyDoorOpenModel[4].addShapeBox(0F, 0F, 0F, 2, 1, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 637
		bodyDoorOpenModel[4].setRotationPoint(9F, -75F, 12F);
		bodyDoorOpenModel[4].rotateAngleY = -0.83775804F;

		bodyDoorOpenModel[5].addShapeBox(0F, 0F, 0F, 5, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 638
		bodyDoorOpenModel[5].setRotationPoint(5F, -75F, 20F);


		bodyDoorCloseModel = new ModelRendererTurbo[5];
		bodyDoorCloseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 651
		bodyDoorCloseModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 652
		bodyDoorCloseModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 653
		bodyDoorCloseModel[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 654
		bodyDoorCloseModel[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 655

		bodyDoorCloseModel[0].addShapeBox(16F, -6F, 14F, 16, 1, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 651
		bodyDoorCloseModel[0].setRotationPoint(-38F, -63.8F, 2F);

		bodyDoorCloseModel[1].addShapeBox(0F, 0F, 0F, 2, 1, 9, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 652
		bodyDoorCloseModel[1].setRotationPoint(-19F, -70.8F, 16F);
		bodyDoorCloseModel[1].rotateAngleX = -0.08726646F;

		bodyDoorCloseModel[2].addShapeBox(0F, 0F, 0F, 2, 1, 9, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 653
		bodyDoorCloseModel[2].setRotationPoint(-11F, -70.8F, 16F);
		bodyDoorCloseModel[2].rotateAngleX = -0.08726646F;

		bodyDoorCloseModel[3].addShapeBox(16F, -6F, 14F, 1, 1, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 654
		bodyDoorCloseModel[3].setRotationPoint(-36F, -62.8F, 4F);

		bodyDoorCloseModel[4].addShapeBox(16F, -6F, 14F, 4, 2, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 655
		bodyDoorCloseModel[4].setRotationPoint(-32F, -62.8F, 5F);


		turretModel = new ModelRendererTurbo[129];
		turretModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[5] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[6] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[7] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[8] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[9] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[10] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[11] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[12] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[13] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[14] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[15] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[16] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[17] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[18] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[19] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[20] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[21] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 454
		turretModel[22] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 454
		turretModel[23] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[24] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[25] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[26] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[27] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[28] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[29] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[30] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[31] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[32] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[33] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[34] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[35] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[36] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[37] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[38] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[39] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[40] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[41] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[42] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[43] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[44] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[45] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[46] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[47] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[48] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[49] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[50] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[51] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[52] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[53] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[54] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[55] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[56] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[57] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[58] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[59] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[60] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[61] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[62] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[63] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[64] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[65] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[66] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[67] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[68] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[69] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[70] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[71] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[72] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[73] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[74] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[75] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[76] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[77] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[78] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[79] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[80] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[81] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[82] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[83] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[84] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 454
		turretModel[85] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 454
		turretModel[86] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[87] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[88] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[89] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[90] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 454
		turretModel[91] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 454
		turretModel[92] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[93] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[94] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[95] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[96] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[97] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[98] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[99] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[100] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[101] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[102] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[103] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		turretModel[104] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 639
		turretModel[105] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 640
		turretModel[106] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 641
		turretModel[107] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 642
		turretModel[108] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 639
		turretModel[109] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 640
		turretModel[110] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 641
		turretModel[111] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 642
		turretModel[112] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 643
		turretModel[113] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 644
		turretModel[114] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 645
		turretModel[115] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 646
		turretModel[116] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 640
		turretModel[117] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 640
		turretModel[118] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 640
		turretModel[119] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 640
		turretModel[120] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 641
		turretModel[121] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 642
		turretModel[122] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 641
		turretModel[123] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 642
		turretModel[124] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 631
		turretModel[125] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 647
		turretModel[126] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 648
		turretModel[127] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 649
		turretModel[128] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 650

		turretModel[0].addBox(-3F, 0F, -3F, 6, 16, 6, 0F); // Box 11
		turretModel[0].setRotationPoint(0F, -39F, 2F);

		turretModel[1].addBox(-4F, 0F, -4F, 8, 1, 8, 0F); // Box 11
		turretModel[1].setRotationPoint(0F, -40F, 2F);

		turretModel[2].addBox(-11F, 0F, -9F, 8, 1, 18, 0F); // Box 11
		turretModel[2].setRotationPoint(0F, -32F, 2F);

		turretModel[3].addBox(3F, 0F, -9F, 8, 1, 18, 0F); // Box 11
		turretModel[3].setRotationPoint(0F, -32F, 2F);

		turretModel[4].addShapeBox(-28F, 0F, -3F, 4, 26, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, 0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, 0.25F); // Box 11
		turretModel[4].setRotationPoint(0F, -67F, 2F);
		turretModel[4].rotateAngleY = -0.17453293F;

		turretModel[5].addBox(10F, -4F, 26F, 16, 22, 4, 0F); // Box 11
		turretModel[5].setRotationPoint(0F, -63F, 2F);

		turretModel[6].addBox(-26F, -18F, 22F, 4, 22, 4, 0F); // Box 11
		turretModel[6].setRotationPoint(0F, -49F, 2F);

		turretModel[7].addBox(22F, -18F, 22F, 4, 22, 4, 0F); // Box 11
		turretModel[7].setRotationPoint(0F, -49F, 2F);

		turretModel[8].addShapeBox(0F, 22F, 21F, 26, 4, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, 0F, -4F, 0F); // Box 11
		turretModel[8].setRotationPoint(0F, -67F, 3F);

		turretModel[9].addBox(22F, 0F, 14F, 4, 26, 8, 0F); // Box 11
		turretModel[9].setRotationPoint(0F, -67F, 2F);

		turretModel[10].addShapeBox(-26F, 22F, 21F, 26, 4, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, 0F, -4F, 0F); // Box 11
		turretModel[10].setRotationPoint(0F, -67F, 3F);

		turretModel[11].addBox(-26F, 0F, 14F, 4, 26, 8, 0F); // Box 11
		turretModel[11].setRotationPoint(0F, -67F, 2F);

		turretModel[12].addShapeBox(-28F, 0F, -9F, 4, 26, 10, 0F, 0F, 0F, 0F, 0F, 0F, -0.75F, -0.125F, 0F, 0.25F, 0.125F, 0F, 0.875F, 0F, 0F, 0F, 0F, 0F, -0.75F, -0.125F, 0F, 0.25F, 0.125F, 0F, 0.875F); // Box 11
		turretModel[12].setRotationPoint(0F, -67F, 2F);

		turretModel[13].addBox(-26F, -4F, 26F, 16, 22, 4, 0F); // Box 11
		turretModel[13].setRotationPoint(0F, -63F, 2F);

		turretModel[14].addBox(-24F, 1F, -27F, 5, 21, 3, 0F); // Box 11
		turretModel[14].setRotationPoint(0F, -65F, 2F);

		turretModel[15].addBox(-16F, -4F, 40F, 16, 22, 2, 0F); // Box 11
		turretModel[15].setRotationPoint(0F, -63F, 2F);

		turretModel[16].addBox(0F, -4F, 40F, 16, 22, 2, 0F); // Box 11
		turretModel[16].setRotationPoint(0F, -63F, 2F);

		turretModel[17].addBox(-16F, -4F, 30F, 2, 22, 10, 0F); // Box 11
		turretModel[17].setRotationPoint(0F, -63F, 2F);

		turretModel[18].addBox(14F, -4F, 30F, 2, 22, 10, 0F); // Box 11
		turretModel[18].setRotationPoint(0F, -63F, 2F);

		turretModel[19].addBox(-14F, 16F, 30F, 28, 2, 10, 0F); // Box 11
		turretModel[19].setRotationPoint(0F, -63F, 2F);

		turretModel[20].addBox(-16F, -4F, 30F, 32, 1, 2, 0F); // Box 11
		turretModel[20].setRotationPoint(0F, -64F, 2F);

		turretModel[21].addShape3D(20F, -26F, 25F, new Shape2D(new Coord2D[] { new Coord2D(0, 2, 0, 2), new Coord2D(16, 0, 16, 0), new Coord2D(16, 26, 16, 26), new Coord2D(0, 23, 0, 23) }), 4, 16, 26, 81, 4, ModelRendererTurbo.MR_FRONT, new float[] {21 ,17 ,26 ,17}); // Shape 454
		turretModel[21].setRotationPoint(0F, -67F, 2F);
		turretModel[21].rotateAngleY = -1.3962634F;

		turretModel[22].addShape3D(-4F, -26F, 25F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(16, 2, 16, 2), new Coord2D(16, 23, 16, 23), new Coord2D(0, 26, 0, 26) }), 4, 16, 26, 81, 4, ModelRendererTurbo.MR_FRONT, new float[] {26 ,17 ,21 ,17}); // Shape 454
		turretModel[22].setRotationPoint(0F, -67F, 2F);
		turretModel[22].rotateAngleY = 1.3962634F;

		turretModel[23].addShapeBox(24F, 0F, -3F, 4, 26, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.25F, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.25F, 0F, 0F, 1F); // Box 11
		turretModel[23].setRotationPoint(0F, -67F, 2F);
		turretModel[23].rotateAngleY = 0.17453293F;

		turretModel[24].addShapeBox(24F, 0F, -9F, 4, 26, 10, 0F, 0F, 0F, -0.75F, 0F, 0F, 0F, 0.125F, 0F, 0.875F, -0.125F, 0F, 0.25F, 0F, 0F, -0.75F, 0F, 0F, 0F, 0.125F, 0F, 0.875F, -0.125F, 0F, 0.25F); // Box 11
		turretModel[24].setRotationPoint(0F, -67F, 2F);

		turretModel[25].addBox(-27F, -17F, 24F, 1, 2, 2, 0F); // Box 11
		turretModel[25].setRotationPoint(0F, -49F, 2F);

		turretModel[26].addBox(-27F, 3F, 24F, 1, 2, 2, 0F); // Box 11
		turretModel[26].setRotationPoint(0F, -49F, 2F);

		turretModel[27].addBox(26F, -17F, 24F, 1, 2, 2, 0F); // Box 11
		turretModel[27].setRotationPoint(0F, -49F, 2F);

		turretModel[28].addBox(26F, 3F, 24F, 1, 2, 2, 0F); // Box 11
		turretModel[28].setRotationPoint(0F, -49F, 2F);

		turretModel[29].addBox(28F, 1F, 8F, 1, 2, 2, 0F); // Box 11
		turretModel[29].setRotationPoint(0F, -67F, 2F);
		turretModel[29].rotateAngleY = 0.17453293F;

		turretModel[30].addBox(28F, 21F, 8F, 1, 2, 2, 0F); // Box 11
		turretModel[30].setRotationPoint(0F, -67F, 2F);
		turretModel[30].rotateAngleY = 0.17453293F;

		turretModel[31].addBox(28F, 1F, -1F, 1, 2, 2, 0F); // Box 11
		turretModel[31].setRotationPoint(0F, -67F, 2F);
		turretModel[31].rotateAngleY = 0.08726646F;

		turretModel[32].addBox(28F, 21F, -1F, 1, 2, 2, 0F); // Box 11
		turretModel[32].setRotationPoint(0F, -67F, 2F);
		turretModel[32].rotateAngleY = 0.08726646F;

		turretModel[33].addBox(29F, 4F, -18F, 1, 2, 2, 0F); // Box 11
		turretModel[33].setRotationPoint(0F, -67F, 2F);
		turretModel[33].rotateAngleY = -0.17453293F;

		turretModel[34].addBox(29F, 20F, -18F, 1, 2, 2, 0F); // Box 11
		turretModel[34].setRotationPoint(0F, -67F, 2F);
		turretModel[34].rotateAngleY = -0.17453293F;

		turretModel[35].addBox(29F, 2F, -7F, 1, 2, 2, 0F); // Box 11
		turretModel[35].setRotationPoint(0F, -67F, 2F);
		turretModel[35].rotateAngleY = -0.17453293F;

		turretModel[36].addBox(29F, 22F, -7F, 1, 2, 2, 0F); // Box 11
		turretModel[36].setRotationPoint(0F, -67F, 2F);
		turretModel[36].rotateAngleY = -0.17453293F;

		turretModel[37].addBox(-6F, -6F, -16F, 12, 12, 16, 0F); // Box 11
		turretModel[37].setRotationPoint(0F, -54F, -25F);
		turretModel[37].rotateAngleX = -0.15707963F;

		turretModel[38].addBox(-29F, 1F, -1F, 1, 2, 2, 0F); // Box 11
		turretModel[38].setRotationPoint(0F, -67F, 2F);
		turretModel[38].rotateAngleY = -0.08726646F;

		turretModel[39].addBox(-29F, 21F, -1F, 1, 2, 2, 0F); // Box 11
		turretModel[39].setRotationPoint(0F, -67F, 2F);
		turretModel[39].rotateAngleY = -0.08726646F;

		turretModel[40].addShapeBox(-25F, 22F, -20F, 9, 2, 16, 0F, 0F, 0F, 0F, 0F, 0F, 1.75F, 2.875F, -2F, 0F, 0.125F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 1.75F, 2.875F, 2F, 0F, 0.125F, 2F, 0F); // Box 11
		turretModel[40].setRotationPoint(0F, -67F, 2F);
		turretModel[40].rotateAngleY = 0.17453293F;

		turretModel[41].addShapeBox(-12F, 20F, -24.2F, 24, 2, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, 1F, 0F); // Box 11
		turretModel[41].setRotationPoint(0F, -65F, 2F);

		turretModel[42].addBox(-29F, 1F, 8F, 1, 2, 2, 0F); // Box 11
		turretModel[42].setRotationPoint(0F, -67F, 2F);
		turretModel[42].rotateAngleY = -0.17453293F;

		turretModel[43].addBox(-29F, 21F, 8F, 1, 2, 2, 0F); // Box 11
		turretModel[43].setRotationPoint(0F, -67F, 2F);
		turretModel[43].rotateAngleY = -0.17453293F;

		turretModel[44].addBox(-30F, 4F, -18F, 1, 2, 2, 0F); // Box 11
		turretModel[44].setRotationPoint(0F, -67F, 2F);
		turretModel[44].rotateAngleY = 0.17453293F;

		turretModel[45].addBox(-30F, 20F, -18F, 1, 2, 2, 0F); // Box 11
		turretModel[45].setRotationPoint(0F, -67F, 2F);
		turretModel[45].rotateAngleY = 0.17453293F;

		turretModel[46].addBox(-30F, 2F, -7F, 1, 2, 2, 0F); // Box 11
		turretModel[46].setRotationPoint(0F, -67F, 2F);
		turretModel[46].rotateAngleY = 0.17453293F;

		turretModel[47].addBox(-30F, 22F, -7F, 1, 2, 2, 0F); // Box 11
		turretModel[47].setRotationPoint(0F, -67F, 2F);
		turretModel[47].rotateAngleY = 0.17453293F;

		turretModel[48].addBox(-12F, 23F, -18.2F, 24, 2, 2, 0F); // Box 11
		turretModel[48].setRotationPoint(0F, -65F, 2F);

		turretModel[49].addShapeBox(16F, 22F, -20F, 9, 2, 16, 0F, 0F, 0F, 1.75F, 0F, 0F, 0F, 0.125F, -2F, 0F, 2.875F, -2F, 0F, 0F, 0F, 1.75F, 0F, 0F, 0F, 0.125F, 2F, 0F, 2.875F, 2F, 0F); // Box 11
		turretModel[49].setRotationPoint(0F, -67F, 2F);
		turretModel[49].rotateAngleY = -0.17453293F;

		turretModel[50].addShapeBox(14F, 25F, -6F, 10, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.125F, 0F, 0F, 0.125F, 0F); // Box 11
		turretModel[50].setRotationPoint(0F, -67F, 2F);
		turretModel[50].rotateAngleY = -0.17453293F;

		turretModel[51].addShapeBox(-24F, 25F, -6F, 10, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.125F, 0F, 0F, 0.125F, 0F); // Box 11
		turretModel[51].setRotationPoint(0F, -67F, 2F);
		turretModel[51].rotateAngleY = 0.17453293F;

		turretModel[52].addShapeBox(14F, 25F, -16F, 2, 2, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.125F, 0F, 0F, 0.125F, 0F); // Box 11
		turretModel[52].setRotationPoint(0F, -67F, 2F);
		turretModel[52].rotateAngleY = -0.17453293F;

		turretModel[53].addShapeBox(-16F, 25F, -16F, 2, 2, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.125F, 0F, 0F, -0.125F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.125F, 0F, 0F, 0.125F, 0F); // Box 11
		turretModel[53].setRotationPoint(0F, -67F, 2F);
		turretModel[53].rotateAngleY = 0.17453293F;

		turretModel[54].addBox(-6F, -6F, 0F, 12, 12, 16, 0F); // Box 11
		turretModel[54].setRotationPoint(0F, -54F, -25F);
		turretModel[54].rotateAngleX = -0.15707963F;

		turretModel[55].addBox(19F, 1F, -27F, 5, 21, 3, 0F); // Box 11
		turretModel[55].setRotationPoint(0F, -65F, 2F);

		turretModel[56].addBox(-19F, 1F, -27F, 38, 3, 3, 0F); // Box 11
		turretModel[56].setRotationPoint(0F, -65F, 2F);

		turretModel[57].addBox(-19F, 19F, -27F, 38, 3, 3, 0F); // Box 11
		turretModel[57].setRotationPoint(0F, -65F, 2F);

		turretModel[58].addBox(16F, 0F, 30F, 4, 12, 10, 0F); // Box 11
		turretModel[58].setRotationPoint(0F, -63F, 2F);

		turretModel[59].addBox(-20F, 0F, 30F, 4, 4, 10, 0F); // Box 11
		turretModel[59].setRotationPoint(0F, -63F, 2F);

		turretModel[60].addBox(4F, 0F, 42F, 10, 12, 4, 0F); // Box 11
		turretModel[60].setRotationPoint(0F, -63F, 2F);

		turretModel[61].addBox(-20F, 4F, 30F, 4, 4, 11, 0F); // Box 11
		turretModel[61].setRotationPoint(0F, -63F, 2F);

		turretModel[62].addBox(-20F, 8F, 30F, 4, 4, 9, 0F); // Box 11
		turretModel[62].setRotationPoint(0F, -63F, 2F);

		turretModel[63].addBox(-22F, 2F, -24F, 44, 1, 4, 0F); // Box 11
		turretModel[63].setRotationPoint(0F, -65F, 2F);

		turretModel[64].addBox(-22F, 18F, -24F, 44, 1, 4, 0F); // Box 11
		turretModel[64].setRotationPoint(0F, -65F, 2F);

		turretModel[65].addBox(-6F, -6F, 8F, 6, 2, 22, 0F); // Box 11
		turretModel[65].setRotationPoint(0F, -63F, 2F);

		turretModel[66].addBox(0F, -6F, 22F, 26, 2, 8, 0F); // Box 11
		turretModel[66].setRotationPoint(0F, -63F, 2F);

		turretModel[67].addBox(26F, 5F, 15F, 1, 1, 1, 0F); // Box 11
		turretModel[67].setRotationPoint(0F, -67F, 2F);

		turretModel[68].addBox(27F, 5F, 15F, 1, 1, 12, 0F); // Box 11
		turretModel[68].setRotationPoint(0F, -67F, 2F);

		turretModel[69].addBox(26F, 5F, 26F, 1, 1, 1, 0F); // Box 11
		turretModel[69].setRotationPoint(0F, -67F, 2F);

		turretModel[70].addBox(26F, 10F, 15F, 1, 1, 1, 0F); // Box 11
		turretModel[70].setRotationPoint(0F, -67F, 2F);

		turretModel[71].addBox(27F, 10F, 15F, 1, 1, 12, 0F); // Box 11
		turretModel[71].setRotationPoint(0F, -67F, 2F);

		turretModel[72].addBox(26F, 10F, 26F, 1, 1, 1, 0F); // Box 11
		turretModel[72].setRotationPoint(0F, -67F, 2F);

		turretModel[73].addBox(26F, 15F, 15F, 1, 1, 1, 0F); // Box 11
		turretModel[73].setRotationPoint(0F, -67F, 2F);

		turretModel[74].addBox(27F, 15F, 15F, 1, 1, 12, 0F); // Box 11
		turretModel[74].setRotationPoint(0F, -67F, 2F);

		turretModel[75].addBox(26F, 15F, 26F, 1, 1, 1, 0F); // Box 11
		turretModel[75].setRotationPoint(0F, -67F, 2F);

		turretModel[76].addBox(26F, 20F, 15F, 1, 1, 1, 0F); // Box 11
		turretModel[76].setRotationPoint(0F, -67F, 2F);

		turretModel[77].addBox(27F, 20F, 15F, 1, 1, 12, 0F); // Box 11
		turretModel[77].setRotationPoint(0F, -67F, 2F);

		turretModel[78].addBox(26F, 20F, 26F, 1, 1, 1, 0F); // Box 11
		turretModel[78].setRotationPoint(0F, -67F, 2F);

		turretModel[79].addBox(-16F, -4F, 40F, 32, 1, 2, 0F); // Box 11
		turretModel[79].setRotationPoint(0F, -64F, 2F);

		turretModel[80].addBox(12F, -4F, 32F, 4, 1, 8, 0F); // Box 11
		turretModel[80].setRotationPoint(0F, -64F, 2F);

		turretModel[81].addBox(-16F, -4F, 32F, 4, 1, 8, 0F); // Box 11
		turretModel[81].setRotationPoint(0F, -64F, 2F);

		turretModel[82].addBox(-2F, -4F, 42F, 4, 4, 1, 0F); // Box 11
		turretModel[82].setRotationPoint(0F, -63F, 2F);

		turretModel[83].addBox(16F, -6F, 14F, 10, 2, 8, 0F); // Box 11
		turretModel[83].setRotationPoint(0F, -63F, 2F);

		turretModel[84].addShape3D(28F, -9F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(28, 0, 28, 0), new Coord2D(28, 15, 28, 15), new Coord2D(12, 15, 12, 15), new Coord2D(12, 23, 12, 23), new Coord2D(2, 23, 2, 23), new Coord2D(0, 11, 0, 11) }), 2, 28, 23, 101, 2, ModelRendererTurbo.MR_FRONT, new float[] {11 ,13 ,10 ,8 ,16 ,15 ,28}); // Shape 454
		turretModel[84].setRotationPoint(0F, -67F, 2F);
		turretModel[84].rotateAngleX = -1.57079633F;

		turretModel[85].addShape3D(0F, -9F, 0F, new Shape2D(new Coord2D[] { new Coord2D(16, 0, 16, 0), new Coord2D(28, 0, 28, 0), new Coord2D(28, 11, 28, 11), new Coord2D(26, 23, 26, 23), new Coord2D(16, 23, 16, 23) }), 2, 28, 23, 69, 2, ModelRendererTurbo.MR_FRONT, new float[] {23 ,10 ,13 ,11 ,12}); // Shape 454
		turretModel[85].setRotationPoint(0F, -67F, 2F);
		turretModel[85].rotateAngleX = -1.57079633F;

		turretModel[86].addBox(-16F, -6F, -9F, 16, 2, 17, 0F); // Box 11
		turretModel[86].setRotationPoint(0F, -63F, 2F);

		turretModel[87].addBox(-26F, -6F, 14F, 4, 2, 16, 0F); // Box 11
		turretModel[87].setRotationPoint(0F, -63F, 2F);

		turretModel[88].addBox(-22F, -6F, 26F, 16, 2, 4, 0F); // Box 11
		turretModel[88].setRotationPoint(0F, -63F, 2F);

		turretModel[89].addBox(-16F, -6F, 8F, 10, 2, 6, 0F); // Box 11
		turretModel[89].setRotationPoint(0F, -63F, 2F);

		turretModel[90].addShape3D(28F, -27F, -1F, new Shape2D(new Coord2D[] { new Coord2D(4, 0, 4, 0), new Coord2D(28, 0, 28, 0), new Coord2D(28, 18, 28, 18), new Coord2D(0, 18, 0, 18) }), 2, 28, 18, 89, 2, ModelRendererTurbo.MR_FRONT, new float[] {19 ,28 ,18 ,24}); // Shape 454
		turretModel[90].setRotationPoint(0F, -69.5F, 2F);
		turretModel[90].rotateAngleX = -1.3962634F;

		turretModel[91].addShape3D(0F, -27F, -1F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(24, 0, 24, 0), new Coord2D(28, 18, 28, 18), new Coord2D(0, 18, 0, 18) }), 2, 28, 18, 89, 2, ModelRendererTurbo.MR_FRONT, new float[] {18 ,28 ,19 ,24}); // Shape 454
		turretModel[91].setRotationPoint(0F, -69.5F, 2F);
		turretModel[91].rotateAngleX = -1.3962634F;

		turretModel[92].addBox(9F, -3F, 5F, 6, 6, 12, 0F); // Box 11
		turretModel[92].setRotationPoint(0F, -54F, -25F);
		turretModel[92].rotateAngleX = -0.15707963F;

		turretModel[93].addBox(11F, -1F, 17F, 2, 2, 4, 0F); // Box 11
		turretModel[93].setRotationPoint(0F, -54F, -25F);
		turretModel[93].rotateAngleX = -0.15707963F;

		turretModel[94].addBox(10F, -2F, 21F, 4, 4, 2, 0F); // Box 11
		turretModel[94].setRotationPoint(0F, -54F, -25F);
		turretModel[94].rotateAngleX = -0.15707963F;

		turretModel[95].addBox(-6F, 4F, 16F, 12, 4, 16, 0F); // Box 11
		turretModel[95].setRotationPoint(0F, -54F, -25F);
		turretModel[95].rotateAngleX = -0.15707963F;

		turretModel[96].addBox(-13F, -2F, -1F, 4, 4, 6, 0F); // Box 11
		turretModel[96].setRotationPoint(0F, -54F, -25F);
		turretModel[96].rotateAngleX = -0.15707963F;

		turretModel[97].addBox(-14F, -3F, 5F, 6, 6, 4, 0F); // Box 11
		turretModel[97].setRotationPoint(0F, -54F, -25F);
		turretModel[97].rotateAngleX = -0.15707963F;

		turretModel[98].addBox(-6F, -6F, 16F, 2, 10, 10, 0F); // Box 11
		turretModel[98].setRotationPoint(0F, -54F, -25F);
		turretModel[98].rotateAngleX = -0.15707963F;

		turretModel[99].addBox(4F, -6F, 16F, 2, 10, 10, 0F); // Box 11
		turretModel[99].setRotationPoint(0F, -54F, -25F);
		turretModel[99].rotateAngleX = -0.15707963F;

		turretModel[100].addBox(-24F, -2F, -8F, 8, 1, 24, 0F); // Box 11
		turretModel[100].setRotationPoint(0F, -40F, 2F);

		turretModel[101].addBox(16F, -2F, -8F, 8, 1, 24, 0F); // Box 11
		turretModel[101].setRotationPoint(0F, -40F, 2F);

		turretModel[102].addBox(-13F, -2F, -8F, 4, 4, 2, 0F); // Box 11
		turretModel[102].setRotationPoint(0F, -54F, -25F);
		turretModel[102].rotateAngleX = -0.15707963F;

		turretModel[103].addBox(-14F, -9F, 5F, 6, 6, 4, 0F); // Box 11
		turretModel[103].setRotationPoint(0F, -54F, -25F);
		turretModel[103].rotateAngleX = -0.15707963F;

		turretModel[104].addShapeBox(16F, -6F, 14F, 3, 4, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 639
		turretModel[104].setRotationPoint(-19F, -67F, -6F);

		turretModel[105].addShapeBox(16F, -6F, 14F, 3, 4, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 640
		turretModel[105].setRotationPoint(0F, -67F, -6F);

		turretModel[106].addShapeBox(16F, -6F, 14F, 16, 4, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 641
		turretModel[106].setRotationPoint(-16F, -67F, -9F);

		turretModel[107].addShapeBox(16F, -6F, 14F, 16, 4, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 642
		turretModel[107].setRotationPoint(-16F, -67F, 10F);

		turretModel[108].addShapeBox(16F, -6F, 14F, 3, 3, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 639
		turretModel[108].setRotationPoint(-19F, -71F, -6F);

		turretModel[109].addShapeBox(16F, -6F, 14F, 3, 3, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 640
		turretModel[109].setRotationPoint(0F, -71F, -6F);

		turretModel[110].addShapeBox(16F, -6F, 14F, 16, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 641
		turretModel[110].setRotationPoint(-16F, -71F, -9F);

		turretModel[111].addShapeBox(16F, -6F, 14F, 16, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 642
		turretModel[111].setRotationPoint(-16F, -71F, 10F);

		turretModel[112].addShapeBox(16F, -6F, 14F, 3, 8, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -3F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -3F, 0F, 0F); // Box 643
		turretModel[112].setRotationPoint(-19F, -71F, 10F);

		turretModel[113].addShapeBox(16F, -6F, 14F, 3, 8, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -3F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -3F, 0F, 0F, 0F); // Box 644
		turretModel[113].setRotationPoint(0F, -71F, 10F);

		turretModel[114].addShapeBox(16F, -6F, 14F, 3, 8, 3, 0F, 0F, 0F, 0F, -3F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -3F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 645
		turretModel[114].setRotationPoint(0F, -71F, -9F);

		turretModel[115].addShapeBox(16F, -6F, 14F, 3, 8, 3, 0F, 0F, 0F, -3F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -3F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 646
		turretModel[115].setRotationPoint(-19F, -71F, -9F);

		turretModel[116].addShapeBox(16F, -6F, 14F, 3, 1, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 640
		turretModel[116].setRotationPoint(0F, -68F, -6F);

		turretModel[117].addShapeBox(16F, -6F, 14F, 3, 1, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 640
		turretModel[117].setRotationPoint(0F, -68F, 4F);

		turretModel[118].addShapeBox(16F, -6F, 14F, 3, 1, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 640
		turretModel[118].setRotationPoint(-19F, -68F, -6F);

		turretModel[119].addShapeBox(16F, -6F, 14F, 3, 1, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 640
		turretModel[119].setRotationPoint(-19F, -68F, 4F);

		turretModel[120].addShapeBox(16F, -6F, 14F, 6, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 641
		turretModel[120].setRotationPoint(-16F, -68F, -9F);

		turretModel[121].addShapeBox(16F, -6F, 14F, 6, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 642
		turretModel[121].setRotationPoint(-16F, -68F, 10F);

		turretModel[122].addShapeBox(16F, -6F, 14F, 6, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 641
		turretModel[122].setRotationPoint(-6F, -68F, -9F);

		turretModel[123].addShapeBox(16F, -6F, 14F, 6, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 642
		turretModel[123].setRotationPoint(-6F, -68F, 10F);

		turretModel[124].addShapeBox(16F, -6F, 14F, 2, 2, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 631
		turretModel[124].setRotationPoint(-17F, -73F, -3F);

		turretModel[125].addShapeBox(16F, -6F, 14F, 1, 1, 14, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 647
		turretModel[125].setRotationPoint(-22F, -64F, 1F);

		turretModel[126].addShapeBox(16F, -6F, 14F, 1, 1, 14, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 648
		turretModel[126].setRotationPoint(-39F, -64F, 1F);

		turretModel[127].addShapeBox(16F, -6F, 14F, 16, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 649
		turretModel[127].setRotationPoint(-38F, -65F, 1F);

		turretModel[128].addShapeBox(16F, -6F, 14F, 16, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 650
		turretModel[128].setRotationPoint(-38F, -64F, 14F);


		barrelModel = new ModelRendererTurbo[41];
		barrelModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[5] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[6] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[7] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[8] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[9] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[10] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[11] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[12] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[13] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[14] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[15] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[16] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[17] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[18] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[19] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[20] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[21] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[22] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[23] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[24] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[25] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[26] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[27] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[28] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[29] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[30] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[31] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[32] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[33] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[34] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[35] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[36] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[37] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[38] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[39] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		barrelModel[40] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11

		barrelModel[0].addBox(-5F, -5F, -32F, 1, 10, 16, 0F); // Box 11
		barrelModel[0].setRotationPoint(0F, -54F, -25F);
		barrelModel[0].rotateAngleX = -0.15707963F;

		barrelModel[1].addBox(-5F, -5F, -48F, 1, 10, 16, 0F); // Box 11
		barrelModel[1].setRotationPoint(0F, -54F, -25F);
		barrelModel[1].rotateAngleX = -0.15707963F;

		barrelModel[2].addBox(-5F, -5F, -60F, 1, 10, 12, 0F); // Box 11
		barrelModel[2].setRotationPoint(0F, -54F, -25F);
		barrelModel[2].rotateAngleX = -0.15707963F;

		barrelModel[3].addBox(4F, -5F, -32F, 1, 10, 16, 0F); // Box 11
		barrelModel[3].setRotationPoint(0F, -54F, -25F);
		barrelModel[3].rotateAngleX = -0.15707963F;

		barrelModel[4].addBox(4F, -5F, -48F, 1, 10, 16, 0F); // Box 11
		barrelModel[4].setRotationPoint(0F, -54F, -25F);
		barrelModel[4].rotateAngleX = -0.15707963F;

		barrelModel[5].addBox(4F, -5F, -60F, 1, 10, 12, 0F); // Box 11
		barrelModel[5].setRotationPoint(0F, -54F, -25F);
		barrelModel[5].rotateAngleX = -0.15707963F;

		barrelModel[6].addBox(-4F, -5F, -32F, 8, 1, 16, 0F); // Box 11
		barrelModel[6].setRotationPoint(0F, -54F, -25F);
		barrelModel[6].rotateAngleX = -0.15707963F;

		barrelModel[7].addBox(-4F, -5F, -48F, 8, 1, 16, 0F); // Box 11
		barrelModel[7].setRotationPoint(0F, -54F, -25F);
		barrelModel[7].rotateAngleX = -0.15707963F;

		barrelModel[8].addBox(-4F, -5F, -60F, 8, 1, 12, 0F); // Box 11
		barrelModel[8].setRotationPoint(0F, -54F, -25F);
		barrelModel[8].rotateAngleX = -0.15707963F;

		barrelModel[9].addBox(-4F, 4F, -32F, 8, 1, 16, 0F); // Box 11
		barrelModel[9].setRotationPoint(0F, -54F, -25F);
		barrelModel[9].rotateAngleX = -0.15707963F;

		barrelModel[10].addBox(-4F, 4F, -48F, 8, 1, 16, 0F); // Box 11
		barrelModel[10].setRotationPoint(0F, -54F, -25F);
		barrelModel[10].rotateAngleX = -0.15707963F;

		barrelModel[11].addBox(-4F, 4F, -60F, 8, 1, 12, 0F); // Box 11
		barrelModel[11].setRotationPoint(0F, -54F, -25F);
		barrelModel[11].rotateAngleX = -0.15707963F;

		barrelModel[12].addBox(-19F, -8F, -6F, 38, 16, 3, 0F); // Box 11
		barrelModel[12].setRotationPoint(0F, -54F, -25F);
		barrelModel[12].rotateAngleX = -0.15707963F;

		barrelModel[13].addBox(-19F, -7F, -3F, 38, 1, 8, 0F); // Box 11
		barrelModel[13].setRotationPoint(0F, -54F, -25F);
		barrelModel[13].rotateAngleX = -0.15707963F;

		barrelModel[14].addBox(-19F, 6F, -3F, 38, 1, 8, 0F); // Box 11
		barrelModel[14].setRotationPoint(0F, -54F, -25F);
		barrelModel[14].rotateAngleX = -0.15707963F;

		barrelModel[15].addBox(-19F, -6F, -3F, 1, 12, 8, 0F); // Box 11
		barrelModel[15].setRotationPoint(0F, -54F, -25F);
		barrelModel[15].rotateAngleX = -0.15707963F;

		barrelModel[16].addBox(18F, -6F, -3F, 1, 12, 8, 0F); // Box 11
		barrelModel[16].setRotationPoint(0F, -54F, -25F);
		barrelModel[16].rotateAngleX = -0.15707963F;

		barrelModel[17].addBox(-17F, -6F, -7F, 2, 2, 1, 0F); // Box 11
		barrelModel[17].setRotationPoint(0F, -54F, -25F);
		barrelModel[17].rotateAngleX = -0.15707963F;

		barrelModel[18].addBox(-17F, 4F, -7F, 2, 2, 1, 0F); // Box 11
		barrelModel[18].setRotationPoint(0F, -54F, -25F);
		barrelModel[18].rotateAngleX = -0.15707963F;

		barrelModel[19].addBox(15F, -6F, -7F, 2, 2, 1, 0F); // Box 11
		barrelModel[19].setRotationPoint(0F, -54F, -25F);
		barrelModel[19].rotateAngleX = -0.15707963F;

		barrelModel[20].addBox(15F, 4F, -7F, 2, 2, 1, 0F); // Box 11
		barrelModel[20].setRotationPoint(0F, -54F, -25F);
		barrelModel[20].rotateAngleX = -0.15707963F;

		barrelModel[21].addBox(-6F, -5F, -58F, 1, 10, 2, 0F); // Box 11
		barrelModel[21].setRotationPoint(0F, -54F, -25F);
		barrelModel[21].rotateAngleX = -0.15707963F;

		barrelModel[22].addBox(5F, -5F, -58F, 1, 10, 2, 0F); // Box 11
		barrelModel[22].setRotationPoint(0F, -54F, -25F);
		barrelModel[22].rotateAngleX = -0.15707963F;

		barrelModel[23].addBox(-6F, -6F, -58F, 12, 1, 2, 0F); // Box 11
		barrelModel[23].setRotationPoint(0F, -54F, -25F);
		barrelModel[23].rotateAngleX = -0.15707963F;

		barrelModel[24].addBox(-6F, 5F, -58F, 12, 1, 2, 0F); // Box 11
		barrelModel[24].setRotationPoint(0F, -54F, -25F);
		barrelModel[24].rotateAngleX = -0.15707963F;

		barrelModel[25].addBox(-6F, -4F, -72F, 2, 8, 2, 0F); // Box 11
		barrelModel[25].setRotationPoint(0F, -54F, -25F);
		barrelModel[25].rotateAngleX = -0.15707963F;

		barrelModel[26].addBox(4F, -4F, -72F, 2, 8, 2, 0F); // Box 11
		barrelModel[26].setRotationPoint(0F, -54F, -25F);
		barrelModel[26].rotateAngleX = -0.15707963F;

		barrelModel[27].addBox(-6F, -6F, -72F, 12, 2, 12, 0F); // Box 11
		barrelModel[27].setRotationPoint(0F, -54F, -25F);
		barrelModel[27].rotateAngleX = -0.15707963F;

		barrelModel[28].addBox(-6F, 4F, -72F, 12, 2, 12, 0F); // Box 11
		barrelModel[28].setRotationPoint(0F, -54F, -25F);
		barrelModel[28].rotateAngleX = -0.15707963F;

		barrelModel[29].addBox(-6F, -4F, -70F, 1, 2, 5, 0F); // Box 11
		barrelModel[29].setRotationPoint(0F, -54F, -25F);
		barrelModel[29].rotateAngleX = -0.15707963F;

		barrelModel[30].addBox(5F, -4F, -70F, 1, 2, 5, 0F); // Box 11
		barrelModel[30].setRotationPoint(0F, -54F, -25F);
		barrelModel[30].rotateAngleX = -0.15707963F;

		barrelModel[31].addBox(-6F, 2F, -70F, 1, 2, 5, 0F); // Box 11
		barrelModel[31].setRotationPoint(0F, -54F, -25F);
		barrelModel[31].rotateAngleX = -0.15707963F;

		barrelModel[32].addBox(5F, 2F, -70F, 1, 2, 5, 0F); // Box 11
		barrelModel[32].setRotationPoint(0F, -54F, -25F);
		barrelModel[32].rotateAngleX = -0.15707963F;

		barrelModel[33].addBox(-6F, -4F, -63F, 1, 2, 3, 0F); // Box 11
		barrelModel[33].setRotationPoint(0F, -54F, -25F);
		barrelModel[33].rotateAngleX = -0.15707963F;

		barrelModel[34].addBox(5F, -4F, -63F, 1, 2, 5, 0F); // Box 11
		barrelModel[34].setRotationPoint(0F, -54F, -25F);
		barrelModel[34].rotateAngleX = -0.15707963F;

		barrelModel[35].addBox(-6F, 2F, -63F, 1, 2, 3, 0F); // Box 11
		barrelModel[35].setRotationPoint(0F, -54F, -25F);
		barrelModel[35].rotateAngleX = -0.15707963F;

		barrelModel[36].addBox(5F, 2F, -63F, 1, 2, 5, 0F); // Box 11
		barrelModel[36].setRotationPoint(0F, -54F, -25F);
		barrelModel[36].rotateAngleX = -0.15707963F;

		barrelModel[37].addBox(-6F, -4F, -65F, 2, 8, 2, 0F); // Box 11
		barrelModel[37].setRotationPoint(0F, -54F, -25F);
		barrelModel[37].rotateAngleX = -0.15707963F;

		barrelModel[38].addBox(4F, -4F, -65F, 2, 8, 2, 0F); // Box 11
		barrelModel[38].setRotationPoint(0F, -54F, -25F);
		barrelModel[38].rotateAngleX = -0.15707963F;

		barrelModel[39].addBox(9F, -3F, -3F, 6, 6, 2, 0F); // Box 11
		barrelModel[39].setRotationPoint(0F, -54F, -25F);
		barrelModel[39].rotateAngleX = -0.15707963F;

		barrelModel[40].addBox(10F, -2F, -1F, 4, 4, 6, 0F); // Box 11
		barrelModel[40].setRotationPoint(0F, -54F, -25F);
		barrelModel[40].rotateAngleX = -0.15707963F;


		leftFrontWheelModel = new ModelRendererTurbo[55];
		leftFrontWheelModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		leftFrontWheelModel[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		leftFrontWheelModel[5] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[6] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[7] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[8] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		leftFrontWheelModel[9] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		leftFrontWheelModel[10] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[11] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[12] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[13] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		leftFrontWheelModel[14] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[15] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		leftFrontWheelModel[16] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[17] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		leftFrontWheelModel[18] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[19] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		leftFrontWheelModel[20] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[21] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		leftFrontWheelModel[22] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[23] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[24] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[25] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[26] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[27] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[28] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[29] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[30] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[31] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[32] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[33] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[34] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[35] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[36] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[37] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[38] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[39] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[40] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[41] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[42] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[43] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[44] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[45] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[46] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[47] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[48] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[49] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[50] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[51] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[52] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[53] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		leftFrontWheelModel[54] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11

		leftFrontWheelModel[0].addBox(2F, -6F, -6F, 2, 12, 12, 0F); // Box 11
		leftFrontWheelModel[0].setRotationPoint(-40F, -21F, -46F);

		leftFrontWheelModel[1].addBox(6F, -6F, -6F, 2, 12, 12, 0F); // Box 11
		leftFrontWheelModel[1].setRotationPoint(-40F, -21F, -46F);

		leftFrontWheelModel[2].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		leftFrontWheelModel[2].setRotationPoint(-41F, -21F, -46F);

		leftFrontWheelModel[3].addShape3D(9F, -9F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(16, 0, 16, 0), new Coord2D(18, 2, 18, 2), new Coord2D(18, 16, 18, 16), new Coord2D(16, 18, 16, 18), new Coord2D(2, 18, 2, 18), new Coord2D(0, 16, 0, 16), new Coord2D(0, 2, 0, 2) }), 2, 18, 18, 68, 2, ModelRendererTurbo.MR_FRONT, new float[] {3 ,14 ,3 ,14 ,3 ,14 ,3 ,14}); // Shape 5
		leftFrontWheelModel[3].setRotationPoint(34F, -21F, -46F);
		leftFrontWheelModel[3].rotateAngleY = 1.57079633F;

		leftFrontWheelModel[4].addShape3D(9F, -9F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(16, 0, 16, 0), new Coord2D(18, 2, 18, 2), new Coord2D(18, 16, 18, 16), new Coord2D(16, 18, 16, 18), new Coord2D(2, 18, 2, 18), new Coord2D(0, 16, 0, 16), new Coord2D(0, 2, 0, 2) }), 2, 18, 18, 68, 2, ModelRendererTurbo.MR_FRONT, new float[] {3 ,14 ,3 ,14 ,3 ,14 ,3 ,14}); // Shape 5
		leftFrontWheelModel[4].setRotationPoint(38F, -21F, -46F);
		leftFrontWheelModel[4].rotateAngleY = 1.57079633F;

		leftFrontWheelModel[5].addBox(2F, -6F, -6F, 2, 12, 12, 0F); // Box 11
		leftFrontWheelModel[5].setRotationPoint(34F, -21F, -46F);

		leftFrontWheelModel[6].addBox(6F, -6F, -6F, 2, 12, 12, 0F); // Box 11
		leftFrontWheelModel[6].setRotationPoint(26F, -21F, -46F);

		leftFrontWheelModel[7].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		leftFrontWheelModel[7].setRotationPoint(40F, -21F, -46F);

		leftFrontWheelModel[8].addShape3D(9F, -9F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(16, 0, 16, 0), new Coord2D(18, 2, 18, 2), new Coord2D(18, 16, 18, 16), new Coord2D(16, 18, 16, 18), new Coord2D(2, 18, 2, 18), new Coord2D(0, 16, 0, 16), new Coord2D(0, 2, 0, 2) }), 2, 18, 18, 68, 2, ModelRendererTurbo.MR_FRONT, new float[] {3 ,14 ,3 ,14 ,3 ,14 ,3 ,14}); // Shape 5
		leftFrontWheelModel[8].setRotationPoint(34F, -21F, 60F);
		leftFrontWheelModel[8].rotateAngleY = 1.57079633F;

		leftFrontWheelModel[9].addShape3D(9F, -9F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(16, 0, 16, 0), new Coord2D(18, 2, 18, 2), new Coord2D(18, 16, 18, 16), new Coord2D(16, 18, 16, 18), new Coord2D(2, 18, 2, 18), new Coord2D(0, 16, 0, 16), new Coord2D(0, 2, 0, 2) }), 2, 18, 18, 68, 2, ModelRendererTurbo.MR_FRONT, new float[] {3 ,14 ,3 ,14 ,3 ,14 ,3 ,14}); // Shape 5
		leftFrontWheelModel[9].setRotationPoint(38F, -21F, 60F);
		leftFrontWheelModel[9].rotateAngleY = 1.57079633F;

		leftFrontWheelModel[10].addBox(2F, -6F, -6F, 2, 12, 12, 0F); // Box 11
		leftFrontWheelModel[10].setRotationPoint(34F, -21F, 60F);

		leftFrontWheelModel[11].addBox(6F, -6F, -6F, 2, 12, 12, 0F); // Box 11
		leftFrontWheelModel[11].setRotationPoint(26F, -21F, 60F);

		leftFrontWheelModel[12].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		leftFrontWheelModel[12].setRotationPoint(40F, -21F, 60F);

		leftFrontWheelModel[13].addShape3D(8F, -8F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(14, 0, 14, 0), new Coord2D(16, 2, 16, 2), new Coord2D(16, 14, 16, 14), new Coord2D(14, 16, 14, 16), new Coord2D(2, 16, 2, 16), new Coord2D(0, 14, 0, 14), new Coord2D(0, 2, 0, 2) }), 6, 16, 16, 60, 6, ModelRendererTurbo.MR_FRONT, new float[] {3 ,12 ,3 ,12 ,3 ,12 ,3 ,12}); // Shape 5
		leftFrontWheelModel[13].setRotationPoint(33F, -6F, -28F);
		leftFrontWheelModel[13].rotateAngleY = 1.57079633F;

		leftFrontWheelModel[14].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		leftFrontWheelModel[14].setRotationPoint(39F, -6F, -28F);

		leftFrontWheelModel[15].addShape3D(8F, -8F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(14, 0, 14, 0), new Coord2D(16, 2, 16, 2), new Coord2D(16, 14, 16, 14), new Coord2D(14, 16, 14, 16), new Coord2D(2, 16, 2, 16), new Coord2D(0, 14, 0, 14), new Coord2D(0, 2, 0, 2) }), 6, 16, 16, 60, 6, ModelRendererTurbo.MR_FRONT, new float[] {3 ,12 ,3 ,12 ,3 ,12 ,3 ,12}); // Shape 5
		leftFrontWheelModel[15].setRotationPoint(33F, -6F, -10F);
		leftFrontWheelModel[15].rotateAngleY = 1.57079633F;

		leftFrontWheelModel[16].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		leftFrontWheelModel[16].setRotationPoint(39F, -6F, -10F);

		leftFrontWheelModel[17].addShape3D(8F, -8F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(14, 0, 14, 0), new Coord2D(16, 2, 16, 2), new Coord2D(16, 14, 16, 14), new Coord2D(14, 16, 14, 16), new Coord2D(2, 16, 2, 16), new Coord2D(0, 14, 0, 14), new Coord2D(0, 2, 0, 2) }), 6, 16, 16, 60, 6, ModelRendererTurbo.MR_FRONT, new float[] {3 ,12 ,3 ,12 ,3 ,12 ,3 ,12}); // Shape 5
		leftFrontWheelModel[17].setRotationPoint(33F, -6F, 8F);
		leftFrontWheelModel[17].rotateAngleY = 1.57079633F;

		leftFrontWheelModel[18].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		leftFrontWheelModel[18].setRotationPoint(39F, -6F, 8F);

		leftFrontWheelModel[19].addShape3D(8F, -8F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(14, 0, 14, 0), new Coord2D(16, 2, 16, 2), new Coord2D(16, 14, 16, 14), new Coord2D(14, 16, 14, 16), new Coord2D(2, 16, 2, 16), new Coord2D(0, 14, 0, 14), new Coord2D(0, 2, 0, 2) }), 6, 16, 16, 60, 6, ModelRendererTurbo.MR_FRONT, new float[] {3 ,12 ,3 ,12 ,3 ,12 ,3 ,12}); // Shape 5
		leftFrontWheelModel[19].setRotationPoint(33F, -6F, 26F);
		leftFrontWheelModel[19].rotateAngleY = 1.57079633F;

		leftFrontWheelModel[20].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		leftFrontWheelModel[20].setRotationPoint(39F, -6F, 26F);

		leftFrontWheelModel[21].addShape3D(8F, -8F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(14, 0, 14, 0), new Coord2D(16, 2, 16, 2), new Coord2D(16, 14, 16, 14), new Coord2D(14, 16, 14, 16), new Coord2D(2, 16, 2, 16), new Coord2D(0, 14, 0, 14), new Coord2D(0, 2, 0, 2) }), 6, 16, 16, 60, 6, ModelRendererTurbo.MR_FRONT, new float[] {3 ,12 ,3 ,12 ,3 ,12 ,3 ,12}); // Shape 5
		leftFrontWheelModel[21].setRotationPoint(33F, -6F, 44F);
		leftFrontWheelModel[21].rotateAngleY = 1.57079633F;

		leftFrontWheelModel[22].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		leftFrontWheelModel[22].setRotationPoint(39F, -6F, 44F);

		leftFrontWheelModel[23].addBox(-7F, -10F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[23].setRotationPoint(40F, -21F, -46F);

		leftFrontWheelModel[24].addBox(-7F, -10F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[24].setRotationPoint(40F, -21F, -46F);
		leftFrontWheelModel[24].rotateAngleX = 1.57079633F;

		leftFrontWheelModel[25].addBox(-7F, -10F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[25].setRotationPoint(40F, -21F, -46F);
		leftFrontWheelModel[25].rotateAngleX = -1.57079633F;

		leftFrontWheelModel[26].addBox(-7F, -10F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[26].setRotationPoint(40F, -21F, -46F);
		leftFrontWheelModel[26].rotateAngleX = -3.14159265F;

		leftFrontWheelModel[27].addBox(-7F, -11.5F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[27].setRotationPoint(40F, -21F, -46F);
		leftFrontWheelModel[27].rotateAngleX = -0.78539816F;

		leftFrontWheelModel[28].addBox(-7F, -11.5F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[28].setRotationPoint(40F, -21F, -46F);
		leftFrontWheelModel[28].rotateAngleX = 0.78539816F;

		leftFrontWheelModel[29].addBox(-7F, -11.5F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[29].setRotationPoint(40F, -21F, -46F);
		leftFrontWheelModel[29].rotateAngleX = -3.92699082F;

		leftFrontWheelModel[30].addBox(-7F, -11.5F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[30].setRotationPoint(40F, -21F, -46F);
		leftFrontWheelModel[30].rotateAngleX = -2.35619449F;

		leftFrontWheelModel[31].addBox(-7F, -10F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[31].setRotationPoint(40F, -21F, 60F);

		leftFrontWheelModel[32].addBox(-7F, -10F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[32].setRotationPoint(40F, -21F, 60F);
		leftFrontWheelModel[32].rotateAngleX = 1.57079633F;

		leftFrontWheelModel[33].addBox(-7F, -10F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[33].setRotationPoint(40F, -21F, 60F);
		leftFrontWheelModel[33].rotateAngleX = -1.57079633F;

		leftFrontWheelModel[34].addBox(-7F, -10F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[34].setRotationPoint(40F, -21F, 60F);
		leftFrontWheelModel[34].rotateAngleX = -3.14159265F;

		leftFrontWheelModel[35].addBox(-7F, -11.5F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[35].setRotationPoint(40F, -21F, 60F);
		leftFrontWheelModel[35].rotateAngleX = -0.78539816F;

		leftFrontWheelModel[36].addBox(-7F, -11.5F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[36].setRotationPoint(40F, -21F, 60F);
		leftFrontWheelModel[36].rotateAngleX = 0.78539816F;

		leftFrontWheelModel[37].addBox(-7F, -11.5F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[37].setRotationPoint(40F, -21F, 60F);
		leftFrontWheelModel[37].rotateAngleX = -3.92699082F;

		leftFrontWheelModel[38].addBox(-7F, -11.5F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[38].setRotationPoint(40F, -21F, 60F);
		leftFrontWheelModel[38].rotateAngleX = -2.35619449F;

		leftFrontWheelModel[39].addBox(-7F, -10F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[39].setRotationPoint(-34F, -21F, -46F);

		leftFrontWheelModel[40].addBox(-7F, -10F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[40].setRotationPoint(-34F, -21F, -46F);
		leftFrontWheelModel[40].rotateAngleX = 1.57079633F;

		leftFrontWheelModel[41].addBox(-7F, -10F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[41].setRotationPoint(-34F, -21F, -46F);
		leftFrontWheelModel[41].rotateAngleX = -1.57079633F;

		leftFrontWheelModel[42].addBox(-7F, -10F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[42].setRotationPoint(-34F, -21F, -46F);
		leftFrontWheelModel[42].rotateAngleX = -3.14159265F;

		leftFrontWheelModel[43].addBox(-7F, -11.5F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[43].setRotationPoint(-34F, -21F, -46F);
		leftFrontWheelModel[43].rotateAngleX = -0.78539816F;

		leftFrontWheelModel[44].addBox(-7F, -11.5F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[44].setRotationPoint(-34F, -21F, -46F);
		leftFrontWheelModel[44].rotateAngleX = 0.78539816F;

		leftFrontWheelModel[45].addBox(-7F, -11.5F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[45].setRotationPoint(-34F, -21F, -46F);
		leftFrontWheelModel[45].rotateAngleX = -3.92699082F;

		leftFrontWheelModel[46].addBox(-7F, -11.5F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[46].setRotationPoint(-34F, -21F, -46F);
		leftFrontWheelModel[46].rotateAngleX = -2.35619449F;

		leftFrontWheelModel[47].addBox(-7F, -10F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[47].setRotationPoint(-34F, -21F, 60F);

		leftFrontWheelModel[48].addBox(-7F, -10F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[48].setRotationPoint(-34F, -21F, 60F);
		leftFrontWheelModel[48].rotateAngleX = 1.57079633F;

		leftFrontWheelModel[49].addBox(-7F, -10F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[49].setRotationPoint(-34F, -21F, 60F);
		leftFrontWheelModel[49].rotateAngleX = -1.57079633F;

		leftFrontWheelModel[50].addBox(-7F, -10F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[50].setRotationPoint(-34F, -21F, 60F);
		leftFrontWheelModel[50].rotateAngleX = -3.14159265F;

		leftFrontWheelModel[51].addBox(-7F, -11.5F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[51].setRotationPoint(-34F, -21F, 60F);
		leftFrontWheelModel[51].rotateAngleX = -0.78539816F;

		leftFrontWheelModel[52].addBox(-7F, -11.5F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[52].setRotationPoint(-34F, -21F, 60F);
		leftFrontWheelModel[52].rotateAngleX = 0.78539816F;

		leftFrontWheelModel[53].addBox(-7F, -11.5F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[53].setRotationPoint(-34F, -21F, 60F);
		leftFrontWheelModel[53].rotateAngleX = -3.92699082F;

		leftFrontWheelModel[54].addBox(-7F, -11.5F, -3F, 8, 2, 6, 0F); // Box 11
		leftFrontWheelModel[54].setRotationPoint(-34F, -21F, 60F);
		leftFrontWheelModel[54].rotateAngleX = -2.35619449F;


		rightFrontWheelModel = new ModelRendererTurbo[23];
		rightFrontWheelModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		rightFrontWheelModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		rightFrontWheelModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		rightFrontWheelModel[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		rightFrontWheelModel[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		rightFrontWheelModel[5] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		rightFrontWheelModel[6] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		rightFrontWheelModel[7] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		rightFrontWheelModel[8] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		rightFrontWheelModel[9] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		rightFrontWheelModel[10] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		rightFrontWheelModel[11] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		rightFrontWheelModel[12] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		rightFrontWheelModel[13] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		rightFrontWheelModel[14] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		rightFrontWheelModel[15] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		rightFrontWheelModel[16] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		rightFrontWheelModel[17] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightFrontWheelModel[18] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightFrontWheelModel[19] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightFrontWheelModel[20] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightFrontWheelModel[21] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightFrontWheelModel[22] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0

		rightFrontWheelModel[0].addShape3D(9F, -9F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(16, 0, 16, 0), new Coord2D(18, 2, 18, 2), new Coord2D(18, 16, 18, 16), new Coord2D(16, 18, 16, 18), new Coord2D(2, 18, 2, 18), new Coord2D(0, 16, 0, 16), new Coord2D(0, 2, 0, 2) }), 2, 18, 18, 68, 2, ModelRendererTurbo.MR_FRONT, new float[] {3 ,14 ,3 ,14 ,3 ,14 ,3 ,14}); // Shape 5
		rightFrontWheelModel[0].setRotationPoint(-40F, -21F, -46F);
		rightFrontWheelModel[0].rotateAngleY = 1.57079633F;

		rightFrontWheelModel[1].addShape3D(9F, -9F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(16, 0, 16, 0), new Coord2D(18, 2, 18, 2), new Coord2D(18, 16, 18, 16), new Coord2D(16, 18, 16, 18), new Coord2D(2, 18, 2, 18), new Coord2D(0, 16, 0, 16), new Coord2D(0, 2, 0, 2) }), 2, 18, 18, 68, 2, ModelRendererTurbo.MR_FRONT, new float[] {3 ,14 ,3 ,14 ,3 ,14 ,3 ,14}); // Shape 5
		rightFrontWheelModel[1].setRotationPoint(-36F, -21F, -46F);
		rightFrontWheelModel[1].rotateAngleY = 1.57079633F;

		rightFrontWheelModel[2].addShape3D(9F, -9F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(16, 0, 16, 0), new Coord2D(18, 2, 18, 2), new Coord2D(18, 16, 18, 16), new Coord2D(16, 18, 16, 18), new Coord2D(2, 18, 2, 18), new Coord2D(0, 16, 0, 16), new Coord2D(0, 2, 0, 2) }), 2, 18, 18, 68, 2, ModelRendererTurbo.MR_FRONT, new float[] {3 ,14 ,3 ,14 ,3 ,14 ,3 ,14}); // Shape 5
		rightFrontWheelModel[2].setRotationPoint(-40F, -21F, 60F);
		rightFrontWheelModel[2].rotateAngleY = 1.57079633F;

		rightFrontWheelModel[3].addShape3D(9F, -9F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(16, 0, 16, 0), new Coord2D(18, 2, 18, 2), new Coord2D(18, 16, 18, 16), new Coord2D(16, 18, 16, 18), new Coord2D(2, 18, 2, 18), new Coord2D(0, 16, 0, 16), new Coord2D(0, 2, 0, 2) }), 2, 18, 18, 68, 2, ModelRendererTurbo.MR_FRONT, new float[] {3 ,14 ,3 ,14 ,3 ,14 ,3 ,14}); // Shape 5
		rightFrontWheelModel[3].setRotationPoint(-36F, -21F, 60F);
		rightFrontWheelModel[3].rotateAngleY = 1.57079633F;

		rightFrontWheelModel[4].addBox(2F, -6F, -6F, 2, 12, 12, 0F); // Box 11
		rightFrontWheelModel[4].setRotationPoint(-40F, -21F, 60F);

		rightFrontWheelModel[5].addBox(6F, -6F, -6F, 2, 12, 12, 0F); // Box 11
		rightFrontWheelModel[5].setRotationPoint(-40F, -21F, 60F);

		rightFrontWheelModel[6].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		rightFrontWheelModel[6].setRotationPoint(-41F, -21F, 60F);

		rightFrontWheelModel[7].addShape3D(8F, -8F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(14, 0, 14, 0), new Coord2D(16, 2, 16, 2), new Coord2D(16, 14, 16, 14), new Coord2D(14, 16, 14, 16), new Coord2D(2, 16, 2, 16), new Coord2D(0, 14, 0, 14), new Coord2D(0, 2, 0, 2) }), 6, 16, 16, 60, 6, ModelRendererTurbo.MR_FRONT, new float[] {3 ,12 ,3 ,12 ,3 ,12 ,3 ,12}); // Shape 5
		rightFrontWheelModel[7].setRotationPoint(-39F, -6F, -28F);
		rightFrontWheelModel[7].rotateAngleY = 1.57079633F;

		rightFrontWheelModel[8].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		rightFrontWheelModel[8].setRotationPoint(-40F, -6F, -28F);

		rightFrontWheelModel[9].addShape3D(8F, -8F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(14, 0, 14, 0), new Coord2D(16, 2, 16, 2), new Coord2D(16, 14, 16, 14), new Coord2D(14, 16, 14, 16), new Coord2D(2, 16, 2, 16), new Coord2D(0, 14, 0, 14), new Coord2D(0, 2, 0, 2) }), 6, 16, 16, 60, 6, ModelRendererTurbo.MR_FRONT, new float[] {3 ,12 ,3 ,12 ,3 ,12 ,3 ,12}); // Shape 5
		rightFrontWheelModel[9].setRotationPoint(-39F, -6F, -10F);
		rightFrontWheelModel[9].rotateAngleY = 1.57079633F;

		rightFrontWheelModel[10].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		rightFrontWheelModel[10].setRotationPoint(-40F, -6F, -10F);

		rightFrontWheelModel[11].addShape3D(8F, -8F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(14, 0, 14, 0), new Coord2D(16, 2, 16, 2), new Coord2D(16, 14, 16, 14), new Coord2D(14, 16, 14, 16), new Coord2D(2, 16, 2, 16), new Coord2D(0, 14, 0, 14), new Coord2D(0, 2, 0, 2) }), 6, 16, 16, 60, 6, ModelRendererTurbo.MR_FRONT, new float[] {3 ,12 ,3 ,12 ,3 ,12 ,3 ,12}); // Shape 5
		rightFrontWheelModel[11].setRotationPoint(-39F, -6F, 8F);
		rightFrontWheelModel[11].rotateAngleY = 1.57079633F;

		rightFrontWheelModel[12].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		rightFrontWheelModel[12].setRotationPoint(-40F, -6F, 8F);

		rightFrontWheelModel[13].addShape3D(8F, -8F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(14, 0, 14, 0), new Coord2D(16, 2, 16, 2), new Coord2D(16, 14, 16, 14), new Coord2D(14, 16, 14, 16), new Coord2D(2, 16, 2, 16), new Coord2D(0, 14, 0, 14), new Coord2D(0, 2, 0, 2) }), 6, 16, 16, 60, 6, ModelRendererTurbo.MR_FRONT, new float[] {3 ,12 ,3 ,12 ,3 ,12 ,3 ,12}); // Shape 5
		rightFrontWheelModel[13].setRotationPoint(-39F, -6F, 26F);
		rightFrontWheelModel[13].rotateAngleY = 1.57079633F;

		rightFrontWheelModel[14].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		rightFrontWheelModel[14].setRotationPoint(-40F, -6F, 26F);

		rightFrontWheelModel[15].addShape3D(8F, -8F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(14, 0, 14, 0), new Coord2D(16, 2, 16, 2), new Coord2D(16, 14, 16, 14), new Coord2D(14, 16, 14, 16), new Coord2D(2, 16, 2, 16), new Coord2D(0, 14, 0, 14), new Coord2D(0, 2, 0, 2) }), 6, 16, 16, 60, 6, ModelRendererTurbo.MR_FRONT, new float[] {3 ,12 ,3 ,12 ,3 ,12 ,3 ,12}); // Shape 5
		rightFrontWheelModel[15].setRotationPoint(-39F, -6F, 44F);
		rightFrontWheelModel[15].rotateAngleY = 1.57079633F;

		rightFrontWheelModel[16].addBox(0F, -4F, -4F, 1, 8, 8, 0F); // Box 11
		rightFrontWheelModel[16].setRotationPoint(-40F, -6F, 44F);

		rightFrontWheelModel[17].addBox(0F, -4F, -4F, 6, 8, 8, 0F); // Box 0
		rightFrontWheelModel[17].setRotationPoint(-39F, -27F, 36F);

		rightFrontWheelModel[18].addBox(0F, -4F, -4F, 6, 8, 8, 0F); // Box 0
		rightFrontWheelModel[18].setRotationPoint(-39F, -27F, 8F);

		rightFrontWheelModel[19].addBox(0F, -4F, -4F, 6, 8, 8, 0F); // Box 0
		rightFrontWheelModel[19].setRotationPoint(-39F, -27F, -20F);

		rightFrontWheelModel[20].addBox(0F, -4F, -4F, 6, 8, 8, 0F); // Box 0
		rightFrontWheelModel[20].setRotationPoint(33F, -27F, 36F);

		rightFrontWheelModel[21].addBox(0F, -4F, -4F, 6, 8, 8, 0F); // Box 0
		rightFrontWheelModel[21].setRotationPoint(33F, -27F, 8F);

		rightFrontWheelModel[22].addBox(0F, -4F, -4F, 6, 8, 8, 0F); // Box 0
		rightFrontWheelModel[22].setRotationPoint(33F, -27F, -20F);


		leftBackWheelModel = new ModelRendererTurbo[1];
		leftBackWheelModel[0] = new ModelRendererTurbo(this, 1, 62, textureX, textureY); // Box 662

		leftBackWheelModel[0].addShapeBox(0F, 0F, 0F, 14, 2, 36, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 662
		leftBackWheelModel[0].setRotationPoint(0F, -39.5F, 31F);


		rightBackWheelModel = new ModelRendererTurbo[1];
		rightBackWheelModel[0] = new ModelRendererTurbo(this, 92, 49, textureX, textureY); // Box 663

		rightBackWheelModel[0].addShapeBox(0F, 0F, 0F, 14, 2, 36, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 663
		rightBackWheelModel[0].setRotationPoint(-14F, -39.5F, 31F);


		leftTrackWheelModels = new ModelRendererTurbo[5];
		leftTrackWheelModels[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		leftTrackWheelModels[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		leftTrackWheelModels[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		leftTrackWheelModels[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		leftTrackWheelModels[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0

		leftTrackWheelModels[0].addBox(-1F, -0.5F, 0F, 1, 1, 36, 0F); // Box 0
		leftTrackWheelModels[0].setRotationPoint(-29F, -40.5F, 31F);

		leftTrackWheelModels[1].addBox(14F, -0.5F, 0F, 1, 1, 36, 0F); // Box 0
		leftTrackWheelModels[1].setRotationPoint(-29F, -40.5F, 31F);

		leftTrackWheelModels[2].addBox(0F, -0.5F, 0F, 14, 1, 20, 0F); // Box 0
		leftTrackWheelModels[2].setRotationPoint(-29F, -40.5F, 31F);

		leftTrackWheelModels[3].addBox(0F, -0.5F, 35F, 14, 1, 1, 0F); // Box 0
		leftTrackWheelModels[3].setRotationPoint(-29F, -40.5F, 31F);

		leftTrackWheelModels[4].addBox(0F, 0F, 20F, 14, 0, 15, 0F); // Box 0
		leftTrackWheelModels[4].setRotationPoint(-29F, -40.5F, 31F);


		rightTrackWheelModels = new ModelRendererTurbo[5];
		rightTrackWheelModels[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackWheelModels[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackWheelModels[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackWheelModels[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackWheelModels[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0

		rightTrackWheelModels[0].addBox(-16F, -0.5F, 0F, 1, 1, 36, 0F); // Box 0
		rightTrackWheelModels[0].setRotationPoint(30F, -40.5F, 31F);

		rightTrackWheelModels[1].addBox(-1F, -0.5F, 0F, 1, 1, 36, 0F); // Box 0
		rightTrackWheelModels[1].setRotationPoint(30F, -40.5F, 31F);

		rightTrackWheelModels[2].addBox(-15F, -0.5F, 0F, 14, 1, 20, 0F); // Box 0
		rightTrackWheelModels[2].setRotationPoint(30F, -40.5F, 31F);

		rightTrackWheelModels[3].addBox(-15F, -0.5F, 35F, 14, 1, 1, 0F); // Box 0
		rightTrackWheelModels[3].setRotationPoint(30F, -40.5F, 31F);

		rightTrackWheelModels[4].addBox(-15F, 0F, 20F, 14, 0, 15, 0F); // Box 0
		rightTrackWheelModels[4].setRotationPoint(30F, -40.5F, 31F);


		frontWheelModel = new ModelRendererTurbo[2];
		frontWheelModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		frontWheelModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23

		frontWheelModel[0].addBox(-0.5F, -5F, -0.5F, 1, 4, 1, 0F); // Box 23
		frontWheelModel[0].setRotationPoint(6F, -18F, -46F);

		frontWheelModel[1].addBox(-1F, -6F, -1F, 2, 2, 2, 0F); // Box 23
		frontWheelModel[1].setRotationPoint(6F, -18F, -46F);


		backWheelModel = new ModelRendererTurbo[3];
		backWheelModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		backWheelModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		backWheelModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23

		backWheelModel[0].addBox(0F, 0F, 0F, 4, 1, 5, 0F); // Box 23
		backWheelModel[0].setRotationPoint(13F, -15F, -55F);
		backWheelModel[0].rotateAngleX = -0.34906585F;

		backWheelModel[1].addBox(0F, 0F, 0F, 4, 1, 5, 0F); // Box 23
		backWheelModel[1].setRotationPoint(19F, -15F, -55F);
		backWheelModel[1].rotateAngleX = -0.34906585F;

		backWheelModel[2].addBox(0F, 0F, 0F, 4, 1, 6, 0F); // Box 23
		backWheelModel[2].setRotationPoint(25F, -15F, -55F);
		backWheelModel[2].rotateAngleX = -0.34906585F;


		leftTrackModel = new ModelRendererTurbo[16];
		leftTrackModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		leftTrackModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		leftTrackModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		leftTrackModel[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		leftTrackModel[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		leftTrackModel[5] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		leftTrackModel[6] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		leftTrackModel[7] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		leftTrackModel[8] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		leftTrackModel[9] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		leftTrackModel[10] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		leftTrackModel[11] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		leftTrackModel[12] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		leftTrackModel[13] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		leftTrackModel[14] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		leftTrackModel[15] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23

		leftTrackModel[0].addBox(-18.5F, -0.5F, -1F, 18, 1, 18, 0F); // Box 23
		leftTrackModel[0].setRotationPoint(25.5F, -39.5F, -48F);

		leftTrackModel[1].addBox(-12.5F, -2F, 0.5F, 6, 2, 4, 0F); // Box 23
		leftTrackModel[1].setRotationPoint(25.5F, -39.5F, -48F);

		leftTrackModel[2].addBox(-13F, 3F, 1.5F, 3, 1, 3, 0F); // Box 23
		leftTrackModel[2].setRotationPoint(25.5F, -39.5F, -48F);

		leftTrackModel[3].addShapeBox(-13F, 3F, 0.5F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 23
		leftTrackModel[3].setRotationPoint(25.5F, -39.5F, -48F);

		leftTrackModel[4].addBox(-17.5F, -1.5F, 0F, 2, 1, 2, 0F); // Box 23
		leftTrackModel[4].setRotationPoint(25.5F, -39.5F, -48F);

		leftTrackModel[5].addBox(-3.5F, -1.5F, 0F, 2, 1, 2, 0F); // Box 23
		leftTrackModel[5].setRotationPoint(25.5F, -39.5F, -48F);

		leftTrackModel[6].addBox(-17.5F, -1.5F, 14F, 2, 1, 2, 0F); // Box 23
		leftTrackModel[6].setRotationPoint(25.5F, -39.5F, -48F);

		leftTrackModel[7].addBox(-3.5F, -1.5F, 14F, 2, 1, 2, 0F); // Box 23
		leftTrackModel[7].setRotationPoint(25.5F, -39.5F, -48F);

		leftTrackModel[8].addBox(-0.5F, -0.5F, 0F, 1, 1, 4, 0F); // Box 23
		leftTrackModel[8].setRotationPoint(25.5F, -39.5F, -48F);

		leftTrackModel[9].addBox(-0.5F, -0.5F, 12F, 1, 1, 4, 0F); // Box 23
		leftTrackModel[9].setRotationPoint(25.5F, -39.5F, -48F);

		leftTrackModel[10].addBox(-13F, 0F, 0.5F, 3, 3, 4, 0F); // Box 23
		leftTrackModel[10].setRotationPoint(25.5F, -39.5F, -48F);

		leftTrackModel[11].addBox(-12.5F, -3F, 0.5F, 6, 1, 3, 0F); // Box 23
		leftTrackModel[11].setRotationPoint(25.5F, -39.5F, -48F);

		leftTrackModel[12].addShapeBox(-12.5F, -3F, 3.5F, 6, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 23
		leftTrackModel[12].setRotationPoint(25.5F, -39.5F, -48F);

		leftTrackModel[13].addBox(-9F, 3F, 1.5F, 3, 1, 3, 0F); // Box 23
		leftTrackModel[13].setRotationPoint(25.5F, -39.5F, -48F);

		leftTrackModel[14].addShapeBox(-9F, 3F, 0.5F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 23
		leftTrackModel[14].setRotationPoint(25.5F, -39.5F, -48F);

		leftTrackModel[15].addBox(-9F, 0F, 0.5F, 3, 3, 4, 0F); // Box 23
		leftTrackModel[15].setRotationPoint(25.5F, -39.5F, -48F);


		rightTrackModel = new ModelRendererTurbo[30];
		rightTrackModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[5] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[6] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[7] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[8] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[9] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[10] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[11] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[12] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[13] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[14] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[15] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[16] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[17] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 357
		rightTrackModel[18] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[19] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[20] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[21] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[22] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[23] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[24] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[25] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[26] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[27] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[28] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightTrackModel[29] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0

		rightTrackModel[0].addBox(-3F, 0F, -14F, 6, 2, 2, 0F); // Box 0
		rightTrackModel[0].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[1].addBox(3F, 0F, -14F, 1, 7, 2, 0F); // Box 0
		rightTrackModel[1].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[2].addBox(-10F, 0F, -14F, 4, 7, 2, 0F); // Box 0
		rightTrackModel[2].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[3].addBox(10F, 0F, -15F, 2, 7, 21, 0F); // Box 0
		rightTrackModel[3].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[4].addBox(-12F, 0F, -15F, 2, 7, 21, 0F); // Box 0
		rightTrackModel[4].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[5].addShapeBox(-12F, 0F, 6F, 2, 7, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 3F, 0F, -2F, -5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 3F, 0F, -2F, -5F, 0F, 0F); // Box 0
		rightTrackModel[5].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[6].addShapeBox(10F, 0F, 6F, 2, 7, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -5F, 0F, 0F, 3F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, -5F, 0F, 0F, 3F, 0F, -2F); // Box 0
		rightTrackModel[6].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[7].addBox(-4F, 0F, -14F, 1, 7, 2, 0F); // Box 0
		rightTrackModel[7].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[8].addBox(6F, 0F, -14F, 4, 7, 2, 0F); // Box 0
		rightTrackModel[8].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[9].addBox(-6F, 0F, -14F, 2, 1, 2, 0F); // Box 0
		rightTrackModel[9].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[10].addBox(-6F, 0F, -14F, 2, 1, 2, 0F); // Box 0
		rightTrackModel[10].setRotationPoint(-12F, -37F, -38F);

		rightTrackModel[11].addBox(4F, 0F, -14F, 2, 1, 2, 0F); // Box 0
		rightTrackModel[11].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[12].addBox(4F, 0F, -14F, 2, 1, 2, 0F); // Box 0
		rightTrackModel[12].setRotationPoint(-12F, -37F, -38F);

		rightTrackModel[13].addBox(-3F, 0F, -14F, 6, 2, 2, 0F); // Box 0
		rightTrackModel[13].setRotationPoint(-12F, -38F, -38F);

		rightTrackModel[14].addBox(-3F, 0F, -13F, 6, 3, 18, 0F); // Box 0
		rightTrackModel[14].setRotationPoint(-12F, -41F, -38F);

		rightTrackModel[15].addBox(-7F, 0F, 10F, 14, 7, 2, 0F); // Box 0
		rightTrackModel[15].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[16].addBox(-12F, -1F, -15F, 24, 1, 14, 0F); // Box 0
		rightTrackModel[16].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[17].addShape3D(12F, -12F, -1F, new Shape2D(new Coord2D[] { new Coord2D(5, 0, 5, 0), new Coord2D(19, 0, 19, 0), new Coord2D(24, 6, 24, 6), new Coord2D(0, 6, 0, 6) }), 1, 24, 6, 54, 1, ModelRendererTurbo.MR_FRONT, new float[] {8 ,24 ,8 ,14}); // Shape 357
		rightTrackModel[17].setRotationPoint(-12F, -43F, -38F);
		rightTrackModel[17].rotateAngleX = 1.57079633F;

		rightTrackModel[18].addBox(-11F, -2F, -14F, 2, 1, 2, 0F); // Box 0
		rightTrackModel[18].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[19].addBox(9F, -2F, -14F, 2, 1, 2, 0F); // Box 0
		rightTrackModel[19].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[20].addBox(-7F, -2F, 4F, 2, 1, 2, 0F); // Box 0
		rightTrackModel[20].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[21].addBox(4F, -2F, 4F, 2, 1, 2, 0F); // Box 0
		rightTrackModel[21].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[22].addBox(-7F, -2F, 9F, 2, 1, 2, 0F); // Box 0
		rightTrackModel[22].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[23].addBox(4F, -2F, 9F, 2, 1, 2, 0F); // Box 0
		rightTrackModel[23].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[24].addBox(-3F, 0F, -13.5F, 6, 3, 0, 0F); // Box 0
		rightTrackModel[24].setRotationPoint(-12F, -41F, -38F);

		rightTrackModel[25].addBox(-9F, -1F, -1F, 18, 1, 7, 0F); // Box 0
		rightTrackModel[25].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[26].addBox(-12F, -1F, -1F, 3, 1, 1, 0F); // Box 0
		rightTrackModel[26].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[27].addBox(-12F, -1F, 4F, 3, 1, 2, 0F); // Box 0
		rightTrackModel[27].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[28].addBox(9F, -1F, -1F, 3, 1, 1, 0F); // Box 0
		rightTrackModel[28].setRotationPoint(-12F, -43F, -38F);

		rightTrackModel[29].addBox(9F, -1F, 4F, 3, 1, 2, 0F); // Box 0
		rightTrackModel[29].setRotationPoint(-12F, -43F, -38F);


		trailerModel = new ModelRendererTurbo[177];
		trailerModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		trailerModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[5] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[6] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[7] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[8] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[9] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[10] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[11] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[12] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[13] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[14] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[15] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[16] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[17] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[18] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[19] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[20] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[21] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[22] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[23] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[24] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[25] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[26] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[27] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[28] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[29] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[30] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[31] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[32] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[33] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[34] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[35] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[36] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[37] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[38] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[39] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[40] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[41] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[42] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[43] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[44] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[45] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[46] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[47] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[48] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[49] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[50] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[51] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[52] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[53] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[54] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[55] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[56] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[57] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[58] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[59] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[60] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[61] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[62] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[63] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[64] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[65] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[66] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[67] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[68] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[69] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[70] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[71] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[72] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[73] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[74] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[75] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[76] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[77] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[78] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[79] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[80] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		trailerModel[81] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[82] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[83] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[84] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[85] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[86] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[87] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[88] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[89] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[90] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[91] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[92] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[93] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[94] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[95] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[96] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[97] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[98] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[99] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[100] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[101] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[102] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[103] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[104] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[105] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[106] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[107] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[108] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[109] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[110] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[111] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[112] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[113] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[114] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[115] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[116] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[117] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[118] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[119] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[120] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[121] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[122] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[123] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[124] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[125] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[126] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[127] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[128] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[129] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[130] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[131] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[132] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[133] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[134] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[135] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[136] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[137] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[138] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[139] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[140] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[141] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[142] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[143] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		trailerModel[144] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[145] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[146] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[147] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[148] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[149] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[150] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[151] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[152] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[153] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[154] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[155] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[156] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[157] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[158] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[159] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[160] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[161] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[162] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[163] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[164] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[165] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[166] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[167] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[168] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[169] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[170] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[171] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[172] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[173] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[174] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[175] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		trailerModel[176] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0

		trailerModel[0].addBox(0F, 0F, 0F, 16, 2, 16, 0F); // Box 11
		trailerModel[0].setRotationPoint(-8F, -8F, -6F);

		trailerModel[1].addBox(0F, 0F, 0F, 1, 1, 16, 0F); // Box 23
		trailerModel[1].setRotationPoint(10F, -13F, -49F);

		trailerModel[2].addBox(0F, 0F, 0F, 1, 1, 16, 0F); // Box 23
		trailerModel[2].setRotationPoint(21F, -13F, -49F);

		trailerModel[3].addBox(0F, 0F, 0F, 16, 1, 12, 0F); // Box 23
		trailerModel[3].setRotationPoint(8F, -14F, -47F);

		trailerModel[4].addBox(0F, 0F, 0F, 16, 1, 12, 0F); // Box 23
		trailerModel[4].setRotationPoint(8F, -14F, -34F);
		trailerModel[4].rotateAngleX = 1.44862328F;

		trailerModel[5].addBox(0F, 0F, 0F, 8, 6, 6, 0F); // Box 23
		trailerModel[5].setRotationPoint(6F, -12F, -45F);

		trailerModel[6].addBox(0F, 0F, 0F, 8, 6, 6, 0F); // Box 23
		trailerModel[6].setRotationPoint(20F, -12F, -45F);

		trailerModel[7].addBox(0F, 0F, 0F, 6, 4, 4, 0F); // Box 23
		trailerModel[7].setRotationPoint(14F, -11F, -44F);

		trailerModel[8].addBox(0F, 0F, 0F, 2, 8, 8, 0F); // Box 23
		trailerModel[8].setRotationPoint(28F, -14F, -46F);

		trailerModel[9].addBox(0F, 0F, 0F, 4, 8, 8, 0F); // Box 23
		trailerModel[9].setRotationPoint(2F, -14F, -46F);

		trailerModel[10].addBox(0F, 0F, 0F, 6, 8, 8, 0F); // Box 23
		trailerModel[10].setRotationPoint(24F, -14F, -32F);

		trailerModel[11].addBox(0F, 0F, 0F, 4, 12, 12, 0F); // Box 23
		trailerModel[11].setRotationPoint(-2F, -18F, -50F);

		trailerModel[12].addBox(0F, 0F, 0F, 1, 6, 6, 0F); // Box 23
		trailerModel[12].setRotationPoint(-3F, -17F, -49F);

		trailerModel[13].addBox(0F, 0F, 0F, 1, 6, 6, 0F); // Box 23
		trailerModel[13].setRotationPoint(-5F, -17F, -49F);

		trailerModel[14].addBox(0F, 0F, 0F, 1, 6, 6, 0F); // Box 23
		trailerModel[14].setRotationPoint(-7F, -17F, -49F);

		trailerModel[15].addBox(0F, 0F, 0F, 1, 6, 6, 0F); // Box 23
		trailerModel[15].setRotationPoint(-9F, -17F, -49F);

		trailerModel[16].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // Box 23
		trailerModel[16].setRotationPoint(-4F, -16F, -48F);

		trailerModel[17].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // Box 23
		trailerModel[17].setRotationPoint(-6F, -16F, -48F);

		trailerModel[18].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // Box 23
		trailerModel[18].setRotationPoint(-8F, -16F, -48F);

		trailerModel[19].addBox(0F, 0F, 0F, 4, 12, 12, 0F); // Box 23
		trailerModel[19].setRotationPoint(-13F, -18F, -50F);

		trailerModel[20].addBox(0F, 0F, 0F, 8, 2, 20, 0F); // Box 23
		trailerModel[20].setRotationPoint(2F, -18F, -58F);

		trailerModel[21].addBox(0F, 0F, 0F, 8, 10, 2, 0F); // Box 23
		trailerModel[21].setRotationPoint(14F, -24F, -58F);

		trailerModel[22].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // Box 23
		trailerModel[22].setRotationPoint(-3F, -10F, -47F);

		trailerModel[23].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // Box 23
		trailerModel[23].setRotationPoint(-9F, -10F, -47F);

		trailerModel[24].addBox(0F, 0F, 0F, 5, 3, 3, 0F); // Box 23
		trailerModel[24].setRotationPoint(-8F, -9.5F, -46.5F);

		trailerModel[25].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // Box 23
		trailerModel[25].setRotationPoint(-3F, -10F, -43F);

		trailerModel[26].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // Box 23
		trailerModel[26].setRotationPoint(-9F, -10F, -43F);

		trailerModel[27].addBox(0F, 0F, 0F, 5, 3, 3, 0F); // Box 23
		trailerModel[27].setRotationPoint(-8F, -9.5F, -42.5F);

		trailerModel[28].addBox(0F, 0F, 0F, 18, 5, 1, 0F); // Box 23
		trailerModel[28].setRotationPoint(11F, -29F, -58F);

		trailerModel[29].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // Box 23
		trailerModel[29].setRotationPoint(12F, -29F, -57F);

		trailerModel[30].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // Box 23
		trailerModel[30].setRotationPoint(17F, -29F, -57F);

		trailerModel[31].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // Box 23
		trailerModel[31].setRotationPoint(22F, -29F, -57F);

		trailerModel[32].addBox(0F, 0F, 0F, 5, 11, 1, 0F); // Box 23
		trailerModel[32].setRotationPoint(3F, -29F, -58F);

		trailerModel[33].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // Box 23
		trailerModel[33].setRotationPoint(3.5F, -28F, -57F);

		trailerModel[34].addBox(0F, 0F, 0F, 4, 2, 1, 0F); // Box 23
		trailerModel[34].setRotationPoint(3.5F, -23F, -57F);

		trailerModel[35].addBox(0F, 0F, 0F, 4, 2, 1, 0F); // Box 23
		trailerModel[35].setRotationPoint(3.5F, -20F, -57F);

		trailerModel[36].addBox(0F, 0F, 0F, 12, 11, 1, 0F); // Box 23
		trailerModel[36].setRotationPoint(-11F, -29F, -58F);
		trailerModel[36].rotateAngleY = -0.06981317F;

		trailerModel[37].addShapeBox(2F, 1F, 0.75F, 8, 12, 1, 0F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F); // Box 23
		trailerModel[37].setRotationPoint(-11F, -29F, -58F);
		trailerModel[37].rotateAngleX = 0.03490659F;
		trailerModel[37].rotateAngleY = -0.06981317F;

		trailerModel[38].addBox(0F, 0F, 0F, 4, 12, 12, 0F); // Box 23
		trailerModel[38].setRotationPoint(-30F, -18F, -50F);

		trailerModel[39].addShapeBox(0F, 0F, 0F, 13, 10, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 23
		trailerModel[39].setRotationPoint(-26F, -16F, -51F);

		trailerModel[40].addBox(0F, 0F, 0F, 30, 33, 2, 0F); // Box 0
		trailerModel[40].setRotationPoint(-30F, -39F, 26F);

		trailerModel[41].addBox(0F, 0F, 0F, 30, 33, 2, 0F); // Box 0
		trailerModel[41].setRotationPoint(0F, -39F, 26F);

		trailerModel[42].addBox(0F, 0F, 0F, 18, 2, 2, 0F); // Box 23
		trailerModel[42].setRotationPoint(12F, -14F, -56F);

		trailerModel[43].addBox(0F, 0F, 0F, 4, 12, 12, 0F); // Box 23
		trailerModel[43].setRotationPoint(-30F, -30F, -50F);

		trailerModel[44].addBox(0F, 0F, 0F, 4, 10, 10, 0F); // Box 23
		trailerModel[44].setRotationPoint(-26F, -29F, -51F);

		trailerModel[45].addBox(0F, 0F, 0F, 4, 12, 10, 0F); // Box 23
		trailerModel[45].setRotationPoint(-13F, -30F, -51F);

		trailerModel[46].addShapeBox(0F, 0F, 0F, 9, 8, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 23
		trailerModel[46].setRotationPoint(-22F, -28F, -50F);

		trailerModel[47].addBox(0F, 0F, 0F, 4, 12, 12, 0F); // Box 23
		trailerModel[47].setRotationPoint(26F, -30F, -50F);

		trailerModel[48].addBox(0F, 0F, 0F, 1, 8, 10, 0F); // Box 23
		trailerModel[48].setRotationPoint(-9F, -29F, -51F);

		trailerModel[49].addBox(0F, 0F, 0F, 16, 30, 24, 0F); // Box 0
		trailerModel[49].setRotationPoint(-30F, -38F, 28F);

		trailerModel[50].addBox(0F, 0F, 0F, 16, 8, 12, 0F); // Box 0
		trailerModel[50].setRotationPoint(-30F, -14F, 53F);

		trailerModel[51].addBox(0F, 0F, 0F, 3, 2, 3, 0F); // Box 23
		trailerModel[51].setRotationPoint(26F, -8F, -24F);

		trailerModel[52].addBox(0F, 0F, 0F, 3, 2, 3, 0F); // Box 23
		trailerModel[52].setRotationPoint(14F, -8F, -24F);

		trailerModel[53].addBox(0F, 0F, 0F, 3, 2, 3, 0F); // Box 23
		trailerModel[53].setRotationPoint(26F, -8F, -8F);

		trailerModel[54].addBox(0F, 0F, 0F, 3, 2, 3, 0F); // Box 23
		trailerModel[54].setRotationPoint(14F, -8F, -8F);

		trailerModel[55].addBox(0F, 0F, 0F, 3, 2, 3, 0F); // Box 23
		trailerModel[55].setRotationPoint(26F, -8F, 7F);

		trailerModel[56].addBox(0F, 0F, 0F, 3, 2, 3, 0F); // Box 23
		trailerModel[56].setRotationPoint(14F, -8F, 7F);

		trailerModel[57].addBox(0F, 0F, 0F, 3, 2, 3, 0F); // Box 23
		trailerModel[57].setRotationPoint(26F, -8F, 23F);

		trailerModel[58].addBox(0F, 0F, 0F, 3, 2, 3, 0F); // Box 23
		trailerModel[58].setRotationPoint(14F, -8F, 23F);

		trailerModel[59].addBox(0F, 0F, 0F, 3, 8, 3, 0F); // Box 23
		trailerModel[59].setRotationPoint(26F, -18F, -24F);

		trailerModel[60].addBox(0F, 0F, 0F, 3, 8, 3, 0F); // Box 23
		trailerModel[60].setRotationPoint(14F, -18F, -24F);

		trailerModel[61].addBox(0F, 0F, 0F, 3, 8, 3, 0F); // Box 23
		trailerModel[61].setRotationPoint(26F, -18F, 23F);

		trailerModel[62].addBox(0F, 0F, 0F, 3, 8, 3, 0F); // Box 23
		trailerModel[62].setRotationPoint(14F, -18F, 23F);

		trailerModel[63].addBox(0F, 0F, 0F, 16, 1, 25, 0F); // Box 23
		trailerModel[63].setRotationPoint(13F, -10F, -24F);

		trailerModel[64].addBox(0F, 0F, 0F, 16, 1, 25, 0F); // Box 23
		trailerModel[64].setRotationPoint(13F, -10F, 1F);

		trailerModel[65].addBox(0F, 0F, 0F, 16, 1, 25, 0F); // Box 23
		trailerModel[65].setRotationPoint(13F, -19F, -24F);

		trailerModel[66].addBox(0F, 0F, 0F, 16, 1, 25, 0F); // Box 23
		trailerModel[66].setRotationPoint(13F, -19F, 1F);

		trailerModel[67].addBox(0F, 0F, 0F, 3, 8, 3, 0F); // Box 23
		trailerModel[67].setRotationPoint(26F, -27F, -24F);

		trailerModel[68].addBox(0F, 0F, 0F, 3, 8, 3, 0F); // Box 23
		trailerModel[68].setRotationPoint(14F, -27F, -24F);

		trailerModel[69].addBox(0F, 0F, 0F, 3, 8, 3, 0F); // Box 23
		trailerModel[69].setRotationPoint(26F, -27F, 23F);

		trailerModel[70].addBox(0F, 0F, 0F, 3, 8, 3, 0F); // Box 23
		trailerModel[70].setRotationPoint(14F, -27F, 23F);

		trailerModel[71].addBox(0F, 0F, 0F, 16, 1, 25, 0F); // Box 23
		trailerModel[71].setRotationPoint(13F, -28F, -24F);

		trailerModel[72].addBox(0F, 0F, 0F, 16, 1, 25, 0F); // Box 23
		trailerModel[72].setRotationPoint(13F, -28F, 1F);

		trailerModel[73].addBox(0F, 0F, 0F, 3, 5, 3, 0F); // Box 23
		trailerModel[73].setRotationPoint(26F, -33F, -24F);

		trailerModel[74].addBox(0F, 0F, 0F, 3, 5, 3, 0F); // Box 23
		trailerModel[74].setRotationPoint(14F, -33F, -24F);

		trailerModel[75].addBox(0F, 0F, 0F, 3, 8, 3, 0F); // Box 23
		trailerModel[75].setRotationPoint(26F, -36F, 23F);

		trailerModel[76].addBox(0F, 0F, 0F, 3, 8, 3, 0F); // Box 23
		trailerModel[76].setRotationPoint(14F, -36F, 23F);

		trailerModel[77].addBox(0F, 0F, 0F, 16, 1, 25, 0F); // Box 23
		trailerModel[77].setRotationPoint(13F, -37F, -24F);

		trailerModel[78].addBox(0F, 0F, 0F, 16, 1, 25, 0F); // Box 23
		trailerModel[78].setRotationPoint(13F, -37F, 1F);

		trailerModel[79].addBox(0F, 0F, 0F, 8, 1, 8, 0F); // Box 0
		trailerModel[79].setRotationPoint(-26F, -39F, 32F);

		trailerModel[80].addBox(0F, 0F, 0F, 8, 16, 8, 0F); // Box 11
		trailerModel[80].setRotationPoint(-4F, -24F, -2F);

		trailerModel[81].addBox(0F, 0F, 0F, 1, 1, 16, 0F); // Box 23
		trailerModel[81].setRotationPoint(-24F, -13F, -38F);

		trailerModel[82].addBox(0F, 0F, 0F, 1, 1, 16, 0F); // Box 23
		trailerModel[82].setRotationPoint(-13F, -13F, -38F);

		trailerModel[83].addBox(0F, 0F, 0F, 16, 1, 12, 0F); // Box 23
		trailerModel[83].setRotationPoint(-26F, -14F, -36F);

		trailerModel[84].addBox(0F, 0F, 0F, 16, 1, 12, 0F); // Box 23
		trailerModel[84].setRotationPoint(-26F, -14F, -23F);
		trailerModel[84].rotateAngleX = 1.44862328F;

		trailerModel[85].addBox(0F, 0F, 0F, 16, 6, 12, 0F); // Box 23
		trailerModel[85].setRotationPoint(-26F, -12F, -36F);

		trailerModel[86].addBox(0F, 0F, 0F, 18, 18, 5, 0F); // Box 0
		trailerModel[86].setRotationPoint(-30F, -39F, 21F);

		trailerModel[87].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // Box 0
		trailerModel[87].setRotationPoint(-29.5F, -21F, 21.5F);

		trailerModel[88].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // Box 0
		trailerModel[88].setRotationPoint(-23.5F, -21F, 21.5F);

		trailerModel[89].addBox(0F, 0F, 0F, 3, 11, 3, 0F); // Box 0
		trailerModel[89].setRotationPoint(-29F, -20F, 22F);

		trailerModel[90].addBox(0F, 0F, 0F, 3, 11, 3, 0F); // Box 0
		trailerModel[90].setRotationPoint(-23F, -20F, 22F);

		trailerModel[91].addBox(0F, 0F, 0F, 3, 3, 3, 0F); // Box 0
		trailerModel[91].setRotationPoint(-29F, -9F, 22F);

		trailerModel[92].addBox(0F, 0F, 0F, 3, 3, 3, 0F); // Box 0
		trailerModel[92].setRotationPoint(-23F, -9F, 22F);

		trailerModel[93].addBox(0F, 0F, 0F, 3, 3, 22, 0F); // Box 0
		trailerModel[93].setRotationPoint(-29F, -9F, 0F);

		trailerModel[94].addBox(0F, 0F, 0F, 6, 3, 18, 0F); // Box 0
		trailerModel[94].setRotationPoint(-29F, -9F, -18F);

		trailerModel[95].addBox(0F, 0F, 0F, 3, 3, 12, 0F); // Box 0
		trailerModel[95].setRotationPoint(-26F, -9F, 0F);

		trailerModel[96].addBox(0F, 0F, 0F, 3, 3, 7, 0F); // Box 0
		trailerModel[96].setRotationPoint(-23F, -9F, 15F);

		trailerModel[97].addBox(0F, 0F, 0F, 4, 3, 3, 0F); // Box 0
		trailerModel[97].setRotationPoint(-25F, -9F, 12F);

		trailerModel[98].addShapeBox(0F, 0F, 0F, 1, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		trailerModel[98].setRotationPoint(-21F, -9F, 12F);

		trailerModel[99].addShapeBox(0F, 0F, 0F, 1, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F); // Box 0
		trailerModel[99].setRotationPoint(-26F, -9F, 12F);

		trailerModel[100].addBox(0F, 0F, 0F, 4, 3, 3, 0F); // Box 0
		trailerModel[100].setRotationPoint(-25F, -9F, -23F);

		trailerModel[101].addShapeBox(0F, 0F, 0F, 1, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F); // Box 0
		trailerModel[101].setRotationPoint(-21F, -9F, -23F);

		trailerModel[102].addShapeBox(0F, 0F, 0F, 1, 3, 3, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		trailerModel[102].setRotationPoint(-26F, -9F, -23F);

		trailerModel[103].addBox(0F, 0F, 0F, 3, 3, 2, 0F); // Box 0
		trailerModel[103].setRotationPoint(-26F, -9F, -20F);

		trailerModel[104].addBox(0F, 0F, 0F, 3, 3, 1, 0F); // Box 0
		trailerModel[104].setRotationPoint(-23F, -9F, -24F);

		trailerModel[105].addBox(0F, 0F, 0F, 3, 3, 20, 0F); // Box 0
		trailerModel[105].setRotationPoint(-29F, -9F, -38F);

		trailerModel[106].addBox(0F, 0F, 0F, 16, 20, 1, 0F); // Box 0
		trailerModel[106].setRotationPoint(-12F, -37F, 25F);

		trailerModel[107].addBox(0F, 0F, 0F, 24, 2, 37, 0F); // Box 0
		trailerModel[107].setRotationPoint(-13F, -8F, 28F);

		trailerModel[108].addBox(0F, 0F, 0F, 16, 8, 30, 0F); // Box 0
		trailerModel[108].setRotationPoint(-8F, -16F, 31F);

		trailerModel[109].addBox(0F, 0F, 0F, 8, 8, 1, 0F); // Box 0
		trailerModel[109].setRotationPoint(-4F, -16F, 30F);

		trailerModel[110].addBox(0F, 0F, 0F, 8, 8, 1, 0F); // Box 0
		trailerModel[110].setRotationPoint(-4F, -16F, 61F);

		trailerModel[111].addBox(0F, 0F, 0F, 4, 3, 3, 0F); // Box 0
		trailerModel[111].setRotationPoint(-12F, -11F, 31F);

		trailerModel[112].addShapeBox(0F, 0F, 0F, 1, 3, 3, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		trailerModel[112].setRotationPoint(-13F, -11F, 31F);

		trailerModel[113].addBox(0F, 0F, 0F, 3, 3, 20, 0F); // Box 0
		trailerModel[113].setRotationPoint(-13F, -11F, 34F);

		trailerModel[114].addBox(0F, 0F, 0F, 8, 3, 3, 0F); // Box 0
		trailerModel[114].setRotationPoint(-19F, -11F, 54F);

		trailerModel[115].addShapeBox(0F, 0F, 0F, 1, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F); // Box 0
		trailerModel[115].setRotationPoint(-11F, -11F, 54F);

		trailerModel[116].addBox(0F, 0F, 0F, 2, 3, 3, 0F); // Box 0
		trailerModel[116].setRotationPoint(-10F, -15F, 48F);

		trailerModel[117].addShapeBox(0F, 0F, 0F, 1, 3, 3, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		trailerModel[117].setRotationPoint(-11F, -15F, 48F);

		trailerModel[118].addBox(0F, 0F, 0F, 3, 3, 15, 0F); // Box 0
		trailerModel[118].setRotationPoint(-11F, -15F, 51F);

		trailerModel[119].addBox(0F, 0F, 0F, 2, 3, 3, 0F); // Box 0
		trailerModel[119].setRotationPoint(8F, -15F, 48F);

		trailerModel[120].addShapeBox(0F, 0F, 0F, 1, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		trailerModel[120].setRotationPoint(10F, -15F, 48F);

		trailerModel[121].addBox(0F, 0F, 0F, 3, 3, 15, 0F); // Box 0
		trailerModel[121].setRotationPoint(8F, -15F, 51F);

		trailerModel[122].addBox(0F, 0F, 0F, 3, 3, 3, 0F); // Box 0
		trailerModel[122].setRotationPoint(-11F, -15F, 66F);
		trailerModel[122].rotateAngleX = -0.17453293F;

		trailerModel[123].addBox(0F, 0F, 0F, 3, 3, 3, 0F); // Box 0
		trailerModel[123].setRotationPoint(8F, -15F, 66F);
		trailerModel[123].rotateAngleX = -0.17453293F;

		trailerModel[124].addShapeBox(0F, 0F, 3F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // Box 0
		trailerModel[124].setRotationPoint(-11F, -15F, 66F);
		trailerModel[124].rotateAngleX = -0.17453293F;

		trailerModel[125].addShapeBox(0F, 0F, 3F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // Box 0
		trailerModel[125].setRotationPoint(8F, -15F, 66F);
		trailerModel[125].rotateAngleX = -0.17453293F;

		trailerModel[126].addBox(-1.5F, -15F, 0F, 6, 15, 6, 0F); // Box 0
		trailerModel[126].setRotationPoint(-11F, -15F, 66F);
		trailerModel[126].rotateAngleX = -0.17453293F;

		trailerModel[127].addBox(-1.5F, -15F, 0F, 6, 15, 6, 0F); // Box 0
		trailerModel[127].setRotationPoint(8F, -15F, 66F);
		trailerModel[127].rotateAngleX = -0.17453293F;

		trailerModel[128].addBox(-1F, -25F, 0.5F, 5, 10, 5, 0F); // Box 0
		trailerModel[128].setRotationPoint(-11F, -15F, 66F);
		trailerModel[128].rotateAngleX = -0.17453293F;

		trailerModel[129].addBox(-1F, -25F, 0.5F, 5, 10, 5, 0F); // Box 0
		trailerModel[129].setRotationPoint(8F, -15F, 66F);
		trailerModel[129].rotateAngleX = -0.17453293F;

		trailerModel[130].addBox(-1F, -29F, 0.5F, 5, 4, 1, 0F); // Box 0
		trailerModel[130].setRotationPoint(-11F, -15F, 66F);
		trailerModel[130].rotateAngleX = -0.17453293F;

		trailerModel[131].addBox(-1F, -29F, 0.5F, 5, 4, 1, 0F); // Box 0
		trailerModel[131].setRotationPoint(8F, -15F, 66F);
		trailerModel[131].rotateAngleX = -0.17453293F;

		trailerModel[132].addBox(-1F, -29F, 4.5F, 5, 4, 1, 0F); // Box 0
		trailerModel[132].setRotationPoint(-11F, -15F, 66F);
		trailerModel[132].rotateAngleX = -0.17453293F;

		trailerModel[133].addBox(-1F, -29F, 4.5F, 5, 4, 1, 0F); // Box 0
		trailerModel[133].setRotationPoint(8F, -15F, 66F);
		trailerModel[133].rotateAngleX = -0.17453293F;

		trailerModel[134].addBox(-1F, -29F, 1.5F, 1, 4, 3, 0F); // Box 0
		trailerModel[134].setRotationPoint(-11F, -15F, 66F);
		trailerModel[134].rotateAngleX = -0.17453293F;

		trailerModel[135].addBox(-1F, -29F, 1.5F, 1, 4, 3, 0F); // Box 0
		trailerModel[135].setRotationPoint(8F, -15F, 66F);
		trailerModel[135].rotateAngleX = -0.17453293F;

		trailerModel[136].addBox(3F, -29F, 1.5F, 1, 4, 3, 0F); // Box 0
		trailerModel[136].setRotationPoint(-11F, -15F, 66F);
		trailerModel[136].rotateAngleX = -0.17453293F;

		trailerModel[137].addBox(3F, -29F, 1.5F, 1, 4, 3, 0F); // Box 0
		trailerModel[137].setRotationPoint(8F, -15F, 66F);
		trailerModel[137].rotateAngleX = -0.17453293F;

		trailerModel[138].addBox(0F, 0F, 0F, 8, 8, 1, 0F); // Box 0
		trailerModel[138].setRotationPoint(-4F, -16F, 28F);

		trailerModel[139].addBox(0F, 0F, 0F, 6, 6, 1, 0F); // Box 0
		trailerModel[139].setRotationPoint(-3F, -15F, 29F);

		trailerModel[140].addBox(0F, 0F, 0F, 8, 8, 1, 0F); // Box 0
		trailerModel[140].setRotationPoint(-4F, -16F, 25F);

		trailerModel[141].addBox(0F, 0F, 0F, 6, 6, 1, 0F); // Box 0
		trailerModel[141].setRotationPoint(-3F, -15F, 24F);

		trailerModel[142].addBox(0F, 0F, 0F, 8, 10, 8, 0F); // Box 0
		trailerModel[142].setRotationPoint(-4F, -16F, 16F);

		trailerModel[143].addBox(0F, 0F, 0F, 8, 1, 4, 0F); // Box 23
		trailerModel[143].setRotationPoint(2F, -19F, -48F);

		trailerModel[144].addBox(0F, 0F, 0F, 10, 8, 30, 0F); // Box 0
		trailerModel[144].setRotationPoint(-8F, -24F, 31F);

		trailerModel[145].addBox(-3F, -6F, 0F, 6, 8, 6, 0F); // Box 0
		trailerModel[145].setRotationPoint(-9F, -24F, 31F);
		trailerModel[145].rotateAngleZ = 0.43633231F;

		trailerModel[146].addBox(-3F, -6F, 0F, 6, 8, 6, 0F); // Box 0
		trailerModel[146].setRotationPoint(-9F, -24F, 38F);
		trailerModel[146].rotateAngleZ = 0.43633231F;

		trailerModel[147].addBox(-3F, -6F, 0F, 6, 8, 6, 0F); // Box 0
		trailerModel[147].setRotationPoint(-9F, -24F, 45F);
		trailerModel[147].rotateAngleZ = 0.43633231F;

		trailerModel[148].addBox(-3F, -6F, 0F, 6, 8, 6, 0F); // Box 0
		trailerModel[148].setRotationPoint(-9F, -24F, 52F);
		trailerModel[148].rotateAngleZ = 0.43633231F;

		trailerModel[149].addBox(-3F, -6F, 0F, 6, 8, 6, 0F); // Box 0
		trailerModel[149].setRotationPoint(1F, -24F, 31F);
		trailerModel[149].rotateAngleZ = -0.43633231F;

		trailerModel[150].addBox(-3F, -6F, 0F, 6, 8, 6, 0F); // Box 0
		trailerModel[150].setRotationPoint(1F, -24F, 38F);
		trailerModel[150].rotateAngleZ = -0.43633231F;

		trailerModel[151].addBox(-3F, -6F, 0F, 6, 8, 6, 0F); // Box 0
		trailerModel[151].setRotationPoint(1F, -24F, 45F);
		trailerModel[151].rotateAngleZ = -0.43633231F;

		trailerModel[152].addBox(-3F, -6F, 0F, 6, 8, 6, 0F); // Box 0
		trailerModel[152].setRotationPoint(1F, -24F, 52F);
		trailerModel[152].rotateAngleZ = -0.43633231F;

		trailerModel[153].addBox(-3F, -6F, 0F, 6, 12, 14, 0F); // Box 0
		trailerModel[153].setRotationPoint(11F, -14F, 31F);

		trailerModel[154].addBox(0F, 0F, 0F, 3, 3, 30, 0F); // Box 0
		trailerModel[154].setRotationPoint(-5.5F, -27F, 31F);

		trailerModel[155].addBox(0F, 0F, 0F, 3, 3, 30, 0F); // Box 0
		trailerModel[155].setRotationPoint(-10.5F, -36F, 31F);

		trailerModel[156].addBox(0F, 0F, 0F, 8, 5, 8, 0F); // Box 0
		trailerModel[156].setRotationPoint(-6.5F, -37F, 31F);

		trailerModel[157].addBox(0F, 0F, 0F, 3, 3, 30, 0F); // Box 0
		trailerModel[157].setRotationPoint(-5.5F, -36F, 31F);

		trailerModel[158].addBox(0F, 0F, 0F, 8, 5, 8, 0F); // Box 0
		trailerModel[158].setRotationPoint(-6.5F, -37F, 41F);

		trailerModel[159].addBox(0F, 0F, 0F, 8, 5, 8, 0F); // Box 0
		trailerModel[159].setRotationPoint(-6.5F, -37F, 51F);

		trailerModel[160].addBox(0F, 0F, 0F, 8, 1, 8, 0F); // Box 0
		trailerModel[160].setRotationPoint(-6.5F, -39F, 31F);

		trailerModel[161].addBox(0F, 0F, 0F, 8, 1, 8, 0F); // Box 0
		trailerModel[161].setRotationPoint(-6.5F, -39F, 41F);

		trailerModel[162].addBox(0F, 0F, 0F, 8, 1, 8, 0F); // Box 0
		trailerModel[162].setRotationPoint(-6.5F, -39F, 51F);

		trailerModel[163].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // Box 0
		trailerModel[163].setRotationPoint(-5.5F, -38F, 32F);

		trailerModel[164].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // Box 0
		trailerModel[164].setRotationPoint(-5.5F, -38F, 42F);

		trailerModel[165].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // Box 0
		trailerModel[165].setRotationPoint(-5.5F, -38F, 52F);

		trailerModel[166].addBox(0F, 0F, 0F, 2, 3, 3, 0F); // Box 0
		trailerModel[166].setRotationPoint(-9.5F, -36F, 61F);

		trailerModel[167].addBox(0F, 0F, 0F, 2, 3, 3, 0F); // Box 0
		trailerModel[167].setRotationPoint(-7.5F, -36F, 61F);

		trailerModel[168].addBox(0F, 0F, 0F, 2, 3, 3, 0F); // Box 0
		trailerModel[168].setRotationPoint(-5.5F, -36F, 61F);

		trailerModel[169].addShapeBox(0F, 0F, 0F, 1, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F); // Box 0
		trailerModel[169].setRotationPoint(-10.5F, -36F, 61F);

		trailerModel[170].addShapeBox(0F, 0F, 0F, 1, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F); // Box 0
		trailerModel[170].setRotationPoint(-3.5F, -36F, 61F);

		trailerModel[171].addBox(-3F, -6F, 0F, 4, 4, 8, 0F); // Box 0
		trailerModel[171].setRotationPoint(5F, -14F, 33F);

		trailerModel[172].addBox(-3F, -6F, 0F, 4, 4, 16, 0F); // Box 0
		trailerModel[172].setRotationPoint(5F, -14F, 43F);

		trailerModel[173].addBox(-3F, -8F, 0F, 16, 16, 16, 0F); // Box 0
		trailerModel[173].setRotationPoint(17F, -14F, 31F);

		trailerModel[174].addBox(-3F, -8F, 19F, 16, 16, 15, 0F); // Box 0
		trailerModel[174].setRotationPoint(17F, -14F, 31F);

		trailerModel[175].addBox(-2F, -4F, 16F, 6, 6, 3, 0F); // Box 0
		trailerModel[175].setRotationPoint(17F, -14F, 31F);

		trailerModel[176].addBox(6F, -4F, 16F, 6, 6, 3, 0F); // Box 0
		trailerModel[176].setRotationPoint(17F, -14F, 31F);


		steeringWheelModel = new ModelRendererTurbo[7];
		steeringWheelModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		steeringWheelModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		steeringWheelModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		steeringWheelModel[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		steeringWheelModel[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		steeringWheelModel[5] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23
		steeringWheelModel[6] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 23

		steeringWheelModel[0].addBox(-1F, -1F, -3F, 2, 2, 8, 0F); // Box 23
		steeringWheelModel[0].setRotationPoint(18F, -22F, -54F);
		steeringWheelModel[0].rotateAngleX = 0.26179939F;

		steeringWheelModel[1].addBox(-5F, -1F, 5F, 10, 2, 1, 0F); // Box 23
		steeringWheelModel[1].setRotationPoint(18F, -22F, -54F);
		steeringWheelModel[1].rotateAngleX = 0.26179939F;

		steeringWheelModel[2].addBox(-5F, -3F, 5F, 1, 2, 1, 0F); // Box 23
		steeringWheelModel[2].setRotationPoint(18F, -22F, -54F);
		steeringWheelModel[2].rotateAngleX = 0.26179939F;

		steeringWheelModel[3].addBox(-5F, 1F, 5F, 1, 2, 1, 0F); // Box 23
		steeringWheelModel[3].setRotationPoint(18F, -22F, -54F);
		steeringWheelModel[3].rotateAngleX = 0.26179939F;

		steeringWheelModel[4].addBox(4F, -3F, 5F, 1, 2, 1, 0F); // Box 23
		steeringWheelModel[4].setRotationPoint(18F, -22F, -54F);
		steeringWheelModel[4].rotateAngleX = 0.26179939F;

		steeringWheelModel[5].addBox(4F, 1F, 5F, 1, 2, 1, 0F); // Box 23
		steeringWheelModel[5].setRotationPoint(18F, -22F, -54F);
		steeringWheelModel[5].rotateAngleX = 0.26179939F;

		steeringWheelModel[6].addBox(-5F, -4F, 5F, 10, 1, 1, 0F); // Box 23
		steeringWheelModel[6].setRotationPoint(18F, -22F, -54F);
		steeringWheelModel[6].rotateAngleX = 0.26179939F;


		drillHeadModel = new ModelRendererTurbo[4];
		drillHeadModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		drillHeadModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		drillHeadModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11
		drillHeadModel[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 11

		drillHeadModel[0].addBox(-16F, -0.5F, 1F, 32, 1, 11, 0F); // Box 11
		drillHeadModel[0].setRotationPoint(0F, -68.5F, 32F);

		drillHeadModel[1].addBox(-12F, -0.5F, 0F, 4, 1, 1, 0F); // Box 11
		drillHeadModel[1].setRotationPoint(0F, -68.5F, 32F);

		drillHeadModel[2].addBox(8F, -0.5F, 0F, 4, 1, 1, 0F); // Box 11
		drillHeadModel[2].setRotationPoint(0F, -68.5F, 32F);

		drillHeadModel[3].addBox(-2F, -0.5F, 12F, 4, 2, 1, 0F); // Box 11
		drillHeadModel[3].setRotationPoint(0F, -68.5F, 32F);
		
		doTranslations();
		flipAll();
	}

	private void doTranslations()
	{
		translate(rightTrackModel,12F, 43F, 42F);

		parts.put("base",baseModel);
		parts.put( "bodyDoorOpen",bodyDoorOpenModel); //commander hatch
		parts.put( "bodyDoorClose",bodyDoorCloseModel); //gunner hatch
		parts.put( "turret",turretModel); //turret
		parts.put( "barrel",barrelModel); //barrel
		parts.put( "leftFrontWheel",leftFrontWheelModel); //left wheels
		parts.put( "rightFrontWheel",rightFrontWheelModel); //right wheels
		parts.put( "leftBackWheel",leftBackWheelModel); //left engine door
		parts.put( "rightBackWheel",rightBackWheelModel); //right engine door
		parts.put( "leftTrackWheelM",leftTrackWheelModels); //left back door
		parts.put( "rightTrackWheelM",rightTrackWheelModels); //right back door
		parts.put( "frontWheel",frontWheelModel); //gear shift lever
		parts.put( "backWheel",backWheelModel); //driver pedals
		parts.put( "leftTrack",leftTrackModel); //driver hatch
		parts.put( "rightTrack",rightTrackModel); //mg turret
		parts.put( "trailer",trailerModel); //internals
		parts.put( "steeringWheel",steeringWheelModel); //something on the turret
		parts.put( "drillHead",drillHeadModel); //turret back door
	}
}
