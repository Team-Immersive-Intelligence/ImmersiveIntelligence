package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.TmtUtil;

/**
 * @author Pabilo8
 * @since 20-06-2019
 */
public class ModelArithmeticLogicMachine extends ModelIIBase
{

	int textureX = 256;
	int textureY = 256;

	public ModelRendererTurbo[] doorRightModel, doorLeftModel, chip1Model, chip2Model, chip3Model, chip4Model;

	public ModelArithmeticLogicMachine() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[39];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // MainBox
		baseModel[1] = new ModelRendererTurbo(this, 0, 32, textureX, textureY); // TableTop
		baseModel[2] = new ModelRendererTurbo(this, 121, 11, textureX, textureY); // TableLeg
		baseModel[3] = new ModelRendererTurbo(this, 121, 11, textureX, textureY); // TableLeg
		baseModel[4] = new ModelRendererTurbo(this, 121, 11, textureX, textureY); // TableLeg
		baseModel[5] = new ModelRendererTurbo(this, 121, 11, textureX, textureY); // TableLeg
		baseModel[6] = new ModelRendererTurbo(this, 81, 17, textureX, textureY); // TableDrawer
		baseModel[7] = new ModelRendererTurbo(this, 48, 51, textureX, textureY); // TableBox1
		baseModel[8] = new ModelRendererTurbo(this, 0, 51, textureX, textureY); // TableBox2
		baseModel[9] = new ModelRendererTurbo(this, 48, 51, textureX, textureY); // TableBox3
		baseModel[10] = new ModelRendererTurbo(this, 0, 67, textureX, textureY); // MainBoxBack
		baseModel[11] = new ModelRendererTurbo(this, 64, 98, textureX, textureY); // BackBoxRight
		baseModel[12] = new ModelRendererTurbo(this, 0, 99, textureX, textureY); // BackBoxLeft
		baseModel[13] = new ModelRendererTurbo(this, 82, 52, textureX, textureY); // MainBoxRight
		baseModel[14] = new ModelRendererTurbo(this, 82, 52, textureX, textureY); // MainBoxLeft
		baseModel[15] = new ModelRendererTurbo(this, 99, 36, textureX, textureY); // MainBoxController
		baseModel[16] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // MainBoxTop
		baseModel[17] = new ModelRendererTurbo(this, 81, 12, textureX, textureY); // TableDrawerPullThingey
		baseModel[18] = new ModelRendererTurbo(this, 81, 7, textureX, textureY); // MainBoxControllerKnob
		baseModel[19] = new ModelRendererTurbo(this, 81, 7, textureX, textureY); // MainBoxControllerKnob
		baseModel[20] = new ModelRendererTurbo(this, 121, 15, textureX, textureY); // MainBoxElectronicHolder1
		baseModel[21] = new ModelRendererTurbo(this, 121, 15, textureX, textureY); // MainBoxElectronicHolder1
		baseModel[22] = new ModelRendererTurbo(this, 121, 15, textureX, textureY); // MainBoxElectronicHolder1
		baseModel[23] = new ModelRendererTurbo(this, 121, 15, textureX, textureY); // MainBoxElectronicHolder1
		baseModel[24] = new ModelRendererTurbo(this, 176, 82, textureX, textureY); // MainBoxControllerTop
		baseModel[25] = new ModelRendererTurbo(this, 16, 147, textureX, textureY); // TableCircuit
		baseModel[26] = new ModelRendererTurbo(this, 141, 8, textureX, textureY); // TableCircuit
		baseModel[27] = new ModelRendererTurbo(this, 50, 101, textureX, textureY); // MainBoxControllerTopCircuitHolder
		baseModel[28] = new ModelRendererTurbo(this, 50, 101, textureX, textureY); // MainBoxControllerTopCircuitHolder
		baseModel[29] = new ModelRendererTurbo(this, 176, 29, textureX, textureY); // MainBoxLeftSideNote
		baseModel[30] = new ModelRendererTurbo(this, 176, 59, textureX, textureY); // MainBoxControllerTopCircuit1
		baseModel[31] = new ModelRendererTurbo(this, 176, 59, textureX, textureY); // MainBoxControllerTopCircuit2
		baseModel[32] = new ModelRendererTurbo(this, 1, 161, textureX, textureY); // MainBoxControllerTop
		baseModel[33] = new ModelRendererTurbo(this, 67, 97, textureX, textureY); // MainBoxControllerTopCableConnector
		baseModel[34] = new ModelRendererTurbo(this, 66, 68, textureX, textureY); // MainBoxControllerTopCableConnector
		baseModel[35] = new ModelRendererTurbo(this, 80, 1, textureX, textureY); // MainBoxControllerTopCable1
		baseModel[36] = new ModelRendererTurbo(this, 80, 1, textureX, textureY); // MainBoxControllerTopCable2
		baseModel[37] = new ModelRendererTurbo(this, 73, 66, textureX, textureY); // MainBoxControllerRightCable2
		baseModel[38] = new ModelRendererTurbo(this, 73, 66, textureX, textureY); // MainBoxControllerRightCable1

