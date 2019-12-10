package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.BaseBlockModel;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * Created by Pabilo8 on 20-06-2019.
 */
public class ModelAmmunitionFactory extends BaseBlockModel
{
	int textureX = 512;
	int textureY = 512;

	public ModelAmmunitionFactory() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[50];
		baseModel[0] = new ModelRendererTurbo(this, 1, 158, textureX, textureY); // Cloak
		baseModel[1] = new ModelRendererTurbo(this, 0, 97, textureX, textureY); // Base
		baseModel[2] = new ModelRendererTurbo(this, 105, 121, textureX, textureY); // Base
		baseModel[3] = new ModelRendererTurbo(this, 187, 84, textureX, textureY); // Base
		baseModel[4] = new ModelRendererTurbo(this, 0, 20, textureX, textureY); // EnergyMain
		baseModel[5] = new ModelRendererTurbo(this, 57, 3, textureX, textureY); // EnergyTop
		baseModel[6] = new ModelRendererTurbo(this, 180, 8, textureX, textureY); // EnergyTopper
		baseModel[7] = new ModelRendererTurbo(this, 124, 21, textureX, textureY); // EnergyToppest
		baseModel[8] = new ModelRendererTurbo(this, 229, 43, textureX, textureY); // Base
		baseModel[9] = new ModelRendererTurbo(this, 32, 164, textureX, textureY); // BaseTop
		baseModel[10] = new ModelRendererTurbo(this, 32, 164, textureX, textureY); // BaseTop
		baseModel[11] = new ModelRendererTurbo(this, 197, 35, textureX, textureY); // BaseTop
		baseModel[12] = new ModelRendererTurbo(this, 49, 56, textureX, textureY); // BaseTop
		baseModel[13] = new ModelRendererTurbo(this, 49, 56, textureX, textureY); // BaseTop
		baseModel[14] = new ModelRendererTurbo(this, 56, 32, textureX, textureY); // BaseTop
		baseModel[15] = new ModelRendererTurbo(this, 53, 160, textureX, textureY); // BaseTop
		baseModel[16] = new ModelRendererTurbo(this, 53, 160, textureX, textureY); // BaseTop
		baseModel[17] = new ModelRendererTurbo(this, 1, 59, textureX, textureY); // BaseTop
		baseModel[18] = new ModelRendererTurbo(this, 50, 190, textureX, textureY); // BaseTopper
		baseModel[19] = new ModelRendererTurbo(this, 2, 82, textureX, textureY); // BaseTopper
		baseModel[20] = new ModelRendererTurbo(this, 80, 91, textureX, textureY); // Base
		baseModel[21] = new ModelRendererTurbo(this, 98, 37, textureX, textureY); // TankMain
		baseModel[22] = new ModelRendererTurbo(this, 171, 77, textureX, textureY); // TankStand
		baseModel[23] = new ModelRendererTurbo(this, 171, 77, textureX, textureY); // TankStand
		baseModel[24] = new ModelRendererTurbo(this, 171, 77, textureX, textureY); // TankStand
		baseModel[25] = new ModelRendererTurbo(this, 171, 78, textureX, textureY); // TankStand
		baseModel[26] = new ModelRendererTurbo(this, 162, 84, textureX, textureY); // TankFront
		baseModel[27] = new ModelRendererTurbo(this, 151, 74, textureX, textureY); // TankFront2
		baseModel[28] = new ModelRendererTurbo(this, 167, 36, textureX, textureY); // TankFront2
		baseModel[29] = new ModelRendererTurbo(this, 162, 84, textureX, textureY); // TankFront
		baseModel[30] = new ModelRendererTurbo(this, 151, 74, textureX, textureY); // TankFront2
		baseModel[31] = new ModelRendererTurbo(this, 167, 36, textureX, textureY); // TankFront2
		baseModel[32] = new ModelRendererTurbo(this, 151, 74, textureX, textureY); // TankFront2
		baseModel[33] = new ModelRendererTurbo(this, 150, 42, textureX, textureY); // TankInput
		baseModel[34] = new ModelRendererTurbo(this, 151, 74, textureX, textureY); // TankFront2
		baseModel[35] = new ModelRendererTurbo(this, 59, 186, textureX, textureY); // MainPipe
		baseModel[36] = new ModelRendererTurbo(this, 127, 12, textureX, textureY); // Pipe
		baseModel[37] = new ModelRendererTurbo(this, 59, 186, textureX, textureY); // MainPipe
		baseModel[38] = new ModelRendererTurbo(this, 230, 9, textureX, textureY); // CloacksBase
		baseModel[39] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Cloak
		baseModel[40] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Cloak1
		baseModel[41] = new ModelRendererTurbo(this, 127, 16, textureX, textureY); // Pipe2
		baseModel[42] = new ModelRendererTurbo(this, 154, 21, textureX, textureY); // Pipe3
		baseModel[43] = new ModelRendererTurbo(this, 154, 16, textureX, textureY); // Pipe2
		baseModel[44] = new ModelRendererTurbo(this, 50, 190, textureX, textureY); // BaseTopper
		baseModel[45] = new ModelRendererTurbo(this, 2, 82, textureX, textureY); // BaseTopper
		baseModel[46] = new ModelRendererTurbo(this, 127, 16, textureX, textureY); // Pipe2
		baseModel[47] = new ModelRendererTurbo(this, 154, 21, textureX, textureY); // Pipe3
		baseModel[48] = new ModelRendererTurbo(this, 154, 16, textureX, textureY); // Pipe2
		baseModel[49] = new ModelRendererTurbo(this, 64, 151, textureX, textureY); // MainPipe

