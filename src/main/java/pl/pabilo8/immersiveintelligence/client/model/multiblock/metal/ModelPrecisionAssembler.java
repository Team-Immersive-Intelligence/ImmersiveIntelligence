package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 20-06-2019
 */
public class ModelPrecisionAssembler extends ModelIIBase
{
	public ModelRendererTurbo[] lowerBox, drawer1Model, drawer2Model, doorLeftModel, doorRightModel, schemeModel;

	int textureX = 256;
	int textureY = 256;

	public ModelPrecisionAssembler(boolean flip) //Same as Filename
	{
		baseModel = new ModelRendererTurbo[59];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // MainBox
		baseModel[1] = new ModelRendererTurbo(this, 0, 48, textureX, textureY); // MainBox
		baseModel[2] = new ModelRendererTurbo(this, 128, 0, textureX, textureY); // MainBox
		baseModel[3] = new ModelRendererTurbo(this, 0, 174, textureX, textureY); // MainBox
		baseModel[4] = new ModelRendererTurbo(this, 0, 96, textureX, textureY); // EnergyInputBox
		baseModel[5] = new ModelRendererTurbo(this, 96, 96, textureX, textureY); // EnergyInputBoxToppest
		baseModel[6] = new ModelRendererTurbo(this, 64, 113, textureX, textureY); // EnergyInputBoxTopper
		baseModel[7] = new ModelRendererTurbo(this, 48, 96, textureX, textureY); // EnergyInputBoxToppest
		baseModel[8] = new ModelRendererTurbo(this, 160, 34, textureX, textureY); // BackWall
		baseModel[9] = new ModelRendererTurbo(this, 196, 57, textureX, textureY); // BackWallCorner
		baseModel[10] = new ModelRendererTurbo(this, 196, 57, textureX, textureY); // BackWallCorner
		baseModel[11] = new ModelRendererTurbo(this, 168, 0, textureX, textureY); // MainBoxDesk
		baseModel[12] = new ModelRendererTurbo(this, 108, 96, textureX, textureY); // BackWallCornerHorizontal
		baseModel[13] = new ModelRendererTurbo(this, 108, 96, textureX, textureY); // BackWallCornerHorizontal
		baseModel[14] = new ModelRendererTurbo(this, 160, 34, textureX, textureY); // BackWall
		baseModel[15] = new ModelRendererTurbo(this, 196, 57, textureX, textureY); // BackWallCorner
		baseModel[16] = new ModelRendererTurbo(this, 196, 57, textureX, textureY); // BackWallCorner
		baseModel[17] = new ModelRendererTurbo(this, 108, 96, textureX, textureY); // BackWallCornerHorizontal
		baseModel[18] = new ModelRendererTurbo(this, 108, 96, textureX, textureY); // BackWallCornerHorizontal
		baseModel[19] = new ModelRendererTurbo(this, 206, 34, textureX, textureY); // MainBoxDesk
		baseModel[20] = new ModelRendererTurbo(this, 0, 134, textureX, textureY); // SideViewerBase
		baseModel[21] = new ModelRendererTurbo(this, 0, 154, textureX, textureY); // SideViewerBase
		baseModel[22] = new ModelRendererTurbo(this, 104, 107, textureX, textureY); // SideViewerBase
		baseModel[23] = new ModelRendererTurbo(this, 104, 107, textureX, textureY); // SideViewerBase
		baseModel[24] = new ModelRendererTurbo(this, 52, 134, textureX, textureY); // SideViewerBase
		baseModel[25] = new ModelRendererTurbo(this, 52, 134, textureX, textureY); // SideViewerBase
		baseModel[26] = new ModelRendererTurbo(this, 64, 146, textureX, textureY); // ItemOutputBox
		baseModel[27] = new ModelRendererTurbo(this, 160, 98, textureX, textureY); // LowerBox
		baseModel[28] = new ModelRendererTurbo(this, 128, 146, textureX, textureY); // MainBox
		baseModel[29] = new ModelRendererTurbo(this, 86, 131, textureX, textureY); // SideViewerBase
		baseModel[30] = new ModelRendererTurbo(this, 188, 17, textureX, textureY); // DrawerPlate
		baseModel[31] = new ModelRendererTurbo(this, 188, 17, textureX, textureY); // DrawerPlate
		baseModel[32] = new ModelRendererTurbo(this, 128, 53, textureX, textureY); // BoxyBox
		baseModel[33] = new ModelRendererTurbo(this, 186, 146, textureX, textureY); // AnotherBoxyBox
		baseModel[34] = new ModelRendererTurbo(this, 64, 178, textureX, textureY); // BackCover
		baseModel[35] = new ModelRendererTurbo(this, 174, 179, textureX, textureY); // HatchBase
		baseModel[36] = new ModelRendererTurbo(this, 174, 179, textureX, textureY); // HatchBase
		baseModel[37] = new ModelRendererTurbo(this, 174, 182, textureX, textureY); // HatchBaseHolder
		baseModel[38] = new ModelRendererTurbo(this, 174, 182, textureX, textureY); // HatchBaseHolder
		baseModel[39] = new ModelRendererTurbo(this, 174, 182, textureX, textureY); // HatchBaseHolder
		baseModel[40] = new ModelRendererTurbo(this, 174, 182, textureX, textureY); // HatchBaseHolder
		baseModel[41] = new ModelRendererTurbo(this, 17, 202, textureX, textureY); // TopGlassPlate
		baseModel[42] = new ModelRendererTurbo(this, 116, 125, textureX, textureY); // BackCablePlate
		baseModel[43] = new ModelRendererTurbo(this, 0, 206, textureX, textureY); // BackCableBack
		baseModel[44] = new ModelRendererTurbo(this, 16, 206, textureX, textureY); // BackCableBack
		baseModel[45] = new ModelRendererTurbo(this, 32, 206, textureX, textureY); // BackCableBack
		baseModel[46] = new ModelRendererTurbo(this, 32, 206, textureX, textureY); // BackCableBack
		baseModel[47] = new ModelRendererTurbo(this, 0, 228, textureX, textureY); // BackCable1
		baseModel[48] = new ModelRendererTurbo(this, 0, 228, textureX, textureY); // BackCable1
		baseModel[49] = new ModelRendererTurbo(this, 42, 206, textureX, textureY); // BackCoverFanCover
		baseModel[50] = new ModelRendererTurbo(this, 42, 206, textureX, textureY); // BackCoverFanCover
		baseModel[51] = new ModelRendererTurbo(this, 42, 208, textureX, textureY); // BackCoverFanCoverVertical
		baseModel[52] = new ModelRendererTurbo(this, 42, 208, textureX, textureY); // BackCoverFanCoverVertical
		baseModel[53] = new ModelRendererTurbo(this, 116, 134, textureX, textureY); // BackCablePlate
		baseModel[54] = new ModelRendererTurbo(this, 46, 208, textureX, textureY); // BackCableBack
		baseModel[55] = new ModelRendererTurbo(this, 46, 208, textureX, textureY); // BackCableBack
		baseModel[56] = new ModelRendererTurbo(this, 168, 182, textureX, textureY); // ItemOutputNeighborBox
		baseModel[57] = new ModelRendererTurbo(this, 46, 208, textureX, textureY); // BackCableBack
		baseModel[58] = new ModelRendererTurbo(this, 46, 208, textureX, textureY); // BackCableBack

		baseModel[0].addBox(0F, 0F, 0F, 48, 16, 32, 0F); // MainBox
		baseModel[0].setRotationPoint(0F, -16F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 48, 16, 32, 0F); // MainBox
		baseModel[1].setRotationPoint(0F, -16F, 48F);

		baseModel[2].addBox(0F, 0F, 0F, 4, 16, 16, 0F); // MainBox
		baseModel[2].setRotationPoint(12F, -16F, 32F);

		baseModel[3].addBox(0F, 0F, 0F, 16, 16, 16, 0F); // MainBox
		baseModel[3].setRotationPoint(32F, -16F, 32F);

		baseModel[4].addBox(0F, 0F, 0F, 16, 22, 16, 0F); // EnergyInputBox
		baseModel[4].setRotationPoint(32F, -38F, 0F);

		baseModel[5].addTrapezoid(0F, 0F, 0F, 10, 1, 10, 0F, -1.00F, ModelRendererTurbo.MR_TOP); // EnergyInputBoxToppest
		baseModel[5].setRotationPoint(35F, -48F, 3F);

		baseModel[6].addBox(0F, 0F, 0F, 10, 8, 10, 0F); // EnergyInputBoxTopper
		baseModel[6].setRotationPoint(35F, -47F, 3F);

		baseModel[7].addTrapezoid(0F, 0F, 0F, 16, 1, 16, 0F, -1.00F, ModelRendererTurbo.MR_TOP); // EnergyInputBoxToppest
		baseModel[7].setRotationPoint(32F, -39F, 0F);

		baseModel[8].addBox(0F, 0F, 0F, 1, 20, 44, 0F); // BackWall
		baseModel[8].setRotationPoint(46.5F, -37F, 18F);
		baseModel[8].flip = !flip;

		baseModel[9].addBox(0F, 0F, 0F, 2, 19, 2, 0F); // BackWallCorner
		baseModel[9].setRotationPoint(46F, -37F, 16F);

		baseModel[10].addBox(0F, 0F, 0F, 2, 19, 2, 0F); // BackWallCorner
		baseModel[10].setRotationPoint(46F, -37F, 62F);

		baseModel[11].addBox(-2F, 0F, 0F, 2, 12, 16, 0F); // MainBoxDesk
		baseModel[11].setRotationPoint(12F, -16F, 32F);
		baseModel[11].rotateAngleZ = -1.30899694F;

		baseModel[12].addBox(0F, 0F, 0F, 2, 2, 48, 0F); // BackWallCornerHorizontal
		baseModel[12].setRotationPoint(46F, -39F, 16F);

		baseModel[13].addBox(0F, 0F, 0F, 2, 2, 48, 0F); // BackWallCornerHorizontal
		baseModel[13].setRotationPoint(46F, -18F, 16F);

		baseModel[14].addBox(0F, 0F, 0F, 1, 20, 44, 0F); // BackWall
		baseModel[14].setRotationPoint(12.5F, -37F, 18F);
		baseModel[14].flip = !flip;

		baseModel[15].addBox(0F, 0F, 0F, 2, 19, 2, 0F); // BackWallCorner
		baseModel[15].setRotationPoint(12F, -37F, 16F);

		baseModel[16].addBox(0F, 0F, 0F, 2, 19, 2, 0F); // BackWallCorner
		baseModel[16].setRotationPoint(12F, -37F, 62F);

		baseModel[17].addBox(0F, 0F, 0F, 2, 2, 48, 0F); // BackWallCornerHorizontal
		baseModel[17].setRotationPoint(12F, -39F, 16F);

		baseModel[18].addBox(0F, 0F, 0F, 2, 2, 48, 0F); // BackWallCornerHorizontal
		baseModel[18].setRotationPoint(12F, -18F, 16F);

		baseModel[19].addBox(-2F, 11.25F, 0F, 1, 1, 16, 0F); // MainBoxDesk
		baseModel[19].setRotationPoint(12F, -17F, 32F);
		baseModel[19].rotateAngleZ = -1.30899694F;

		baseModel[20].addShapeBox(0F, 0F, 0F, 20, 8, 12, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // SideViewerBase
		baseModel[20].setRotationPoint(12F, -24F, 4F);

		baseModel[21].addShapeBox(0F, 0F, 0F, 20, 8, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // SideViewerBase
		baseModel[21].setRotationPoint(12F, -39F, 4F);

		baseModel[22].addBox(0F, 0F, 0F, 3, 7, 11, 0F); // SideViewerBase
		baseModel[22].setRotationPoint(12F, -31F, 5F);

		baseModel[23].addBox(0F, 0F, 0F, 3, 7, 11, 0F); // SideViewerBase
		baseModel[23].setRotationPoint(29F, -31F, 5F);

		baseModel[24].addShapeBox(0F, 0F, 0F, 14, 2, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // SideViewerBase
		baseModel[24].setRotationPoint(15F, -31F, 10F);

		baseModel[25].addShapeBox(0F, 0F, 0F, 14, 2, 6, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // SideViewerBase
		baseModel[25].setRotationPoint(15F, -26F, 10F);

		baseModel[26].addBox(0F, 0F, 0F, 16, 16, 16, 0F); // ItemOutputBox
		baseModel[26].setRotationPoint(16F, -32F, 64F);

		baseModel[27].addBox(0F, 0F, 0F, 16, 4, 16, 0F); // LowerBox
		baseModel[27].setRotationPoint(16F, -4F, 32F);

		baseModel[28].addBox(0F, 0F, 0F, 13, 1, 32, 0F); // MainBox
		baseModel[28].setRotationPoint(33F, -17F, 24F);

		baseModel[29].addShapeBox(0F, 0F, 0F, 14, 3, 1, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // SideViewerBase
		baseModel[29].setRotationPoint(15F, -29F, 14F);
		baseModel[29].flip = !flip;

		baseModel[30].addBox(0F, 0F, 0F, 12, 1, 16, 0F); // DrawerPlate
		baseModel[30].setRotationPoint(0F, -8F, 32F);

		baseModel[31].addBox(0F, 0F, 0F, 12, 1, 16, 0F); // DrawerPlate
		baseModel[31].setRotationPoint(0F, -2F, 32F);

		baseModel[32].addBox(0F, 0F, 0F, 8, 8, 16, 0F); // BoxyBox
		baseModel[32].setRotationPoint(1F, -24F, 0F);
		baseModel[32].rotateAngleY = -0.13962634F;

		baseModel[33].addBox(0F, 0F, 0F, 12, 12, 12, 0F); // AnotherBoxyBox
		baseModel[33].setRotationPoint(0.5F, -28F, 17.5F);
		baseModel[33].rotateAngleY = 0.19198622F;

		baseModel[34].addBox(0F, 0F, 0F, 32, 23, 1, 0F); // BackCover
		baseModel[34].setRotationPoint(14F, -39F, 63F);

		baseModel[35].addBox(0F, 0F, 0F, 19, 1, 2, 0F); // HatchBase
		baseModel[35].setRotationPoint(14F, -17F, 30F);

		baseModel[36].addBox(0F, 0F, 0F, 19, 1, 2, 0F); // HatchBase
		baseModel[36].setRotationPoint(14F, -17F, 48F);

		baseModel[37].addBox(0F, 0F, 0F, 4, 1, 1, 0F); // HatchBaseHolder
		baseModel[37].setRotationPoint(16F, -18F, 30.5F);

		baseModel[38].addBox(0F, 0F, 0F, 4, 1, 1, 0F); // HatchBaseHolder
		baseModel[38].setRotationPoint(27F, -18F, 30.5F);

		baseModel[39].addBox(0F, 0F, 0F, 4, 1, 1, 0F); // HatchBaseHolder
		baseModel[39].setRotationPoint(16F, -18F, 48.5F);

		baseModel[40].addBox(0F, 0F, 0F, 4, 1, 1, 0F); // HatchBaseHolder
		baseModel[40].setRotationPoint(27F, -18F, 48.5F);

		baseModel[41].addBox(0F, 0F, 0F, 32, 1, 47, 0F); // TopGlassPlate
		baseModel[41].setRotationPoint(14F, -38.5F, 16F);
		baseModel[41].flip = !flip;

		baseModel[42].addBox(0F, 0F, 0F, 14, 8, 1, 0F); // BackCablePlate
		baseModel[42].setRotationPoint(32F, -37F, 62F);

		baseModel[43].addShapeBox(0F, 0F, 0F, 4, 18, 4, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BackCableBack
		baseModel[43].setRotationPoint(34F, -35F, 57F);

		baseModel[44].addShapeBox(0F, 0F, 0F, 4, 18, 4, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BackCableBack
		baseModel[44].setRotationPoint(40F, -35F, 57F);

		baseModel[45].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // BackCableBack
		baseModel[45].setRotationPoint(34F, -35F, 61F);

		baseModel[46].addShapeBox(0F, 0F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BackCableBack
		baseModel[46].setRotationPoint(40F, -35F, 61F);

		baseModel[47].addBox(0F, 0F, 0F, 5, 1, 5, 0F); // BackCable1
		baseModel[47].setRotationPoint(33.5F, -18F, 56.5F);

		baseModel[48].addBox(0F, 0F, 0F, 5, 1, 5, 0F); // BackCable1
		baseModel[48].setRotationPoint(39.5F, -18F, 56.5F);

		baseModel[49].addBox(0F, 0F, 0F, 10, 1, 1, 0F); // BackCoverFanCover
		baseModel[49].setRotationPoint(16F, -36F, 62F);

		baseModel[50].addBox(0F, 0F, 0F, 10, 1, 1, 0F); // BackCoverFanCover
		baseModel[50].setRotationPoint(16F, -27F, 62F);

		baseModel[51].addBox(0F, 0F, 0F, 1, 8, 1, 0F); // BackCoverFanCoverVertical
		baseModel[51].setRotationPoint(16F, -35F, 62F);

		baseModel[52].addBox(0F, 0F, 0F, 1, 8, 1, 0F); // BackCoverFanCoverVertical
		baseModel[52].setRotationPoint(25F, -35F, 62F);

		baseModel[53].addBox(0F, 0F, 0F, 14, 7, 1, 0F); // BackCablePlate
		baseModel[53].setRotationPoint(32F, -37F, 64F);

		baseModel[54].addShapeBox(0F, 0F, 0F, 4, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BackCableBack
		baseModel[54].setRotationPoint(34F, -36F, 65F);

		baseModel[55].addShapeBox(0F, 0F, 0F, 4, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BackCableBack
		baseModel[55].setRotationPoint(40F, -36F, 65F);

		baseModel[56].addBox(0F, 0F, 0F, 16, 14, 16, 0F); // ItemOutputNeighborBox
		baseModel[56].setRotationPoint(32F, -30F, 64F);

		baseModel[57].addBox(0F, 0F, 0F, 4, 5, 4, 0F); // BackCableBack
		baseModel[57].setRotationPoint(34F, -35F, 65F);

		baseModel[58].addBox(0F, 0F, 0F, 4, 5, 4, 0F); // BackCableBack
		baseModel[58].setRotationPoint(40F, -35F, 65F);


		lowerBox = new ModelRendererTurbo[1];
		lowerBox[0] = new ModelRendererTurbo(this, 160, 118, textureX, textureY); // LowerBox

		lowerBox[0].addBox(0F, 0F, 0F, 16, 2, 16, 0F); // LowerBox
		lowerBox[0].setRotationPoint(16F, -6F, 32F);


		drawer1Model = new ModelRendererTurbo[5];
		drawer1Model[0] = new ModelRendererTurbo(this, 204, 0, textureX, textureY); // Drawer1
		drawer1Model[1] = new ModelRendererTurbo(this, 168, 28, textureX, textureY); // Drawer1
		drawer1Model[2] = new ModelRendererTurbo(this, 168, 28, textureX, textureY); // Drawer1
		drawer1Model[3] = new ModelRendererTurbo(this, 144, 32, textureX, textureY); // Drawer1
		drawer1Model[4] = new ModelRendererTurbo(this, 162, 32, textureX, textureY); // Drawer1

		drawer1Model[0].addBox(0F, 0F, 0F, 10, 1, 16, 0F); // Drawer1
		drawer1Model[0].setRotationPoint(2F, -9F, 32F);

		drawer1Model[1].addBox(0F, 0F, 0F, 10, 4, 1, 0F); // Drawer1
		drawer1Model[1].setRotationPoint(2F, -13F, 32F);

		drawer1Model[2].addBox(0F, 0F, 0F, 10, 4, 1, 0F); // Drawer1
		drawer1Model[2].setRotationPoint(2F, -13F, 47F);

		drawer1Model[3].addBox(0F, 0F, 0F, 1, 5, 16, 0F); // Drawer1
		drawer1Model[3].setRotationPoint(1F, -13F, 32F);

		drawer1Model[4].addBox(0F, 0F, 0F, 1, 2, 2, 0F); // Drawer1
		drawer1Model[4].setRotationPoint(0F, -11.5F, 39F);


		drawer2Model = new ModelRendererTurbo[5];
		drawer2Model[0] = new ModelRendererTurbo(this, 204, 0, textureX, textureY); // Drawer2
		drawer2Model[1] = new ModelRendererTurbo(this, 168, 28, textureX, textureY); // Drawer2
		drawer2Model[2] = new ModelRendererTurbo(this, 168, 28, textureX, textureY); // Drawer2
		drawer2Model[3] = new ModelRendererTurbo(this, 144, 32, textureX, textureY); // Drawer2
		drawer2Model[4] = new ModelRendererTurbo(this, 162, 32, textureX, textureY); // Drawer2

		drawer2Model[0].addBox(0F, 0F, 0F, 10, 1, 16, 0F); // Drawer2
		drawer2Model[0].setRotationPoint(2F, -3F, 32F);

		drawer2Model[1].addBox(0F, 0F, 0F, 10, 4, 1, 0F); // Drawer2
		drawer2Model[1].setRotationPoint(2F, -7F, 32F);

		drawer2Model[2].addBox(0F, 0F, 0F, 10, 4, 1, 0F); // Drawer2
		drawer2Model[2].setRotationPoint(2F, -7F, 47F);

		drawer2Model[3].addBox(0F, 0F, 0F, 1, 5, 16, 0F); // Drawer2
		drawer2Model[3].setRotationPoint(1F, -7F, 32F);

		drawer2Model[4].addBox(0F, 0F, 0F, 1, 2, 2, 0F); // Drawer2
		drawer2Model[4].setRotationPoint(0F, -5.5F, 39F);


		doorLeftModel = new ModelRendererTurbo[1];
		doorLeftModel[0] = new ModelRendererTurbo(this, 130, 179, textureX, textureY); // LowerBoxHatchFront

		doorLeftModel[0].addBox(-9F, 0F, 1F, 18, 1, 8, 0F); // LowerBoxHatchFront

		doorRightModel = new ModelRendererTurbo[1];
		doorRightModel[0] = new ModelRendererTurbo(this, 130, 188, textureX, textureY); // LowerBoxHatchBack

		doorRightModel[0].addBox(-9F, 0F, -9F, 18, 1, 8, 0F); // LowerBoxHatchBack

		schemeModel = new ModelRendererTurbo[1];
		schemeModel[0] = new ModelRendererTurbo(this, 128, 197, textureX, textureY); // Scheme

		schemeModel[0].addShapeBox(-3F, 0F, 0F, 1, 10, 14, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F); // Scheme
		schemeModel[0].setRotationPoint(12F, -16F, 33F);
		schemeModel[0].rotateAngleZ = -1.30899694F;

		parts.put("base", baseModel);
		parts.put("lowerBox", lowerBox);
		parts.put("drawer1", drawer1Model);
		parts.put("drawer2", drawer2Model);
		parts.put("doorLeft", doorLeftModel);
		parts.put("doorRight", doorRightModel);
		parts.put("scheme", schemeModel);

		flipAll();
	}

	@Override
	public void getBlockRotation(EnumFacing facing, boolean mirrored)
	{
		switch(facing)
		{
			case WEST:
			{
				GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(1f, 0f, mirrored?-5f: 0f);
			}
			break;
			case EAST:
			{
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-2f, 0f, mirrored?0f: 5f);
			}
			break;
			case NORTH:
			{
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(-3f, 0f, mirrored?-4f: 1f);
			}
			break;
			case SOUTH:
			{
				GlStateManager.translate(2f, 0f, mirrored?-1f: 4f);
			}
			break;
		}
	}
}