		baseModel[0].addBox(0F, 0F, 0F, 32, 16, 16, 0F); // MainBox
		baseModel[0].setRotationPoint(0F, -16F, 16F);

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

		baseModel[6].addBox(0F, 0F, 0F, 12, 4, 15, 0F); // TableDrawer
		baseModel[6].setRotationPoint(4F, -13F, 1F);

		baseModel[7].addBox(0F, 0F, 0F, 14, 5, 10, 0F); // TableBox1
		baseModel[7].setRotationPoint(3F, -5F, 7F);
		baseModel[7].rotateAngleY = -0.45378561F;

		baseModel[8].addBox(0F, 0F, 0F, 14, 5, 10, 0F); // TableBox2
		baseModel[8].setRotationPoint(21F, -5F, 15F);
		baseModel[8].rotateAngleY = -1.88495559F;

		baseModel[9].addBox(0F, 0F, 0F, 14, 5, 10, 0F); // TableBox3
		baseModel[9].setRotationPoint(13F, -10F, 7F);
		baseModel[9].rotateAngleY = -0.19198622F;

		baseModel[10].addBox(0F, 0F, 0F, 32, 31, 1, 0F); // MainBoxBack
		baseModel[10].setRotationPoint(0F, -47F, 31F);

		baseModel[11].addBox(0F, 0F, 0F, 16, 48, 16, 0F); // BackBoxRight
		baseModel[11].setRotationPoint(0F, -48F, 32F);

		baseModel[12].addBox(0F, 0F, 0F, 16, 32, 16, 0F); // BackBoxLeft
		baseModel[12].setRotationPoint(16F, -32F, 32F);

		baseModel[13].addBox(0F, 0F, 0F, 1, 31, 15, 0F); // MainBoxRight
		baseModel[13].setRotationPoint(0F, -47F, 16F);

		baseModel[14].addBox(0F, 0F, 0F, 1, 31, 15, 0F); // MainBoxLeft
		baseModel[14].setRotationPoint(31F, -47F, 16F);

		baseModel[15].addBox(0F, 0F, 0F, 30, 4, 16, 0F); // MainBoxController
		baseModel[15].setRotationPoint(1F, -20F, 16F);

		baseModel[16].addBox(0F, 0F, 0F, 32, 1, 16, 0F); // MainBoxTop
		baseModel[16].setRotationPoint(0F, -48F, 16F);

		baseModel[17].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // TableDrawerPullThingey
		baseModel[17].setRotationPoint(9F, -12F, 0F);

		baseModel[18].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // MainBoxControllerKnob
		baseModel[18].setRotationPoint(2F, -19F, 15F);

		baseModel[19].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // MainBoxControllerKnob
		baseModel[19].setRotationPoint(6F, -19F, 15F);

