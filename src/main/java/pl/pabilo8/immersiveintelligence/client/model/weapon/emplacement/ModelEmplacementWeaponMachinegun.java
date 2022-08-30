package pl.pabilo8.immersiveintelligence.client.model.weapon.emplacement;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

import java.util.Arrays;

public class ModelEmplacementWeaponMachinegun extends ModelIIBase
{
	int textureX = 128;
	int textureY = 64;

	public ModelRendererTurbo[] turretModel, turretBaseModel, ammoCrateModel, ammoCrateLidModel, barrelsModel;

	public ModelEmplacementWeaponMachinegun(boolean doOffsets)
	{
		baseModel = new ModelRendererTurbo[38];
		baseModel[0] = new ModelRendererTurbo(this, 76, 31, textureX, textureY); // Box 3
		baseModel[1] = new ModelRendererTurbo(this, 76, 31, textureX, textureY); // Box 7
		baseModel[2] = new ModelRendererTurbo(this, 24, 49, textureX, textureY); // Box 8
		baseModel[3] = new ModelRendererTurbo(this, 24, 49, textureX, textureY); // Box 9
		baseModel[4] = new ModelRendererTurbo(this, 22, 26, textureX, textureY); // Box 10
		baseModel[5] = new ModelRendererTurbo(this, 22, 26, textureX, textureY); // Box 11
		baseModel[6] = new ModelRendererTurbo(this, 22, 26, textureX, textureY); // Box 12
		baseModel[7] = new ModelRendererTurbo(this, 22, 26, textureX, textureY); // Box 13
		baseModel[8] = new ModelRendererTurbo(this, 66, 17, textureX, textureY); // Box 8
		baseModel[9] = new ModelRendererTurbo(this, 108, 19, textureX, textureY); // Box 8
		baseModel[10] = new ModelRendererTurbo(this, 108, 19, textureX, textureY); // Box 8
		baseModel[11] = new ModelRendererTurbo(this, 108, 19, textureX, textureY); // Box 8
		baseModel[12] = new ModelRendererTurbo(this, 66, 17, textureX, textureY); // Box 8
		baseModel[13] = new ModelRendererTurbo(this, 66, 17, textureX, textureY); // Box 8
		baseModel[14] = new ModelRendererTurbo(this, 108, 19, textureX, textureY); // Box 8
		baseModel[15] = new ModelRendererTurbo(this, 66, 17, textureX, textureY); // Box 8
		baseModel[16] = new ModelRendererTurbo(this, 108, 19, textureX, textureY); // Box 8
		baseModel[17] = new ModelRendererTurbo(this, 108, 19, textureX, textureY); // Box 8
		baseModel[18] = new ModelRendererTurbo(this, 66, 17, textureX, textureY); // Box 8
		baseModel[19] = new ModelRendererTurbo(this, 108, 19, textureX, textureY); // Box 8
		baseModel[20] = new ModelRendererTurbo(this, 66, 17, textureX, textureY); // Box 8
		baseModel[21] = new ModelRendererTurbo(this, 26, 14, textureX, textureY); // Box 3
		baseModel[22] = new ModelRendererTurbo(this, 46, 14, textureX, textureY); // Box 3
		baseModel[23] = new ModelRendererTurbo(this, 8, 35, textureX, textureY); // Box 3
		baseModel[24] = new ModelRendererTurbo(this, 26, 14, textureX, textureY); // Box 3
		baseModel[25] = new ModelRendererTurbo(this, 26, 14, textureX, textureY); // Box 3
		baseModel[26] = new ModelRendererTurbo(this, 8, 35, textureX, textureY); // Box 3
		baseModel[27] = new ModelRendererTurbo(this, 46, 14, textureX, textureY); // Box 3
		baseModel[28] = new ModelRendererTurbo(this, 26, 14, textureX, textureY); // Box 3
		baseModel[29] = new ModelRendererTurbo(this, 8, 35, textureX, textureY); // Box 3
		baseModel[30] = new ModelRendererTurbo(this, 8, 35, textureX, textureY); // Box 3
		baseModel[31] = new ModelRendererTurbo(this, 26, 14, textureX, textureY); // Box 3
		baseModel[32] = new ModelRendererTurbo(this, 46, 14, textureX, textureY); // Box 3
		baseModel[33] = new ModelRendererTurbo(this, 26, 14, textureX, textureY); // Box 3
		baseModel[34] = new ModelRendererTurbo(this, 46, 14, textureX, textureY); // Box 3
		baseModel[35] = new ModelRendererTurbo(this, 8, 35, textureX, textureY); // Box 3
		baseModel[36] = new ModelRendererTurbo(this, 46, 14, textureX, textureY); // Box 3
		baseModel[37] = new ModelRendererTurbo(this, 108, 19, textureX, textureY); // Box 8

		baseModel[0].setFlipped(true);
		baseModel[0].addShapeBox(0F, 0F, 0F, 4, 11, 22, 0F, -4F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F); // Box 3
		baseModel[0].setRotationPoint(1F, -11F, 5F);

		baseModel[1].addShapeBox(0F, 0F, 0F, 4, 11, 22, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 7
		baseModel[1].setRotationPoint(27F, -11F, 5F);

		baseModel[2].addShapeBox(0F, 0F, 0F, 22, 11, 4, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 8
		baseModel[2].setRotationPoint(5F, -11F, 1F);

		baseModel[3].setFlipped(true);
		baseModel[3].addShapeBox(0F, 0F, 0F, 22, 11, 4, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F); // Box 9
		baseModel[3].setRotationPoint(5F, -11F, 27F);

		baseModel[4].addShapeBox(0F, 0F, 0F, 4, 11, 4, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 10
		baseModel[4].setRotationPoint(27F, -11F, 27F);

		baseModel[5].addShapeBox(0F, 0F, 0F, 4, 11, 4, 0F, 0F, 0F, -2F, -2F, 0F, -2F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 11
		baseModel[5].setRotationPoint(27F, -11F, 1F);

		baseModel[6].addShapeBox(0F, 0F, 0F, 4, 11, 4, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, -2F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 12
		baseModel[6].setRotationPoint(1F, -11F, 27F);

		baseModel[7].addShapeBox(0F, 0F, 0F, 4, 11, 4, 0F, -2F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 13
		baseModel[7].setRotationPoint(1F, -11F, 1F);

		baseModel[8].addBox(0F, -1F, -2F, 8, 4, 2, 0F); // Box 8
		baseModel[8].setRotationPoint(5F, -11F, 3F);
		baseModel[8].rotateAngleX = -0.17453293F;

		baseModel[9].addBox(12F, -1F, -2F, 8, 4, 2, 0F); // Box 8
		baseModel[9].setRotationPoint(5F, -11F, 3F);
		baseModel[9].rotateAngleX = -0.17453293F;

		baseModel[10].addBox(6F, 3F, -2F, 8, 4, 2, 0F); // Box 8
		baseModel[10].setRotationPoint(5F, -11F, 3F);
		baseModel[10].rotateAngleX = -0.17453293F;

		baseModel[11].addBox(17F, 3F, -2F, 8, 4, 2, 0F); // Box 8
		baseModel[11].setRotationPoint(5F, -11F, 3F);
		baseModel[11].rotateAngleX = -0.17453293F;

		baseModel[12].addBox(-2F, 7F, -2F, 8, 4, 2, 0F); // Box 8
		baseModel[12].setRotationPoint(5F, -11F, 3F);
		baseModel[12].rotateAngleX = -0.17453293F;

		baseModel[13].addBox(10F, 7F, -2F, 8, 4, 2, 0F); // Box 8
		baseModel[13].setRotationPoint(5F, -11F, 3F);
		baseModel[13].rotateAngleX = -0.17453293F;

		baseModel[14].addBox(-2F, -1F, 0F, 8, 4, 2, 0F); // Box 8
		baseModel[14].setRotationPoint(5F, -11F, 29F);
		baseModel[14].rotateAngleX = 0.17453293F;

		baseModel[15].addBox(10F, -1F, 0F, 8, 4, 2, 0F); // Box 8
		baseModel[15].setRotationPoint(5F, -11F, 29F);
		baseModel[15].rotateAngleX = 0.17453293F;

		baseModel[16].addBox(4F, 3F, 0F, 8, 4, 2, 0F); // Box 8
		baseModel[16].setRotationPoint(5F, -11F, 29F);
		baseModel[16].rotateAngleX = 0.17453293F;

		baseModel[17].addBox(15F, 3F, 0F, 8, 4, 2, 0F); // Box 8
		baseModel[17].setRotationPoint(5F, -11F, 29F);
		baseModel[17].rotateAngleX = 0.17453293F;

		baseModel[18].addBox(-4F, 7F, 0F, 8, 4, 2, 0F); // Box 8
		baseModel[18].setRotationPoint(5F, -11F, 29F);
		baseModel[18].rotateAngleX = 0.17453293F;

		baseModel[19].addBox(8F, 7F, 0F, 8, 4, 2, 0F); // Box 8
		baseModel[19].setRotationPoint(5F, -11F, 29F);
		baseModel[19].rotateAngleX = 0.17453293F;

		baseModel[20].addBox(19F, 7F, 0F, 8, 4, 2, 0F); // Box 8
		baseModel[20].setRotationPoint(5F, -11F, 29F);
		baseModel[20].rotateAngleX = 0.17453293F;

		baseModel[21].addBox(-2F, -1F, -4F, 2, 4, 8, 0F); // Box 3
		baseModel[21].setRotationPoint(3F, -11F, 5F);
		baseModel[21].rotateAngleZ = -0.17453293F;

		baseModel[22].addBox(-2F, 7F, -4F, 2, 4, 8, 0F); // Box 3
		baseModel[22].setRotationPoint(3F, -11F, 5F);
		baseModel[22].rotateAngleZ = -0.17453293F;

		baseModel[23].addBox(-2F, 3F, 1F, 2, 4, 8, 0F); // Box 3
		baseModel[23].setRotationPoint(3F, -11F, 5F);
		baseModel[23].rotateAngleZ = -0.17453293F;

		baseModel[24].addBox(-2F, -1F, 7F, 2, 4, 8, 0F); // Box 3
		baseModel[24].setRotationPoint(3F, -11F, 5F);
		baseModel[24].rotateAngleZ = -0.17453293F;

		baseModel[25].addBox(-2F, 7F, 14F, 2, 4, 8, 0F); // Box 3
		baseModel[25].setRotationPoint(3F, -11F, 8F);
		baseModel[25].rotateAngleZ = -0.17453293F;

		baseModel[26].addBox(-2F, -1F, 18F, 2, 4, 8, 0F); // Box 3
		baseModel[26].setRotationPoint(3F, -11F, 5F);
		baseModel[26].rotateAngleZ = -0.17453293F;

		baseModel[27].addBox(-2F, 3F, 1F, 2, 4, 8, 0F); // Box 3
		baseModel[27].setRotationPoint(3F, -11F, 17F);
		baseModel[27].rotateAngleZ = -0.17453293F;

		baseModel[28].addBox(-2F, 7F, 6F, 2, 4, 8, 0F); // Box 3
		baseModel[28].setRotationPoint(3F, -11F, 5F);
		baseModel[28].rotateAngleZ = -0.17453293F;

		baseModel[29].addBox(0F, -1F, -4F, 2, 4, 8, 0F); // Box 3
		baseModel[29].setRotationPoint(29F, -11F, 5F);
		baseModel[29].rotateAngleZ = 0.17453293F;

		baseModel[30].addBox(0F, 7F, -4F, 2, 4, 8, 0F); // Box 3
		baseModel[30].setRotationPoint(29F, -11F, 5F);
		baseModel[30].rotateAngleZ = 0.17453293F;

		baseModel[31].addBox(0F, 3F, 1F, 2, 4, 8, 0F); // Box 3
		baseModel[31].setRotationPoint(29F, -11F, 5F);
		baseModel[31].rotateAngleZ = 0.17453293F;

		baseModel[32].addBox(0F, -1F, 7F, 2, 4, 8, 0F); // Box 3
		baseModel[32].setRotationPoint(29F, -11F, 5F);
		baseModel[32].rotateAngleZ = 0.17453293F;

		baseModel[33].addBox(0F, 7F, 14F, 2, 4, 8, 0F); // Box 3
		baseModel[33].setRotationPoint(29F, -11F, 8F);
		baseModel[33].rotateAngleZ = 0.17453293F;

		baseModel[34].addBox(0F, -1F, 18F, 2, 4, 8, 0F); // Box 3
		baseModel[34].setRotationPoint(29F, -11F, 5F);
		baseModel[34].rotateAngleZ = 0.17453293F;

		baseModel[35].addBox(0F, 3F, 1F, 2, 4, 8, 0F); // Box 3
		baseModel[35].setRotationPoint(29F, -11F, 17F);
		baseModel[35].rotateAngleZ = 0.17453293F;

		baseModel[36].addBox(0F, 7F, 6F, 2, 4, 8, 0F); // Box 3
		baseModel[36].setRotationPoint(29F, -11F, 5F);
		baseModel[36].rotateAngleZ = 0.17453293F;

		baseModel[37].addBox(-4F, 3F, -2F, 8, 4, 2, 0F); // Box 8
		baseModel[37].setRotationPoint(5F, -11F, 3F);
		baseModel[37].rotateAngleX = -0.17453293F;


		turretModel = new ModelRendererTurbo[25];
		turretModel[0] = new ModelRendererTurbo(this, 90, 23, textureX, textureY); // Box 17
		turretModel[1] = new ModelRendererTurbo(this, 88, 38, textureX, textureY); // Box 18
		turretModel[2] = new ModelRendererTurbo(this, 88, 38, textureX, textureY); // Box 19
		turretModel[3] = new ModelRendererTurbo(this, 88, 49, textureX, textureY); // Box 20
		turretModel[4] = new ModelRendererTurbo(this, 2, 47, textureX, textureY); // Box 21
		turretModel[5] = new ModelRendererTurbo(this, 88, 49, textureX, textureY); // Box 23
		turretModel[6] = new ModelRendererTurbo(this, 88, 49, textureX, textureY); // Box 24
		turretModel[7] = new ModelRendererTurbo(this, 2, 47, textureX, textureY); // Box 25
		turretModel[8] = new ModelRendererTurbo(this, 58, 33, textureX, textureY); // Box 27
		turretModel[9] = new ModelRendererTurbo(this, 36, 26, textureX, textureY); // Box 33
		turretModel[10] = new ModelRendererTurbo(this, 12, 50, textureX, textureY); // Box 27
		turretModel[11] = new ModelRendererTurbo(this, 10, 47, textureX, textureY); // Box 27
		turretModel[12] = new ModelRendererTurbo(this, 2, 31, textureX, textureY); // Box 27
		turretModel[13] = new ModelRendererTurbo(this, 8, 54, textureX, textureY); // Box 27
		turretModel[14] = new ModelRendererTurbo(this, 114, 38, textureX, textureY); // Box 27
		turretModel[15] = new ModelRendererTurbo(this, 114, 38, textureX, textureY); // Box 27
		turretModel[16] = new ModelRendererTurbo(this, 0, 55, textureX, textureY); // Box 27
		turretModel[17] = new ModelRendererTurbo(this, 0, 43, textureX, textureY); // Box 27
		turretModel[18] = new ModelRendererTurbo(this, 114, 47, textureX, textureY); // Box 27
		turretModel[19] = new ModelRendererTurbo(this, 0, 59, textureX, textureY); // Box 27
		turretModel[20] = new ModelRendererTurbo(this, 48, 32, textureX, textureY); // Box 27
		turretModel[21] = new ModelRendererTurbo(this, 0, 51, textureX, textureY); // Box 27
		turretModel[22] = new ModelRendererTurbo(this, 122, 47, textureX, textureY); // Box 27
		turretModel[23] = new ModelRendererTurbo(this, 26, 43, textureX, textureY); // Box 27
		turretModel[24] = new ModelRendererTurbo(this, 26, 43, textureX, textureY); // Box 27

		turretModel[0].addShapeBox(0F, 1.25F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 17
		turretModel[0].setRotationPoint(5F, -22.75F, 12F);
		turretModel[0].rotateAngleZ = 0.2268928F;

		turretModel[1].addShapeBox(0F, 1.25F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 18
		turretModel[1].setRotationPoint(5F, -22.75F, 7F);
		turretModel[1].rotateAngleZ = 0.2268928F;

		turretModel[2].addShapeBox(0F, 1.25F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 19
		turretModel[2].setRotationPoint(5F, -22.75F, 5F);
		turretModel[2].rotateAngleZ = 0.2268928F;

		turretModel[3].addShapeBox(0F, 1.25F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 20
		turretModel[3].setRotationPoint(5.5F, -22.25F, 6F);
		turretModel[3].rotateAngleZ = 0.2268928F;

		turretModel[4].addShapeBox(0F, 1.25F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F); // Box 21
		turretModel[4].setRotationPoint(5.5F, -22.25F, 8F);
		turretModel[4].rotateAngleZ = 0.2268928F;

		turretModel[5].addShapeBox(0F, 1.25F, 0F, 3, 3, 1, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 23
		turretModel[5].setRotationPoint(5.5F, -22.25F, 9F);
		turretModel[5].rotateAngleZ = 0.2268928F;

		turretModel[6].addShapeBox(0F, 1.25F, 0F, 3, 3, 1, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 24
		turretModel[6].setRotationPoint(5.5F, -22.25F, 11F);
		turretModel[6].rotateAngleZ = 0.2268928F;

		turretModel[7].addShapeBox(0F, 1.25F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F); // Box 25
		turretModel[7].setRotationPoint(5.5F, -22.25F, 10F);
		turretModel[7].rotateAngleZ = 0.2268928F;

		turretModel[8].addShapeBox(0F, 0F, 0F, 10, 6, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		turretModel[8].setRotationPoint(11F, -18F, 11F);

		turretModel[9].addShapeBox(0F, 0F, 0F, 13, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 33
		turretModel[9].setRotationPoint(9.5F, -16F, 13F);

		turretModel[10].addShapeBox(-6F, 0F, 0F, 6, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		turretModel[10].setRotationPoint(11F, -18.75F, 13F);
		turretModel[10].rotateAngleZ = 0.2268928F;

		turretModel[11].addShapeBox(-6F, -5F, 0F, 7, 1, 2, 0F, 0F, 0F, 0F, 0.5F, 0F, 0F, 0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		turretModel[11].setRotationPoint(11F, -18.75F, 13F);
		turretModel[11].rotateAngleZ = 0.2268928F;

		turretModel[12].addShapeBox(-6F, -4F, 0F, 1, 4, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		turretModel[12].setRotationPoint(11F, -18.75F, 13F);
		turretModel[12].rotateAngleZ = 0.2268928F;

		turretModel[13].addShapeBox(0F, 0F, 0F, 4, 6, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		turretModel[13].setRotationPoint(11F, -24F, 12F);

		turretModel[14].addShapeBox(0F, 0F, 0F, 3, 5, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		turretModel[14].setRotationPoint(11.5F, -17F, 7F);

		turretModel[15].addShapeBox(0F, 0F, 0F, 3, 5, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		turretModel[15].setRotationPoint(17.5F, -17F, 7F);

		turretModel[16].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		turretModel[16].setRotationPoint(15F, -20F, 13F);

		turretModel[17].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		turretModel[17].setRotationPoint(17F, -20F, 13F);

		turretModel[18].addShapeBox(0F, 0F, 0F, 2, 4, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		turretModel[18].setRotationPoint(17F, -18F, 15F);
		turretModel[18].rotateAngleX = 1.57079633F;

		turretModel[19].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F); // Box 27
		turretModel[19].setRotationPoint(17F, -20F, 19F);

		turretModel[20].addShapeBox(0F, 0F, 0F, 8, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		turretModel[20].setRotationPoint(11F, -20F, 21F);

		turretModel[21].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		turretModel[21].setRotationPoint(14F, -20F, 19F);

		turretModel[22].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		turretModel[22].setRotationPoint(16F, -20F, 19F);

		turretModel[23].addShapeBox(0F, 0F, 0F, 2, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		turretModel[23].setRotationPoint(11F, -19F, 16.5F);

		turretModel[24].addShapeBox(0F, 0F, 0F, 2, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		turretModel[24].setRotationPoint(19F, -19F, 16.5F);

		turretBaseModel = new ModelRendererTurbo[6];
		turretBaseModel[0] = new ModelRendererTurbo(this, 2, 31, textureX, textureY); // Box 14
		turretBaseModel[1] = new ModelRendererTurbo(this, 106, 25, textureX, textureY); // Box 15
		turretBaseModel[2] = new ModelRendererTurbo(this, 2, 31, textureX, textureY); // Box 16
		turretBaseModel[3] = new ModelRendererTurbo(this, 8, 51, textureX, textureY); // Box 31
		turretBaseModel[4] = new ModelRendererTurbo(this, 8, 51, textureX, textureY); // Box 32
		turretBaseModel[5] = new ModelRendererTurbo(this, 58, 23, textureX, textureY); // Box 36

		turretBaseModel[0].addShapeBox(0F, 0F, 0F, 1, 6, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 14
		turretBaseModel[0].setRotationPoint(10F, -16F, 11F);

		turretBaseModel[1].addShapeBox(0F, 0F, 0F, 6, 8, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 15
		turretBaseModel[1].setRotationPoint(13F, -9F, 13F);

		turretBaseModel[2].addShapeBox(0F, 0F, 0F, 1, 6, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		turretBaseModel[2].setRotationPoint(21F, -16F, 11F);

		turretBaseModel[3].addShapeBox(0F, 0F, 0F, 1, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // Box 31
		turretBaseModel[3].setRotationPoint(21F, -16F, 11F);
		turretBaseModel[3].rotateAngleX = 1.57079633F;

		turretBaseModel[4].addShapeBox(0F, 0F, 0F, 1, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // Box 32
		turretBaseModel[4].setRotationPoint(10F, -16F, 11F);
		turretBaseModel[4].rotateAngleX = 1.57079633F;

		turretBaseModel[5].addShapeBox(0F, 0F, 0F, 12, 1, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 36
		turretBaseModel[5].setRotationPoint(10F, -10F, 10F);

		ammoCrateModel = new ModelRendererTurbo[2];
		ammoCrateModel[0] = new ModelRendererTurbo(this, 102, 5, textureX, textureY); // Box 34
		ammoCrateModel[1] = new ModelRendererTurbo(this, 102, 5, textureX, textureY); // Box 35

		ammoCrateModel[0].addShapeBox(0F, 0F, 0F, 5, 6, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 34
		ammoCrateModel[0].setRotationPoint(6F, -17F, 18F);

		ammoCrateModel[1].addShapeBox(0F, 0F, 0F, 5, 6, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 35
		ammoCrateModel[1].setRotationPoint(21F, -17F, 18F);

		ammoCrateLidModel = new ModelRendererTurbo[4];
		ammoCrateLidModel[0] = new ModelRendererTurbo(this, 30, 33, textureX, textureY); // Box 34
		ammoCrateLidModel[1] = new ModelRendererTurbo(this, 30, 33, textureX, textureY); // Box 34
		ammoCrateLidModel[2] = new ModelRendererTurbo(this, 38, 30, textureX, textureY); // Box 34
		ammoCrateLidModel[3] = new ModelRendererTurbo(this, 38, 30, textureX, textureY); // Box 34

		ammoCrateLidModel[0].addShapeBox(0F, 0F, 0F, 5, 1, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 34
		ammoCrateLidModel[0].setRotationPoint(6F, -18F, 18F);

		ammoCrateLidModel[1].addShapeBox(0F, 0F, 0F, 5, 1, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 34
		ammoCrateLidModel[1].setRotationPoint(21F, -18F, 18F);

		ammoCrateLidModel[2].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 34
		ammoCrateLidModel[2].setRotationPoint(7.5F, -18F, 17F);

		ammoCrateLidModel[3].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 34
		ammoCrateLidModel[3].setRotationPoint(22.5F, -18F, 17F);


		barrelsModel = new ModelRendererTurbo[8];
		barrelsModel[0] = new ModelRendererTurbo(this, 106, 38, textureX, textureY); // Box 29
		barrelsModel[1] = new ModelRendererTurbo(this, 16, 12, textureX, textureY); // Box 37
		barrelsModel[2] = new ModelRendererTurbo(this, 72, 49, textureX, textureY); // Box 38
		barrelsModel[3] = new ModelRendererTurbo(this, 80, 49, textureX, textureY); // Box 39
		barrelsModel[4] = new ModelRendererTurbo(this, 106, 38, textureX, textureY); // Box 29
		barrelsModel[5] = new ModelRendererTurbo(this, 16, 12, textureX, textureY); // Box 37
		barrelsModel[6] = new ModelRendererTurbo(this, 72, 49, textureX, textureY); // Box 38
		barrelsModel[7] = new ModelRendererTurbo(this, 80, 49, textureX, textureY); // Box 39

		barrelsModel[0].addShapeBox(0F, 0F, 0F, 2, 13, 2, 0F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F); // Box 29
		barrelsModel[0].setRotationPoint(18F, -14F, -13F);
		barrelsModel[0].rotateAngleX = 1.57079633F;

		barrelsModel[1].addShapeBox(0F, 0F, 0F, 2, 3, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 37
		barrelsModel[1].setRotationPoint(18F, -16F, 0F);

		barrelsModel[2].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.75F, -0.75F, 0F, -0.75F, -0.75F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.75F, -0.75F, 0F, -0.75F, -0.75F, 0F); // Box 38
		barrelsModel[2].setRotationPoint(17.5F, -16.5F, -14F);

		barrelsModel[3].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F); // Box 39
		barrelsModel[3].setRotationPoint(17.5F, -16.5F, -15F);

		barrelsModel[4].addShapeBox(0F, 0F, 0F, 2, 13, 2, 0F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F); // Box 29
		barrelsModel[4].setRotationPoint(12F, -14F, -13F);
		barrelsModel[4].rotateAngleX = 1.57079633F;

		barrelsModel[5].addShapeBox(0F, 0F, 0F, 2, 3, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 37
		barrelsModel[5].setRotationPoint(12F, -16F, 0F);

		barrelsModel[6].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.75F, -0.75F, 0F, -0.75F, -0.75F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.75F, -0.75F, 0F, -0.75F, -0.75F, 0F); // Box 38
		barrelsModel[6].setRotationPoint(11.5F, -16.5F, -14F);

		barrelsModel[7].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F); // Box 39
		barrelsModel[7].setRotationPoint(11.5F, -16.5F, -15F);

		parts.put("base", baseModel);
		parts.put("turretBase", turretBaseModel);
		parts.put("turret", turretModel);
		parts.put("ammoCrate", ammoCrateModel);
		parts.put("ammoCrateLid", ammoCrateLidModel);
		parts.put("barrels", barrelsModel);

		translateAll(-16,0,-16);

		if(doOffsets)
		{
			translate(turretModel,0,15,2);
			translate(ammoCrateModel,0,15,2);
			translate(ammoCrateLidModel,0,15,2);
			translate(barrelsModel,0,15,2);
		}


		baseModel = Arrays.stream(baseModel).
				sorted((o1, o2) -> Float.compare(o1.rotationPointY, o2.rotationPointY)).
				toArray(ModelRendererTurbo[]::new);

		turretModel = Arrays.stream(turretModel).
				sorted((o1, o2) -> Float.compare(o1.rotationPointY, o2.rotationPointY)).
				toArray(ModelRendererTurbo[]::new);

		turretBaseModel = Arrays.stream(turretBaseModel).
				sorted((o1, o2) -> Float.compare(o1.rotationPointY, o2.rotationPointY)).
				toArray(ModelRendererTurbo[]::new);

		ammoCrateModel = Arrays.stream(ammoCrateModel).
				sorted((o1, o2) -> Float.compare(o1.rotationPointY, o2.rotationPointY)).
				toArray(ModelRendererTurbo[]::new);

		ammoCrateLidModel = Arrays.stream(ammoCrateLidModel).
				sorted((o1, o2) -> Float.compare(o1.rotationPointY, o2.rotationPointY)).
				toArray(ModelRendererTurbo[]::new);

		barrelsModel = Arrays.stream(barrelsModel).
				sorted((o1, o2) -> Float.compare(o1.rotationPointY, o2.rotationPointY)).
				toArray(ModelRendererTurbo[]::new);

		flipAll();
	}
}
