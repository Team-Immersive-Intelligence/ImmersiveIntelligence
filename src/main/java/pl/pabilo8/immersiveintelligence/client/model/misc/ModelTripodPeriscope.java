package pl.pabilo8.immersiveintelligence.client.model.misc;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 21.01.2021
 */
public class ModelTripodPeriscope extends ModelIIBase
{
	int textureX = 32;
	int textureY = 32;

	public ModelRendererTurbo[] periscopeModel, leverModel, leg1Model, leg2Model, leg3Model;

	public ModelTripodPeriscope() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[4];
		baseModel[0] = new ModelRendererTurbo(this, 22, 8, textureX, textureY); // Tripod02
		baseModel[1] = new ModelRendererTurbo(this, 22, 11, textureX, textureY); // Tripod03
		baseModel[2] = new ModelRendererTurbo(this, 22, 11, textureX, textureY); // Tripod04
		baseModel[3] = new ModelRendererTurbo(this, 28, 15, textureX, textureY); // Tripod05

		baseModel[0].addShapeBox(0F, 0F, 0F, 3, 1, 2, 0F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F); // Tripod02
		baseModel[0].setRotationPoint(-1F, -21.01F, -2F);

		baseModel[1].addShapeBox(0F, 0F, 0F, 3, 1, 2, 0F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F); // Tripod03
		baseModel[1].setRotationPoint(0F, -21.02F, 1F);
		baseModel[1].rotateAngleY = -0.78539816F;

