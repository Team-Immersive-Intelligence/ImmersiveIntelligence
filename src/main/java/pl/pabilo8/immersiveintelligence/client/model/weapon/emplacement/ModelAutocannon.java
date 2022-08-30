package pl.pabilo8.immersiveintelligence.client.model.weapon.emplacement;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

import java.util.Arrays;

public class ModelAutocannon extends ModelIIBase
{
	int textureX = 128;
	int textureY = 128;

	public ModelRendererTurbo[] turretModel, barrel1Model, barrel2Model, barrel3Model, barrel4Model, magazineLeftBottomModel, magazineLeftTopModel, magazineRightTopModel, magazineRightBottomModel, ammoBoxLidModel, turretTopFlapsModel, gunModel, magazineLeftModel, magazineRightModel;

	public ModelAutocannon(boolean doOffsets) //Same as Filename
	{
		baseModel = new ModelRendererTurbo[1];
		baseModel[0] = new ModelRendererTurbo(this, 2, 73, textureX, textureY); // Box 0

		baseModel[0].addBox(0F, 0F, 0F, 8, 1, 8, 0F); // Box 0
		baseModel[0].setRotationPoint(-4F, -1F, -4F);


		turretModel = new ModelRendererTurbo[28];
		turretModel[0] = new ModelRendererTurbo(this, 80, 53, textureX, textureY); // Box 0
		turretModel[1] = new ModelRendererTurbo(this, 60, 56, textureX, textureY); // Box 0
		turretModel[2] = new ModelRendererTurbo(this, 60, 56, textureX, textureY); // Box 0
		turretModel[3] = new ModelRendererTurbo(this, 40, 69, textureX, textureY); // Box 0
		turretModel[4] = new ModelRendererTurbo(this, 40, 69, textureX, textureY); // Box 0
		turretModel[5] = new ModelRendererTurbo(this, 12, 89, textureX, textureY); // Box 0
		turretModel[6] = new ModelRendererTurbo(this, 12, 89, textureX, textureY); // Box 0
		turretModel[7] = new ModelRendererTurbo(this, 0, 88, textureX, textureY); // Box 0
		turretModel[8] = new ModelRendererTurbo(this, 38, 84, textureX, textureY); // Box 0
		turretModel[9] = new ModelRendererTurbo(this, 0, 88, textureX, textureY); // Box 0
		turretModel[10] = new ModelRendererTurbo(this, 38, 84, textureX, textureY); // Box 0
		turretModel[11] = new ModelRendererTurbo(this, 64, 85, textureX, textureY); // Box 0
		turretModel[12] = new ModelRendererTurbo(this, 72, 79, textureX, textureY); // Box 0
		turretModel[13] = new ModelRendererTurbo(this, 64, 85, textureX, textureY); // Box 0
		turretModel[14] = new ModelRendererTurbo(this, 68, 117, textureX, textureY); // Box 0
		turretModel[15] = new ModelRendererTurbo(this, 88, 100, textureX, textureY); // Box 0
		turretModel[16] = new ModelRendererTurbo(this, 36, 118, textureX, textureY); // Box 0
		turretModel[17] = new ModelRendererTurbo(this, 104, 81, textureX, textureY); // Box 0
		turretModel[18] = new ModelRendererTurbo(this, 77, 102, textureX, textureY); // Box 0
		turretModel[19] = new ModelRendererTurbo(this, 78, 97, textureX, textureY); // Box 0
		turretModel[20] = new ModelRendererTurbo(this, 70, 108, textureX, textureY); // Box 0
		turretModel[21] = new ModelRendererTurbo(this, 66, 114, textureX, textureY); // Box 0
		turretModel[22] = new ModelRendererTurbo(this, 66, 120, textureX, textureY); // Box 0
		turretModel[23] = new ModelRendererTurbo(this, 78, 97, textureX, textureY); // Box 0
		turretModel[24] = new ModelRendererTurbo(this, 48, 84, textureX, textureY); // Box 0
		turretModel[25] = new ModelRendererTurbo(this, 48, 84, textureX, textureY); // Box 0
		turretModel[26] = new ModelRendererTurbo(this, 42, 57, textureX, textureY); // Box 0
		turretModel[27] = new ModelRendererTurbo(this, 42, 57, textureX, textureY); // Box 0

		turretModel[0].addBox(-6F, 0F, -6F, 12, 4, 12, 0F); // Box 0
		turretModel[0].setRotationPoint(0F, -5F, 0F);

		turretModel[1].addBox(6F, 0F, -8F, 8, 1, 8, 0F); // Box 0
		turretModel[1].setRotationPoint(0F, -5F, 0F);

		turretModel[2].addBox(-14F, 0F, -8F, 8, 1, 8, 0F); // Box 0
		turretModel[2].setRotationPoint(0F, -5F, 0F);

		turretModel[3].addBox(-14F, 0F, -8F, 8, 4, 1, 0F); // Box 0
		turretModel[3].setRotationPoint(0F, -9F, 0F);

		turretModel[4].addBox(6F, 0F, -8F, 8, 4, 1, 0F); // Box 0
		turretModel[4].setRotationPoint(0F, -9F, 0F);

		turretModel[5].addBox(4F, 2.5F, -9.25F, 12, 6, 1, 0F); // Box 0
		turretModel[5].setRotationPoint(0F, -10F, 0F);
		turretModel[5].rotateAngleX = 0.15707963F;

		turretModel[6].addBox(-16F, 2.5F, -9.25F, 12, 6, 1, 0F); // Box 0
		turretModel[6].setRotationPoint(0F, -10F, 0F);
		turretModel[6].rotateAngleX = 0.15707963F;

		turretModel[7].addBox(4F, 0F, -8.75F, 4, 21, 1, 0F); // Box 0
		turretModel[7].setRotationPoint(0F, -27F, 0F);

		turretModel[8].addBox(-8F, 0F, -8.75F, 4, 21, 1, 0F); // Box 0
		turretModel[8].setRotationPoint(0F, -27F, 0F);

		turretModel[9].addBox(-16F, 0F, -8.75F, 4, 21, 1, 0F); // Box 0
		turretModel[9].setRotationPoint(0F, -27F, 0F);

		turretModel[10].addBox(12F, 0F, -8.75F, 4, 21, 1, 0F); // Box 0
		turretModel[10].setRotationPoint(0F, -27F, 0F);

		turretModel[11].addBox(-4F, 0F, -3F, 1, 12, 6, 0F); // Box 0
		turretModel[11].setRotationPoint(0F, -21F, 0F);

		turretModel[12].addBox(-4F, 0F, -4F, 8, 4, 8, 0F); // Box 0
		turretModel[12].setRotationPoint(0F, -9F, 0F);

		turretModel[13].addBox(3F, 0F, -3F, 1, 12, 6, 0F); // Box 0
		turretModel[13].setRotationPoint(0F, -21F, 0F);

		turretModel[14].addBox(-14F, 0F, 6F, 20, 1, 10, 0F); // Box 0
		turretModel[14].setRotationPoint(0F, -5F, 0F);

		turretModel[15].addBox(-6F, 1F, 10F, 12, 9, 8, 0F); // Box 0
		turretModel[15].setRotationPoint(0F, -15F, 0F);

		turretModel[16].addBox(-14F, 0F, 10F, 8, 2, 8, 0F); // Box 0
		turretModel[16].setRotationPoint(0F, -7F, 0F);

		turretModel[17].addBox(-13F, 0F, 11F, 6, 4, 6, 0F); // Box 0
		turretModel[17].setRotationPoint(0F, -11F, 0F);

		turretModel[18].addShapeBox(-3F, 0F, 6F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // Box 0
		turretModel[18].setRotationPoint(0F, -8F, 0F);

		turretModel[19].addBox(-3F, 0F, 4F, 3, 3, 2, 0F); // Box 0
		turretModel[19].setRotationPoint(0F, -8F, 0F);

		turretModel[20].addBox(-9F, 0F, 6F, 6, 3, 3, 0F); // Box 0
		turretModel[20].setRotationPoint(0F, -8F, 0F);

		turretModel[21].addShapeBox(-12F, 0F, 6F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // Box 0
		turretModel[21].setRotationPoint(0F, -8F, 0F);

		turretModel[22].addShapeBox(-12F, -3F, 6F, 3, 3, 3, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		turretModel[22].setRotationPoint(0F, -8F, 0F);

		turretModel[23].addBox(-12F, -3F, 9F, 3, 3, 2, 0F); // Box 0
		turretModel[23].setRotationPoint(0F, -8F, 0F);

		turretModel[24].addBox(5F, 0.25F, 11F, 2, 2, 2, 0F); // Box 0
		turretModel[24].setRotationPoint(0F, -15F, 0F);

		turretModel[25].addBox(5F, 0.25F, 15F, 2, 2, 2, 0F); // Box 0
		turretModel[25].setRotationPoint(0F, -15F, 0F);

		turretModel[26].addBox(4F, -3F, -8F, 1, 2, 1, 0F); // Box 0
		turretModel[26].setRotationPoint(0F, -24F, 0F);
		turretModel[26].rotateAngleX = -0.12217305F;

		turretModel[27].addBox(-5F, -3F, -8F, 1, 2, 1, 0F); // Box 0
		turretModel[27].setRotationPoint(0F, -24F, 0F);
		turretModel[27].rotateAngleX = -0.12217305F;


		barrel1Model = new ModelRendererTurbo[3];
		barrel1Model[0] = new ModelRendererTurbo(this, 30, 85, textureX, textureY); // Box 0
		barrel1Model[1] = new ModelRendererTurbo(this, 18, 96, textureX, textureY); // Box 0
		barrel1Model[2] = new ModelRendererTurbo(this, 18, 66, textureX, textureY); // Box 0

		barrel1Model[0].addShapeBox(8F, -4.5F, -30F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F); // Box 0
		barrel1Model[0].setRotationPoint(0F, -18F, 0F);

		barrel1Model[1].addBox(8F, -4.5F, -32F, 3, 3, 2, 0F); // Box 0
		barrel1Model[1].setRotationPoint(0F, -18F, 0F);

		barrel1Model[2].addBox(8.5F, -4F, -29F, 2, 2, 16, 0F); // Box 0
		barrel1Model[2].setRotationPoint(0F, -18F, 0F);


		barrel2Model = new ModelRendererTurbo[3];
		barrel2Model[0] = new ModelRendererTurbo(this, 30, 85, textureX, textureY); // Box 0
		barrel2Model[1] = new ModelRendererTurbo(this, 18, 96, textureX, textureY); // Box 0
		barrel2Model[2] = new ModelRendererTurbo(this, 18, 66, textureX, textureY); // Box 0

		barrel2Model[0].addShapeBox(8F, 1.5F, -30F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F); // Box 0
		barrel2Model[0].setRotationPoint(0F, -18F, 0F);

		barrel2Model[1].addBox(8F, 1.5F, -32F, 3, 3, 2, 0F); // Box 0
		barrel2Model[1].setRotationPoint(0F, -18F, 0F);

		barrel2Model[2].addBox(8.5F, 2F, -29F, 2, 2, 16, 0F); // Box 0
		barrel2Model[2].setRotationPoint(0F, -18F, 0F);


		barrel3Model = new ModelRendererTurbo[3];
		barrel3Model[0] = new ModelRendererTurbo(this, 30, 85, textureX, textureY); // Box 0
		barrel3Model[1] = new ModelRendererTurbo(this, 18, 96, textureX, textureY); // Box 0
		barrel3Model[2] = new ModelRendererTurbo(this, 18, 66, textureX, textureY); // Box 0

		barrel3Model[0].addShapeBox(-11F, 1.5F, -30F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F); // Box 0
		barrel3Model[0].setRotationPoint(0F, -18F, 0F);

		barrel3Model[1].addBox(-11F, 1.5F, -32F, 3, 3, 2, 0F); // Box 0
		barrel3Model[1].setRotationPoint(0F, -18F, 0F);

		barrel3Model[2].addBox(-10.5F, 2F, -29F, 2, 2, 16, 0F); // Box 0
		barrel3Model[2].setRotationPoint(0F, -18F, 0F);


		barrel4Model = new ModelRendererTurbo[3];
		barrel4Model[0] = new ModelRendererTurbo(this, 30, 85, textureX, textureY); // Box 0
		barrel4Model[1] = new ModelRendererTurbo(this, 18, 96, textureX, textureY); // Box 0
		barrel4Model[2] = new ModelRendererTurbo(this, 18, 66, textureX, textureY); // Box 0

		barrel4Model[0].addShapeBox(-11F, -4.5F, -30F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F); // Box 0
		barrel4Model[0].setRotationPoint(0F, -18F, 0F);

		barrel4Model[1].addBox(-11F, -4.5F, -32F, 3, 3, 2, 0F); // Box 0
		barrel4Model[1].setRotationPoint(0F, -18F, 0F);

		barrel4Model[2].addBox(-10.5F, -4F, -29F, 2, 2, 16, 0F); // Box 0
		barrel4Model[2].setRotationPoint(0F, -18F, 0F);

		for(ModelRendererTurbo mod : barrel1Model)
			mod.hasOffset = true;
		for(ModelRendererTurbo mod : barrel2Model)
			mod.hasOffset = true;
		for(ModelRendererTurbo mod : barrel3Model)
			mod.hasOffset = true;
		for(ModelRendererTurbo mod : barrel4Model)
			mod.hasOffset = true;

		magazineLeftBottomModel = new ModelRendererTurbo[1];
		magazineLeftBottomModel[0] = new ModelRendererTurbo(this, 72, 69, textureX, textureY); // Shape 62

		magazineLeftBottomModel[0].addShape3D(20F, -3F, -3.5F, new Shape2D(new Coord2D[]{new Coord2D(0, 2, 0, 2), new Coord2D(4, 0, 4, 0), new Coord2D(7, 0, 7, 0), new Coord2D(7, 6, 7, 6), new Coord2D(3, 8, 3, 8), new Coord2D(0, 8, 0, 8)}), 2, 7, 8, 28, 2, ModelRendererTurbo.MR_FRONT, new float[]{6, 3, 5, 6, 3, 5}); // Shape 62
		magazineLeftBottomModel[0].setRotationPoint(0F, -18F, 0F);
		magazineLeftBottomModel[0].rotateAngleX = -1.57079633F;


		magazineLeftTopModel = new ModelRendererTurbo[1];
		magazineLeftTopModel[0] = new ModelRendererTurbo(this, 72, 69, textureX, textureY); // Shape 62

		magazineLeftTopModel[0].addShape3D(20F, -3F, 2.5F, new Shape2D(new Coord2D[]{new Coord2D(0, 2, 0, 2), new Coord2D(4, 0, 4, 0), new Coord2D(7, 0, 7, 0), new Coord2D(7, 6, 7, 6), new Coord2D(3, 8, 3, 8), new Coord2D(0, 8, 0, 8)}), 2, 7, 8, 28, 2, ModelRendererTurbo.MR_FRONT, new float[]{6, 3, 5, 6, 3, 5}); // Shape 62
		magazineLeftTopModel[0].setRotationPoint(0F, -18F, 0F);
		magazineLeftTopModel[0].rotateAngleX = -1.57079633F;


		magazineRightTopModel = new ModelRendererTurbo[1];
		magazineRightTopModel[0] = new ModelRendererTurbo(this, 100, 71, textureX, textureY); // Shape 62

		magazineRightTopModel[0].addShape3D(-13F, -3F, 2.5F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(2, 0, 2, 0), new Coord2D(7, 2, 7, 2), new Coord2D(7, 8, 7, 8), new Coord2D(4, 8, 4, 8), new Coord2D(0, 6, 0, 6)}), 2, 7, 8, 28, 2, ModelRendererTurbo.MR_FRONT, new float[]{6, 5, 3, 6, 6, 2}); // Shape 62
		magazineRightTopModel[0].setRotationPoint(0F, -18F, 0F);
		magazineRightTopModel[0].rotateAngleX = -1.57079633F;


		magazineRightBottomModel = new ModelRendererTurbo[1];
		magazineRightBottomModel[0] = new ModelRendererTurbo(this, 100, 71, textureX, textureY); // Shape 62

		magazineRightBottomModel[0].addShape3D(-13F, -3F, -3.5F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(2, 0, 2, 0), new Coord2D(7, 2, 7, 2), new Coord2D(7, 8, 7, 8), new Coord2D(4, 8, 4, 8), new Coord2D(0, 6, 0, 6)}), 2, 7, 8, 28, 2, ModelRendererTurbo.MR_FRONT, new float[]{6, 5, 3, 6, 6, 2}); // Shape 62
		magazineRightBottomModel[0].setRotationPoint(0F, -18F, 0F);
		magazineRightBottomModel[0].rotateAngleX = -1.57079633F;


