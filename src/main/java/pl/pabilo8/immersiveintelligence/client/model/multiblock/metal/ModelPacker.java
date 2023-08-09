package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * @author Pabilo8
 * @since 26.09.2021
 */
public class ModelPacker extends ModelIIBase
{
	public ModelRendererTurbo[] loaderLidRightModel, loaderLidLeftModel, clampModel, clampRightModel, clampLeftModel, markerOutputModel, markerInputModel;

	int textureX = 256;
	int textureY = 128;

	public ModelPacker() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[52];
		baseModel[0] = new ModelRendererTurbo(this, 2, 0, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 128, 89, textureX, textureY); // Box 1
		baseModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 3
		baseModel[3] = new ModelRendererTurbo(this, 0, 96, textureX, textureY); // Box 5
		baseModel[4] = new ModelRendererTurbo(this, 112, 32, textureX, textureY); // Box 8
		baseModel[5] = new ModelRendererTurbo(this, 4, 24, textureX, textureY); // Box 13
		baseModel[6] = new ModelRendererTurbo(this, 102, 32, textureX, textureY); // Box 15
		baseModel[7] = new ModelRendererTurbo(this, 98, 32, textureX, textureY); // Box 16
		baseModel[8] = new ModelRendererTurbo(this, 98, 32, textureX, textureY); // Box 17
		baseModel[9] = new ModelRendererTurbo(this, 130, 0, textureX, textureY); // Box 22
		baseModel[10] = new ModelRendererTurbo(this, 6, 94, textureX, textureY); // Box 24
		baseModel[11] = new ModelRendererTurbo(this, 6, 94, textureX, textureY); // Box 25
		baseModel[12] = new ModelRendererTurbo(this, 104, 0, textureX, textureY); // Box 26
		baseModel[13] = new ModelRendererTurbo(this, 0, 19, textureX, textureY); // Box 27
		baseModel[14] = new ModelRendererTurbo(this, 32, 78, textureX, textureY); // Box 28
		baseModel[15] = new ModelRendererTurbo(this, 130, 56, textureX, textureY); // Box 30
		baseModel[16] = new ModelRendererTurbo(this, 236, 96, textureX, textureY); // Box 32
		baseModel[17] = new ModelRendererTurbo(this, 236, 96, textureX, textureY); // Box 33
		baseModel[18] = new ModelRendererTurbo(this, 189, 40, textureX, textureY); // Box 35
		baseModel[19] = new ModelRendererTurbo(this, 112, 96, textureX, textureY); // Box 36
		baseModel[20] = new ModelRendererTurbo(this, 0, 78, textureX, textureY); // FENCE01
		baseModel[21] = new ModelRendererTurbo(this, 50, 78, textureX, textureY); // Box 27
		baseModel[22] = new ModelRendererTurbo(this, 112, 32, textureX, textureY); // Box 8
		baseModel[23] = new ModelRendererTurbo(this, 66, 0, textureX, textureY); // Shape 50
		baseModel[24] = new ModelRendererTurbo(this, 160, 57, textureX, textureY); // Box 1
		baseModel[25] = new ModelRendererTurbo(this, 130, 33, textureX, textureY); // Box 1
		baseModel[26] = new ModelRendererTurbo(this, 95, 72, textureX, textureY); // Box 1
		baseModel[27] = new ModelRendererTurbo(this, 128, 89, textureX, textureY); // Box 1
		baseModel[28] = new ModelRendererTurbo(this, 192, 0, textureX, textureY); // Box 1
		baseModel[29] = new ModelRendererTurbo(this, 2, 0, textureX, textureY); // Box 0
		baseModel[30] = new ModelRendererTurbo(this, 4, 24, textureX, textureY); // Box 13
		baseModel[31] = new ModelRendererTurbo(this, 0, 78, textureX, textureY); // FENCE01
		baseModel[32] = new ModelRendererTurbo(this, 0, 78, textureX, textureY); // FENCE01
		baseModel[33] = new ModelRendererTurbo(this, 112, 96, textureX, textureY); // Box 36
		baseModel[34] = new ModelRendererTurbo(this, 192, 0, textureX, textureY); // Box 1
		baseModel[35] = new ModelRendererTurbo(this, 98, 32, textureX, textureY); // Box 16
		baseModel[36] = new ModelRendererTurbo(this, 112, 32, textureX, textureY); // Box 27
		baseModel[37] = new ModelRendererTurbo(this, 98, 32, textureX, textureY); // Box 16
		baseModel[38] = new ModelRendererTurbo(this, 98, 32, textureX, textureY); // Box 16
		baseModel[39] = new ModelRendererTurbo(this, 98, 32, textureX, textureY); // Box 16
		baseModel[40] = new ModelRendererTurbo(this, 98, 32, textureX, textureY); // Box 16
		baseModel[41] = new ModelRendererTurbo(this, 98, 32, textureX, textureY); // Box 16
		baseModel[42] = new ModelRendererTurbo(this, 98, 32, textureX, textureY); // Box 16
		baseModel[43] = new ModelRendererTurbo(this, 98, 40, textureX, textureY); // Box 16
		baseModel[44] = new ModelRendererTurbo(this, 98, 59, textureX, textureY); // Box 16
		baseModel[45] = new ModelRendererTurbo(this, 98, 32, textureX, textureY); // Box 16
		baseModel[46] = new ModelRendererTurbo(this, 98, 32, textureX, textureY); // Box 16
		baseModel[47] = new ModelRendererTurbo(this, 222, 89, textureX, textureY); // Box 32
		baseModel[48] = new ModelRendererTurbo(this, 222, 89, textureX, textureY); // Box 33
		baseModel[49] = new ModelRendererTurbo(this, 222, 89, textureX, textureY); // Box 32
		baseModel[50] = new ModelRendererTurbo(this, 222, 89, textureX, textureY); // Box 33
		baseModel[51] = new ModelRendererTurbo(this, 66, 0, textureX, textureY); // Shape 50

