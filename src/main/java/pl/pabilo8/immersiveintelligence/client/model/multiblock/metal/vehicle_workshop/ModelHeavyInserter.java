package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.vehicle_workshop;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 09.04.2021
 */
public class ModelHeavyInserter extends ModelIIBase
{
	public ModelRendererTurbo[] inserterLowerArmModel, inserterUpperArmModel, boxDoorLeftModel, boxDoorRightModel, inserterGrabBoxModel, inserterGrabberModel, inserterSoldererModel;
	
	int textureX = 64;
	int textureY = 64;

	public ModelHeavyInserter() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[4];
		baseModel[0] = new ModelRendererTurbo(this, 32, 29, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 40, 9, textureX, textureY); // Box 0
		baseModel[2] = new ModelRendererTurbo(this, 26, 26, textureX, textureY); // Box 0
		baseModel[3] = new ModelRendererTurbo(this, 26, 26, textureX, textureY); // Box 0

		baseModel[0].addBox(-4F, 0F, -4F, 8, 1, 8, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, -1F, 0F);

		baseModel[1].addBox(-3F, 0F, -3F, 6, 3, 6, 0F); // Box 0
		baseModel[1].setRotationPoint(0F, -4F, 0F);

		baseModel[2].addBox(-3F, 0F, -3F, 1, 5, 6, 0F); // Box 0
		baseModel[2].setRotationPoint(0F, -9F, 0F);

		baseModel[3].addBox(2F, 0F, -3F, 1, 5, 6, 0F); // Box 0
		baseModel[3].setRotationPoint(0F, -9F, 0F);


		inserterLowerArmModel = new ModelRendererTurbo[25];
		inserterLowerArmModel[0] = new ModelRendererTurbo(this, 28, 60, textureX, textureY); // Box 0
		inserterLowerArmModel[1] = new ModelRendererTurbo(this, 16, 53, textureX, textureY); // Box 0
		inserterLowerArmModel[2] = new ModelRendererTurbo(this, 16, 53, textureX, textureY); // Box 0
		inserterLowerArmModel[3] = new ModelRendererTurbo(this, 28, 60, textureX, textureY); // Box 0
		inserterLowerArmModel[4] = new ModelRendererTurbo(this, 8, 52, textureX, textureY); // Box 0
		inserterLowerArmModel[5] = new ModelRendererTurbo(this, 8, 52, textureX, textureY); // Box 0
		inserterLowerArmModel[6] = new ModelRendererTurbo(this, 56, 46, textureX, textureY); // Box 0
		inserterLowerArmModel[7] = new ModelRendererTurbo(this, 56, 46, textureX, textureY); // Box 0
		inserterLowerArmModel[8] = new ModelRendererTurbo(this, 15, 34, textureX, textureY); // Box 0
		inserterLowerArmModel[9] = new ModelRendererTurbo(this, 38, 5, textureX, textureY); // Box 0
		inserterLowerArmModel[10] = new ModelRendererTurbo(this, 4, 30, textureX, textureY); // Box 0
		inserterLowerArmModel[11] = new ModelRendererTurbo(this, 17, 1, textureX, textureY); // Box 0
		inserterLowerArmModel[12] = new ModelRendererTurbo(this, 34, 24, textureX, textureY); // Box 0
		inserterLowerArmModel[13] = new ModelRendererTurbo(this, 25, 1, textureX, textureY); // Box 0
		inserterLowerArmModel[14] = new ModelRendererTurbo(this, 30, 2, textureX, textureY); // Box 0
		inserterLowerArmModel[15] = new ModelRendererTurbo(this, 34, 24, textureX, textureY); // Box 0
		inserterLowerArmModel[16] = new ModelRendererTurbo(this, 25, 1, textureX, textureY); // Box 0
		inserterLowerArmModel[17] = new ModelRendererTurbo(this, 30, 2, textureX, textureY); // Box 0
		inserterLowerArmModel[18] = new ModelRendererTurbo(this, 13, 9, textureX, textureY); // Box 0
		inserterLowerArmModel[19] = new ModelRendererTurbo(this, 10, 15, textureX, textureY); // Box 0
		inserterLowerArmModel[20] = new ModelRendererTurbo(this, 19, 41, textureX, textureY); // Box 0
		inserterLowerArmModel[21] = new ModelRendererTurbo(this, 6, 40, textureX, textureY); // Box 0
		inserterLowerArmModel[22] = new ModelRendererTurbo(this, 6, 40, textureX, textureY); // Box 0
		inserterLowerArmModel[23] = new ModelRendererTurbo(this, 14, 50, textureX, textureY); // Box 0
		inserterLowerArmModel[24] = new ModelRendererTurbo(this, 16, 13, textureX, textureY); // Box 0

