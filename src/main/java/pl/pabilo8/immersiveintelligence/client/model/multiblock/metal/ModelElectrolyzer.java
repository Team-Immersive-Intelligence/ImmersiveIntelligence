package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.BaseBlockModel;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * Created by Pabilo8 on 20-06-2019.
 */
public class ModelElectrolyzer extends BaseBlockModel
{
	int textureX = 256;
	int textureY = 256;

	public ModelElectrolyzer() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[71];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BaseBox
		baseModel[1] = new ModelRendererTurbo(this, 68, 120, textureX, textureY); // UpperBox
		baseModel[2] = new ModelRendererTurbo(this, 17, 57, textureX, textureY); // BorderBox
		baseModel[3] = new ModelRendererTurbo(this, 17, 57, textureX, textureY); // BorderBox
		baseModel[4] = new ModelRendererTurbo(this, 0, 56, textureX, textureY); // UpperBoxGauge
		baseModel[5] = new ModelRendererTurbo(this, 0, 56, textureX, textureY); // UpperBoxGauge
		baseModel[6] = new ModelRendererTurbo(this, 14, 56, textureX, textureY); // UpperBoxPlate
		baseModel[7] = new ModelRendererTurbo(this, 0, 106, textureX, textureY); // TankMain
		baseModel[8] = new ModelRendererTurbo(this, 4, 124, textureX, textureY); // TankStand
		baseModel[9] = new ModelRendererTurbo(this, 4, 124, textureX, textureY); // TankStand
		baseModel[10] = new ModelRendererTurbo(this, 44, 115, textureX, textureY); // TankFront
		baseModel[11] = new ModelRendererTurbo(this, 0, 115, textureX, textureY); // TankCorner
		baseModel[12] = new ModelRendererTurbo(this, 136, 97, textureX, textureY); // TankSide
		baseModel[13] = new ModelRendererTurbo(this, 44, 115, textureX, textureY); // TankFront
		baseModel[14] = new ModelRendererTurbo(this, 0, 115, textureX, textureY); // TankCorner
		baseModel[15] = new ModelRendererTurbo(this, 0, 115, textureX, textureY); // TankCorner
		baseModel[16] = new ModelRendererTurbo(this, 136, 97, textureX, textureY); // TankSide
		baseModel[17] = new ModelRendererTurbo(this, 0, 115, textureX, textureY); // TankCorner
		baseModel[18] = new ModelRendererTurbo(this, 4, 124, textureX, textureY); // TankStand
		baseModel[19] = new ModelRendererTurbo(this, 4, 124, textureX, textureY); // TankStand
		baseModel[20] = new ModelRendererTurbo(this, 0, 106, textureX, textureY); // TankMain
		baseModel[21] = new ModelRendererTurbo(this, 4, 124, textureX, textureY); // TankStand
		baseModel[22] = new ModelRendererTurbo(this, 4, 124, textureX, textureY); // TankStand
		baseModel[23] = new ModelRendererTurbo(this, 44, 115, textureX, textureY); // TankFront
		baseModel[24] = new ModelRendererTurbo(this, 0, 115, textureX, textureY); // TankCorner
		baseModel[25] = new ModelRendererTurbo(this, 136, 97, textureX, textureY); // TankSide
		baseModel[26] = new ModelRendererTurbo(this, 44, 115, textureX, textureY); // TankFront
		baseModel[27] = new ModelRendererTurbo(this, 0, 115, textureX, textureY); // TankCorner
		baseModel[28] = new ModelRendererTurbo(this, 0, 115, textureX, textureY); // TankCorner
		baseModel[29] = new ModelRendererTurbo(this, 136, 97, textureX, textureY); // TankSide
		baseModel[30] = new ModelRendererTurbo(this, 0, 115, textureX, textureY); // TankCorner
		baseModel[31] = new ModelRendererTurbo(this, 4, 124, textureX, textureY); // TankStand
		baseModel[32] = new ModelRendererTurbo(this, 4, 124, textureX, textureY); // TankStand
		baseModel[33] = new ModelRendererTurbo(this, 66, 88, textureX, textureY); // InputTankBase
		baseModel[34] = new ModelRendererTurbo(this, 95, 56, textureX, textureY); // InputTankGlassTop
		baseModel[35] = new ModelRendererTurbo(this, 0, 142, textureX, textureY); // EnergyInput
		baseModel[36] = new ModelRendererTurbo(this, 0, 166, textureX, textureY); // FluidyInput
		baseModel[37] = new ModelRendererTurbo(this, 132, 131, textureX, textureY); // InputTankGlass
		baseModel[38] = new ModelRendererTurbo(this, 132, 131, textureX, textureY); // InputTankGlass
		baseModel[39] = new ModelRendererTurbo(this, 64, 160, textureX, textureY); // InputTankGlass
		baseModel[40] = new ModelRendererTurbo(this, 64, 177, textureX, textureY); // FluidyInputPipe
		baseModel[41] = new ModelRendererTurbo(this, 90, 160, textureX, textureY); // ElectrolyzerRod
		baseModel[42] = new ModelRendererTurbo(this, 90, 160, textureX, textureY); // ElectrolyzerRod
		baseModel[43] = new ModelRendererTurbo(this, 83, 190, textureX, textureY); // ElectrolyzerBox
		baseModel[44] = new ModelRendererTurbo(this, 43, 190, textureX, textureY); // ElectrolyzerUpperBox
		baseModel[45] = new ModelRendererTurbo(this, 84, 177, textureX, textureY); // ElectrolyzerEnergyPort
		baseModel[46] = new ModelRendererTurbo(this, 116, 175, textureX, textureY); // ElectrolyzerEnergyCable
		baseModel[47] = new ModelRendererTurbo(this, 132, 175, textureX, textureY); // ElectrolyzerEnergyCable
		baseModel[48] = new ModelRendererTurbo(this, 140, 185, textureX, textureY); // ElectrolyzerEnergyCable
		baseModel[49] = new ModelRendererTurbo(this, 148, 177, textureX, textureY); // ElectrolyzerEnergyCable
		baseModel[50] = new ModelRendererTurbo(this, 124, 185, textureX, textureY); // ElectrolyzerEnergyCable
		baseModel[51] = new ModelRendererTurbo(this, 116, 120, textureX, textureY); // PowerInputSmallBox
		baseModel[52] = new ModelRendererTurbo(this, 136, 193, textureX, textureY); // ElectrolyzerEnergyCable
		baseModel[53] = new ModelRendererTurbo(this, 156, 185, textureX, textureY); // ElectrolyzerEnergyCable
		baseModel[54] = new ModelRendererTurbo(this, 118, 184, textureX, textureY); // ElectrolyzerEnergyCable
		baseModel[55] = new ModelRendererTurbo(this, 0, 190, textureX, textureY); // OutputTop
		baseModel[56] = new ModelRendererTurbo(this, 0, 190, textureX, textureY); // OutputTop
		baseModel[57] = new ModelRendererTurbo(this, 0, 190, textureX, textureY); // OutputTop
		baseModel[58] = new ModelRendererTurbo(this, 16, 190, textureX, textureY); // OutputPipe
		baseModel[59] = new ModelRendererTurbo(this, 28, 190, textureX, textureY); // OutputPipe
		baseModel[60] = new ModelRendererTurbo(this, 0, 195, textureX, textureY); // OutputPipe
		baseModel[61] = new ModelRendererTurbo(this, 26, 194, textureX, textureY); // OutputPipe
		baseModel[62] = new ModelRendererTurbo(this, 39, 191, textureX, textureY); // OutputPipe
		baseModel[63] = new ModelRendererTurbo(this, 0, 190, textureX, textureY); // OutputTop
		baseModel[64] = new ModelRendererTurbo(this, 28, 190, textureX, textureY); // OutputPipe
		baseModel[65] = new ModelRendererTurbo(this, 0, 195, textureX, textureY); // OutputPipe
		baseModel[66] = new ModelRendererTurbo(this, 39, 191, textureX, textureY); // OutputPipe
		baseModel[67] = new ModelRendererTurbo(this, 0, 65, textureX, textureY); // TankFrontInput
		baseModel[68] = new ModelRendererTurbo(this, 0, 65, textureX, textureY); // TankFrontInput
		baseModel[69] = new ModelRendererTurbo(this, 26, 194, textureX, textureY); // OutputPipe
		baseModel[70] = new ModelRendererTurbo(this, 16, 190, textureX, textureY); // OutputPipe