		baseModel[0].addShapeBox(0F, 0F, 0F, 16, 46, 32, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, -47F, 0F);

		baseModel[1].addShapeBox(0F, 0F, 0F, 23, 8, 31, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		baseModel[1].setRotationPoint(-15F, 3F, -15F);

		baseModel[2].addShapeBox(0F, 0F, 0F, 16, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 3
		baseModel[2].setRotationPoint(0F, -28F, -32F);

		baseModel[3].addShapeBox(0F, 0F, 0F, 48, 16, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 5
		baseModel[3].setRotationPoint(-16F, -13F, -16F);

		baseModel[4].addShapeBox(0F, 0F, 0F, 5, 5, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 8
		baseModel[4].setRotationPoint(-5F, -27F, -9F);
		baseModel[4].rotateAngleX = -0.61086524F;

		baseModel[5].addBox(0F, 0F, 0F, 1, 6, 2, 0F); // Box 13
		baseModel[5].setRotationPoint(15.5F, -13.5F, -27F);
		baseModel[5].rotateAngleX = 1.57079633F;

		baseModel[6].addShapeBox(0F, 0F, 0F, 4, 34, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 15
		baseModel[6].setRotationPoint(6F, -15F, -17F);

		baseModel[7].addShapeBox(0F, 0F, 0F, 1, 34, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		baseModel[7].setRotationPoint(6F, -15F, -18F);

		baseModel[8].addShapeBox(0F, 0F, 0F, 1, 34, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 17
		baseModel[8].setRotationPoint(9F, -15F, -18F);

		baseModel[9].addShapeBox(0F, 0F, 0F, 16, 16, 15, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 22
		baseModel[9].setRotationPoint(-16F, -13F, 0F);

		baseModel[10].addShapeBox(0F, 0F, 0F, 1, 10, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 24
		baseModel[10].setRotationPoint(-16F, -10F, 15F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 1, 10, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 25
		baseModel[11].setRotationPoint(-1F, -10F, 15F);

		baseModel[12].addShapeBox(0F, 0F, 0F, 14, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 26
		baseModel[12].setRotationPoint(-15F, -10F, 15F);

		baseModel[13].addShapeBox(0F, 0F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		baseModel[13].setRotationPoint(-13F, -1F, -18F);

		baseModel[14].addShapeBox(0F, 0F, 0F, 6, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 28
		baseModel[14].setRotationPoint(-10F, -8F, -18F);

		baseModel[15].addShapeBox(0F, 0F, 0F, 8, 1, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 30
		baseModel[15].setRotationPoint(4F, -30F, -28F);

		baseModel[16].addShapeBox(0F, 0F, 0F, 4, 30, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 32
		baseModel[16].setRotationPoint(26F, 2F, 1F);
		baseModel[16].rotateAngleX = 1.57079633F;

		baseModel[17].addShapeBox(0F, 0F, 0F, 4, 30, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 33
		baseModel[17].setRotationPoint(2F, 2F, 1F);
		baseModel[17].rotateAngleX = 1.57079633F;

		baseModel[18].addShapeBox(0F, 0F, 0F, 16, 1, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 35
		baseModel[18].setRotationPoint(0F, -29F, -32F);

		baseModel[19].addShapeBox(0F, 0F, 0F, 15, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 36
		baseModel[19].setRotationPoint(1F, -28F, -31F);
		baseModel[19].rotateAngleY = 1.57079633F;

		baseModel[20].addShapeBox(0F, 0F, 0F, 16, 16, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FENCE01
		baseModel[20].setRotationPoint(-16F, 1F, -31.5F);

		baseModel[21].addShapeBox(0F, 0F, 0F, 14, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		baseModel[21].setRotationPoint(-15F, -12F, -17F);

		baseModel[22].addShapeBox(0F, 0F, 0F, 5, 5, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 8
		baseModel[22].setRotationPoint(16F, -27F, -9F);
		baseModel[22].rotateAngleX = -0.61086524F;

		baseModel[23].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(16, 0, 16, 0), new Coord2D(16, 16, 16, 16), new Coord2D(8, 16, 8, 16), new Coord2D(0, 10, 0, 10)}), 16, 16, 16, 60, 16, ModelRendererTurbo.MR_FRONT, new float[]{10, 10, 8, 16, 16}); // Shape 50
		baseModel[23].setRotationPoint(0F, -13F, 0F);
		baseModel[23].rotateAngleY = 1.57079633F;
		baseModel[23].flip = true;

		baseModel[24].addShapeBox(0F, 0F, 0F, 32, 16, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		baseModel[24].setRotationPoint(0F, 3F, 16F);

		baseModel[25].addShapeBox(0F, 0F, 0F, 15, 8, 15, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		baseModel[25].setRotationPoint(-15F, 3F, 16F);

		baseModel[26].addShapeBox(0F, 0F, 0F, 16, 8, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		baseModel[26].setRotationPoint(-16F, 11F, 16F);

		baseModel[27].setFlipped(true);
		baseModel[27].addShapeBox(0F, 0F, 0F, 23, 8, 31, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F, -23F, 0F, 0F); // Box 1
		baseModel[27].setRotationPoint(8F, 3F, -15F);

		baseModel[28].setFlipped(true);
		baseModel[28].addShapeBox(0F, 0F, 0F, 24, 32, 8, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F, -24F, 0F, 0F); // Box 1
		baseModel[28].setRotationPoint(8F, 19F, -16F);
		baseModel[28].rotateAngleX = 1.57079633F;

		baseModel[29].setFlipped(true);
		baseModel[29].addShapeBox(0F, 0F, 0F, 16, 46, 32, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F, -16F, 0F, 0F); // Box 0
		baseModel[29].setRotationPoint(16F, -47F, 0F);

		baseModel[30].addBox(0F, 0F, 0F, 1, 6, 2, 0F); // Box 13
		baseModel[30].setRotationPoint(-0.5F, -13.5F, -27F);
		baseModel[30].rotateAngleX = 1.57079633F;

		baseModel[31].addShapeBox(0F, 0F, 0F, 16, 16, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FENCE01
		baseModel[31].setRotationPoint(0F, 1F, -31.5F);

		baseModel[32].addShapeBox(0F, 0F, 0F, 16, 16, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // FENCE01
		baseModel[32].setRotationPoint(16F, 1F, -31.5F);

		baseModel[33].addShapeBox(0F, 0F, 0F, 15, 14, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 36
		baseModel[33].setRotationPoint(16F, -28F, -31F);
		baseModel[33].rotateAngleY = 1.57079633F;

		baseModel[34].addShapeBox(0F, 0F, 0F, 24, 32, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		baseModel[34].setRotationPoint(-16F, 19F, -16F);
		baseModel[34].rotateAngleX = 1.57079633F;

		baseModel[35].addShapeBox(0F, 0F, 0F, 1, 31, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		baseModel[35].setRotationPoint(-16F, 4F, -15F);
		baseModel[35].rotateAngleX = 1.57079633F;

		baseModel[36].addShapeBox(0F, 0F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		baseModel[36].setRotationPoint(-8F, -1F, -18F);

		baseModel[37].addShapeBox(0F, 0F, 0F, 1, 15, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		baseModel[37].setRotationPoint(-16F, 4F, 16F);
		baseModel[37].rotateAngleX = 1.57079633F;

		baseModel[38].addShapeBox(0F, 0F, 0F, 1, 31, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		baseModel[38].setRotationPoint(31F, 4F, -15F);
		baseModel[38].rotateAngleX = 1.57079633F;

		baseModel[39].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		baseModel[39].setRotationPoint(-16F, 4F, 32F);
		baseModel[39].rotateAngleX = 1.57079633F;
		baseModel[39].rotateAngleY = -1.57079633F;

		baseModel[40].addShapeBox(0F, 0F, 0F, 1, 32, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		baseModel[40].setRotationPoint(-16F, 4F, -15F);
		baseModel[40].rotateAngleX = 1.57079633F;
		baseModel[40].rotateAngleY = -1.57079633F;

		baseModel[41].addShapeBox(0F, 0F, 0F, 1, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		baseModel[41].setRotationPoint(16F, 4F, -15F);
		baseModel[41].rotateAngleX = 1.57079633F;
		baseModel[41].rotateAngleY = -1.57079633F;

		baseModel[42].addShapeBox(0F, 0F, 0F, 1, 7, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		baseModel[42].setRotationPoint(-16F, 4F, 32F);
		baseModel[42].rotateAngleY = -1.57079633F;

		baseModel[43].addShapeBox(0F, 0F, 0F, 1, 7, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		baseModel[43].setRotationPoint(-1F, 4F, 32F);
		baseModel[43].rotateAngleY = -1.57079633F;

		baseModel[44].addShapeBox(0F, 0F, 0F, 1, 7, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		baseModel[44].setRotationPoint(-16F, 4F, -15F);
		baseModel[44].rotateAngleY = -1.57079633F;

		baseModel[45].addShapeBox(0F, 0F, 0F, 1, 7, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		baseModel[45].setRotationPoint(31F, 4F, -15F);
		baseModel[45].rotateAngleY = -1.57079633F;

		baseModel[46].addShapeBox(0F, 0F, 0F, 1, 7, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		baseModel[46].setRotationPoint(31F, 4F, 16F);
		baseModel[46].rotateAngleY = -1.57079633F;

		baseModel[47].addShapeBox(0F, 0F, 0F, 6, 30, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 32
		baseModel[47].setRotationPoint(25F, 0F, 1F);
		baseModel[47].rotateAngleX = 1.57079633F;

		baseModel[48].addShapeBox(0F, 0F, 0F, 6, 30, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 33
		baseModel[48].setRotationPoint(1F, 0F, 1F);
		baseModel[48].rotateAngleX = 1.57079633F;

		baseModel[49].addShapeBox(0F, 0F, 0F, 6, 30, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 32
		baseModel[49].setRotationPoint(25F, 3F, 1F);
		baseModel[49].rotateAngleX = 1.57079633F;

		baseModel[50].addShapeBox(0F, 0F, 0F, 6, 30, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 33
		baseModel[50].setRotationPoint(1F, 3F, 1F);
		baseModel[50].rotateAngleX = 1.57079633F;

		baseModel[51].addShape3D(0F, -0.05F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(16, 0, 16, 0), new Coord2D(16, 16, 16, 16), new Coord2D(8, 16, 8, 16), new Coord2D(0, 10, 0, 10)}), 16, 16, 16, 60, 16, ModelRendererTurbo.MR_FRONT, new float[]{10, 10, 8, 16, 16}, true); // Shape 50
		baseModel[51].setRotationPoint(0F, -13F, -0.01F);
		baseModel[51].rotateAngleY = 1.57079633F;
		baseModel[51].flip = true;

		loaderLidRightModel = new ModelRendererTurbo[1];
		loaderLidRightModel[0] = new ModelRendererTurbo(this, 98, 54, textureX, textureY); // Box 10

		loaderLidRightModel[0].addShapeBox(0F, 0F, 0F, 8, 1, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 10
		loaderLidRightModel[0].setRotationPoint(0F, -14F, -32F);


		loaderLidLeftModel = new ModelRendererTurbo[1];
		loaderLidLeftModel[0] = new ModelRendererTurbo(this, 98, 54, textureX, textureY); // Box 11

		loaderLidLeftModel[0].setFlipped(true);
		loaderLidLeftModel[0].addShapeBox(-8F, 0F, -1F, 8, 1, 16, 0F, -8F, 0F, 0F, -8F, 0F, 0F, -8F, 0F, 0F, -8F, 0F, 0F, -8F, 0F, 0F, -8F, 0F, 0F, -8F, 0F, 0F, -8F, 0F, 0F); // Box 11
		loaderLidLeftModel[0].setRotationPoint(16F, -14F, -31F);


		clampModel = new ModelRendererTurbo[4];
		clampModel[0] = new ModelRendererTurbo(this, 98, 5, textureX, textureY); // CLAMP01
		clampModel[1] = new ModelRendererTurbo(this, 6, 15, textureX, textureY); // CLAMP02
		clampModel[2] = new ModelRendererTurbo(this, 98, 10, textureX, textureY); // CLAMP01
		clampModel[3] = new ModelRendererTurbo(this, 98, 10, textureX, textureY); // CLAMP01

		clampModel[0].addBox(0F, 0F, 0F, 18, 4, 1, 0F); // CLAMP01
		clampModel[0].setRotationPoint(-1F, -3.5F, -19F);

		clampModel[1].addShapeBox(0F, 0F, 0F, 2, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CLAMP02
		clampModel[1].setRotationPoint(7F, -3F, -18F);

		clampModel[2].addBox(0F, 0F, 0F, 18, 1, 1, 0F); // CLAMP01
		clampModel[2].setRotationPoint(-1F, -3.5F, -20F);

		clampModel[3].addBox(0F, 0F, 0F, 18, 1, 1, 0F); // CLAMP01
		clampModel[3].setRotationPoint(-1F, -0.5F, -20F);


		clampRightModel = new ModelRendererTurbo[1];
		clampRightModel[0] = new ModelRendererTurbo(this, 0, 94, textureX, textureY); // CLAMP03

		clampRightModel[0].addShapeBox(0F, 0F, 0F, 1, 12, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CLAMP03
		clampRightModel[0].setRotationPoint(-1F, -0.5F, -31F);
		clampRightModel[0].rotateAngleX = 1.57079633F;


		clampLeftModel = new ModelRendererTurbo[1];
		clampLeftModel[0] = new ModelRendererTurbo(this, 0, 94, textureX, textureY); // CLAMP04

		clampLeftModel[0].addShapeBox(0F, 0F, 0F, 1, 12, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CLAMP04
		clampLeftModel[0].setRotationPoint(16F, -0.5F, -31F);
		clampLeftModel[0].rotateAngleX = 1.57079633F;


		markerOutputModel = new ModelRendererTurbo[1];
		markerOutputModel[0] = new ModelRendererTurbo(this, 12, 15, textureX, textureY); // Box 23

		markerOutputModel[0].addShapeBox(0F, 0F, 0F, 4, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 23
		markerOutputModel[0].setRotationPoint(-10F, -13F, 15F);


		markerInputModel = new ModelRendererTurbo[1];
		markerInputModel[0] = new ModelRendererTurbo(this, 32, 87, textureX, textureY); // Box 23

		markerInputModel[0].addShapeBox(0F, 0F, 0F, 4, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 23
		markerInputModel[0].setRotationPoint(-10F, -13F, 15F);

		parts.put("base", baseModel);
		parts.put("loaderLidRight", loaderLidRightModel);
		parts.put("loaderLidLeft", loaderLidLeftModel);
		parts.put("clamp", clampModel);
		parts.put("clampRight", clampRightModel);
		parts.put("clampLeft", clampLeftModel);
		parts.put("markerOutput", markerOutputModel);
		parts.put("markerInput", markerInputModel);

		translateAll(0F, -3F, 0F);


		flipAll();
	}

	@Override
	public void getBlockRotation(EnumFacing facing, boolean mirrored)
	{
		switch(facing)
		{
			case NORTH:
			{
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?-1f: 0, 0f, -1f);

			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(-90F, 0F, 1F, 0F);
				if(!mirrored)
					GlStateManager.translate(1f, 0f, 0f);
			}
			break;
			case EAST:
			{
				GlStateManager.rotate(0F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?-1f: 0, 0f, 0f);

			}
			break;
			case WEST:
			{
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?0: 1, 0f, -1f);

			}
			break;
		}
	}
}
