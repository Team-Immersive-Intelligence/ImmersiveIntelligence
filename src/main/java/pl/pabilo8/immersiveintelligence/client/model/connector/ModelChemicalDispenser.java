package pl.pabilo8.immersiveintelligence.client.model.connector;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * Created by Pabilo8 on 2019-06-01.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */

public class ModelChemicalDispenser extends ModelIIBase
{
	int textureX = 64;
	int textureY = 128;
	public ModelRendererTurbo[] barrelModel;

	public ModelChemicalDispenser() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[14];
		baseModel[0] = new ModelRendererTurbo(this, 24, 59, textureX, textureY); // FluidInput
		baseModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // MainBox
		baseModel[2] = new ModelRendererTurbo(this, 0, 21, textureX, textureY); // TopBox
		baseModel[3] = new ModelRendererTurbo(this, 24, 59, textureX, textureY); // OutputBox
		baseModel[4] = new ModelRendererTurbo(this, 0, 39, textureX, textureY); // TankLeft
		baseModel[5] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // TankRightLeg
		baseModel[6] = new ModelRendererTurbo(this, 0, 59, textureX, textureY); // TankRight
		baseModel[7] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // TankRightLeg
		baseModel[8] = new ModelRendererTurbo(this, 4, 0, textureX, textureY); // TankRightLeg
		baseModel[9] = new ModelRendererTurbo(this, 0, 2, textureX, textureY); // TankRightLeg
		baseModel[10] = new ModelRendererTurbo(this, 4, 2, textureX, textureY); // TankLeftLeg
		baseModel[11] = new ModelRendererTurbo(this, 4, 2, textureX, textureY); // TankLeftLeg
		baseModel[12] = new ModelRendererTurbo(this, 0, 2, textureX, textureY); // TankLeftLeg
		baseModel[13] = new ModelRendererTurbo(this, 4, 0, textureX, textureY); // TankLeftLeg

		baseModel[0].addBox(-4F, 0F, -4F, 8, 1, 8, 0F); // FluidInput
		baseModel[0].setRotationPoint(8F, -1F, 8F);

		baseModel[1].addBox(-8F, 0F, -8F, 16, 5, 16, 0F); // MainBox
		baseModel[1].setRotationPoint(8F, -6F, 8F);

		baseModel[2].addTrapezoid(-8F, 0F, -8F, 16, 2, 16, 0F, -2.00F, ModelRendererTurbo.MR_TOP); // TopBox
		baseModel[2].setRotationPoint(8F, -8F, 8F);

		baseModel[3].addBox(-4F, 0F, -4F, 8, 1, 8, 0F); // OutputBox
		baseModel[3].setRotationPoint(8F, -9F, 8F);

		baseModel[4].addBox(9F, 0F, -8F, 4, 4, 16, 0F); // TankLeft
		baseModel[4].setRotationPoint(8F, -5.5F, 8F);

		baseModel[5].addBox(-9F, 0F, -7F, 1, 1, 1, 0F); // TankRightLeg
		baseModel[5].setRotationPoint(8F, -5F, 8F);

		baseModel[6].addBox(-13F, 0F, -8F, 4, 4, 16, 0F); // TankRight
		baseModel[6].setRotationPoint(8F, -5.5F, 8F);

		baseModel[7].addBox(-9F, 0F, -7F, 1, 1, 1, 0F); // TankRightLeg
		baseModel[7].setRotationPoint(8F, -3F, 8F);

		baseModel[8].addBox(-9F, 0F, -7F, 1, 1, 1, 0F); // TankRightLeg
		baseModel[8].setRotationPoint(8F, -5F, 21F);

		baseModel[9].addBox(-9F, 0F, -7F, 1, 1, 1, 0F); // TankRightLeg
		baseModel[9].setRotationPoint(8F, -3F, 21F);

		baseModel[10].addBox(8F, 0F, -7F, 1, 1, 1, 0F); // TankLeftLeg
		baseModel[10].setRotationPoint(8F, -5F, 8F);

		baseModel[11].addBox(8F, 0F, -7F, 1, 1, 1, 0F); // TankLeftLeg
		baseModel[11].setRotationPoint(8F, -3F, 8F);

		baseModel[12].addBox(8F, 0F, -7F, 1, 1, 1, 0F); // TankLeftLeg
		baseModel[12].setRotationPoint(8F, -5F, 21F);

		baseModel[13].addBox(8F, 0F, -7F, 1, 1, 1, 0F); // TankLeftLeg
		baseModel[13].setRotationPoint(8F, -3F, 21F);


		barrelModel = new ModelRendererTurbo[5];
		barrelModel[0] = new ModelRendererTurbo(this, 48, 21, textureX, textureY); // OutputPlate
		barrelModel[1] = new ModelRendererTurbo(this, 48, 0, textureX, textureY); // OutputPlateTop
		barrelModel[2] = new ModelRendererTurbo(this, 48, 5, textureX, textureY); // OutputRod
		barrelModel[3] = new ModelRendererTurbo(this, 0, 21, textureX, textureY); // End
		barrelModel[4] = new ModelRendererTurbo(this, 40, 48, textureX, textureY); // EndTop

		barrelModel[0].addBox(-2F, -1F, -2F, 4, 1, 4, 0F); // OutputPlate

		barrelModel[1].addTrapezoid(-2F, -2F, -2F, 4, 1, 4, 0F, -1.00F, ModelRendererTurbo.MR_TOP); // OutputPlateTop

		barrelModel[2].addBox(-1F, -6F, -1F, 2, 5, 2, 0F); // OutputRod

		barrelModel[3].addShapeBox(-2F, -11F, -2F, 4, 5, 4, 0F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, -0.25F, -0.5F, -0.5F, -0.25F, -0.5F, -0.5F, -0.25F, -0.5F, -0.5F, -0.25F, -0.5F); // End

		barrelModel[4].addBox(-2.5F, -12F, -2.5F, 5, 6, 5, 0F); // EndTop
		barrelModel[4].flip = true;

		parts.put("base", baseModel);
		parts.put("barrel", barrelModel);

		flipAll();
	}
}