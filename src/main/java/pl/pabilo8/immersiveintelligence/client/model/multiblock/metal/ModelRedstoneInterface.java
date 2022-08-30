package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 20-06-2019
 */
public class ModelRedstoneInterface extends ModelIIBase
{
	int textureX = 128;
	int textureY = 128;

	public ModelRedstoneInterface() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[52];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BaseBox
		baseModel[1] = new ModelRendererTurbo(this, 71, 75, textureX, textureY); // InputData
		baseModel[2] = new ModelRendererTurbo(this, 0, 36, textureX, textureY); // OutputRedstone
		baseModel[3] = new ModelRendererTurbo(this, 0, 89, textureX, textureY); // MainBox
		baseModel[4] = new ModelRendererTurbo(this, 40, 36, textureX, textureY); // OutputRedstoneBackBox
		baseModel[5] = new ModelRendererTurbo(this, 41, 77, textureX, textureY); // OutputRedstoneBackCable1
		baseModel[6] = new ModelRendererTurbo(this, 49, 78, textureX, textureY); // OutputRedstoneBackCable1
		baseModel[7] = new ModelRendererTurbo(this, 24, 50, textureX, textureY); // OutputRedstoneBackCable1
		baseModel[8] = new ModelRendererTurbo(this, 36, 44, textureX, textureY); // RedstoneCoil
		baseModel[9] = new ModelRendererTurbo(this, 74, 44, textureX, textureY); // DataCoil
		baseModel[10] = new ModelRendererTurbo(this, 0, 50, textureX, textureY); // OutputRedstoneCoilPart
		baseModel[11] = new ModelRendererTurbo(this, 0, 50, textureX, textureY); // OutputRedstoneCoilPart
		baseModel[12] = new ModelRendererTurbo(this, 64, 36, textureX, textureY); // OutputRedstoneCoilCable
		baseModel[13] = new ModelRendererTurbo(this, 2, 58, textureX, textureY); // OutputRedstoneCoilCable
		baseModel[14] = new ModelRendererTurbo(this, 54, 64, textureX, textureY); // OutputRedstoneCoilCable
		baseModel[15] = new ModelRendererTurbo(this, 48, 60, textureX, textureY); // OutputRedstoneCoilCable
		baseModel[16] = new ModelRendererTurbo(this, 66, 60, textureX, textureY); // InputtDataCoilCable
		baseModel[17] = new ModelRendererTurbo(this, 96, 44, textureX, textureY); // InputtDataCoilCable
		baseModel[18] = new ModelRendererTurbo(this, 48, 60, textureX, textureY); // InputtDataCoilCable
		baseModel[19] = new ModelRendererTurbo(this, 34, 60, textureX, textureY); // MainBoxCablePlate
		baseModel[20] = new ModelRendererTurbo(this, 70, 68, textureX, textureY); // VacuumTubeBase
		baseModel[21] = new ModelRendererTurbo(this, 89, 69, textureX, textureY); // VacuumTubeBase
		baseModel[22] = new ModelRendererTurbo(this, 17, 49, textureX, textureY); // VacuumTubeRod
		baseModel[23] = new ModelRendererTurbo(this, 9, 49, textureX, textureY); // VacuumTubeCenter
		baseModel[24] = new ModelRendererTurbo(this, 56, 41, textureX, textureY); // VacuumTubeCenter
		baseModel[25] = new ModelRendererTurbo(this, 56, 38, textureX, textureY); // VacuumTubeCenter
		baseModel[26] = new ModelRendererTurbo(this, 56, 38, textureX, textureY); // VacuumTubeCenter
		baseModel[27] = new ModelRendererTurbo(this, 56, 38, textureX, textureY); // VacuumTubeCenter
		baseModel[28] = new ModelRendererTurbo(this, 56, 38, textureX, textureY); // VacuumTubeCenter
		baseModel[29] = new ModelRendererTurbo(this, 0, 74, textureX, textureY); // VacuumTubeBelljar
		baseModel[30] = new ModelRendererTurbo(this, 12, 74, textureX, textureY); // VacuumTubeBelljar
		baseModel[31] = new ModelRendererTurbo(this, 64, 44, textureX, textureY); // DataCoilPart
		baseModel[32] = new ModelRendererTurbo(this, 64, 44, textureX, textureY); // DataCoilPart
		baseModel[33] = new ModelRendererTurbo(this, 18, 36, textureX, textureY); // DataCoilPlate
		baseModel[34] = new ModelRendererTurbo(this, 18, 36, textureX, textureY); // DataCoilPlate
		baseModel[35] = new ModelRendererTurbo(this, 111, 42, textureX, textureY); // DataCoilCablePlate
		baseModel[36] = new ModelRendererTurbo(this, 64, 44, textureX, textureY); // InputtDataCoilCable
		baseModel[37] = new ModelRendererTurbo(this, 101, 70, textureX, textureY); // InputtDataCoilCable
		baseModel[38] = new ModelRendererTurbo(this, 113, 51, textureX, textureY); // InputtDataCoilCable
		baseModel[39] = new ModelRendererTurbo(this, 96, 16, textureX, textureY); // EnergyInput
		baseModel[40] = new ModelRendererTurbo(this, 25, 76, textureX, textureY); // RedstoneCoilHolder
		baseModel[41] = new ModelRendererTurbo(this, 49, 87, textureX, textureY); // PunchtapeInput
		baseModel[42] = new ModelRendererTurbo(this, 49, 87, textureX, textureY); // PunchtapeOutput
		baseModel[43] = new ModelRendererTurbo(this, 49, 85, textureX, textureY); // PunchtapeInputBottom
		baseModel[44] = new ModelRendererTurbo(this, 49, 83, textureX, textureY); // PunchtapeOutputBottom
		baseModel[45] = new ModelRendererTurbo(this, 67, 83, textureX, textureY); // PunchtapeOutputBottomCorner
		baseModel[46] = new ModelRendererTurbo(this, 67, 83, textureX, textureY); // PunchtapeOutputBottomCorner
		baseModel[47] = new ModelRendererTurbo(this, 67, 83, textureX, textureY); // PunchtapeInputBottomCorner
		baseModel[48] = new ModelRendererTurbo(this, 67, 83, textureX, textureY); // PunchtapeInputBottomCorner
		baseModel[49] = new ModelRendererTurbo(this, 74, 44, textureX, textureY); // DataCoilPart
		baseModel[50] = new ModelRendererTurbo(this, 74, 36, textureX, textureY); // DataCoilPart
		baseModel[51] = new ModelRendererTurbo(this, 41, 87, textureX, textureY); // OutputRedstoneBackCable1

