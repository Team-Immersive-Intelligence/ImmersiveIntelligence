package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * @author Pabilo8
 * @since 30.09.2020
 */
public class ModelProjectileWorkshop extends ModelIIBase
{
	int textureX = 128;
	int textureY = 128;

	public ModelRendererTurbo[] insidesModel, lidLeftModel, lidRightModel;

	public ModelProjectileWorkshop() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[83];
		baseModel[0] = new ModelRendererTurbo(this, 78, 33, textureX, textureY); // MainPart03
		baseModel[1] = new ModelRendererTurbo(this, 0, 51, textureX, textureY); // MainPart10
		baseModel[2] = new ModelRendererTurbo(this, 0, 51, textureX, textureY); // MainPart11
		baseModel[3] = new ModelRendererTurbo(this, 0, 51, textureX, textureY); // MainPart12
		baseModel[4] = new ModelRendererTurbo(this, 16, 71, textureX, textureY); // MainPart13
		baseModel[5] = new ModelRendererTurbo(this, 64, 0, textureX, textureY); // MainPart15
		baseModel[6] = new ModelRendererTurbo(this, 66, 34, textureX, textureY); // MainPart18
		baseModel[7] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 18
		baseModel[8] = new ModelRendererTurbo(this, 59, 83, textureX, textureY); // Box 23
		baseModel[9] = new ModelRendererTurbo(this, 97, 105, textureX, textureY); // MaintenanceBoxShell08
		baseModel[10] = new ModelRendererTurbo(this, 48, 0, textureX, textureY); // MoshpitFence01
		baseModel[11] = new ModelRendererTurbo(this, 59, 74, textureX, textureY); // MetalThing101
		baseModel[12] = new ModelRendererTurbo(this, 0, 47, textureX, textureY); // Box 44
		baseModel[13] = new ModelRendererTurbo(this, 120, 33, textureX, textureY); // Box 45
		baseModel[14] = new ModelRendererTurbo(this, 112, 38, textureX, textureY); // Box 46
		baseModel[15] = new ModelRendererTurbo(this, 112, 38, textureX, textureY); // Box 47
		baseModel[16] = new ModelRendererTurbo(this, 120, 33, textureX, textureY); // Box 48
		baseModel[17] = new ModelRendererTurbo(this, 112, 38, textureX, textureY); // Box 49
		baseModel[18] = new ModelRendererTurbo(this, 112, 38, textureX, textureY); // Box 50
		baseModel[19] = new ModelRendererTurbo(this, 8, 51, textureX, textureY); // Box 51
		baseModel[20] = new ModelRendererTurbo(this, 54, 44, textureX, textureY); // Box 54
		baseModel[21] = new ModelRendererTurbo(this, 0, 98, textureX, textureY); // BoxCover01
		baseModel[22] = new ModelRendererTurbo(this, 0, 98, textureX, textureY); // BoxCover03
		baseModel[23] = new ModelRendererTurbo(this, 0, 98, textureX, textureY); // BoxCover08
		baseModel[24] = new ModelRendererTurbo(this, 48, 96, textureX, textureY); // MainPart01
		baseModel[25] = new ModelRendererTurbo(this, 10, 72, textureX, textureY); // BoxCover02
		baseModel[26] = new ModelRendererTurbo(this, 0, 98, textureX, textureY); // BoxCover03
		baseModel[27] = new ModelRendererTurbo(this, 10, 72, textureX, textureY); // BoxCover02
		baseModel[28] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // Shape 93
		baseModel[29] = new ModelRendererTurbo(this, 12, 49, textureX, textureY); // Box 23
		baseModel[30] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // Box 54
		baseModel[31] = new ModelRendererTurbo(this, 113, 0, textureX, textureY); // Box 54
		baseModel[32] = new ModelRendererTurbo(this, 54, 44, textureX, textureY); // Box 54
		baseModel[33] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // Box 54
		baseModel[34] = new ModelRendererTurbo(this, 54, 44, textureX, textureY); // Box 54
		baseModel[35] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // Box 54
		baseModel[36] = new ModelRendererTurbo(this, 54, 44, textureX, textureY); // Box 54
		baseModel[37] = new ModelRendererTurbo(this, 54, 44, textureX, textureY); // Box 54
		baseModel[38] = new ModelRendererTurbo(this, 54, 44, textureX, textureY); // Box 54
		baseModel[39] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // Box 54
		baseModel[40] = new ModelRendererTurbo(this, 78, 33, textureX, textureY); // MainPart03
		baseModel[41] = new ModelRendererTurbo(this, 78, 33, textureX, textureY); // MainPart03
		baseModel[42] = new ModelRendererTurbo(this, 78, 33, textureX, textureY); // MainPart03
		baseModel[43] = new ModelRendererTurbo(this, 78, 33, textureX, textureY); // MainPart03
		baseModel[44] = new ModelRendererTurbo(this, 78, 33, textureX, textureY); // MainPart03
		baseModel[45] = new ModelRendererTurbo(this, 16, 71, textureX, textureY); // MainPart13
		baseModel[46] = new ModelRendererTurbo(this, 78, 33, textureX, textureY); // MainPart03
		baseModel[47] = new ModelRendererTurbo(this, 78, 33, textureX, textureY); // MainPart03
		baseModel[48] = new ModelRendererTurbo(this, 78, 33, textureX, textureY); // MainPart03
		baseModel[49] = new ModelRendererTurbo(this, 78, 33, textureX, textureY); // MainPart03
		baseModel[50] = new ModelRendererTurbo(this, 78, 33, textureX, textureY); // MainPart03
		baseModel[51] = new ModelRendererTurbo(this, 78, 33, textureX, textureY); // MainPart03
		baseModel[52] = new ModelRendererTurbo(this, 48, 96, textureX, textureY); // MainPart01
		baseModel[53] = new ModelRendererTurbo(this, 48, 96, textureX, textureY); // MainPart01
		baseModel[54] = new ModelRendererTurbo(this, 48, 96, textureX, textureY); // MainPart01
		baseModel[55] = new ModelRendererTurbo(this, 50, 65, textureX, textureY); // MainPart01
		baseModel[56] = new ModelRendererTurbo(this, 50, 65, textureX, textureY); // MainPart01
		baseModel[57] = new ModelRendererTurbo(this, 50, 65, textureX, textureY); // MainPart01
		baseModel[58] = new ModelRendererTurbo(this, 50, 65, textureX, textureY); // MainPart01
		baseModel[59] = new ModelRendererTurbo(this, 66, 34, textureX, textureY); // MainPart18
		baseModel[60] = new ModelRendererTurbo(this, 66, 34, textureX, textureY); // MainPart18
		baseModel[61] = new ModelRendererTurbo(this, 66, 34, textureX, textureY); // MainPart18
		baseModel[62] = new ModelRendererTurbo(this, 0, 51, textureX, textureY); // MainPart12
		baseModel[63] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 18
		baseModel[64] = new ModelRendererTurbo(this, 54, 44, textureX, textureY); // Box 54
		baseModel[65] = new ModelRendererTurbo(this, 54, 44, textureX, textureY); // Box 54
		baseModel[66] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 18
		baseModel[67] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 18
		baseModel[68] = new ModelRendererTurbo(this, 48, 0, textureX, textureY); // MoshpitFence01
		baseModel[69] = new ModelRendererTurbo(this, 8, 51, textureX, textureY); // Box 51
		baseModel[70] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // Shape 93
		baseModel[71] = new ModelRendererTurbo(this, 0, 111, textureX, textureY); // Shape 93
		baseModel[72] = new ModelRendererTurbo(this, 59, 83, textureX, textureY); // Box 23
		baseModel[73] = new ModelRendererTurbo(this, 59, 83, textureX, textureY); // Box 23
		baseModel[74] = new ModelRendererTurbo(this, 59, 83, textureX, textureY); // Box 23
		baseModel[75] = new ModelRendererTurbo(this, 97, 42, textureX, textureY); // Box 23
		baseModel[76] = new ModelRendererTurbo(this, 97, 105, textureX, textureY); // MaintenanceBoxShell08
		baseModel[77] = new ModelRendererTurbo(this, 97, 105, textureX, textureY); // MaintenanceBoxShell08
		baseModel[78] = new ModelRendererTurbo(this, 97, 105, textureX, textureY); // MaintenanceBoxShell08
		baseModel[79] = new ModelRendererTurbo(this, 113, 0, textureX, textureY); // Box 54
		baseModel[80] = new ModelRendererTurbo(this, 113, 0, textureX, textureY); // Box 54
		baseModel[81] = new ModelRendererTurbo(this, 113, 0, textureX, textureY); // Box 54
		baseModel[82] = new ModelRendererTurbo(this, 114, 51, textureX, textureY); // Box 112