		baseModel[20].addBox(-0.5F, 0F, -15F, 2, 2, 14, 0F); // MainBoxElectronicHolder1
		baseModel[20].setRotationPoint(2F, -22F, 31F);
		baseModel[20].rotateAngleY = 0.2443461F;

		baseModel[21].addBox(-0.5F, 0F, -15F, 2, 2, 14, 0F); // MainBoxElectronicHolder1
		baseModel[21].setRotationPoint(5F, -22F, 31F);
		baseModel[21].rotateAngleY = 0.2443461F;

		baseModel[22].addBox(-0.5F, 0F, -15F, 2, 2, 14, 0F); // MainBoxElectronicHolder1
		baseModel[22].setRotationPoint(8F, -22F, 31F);
		baseModel[22].rotateAngleY = 0.2443461F;

		baseModel[23].addBox(-0.5F, 0F, -15F, 2, 2, 14, 0F); // MainBoxElectronicHolder1
		baseModel[23].setRotationPoint(11F, -22F, 31F);
		baseModel[23].rotateAngleY = 0.2443461F;

		baseModel[24].addBox(0F, 0F, 0F, 12, 8, 12, 0F); // MainBoxControllerTop
		baseModel[24].setRotationPoint(17F, -28F, 18F);

		baseModel[25].addBox(0F, 0F, 0F, 14, 1, 10, 0F); // TableCircuit
		baseModel[25].setRotationPoint(22F, -17F, 15F);
		baseModel[25].rotateAngleY = -1.67551608F;

		baseModel[26].addBox(0F, 0F, 0F, 14, 1, 18, 0F); // TableCircuit
		baseModel[26].setRotationPoint(1F, -16.5F, 14.25F);
		baseModel[26].rotateAngleY = -1.46607657F;

		baseModel[27].addBox(0F, 0F, 0F, 3, 1, 10, 0F); // MainBoxControllerTopCircuitHolder
		baseModel[27].setRotationPoint(18F, -29F, 19F);

		baseModel[28].addBox(0F, 0F, 0F, 3, 1, 10, 0F); // MainBoxControllerTopCircuitHolder
		baseModel[28].setRotationPoint(22F, -29F, 19F);

		baseModel[29].addBox(0F, 0F, 0F, 1, 12, 10, 0F); // MainBoxLeftSideNote
		baseModel[29].setRotationPoint(30.5F, -44F, 18F);
		baseModel[29].rotateAngleX = 0.01745329F;

		baseModel[30].addBox(0F, 0F, 0F, 1, 10, 10, 0F); // MainBoxControllerTopCircuit1
		baseModel[30].setRotationPoint(19F, -39F, 19F);

		baseModel[31].addBox(0F, 0F, 0F, 1, 10, 10, 0F); // MainBoxControllerTopCircuit2
		baseModel[31].setRotationPoint(23F, -39F, 19F);

		baseModel[32].addBox(0F, 0F, 0F, 12, 5, 12, 0F); // MainBoxControllerTop
		baseModel[32].setRotationPoint(17F, -47F, 18F);

		baseModel[33].addBox(0F, 0F, 0F, 3, 1, 3, 0F); // MainBoxControllerTopCableConnector
		baseModel[33].setRotationPoint(25.5F, -29F, 27F);

		baseModel[34].addBox(0F, 0F, 0F, 2, 13, 1, 0F); // MainBoxControllerTopCableConnector
		baseModel[34].setRotationPoint(26F, -42F, 28F);

		baseModel[35].addBox(0F, 0F, 0F, 16, 1, 3, 0F); // MainBoxControllerTopCable1
		baseModel[35].setRotationPoint(1F, -47F, 28F);

		baseModel[36].addBox(0F, 0F, 0F, 16, 1, 3, 0F); // MainBoxControllerTopCable2
		baseModel[36].setRotationPoint(1F, -47F, 22F);

		baseModel[37].addBox(0F, 0F, 0F, 1, 26, 3, 0F); // MainBoxControllerRightCable2
		baseModel[37].setRotationPoint(1F, -46F, 22F);

