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
public class ModelDataInputMachine extends ModelIIBase
{
	int textureX = 128;
	int textureY = 256;

	public ModelRendererTurbo[] lidModel;
	public ModelRendererTurbo[] drawerModel;

	public ModelDataInputMachine() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[45];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // MainBox
		baseModel[1] = new ModelRendererTurbo(this, 0, 32, textureX, textureY); // TableTop
		baseModel[2] = new ModelRendererTurbo(this, 54, 86, textureX, textureY); // TableLeg
		baseModel[3] = new ModelRendererTurbo(this, 54, 86, textureX, textureY); // TableLeg
		baseModel[4] = new ModelRendererTurbo(this, 54, 86, textureX, textureY); // TableLeg
		baseModel[5] = new ModelRendererTurbo(this, 54, 86, textureX, textureY); // TableLeg
		baseModel[6] = new ModelRendererTurbo(this, 0, 83, textureX, textureY); // TableDrawer

		baseModel[7] = new ModelRendererTurbo(this, 81, 12, textureX, textureY); // TableDrawerPullThingey

		baseModel[8] = new ModelRendererTurbo(this, 66, 92, textureX, textureY); // TableBook1
		baseModel[9] = new ModelRendererTurbo(this, 80, 38, textureX, textureY); // TableBook2
		baseModel[10] = new ModelRendererTurbo(this, 0, 51, textureX, textureY); // CentralBox
		baseModel[11] = new ModelRendererTurbo(this, 96, 48, textureX, textureY); // TableKeyboard
		baseModel[12] = new ModelRendererTurbo(this, 104, 54, textureX, textureY); // TableKeyboardKeys1
		baseModel[13] = new ModelRendererTurbo(this, 80, 54, textureX, textureY); // TableKeyboardKeys2
		baseModel[14] = new ModelRendererTurbo(this, 104, 52, textureX, textureY); // TableKeyboardKeys3
		baseModel[15] = new ModelRendererTurbo(this, 80, 52, textureX, textureY); // TableKeyboardKeys4
		baseModel[16] = new ModelRendererTurbo(this, 105, 14, textureX, textureY); // PunchtapeInput
		baseModel[17] = new ModelRendererTurbo(this, 105, 14, textureX, textureY); // PunchtapeOutput
		baseModel[18] = new ModelRendererTurbo(this, 87, 12, textureX, textureY); // PunchtapeInputBottom
		baseModel[19] = new ModelRendererTurbo(this, 87, 14, textureX, textureY); // PunchtapeOutputBottom
		baseModel[20] = new ModelRendererTurbo(this, 88, 34, textureX, textureY); // TableBookHolder
		baseModel[21] = new ModelRendererTurbo(this, 105, 12, textureX, textureY); // PunchtapeOutputBottomCorner
		baseModel[22] = new ModelRendererTurbo(this, 105, 12, textureX, textureY); // PunchtapeOutputBottomCorner
		baseModel[23] = new ModelRendererTurbo(this, 105, 12, textureX, textureY); // PunchtapeInputBottomCorner
		baseModel[24] = new ModelRendererTurbo(this, 105, 12, textureX, textureY); // PunchtapeInputBottomCorner
		baseModel[25] = new ModelRendererTurbo(this, 96, 56, textureX, textureY); // CenterBoxPaperPage
		baseModel[26] = new ModelRendererTurbo(this, 65, 102, textureX, textureY); // TopBoxWall
		baseModel[27] = new ModelRendererTurbo(this, 0, 102, textureX, textureY); // TopBoxWall
		baseModel[28] = new ModelRendererTurbo(this, 97, 70, textureX, textureY); // TopBoxWall
		baseModel[29] = new ModelRendererTurbo(this, 0, 134, textureX, textureY); // TopBoxWallTop
		baseModel[30] = new ModelRendererTurbo(this, 74, 139, textureX, textureY); // TopBoxCircuit1
		baseModel[31] = new ModelRendererTurbo(this, 48, 139, textureX, textureY); // TopBoxCircuit2
		baseModel[32] = new ModelRendererTurbo(this, 86, 91, textureX, textureY); // TopBoxCircuit3
		baseModel[33] = new ModelRendererTurbo(this, 62, 137, textureX, textureY); // TopBoxCircuitBase
		baseModel[34] = new ModelRendererTurbo(this, 62, 137, textureX, textureY); // TopBoxCircuitBase
		baseModel[35] = new ModelRendererTurbo(this, 62, 137, textureX, textureY); // TopBoxCircuitBase
		baseModel[36] = new ModelRendererTurbo(this, 50, 102, textureX, textureY); // TableBookHolder1
		baseModel[37] = new ModelRendererTurbo(this, 50, 111, textureX, textureY); // TableBookHolder2
		baseModel[38] = new ModelRendererTurbo(this, 100, 115, textureX, textureY); // TableBookLeft
		baseModel[39] = new ModelRendererTurbo(this, 100, 124, textureX, textureY); // TableBookLeft
		baseModel[40] = new ModelRendererTurbo(this, 101, 88, textureX, textureY); // TopBoxCable1
		baseModel[41] = new ModelRendererTurbo(this, 109, 87, textureX, textureY); // TopBoxCable2
		baseModel[42] = new ModelRendererTurbo(this, 64, 103, textureX, textureY); // TopBoxPanelHolder
		baseModel[43] = new ModelRendererTurbo(this, 64, 103, textureX, textureY); // TopBoxPanelHolder
		baseModel[44] = new ModelRendererTurbo(this, 0, 151, textureX, textureY); // TableKeyboardMain

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
		baseModel[6].setRotationPoint(4F, -13F, 2F);

