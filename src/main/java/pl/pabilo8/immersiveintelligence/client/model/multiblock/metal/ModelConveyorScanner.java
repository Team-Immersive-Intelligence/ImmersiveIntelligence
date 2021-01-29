package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 20-06-2019
 */
public class ModelConveyorScanner extends ModelIIBase
{
	int textureX = 64;
	int textureY = 128;

	public ModelConveyorScanner() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[25];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BoxMain
		baseModel[1] = new ModelRendererTurbo(this, 0, 47, textureX, textureY); // BoxConveyorUpper
		baseModel[2] = new ModelRendererTurbo(this, 0, 47, textureX, textureY); // BoxConveyorUpper
		baseModel[3] = new ModelRendererTurbo(this, 0, 42, textureX, textureY); // BoxConveyorUpper
		baseModel[4] = new ModelRendererTurbo(this, 0, 42, textureX, textureY); // BoxConveyorUpper
		baseModel[5] = new ModelRendererTurbo(this, 0, 64, textureX, textureY); // BoxEnergyInputTop
		baseModel[6] = new ModelRendererTurbo(this, 20, 49, textureX, textureY); // BoxEnergyInput
		baseModel[7] = new ModelRendererTurbo(this, 35, 32, textureX, textureY); // InserterArm2TopProjector
		baseModel[8] = new ModelRendererTurbo(this, 0, 32, textureX, textureY); // InserterArm2TopProjectorUpper
		baseModel[9] = new ModelRendererTurbo(this, 18, 42, textureX, textureY); // InserterArm2TopProjectorMiddle
		baseModel[10] = new ModelRendererTurbo(this, 15, 37, textureX, textureY); // InserterArm2TopProjectorMiddle
		baseModel[11] = new ModelRendererTurbo(this, 0, 36, textureX, textureY); // InserterArm2TopProjectorMiddle
		baseModel[12] = new ModelRendererTurbo(this, 34, 39, textureX, textureY); // InserterArm2TopProjectorMiddle
		baseModel[13] = new ModelRendererTurbo(this, 15, 32, textureX, textureY); // InserterArm2TopProjectorMiddle
		baseModel[14] = new ModelRendererTurbo(this, 23, 37, textureX, textureY); // InserterArm2TopProjectorMiddle
		baseModel[15] = new ModelRendererTurbo(this, 18, 48, textureX, textureY); // BoxConveyorUpper
		baseModel[16] = new ModelRendererTurbo(this, 18, 48, textureX, textureY); // BoxConveyorUpper
		baseModel[17] = new ModelRendererTurbo(this, 10, 42, textureX, textureY); // BoxConveyorUpper
		baseModel[18] = new ModelRendererTurbo(this, 10, 42, textureX, textureY); // BoxConveyorUpper
		baseModel[19] = new ModelRendererTurbo(this, 0, 75, textureX, textureY); // BoxEnergyInput
		baseModel[20] = new ModelRendererTurbo(this, 30, 63, textureX, textureY); // BoxEnergyInput
		baseModel[21] = new ModelRendererTurbo(this, 10, 47, textureX, textureY); // BoxEnergyInputRod
		baseModel[22] = new ModelRendererTurbo(this, 10, 47, textureX, textureY); // BoxEnergyInputRod
		baseModel[23] = new ModelRendererTurbo(this, 10, 47, textureX, textureY); // BoxEnergyInputRod
		baseModel[24] = new ModelRendererTurbo(this, 10, 47, textureX, textureY); // BoxEnergyInputRod

		baseModel[0].addBox(0F, 0F, 0F, 16, 16, 16, 0F); // BoxMain
		baseModel[0].setRotationPoint(0F, -16F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 1, 1, 16, 0F); // BoxConveyorUpper
		baseModel[1].setRotationPoint(0F, -20F, 0F);

		baseModel[2].addBox(0F, 0F, 0F, 1, 1, 16, 0F); // BoxConveyorUpper
		baseModel[2].setRotationPoint(15F, -20F, 0F);

		baseModel[3].addBox(0F, 0F, 0F, 1, 12, 4, 0F); // BoxConveyorUpper
		baseModel[3].setRotationPoint(0F, -32F, 6F);

		baseModel[4].addBox(0F, 0F, 0F, 1, 12, 4, 0F); // BoxConveyorUpper
		baseModel[4].setRotationPoint(15F, -32F, 6F);