		baseModel[38].addBox(0F, 0F, 0F, 1, 26, 3, 0F); // MainBoxControllerRightCable1
		baseModel[38].setRotationPoint(1F, -46F, 28F);


		doorLeftModel = new ModelRendererTurbo[1];
		doorLeftModel[0] = new ModelRendererTurbo(this, 172, 112, textureX, textureY); // MainBoxDoorLeft

		doorLeftModel[0].addBox(-0.5F, 0F, -15F, 1, 28, 16, 0F); // MainBoxDoorLeft
		//doorLeftModel[0].setRotationPoint(31F, -48F, 15.5F);
		doorLeftModel[0].rotateAngleY = TmtUtil.AngleToTMT(-90);

		doorRightModel = new ModelRendererTurbo[1];
		doorRightModel[0] = new ModelRendererTurbo(this, 172, 112, textureX, textureY); // MainBoxDoorRight

		doorRightModel[0].addBox(-0.5F, 0F, -15F, 1, 28, 16, 0F); // MainBoxDoorRight
		//doorRightModel[0].setRotationPoint(1F, -48F, 15.5F);
		doorRightModel[0].rotateAngleY = TmtUtil.AngleToTMT(90);


		chip1Model = new ModelRendererTurbo[1];
		chip1Model[0] = new ModelRendererTurbo(this, 0, 0, 128, 64); // MainBoxElectronic1

		chip1Model[0].addBox(0F, 0F, -15F, 1, 20, 14, 0F); // MainBoxElectronic1
		chip1Model[0].setRotationPoint(2F, -42F, 31F);
		chip1Model[0].rotateAngleY = 0.2443461F;


		chip2Model = new ModelRendererTurbo[1];
		chip2Model[0] = new ModelRendererTurbo(this, 30, 0, 128, 64); // MainBoxElectronic2

		chip2Model[0].addBox(0F, 0F, -15F, 1, 20, 14, 0F); // MainBoxElectronic2
		chip2Model[0].setRotationPoint(5F, -42F, 31F);
		chip2Model[0].rotateAngleY = 0.2443461F;


		chip3Model = new ModelRendererTurbo[1];
		chip3Model[0] = new ModelRendererTurbo(this, 60, 0, 128, 64); // MainBoxElectronic3

		chip3Model[0].addBox(0F, 0F, -15F, 1, 20, 14, 0F); // MainBoxElectronic3
		chip3Model[0].setRotationPoint(8F, -42F, 31F);
		chip3Model[0].rotateAngleY = 0.2443461F;


		chip4Model = new ModelRendererTurbo[1];
		chip4Model[0] = new ModelRendererTurbo(this, 90, 0, 128, 64); // MainBoxElectronic4

		chip4Model[0].addBox(0F, 0F, -15F, 1, 20, 14, 0F); // MainBoxElectronic4
		chip4Model[0].setRotationPoint(11F, -42F, 31F);
		chip4Model[0].rotateAngleY = 0.2443461F;

		parts.put("base", baseModel);
		parts.put("chip1", this.chip1Model);
		parts.put("chip2", this.chip2Model);
		parts.put("chip3", this.chip3Model);
		parts.put("chip4", this.chip4Model);
		parts.put("doorLeftModel", this.doorLeftModel);
		parts.put("doorRightModel", this.doorRightModel);

		flipAll();
	}

	@Override
	public void getBlockRotation(EnumFacing facing, boolean mirrored)
	{
		if(mirrored)
			GlStateManager.rotate(180, 0, 1, 0);
		switch (facing)
		{
			case NORTH: {
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?-1: 0, -2, mirrored?-3: 3);
			} break;
			case SOUTH: {
				GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?0: -1, -2, 0);
			} break;
			case EAST: {
				GlStateManager.translate(mirrored?-2f: 1f, -2, mirrored?-1: 1);
			} break;
			case WEST: {
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?1: -2, -2, mirrored?-2: 2);
			} break;
		}
	}
}