		ammoBoxLidModel = new ModelRendererTurbo[2];
		ammoBoxLidModel[0] = new ModelRendererTurbo(this, 88, 91, textureX, textureY); // Box 0
		ammoBoxLidModel[1] = new ModelRendererTurbo(this, 86, 93, textureX, textureY); // Box 0

		ammoBoxLidModel[0].addBox(-12F, -0.5F, 0F, 12, 1, 8, 0F); // Box 0
		ammoBoxLidModel[0].setRotationPoint(6F, -14.5F, 10F);

		ammoBoxLidModel[1].addBox(-13F, 0F, 2F, 1, 2, 4, 0F); // Box 0
		ammoBoxLidModel[1].setRotationPoint(6F, -14.5F, 10F);


		turretTopFlapsModel = new ModelRendererTurbo[2];
		turretTopFlapsModel[0] = new ModelRendererTurbo(this, 42, 57, textureX, textureY); // Box 0
		turretTopFlapsModel[1] = new ModelRendererTurbo(this, 42, 57, textureX, textureY); // Box 0

		turretTopFlapsModel[0].addBox(0, -8F, -1f, 12, 6, 1, 0F); // Box 0
		turretTopFlapsModel[0].setRotationPoint(4F, -24F, -8F);
		turretTopFlapsModel[0].rotateAngleX = -0.12217305F;

