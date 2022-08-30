package pl.pabilo8.immersiveintelligence.client.model.weapon.emplacement;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

public class ModelCPDS extends ModelIIBase
{
	int textureX = 128;
	int textureY = 128;

	//Counter-Projectile Defense System
	public ModelRendererTurbo[] gunModel, barrelsModel, observeModel, internalsModel, hatchModel;

	public ModelCPDS(boolean doOffsets) //Same as Filename
	{
		baseModel = new ModelRendererTurbo[34];
		baseModel[0] = new ModelRendererTurbo(this, 23, 110, textureX, textureY); // BASE01
		baseModel[1] = new ModelRendererTurbo(this, 8, 59, textureX, textureY); // BASE03
		baseModel[2] = new ModelRendererTurbo(this, 60, 28, textureX, textureY); // BASE04
		baseModel[3] = new ModelRendererTurbo(this, 60, 28, textureX, textureY); // BASE05
		baseModel[4] = new ModelRendererTurbo(this, 0, 54, textureX, textureY); // COVER01
		baseModel[5] = new ModelRendererTurbo(this, 106, 40, textureX, textureY); // COVER02
		baseModel[6] = new ModelRendererTurbo(this, 106, 40, textureX, textureY); // COVER04
		baseModel[7] = new ModelRendererTurbo(this, 8, 70, textureX, textureY); // COVER05
		baseModel[8] = new ModelRendererTurbo(this, 102, 18, textureX, textureY); // COVER06
		baseModel[9] = new ModelRendererTurbo(this, 92, 24, textureX, textureY); // BASE06
		baseModel[10] = new ModelRendererTurbo(this, 92, 24, textureX, textureY); // BASE07
		baseModel[11] = new ModelRendererTurbo(this, 62, 75, textureX, textureY); // COVER07
		baseModel[12] = new ModelRendererTurbo(this, 18, 59, textureX, textureY); // COVER09
		baseModel[13] = new ModelRendererTurbo(this, 0, 51, textureX, textureY); // COVER11
		baseModel[14] = new ModelRendererTurbo(this, 0, 41, textureX, textureY); // COVER12
		baseModel[15] = new ModelRendererTurbo(this, 52, 0, textureX, textureY); // COVER13
		baseModel[16] = new ModelRendererTurbo(this, 0, 41, textureX, textureY); // COVER14
		baseModel[17] = new ModelRendererTurbo(this, 0, 28, textureX, textureY); // COVER15
		baseModel[18] = new ModelRendererTurbo(this, 22, 28, textureX, textureY); // COVER16
		baseModel[19] = new ModelRendererTurbo(this, 59, 7, textureX, textureY); // COVER17
		baseModel[20] = new ModelRendererTurbo(this, 70, 7, textureX, textureY); // COVER18
		baseModel[21] = new ModelRendererTurbo(this, 0, 66, textureX, textureY); // COVER19
		baseModel[22] = new ModelRendererTurbo(this, 38, 95, textureX, textureY); // COVER20
		baseModel[23] = new ModelRendererTurbo(this, 60, 58, textureX, textureY); // COVER21
		baseModel[24] = new ModelRendererTurbo(this, 36, 18, textureX, textureY); // COVER24
		baseModel[25] = new ModelRendererTurbo(this, 86, 47, textureX, textureY); // COVER25
		baseModel[26] = new ModelRendererTurbo(this, 8, 70, textureX, textureY); // COVER03
		baseModel[27] = new ModelRendererTurbo(this, 86, 47, textureX, textureY); // COVER26
		baseModel[28] = new ModelRendererTurbo(this, 36, 18, textureX, textureY); // COVER27
		baseModel[29] = new ModelRendererTurbo(this, 18, 59, textureX, textureY); // COVER09
		baseModel[30] = new ModelRendererTurbo(this, 60, 58, textureX, textureY); // COVER21
		baseModel[31] = new ModelRendererTurbo(this, 8, 59, textureX, textureY); // BASE03
		baseModel[32] = new ModelRendererTurbo(this, 23, 110, textureX, textureY); // BASE01
		baseModel[33] = new ModelRendererTurbo(this, 38, 102, textureX, textureY); // COVER20

		baseModel[0].addBox(0F, 0F, 0F, 30, 3, 15, 0F); // BASE01
		baseModel[0].setRotationPoint(-15F, -3F, -15F);

		baseModel[1].addBox(0F, 0F, 0F, 2, 3, 3, 0F); // BASE03
		baseModel[1].setRotationPoint(-7F, -21F, -9F);

		baseModel[2].addShapeBox(0F, 0F, 0F, 1, 18, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE04
		baseModel[2].setRotationPoint(-7.5F, -22F, -11F);

		baseModel[3].addShapeBox(0F, 0F, 0F, 1, 18, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE05
		baseModel[3].setRotationPoint(6.5F, -22F, -11F);

		baseModel[4].addBox(0F, 0F, 0F, 1, 9, 1, 0F); // COVER01
		baseModel[4].setRotationPoint(-16F, -28F, 7F);
		baseModel[4].rotateAngleX = 1.57079633F;

		baseModel[5].addBox(0F, 0F, 0F, 10, 16, 1, 0F); // COVER02
		baseModel[5].setRotationPoint(6F, -19F, -15F);

		baseModel[6].addBox(0F, 0F, 0F, 10, 16, 1, 0F); // COVER04
		baseModel[6].setRotationPoint(-16F, -19F, -15F);

		baseModel[7].addShapeBox(0F, 0F, 0F, 8, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.15F, 0F, 0F, 0.15F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // COVER05
		baseModel[7].setRotationPoint(-15F, -29F, -5F);
		baseModel[7].rotateAngleX = -0.78539816F;

		baseModel[8].addBox(0F, 0F, 0F, 12, 2, 1, 0F); // COVER06
		baseModel[8].setRotationPoint(-6F, -5F, -15F);

		baseModel[9].addShapeBox(0F, 0F, 0F, 2, 18, 5, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE06
		baseModel[9].setRotationPoint(-9.5F, -21F, -10F);

		baseModel[10].addShapeBox(0F, 0F, 0F, 2, 18, 5, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BASE07
		baseModel[10].setRotationPoint(7.5F, -21F, -10F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 32, 19, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // COVER07
		baseModel[11].setRotationPoint(-16F, -28F, 15F);

		baseModel[12].addShapeBox(0F, 0F, 0F, 20, 9, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // COVER09
		baseModel[12].setRotationPoint(16F, -28F, -5F);
		baseModel[12].rotateAngleY = 1.57079633F;

		baseModel[13].addBox(0F, 0F, 0F, 1, 21, 1, 0F); // COVER11
		baseModel[13].setRotationPoint(-15F, -28F, 7F);
		baseModel[13].rotateAngleZ = 1.57079633F;

		baseModel[14].addBox(0F, 0F, 0F, 1, 31, 1, 0F); // COVER12
		baseModel[14].setRotationPoint(-15F, -28F, 15F);
		baseModel[14].rotateAngleZ = 1.57079633F;

		baseModel[15].addBox(0F, 0F, 0F, 21, 0, 7, 0F); // COVER13
		baseModel[15].setRotationPoint(-15F, -28.5F, 8F);

		baseModel[16].addBox(0F, 0F, 0F, 1, 20, 1, 0F); // COVER14
		baseModel[16].setRotationPoint(6F, -28F, -5F);
		baseModel[16].rotateAngleX = 1.57079633F;

		baseModel[17].addBox(0F, 0F, 0F, 10, 12, 1, 0F); // COVER15
		baseModel[17].setRotationPoint(-16F, -28F, -5F);
		baseModel[17].rotateAngleX = 1.57079633F;

		baseModel[18].addBox(0F, 0F, 0F, 2, 4, 3, 0F); // COVER16
		baseModel[18].setRotationPoint(-12F, -33F, 1F);

		baseModel[19].addBox(0F, 0F, 0F, 5, 1, 1, 0F); // COVER17
		baseModel[19].setRotationPoint(-13.5F, -32F, 2F);

		baseModel[20].addBox(0F, 0F, 0F, 9, 20, 1, 0F); // COVER18
		baseModel[20].setRotationPoint(7F, -28F, -5F);
		baseModel[20].rotateAngleX = 1.57079633F;

		baseModel[21].addShapeBox(0F, 0F, 0F, 1, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // COVER19
		baseModel[21].setRotationPoint(-16F, -9F, 15F);

		baseModel[22].addShapeBox(0F, 0F, 0F, 23, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // COVER20
		baseModel[22].setRotationPoint(-7F, -9F, 15F);

		baseModel[23].addShapeBox(0F, 0F, 0F, 29, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // COVER21
		baseModel[23].setRotationPoint(-15F, -19F, -14F);
		baseModel[23].rotateAngleY = 1.57079633F;

		baseModel[24].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(10, 0, 10, 0), new Coord2D(10, 10, 10, 10) }), 1, 10, 10, 35, 1, ModelRendererTurbo.MR_FRONT, new float[] {15 ,10 ,10}); // COVER24
		baseModel[24].setRotationPoint(-15F, -19F, -15F);
		baseModel[24].rotateAngleY = -1.57079633F;

		baseModel[25].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(10, 0, 10, 0), new Coord2D(10, 10, 10, 10) }), 1, 10, 10, 35, 1, ModelRendererTurbo.MR_FRONT, new float[] {15 ,10 ,10}); // COVER25
		baseModel[25].setRotationPoint(-6F, -19F, -15F);
		baseModel[25].rotateAngleY = -1.57079633F;

		baseModel[26].addShapeBox(0F, 0F, 0F, 8, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.15F, 0F, 0F, 0.15F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // COVER03
		baseModel[26].setRotationPoint(7F, -29F, -5F);
		baseModel[26].rotateAngleX = -0.78539816F;

		baseModel[27].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(10, 0, 10, 0), new Coord2D(10, 10, 10, 10) }), 1, 10, 10, 35, 1, ModelRendererTurbo.MR_FRONT, new float[] {15 ,10 ,10}); // COVER26
		baseModel[27].setRotationPoint(16F, -19F, -15F);
		baseModel[27].rotateAngleY = -1.57079633F;

		baseModel[28].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(10, 0, 10, 0), new Coord2D(10, 10, 10, 10) }), 1, 10, 10, 35, 1, ModelRendererTurbo.MR_FRONT, new float[] {15 ,10 ,10}); // COVER27
		baseModel[28].setRotationPoint(7F, -19F, -15F);
		baseModel[28].rotateAngleY = -1.57079633F;

