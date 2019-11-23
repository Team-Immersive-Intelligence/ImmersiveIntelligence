package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.BaseBlockModel;
import pl.pabilo8.immersiveintelligence.client.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.tmt.Shape2D;

/**
 * Created by Pabilo8 on 20-06-2019.
 */
public class ModelArtilleryHowitzer extends BaseBlockModel
{
	public ModelRendererTurbo[] cannon_platform, cannon, cannon_barrel, cannon_ammo_door_right, cannon_ammo_door_left, ammo_door, hatch, platform_rod;

	int textureX = 1024;
	int textureY = 1024;

	public ModelArtilleryHowitzer() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[78];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Base
		baseModel[1] = new ModelRendererTurbo(this, 0, 160, textureX, textureY); // Corner1
		baseModel[2] = new ModelRendererTurbo(this, 0, 160, textureX, textureY); // Corner2
		baseModel[3] = new ModelRendererTurbo(this, 0, 160, textureX, textureY); // Corner3
		baseModel[4] = new ModelRendererTurbo(this, 0, 160, textureX, textureY); // Corner4
		baseModel[5] = new ModelRendererTurbo(this, 120, 281, textureX, textureY); // TopBorder1
		baseModel[6] = new ModelRendererTurbo(this, 120, 281, textureX, textureY); // TopBorder2
		baseModel[7] = new ModelRendererTurbo(this, 120, 281, textureX, textureY); // TopBorder3
		baseModel[8] = new ModelRendererTurbo(this, 120, 281, textureX, textureY); // TopBorder4
		baseModel[9] = new ModelRendererTurbo(this, 186, 162, textureX, textureY); // WallB
		baseModel[10] = new ModelRendererTurbo(this, -2, 159, textureX, textureY); // WallF
		baseModel[11] = new ModelRendererTurbo(this, 186, 257, textureX, textureY); // WallL
		baseModel[12] = new ModelRendererTurbo(this, 346, 162, textureX, textureY); // WallR
		baseModel[13] = new ModelRendererTurbo(this, 6, 277, textureX, textureY); // TableCorner1
		baseModel[14] = new ModelRendererTurbo(this, 6, 277, textureX, textureY); // TableCorner2
		baseModel[15] = new ModelRendererTurbo(this, 6, 277, textureX, textureY); // TableCorner3
		baseModel[16] = new ModelRendererTurbo(this, 6, 277, textureX, textureY); // TableCorner4
		baseModel[17] = new ModelRendererTurbo(this, 6, 316, textureX, textureY); // Wire2
		baseModel[18] = new ModelRendererTurbo(this, 190, 262, textureX, textureY); // Wire2_2
		baseModel[19] = new ModelRendererTurbo(this, 170, 276, textureX, textureY); // TableLeg
		baseModel[20] = new ModelRendererTurbo(this, 170, 276, textureX, textureY); // TableLeg
		baseModel[21] = new ModelRendererTurbo(this, 170, 276, textureX, textureY); // TableLeg
		baseModel[22] = new ModelRendererTurbo(this, 170, 276, textureX, textureY); // TableLeg
		baseModel[23] = new ModelRendererTurbo(this, 170, 276, textureX, textureY); // TableLeg
		baseModel[24] = new ModelRendererTurbo(this, 170, 276, textureX, textureY); // TableLeg
		baseModel[25] = new ModelRendererTurbo(this, 170, 276, textureX, textureY); // TableLeg
		baseModel[26] = new ModelRendererTurbo(this, 170, 276, textureX, textureY); // TableLeg
		baseModel[27] = new ModelRendererTurbo(this, 166, 358, textureX, textureY); // Wire3
		baseModel[28] = new ModelRendererTurbo(this, 133, 348, textureX, textureY); // Wire3
		baseModel[29] = new ModelRendererTurbo(this, 201, 355, textureX, textureY); // Wire3
		baseModel[30] = new ModelRendererTurbo(this, 152, 288, textureX, textureY); // Wire3_2
		baseModel[31] = new ModelRendererTurbo(this, 153, 273, textureX, textureY); // Wire3_2
		baseModel[32] = new ModelRendererTurbo(this, 153, 273, textureX, textureY); // Wire3_2
		baseModel[33] = new ModelRendererTurbo(this, 153, 273, textureX, textureY); // Wire3_2
		baseModel[34] = new ModelRendererTurbo(this, 153, 273, textureX, textureY); // Wire3_2
		baseModel[35] = new ModelRendererTurbo(this, 186, 270, textureX, textureY); // Wire3_2
		baseModel[36] = new ModelRendererTurbo(this, 244, 356, textureX, textureY); // BoxWooden1
		baseModel[37] = new ModelRendererTurbo(this, 244, 356, textureX, textureY); // BoxWooden1
		baseModel[38] = new ModelRendererTurbo(this, 293, 356, textureX, textureY); // BoxWooden1
		baseModel[39] = new ModelRendererTurbo(this, 322, 269, textureX, textureY); // Blueprint1
		baseModel[40] = new ModelRendererTurbo(this, 329, 358, textureX, textureY); // BoxMetal1
		baseModel[41] = new ModelRendererTurbo(this, 293, 356, textureX, textureY); // BoxWooden1
		baseModel[42] = new ModelRendererTurbo(this, 290, 374, textureX, textureY); // Book1
		baseModel[43] = new ModelRendererTurbo(this, 222, 377, textureX, textureY); // Book2
		baseModel[44] = new ModelRendererTurbo(this, 260, 378, textureX, textureY); // Book2
		baseModel[45] = new ModelRendererTurbo(this, 374, 358, textureX, textureY); // Blueprint2
		baseModel[46] = new ModelRendererTurbo(this, 374, 358, textureX, textureY); // Blueprint2
		baseModel[47] = new ModelRendererTurbo(this, 244, 356, textureX, textureY); // BoxWooden1
		baseModel[48] = new ModelRendererTurbo(this, 294, 385, textureX, textureY); // BigRedButton1
		baseModel[49] = new ModelRendererTurbo(this, 386, 371, textureX, textureY); // BoxMetal2
		baseModel[50] = new ModelRendererTurbo(this, 386, 371, textureX, textureY); // BoxMetal2
		baseModel[51] = new ModelRendererTurbo(this, 293, 356, textureX, textureY); // BoxWooden1
		baseModel[52] = new ModelRendererTurbo(this, 428, 361, textureX, textureY); // BoxWooden1
		baseModel[53] = new ModelRendererTurbo(this, 428, 361, textureX, textureY); // BoxWooden1
		baseModel[54] = new ModelRendererTurbo(this, 306, 386, textureX, textureY); // LanternBase
		baseModel[55] = new ModelRendererTurbo(this, 323, 386, textureX, textureY); // Lantern
		baseModel[56] = new ModelRendererTurbo(this, 306, 386, textureX, textureY); // LanternTop
		baseModel[57] = new ModelRendererTurbo(this, 428, 361, textureX, textureY); // BoxWooden1
		baseModel[58] = new ModelRendererTurbo(this, 329, 358, textureX, textureY); // BoxMetal1
		baseModel[59] = new ModelRendererTurbo(this, 386, 371, textureX, textureY); // BoxMetal2
		baseModel[60] = new ModelRendererTurbo(this, 293, 356, textureX, textureY); // BoxWooden1
		baseModel[61] = new ModelRendererTurbo(this, 302, 395, textureX, textureY); // DeskLampBase
		baseModel[62] = new ModelRendererTurbo(this, 296, 391, textureX, textureY); // DeskLampRodBase
		baseModel[63] = new ModelRendererTurbo(this, 289, 390, textureX, textureY); // DeskLampRod
		baseModel[64] = new ModelRendererTurbo(this, 349, 388, textureX, textureY); // DeskLamp
		baseModel[65] = new ModelRendererTurbo(this, 322, 269, textureX, textureY); // Blueprint1
		baseModel[66] = new ModelRendererTurbo(this, 434, 376, textureX, textureY); // WallSteelChain1
		baseModel[67] = new ModelRendererTurbo(this, 434, 376, textureX, textureY); // WallSteelChain1
		baseModel[68] = new ModelRendererTurbo(this, 434, 376, textureX, textureY); // WallSteelChain1
		baseModel[69] = new ModelRendererTurbo(this, 434, 376, textureX, textureY); // WallSteelChain1
		baseModel[70] = new ModelRendererTurbo(this, 97, 163, textureX, textureY); // TurntableBase
		baseModel[71] = new ModelRendererTurbo(this, 433, 111, textureX, textureY); // ConnectorEnergy
		baseModel[72] = new ModelRendererTurbo(this, 433, 79, textureX, textureY); // ConnectorRedstone
		baseModel[73] = new ModelRendererTurbo(this, 433, 47, textureX, textureY); // ConnectorData
		baseModel[74] = new ModelRendererTurbo(this, 100, 393, textureX, textureY); // WallBack2
		baseModel[75] = new ModelRendererTurbo(this, 166, 407, textureX, textureY); // WallBack1
		baseModel[76] = new ModelRendererTurbo(this, 269, 408, textureX, textureY); // WallBackSorter
		baseModel[77] = new ModelRendererTurbo(this, 25, 314, textureX, textureY); // FlakAmmoLoaderHatch