		baseModel[7].addBox(0F, 0F, 0F, 0, 0, 0, 0F); // TableDrawerPullThingey
		baseModel[7].setRotationPoint(0F, 0F, 0F);

		//

		baseModel[8].addBox(0F, 0F, 0F, 6, 2, 8, 0F); // TableBook1
		baseModel[8].setRotationPoint(22F, -18F, 4F);
		baseModel[8].rotateAngleY = -0.31415927F;

		baseModel[9].addBox(0F, 0F, 0F, 6, 2, 8, 0F); // TableBook2
		baseModel[9].setRotationPoint(24F, -20F, 2F);
		baseModel[9].rotateAngleY = 0.20943951F;

		baseModel[10].addBox(0F, 0F, 0F, 32, 16, 16, 0F); // CentralBox
		baseModel[10].setRotationPoint(0F, -32F, 16F);

		baseModel[11].addFlexTrapezoid(0F, 0F, 0F, 11, 2, 2, 0F, -1.00F, -1.00F, 0F, 0F, 0F, 0F, ModelRendererTurbo.MR_TOP); // TableKeyboard
		baseModel[11].setRotationPoint(1.5F, -21.5F, 14F);

		baseModel[12].addBox(0F, 0F, 4.5F, 10, 1, 1, 0F); // TableKeyboardKeys1
		baseModel[12].setRotationPoint(2F, -19.5F, 1F);
		baseModel[12].rotateAngleX = 0.03490659F;

		baseModel[13].addBox(0F, 0F, 2F, 11, 1, 1, 0F); // TableKeyboardKeys2
		baseModel[13].setRotationPoint(1.5F, -18.5F, 2F);
		baseModel[13].rotateAngleX = 0.17453293F;

		baseModel[14].addBox(0F, 0F, 2F, 11, 1, 1, 0F); // TableKeyboardKeys3
		baseModel[14].setRotationPoint(1.5F, -18F, 1F);
		baseModel[14].rotateAngleX = 0.17453293F;

		baseModel[15].addBox(0F, 0F, 1F, 11, 1, 1, 0F); // TableKeyboardKeys4
		baseModel[15].setRotationPoint(1.5F, -17.5F, 1F);
		baseModel[15].rotateAngleX = 0.17453293F;

		baseModel[16].addFlexTrapezoid(0F, 0F, 0F, 8, 1, 1, 0F, -1.00F, -1.00F, -0.50F, 0.00F, 0.00F, 0.00F, ModelRendererTurbo.MR_TOP); // PunchtapeInput
		baseModel[16].setRotationPoint(14F, -26F, 15F);

		baseModel[17].addFlexTrapezoid(0F, 0F, 0F, 8, 1, 1, 0F, -1.00F, -1.00F, -0.50F, 0.00F, 0.00F, 0.00F, ModelRendererTurbo.MR_TOP); // PunchtapeOutput
		baseModel[17].setRotationPoint(14F, -20F, 15F);