		inserterLowerArmModel[0].addBox(-7F, -0.5F, -1F, 14, 2, 2, 0F); // Box 0
		inserterLowerArmModel[0].setRotationPoint(0F, -7.5F, 0F);

		inserterLowerArmModel[1].addBox(-6F, -6F, -1.5F, 3, 8, 3, 0F); // Box 0
		inserterLowerArmModel[1].setRotationPoint(0F, -7.5F, 0F);

		inserterLowerArmModel[2].addBox(3F, -6F, -1.5F, 3, 8, 3, 0F); // Box 0
		inserterLowerArmModel[2].setRotationPoint(0F, -7.5F, 0F);

		inserterLowerArmModel[3].addBox(-7F, -5.5F, -1F, 14, 2, 2, 0F); // Box 0
		inserterLowerArmModel[3].setRotationPoint(0F, -7.5F, 0F);

		inserterLowerArmModel[4].addBox(3.5F, -16F, -1F, 2, 10, 2, 0F); // Box 0
		inserterLowerArmModel[4].setRotationPoint(0F, -7.5F, 0F);

		inserterLowerArmModel[5].addBox(-5.5F, -16F, -1F, 2, 10, 2, 0F); // Box 0
		inserterLowerArmModel[5].setRotationPoint(0F, -7.5F, 0F);

		inserterLowerArmModel[6].addShapeBox(6F, -15F, -1F, 2, 12, 2, 0F, 0F, 0F, -0.3F, 0F, 0F, -0.3F, 0F, 0F, -0.3F, 0F, 0F, -0.3F, 0F, 0F, -0.3F, 0F, 0F, -0.3F, 0F, 0F, -0.3F, 0F, 0F, -0.3F); // Box 0
		inserterLowerArmModel[6].setRotationPoint(0F, -7.5F, 0F);
		inserterLowerArmModel[6].rotateAngleZ = 0.61086524F;

		inserterLowerArmModel[7].addShapeBox(-8F, -15F, -1F, 2, 12, 2, 0F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F); // Box 0
		inserterLowerArmModel[7].setRotationPoint(0F, -7.5F, 0F);
		inserterLowerArmModel[7].rotateAngleZ = -0.59341195F;

		inserterLowerArmModel[8].addBox(-6F, -16F, 1F, 4, 4, 3, 0F); // Box 0
		inserterLowerArmModel[8].setRotationPoint(0F, -7.5F, 0F);

		inserterLowerArmModel[9].addBox(-2F, -15F, 1F, 5, 2, 2, 0F); // Box 0
		inserterLowerArmModel[9].setRotationPoint(0F, -7.5F, 0F);

