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
public class ModelChemicalBath extends BaseBlockModel
{
	public ModelRendererTurbo[] slider, slider_lowering, itemPickerLeftTop, itemPickerLeftBottom, itemPickerRightTop, itemPickerRightBottom, itemDoor;

	int textureX = 256;
	int textureY = 256;

	public ModelChemicalBath() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[38];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BasePlate
		baseModel[1] = new ModelRendererTurbo(this, 0, 20, textureX, textureY); // BasePlateMid
		baseModel[2] = new ModelRendererTurbo(this, 0, 20, textureX, textureY); // BasePlateMid
		baseModel[3] = new ModelRendererTurbo(this, 0, 20, textureX, textureY); // BasePlateMid
		baseModel[4] = new ModelRendererTurbo(this, 0, 40, textureX, textureY); // BasePlate
		baseModel[5] = new ModelRendererTurbo(this, 128, 0, textureX, textureY); // EnergyBox
		baseModel[6] = new ModelRendererTurbo(this, 0, 60, textureX, textureY); // BathFront
		baseModel[7] = new ModelRendererTurbo(this, 0, 60, textureX, textureY); // BathBack
		baseModel[8] = new ModelRendererTurbo(this, 146, 0, textureX, textureY); // BathSideUpper
		baseModel[9] = new ModelRendererTurbo(this, 146, 0, textureX, textureY); // BathSideUpper
		baseModel[10] = new ModelRendererTurbo(this, 146, 0, textureX, textureY); // BathSideLower
		baseModel[11] = new ModelRendererTurbo(this, 146, 0, textureX, textureY); // BathSideLower
		baseModel[12] = new ModelRendererTurbo(this, 0, 77, textureX, textureY); // ItemInput
		baseModel[13] = new ModelRendererTurbo(this, 64, 77, textureX, textureY); // ItemOutput
		baseModel[14] = new ModelRendererTurbo(this, 128, 56, textureX, textureY); // FluidInputBox
		baseModel[15] = new ModelRendererTurbo(this, 112, 100, textureX, textureY); // EnergyInput
		baseModel[16] = new ModelRendererTurbo(this, 116, 8, textureX, textureY); // CraneBox
		baseModel[17] = new ModelRendererTurbo(this, 104, 8, textureX, textureY); // CraneBox
		baseModel[18] = new ModelRendererTurbo(this, 116, 8, textureX, textureY); // CraneBox
		baseModel[19] = new ModelRendererTurbo(this, 104, 8, textureX, textureY); // CraneBox
		baseModel[20] = new ModelRendererTurbo(this, 0, 109, textureX, textureY); // CraneRamp
		baseModel[21] = new ModelRendererTurbo(this, 0, 109, textureX, textureY); // CraneRamp
		baseModel[22] = new ModelRendererTurbo(this, 80, 40, textureX, textureY); // FluidOutlet
		baseModel[23] = new ModelRendererTurbo(this, 76, 118, textureX, textureY); // ItemInputSlide
		baseModel[24] = new ModelRendererTurbo(this, 96, 73, textureX, textureY); // ItemInputSlideSide
		baseModel[25] = new ModelRendererTurbo(this, 76, 118, textureX, textureY); // ItemOutputSlide