		baseModel[0].addBox(0F, 0F, 0F, 48, 8, 48, 0F); // BaseBox
		baseModel[0].setRotationPoint(0F, -8F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 16, 24, 16, 0F); // UpperBox
		baseModel[1].setRotationPoint(16F, -32F, 1F);

		baseModel[2].addBox(0F, 0F, 0F, 1, 1, 30, 0F); // BorderBox
		baseModel[2].setRotationPoint(0F, -9F, 2F);

		baseModel[3].addBox(0F, 0F, 0F, 1, 1, 30, 0F); // BorderBox
		baseModel[3].setRotationPoint(47F, -9F, 2F);

		baseModel[4].addBox(0F, 0F, 0F, 6, 6, 1, 0F); // UpperBoxGauge
		baseModel[4].setRotationPoint(17F, -31F, 0F);

		baseModel[5].addBox(0F, 0F, 0F, 6, 6, 1, 0F); // UpperBoxGauge
		baseModel[5].setRotationPoint(25F, -31F, 0F);

		baseModel[6].addBox(0F, 0F, 0F, 14, 8, 1, 0F); // UpperBoxPlate
		baseModel[6].setRotationPoint(17F, -22F, 0F);

		baseModel[7].addBox(0F, 0F, 0F, 10, 12, 22, 0F); // TankMain
		baseModel[7].setRotationPoint(35F, -22F, 3F);

