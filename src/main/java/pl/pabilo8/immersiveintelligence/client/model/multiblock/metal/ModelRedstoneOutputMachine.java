package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import pl.pabilo8.immersiveintelligence.client.model.BaseBlockModel;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * Created by Pabilo8 on 20-06-2019.
 */
public class ModelRedstoneOutputMachine extends BaseBlockModel
{
	int textureX = 128;
	int textureY = 128;

	public ModelRedstoneOutputMachine() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[53];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BaseBox
		baseModel[1] = new ModelRendererTurbo(this, 71, 75, textureX, textureY); // InputData
		baseModel[2] = new ModelRendererTurbo(this, 36, 36, textureX, textureY); // InputData
		baseModel[3] = new ModelRendererTurbo(this, 36, 36, textureX, textureY); // InputData
		baseModel[4] = new ModelRendererTurbo(this, 0, 36, textureX, textureY); // OutputRedstone
		baseModel[5] = new ModelRendererTurbo(this, 36, 36, textureX, textureY); // OutputRedstone
		baseModel[6] = new ModelRendererTurbo(this, 36, 36, textureX, textureY); // OutputRedstone
		baseModel[7] = new ModelRendererTurbo(this, 0, 89, textureX, textureY); // MainBox
		baseModel[8] = new ModelRendererTurbo(this, 40, 36, textureX, textureY); // OutputRedstoneBackBox
		baseModel[9] = new ModelRendererTurbo(this, 41, 77, textureX, textureY); // OutputRedstoneBackCable1
		baseModel[10] = new ModelRendererTurbo(this, 49, 78, textureX, textureY); // OutputRedstoneBackCable1
		baseModel[11] = new ModelRendererTurbo(this, 24, 50, textureX, textureY); // OutputRedstoneBackCable1
		baseModel[12] = new ModelRendererTurbo(this, 36, 44, textureX, textureY); // RedstoneCoil
		baseModel[13] = new ModelRendererTurbo(this, 72, 42, textureX, textureY); // DataCoil
		baseModel[14] = new ModelRendererTurbo(this, 0, 50, textureX, textureY); // OutputRedstoneCoilPart
		baseModel[15] = new ModelRendererTurbo(this, 0, 50, textureX, textureY); // OutputRedstoneCoilPart
		baseModel[16] = new ModelRendererTurbo(this, 64, 36, textureX, textureY); // OutputRedstoneCoilCable
		baseModel[17] = new ModelRendererTurbo(this, 2, 58, textureX, textureY); // OutputRedstoneCoilCable
		baseModel[18] = new ModelRendererTurbo(this, 54, 64, textureX, textureY); // OutputRedstoneCoilCable
		baseModel[19] = new ModelRendererTurbo(this, 48, 60, textureX, textureY); // OutputRedstoneCoilCable
		baseModel[20] = new ModelRendererTurbo(this, 66, 60, textureX, textureY); // InputtDataCoilCable
		baseModel[21] = new ModelRendererTurbo(this, 96, 44, textureX, textureY); // InputtDataCoilCable
		baseModel[22] = new ModelRendererTurbo(this, 48, 60, textureX, textureY); // InputtDataCoilCable
		baseModel[23] = new ModelRendererTurbo(this, 34, 60, textureX, textureY); // MainBoxCablePlate
		baseModel[24] = new ModelRendererTurbo(this, 70, 68, textureX, textureY); // VacuumTubeBase
		baseModel[25] = new ModelRendererTurbo(this, 89, 69, textureX, textureY); // VacuumTubeBase
		baseModel[26] = new ModelRendererTurbo(this, 17, 49, textureX, textureY); // VacuumTubeRod
		baseModel[27] = new ModelRendererTurbo(this, 9, 49, textureX, textureY); // VacuumTubeCenter
		baseModel[28] = new ModelRendererTurbo(this, 56, 41, textureX, textureY); // VacuumTubeCenter
		baseModel[29] = new ModelRendererTurbo(this, 56, 38, textureX, textureY); // VacuumTubeCenter
		baseModel[30] = new ModelRendererTurbo(this, 56, 38, textureX, textureY); // VacuumTubeCenter
		baseModel[31] = new ModelRendererTurbo(this, 56, 38, textureX, textureY); // VacuumTubeCenter
		baseModel[32] = new ModelRendererTurbo(this, 56, 38, textureX, textureY); // VacuumTubeCenter
		baseModel[33] = new ModelRendererTurbo(this, 0, 74, textureX, textureY); // VacuumTubeBelljar
		baseModel[34] = new ModelRendererTurbo(this, 12, 74, textureX, textureY); // VacuumTubeBelljar
		baseModel[35] = new ModelRendererTurbo(this, 64, 44, textureX, textureY); // DataCoilPart
		baseModel[36] = new ModelRendererTurbo(this, 64, 44, textureX, textureY); // DataCoilPart
		baseModel[37] = new ModelRendererTurbo(this, 18, 36, textureX, textureY); // DataCoilPlate
		baseModel[38] = new ModelRendererTurbo(this, 18, 36, textureX, textureY); // DataCoilPlate
		baseModel[39] = new ModelRendererTurbo(this, 111, 42, textureX, textureY); // DataCoilCablePlate
		baseModel[40] = new ModelRendererTurbo(this, 64, 44, textureX, textureY); // InputtDataCoilCable
		baseModel[41] = new ModelRendererTurbo(this, 101, 70, textureX, textureY); // InputtDataCoilCable
		baseModel[42] = new ModelRendererTurbo(this, 113, 51, textureX, textureY); // InputtDataCoilCable
		baseModel[43] = new ModelRendererTurbo(this, 96, 16, textureX, textureY); // EnergyInput
		baseModel[44] = new ModelRendererTurbo(this, 25, 76, textureX, textureY); // RedstoneCoilHolder
		baseModel[45] = new ModelRendererTurbo(this, 49, 87, textureX, textureY); // PunchtapeInput
		baseModel[46] = new ModelRendererTurbo(this, 49, 87, textureX, textureY); // PunchtapeOutput
		baseModel[47] = new ModelRendererTurbo(this, 49, 85, textureX, textureY); // PunchtapeInputBottom
		baseModel[48] = new ModelRendererTurbo(this, 49, 83, textureX, textureY); // PunchtapeOutputBottom
		baseModel[49] = new ModelRendererTurbo(this, 67, 83, textureX, textureY); // PunchtapeOutputBottomCorner
		baseModel[50] = new ModelRendererTurbo(this, 67, 83, textureX, textureY); // PunchtapeOutputBottomCorner
		baseModel[51] = new ModelRendererTurbo(this, 67, 83, textureX, textureY); // PunchtapeInputBottomCorner
		baseModel[52] = new ModelRendererTurbo(this, 67, 83, textureX, textureY); // PunchtapeInputBottomCorner