		baseModel[29].addShapeBox(0F, 0F, 0F, 20, 9, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // COVER09
		baseModel[29].setRotationPoint(-15F, -28F, -5F);
		baseModel[29].rotateAngleY = 1.57079633F;

		baseModel[30].addShapeBox(0F, 0F, 0F, 29, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // COVER21
		baseModel[30].setRotationPoint(16F, -19F, -14F);
		baseModel[30].rotateAngleY = 1.57079633F;

		baseModel[31].addBox(0F, 0F, 0F, 2, 3, 3, 0F); // BASE03
		baseModel[31].setRotationPoint(5F, -21F, -9F);

		baseModel[32].addBox(0F, 0F, 0F, 30, 3, 15, 0F); // BASE01
		baseModel[32].setRotationPoint(15F, -3F, 15F);
		baseModel[32].rotateAngleY = -3.14159265F;

		baseModel[33].addShapeBox(0F, 0F, 0F, 12, 7, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // COVER20
		baseModel[33].setRotationPoint(-16F, -24F, -2F);
		baseModel[33].rotateAngleY = 1.57079633F;


		gunModel = new ModelRendererTurbo[33];
		gunModel[0] = new ModelRendererTurbo(this, 10, 69, textureX, textureY); // GUN01
		gunModel[1] = new ModelRendererTurbo(this, 4, 17, textureX, textureY); // GUN02
		gunModel[2] = new ModelRendererTurbo(this, 12, 95, textureX, textureY); // GUN03
		gunModel[3] = new ModelRendererTurbo(this, 4, 0, textureX, textureY); // GUN04
		gunModel[4] = new ModelRendererTurbo(this, 0, 112, textureX, textureY); // GUN05
		gunModel[5] = new ModelRendererTurbo(this, 46, 69, textureX, textureY); // GUN06
		gunModel[6] = new ModelRendererTurbo(this, 30, 20, textureX, textureY); // GUN07
		gunModel[7] = new ModelRendererTurbo(this, 30, 20, textureX, textureY); // GUN08
		gunModel[8] = new ModelRendererTurbo(this, 0, 99, textureX, textureY); // GUN09
		gunModel[9] = new ModelRendererTurbo(this, 44, 9, textureX, textureY); // GUN10
		gunModel[10] = new ModelRendererTurbo(this, 106, 23, textureX, textureY); // GUN11
		gunModel[11] = new ModelRendererTurbo(this, 98, 101, textureX, textureY); // GUN12
		gunModel[12] = new ModelRendererTurbo(this, 108, 21, textureX, textureY); // GUN13
		gunModel[13] = new ModelRendererTurbo(this, 98, 113, textureX, textureY); // GUN14
		gunModel[14] = new ModelRendererTurbo(this, 102, 113, textureX, textureY); // GUN15
		gunModel[15] = new ModelRendererTurbo(this, 98, 98, textureX, textureY); // GUN16
		gunModel[16] = new ModelRendererTurbo(this, 4, 42, textureX, textureY); // GUN17
		gunModel[17] = new ModelRendererTurbo(this, 122, 117, textureX, textureY); // GUN18
		gunModel[18] = new ModelRendererTurbo(this, 38, 9, textureX, textureY); // GUN19
		gunModel[19] = new ModelRendererTurbo(this, 116, 95, textureX, textureY); // GUN20
		gunModel[20] = new ModelRendererTurbo(this, 20, 109, textureX, textureY); // Box 75
		gunModel[21] = new ModelRendererTurbo(this, 16, 38, textureX, textureY); // Box 76
		gunModel[22] = new ModelRendererTurbo(this, 56, 15, textureX, textureY); // Box 78
		gunModel[23] = new ModelRendererTurbo(this, 56, 15, textureX, textureY); // Box 78
		gunModel[24] = new ModelRendererTurbo(this, 38, 9, textureX, textureY); // GUN19
		gunModel[25] = new ModelRendererTurbo(this, 20, 109, textureX, textureY); // Box 75
		gunModel[26] = new ModelRendererTurbo(this, 0, 112, textureX, textureY); // GUN05
		gunModel[27] = new ModelRendererTurbo(this, 64, 102, textureX, textureY); // GUN05
		gunModel[28] = new ModelRendererTurbo(this, 64, 102, textureX, textureY); // GUN05
		gunModel[29] = new ModelRendererTurbo(this, 116, 95, textureX, textureY); // GUN20
		gunModel[30] = new ModelRendererTurbo(this, 4, 17, textureX, textureY); // GUN02
		gunModel[31] = new ModelRendererTurbo(this, 98, 115, textureX, textureY); // Shape 133
		gunModel[32] = new ModelRendererTurbo(this, 98, 115, textureX, textureY); // Shape 133

		gunModel[0].addBox(0F, 0F, 0F, 10, 10, 16, 0F); // GUN01
		gunModel[0].setRotationPoint(-5F, -24F, -15F);

		gunModel[1].addBox(0F, 0F, 0F, 10, 5, 6, 0F); // GUN02
		gunModel[1].setRotationPoint(-5F, -27F, 1F);

		gunModel[2].addBox(0F, 0F, 0F, 10, 10, 3, 0F); // GUN03
		gunModel[2].setRotationPoint(-5F, -24F, -9F);
		gunModel[2].rotateAngleX = 1.57079633F;

		gunModel[3].addBox(0F, 0F, 0F, 8, 8, 9, 0F); // GUN04
		gunModel[3].setRotationPoint(-4F, -23F, -24F);

		gunModel[4].addBox(0F, 0F, 0F, 7, 7, 3, 0F); // GUN05
		gunModel[4].setRotationPoint(-3.5F, -14F, -24F);

		gunModel[5].addTrapezoid(0F, 0F, 0F, 6, 6, 1, 0F, -0.50F, ModelRendererTurbo.MR_FRONT); // GUN06
		gunModel[5].setRotationPoint(-3F, -13.5F, -25F);

		gunModel[6].addBox(0F, 0F, 0F, 1, 1, 2, 0F); // GUN07
		gunModel[6].setRotationPoint(-0.5F, -15F, -23F);

		gunModel[7].addBox(0F, 0F, 0F, 1, 1, 2, 0F); // GUN08
		gunModel[7].setRotationPoint(-0.5F, -15F, -19F);

		gunModel[8].addBox(0F, 0F, 0F, 3, 10, 3, 0F); // GUN09
		gunModel[8].setRotationPoint(-5F, -14F, 1F);
		gunModel[8].rotateAngleZ = 1.57079633F;

		gunModel[9].addShapeBox(0F, 0F, 0F, 10, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -3F, 0F, 0F, -3F); // GUN10
		gunModel[9].setRotationPoint(-5F, -17F, 4F);

		gunModel[10].addShapeBox(0F, 0F, 0F, 10, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F); // GUN11
		gunModel[10].setRotationPoint(-5F, -27F, -9F);
		gunModel[10].rotateAngleX = 1.57079633F;

		gunModel[11].addShapeBox(0F, 0F, 0F, 10, 3, 5, 0F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN12
		gunModel[11].setRotationPoint(-5F, -27F, -14F);

		gunModel[12].addShapeBox(0F, 0F, 0F, 8, 1, 1, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN13
		gunModel[12].setRotationPoint(-4F, -28F, -10F);

		gunModel[13].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, -1F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN14
		gunModel[13].setRotationPoint(-5F, -28F, -10F);

		gunModel[14].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN15
		gunModel[14].setRotationPoint(4F, -28F, -10F);

		gunModel[15].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // GUN16
		gunModel[15].setRotationPoint(-1F, -27F, -23.5F);

		gunModel[16].addShapeBox(0F, 0F, 0F, 3, 14, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN17
		gunModel[16].setRotationPoint(-1.5F, -24.5F, -23F);
		gunModel[16].rotateAngleX = 1.57079633F;

		gunModel[17].addShapeBox(0F, 0F, 0F, 1, 6, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // GUN18
		gunModel[17].setRotationPoint(-0.5F, -28.5F, -18F);

		gunModel[18].addBox(0F, 0F, 0F, 2, 6, 1, 0F); // GUN19
		gunModel[18].setRotationPoint(-6F, -20F, 1.5F);
		gunModel[18].rotateAngleY = -1.57079633F;
		gunModel[18].rotateAngleZ = 1.57079633F;

		gunModel[19].addBox(0F, 0F, 0F, 5, 5, 1, 0F); // GUN20
		gunModel[19].setRotationPoint(-5F, -22F, -10F);
		gunModel[19].rotateAngleY = 1.57079633F;

		gunModel[20].addShapeBox(0F, 0F, 0F, 7, 14, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F); // Box 75
		gunModel[20].setRotationPoint(3.5F, -14F, -13F);
		gunModel[20].rotateAngleX = 1.57079633F;
		gunModel[20].rotateAngleZ = -1.57079633F;

		gunModel[21].addShapeBox(0F, 0F, 0F, 7, 14, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 76
		gunModel[21].setRotationPoint(-3.5F, -7F, -13F);
		gunModel[21].rotateAngleX = 1.57079633F;

		gunModel[22].addBox(0F, 0F, 0F, 6, 8, 1, 0F); // Box 78
		gunModel[22].setRotationPoint(6F, -20F, -4.5F);
		gunModel[22].rotateAngleY = 1.57079633F;

		gunModel[23].addBox(0F, 0F, 0F, 6, 8, 1, 0F); // Box 78
		gunModel[23].setRotationPoint(-5F, -20F, -4.5F);
		gunModel[23].rotateAngleY = 1.57079633F;

		gunModel[24].addBox(0F, 0F, 0F, 2, 6, 1, 0F); // GUN19
		gunModel[24].setRotationPoint(5F, -20F, 1.5F);
		gunModel[24].rotateAngleY = -1.57079633F;
		gunModel[24].rotateAngleZ = 1.57079633F;

		gunModel[25].addShapeBox(0F, 0F, 0F, 7, 14, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F); // Box 75
		gunModel[25].setRotationPoint(-3.5F, -7F, -13F);
		gunModel[25].rotateAngleX = 1.57079633F;
		gunModel[25].rotateAngleZ = -4.71238898F;

		gunModel[26].addBox(0F, 0F, 0F, 7, 7, 3, 0F); // GUN05
		gunModel[26].setRotationPoint(-3.5F, -14F, -16F);
		gunModel[26].rotateAngleX = 3.14159265F;
		gunModel[26].rotateAngleZ = -1.57079633F;

		gunModel[27].addBox(0F, 0F, 0F, 7, 1, 7, 0F); // GUN05
		gunModel[27].setRotationPoint(-3.5F, -7F, -21F);
		gunModel[27].rotateAngleX = 1.57079633F;

		gunModel[28].addBox(0F, 0F, 0F, 7, 1, 7, 0F); // GUN05
		gunModel[28].setRotationPoint(3.5F, -14F, -19F);
		gunModel[28].rotateAngleX = 1.57079633F;
		gunModel[28].rotateAngleY = -3.14159265F;
		gunModel[28].rotateAngleZ = -1.57079633F;

		gunModel[29].addBox(0F, 0F, 0F, 5, 5, 1, 0F); // GUN20
		gunModel[29].setRotationPoint(6F, -22F, -10F);
		gunModel[29].rotateAngleY = 1.57079633F;

		gunModel[30].addBox(0F, 0F, 0F, 10, 5, 6, 0F); // GUN02
		gunModel[30].setRotationPoint(5F, -17F, 1F);
		gunModel[30].rotateAngleZ = 3.14159265F;

		gunModel[31].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(5, 0, 5, 0), new Coord2D(7, 2, 7, 2), new Coord2D(7, 5, 7, 5), new Coord2D(5, 7, 5, 7), new Coord2D(2, 7, 2, 7), new Coord2D(0, 5, 0, 5), new Coord2D(0, 2, 0, 2) }), 3, 7, 7, 24, 3, ModelRendererTurbo.MR_FRONT, new float[] {3 ,3 ,3 ,3 ,3 ,3 ,3 ,3}); // Shape 133
		gunModel[31].setRotationPoint(3.5F, -15.5F, -33F);

		gunModel[32].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(5, 0, 5, 0), new Coord2D(7, 2, 7, 2), new Coord2D(7, 5, 7, 5), new Coord2D(5, 7, 5, 7), new Coord2D(2, 7, 2, 7), new Coord2D(0, 5, 0, 5), new Coord2D(0, 2, 0, 2) }), 3, 7, 7, 24, 3, ModelRendererTurbo.MR_FRONT, new float[] {3 ,3 ,3 ,3 ,3 ,3 ,3 ,3}); // Shape 133
		gunModel[32].setRotationPoint(3.5F, -15.5F, -24F);

		barrelsModel = new ModelRendererTurbo[16];
		barrelsModel[0] = new ModelRendererTurbo(this, 120, 57, textureX, textureY); // BARRELS04
		barrelsModel[1] = new ModelRendererTurbo(this, 56, 24, textureX, textureY); // BARRELS12
		barrelsModel[2] = new ModelRendererTurbo(this, 56, 24, textureX, textureY); // BARRELS13
		barrelsModel[3] = new ModelRendererTurbo(this, 56, 24, textureX, textureY); // BARRELS14
		barrelsModel[4] = new ModelRendererTurbo(this, 56, 24, textureX, textureY); // BARRELS15
		barrelsModel[5] = new ModelRendererTurbo(this, 56, 24, textureX, textureY); // BARRELS16
		barrelsModel[6] = new ModelRendererTurbo(this, 56, 24, textureX, textureY); // BARRELS17
		barrelsModel[7] = new ModelRendererTurbo(this, 56, 24, textureX, textureY); // BARRELS18
		barrelsModel[8] = new ModelRendererTurbo(this, 56, 24, textureX, textureY); // BARRELS19
		barrelsModel[9] = new ModelRendererTurbo(this, 120, 57, textureX, textureY); // BARRELS04
		barrelsModel[10] = new ModelRendererTurbo(this, 120, 57, textureX, textureY); // BARRELS04
		barrelsModel[11] = new ModelRendererTurbo(this, 120, 57, textureX, textureY); // BARRELS04
		barrelsModel[12] = new ModelRendererTurbo(this, 120, 57, textureX, textureY); // BARRELS04
		barrelsModel[13] = new ModelRendererTurbo(this, 120, 57, textureX, textureY); // BARRELS04
		barrelsModel[14] = new ModelRendererTurbo(this, 120, 57, textureX, textureY); // BARRELS04
		barrelsModel[15] = new ModelRendererTurbo(this, 120, 57, textureX, textureY); // BARRELS04

		barrelsModel[0].addShapeBox(-1F, 0F, -3.5F, 2, 16, 2, 0F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F); // BARRELS04
		barrelsModel[0].setRotationPoint(0F, -19F, -26F);
		barrelsModel[0].rotateAngleX = 4.71238898F;

		barrelsModel[1].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F); // BARRELS12
		barrelsModel[1].setRotationPoint(-1F, -22.5F, -43.2F);

		barrelsModel[2].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F); // BARRELS13
		barrelsModel[2].setRotationPoint(-1F, -17.5F, -43.2F);