		baseModel[0].addBox(0F, 0F, 0F, 80, 16, 80, 0F); // Cloak
		baseModel[0].setRotationPoint(0F, -16F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 16, 16, 32, 0F); // Base
		baseModel[1].setRotationPoint(64F, -32F, 0F);

		baseModel[2].addBox(0F, 0F, 0F, 80, 16, 16, 0F); // Base
		baseModel[2].setRotationPoint(0F, -32F, 64F);

		baseModel[3].addBox(0F, 0F, 0F, 16, 16, 16, 0F); // Base
		baseModel[3].setRotationPoint(0F, -32F, 48F);

		baseModel[4].addBox(0F, 0F, 0F, 16, 7, 16, 0F); // EnergyMain
		baseModel[4].setRotationPoint(64F, -39F, 64F);

		baseModel[5].addTrapezoid(0F, 0F, 0F, 16, 1, 16, 0F, -1.00F, ModelRendererTurbo.MR_TOP); // EnergyTop
		baseModel[5].setRotationPoint(64F, -40F, 64F);

		baseModel[6].addBox(0F, 0F, 0F, 12, 7, 12, 0F); // EnergyTopper
		baseModel[6].setRotationPoint(66F, -47F, 66F);

		baseModel[7].addTrapezoid(0F, 0F, 0F, 10, 1, 10, 0F, 1.00F, ModelRendererTurbo.MR_BOTTOM); // EnergyToppest
		baseModel[7].setRotationPoint(67F, -48F, 67F);

		baseModel[8].addBox(0F, 0F, 0F, 16, 16, 16, 0F); // Base
		baseModel[8].setRotationPoint(64F, -32F, 48F);

		baseModel[9].addBox(0F, 0F, 0F, 5, 14, 1, 0F); // BaseTop
		baseModel[9].setRotationPoint(52F, -31F, 48F);

		baseModel[10].addBox(0F, -2F, -1F, 5, 14, 1, 0F); // BaseTop
		baseModel[10].setRotationPoint(52F, -29F, 64F);

		baseModel[11].addBox(0F, 0F, 0F, 5, 1, 16, 0F); // BaseTop
		baseModel[11].setRotationPoint(52F, -32F, 48F);

		baseModel[12].addBox(0F, -4F, 2F, 1, 13, 16, 0F); // BaseTop
		baseModel[12].setRotationPoint(15F, -27F, 14F);

		baseModel[13].addBox(0F, -6F, 1F, 1, 13, 16, 0F); // BaseTop
		baseModel[13].setRotationPoint(0F, -25F, 15F);

		baseModel[14].addBox(0F, -4F, 2F, 16, 1, 16, 0F); // BaseTop
		baseModel[14].setRotationPoint(0F, -28F, 14F);

		baseModel[15].addBox(0F, -4F, 2F, 1, 13, 7, 0F); // BaseTop
		baseModel[15].setRotationPoint(63F, -27F, 18F);

		baseModel[16].addBox(0F, -6F, 1F, 1, 13, 7, 0F); // BaseTop
		baseModel[16].setRotationPoint(48F, -25F, 19F);

		baseModel[17].addBox(0F, -4F, 2F, 16, 1, 7, 0F); // BaseTop
		baseModel[17].setRotationPoint(48F, -28F, 18F);

		baseModel[18].addTrapezoid(0F, 0F, 0F, 1, 3, 1, 0F, 3.00F, ModelRendererTurbo.MR_TOP); // BaseTopper
		baseModel[18].setRotationPoint(55F, -31F, 23F);

		baseModel[19].addBox(0F, 0F, 0F, 7, 6, 7, 0F); // BaseTopper
		baseModel[19].setRotationPoint(52F, -38F, 20F);

		baseModel[20].addBox(0F, 0F, 0F, 16, 3, 16, 0F); // Base
		baseModel[20].setRotationPoint(16F, -19F, 48F);

		baseModel[21].addBox(0F, 0F, 0F, 10, 12, 24, 0F); // TankMain
		baseModel[21].setRotationPoint(35F, -30F, 3F);

		baseModel[22].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // TankStand
		baseModel[22].setRotationPoint(35F, -18F, 24F);

		baseModel[23].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // TankStand
		baseModel[23].setRotationPoint(43F, -18F, 24F);

		baseModel[24].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // TankStand
		baseModel[24].setRotationPoint(35F, -18F, 4F);

