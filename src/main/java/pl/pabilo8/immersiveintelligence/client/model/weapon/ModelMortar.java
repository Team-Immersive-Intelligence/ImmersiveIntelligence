package pl.pabilo8.immersiveintelligence.client.model.weapon;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * @author Pabilo8
 * @since 17.07.2020
 */
public class ModelMortar extends ModelIIBase
{
	int textureX = 64;
	int textureY = 64;

	public ModelRendererTurbo[] baseHandleModel, tubeModel, bipodModel, heightRodModel, heightKnobModel, horizontalRodModel, horizontalKnobModel, tubeRodsModel, sightsHolderModel, sightsModel;


	public ModelMortar() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[5];
		baseModel[0] = new ModelRendererTurbo(this, 40, 16, textureX, textureY); // Base02
		baseModel[1] = new ModelRendererTurbo(this, 6, 32, textureX, textureY); // Base03
		baseModel[2] = new ModelRendererTurbo(this, 49, 33, textureX, textureY); // Tube13
		baseModel[3] = new ModelRendererTurbo(this, 49, 30, textureX, textureY); // Tube13
		baseModel[4] = new ModelRendererTurbo(this, 0, 49, textureX, textureY); // Shape 59

		baseModel[0].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // Base02
		baseModel[0].setRotationPoint(1F, 6F, 1F);

		baseModel[1].addBox(0F, 0F, 0F, 1, 2, 6, 0F); // Base03
		baseModel[1].setRotationPoint(7F, 8F, 1F);
		baseModel[1].rotateAngleZ = 0.26179939F;