		barrelsModel[3].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F); // BARRELS14
		barrelsModel[3].setRotationPoint(-3.5F, -20F, -43.2F);

		barrelsModel[4].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F); // BARRELS15
		barrelsModel[4].setRotationPoint(1.5F, -20F, -43.2F);

		barrelsModel[5].addShapeBox(0.5F, 0F, 0F, 2, 2, 2, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F); // BARRELS16
		barrelsModel[5].setRotationPoint(0F, -20.5F, -43.2F);
		barrelsModel[5].rotateAngleZ = 0.78539816F;

		barrelsModel[6].addShapeBox(0F, -0.5F, 0F, 2, 2, 2, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F); // BARRELS17
		barrelsModel[6].setRotationPoint(-3F, -20.5F, -43.2F);
		barrelsModel[6].rotateAngleZ = 0.78539816F;

		barrelsModel[7].addShapeBox(0.2F, -0.7F, 0F, 2, 2, 2, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F); // BARRELS18
		barrelsModel[7].setRotationPoint(-3F, -16.5F, -43.2F);
		barrelsModel[7].rotateAngleZ = 0.78539816F;

		barrelsModel[8].addShapeBox(0F, -0.9F, 0F, 2, 2, 2, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F, -0.2F, -0.2F, 0F); // BARRELS19
		barrelsModel[8].setRotationPoint(1F, -16.5F, -43.2F);
		barrelsModel[8].rotateAngleZ = 0.78539816F;

		barrelsModel[9].addShapeBox(-1F, 0F, -3.5F, 2, 16, 2, 0F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F); // BARRELS04
		barrelsModel[9].setRotationPoint(0F, -19F, -26F);
		barrelsModel[9].rotateAngleX = 4.71238898F;
		barrelsModel[9].rotateAngleZ = 1.57079633F;

		barrelsModel[10].addShapeBox(-1F, 0F, -3.5F, 2, 16, 2, 0F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F); // BARRELS04
		barrelsModel[10].setRotationPoint(0F, -19F, -26F);
		barrelsModel[10].rotateAngleX = 4.71238898F;
		barrelsModel[10].rotateAngleZ = 3.14159265F;

		barrelsModel[11].addShapeBox(-1F, 0F, -3.5F, 2, 16, 2, 0F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F); // BARRELS04
		barrelsModel[11].setRotationPoint(0F, -19F, -26F);
		barrelsModel[11].rotateAngleX = 4.71238898F;
		barrelsModel[11].rotateAngleZ = 4.71238898F;

		barrelsModel[12].addShapeBox(-1F, 0F, -3.5F, 2, 16, 2, 0F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F); // BARRELS04
		barrelsModel[12].setRotationPoint(0F, -19F, -26F);
		barrelsModel[12].rotateAngleX = 4.71238898F;
		barrelsModel[12].rotateAngleZ = 0.78539816F;

		barrelsModel[13].addShapeBox(-1F, 0F, -3.5F, 2, 16, 2, 0F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F); // BARRELS04
		barrelsModel[13].setRotationPoint(0F, -19F, -26F);
		barrelsModel[13].rotateAngleX = 4.71238898F;
		barrelsModel[13].rotateAngleZ = 2.35619449F;

		barrelsModel[14].addShapeBox(-1F, 0F, -3.5F, 2, 16, 2, 0F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F); // BARRELS04
		barrelsModel[14].setRotationPoint(0F, -19F, -26F);
		barrelsModel[14].rotateAngleX = 4.71238898F;
		barrelsModel[14].rotateAngleZ = 3.92699082F;

		barrelsModel[15].addShapeBox(-1F, 0F, -3.5F, 2, 16, 2, 0F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F, -0.375F, 0F, -0.375F); // BARRELS04
		barrelsModel[15].setRotationPoint(0F, -19F, -26F);
		barrelsModel[15].rotateAngleX = 4.71238898F;
		barrelsModel[15].rotateAngleZ = 5.49778714F;


		observeModel = new ModelRendererTurbo[13];
		observeModel[0] = new ModelRendererTurbo(this, 90, 0, textureX, textureY); // IROBSERVE01
		observeModel[1] = new ModelRendererTurbo(this, 93, 7, textureX, textureY); // IROBSERVE02
		observeModel[2] = new ModelRendererTurbo(this, 93, 7, textureX, textureY); // IROBSERVE03
		observeModel[3] = new ModelRendererTurbo(this, 91, 18, textureX, textureY); // IROBSERVE04
		observeModel[4] = new ModelRendererTurbo(this, 5, 0, textureX, textureY); // IROBSERVE05
		observeModel[5] = new ModelRendererTurbo(this, 74, 35, textureX, textureY); // IROBSERVE06
		observeModel[6] = new ModelRendererTurbo(this, 112, 117, textureX, textureY); // IROBSERVE07
		observeModel[7] = new ModelRendererTurbo(this, 112, 109, textureX, textureY); // IROBSERVE08
		observeModel[8] = new ModelRendererTurbo(this, 98, 109, textureX, textureY); // IROBSERVE09
		observeModel[9] = new ModelRendererTurbo(this, 98, 111, textureX, textureY); // IROBSERVE09
		observeModel[10] = new ModelRendererTurbo(this, 82, 35, textureX, textureY); // IROBSERVE09
		observeModel[11] = new ModelRendererTurbo(this, 98, 101, textureX, textureY); // IROBSERVE09
		observeModel[12] = new ModelRendererTurbo(this, 98, 98, textureX, textureY); // IROBSERVE07

		observeModel[0].addShapeBox(0F, 0F, 0F, 8, 7, 11, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // IROBSERVE01
		observeModel[0].setRotationPoint(-15F, -40F, -5F);

		observeModel[1].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // IROBSERVE02
		observeModel[1].setRotationPoint(-12F, -30F, 1F);
		observeModel[1].rotateAngleX = 1.57079633F;
		observeModel[1].rotateAngleZ = 1.57079633F;

		observeModel[2].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // IROBSERVE03
		observeModel[2].setRotationPoint(-9F, -30F, 1F);
		observeModel[2].rotateAngleX = 1.57079633F;
		observeModel[2].rotateAngleZ = 1.57079633F;

		observeModel[3].addShapeBox(0F, 0F, 0F, 3, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // IROBSERVE04
		observeModel[3].setRotationPoint(-10.5F, -44F, -1F);
		observeModel[3].rotateAngleY = 1.57079633F;

		observeModel[4].addShapeBox(0F, 0F, 0F, 2, 7, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // IROBSERVE05
		observeModel[4].setRotationPoint(-12F, -41F, -3F);
		observeModel[4].rotateAngleX = 1.57079633F;

		observeModel[5].addTrapezoid(0F, 0F, 0F, 3, 3, 1, 0F, -0.50F, ModelRendererTurbo.MR_BACK); // IROBSERVE06
		observeModel[5].setRotationPoint(-12.5F, -43.5F, -4F);

		observeModel[6].addTrapezoid(0F, 0F, 0F, 3, 3, 2, 0F, 0F, ModelRendererTurbo.MR_TOP); // IROBSERVE07
		observeModel[6].setRotationPoint(-12.5F, -43.5F, -6F);

		observeModel[7].addTrapezoid(0F, 0F, 0F, 6, 6, 2, 0F, -0.50F, ModelRendererTurbo.MR_BACK); // IROBSERVE08
		observeModel[7].setRotationPoint(-14F, -39.5F, -7F);

		observeModel[8].addBox(0F, 0F, 0F, 6, 1, 1, 0F); // IROBSERVE09
		observeModel[8].setRotationPoint(-14F, -39.5F, -8F);

		observeModel[9].addBox(0F, 0F, 0F, 6, 1, 1, 0F); // IROBSERVE09
		observeModel[9].setRotationPoint(-14F, -34.5F, -8F);

		observeModel[10].addBox(0F, 0F, 0F, 1, 4, 1, 0F); // IROBSERVE09
		observeModel[10].setRotationPoint(-14F, -38.5F, -8F);

		observeModel[11].addBox(0F, 0F, 0F, 1, 4, 1, 0F); // IROBSERVE09
		observeModel[11].setRotationPoint(-9F, -38.5F, -8F);

		observeModel[12].addTrapezoid(0F, 0F, 0F, 2, 2, 1, 0F, 0F, ModelRendererTurbo.MR_TOP); // IROBSERVE07
		observeModel[12].setRotationPoint(-12F, -43F, -6.5F);


		internalsModel = new ModelRendererTurbo[30];
		internalsModel[0] = new ModelRendererTurbo(this, 44, 41, textureX, textureY); // INSIDES02
		internalsModel[1] = new ModelRendererTurbo(this, 0, 73, textureX, textureY); // INSIDES03
		internalsModel[2] = new ModelRendererTurbo(this, 29, 0, textureX, textureY); // INSIDES07
		internalsModel[3] = new ModelRendererTurbo(this, 38, 29, textureX, textureY); // INSIDES08
		internalsModel[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // INSIDES09
		internalsModel[5] = new ModelRendererTurbo(this, 0, 8, textureX, textureY); // INSIDES10
		internalsModel[6] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // INSIDES11
		internalsModel[7] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // INSIDES12
		internalsModel[8] = new ModelRendererTurbo(this, 0, 16, textureX, textureY); // INSIDES13
		internalsModel[9] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // INSIDES14
		internalsModel[10] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // INSIDES15
		internalsModel[11] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // INSIDES16
		internalsModel[12] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // INSIDES17
		internalsModel[13] = new ModelRendererTurbo(this, 0, 5, textureX, textureY); // INSIDES18
		internalsModel[14] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // INSIDES19
		internalsModel[15] = new ModelRendererTurbo(this, 117, 6, textureX, textureY); // INSIDES20
		internalsModel[16] = new ModelRendererTurbo(this, 117, 6, textureX, textureY); // INSIDES21
		internalsModel[17] = new ModelRendererTurbo(this, 117, 6, textureX, textureY); // INSIDES22
		internalsModel[18] = new ModelRendererTurbo(this, 117, 6, textureX, textureY); // INSIDES23
		internalsModel[19] = new ModelRendererTurbo(this, 117, 0, textureX, textureY); // INSIDES27
		internalsModel[20] = new ModelRendererTurbo(this, 117, 0, textureX, textureY); // INSIDES28
		internalsModel[21] = new ModelRendererTurbo(this, 117, 0, textureX, textureY); // INSIDES29
		internalsModel[22] = new ModelRendererTurbo(this, 30, 17, textureX, textureY); // INSIDES31
		internalsModel[23] = new ModelRendererTurbo(this, 0, 12, textureX, textureY); // INSIDES32
		internalsModel[24] = new ModelRendererTurbo(this, 0, 3, textureX, textureY); // INSIDES33
		internalsModel[25] = new ModelRendererTurbo(this, 0, 8, textureX, textureY); // INSIDES34
		internalsModel[26] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 157
		internalsModel[27] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 158
		internalsModel[28] = new ModelRendererTurbo(this, 0, 5, textureX, textureY); // Box 159
		internalsModel[29] = new ModelRendererTurbo(this, 30, 17, textureX, textureY); // INSIDES31

		internalsModel[0].addShapeBox(0F, 0F, 0F, 7, 17, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES02
		internalsModel[0].setRotationPoint(6.5F, -28F, 8F);
		internalsModel[0].rotateAngleY = 1.57079633F;

		internalsModel[1].addShapeBox(0F, 0F, 0F, 2, 25, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES03
		internalsModel[1].setRotationPoint(13F, -28F, 0F);

		internalsModel[2].addShapeBox(0F, 0F, 0F, 14, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES07
		internalsModel[2].setRotationPoint(-10F, -23F, 13F);

		internalsModel[3].addShapeBox(0F, 0F, 0F, 16, 10, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES08
		internalsModel[3].setRotationPoint(-11F, -24F, 14F);

		internalsModel[4].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES09
		internalsModel[4].setRotationPoint(-7F, -26F, 13.7F);

		internalsModel[5].addShapeBox(0F, 0F, 0F, 1, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES10
		internalsModel[5].setRotationPoint(-5F, -27F, 13.7F);

		internalsModel[6].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES11
		internalsModel[6].setRotationPoint(-5F, -15F, 13.7F);

		internalsModel[7].addShapeBox(0F, 0F, 0F, 1, 11, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES12
		internalsModel[7].setRotationPoint(-15F, -27F, 13.7F);
		internalsModel[7].rotateAngleZ = 1.57079633F;

		internalsModel[8].addShapeBox(0F, 0F, 0F, 1, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES13
		internalsModel[8].setRotationPoint(-15F, -25F, 13.7F);
		internalsModel[8].rotateAngleZ = 1.57079633F;

		internalsModel[9].addShapeBox(0F, 0F, 0F, 1, 17, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES14
		internalsModel[9].setRotationPoint(-15F, -25F, -3F);
		internalsModel[9].rotateAngleX = 1.57079633F;

		internalsModel[10].addShapeBox(0F, 0F, 0F, 1, 19, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES15
		internalsModel[10].setRotationPoint(-15F, -27F, -5F);
		internalsModel[10].rotateAngleX = 1.57079633F;

		internalsModel[11].addShapeBox(0F, 0F, 0F, 1, 22, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES16
		internalsModel[11].setRotationPoint(-15F, -25F, -3F);

		internalsModel[12].addShapeBox(0F, 0F, 0F, 1, 24, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES17
		internalsModel[12].setRotationPoint(-15F, -27F, -5F);

		internalsModel[13].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES18
		internalsModel[13].setRotationPoint(-14F, -3F, -5F);
		internalsModel[13].rotateAngleZ = 1.57079633F;

		internalsModel[14].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES19
		internalsModel[14].setRotationPoint(-14F, -3F, -3F);
		internalsModel[14].rotateAngleZ = 1.57079633F;

		internalsModel[15].addShapeBox(0F, 0F, 0F, 2, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES20
		internalsModel[15].setRotationPoint(-11F, -24F, 11F);

		internalsModel[16].addShapeBox(0F, 0F, 0F, 2, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES21
		internalsModel[16].setRotationPoint(-11F, -16F, 11F);

		internalsModel[17].addShapeBox(0F, 0F, 0F, 2, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES22
		internalsModel[17].setRotationPoint(3F, -16F, 11F);

		internalsModel[18].addShapeBox(0F, 0F, 0F, 2, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES23
		internalsModel[18].setRotationPoint(3F, -24F, 11F);

		internalsModel[19].addShapeBox(0F, 0F, 0F, 3, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES27
		internalsModel[19].setRotationPoint(-1.5F, -21.5F, 12F);

		internalsModel[20].addShapeBox(0F, 0F, 0F, 3, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES28
		internalsModel[20].setRotationPoint(-5F, -21.5F, 12F);

		internalsModel[21].addShapeBox(0F, 0F, 0F, 3, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES29
		internalsModel[21].setRotationPoint(-8.5F, -21.5F, 12F);

		internalsModel[22].addShapeBox(0F, 0F, 0F, 1, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES31
		internalsModel[22].setRotationPoint(2F, -18F, 11F);

		internalsModel[23].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES32
		internalsModel[23].setRotationPoint(-15F, -20F, 13.7F);
		internalsModel[23].rotateAngleZ = 1.57079633F;

		internalsModel[24].addShapeBox(0F, 0F, 0F, 1, 11, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES33
		internalsModel[24].setRotationPoint(-15F, -20F, 2.7F);
		internalsModel[24].rotateAngleX = 1.57079633F;

		internalsModel[25].addShapeBox(0F, 0F, 0F, 1, 7, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES34
		internalsModel[25].setRotationPoint(-15F, -28F, 2.7F);

		internalsModel[26].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 157
		internalsModel[26].setRotationPoint(-10F, -3F, -6F);
		internalsModel[26].rotateAngleX = 1.57079633F;

		internalsModel[27].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 158
		internalsModel[27].setRotationPoint(-12F, -3F, -8F);
		internalsModel[27].rotateAngleX = 1.57079633F;

		internalsModel[28].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 159
		internalsModel[28].setRotationPoint(-11F, -3F, -8F);
		internalsModel[28].rotateAngleZ = 1.57079633F;

		internalsModel[29].addShapeBox(0F, 0F, 0F, 1, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // INSIDES31
		internalsModel[29].setRotationPoint(2F, -20F, 11F);


		hatchModel = new ModelRendererTurbo[1];
		hatchModel[0] = new ModelRendererTurbo(this, 74, 28, textureX, textureY); // YEET01

		hatchModel[0].addShapeBox(0F, 0F, -1F, 8, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // YEET01
		hatchModel[0].setRotationPoint(-15F, -9F, 16F);

		parts.put("base",  baseModel);
		parts.put("hatch", hatchModel);
		parts.put("internals", internalsModel);
		parts.put("observe", observeModel);
		parts.put("gun", gunModel);
		parts.put("barrels", barrelsModel);

		if(doOffsets)
		{
			translate(gunModel,0f, 19.50F, 7.5F);
			translate(barrelsModel,0f, 19F, 6F);
			translate(observeModel,0,31.5f,-2.5f);
		}

		flipAll();
	}
}