		baseModel[0].addBox(0F, 0F, 0F, 32, 4, 32, 0F); // BaseBox
		baseModel[0].setRotationPoint(0F, -4F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 8, 8, 1, 0F); // InputData
		baseModel[1].setRotationPoint(4F, -12F, 0F);

		baseModel[2].addBox(0F, 0F, 0F, 8, 8, 1, 0F); // OutputRedstone
		baseModel[2].setRotationPoint(4F, -12F, 31F);

		baseModel[3].addBox(0F, 0F, 0F, 16, 12, 16, 0F); // MainBox
		baseModel[3].setRotationPoint(10F, -16F, 2F);

		baseModel[4].addBox(0F, 0F, 0F, 6, 6, 2, 0F); // OutputRedstoneBackBox
		baseModel[4].setRotationPoint(5F, -11F, 29F);

		baseModel[5].addBox(0F, 0F, 0F, 3, 5, 1, 0F); // OutputRedstoneBackCable1
		baseModel[5].setRotationPoint(6.5F, -9F, 28F);

		baseModel[6].addBox(0F, 0F, 0F, 5, 1, 3, 0F); // OutputRedstoneBackCable1
		baseModel[6].setRotationPoint(6.5F, -5F, 24.5F);

		baseModel[7].addBox(0F, 0F, 0F, 1, 8, 3, 0F); // OutputRedstoneBackCable1
		baseModel[7].setRotationPoint(11.5F, -12F, 24.5F);

		baseModel[8].addBox(0F, 0F, 0F, 10, 8, 8, 0F); // RedstoneCoil
		baseModel[8].setRotationPoint(13.5F, -15F, 22F);

		baseModel[9].addBox(0F, 0F, 0F, 6, 6, 10, 0F); // DataCoil
		baseModel[9].setRotationPoint(3F, -15F, 4F);

		baseModel[10].addBox(0F, 0F, 0F, 1, 6, 6, 0F); // OutputRedstoneCoilPart
		baseModel[10].setRotationPoint(12.5F, -14F, 23F);