		baseModel[0].addBox(0F, 0F, 0F, 32, 4, 32, 0F); // BaseBox
		baseModel[0].setRotationPoint(0F, -4F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 8, 8, 1, 0F); // InputData
		baseModel[1].setRotationPoint(2F, -16F, 0F);

		baseModel[2].addBox(0F, 0F, 0F, 1, 4, 1, 0F); // InputData
		baseModel[2].setRotationPoint(3F, -8F, 0F);

		baseModel[3].addBox(0F, 0F, 0F, 1, 4, 1, 0F); // InputData
		baseModel[3].setRotationPoint(8F, -8F, 0F);

		baseModel[4].addBox(0F, 0F, 0F, 8, 8, 1, 0F); // OutputRedstone
		baseModel[4].setRotationPoint(2F, -16F, 31F);

		baseModel[5].addBox(0F, 0F, 0F, 1, 4, 1, 0F); // OutputRedstone
		baseModel[5].setRotationPoint(3F, -8F, 31F);

		baseModel[6].addBox(0F, 0F, 0F, 1, 4, 1, 0F); // OutputRedstone
		baseModel[6].setRotationPoint(8F, -8F, 31F);

		baseModel[7].addBox(0F, 0F, 0F, 16, 12, 16, 0F); // MainBox
		baseModel[7].setRotationPoint(10F, -16F, 2F);

		baseModel[8].addBox(0F, 0F, 0F, 6, 6, 2, 0F); // OutputRedstoneBackBox
		baseModel[8].setRotationPoint(3F, -15F, 29F);

		baseModel[9].addBox(0F, 0F, 0F, 3, 9, 1, 0F); // OutputRedstoneBackCable1
		baseModel[9].setRotationPoint(4.5F, -13F, 28F);