		baseModel[25].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // TankStand
		baseModel[25].setRotationPoint(43F, -18F, 4F);

		baseModel[26].addFlexTrapezoid(0F, 0F, 0F, 10, 12, 1, 0F, -1.00F, -1.00F, 0F, 0.00F, 0.00F, 0.00F, ModelRendererTurbo.MR_FRONT); // TankFront
		baseModel[26].setRotationPoint(35F, -30F, 2F);

		baseModel[27].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // TankFront2
		baseModel[27].setRotationPoint(34F, -30F, 2F);

		baseModel[28].addShapeBox(0F, 0F, 0F, 1, 12, 24, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // TankFront2
		baseModel[28].setRotationPoint(34F, -30F, 3F);

		baseModel[29].addFlexTrapezoid(0F, 0F, 0F, 10, 12, 1, 0F, -1.00F, -1.00F, 0F, 0.00F, 0.00F, 0.00F, ModelRendererTurbo.MR_FRONT); // TankFront
		baseModel[29].setRotationPoint(45F, -30F, 28F);
		baseModel[29].rotateAngleY = -3.14159265F;

		baseModel[30].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // TankFront2
		baseModel[30].setRotationPoint(46F, -30F, 2F);
		baseModel[30].rotateAngleY = 1.57079633F;

		baseModel[31].addShapeBox(0F, 0F, -1F, 1, 12, 24, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // TankFront2
		baseModel[31].setRotationPoint(46F, -30F, 26F);
		baseModel[31].rotateAngleY = -3.14159265F;

		baseModel[32].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, -1F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // TankFront2
		baseModel[32].setRotationPoint(46F, -30F, 28F);
		baseModel[32].rotateAngleY = 3.12413936F;

		baseModel[33].addBox(0F, 0F, 0F, 8, 2, 8, 0F); // TankInput
		baseModel[33].setRotationPoint(36F, -32F, 4F);

		baseModel[34].addShapeBox(0F, 0F, 0F, 1, 12, 1, 0F, 0F, -1F, 0F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F); // TankFront2
		baseModel[34].setRotationPoint(35F, -30F, 28F);
		baseModel[34].rotateAngleY = 3.12413936F;

		baseModel[35].addBox(0F, 0F, 11F, 1, 4, 4, 0F); // MainPipe
		baseModel[35].setRotationPoint(33F, -26F, 11F);

		baseModel[36].addBox(0F, 0F, 0F, 17, 2, 2, 0F); // Pipe
		baseModel[36].setRotationPoint(16F, -25F, 23F);

		baseModel[37].addBox(0F, 0F, 11F, 1, 4, 4, 0F); // MainPipe
		baseModel[37].setRotationPoint(16F, -26F, 11F);

		baseModel[38].addBox(0F, 0F, -2F, 16, 16, 15, 0F); // CloacksBase
		baseModel[38].setRotationPoint(16F, -32F, 3F);

		baseModel[39].addBox(19F, -30F, 0F, 6, 6, 1, 0F); // Cloak
		baseModel[39].setRotationPoint(-2F, 0F, 0F);

		baseModel[40].addBox(0F, 0F, 0F, 6, 6, 1, 0F); // Cloak1
		baseModel[40].setRotationPoint(25F, -30F, 0F);

		baseModel[41].addBox(0F, 0F, 0F, 12, 2, 2, 0F); // Pipe2
		baseModel[41].setRotationPoint(59F, -36F, 22.5F);

		baseModel[42].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Pipe3
		baseModel[42].setRotationPoint(71F, -36F, 22.5F);

		baseModel[43].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // Pipe2
		baseModel[43].setRotationPoint(70F, -33F, 21.5F);

		baseModel[44].addTrapezoid(0F, 0F, 0F, 1, 3, 1, 0F, 3.00F, ModelRendererTurbo.MR_TOP); // BaseTopper
		baseModel[44].setRotationPoint(54F, -31F, 56F);

		baseModel[45].addBox(0F, 0F, 0F, 7, 6, 7, 0F); // BaseTopper
		baseModel[45].setRotationPoint(51F, -37F, 53F);

		baseModel[46].addBox(0F, 0F, 0F, 12, 2, 2, 0F); // Pipe2
		baseModel[46].setRotationPoint(55.5F, -36F, 60F);
		baseModel[46].rotateAngleY = 1.57079633F;

		baseModel[47].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Pipe3
		baseModel[47].setRotationPoint(53.5F, -36F, 72F);

		baseModel[48].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // Pipe2
		baseModel[48].setRotationPoint(52.5F, -33F, 71F);

		baseModel[49].addBox(0F, 0F, 11F, 1, 6, 6, 0F); // MainPipe
		baseModel[49].setRotationPoint(14F, -27F, 11F);

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
				GlStateManager.translate(-3f, 0f, 0f);
			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(-2f, 0f, 1f);
			}
			break;
			case EAST:
			{
				GlStateManager.translate(-3f, 0f, 1f);
			}
			break;
			case WEST:
			{
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(-2f, 0f, 0f);
			}
			break;
		}
	}
}