		baseModel[18].addBox(0F, 0F, 0F, 8, 1, 1, 0F); // PunchtapeInputBottom
		baseModel[18].setRotationPoint(14F, -24.5F, 15F);

		baseModel[19].addBox(0F, 0F, 0F, 8, 1, 1, 0F); // PunchtapeOutputBottom
		baseModel[19].setRotationPoint(14F, -18.5F, 15F);

		baseModel[20].addFlexTrapezoid(0F, 0F, 0F, 11, 1, 3, 0F, -0.50F, -0.50F, -0.50F, -0.50F, -0.50F, -0.50F, ModelRendererTurbo.MR_TOP); // TableBookHolder
		baseModel[20].setRotationPoint(1.5F, -20.5F, 9F);

		baseModel[21].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // PunchtapeOutputBottomCorner
		baseModel[21].setRotationPoint(14F, -19F, 15.1F);

		baseModel[22].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // PunchtapeOutputBottomCorner
		baseModel[22].setRotationPoint(21F, -19F, 15.1F);

		baseModel[23].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // PunchtapeInputBottomCorner
		baseModel[23].setRotationPoint(14F, -25F, 15.1F);

		baseModel[24].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // PunchtapeInputBottomCorner
		baseModel[24].setRotationPoint(21F, -25F, 15.1F);

		baseModel[25].addBox(0F, 0F, 0F, 8, 12, 1, 0F); // CenterBoxPaperPage
		baseModel[25].setRotationPoint(23F, -30.5F, 15.5F);
		baseModel[25].rotateAngleX = -0.03490659F;

		baseModel[26].addBox(0F, 0F, 0F, 1, 16, 16, 0F); // TopBoxWall
		baseModel[26].setRotationPoint(31F, -48F, 16F);

		baseModel[27].addBox(0F, 0F, 0F, 17, 16, 16, 0F); // TopBoxWall
		baseModel[27].setRotationPoint(0F, -48F, 16F);

		baseModel[28].addBox(0F, 0F, 0F, 14, 15, 1, 0F); // TopBoxWall
		baseModel[28].setRotationPoint(17F, -47F, 31F);

		baseModel[29].addBox(0F, 0F, 0F, 14, 1, 16, 0F); // TopBoxWallTop
		baseModel[29].setRotationPoint(17F, -48F, 16F);

		baseModel[30].addBox(0F, 0F, 0F, 1, 12, 12, 0F); // TopBoxCircuit1
		baseModel[30].setRotationPoint(29F, -45F, 17F);

		baseModel[31].addBox(0F, 0F, 0F, 1, 12, 12, 0F); // TopBoxCircuit2
		baseModel[31].setRotationPoint(26F, -45F, 17F);

		baseModel[32].addBox(0F, 0F, 0F, 1, 12, 12, 0F); // TopBoxCircuit3
		baseModel[32].setRotationPoint(23F, -45F, 17F);

		baseModel[33].addShapeBox(0F, 0F, 0F, 1, 12, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 1F, 0F, 1F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 1F, 0F, 1F, 1F, 0F); // TopBoxCircuitBase
		baseModel[33].setRotationPoint(29F, -45F, 29F);

		baseModel[34].addShapeBox(0F, 0F, 0F, 1, 12, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 1F, 0F, 1F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 1F, 0F, 1F, 1F, 0F); // TopBoxCircuitBase
		baseModel[34].setRotationPoint(26F, -45F, 29F);

		baseModel[35].addShapeBox(0F, 0F, 0F, 1, 12, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 1F, 0F, 1F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 1F, 0F, 1F, 1F, 0F); // TopBoxCircuitBase
		baseModel[35].setRotationPoint(23F, -45F, 29F);

		baseModel[36].addBox(0F, 0F, 0F, 1, 1, 8, 0F); // TableBookHolder1
		baseModel[36].setRotationPoint(6.5F, -20.5F, 8F);
		baseModel[36].rotateAngleX = 0.48869219F;

		baseModel[37].addBox(0F, 0F, 0F, 5, 1, 1, 0F); // TableBookHolder2
		baseModel[37].setRotationPoint(4.5F, -20.5F, 7F);
		baseModel[37].rotateAngleX = 0.48869219F;

		baseModel[38].addBox(0F, 0F, 1F, 6, 1, 8, 0F); // TableBookLeft
		baseModel[38].setRotationPoint(7F, -21F, 7F);
		baseModel[38].rotateAngleX = 0.48869219F;
		baseModel[38].rotateAngleZ = 0.05235988F;