		baseModel[10].addBox(0F, 0F, 0F, 7, 1, 4, 0F); // OutputRedstoneBackCable1
		baseModel[10].setRotationPoint(4.5F, -5F, 24F);

		baseModel[11].addBox(0F, 0F, 0F, 1, 8, 4, 0F); // OutputRedstoneBackCable1
		baseModel[11].setRotationPoint(11.5F, -12F, 24F);

		baseModel[12].addBox(0F, 0F, 0F, 10, 8, 8, 0F); // RedstoneCoil
		baseModel[12].setRotationPoint(13.5F, -15F, 22F);

		baseModel[13].addBox(0F, 0F, 0F, 6, 6, 12, 0F); // DataCoil
		baseModel[13].setRotationPoint(3F, -15F, 2F);

		baseModel[14].addBox(0F, 0F, 0F, 1, 6, 6, 0F); // OutputRedstoneCoilPart
		baseModel[14].setRotationPoint(12.5F, -14F, 23F);

		baseModel[15].addBox(0F, 0F, 0F, 1, 6, 6, 0F); // OutputRedstoneCoilPart
		baseModel[15].setRotationPoint(23.5F, -14F, 23F);

		baseModel[16].addBox(0F, 0F, 0F, 3, 4, 4, 0F); // OutputRedstoneCoilCable
		baseModel[16].setRotationPoint(24.5F, -13F, 24F);

		baseModel[17].addShapeBox(0F, 0F, 0F, 4, 4, 12, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // OutputRedstoneCoilCable
		baseModel[17].setRotationPoint(27.5F, -8F, 12F);

		baseModel[18].addShapeBox(0F, 0F, 0F, 4, 9, 4, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // OutputRedstoneCoilCable
		baseModel[18].setRotationPoint(27.5F, -13F, 24F);

		baseModel[19].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // OutputRedstoneCoilCable
		baseModel[19].setRotationPoint(26.5F, -8F, 12F);

		baseModel[20].addShapeBox(0F, 0F, 0F, 22, 4, 4, 0F, 0.5F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0.5F, 0F, 0F, 0.5F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0.5F, 0F, 0F); // InputtDataCoilCable
		baseModel[20].setRotationPoint(9.5F, -13F, 18F);

		baseModel[21].addShapeBox(0F, 0F, 0F, 4, 4, 6, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // InputtDataCoilCable
		baseModel[21].setRotationPoint(27.5F, -13F, 12F);

		baseModel[22].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // InputtDataCoilCable
		baseModel[22].setRotationPoint(26.5F, -13F, 12F);

		baseModel[23].addBox(0F, 0F, 0F, 1, 10, 6, 0F); // MainBoxCablePlate
		baseModel[23].setRotationPoint(25.5F, -14F, 11F);

		baseModel[24].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // VacuumTubeBase
		baseModel[24].setRotationPoint(26F, -5F, 3F);

		baseModel[25].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // VacuumTubeBase
		baseModel[25].setRotationPoint(27F, -5.5F, 4F);

		baseModel[26].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, -0.625F, -0.5F, -0.625F, -0.625F, -0.5F, -0.625F, -0.625F, -0.5F, -0.625F, -0.625F, -0.5F, -0.625F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // VacuumTubeRod
		baseModel[26].setRotationPoint(28F, -10F, 5F);

		baseModel[27].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, -0.125F, 0F, -0.125F, -0.125F, 0F, -0.125F, -0.125F, 0F, -0.125F, -0.125F, 0F, -0.125F, -0.125F, 0F, -0.125F, -0.125F, 0F, -0.125F, -0.125F, 0F, -0.125F, -0.125F, 0F, -0.125F); // VacuumTubeCenter
		baseModel[27].setRotationPoint(28F, -9F, 5F);

		baseModel[28].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // VacuumTubeCenter
		baseModel[28].setRotationPoint(28F, -7.5F, 5F);

		baseModel[29].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F); // VacuumTubeCenter
		baseModel[29].setRotationPoint(28F, -6.5F, 5F);

		baseModel[30].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F); // VacuumTubeCenter
		baseModel[30].setRotationPoint(29F, -6.5F, 5F);

		baseModel[31].addShapeBox(0F, 0F, 1F, 1, 1, 1, 0F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F); // VacuumTubeCenter
		baseModel[31].setRotationPoint(28F, -6.5F, 5F);

