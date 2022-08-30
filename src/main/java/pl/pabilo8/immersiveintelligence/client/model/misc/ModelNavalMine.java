package pl.pabilo8.immersiveintelligence.client.model.misc;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.render.NavalMineRenderer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 29.01.2021
 */
public class ModelNavalMine extends ModelIIBase implements IBulletModel
{
	int textureX = 64;
	int textureY = 64;

	public ModelRendererTurbo[] topModel, coreModel;

	public ModelNavalMine() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[46];
		baseModel[0] = new ModelRendererTurbo(this, 32, 42, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 44, 32, textureX, textureY); // Box 1
		baseModel[2] = new ModelRendererTurbo(this, 4, 0, textureX, textureY); // Box 2
		baseModel[3] = new ModelRendererTurbo(this, 40, 52, textureX, textureY); // Box 9
		baseModel[4] = new ModelRendererTurbo(this, 16, 0, textureX, textureY); // Box 10
		baseModel[5] = new ModelRendererTurbo(this, 20, 16, textureX, textureY); // Box 11
		baseModel[6] = new ModelRendererTurbo(this, 24, 52, textureX, textureY); // Box 14
		baseModel[7] = new ModelRendererTurbo(this, 32, 24, textureX, textureY); // Box 21
		baseModel[8] = new ModelRendererTurbo(this, 48, 24, textureX, textureY); // Box 22
		baseModel[9] = new ModelRendererTurbo(this, 20, 35, textureX, textureY); // Box 23
		baseModel[10] = new ModelRendererTurbo(this, 56, 42, textureX, textureY); // Box 25
		baseModel[11] = new ModelRendererTurbo(this, 56, 44, textureX, textureY); // Box 26
		baseModel[12] = new ModelRendererTurbo(this, 60, 44, textureX, textureY); // Box 27
		baseModel[13] = new ModelRendererTurbo(this, 56, 46, textureX, textureY); // Box 28
		baseModel[14] = new ModelRendererTurbo(this, 56, 4, textureX, textureY); // Box 53
		baseModel[15] = new ModelRendererTurbo(this, 52, 20, textureX, textureY); // Box 56
		baseModel[16] = new ModelRendererTurbo(this, 56, 4, textureX, textureY); // Box 53
		baseModel[17] = new ModelRendererTurbo(this, 52, 20, textureX, textureY); // Box 56
		baseModel[18] = new ModelRendererTurbo(this, 56, 4, textureX, textureY); // Box 53
		baseModel[19] = new ModelRendererTurbo(this, 52, 20, textureX, textureY); // Box 56
		baseModel[20] = new ModelRendererTurbo(this, 56, 4, textureX, textureY); // Box 53
		baseModel[21] = new ModelRendererTurbo(this, 52, 20, textureX, textureY); // Box 56
		baseModel[22] = new ModelRendererTurbo(this, 56, 4, textureX, textureY); // Box 53
		baseModel[23] = new ModelRendererTurbo(this, 52, 20, textureX, textureY); // Box 56
		baseModel[24] = new ModelRendererTurbo(this, 56, 4, textureX, textureY); // Box 53
		baseModel[25] = new ModelRendererTurbo(this, 52, 20, textureX, textureY); // Box 56
		baseModel[26] = new ModelRendererTurbo(this, 56, 4, textureX, textureY); // Box 53
		baseModel[27] = new ModelRendererTurbo(this, 52, 20, textureX, textureY); // Box 56
		baseModel[28] = new ModelRendererTurbo(this, 56, 4, textureX, textureY); // Box 53
		baseModel[29] = new ModelRendererTurbo(this, 52, 20, textureX, textureY); // Box 56
		baseModel[30] = new ModelRendererTurbo(this, 56, 4, textureX, textureY); // Box 53
		baseModel[31] = new ModelRendererTurbo(this, 52, 20, textureX, textureY); // Box 56
		baseModel[32] = new ModelRendererTurbo(this, 56, 4, textureX, textureY); // Box 53
		baseModel[33] = new ModelRendererTurbo(this, 52, 20, textureX, textureY); // Box 56
		baseModel[34] = new ModelRendererTurbo(this, 56, 4, textureX, textureY); // Box 53
		baseModel[35] = new ModelRendererTurbo(this, 52, 20, textureX, textureY); // Box 56
		baseModel[36] = new ModelRendererTurbo(this, 56, 4, textureX, textureY); // Box 53
		baseModel[37] = new ModelRendererTurbo(this, 52, 20, textureX, textureY); // Box 56
		baseModel[38] = new ModelRendererTurbo(this, 44, 32, textureX, textureY); // Box 1
		baseModel[39] = new ModelRendererTurbo(this, 16, 0, textureX, textureY); // Box 10
		baseModel[40] = new ModelRendererTurbo(this, 20, 16, textureX, textureY); // Box 11
		baseModel[41] = new ModelRendererTurbo(this, 32, 24, textureX, textureY); // Box 21
		baseModel[42] = new ModelRendererTurbo(this, 4, 0, textureX, textureY); // Box 2
		baseModel[43] = new ModelRendererTurbo(this, 40, 52, textureX, textureY); // Box 9
		baseModel[44] = new ModelRendererTurbo(this, 24, 52, textureX, textureY); // Box 14
		baseModel[45] = new ModelRendererTurbo(this, 48, 24, textureX, textureY); // Box 22