		baseModel[5].addShapeBox(0F, 0F, 0F, 10, 1, 10, 0F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BoxEnergyInputTop
		baseModel[5].setRotationPoint(3F, -48F, 3F);

		baseModel[6].addBox(0F, 0F, 0F, 10, 4, 10, 0F); // BoxEnergyInput
		baseModel[6].setRotationPoint(3F, -47F, 3F);

		baseModel[7].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // InserterArm2TopProjector
		baseModel[7].setRotationPoint(5F, -38F, 5F);

		baseModel[8].addBox(1.5F, 0F, 1F, 3, 1, 3, 0F); // InserterArm2TopProjectorUpper
		baseModel[8].setRotationPoint(5F, -31F, 5F);

		baseModel[9].addShapeBox(0.5F, 0F, 0F, 5, 1, 5, 0F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.75F, 0F, -0.75F, -0.75F, 0F, -0.75F, -0.75F, 0F, -0.75F, -0.75F, 0F, -0.75F); // InserterArm2TopProjectorMiddle
		baseModel[9].setRotationPoint(5F, -37F, 5F);

		baseModel[10].addShapeBox(1.5F, 0F, 1F, 3, 1, 3, 0F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.75F, 0F, -0.75F, -0.75F, 0F, -0.75F, -0.75F, 0F, -0.75F, -0.75F, 0F, -0.75F); // InserterArm2TopProjectorMiddle
		baseModel[10].setRotationPoint(5F, -32F, 5F);

		baseModel[11].addShapeBox(0.5F, 0F, 0F, 5, 1, 5, 0F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F); // InserterArm2TopProjectorMiddle
		baseModel[11].setRotationPoint(5F, -35F, 5F);

		baseModel[12].addShapeBox(0.5F, 0F, 0F, 5, 1, 5, 0F, -0.75F, 0F, -0.75F, -0.75F, 0F, -0.75F, -0.75F, 0F, -0.75F, -0.75F, 0F, -0.75F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F); // InserterArm2TopProjectorMiddle
		baseModel[12].setRotationPoint(5F, -36F, 5F);

		baseModel[13].addShapeBox(1F, 0F, 0.5F, 4, 1, 4, 0F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F); // InserterArm2TopProjectorMiddle
		baseModel[13].setRotationPoint(5F, -33F, 5F);

		baseModel[14].addShapeBox(1F, 0F, 0.5F, 4, 1, 4, 0F, -0.75F, 0F, -0.75F, -0.75F, 0F, -0.75F, -0.75F, 0F, -0.75F, -0.75F, 0F, -0.75F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F); // InserterArm2TopProjectorMiddle
		baseModel[14].setRotationPoint(5F, -34F, 5F);

		baseModel[15].addBox(0F, 0F, 0F, 2, 6, 4, 0F); // BoxConveyorUpper
		baseModel[15].setRotationPoint(0F, -38F, 6F);

		baseModel[16].addBox(0F, 0F, 0F, 2, 6, 4, 0F); // BoxConveyorUpper
		baseModel[16].setRotationPoint(14F, -38F, 6F);

		baseModel[17].addShapeBox(0F, 0F, 0F, 1, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // BoxConveyorUpper
		baseModel[17].setRotationPoint(14F, -32F, 6F);

		baseModel[18].addShapeBox(0F, 0F, 0F, 1, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // BoxConveyorUpper
		baseModel[18].setRotationPoint(1F, -32F, 6F);

		baseModel[19].addBox(0F, 0F, 0F, 16, 2, 10, 0F); // BoxEnergyInput
		baseModel[19].setRotationPoint(0F, -40F, 3F);

		baseModel[20].addBox(0F, 0F, 0F, 8, 3, 8, 0F); // BoxEnergyInput
		baseModel[20].setRotationPoint(4F, -43F, 4F);

		baseModel[21].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // BoxEnergyInputRod
		baseModel[21].setRotationPoint(3F, -43F, 3F);

		baseModel[22].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // BoxEnergyInputRod
		baseModel[22].setRotationPoint(12F, -43F, 3F);

		baseModel[23].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // BoxEnergyInputRod
		baseModel[23].setRotationPoint(3F, -43F, 12F);

		baseModel[24].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // BoxEnergyInputRod
		baseModel[24].setRotationPoint(12F, -43F, 12F);


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
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-1f, 0f, 0f);
			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(0f, 0f, 1f);
			}
			break;
			case EAST:
			{
				GlStateManager.translate(-1f, 0f, 1f);
			}
			break;
			case WEST:
			{
				GlStateManager.rotate(180F, 0F, 1F, 0F);
			}
			break;
		}
	}
}
