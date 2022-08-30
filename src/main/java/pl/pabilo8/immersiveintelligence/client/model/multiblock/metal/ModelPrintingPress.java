package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * @author Pabilo8
 * @since 20-06-2019
 */
public class ModelPrintingPress extends ModelIIBase
{
	int textureX = 256;
	int textureY = 256;

	public ModelRendererTurbo[] rollerModel, paperInserterDoorModel;

	public ModelPrintingPress(boolean flipped) //Same as Filename
	{
		int modifier = flipped?-1: 1;
		baseModel = new ModelRendererTurbo[64];
		baseModel[0] = new ModelRendererTurbo(this, 0, 19, textureX, textureY); // MainBox
		baseModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // TableTop
		baseModel[2] = new ModelRendererTurbo(this, 0, 19, textureX, textureY); // TableLeg
		baseModel[3] = new ModelRendererTurbo(this, 0, 19, textureX, textureY); // TableLeg
		baseModel[4] = new ModelRendererTurbo(this, 0, 19, textureX, textureY); // TableLeg
		baseModel[5] = new ModelRendererTurbo(this, 0, 19, textureX, textureY); // TableLeg
		baseModel[6] = new ModelRendererTurbo(this, 80, 3, textureX, textureY); // CenterBoxPaperPage
		baseModel[7] = new ModelRendererTurbo(this, 112, 14, textureX, textureY); // PaperCollectorSide
		baseModel[8] = new ModelRendererTurbo(this, 210, 5, textureX, textureY); // PaperCollectorSide
		baseModel[9] = new ModelRendererTurbo(this, 132, 10, textureX, textureY); // PaperCollectorSide
		baseModel[10] = new ModelRendererTurbo(this, 12, 19, textureX, textureY); // PaperCollectorBottom
		baseModel[11] = new ModelRendererTurbo(this, 0, 99, textureX, textureY); // PaperCollectorSlide
		baseModel[12] = new ModelRendererTurbo(this, 14, 99, textureX, textureY); // MainBoxProduction
		baseModel[13] = new ModelRendererTurbo(this, 14, 99, textureX, textureY); // EnergyInput
		baseModel[14] = new ModelRendererTurbo(this, 6, 147, textureX, textureY); // ProductionHolderPost1
		baseModel[15] = new ModelRendererTurbo(this, 6, 147, textureX, textureY); // ProductionHolderPost2
		baseModel[16] = new ModelRendererTurbo(this, 6, 147, textureX, textureY); // ProductionHolderPost3
		baseModel[17] = new ModelRendererTurbo(this, 6, 147, textureX, textureY); // ProductionHolderPost4
		baseModel[18] = new ModelRendererTurbo(this, 6, 147, textureX, textureY); // ProductionHolderPost5
		baseModel[19] = new ModelRendererTurbo(this, 6, 147, textureX, textureY); // ProductionHolderPost6
		baseModel[20] = new ModelRendererTurbo(this, 0, 35, textureX, textureY); // ProductionPaperInserter
		baseModel[21] = new ModelRendererTurbo(this, 94, 99, textureX, textureY); // FrontPreviewHolder
		baseModel[22] = new ModelRendererTurbo(this, 24, 46, textureX, textureY); // ProductionPaperInserterSide
		baseModel[23] = new ModelRendererTurbo(this, 94, 114, textureX, textureY); // MainBoxProduction
		baseModel[24] = new ModelRendererTurbo(this, 148, 36, textureX, textureY); // TankMain
		baseModel[25] = new ModelRendererTurbo(this, 160, 19, textureX, textureY); // TankStand
		baseModel[26] = new ModelRendererTurbo(this, 160, 19, textureX, textureY); // TankStand
		baseModel[27] = new ModelRendererTurbo(this, 160, 19, textureX, textureY); // TankStand
		baseModel[28] = new ModelRendererTurbo(this, 160, 19, textureX, textureY); // TankStand
		baseModel[29] = new ModelRendererTurbo(this, 160, 58, textureX, textureY); // TankFront
		baseModel[30] = new ModelRendererTurbo(this, 132, 48, textureX, textureY); // TankFront2
		baseModel[31] = new ModelRendererTurbo(this, 160, 23, textureX, textureY); // TankFront2
		baseModel[32] = new ModelRendererTurbo(this, 160, 58, textureX, textureY); // TankFront
		baseModel[33] = new ModelRendererTurbo(this, 132, 48, textureX, textureY); // TankFront2
		baseModel[34] = new ModelRendererTurbo(this, 132, 48, textureX, textureY); // TankFront2
		baseModel[35] = new ModelRendererTurbo(this, 160, 23, textureX, textureY); // TankFront2
		baseModel[36] = new ModelRendererTurbo(this, 132, 48, textureX, textureY); // TankFront2
		baseModel[37] = new ModelRendererTurbo(this, 168, 13, textureX, textureY); // TankInput
		baseModel[38] = new ModelRendererTurbo(this, 160, 82, textureX, textureY); // EnergyInput
		baseModel[39] = new ModelRendererTurbo(this, 206, 105, textureX, textureY); // EnergyInput2
		baseModel[40] = new ModelRendererTurbo(this, 142, 146, textureX, textureY); // EnergyInput
		baseModel[41] = new ModelRendererTurbo(this, 182, 67, textureX, textureY); // EnergyInput2
		baseModel[42] = new ModelRendererTurbo(this, 0, 165, textureX, textureY); // PaperInputMain
		baseModel[43] = new ModelRendererTurbo(this, 47, 167, textureX, textureY); // PaperInputMain
		baseModel[44] = new ModelRendererTurbo(this, 47, 165, textureX, textureY); // PaperInputMain
		baseModel[45] = new ModelRendererTurbo(this, 47, 169, textureX, textureY); // PaperInputMain
		baseModel[46] = new ModelRendererTurbo(this, 47, 169, textureX, textureY); // PaperInputMain
		baseModel[47] = new ModelRendererTurbo(this, 160, 9, textureX, textureY); // InkPipe
		baseModel[48] = new ModelRendererTurbo(this, 192, 5, textureX, textureY); // InkPipe2
		baseModel[49] = new ModelRendererTurbo(this, 160, 9, textureX, textureY); // InkPipe
		baseModel[50] = new ModelRendererTurbo(this, 160, 5, textureX, textureY); // InkPipe
		baseModel[51] = new ModelRendererTurbo(this, 176, 8, textureX, textureY); // InkPipe
		baseModel[52] = new ModelRendererTurbo(this, 168, 106, textureX, textureY); // FrontPreviewHolderBase
		baseModel[53] = new ModelRendererTurbo(this, 94, 99, textureX, textureY); // FrontPreviewHolderRod
		baseModel[54] = new ModelRendererTurbo(this, 137, 109, textureX, textureY); // FrontPreviewHolder2
		baseModel[55] = new ModelRendererTurbo(this, 138, 99, textureX, textureY); // PaperPage
		baseModel[56] = new ModelRendererTurbo(this, 209, 81, textureX, textureY); // PaperPage
		baseModel[57] = new ModelRendererTurbo(this, 138, 99, textureX, textureY); // PaperPage
		baseModel[58] = new ModelRendererTurbo(this, 209, 81, textureX, textureY); // PaperPage
		baseModel[59] = new ModelRendererTurbo(this, 138, 99, textureX, textureY); // PaperPage
		baseModel[60] = new ModelRendererTurbo(this, 209, 81, textureX, textureY); // PaperPage
		baseModel[61] = new ModelRendererTurbo(this, 138, 99, textureX, textureY); // PaperPage
		baseModel[62] = new ModelRendererTurbo(this, 184, 106, textureX, textureY); // PenHolder
		baseModel[63] = new ModelRendererTurbo(this, 192, 105, textureX, textureY); // Pen

		baseModel[0].addBox(0F, 0F, 0F, 32, 32, 48, 0F); // MainBox
		baseModel[0].setRotationPoint(0F, -32F, 16F);

		baseModel[1].addBox(0F, 0F, 0F, 32, 3, 16, 0F); // TableTop
		baseModel[1].setRotationPoint(0F, -16F, 0F);

		baseModel[2].addBox(0F, 0F, 0F, 3, 13, 3, 0F); // TableLeg
		baseModel[2].setRotationPoint(1F, -13F, 1F);

		baseModel[3].addBox(0F, 0F, 0F, 3, 13, 3, 0F); // TableLeg
		baseModel[3].setRotationPoint(1F, -13F, 12F);

		baseModel[4].addBox(0F, 0F, 0F, 3, 13, 3, 0F); // TableLeg
		baseModel[4].setRotationPoint(28F, -13F, 1F);

		baseModel[5].addBox(0F, 0F, 0F, 3, 13, 3, 0F); // TableLeg
		baseModel[5].setRotationPoint(28F, -13F, 12F);

		baseModel[6].addBox(0F, 0F, 0F, 8, 12, 1, 0F); // CenterBoxPaperPage
		baseModel[6].setRotationPoint(2F, -30.5F, 15.5F);
		baseModel[6].rotateAngleX = -0.03490659F;

		baseModel[7].addBox(0F, 0F, 0F, 2, 18, 16, 0F); // PaperCollectorSide
		baseModel[7].setRotationPoint(32F, -18F, 0F);

		baseModel[8].addBox(0F, 0F, 0F, 2, 18, 16, 0F); // PaperCollectorSide
		baseModel[8].setRotationPoint(46F, -18F, 0F);

		baseModel[9].addBox(0F, 0F, 0F, 12, 18, 2, 0F); // PaperCollectorSide
		baseModel[9].setRotationPoint(34F, -18F, 0F);

		baseModel[10].addBox(0F, 0F, 0F, 12, 2, 2, 0F); // PaperCollectorBottom
		baseModel[10].setRotationPoint(34F, -2F, 2F);

		baseModel[11].addBox(0F, 0F, 0.1F, 12, 2, 19, 0F); // PaperCollectorSlide
		baseModel[11].setRotationPoint(34F, -2F, 3F);
		baseModel[11].rotateAngleX = 0.82030475F;

		baseModel[12].addBox(0F, 0F, 0F, 16, 16, 48, 0F); // MainBoxProduction
		baseModel[12].setRotationPoint(32F, -16F, 16F);

		baseModel[13].addBox(0F, 0F, 0F, 16, 16, 48, 0F); // EnergyInput
		baseModel[13].setRotationPoint(32F, -16F, 16F);

		baseModel[14].flip = flipped;
		baseModel[14].addShape3D(2F, -3F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(4, 0, 4, 0), new Coord2D(4, 15, 4, 15), new Coord2D(3, 16, 3, 16), new Coord2D(1, 16, 1, 16), new Coord2D(0, 15, 0, 15)}), 2, 4, 16, 40, 2, ModelRendererTurbo.MR_FRONT, new float[]{15, 2, 2, 2, 15, 4}); // ProductionHolderPost1
		baseModel[14].setRotationPoint(48F, -19F*modifier, 22F*modifier);
		baseModel[14].rotateAngleY = -1.57079633F;

		baseModel[15].flip = flipped;
		baseModel[15].addShape3D(2F, -3F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(4, 0, 4, 0), new Coord2D(4, 15, 4, 15), new Coord2D(3, 16, 3, 16), new Coord2D(1, 16, 1, 16), new Coord2D(0, 15, 0, 15)}), 2, 4, 16, 40, 2, ModelRendererTurbo.MR_FRONT, new float[]{15, 2, 2, 2, 15, 4}); // ProductionHolderPost2
		baseModel[15].setRotationPoint(48F, -19F*modifier, 28F*modifier);
		baseModel[15].rotateAngleY = -1.57079633F;

		baseModel[16].flip = flipped;
		baseModel[16].addShape3D(2F, -3F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(4, 0, 4, 0), new Coord2D(4, 15, 4, 15), new Coord2D(3, 16, 3, 16), new Coord2D(1, 16, 1, 16), new Coord2D(0, 15, 0, 15)}), 2, 4, 16, 40, 2, ModelRendererTurbo.MR_FRONT, new float[]{15, 2, 2, 2, 15, 4}); // ProductionHolderPost3
		baseModel[16].setRotationPoint(48F, -19F*modifier, 34F*modifier);
		baseModel[16].rotateAngleY = -1.57079633F;

		baseModel[17].flip = flipped;
		baseModel[17].addShape3D(2F, -3F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(4, 0, 4, 0), new Coord2D(4, 15, 4, 15), new Coord2D(3, 16, 3, 16), new Coord2D(1, 16, 1, 16), new Coord2D(0, 15, 0, 15)}), 2, 4, 16, 40, 2, ModelRendererTurbo.MR_FRONT, new float[]{15, 2, 2, 2, 15, 4}); // ProductionHolderPost4
		baseModel[17].setRotationPoint(48F, -19F*modifier, 40F*modifier);
		baseModel[17].rotateAngleY = -1.57079633F;

		baseModel[18].flip = flipped;
		baseModel[18].addShape3D(2F, -3F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(4, 0, 4, 0), new Coord2D(4, 15, 4, 15), new Coord2D(3, 16, 3, 16), new Coord2D(1, 16, 1, 16), new Coord2D(0, 15, 0, 15)}), 2, 4, 16, 40, 2, ModelRendererTurbo.MR_FRONT, new float[]{15, 2, 2, 2, 15, 4}); // ProductionHolderPost5
		baseModel[18].setRotationPoint(48F, -19F*modifier, 46F*modifier);
		baseModel[18].rotateAngleY = -1.57079633F;

		baseModel[19].flip = flipped;
		baseModel[19].addShape3D(2F, -3F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(4, 0, 4, 0), new Coord2D(4, 15, 4, 15), new Coord2D(3, 16, 3, 16), new Coord2D(1, 16, 1, 16), new Coord2D(0, 15, 0, 15)}), 2, 4, 16, 40, 2, ModelRendererTurbo.MR_FRONT, new float[]{15, 2, 2, 2, 15, 4}); // ProductionHolderPost6
		baseModel[19].setRotationPoint(48F, -19F*modifier, 52F*modifier);
		baseModel[19].rotateAngleY = -1.57079633F;

		baseModel[20].addBox(0F, 0F, 0F, 14, 1, 10, 0F); // ProductionPaperInserter
		baseModel[20].setRotationPoint(32F, -26F, 54F);
		baseModel[20].rotateAngleX = 0.27925268F;

		baseModel[21].addBox(0F, 0F, 0F, 14, 1, 14, 0F); // FrontPreviewHolder
		baseModel[21].setRotationPoint(16F, -18F, 1F);
		baseModel[21].rotateAngleX = 0.34906585F;

		baseModel[22].addBox(0F, -0.5F, 0F, 1, 2, 10, 0F); // ProductionPaperInserterSide
		baseModel[22].setRotationPoint(46F, -26F, 54F);
		baseModel[22].rotateAngleX = 0.27925268F;

		baseModel[23].addBox(0F, 0F, 0F, 48, 16, 16, 0F); // MainBoxProduction
		baseModel[23].setRotationPoint(0F, -16F, 64F);

		baseModel[24].addBox(0F, 0F, 0F, 24, 12, 10, 0F); // TankMain
		baseModel[24].setRotationPoint(21F, -30F, 67F);

		baseModel[25].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // TankStand
		baseModel[25].setRotationPoint(22F, -18F, 67F);

		baseModel[26].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // TankStand
		baseModel[26].setRotationPoint(22F, -18F, 75F);

		baseModel[27].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // TankStand
		baseModel[27].setRotationPoint(42F, -18F, 67F);

		baseModel[28].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // TankStand
		baseModel[28].setRotationPoint(42F, -18F, 75F);

		baseModel[29].addFlexTrapezoid(0F, 0F, 0F, 1, 12, 10, 0F, -1.00F, -1.00F, 0F, 0.00F, 0.00F, 0.00F, ModelRendererTurbo.MR_LEFT); // TankFront
		baseModel[29].setRotationPoint(45F, -30F, 67F);

		baseModel[30].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, 0F, -1F, 0F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F); // TankFront2
		baseModel[30].setRotationPoint(45F, -30F, 66F);

		baseModel[31].addShapeBox(0F, 0F, 0F, 24, 12, 1, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TankFront2
		baseModel[31].setRotationPoint(21F, -30F, 66F);

		baseModel[32].addFlexTrapezoid(0F, 0F, 0F, 1, 12, 10, 0F, -1.00F, -1.00F, 0F, 0.00F, 0.00F, 0.00F, ModelRendererTurbo.MR_LEFT); // TankFront
		baseModel[32].setRotationPoint(21F, -30F, 77F);
		baseModel[32].rotateAngleY = 3.14159265F;

		baseModel[33].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, 0F, -1F, 0F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F); // TankFront2
		baseModel[33].setRotationPoint(20F, -30F, 67F);
		baseModel[33].rotateAngleY = -1.57079633F;

		baseModel[34].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, 0F, -1F, 0F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F); // TankFront2
		baseModel[34].setRotationPoint(46F, -30F, 77F);
		baseModel[34].rotateAngleY = 1.57079633F;

		baseModel[35].addShapeBox(0F, 0F, 0F, 24, 12, 1, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TankFront2
		baseModel[35].setRotationPoint(45F, -30F, 78F);
		baseModel[35].rotateAngleY = 3.14159265F;

		baseModel[36].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, 0F, -1F, 0F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F); // TankFront2
		baseModel[36].setRotationPoint(21F, -30F, 78F);
		baseModel[36].rotateAngleY = -3.12413936F;

		baseModel[37].addBox(0F, 0F, 0F, 8, 2, 8, 0F); // TankInput
		baseModel[37].setRotationPoint(36F, -32F, 68F);

		baseModel[38].addBox(0F, 0F, 0F, 16, 7, 16, 0F); // EnergyInput
		baseModel[38].setRotationPoint(16F, -39F, 16F);

		baseModel[39].addBox(0F, 0F, 0F, 12, 7, 12, 0F); // EnergyInput2
		baseModel[39].setRotationPoint(18F, -47F, 18F);

		baseModel[40].addFlexTrapezoid(0F, 0F, 0F, 16, 1, 16, 0F, -1.00F, -1.00F, -1.00F, -1.00F, 0.00F, 0.00F, ModelRendererTurbo.MR_TOP); // EnergyInput
		baseModel[40].setRotationPoint(16F, -40F, 16F);

		baseModel[41].addFlexTrapezoid(0F, 0F, 0F, 12, 1, 12, 0F, -1.00F, -1.00F, -1.00F, -1.00F, 0.00F, 0.00F, ModelRendererTurbo.MR_TOP); // EnergyInput2
		baseModel[41].setRotationPoint(18F, -48F, 18F);

		baseModel[42].addBox(0F, 0F, 0F, 16, 16, 15, 0F); // PaperInputMain
		baseModel[42].setRotationPoint(0F, -32F, 64F);

		baseModel[43].addFlexTrapezoid(0F, 0F, 0F, 14, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, ModelRendererTurbo.MR_BOTTOM); // PaperInputMain
		baseModel[43].setRotationPoint(1F, -18F, 78.5F);

		baseModel[44].addFlexTrapezoid(0F, 0F, 0F, 14, 1, 1, 0F, -1.00F, -1.00F, 0F, 0F, 0F, 0F, ModelRendererTurbo.MR_TOP); // PaperInputMain
		baseModel[44].setRotationPoint(1F, -20F, 78.5F);

		baseModel[45].addFlexTrapezoid(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, ModelRendererTurbo.MR_TOP); // PaperInputMain
		baseModel[45].setRotationPoint(1F, -19F, 78.5F);

		baseModel[46].addFlexTrapezoid(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, ModelRendererTurbo.MR_TOP); // PaperInputMain
		baseModel[46].setRotationPoint(14F, -19F, 78.5F);

		baseModel[47].addBox(0F, 0F, 0F, 2, 8, 2, 0F); // InkPipe
		baseModel[47].setRotationPoint(17F, -24F, 64F);

		baseModel[48].addBox(0F, 0F, 0F, 2, 2, 14, 0F); // InkPipe2
		baseModel[48].setRotationPoint(17F, -18F, 66F);

		baseModel[49].addBox(0F, 0F, 0F, 2, 8, 2, 0F); // InkPipe
		baseModel[49].setRotationPoint(24F, -24F, 78F);

		baseModel[50].addBox(0F, 0F, 0F, 5, 2, 2, 0F); // InkPipe
		baseModel[50].setRotationPoint(19F, -18F, 78F);

		baseModel[51].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // InkPipe
		baseModel[51].setRotationPoint(23F, -25F, 77.5F);

		baseModel[52].addBox(0F, 0F, 0F, 6, 1, 2, 0F); // FrontPreviewHolderBase
		baseModel[52].setRotationPoint(20F, -17F, 7F);

		baseModel[53].addBox(0F, 0F, 0F, 2, 3, 2, 0F); // FrontPreviewHolderRod
		baseModel[53].setRotationPoint(22F, -20F, 7F);

		baseModel[54].addBox(0F, -0.5F, -1F, 14, 2, 1, 0F); // FrontPreviewHolder2
		baseModel[54].setRotationPoint(16F, -18F, 1F);
		baseModel[54].rotateAngleX = 0.34906585F;

		baseModel[55].addBox(0F, 0F, 0F, 6, 1, 8, 0F); // PaperPage
		baseModel[55].setRotationPoint(4F, -17F, 3F);

		baseModel[56].addBox(0F, 0F, 0F, 6, 1, 8, 0F); // PaperPage
		baseModel[56].setRotationPoint(3F, -18F, 4F);
		baseModel[56].rotateAngleY = -0.27925268F;

		baseModel[57].addBox(0F, 0F, 0F, 6, 1, 8, 0F); // PaperPage
		baseModel[57].setRotationPoint(5F, -19F, 2F);
		baseModel[57].rotateAngleY = 0.31415927F;

		baseModel[58].addBox(0F, 0F, 0F, 6, 1, 8, 0F); // PaperPage
		baseModel[58].setRotationPoint(4F, -20F, 3F);
		baseModel[58].rotateAngleY = -0.03490659F;

		baseModel[59].addBox(0F, 0F, 0F, 6, 1, 8, 0F); // PaperPage
		baseModel[59].setRotationPoint(8F, -21F, 2F);
		baseModel[59].rotateAngleY = 1.01229097F;

		baseModel[60].addBox(0F, 0F, 0F, 6, 1, 8, 0F); // PaperPage
		baseModel[60].setRotationPoint(3F, -22F, 4F);
		baseModel[60].rotateAngleY = -0.33161256F;

		baseModel[61].addBox(0F, 0F, 0F, 6, 1, 8, 0F); // PaperPage
		baseModel[61].setRotationPoint(5F, -23F, 2F);
		baseModel[61].rotateAngleY = 0.26179939F;

		baseModel[62].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // PenHolder
		baseModel[62].setRotationPoint(12F, -18F, 7F);

		baseModel[63].addBox(0F, -4F, 0F, 1, 4, 1, 0F); // Pen
		baseModel[63].setRotationPoint(12.5F, -18F, 7.5F);
		baseModel[63].rotateAngleX = -0.20943951F;
		baseModel[63].rotateAngleZ = 0.2443461F;

		//Rollers
		rollerModel = new ModelRendererTurbo[18];

		rollerModel[0] = new ModelRendererTurbo(this, 0, 99, textureX, textureY); // ProductionAxle1
		rollerModel[1] = new ModelRendererTurbo(this, 0, 99, textureX, textureY); // ProductionAxle2
		rollerModel[2] = new ModelRendererTurbo(this, 0, 99, textureX, textureY); // ProductionAxle3
		rollerModel[3] = new ModelRendererTurbo(this, 0, 99, textureX, textureY); // ProductionAxle4
		rollerModel[4] = new ModelRendererTurbo(this, 0, 99, textureX, textureY); // ProductionAxle5
		rollerModel[5] = new ModelRendererTurbo(this, 0, 99, textureX, textureY); // ProductionAxle6

		rollerModel[6] = new ModelRendererTurbo(this, 0, 99, textureX, textureY); // ProductionAxle1
		rollerModel[7] = new ModelRendererTurbo(this, 0, 99, textureX, textureY); // ProductionAxle2
		rollerModel[8] = new ModelRendererTurbo(this, 0, 99, textureX, textureY); // ProductionAxle3
		rollerModel[9] = new ModelRendererTurbo(this, 0, 99, textureX, textureY); // ProductionAxle4
		rollerModel[10] = new ModelRendererTurbo(this, 0, 99, textureX, textureY); // ProductionAxle5
		rollerModel[11] = new ModelRendererTurbo(this, 0, 99, textureX, textureY); // ProductionAxle6

		rollerModel[12] = new ModelRendererTurbo(this, 0, 120, textureX, textureY); // ProductionRoller1
		rollerModel[13] = new ModelRendererTurbo(this, 0, 120, textureX, textureY); // ProductionRoller3
		rollerModel[14] = new ModelRendererTurbo(this, 0, 120, textureX, textureY); // ProductionRoller5
		rollerModel[15] = new ModelRendererTurbo(this, 0, 120, textureX, textureY); // ProductionRoller2
		rollerModel[16] = new ModelRendererTurbo(this, 0, 120, textureX, textureY); // ProductionRoller4
		rollerModel[17] = new ModelRendererTurbo(this, 0, 120, textureX, textureY); // ProductionRoller6

		rollerModel[0].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle1
		rollerModel[0].setRotationPoint(32F, -26F, 22F);

		rollerModel[1].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle2
		rollerModel[1].setRotationPoint(32F, -21F, 28F);

		rollerModel[2].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle3
		rollerModel[2].setRotationPoint(32F, -26F, 34F);

		rollerModel[3].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle4
		rollerModel[3].setRotationPoint(32F, -21F, 40F);

		rollerModel[4].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle5
		rollerModel[4].setRotationPoint(32F, -26F, 46F);

		rollerModel[5].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle6
		rollerModel[5].setRotationPoint(32F, -21F, 52F);

		rollerModel[6].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle1
		rollerModel[6].setRotationPoint(45F, -26F, 22F);

		rollerModel[7].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle2
		rollerModel[7].setRotationPoint(45F, -21F, 28F);

		rollerModel[8].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle3
		rollerModel[8].setRotationPoint(45F, -26F, 34F);

		rollerModel[9].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle4
		rollerModel[9].setRotationPoint(45F, -21F, 40F);

		rollerModel[10].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle5
		rollerModel[10].setRotationPoint(45F, -26F, 46F);

		rollerModel[11].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle6
		rollerModel[11].setRotationPoint(45F, -21F, 52F);

		rollerModel[12].flip = flipped;
		rollerModel[12].addShape3D(3F, -3F, 0F, new Shape2D(new Coord2D[]{new Coord2D(1, 0, 1, 0), new Coord2D(5, 0, 5, 0), new Coord2D(6, 1, 6, 1), new Coord2D(6, 5, 6, 5), new Coord2D(5, 6, 5, 6), new Coord2D(1, 6, 1, 6), new Coord2D(0, 5, 0, 5), new Coord2D(0, 1, 0, 1)}), 12, 6, 6, 24, 12, ModelRendererTurbo.MR_FRONT, new float[]{2, 4, 2, 4, 2, 4, 2, 4}); // ProductionRoller1
		rollerModel[12].setRotationPoint(33F, -26F*modifier, 22F*modifier);
		rollerModel[12].rotateAngleY = 1.57079633F;

		rollerModel[13].flip = flipped;
		rollerModel[13].addShape3D(3F, -3F, 0F, new Shape2D(new Coord2D[]{new Coord2D(1, 0, 1, 0), new Coord2D(5, 0, 5, 0), new Coord2D(6, 1, 6, 1), new Coord2D(6, 5, 6, 5), new Coord2D(5, 6, 5, 6), new Coord2D(1, 6, 1, 6), new Coord2D(0, 5, 0, 5), new Coord2D(0, 1, 0, 1)}), 12, 6, 6, 24, 12, ModelRendererTurbo.MR_FRONT, new float[]{2, 4, 2, 4, 2, 4, 2, 4}); // ProductionRoller2
		rollerModel[13].setRotationPoint(33F, -21F*modifier, 28F*modifier);
		rollerModel[13].rotateAngleY = 1.57079633F;

		rollerModel[14].flip = flipped;
		rollerModel[14].addShape3D(3F, -3F, 0F, new Shape2D(new Coord2D[]{new Coord2D(1, 0, 1, 0), new Coord2D(5, 0, 5, 0), new Coord2D(6, 1, 6, 1), new Coord2D(6, 5, 6, 5), new Coord2D(5, 6, 5, 6), new Coord2D(1, 6, 1, 6), new Coord2D(0, 5, 0, 5), new Coord2D(0, 1, 0, 1)}), 12, 6, 6, 24, 12, ModelRendererTurbo.MR_FRONT, new float[]{2, 4, 2, 4, 2, 4, 2, 4}); // ProductionRoller3
		rollerModel[14].setRotationPoint(33F, -26F*modifier, 46F*modifier);
		rollerModel[14].rotateAngleY = 1.57079633F;

		rollerModel[15].flip = flipped;
		rollerModel[15].addShape3D(3F, -3F, 0F, new Shape2D(new Coord2D[]{new Coord2D(1, 0, 1, 0), new Coord2D(5, 0, 5, 0), new Coord2D(6, 1, 6, 1), new Coord2D(6, 5, 6, 5), new Coord2D(5, 6, 5, 6), new Coord2D(1, 6, 1, 6), new Coord2D(0, 5, 0, 5), new Coord2D(0, 1, 0, 1)}), 12, 6, 6, 24, 12, ModelRendererTurbo.MR_FRONT, new float[]{2, 4, 2, 4, 2, 4, 2, 4}); // ProductionRoller4
		rollerModel[15].setRotationPoint(33F, -21F*modifier, 40F*modifier);
		rollerModel[15].rotateAngleY = 1.57079633F;

		rollerModel[16].flip = flipped;
		rollerModel[16].addShape3D(3F, -3F, 0F, new Shape2D(new Coord2D[]{new Coord2D(1, 0, 1, 0), new Coord2D(5, 0, 5, 0), new Coord2D(6, 1, 6, 1), new Coord2D(6, 5, 6, 5), new Coord2D(5, 6, 5, 6), new Coord2D(1, 6, 1, 6), new Coord2D(0, 5, 0, 5), new Coord2D(0, 1, 0, 1)}), 12, 6, 6, 24, 12, ModelRendererTurbo.MR_FRONT, new float[]{2, 4, 2, 4, 2, 4, 2, 4}); // ProductionRoller5
		rollerModel[16].setRotationPoint(33F, -26F*modifier, 34F*modifier);
		rollerModel[16].rotateAngleY = 1.57079633F;

		rollerModel[17].flip = flipped;
		rollerModel[17].addShape3D(3F, -3F, 0F, new Shape2D(new Coord2D[]{new Coord2D(1, 0, 1, 0), new Coord2D(5, 0, 5, 0), new Coord2D(6, 1, 6, 1), new Coord2D(6, 5, 6, 5), new Coord2D(5, 6, 5, 6), new Coord2D(1, 6, 1, 6), new Coord2D(0, 5, 0, 5), new Coord2D(0, 1, 0, 1)}), 12, 6, 6, 24, 12, ModelRendererTurbo.MR_FRONT, new float[]{2, 4, 2, 4, 2, 4, 2, 4}); // ProductionRoller6
		rollerModel[17].setRotationPoint(33F, -21F*modifier, 52F*modifier);
		rollerModel[17].rotateAngleY = 1.57079633F;

		paperInserterDoorModel = new ModelRendererTurbo[1];
		paperInserterDoorModel[0] = new ModelRendererTurbo(this, 0, 47, textureX, textureY); // ProductionPaperInserterDoor
		paperInserterDoorModel[0].addBox(0F, -1F, 0F, 2, 1, 10, 0F); // ProductionPaperInserterDoor
		paperInserterDoorModel[0].setRotationPoint(32F, -31F, 54F);
		paperInserterDoorModel[0].rotateAngleZ = -1.57079633F;

		if(flipped)
			for(ModelRendererTurbo mod : rollerModel)
				mod.rotateAngleY*=-1;

		parts.put("base", baseModel);
		parts.put("rollerModel", rollerModel);
		parts.put("paperInserterDoor", paperInserterDoorModel);
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
				GlStateManager.translate(mirrored?-2f: -1f, -2f, mirrored?-2f: 2f);
			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(mirrored?90f: 270F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?-1f: -2f, -2f, mirrored?1f: -1f);
			}
			break;
			case EAST:
			{
				if(mirrored)
					GlStateManager.rotate(180, 0, 1, 0);
				GlStateManager.translate(mirrored?-3: 0, -2f, 0f);
			}
			break;
			case WEST:
			{
				if(!mirrored)
					GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?0: -3, -2f, mirrored?-1f: 1f);
			}
			break;
		}
	}
}