		baseModel[2].addShapeBox(0F, 0F, 0F, 4, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tube13
		baseModel[2].setRotationPoint(2F, 5F, 5F);

		baseModel[3].addShapeBox(0F, 0F, 0F, 4, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tube13
		baseModel[3].setRotationPoint(2F, 5F, 2F);

		baseModel[4].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(12, 0, 12, 0), new Coord2D(14, 2, 14, 2), new Coord2D(14, 12, 14, 12), new Coord2D(12, 14, 12, 14), new Coord2D(2, 14, 2, 14), new Coord2D(0, 12, 0, 12), new Coord2D(0, 2, 0, 2) }), 1, 14, 14, 52, 1, ModelRendererTurbo.MR_FRONT, new float[] {3 ,10 ,3 ,10 ,3 ,10 ,3 ,10}); // Shape 59
		baseModel[4].setRotationPoint(11F, 7F, 11F);
		baseModel[4].rotateAngleX = 1.57079633F;


		baseHandleModel = new ModelRendererTurbo[3];
		baseHandleModel[0] = new ModelRendererTurbo(this, 0, 42, textureX, textureY); // Shape 59
		baseHandleModel[1] = new ModelRendererTurbo(this, 20, 47, textureX, textureY); // Base04
		baseHandleModel[2] = new ModelRendererTurbo(this, 20, 47, textureX, textureY); // Base05

		baseHandleModel[0].setFlipped(true);
		baseHandleModel[0].addShape3D(4F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 1, 0, 1), new Coord2D(0, 5, 0, 5), new Coord2D(1, 6, 1, 6), new Coord2D(1, 0, 1, 0) }), 1, 1, 6, 14, 1, ModelRendererTurbo.MR_FRONT, new float[] {2 ,6 ,2 ,4}); // Shape 59
		baseHandleModel[0].setRotationPoint(9F, 6F, 7F);
		baseHandleModel[0].rotateAngleX = 1.57079633F;

		baseHandleModel[1].addShapeBox(0F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Base04
		baseHandleModel[1].setRotationPoint(9F, 6F, 1F);

		baseHandleModel[2].addShapeBox(0F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Base05
		baseHandleModel[2].setRotationPoint(9F, 6F, 6F);


		tubeModel = new ModelRendererTurbo[16];
		tubeModel[0] = new ModelRendererTurbo(this, 28, 36, textureX, textureY); // Tube03
		tubeModel[1] = new ModelRendererTurbo(this, 28, 36, textureX, textureY); // Tube04
		tubeModel[2] = new ModelRendererTurbo(this, 21, 22, textureX, textureY); // Tube09
		tubeModel[3] = new ModelRendererTurbo(this, 28, 17, textureX, textureY); // Tube10
		tubeModel[4] = new ModelRendererTurbo(this, 30, 12, textureX, textureY); // Tube11
		tubeModel[5] = new ModelRendererTurbo(this, 32, 0, textureX, textureY); // Tube12
		tubeModel[6] = new ModelRendererTurbo(this, 45, 26, textureX, textureY); // Tube13
		tubeModel[7] = new ModelRendererTurbo(this, 39, 30, textureX, textureY); // Box 53
		tubeModel[8] = new ModelRendererTurbo(this, 20, 35, textureX, textureY); // Box 61
		tubeModel[9] = new ModelRendererTurbo(this, 20, 35, textureX, textureY); // Box 62
		tubeModel[10] = new ModelRendererTurbo(this, 40, 40, textureX, textureY); // Shape 62
		tubeModel[11] = new ModelRendererTurbo(this, 40, 40, textureX, textureY); // Shape 62
		tubeModel[12] = new ModelRendererTurbo(this, 0, 30, textureX, textureY); // Tube03
		tubeModel[13] = new ModelRendererTurbo(this, 0, 30, textureX, textureY); // Tube03
		tubeModel[14] = new ModelRendererTurbo(this, 0, 30, textureX, textureY); // Tube03
		tubeModel[15] = new ModelRendererTurbo(this, 0, 30, textureX, textureY); // Tube03

		tubeModel[0].setFlipped(true);
		tubeModel[0].addShapeBox(0F, 0F, 0F, 1, 22, 5, 0F, 0F, 0F, -5F, 0F, 0F, -5F, 0F, 0F, -5F, 0F, 0F, -5F, 0F, 0F, -5F, 0F, 0F, -5F, 0F, 0F, -5F, 0F, 0F, -5F); // Tube03
		tubeModel[0].setRotationPoint(0.5F, -18F, 1.5F);

		tubeModel[1].addShapeBox(0F, 0F, 0F, 1, 22, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tube04
		tubeModel[1].setRotationPoint(6.5F, -18F, 1.5F);

		tubeModel[2].addBox(0F, 0F, 0F, 6, 2, 6, 0F); // Tube09
		tubeModel[2].setRotationPoint(1F, 2F, 1F);

		tubeModel[3].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // Tube10
		tubeModel[3].setRotationPoint(2F, 1F, 2F);

		tubeModel[4].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Tube11
		tubeModel[4].setRotationPoint(3.5F, -2F, 3.5F);

		tubeModel[5].addBox(0F, 0F, 0F, 8, 1, 8, 0F); // Tube12
		tubeModel[5].setRotationPoint(0F, 4F, 0F);

		tubeModel[6].addShapeBox(0F, 0F, 0F, 4, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tube13
		tubeModel[6].setRotationPoint(2F, 5F, 3F);

		tubeModel[7].addShapeBox(0F, 0F, 0F, 2, 4, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 53
		tubeModel[7].setRotationPoint(-1F, -10F, 1F);

		tubeModel[8].addShapeBox(0F, 0F, 0F, 2, 10, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 61
		tubeModel[8].setRotationPoint(-2F, -13F, 1.5F);

		tubeModel[9].addShapeBox(0F, 0F, 0F, 2, 10, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 62
		tubeModel[9].setRotationPoint(-2F, -13F, 4.5F);

		tubeModel[10].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(7, 0, 7, 0), new Coord2D(6, 1, 6, 1), new Coord2D(1, 1, 1, 1) }), 22, 7, 1, 16, 22, ModelRendererTurbo.MR_FRONT, new float[] {2 ,5 ,2 ,7}); // Shape 62
		tubeModel[10].setRotationPoint(7.5F, -18F, 1.5F);
		tubeModel[10].rotateAngleX = 1.57079633F;

		tubeModel[11].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(7, 0, 7, 0), new Coord2D(6, 1, 6, 1), new Coord2D(1, 1, 1, 1) }), 22, 7, 1, 16, 22, ModelRendererTurbo.MR_FRONT, new float[] {2 ,5 ,2 ,7}); // Shape 62
		tubeModel[11].setRotationPoint(0.5F, -18F, 6.5F);
		tubeModel[11].rotateAngleX = 1.57079633F;
		tubeModel[11].rotateAngleY = -3.14159265F;

		tubeModel[12].addShapeBox(0F, 0F, 0F, 1, 3, 5, 0F, 0F, 0F, -5F, 0F, 0F, -5F, 0F, 0F, -5F, 0F, 0F, -5F, 0F, 0F, -5F, 0F, 0F, -5F, 0F, 0F, -5F, 0F, 0F, -5F); // Tube03
		tubeModel[12].setRotationPoint(0.51F, -16F, 1.5F);

		tubeModel[13].addShapeBox(0F, 0F, 0F, 1, 3, 5, 0F, 0F, 0F, -5F, 0F, 0F, -5F, 0F, 0F, -5F, 0F, 0F, -5F, 0F, 0F, -5F, 0F, 0F, -5F, 0F, 0F, -5F, 0F, 0F, -5F); // Tube03
		tubeModel[13].setRotationPoint(1.5F, -16F, 1.51F);
		tubeModel[13].rotateAngleY = -1.57079633F;

		tubeModel[14].addShapeBox(0F, 0F, 0F, 1, 3, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tube03
		tubeModel[14].setRotationPoint(6.49F, -16F, 1.5F);

		tubeModel[15].addShapeBox(0F, 0F, 0F, 1, 3, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tube03
		tubeModel[15].setRotationPoint(1.5F, -16F, 7.49F);
		tubeModel[15].rotateAngleY = -1.57079633F;


		bipodModel = new ModelRendererTurbo[7];
		bipodModel[0] = new ModelRendererTurbo(this, 40, 9, textureX, textureY); // Legs01
		bipodModel[1] = new ModelRendererTurbo(this, 40, 9, textureX, textureY); // Legs02
		bipodModel[2] = new ModelRendererTurbo(this, 56, 50, textureX, textureY); // Legs03
		bipodModel[3] = new ModelRendererTurbo(this, 56, 50, textureX, textureY); // Legs04
		bipodModel[4] = new ModelRendererTurbo(this, 2, 40, textureX, textureY); // Legs05
		bipodModel[5] = new ModelRendererTurbo(this, 56, 41, textureX, textureY); // Legs06
		bipodModel[6] = new ModelRendererTurbo(this, 56, 4, textureX, textureY); // Legs07

		bipodModel[0].addShapeBox(0F, 0F, 0F, 6, 1, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Legs01
		bipodModel[0].setRotationPoint(-13F, 7F, -5F);

		bipodModel[1].addShapeBox(0F, 0F, 0F, 6, 1, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Legs02
		bipodModel[1].setRotationPoint(-13F, 7F, 7F);

		bipodModel[2].addShapeBox(0F, 0F, 0F, 2, 12, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Legs03
		bipodModel[2].setRotationPoint(-11F, -3.25F, 5.2F);
		bipodModel[2].rotateAngleX = 0.36651914F;

		bipodModel[3].setFlipped(true);
		bipodModel[3].addShapeBox(0F, 0F, -2F, 2, 12, 2, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F); // Legs04
		bipodModel[3].setRotationPoint(-11F, -3.25F, 2.75F);
		bipodModel[3].rotateAngleX = -0.36651914F;

		bipodModel[4].addShapeBox(0F, 0F, 0F, 3, 2, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Legs05
		bipodModel[4].setRotationPoint(-11.5F, -5F, 1F);

		bipodModel[5].addShapeBox(0F, 0F, 0F, 2, 7, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Legs06
		bipodModel[5].setRotationPoint(-11F, -3F, 3F);

		bipodModel[6].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Legs07
		bipodModel[6].setRotationPoint(-11F, -7F, 3F);


		heightRodModel = new ModelRendererTurbo[1];
		heightRodModel[0] = new ModelRendererTurbo(this, 59, 28, textureX, textureY); // AttitudeWorm01

		heightRodModel[0].addBox(0F, 0F, 0F, 1, 12, 1, 0F); // AttitudeWorm01
		heightRodModel[0].setRotationPoint(-10.5F, -9F, 3.5F);


		heightKnobModel = new ModelRendererTurbo[2];
		heightKnobModel[0] = new ModelRendererTurbo(this, 16, 44, textureX, textureY); // RotHandleAttitide01
		heightKnobModel[1] = new ModelRendererTurbo(this, 55, 36, textureX, textureY); // RotHandleAttitide02

		heightKnobModel[0].addShapeBox(0F, -0.5F, -0.5F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // RotHandleAttitide01
		heightKnobModel[0].setRotationPoint(-12F, -6F, 4F);

		heightKnobModel[1].addShapeBox(0F, -0.5F, -0.5F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // RotHandleAttitide02
		heightKnobModel[1].setRotationPoint(-13F, -6F, 4F);


		horizontalRodModel = new ModelRendererTurbo[7];
		horizontalRodModel[0] = new ModelRendererTurbo(this, 20, 3, textureX, textureY); // WormHorizontal02
		horizontalRodModel[1] = new ModelRendererTurbo(this, 56, 1, textureX, textureY); // WormHorizontal03
		horizontalRodModel[2] = new ModelRendererTurbo(this, 56, 1, textureX, textureY); // WormHorizontal04
		horizontalRodModel[3] = new ModelRendererTurbo(this, 58, 9, textureX, textureY); // WormHorizontal05
		horizontalRodModel[4] = new ModelRendererTurbo(this, 58, 9, textureX, textureY); // WormHorizontal06
		horizontalRodModel[5] = new ModelRendererTurbo(this, 13, 21, textureX, textureY); // Box 65
		horizontalRodModel[6] = new ModelRendererTurbo(this, 56, 4, textureX, textureY); // AttitudeWorm02

		horizontalRodModel[0].addShapeBox(0F, 0F, 0F, 2, 1, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // WormHorizontal02
		horizontalRodModel[0].setRotationPoint(-11F, -12F, 0F);

		horizontalRodModel[1].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // WormHorizontal03
		horizontalRodModel[1].setRotationPoint(-11F, -11F, -1F);

		horizontalRodModel[2].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // WormHorizontal04
		horizontalRodModel[2].setRotationPoint(-11F, -11F, 8F);

		horizontalRodModel[3].addShapeBox(0F, 0F, 0F, 2, 1, 1, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // WormHorizontal05
		horizontalRodModel[3].setRotationPoint(-11F, -12F, -1F);

		horizontalRodModel[4].addShapeBox(0F, 0F, 0F, 2, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // WormHorizontal06
		horizontalRodModel[4].setRotationPoint(-11F, -12F, 8F);

		horizontalRodModel[5].addShapeBox(0F, 0F, 0F, 1, 1, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 65
		horizontalRodModel[5].setRotationPoint(-10F, -13F, 1F);

		horizontalRodModel[6].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // AttitudeWorm02
		horizontalRodModel[6].setRotationPoint(-11F, -11F, 3F);


		horizontalKnobModel = new ModelRendererTurbo[2];
		horizontalKnobModel[0] = new ModelRendererTurbo(this, 55, 36, textureX, textureY); // RotHandleHorizontal01
		horizontalKnobModel[1] = new ModelRendererTurbo(this, 29, 30, textureX, textureY); // WormHorizontal01

		horizontalKnobModel[0].addShapeBox(-0.5F, -2.5F, -0.5F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // RotHandleHorizontal01
		horizontalKnobModel[0].setRotationPoint(-10F, -10F, -2.5F);

		horizontalKnobModel[1].addShapeBox(-0.5F, -0.5F, -0.5F, 1, 10, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // WormHorizontal01
		horizontalKnobModel[1].setRotationPoint(-10F, -10F, -1.5F);
		horizontalKnobModel[1].rotateAngleX = 1.57079633F;

		tubeRodsModel = new ModelRendererTurbo[2];
		tubeRodsModel[0] = new ModelRendererTurbo(this, 35, 28, textureX, textureY); // PistonsMaybe01
		tubeRodsModel[1] = new ModelRendererTurbo(this, 35, 28, textureX, textureY); // PistonsMaybe02

		tubeRodsModel[0].addBox(0F, 0F, 0F, 1, 14, 1, 0F); // PistonsMaybe01
		tubeRodsModel[0].setRotationPoint(-1.5F, -16F, 2F);

		tubeRodsModel[1].addBox(0F, 0F, 0F, 1, 14, 1, 0F); // PistonsMaybe02
		tubeRodsModel[1].setRotationPoint(-1.5F, -16F, 5F);

		sightsHolderModel = new ModelRendererTurbo[1];
		sightsHolderModel[0] = new ModelRendererTurbo(this, 24, 0, textureX, textureY); // Box 66

		sightsHolderModel[0].addBox(0F, 0F, 0F, 7, 2, 1, 0F); // Box 66
		sightsHolderModel[0].setRotationPoint(-11F, -11F, -1.01F);
		sightsHolderModel[0].rotateAngleZ = 0.78539816F;

		sightsModel = new ModelRendererTurbo[12];
		sightsModel[0] = new ModelRendererTurbo(this, 32, 3, textureX, textureY); // Scope01
		sightsModel[1] = new ModelRendererTurbo(this, 58, 11, textureX, textureY); // Scope02
		sightsModel[2] = new ModelRendererTurbo(this, 58, 11, textureX, textureY); // Scope03
		sightsModel[3] = new ModelRendererTurbo(this, 58, 16, textureX, textureY); // Scope04
		sightsModel[4] = new ModelRendererTurbo(this, 42, 13, textureX, textureY); // Scope05
		sightsModel[5] = new ModelRendererTurbo(this, 58, 18, textureX, textureY); // Scope06
		sightsModel[6] = new ModelRendererTurbo(this, 58, 20, textureX, textureY); // Scope07
		sightsModel[7] = new ModelRendererTurbo(this, 24, 12, textureX, textureY); // Scope08
		sightsModel[8] = new ModelRendererTurbo(this, 42, 9, textureX, textureY); // Scope09
		sightsModel[9] = new ModelRendererTurbo(this, 32, 9, textureX, textureY); // Scope10
		sightsModel[10] = new ModelRendererTurbo(this, 34, 12, textureX, textureY); // Scope11
		sightsModel[11] = new ModelRendererTurbo(this, 40, 16, textureX, textureY); // Scope12

		sightsModel[0].addBox(0F, 0F, 0F, 3, 2, 1, 0F); // Scope01
		sightsModel[0].setRotationPoint(-7F, -16F, -2.01F);

		sightsModel[1].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Scope02
		sightsModel[1].setRotationPoint(-6F, -18F, -3.01F);

		sightsModel[2].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Scope03
		sightsModel[2].setRotationPoint(-6F, -18F, -5.01F);

		sightsModel[3].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Scope04
		sightsModel[3].setRotationPoint(-6F, -19F, -4.01F);

		sightsModel[4].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Scope05
		sightsModel[4].setRotationPoint(-6F, -16F, -4.01F);

		sightsModel[5].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Scope06
		sightsModel[5].setRotationPoint(-6F, -19F, -5.01F);

		sightsModel[6].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Scope07
		sightsModel[6].setRotationPoint(-6F, -19F, -3.01F);

		sightsModel[7].addBox(0F, 0F, 0F, 1, 1, 4, 0F); // Scope08
		sightsModel[7].setRotationPoint(-6F, -14.8F, -5.51F);

		sightsModel[8].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Scope09
		sightsModel[8].setRotationPoint(-7.2F, -16.2F, -3.01F);

		sightsModel[9].addBox(0F, 0F, 0F, 2, 1, 1, 0F); // Scope10
		sightsModel[9].setRotationPoint(-6.2F, -14.2F, -3F);

		sightsModel[10].addBox(0F, 0F, 0F, 1, 2, 2, 0F); // Scope11
		sightsModel[10].setRotationPoint(-4.8F, -14.7F, -3.5F);

		sightsModel[11].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // Scope12
		sightsModel[11].setRotationPoint(-6.5F, -15.25F, -6.51F);


		parts.put("base", baseModel);
		parts.put("baseHandle", baseHandleModel);
		parts.put("tube", tubeModel);
		parts.put("bipod", bipodModel);
		parts.put("heightRod", heightRodModel);
		parts.put("heightKnob", heightKnobModel);
		parts.put("horizontalRod", horizontalRodModel);
		parts.put("horizontalKnob", horizontalKnobModel);
		parts.put("tubeRods", tubeRodsModel);
		parts.put("sightsHolder", sightsHolderModel);
		parts.put("sights", sightsModel);

		flipAll();

		translateAll(-4,8,4);
	}
}