		baseModel[2].addShapeBox(0F, 0F, 0F, 3, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod04
		baseModel[2].setRotationPoint(-1F, -21F, -1F);
		baseModel[2].rotateAngleY = 0.78539816F;

		baseModel[3].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod05
		baseModel[3].setRotationPoint(0F, -22F, 0F);


		periscopeModel = new ModelRendererTurbo[19];
		periscopeModel[0] = new ModelRendererTurbo(this, 24, 1, textureX, textureY); // Box 16
		periscopeModel[1] = new ModelRendererTurbo(this, 11, 18, textureX, textureY); // Box 17
		periscopeModel[2] = new ModelRendererTurbo(this, 26, 5, textureX, textureY); // Box 18
		periscopeModel[3] = new ModelRendererTurbo(this, 12, 11, textureX, textureY); // Box 21
		periscopeModel[4] = new ModelRendererTurbo(this, 0, 24, textureX, textureY); // Box 23
		periscopeModel[5] = new ModelRendererTurbo(this, 0, 24, textureX, textureY); // Box 24
		periscopeModel[6] = new ModelRendererTurbo(this, 7, 1, textureX, textureY); // Box 25
		periscopeModel[7] = new ModelRendererTurbo(this, 12, 8, textureX, textureY); // Box 27
		periscopeModel[8] = new ModelRendererTurbo(this, 12, 18, textureX, textureY); // Box 28
		periscopeModel[9] = new ModelRendererTurbo(this, 12, 18, textureX, textureY); // Box 29
		periscopeModel[10] = new ModelRendererTurbo(this, 12, 22, textureX, textureY); // Box 30
		periscopeModel[11] = new ModelRendererTurbo(this, 12, 22, textureX, textureY); // Box 31
		periscopeModel[12] = new ModelRendererTurbo(this, 4, 10, textureX, textureY); // Box 33
		periscopeModel[13] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 34
		periscopeModel[14] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 35
		periscopeModel[15] = new ModelRendererTurbo(this, 15, 2, textureX, textureY); // Box 36
		periscopeModel[16] = new ModelRendererTurbo(this, 7, 1, textureX, textureY); // Box 25
		periscopeModel[17] = new ModelRendererTurbo(this, 4, 6, textureX, textureY); // Box 35
		periscopeModel[18] = new ModelRendererTurbo(this, 4, 6, textureX, textureY); // Box 35

		periscopeModel[0].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		periscopeModel[0].setRotationPoint(-0.5F, -25.5F, -0.5F);

		periscopeModel[1].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 17
		periscopeModel[1].setRotationPoint(0F, -24F, 0F);

		periscopeModel[2].addShapeBox(0F, 0F, 0F, 1, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 18
		periscopeModel[2].setRotationPoint(0F, -23F, 1F);

		periscopeModel[3].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F); // Box 21
		periscopeModel[3].setRotationPoint(-0.5F, -27.5F, 0F);

		periscopeModel[4].addShapeBox(0F, 0F, 0F, 2, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 23
		periscopeModel[4].setRotationPoint(-3F, -39F, -4F);

		periscopeModel[5].addShapeBox(0F, 0F, 0F, 2, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 24
		periscopeModel[5].setRotationPoint(-3F, -39F, 0.5F);

		periscopeModel[6].addShapeBox(0F, 0F, 0F, 3, 2, 3, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 25
		periscopeModel[6].setRotationPoint(-1F, -38.5F, 1F);

		periscopeModel[7].addShapeBox(0F, 0F, 0F, 2, 2, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		periscopeModel[7].setRotationPoint(-1F, -28.5F, -3F);

		periscopeModel[8].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 28
		periscopeModel[8].setRotationPoint(-1F, -29.5F, -3.5F);

		periscopeModel[9].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 29
		periscopeModel[9].setRotationPoint(-1F, -29.5F, 1F);

		periscopeModel[10].addTrapezoid(0F, 0F, 0F, 3, 7, 3, 0F, -0.35F, ModelRendererTurbo.MR_BOTTOM); // Box 30
		periscopeModel[10].setRotationPoint(-1F, -36.5F, 1F);

		periscopeModel[11].addTrapezoid(0F, 0F, 0F, 3, 7, 3, 0F, -0.35F, ModelRendererTurbo.MR_BOTTOM); // Box 31
		periscopeModel[11].setRotationPoint(-1F, -36.5F, -3.5F);

		periscopeModel[12].addShapeBox(0F, 0F, 0F, 1, 2, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 33
		periscopeModel[12].setRotationPoint(1F, -28.5F, -3F);

		periscopeModel[13].addTrapezoid(0F, 0F, 0F, 3, 2, 2, 0F, -0.30F, ModelRendererTurbo.MR_RIGHT); // Box 34
		periscopeModel[13].setRotationPoint(2F, -28.5F, -3F);

		periscopeModel[14].addTrapezoid(0F, 0F, 0F, 3, 2, 2, 0F, -0.30F, ModelRendererTurbo.MR_RIGHT); // Box 35
		periscopeModel[14].setRotationPoint(2F, -28.5F, 1F);

		periscopeModel[15].addShapeBox(0F, 0F, 0F, 2, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 36
		periscopeModel[15].setRotationPoint(2F, -28F, -2F);

		periscopeModel[16].addShapeBox(0F, 0F, 0F, 3, 2, 3, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 25
		periscopeModel[16].setRotationPoint(-1F, -38.5F, -3.5F);

		periscopeModel[17].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, -0.25F, -0.45F, -0.45F, -0.25F, -0.45F, -0.45F, -0.25F, -0.45F, -0.45F, -0.25F, -0.45F, -0.45F, -0.25F, -0.45F, -0.45F, -0.25F, -0.45F, -0.45F, -0.25F, -0.45F, -0.45F, -0.25F, -0.45F, -0.45F); // Box 35
		periscopeModel[17].setRotationPoint(4.35F, -28.5F, 1F);

		periscopeModel[18].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, -0.25F, -0.45F, -0.45F, -0.25F, -0.45F, -0.45F, -0.25F, -0.45F, -0.45F, -0.25F, -0.45F, -0.45F, -0.25F, -0.45F, -0.45F, -0.25F, -0.45F, -0.45F, -0.25F, -0.45F, -0.45F, -0.25F, -0.45F, -0.45F); // Box 35
		periscopeModel[18].setRotationPoint(4.35F, -28.5F, -3F);


		leverModel = new ModelRendererTurbo[2];
		leverModel[0] = new ModelRendererTurbo(this, 5, 18, textureX, textureY); // Box 19
		leverModel[1] = new ModelRendererTurbo(this, 11, 23, textureX, textureY); // Box 20

		leverModel[0].addShapeBox(0F, -0.5F, -0.5F, 1, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 19
		leverModel[0].setRotationPoint(-2F, -26F, 0.5F);

		leverModel[1].addShapeBox(0F, -0.5F, -0.5F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 20
		leverModel[1].setRotationPoint(-1F, -26F, 0.5F);


		leg1Model = new ModelRendererTurbo[6];
		leg1Model[0] = new ModelRendererTurbo(this, 0, 17, textureX, textureY); // Tripod14
		leg1Model[1] = new ModelRendererTurbo(this, 6, 10, textureX, textureY); // Tripod15
		leg1Model[2] = new ModelRendererTurbo(this, 16, 0, textureX, textureY); // Tripod16
		leg1Model[3] = new ModelRendererTurbo(this, 24, 16, textureX, textureY); // Tripod15
		leg1Model[4] = new ModelRendererTurbo(this, 0, 4, textureX, textureY); // Tripod15
		leg1Model[5] = new ModelRendererTurbo(this, 28, 19, textureX, textureY); // Tripod15

		leg1Model[0].addShapeBox(-2.7F, -0.5F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod14
		leg1Model[0].setRotationPoint(0F, -20F, 2F);
		leg1Model[0].rotateAngleX = 0.29670597F;
		leg1Model[0].rotateAngleY = 0.78539816F;

		leg1Model[1].addShapeBox(-0.7F, -0.5F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod15
		leg1Model[1].setRotationPoint(0F, -20F, 2F);
		leg1Model[1].rotateAngleX = 0.29670597F;
		leg1Model[1].rotateAngleY = 0.78539816F;

		leg1Model[2].addShapeBox(-3.2F, 4.5F, 0F, 4, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod16
		leg1Model[2].setRotationPoint(0F, -20F, 2F);
		leg1Model[2].rotateAngleX = 0.29670597F;
		leg1Model[2].rotateAngleY = 0.78539816F;

		leg1Model[3].addShapeBox(-1.7F, 7.5F, 0F, 1, 15, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod15
		leg1Model[3].setRotationPoint(0F, -20F, 2F);
		leg1Model[3].rotateAngleX = 0.29670597F;
		leg1Model[3].rotateAngleY = 0.78539816F;

		leg1Model[4].addShapeBox(-0.7F, 5.5F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod15
		leg1Model[4].setRotationPoint(0F, -20F, 2F);
		leg1Model[4].rotateAngleX = 0.29670597F;
		leg1Model[4].rotateAngleY = 0.78539816F;

		leg1Model[5].addShapeBox(-2.7F, 5.5F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod15
		leg1Model[5].setRotationPoint(0F, -20F, 2F);
		leg1Model[5].rotateAngleX = 0.29670597F;
		leg1Model[5].rotateAngleY = 0.78539816F;


		leg2Model = new ModelRendererTurbo[6];
		leg2Model[0] = new ModelRendererTurbo(this, 24, 16, textureX, textureY); // Tripod01
		leg2Model[1] = new ModelRendererTurbo(this, 6, 10, textureX, textureY); // Tripod07
		leg2Model[2] = new ModelRendererTurbo(this, 16, 0, textureX, textureY); // Tripod08
		leg2Model[3] = new ModelRendererTurbo(this, 0, 17, textureX, textureY); // Tripod09
		leg2Model[4] = new ModelRendererTurbo(this, 28, 19, textureX, textureY); // Tripod01
		leg2Model[5] = new ModelRendererTurbo(this, 0, 4, textureX, textureY); // Tripod01

		leg2Model[0].addShapeBox(0F, 8F, 0F, 1, 15, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod01
		leg2Model[0].setRotationPoint(0F, -21F, -2F);
		leg2Model[0].rotateAngleX = -0.29670597F;

		leg2Model[1].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod07
		leg2Model[1].setRotationPoint(-1F, -21F, -2F);
		leg2Model[1].rotateAngleX = -0.29670597F;

		leg2Model[2].addShapeBox(0F, 5F, 0F, 4, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod08
		leg2Model[2].setRotationPoint(-1.5F, -21F, -2F);
		leg2Model[2].rotateAngleX = -0.29670597F;

		leg2Model[3].addShapeBox(0F, 0F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod09
		leg2Model[3].setRotationPoint(1F, -21F, -2F);
		leg2Model[3].rotateAngleX = -0.29670597F;

		leg2Model[4].addShapeBox(1F, 6F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod01
		leg2Model[4].setRotationPoint(0F, -21F, -2F);
		leg2Model[4].rotateAngleX = -0.29670597F;

		leg2Model[5].addShapeBox(-1F, 6F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod01
		leg2Model[5].setRotationPoint(0F, -21F, -2F);
		leg2Model[5].rotateAngleX = -0.29670597F;


		leg3Model = new ModelRendererTurbo[6];
		leg3Model[0] = new ModelRendererTurbo(this, 0, 17, textureX, textureY); // Tripod06
		leg3Model[1] = new ModelRendererTurbo(this, 6, 10, textureX, textureY); // Tripod10
		leg3Model[2] = new ModelRendererTurbo(this, 16, 0, textureX, textureY); // Tripod12
		leg3Model[3] = new ModelRendererTurbo(this, 24, 16, textureX, textureY); // Tripod13
		leg3Model[4] = new ModelRendererTurbo(this, 28, 19, textureX, textureY); // Tripod13
		leg3Model[5] = new ModelRendererTurbo(this, 0, 4, textureX, textureY); // Tripod13

		leg3Model[0].addShapeBox(0F, -0.8F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod06
		leg3Model[0].setRotationPoint(1F, -20F, 2F);
		leg3Model[0].rotateAngleX = 0.29670597F;
		leg3Model[0].rotateAngleY = -0.78539816F;

		leg3Model[1].addShapeBox(2F, -0.8F, 0F, 1, 5, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod10
		leg3Model[1].setRotationPoint(1F, -20F, 2F);
		leg3Model[1].rotateAngleX = 0.29670597F;
		leg3Model[1].rotateAngleY = -0.78539816F;

		leg3Model[2].addShapeBox(-0.5F, 4F, 0.01F, 4, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod12
		leg3Model[2].setRotationPoint(1F, -20F, 2F);
		leg3Model[2].rotateAngleX = 0.29670597F;
		leg3Model[2].rotateAngleY = -0.78539816F;

		leg3Model[3].addShapeBox(1F, 7F, 0F, 1, 15, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod13
		leg3Model[3].setRotationPoint(1F, -20F, 2F);
		leg3Model[3].rotateAngleX = 0.29670597F;
		leg3Model[3].rotateAngleY = -0.78539816F;

		leg3Model[4].addShapeBox(0F, 5F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod13
		leg3Model[4].setRotationPoint(1F, -20F, 2F);
		leg3Model[4].rotateAngleX = 0.29670597F;
		leg3Model[4].rotateAngleY = -0.78539816F;

		leg3Model[5].addShapeBox(2F, 5F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Tripod13
		leg3Model[5].setRotationPoint(1F, -20F, 2F);
		leg3Model[5].rotateAngleX = 0.29670597F;
		leg3Model[5].rotateAngleY = -0.78539816F;

		parts.put("base", baseModel);
		parts.put("periscope", periscopeModel);
		parts.put("lever", leverModel);

		parts.put("leg1", leg1Model);
		parts.put("leg2", leg2Model);
		parts.put("leg3", leg3Model);

		translate(periscopeModel, -0.5f, 0, -0.5f);
		translate(leverModel, -0.5f, 0, -0.5f);

		flipAll();
	}
}
