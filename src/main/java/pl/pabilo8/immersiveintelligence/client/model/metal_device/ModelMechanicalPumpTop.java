package pl.pabilo8.immersiveintelligence.client.model.metal_device;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 03.10.2020
 */
public class ModelMechanicalPumpTop extends ModelIIBase
{
	int textureX = 64;
	int textureY = 64;

	public ModelRendererTurbo[] axleModel, pullThingy1Model, pullThingy2Model, pullThingy3Model, pullThingy4Model;

	public ModelMechanicalPumpTop() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[19];
		baseModel[0] = new ModelRendererTurbo(this, 0, 46, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 40, 34, textureX, textureY); // Box 2
		baseModel[2] = new ModelRendererTurbo(this, 27, 8, textureX, textureY); // Box 2
		baseModel[3] = new ModelRendererTurbo(this, 0, 50, textureX, textureY); // Box 2
		baseModel[4] = new ModelRendererTurbo(this, 40, 34, textureX, textureY); // Box 2
		baseModel[5] = new ModelRendererTurbo(this, 27, 8, textureX, textureY); // Box 2
		baseModel[6] = new ModelRendererTurbo(this, 0, 50, textureX, textureY); // Box 2
		baseModel[7] = new ModelRendererTurbo(this, 40, 34, textureX, textureY); // Box 2
		baseModel[8] = new ModelRendererTurbo(this, 27, 8, textureX, textureY); // Box 2
		baseModel[9] = new ModelRendererTurbo(this, 0, 50, textureX, textureY); // Box 2
		baseModel[10] = new ModelRendererTurbo(this, 40, 34, textureX, textureY); // Box 2
		baseModel[11] = new ModelRendererTurbo(this, 27, 8, textureX, textureY); // Box 2
		baseModel[12] = new ModelRendererTurbo(this, 0, 50, textureX, textureY); // Box 2
		baseModel[13] = new ModelRendererTurbo(this, 0, 29, textureX, textureY); // Box 0
		baseModel[14] = new ModelRendererTurbo(this, 0, 29, textureX, textureY); // Box 0
		baseModel[15] = new ModelRendererTurbo(this, 46, 0, textureX, textureY); // Box 0
		baseModel[16] = new ModelRendererTurbo(this, 0, 8, textureX, textureY); // Box 0
		baseModel[17] = new ModelRendererTurbo(this, 27, 8, textureX, textureY); // Box 0
		baseModel[18] = new ModelRendererTurbo(this, 27, 16, textureX, textureY); // Box 0

		baseModel[0].addShapeBox(0F, 0F, 0F, 16, 2, 16, 0F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, -2F, 0F);

		baseModel[1].addBox(-1F, -4F, -3F, 6, 6, 6, 0F); // Box 2
		baseModel[1].setRotationPoint(0F, -1F, 0F);
		baseModel[1].rotateAngleY = 0.78539816F;
		baseModel[1].rotateAngleZ = 0.26179939F;

		baseModel[2].addBox(-1F, -14F, -3F, 6, 2, 6, 0F); // Box 2
		baseModel[2].setRotationPoint(0F, -1F, 0F);
		baseModel[2].rotateAngleY = 0.78539816F;
		baseModel[2].rotateAngleZ = 0.26179939F;

		baseModel[3].addBox(0F, -12F, -2F, 4, 8, 4, 0F); // Box 2
		baseModel[3].setRotationPoint(0F, -1F, 0F);
		baseModel[3].rotateAngleY = 0.78539816F;
		baseModel[3].rotateAngleZ = 0.26179939F;

		baseModel[4].addBox(-1F, -4F, -3F, 6, 6, 6, 0F); // Box 2
		baseModel[4].setRotationPoint(0F, -1F, 16F);
		baseModel[4].rotateAngleY = -0.78539816F;
		baseModel[4].rotateAngleZ = 0.26179939F;

		baseModel[5].addBox(-1F, -14F, -3F, 6, 2, 6, 0F); // Box 2
		baseModel[5].setRotationPoint(0F, -1F, 16F);
		baseModel[5].rotateAngleY = -0.78539816F;
		baseModel[5].rotateAngleZ = 0.26179939F;

		baseModel[6].addBox(0F, -12F, -2F, 4, 8, 4, 0F); // Box 2
		baseModel[6].setRotationPoint(0F, -1F, 16F);
		baseModel[6].rotateAngleY = -0.78539816F;
		baseModel[6].rotateAngleZ = 0.26179939F;

		baseModel[7].addBox(0F, -4F, -3F, 6, 6, 6, 0F); // Box 2
		baseModel[7].setRotationPoint(16F, -1F, 17F);
		baseModel[7].rotateAngleY = -2.35619449F;
		baseModel[7].rotateAngleZ = 0.26179939F;

		baseModel[8].addBox(0F, -14F, -3F, 6, 2, 6, 0F); // Box 2
		baseModel[8].setRotationPoint(16F, -1F, 17F);
		baseModel[8].rotateAngleY = -2.35619449F;
		baseModel[8].rotateAngleZ = 0.26179939F;