		baseModel[0].addShapeBox(0F, 0F, 0F, 8, 2, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, -5F, 0F);

		baseModel[1].addShapeBox(0F, 0F, 0F, 8, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		baseModel[1].setRotationPoint(0F, -15F, -4F);

		baseModel[2].addShapeBox(0F, 0F, 0F, 2, 8, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[2].setRotationPoint(-4F, -15F, 0F);

		baseModel[3].addShapeBox(0F, 0F, 0F, 4, 4, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F); // Box 9
		baseModel[3].setRotationPoint(-4F, -7F, 0F);

		baseModel[4].addShapeBox(0F, 0F, 0F, 8, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 10
		baseModel[4].setRotationPoint(0F, -7F, -4F);

		baseModel[5].addShapeBox(0F, 0F, 0F, 4, 8, 4, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 11
		baseModel[5].setRotationPoint(-4F, -15F, -4F);

		baseModel[6].addShapeBox(0F, 0F, 0F, 4, 8, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F); // Box 14
		baseModel[6].setRotationPoint(-4F, -15F, 8F);

		baseModel[7].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, -4F, 0F); // Box 21
		baseModel[7].setRotationPoint(-4F, -7F, -4F);

		baseModel[8].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, -4F, 0F, -4F); // Box 22
		baseModel[8].setRotationPoint(-4F, -7F, 8F);

		baseModel[9].addTrapezoid(0F, 0F, 0F, 6, 1, 6, 0F, -0.40F, ModelRendererTurbo.MR_BOTTOM); // Box 23
		baseModel[9].setRotationPoint(1F, -3F, 1F);

		baseModel[10].addShapeBox(0F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 25
		baseModel[10].setRotationPoint(2.5F, -2F, 3.5F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 26
		baseModel[11].setRotationPoint(2.5F, -1F, 3.5F);

		baseModel[12].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		baseModel[12].setRotationPoint(4.5F, -1F, 3.5F);

		baseModel[13].addShapeBox(0F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 28
		baseModel[13].setRotationPoint(2.5F, 0F, 3.5F);

		baseModel[14].addBox(-0.5F, -12F, -0.5F, 1, 3, 1, 0F); // Box 53
		baseModel[14].setRotationPoint(4F, -11F, 4F);
		baseModel[14].rotateAngleZ = 1.57079633F;

		baseModel[15].addBox(-1.5F, -9F, -1.5F, 3, 1, 3, 0F); // Box 56
		baseModel[15].setRotationPoint(4F, -11F, 4F);
		baseModel[15].rotateAngleZ = 1.57079633F;

		baseModel[16].addBox(-0.5F, -12F, -0.5F, 1, 3, 1, 0F); // Box 53
		baseModel[16].setRotationPoint(4F, -11F, 4F);
		baseModel[16].rotateAngleY = -1.57079633F;
		baseModel[16].rotateAngleZ = 1.57079633F;

		baseModel[17].addBox(-1.5F, -9F, -1.5F, 3, 1, 3, 0F); // Box 56
		baseModel[17].setRotationPoint(4F, -11F, 4F);
		baseModel[17].rotateAngleY = -1.57079633F;
		baseModel[17].rotateAngleZ = 1.57079633F;

		baseModel[18].addBox(-0.5F, -12F, -0.5F, 1, 3, 1, 0F); // Box 53
		baseModel[18].setRotationPoint(4F, -11F, 4F);
		baseModel[18].rotateAngleY = -3.14159265F;
		baseModel[18].rotateAngleZ = 1.57079633F;

		baseModel[19].addBox(-1.5F, -9F, -1.5F, 3, 1, 3, 0F); // Box 56
		baseModel[19].setRotationPoint(4F, -11F, 4F);
		baseModel[19].rotateAngleY = -3.14159265F;
		baseModel[19].rotateAngleZ = 1.57079633F;

		baseModel[20].addBox(-0.5F, -12F, -0.5F, 1, 3, 1, 0F); // Box 53
		baseModel[20].setRotationPoint(4F, -11F, 4F);
		baseModel[20].rotateAngleY = -4.71238898F;
		baseModel[20].rotateAngleZ = 1.57079633F;

		baseModel[21].addBox(-1.5F, -9F, -1.5F, 3, 1, 3, 0F); // Box 56
		baseModel[21].setRotationPoint(4F, -11F, 4F);
		baseModel[21].rotateAngleY = -4.71238898F;
		baseModel[21].rotateAngleZ = 1.57079633F;

		baseModel[22].addBox(-0.5F, -12.5F, -0.5F, 1, 3, 1, 0F); // Box 53
		baseModel[22].setRotationPoint(4F, -11F, 4F);
		baseModel[22].rotateAngleY = 0.78539816F;
		baseModel[22].rotateAngleZ = 1.57079633F;

		baseModel[23].addBox(-1.5F, -9.5F, -1.5F, 3, 1, 3, 0F); // Box 56
		baseModel[23].setRotationPoint(4F, -11F, 4F);
		baseModel[23].rotateAngleY = 0.78539816F;
		baseModel[23].rotateAngleZ = 1.57079633F;

		baseModel[24].addBox(-0.5F, -12.5F, -0.5F, 1, 3, 1, 0F); // Box 53
		baseModel[24].setRotationPoint(4F, -11F, 4F);
		baseModel[24].rotateAngleY = -0.78539816F;
		baseModel[24].rotateAngleZ = 1.57079633F;

		baseModel[25].addBox(-1.5F, -9.5F, -1.5F, 3, 1, 3, 0F); // Box 56
		baseModel[25].setRotationPoint(4F, -11F, 4F);
		baseModel[25].rotateAngleY = -0.78539816F;
		baseModel[25].rotateAngleZ = 1.57079633F;

		baseModel[26].addBox(-0.5F, -12.5F, -0.5F, 1, 3, 1, 0F); // Box 53
		baseModel[26].setRotationPoint(4F, -11F, 4F);
		baseModel[26].rotateAngleY = -2.35619449F;
		baseModel[26].rotateAngleZ = 1.57079633F;

		baseModel[27].addBox(-1.5F, -9.5F, -1.5F, 3, 1, 3, 0F); // Box 56
		baseModel[27].setRotationPoint(4F, -11F, 4F);
		baseModel[27].rotateAngleY = -2.35619449F;
		baseModel[27].rotateAngleZ = 1.57079633F;

		baseModel[28].addBox(-0.5F, -12.5F, -0.5F, 1, 3, 1, 0F); // Box 53
		baseModel[28].setRotationPoint(4F, -11F, 4F);
		baseModel[28].rotateAngleY = -3.92699082F;
		baseModel[28].rotateAngleZ = 1.57079633F;

		baseModel[29].addBox(-1.5F, -9.5F, -1.5F, 3, 1, 3, 0F); // Box 56
		baseModel[29].setRotationPoint(4F, -11F, 4F);
		baseModel[29].rotateAngleY = -3.92699082F;
		baseModel[29].rotateAngleZ = 1.57079633F;

		baseModel[30].addBox(-0.5F, -12.5F, -0.5F, 1, 3, 1, 0F); // Box 53
		baseModel[30].setRotationPoint(4F, -11F, 4F);
		baseModel[30].rotateAngleZ = 2.35619449F;

		baseModel[31].addBox(-1.5F, -9.5F, -1.5F, 3, 1, 3, 0F); // Box 56
		baseModel[31].setRotationPoint(4F, -11F, 4F);
		baseModel[31].rotateAngleZ = 2.35619449F;

		baseModel[32].addBox(-0.5F, -12.5F, -0.5F, 1, 3, 1, 0F); // Box 53
		baseModel[32].setRotationPoint(4F, -11F, 4F);
		baseModel[32].rotateAngleY = -1.57079633F;
		baseModel[32].rotateAngleZ = 2.35619449F;

		baseModel[33].addBox(-1.5F, -9.5F, -1.5F, 3, 1, 3, 0F); // Box 56
		baseModel[33].setRotationPoint(4F, -11F, 4F);
		baseModel[33].rotateAngleY = -1.57079633F;
		baseModel[33].rotateAngleZ = 2.35619449F;

		baseModel[34].addBox(-0.5F, -12.5F, -0.5F, 1, 3, 1, 0F); // Box 53
		baseModel[34].setRotationPoint(4F, -11F, 4F);
		baseModel[34].rotateAngleY = -3.14159265F;
		baseModel[34].rotateAngleZ = 2.35619449F;

		baseModel[35].addBox(-1.5F, -9.5F, -1.5F, 3, 1, 3, 0F); // Box 56
		baseModel[35].setRotationPoint(4F, -11F, 4F);
		baseModel[35].rotateAngleY = -3.14159265F;
		baseModel[35].rotateAngleZ = 2.35619449F;

		baseModel[36].addBox(-0.5F, -12.5F, -0.5F, 1, 3, 1, 0F); // Box 53
		baseModel[36].setRotationPoint(4F, -11F, 4F);
		baseModel[36].rotateAngleY = -4.71238898F;
		baseModel[36].rotateAngleZ = 2.35619449F;

		baseModel[37].addBox(-1.5F, -9.5F, -1.5F, 3, 1, 3, 0F); // Box 56
		baseModel[37].setRotationPoint(4F, -11F, 4F);
		baseModel[37].rotateAngleY = -4.71238898F;
		baseModel[37].rotateAngleZ = 2.35619449F;

		baseModel[38].addShapeBox(0F, 0F, 0F, 8, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		baseModel[38].setRotationPoint(8F, -15F, 12F);
		baseModel[38].rotateAngleY = 3.14159265F;

		baseModel[39].addShapeBox(0F, 0F, 0F, 8, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 10
		baseModel[39].setRotationPoint(8F, -7F, 12F);
		baseModel[39].rotateAngleY = 3.14159265F;

		baseModel[40].addShapeBox(0F, 0F, 0F, 4, 8, 4, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 11
		baseModel[40].setRotationPoint(12F, -15F, 12F);
		baseModel[40].rotateAngleY = -3.14159265F;

		baseModel[41].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, -4F, 0F); // Box 21
		baseModel[41].setRotationPoint(12F, -7F, 12F);
		baseModel[41].rotateAngleY = -3.14159265F;

		baseModel[42].addShapeBox(0F, 0F, 0F, 2, 8, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[42].setRotationPoint(12F, -15F, 8F);
		baseModel[42].rotateAngleY = -3.14159265F;

		baseModel[43].addShapeBox(0F, 0F, 0F, 4, 4, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F); // Box 9
		baseModel[43].setRotationPoint(12F, -7F, 8F);
		baseModel[43].rotateAngleY = -3.14159265F;

		baseModel[44].addShapeBox(0F, 0F, 0F, 4, 8, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F); // Box 14
		baseModel[44].setRotationPoint(12F, -15F, 0F);
		baseModel[44].rotateAngleY = -3.14159265F;

		baseModel[45].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, -4F, 0F, -4F); // Box 22
		baseModel[45].setRotationPoint(12F, -7F, 0F);
		baseModel[45].rotateAngleY = -3.14159265F;


		topModel = new ModelRendererTurbo[30];
		topModel[0] = new ModelRendererTurbo(this, 0, 28, textureX, textureY); // Box 3
		topModel[1] = new ModelRendererTurbo(this, 0, 52, textureX, textureY); // Box 4
		topModel[2] = new ModelRendererTurbo(this, 0, 20, textureX, textureY); // Box 15
		topModel[3] = new ModelRendererTurbo(this, 24, 42, textureX, textureY); // Box 16
		topModel[4] = new ModelRendererTurbo(this, 10, 36, textureX, textureY); // Box 24
		topModel[5] = new ModelRendererTurbo(this, 24, 12, textureX, textureY); // Box 29
		topModel[6] = new ModelRendererTurbo(this, 24, 12, textureX, textureY); // Box 30
		topModel[7] = new ModelRendererTurbo(this, 24, 12, textureX, textureY); // Box 31
		topModel[8] = new ModelRendererTurbo(this, 24, 12, textureX, textureY); // Box 32
		topModel[9] = new ModelRendererTurbo(this, 28, 8, textureX, textureY); // Box 33
		topModel[10] = new ModelRendererTurbo(this, 28, 8, textureX, textureY); // Box 34
		topModel[11] = new ModelRendererTurbo(this, 28, 8, textureX, textureY); // Box 35
		topModel[12] = new ModelRendererTurbo(this, 28, 8, textureX, textureY); // Box 36
		topModel[13] = new ModelRendererTurbo(this, 24, 8, textureX, textureY); // Box 37
		topModel[14] = new ModelRendererTurbo(this, 24, 8, textureX, textureY); // Box 38
		topModel[15] = new ModelRendererTurbo(this, 24, 8, textureX, textureY); // Box 39
		topModel[16] = new ModelRendererTurbo(this, 24, 8, textureX, textureY); // Box 40
		topModel[17] = new ModelRendererTurbo(this, 56, 4, textureX, textureY); // Box 41
		topModel[18] = new ModelRendererTurbo(this, 52, 20, textureX, textureY); // Box 44
		topModel[19] = new ModelRendererTurbo(this, 56, 4, textureX, textureY); // Box 45
		topModel[20] = new ModelRendererTurbo(this, 52, 20, textureX, textureY); // Box 48
		topModel[21] = new ModelRendererTurbo(this, 56, 4, textureX, textureY); // Box 61
		topModel[22] = new ModelRendererTurbo(this, 52, 20, textureX, textureY); // Box 64
		topModel[23] = new ModelRendererTurbo(this, 56, 4, textureX, textureY); // Box 65
		topModel[24] = new ModelRendererTurbo(this, 52, 20, textureX, textureY); // Box 68
		topModel[25] = new ModelRendererTurbo(this, 0, 42, textureX, textureY); // Box 0
		topModel[26] = new ModelRendererTurbo(this, 0, 28, textureX, textureY); // Box 3
		topModel[27] = new ModelRendererTurbo(this, 24, 42, textureX, textureY); // Box 16
		topModel[28] = new ModelRendererTurbo(this, 0, 52, textureX, textureY); // Box 4
		topModel[29] = new ModelRendererTurbo(this, 0, 20, textureX, textureY); // Box 15

		topModel[0].addShapeBox(0F, 0F, 0F, 8, 4, 4, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 3
		topModel[0].setRotationPoint(0F, -19F, -4F);

		topModel[1].addShapeBox(0F, 0F, 0F, 4, 4, 8, 0F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		topModel[1].setRotationPoint(-4F, -19F, 0F);

		topModel[2].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, -4F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F); // Box 15
		topModel[2].setRotationPoint(-4F, -19F, 8F);

		topModel[3].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, -4F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, -4F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		topModel[3].setRotationPoint(-4F, -19F, -4F);

		topModel[4].addTrapezoid(0F, 0F, 0F, 4, 1, 4, 0F, -0.20F, ModelRendererTurbo.MR_TOP); // Box 24
		topModel[4].setRotationPoint(2F, -20F, 2F);

		topModel[5].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 29
		topModel[5].setRotationPoint(0.5F, -20.5F, 3.5F);

		topModel[6].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 30
		topModel[6].setRotationPoint(6.5F, -20.5F, 3.5F);

		topModel[7].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 31
		topModel[7].setRotationPoint(3.5F, -20.5F, 0.5F);

		topModel[8].addShapeBox(0F, 0F, 0F, 1, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 32
		topModel[8].setRotationPoint(3.5F, -20.5F, 6.5F);

		topModel[9].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 33
		topModel[9].setRotationPoint(3.5F, -20F, 8.5F);

		topModel[10].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 34
		topModel[10].setRotationPoint(8.5F, -20F, 3.5F);

		topModel[11].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 35
		topModel[11].setRotationPoint(3.5F, -20F, -1.5F);

		topModel[12].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 36
		topModel[12].setRotationPoint(-1.5F, -20F, 3.5F);

		topModel[13].addShapeBox(0F, -3F, 0F, 1, 3, 1, 0F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F); // Box 37
		topModel[13].setRotationPoint(-1.5F, -20F, 3.5F);
		topModel[13].rotateAngleZ = -1.13446401F;

		topModel[14].addShapeBox(0F, -3F, -1F, 1, 3, 1, 0F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F); // Box 38
		topModel[14].setRotationPoint(3.5F, -20F, 9.5F);
		topModel[14].rotateAngleX = 1.13446401F;

		topModel[15].addShapeBox(-1F, -3F, -1F, 1, 3, 1, 0F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F); // Box 39
		topModel[15].setRotationPoint(9.5F, -20F, 4.5F);
		topModel[15].rotateAngleZ = 1.13446401F;

		topModel[16].addShapeBox(1F, 0F, -0.8F, 1, 3, 1, 0F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F); // Box 40
		topModel[16].setRotationPoint(2.5F, -20.5F, 1.5F);
		topModel[16].rotateAngleX = -1.13446401F;

		topModel[17].addBox(0F, -7.3F, 0F, 1, 3, 1, 0F); // Box 41
		topModel[17].setRotationPoint(3.5F, -14F, 0F);
		topModel[17].rotateAngleX = 0.78539816F;

		topModel[18].addBox(-1F, -4.3F, -1F, 3, 1, 3, 0F); // Box 44
		topModel[18].setRotationPoint(3.5F, -14F, 0F);
		topModel[18].rotateAngleX = 0.78539816F;

		topModel[19].addBox(0F, -11.5F, 0F, 1, 3, 1, 0F); // Box 45
		topModel[19].setRotationPoint(3F, -11F, 3.5F);
		topModel[19].rotateAngleZ = 0.78539816F;

		topModel[20].addBox(-1F, -8.5F, -1F, 3, 1, 3, 0F); // Box 48
		topModel[20].setRotationPoint(3F, -11F, 3.5F);
		topModel[20].rotateAngleZ = 0.78539816F;

		topModel[21].addBox(0F, -11F, 0F, 1, 3, 1, 0F); // Box 61
		topModel[21].setRotationPoint(3.5F, -12F, 4.8F);
		topModel[21].rotateAngleX = -0.78539816F;

		topModel[22].addBox(-1F, -8F, -1F, 3, 1, 3, 0F); // Box 64
		topModel[22].setRotationPoint(3.5F, -12F, 4.8F);
		topModel[22].rotateAngleX = -0.78539816F;

		topModel[23].addBox(0F, -11.2F, 0F, 1, 3, 1, 0F); // Box 65
		topModel[23].setRotationPoint(4.5F, -12F, 3.5F);
		topModel[23].rotateAngleZ = -0.78539816F;

		topModel[24].addBox(-1F, -8.2F, -1F, 3, 1, 3, 0F); // Box 68
		topModel[24].setRotationPoint(4.5F, -12F, 3.5F);
		topModel[24].rotateAngleZ = -0.78539816F;

		topModel[25].addShapeBox(0F, 0F, 0F, 8, 2, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		topModel[25].setRotationPoint(0F, -19F, 0F);

		topModel[26].addShapeBox(0F, 0F, 0F, 8, 4, 4, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 3
		topModel[26].setRotationPoint(8F, -19F, 12F);
		topModel[26].rotateAngleY = 3.14159265F;

		topModel[27].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, -4F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, -4F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		topModel[27].setRotationPoint(12F, -19F, 12F);
		topModel[27].rotateAngleY = -3.14159265F;

		topModel[28].addShapeBox(0F, 0F, 0F, 4, 4, 8, 0F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		topModel[28].setRotationPoint(12F, -19F, 8F);
		topModel[28].rotateAngleY = -3.14159265F;

		topModel[29].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, -4F, 0F, -4F, 0F, -4F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -4F); // Box 15
		topModel[29].setRotationPoint(12F, -19F, 0F);
		topModel[29].rotateAngleY = -3.14159265F;


		coreModel = new ModelRendererTurbo[1];
		coreModel[0] = new ModelRendererTurbo(this, 32, 0, textureX, textureY); // Box 0

		coreModel[0].addShapeBox(0F, 0F, 0F, 8, 12, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		coreModel[0].setRotationPoint(0F, -17F, 0F);

		parts.put("base",baseModel);
		parts.put("top",topModel);
		parts.put("core",coreModel);

		translateAll(-4,4,-4);

		flipAll();
	}

	@Override
	public void renderCasing(float gunpowderPercentage, int paintColour)
	{
		ClientUtils.bindTexture(NavalMineRenderer.TEXTURE);
		for(ModelRendererTurbo mod : baseModel)
			mod.render();
	}

	@Override
	public void renderCore(int coreColour, EnumCoreTypes coreType)
	{
		ClientUtils.bindTexture(NavalMineRenderer.TEXTURE);
		float[] c = IIUtils.rgbIntToRGB(coreColour);
		GlStateManager.color(c[0], c[1], c[2]);
		for(ModelRendererTurbo mod : coreModel)
			mod.render();
	}

	@Override
	public void renderBulletUsed(int coreColour, EnumCoreTypes coreType, int paintColour)
	{
		float[] c = IIUtils.rgbIntToRGB(coreColour);
		ClientUtils.bindTexture(NavalMineRenderer.TEXTURE);
		for(ModelRendererTurbo mod : baseModel)
			mod.render();
		for(ModelRendererTurbo mod : topModel)
			mod.render();
		GlStateManager.color(c[0], c[1], c[2]);
		for(ModelRendererTurbo mod : coreModel)
			mod.render();

	}

	@Override
	public void reloadModels()
	{
		ModelNavalMine newModel = new ModelNavalMine();
		this.baseModel=newModel.baseModel;
		this.coreModel=newModel.coreModel;
		this.topModel=newModel.topModel;
	}
}
