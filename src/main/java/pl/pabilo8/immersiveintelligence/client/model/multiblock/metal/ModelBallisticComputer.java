package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.BaseBlockModel;
import pl.pabilo8.immersiveintelligence.client.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.tmt.Shape2D;

/**
 * @author Pabilo8
 * @since 20-06-2019
 */
public class ModelBallisticComputer extends BaseBlockModel
{
	int textureX = 128;
	int textureY = 128;

	public ModelBallisticComputer() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[46];
		baseModel[0] = new ModelRendererTurbo(this, 0, 82, textureX, textureY); // BoxBottom
		baseModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BoxBottom
		baseModel[2] = new ModelRendererTurbo(this, 53, 62, textureX, textureY); // ShapeMiddleWall
		baseModel[3] = new ModelRendererTurbo(this, 0, 115, textureX, textureY); // BoxMap
		baseModel[4] = new ModelRendererTurbo(this, 80, 1, textureX, textureY); // BoxPaperPage
		baseModel[5] = new ModelRendererTurbo(this, 115, 63, textureX, textureY); // BulletSide
		baseModel[6] = new ModelRendererTurbo(this, 98, 0, textureX, textureY); // BulletSide
		baseModel[7] = new ModelRendererTurbo(this, 112, 95, textureX, textureY); // BulletCenter
		baseModel[8] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BulletTop
		baseModel[9] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BulletTop
		baseModel[10] = new ModelRendererTurbo(this, 37, 56, textureX, textureY); // BulletCenter
		baseModel[11] = new ModelRendererTurbo(this, 62, 50, textureX, textureY); // BulletCenter
		baseModel[12] = new ModelRendererTurbo(this, 44, 113, textureX, textureY); // ShelfBase
		baseModel[13] = new ModelRendererTurbo(this, 85, 54, textureX, textureY); // ShelfBase
		baseModel[14] = new ModelRendererTurbo(this, 96, 23, textureX, textureY); // ShelfBaseEnd
		baseModel[15] = new ModelRendererTurbo(this, 96, 23, textureX, textureY); // ShelfBaseEnd
		baseModel[16] = new ModelRendererTurbo(this, 101, 46, textureX, textureY); // CupSide
		baseModel[17] = new ModelRendererTurbo(this, 0, 10, textureX, textureY); // CupSide
		baseModel[18] = new ModelRendererTurbo(this, 116, 44, textureX, textureY); // CupSide
		baseModel[19] = new ModelRendererTurbo(this, 0, 5, textureX, textureY); // CupSide
		baseModel[20] = new ModelRendererTurbo(this, 116, 49, textureX, textureY); // CupSide
		baseModel[21] = new ModelRendererTurbo(this, 113, 54, textureX, textureY); // CupSide
		baseModel[22] = new ModelRendererTurbo(this, 97, 17, textureX, textureY); // CupSide
		baseModel[23] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // CupSide
		baseModel[24] = new ModelRendererTurbo(this, 8, 5, textureX, textureY); // CupSide
		baseModel[25] = new ModelRendererTurbo(this, 8, 0, textureX, textureY); // CupSide
		baseModel[26] = new ModelRendererTurbo(this, 80, 83, textureX, textureY); // BoxBottom
		baseModel[27] = new ModelRendererTurbo(this, 67, 41, textureX, textureY); // BoxEnergyCableEnd
		baseModel[28] = new ModelRendererTurbo(this, 64, 106, textureX, textureY); // BoxEnergyUpper
		baseModel[29] = new ModelRendererTurbo(this, 0, 64, textureX, textureY); // BoxEnergyUpper
		baseModel[30] = new ModelRendererTurbo(this, 78, 50, textureX, textureY); // BoxPowerWire
		baseModel[31] = new ModelRendererTurbo(this, 110, 0, textureX, textureY); // BoxPowerWire
		baseModel[32] = new ModelRendererTurbo(this, 97, 25, textureX, textureY); // PowerWire
		baseModel[33] = new ModelRendererTurbo(this, 102, 35, textureX, textureY); // PowerWire
		baseModel[34] = new ModelRendererTurbo(this, 88, 65, textureX, textureY); // PowerWireEnd
		baseModel[35] = new ModelRendererTurbo(this, 0, 51, textureX, textureY); // PowerWireEnd
		baseModel[36] = new ModelRendererTurbo(this, 26, 40, textureX, textureY); // BoxCoil
		baseModel[37] = new ModelRendererTurbo(this, 16, 51, textureX, textureY); // BoxCoilEnd
		baseModel[38] = new ModelRendererTurbo(this, 16, 51, textureX, textureY); // BoxCoilEnd
		baseModel[39] = new ModelRendererTurbo(this, 112, 12, textureX, textureY); // BoxCoilCable
		baseModel[40] = new ModelRendererTurbo(this, 54, 40, textureX, textureY); // BoxCoilCable
		baseModel[41] = new ModelRendererTurbo(this, 0, 60, textureX, textureY); // BoxCoilEnd
		baseModel[42] = new ModelRendererTurbo(this, 33, 56, textureX, textureY); // BoxCoilCable
		baseModel[43] = new ModelRendererTurbo(this, 23, 56, textureX, textureY); // BoxCoilCable
		baseModel[44] = new ModelRendererTurbo(this, 0, 60, textureX, textureY); // BoxCoilEnd
		baseModel[45] = new ModelRendererTurbo(this, 0, 40, textureX, textureY); // BoxFrontPlate