		baseModel[11].addBox(0F, 0F, 0F, 1, 6, 6, 0F); // OutputRedstoneCoilPart
		baseModel[11].setRotationPoint(23.5F, -14F, 23F);

		baseModel[12].addBox(0F, 0F, 0F, 3, 4, 4, 0F); // OutputRedstoneCoilCable
		baseModel[12].setRotationPoint(24.5F, -13F, 24F);

		baseModel[13].addShapeBox(0F, 0F, 0F, 4, 4, 12, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // OutputRedstoneCoilCable
		baseModel[13].setRotationPoint(27.5F, -8F, 12F);

		baseModel[14].addShapeBox(0F, 0F, 0F, 4, 9, 4, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // OutputRedstoneCoilCable
		baseModel[14].setRotationPoint(27.5F, -13F, 24F);

		baseModel[15].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // OutputRedstoneCoilCable
		baseModel[15].setRotationPoint(26.5F, -8F, 12F);

		baseModel[16].addShapeBox(0F, 0F, 0F, 22, 4, 4, 0F, 0.5F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0.5F, 0F, 0F, 0.5F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0.5F, 0F, 0F); // InputtDataCoilCable
		baseModel[16].setRotationPoint(9.5F, -13F, 18F);

		baseModel[17].addShapeBox(0F, 0F, 0F, 4, 4, 6, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // InputtDataCoilCable
		baseModel[17].setRotationPoint(27.5F, -13F, 12F);

		baseModel[18].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // InputtDataCoilCable
		baseModel[18].setRotationPoint(26.5F, -13F, 12F);

		baseModel[19].addBox(0F, 0F, 0F, 1, 10, 6, 0F); // MainBoxCablePlate
		baseModel[19].setRotationPoint(25.5F, -14F, 11F);

		baseModel[20].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // VacuumTubeBase
		baseModel[20].setRotationPoint(26F, -5F, 3F);

		baseModel[21].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // VacuumTubeBase
		baseModel[21].setRotationPoint(27F, -5.5F, 4F);

		baseModel[22].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, -0.625F, -0.5F, -0.625F, -0.625F, -0.5F, -0.625F, -0.625F, -0.5F, -0.625F, -0.625F, -0.5F, -0.625F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // VacuumTubeRod
		baseModel[22].setRotationPoint(28F, -10F, 5F);

		baseModel[23].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, -0.125F, 0F, -0.125F, -0.125F, 0F, -0.125F, -0.125F, 0F, -0.125F, -0.125F, 0F, -0.125F, -0.125F, 0F, -0.125F, -0.125F, 0F, -0.125F, -0.125F, 0F, -0.125F, -0.125F, 0F, -0.125F); // VacuumTubeCenter
		baseModel[23].setRotationPoint(28F, -9F, 5F);

		baseModel[24].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // VacuumTubeCenter
		baseModel[24].setRotationPoint(28F, -7.5F, 5F);

		baseModel[25].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F); // VacuumTubeCenter
		baseModel[25].setRotationPoint(28F, -6.5F, 5F);

		baseModel[26].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F); // VacuumTubeCenter
		baseModel[26].setRotationPoint(29F, -6.5F, 5F);

		baseModel[27].addShapeBox(0F, 0F, 1F, 1, 1, 1, 0F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F); // VacuumTubeCenter
		baseModel[27].setRotationPoint(28F, -6.5F, 5F);

		baseModel[28].addShapeBox(0F, 0F, 1F, 1, 1, 1, 0F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F); // VacuumTubeCenter
		baseModel[28].setRotationPoint(29F, -6.5F, 5F);

		baseModel[29].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // VacuumTubeBelljar
		baseModel[29].setRotationPoint(27.5F, -10.5F, 4.5F);

		baseModel[30].addBox(0F, 0F, 0F, 3, 4, 3, 0F); // VacuumTubeBelljar
		baseModel[30].setRotationPoint(27.5F, -9.5F, 4.5F);

		baseModel[31].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // DataCoilPart
		baseModel[31].setRotationPoint(4F, -14F, 3F);

		baseModel[32].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // DataCoilPart
		baseModel[32].setRotationPoint(4F, -14F, 14F);

		baseModel[33].addBox(0F, 0F, 0F, 8, 11, 1, 0F); // DataCoilPlate
		baseModel[33].setRotationPoint(2F, -15F, 15F);