		baseModel[9].addBox(1F, -12F, -2F, 4, 8, 4, 0F); // Box 2
		baseModel[9].setRotationPoint(16F, -1F, 17F);
		baseModel[9].rotateAngleY = -2.35619449F;
		baseModel[9].rotateAngleZ = 0.26179939F;

		baseModel[10].addBox(-2F, -4F, -3F, 6, 6, 6, 0F); // Box 2
		baseModel[10].setRotationPoint(15F, -1F, 1F);
		baseModel[10].rotateAngleY = -3.92699082F;
		baseModel[10].rotateAngleZ = 0.26179939F;

		baseModel[11].addBox(-2F, -14F, -3F, 6, 2, 6, 0F); // Box 2
		baseModel[11].setRotationPoint(15F, -1F, 1F);
		baseModel[11].rotateAngleY = -3.92699082F;
		baseModel[11].rotateAngleZ = 0.26179939F;

		baseModel[12].addBox(-1F, -12F, -2F, 4, 8, 4, 0F); // Box 2
		baseModel[12].setRotationPoint(15F, -1F, 1F);
		baseModel[12].rotateAngleY = -3.92699082F;
		baseModel[12].rotateAngleZ = 0.26179939F;

		baseModel[13].addShapeBox(0F, 0F, 0F, 10, 1, 10, 0F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[13].setRotationPoint(3F, -10F, 3F);

		baseModel[14].addBox(0F, 0F, 0F, 10, 7, 10, 0F); // Box 0
		baseModel[14].setRotationPoint(3F, -9F, 3F);

		baseModel[15].addBox(0F, 0F, 0F, 8, 8, 1, 0F); // Box 0
		baseModel[15].setRotationPoint(4F, -12F, 0F);

		baseModel[16].addBox(0F, 0F, 0F, 6, 6, 15, 0F); // Box 0
		baseModel[16].setRotationPoint(5F, -11F, 1F);

		baseModel[17].addBox(0F, 0F, 0F, 6, 2, 6, 0F); // Box 0
		baseModel[17].setRotationPoint(5F, -13F, 5F);

		baseModel[18].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // Box 0
		baseModel[18].setRotationPoint(5F, -16F, 5F);


		axleModel = new ModelRendererTurbo[1];
		axleModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0

		axleModel[0].addBox(-2F, 0F, -2F, 4, 2, 4, 0F); // Box 0
		axleModel[0].hasOffset = true;
		axleModel[0].setRotationPoint(8F, -15F, 8F);

		pullThingy1Model = new ModelRendererTurbo[1];
		pullThingy1Model[0] = new ModelRendererTurbo(this, 36, 26, textureX, textureY); // Box 2

		pullThingy1Model[0].addBox(-1F, -12F, -3F, 6, 2, 6, 0F); // Box 2
		pullThingy1Model[0].setRotationPoint(0F, -1F, 0F);
		pullThingy1Model[0].rotateAngleY = 0.78539816F;
		pullThingy1Model[0].rotateAngleZ = 0.26179939F;
		pullThingy1Model[0].hasOffset = true;

		pullThingy2Model = new ModelRendererTurbo[1];
		pullThingy2Model[0] = new ModelRendererTurbo(this, 36, 26, textureX, textureY); // Box 2

		pullThingy2Model[0].addBox(-1F, -12F, -3F, 6, 2, 6, 0F); // Box 2
		pullThingy2Model[0].setRotationPoint(0F, -1F, 16F);
		pullThingy2Model[0].rotateAngleY = -0.78539816F;
		pullThingy2Model[0].rotateAngleZ = 0.26179939F;
		pullThingy2Model[0].hasOffset = true;

		pullThingy3Model = new ModelRendererTurbo[1];
		pullThingy3Model[0] = new ModelRendererTurbo(this, 36, 26, textureX, textureY); // Box 2

		pullThingy3Model[0].addBox(0F, -12F, -3F, 6, 2, 6, 0F); // Box 2
		pullThingy3Model[0].setRotationPoint(16F, -1F, 17F);
		pullThingy3Model[0].rotateAngleY = -2.35619449F;
		pullThingy3Model[0].rotateAngleZ = 0.26179939F;
		pullThingy3Model[0].hasOffset = true;


		pullThingy4Model = new ModelRendererTurbo[1];
		pullThingy4Model[0] = new ModelRendererTurbo(this, 36, 26, textureX, textureY); // Box 2

		pullThingy4Model[0].addBox(-2F, -12F, -3F, 6, 2, 6, 0F); // Box 2
		pullThingy4Model[0].setRotationPoint(15F, -1F, 1F);
		pullThingy4Model[0].rotateAngleY = -3.92699082F;
		pullThingy4Model[0].rotateAngleZ = 0.26179939F;
		pullThingy4Model[0].hasOffset = true;

		parts.put("base", baseModel);
		parts.put("axle", axleModel);
		parts.put("pullthingy1", pullThingy1Model);
		parts.put("pullthingy2", pullThingy2Model);
		parts.put("pullthingy3", pullThingy3Model);
		parts.put("pullthingy4", pullThingy4Model);

		flipAll();
	}
}
