package pl.pabilo8.immersiveintelligence.client.model.weapon.emplacement;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

public class ModelHeavyRailgun extends ModelIIBase
{
	int textureX = 128;
	int textureY = 128;

	public ModelRendererTurbo[] gunModel;

	public ModelHeavyRailgun(boolean doOffsets) //Same as Filename
	{
		baseModel = new ModelRendererTurbo[30];
		baseModel[0] = new ModelRendererTurbo(this, 4, 87, textureX, textureY); // base01
		baseModel[1] = new ModelRendererTurbo(this, 86, 94, textureX, textureY); // base02
		baseModel[2] = new ModelRendererTurbo(this, 78, 0, textureX, textureY); // base03
		baseModel[3] = new ModelRendererTurbo(this, 78, 0, textureX, textureY); // base04
		baseModel[4] = new ModelRendererTurbo(this, 86, 94, textureX, textureY); // base05
		baseModel[5] = new ModelRendererTurbo(this, 0, 71, textureX, textureY); // base06
		baseModel[6] = new ModelRendererTurbo(this, 0, 71, textureX, textureY); // base07
		baseModel[7] = new ModelRendererTurbo(this, 28, 39, textureX, textureY); // CRANE01
		baseModel[8] = new ModelRendererTurbo(this, 20, 68, textureX, textureY); // base09
		baseModel[9] = new ModelRendererTurbo(this, 72, 55, textureX, textureY); // base10
		baseModel[10] = new ModelRendererTurbo(this, 0, 95, textureX, textureY); // base11
		baseModel[11] = new ModelRendererTurbo(this, 0, 95, textureX, textureY); // base12
		baseModel[12] = new ModelRendererTurbo(this, 84, 69, textureX, textureY); // base13
		baseModel[13] = new ModelRendererTurbo(this, 84, 69, textureX, textureY); // base14
		baseModel[14] = new ModelRendererTurbo(this, 72, 71, textureX, textureY); // base15
		baseModel[15] = new ModelRendererTurbo(this, 56, 8, textureX, textureY); // base16
		baseModel[16] = new ModelRendererTurbo(this, 70, 18, textureX, textureY); // base17
		baseModel[17] = new ModelRendererTurbo(this, 72, 14, textureX, textureY); // base18
		baseModel[18] = new ModelRendererTurbo(this, 25, 21, textureX, textureY); // base20
		baseModel[19] = new ModelRendererTurbo(this, 94, 0, textureX, textureY); // base21
		baseModel[20] = new ModelRendererTurbo(this, 85, 26, textureX, textureY); // SHIELD02
		baseModel[21] = new ModelRendererTurbo(this, 0, 101, textureX, textureY); // Shape 168
		baseModel[22] = new ModelRendererTurbo(this, 0, 101, textureX, textureY); // Shape 168
		baseModel[23] = new ModelRendererTurbo(this, 20, 94, textureX, textureY); // base09
		baseModel[24] = new ModelRendererTurbo(this, 16, 25, textureX, textureY); // base16
		baseModel[25] = new ModelRendererTurbo(this, 24, 25, textureX, textureY); // base20
		baseModel[26] = new ModelRendererTurbo(this, 94, 0, textureX, textureY); // base21
		baseModel[27] = new ModelRendererTurbo(this, 86, 114, textureX, textureY); // base21
		baseModel[28] = new ModelRendererTurbo(this, 86, 114, textureX, textureY); // base21
		baseModel[29] = new ModelRendererTurbo(this, 4, 87, textureX, textureY); // base01

		baseModel[0].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base01
		baseModel[0].setRotationPoint(8F, -22F, 11F);

		baseModel[1].addShapeBox(0F, 0F, 0F, 12, 18, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base02
		baseModel[1].setRotationPoint(11F, -22F, 7F);
		baseModel[1].rotateAngleY = 1.57079633F;

		baseModel[2].addShapeBox(0F, 0F, 0F, 2, 2, 12, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base03
		baseModel[2].setRotationPoint(9F, -24F, 7F);

		baseModel[3].addShapeBox(0F, 0F, 0F, 2, 2, 12, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base04
		baseModel[3].setRotationPoint(22F, -24F, 7F);

		baseModel[4].addShapeBox(0F, 0F, 0F, 12, 18, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base05
		baseModel[4].setRotationPoint(24F, -22F, 7F);
		baseModel[4].rotateAngleY = 1.57079633F;

		baseModel[5].addShapeBox(0F, 0F, 0F, 3, 14, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base06
		baseModel[5].setRotationPoint(21.5F, -2F, 6F);
		baseModel[5].rotateAngleX = 1.57079633F;

		baseModel[6].addShapeBox(0F, 0F, 0F, 3, 14, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base07
		baseModel[6].setRotationPoint(8.5F, -2F, 6F);
		baseModel[6].rotateAngleX = 1.57079633F;

		baseModel[7].addShapeBox(0F, 0F, 0F, 8, 8, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CRANE01
		baseModel[7].setRotationPoint(0F, -10F, 24F);

		baseModel[8].addShapeBox(0F, 0F, 0F, 24, 24, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base09
		baseModel[8].setRotationPoint(4F, 1F, 4F);
		baseModel[8].rotateAngleX = 1.57079633F;

		baseModel[9].addShapeBox(0F, 0F, 0F, 7, 19, 20, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base10
		baseModel[9].setRotationPoint(25F, -21F, 2F);

		baseModel[10].addShapeBox(0F, 0F, 0F, 5, 1, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base11
		baseModel[10].setRotationPoint(26F, -22F, 3F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 5, 1, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base12
		baseModel[11].setRotationPoint(26F, -22F, 10F);

		baseModel[12].addShapeBox(0F, 0F, 0F, 1, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base13
		baseModel[12].setRotationPoint(25.5F, -24F, 18F);

		baseModel[13].addShapeBox(0F, 0F, 0F, 1, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base14
		baseModel[13].setRotationPoint(30.5F, -24F, 18F);

		baseModel[14].addShapeBox(0F, 0F, 0F, 4, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base15
		baseModel[14].setRotationPoint(26.5F, -23.5F, 18.5F);

		baseModel[15].addShapeBox(0F, 0F, 0F, 2, 2, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base16
		baseModel[15].setRotationPoint(28F, -4F, 22F);

		baseModel[16].addShapeBox(0F, 0F, 0F, 20, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base17
		baseModel[16].setRotationPoint(8F, -4F, 28F);

		baseModel[17].addShapeBox(0F, 0F, 0F, 19, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base18
		baseModel[17].setRotationPoint(6F, -4F, 20F);

		baseModel[18].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base20
		baseModel[18].setRotationPoint(4F, -4F, 18F);

		baseModel[19].addShapeBox(0F, 0F, 0F, 16, 11, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base21
		baseModel[19].setRotationPoint(0F, -13F, 18F);
		baseModel[19].rotateAngleY = -1.57079633F;

		baseModel[20].addShapeBox(0F, 18F, 0F, 13, 9, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // SHIELD02
		baseModel[20].setRotationPoint(10F, -29F, 2F);
		baseModel[20].rotateAngleX = -0.06981317F;

		baseModel[21].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(10, 0, 10, 0), new Coord2D(10, 22, 10, 22), new Coord2D(6, 26, 6, 26), new Coord2D(0, 26, 0, 26) }), 1, 10, 26, 70, 1, ModelRendererTurbo.MR_FRONT, new float[] {26 ,6 ,6 ,22 ,10}); // Shape 168
		baseModel[21].setRotationPoint(10F, -2F, 1F);
		baseModel[21].rotateAngleX = -0.06981317F;

		baseModel[22].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(10, 0, 10, 0), new Coord2D(10, 22, 10, 22), new Coord2D(6, 26, 6, 26), new Coord2D(0, 26, 0, 26) }), 1, 10, 26, 70, 1, ModelRendererTurbo.MR_FRONT, new float[] {26 ,6 ,6 ,22 ,10}); // Shape 168
		baseModel[22].setRotationPoint(22F, -2F, 0F);
		baseModel[22].rotateAngleX = 0.06981317F;
		baseModel[22].rotateAngleY = -3.14159265F;

		baseModel[23].addShapeBox(0F, 0F, 0F, 32, 32, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base09
		baseModel[23].setRotationPoint(0F, -1F, 0F);
		baseModel[23].rotateAngleX = 1.57079633F;

		baseModel[24].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F); // base16
		baseModel[24].setRotationPoint(28F, -4F, 28F);

		baseModel[25].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F); // base20
		baseModel[25].setRotationPoint(4F, -4F, 20F);

		baseModel[26].addShapeBox(0F, 0F, 0F, 16, 11, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base21
		baseModel[26].setRotationPoint(8F, -13F, 18F);
		baseModel[26].rotateAngleY = -1.57079633F;

		baseModel[27].addShapeBox(0F, 0F, 0F, 7, 11, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base21
		baseModel[27].setRotationPoint(1F, -13F, 2F);

		baseModel[28].addShapeBox(0F, 0F, 0F, 7, 11, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base21
		baseModel[28].setRotationPoint(1F, -13F, 17F);

		baseModel[29].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // base01
		baseModel[29].setRotationPoint(21F, -22F, 11F);


		gunModel = new ModelRendererTurbo[153];
		gunModel[0] = new ModelRendererTurbo(this, 46, 42, textureX, textureY); // GUN01
		gunModel[1] = new ModelRendererTurbo(this, 6, 29, textureX, textureY); // GUN02
		gunModel[2] = new ModelRendererTurbo(this, 8, 47, textureX, textureY); // GUN03
		gunModel[3] = new ModelRendererTurbo(this, 78, 36, textureX, textureY); // GUN04
		gunModel[4] = new ModelRendererTurbo(this, 32, 20, textureX, textureY); // GUN05
		gunModel[5] = new ModelRendererTurbo(this, 8, 55, textureX, textureY); // GUN06
		gunModel[6] = new ModelRendererTurbo(this, 53, 23, textureX, textureY); // GUN07
		gunModel[7] = new ModelRendererTurbo(this, 8, 51, textureX, textureY); // GUN08
		gunModel[8] = new ModelRendererTurbo(this, 107, 36, textureX, textureY); // GUN09
		gunModel[9] = new ModelRendererTurbo(this, 41, 30, textureX, textureY); // GUN10
		gunModel[10] = new ModelRendererTurbo(this, 114, 12, textureX, textureY); // GUN11
		gunModel[11] = new ModelRendererTurbo(this, 0, 44, textureX, textureY); // GUN12
		gunModel[12] = new ModelRendererTurbo(this, 7, 32, textureX, textureY); // GUN13
		gunModel[13] = new ModelRendererTurbo(this, 106, 58, textureX, textureY); // GUN14
		gunModel[14] = new ModelRendererTurbo(this, 114, 105, textureX, textureY); // GUN15
		gunModel[15] = new ModelRendererTurbo(this, 36, 55, textureX, textureY); // GUN17
		gunModel[16] = new ModelRendererTurbo(this, 109, 54, textureX, textureY); // GUN18
		gunModel[17] = new ModelRendererTurbo(this, 109, 54, textureX, textureY); // GUN19
		gunModel[18] = new ModelRendererTurbo(this, 0, 52, textureX, textureY); // GUN20
		gunModel[19] = new ModelRendererTurbo(this, 10, 55, textureX, textureY); // GUN21
		gunModel[20] = new ModelRendererTurbo(this, 103, 37, textureX, textureY); // GUN22
		gunModel[21] = new ModelRendererTurbo(this, 103, 37, textureX, textureY); // GUN23
		gunModel[22] = new ModelRendererTurbo(this, 102, 115, textureX, textureY); // GUN24
		gunModel[23] = new ModelRendererTurbo(this, 103, 37, textureX, textureY); // GUN25
		gunModel[24] = new ModelRendererTurbo(this, 103, 37, textureX, textureY); // GUN26
		gunModel[25] = new ModelRendererTurbo(this, 103, 37, textureX, textureY); // GUN27
		gunModel[26] = new ModelRendererTurbo(this, 103, 37, textureX, textureY); // GUN28
		gunModel[27] = new ModelRendererTurbo(this, 103, 37, textureX, textureY); // GUN29
		gunModel[28] = new ModelRendererTurbo(this, 103, 37, textureX, textureY); // GUN30
		gunModel[29] = new ModelRendererTurbo(this, 118, 57, textureX, textureY); // GUN31
		gunModel[30] = new ModelRendererTurbo(this, 70, 68, textureX, textureY); // GUN32
		gunModel[31] = new ModelRendererTurbo(this, 70, 68, textureX, textureY); // GUN33
		gunModel[32] = new ModelRendererTurbo(this, 70, 68, textureX, textureY); // GUN34
		gunModel[33] = new ModelRendererTurbo(this, 70, 68, textureX, textureY); // GUN35
		gunModel[34] = new ModelRendererTurbo(this, 70, 68, textureX, textureY); // GUN36
		gunModel[35] = new ModelRendererTurbo(this, 70, 68, textureX, textureY); // GUN37
		gunModel[36] = new ModelRendererTurbo(this, 70, 68, textureX, textureY); // GUN38
		gunModel[37] = new ModelRendererTurbo(this, 70, 68, textureX, textureY); // GUN39
		gunModel[38] = new ModelRendererTurbo(this, 0, 52, textureX, textureY); // GUN40
		gunModel[39] = new ModelRendererTurbo(this, 10, 55, textureX, textureY); // GUN41
		gunModel[40] = new ModelRendererTurbo(this, 118, 57, textureX, textureY); // GUN42
		gunModel[41] = new ModelRendererTurbo(this, 122, 105, textureX, textureY); // GUN43
		gunModel[42] = new ModelRendererTurbo(this, 122, 105, textureX, textureY); // GUN44
		gunModel[43] = new ModelRendererTurbo(this, 122, 105, textureX, textureY); // GUN45
		gunModel[44] = new ModelRendererTurbo(this, 122, 105, textureX, textureY); // GUN46
		gunModel[45] = new ModelRendererTurbo(this, 122, 105, textureX, textureY); // GUN47
		gunModel[46] = new ModelRendererTurbo(this, 122, 105, textureX, textureY); // GUN48
		gunModel[47] = new ModelRendererTurbo(this, 122, 105, textureX, textureY); // GUN49
		gunModel[48] = new ModelRendererTurbo(this, 122, 105, textureX, textureY); // GUN50
		gunModel[49] = new ModelRendererTurbo(this, 83, 70, textureX, textureY); // GUN51
		gunModel[50] = new ModelRendererTurbo(this, 83, 70, textureX, textureY); // GUN52
		gunModel[51] = new ModelRendererTurbo(this, 83, 70, textureX, textureY); // GUN53
		gunModel[52] = new ModelRendererTurbo(this, 83, 70, textureX, textureY); // GUN54
		gunModel[53] = new ModelRendererTurbo(this, 83, 70, textureX, textureY); // GUN55
		gunModel[54] = new ModelRendererTurbo(this, 83, 70, textureX, textureY); // GUN56
		gunModel[55] = new ModelRendererTurbo(this, 83, 70, textureX, textureY); // GUN57
		gunModel[56] = new ModelRendererTurbo(this, 83, 70, textureX, textureY); // GUN58
		gunModel[57] = new ModelRendererTurbo(this, 74, 68, textureX, textureY); // GUN59
		gunModel[58] = new ModelRendererTurbo(this, 74, 68, textureX, textureY); // GUN60
		gunModel[59] = new ModelRendererTurbo(this, 74, 68, textureX, textureY); // GUN61
		gunModel[60] = new ModelRendererTurbo(this, 74, 68, textureX, textureY); // GUN62
		gunModel[61] = new ModelRendererTurbo(this, 74, 68, textureX, textureY); // GUN63
		gunModel[62] = new ModelRendererTurbo(this, 74, 68, textureX, textureY); // GUN64
		gunModel[63] = new ModelRendererTurbo(this, 74, 68, textureX, textureY); // GUN65
		gunModel[64] = new ModelRendererTurbo(this, 74, 68, textureX, textureY); // GUN66
		gunModel[65] = new ModelRendererTurbo(this, 10, 85, textureX, textureY); // GUN67
		gunModel[66] = new ModelRendererTurbo(this, 42, 55, textureX, textureY); // GUN69
		gunModel[67] = new ModelRendererTurbo(this, 37, 35, textureX, textureY); // GUN70
		gunModel[68] = new ModelRendererTurbo(this, 41, 37, textureX, textureY); // GUN71
		gunModel[69] = new ModelRendererTurbo(this, 64, 18, textureX, textureY); // GUN72
		gunModel[70] = new ModelRendererTurbo(this, 16, 88, textureX, textureY); // GUN77
		gunModel[71] = new ModelRendererTurbo(this, 75, 6, textureX, textureY); // GUN78
		gunModel[72] = new ModelRendererTurbo(this, 52, 42, textureX, textureY); // GUN81
		gunModel[73] = new ModelRendererTurbo(this, 83, 23, textureX, textureY); // GUN83
		gunModel[74] = new ModelRendererTurbo(this, 56, 42, textureX, textureY); // GUN84
		gunModel[75] = new ModelRendererTurbo(this, 78, 23, textureX, textureY); // GUN87
		gunModel[76] = new ModelRendererTurbo(this, 10, 27, textureX, textureY); // GUN96
		gunModel[77] = new ModelRendererTurbo(this, 60, 16, textureX, textureY); // GUN101
		gunModel[78] = new ModelRendererTurbo(this, 78, 27, textureX, textureY); // GUN103
		gunModel[79] = new ModelRendererTurbo(this, 60, 16, textureX, textureY); // GUN106
		gunModel[80] = new ModelRendererTurbo(this, 93, 22, textureX, textureY); // Box 142
		gunModel[81] = new ModelRendererTurbo(this, 114, 94, textureX, textureY); // Box 145
		gunModel[82] = new ModelRendererTurbo(this, 70, 68, textureX, textureY); // Box 146
		gunModel[83] = new ModelRendererTurbo(this, 70, 68, textureX, textureY); // Box 147
		gunModel[84] = new ModelRendererTurbo(this, 114, 109, textureX, textureY); // Box 148
		gunModel[85] = new ModelRendererTurbo(this, 4, 64, textureX, textureY); // GUN103
		gunModel[86] = new ModelRendererTurbo(this, 8, 68, textureX, textureY); // GUN103
		gunModel[87] = new ModelRendererTurbo(this, 31, 19, textureX, textureY); // Box 142
		gunModel[88] = new ModelRendererTurbo(this, 29, 30, textureX, textureY); // Box 142
		gunModel[89] = new ModelRendererTurbo(this, 82, 0, textureX, textureY); // GUN17
		gunModel[90] = new ModelRendererTurbo(this, 31, 23, textureX, textureY); // Box 142
		gunModel[91] = new ModelRendererTurbo(this, 52, 16, textureX, textureY); // Box 142
		gunModel[92] = new ModelRendererTurbo(this, 0, 48, textureX, textureY); // Box 142
		gunModel[93] = new ModelRendererTurbo(this, 66, 4, textureX, textureY); // GUN17
		gunModel[94] = new ModelRendererTurbo(this, 36, 30, textureX, textureY); // GUN79
		gunModel[95] = new ModelRendererTurbo(this, 15, 95, textureX, textureY); // GUN121
		gunModel[96] = new ModelRendererTurbo(this, 36, 30, textureX, textureY); // GUN121
		gunModel[97] = new ModelRendererTurbo(this, 52, 42, textureX, textureY); // GUN81
		gunModel[98] = new ModelRendererTurbo(this, 56, 42, textureX, textureY); // GUN84
		gunModel[99] = new ModelRendererTurbo(this, 15, 95, textureX, textureY); // GUN121
		gunModel[100] = new ModelRendererTurbo(this, 36, 30, textureX, textureY); // GUN121
		gunModel[101] = new ModelRendererTurbo(this, 52, 42, textureX, textureY); // GUN81
		gunModel[102] = new ModelRendererTurbo(this, 56, 42, textureX, textureY); // GUN84
		gunModel[103] = new ModelRendererTurbo(this, 15, 95, textureX, textureY); // GUN121
		gunModel[104] = new ModelRendererTurbo(this, 36, 30, textureX, textureY); // GUN121
		gunModel[105] = new ModelRendererTurbo(this, 52, 42, textureX, textureY); // GUN81
		gunModel[106] = new ModelRendererTurbo(this, 56, 42, textureX, textureY); // GUN84
		gunModel[107] = new ModelRendererTurbo(this, 15, 95, textureX, textureY); // GUN121
		gunModel[108] = new ModelRendererTurbo(this, 36, 30, textureX, textureY); // GUN121
		gunModel[109] = new ModelRendererTurbo(this, 15, 95, textureX, textureY); // GUN121
		gunModel[110] = new ModelRendererTurbo(this, 14, 85, textureX, textureY); // GUN77
		gunModel[111] = new ModelRendererTurbo(this, 56, 42, textureX, textureY); // GUN118
		gunModel[112] = new ModelRendererTurbo(this, 9, 29, textureX, textureY); // GUN81
		gunModel[113] = new ModelRendererTurbo(this, 52, 42, textureX, textureY); // GUN81
		gunModel[114] = new ModelRendererTurbo(this, 83, 23, textureX, textureY); // GUN83
		gunModel[115] = new ModelRendererTurbo(this, 56, 42, textureX, textureY); // GUN84
		gunModel[116] = new ModelRendererTurbo(this, 10, 27, textureX, textureY); // GUN96
		gunModel[117] = new ModelRendererTurbo(this, 36, 30, textureX, textureY); // GUN79
		gunModel[118] = new ModelRendererTurbo(this, 15, 95, textureX, textureY); // GUN121
		gunModel[119] = new ModelRendererTurbo(this, 36, 30, textureX, textureY); // GUN121
		gunModel[120] = new ModelRendererTurbo(this, 52, 42, textureX, textureY); // GUN81
		gunModel[121] = new ModelRendererTurbo(this, 56, 42, textureX, textureY); // GUN84
		gunModel[122] = new ModelRendererTurbo(this, 15, 95, textureX, textureY); // GUN121
		gunModel[123] = new ModelRendererTurbo(this, 36, 30, textureX, textureY); // GUN121
		gunModel[124] = new ModelRendererTurbo(this, 52, 42, textureX, textureY); // GUN81
		gunModel[125] = new ModelRendererTurbo(this, 56, 42, textureX, textureY); // GUN84
		gunModel[126] = new ModelRendererTurbo(this, 15, 95, textureX, textureY); // GUN121
		gunModel[127] = new ModelRendererTurbo(this, 36, 30, textureX, textureY); // GUN121
		gunModel[128] = new ModelRendererTurbo(this, 52, 42, textureX, textureY); // GUN81
		gunModel[129] = new ModelRendererTurbo(this, 56, 42, textureX, textureY); // GUN84
		gunModel[130] = new ModelRendererTurbo(this, 15, 95, textureX, textureY); // GUN121
		gunModel[131] = new ModelRendererTurbo(this, 15, 95, textureX, textureY); // GUN121
		gunModel[132] = new ModelRendererTurbo(this, 56, 42, textureX, textureY); // GUN118
		gunModel[133] = new ModelRendererTurbo(this, 9, 29, textureX, textureY); // GUN81
		gunModel[134] = new ModelRendererTurbo(this, 4, 64, textureX, textureY); // GUN103
		gunModel[135] = new ModelRendererTurbo(this, 78, 27, textureX, textureY); // GUN103
		gunModel[136] = new ModelRendererTurbo(this, 8, 68, textureX, textureY); // GUN103
		gunModel[137] = new ModelRendererTurbo(this, 16, 88, textureX, textureY); // GUN77
		gunModel[138] = new ModelRendererTurbo(this, 75, 6, textureX, textureY); // GUN78
		gunModel[139] = new ModelRendererTurbo(this, 78, 23, textureX, textureY); // GUN87
		gunModel[140] = new ModelRendererTurbo(this, 14, 85, textureX, textureY); // GUN77
		gunModel[141] = new ModelRendererTurbo(this, 10, 85, textureX, textureY); // GUN67
		gunModel[142] = new ModelRendererTurbo(this, 42, 55, textureX, textureY); // GUN69
		gunModel[143] = new ModelRendererTurbo(this, 37, 35, textureX, textureY); // GUN70
		gunModel[144] = new ModelRendererTurbo(this, 41, 37, textureX, textureY); // GUN71
		gunModel[145] = new ModelRendererTurbo(this, 64, 18, textureX, textureY); // GUN72
		gunModel[146] = new ModelRendererTurbo(this, 49, 37, textureX, textureY); // GUN71
		gunModel[147] = new ModelRendererTurbo(this, 49, 37, textureX, textureY); // GUN71
		gunModel[148] = new ModelRendererTurbo(this, 110, 42, textureX, textureY); // GUN02
		gunModel[149] = new ModelRendererTurbo(this, 12, 72, textureX, textureY); // GUN02
		gunModel[150] = new ModelRendererTurbo(this, 12, 72, textureX, textureY); // GUN02
		gunModel[151] = new ModelRendererTurbo(this, 10, 76, textureX, textureY); // GUN02
		gunModel[152] = new ModelRendererTurbo(this, 107, 39, textureX, textureY); // GUN09

		gunModel[0].addShapeBox(0F, 0F, 0F, 9, 12, 14, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN01
		gunModel[0].setRotationPoint(12F, -26F, 3F);

		gunModel[1].addShapeBox(0F, 0F, 0F, 8, 11, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN02
		gunModel[1].setRotationPoint(12.5F, -25.5F, 17F);

		gunModel[2].addShapeBox(0F, 0F, 0F, 9, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN03
		gunModel[2].setRotationPoint(12F, -26F, 31F);

		gunModel[3].addShapeBox(0F, 0F, 0F, 9, 12, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN04
		gunModel[3].setRotationPoint(12F, -26F, -4F);

		gunModel[4].addShapeBox(0F, 0F, 0F, 7, 3, 7, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN05
		gunModel[4].setRotationPoint(13F, -29F, 0F);

		gunModel[5].addShapeBox(0F, 0F, 0F, 9, 3, 10, 0F, -1.5F, 0F, 0F, -1.5F, 0F, 0F, -1.5F, 0F, 0F, -1.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN06
		gunModel[5].setRotationPoint(12F, -29F, 7F);

		gunModel[6].addShapeBox(0F, 0F, 0F, 9, 12, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN07
		gunModel[6].setRotationPoint(12F, -26F, 24F);

		gunModel[7].addShapeBox(0F, 0F, 0F, 9, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN08
		gunModel[7].setRotationPoint(12F, -17F, 31F);

		gunModel[8].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN09
		gunModel[8].setRotationPoint(12F, -21F, 30.99F);

		gunModel[9].addShapeBox(0F, 0F, 0F, 5, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN10
		gunModel[9].setRotationPoint(14F, -23F, 31F);

		gunModel[10].addShapeBox(0F, 0F, 0F, 3, 25, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN11
		gunModel[10].setRotationPoint(15F, -19.5F, -33F);
		gunModel[10].rotateAngleX = 1.57079633F;

		gunModel[11].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN12
		gunModel[11].setRotationPoint(16F, -31F, 8F);

		gunModel[12].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN13
		gunModel[12].setRotationPoint(16F, -31F, 13F);

		gunModel[13].addShapeBox(0F, 0F, 0F, 3, 14, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN14
		gunModel[13].setRotationPoint(15F, -31F, 4F);
		gunModel[13].rotateAngleX = 1.57079633F;

		gunModel[14].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F); // GUN15
		gunModel[14].setRotationPoint(15F, -34F, 3.5F);

		gunModel[15].addShapeBox(0F, 0F, 0F, 2, 7, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN17
		gunModel[15].setRotationPoint(15.5F, -25F, -17F);
		gunModel[15].rotateAngleX = 1.57079633F;

		gunModel[16].addBox(0F, 0F, 0F, 3, 3, 1, 0F); // GUN18
		gunModel[16].setRotationPoint(22F, -24F, -3F);
		gunModel[16].rotateAngleY = 1.57079633F;

		gunModel[17].addBox(0F, 0F, 0F, 3, 3, 1, 0F); // GUN19
		gunModel[17].setRotationPoint(12F, -24F, -3F);
		gunModel[17].rotateAngleY = 1.57079633F;

		gunModel[18].addShapeBox(0F, 0F, 0F, 1, 8, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN20
		gunModel[18].setRotationPoint(19F, -26F, -31F);

		gunModel[19].addShapeBox(0F, 0F, 0F, 1, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN21
		gunModel[19].setRotationPoint(19F, -26F, -27F);

		gunModel[20].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN22
		gunModel[20].setRotationPoint(19F, -24F, -22F);

		gunModel[21].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN23
		gunModel[21].setRotationPoint(19F, -24F, -24F);

		gunModel[22].addShapeBox(0F, 0F, 0F, 9, 9, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN24
		gunModel[22].setRotationPoint(12F, -26F, -8F);

		gunModel[23].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN25
		gunModel[23].setRotationPoint(19F, -24F, -20F);

		gunModel[24].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN26
		gunModel[24].setRotationPoint(19F, -24F, -18F);

		gunModel[25].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN27
		gunModel[25].setRotationPoint(19F, -24F, -16F);

		gunModel[26].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN28
		gunModel[26].setRotationPoint(19F, -24F, -14F);

		gunModel[27].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN29
		gunModel[27].setRotationPoint(19F, -24F, -12F);

		gunModel[28].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN30
		gunModel[28].setRotationPoint(19F, -24F, -10F);

		gunModel[29].addShapeBox(0F, 0F, 0F, 1, 17, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN31
		gunModel[29].setRotationPoint(19F, -18F, -25F);
		gunModel[29].rotateAngleX = 1.57079633F;

		gunModel[30].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN32
		gunModel[30].setRotationPoint(19F, -25F, -10F);

		gunModel[31].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN33
		gunModel[31].setRotationPoint(19F, -25F, -12F);

		gunModel[32].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN34
		gunModel[32].setRotationPoint(19F, -25F, -14F);

		gunModel[33].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN35
		gunModel[33].setRotationPoint(19F, -25F, -18F);

		gunModel[34].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN36
		gunModel[34].setRotationPoint(19F, -25F, -16F);

		gunModel[35].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN37
		gunModel[35].setRotationPoint(19F, -25F, -20F);

		gunModel[36].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN38
		gunModel[36].setRotationPoint(19F, -25F, -24F);

		gunModel[37].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN39
		gunModel[37].setRotationPoint(19F, -25F, -22F);

		gunModel[38].addShapeBox(0F, 0F, 0F, 1, 8, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN40
		gunModel[38].setRotationPoint(13F, -26F, -31F);

		gunModel[39].addShapeBox(0F, 0F, 0F, 1, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN41
		gunModel[39].setRotationPoint(13F, -26F, -27F);

		gunModel[40].addShapeBox(0F, 0F, 0F, 1, 17, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN42
		gunModel[40].setRotationPoint(13F, -18F, -25F);
		gunModel[40].rotateAngleX = 1.57079633F;

		gunModel[41].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN43
		gunModel[41].setRotationPoint(13F, -24F, -24F);

		gunModel[42].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN44
		gunModel[42].setRotationPoint(13F, -24F, -22F);

		gunModel[43].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN45
		gunModel[43].setRotationPoint(13F, -24F, -20F);

		gunModel[44].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN46
		gunModel[44].setRotationPoint(13F, -24F, -18F);

		gunModel[45].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN47
		gunModel[45].setRotationPoint(13F, -24F, -16F);

		gunModel[46].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN48
		gunModel[46].setRotationPoint(13F, -24F, -12F);

		gunModel[47].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN49
		gunModel[47].setRotationPoint(13F, -24F, -14F);

		gunModel[48].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN50
		gunModel[48].setRotationPoint(13F, -24F, -10F);

		gunModel[49].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN51
		gunModel[49].setRotationPoint(13F, -25F, -24F);

		gunModel[50].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN52
		gunModel[50].setRotationPoint(13F, -25F, -10F);

		gunModel[51].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN53
		gunModel[51].setRotationPoint(13F, -25F, -12F);

		gunModel[52].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN54
		gunModel[52].setRotationPoint(13F, -25F, -14F);

		gunModel[53].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN55
		gunModel[53].setRotationPoint(13F, -25F, -16F);

		gunModel[54].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN56
		gunModel[54].setRotationPoint(13F, -25F, -18F);

		gunModel[55].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN57
		gunModel[55].setRotationPoint(13F, -25F, -20F);

		gunModel[56].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN58
		gunModel[56].setRotationPoint(13F, -25F, -22F);

		gunModel[57].addShapeBox(0F, 0F, 0F, 5, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN59
		gunModel[57].setRotationPoint(14F, -25F, -10F);

		gunModel[58].addShapeBox(0F, 0F, 0F, 5, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN60
		gunModel[58].setRotationPoint(14F, -25F, -12F);

		gunModel[59].addShapeBox(0F, 0F, 0F, 5, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN61
		gunModel[59].setRotationPoint(14F, -25F, -16F);

		gunModel[60].addShapeBox(0F, 0F, 0F, 5, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN62
		gunModel[60].setRotationPoint(14F, -25F, -14F);

		gunModel[61].addShapeBox(0F, 0F, 0F, 5, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN63
		gunModel[61].setRotationPoint(14F, -25F, -20F);

		gunModel[62].addShapeBox(0F, 0F, 0F, 5, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN64
		gunModel[62].setRotationPoint(14F, -25F, -18F);

		gunModel[63].addShapeBox(0F, 0F, 0F, 5, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN65
		gunModel[63].setRotationPoint(14F, -25F, -24F);

		gunModel[64].addShapeBox(0F, 0F, 0F, 5, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN66
		gunModel[64].setRotationPoint(14F, -25F, -22F);

		gunModel[65].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F); // GUN67
		gunModel[65].setRotationPoint(22F, -23F, -2F);

		gunModel[66].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN69
		gunModel[66].setRotationPoint(22F, -24F, -2F);

		gunModel[67].addShapeBox(0.05F, 0.05F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0.05F, 0.175F, 0F, 0.05F, 0.175F, 0F, 0F, 0F, 0F, 0F, -0.375F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.375F, 0F); // GUN70
		gunModel[67].setRotationPoint(21F, -26.5F, -2F);
		gunModel[67].rotateAngleZ = 0.34906585F;

		gunModel[68].addShapeBox(0F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN71
		gunModel[68].setRotationPoint(19F, -27F, -2.01F);

		gunModel[69].addShapeBox(0F, 0F, 0F, 1, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN72
		gunModel[69].setRotationPoint(18F, -27F, -1.01F);

		gunModel[70].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN77
		gunModel[70].setRotationPoint(21F, -21F, -2F);

		gunModel[71].addShapeBox(0F, 0F, 0F, 1, 1, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN78
		gunModel[71].setRotationPoint(21F, -19F, -7F);

		gunModel[72].addShapeBox(0F, 0.75F, 0F, 1, 3, 1, 0F, 0F, -0.45F, 0F, 0F, -0.45F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F); // GUN81
		gunModel[72].setRotationPoint(18F, -19F, -14.2F);
		gunModel[72].rotateAngleX = 0.62831853F;

		gunModel[73].addShapeBox(0F, 0F, 0F, 4, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN83
		gunModel[73].setRotationPoint(18F, -18F, -10F);

		gunModel[74].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN84
		gunModel[74].setRotationPoint(18F, -19F, -14F);
		gunModel[74].rotateAngleX = -0.62831853F;

		gunModel[75].addShapeBox(0F, 0F, 0F, 1, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.075F, 0.175F, 0F, -0.075F, 0.175F, 0F, 0.3F, -0.625F, 0F, 0.3F, -0.625F, 0F, 0F, -0.15F, 0F, 0F, -0.15F); // GUN87
		gunModel[75].setRotationPoint(21.01F, -18F, -10F);
		gunModel[75].rotateAngleX = 0.34906585F;

		gunModel[76].addShapeBox(0F, 0F, 0F, 2, 1, 1, 0F, 0.01F, 0F, 0F, 0.26F, 0F, 0F, 0.26F, 0F, 0F, 0.01F, 0F, 0F, 0.01F, 0F, 0F, 0.26F, 0F, 0F, 0.26F, 0F, 0F, 0.01F, 0F, 0F); // GUN96
		gunModel[76].setRotationPoint(18F, -18F, -29F);

		gunModel[77].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // GUN101
		gunModel[77].setRotationPoint(19.5F, -22F, -30F);

		gunModel[78].addShapeBox(0F, -0.3F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN103
		gunModel[78].setRotationPoint(20.3F, -20F, -29F);

		gunModel[79].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // GUN106
		gunModel[79].setRotationPoint(12.5F, -22F, -30F);

		gunModel[80].addShapeBox(0F, 0F, 0F, 3, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 142
		gunModel[80].setRotationPoint(12.5F, -27F, -19F);

		gunModel[81].addShapeBox(0F, 0F, 0F, 3, 8, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 145
		gunModel[81].setRotationPoint(9F, -20.5F, -27F);
		gunModel[81].rotateAngleX = 1.57079633F;

		gunModel[82].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 146
		gunModel[82].setRotationPoint(12F, -22.5F, -26F);

		gunModel[83].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 147
		gunModel[83].setRotationPoint(12F, -22.5F, -22F);

		gunModel[84].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // Box 148
		gunModel[84].setRotationPoint(9.5F, -23F, -27.5F);

		gunModel[85].addShapeBox(0F, -0.3F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.25F, 0F, -1F, 0.25F, 0F, -1F, 0.25F, 0F, 0F, 0.25F, 0F); // GUN103
		gunModel[85].setRotationPoint(20.3F, -18F, -29F);

		gunModel[86].addShapeBox(0F, -0.3F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN103
		gunModel[86].setRotationPoint(20.3F, -21F, -29F);

		gunModel[87].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 142
		gunModel[87].setRotationPoint(15.5F, -27F, -19F);

		gunModel[88].addShapeBox(0F, 0F, 0F, 2, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // Box 142
		gunModel[88].setRotationPoint(15.5F, -27F, -10F);

		gunModel[89].addShapeBox(0F, 0F, 0F, 2, 9, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN17
		gunModel[89].setRotationPoint(15.5F, -26F, -7F);
		gunModel[89].rotateAngleX = 1.57079633F;

		gunModel[90].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 142
		gunModel[90].setRotationPoint(10.5F, -25F, -19F);

		gunModel[91].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // Box 142
		gunModel[91].setRotationPoint(9.5F, -23F, -19F);

		gunModel[92].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 142
		gunModel[92].setRotationPoint(10.5F, -27F, -19F);

		gunModel[93].addShapeBox(0F, 0F, 0F, 3, 3, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN17
		gunModel[93].setRotationPoint(15F, -28.5F, 2F);

		gunModel[94].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F); // GUN79
		gunModel[94].setRotationPoint(18F, -19F, -26F);

		gunModel[95].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.1F, 0F, 0F, 0.1F); // GUN121
		gunModel[95].setRotationPoint(18.01F, -17F, -16F);

		gunModel[96].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F); // GUN121
		gunModel[96].setRotationPoint(18F, -19F, -14F);

		gunModel[97].addShapeBox(0F, 0.75F, 0F, 1, 3, 1, 0F, 0F, -0.45F, 0F, 0F, -0.45F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F); // GUN81
		gunModel[97].setRotationPoint(18F, -19F, -18.2F);
		gunModel[97].rotateAngleX = 0.62831853F;

		gunModel[98].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN84
		gunModel[98].setRotationPoint(18F, -19F, -18F);
		gunModel[98].rotateAngleX = -0.62831853F;

		gunModel[99].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.1F, 0F, 0F, 0.1F); // GUN121
		gunModel[99].setRotationPoint(18.01F, -17F, -20F);

		gunModel[100].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F); // GUN121
		gunModel[100].setRotationPoint(18F, -19F, -18F);

		gunModel[101].addShapeBox(0F, 0.75F, 0F, 1, 3, 1, 0F, 0F, -0.45F, 0F, 0F, -0.45F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F); // GUN81
		gunModel[101].setRotationPoint(18F, -19F, -22.2F);
		gunModel[101].rotateAngleX = 0.62831853F;

		gunModel[102].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN84
		gunModel[102].setRotationPoint(18F, -19F, -22F);
		gunModel[102].rotateAngleX = -0.62831853F;

		gunModel[103].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.1F, 0F, 0F, 0.1F); // GUN121
		gunModel[103].setRotationPoint(18.01F, -17F, -24F);

		gunModel[104].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F); // GUN121
		gunModel[104].setRotationPoint(18F, -19F, -22F);

		gunModel[105].addShapeBox(0F, 0.75F, 0F, 1, 3, 1, 0F, 0F, -0.45F, 0F, 0F, -0.45F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F); // GUN81
		gunModel[105].setRotationPoint(18F, -19F, -26.2F);
		gunModel[105].rotateAngleX = 0.62831853F;

		gunModel[106].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN84
		gunModel[106].setRotationPoint(18F, -19F, -26F);
		gunModel[106].rotateAngleX = -0.62831853F;

		gunModel[107].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.1F, 0F, 0F, 0.1F); // GUN121
		gunModel[107].setRotationPoint(18.01F, -17F, -28F);

		gunModel[108].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F); // GUN121
		gunModel[108].setRotationPoint(18F, -19F, -26F);

		gunModel[109].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.1F, 0F, 0F, 0.1F); // GUN121
		gunModel[109].setRotationPoint(18.01F, -17F, -12F);

		gunModel[110].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // GUN77
		gunModel[110].setRotationPoint(21F, -19F, -2F);

		gunModel[111].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN118
		gunModel[111].setRotationPoint(18F, -18F, -10F);
		gunModel[111].rotateAngleX = -0.9424778F;

		gunModel[112].addShapeBox(0F, 1.75F, 0F, 1, 2, 1, 0F, 0F, -0.45F, 0F, 0F, -0.45F, 0F, 0F, -0.35F, 0.175F, 0F, -0.35F, 0.175F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F); // GUN81
		gunModel[112].setRotationPoint(18F, -19F, -30.2F);
		gunModel[112].rotateAngleX = 0.62831853F;

		gunModel[113].addShapeBox(0F, 0.75F, 0F, 1, 3, 1, 0F, 0F, -0.45F, 0F, 0F, -0.45F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F); // GUN81
		gunModel[113].setRotationPoint(14F, -19F, -14.2F);
		gunModel[113].rotateAngleX = 0.62831853F;

		gunModel[114].addShapeBox(0F, 0F, 0F, 4, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN83
		gunModel[114].setRotationPoint(11F, -18F, -10F);

		gunModel[115].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN84
		gunModel[115].setRotationPoint(14F, -19F, -14F);
		gunModel[115].rotateAngleX = -0.62831853F;

		gunModel[116].addShapeBox(0F, 0F, 0F, 2, 1, 1, 0F, 0.26F, 0F, 0F, 0.01F, 0F, 0F, 0.01F, 0F, 0F, 0.26F, 0F, 0F, 0.26F, 0F, 0F, 0.01F, 0F, 0F, 0.01F, 0F, 0F, 0.26F, 0F, 0F); // GUN96
		gunModel[116].setRotationPoint(13F, -18F, -29F);

		gunModel[117].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F); // GUN79
		gunModel[117].setRotationPoint(14F, -19F, -26F);

		gunModel[118].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.1F, 0F, 0F, 0.1F); // GUN121
		gunModel[118].setRotationPoint(14.01F, -17F, -16F);

		gunModel[119].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F); // GUN121
		gunModel[119].setRotationPoint(14F, -19F, -14F);

		gunModel[120].addShapeBox(0F, 0.75F, 0F, 1, 3, 1, 0F, 0F, -0.45F, 0F, 0F, -0.45F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F); // GUN81
		gunModel[120].setRotationPoint(14F, -19F, -18.2F);
		gunModel[120].rotateAngleX = 0.62831853F;

		gunModel[121].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN84
		gunModel[121].setRotationPoint(14F, -19F, -18F);
		gunModel[121].rotateAngleX = -0.62831853F;

		gunModel[122].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.1F, 0F, 0F, 0.1F); // GUN121
		gunModel[122].setRotationPoint(14.01F, -17F, -20F);

		gunModel[123].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F); // GUN121
		gunModel[123].setRotationPoint(14F, -19F, -18F);

		gunModel[124].addShapeBox(0F, 0.75F, 0F, 1, 3, 1, 0F, 0F, -0.45F, 0F, 0F, -0.45F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F); // GUN81
		gunModel[124].setRotationPoint(14F, -19F, -22.2F);
		gunModel[124].rotateAngleX = 0.62831853F;

		gunModel[125].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN84
		gunModel[125].setRotationPoint(14F, -19F, -22F);
		gunModel[125].rotateAngleX = -0.62831853F;

		gunModel[126].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.1F, 0F, 0F, 0.1F); // GUN121
		gunModel[126].setRotationPoint(14.01F, -17F, -24F);

		gunModel[127].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F); // GUN121
		gunModel[127].setRotationPoint(14F, -19F, -22F);

		gunModel[128].addShapeBox(0F, 0.75F, 0F, 1, 3, 1, 0F, 0F, -0.45F, 0F, 0F, -0.45F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F); // GUN81
		gunModel[128].setRotationPoint(14F, -19F, -26.2F);
		gunModel[128].rotateAngleX = 0.62831853F;

		gunModel[129].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN84
		gunModel[129].setRotationPoint(14F, -19F, -26F);
		gunModel[129].rotateAngleX = -0.62831853F;

		gunModel[130].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.1F, 0F, 0F, 0.1F); // GUN121
		gunModel[130].setRotationPoint(14.01F, -17F, -28F);

		gunModel[131].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.1F, 0F, 0F, 0.1F); // GUN121
		gunModel[131].setRotationPoint(14.01F, -17F, -12F);

		gunModel[132].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN118
		gunModel[132].setRotationPoint(14F, -18F, -10F);
		gunModel[132].rotateAngleX = -0.9424778F;

		gunModel[133].addShapeBox(0F, 1.75F, 0F, 1, 2, 1, 0F, 0F, -0.45F, 0F, 0F, -0.45F, 0F, 0F, -0.35F, 0.175F, 0F, -0.35F, 0.175F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F); // GUN81
		gunModel[133].setRotationPoint(14F, -19F, -30.2F);
		gunModel[133].rotateAngleX = 0.62831853F;

		gunModel[134].addShapeBox(0F, -0.3F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0.25F, 0F, 0F, 0.25F, 0F, 0F, 0.25F, 0F, -1F, 0.25F, 0F); // GUN103
		gunModel[134].setRotationPoint(11.7F, -18F, -29F);

		gunModel[135].addShapeBox(0F, -0.3F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN103
		gunModel[135].setRotationPoint(11.7F, -20F, -29F);

		gunModel[136].addShapeBox(0F, -0.3F, 0F, 1, 1, 1, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN103
		gunModel[136].setRotationPoint(11.7F, -21F, -29F);

		gunModel[137].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN77
		gunModel[137].setRotationPoint(11F, -21F, -2F);

		gunModel[138].addShapeBox(0F, 0F, 0F, 1, 1, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN78
		gunModel[138].setRotationPoint(11F, -19F, -7F);

		gunModel[139].addShapeBox(0F, 0F, 0F, 1, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.075F, 0.175F, 0F, -0.075F, 0.175F, 0F, 0.3F, -0.625F, 0F, 0.3F, -0.625F, 0F, 0F, -0.15F, 0F, 0F, -0.15F); // GUN87
		gunModel[139].setRotationPoint(11.01F, -18F, -10F);
		gunModel[139].rotateAngleX = 0.34906585F;

		gunModel[140].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // GUN77
		gunModel[140].setRotationPoint(11F, -19F, -2F);

		gunModel[141].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F); // GUN67
		gunModel[141].setRotationPoint(10F, -23F, -2F);

		gunModel[142].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN69
		gunModel[142].setRotationPoint(10F, -24F, -2F);

		gunModel[143].addShapeBox(0.05F, 0.05F, 0F, 1, 3, 1, 0F, 0.05F, 0.175F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.05F, 0.175F, 0F, 0F, 0F, 0F, 0F, -0.375F, 0F, 0F, -0.375F, 0F, 0F, 0F, 0F); // GUN70
		gunModel[143].setRotationPoint(11F, -26.9F, -2F);
		gunModel[143].rotateAngleZ = -0.34906585F;

		gunModel[144].addShapeBox(0F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN71
		gunModel[144].setRotationPoint(11F, -27F, -1.99F);

		gunModel[145].addShapeBox(0F, 0F, 0F, 1, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN72
		gunModel[145].setRotationPoint(14F, -27F, -1.01F);

		gunModel[146].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN71
		gunModel[146].setRotationPoint(18F, -27F, -1.99F);

		gunModel[147].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN71
		gunModel[147].setRotationPoint(14F, -27F, -1.99F);

		gunModel[148].addShapeBox(0F, 0F, 0F, 8, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN02
		gunModel[148].setRotationPoint(12.5F, -28.5F, 17F);

		gunModel[149].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN02
		gunModel[149].setRotationPoint(13.5F, -28.5F, 18F);
		gunModel[149].rotateAngleY = 1.57079633F;

		gunModel[150].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN02
		gunModel[150].setRotationPoint(20.5F, -28.5F, 18F);
		gunModel[150].rotateAngleY = 1.57079633F;

		gunModel[151].addShapeBox(0F, 0F, 0F, 4, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN02
		gunModel[151].setRotationPoint(12.5F, -28.5F, 21F);
		gunModel[151].rotateAngleX = 1.57079633F;
		gunModel[151].rotateAngleY = -1.57079633F;

		gunModel[152].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN09
		gunModel[152].setRotationPoint(19F, -21F, 30.99F);

		parts.put("base",  baseModel);
		parts.put("gun", gunModel);
		translateAll(-16,-1,-16);

		if(doOffsets)
		{
			translate(gunModel,0,20,4);
		}

		flipAll();
	}
}