		baseModel[34].addFlexTrapezoid(0F, 0F, 0F, 8, 1, 1, 0F, -1.00F, 0.00F, 0.00F, 0F, 0F, 0F, ModelRendererTurbo.MR_TOP); // DataCoilPlate
		baseModel[34].setRotationPoint(2F, -16F, 15F);

		baseModel[35].addBox(0F, 0F, 0F, 6, 6, 1, 0F); // DataCoilCablePlate
		baseModel[35].setRotationPoint(3F, -15F, 16F);

		baseModel[36].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // InputtDataCoilCable
		baseModel[36].setRotationPoint(4F, -14F, 17F);

		baseModel[37].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F, 0F, 0F); // InputtDataCoilCable
		baseModel[37].setRotationPoint(4F, -14F, 18F);

		baseModel[38].addShapeBox(0F, 0F, 0F, 1, 4, 4, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // InputtDataCoilCable
		baseModel[38].setRotationPoint(8F, -13F, 18F);

		baseModel[39].addBox(0F, 0F, 0F, 2, 8, 8, 0F); // EnergyInput
		baseModel[39].setRotationPoint(0F, -12F, 4F);

		baseModel[40].addBox(0F, 0F, 0F, 6, 12, 1, 0F); // RedstoneCoilHolder
		baseModel[40].setRotationPoint(15.5F, -16F, 30F);

		baseModel[41].addFlexTrapezoid(0F, 0F, 0F, 8, 1, 1, 0F, -1.00F, -1.00F, -0.50F, 0.00F, 0.00F, 0.00F, ModelRendererTurbo.MR_TOP); // PunchtapeInput
		baseModel[41].setRotationPoint(14F, -14F, 1F);

		baseModel[42].addFlexTrapezoid(0F, 0F, 0F, 8, 1, 1, 0F, -1.00F, -1.00F, -0.50F, 0.00F, 0.00F, 0.00F, ModelRendererTurbo.MR_TOP); // PunchtapeOutput
		baseModel[42].setRotationPoint(14F, -8F, 1F);

		baseModel[43].addBox(0F, 0F, 0F, 8, 1, 1, 0F); // PunchtapeInputBottom
		baseModel[43].setRotationPoint(14F, -12.5F, 1F);

		baseModel[44].addBox(0F, 0F, 0F, 8, 1, 1, 0F); // PunchtapeOutputBottom
		baseModel[44].setRotationPoint(14F, -6.5F, 1F);

		baseModel[45].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // PunchtapeOutputBottomCorner
		baseModel[45].setRotationPoint(14F, -7F, 1.1F);

		baseModel[46].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // PunchtapeOutputBottomCorner
		baseModel[46].setRotationPoint(21F, -7F, 1.1F);

		baseModel[47].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // PunchtapeInputBottomCorner
		baseModel[47].setRotationPoint(14F, -13F, 1.1F);

		baseModel[48].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // PunchtapeInputBottomCorner
		baseModel[48].setRotationPoint(21F, -13F, 1.1F);

		baseModel[49].addBox(0F, 0F, 0F, 2, 6, 1, 0F); // DataCoilPart
		baseModel[49].setRotationPoint(5F, -13F, 2F);

		baseModel[50].addBox(0F, 0F, 0F, 4, 2, 1, 0F); // DataCoilPart
		baseModel[50].setRotationPoint(5F, -9F, 1F);

		baseModel[51].addShapeBox(0F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F); // OutputRedstoneBackCable1
		baseModel[51].setRotationPoint(6.5F, -5F, 27F);

		translateAll(0F, 0F, 0F);


		flipAll();
	}

	@Override
	public void getBlockRotation(EnumFacing facing, boolean mirrored)
	{
		switch(facing)
		{
			case NORTH:
			{
				GlStateManager.rotate(mirrored?90f: 270F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?-1f: 0f, -1f, 0f);
			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(mirrored?270f: 90F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?0f: -1f, -1f, mirrored?-1f: 1f);
			}
			break;
			case EAST:
			{
				if(!mirrored)
					GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?0: -1f, -1f, 0f);

			}
			break;
			case WEST:
			{
				if(mirrored)
					GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?-1f: 0f, -1f, mirrored?-1f: 1f);

			}
			break;
		}
	}
}