		inserterLowerArmModel[10].addShapeBox(3F, -15F, 1F, 2, 2, 2, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		inserterLowerArmModel[10].setRotationPoint(0F, -7.5F, 0F);

		inserterLowerArmModel[11].addBox(3F, -13F, 1F, 2, 1, 2, 0F); // Box 0
		inserterLowerArmModel[11].setRotationPoint(0F, -7.5F, 0F);

		inserterLowerArmModel[12].addBox(-5F, -16F, 4F, 2, 2, 1, 0F); // Box 0
		inserterLowerArmModel[12].setRotationPoint(0F, -7.5F, 0F);
		inserterLowerArmModel[12].rotateAngleX = 0.05235988F;

		inserterLowerArmModel[13].addShapeBox(-5F, -16F, 5F, 2, 2, 2, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F); // Box 0
		inserterLowerArmModel[13].setRotationPoint(0F, -7.5F, 0F);
		inserterLowerArmModel[13].rotateAngleX = 0.05235988F;

		inserterLowerArmModel[14].addShapeBox(-5F, -16F, 5F, 2, 2, 3, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F); // Box 0
		inserterLowerArmModel[14].setRotationPoint(0F, -7.5F, 0F);
		inserterLowerArmModel[14].rotateAngleX = 0.05235988F;

		inserterLowerArmModel[15].addBox(-5F, -14F, 3F, 2, 2, 1, 0F); // Box 0
		inserterLowerArmModel[15].setRotationPoint(0F, -7.5F, 0F);
		inserterLowerArmModel[15].rotateAngleX = -0.05235988F;

		inserterLowerArmModel[16].addShapeBox(-5F, -14F, 4F, 2, 2, 2, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F); // Box 0
		inserterLowerArmModel[16].setRotationPoint(0F, -7.5F, 0F);
		inserterLowerArmModel[16].rotateAngleX = -0.05235988F;

		inserterLowerArmModel[17].addShapeBox(-5F, -14F, 4F, 2, 2, 3, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F); // Box 0
		inserterLowerArmModel[17].setRotationPoint(0F, -7.5F, 0F);
		inserterLowerArmModel[17].rotateAngleX = -0.05235988F;

		inserterLowerArmModel[18].addBox(-5.5F, -12F, 1F, 2, 2, 2, 0F); // Box 0
		inserterLowerArmModel[18].setRotationPoint(0F, -7.5F, 0F);

		inserterLowerArmModel[19].addShapeBox(-5.5F, -10F, 1F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F); // Box 0
		inserterLowerArmModel[19].setRotationPoint(0F, -7.5F, 0F);

		inserterLowerArmModel[20].addBox(1F, -11F, 2.5F, 6, 8, 1, 0F); // Box 0
		inserterLowerArmModel[20].setRotationPoint(0F, -7.5F, 0F);
		inserterLowerArmModel[20].rotateAngleX = 0.03490659F;

		inserterLowerArmModel[21].addBox(2F, -10.5F, 3.5F, 4, 3, 1, 0F); // Box 0
		inserterLowerArmModel[21].setRotationPoint(0F, -7.5F, 0F);
		inserterLowerArmModel[21].rotateAngleX = 0.03490659F;

		inserterLowerArmModel[22].addBox(2F, -7F, 3.5F, 4, 3, 1, 0F); // Box 0
		inserterLowerArmModel[22].setRotationPoint(0F, -7.5F, 0F);
		inserterLowerArmModel[22].rotateAngleX = 0.03490659F;

		inserterLowerArmModel[23].addBox(1F, -11F, 1F, 6, 2, 1, 0F); // Box 0
		inserterLowerArmModel[23].setRotationPoint(0F, -7.5F, 0F);

		inserterLowerArmModel[24].addBox(2F, -12F, 1F, 4, 1, 2, 0F); // Box 0
		inserterLowerArmModel[24].setRotationPoint(0F, -7.5F, 0F);


		inserterUpperArmModel = new ModelRendererTurbo[23];
		inserterUpperArmModel[0] = new ModelRendererTurbo(this, 32, 38, textureX, textureY); // Box 0
		inserterUpperArmModel[1] = new ModelRendererTurbo(this, 0, 49, textureX, textureY); // Box 0
		inserterUpperArmModel[2] = new ModelRendererTurbo(this, 48, 42, textureX, textureY); // Box 0
		inserterUpperArmModel[3] = new ModelRendererTurbo(this, 28, 60, textureX, textureY); // Box 0
		inserterUpperArmModel[4] = new ModelRendererTurbo(this, 28, 42, textureX, textureY); // Box 0
		inserterUpperArmModel[5] = new ModelRendererTurbo(this, 39, 0, textureX, textureY); // Box 0
		inserterUpperArmModel[6] = new ModelRendererTurbo(this, 39, 0, textureX, textureY); // Box 0
		inserterUpperArmModel[7] = new ModelRendererTurbo(this, 21, 5, textureX, textureY); // Box 0
		inserterUpperArmModel[8] = new ModelRendererTurbo(this, 5, 35, textureX, textureY); // Box 0
		inserterUpperArmModel[9] = new ModelRendererTurbo(this, 5, 35, textureX, textureY); // Box 0
		inserterUpperArmModel[10] = new ModelRendererTurbo(this, 7, 44, textureX, textureY); // Box 0
		inserterUpperArmModel[11] = new ModelRendererTurbo(this, 12, 23, textureX, textureY); // Box 0
		inserterUpperArmModel[12] = new ModelRendererTurbo(this, 12, 23, textureX, textureY); // Box 0
		inserterUpperArmModel[13] = new ModelRendererTurbo(this, 12, 27, textureX, textureY); // Box 0
		inserterUpperArmModel[14] = new ModelRendererTurbo(this, 18, 21, textureX, textureY); // Box 0
		inserterUpperArmModel[15] = new ModelRendererTurbo(this, 56, 42, textureX, textureY); // Box 0
		inserterUpperArmModel[16] = new ModelRendererTurbo(this, 12, 19, textureX, textureY); // Box 0
		inserterUpperArmModel[17] = new ModelRendererTurbo(this, 39, 0, textureX, textureY); // Box 0
		inserterUpperArmModel[18] = new ModelRendererTurbo(this, 56, 29, textureX, textureY); // Box 0
		inserterUpperArmModel[19] = new ModelRendererTurbo(this, 40, 18, textureX, textureY); // Box 0
		inserterUpperArmModel[20] = new ModelRendererTurbo(this, 4, 30, textureX, textureY); // Box 0
		inserterUpperArmModel[21] = new ModelRendererTurbo(this, 17, 1, textureX, textureY); // Box 0
		inserterUpperArmModel[22] = new ModelRendererTurbo(this, 58, 9, textureX, textureY); // Box 0

		inserterUpperArmModel[0].addBox(-7F, -18F, -1F, 14, 2, 2, 0F); // Box 0
		inserterUpperArmModel[0].setRotationPoint(0F, -7.5F, 0F);

		inserterUpperArmModel[1].addBox(3.5F, -14F, -1F, 2, 13, 2, 0F); // Box 0
		inserterUpperArmModel[1].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[2].addBox(-5.5F, -7F, -1F, 2, 6, 2, 0F); // Box 0
		inserterUpperArmModel[2].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[3].addBox(-7F, -16F, -1F, 14, 2, 2, 0F); // Box 0
		inserterUpperArmModel[3].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[4].addBox(-2.5F, -11F, -3F, 6, 10, 8, 0F); // Box 0
		inserterUpperArmModel[4].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[5].addBox(-6.5F, -8F, -2F, 4, 1, 4, 0F); // Box 0
		inserterUpperArmModel[5].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[6].addBox(-6.5F, -14F, -2F, 4, 1, 4, 0F); // Box 0
		inserterUpperArmModel[6].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[7].addBox(-6F, -13F, -1.5F, 3, 5, 3, 0F); // Box 0
		inserterUpperArmModel[7].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[8].addBox(-1.5F, -10F, 7F, 4, 4, 1, 0F); // Box 0
		inserterUpperArmModel[8].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[9].addBox(-1.5F, -10F, 5F, 4, 4, 1, 0F); // Box 0
		inserterUpperArmModel[9].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[10].addBox(-1F, -9.5F, 6F, 3, 3, 3, 0F); // Box 0
		inserterUpperArmModel[10].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[11].addShapeBox(3.5F, -10F, 1F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F); // Box 0
		inserterUpperArmModel[11].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[12].addShapeBox(3.5F, -7F, 1F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F); // Box 0
		inserterUpperArmModel[12].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[13].addBox(0.5F, -14F, 1F, 2, 2, 2, 0F); // Box 0
		inserterUpperArmModel[13].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[14].addShapeBox(0.5F, -16F, 1F, 2, 2, 2, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		inserterUpperArmModel[14].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[15].addShapeBox(3.5F, -16F, 1F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F); // Box 0
		inserterUpperArmModel[15].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[16].addBox(2.5F, -16F, 1F, 1, 2, 2, 0F); // Box 0
		inserterUpperArmModel[16].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[17].addBox(-0.5F, -12F, 0F, 4, 1, 4, 0F); // Box 0
		inserterUpperArmModel[17].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[18].addBox(3.5F, -20F, -1F, 2, 4, 2, 0F); // Box 0
		inserterUpperArmModel[18].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[19].addBox(-3.5F, -21F, -2F, 6, 5, 6, 0F); // Box 0
		inserterUpperArmModel[19].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[20].addShapeBox(3.5F, -19F, 1F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		inserterUpperArmModel[20].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[21].addBox(3.5F, -17F, 1F, 2, 1, 2, 0F); // Box 0
		inserterUpperArmModel[21].setRotationPoint(0F, -24.5F, 0F);

		inserterUpperArmModel[22].addBox(2.5F, -19F, 1F, 1, 2, 2, 0F); // Box 0
		inserterUpperArmModel[22].setRotationPoint(0F, -24.5F, 0F);


		boxDoorLeftModel = new ModelRendererTurbo[1];
		boxDoorLeftModel[0] = new ModelRendererTurbo(this, 28, 17, textureX, textureY); // Box 0

		boxDoorLeftModel[0].addBox(-0.5F, -0.5F, -2F, 3, 1, 6, 0F); // Box 0
		boxDoorLeftModel[0].setRotationPoint(-3F, -46F, 0F);


		boxDoorRightModel = new ModelRendererTurbo[1];
		boxDoorRightModel[0] = new ModelRendererTurbo(this, 14, 25, textureX, textureY); // Box 0

		boxDoorRightModel[0].addBox(-0.5F, -0.5F, -2F, 3, 1, 6, 0F); // Box 0
		boxDoorRightModel[0].setRotationPoint(2F, -46F, 0F);
		boxDoorRightModel[0].rotateAngleZ = 3.14159265F;


		inserterGrabBoxModel = new ModelRendererTurbo[1];
		inserterGrabBoxModel[0] = new ModelRendererTurbo(this, 52, 2, textureX, textureY); // Box 0

		inserterGrabBoxModel[0].addBox(-2F, -5.5F, -2F, 3, 4, 3, 0F); // Box 0
		inserterGrabBoxModel[0].setRotationPoint(0F, -44F, 1F);


		inserterGrabberModel = new ModelRendererTurbo[1];
		inserterGrabberModel[0] = new ModelRendererTurbo(this, 9, 10, textureX, textureY); // Box 0

		inserterGrabberModel[0].addBox(-1F, -4F, -0.5F, 1, 4, 1, 0F); // Box 0
		inserterGrabberModel[0].setRotationPoint(-0.5F, -49.5F, 0.5F);
		inserterGrabberModel[0].rotateAngleZ = 0.78539816F;


		inserterSoldererModel = new ModelRendererTurbo[2];
		inserterSoldererModel[0] = new ModelRendererTurbo(this, 10, 32, textureX, textureY); // Box 0
		inserterSoldererModel[1] = new ModelRendererTurbo(this, 26, 16, textureX, textureY); // Box 0

		inserterSoldererModel[0].addShapeBox(-1F, -4F, -1F, 2, 1, 2, 0F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		inserterSoldererModel[0].setRotationPoint(-0.5F, -49.5F, 0.5F);

		inserterSoldererModel[1].addBox(-1F, -3F, -1F, 2, 3, 2, 0F); // Box 0
		inserterSoldererModel[1].setRotationPoint(-0.5F, -49.5F, 0.5F);

		translate(inserterLowerArmModel,0F, 7.5F, 0F);
		translate(inserterUpperArmModel,0F, 24.5F, 0F);
		translate(boxDoorLeftModel,0F, 24.5F, 0F);
		translate(boxDoorRightModel,0F, 24.5F, 0F);

		parts.put("base",baseModel);
		parts.put("inserterLower",inserterLowerArmModel);
		parts.put("inserterUpper",inserterUpperArmModel);
		parts.put("doorLeft",boxDoorLeftModel);
		parts.put("doorRight",boxDoorRightModel);
		parts.put("boxThingy",inserterGrabBoxModel);
		parts.put("grabber",inserterGrabberModel);
		parts.put("solderer",inserterSoldererModel);

		flipAll();
	}

	public void renderProgress(float progress, float angle, float maxProgress)
	{
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();

		ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/blocks/multiblock/vehicle_workshop/inserter.png");

		progress = progress < 0.25?progress*4: progress > 0.75?1f-((progress-0.75f)*4): 1f;

		GlStateManager.translate(0.5f, 0.125f, -0.5);
		GlStateManager.rotate(angle*progress, 0f, 1f, 0f);
		progress *= maxProgress;

		for(ModelRendererTurbo mod : baseModel)
			mod.render(0.0625f);

		GlStateManager.translate(0f, 0.385f, 0);
		GlStateManager.rotate(15+55*progress, 1, 0, 0);

		for(ModelRendererTurbo mod : inserterLowerArmModel)
			mod.render(0.0625f);

		GlStateManager.translate(0f, 1.0f, -0.0625f);
		GlStateManager.rotate(135-(95f*progress), 1, 0, 0);
		GlStateManager.translate(0f, 0.0625f, 0.03125f);

		for(ModelRendererTurbo mod : inserterUpperArmModel)
			mod.render(0.0625f);

		for(ModelRendererTurbo mod : boxDoorLeftModel)
			mod.render(0.0625f);
		for(ModelRendererTurbo mod : boxDoorRightModel)
			mod.render(0.0625f);

		GlStateManager.popMatrix();
	}
}