		baseModel[0].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart03
		baseModel[0].setRotationPoint(0F, 1F, 2F);
		baseModel[0].rotateAngleX = 1.57079633F;

		baseModel[1].addShapeBox(0F, 0F, 0F, 2, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart10
		baseModel[1].setRotationPoint(0F, -0.05F, 46F);

		baseModel[2].addShapeBox(0F, 0F, 0F, 2, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart11
		baseModel[2].setRotationPoint(0F, -0.05F, 0F);

		baseModel[3].addShapeBox(0F, 0F, 0F, 2, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart12
		baseModel[3].setRotationPoint(62F, -0.05F, 0F);

		baseModel[4].addShapeBox(0F, 0F, 0F, 16, 16, 1, 0F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F, 0F, 0F, 0.05F); // MainPart13
		baseModel[4].setRotationPoint(32F, 0F, 47F);

		baseModel[5].addShapeBox(0F, 0F, 0F, 16, 17, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart15
		baseModel[5].setRotationPoint(64F, -16F, 30F);
		baseModel[5].rotateAngleZ = -1.57079633F;

		baseModel[6].addShapeBox(0F, 0F, 0F, 15, 15, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart18
		baseModel[6].setRotationPoint(0F, -30F, 16F);

		baseModel[7].addShapeBox(0F, 0F, 0F, 16, 32, 15, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 18
		baseModel[7].setRotationPoint(15F, -32F, 17F);

		baseModel[8].setFlipped(true);
		baseModel[8].addShapeBox(0F, 0F, 0F, 10, 7, 5, 0F, 0F, -7F, 0F, 0F, -7F, 0F, 0F, -7F, 0F, 0F, -7F, 0F, 0F, -7F, 0F, 0F, -7F, 0F, 0F, -7F, 0F, 0F, -7F, 0F); // Box 23
		baseModel[8].setRotationPoint(19F, -47F, 35F);

		baseModel[9].addShapeBox(0F, 0F, 0F, 15, 16, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MaintenanceBoxShell08
		baseModel[9].setRotationPoint(17F, -0.05F, 1.05F);
		baseModel[9].rotateAngleX = 1.57079633F;

		baseModel[10].addShapeBox(0F, 0F, 0F, 16, 15, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MoshpitFence01
		baseModel[10].setRotationPoint(31F, -32F, 16.95F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 8, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MetalThing101
		baseModel[11].setRotationPoint(20F, -13F, 47F);

		baseModel[12].addShapeBox(0F, 0F, 0F, 4, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 44
		baseModel[12].setRotationPoint(54F, -15F, 29F);

		baseModel[13].addShapeBox(0F, 0F, 0F, 3, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 45
		baseModel[13].setRotationPoint(48F, -11F, 30F);
		baseModel[13].rotateAngleX = 1.57079633F;
		baseModel[13].rotateAngleY = -1.57079633F;

		baseModel[14].addShapeBox(0F, 0F, 0F, 3, 11, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 46
		baseModel[14].setRotationPoint(48F, -11F, 30F);
		baseModel[14].rotateAngleY = -1.57079633F;

		baseModel[15].addShapeBox(0F, 0F, 0F, 3, 11, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 47
		baseModel[15].setRotationPoint(63F, -11F, 30F);
		baseModel[15].rotateAngleY = -1.57079633F;

		baseModel[16].addShapeBox(0F, 0F, 0F, 3, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 48
		baseModel[16].setRotationPoint(0F, -11F, 16F);
		baseModel[16].rotateAngleX = 1.57079633F;
		baseModel[16].rotateAngleY = -1.57079633F;

		baseModel[17].addShapeBox(0F, 0F, 0F, 3, 11, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 49
		baseModel[17].setRotationPoint(0F, -11F, 16F);
		baseModel[17].rotateAngleY = -1.57079633F;

		baseModel[18].addShapeBox(0F, 0F, 0F, 3, 11, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 50
		baseModel[18].setRotationPoint(15F, -11F, 16F);
		baseModel[18].rotateAngleY = -1.57079633F;

		baseModel[19].addShapeBox(0F, 0F, 0F, 4, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 51
		baseModel[19].setRotationPoint(6F, -15F, 15F);

		baseModel[20].addShapeBox(0F, 0F, 0F, 3, 18, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 54
		baseModel[20].setRotationPoint(42F, -26F, 47F);

		baseModel[21].addShapeBox(0F, 0F, 0F, 16, 12, 1, 0F, -0.1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.1F, 0F, 0F, -0.1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.1F, 0F, 0F); // BoxCover01
		baseModel[21].setRotationPoint(16F, -14.95F, 5F);
		baseModel[21].rotateAngleX = 1.57079633F;

		baseModel[22].addShapeBox(0F, -0.5F, 0F, 16, 7, 1, 0F, -0.05F, 0F, 0F, -0.05F, 0F, 0F, -0.05F, 0F, 0F, -0.05F, 0F, 0F, -0.05F, 0F, 0F, -0.05F, 0F, 0F, -0.05F, -1F, 0F, -0.05F, -1F, 0F); // BoxCover03
		baseModel[22].setRotationPoint(16F, -15.64F, 4.65F);
		baseModel[22].rotateAngleX = -0.78539816F;

		baseModel[23].addShapeBox(0F, 0F, 0F, 16, 12, 1, 0F, 0F, 0F, 0F, -0.1F, 0F, 0F, -0.1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.1F, 0F, 0F, -0.1F, 0F, 0F, 0F, 0F, 0F); // BoxCover08
		baseModel[23].setRotationPoint(32F, -14.95F, 5F);
		baseModel[23].rotateAngleX = 1.57079633F;

		baseModel[24].addShapeBox(0F, 0F, 0F, 32, 24, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart01
		baseModel[24].setRotationPoint(0F, 16F, 0F);
		baseModel[24].rotateAngleX = 1.57079633F;

		baseModel[25].addShapeBox(0F, 0F, 0F, 2, 15, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxCover02
		baseModel[25].setRotationPoint(17F, -9.05F, 0F);
		baseModel[25].rotateAngleZ = 1.57079633F;

		baseModel[26].addShapeBox(0F, -0.5F, 0F, 16, 7, 1, 0F, -0.05F, 0F, 0F, -0.05F, 0F, 0F, -0.05F, 0F, 0F, -0.05F, 0F, 0F, -0.05F, 0F, 0F, -0.05F, 0F, 0F, -0.05F, -1F, 0F, -0.05F, -1F, 0F); // BoxCover03
		baseModel[26].setRotationPoint(32F, -15.64F, 4.65F);
		baseModel[26].rotateAngleX = -0.78539816F;

		baseModel[27].addShapeBox(0F, 0F, 0F, 2, 15, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxCover02
		baseModel[27].setRotationPoint(32F, -9.05F, 0F);
		baseModel[27].rotateAngleZ = 1.57079633F;

		baseModel[28].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(17, 0, 17, 0), new Coord2D(17, 16, 17, 16), new Coord2D(5, 16, 5, 16), new Coord2D(0, 11, 0, 11) }), 1, 17, 16, 64, 1, ModelRendererTurbo.MR_FRONT, new float[] {11 ,8 ,12 ,16 ,17}); // Shape 93
		baseModel[28].setRotationPoint(17F, 0F, 0F);
		baseModel[28].rotateAngleY = -1.57079633F;

		baseModel[29].addShapeBox(0F, 0F, 0F, 13, 14, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 23
		baseModel[29].setRotationPoint(15F, -32F, 46F);
		baseModel[29].rotateAngleX = 1.57079633F;
		baseModel[29].rotateAngleY = -1.57079633F;

		baseModel[30].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F); // Box 54
		baseModel[30].setRotationPoint(42F, -8F, 47F);

		baseModel[31].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 54
		baseModel[31].setRotationPoint(42F, -29F, 47F);

		baseModel[32].addShapeBox(0F, 0F, 0F, 3, 15, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 54
		baseModel[32].setRotationPoint(42F, -29F, 47F);
		baseModel[32].rotateAngleZ = -1.57079633F;

		baseModel[33].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F); // Box 54
		baseModel[33].setRotationPoint(27F, -35F, 47F);
		baseModel[33].rotateAngleZ = 3.14159265F;

		baseModel[34].addShapeBox(0F, 0F, 0F, 3, 6, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 54
		baseModel[34].setRotationPoint(24F, -35F, 47F);

		baseModel[35].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F); // Box 54
		baseModel[35].setRotationPoint(20F, -35F, 47F);
		baseModel[35].rotateAngleZ = 3.14159265F;

		baseModel[36].addShapeBox(0F, 0F, 0F, 3, 13, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 54
		baseModel[36].setRotationPoint(17F, -35F, 47F);

		baseModel[37].addShapeBox(0F, 0F, 0F, 3, 16, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 54
		baseModel[37].setRotationPoint(36F, -22F, 47F);
		baseModel[37].rotateAngleZ = -1.57079633F;

		baseModel[38].addShapeBox(0F, 0F, 0F, 3, 11, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 54
		baseModel[38].setRotationPoint(36F, -19F, 47F);

		baseModel[39].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F); // Box 54
		baseModel[39].setRotationPoint(36F, -8F, 47F);

		baseModel[40].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart03
		baseModel[40].setRotationPoint(0F, 1F, 18F);
		baseModel[40].rotateAngleX = 1.57079633F;

		baseModel[41].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart03
		baseModel[41].setRotationPoint(0F, 1F, 34F);
		baseModel[41].rotateAngleX = 1.57079633F;

		baseModel[42].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart03
		baseModel[42].setRotationPoint(63F, 1F, 2F);
		baseModel[42].rotateAngleX = 1.57079633F;

		baseModel[43].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart03
		baseModel[43].setRotationPoint(63F, 1F, 18F);
		baseModel[43].rotateAngleX = 1.57079633F;

		baseModel[44].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart03
		baseModel[44].setRotationPoint(63F, 1F, 34F);
		baseModel[44].rotateAngleX = 1.57079633F;

		baseModel[45].setFlipped(true);
		baseModel[45].addShapeBox(0F, 0F, 0F, 16, 16, 1, 0F, 0F, 0F, -1.05F, 0F, 0F, -1.05F, 0F, 0F, -1.05F, 0F, 0F, -1.05F, 0F, 0F, -1.05F, 0F, 0F, -1.05F, 0F, 0F, -1.05F, 0F, 0F, -1.05F); // MainPart13
		baseModel[45].setRotationPoint(48F, 0F, 47F);

		baseModel[46].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart03
		baseModel[46].setRotationPoint(2F, 1F, 0F);
		baseModel[46].rotateAngleZ = 1.57079633F;

		baseModel[47].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart03
		baseModel[47].setRotationPoint(18F, 1F, 0F);
		baseModel[47].rotateAngleZ = 1.57079633F;

		baseModel[48].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart03
		baseModel[48].setRotationPoint(34F, 1F, 0F);
		baseModel[48].rotateAngleZ = 1.57079633F;

		baseModel[49].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart03
		baseModel[49].setRotationPoint(50F, 1F, 0F);
		baseModel[49].rotateAngleZ = 1.57079633F;

		baseModel[50].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart03
		baseModel[50].setRotationPoint(2F, 1F, 47F);
		baseModel[50].rotateAngleZ = 1.57079633F;

		baseModel[51].addShapeBox(0F, 0F, 0F, 1, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart03
		baseModel[51].setRotationPoint(18F, 1F, 47F);
		baseModel[51].rotateAngleZ = 1.57079633F;

		baseModel[52].setFlipped(true);
		baseModel[52].addShapeBox(0F, 0F, 0F, 32, 24, 8, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F); // MainPart01
		baseModel[52].setRotationPoint(32F, 16F, 0F);
		baseModel[52].rotateAngleX = 1.57079633F;

		baseModel[53].setFlipped(true);
		baseModel[53].addShapeBox(0F, 0F, 0F, 32, 24, 8, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F); // MainPart01
		baseModel[53].setRotationPoint(0F, 16F, 24F);
		baseModel[53].rotateAngleX = 1.57079633F;

		baseModel[54].addShapeBox(0F, 0F, 0F, 32, 24, 8, 0F, -32F, -24F, 0F, -32F, -24F, 0F, -32F, -24F, 0F, -32F, -24F, 0F, -32F, -24F, 0F, -32F, -24F, 0F, -32F, -24F, 0F, -32F, -24F, 0F); // MainPart01
		baseModel[54].setRotationPoint(32F, 16F, 24F);
		baseModel[54].rotateAngleX = 1.57079633F;

		baseModel[55].addShapeBox(0F, 0F, 0F, 31, 23, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart01
		baseModel[55].setRotationPoint(1F, 8F, 1F);
		baseModel[55].rotateAngleX = 1.57079633F;

		baseModel[56].setFlipped(true);
		baseModel[56].addShapeBox(0F, 0F, 0F, 31, 23, 8, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F, -31F, 0F, 0F); // MainPart01
		baseModel[56].setRotationPoint(32F, 8F, 1F);
		baseModel[56].rotateAngleX = 1.57079633F;

		baseModel[57].setFlipped(true);
		baseModel[57].addShapeBox(0F, 0F, 0F, 31, 23, 8, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F); // MainPart01
		baseModel[57].setRotationPoint(1F, 8F, 24F);
		baseModel[57].rotateAngleX = 1.57079633F;

		baseModel[58].addShapeBox(0F, 0F, 0F, 31, 23, 8, 0F, -31F, -23F, 0F, -31F, -23F, 0F, -31F, -23F, 0F, -31F, -23F, 0F, -31F, -23F, 0F, -31F, -23F, 0F, -31F, -23F, 0F, -31F, -23F, 0F); // MainPart01
		baseModel[58].setRotationPoint(32F, 8F, 24F);
		baseModel[58].rotateAngleX = 1.57079633F;

		baseModel[59].setFlipped(true);
		baseModel[59].addShapeBox(0F, 0F, 0F, 15, 15, 16, 0F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F); // MainPart18
		baseModel[59].setRotationPoint(0F, -30F, 32F);

		baseModel[60].setFlipped(true);
		baseModel[60].addShapeBox(0F, 0F, 0F, 15, 15, 16, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F); // MainPart18
		baseModel[60].setRotationPoint(0F, -15F, 16F);

		baseModel[61].addShapeBox(0F, 0F, 0F, 15, 15, 16, 0F, 0F, -15F, -16F, 0F, -15F, -16F, 0F, -15F, -16F, 0F, -15F, -16F, 0F, -15F, -16F, 0F, -15F, -16F, 0F, -15F, -16F, 0F, -15F, -16F); // MainPart18
		baseModel[61].setRotationPoint(0F, -15F, 32F);

		baseModel[62].addShapeBox(0F, 0F, 0F, 2, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainPart12
		baseModel[62].setRotationPoint(62F, -0.05F, 46F);

		baseModel[63].setFlipped(true);
		baseModel[63].addShapeBox(0F, 0F, 0F, 16, 32, 15, 0F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F); // Box 18
		baseModel[63].setRotationPoint(15F, -32F, 32F);

		baseModel[64].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 54
		baseModel[64].setRotationPoint(24F, -38F, 47F);
		baseModel[64].rotateAngleX = -1.57079633F;

		baseModel[65].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 54
		baseModel[65].setRotationPoint(17F, -38F, 47F);
		baseModel[65].rotateAngleX = -1.57079633F;

		baseModel[66].setFlipped(true);
		baseModel[66].addShapeBox(0F, 0F, 0F, 16, 32, 15, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F); // Box 18
		baseModel[66].setRotationPoint(31F, -32F, 17F);

		baseModel[67].addShapeBox(0F, 0F, 0F, 16, 32, 15, 0F, -16F, 0F, -15F, -16F, 0F, -15F, -16F, 0F, -15F, -16F, 0F, -15F, -16F, 0F, -15F, -16F, 0F, -15F, -16F, 0F, -15F, -16F, 0F, -15F); // Box 18
		baseModel[67].setRotationPoint(31F, -32F, 32F);

		baseModel[68].setFlipped(true);
		baseModel[68].addShapeBox(0F, 0F, 0F, 16, 15, 0, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F); // MoshpitFence01
		baseModel[68].setRotationPoint(47.05F, -32F, 17F);
		baseModel[68].rotateAngleY = 1.57079633F;

		baseModel[69].addShapeBox(0F, 0F, 0F, 4, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 51
		baseModel[69].setRotationPoint(22F, -16F, 47F);

		baseModel[70].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(17, 0, 17, 0), new Coord2D(17, 16, 17, 16), new Coord2D(5, 16, 5, 16), new Coord2D(0, 11, 0, 11) }), 1, 17, 16, 64, 1, ModelRendererTurbo.MR_FRONT, new float[] {11 ,8 ,12 ,16 ,17}); // Shape 93
		baseModel[70].setRotationPoint(48F, 0F, 0F);
		baseModel[70].rotateAngleY = -1.57079633F;

		baseModel[71].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(17, 0, 17, 0), new Coord2D(17, 16, 17, 16), new Coord2D(5, 16, 5, 16), new Coord2D(0, 11, 0, 11) }), 1, 17, 16, 64, 1, ModelRendererTurbo.MR_FRONT, new float[] {11 ,8 ,12 ,16 ,17}); // Shape 93
		baseModel[71].setRotationPoint(32.5F, 0.1F, 0F);
		baseModel[71].rotateAngleY = -1.57079633F;

		baseModel[72].addShapeBox(0F, 0F, 0F, 10, 7, 5, 0F, -10F, -7F, 0F, -10F, -7F, 0F, -10F, -7F, 0F, -10F, -7F, 0F, -10F, -7F, 0F, -10F, -7F, 0F, -10F, -7F, 0F, -10F, -7F, 0F); // Box 23
		baseModel[72].setRotationPoint(29F, -47F, 45F);
		baseModel[72].rotateAngleY = -3.14159265F;

		baseModel[73].addShapeBox(0F, 0F, 0F, 10, 1, 5, 0F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 23
		baseModel[73].setRotationPoint(19F, -48F, 35F);

		baseModel[74].setFlipped(true);
		baseModel[74].addShapeBox(0F, 0F, 0F, 10, 1, 5, 0F, -9F, 0F, -1F, -9F, 0F, -1F, -9F, 0F, 0F, -9F, 0F, 0F, -10F, 0F, 0F, -10F, 0F, 0F, -10F, 0F, 0F, -10F, 0F, 0F); // Box 23
		baseModel[74].setRotationPoint(29F, -48F, 45F);
		baseModel[74].rotateAngleY = -3.14159265F;

		baseModel[75].setFlipped(true);
		baseModel[75].addBox(0F, 0F, 0F, 8, 8, 0, 0F); // Box 23
		baseModel[75].setRotationPoint(20F, -48.1F, 36F);
		baseModel[75].rotateAngleX = 1.57079633F;

		baseModel[76].setFlipped(true);
		baseModel[76].addShapeBox(0F, 0F, 0F, 15, 16, 0, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F); // MaintenanceBoxShell08
		baseModel[76].setRotationPoint(32F, -0.05F, 1.05F);
		baseModel[76].rotateAngleX = 1.57079633F;

		baseModel[77].addShapeBox(0F, 0F, 0F, 15, 15, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MaintenanceBoxShell08
		baseModel[77].setRotationPoint(18F, -15.05F, 16.05F);

		baseModel[78].setFlipped(true);
		baseModel[78].addShapeBox(0F, 0F, 0F, 15, 15, 0, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F, -15F, 0F, 0F); // MaintenanceBoxShell08
		baseModel[78].setRotationPoint(33F, -15.05F, 16.05F);

		baseModel[79].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 54
		baseModel[79].setRotationPoint(36F, -22F, 47F);

		baseModel[80].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 54
		baseModel[80].setRotationPoint(27F, -26F, 47F);
		baseModel[80].rotateAngleZ = 3.14159265F;

		baseModel[81].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 54
		baseModel[81].setRotationPoint(20F, -19F, 47F);
		baseModel[81].rotateAngleZ = 3.14159265F;

		baseModel[82].addShapeBox(0F, 0F, 0F, 7, 12, 0, 0F, 0F, 0F, 0F, 7F, 0F, 0F, 7F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 7F, 0F, 0F, 7F, 0F, 0F, 0F, 0F, 0F); // Box 112
		baseModel[82].setRotationPoint(1F, -11.5F, 15.95F);


		insidesModel = new ModelRendererTurbo[26];
		insidesModel[0] = new ModelRendererTurbo(this, 70, 33, textureX, textureY); // BoxInsidesMaintan01
		insidesModel[1] = new ModelRendererTurbo(this, 70, 33, textureX, textureY); // BoxInsidesMaintan02
		insidesModel[2] = new ModelRendererTurbo(this, 70, 33, textureX, textureY); // BoxInsidesMaintan03
		insidesModel[3] = new ModelRendererTurbo(this, 70, 33, textureX, textureY); // BoxInsidesMaintan04
		insidesModel[4] = new ModelRendererTurbo(this, 34, 115, textureX, textureY); // BoxInsidesMaintan05
		insidesModel[5] = new ModelRendererTurbo(this, 0, 88, textureX, textureY); // BoxInsidesMaintan06
		insidesModel[6] = new ModelRendererTurbo(this, 96, 0, textureX, textureY); // paper
		insidesModel[7] = new ModelRendererTurbo(this, 35, 93, textureX, textureY); // BoxInsidesMaintan13
		insidesModel[8] = new ModelRendererTurbo(this, 35, 93, textureX, textureY); // BoxInsidesMaintan14
		insidesModel[9] = new ModelRendererTurbo(this, 70, 33, textureX, textureY); // BoxInsidesMaintan15
		insidesModel[10] = new ModelRendererTurbo(this, 70, 34, textureX, textureY); // BoxInsidesMaintan16
		insidesModel[11] = new ModelRendererTurbo(this, 97, 121, textureX, textureY); // BoxInsidesMaintan17
		insidesModel[12] = new ModelRendererTurbo(this, 0, 61, textureX, textureY); // BoxInsidesMaintan18
		insidesModel[13] = new ModelRendererTurbo(this, 0, 61, textureX, textureY); // BoxInsidesMaintan19
		insidesModel[14] = new ModelRendererTurbo(this, 0, 61, textureX, textureY); // BoxInsidesMaintan20
		insidesModel[15] = new ModelRendererTurbo(this, 0, 61, textureX, textureY); // BoxInsidesMaintan21
		insidesModel[16] = new ModelRendererTurbo(this, 97, 121, textureX, textureY); // BoxInsidesMaintan22
		insidesModel[17] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BoxInsidesMaintan06
		insidesModel[18] = new ModelRendererTurbo(this, 0, 65, textureX, textureY); // BoxInsidesMaintan06
		insidesModel[19] = new ModelRendererTurbo(this, 70, 33, textureX, textureY); // BoxInsidesMaintan02
		insidesModel[20] = new ModelRendererTurbo(this, 70, 33, textureX, textureY); // BoxInsidesMaintan02
		insidesModel[21] = new ModelRendererTurbo(this, 120, 69, textureX, textureY); // BoxInsidesMaintan02
		insidesModel[22] = new ModelRendererTurbo(this, 120, 65, textureX, textureY); // BoxInsidesMaintan02
		insidesModel[23] = new ModelRendererTurbo(this, 0, 61, textureX, textureY); // BoxInsidesMaintan21
		insidesModel[24] = new ModelRendererTurbo(this, 34, 115, textureX, textureY); // BoxInsidesMaintan05
		insidesModel[25] = new ModelRendererTurbo(this, 10, 47, textureX, textureY); // BoxInsidesMaintan16

		insidesModel[0].addShapeBox(0F, 0F, 0F, 2, 12, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan01
		insidesModel[0].setRotationPoint(34F, -0.5F, 5F);
		insidesModel[0].rotateAngleZ = 1.57079633F;

		insidesModel[1].addShapeBox(0F, 0F, 0F, 2, 9, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan02
		insidesModel[1].setRotationPoint(32F, -1F, 8F);
		insidesModel[1].rotateAngleZ = 1.57079633F;

		insidesModel[2].addShapeBox(0F, 0F, 0F, 2, 15, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan03
		insidesModel[2].setRotationPoint(32F, -4F, 14F);
		insidesModel[2].rotateAngleZ = 1.57079633F;

		insidesModel[3].addShapeBox(0F, 0F, 0F, 2, 15, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan04
		insidesModel[3].setRotationPoint(17F, -4F, 14F);
		insidesModel[3].rotateAngleZ = 1.57079633F;

		insidesModel[4].addShapeBox(0F, 0F, 0F, 4, 9, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan05
		insidesModel[4].setRotationPoint(17F, 0F, 4F);
		insidesModel[4].rotateAngleX = 1.57079633F;

		insidesModel[5].addShapeBox(0F, 0F, 0F, 12, 8, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan06
		insidesModel[5].setRotationPoint(18F, -14F, 15F);

		insidesModel[6].addShapeBox(0F, 0F, 0F, 7, 9, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // paper
		insidesModel[6].setRotationPoint(30.5F, -13F, 13F);
		insidesModel[6].rotateAngleX = -0.10471976F;
		insidesModel[6].rotateAngleY = -1.57079633F;

		insidesModel[7].addShapeBox(0F, 0F, 0F, 6, 8, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan13
		insidesModel[7].setRotationPoint(34F, -14F, 14F);

		insidesModel[8].addShapeBox(0F, 0F, 0F, 6, 8, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan14
		insidesModel[8].setRotationPoint(41F, -14F, 14F);

		insidesModel[9].addShapeBox(0F, 0F, 0F, 2, 10, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan15
		insidesModel[9].setRotationPoint(27F, -0.5F, 7F);
		insidesModel[9].rotateAngleX = 1.57079633F;

		insidesModel[10].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan16
		insidesModel[10].setRotationPoint(29F, -0.5F, 5F);
		insidesModel[10].rotateAngleZ = 1.57079633F;

		insidesModel[11].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan17
		insidesModel[11].setRotationPoint(31F, -3.5F, 4.5F);

		insidesModel[12].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan18
		insidesModel[12].setRotationPoint(46F, -3F, 7.5F);
		insidesModel[12].rotateAngleY = -1.57079633F;

		insidesModel[13].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan19
		insidesModel[13].setRotationPoint(46F, -3.5F, 16.5F);
		insidesModel[13].rotateAngleY = -1.57079633F;

		insidesModel[14].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan20
		insidesModel[14].setRotationPoint(46F, -3.5F, 13.5F);
		insidesModel[14].rotateAngleY = -1.57079633F;

		insidesModel[15].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan21
		insidesModel[15].setRotationPoint(17F, -6.5F, 16.5F);
		insidesModel[15].rotateAngleY = -1.57079633F;

		insidesModel[16].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan22
		insidesModel[16].setRotationPoint(31F, -6.25F, 13.5F);

		insidesModel[17].addShapeBox(0F, 0F, 0F, 6, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan06
		insidesModel[17].setRotationPoint(19F, -13F, 14F);

		insidesModel[18].addShapeBox(0F, 0F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan06
		insidesModel[18].setRotationPoint(26F, -13F, 14F);

		insidesModel[19].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan02
		insidesModel[19].setRotationPoint(43F, -1F, 11F);
		insidesModel[19].rotateAngleZ = 1.57079633F;

		insidesModel[20].setFlipped(true);
		insidesModel[20].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F); // BoxInsidesMaintan02
		insidesModel[20].setRotationPoint(41F, -1F, 10F);
		insidesModel[20].rotateAngleX = 1.57079633F;

		insidesModel[21].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // BoxInsidesMaintan02
		insidesModel[21].setRotationPoint(41F, -1F, 11F);
		insidesModel[21].rotateAngleX = 1.57079633F;

		insidesModel[22].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan02
		insidesModel[22].setRotationPoint(41F, -1F, 8F);
		insidesModel[22].rotateAngleX = 1.57079633F;

		insidesModel[23].addShapeBox(0F, 0F, 0F, 3, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan21
		insidesModel[23].setRotationPoint(26.5F, -3.5F, 16.5F);

		insidesModel[24].setFlipped(true);
		insidesModel[24].addShapeBox(0F, 0F, 0F, 4, 9, 3, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F); // BoxInsidesMaintan05
		insidesModel[24].setRotationPoint(21F, 0F, 4F);
		insidesModel[24].rotateAngleX = 1.57079633F;

		insidesModel[25].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxInsidesMaintan16
		insidesModel[25].setRotationPoint(27F, -2.5F, 5F);


		lidLeftModel = new ModelRendererTurbo[2];
		lidLeftModel[0] = new ModelRendererTurbo(this, 0, 98, textureX, textureY); // BoxCover02
		lidLeftModel[1] = new ModelRendererTurbo(this, 112, 35, textureX, textureY); // BoxCover04

		lidLeftModel[0].addShapeBox(0F, 0F, -1F, 16, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxCover02
		lidLeftModel[0].setRotationPoint(16F, -11.05F, 0F);

		lidLeftModel[1].addShapeBox(0F, 0F, -1F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxCover04
		lidLeftModel[1].setRotationPoint(23F, -7F, -1F);


		lidRightModel = new ModelRendererTurbo[2];
		lidRightModel[0] = new ModelRendererTurbo(this, 112, 35, textureX, textureY); // BoxCover05
		lidRightModel[1] = new ModelRendererTurbo(this, 0, 98, textureX, textureY); // BoxCover07

		lidRightModel[0].addShapeBox(0F, 0F, -1F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxCover05
		lidRightModel[0].setRotationPoint(39F, -7F, -1F);

		lidRightModel[1].addShapeBox(0F, 0F, -1F, 16, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxCover07
		lidRightModel[1].setRotationPoint(32F, -11.05F, 0F);

		parts.put("base",baseModel);
		parts.put("insides",insidesModel);
		parts.put("lid_left",lidLeftModel);
		parts.put("lid_right",lidRightModel);

		flipAll();

		translate(lidLeftModel,0,-5,0);
		translate(lidRightModel,0,-5,0);
	}

	@Override
	public void getBlockRotation(EnumFacing facing, boolean mirrored)
	{
		switch(facing)
		{
			case EAST:
			{
				GlStateManager.rotate(180, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?-2f: 1f, 0f, 1f);
			}
			break;
			case WEST:
			{
				GlStateManager.translate( mirrored?-1f:2f, 0f,2f);

			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?-2:1, 0f, 2);

			}
			break;
			case NORTH:
			{
				GlStateManager.rotate(-90F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?-1f: 2, 0f, 1f);

			}
			break;
		}
	}
}