		baseModel[39].addBox(-6F, 0F, 1F, 6, 1, 8, 0F); // TableBookLeft
		baseModel[39].setRotationPoint(7F, -21F, 7F);
		baseModel[39].rotateAngleX = 0.48869219F;
		baseModel[39].rotateAngleZ = -0.08726646F;

		baseModel[40].addBox(0F, 0F, 0F, 6, 2, 1, 0F); // TopBoxCable1
		baseModel[40].setRotationPoint(17F, -45F, 30F);

		baseModel[41].addBox(0F, 0F, 0F, 1, 2, 8, 0F); // TopBoxCable2
		baseModel[41].setRotationPoint(17F, -45F, 22F);

		baseModel[42].addBox(-0.5F, 0F, 0F, 1, 2, 1, 0F); // TopBoxPanelHolder
		baseModel[42].setRotationPoint(16F, -46F, 15.5F);

		baseModel[43].addBox(-0.5F, 0F, 0F, 1, 2, 1, 0F); // TopBoxPanelHolder
		baseModel[43].setRotationPoint(16F, -36F, 15.5F);

		baseModel[44].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(15, 0, 15, 0), new Coord2D(15, 4, 15, 4), new Coord2D(6, 4, 6, 4)}), 12, 15, 4, 36, 12, ModelRendererTurbo.MR_FRONT, new float[]{8, 9, 4, 15}); // TableKeyboardMain
		baseModel[44].setRotationPoint(13F, -16F, 1F);
		baseModel[44].rotateAngleY = -1.57079633F;


		lidModel = new ModelRendererTurbo[1];
		lidModel[0] = new ModelRendererTurbo(this, 84, 147, textureX, textureY); // TopBoxPanel

		lidModel[0].addBox(-0.5F, 0F, 0F, 1, 16, 16, 0F); // TopBoxPanel
		lidModel[0].setRotationPoint(0f, -48F, 0f);
		lidModel[0].rotateAngleY = -1.57079633F;

		drawerModel = new ModelRendererTurbo[5];
		drawerModel[0] = new ModelRendererTurbo(this, 0, 167, textureX, textureY); // TableDrawerSliding1
		drawerModel[1] = new ModelRendererTurbo(this, 0, 172, textureX, textureY); // TableDrawerSliding2
		drawerModel[2] = new ModelRendererTurbo(this, 30, 170, textureX, textureY); // TableDrawerSliding3a
		drawerModel[3] = new ModelRendererTurbo(this, 30, 170, textureX, textureY); // TableDrawerSliding3a
		drawerModel[4] = new ModelRendererTurbo(this, 81, 12, textureX, textureY); // TableDrawerPullThingey

		drawerModel[0].addBox(0F, 0F, 0F, 12, 4, 1, 0F); // TableDrawerSliding1
		drawerModel[0].setRotationPoint(4F, -13F, 1F);

		drawerModel[1].addBox(0F, 0F, 0F, 11, 1, 8, 0F); // TableDrawerSliding2
		drawerModel[1].setRotationPoint(4.5F, -10.5F, 2F);

		drawerModel[2].addBox(0F, 0F, 0F, 1, 2, 8, 0F); // TableDrawerSliding3a
		drawerModel[2].setRotationPoint(4.5F, -12.5F, 2F);

		drawerModel[3].addBox(0F, 0F, 0F, 1, 2, 8, 0F); // TableDrawerSliding3a
		drawerModel[3].setRotationPoint(14.5F, -12.5F, 2F);

		drawerModel[4].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // TableDrawerPullThingey
		drawerModel[4].setRotationPoint(9F, -12F, 0F);

		parts.put("base", baseModel);
		parts.put("drawer", drawerModel);
		parts.put("lid", lidModel);
		flipAll();


	}

	@Override
	public void getBlockRotation(EnumFacing facing, boolean mirrored)
	{
		switch (facing)
		{
			case NORTH: {
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(0f, -2f, 2f);
			} break;
			case SOUTH: {
				GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(-1f, -2f, -1f);
			} break;
			case EAST: {
				GlStateManager.translate(1f, -2f, 0f);
			} break;
			case WEST: {
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(-2f, -2f, 1f);
			} break;
		}
	}
}