		baseModel[0].addBox(0F, 0F, 0F, 144, 16, 144, 0F); // Base
		baseModel[0].setRotationPoint(0F, 0F, 0F);

		baseModel[1].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(32, 0, 32, 0), new Coord2D(48, 16, 48, 16), new Coord2D(48, 48, 48, 48), new Coord2D(0, 48, 0, 48)}), 64, 48, 48, 183, 64, ModelRendererTurbo.MR_FRONT, new float[]{48, 48, 32, 23, 32}); // Corner1
		baseModel[1].setRotationPoint(96F, 0F, 0F);
		baseModel[1].rotateAngleX = -1.57079633F;
		baseModel[1].rotateAngleY = -1.57079633F;

		baseModel[2].addShape3D(72F, 56F, -24F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(32, 0, 32, 0), new Coord2D(48, 16, 48, 16), new Coord2D(48, 48, 48, 48), new Coord2D(0, 48, 0, 48)}), 64, 48, 48, 183, 64, ModelRendererTurbo.MR_FRONT, new float[]{48, 48, 32, 23, 32}); // Corner2
		baseModel[2].setRotationPoint(72F, -24F, 104F);
		baseModel[2].rotateAngleX = -1.57079633F;
		baseModel[2].rotateAngleY = 3.14159265F;

		baseModel[3].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(32, 0, 32, 0), new Coord2D(48, 16, 48, 16), new Coord2D(48, 48, 48, 48), new Coord2D(0, 48, 0, 48)}), 64, 48, 48, 183, 64, ModelRendererTurbo.MR_FRONT, new float[]{48, 48, 32, 23, 32}); // Corner3
		baseModel[3].setRotationPoint(144F, 0F, 96F);
		baseModel[3].rotateAngleX = -1.57079633F;

		baseModel[4].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(32, 0, 32, 0), new Coord2D(48, 16, 48, 16), new Coord2D(48, 48, 48, 48), new Coord2D(0, 48, 0, 48)}), 64, 48, 48, 183, 64, ModelRendererTurbo.MR_FRONT, new float[]{48, 48, 32, 23, 32}); // Corner4
		baseModel[4].setRotationPoint(48F, 0F, 144F);
		baseModel[4].rotateAngleX = -1.57079633F;
		baseModel[4].rotateAngleY = 1.57079633F;

		baseModel[5].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(16, 0, 16, 0), new Coord2D(16, 16, 16, 16), new Coord2D(6, 16, 6, 16)}), 48, 16, 16, 60, 48, ModelRendererTurbo.MR_FRONT, new float[]{18, 10, 16, 16}); // TopBorder1
		baseModel[5].setRotationPoint(32F, -64F, 96F);

		baseModel[6].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(16, 0, 16, 0), new Coord2D(16, 16, 16, 16), new Coord2D(6, 16, 6, 16)}), 48, 16, 16, 60, 48, ModelRendererTurbo.MR_FRONT, new float[]{18, 10, 16, 16}); // TopBorder2
		baseModel[6].setRotationPoint(112F, -64F, 48F);
		baseModel[6].rotateAngleY = -3.14159265F;

		baseModel[7].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(16, 0, 16, 0), new Coord2D(16, 16, 16, 16), new Coord2D(6, 16, 6, 16)}), 48, 16, 16, 60, 48, ModelRendererTurbo.MR_FRONT, new float[]{18, 10, 16, 16}); // TopBorder3
		baseModel[7].setRotationPoint(48F, -64F, 32F);
		baseModel[7].rotateAngleY = 1.57079633F;

		baseModel[8].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(16, 0, 16, 0), new Coord2D(16, 16, 16, 16), new Coord2D(6, 16, 6, 16)}), 48, 16, 16, 60, 48, ModelRendererTurbo.MR_FRONT, new float[]{18, 10, 16, 16}); // TopBorder4
		baseModel[8].setRotationPoint(96F, -64F, 112F);
		baseModel[8].rotateAngleY = 4.71238898F;

		baseModel[9].addBox(0F, 0F, 0F, 48, 64, 32, 0F); // WallB
		baseModel[9].setRotationPoint(48F, -64F, 0F);

		baseModel[10].addBox(0F, 0F, 0F, 48, 63, 1, 0F); // WallF
		baseModel[10].setRotationPoint(96F, -64F, 113F);
		baseModel[10].rotateAngleY = -3.14159265F;

		baseModel[11].addBox(0F, 0F, 0F, 48, 64, 32, 0F); // WallL
		baseModel[11].setRotationPoint(144F, -64F, 48F);
		baseModel[11].rotateAngleY = -4.71238898F;

		baseModel[12].addBox(0F, 0F, 0F, 48, 64, 32, 0F); // WallR
		baseModel[12].setRotationPoint(0F, -64F, 96F);
		baseModel[12].rotateAngleY = -1.57079633F;

		baseModel[13].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(32, 22, 32, 22), new Coord2D(32, 32, 32, 32), new Coord2D(16, 32, 16, 32), new Coord2D(0, 16, 0, 16), new Coord2D(0, 0, 0, 0), new Coord2D(10, 0, 10, 0)}), 2, 32, 32, 107, 2, ModelRendererTurbo.MR_FRONT, new float[]{32, 10, 16, 23, 16, 10}); // TableCorner1
		baseModel[13].setRotationPoint(128F, -78F, 96F);
		baseModel[13].rotateAngleX = -1.57079633F;

		baseModel[14].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(32, 22, 32, 22), new Coord2D(32, 32, 32, 32), new Coord2D(16, 32, 16, 32), new Coord2D(0, 16, 0, 16), new Coord2D(0, 0, 0, 0), new Coord2D(10, 0, 10, 0)}), 2, 32, 32, 107, 2, ModelRendererTurbo.MR_FRONT, new float[]{32, 10, 16, 23, 16, 10}); // TableCorner2
		baseModel[14].setRotationPoint(96F, -78F, 16F);
		baseModel[14].rotateAngleX = -1.57079633F;
		baseModel[14].rotateAngleY = -1.57079633F;

		baseModel[15].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(32, 22, 32, 22), new Coord2D(32, 32, 32, 32), new Coord2D(16, 32, 16, 32), new Coord2D(0, 16, 0, 16), new Coord2D(0, 0, 0, 0), new Coord2D(10, 0, 10, 0)}), 2, 32, 32, 107, 2, ModelRendererTurbo.MR_FRONT, new float[]{32, 10, 16, 23, 16, 10}); // TableCorner3
		baseModel[15].setRotationPoint(16F, -78F, 48F);
		baseModel[15].rotateAngleX = -1.57079633F;
		baseModel[15].rotateAngleY = -3.14159265F;

		baseModel[16].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(32, 22, 32, 22), new Coord2D(32, 32, 32, 32), new Coord2D(16, 32, 16, 32), new Coord2D(0, 16, 0, 16), new Coord2D(0, 0, 0, 0), new Coord2D(10, 0, 10, 0)}), 2, 32, 32, 107, 2, ModelRendererTurbo.MR_FRONT, new float[]{32, 10, 16, 23, 16, 10}); // TableCorner4
		baseModel[16].setRotationPoint(48F, -78F, 128F);
		baseModel[16].rotateAngleX = -1.57079633F;
		baseModel[16].rotateAngleY = 1.57079633F;

		baseModel[17].addBox(0F, 0F, 0F, 4, 56, 4, 0F); // Wire2
		baseModel[17].setRotationPoint(102F, -56F, 40F);
		baseModel[17].rotateAngleY = 0.78539816F;

		baseModel[18].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(2, 4, 2, 4), new Coord2D(8, 4, 8, 4), new Coord2D(8, 0, 8, 0)}), 4, 8, 4, 23, 4, ModelRendererTurbo.MR_FRONT, new float[]{8, 4, 6, 5}, true); // Wire2_2
		baseModel[18].setRotationPoint(99F, -56F, 43F);
		baseModel[18].rotateAngleY = 2.35619449F;

		baseModel[19].addBox(0F, 0F, 0F, 2, 14, 2, 0F); // TableLeg
		baseModel[19].setRotationPoint(110F, -78F, 16F);

		baseModel[20].addBox(0F, 0F, 0F, 2, 14, 2, 0F); // TableLeg
		baseModel[20].setRotationPoint(126F, -78F, 32F);

		baseModel[21].addBox(0F, 0F, 0F, 2, 14, 2, 0F); // TableLeg
		baseModel[21].setRotationPoint(126F, -78F, 110F);

		baseModel[22].addBox(0F, 0F, 0F, 2, 14, 2, 0F); // TableLeg
		baseModel[22].setRotationPoint(110F, -78F, 126F);

		baseModel[23].addBox(0F, 0F, 0F, 2, 14, 2, 0F); // TableLeg
		baseModel[23].setRotationPoint(32F, -78F, 16F);

		baseModel[24].addBox(0F, 0F, 0F, 2, 14, 2, 0F); // TableLeg
		baseModel[24].setRotationPoint(16F, -78F, 32F);

		baseModel[25].addBox(0F, 0F, 0F, 2, 14, 2, 0F); // TableLeg
		baseModel[25].setRotationPoint(32F, -78F, 126F);

		baseModel[26].addBox(0F, 0F, 0F, 2, 14, 2, 0F); // TableLeg
		baseModel[26].setRotationPoint(16F, -78F, 110F);

		baseModel[27].addBox(0F, 0F, 0F, 4, 4, 13, 0F); // Wire3
		baseModel[27].setRotationPoint(44F, -48F, 36F);
		baseModel[27].rotateAngleY = 0.78539816F;

		baseModel[28].addBox(0F, 0F, 0F, 4, 4, 18, 0F); // Wire3
		baseModel[28].setRotationPoint(48F, -56F, 32F);
		baseModel[28].rotateAngleY = 0.78539816F;

		baseModel[29].addBox(0F, 0F, 0F, 4, 4, 16, 0F); // Wire3
		baseModel[29].setRotationPoint(46F, -40F, 34F);
		baseModel[29].rotateAngleY = 0.78539816F;

		baseModel[30].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(2, 4, 2, 4), new Coord2D(5, 4, 5, 4), new Coord2D(5, 0, 5, 0)}), 4, 5, 4, 17, 4, ModelRendererTurbo.MR_FRONT, new float[]{5, 4, 3, 5}); // Wire3_2
		baseModel[30].setRotationPoint(32F, -56F, 48F);
		baseModel[30].rotateAngleX = 1.57079633F;
		baseModel[30].rotateAngleY = 2.35619449F;

		baseModel[31].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(2, 4, 2, 4), new Coord2D(4, 4, 4, 4), new Coord2D(4, 0, 4, 0)}), 4, 4, 4, 15, 4, ModelRendererTurbo.MR_FRONT, new float[]{4, 4, 2, 5}); // Wire3_2
		baseModel[31].setRotationPoint(47F, -48F, 39F);
		baseModel[31].rotateAngleX = 1.57079633F;
		baseModel[31].rotateAngleY = 0.78539816F;

		baseModel[32].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(2, 4, 2, 4), new Coord2D(4, 4, 4, 4), new Coord2D(4, 0, 4, 0)}), 4, 4, 4, 15, 4, ModelRendererTurbo.MR_FRONT, new float[]{4, 4, 2, 5}); // Wire3_2
		baseModel[32].setRotationPoint(32F, -40F, 48F);
		baseModel[32].rotateAngleX = 1.57079633F;
		baseModel[32].rotateAngleY = 2.35619449F;

		baseModel[33].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(2, 4, 2, 4), new Coord2D(4, 4, 4, 4), new Coord2D(4, 0, 4, 0)}), 4, 4, 4, 15, 4, ModelRendererTurbo.MR_FRONT, new float[]{4, 4, 2, 5}); // Wire3_2
		baseModel[33].setRotationPoint(32F, -48F, 48F);
		baseModel[33].rotateAngleX = 1.57079633F;
		baseModel[33].rotateAngleY = 2.35619449F;

		baseModel[34].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(2, 4, 2, 4), new Coord2D(4, 4, 4, 4), new Coord2D(4, 0, 4, 0)}), 4, 4, 4, 15, 4, ModelRendererTurbo.MR_FRONT, new float[]{4, 4, 2, 5}); // Wire3_2
		baseModel[34].setRotationPoint(49F, -40F, 37F);
		baseModel[34].rotateAngleX = 1.57079633F;
		baseModel[34].rotateAngleY = 0.78539816F;

		baseModel[35].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 3, 0, 3), new Coord2D(2, 8, 2, 8), new Coord2D(4, 8, 4, 8), new Coord2D(4, 0, 4, 0)}), 4, 4, 8, 21, 4, ModelRendererTurbo.MR_FRONT, new float[]{5, 8, 2, 6}); // Wire3_2
		baseModel[35].setRotationPoint(47F, -56F, 35F);
		baseModel[35].rotateAngleX = 1.57079633F;
		baseModel[35].rotateAngleY = 1.57079633F;

		baseModel[36].addBox(0F, 0F, 0F, 14, 10, 10, 0F); // BoxWooden1
		baseModel[36].setRotationPoint(30F, -74F, 98F);
		baseModel[36].rotateAngleY = 1.32645023F;

		baseModel[37].addBox(0F, 0F, 0F, 14, 10, 10, 0F); // BoxWooden1
		baseModel[37].setRotationPoint(29F, -74F, 117F);
		baseModel[37].rotateAngleY = -0.61086524F;

		baseModel[38].addBox(0F, 0F, 0F, 14, 5, 10, 0F); // BoxWooden1
		baseModel[38].setRotationPoint(15F, -85F, 109F);
		baseModel[38].rotateAngleY = -1.11701072F;

		baseModel[39].addBox(0F, 0F, 0F, 14, 1, 10, 0F); // Blueprint1
		baseModel[39].setRotationPoint(25F, -80.2F, 117F);
		baseModel[39].rotateAngleY = -0.89011792F;

		baseModel[40].addBox(0F, 0F, 0F, 14, 14, 14, 0F); // BoxMetal1
		baseModel[40].setRotationPoint(94F, -78F, 116F);
		baseModel[40].rotateAngleY = -0.61086524F;

		baseModel[41].addBox(0F, 0F, 0F, 14, 5, 10, 0F); // BoxWooden1
		baseModel[41].setRotationPoint(101F, -85F, 116F);
		baseModel[41].rotateAngleY = 0.31415927F;

		baseModel[42].addBox(0F, 0F, 0F, 10, 2, 8, 0F); // Book1
		baseModel[42].setRotationPoint(112F, -82F, 104F);
		baseModel[42].rotateAngleY = 0.54105207F;

		baseModel[43].addBox(0F, 0F, 0F, 10, 2, 8, 0F); // Book2
		baseModel[43].setRotationPoint(110F, -84F, 105F);
		baseModel[43].rotateAngleY = 0.13962634F;

		baseModel[44].addBox(0F, 0F, 0F, 10, 2, 8, 0F); // Book2
		baseModel[44].setRotationPoint(110F, -86F, 105F);
		baseModel[44].rotateAngleY = 0.2268928F;

		baseModel[45].addBox(0F, 0F, 0F, 14, 1, 10, 0F); // Blueprint2
		baseModel[45].setRotationPoint(108F, -80.2F, 114F);
		baseModel[45].rotateAngleY = -0.89011792F;

		baseModel[46].addBox(0F, 0F, 0F, 14, 1, 10, 0F); // Blueprint2
		baseModel[46].setRotationPoint(115F, -80.2F, 103F);
		baseModel[46].rotateAngleY = -1.36135682F;

		baseModel[47].addBox(0F, 0F, 0F, 14, 10, 10, 0F); // BoxWooden1
		baseModel[47].setRotationPoint(121F, -74F, 98F);
		baseModel[47].rotateAngleY = 1.32645023F;

		baseModel[48].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // BigRedButton1
		baseModel[48].setRotationPoint(118F, -81F, 104F);
		baseModel[48].rotateAngleY = -0.33161256F;

		baseModel[49].addBox(0F, 0F, 0F, 14, 10, 10, 0F); // BoxMetal2
		baseModel[49].setRotationPoint(118F, -74F, 31F);
		baseModel[49].rotateAngleY = 0.43633231F;

		baseModel[50].addBox(0F, 0F, 0F, 14, 10, 10, 0F); // BoxMetal2
		baseModel[50].setRotationPoint(111F, -74F, 20F);
		baseModel[50].rotateAngleY = 1.18682389F;

		baseModel[51].addBox(0F, 0F, 0F, 14, 5, 10, 0F); // BoxWooden1
		baseModel[51].setRotationPoint(119F, -85F, 47F);
		baseModel[51].rotateAngleY = -2.35619449F;

		baseModel[52].addBox(0F, 0F, 0F, 14, 5, 10, 0F); // BoxWooden1
		baseModel[52].setRotationPoint(116F, -85F, 30F);
		baseModel[52].rotateAngleY = -3.35103216F;

		baseModel[53].addBox(0F, 0F, 0F, 14, 5, 10, 0F); // BoxWooden1
		baseModel[53].setRotationPoint(116F, -90F, 39F);
		baseModel[53].rotateAngleY = -2.67035376F;

		baseModel[54].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // LanternBase
		baseModel[54].setRotationPoint(23F, -81F, 41F);
		baseModel[54].rotateAngleY = -1.11701072F;

		baseModel[55].addBox(0F, 0F, 0F, 8, 11, 8, 0F); // Lantern
		baseModel[55].setRotationPoint(21.5F, -92F, 41F);
		baseModel[55].rotateAngleY = -1.11701072F;

		baseModel[56].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // LanternTop
		baseModel[56].setRotationPoint(23F, -93F, 41F);
		baseModel[56].rotateAngleY = -1.11701072F;

		baseModel[57].addBox(0F, 0F, 0F, 14, 5, 10, 0F); // BoxWooden1
		baseModel[57].setRotationPoint(49F, -85F, 24F);
		baseModel[57].rotateAngleY = -3.17649924F;

		baseModel[58].addBox(0F, 0F, 0F, 14, 14, 14, 0F); // BoxMetal1
		baseModel[58].setRotationPoint(27F, -78F, 27F);
		baseModel[58].rotateAngleY = -0.61086524F;

		baseModel[59].addBox(0F, 0F, 0F, 14, 10, 10, 0F); // BoxMetal2
		baseModel[59].setRotationPoint(21F, -74F, 30F);
		baseModel[59].rotateAngleY = 0.43633231F;

		baseModel[60].addBox(0F, 0F, 0F, 14, 5, 10, 0F); // BoxWooden1
		baseModel[60].setRotationPoint(36F, -85F, 29F);
		baseModel[60].rotateAngleY = 2.75762022F;

		baseModel[61].addBox(0F, 0F, 0F, 4, 1, 6, 0F); // DeskLampBase
		baseModel[61].setRotationPoint(41F, -81F, 116F);
		baseModel[61].rotateAngleY = -0.40142573F;

		baseModel[62].addTrapezoid(0F, 0F, 0F, 3, 1, 3, 0F, -1.00F, ModelRendererTurbo.MR_TOP); // DeskLampRodBase
		baseModel[62].setRotationPoint(41.5F, -81.5F, 116.5F);
		baseModel[62].rotateAngleY = -0.40142573F;

		baseModel[63].addBox(-0.5F, -10F, 0F, 1, 12, 1, 0F); // DeskLampRod
		baseModel[63].setRotationPoint(44.5F, -81F, 117F);
		baseModel[63].rotateAngleY = 0.41887902F;
		baseModel[63].rotateAngleZ = 0.54105207F;

		baseModel[64].addTrapezoid(0F, -1F, -1.5F, 6, 1, 3, 0F, -1.00F, ModelRendererTurbo.MR_TOP); // DeskLamp
		baseModel[64].setRotationPoint(40F, -89.5F, 115.5F);
		baseModel[64].rotateAngleY = 3.19395253F;
		baseModel[64].rotateAngleZ = 0.05235988F;

		baseModel[65].addBox(0F, 0F, 0F, 14, 1, 10, 0F); // Blueprint1
		baseModel[65].setRotationPoint(24F, -81.2F, 32F);
		baseModel[65].rotateAngleX = -0.10471976F;
		baseModel[65].rotateAngleY = -0.59341195F;

		baseModel[66].addBox(0F, 0F, 0F, 32, 14, 0, 0F); // WallSteelChain1
		baseModel[66].setRotationPoint(94.5F, -78F, 27F);
		baseModel[66].rotateAngleY = 0.78539816F;
		baseModel[66].flip = true;

		baseModel[67].addBox(0F, 0F, 0F, 32, 14, 0, 0F); // WallSteelChain1
		baseModel[67].setRotationPoint(116.5F, -78F, 94F);
		baseModel[67].rotateAngleY = 2.35619449F;

		baseModel[68].addBox(0F, 0F, 0F, 32, 14, 0, 0F); // WallSteelChain1
		baseModel[68].setRotationPoint(27.5F, -78F, 94F);
		baseModel[68].rotateAngleY = 0.78539816F;

		baseModel[69].addBox(0F, 0F, 0F, 32, 14, 0, 0F); // WallSteelChain1
		baseModel[69].setRotationPoint(49.5F, -78F, 28F);
		baseModel[69].rotateAngleY = 2.35619449F;

		baseModel[70].addTrapezoid(-4F, -1F, -4F, 8, 1, 8, 0F, -1.00F, ModelRendererTurbo.MR_TOP); // TurntableBase
		baseModel[70].setRotationPoint(72F, 0F, 72F);

		baseModel[71].addBox(0F, 0F, 0F, 48, 16, 16, 0F); // ConnectorEnergy
		baseModel[71].setRotationPoint(144F, -80F, 48F);
		baseModel[71].rotateAngleY = -4.71238898F;

		baseModel[72].addBox(0F, 0F, 0F, 48, 16, 16, 0F); // ConnectorRedstone
		baseModel[72].setRotationPoint(48F, -80F, 0F);
		baseModel[72].rotateAngleY = 0.01745329F;

		baseModel[73].addBox(0F, 0F, 0F, 48, 16, 16, 0F); // ConnectorData
		baseModel[73].setRotationPoint(0F, -80F, 96F);
		baseModel[73].rotateAngleY = -1.57079633F;

		baseModel[74].addBox(0F, 0F, 0F, 16, 80, 16, 0F); // WallBack2
		baseModel[74].setRotationPoint(80F, -80F, 144F);
		baseModel[74].rotateAngleY = -3.14159265F;

		baseModel[75].addBox(0F, 0F, 0F, 48, 64, 2, 0F); // WallBack1
		baseModel[75].setRotationPoint(96F, -64F, 144F);
		baseModel[75].rotateAngleY = -3.14159265F;

		baseModel[76].addBox(0F, 0F, 0F, 16, 64, 15, 0F); // WallBackSorter
		baseModel[76].setRotationPoint(80F, -64F, 128F);
		baseModel[76].rotateAngleY = -3.14159265F;

		baseModel[77].addBox(0F, 0F, 0F, 24, 24, 2, 0F); // FlakAmmoLoaderHatch
		baseModel[77].setRotationPoint(84F, -27F, 112F);
		baseModel[77].rotateAngleY = -3.14159265F;

		translateAll(0F, 0F, 0F);

		//

		cannon_barrel = new ModelRendererTurbo[4];
		cannon_barrel[0] = new ModelRendererTurbo(this, 78, 96, textureX, textureY); // FlakBarrel2
		cannon_barrel[1] = new ModelRendererTurbo(this, 105, 116, textureX, textureY); // FlakMuzzle
		cannon_barrel[2] = new ModelRendererTurbo(this, 122, 113, textureX, textureY); // FlakMuzzle2
		cannon_barrel[3] = new ModelRendererTurbo(this, 104, 107, textureX, textureY); // FlakMuzzle


		cannon_barrel[0].addTrapezoid(-1.5F, -74F, -7.5F, 6, 38, 6, 0F, -1.00F, ModelRendererTurbo.MR_TOP); // FlakBarrel2
		cannon_barrel[0].setRotationPoint(0F, 28F, 0F);

		cannon_barrel[1].addTrapezoid(-1F, -76F, -7F, 5, 2, 5, 0F, -0.50F, ModelRendererTurbo.MR_BOTTOM); // FlakMuzzle
		cannon_barrel[1].setRotationPoint(0F, 28F, 0F);

		cannon_barrel[2].addBox(-1F, -78F, -7F, 5, 2, 5, 0F); // FlakMuzzle2
		cannon_barrel[2].setRotationPoint(0F, 28F, 0F);

		cannon_barrel[3].addTrapezoid(-1F, -81F, -7F, 5, 3, 5, 0F, -0.50F, ModelRendererTurbo.MR_TOP); // FlakMuzzle
		cannon_barrel[3].setRotationPoint(0F, 28F, 0F);

		//

		cannon_platform = new ModelRendererTurbo[6];
		cannon_platform[0] = new ModelRendererTurbo(this, 111, 174, textureX, textureY); // TurntableRodEnd
		cannon_platform[1] = new ModelRendererTurbo(this, 1, 347, textureX, textureY); // TurntablePlatform
		cannon_platform[2] = new ModelRendererTurbo(this, 131, 162, textureX, textureY); // FlakSidePlate
		cannon_platform[3] = new ModelRendererTurbo(this, 111, 174, textureX, textureY); // TurntableRodEnd
		cannon_platform[4] = new ModelRendererTurbo(this, 131, 162, textureX, textureY); // FlakSidePlate
		cannon_platform[5] = new ModelRendererTurbo(this, 63, 89, textureX, textureY); // TurntableRodAxle

		cannon_platform[0].addBox(6.5F, -24F, -1.5F, 3, 20, 3, 0F); // TurntableRodEnd

		cannon_platform[1].addBox(-21F, -4F, -21F, 42, 3, 42, 0F); // TurntablePlatform
		cannon_platform[1].flip = true;

		cannon_platform[2].addShape3D(6.5F, 0F, -7F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(12, 0, 12, 0), new Coord2D(12, 20, 12, 20), new Coord2D(4, 20, 4, 20), new Coord2D(0, 16, 0, 16)}), 1, 12, 20, 62, 1, ModelRendererTurbo.MR_FRONT, new float[]{16, 6, 8, 20, 12}); // FlakSidePlate
		cannon_platform[2].setRotationPoint(0, -16F, 0);
		cannon_platform[2].rotateAngleY = 1.57079633F;

		cannon_platform[3].addBox(-6.5F, -24F, -1.5F, 3, 20, 3, 0F); // TurntableRodEnd

		cannon_platform[4].addShape3D(6.5F, 0F, 9F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(12, 0, 12, 0), new Coord2D(12, 20, 12, 20), new Coord2D(4, 20, 4, 20), new Coord2D(0, 16, 0, 16)}), 1, 12, 20, 62, 1, ModelRendererTurbo.MR_FRONT, new float[]{16, 6, 8, 20, 12}); // FlakSidePlate
		cannon_platform[4].setRotationPoint(0, -16F, 0);
		cannon_platform[4].rotateAngleY = 1.57079633F;

		cannon_platform[5].addBox(-7.5F, -27F, -1.5F, 18, 3, 3, 0F); // TurntableRodAxle


		//

		platform_rod = new ModelRendererTurbo[1];
		platform_rod[0] = new ModelRendererTurbo(this, 98, 174, textureX, textureY); // TurntableRod1
		platform_rod[0].addBox(-1.5F, -2F, -1.5F, 3, 16, 3, 0F); // TurntableRod1

		//

		hatch = new ModelRendererTurbo[2];
		hatch[0] = new ModelRendererTurbo(this, 7, 397, textureX, textureY); // HatchFront
		hatch[1] = new ModelRendererTurbo(this, 7, 397, textureX, textureY); // HatchBack

		hatch[0].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(46, 92, 46, 92), new Coord2D(22, 92, 22, 92), new Coord2D(0, 70, 0, 70), new Coord2D(0, 22, 0, 22), new Coord2D(22, 0, 22, 0), new Coord2D(46, 0, 46, 0)}), 2, 46, 92, 252, 2, ModelRendererTurbo.MR_FRONT, new float[]{92, 24, 32, 48, 32, 24}); // HatchFront
		hatch[0].rotateAngleX = -1.57079633F;
		hatch[0].rotateAngleY = -1.57079633F;

		hatch[1].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(46, 92, 46, 92), new Coord2D(21, 92, 21, 92), new Coord2D(0, 70, 0, 70), new Coord2D(0, 22, 0, 22), new Coord2D(22, 0, 22, 0), new Coord2D(46, 0, 46, 0)}), 2, 46, 92, 252, 2, ModelRendererTurbo.MR_FRONT, new float[]{92, 24, 32, 48, 31, 25}); // HatchBack
		hatch[1].rotateAngleX = -1.57079633F;
		hatch[1].rotateAngleY = -1.57079633F;
		hatch[1].rotateAngleZ = -3.14159265F;

		//

		ammo_door = new ModelRendererTurbo[3];
		ammo_door[0] = new ModelRendererTurbo(this, 406, 396, textureX, textureY); // FlakAmmoLoader2
		ammo_door[1] = new ModelRendererTurbo(this, 82, 317, textureX, textureY); // FlakAmmoLoader1
		ammo_door[2] = new ModelRendererTurbo(this, 75, 279, textureX, textureY); // FlakAmmoLoader1

		ammo_door[0].addBox(0F, 0F, 0F, 2, 2, 34, 0F); // FlakAmmoLoader2
		ammo_door[0].setRotationPoint(74F, -15F, 143F);
		ammo_door[0].rotateAngleY = -3.14159265F;

		ammo_door[1].addShape3D(-0.5F, -1.5F, 0F, new Shape2D(new Coord2D[]{new Coord2D(4, 0, 4, 0), new Coord2D(6, 0, 6, 0), new Coord2D(6, 5, 6, 5), new Coord2D(0, 5, 0, 5), new Coord2D(0, 4, 0, 4), new Coord2D(3, 4, 3, 4), new Coord2D(4, 3, 4, 3)}), 16, 6, 5, 22, 16, ModelRendererTurbo.MR_FRONT, new float[]{3, 2, 3, 1, 6, 5, 2}); // FlakAmmoLoader1
		ammo_door[1].setRotationPoint(72F, -24F, 103F);
		ammo_door[1].rotateAngleX = -1.57079633F;
		ammo_door[1].rotateAngleY = 1.57079633F;
		ammo_door[1].rotateAngleZ = 3.14159265F;

		ammo_door[2].addShape3D(-0.5F, -6.5F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(6, 0, 6, 0), new Coord2D(6, 5, 6, 5), new Coord2D(4, 5, 4, 5), new Coord2D(4, 2, 4, 2), new Coord2D(3, 1, 3, 1), new Coord2D(0, 1, 0, 1)}), 16, 6, 5, 22, 16, ModelRendererTurbo.MR_FRONT, new float[]{1, 3, 2, 3, 2, 5, 6}); // FlakAmmoLoader1
		ammo_door[2].setRotationPoint(72F, -24F, 103F);
		ammo_door[2].rotateAngleX = -1.57079633F;
		ammo_door[2].rotateAngleY = 1.57079633F;
		ammo_door[2].rotateAngleZ = 3.14159265F;

		//

		cannon_ammo_door_right = new ModelRendererTurbo[1];
		cannon_ammo_door_left = new ModelRendererTurbo[1];
		cannon_ammo_door_left[0] = new ModelRendererTurbo(this, 125, 184, textureX, textureY); // FlakAmmoTop
		cannon_ammo_door_right[0] = new ModelRendererTurbo(this, 159, 160, textureX, textureY); // FlakAmmoBottom

		cannon_ammo_door_left[0].addShape3D(-0.5F, -1.5F, 0F, new Shape2D(new Coord2D[]{new Coord2D(4, 0, 4, 0), new Coord2D(6, 0, 6, 0), new Coord2D(6, 5, 6, 5), new Coord2D(0, 5, 0, 5), new Coord2D(0, 4, 0, 4), new Coord2D(3, 4, 3, 4), new Coord2D(4, 3, 4, 3)}), 16, 6, 5, 22, 16, ModelRendererTurbo.MR_FRONT, new float[]{3, 2, 3, 1, 6, 5, 2}); // FlakAmmoTop
		cannon_ammo_door_left[0].setRotationPoint(0F, 4F, -3F);
		cannon_ammo_door_left[0].rotateAngleX = -1.57079633F;
		cannon_ammo_door_left[0].rotateAngleY = 1.57079633F;
		cannon_ammo_door_left[0].rotateAngleZ = 3.14159265F;

		cannon_ammo_door_right[0].addShape3D(-0.5F, -6.5F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(6, 0, 6, 0), new Coord2D(6, 5, 6, 5), new Coord2D(4, 5, 4, 5), new Coord2D(4, 2, 4, 2), new Coord2D(3, 1, 3, 1), new Coord2D(0, 1, 0, 1)}), 16, 6, 5, 22, 16, ModelRendererTurbo.MR_FRONT, new float[]{1, 3, 2, 3, 2, 5, 6}); // FlakAmmoBottom
		cannon_ammo_door_right[0].setRotationPoint(0F, 4F, -3F);
		cannon_ammo_door_right[0].rotateAngleX = -1.57079633F;
		cannon_ammo_door_right[0].rotateAngleY = 1.57079633F;
		cannon_ammo_door_right[0].rotateAngleZ = 3.14159265F;

		//

		cannon = new ModelRendererTurbo[11];
		cannon[0] = new ModelRendererTurbo(this, 104, 124, textureX, textureY); // FlakCentralBox
		cannon[1] = new ModelRendererTurbo(this, 159, 160, textureX, textureY); // FlakAmmoBottom
		cannon[2] = new ModelRendererTurbo(this, 125, 184, textureX, textureY); // FlakAmmoBottom
		cannon[3] = new ModelRendererTurbo(this, 52, 131, textureX, textureY); // FlakBarrelThing1
		cannon[4] = new ModelRendererTurbo(this, 53, 96, textureX, textureY); // FlakBarrelThing2
		cannon[5] = new ModelRendererTurbo(this, 120, 98, textureX, textureY); // FlakBarrelThing3
		cannon[6] = new ModelRendererTurbo(this, 106, 87, textureX, textureY); // FlakBarrelBottom1
		cannon[7] = new ModelRendererTurbo(this, 106, 76, textureX, textureY); // FlakBarrelBottom2
		cannon[8] = new ModelRendererTurbo(this, 1, 50, textureX, textureY); // FlakSidePlateLeft
		cannon[9] = new ModelRendererTurbo(this, 1, 50, textureX, textureY); // FlakSidePlateRight
		cannon[10] = new ModelRendererTurbo(this, 148, 185, textureX, textureY); // FlakBarrel1

		cannon[0].addShape3D(3.5F, 8F, -3.5F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(12, 0, 12, 0), new Coord2D(12, 8, 12, 8), new Coord2D(4, 8, 4, 8), new Coord2D(0, 5, 0, 5)}), 10, 12, 8, 38, 10, ModelRendererTurbo.MR_FRONT, new float[]{5, 5, 8, 8, 12}); // FlakCentralBox
		cannon[0].setRotationPoint(0, 12F, 0);
		cannon[0].rotateAngleY = 1.57079633F;

		cannon[1].addShape3D(-2.5F, -6.5F, -8F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(6, 0, 6, 0), new Coord2D(6, 5, 6, 5), new Coord2D(4, 5, 4, 5), new Coord2D(4, 2, 4, 2), new Coord2D(3, 1, 3, 1), new Coord2D(0, 1, 0, 1)}), 16, 6, 5, 22, 16, ModelRendererTurbo.MR_FRONT, new float[]{1, 3, 2, 3, 2, 5, 6}); // FlakAmmoBottom
		cannon[1].setRotationPoint(0, 12F, 0);
		cannon[1].rotateAngleX = -1.57079633F;
		cannon[1].rotateAngleY = 1.57079633F;

		cannon[2].addShape3D(-2.5F, -1.5F, -8F, new Shape2D(new Coord2D[]{new Coord2D(4, 0, 4, 0), new Coord2D(6, 0, 6, 0), new Coord2D(6, 5, 6, 5), new Coord2D(0, 5, 0, 5), new Coord2D(0, 4, 0, 4), new Coord2D(3, 4, 3, 4), new Coord2D(4, 3, 4, 3)}), 16, 6, 5, 22, 16, ModelRendererTurbo.MR_FRONT, new float[]{3, 2, 3, 1, 6, 5, 2}); // FlakAmmoBottom
		cannon[2].setRotationPoint(0F, 12F, 0F);
		cannon[2].rotateAngleX = -1.57079633F;
		cannon[2].rotateAngleY = 1.57079633F;

		cannon[3].addTrapezoid(-2.5F, -50F, -14.5F, 6, 4, 6, 0F, -1.00F, ModelRendererTurbo.MR_TOP); // FlakBarrelThing1
		cannon[3].setRotationPoint(0F, 30F, 0F);

		cannon[4].addBox(-2.5F, -46F, -14.5F, 6, 28, 6, 0F); // FlakBarrelThing2
		cannon[4].setRotationPoint(0F, 30F, 0F);

		cannon[5].addShapeBox(-2.5F, -15F, -14.5F, 6, 7, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, -2F, -2F, 0F, -2F, -2F, 0F, 0F, -2F, 0F, 0F); // FlakBarrelThing3
		cannon[5].setRotationPoint(0F, 27F, 0F);

		cannon[6].addTrapezoid(-3.5F, -8F, -8.5F, 10, 1, 8, 0F, -0.75F, ModelRendererTurbo.MR_BOTTOM); // FlakBarrelBottom1
		cannon[6].setRotationPoint(0F, 27F, 0F);

		cannon[7].addTrapezoid(-3.5F, -7F, -8.5F, 10, 2, 8, 0F, -0.75F, ModelRendererTurbo.MR_TOP); // FlakBarrelBottom2
		cannon[7].setRotationPoint(0F, 27F, 0F);

		cannon[8].addShape3D(17.5F, -10F, -23F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(14, 0, 14, 0), new Coord2D(14, 32, 14, 32), new Coord2D(4, 32, 4, 32), new Coord2D(0, 28, 0, 28)}), 1, 14, 32, 90, 1, ModelRendererTurbo.MR_FRONT, new float[]{28, 6, 10, 32, 14}); // FlakSidePlateLeft
		cannon[8].setRotationPoint(0, 10F, 0);
		cannon[8].rotateAngleX = 1.57079633F;

		cannon[9].addShape3D(14.5F, -10F, 22F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(14, 0, 14, 0), new Coord2D(14, 32, 14, 32), new Coord2D(4, 32, 4, 32), new Coord2D(0, 28, 0, 28)}), 1, 14, 32, 90, 1, ModelRendererTurbo.MR_FRONT, new float[]{28, 6, 10, 32, 14}); // FlakSidePlateRight
		cannon[9].setRotationPoint(0, 10F, 0);
		cannon[9].rotateAngleX = 1.57079633F;
		cannon[9].rotateAngleZ = 3.14159265F;

		cannon[10].addTrapezoid(-2.5F, -36F, -8.5F, 8, 4, 8, 0F, -0.75F, ModelRendererTurbo.MR_TOP); // FlakBarrel1
		cannon[10].setRotationPoint(0F, 28F, 0F);

		flipAll();
	}

	@Override
	public void flipAll()
	{
		super.flipAll();
		flip(cannon_platform);
		flip(cannon);
		flip(cannon_barrel);
		flip(cannon_ammo_door_right);
		flip(cannon_ammo_door_left);
		flip(ammo_door);
		flip(hatch);
		flip(platform_rod);
	}

	@Override
	public void rotateAll(float x, float y, float z)
	{
		super.rotateAll(x, y, z);
		rotate(cannon_platform, x, y, z);
		rotate(cannon, x, y, z);
		rotate(cannon_barrel, x, y, z);
		rotate(cannon_ammo_door_right, x, y, z);
		rotate(cannon_ammo_door_left, x, y, z);
		rotate(ammo_door, x, y, z);
		rotate(hatch, x, y, z);
		rotate(platform_rod, x, y, z);
	}

	@Override
	public void rotateAddAll(float x, float y, float z)
	{
		super.rotateAddAll(x, y, z);
		addRotation(cannon_platform, x, y, z);
		addRotation(cannon, x, y, z);
		addRotation(cannon_barrel, x, y, z);
		addRotation(cannon_ammo_door_right, x, y, z);
		addRotation(cannon_ammo_door_left, x, y, z);
		addRotation(ammo_door, x, y, z);
		addRotation(hatch, x, y, z);
		addRotation(platform_rod, x, y, z);
	}

	@Override
	public void render()
	{
		super.render();
	}

	@Override
	public void getBlockRotation(EnumFacing facing, BaseBlockModel model)
	{
		switch(facing)
		{
			case WEST:
			{
				GlStateManager.translate(-2f, 0f, 12f);
			}
			break;
			case EAST:
			{
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(-7f, 0f, 5f);

			}
			break;
			case NORTH:
			{

				GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(-1f, 0f, 6f);
			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-8f, 0f, 11f);
			}
			break;
		}
	}

	public void getModelCounterRotation(EnumFacing facing)
	{
		switch(facing)
		{
			case WEST:
			{

			}
			break;
			case EAST:
			{
				GlStateManager.rotate(-180F, 0F, 1F, 0F);
				GlStateManager.translate(-9f, 0f, 9f);

			}
			break;
			case NORTH:
			{

				GlStateManager.rotate(-270F, 0F, 1F, 0F);
				GlStateManager.translate(0f, 0f, 9f);
			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(-90F, 0F, 1F, 0F);
				GlStateManager.translate(-9f, 0f, 0f);
			}
			break;
		}
	}
}