		baseModel[8].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // TankStand
		baseModel[8].setRotationPoint(35F, -10F, 4F);

		baseModel[9].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // TankStand
		baseModel[9].setRotationPoint(43F, -10F, 4F);

		baseModel[10].addFlexTrapezoid(0F, 0F, 0F, 10, 12, 1, 0F, -1.00F, -1.00F, 0F, 0.00F, 0.00F, 0.00F, ModelRendererTurbo.MR_FRONT); // TankFront
		baseModel[10].setRotationPoint(35F, -22F, 2F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // TankCorner
		baseModel[11].setRotationPoint(34F, -22F, 2F);

		baseModel[12].addShapeBox(0F, 0F, 0F, 1, 12, 22, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // TankSide
		baseModel[12].setRotationPoint(34F, -22F, 3F);

		baseModel[13].addFlexTrapezoid(0F, 0F, 0F, 10, 12, 1, 0F, -1.00F, -1.00F, 0F, 0.00F, 0.00F, 0.00F, ModelRendererTurbo.MR_FRONT); // TankFront
		baseModel[13].setRotationPoint(45F, -22F, 26F);
		baseModel[13].rotateAngleY = 3.14159265F;

		baseModel[14].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // TankCorner
		baseModel[14].setRotationPoint(34F, -22F, 26F);
		baseModel[14].rotateAngleY = -1.57079633F;

		baseModel[15].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // TankCorner
		baseModel[15].setRotationPoint(46F, -22F, 2F);
		baseModel[15].rotateAngleY = 1.57079633F;

		baseModel[16].addShapeBox(0F, 0F, 0F, 1, 12, 22, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // TankSide
		baseModel[16].setRotationPoint(46F, -22F, 25F);
		baseModel[16].rotateAngleY = 3.14159265F;

		baseModel[17].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // TankCorner
		baseModel[17].setRotationPoint(46F, -22F, 26F);
		baseModel[17].rotateAngleY = -3.12413936F;

		baseModel[18].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // TankStand
		baseModel[18].setRotationPoint(35F, -10F, 22F);

		baseModel[19].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // TankStand
		baseModel[19].setRotationPoint(43F, -10F, 22F);

		baseModel[20].addBox(0F, 0F, 0F, 10, 12, 22, 0F); // TankMain
		baseModel[20].setRotationPoint(3F, -22F, 3F);

		baseModel[21].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // TankStand
		baseModel[21].setRotationPoint(3F, -10F, 4F);

		baseModel[22].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // TankStand
		baseModel[22].setRotationPoint(11F, -10F, 4F);

		baseModel[23].addFlexTrapezoid(0F, 0F, 0F, 10, 12, 1, 0F, -1.00F, -1.00F, 0F, 0.00F, 0.00F, 0.00F, ModelRendererTurbo.MR_FRONT); // TankFront
		baseModel[23].setRotationPoint(3F, -22F, 2F);

		baseModel[24].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // TankCorner
		baseModel[24].setRotationPoint(2F, -22F, 2F);

		baseModel[25].addShapeBox(0F, 0F, 0F, 1, 12, 22, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // TankSide
		baseModel[25].setRotationPoint(2F, -22F, 3F);

		baseModel[26].addFlexTrapezoid(0F, 0F, 0F, 10, 12, 1, 0F, -1.00F, -1.00F, 0F, 0.00F, 0.00F, 0.00F, ModelRendererTurbo.MR_FRONT); // TankFront
		baseModel[26].setRotationPoint(13F, -22F, 26F);
		baseModel[26].rotateAngleY = 3.14159265F;

		baseModel[27].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // TankCorner
		baseModel[27].setRotationPoint(2F, -22F, 26F);
		baseModel[27].rotateAngleY = -1.57079633F;

		baseModel[28].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // TankCorner
		baseModel[28].setRotationPoint(14F, -22F, 2F);
		baseModel[28].rotateAngleY = 1.57079633F;

		baseModel[29].addShapeBox(0F, 0F, 0F, 1, 12, 22, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // TankSide
		baseModel[29].setRotationPoint(14F, -22F, 25F);
		baseModel[29].rotateAngleY = 3.14159265F;

		baseModel[30].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // TankCorner
		baseModel[30].setRotationPoint(14F, -22F, 26F);
		baseModel[30].rotateAngleY = -3.12413936F;

		baseModel[31].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // TankStand
		baseModel[31].setRotationPoint(3F, -10F, 22F);

		baseModel[32].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // TankStand
		baseModel[32].setRotationPoint(11F, -10F, 22F);

		baseModel[33].addBox(0F, 0F, 0F, 16, 1, 30, 0F); // InputTankBase
		baseModel[33].setRotationPoint(16F, -9F, 17F);

		baseModel[34].addShapeBox(0F, 0F, 0F, 12, 1, 29, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // InputTankGlassTop
		baseModel[34].setRotationPoint(18F, -26F, 17F);

		baseModel[35].addBox(0F, 0F, 0F, 16, 8, 16, 0F); // EnergyInput
		baseModel[35].setRotationPoint(32F, -16F, 32F);

		baseModel[36].addBox(0F, 0F, 0F, 16, 8, 16, 0F); // FluidyInput
		baseModel[36].setRotationPoint(0F, -16F, 32F);

		baseModel[37].addBox(0F, 0F, 0F, 1, 16, 28, 0F); // InputTankGlass
		baseModel[37].setRotationPoint(18F, -25F, 17F);

		baseModel[38].addBox(0F, 0F, 0F, 1, 16, 28, 0F); // InputTankGlass
		baseModel[38].setRotationPoint(29F, -25F, 17F);

		baseModel[39].addBox(0F, 0F, 0F, 12, 16, 1, 0F); // InputTankGlass
		baseModel[39].setRotationPoint(18F, -25F, 45F);

		baseModel[40].addBox(0F, 0F, 0F, 4, 6, 6, 0F); // FluidyInputPipe
		baseModel[40].setRotationPoint(16F, -15.5F, 36F);

		baseModel[41].addShapeBox(0F, 0F, 0F, 2, 15, 2, 0F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F); // ElectrolyzerRod
		baseModel[41].setRotationPoint(26F, -27F, 31F);

		baseModel[42].addShapeBox(0F, 0F, 0F, 2, 15, 2, 0F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F); // ElectrolyzerRod
		baseModel[42].setRotationPoint(20F, -27F, 31F);

		baseModel[43].addShapeBox(0F, 0F, 0F, 12, 1, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // ElectrolyzerBox
		baseModel[43].setRotationPoint(18F, -28F, 28F);

		baseModel[44].addShapeBox(0F, 0F, 0F, 12, 1, 8, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // ElectrolyzerUpperBox
		baseModel[44].setRotationPoint(18F, -29F, 28F);

		baseModel[45].addShapeBox(0F, 0F, 0F, 1, 6, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // ElectrolyzerEnergyPort
		baseModel[45].setRotationPoint(30F, -31F, 29F);

		baseModel[46].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // ElectrolyzerEnergyCable
		baseModel[46].setRotationPoint(31F, -30F, 30F);

		baseModel[47].addBox(0F, 0F, 0F, 4, 6, 4, 0F); // ElectrolyzerEnergyCable
		baseModel[47].setRotationPoint(31F, -26F, 30F);

		baseModel[48].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F); // ElectrolyzerEnergyCable
		baseModel[48].setRotationPoint(31F, -20F, 30F);

		baseModel[49].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // ElectrolyzerEnergyCable
		baseModel[49].setRotationPoint(31F, -20F, 26F);

		baseModel[50].addShapeBox(0F, 0F, 0F, 4, 6, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F); // ElectrolyzerEnergyCable
		baseModel[50].setRotationPoint(31F, -16F, 26F);

		baseModel[51].addBox(0F, 0F, 0F, 6, 6, 1, 0F); // PowerInputSmallBox
		baseModel[51].setRotationPoint(38F, -15F, 31F);

		baseModel[52].addBox(0F, 0F, 0F, 7, 4, 4, 0F); // ElectrolyzerEnergyCable
		baseModel[52].setRotationPoint(35F, -14F, 26F);

		baseModel[53].addShapeBox(0F, 0F, 0F, 1, 4, 4, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // ElectrolyzerEnergyCable
		baseModel[53].setRotationPoint(42F, -14F, 26F);

		baseModel[54].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // ElectrolyzerEnergyCable
		baseModel[54].setRotationPoint(39F, -14F, 30F);

		baseModel[55].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // OutputTop
		baseModel[55].setRotationPoint(25F, -27F, 19F);

		baseModel[56].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // OutputTop
		baseModel[56].setRotationPoint(38F, -23F, 19F);

		baseModel[57].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // OutputTop
		baseModel[57].setRotationPoint(6F, -23F, 19F);

		baseModel[58].addBox(0F, 0F, 0F, 3, 2, 3, 0F); // OutputPipe
		baseModel[58].setRotationPoint(25.5F, -29F, 19.5F);

		baseModel[59].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // OutputPipe
		baseModel[59].setRotationPoint(25.5F, -30F, 19.5F);

		baseModel[60].addBox(0F, 0F, 0F, 10, 3, 3, 0F); // OutputPipe
		baseModel[60].setRotationPoint(28.5F, -30F, 19.5F);

		baseModel[61].addBox(0F, 0F, 0F, 3, 6, 3, 0F); // OutputPipe
		baseModel[61].setRotationPoint(38.5F, -29F, 19.5F);

		baseModel[62].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // OutputPipe
		baseModel[62].setRotationPoint(38.5F, -30F, 19.5F);

		baseModel[63].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // OutputTop
		baseModel[63].setRotationPoint(19F, -27F, 19F);

		baseModel[64].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // OutputPipe
		baseModel[64].setRotationPoint(19.5F, -30F, 19.5F);

		baseModel[65].addBox(0F, 0F, 0F, 10, 3, 3, 0F); // OutputPipe
		baseModel[65].setRotationPoint(9.5F, -30F, 19.5F);

		baseModel[66].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // OutputPipe
		baseModel[66].setRotationPoint(6.5F, -30F, 19.5F);

		baseModel[67].addBox(0F, 0F, 0F, 16, 8, 2, 0F); // TankFrontInput
		baseModel[67].setRotationPoint(32F, -16F, 0F);

		baseModel[68].addBox(0F, 0F, 0F, 16, 8, 2, 0F); // TankFrontInput
		baseModel[68].setRotationPoint(0F, -16F, 0F);

		baseModel[69].addBox(0F, 0F, 0F, 3, 6, 3, 0F); // OutputPipe
		baseModel[69].setRotationPoint(9.5F, -29F, 22.5F);
		baseModel[69].rotateAngleY = 3.14159265F;

		baseModel[70].addBox(0F, 0F, 0F, 3, 2, 3, 0F); // OutputPipe
		baseModel[70].setRotationPoint(22.5F, -29F, 22.5F);
		baseModel[70].rotateAngleY = 3.14159265F;


		translateAll(0F, 0F, 0F);


		flipAll();
	}

	@Override
	public void getBlockRotation(EnumFacing facing, BaseBlockModel model)
	{
		switch(facing)
		{
			case NORTH:
			{
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-2f, 0f, 0f);
			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(-1f, 0f, 1f);
			}
			break;
			case EAST:
			{
				GlStateManager.translate(-2f, 0f, 1f);
			}
			break;
			case WEST:
			{
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(-1f, 0f, 0f);
			}
			break;
		}
	}
}
