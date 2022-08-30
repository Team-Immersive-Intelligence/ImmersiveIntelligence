package pl.pabilo8.immersiveintelligence.client.model.vehicle;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * @author Pabilo8
 * @since 17.07.2020
 */
public class ModelFieldHowitzer extends ModelIIBase
{
	int textureX = 128;
	int textureY = 64;

	public ModelRendererTurbo[] gunModel, barrelModel, leftWheelModel, rightWheelModel, wheelAxleModel, triggerModel, ejectionPlateModel, pitchThingyModel;


	public ModelFieldHowitzer() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[19];
		baseModel[0] = new ModelRendererTurbo(this, 50, 52, textureX, textureY); // Box 2
		baseModel[1] = new ModelRendererTurbo(this, 4, 35, textureX, textureY); // Box 2
		baseModel[2] = new ModelRendererTurbo(this, 0, 48, textureX, textureY); // Box 2
		baseModel[3] = new ModelRendererTurbo(this, 0, 48, textureX, textureY); // Box 2
		baseModel[4] = new ModelRendererTurbo(this, 88, 47, textureX, textureY); // Box 2
		baseModel[5] = new ModelRendererTurbo(this, 64, 41, textureX, textureY); // Shape 8
		baseModel[6] = new ModelRendererTurbo(this, 64, 18, textureX, textureY); // Shape 10
		baseModel[7] = new ModelRendererTurbo(this, 56, 16, textureX, textureY); // Box 11
		baseModel[8] = new ModelRendererTurbo(this, 56, 16, textureX, textureY); // Box 11
		baseModel[9] = new ModelRendererTurbo(this, 22, 16, textureX, textureY); // Box 2
		baseModel[10] = new ModelRendererTurbo(this, 104, 1, textureX, textureY); // Box 2
		baseModel[11] = new ModelRendererTurbo(this, 24, 8, textureX, textureY); // Box 2
		baseModel[12] = new ModelRendererTurbo(this, 88, 39, textureX, textureY); // Shape 17
		baseModel[13] = new ModelRendererTurbo(this, 18, 2, textureX, textureY); // Box 2
		baseModel[14] = new ModelRendererTurbo(this, 16, 8, textureX, textureY); // Box 2
		baseModel[15] = new ModelRendererTurbo(this, 16, 8, textureX, textureY); // Box 2
		baseModel[16] = new ModelRendererTurbo(this, 42, 41, textureX, textureY); // Box 11
		baseModel[17] = new ModelRendererTurbo(this, 116, 3, textureX, textureY); // Box 11
		baseModel[18] = new ModelRendererTurbo(this, 20, 52, textureX, textureY); // Box 11

		baseModel[0].addBox(0F, 0F, 0F, 1, 6, 6, 0F); // Box 2
		baseModel[0].setRotationPoint(-2F, -3F, 5F);

		baseModel[1].addBox(0F, 0F, 0F, 1, 6, 6, 0F); // Box 2
		baseModel[1].setRotationPoint(17F, -3F, 5F);

		baseModel[2].addBox(0F, 0F, 0F, 4, 4, 12, 0F); // Box 2
		baseModel[2].setRotationPoint(0F, -2F, 6F);

		baseModel[3].addBox(0F, 0F, 0F, 4, 4, 12, 0F); // Box 2
		baseModel[3].setRotationPoint(12F, -2F, 6F);

		baseModel[4].addBox(0F, 0F, 0F, 16, 6, 4, 0F); // Box 2
		baseModel[4].setRotationPoint(0F, -2F, 2F);