		turretTopFlapsModel[1].addBox(-12f, -8F, -1f, 12, 6, 1, 0F); // Box 0
		turretTopFlapsModel[1].setRotationPoint(-4F, -24F, -8F);
		turretTopFlapsModel[1].rotateAngleX = -0.12217305F;


		gunModel = new ModelRendererTurbo[30];
		gunModel[0] = new ModelRendererTurbo(this, 48, 95, textureX, textureY); // Box 0
		gunModel[1] = new ModelRendererTurbo(this, 24, 110, textureX, textureY); // Box 0
		gunModel[2] = new ModelRendererTurbo(this, 24, 110, textureX, textureY); // Box 0
		gunModel[3] = new ModelRendererTurbo(this, 38, 110, textureX, textureY); // Box 0
		gunModel[4] = new ModelRendererTurbo(this, 38, 110, textureX, textureY); // Box 0
		gunModel[5] = new ModelRendererTurbo(this, 18, 96, textureX, textureY); // Box 0
		gunModel[6] = new ModelRendererTurbo(this, 50, 66, textureX, textureY); // Box 0
		gunModel[7] = new ModelRendererTurbo(this, 10, 96, textureX, textureY); // Box 0
		gunModel[8] = new ModelRendererTurbo(this, 18, 96, textureX, textureY); // Box 0
		gunModel[9] = new ModelRendererTurbo(this, 50, 66, textureX, textureY); // Box 0
		gunModel[10] = new ModelRendererTurbo(this, 10, 96, textureX, textureY); // Box 0
		gunModel[11] = new ModelRendererTurbo(this, 18, 96, textureX, textureY); // Box 0
		gunModel[12] = new ModelRendererTurbo(this, 50, 66, textureX, textureY); // Box 0
		gunModel[13] = new ModelRendererTurbo(this, 10, 96, textureX, textureY); // Box 0
		gunModel[14] = new ModelRendererTurbo(this, 18, 96, textureX, textureY); // Box 0
		gunModel[15] = new ModelRendererTurbo(this, 50, 66, textureX, textureY); // Box 0
		gunModel[16] = new ModelRendererTurbo(this, 10, 96, textureX, textureY); // Box 0
		gunModel[17] = new ModelRendererTurbo(this, 6, 100, textureX, textureY); // Box 0
		gunModel[18] = new ModelRendererTurbo(this, 6, 100, textureX, textureY); // Box 0
		gunModel[19] = new ModelRendererTurbo(this, 6, 100, textureX, textureY); // Box 0
		gunModel[20] = new ModelRendererTurbo(this, 6, 100, textureX, textureY); // Box 0
		gunModel[21] = new ModelRendererTurbo(this, 38, 77, textureX, textureY); // Box 0
		gunModel[22] = new ModelRendererTurbo(this, 38, 77, textureX, textureY); // Box 0
		gunModel[23] = new ModelRendererTurbo(this, 48, 95, textureX, textureY); // Box 0
		gunModel[24] = new ModelRendererTurbo(this, 8, 113, textureX, textureY); // Box 0
		gunModel[25] = new ModelRendererTurbo(this, 48, 78, textureX, textureY); // Box 0
		gunModel[26] = new ModelRendererTurbo(this, 56, 78, textureX, textureY); // Box 0
		gunModel[27] = new ModelRendererTurbo(this, 48, 78, textureX, textureY); // Box 0
		gunModel[28] = new ModelRendererTurbo(this, 56, 78, textureX, textureY); // Box 0
		gunModel[29] = new ModelRendererTurbo(this, 64, 45, textureX, textureY); // Box 0