		baseModel[32].addShapeBox(0F, 0F, 1F, 1, 1, 1, 0F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F); // VacuumTubeCenter
		baseModel[32].setRotationPoint(29F, -6.5F, 5F);

		baseModel[33].addFlexTrapezoid(0F, 0F, 0F, 3, 1, 3, 0F, -0.50F, -0.50F, -0.50F, -0.50F, -0.50F, -0.50F, ModelRendererTurbo.MR_TOP); // VacuumTubeBelljar
		baseModel[33].setRotationPoint(27.5F, -10.5F, 4.5F);

		baseModel[34].addBox(0F, 0F, 0F, 3, 4, 3, 0F); // VacuumTubeBelljar
		baseModel[34].setRotationPoint(27.5F, -9.5F, 4.5F);

		baseModel[35].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // DataCoilPart
		baseModel[35].setRotationPoint(4F, -14F, 1F);

		baseModel[36].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // DataCoilPart
		baseModel[36].setRotationPoint(4F, -14F, 14F);

		baseModel[37].addBox(0F, 0F, 0F, 8, 11, 1, 0F); // DataCoilPlate
		baseModel[37].setRotationPoint(2F, -15F, 15F);

		baseModel[38].addFlexTrapezoid(0F, 0F, 0F, 8, 1, 1, 0F, -1.00F, 0.00F, 0.00F, 0F, 0F, 0F, ModelRendererTurbo.MR_TOP); // DataCoilPlate
		baseModel[38].setRotationPoint(2F, -16F, 15F);

		baseModel[39].addBox(0F, 0F, 0F, 6, 6, 1, 0F); // DataCoilCablePlate
		baseModel[39].setRotationPoint(3F, -15F, 16F);

		baseModel[40].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // InputtDataCoilCable
		baseModel[40].setRotationPoint(4F, -14F, 17F);

		baseModel[41].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F, 0F, 0F); // InputtDataCoilCable
		baseModel[41].setRotationPoint(4F, -14F, 18F);

		baseModel[42].addShapeBox(0F, 0F, 0F, 1, 4, 4, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // InputtDataCoilCable
		baseModel[42].setRotationPoint(8F, -13F, 18F);

		baseModel[43].addBox(0F, 0F, 0F, 2, 8, 8, 0F); // EnergyInput
		baseModel[43].setRotationPoint(0F, -12F, 4F);

		baseModel[44].addBox(0F, 0F, 0F, 6, 12, 1, 0F); // RedstoneCoilHolder
		baseModel[44].setRotationPoint(15.5F, -16F, 30F);

		baseModel[45].addFlexTrapezoid(0F, 0F, 0F, 8, 1, 1, 0F, -1.00F, -1.00F, -0.50F, 0.00F, 0.00F, 0.00F, ModelRendererTurbo.MR_TOP); // PunchtapeInput
		baseModel[45].setRotationPoint(14F, -14F, 1F);

		baseModel[46].addFlexTrapezoid(0F, 0F, 0F, 8, 1, 1, 0F, -1.00F, -1.00F, -0.50F, 0.00F, 0.00F, 0.00F, ModelRendererTurbo.MR_TOP); // PunchtapeOutput
		baseModel[46].setRotationPoint(14F, -8F, 1F);

		baseModel[47].addBox(0F, 0F, 0F, 8, 1, 1, 0F); // PunchtapeInputBottom
		baseModel[47].setRotationPoint(14F, -12.5F, 1F);

		baseModel[48].addBox(0F, 0F, 0F, 8, 1, 1, 0F); // PunchtapeOutputBottom
		baseModel[48].setRotationPoint(14F, -6.5F, 1F);

		baseModel[49].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // PunchtapeOutputBottomCorner
		baseModel[49].setRotationPoint(14F, -7F, 1.1F);

		baseModel[50].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // PunchtapeOutputBottomCorner
		baseModel[50].setRotationPoint(21F, -7F, 1.1F);

		baseModel[51].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // PunchtapeInputBottomCorner
		baseModel[51].setRotationPoint(14F, -13F, 1.1F);

		baseModel[52].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // PunchtapeInputBottomCorner
		baseModel[52].setRotationPoint(21F, -13F, 1.1F);


		translateAll(0F, 0F, 0F);


		flipAll();
	}
}