		baseModel[26] = new ModelRendererTurbo(this, 96, 73, textureX, textureY); // ItemOutputSlide
		baseModel[27] = new ModelRendererTurbo(this, 0, 77, textureX, textureY); // SideRods
		baseModel[28] = new ModelRendererTurbo(this, 0, 77, textureX, textureY); // SideRods
		baseModel[29] = new ModelRendererTurbo(this, 0, 77, textureX, textureY); // SideRods
		baseModel[30] = new ModelRendererTurbo(this, 0, 77, textureX, textureY); // SideRods
		baseModel[31] = new ModelRendererTurbo(this, 0, 77, textureX, textureY); // SideRodsRight
		baseModel[32] = new ModelRendererTurbo(this, 0, 77, textureX, textureY); // SideRodsRight
		baseModel[33] = new ModelRendererTurbo(this, 0, 77, textureX, textureY); // SideRodsRight
		baseModel[34] = new ModelRendererTurbo(this, 0, 77, textureX, textureY); // SideRodsRight
		baseModel[35] = new ModelRendererTurbo(this, 0, 109, textureX, textureY); // ItemInputDoorKnob
		baseModel[36] = new ModelRendererTurbo(this, 0, 109, textureX, textureY); // ItemInputDoorKnob
		baseModel[37] = new ModelRendererTurbo(this, 0, 183, textureX, textureY); // BathBottom

		baseModel[0].addBox(0F, 0F, 0F, 32, 4, 16, 0F); // BasePlate
		baseModel[0].setRotationPoint(0F, -4F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 48, 4, 16, 0F); // BasePlateMid
		baseModel[1].setRotationPoint(0F, -4F, 16F);

		baseModel[2].addBox(0F, 0F, 0F, 48, 4, 16, 0F); // BasePlateMid
		baseModel[2].setRotationPoint(0F, -4F, 32F);

		baseModel[3].addBox(0F, 0F, 0F, 48, 4, 16, 0F); // BasePlateMid
		baseModel[3].setRotationPoint(0F, -4F, 48F);

		baseModel[4].addBox(0F, 0F, 0F, 32, 4, 16, 0F); // BasePlate
		baseModel[4].setRotationPoint(0F, -4F, 64F);

		baseModel[5].addBox(0F, 0F, 0F, 16, 26, 16, 0F); // EnergyBox
		baseModel[5].setRotationPoint(0F, -30F, 0F);

