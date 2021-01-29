package pl.pabilo8.immersiveintelligence.client.model.misc;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 15-06-2019
 */
public class ModelSandbagsStraight extends ModelIIBase
{
	public ModelRendererTurbo[] reiforcementWood, reinforcementPlates, reinforcementWireMesh, rightFullModel, leftFullModel, rightDotModel, leftDotModel;

	int textureX = 64;
	int textureY = 32;

	public ModelSandbagsStraight() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[12];
		baseModel[0] = new ModelRendererTurbo(this, 20, 4, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[2] = new ModelRendererTurbo(this, 40, 0, textureX, textureY); // Box 0
		baseModel[3] = new ModelRendererTurbo(this, 40, 0, textureX, textureY); // Box 0
		baseModel[4] = new ModelRendererTurbo(this, 20, 4, textureX, textureY); // Box 0
		baseModel[5] = new ModelRendererTurbo(this, 20, 4, textureX, textureY); // Box 0
		baseModel[6] = new ModelRendererTurbo(this, 40, 0, textureX, textureY); // Box 0
		baseModel[7] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[8] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[9] = new ModelRendererTurbo(this, 20, 4, textureX, textureY); // Box 0
		baseModel[10] = new ModelRendererTurbo(this, 20, 4, textureX, textureY); // Box 0
		baseModel[11] = new ModelRendererTurbo(this, 40, 0, textureX, textureY); // Box 0

		baseModel[0].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, -4F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[1].setRotationPoint(8F, -4F, 1F);

		baseModel[2].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[2].setRotationPoint(4F, -8F, 1F);

		baseModel[3].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[3].setRotationPoint(0F, -4F, 4F);

		baseModel[4].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[4].setRotationPoint(8F, -4F, 5F);

		baseModel[5].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[5].setRotationPoint(4F, -8F, 5F);

		baseModel[6].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[6].setRotationPoint(0F, -12F, 0F);

		baseModel[7].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[7].setRotationPoint(8F, -12F, 1F);

		baseModel[8].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[8].setRotationPoint(0F, -12F, 4F);

		baseModel[9].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[9].setRotationPoint(8F, -12F, 5F);

		baseModel[10].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[10].setRotationPoint(4F, -16F, 0F);

		baseModel[11].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[11].setRotationPoint(4F, -16F, 4F);


		reiforcementWood = new ModelRendererTurbo[3];
		reiforcementWood[0] = new ModelRendererTurbo(this, 24, 14, textureX, textureY); // Box 16
		reiforcementWood[1] = new ModelRendererTurbo(this, 40, 14, textureX, textureY); // Box 16
		reiforcementWood[2] = new ModelRendererTurbo(this, 32, 14, textureX, textureY); // Box 16

		reiforcementWood[0].addBox(0F, 0F, 0F, 2, 16, 2, 0F); // Box 16
		reiforcementWood[0].setRotationPoint(2F, -16F, -2F);
		reiforcementWood[0].rotateAngleX = 0.08726646F;

		reiforcementWood[1].addBox(0F, 0F, 0F, 2, 16, 2, 0F); // Box 16
		reiforcementWood[1].setRotationPoint(7F, -16F, -2F);
		reiforcementWood[1].rotateAngleX = 0.12217305F;

		reiforcementWood[2].addBox(0F, 0F, 0F, 2, 16, 2, 0F); // Box 16
		reiforcementWood[2].setRotationPoint(12F, -16F, -2F);
		reiforcementWood[2].rotateAngleX = 0.08726646F;


		reinforcementPlates = new ModelRendererTurbo[4];
		reinforcementPlates[0] = new ModelRendererTurbo(this, 6, 23, textureX, textureY); // Box 19
		reinforcementPlates[1] = new ModelRendererTurbo(this, 6, 14, textureX, textureY); // Box 19
		reinforcementPlates[2] = new ModelRendererTurbo(this, 6, 14, textureX, textureY); // Box 19
		reinforcementPlates[3] = new ModelRendererTurbo(this, 6, 23, textureX, textureY); // Box 19

		reinforcementPlates[0].addBox(0F, 0F, 0F, 8, 8, 1, 0F); // Box 19
		reinforcementPlates[0].setRotationPoint(1F, -15F, -2F);
		reinforcementPlates[0].rotateAngleX = 0.05235988F;
		reinforcementPlates[0].rotateAngleZ = 0.06981317F;

		reinforcementPlates[1].addBox(0F, -1F, 0F, 8, 8, 1, 0F); // Box 19
		reinforcementPlates[1].setRotationPoint(1F, -8F, -2F);
		reinforcementPlates[1].rotateAngleX = 0.15707963F;
		reinforcementPlates[1].rotateAngleZ = -0.15707963F;

		reinforcementPlates[2].addBox(0F, 0F, 0F, 8, 8, 1, 0F); // Box 19
		reinforcementPlates[2].setRotationPoint(9F, -15F, -2F);
		reinforcementPlates[2].rotateAngleX = -0.03490659F;
		reinforcementPlates[2].rotateAngleZ = -0.19198622F;

		reinforcementPlates[3].addBox(0F, 0F, -1F, 8, 8, 1, 0F); // Box 19
		reinforcementPlates[3].setRotationPoint(9F, -8F, -2F);
		reinforcementPlates[3].rotateAngleX = 0.2443461F;


		reinforcementWireMesh = new ModelRendererTurbo[6];
		reinforcementWireMesh[0] = new ModelRendererTurbo(this, 48, 8, textureX, textureY); // Box 23
		reinforcementWireMesh[1] = new ModelRendererTurbo(this, 48, 8, textureX, textureY); // Box 23
		reinforcementWireMesh[2] = new ModelRendererTurbo(this, 36, 12, textureX, textureY); // Box 23
		reinforcementWireMesh[3] = new ModelRendererTurbo(this, 30, 12, textureX, textureY); // Box 23
		reinforcementWireMesh[4] = new ModelRendererTurbo(this, 24, 12, textureX, textureY); // Box 23
		reinforcementWireMesh[5] = new ModelRendererTurbo(this, 42, 12, textureX, textureY); // Box 23

		reinforcementWireMesh[0].addBox(0F, 0F, 0F, 8, 16, 0, 0F); // Box 23
		reinforcementWireMesh[0].setRotationPoint(0F, -16F, -2F);
		reinforcementWireMesh[0].rotateAngleX = -0.12217305F;

		reinforcementWireMesh[1].addBox(0F, 0F, 0F, 8, 16, 0, 0F); // Box 23
		reinforcementWireMesh[1].setRotationPoint(8F, -16F, -2F);
		reinforcementWireMesh[1].rotateAngleX = -0.12217305F;

		reinforcementWireMesh[2].addBox(0F, -1F, 0F, 2, 1, 1, 0F); // Box 23
		reinforcementWireMesh[2].setRotationPoint(8F, -16F, -2F);
		reinforcementWireMesh[2].rotateAngleX = -0.12217305F;

		reinforcementWireMesh[3].addBox(0F, -1F, -0.25F, 2, 1, 1, 0F); // Box 23
		reinforcementWireMesh[3].setRotationPoint(12F, -16F, -2F);
		reinforcementWireMesh[3].rotateAngleX = -0.12217305F;
		reinforcementWireMesh[3].rotateAngleY = 0.08726646F;

		reinforcementWireMesh[4].addBox(0F, -1F, 0F, 2, 1, 1, 0F); // Box 23
		reinforcementWireMesh[4].setRotationPoint(2F, -16F, -2F);
		reinforcementWireMesh[4].rotateAngleX = -0.12217305F;
		reinforcementWireMesh[4].rotateAngleY = -0.08726646F;

		reinforcementWireMesh[5].addBox(0F, -1F, 0F, 2, 1, 1, 0F); // Box 23
		reinforcementWireMesh[5].setRotationPoint(6F, -16F, -2F);
		reinforcementWireMesh[5].rotateAngleX = -0.12217305F;


		leftFullModel = new ModelRendererTurbo[4];
		leftFullModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		leftFullModel[1] = new ModelRendererTurbo(this, 20, 4, textureX, textureY); // Box 0
		leftFullModel[2] = new ModelRendererTurbo(this, 20, 4, textureX, textureY); // Box 0
		leftFullModel[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0

		leftFullModel[0].addBox(0F, 0F, 1F, 8, 4, 4, 0F); // Box 0
		leftFullModel[0].setRotationPoint(12F, -8F, 4F);

		leftFullModel[1].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		leftFullModel[1].setRotationPoint(12F, -8F, 0F);

		leftFullModel[2].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		leftFullModel[2].setRotationPoint(12F, -16F, 4F);

		leftFullModel[3].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		leftFullModel[3].setRotationPoint(12F, -16F, 0F);


		rightFullModel = new ModelRendererTurbo[4];
		rightFullModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		rightFullModel[1] = new ModelRendererTurbo(this, 20, 4, textureX, textureY); // Box 0
		rightFullModel[2] = new ModelRendererTurbo(this, 20, 4, textureX, textureY); // Box 0
		rightFullModel[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0

		rightFullModel[0].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		rightFullModel[0].setRotationPoint(-4F, -8F, 4F);

		rightFullModel[1].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		rightFullModel[1].setRotationPoint(-4F, -8F, 0F);

		rightFullModel[2].addBox(0F, 0F, 1F, 8, 4, 4, 0F); // Box 0
		rightFullModel[2].setRotationPoint(-4F, -16F, 4F);

		rightFullModel[3].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		rightFullModel[3].setRotationPoint(-4F, -16F, 0F);


		leftDotModel = new ModelRendererTurbo[2];
		leftDotModel[0] = new ModelRendererTurbo(this, 20, 4, textureX, textureY); // Box 0
		leftDotModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0

		leftDotModel[0].addBox(-8.5F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		leftDotModel[0].setRotationPoint(12F, -8F, 0F);
		leftDotModel[0].rotateAngleY = -1.58824962F;

		leftDotModel[1].addBox(-8.25F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		leftDotModel[1].setRotationPoint(12F, -16F, 0F);
		leftDotModel[1].rotateAngleY = -1.6406095F;


		rightDotModel = new ModelRendererTurbo[2];
		rightDotModel[0] = new ModelRendererTurbo(this, 40, 0, textureX, textureY); // Box 0
		rightDotModel[1] = new ModelRendererTurbo(this, 20, 4, textureX, textureY); // Box 0

		rightDotModel[0].addBox(-8.5F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		rightDotModel[0].setRotationPoint(0F, -8F, 0F);
		rightDotModel[0].rotateAngleY = -1.58824962F;

		rightDotModel[1].addBox(-8.25F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		rightDotModel[1].setRotationPoint(0F, -16F, 0F);
		rightDotModel[1].rotateAngleY = -1.6406095F;

		parts.put("base", baseModel);
		parts.put("reinfWood", reiforcementWood);
		parts.put("reinfPlates", reinforcementPlates);
		parts.put("reinfWireMesh", reinforcementWireMesh);
		parts.put("rightFull", rightFullModel);
		parts.put("leftFull", leftFullModel);
		parts.put("rightDot", rightDotModel);
		parts.put("leftDot", leftDotModel);
		flipAll();
	}

}
