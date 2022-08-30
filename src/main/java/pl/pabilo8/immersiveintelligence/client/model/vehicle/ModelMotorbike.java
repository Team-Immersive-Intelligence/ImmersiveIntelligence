package pl.pabilo8.immersiveintelligence.client.model.vehicle;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * Created by Pabilo8 on 07-06-2019.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class ModelMotorbike extends ModelIIBase
{
	int textureX = 128;
	int textureY = 128;

	public ModelRendererTurbo[] accelerationModel, brakeModel, frontWheelModel, backWheelModel, frontThingyModel, frontThingyUpperModel, steeringGearModel, engineModel, exhaustPipesModel, trailerThingyModel;
	public ModelRendererTurbo[] upgradeSeatModel, upgradeTankModel, upgradeStorageModel;

	public ModelMotorbike() //Same as Filename
	{

		baseModel = new ModelRendererTurbo[38];
		baseModel[0] = new ModelRendererTurbo(this, 47, 68, textureX, textureY); // Box 1
		baseModel[1] = new ModelRendererTurbo(this, 35, 48, textureX, textureY); // Box 2
		baseModel[2] = new ModelRendererTurbo(this, 98, 41, textureX, textureY); // Box 1
		baseModel[3] = new ModelRendererTurbo(this, 2, 24, textureX, textureY); // Box 2
		baseModel[4] = new ModelRendererTurbo(this, 2, 24, textureX, textureY); // Box 2
		baseModel[5] = new ModelRendererTurbo(this, 12, 8, textureX, textureY); // Box 2
		baseModel[6] = new ModelRendererTurbo(this, 8, 4, textureX, textureY); // Box 1
		baseModel[7] = new ModelRendererTurbo(this, 8, 4, textureX, textureY); // Box 1
		baseModel[8] = new ModelRendererTurbo(this, 75, 51, textureX, textureY); // Box 2
		baseModel[9] = new ModelRendererTurbo(this, 75, 51, textureX, textureY); // Box 2
		baseModel[10] = new ModelRendererTurbo(this, 19, 50, textureX, textureY); // Box 2
		baseModel[11] = new ModelRendererTurbo(this, 95, 55, textureX, textureY); // Box 2
		baseModel[12] = new ModelRendererTurbo(this, 68, 37, textureX, textureY); // Box 2
		baseModel[13] = new ModelRendererTurbo(this, 0, 2, textureX, textureY); // Box 2
		baseModel[14] = new ModelRendererTurbo(this, 0, 2, textureX, textureY); // Box 2
		baseModel[15] = new ModelRendererTurbo(this, 63, 18, textureX, textureY); // Box 2
		baseModel[16] = new ModelRendererTurbo(this, 64, 19, textureX, textureY); // Box 2
		baseModel[17] = new ModelRendererTurbo(this, 7, 50, textureX, textureY); // Box 2
		baseModel[18] = new ModelRendererTurbo(this, 7, 50, textureX, textureY); // Box 2
		baseModel[19] = new ModelRendererTurbo(this, 104, 0, textureX, textureY); // Box 2
		baseModel[20] = new ModelRendererTurbo(this, 16, 2, textureX, textureY); // Box 2
		baseModel[21] = new ModelRendererTurbo(this, 27, 74, textureX, textureY); // Box 2
		baseModel[22] = new ModelRendererTurbo(this, 100, 28, textureX, textureY); // Box 2
		baseModel[23] = new ModelRendererTurbo(this, 115, 3, textureX, textureY); // Box 2
		baseModel[24] = new ModelRendererTurbo(this, 15, 73, textureX, textureY); // Box 2
		baseModel[25] = new ModelRendererTurbo(this, 16, 68, textureX, textureY); // Box 2
		baseModel[26] = new ModelRendererTurbo(this, 44, 32, textureX, textureY); // Box 2
		baseModel[27] = new ModelRendererTurbo(this, 28, 4, textureX, textureY); // Box 2
		baseModel[28] = new ModelRendererTurbo(this, 34, 20, textureX, textureY); // Box 2
		baseModel[29] = new ModelRendererTurbo(this, 26, 12, textureX, textureY); // Box 2
		baseModel[30] = new ModelRendererTurbo(this, 99, 34, textureX, textureY); // Box 2
		baseModel[31] = new ModelRendererTurbo(this, 22, 24, textureX, textureY); // Box 2
		baseModel[32] = new ModelRendererTurbo(this, 68, 30, textureX, textureY); // Box 2
		baseModel[33] = new ModelRendererTurbo(this, 41, 78, textureX, textureY); // Box 2
		baseModel[34] = new ModelRendererTurbo(this, 40, 71, textureX, textureY); // Box 2
		baseModel[35] = new ModelRendererTurbo(this, 83, 69, textureX, textureY); // Box 2
		baseModel[36] = new ModelRendererTurbo(this, 4, 0, textureX, textureY); // Box 2
		baseModel[37] = new ModelRendererTurbo(this, 63, 51, textureX, textureY); // Box 2

		baseModel[0].addBox(0F, 0F, 0F, 10, 3, 8, 0F); // Box 1
		baseModel[0].setRotationPoint(4F, -14F, 5F);

		baseModel[1].addBox(0F, 0F, 0F, 8, 8, 12, 0F); // Box 2
		baseModel[1].setRotationPoint(5F, -16F, -8F);

		baseModel[2].addBox(0F, 0F, 0F, 8, 1, 6, 0F); // Box 1
		baseModel[2].setRotationPoint(5F, -11F, 6F);

		baseModel[3].addBox(0F, 0F, 0F, 2, 2, 16, 0F); // Box 2
		baseModel[3].setRotationPoint(6F, -8F, -8F);

		baseModel[4].addBox(0F, 0F, 0F, 2, 2, 16, 0F); // Box 2
		baseModel[4].setRotationPoint(10F, -8F, -8F);

		baseModel[5].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // Box 2
		baseModel[5].setRotationPoint(7F, -17F, -1F);

		baseModel[6].addBox(0F, 0F, 0F, 2, 6, 2, 0F); // Box 1
		baseModel[6].setRotationPoint(6F, -10F, 8F);

		baseModel[7].addBox(0F, 0F, 0F, 2, 6, 2, 0F); // Box 1
		baseModel[7].setRotationPoint(10F, -10F, 8F);

		baseModel[8].addShapeBox(0F, 0F, 0F, 2, 2, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[8].setRotationPoint(12.01F, -8F, 6F);
		baseModel[8].rotateAngleX = -0.38397244F;

		baseModel[9].addShapeBox(0F, 0F, 0F, 2, 2, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[9].setRotationPoint(4.01F, -8F, 6F);
		baseModel[9].rotateAngleX = -0.38397244F;

		baseModel[10].addShapeBox(0F, 0F, 0F, 6, 2, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.25F, -1F, 0F, -0.25F, -1F); // Box 2
		baseModel[10].setRotationPoint(6F, -6F, 10F);
		baseModel[10].rotateAngleX = 0.52359878F;

		baseModel[11].addShapeBox(0F, 0F, 0F, 6, 2, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[11].setRotationPoint(6F, -10F, 17F);

		baseModel[12].addShapeBox(0F, 0F, 0F, 6, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[12].setRotationPoint(6F, -2.01F, 19.01F);

		baseModel[13].addShapeBox(0F, 0F, -2F, 2, 2, 2, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[13].setRotationPoint(12.01F, -8F, 6F);
		baseModel[13].rotateAngleX = -0.38397244F;

		baseModel[14].addShapeBox(0F, 0F, -2F, 2, 2, 2, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[14].setRotationPoint(4.01F, -8F, 6F);
		baseModel[14].rotateAngleX = -0.38397244F;

		baseModel[15].addBox(0F, 0F, 0F, 2, 2, 31, 0F); // Box 2
		baseModel[15].setRotationPoint(12F, -2F, -10F);

		baseModel[16].addBox(0F, 0F, 0F, 2, 2, 30, 0F); // Box 2
		baseModel[16].setRotationPoint(4F, -2F, -10F);

		baseModel[17].addBox(-1F, 0F, -1F, 2, 10, 2, 0F); // Box 2
		baseModel[17].setRotationPoint(13F, -12F, -9F);
		baseModel[17].rotateAngleY = -1.57079633F;

		baseModel[18].addBox(0F, 0F, 0F, 2, 10, 2, 0F); // Box 2
		baseModel[18].setRotationPoint(4F, -12F, -10F);

		baseModel[19].addBox(0F, 0F, 0F, 1, 5, 9, 0F); // Box 2
		baseModel[19].setRotationPoint(13F, -15F, -7F);

		baseModel[20].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[20].setRotationPoint(1F, -2F, -1F);

		baseModel[21].addShapeBox(0F, 0F, 0F, 5, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // Box 2
		baseModel[21].setRotationPoint(1F, 1F, -1F);

		baseModel[22].addShapeBox(0F, 0F, 0F, 11, 3, 3, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[22].setRotationPoint(1F, 1F, 4F);

		baseModel[23].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[23].setRotationPoint(12F, 1F, -1F);

		baseModel[24].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[24].setRotationPoint(12F, 1F, 4F);

		baseModel[25].addBox(0F, 0F, 0F, 3, 3, 2, 0F); // Box 2
		baseModel[25].setRotationPoint(12F, 1F, 2F);

		baseModel[26].addBox(-2F, -4F, -2F, 4, 6, 4, 0F); // Box 2
		baseModel[26].setRotationPoint(9F, -1F, 5F);
		baseModel[26].rotateAngleX = -0.38397244F;

		baseModel[27].addShapeBox(0F, 0F, 0F, 10, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[27].setRotationPoint(4F, -2F, 21F);

		baseModel[28].addBox(0F, 0F, 0F, 12, 2, 2, 0F); // Box 2
		baseModel[28].setRotationPoint(3F, -14F, -10F);

		baseModel[29].addShapeBox(0F, 0F, -2F, 4, 1, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[29].setRotationPoint(14F, -3F, 1F);
		baseModel[29].rotateAngleX = -0.06981317F;

		baseModel[30].addShapeBox(0F, 0F, -2F, 4, 1, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[30].setRotationPoint(0F, -3F, 1F);
		baseModel[30].rotateAngleX = -0.05235988F;

		baseModel[31].addBox(0F, 0F, 0F, 2, 2, 5, 0F); // Box 2
		baseModel[31].setRotationPoint(6F, -16F, -13F);

		baseModel[32].addBox(0F, 0F, 0F, 2, 2, 5, 0F); // Box 2
		baseModel[32].setRotationPoint(10F, -16F, -13F);

		baseModel[33].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, -0.5F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[33].setRotationPoint(10F, -16F, -15F);

		baseModel[34].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, -0.5F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[34].setRotationPoint(6F, -16F, -15F);

		baseModel[35].addBox(0F, 0F, 0F, 2, 4, 6, 0F); // Box 2
		baseModel[35].setRotationPoint(3F, -16F, -8F);

		baseModel[36].addShapeBox(0F, 0F, 0F, 2, 1, 1, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[36].setRotationPoint(4F, -15.5F, -9F);

		baseModel[37].addBox(-1.5F, -18F, -1.5F, 3, 4, 3, 0F); // Box 2
		baseModel[37].setRotationPoint(9F, 0F, -19F);
		baseModel[37].rotateAngleX = -0.31415927F;

		engineModel = new ModelRendererTurbo[15];
		engineModel[0] = new ModelRendererTurbo(this, 40, 0, textureX, textureY); // Box 2
		engineModel[1] = new ModelRendererTurbo(this, 1, 42, textureX, textureY); // Box 2
		engineModel[2] = new ModelRendererTurbo(this, 1, 42, textureX, textureY); // Box 2
		engineModel[3] = new ModelRendererTurbo(this, 19, 60, textureX, textureY); // Box 2
		engineModel[4] = new ModelRendererTurbo(this, 26, 68, textureX, textureY); // Box 2
		engineModel[5] = new ModelRendererTurbo(this, 15, 54, textureX, textureY); // Box 2
		engineModel[6] = new ModelRendererTurbo(this, 19, 60, textureX, textureY); // Box 2
		engineModel[7] = new ModelRendererTurbo(this, 26, 68, textureX, textureY); // Box 2
		engineModel[8] = new ModelRendererTurbo(this, 15, 54, textureX, textureY); // Box 2
		engineModel[9] = new ModelRendererTurbo(this, 19, 60, textureX, textureY); // Box 2
		engineModel[10] = new ModelRendererTurbo(this, 26, 68, textureX, textureY); // Box 2
		engineModel[11] = new ModelRendererTurbo(this, 15, 54, textureX, textureY); // Box 2
		engineModel[12] = new ModelRendererTurbo(this, 19, 60, textureX, textureY); // Box 2
		engineModel[13] = new ModelRendererTurbo(this, 26, 68, textureX, textureY); // Box 2
		engineModel[14] = new ModelRendererTurbo(this, 15, 54, textureX, textureY); // Box 2

		engineModel[0].addBox(0F, 0F, 0F, 6, 6, 12, 0F); // Box 2
		engineModel[0].setRotationPoint(6F, -1F, -8F);

		engineModel[1].addBox(0F, 0F, 0F, 3, 4, 4, 0F); // Box 2
		engineModel[1].setRotationPoint(12F, 0F, -6F);

		engineModel[2].addBox(0F, 0F, 0F, 3, 4, 4, 0F); // Box 2
		engineModel[2].setRotationPoint(3F, 0F, -6F);

		engineModel[3].addBox(-2F, -3F, -2F, 4, 4, 4, 0F); // Box 2
		engineModel[3].setRotationPoint(7F, -1F, -4F);
		engineModel[3].rotateAngleZ = 0.36651914F;

		engineModel[4].addBox(-2F, -5F, -2F, 4, 1, 4, 0F); // Box 2
		engineModel[4].setRotationPoint(7F, -1F, -4F);
		engineModel[4].rotateAngleZ = 0.36651914F;

		engineModel[5].addBox(-1.5F, -4F, -1.5F, 3, 1, 3, 0F); // Box 2
		engineModel[5].setRotationPoint(7F, -1F, -4F);
		engineModel[5].rotateAngleZ = 0.36651914F;

		engineModel[6].addBox(-2F, -3F, -2F, 4, 4, 4, 0F); // Box 2
		engineModel[6].setRotationPoint(7F, -1F, 1F);
		engineModel[6].rotateAngleZ = 0.36651914F;

		engineModel[7].addBox(-2F, -5F, -2F, 4, 1, 4, 0F); // Box 2
		engineModel[7].setRotationPoint(7F, -1F, 1F);
		engineModel[7].rotateAngleZ = 0.36651914F;

		engineModel[8].addBox(-1.5F, -4F, -1.5F, 3, 1, 3, 0F); // Box 2
		engineModel[8].setRotationPoint(7F, -1F, 1F);
		engineModel[8].rotateAngleZ = 0.36651914F;

		engineModel[9].addBox(-2F, -3F, -2F, 4, 4, 4, 0F); // Box 2
		engineModel[9].setRotationPoint(11F, -1F, -4F);
		engineModel[9].rotateAngleZ = -0.36651914F;

		engineModel[10].addBox(-2F, -5F, -2F, 4, 1, 4, 0F); // Box 2
		engineModel[10].setRotationPoint(11F, -1F, -4F);
		engineModel[10].rotateAngleZ = -0.36651914F;

		engineModel[11].addBox(-1.5F, -4F, -1.5F, 3, 1, 3, 0F); // Box 2
		engineModel[11].setRotationPoint(11F, -1F, -4F);
		engineModel[11].rotateAngleZ = -0.36651914F;

		engineModel[12].addBox(-2F, -3F, -2F, 4, 4, 4, 0F); // Box 2
		engineModel[12].setRotationPoint(11F, -1F, 1F);
		engineModel[12].rotateAngleZ = -0.36651914F;

		engineModel[13].addBox(-2F, -5F, -2F, 4, 1, 4, 0F); // Box 2
		engineModel[13].setRotationPoint(11F, -1F, 1F);
		engineModel[13].rotateAngleZ = -0.36651914F;

		engineModel[14].addBox(-1.5F, -4F, -1.5F, 3, 1, 3, 0F); // Box 2
		engineModel[14].setRotationPoint(11F, -1F, 1F);
		engineModel[14].rotateAngleZ = -0.36651914F;


		exhaustPipesModel = new ModelRendererTurbo[2];
		exhaustPipesModel[0] = new ModelRendererTurbo(this, 20, 24, textureX, textureY); // Box 2
		exhaustPipesModel[1] = new ModelRendererTurbo(this, 37, 20, textureX, textureY); // Box 2

		exhaustPipesModel[0].addBox(0F, 0F, 0F, 3, 3, 18, 0F); // Box 2
		exhaustPipesModel[0].setRotationPoint(1F, 1F, 7F);

		exhaustPipesModel[1].addBox(0F, 0F, 0F, 3, 3, 25, 0F); // Box 2
		exhaustPipesModel[1].setRotationPoint(1F, -2F, 2F);


		frontThingyModel = new ModelRendererTurbo[3];
		frontThingyModel[0] = new ModelRendererTurbo(this, 104, 3, textureX, textureY); // Box 2
		frontThingyModel[1] = new ModelRendererTurbo(this, 0, 12, textureX, textureY); // Box 2
		frontThingyModel[2] = new ModelRendererTurbo(this, 28, 4, textureX, textureY); // Box 2

		frontThingyModel[0].addBox(-4F, -4F, -1F, 2, 4, 2, 0F); // Box 2
		frontThingyModel[0].rotateAngleX = -0.31415927F;

		frontThingyModel[1].addBox(2F, -4F, -1F, 2, 4, 2, 0F); // Box 2
		frontThingyModel[1].rotateAngleX = -0.31415927F;

		frontThingyModel[2].addBox(-5F, -1F, -1F, 10, 2, 2, 0F); // Box 2


		steeringGearModel = new ModelRendererTurbo[15];
		steeringGearModel[0] = new ModelRendererTurbo(this, 24, 8, textureX, textureY); // Box 2
		steeringGearModel[1] = new ModelRendererTurbo(this, 102, 21, textureX, textureY); // Box 2
		steeringGearModel[2] = new ModelRendererTurbo(this, 4, 33, textureX, textureY); // Box 2
		steeringGearModel[3] = new ModelRendererTurbo(this, 43, 70, textureX, textureY); // Box 2
		steeringGearModel[4] = new ModelRendererTurbo(this, 43, 70, textureX, textureY); // Box 2
		steeringGearModel[5] = new ModelRendererTurbo(this, 95, 51, textureX, textureY); // Box 2
		steeringGearModel[6] = new ModelRendererTurbo(this, 46, 24, textureX, textureY); // Box 2
		steeringGearModel[7] = new ModelRendererTurbo(this, 46, 24, textureX, textureY); // Box 2
		steeringGearModel[8] = new ModelRendererTurbo(this, 75, 56, textureX, textureY); // Box 2
		steeringGearModel[9] = new ModelRendererTurbo(this, 75, 56, textureX, textureY); // Box 2
		steeringGearModel[10] = new ModelRendererTurbo(this, 22, 21, textureX, textureY); // Box 2
		steeringGearModel[11] = new ModelRendererTurbo(this, 75, 64, textureX, textureY); // Box 2
		steeringGearModel[12] = new ModelRendererTurbo(this, 22, 31, textureX, textureY); // Box 2
		steeringGearModel[13] = new ModelRendererTurbo(this, 30, 31, textureX, textureY); // Box 2
		steeringGearModel[14] = new ModelRendererTurbo(this, 95, 51, textureX, textureY); // Box 2

		steeringGearModel[0].addBox(-6F, -1F, -1F, 12, 2, 2, 0F); // Box 2
		//steeringGearModel[0].setRotationPoint(9F, -20F, -12F);
		steeringGearModel[0].rotateAngleX = -0.31415927F;

		steeringGearModel[1].addBox(-3F, -3F, 0F, 6, 6, 1, 0F); // Box 2
		steeringGearModel[1].setRotationPoint(0f, -2F, -4F);
		steeringGearModel[1].rotateAngleX = 0.06981317F;

		steeringGearModel[2].addShapeBox(-3F, -3F, -1F, 6, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, -1F, 0F, -1F, -1F, 0F); // Box 2
		steeringGearModel[2].setRotationPoint(0F, -2F, -2F);
		steeringGearModel[2].rotateAngleX = 0.06981317F;

		steeringGearModel[3].addBox(0F, 0F, 0F, 1, 1, 5, 0F); // Box 2
		steeringGearModel[3].setRotationPoint(3F, -2F, -4F);

		steeringGearModel[4].addBox(0F, 0F, 0F, 1, 1, 5, 0F); // Box 2
		steeringGearModel[4].setRotationPoint(-4F, -2F, -4F);

		steeringGearModel[5].addShapeBox(0F, -1F, -1F, 8, 2, 2, 0F, 0.5F, 0.125F, -0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1.25F, -0.25F, -0.625F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0.125F, -0.25F); // Box 2
		steeringGearModel[5].setRotationPoint(6F, 0F, 0F);
		steeringGearModel[5].rotateAngleX = -0.31415927F;
		steeringGearModel[5].rotateAngleY = 1.13446401F;
		steeringGearModel[5].rotateAngleZ = 0.06981317F;

		steeringGearModel[6].addShapeBox(8F, -1F, -1F, 1, 2, 2, 0F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F); // Box 2
		steeringGearModel[6].setRotationPoint(-6F, 0F, 0F);
		steeringGearModel[6].rotateAngleX = 0.31415927F;
		steeringGearModel[6].rotateAngleY = 2.00712864F;
		steeringGearModel[6].rotateAngleZ = 0.06981317F;

		steeringGearModel[7].addShapeBox(8F, -1F, -1F, 1, 2, 2, 0F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F); // Box 2
		steeringGearModel[7].setRotationPoint(6F, 0F, 0F);
		steeringGearModel[7].rotateAngleX = -0.31415927F;
		steeringGearModel[7].rotateAngleY = 1.13446401F;
		steeringGearModel[7].rotateAngleZ = 0.06981317F;

		steeringGearModel[8].addBox(-3F, -3F, -2F, 1, 6, 2, 0F); // Box 2
		steeringGearModel[8].setRotationPoint(0F, -2F, -4F);
		steeringGearModel[8].rotateAngleX = 0.06981317F;

		steeringGearModel[9].addBox(2F, -3F, -2F, 1, 6, 2, 0F); // Box 2
		steeringGearModel[9].setRotationPoint(0F, -2F, -4f);
		steeringGearModel[9].rotateAngleX = 0.06981317F;

		steeringGearModel[10].addBox(-2F, -3F, -2F, 4, 1, 2, 0F); // Box 2
		steeringGearModel[10].setRotationPoint(0F, -2F, -4f);
		steeringGearModel[10].rotateAngleX = 0.06981317F;

		steeringGearModel[11].addBox(-2F, 2F, -2F, 4, 1, 2, 0F); // Box 2
		steeringGearModel[11].setRotationPoint(0F, -2F, -4f);
		steeringGearModel[11].rotateAngleX = 0.06981317F;

		steeringGearModel[12].addBox(-1.5F, -1.5F, -1F, 3, 3, 1, 0F); // Box 2
		steeringGearModel[12].setRotationPoint(0F, -2F, -4f);
		steeringGearModel[12].rotateAngleX = 0.06981317F;

		steeringGearModel[13].addBox(-2F, -2F, -1.5F, 4, 4, 0, 0F); // Box 2
		steeringGearModel[13].setRotationPoint(0F, -2F, -4f);
		steeringGearModel[13].rotateAngleX = 0.06981317F;

		steeringGearModel[14].addShapeBox(0F, -1F, -1F, 8, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.5F, 0.125F, -0.25F, -0.5F, 0.125F, -0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 1.25F, -0.25F, -0.625F); // Box 2
		steeringGearModel[14].setRotationPoint(-6F, 0F, 0f);
		steeringGearModel[14].rotateAngleX = 0.31415927F;
		steeringGearModel[14].rotateAngleY = -4.27605667F;
		steeringGearModel[14].rotateAngleZ = 0.06981317F;


		backWheelModel = new ModelRendererTurbo[1];
		backWheelModel[0] = new ModelRendererTurbo(this, 76, 0, textureX, textureY); // Shape 0

		backWheelModel[0].addShape3D(7F, -7F, -2F, new Shape2D(new Coord2D[]{new Coord2D(3, 0, 3, 0), new Coord2D(11, 0, 11, 0), new Coord2D(14, 3, 14, 3), new Coord2D(14, 11, 14, 11), new Coord2D(11, 14, 11, 14), new Coord2D(3, 14, 3, 14), new Coord2D(0, 11, 0, 11), new Coord2D(0, 3, 0, 3)}), 4, 14, 14, 52, 4, ModelRendererTurbo.MR_FRONT, new float[]{5, 8, 5, 8, 5, 8, 5, 8}); // Shape 0
		backWheelModel[0].rotateAngleY = -1.57079633F;


		frontWheelModel = new ModelRendererTurbo[1];
		frontWheelModel[0] = new ModelRendererTurbo(this, 76, 0, textureX, textureY); // Shape 0

		frontWheelModel[0].addShape3D(7F, -7F, 0F, new Shape2D(new Coord2D[]{new Coord2D(3, 0, 3, 0), new Coord2D(11, 0, 11, 0), new Coord2D(14, 3, 14, 3), new Coord2D(14, 11, 14, 11), new Coord2D(11, 14, 11, 14), new Coord2D(3, 14, 3, 14), new Coord2D(0, 11, 0, 11), new Coord2D(0, 3, 0, 3)}), 4, 14, 14, 52, 4, ModelRendererTurbo.MR_FRONT, new float[]{5, 8, 5, 8, 5, 8, 5, 8}); // Shape 0
		frontWheelModel[0].rotateAngleY = -1.57079633F;


		brakeModel = new ModelRendererTurbo[1];
		brakeModel[0] = new ModelRendererTurbo(this, 52, 24, textureX, textureY); // Box 2

		brakeModel[0].addBox(9F, -1F, -1F, 3, 2, 2, 0F); // Box 2
		brakeModel[0].rotateAngleX = -0.31415927F;
		brakeModel[0].rotateAngleY = 1.13446401F;
		brakeModel[0].rotateAngleZ = 0.06981317F;


		frontThingyUpperModel = new ModelRendererTurbo[13];
		frontThingyUpperModel[0] = new ModelRendererTurbo(this, 12, 13, textureX, textureY); // Box 2
		frontThingyUpperModel[1] = new ModelRendererTurbo(this, 0, 6, textureX, textureY); // Box 2
		frontThingyUpperModel[2] = new ModelRendererTurbo(this, 0, 6, textureX, textureY); // Box 2
		frontThingyUpperModel[3] = new ModelRendererTurbo(this, 104, 3, textureX, textureY); // Box 2
		frontThingyUpperModel[4] = new ModelRendererTurbo(this, 0, 12, textureX, textureY); // Box 2
		frontThingyUpperModel[5] = new ModelRendererTurbo(this, 6, 25, textureX, textureY); // Box 2
		frontThingyUpperModel[6] = new ModelRendererTurbo(this, 109, 67, textureX, textureY); // Box 2
		frontThingyUpperModel[7] = new ModelRendererTurbo(this, 77, 27, textureX, textureY); // Box 2
		frontThingyUpperModel[8] = new ModelRendererTurbo(this, 32, 0, textureX, textureY); // Box 2
		frontThingyUpperModel[9] = new ModelRendererTurbo(this, 120, 38, textureX, textureY); // Box 2
		frontThingyUpperModel[10] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 2
		frontThingyUpperModel[11] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 2
		frontThingyUpperModel[12] = new ModelRendererTurbo(this, 116, 21, textureX, textureY); // Box 2

		frontThingyUpperModel[0].addShapeBox(-2.01F, -5.8F, 8F, 4, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		frontThingyUpperModel[0].rotateAngleX = 0.03490659F;

		frontThingyUpperModel[1].addShapeBox(-4F, -7F, -1F, 2, 4, 2, 0F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F); // Box 2
		frontThingyUpperModel[1].rotateAngleX = -0.31415927F;

		frontThingyUpperModel[2].addShapeBox(2F, -7F, -1F, 2, 4, 2, 0F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F); // Box 2
		frontThingyUpperModel[2].rotateAngleX = -0.31415927F;

		frontThingyUpperModel[3].addBox(-4F, -11F, -1F, 2, 4, 2, 0F); // Box 2
		frontThingyUpperModel[3].rotateAngleX = -0.31415927F;

		frontThingyUpperModel[4].addBox(2F, -11F, -1F, 2, 4, 2, 0F); // Box 2
		frontThingyUpperModel[4].rotateAngleX = -0.31415927F;

		frontThingyUpperModel[5].addShapeBox(-2F, -2F, 8.5F, 4, 6, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		frontThingyUpperModel[5].rotateAngleX = 0.97738438F;

		frontThingyUpperModel[6].addShapeBox(-2F, -4F, 8F, 4, 8, 2, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0.75F, -0.25F, 0F, 0.75F, -0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		frontThingyUpperModel[6].rotateAngleX = 1.57079633F;

		frontThingyUpperModel[7].addShapeBox(-2F, -3.5F, 8.5F, 4, 6, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		frontThingyUpperModel[7].rotateAngleX = 2.25147474F;

		frontThingyUpperModel[8].addBox(-4F, -13F, -1F, 8, 2, 2, 0F); // Box 2
		frontThingyUpperModel[8].rotateAngleX = -0.31415927F;

		frontThingyUpperModel[9].addBox(-1F, -20F, -1F, 2, 7, 2, 0F); // Box 2
		frontThingyUpperModel[9].rotateAngleX = -0.31415927F;

		frontThingyUpperModel[10].addBox(-0.5F, -15F, -2F, 1, 1, 1, 0F); // Box 2
		frontThingyUpperModel[10].rotateAngleX = -0.31415927F;

		frontThingyUpperModel[11].addBox(-0.5F, -17F, -2F, 1, 1, 1, 0F); // Box 2
		frontThingyUpperModel[11].rotateAngleX = -0.31415927F;

		frontThingyUpperModel[12].addBox(-2.5F, -18F, -3F, 5, 6, 1, 0F); // Box 2
		frontThingyUpperModel[12].rotateAngleX = -0.31415927F;

		trailerThingyModel = new ModelRendererTurbo[8];
		trailerThingyModel[0] = new ModelRendererTurbo(this, 3, 71, textureX, textureY); // Box 90
		trailerThingyModel[1] = new ModelRendererTurbo(this, 3, 71, textureX, textureY); // Box 90
		trailerThingyModel[2] = new ModelRendererTurbo(this, 43, 79, textureX, textureY); // Box 90
		trailerThingyModel[3] = new ModelRendererTurbo(this, 17, 80, textureX, textureY); // Box 90
		trailerThingyModel[4] = new ModelRendererTurbo(this, 68, 18, textureX, textureY); // Box 90
		trailerThingyModel[5] = new ModelRendererTurbo(this, 117, 57, textureX, textureY); // Box 90
		trailerThingyModel[6] = new ModelRendererTurbo(this, 3, 80, textureX, textureY); // Box 90
		trailerThingyModel[7] = new ModelRendererTurbo(this, 3, 80, textureX, textureY); // Box 90

		trailerThingyModel[0].addBox(0F, 0F, 0F, 2, 1, 8, 0F); // Box 90
		trailerThingyModel[0].setRotationPoint(12F, -3F, 18F);

		trailerThingyModel[1].addBox(-2F, 0F, 0F, 2, 1, 8, 0F); // Box 90
		trailerThingyModel[1].setRotationPoint(14.01F, -3F, 26F);
		trailerThingyModel[1].rotateAngleX = -0.78539816F;

		trailerThingyModel[2].addBox(0F, 0F, 0F, 2, 1, 6, 0F); // Box 90
		trailerThingyModel[2].setRotationPoint(12F, 2F, 31F);

		trailerThingyModel[3].addBox(0F, 0F, 0F, 2, 1, 6, 0F); // Box 90
		trailerThingyModel[3].setRotationPoint(4F, 2F, 31F);

		trailerThingyModel[4].addBox(0F, 0F, 0F, 6, 1, 4, 0F); // Box 90
		trailerThingyModel[4].setRotationPoint(6F, 2F, 33F);

		trailerThingyModel[5].addBox(0F, 0F, 0F, 2, 6, 2, 0F); // Box 90
		trailerThingyModel[5].setRotationPoint(8F, -4F, 34F);

		trailerThingyModel[6].addBox(0F, 0F, 0F, 2, 1, 8, 0F); // Box 90
		trailerThingyModel[6].setRotationPoint(4F, -3F, 18F);

		trailerThingyModel[7].addBox(-2F, 0F, 0F, 2, 1, 8, 0F); // Box 90
		trailerThingyModel[7].setRotationPoint(6.01F, -3F, 26F);
		trailerThingyModel[7].rotateAngleX = -0.78539816F;


		accelerationModel = new ModelRendererTurbo[1];
		accelerationModel[0] = new ModelRendererTurbo(this, 52, 24, textureX, textureY); // Box 2

		accelerationModel[0].addBox(9F, -1F, -1F, 3, 2, 2, 0F); // Box 2
		accelerationModel[0].setRotationPoint(-6F, 0f, 0F);
		accelerationModel[0].rotateAngleX = 0.31415927F;
		accelerationModel[0].rotateAngleY = 2.00712864F;
		accelerationModel[0].rotateAngleZ = 0.06981317F;


		upgradeSeatModel = new ModelRendererTurbo[4];
		upgradeSeatModel[0] = new ModelRendererTurbo(this, 47, 68, textureX, textureY); // Box 1
		upgradeSeatModel[1] = new ModelRendererTurbo(this, 98, 41, textureX, textureY); // Box 1
		upgradeSeatModel[2] = new ModelRendererTurbo(this, 98, 41, textureX, textureY); // Box 1
		upgradeSeatModel[3] = new ModelRendererTurbo(this, 98, 41, textureX, textureY); // Box 1

		upgradeSeatModel[0].addBox(0F, 0F, 0F, 10, 3, 8, 0F); // Box 1
		upgradeSeatModel[0].setRotationPoint(4F, -14F, 15F);

		upgradeSeatModel[1].addBox(0F, 0F, 0F, 8, 1, 6, 0F); // Box 1
		upgradeSeatModel[1].setRotationPoint(5F, -11F, 16F);

		upgradeSeatModel[2].addBox(0F, 0F, 0F, 1, 2, 2, 0F); // Box 1
		upgradeSeatModel[2].setRotationPoint(5F, -10F, 18F);

		upgradeSeatModel[3].addBox(0F, 0F, 0F, 1, 2, 2, 0F); // Box 1
		upgradeSeatModel[3].setRotationPoint(12F, -10F, 18F);


		upgradeStorageModel = new ModelRendererTurbo[12];
		upgradeStorageModel[0] = new ModelRendererTurbo(this, 51, 79, textureX, textureY); // Box 1
		upgradeStorageModel[1] = new ModelRendererTurbo(this, 98, 41, textureX, textureY); // Box 1
		upgradeStorageModel[2] = new ModelRendererTurbo(this, 98, 41, textureX, textureY); // Box 1
		upgradeStorageModel[3] = new ModelRendererTurbo(this, 98, 41, textureX, textureY); // Box 1
		upgradeStorageModel[4] = new ModelRendererTurbo(this, 53, 79, textureX, textureY); // Box 1
		upgradeStorageModel[5] = new ModelRendererTurbo(this, 39, 86, textureX, textureY); // Box 1
		upgradeStorageModel[6] = new ModelRendererTurbo(this, 39, 86, textureX, textureY); // Box 1
		upgradeStorageModel[7] = new ModelRendererTurbo(this, 87, 79, textureX, textureY); // Box 1
		upgradeStorageModel[8] = new ModelRendererTurbo(this, 27, 81, textureX, textureY); // Box 1
		upgradeStorageModel[9] = new ModelRendererTurbo(this, 27, 81, textureX, textureY); // Box 1
		upgradeStorageModel[10] = new ModelRendererTurbo(this, 98, 41, textureX, textureY); // Box 1
		upgradeStorageModel[11] = new ModelRendererTurbo(this, 98, 41, textureX, textureY); // Box 1

		upgradeStorageModel[0].addBox(0F, 0F, 0F, 10, 6, 8, 0F); // Box 1
		upgradeStorageModel[0].setRotationPoint(4F, -17F, 19F);

		upgradeStorageModel[1].addBox(0F, 0F, 0F, 8, 1, 6, 0F); // Box 1
		upgradeStorageModel[1].setRotationPoint(5F, -11F, 20F);

		upgradeStorageModel[2].addBox(0F, 0F, 0F, 1, 2, 2, 0F); // Box 1
		upgradeStorageModel[2].setRotationPoint(5F, -10F, 22F);

		upgradeStorageModel[3].addBox(0F, 0F, 0F, 1, 2, 2, 0F); // Box 1
		upgradeStorageModel[3].setRotationPoint(12F, -10F, 22F);

		upgradeStorageModel[4].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // Box 1
		upgradeStorageModel[4].setRotationPoint(8F, -16F, 27F);

		upgradeStorageModel[5].addBox(0F, 0F, 0F, 2, 8, 4, 0F); // Box 1
		upgradeStorageModel[5].setRotationPoint(2F, -18F, 21F);

		upgradeStorageModel[6].addBox(0F, 0F, 0F, 2, 8, 4, 0F); // Box 1
		upgradeStorageModel[6].setRotationPoint(14F, -18F, 21F);

		upgradeStorageModel[7].addBox(0F, 0F, 0F, 12, 5, 5, 0F); // Box 1
		upgradeStorageModel[7].setRotationPoint(3F, -16F, 13.5F);

		upgradeStorageModel[8].addBox(0F, 0F, 0F, 1, 1, 6, 0F); // Box 1
		upgradeStorageModel[8].setRotationPoint(5F, -11F, 14F);

		upgradeStorageModel[9].addBox(0F, 0F, 0F, 1, 1, 6, 0F); // Box 1
		upgradeStorageModel[9].setRotationPoint(12F, -11F, 14F);

		upgradeStorageModel[10].addBox(0F, 0F, -4F, 1, 2, 2, 0F); // Box 1
		upgradeStorageModel[10].setRotationPoint(5F, -10F, 22F);

		upgradeStorageModel[11].addBox(0F, 0F, -4F, 1, 2, 2, 0F); // Box 1
		upgradeStorageModel[11].setRotationPoint(12F, -10F, 22F);


		upgradeTankModel = new ModelRendererTurbo[12];
		upgradeTankModel[0] = new ModelRendererTurbo(this, 71, 89, textureX, textureY); // Box 1
		upgradeTankModel[1] = new ModelRendererTurbo(this, 98, 41, textureX, textureY); // Box 1
		upgradeTankModel[2] = new ModelRendererTurbo(this, 98, 41, textureX, textureY); // Box 1
		upgradeTankModel[3] = new ModelRendererTurbo(this, 98, 41, textureX, textureY); // Box 1
		upgradeTankModel[4] = new ModelRendererTurbo(this, 27, 81, textureX, textureY); // Box 1
		upgradeTankModel[5] = new ModelRendererTurbo(this, 27, 81, textureX, textureY); // Box 1
		upgradeTankModel[6] = new ModelRendererTurbo(this, 98, 41, textureX, textureY); // Box 1
		upgradeTankModel[7] = new ModelRendererTurbo(this, 98, 41, textureX, textureY); // Box 1
		upgradeTankModel[8] = new ModelRendererTurbo(this, 58, 93, textureX, textureY); // Box 2
		upgradeTankModel[9] = new ModelRendererTurbo(this, 78, 96, textureX, textureY); // Box 1
		upgradeTankModel[10] = new ModelRendererTurbo(this, 78, 99, textureX, textureY); // Box 1
		upgradeTankModel[11] = new ModelRendererTurbo(this, 65, 94, textureX, textureY); // Box 1

		upgradeTankModel[0].addBox(0F, 0F, 0F, 10, 10, 16, 0F); // Box 1
		upgradeTankModel[0].setRotationPoint(4F, -21F, 13F);

		upgradeTankModel[1].addBox(0F, 0F, 0F, 8, 1, 6, 0F); // Box 1
		upgradeTankModel[1].setRotationPoint(5F, -11F, 20F);

		upgradeTankModel[2].addBox(0F, 0F, 0F, 1, 2, 2, 0F); // Box 1
		upgradeTankModel[2].setRotationPoint(5F, -10F, 22F);

		upgradeTankModel[3].addBox(0F, 0F, 0F, 1, 2, 2, 0F); // Box 1
		upgradeTankModel[3].setRotationPoint(12F, -10F, 22F);

		upgradeTankModel[4].addBox(0F, 0F, 0F, 1, 1, 6, 0F); // Box 1
		upgradeTankModel[4].setRotationPoint(5F, -11F, 14F);

		upgradeTankModel[5].addBox(0F, 0F, 0F, 1, 1, 6, 0F); // Box 1
		upgradeTankModel[5].setRotationPoint(12F, -11F, 14F);

		upgradeTankModel[6].addBox(0F, 0F, -4F, 1, 2, 2, 0F); // Box 1
		upgradeTankModel[6].setRotationPoint(5F, -10F, 22F);

		upgradeTankModel[7].addBox(0F, 0F, -4F, 1, 2, 2, 0F); // Box 1
		upgradeTankModel[7].setRotationPoint(12F, -10F, 22F);

		upgradeTankModel[8].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // Box 2
		upgradeTankModel[8].setRotationPoint(7F, -22F, 23F);

		upgradeTankModel[9].addBox(0F, 0F, 0F, 2, 1, 2, 0F); // Box 1
		upgradeTankModel[9].setRotationPoint(8F, -11F, 13F);

		upgradeTankModel[10].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // Box 1
		upgradeTankModel[10].setRotationPoint(8F, -10F, 13F);

		upgradeTankModel[11].addBox(0F, 0F, 0F, 2, 2, 9, 0F); // Box 1
		upgradeTankModel[11].setRotationPoint(8F, -10F, 4F);

		parts.put("base", baseModel);
		parts.put("steeringGear", steeringGearModel);
		parts.put("acceleration", accelerationModel);
		parts.put("brake", brakeModel);
		parts.put("backWheel", backWheelModel);
		parts.put("frontWheel", frontWheelModel);
		parts.put("engine", engineModel);
		parts.put("exhaustPipes", exhaustPipesModel);
		parts.put("frontThingy", frontThingyModel);
		parts.put("frontThingyUpper", frontThingyUpperModel);
		parts.put("trailerThingy", trailerThingyModel);

		parts.put("upgradeSeat", upgradeSeatModel);
		parts.put("upgradeStorage", upgradeStorageModel);
		parts.put("upgradeTank", upgradeTankModel);


		flipAll();
	}
}