		baseModel[0].addBox(0F, 0F, 0F, 32, 8, 16, 0F); // BoxBottom
		baseModel[0].setRotationPoint(0F, -8F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 32, 24, 16, 0F); // BoxBottom
		baseModel[1].setRotationPoint(0F, -24F, 16F);

		baseModel[2].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(16, 0, 16, 0), new Coord2D(16, 16, 16, 16), new Coord2D(4, 16, 4, 16), new Coord2D(0, 12, 0, 12)}), 1, 16, 16, 62, 1, ModelRendererTurbo.MR_FRONT, new float[]{12, 6, 12, 16, 16}); // ShapeMiddleWall
		baseModel[2].setRotationPoint(17F, -8F, 0F);
		baseModel[2].rotateAngleY = -1.57079633F;

		baseModel[3].addShapeBox(0F, 0F, 0F, 14, 1, 12, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxMap
		baseModel[3].setRotationPoint(17F, -9F, 3F);
		baseModel[3].rotateAngleY = 0.13962634F;

		baseModel[4].addShapeBox(0F, 0F, 0F, 8, 12, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F); // BoxPaperPage
		baseModel[4].setRotationPoint(17F, -22F, 10F);
		baseModel[4].rotateAngleY = -1.57079633F;
		baseModel[4].rotateAngleZ = 0.10471976F;

		baseModel[5].addShapeBox(0.5F, 0F, -2F, 2, 12, 4, 0F, 0F, 0F, 0F, -0.75F, 0F, -0.75F, -0.75F, 0F, -0.75F, 0F, 0F, 0F, 0F, 0F, 0F, -0.75F, 0F, -0.75F, -0.75F, 0F, -0.75F, 0F, 0F, 0F); // BulletSide
		baseModel[5].setRotationPoint(19F, -20F, 14F);
		baseModel[5].rotateAngleX = 0.01745329F;
		baseModel[5].rotateAngleY = -1.88495559F;
		baseModel[5].rotateAngleZ = -0.38397244F;

		baseModel[6].addShapeBox(-3.5F, 0F, -2F, 2, 12, 4, 0F, -0.75F, 0F, -0.75F, 0F, 0F, 0F, 0F, 0F, 0F, -0.75F, 0F, -0.75F, -0.75F, 0F, -0.75F, 0F, 0F, 0F, 0F, 0F, 0F, -0.75F, 0F, -0.75F); // BulletSide
		baseModel[6].setRotationPoint(19F, -20F, 14F);
		baseModel[6].rotateAngleX = 0.01745329F;
		baseModel[6].rotateAngleY = -1.88495559F;
		baseModel[6].rotateAngleZ = -0.38397244F;

		baseModel[7].addBox(-1.5F, 0F, -2F, 2, 12, 4, 0F); // BulletCenter
		baseModel[7].setRotationPoint(19F, -20F, 14F);
		baseModel[7].rotateAngleX = 0.01745329F;
		baseModel[7].rotateAngleY = -1.88495559F;
		baseModel[7].rotateAngleZ = -0.38397244F;

		baseModel[8].addShapeBox(0.5F, -1F, -2F, 2, 1, 4, 0F, 0F, 0F, -1F, -2F, 0F, -1F, -2F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, -0.75F, 0F, -0.75F, -0.75F, 0F, -0.75F, 0F, 0F, 0F); // BulletTop
		baseModel[8].setRotationPoint(19F, -20F, 14F);
		baseModel[8].rotateAngleX = 0.01745329F;
		baseModel[8].rotateAngleY = -1.88495559F;
		baseModel[8].rotateAngleZ = -0.38397244F;

		baseModel[9].addShapeBox(-3.5F, -1F, -2F, 2, 1, 4, 0F, -2F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, -2F, 0F, -1F, -0.75F, 0F, -0.75F, 0F, 0F, 0F, 0F, 0F, 0F, -0.75F, 0F, -0.75F); // BulletTop
		baseModel[9].setRotationPoint(19F, -20F, 14F);
		baseModel[9].rotateAngleX = 0.01745329F;
		baseModel[9].rotateAngleY = -1.88495559F;
		baseModel[9].rotateAngleZ = -0.38397244F;

		baseModel[10].addShapeBox(-1.5F, -1F, -2F, 2, 1, 4, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BulletCenter
		baseModel[10].setRotationPoint(19F, -20F, 14F);
		baseModel[10].rotateAngleX = 0.01745329F;
		baseModel[10].rotateAngleY = -1.88495559F;
		baseModel[10].rotateAngleZ = -0.38397244F;

		baseModel[11].addShapeBox(-1.5F, -2F, -1F, 2, 1, 2, 0F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BulletCenter
		baseModel[11].setRotationPoint(19F, -20F, 14F);
		baseModel[11].rotateAngleX = 0.01745329F;
		baseModel[11].rotateAngleY = -1.88495559F;
		baseModel[11].rotateAngleZ = -0.38397244F;

		baseModel[12].addBox(0F, 0F, 0F, 10, 1, 8, 0F); // ShelfBase
		baseModel[12].setRotationPoint(22F, -20F, 8F);
		baseModel[12].rotateAngleX = 0.06981317F;

		baseModel[13].addBox(0F, 0F, 0F, 10, 1, 8, 0F); // ShelfBase
		baseModel[13].setRotationPoint(22F, -15F, 8F);
		baseModel[13].rotateAngleX = 0.06981317F;

		baseModel[14].addBox(0F, -1F, 0F, 10, 1, 1, 0F); // ShelfBaseEnd
		baseModel[14].setRotationPoint(22F, -20F, 8F);
		baseModel[14].rotateAngleX = 0.06981317F;

		baseModel[15].addBox(0F, -1F, 0F, 10, 1, 1, 0F); // ShelfBaseEnd
		baseModel[15].setRotationPoint(22F, -15F, 8F);
		baseModel[15].rotateAngleX = 0.06981317F;

		baseModel[16].addShapeBox(-3F, 0F, -2F, 2, 4, 4, 0F, -1.25F, 0F, -0.75F, 0F, 0F, 0F, 0F, 0F, 0F, -1.25F, 0F, -0.75F, -1.25F, 0F, -0.75F, 0F, 0F, 0F, 0F, 0F, 0F, -1.25F, 0F, -0.75F); // CupSide
		baseModel[16].setRotationPoint(27F, -13.5F, 6F);
		baseModel[16].rotateAngleX = 0.01745329F;
		baseModel[16].rotateAngleY = -1.36135682F;

		baseModel[17].addShapeBox(-3F, 0F, -2F, 2, 1, 4, 0F, -1F, 0F, -0.625F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, -1F, 0F, -0.625F, -1F, 0F, -0.625F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, -1F, 0F, -0.625F); // CupSide
		baseModel[17].setRotationPoint(27F, -9.5F, 6F);
		baseModel[17].rotateAngleX = 0.01745329F;
		baseModel[17].rotateAngleY = -1.36135682F;

		baseModel[18].addShapeBox(-1F, 0F, -2F, 2, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CupSide
		baseModel[18].setRotationPoint(27F, -13.5F, 6F);
		baseModel[18].rotateAngleX = 0.01745329F;
		baseModel[18].rotateAngleY = -1.36135682F;

		baseModel[19].addShapeBox(-1F, 0F, -1.84F, 2, 1, 4, 0F, -0.125F, 0F, 0.25F, -0.125F, 0F, 0.25F, 0F, 0F, 0F, 0F, 0F, 0F, -0.125F, 0F, 0.25F, -0.125F, 0F, 0.25F, 0F, 0F, 0F, 0F, 0F, 0F); // CupSide
		baseModel[19].setRotationPoint(27F, -9.5F, 6F);
		baseModel[19].rotateAngleX = 0.01745329F;
		baseModel[19].rotateAngleY = -1.36135682F;

		baseModel[20].addShapeBox(-1F, 0F, 1.25F, 2, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.25F, 0F, 0F, -0.25F); // CupSide
		baseModel[20].setRotationPoint(27F, -13.5F, 6F);
		baseModel[20].rotateAngleX = 0.01745329F;
		baseModel[20].rotateAngleY = -1.37881011F;

		baseModel[21].addShapeBox(1F, 0F, -2F, 2, 4, 4, 0F, 0F, 0F, 0F, -1.25F, 0F, -0.75F, -1.25F, 0F, -0.75F, 0F, 0F, 0F, 0F, 0F, 0F, -1.25F, 0F, -0.75F, -1.25F, 0F, -0.75F, 0F, 0F, 0F); // CupSide
		baseModel[21].setRotationPoint(27F, -13.5F, 6F);
		baseModel[21].rotateAngleX = 0.01745329F;
		baseModel[21].rotateAngleY = -1.36135682F;

		baseModel[22].addShapeBox(1F, 0F, -2F, 2, 1, 4, 0F, 0.125F, 0F, 0.125F, -1F, 0F, -0.625F, -1F, 0F, -0.625F, 0.125F, 0F, 0.125F, 0.125F, 0F, 0.125F, -1F, 0F, -0.625F, -1F, 0F, -0.625F, 0.125F, 0F, 0.125F); // CupSide
		baseModel[22].setRotationPoint(27F, -9.5F, 6F);
		baseModel[22].rotateAngleX = 0.01745329F;
		baseModel[22].rotateAngleY = -1.36135682F;

		baseModel[23].addShapeBox(-0.5F, 3F, -3F, 1, 1, 1, 0F, 0F, 0F, 0.5F, 0F, 0F, 0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CupSide
		baseModel[23].setRotationPoint(27F, -13.5F, 6F);
		baseModel[23].rotateAngleX = 0.01745329F;
		baseModel[23].rotateAngleY = -1.36135682F;

		baseModel[24].addBox(-0.5F, 2F, -3.5F, 1, 1, 1, 0F); // CupSide
		baseModel[24].setRotationPoint(27F, -13.5F, 6F);
		baseModel[24].rotateAngleX = 0.01745329F;
		baseModel[24].rotateAngleY = -1.36135682F;

		baseModel[25].addShapeBox(-0.5F, 1F, -3F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.5F, 0F, 0F, 0.5F, 0F, 0F, 0F, 0F, 0F, 0F); // CupSide
		baseModel[25].setRotationPoint(27F, -13.5F, 6F);
		baseModel[25].rotateAngleX = 0.01745329F;
		baseModel[25].rotateAngleY = -1.36135682F;

		baseModel[26].addBox(0F, 0F, 0F, 16, 8, 4, 0F); // BoxBottom
		baseModel[26].setRotationPoint(0F, -16F, 0F);

		baseModel[27].addBox(0F, 0F, 0F, 16, 8, 1, 0F); // BoxEnergyCableEnd
		baseModel[27].setRotationPoint(0F, -16F, 15F);

		baseModel[28].addBox(0F, 0F, 0F, 16, 6, 16, 0F); // BoxEnergyUpper
		baseModel[28].setRotationPoint(0F, -30F, 16F);

		baseModel[29].addTrapezoid(0F, 0F, 0F, 16, 2, 16, 0F, -1.00F, ModelRendererTurbo.MR_TOP); // BoxEnergyUpper
		baseModel[29].setRotationPoint(0F, -32F, 16F);

		baseModel[30].addBox(0F, 0F, 0F, 1, 6, 6, 0F); // BoxPowerWire
		baseModel[30].setRotationPoint(16F, -30F, 17F);

		baseModel[31].addBox(0F, 0F, 0F, 1, 6, 6, 0F); // BoxPowerWire
		baseModel[31].setRotationPoint(16F, -30F, 25F);

		baseModel[32].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // PowerWire
		baseModel[32].setRotationPoint(17F, -29F, 18F);

		baseModel[33].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // PowerWire
		baseModel[33].setRotationPoint(17F, -29F, 26F);

		baseModel[34].addShapeBox(0F, 0F, 0F, 4, 5, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // PowerWireEnd
		baseModel[34].setRotationPoint(25F, -29F, 18F);

		baseModel[35].addShapeBox(0F, 0F, 0F, 4, 5, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // PowerWireEnd
		baseModel[35].setRotationPoint(25F, -29F, 26F);

		baseModel[36].addBox(0F, 0F, 0F, 10, 8, 8, 0F); // BoxCoil
		baseModel[36].setRotationPoint(3F, -16F, 5F);

		baseModel[37].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // BoxCoilEnd
		baseModel[37].setRotationPoint(2F, -14F, 7F);

		baseModel[38].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // BoxCoilEnd
		baseModel[38].setRotationPoint(13F, -14F, 7F);

		baseModel[39].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, -0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxCoilCable
		baseModel[39].setRotationPoint(0F, -13.5F, 8F);

		baseModel[40].addShapeBox(0F, 0F, 0F, 2, 2, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.5F, 0F, 0F, -0.5F, 0F, 0F); // BoxCoilCable
		baseModel[40].setRotationPoint(0F, -13.5F, 10F);

		baseModel[41].addBox(0F, 0F, 0F, 3, 3, 1, 0F); // BoxCoilEnd
		baseModel[41].setRotationPoint(0F, -14F, 14F);

		baseModel[42].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.25F, 0F, 0F, 0F, 0F, 0F); // BoxCoilCable
		baseModel[42].setRotationPoint(14F, -13.5F, 8F);

		baseModel[43].addShapeBox(0F, 0F, 0F, 2, 2, 3, 0F, 0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxCoilCable
		baseModel[43].setRotationPoint(14F, -13.5F, 5F);

		baseModel[44].addBox(0F, 0F, 0F, 3, 3, 1, 0F); // BoxCoilEnd
		baseModel[44].setRotationPoint(13F, -14F, 4F);

		baseModel[45].addBox(0F, 0F, 0F, 12, 10, 1, 0F); // BoxFrontPlate
		baseModel[45].setRotationPoint(2F, -28F, 15F);

		parts.put("base", baseModel);
		flipAll();

	}

	@Override
	public void getBlockRotation(EnumFacing facing, boolean mirrored)
	{
		switch(facing)
		{
			case NORTH:
			{
				GlStateManager.rotate(mirrored?270f: 90F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?-1f: 0f, 0f, mirrored?-2f: 2f);
			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(mirrored?90F: 270f, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?0f: -1f, 0f, mirrored?1f: -1f);
			}
			break;
			case EAST:
			{
				if(mirrored)
					GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?-2f: 1f, 0f, 0f);
			}
			break;
			case WEST:
			{
				if(!mirrored)
					GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?1f: -2f, 0f, mirrored?-1f: 1f);
			}
			break;
		}
	}
}