		gunModel[0].addBox(-2F, -4F, -4F, 4, 8, 8, 0F); // Box 0
		gunModel[0].setRotationPoint(0F, -18F, 0F);

		gunModel[1].addBox(5F, -4F, -4F, 2, 8, 8, 0F); // Box 0
		gunModel[1].setRotationPoint(0F, -18F, 0F);

		gunModel[2].addBox(-7F, -4F, -4F, 2, 8, 8, 0F); // Box 0
		gunModel[2].setRotationPoint(0F, -18F, 0F);

		gunModel[3].addBox(2F, -2F, -2F, 3, 4, 4, 0F); // Box 0
		gunModel[3].setRotationPoint(0F, -18F, 0F);

		gunModel[4].addBox(-5F, -2F, -2F, 3, 4, 4, 0F); // Box 0
		gunModel[4].setRotationPoint(0F, -18F, 0F);

		gunModel[5].addBox(7F, -5F, -4F, 5, 4, 10, 0F); // Box 0
		gunModel[5].setRotationPoint(0F, -18F, 0F);

		gunModel[6].addBox(8F, -4.5F, -12F, 3, 3, 8, 0F); // Box 0
		gunModel[6].setRotationPoint(0F, -18F, 0F);

		gunModel[7].addShapeBox(8F, -4.5F, -13F, 3, 3, 1, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		gunModel[7].setRotationPoint(0F, -18F, 0F);

		gunModel[8].addBox(7F, 1F, -4F, 5, 4, 10, 0F); // Box 0
		gunModel[8].setRotationPoint(0F, -18F, 0F);

		gunModel[9].addBox(8F, 1.5F, -12F, 3, 3, 8, 0F); // Box 0
		gunModel[9].setRotationPoint(0F, -18F, 0F);

		gunModel[10].addShapeBox(8F, 1.5F, -13F, 3, 3, 1, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		gunModel[10].setRotationPoint(0F, -18F, 0F);

		gunModel[11].addBox(-12F, -5F, -4F, 5, 4, 10, 0F); // Box 0
		gunModel[11].setRotationPoint(0F, -18F, 0F);

		gunModel[12].addBox(-11F, -4.5F, -12F, 3, 3, 8, 0F); // Box 0
		gunModel[12].setRotationPoint(0F, -18F, 0F);

		gunModel[13].addShapeBox(-11F, -4.5F, -13F, 3, 3, 1, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		gunModel[13].setRotationPoint(0F, -18F, 0F);

		gunModel[14].addBox(-12F, 1F, -4F, 5, 4, 10, 0F); // Box 0
		gunModel[14].setRotationPoint(0F, -18F, 0F);

		gunModel[15].addBox(-11F, 1.5F, -12F, 3, 3, 8, 0F); // Box 0
		gunModel[15].setRotationPoint(0F, -18F, 0F);

		gunModel[16].addShapeBox(-11F, 1.5F, -13F, 3, 3, 1, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		gunModel[16].setRotationPoint(0F, -18F, 0F);

		gunModel[17].addBox(-13F, -5F, -4F, 1, 3, 10, 0F); // Box 0
		gunModel[17].setRotationPoint(0F, -18F, 0F);

		gunModel[18].addBox(-13F, 1F, -4F, 1, 3, 10, 0F); // Box 0
		gunModel[18].setRotationPoint(0F, -18F, 0F);

		gunModel[19].addBox(12F, -5F, -4F, 1, 3, 10, 0F); // Box 0
		gunModel[19].setRotationPoint(0F, -18F, 0F);

		gunModel[20].addBox(12F, 1F, -4F, 1, 3, 10, 0F); // Box 0
		gunModel[20].setRotationPoint(0F, -18F, 0F);

		gunModel[21].addBox(-2F, -9F, -4F, 4, 4, 1, 0F); // Box 0
		gunModel[21].setRotationPoint(0F, -18F, 0F);

		gunModel[22].addBox(-2F, -9F, -6F, 4, 4, 1, 0F); // Box 0
		gunModel[22].setRotationPoint(0F, -18F, 0F);

		gunModel[23].addBox(-1.5F, -8.5F, -5F, 3, 3, 1, 0F); // Box 0
		gunModel[23].setRotationPoint(0F, -18F, 0F);

		gunModel[24].addBox(-2F, -9F, 1F, 4, 4, 4, 0F); // Box 0
		gunModel[24].setRotationPoint(0F, -18F, 0F);

		gunModel[25].addShapeBox(-1.5F, -8.5F, -2F, 3, 3, 1, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		gunModel[25].setRotationPoint(0F, -18F, 0F);

		gunModel[26].addShapeBox(-1.5F, -8.5F, -3F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F); // Box 0
		gunModel[26].setRotationPoint(0F, -18F, 0F);

		gunModel[27].addShapeBox(-1.5F, -8.5F, 0F, 3, 3, 1, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		gunModel[27].setRotationPoint(0F, -18F, 0F);

		gunModel[28].addShapeBox(-1.5F, -8.5F, -1F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F); // Box 0
		gunModel[28].setRotationPoint(0F, -18F, 0F);

		gunModel[29].addBox(-2F, -5F, -4F, 4, 1, 10, 0F); // Box 0
		gunModel[29].setRotationPoint(0F, -18F, 0F);

		magazineRightModel = new ModelRendererTurbo[1];
		magazineRightModel[0] = new ModelRendererTurbo(this, 72, 69, textureX, textureY); // Shape 62
		magazineRightModel[0].addShape3D(4F, 0.5F, -1.5F, new Shape2D(new Coord2D[]{new Coord2D(0, 2, 0, 2), new Coord2D(4, 0, 4, 0), new Coord2D(7, 0, 7, 0), new Coord2D(7, 6, 7, 6), new Coord2D(3, 8, 3, 8), new Coord2D(0, 8, 0, 8)}), 2, 7, 8, 28, 2, ModelRendererTurbo.MR_FRONT, new float[]{6, 3, 5, 6, 3, 5}); // Shape 62

		magazineLeftModel = new ModelRendererTurbo[1];
		magazineLeftModel[0] = new ModelRendererTurbo(this, 100, 71, textureX, textureY); // Shape 62
		magazineLeftModel[0].addShape3D(4F, 0.5F, -1.5F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(2, 0, 2, 0), new Coord2D(7, 2, 7, 2), new Coord2D(7, 8, 7, 8), new Coord2D(4, 8, 4, 8), new Coord2D(0, 6, 0, 6)}), 2, 7, 8, 28, 2, ModelRendererTurbo.MR_FRONT, new float[]{6, 5, 3, 6, 6, 2}); // Shape 62

		parts.put("base", baseModel);
		parts.put("turret", turretModel);
		parts.put("gun", gunModel);
		parts.put("barrel1", barrel1Model);
		parts.put("barrel2", barrel2Model);
		parts.put("barrel3", barrel3Model);
		parts.put("barrel4", barrel4Model);
		parts.put("ammoBoxLid", ammoBoxLidModel);
		parts.put("turretTopFlaps", turretTopFlapsModel);

		if(doOffsets)
		{
			parts.put("magazineLeftBottom", magazineLeftBottomModel);
			parts.put("magazineLeftTop", magazineLeftTopModel);
			parts.put("magazineRightTop", magazineRightTopModel);
			parts.put("magazineRightBottom", magazineRightBottomModel);

			parts.put("magRight", magazineRightModel);
			parts.put("magLeft", magazineLeftModel);

			translate(gunModel, 0, 18f, 0);
			//translate(turretTopFlapsModel, 0, 24f, 0);

			translate(barrel1Model, 0, 18f, 0);
			translate(barrel2Model, 0, 18f, 0);
			translate(barrel3Model, 0, 18f, 0);
			translate(barrel4Model, 0, 18f, 0);

			translate(magazineLeftBottomModel, 0, 18f, 0);
			translate(magazineLeftTopModel, 0, 18f, 0);
			translate(magazineRightBottomModel, 0, 18f, 0);
			translate(magazineRightTopModel, 0, 18f, 0);
		}

		baseModel= Arrays.stream(baseModel).sorted((o1, o2) -> Float.compare(o1.rotationPointY,o2.rotationPointY)).toArray(ModelRendererTurbo[]::new);
		turretModel= Arrays.stream(turretModel).sorted((o1, o2) -> Float.compare(o1.rotationPointY,o2.rotationPointY)).toArray(ModelRendererTurbo[]::new);
		gunModel= Arrays.stream(gunModel).sorted((o1, o2) -> Float.compare(o1.rotationPointY,o2.rotationPointY)).toArray(ModelRendererTurbo[]::new);
		turretTopFlapsModel= Arrays.stream(turretTopFlapsModel).sorted((o1, o2) -> Float.compare(o1.rotationPointY,o2.rotationPointY)).toArray(ModelRendererTurbo[]::new);

		barrel1Model= Arrays.stream(barrel1Model).sorted((o1, o2) -> Float.compare(o1.rotationPointX,o2.rotationPointX)).toArray(ModelRendererTurbo[]::new);
		barrel2Model= Arrays.stream(barrel2Model).sorted((o1, o2) -> Float.compare(o1.rotationPointX,o2.rotationPointX)).toArray(ModelRendererTurbo[]::new);
		barrel3Model= Arrays.stream(barrel3Model).sorted((o1, o2) -> Float.compare(o1.rotationPointX,o2.rotationPointX)).toArray(ModelRendererTurbo[]::new);
		barrel4Model= Arrays.stream(barrel4Model).sorted((o1, o2) -> Float.compare(o1.rotationPointX,o2.rotationPointX)).toArray(ModelRendererTurbo[]::new);

		flipAll();
	}
}