		baseModel[6].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 16, 0, 16), new Coord2D(48, 16, 48, 16), new Coord2D(48, 8, 48, 8), new Coord2D(42, 0, 42, 0), new Coord2D(6, 0, 6, 0), new Coord2D(0, 8, 0, 8)}), 1, 48, 16, 120, 1, ModelRendererTurbo.MR_FRONT, new float[]{8, 10, 36, 10, 8, 48}); // BathFront
		baseModel[6].setRotationPoint(48F, -4F, 17F);
		//baseModel[6].flip=true;

		baseModel[7].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 16, 0, 16), new Coord2D(48, 16, 48, 16), new Coord2D(48, 8, 48, 8), new Coord2D(42, 0, 42, 0), new Coord2D(6, 0, 6, 0), new Coord2D(0, 8, 0, 8)}), 1, 48, 16, 120, 1, ModelRendererTurbo.MR_FRONT, new float[]{8, 10, 36, 10, 8, 48}); // BathBack
		baseModel[7].setRotationPoint(48F, -4F, 64F);
		//baseModel[7].flip=true;

		baseModel[8].addBox(0F, 0F, 0F, 1, 8, 46, 0F); // BathSideUpper
		baseModel[8].setRotationPoint(0F, -20F, 17F);

		baseModel[9].addBox(0F, 0F, 0F, 1, 8, 46, 0F); // BathSideUpper
		baseModel[9].setRotationPoint(47F, -20F, 17F);

		baseModel[10].addBox(0F, 0F, 0F, 1, 10, 46, 0F); // BathSideLower
		baseModel[10].setRotationPoint(0F, -12F, 17F);
		baseModel[10].rotateAngleZ = 0.62831853F;

		baseModel[11].addBox(0F, 0.5F, 0F, 1, 10, 46, 0F); // BathSideLower
		baseModel[11].setRotationPoint(47.5F, -13F, 17F);
		baseModel[11].rotateAngleZ = -0.62831853F;

		baseModel[12].addBox(0F, 0F, 0F, 16, 16, 16, 0F); // ItemInput
		baseModel[12].setRotationPoint(32F, -16F, 0F);

		baseModel[13].addBox(0F, 0F, 0F, 16, 16, 16, 0F); // ItemOutput
		baseModel[13].setRotationPoint(32F, -16F, 64F);

		baseModel[14].addBox(0F, 0F, 0F, 16, 28, 16, 0F); // FluidInputBox
		baseModel[14].setRotationPoint(0F, -32F, 64F);

		baseModel[15].addFlexBox(0F, 0F, 0F, 16, 2, 16, 0F, -1.00F, -1.00F, -1.00F, -1.00F, ModelRendererTurbo.MR_TOP); // EnergyInput
		baseModel[15].setRotationPoint(0F, -32F, 0F);

		baseModel[16].addBox(0F, 0F, 0F, 10, 4, 4, 0F); // CraneBox
		baseModel[16].setRotationPoint(16F, -30F, 0F);

		baseModel[17].addShapeBox(0F, 0F, 0F, 2, 4, 4, 0F, 0F, 0F, 0F, -0.5F, -0.375F, -0.375F, -0.5F, -0.375F, -0.375F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.375F, -0.375F, -0.5F, -0.375F, -0.375F, 0F, 0F, 0F); // CraneBox
		baseModel[17].setRotationPoint(26F, -30F, 0F);

		baseModel[18].addBox(0F, 0F, 0F, 10, 4, 4, 0F); // CraneBox
		baseModel[18].setRotationPoint(16F, -30F, 76F);

		baseModel[19].addShapeBox(0F, 0F, 0F, 2, 4, 4, 0F, 0F, 0F, 0F, -0.5F, -0.375F, -0.375F, -0.5F, -0.375F, -0.375F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, -0.375F, -0.375F, -0.5F, -0.375F, -0.375F, 0F, 0F, 0F); // CraneBox
		baseModel[19].setRotationPoint(26F, -30F, 76F);

		baseModel[20].addBox(0F, 0F, 0F, 2, 2, 72, 0F); // CraneRamp
		baseModel[20].setRotationPoint(17F, -29F, 4F);

		baseModel[21].addBox(0F, 0F, 0F, 2, 2, 72, 0F); // CraneRamp
		baseModel[21].setRotationPoint(24F, -29F, 4F);

		baseModel[22].addBox(0F, 0F, 0F, 9, 8, 1, 0F); // FluidOutlet
		baseModel[22].setRotationPoint(4.5F, -16F, 62F);

		baseModel[23].addBox(0F, 0F, 0F, 13, 1, 15, 0F); // ItemInputSlide
		baseModel[23].setRotationPoint(20F, -5F, 1F);
		baseModel[23].rotateAngleZ = 0.21816616F;

		baseModel[24].addBox(0F, 0F, 0F, 13, 2, 1, 0F); // ItemInputSlideSide
		baseModel[24].setRotationPoint(19.25F, -6F, 0.5F);
		baseModel[24].rotateAngleZ = 0.21816616F;

		baseModel[25].addBox(0F, 0F, 0F, 13, 1, 15, 0F); // ItemOutputSlide
		baseModel[25].setRotationPoint(20F, -10F, 64F);
		baseModel[25].rotateAngleZ = -0.21816616F;

		baseModel[26].addBox(0F, 0F, 0F, 13, 2, 1, 0F); // ItemOutputSlide
		baseModel[26].setRotationPoint(19.75F, -11F, 78.5F);
		baseModel[26].rotateAngleZ = -0.21816616F;

		baseModel[27].addBox(0F, -5F, 0F, 1, 5, 1, 0F); // SideRods
		baseModel[27].setRotationPoint(46F, -3.5F, 19F);
		baseModel[27].rotateAngleZ = 0.34906585F;

		baseModel[28].addBox(0F, -5F, 0F, 1, 5, 1, 0F); // SideRods
		baseModel[28].setRotationPoint(46F, -3.5F, 28F);
		baseModel[28].rotateAngleZ = 0.34906585F;

		baseModel[29].addBox(0F, -5F, 0F, 1, 5, 1, 0F); // SideRods
		baseModel[29].setRotationPoint(46F, -3.5F, 51F);
		baseModel[29].rotateAngleZ = 0.34906585F;

		baseModel[30].addBox(0F, -5F, 0F, 1, 5, 1, 0F); // SideRods
		baseModel[30].setRotationPoint(46F, -3.5F, 60F);
		baseModel[30].rotateAngleZ = 0.34906585F;

		baseModel[31].addBox(0F, -5F, 0F, 1, 5, 1, 0F); // SideRodsRight
		baseModel[31].setRotationPoint(2F, -3.5F, 19F);
		baseModel[31].rotateAngleY = 3.14159265F;
		baseModel[31].rotateAngleZ = 0.34906585F;

		baseModel[32].addBox(0F, -5F, 0F, 1, 5, 1, 0F); // SideRodsRight
		baseModel[32].setRotationPoint(2F, -3.5F, 28F);
		baseModel[32].rotateAngleY = 3.14159265F;
		baseModel[32].rotateAngleZ = 0.34906585F;

		baseModel[33].addBox(0F, -5F, 0F, 1, 5, 1, 0F); // SideRodsRight
		baseModel[33].setRotationPoint(2F, -3.5F, 51F);
		baseModel[33].rotateAngleY = 3.14159265F;
		baseModel[33].rotateAngleZ = 0.34906585F;

		baseModel[34].addBox(0F, -5F, 0F, 1, 5, 1, 0F); // SideRodsRight
		baseModel[34].setRotationPoint(2F, -3.5F, 60F);
		baseModel[34].rotateAngleY = 3.14159265F;
		baseModel[34].rotateAngleZ = 0.34906585F;

		baseModel[35].addBox(-1F, 0F, -6F, 1, 1, 2, 0F); // ItemInputDoorKnob
		baseModel[35].setRotationPoint(32F, -16F, 8F);

		baseModel[36].addBox(-1F, 0F, 4F, 1, 1, 2, 0F); // ItemInputDoorKnob
		baseModel[36].setRotationPoint(32F, -16F, 8F);

		baseModel[37].addBox(0F, 0F, 0F, 36, 1, 46, 0F); // BathBottom
		baseModel[37].setRotationPoint(6F, -5F, 17F);


		slider = new ModelRendererTurbo[3];
		slider_lowering = new ModelRendererTurbo[1];
		itemPickerLeftTop = new ModelRendererTurbo[1];
		itemPickerRightTop = new ModelRendererTurbo[1];
		itemPickerLeftBottom = new ModelRendererTurbo[1];
		itemPickerRightBottom = new ModelRendererTurbo[1];

		slider[0] = new ModelRendererTurbo(this, 100, 40, textureX, textureY); // CraneMainBox
		slider[1] = new ModelRendererTurbo(this, 118, 43, textureX, textureY); // CraneMainBoxUpper
		slider[2] = new ModelRendererTurbo(this, 80, 49, textureX, textureY); // CraneMainBox

		slider[0].addBox(-2.5F, 0F, -2F, 5, 3, 4, 0F); // CraneMainBox
		slider[0].setRotationPoint(21.5F, -29F, 6F);

		slider[1].addBox(-3.5F, 0F, -2F, 7, 1, 4, 0F); // CraneMainBoxUpper
		slider[1].setRotationPoint(21.5F, -30F, 6F);

		slider[2].addShapeBox(-2F, 0F, -1.5F, 4, 2, 3, 0F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // CraneMainBox
		slider[2].setRotationPoint(21.5F, -32F, 6F);

		slider_lowering[0] = new ModelRendererTurbo(this, 100, 40, textureX, textureY); // CraneGrabberBox

		slider_lowering[0].addBox(-2.5F, 0F, -2F, 5, 1, 4, 0F); // CraneGrabberBox
		slider_lowering[0].setRotationPoint(21.5F, -25F, 6F);


		//baseModel[32] = new ModelRendererTurbo(this, 91, 49, textureX, textureY); // CraneRope

		itemPickerLeftTop[0] = new ModelRendererTurbo(this, 122, 48, textureX, textureY); // CraneGrabberLeftTop
		itemPickerRightTop[0] = new ModelRendererTurbo(this, 122, 48, textureX, textureY); // CraneGrabberRightTop
		itemPickerLeftBottom[0] = new ModelRendererTurbo(this, 126, 48, textureX, textureY); // CraneGrabberLeftTop
		itemPickerRightBottom[0] = new ModelRendererTurbo(this, 126, 48, textureX, textureY); // CraneGrabberRightTop

		itemPickerLeftTop[0].addBox(-0.5F, 0F, -0.5F, 1, 4, 1, 0F); // CraneGrabberLeftTop
		//itemPickerLeftTop[0].setRotationPoint(23.5F, -24F, 6F);

		itemPickerRightTop[0].addBox(-0.5F, 0F, -0.5F, 1, 4, 1, 0F); // CraneGrabberRightTop
		//itemPickerRightTop[0].setRotationPoint(19.5F, -24F, 6F);

		itemPickerLeftBottom[0].addBox(-0.5F, 0F, -0.5F, 1, 3, 1, 0F); // CraneGrabberLeftTop
		//itemPickerLeftBottom[0].setRotationPoint(23.5F, -20F, 6F);

		itemPickerRightBottom[0].addBox(-0.5F, 0F, -0.5F, 1, 3, 1, 0F); // CraneGrabberRightTop
		//itemPickerRightBottom[0].setRotationPoint(19.5F, -20F, 6F);


		itemDoor = new ModelRendererTurbo[1];

		itemDoor[0] = new ModelRendererTurbo(this, 0, 109, textureX, textureY); // ItemInputDoor

		itemDoor[0].addBox(-0.5F, 0.5F, -6F, 1, 7, 12, 0F); // ItemInputDoor
		itemDoor[0].setRotationPoint(31.5F, -15.5F, 8F);

		translateAll(0F, 0F, 0F);
		flipAll();
	}

	@Override
	public void flipAll()
	{
		super.flipAll();
		flip(slider);
		flip(slider_lowering);
		flip(itemPickerLeftTop);
		flip(itemPickerRightTop);
		flip(itemPickerLeftBottom);
		flip(itemPickerRightBottom);

		baseModel[6].doMirror(false, true, false);
		baseModel[7].doMirror(false, true, false);
	}

	@Override
	public void rotateAll(float x, float y, float z)
	{
		super.rotateAll(x, y, z);
		rotate(slider, x, y, z);
		rotate(slider_lowering, x, y, z);
		rotate(itemPickerLeftTop, x, y, z);
		rotate(itemPickerRightTop, x, y, z);
		rotate(itemPickerLeftBottom, x, y, z);
		rotate(itemPickerRightBottom, x, y, z);
	}

	@Override
	public void rotateAddAll(float x, float y, float z)
	{
		super.rotateAddAll(x, y, z);
		addRotation(slider, x, y, z);
		addRotation(slider_lowering, x, y, z);
		addRotation(itemPickerLeftTop, x, y, z);
		addRotation(itemPickerRightTop, x, y, z);
		addRotation(itemPickerLeftBottom, x, y, z);
		addRotation(itemPickerRightBottom, x, y, z);
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
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-3f, 0f, 4f);
			}
			break;
			case EAST:
			{
				GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(0f, 0f, 1f);
			}
			break;
			case NORTH:
			{
				GlStateManager.translate(0f, 0f, 4f);
			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(-3f, 0f, 1f);
			}
			break;
		}
	}
}