		baseModel[5].addShape3D(0F, 0F, 0.5F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(12, 0, 12, 0), new Coord2D(12, 18, 12, 18), new Coord2D(10, 20, 10, 20), new Coord2D(0, 20, 0, 20)}), 1, 12, 20, 63, 1, ModelRendererTurbo.MR_FRONT, new float[]{20, 10, 3, 18, 12}); // Shape 8
		baseModel[5].setRotationPoint(3F, 0F, 0F);
		baseModel[5].rotateAngleX = -0.13962634F;

		baseModel[6].addShape3D(0F, 0F, 0.5F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(12, 0, 12, 0), new Coord2D(12, 20, 12, 20), new Coord2D(2, 20, 2, 20), new Coord2D(0, 18, 0, 18)}), 1, 12, 20, 63, 1, ModelRendererTurbo.MR_FRONT, new float[]{18, 3, 10, 20, 12}); // Shape 10
		baseModel[6].setRotationPoint(25F, 0F, 0F);
		baseModel[6].rotateAngleX = -0.13962634F;

		baseModel[7].addBox(-2F, -20F, -0.5F, 2, 20, 2, 0F); // Box 11
		baseModel[7].setRotationPoint(3F, 0F, 0F);
		baseModel[7].rotateAngleX = -0.13962634F;

		baseModel[8].addBox(-1.5F, -20F, -2F, 2, 20, 2, 0F); // Box 11
		baseModel[8].setRotationPoint(15F, 0F, 0F);
		baseModel[8].rotateAngleY = -1.57079633F;
		baseModel[8].rotateAngleZ = 0.13962634F;

		baseModel[9].addShapeBox(0F, 0F, 0F, 2, 6, 4, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[9].setRotationPoint(1F, -8F, 2F);

		baseModel[10].addShapeBox(0F, 0F, 0F, 2, 6, 4, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[10].setRotationPoint(13F, -8F, 2F);

		baseModel[11].addBox(0F, 0F, 0F, 6, 4, 4, 0F); // Box 2
		baseModel[11].setRotationPoint(5F, -2F, 22F);

		baseModel[12].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(16, 0, 16, 0), new Coord2D(12, 4, 12, 4), new Coord2D(4, 4, 4, 4)}), 4, 16, 4, 36, 4, ModelRendererTurbo.MR_FRONT, new float[]{6, 8, 6, 16}); // Shape 17
		baseModel[12].setRotationPoint(16F, 2F, 18F);
		baseModel[12].rotateAngleX = -1.57079633F;

		baseModel[13].addBox(0F, 0F, 0F, 6, 4, 2, 0F); // Box 2
		baseModel[13].setRotationPoint(5F, -2F, 28F);

		baseModel[14].addBox(0F, 0F, 0F, 2, 4, 2, 0F); // Box 2
		baseModel[14].setRotationPoint(5F, -2F, 26F);

		baseModel[15].addBox(-2F, 0F, -2F, 2, 4, 2, 0F); // Box 2
		baseModel[15].setRotationPoint(9F, -2F, 26F);
		baseModel[15].rotateAngleY = -3.14159265F;

		baseModel[16].addBox(-1F, -14F, -0.5F, 9, 6, 1, 0F); // Box 11
		baseModel[16].setRotationPoint(-7F, 0F, 0F);
		baseModel[16].rotateAngleX = -0.13962634F;

		baseModel[17].addBox(-2F, -14F, 1.5F, 2, 4, 4, 0F); // Box 11
		baseModel[17].setRotationPoint(15F, 0F, 0F);
		baseModel[17].rotateAngleX = -0.13962634F;

		baseModel[18].addBox(-2F, -14F, 1.5F, 2, 4, 4, 0F); // Box 11
		baseModel[18].setRotationPoint(3F, 0F, 0F);
		baseModel[18].rotateAngleX = -0.13962634F;


		gunModel = new ModelRendererTurbo[4];
		gunModel[0] = new ModelRendererTurbo(this, 28, 19, textureX, textureY); // Box 23
		gunModel[1] = new ModelRendererTurbo(this, 100, 18, textureX, textureY); // Box 23
		gunModel[2] = new ModelRendererTurbo(this, 0, 6, textureX, textureY); // Box 23
		gunModel[3] = new ModelRendererTurbo(this, 6, 6, textureX, textureY); // Box 23

		gunModel[0].addBox(2F, 4F, -15F, 4, 2, 20, 0F); // Box 23

		gunModel[1].addBox(0F, -4F, 4F, 8, 8, 4, 0F); // Box 23

		gunModel[2].addBox(-1F, -1F, -1F, 1, 2, 2, 0F); // Box 23

		gunModel[3].addBox(8F, -1F, -1F, 1, 2, 2, 0F); // Box 23


		barrelModel = new ModelRendererTurbo[5];
		barrelModel[0] = new ModelRendererTurbo(this, 44, 0, textureX, textureY); // Box 23
		barrelModel[1] = new ModelRendererTurbo(this, 32, 52, textureX, textureY); // Box 23
		barrelModel[2] = new ModelRendererTurbo(this, 24, 16, textureX, textureY); // Box 23
		barrelModel[3] = new ModelRendererTurbo(this, 88, 20, textureX, textureY); // Box 23
		barrelModel[4] = new ModelRendererTurbo(this, 0, 24, textureX, textureY); // Box 23

		barrelModel[0].addBox(0F, -4F, -14F, 8, 8, 8, 0F); // Box 23

		barrelModel[1].addBox(0.5F, -3.5F, -16F, 6, 6, 2, 0F); // Box 23

		barrelModel[2].addBox(0F, -4F, -6F, 1, 8, 10, 0F); // Box 23

		barrelModel[3].addBox(7F, -4F, -6F, 1, 8, 10, 0F); // Box 23

		barrelModel[4].addBox(1F, 3F, -6F, 6, 1, 10, 0F); // Box 23


		triggerModel = new ModelRendererTurbo[3];
		triggerModel[0] = new ModelRendererTurbo(this, 0, 10, textureX, textureY); // Box 23
		triggerModel[1] = new ModelRendererTurbo(this, 2, 10, textureX, textureY); // Box 23
		triggerModel[2] = new ModelRendererTurbo(this, 10, 10, textureX, textureY); // Box 23

		triggerModel[0].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // Box 23
		triggerModel[0].setRotationPoint(15F, -11F, 5F);

		triggerModel[1].addBox(1F, -1F, -1F, 1, 2, 6, 0F); // Box 23
		triggerModel[1].setRotationPoint(15F, -11F, 5F);

		triggerModel[2].addBox(2F, -1F, 3F, 1, 2, 2, 0F); // Box 23
		triggerModel[2].setRotationPoint(15F, -11F, 5F);


		leftWheelModel = new ModelRendererTurbo[1];
		leftWheelModel[0] = new ModelRendererTurbo(this, 76, 0, textureX, textureY); // Shape 0

		leftWheelModel[0].addShape3D(7F, -7F, -2F, new Shape2D(new Coord2D[]{new Coord2D(3, 0, 3, 0), new Coord2D(11, 0, 11, 0), new Coord2D(14, 3, 14, 3), new Coord2D(14, 11, 14, 11), new Coord2D(11, 14, 11, 14), new Coord2D(3, 14, 3, 14), new Coord2D(0, 11, 0, 11), new Coord2D(0, 3, 0, 3)}), 4, 14, 14, 52, 4, ModelRendererTurbo.MR_FRONT, new float[]{5, 8, 5, 8, 5, 8, 5, 8}); // Shape 0
		leftWheelModel[0].setRotationPoint(20F, 0F, 0F);
		leftWheelModel[0].rotateAngleY = -1.57079633F;


		rightWheelModel = new ModelRendererTurbo[1];
		rightWheelModel[0] = new ModelRendererTurbo(this, 76, 0, textureX, textureY); // Shape 0

		rightWheelModel[0].addShape3D(7F, -7F, -2F, new Shape2D(new Coord2D[]{new Coord2D(3, 0, 3, 0), new Coord2D(11, 0, 11, 0), new Coord2D(14, 3, 14, 3), new Coord2D(14, 11, 14, 11), new Coord2D(11, 14, 11, 14), new Coord2D(3, 14, 3, 14), new Coord2D(0, 11, 0, 11), new Coord2D(0, 3, 0, 3)}), 4, 14, 14, 52, 4, ModelRendererTurbo.MR_FRONT, new float[]{5, 8, 5, 8, 5, 8, 5, 8}); // Shape 0
		rightWheelModel[0].setRotationPoint(-4F, 0F, 0F);
		rightWheelModel[0].rotateAngleY = -1.57079633F;


		wheelAxleModel = new ModelRendererTurbo[1];
		wheelAxleModel[0] = new ModelRendererTurbo(this, 88, 57, textureX, textureY); // Box 2

		wheelAxleModel[0].addBox(0F, -1F, -1F, 18, 2, 2, 0F); // Box 2
		wheelAxleModel[0].setRotationPoint(-1F, 0F, 8F);


		ejectionPlateModel = new ModelRendererTurbo[1];
		ejectionPlateModel[0] = new ModelRendererTurbo(this, 20, 41, textureX, textureY); // Box 23

		ejectionPlateModel[0].addBox(0F, 0F, 0F, 6, 1, 10, 0F); // Box 23

		pitchThingyModel = new ModelRendererTurbo[2];
		pitchThingyModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 35
		pitchThingyModel[1] = new ModelRendererTurbo(this, 0, 2, textureX, textureY); // Box 35

		pitchThingyModel[0].addBox(-0.5F, 0F, -0.5F, 1, 1, 1, 0F); // Box 35
		pitchThingyModel[0].setRotationPoint(14F, -3F, 17F);

		pitchThingyModel[1].addBox(-1.5F, -1F, -1.5F, 3, 1, 3, 0F); // Box 35
		pitchThingyModel[1].setRotationPoint(14F, -3F, 17F);

		parts.put("base", baseModel);
		parts.put("gun", gunModel);
		parts.put("barrel", barrelModel);
		parts.put("leftWheel", leftWheelModel);
		parts.put("rightWheel", rightWheelModel);
		parts.put("wheelAxle", wheelAxleModel);
		parts.put("trigger", triggerModel);
		parts.put("ejectionPlate", ejectionPlateModel);
		parts.put("pitchThingy", pitchThingyModel);


		flipAll();
	}
}