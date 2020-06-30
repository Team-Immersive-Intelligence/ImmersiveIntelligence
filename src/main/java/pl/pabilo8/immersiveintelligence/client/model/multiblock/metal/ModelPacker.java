package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.BaseBlockModel;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * Created by Pabilo8 on 20-06-2019.
 */
public class ModelPacker extends BaseBlockModel
{
	int textureX = 256;
	int textureY = 256;

	public ModelRendererTurbo[] doorModel;

	public ModelPacker() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[38];
		baseModel[0] = new ModelRendererTurbo(this, 0, 208, textureX, textureY); // Base
		baseModel[1] = new ModelRendererTurbo(this, 0, 112, textureX, textureY); // Base 2
		baseModel[2] = new ModelRendererTurbo(this, 128, 208, textureX, textureY); // Base 4
		baseModel[3] = new ModelRendererTurbo(this, 181, 148, textureX, textureY); // Base5
		baseModel[4] = new ModelRendererTurbo(this, 0, 48, textureX, textureY); // Base
		baseModel[5] = new ModelRendererTurbo(this, 87, 0, textureX, textureY); // Base6
		baseModel[6] = new ModelRendererTurbo(this, 22, 12, textureX, textureY); // Box 8
		baseModel[7] = new ModelRendererTurbo(this, 22, 12, textureX, textureY); // Box 8
		baseModel[8] = new ModelRendererTurbo(this, 4, 43, textureX, textureY); // Box 10
		baseModel[9] = new ModelRendererTurbo(this, 4, 43, textureX, textureY); // Box 10
		baseModel[10] = new ModelRendererTurbo(this, 31, 23, textureX, textureY); // Box 12
		baseModel[11] = new ModelRendererTurbo(this, 5, 30, textureX, textureY); // Box 8
		baseModel[12] = new ModelRendererTurbo(this, 7, 68, textureX, textureY); // Box 10
		baseModel[13] = new ModelRendererTurbo(this, 127, 142, textureX, textureY); // Box 12
		baseModel[14] = new ModelRendererTurbo(this, 10, 9, textureX, textureY); // Box 15
		baseModel[15] = new ModelRendererTurbo(this, 6, 2, textureX, textureY); // Box 16
		baseModel[16] = new ModelRendererTurbo(this, 5, 30, textureX, textureY); // Box 17
		baseModel[17] = new ModelRendererTurbo(this, 5, 30, textureX, textureY); // Box 17
		baseModel[18] = new ModelRendererTurbo(this, 28, 61, textureX, textureY); // Box 10
		baseModel[19] = new ModelRendererTurbo(this, 28, 61, textureX, textureY); // Box 10
		baseModel[20] = new ModelRendererTurbo(this, 28, 4, textureX, textureY); // Box 21
		baseModel[21] = new ModelRendererTurbo(this, 28, 4, textureX, textureY); // Box 21
		baseModel[22] = new ModelRendererTurbo(this, 16, 3, textureX, textureY); // Box 21
		baseModel[23] = new ModelRendererTurbo(this, 16, 3, textureX, textureY); // Box 21
		baseModel[24] = new ModelRendererTurbo(this, 24, 32, textureX, textureY); // Box 25
		baseModel[25] = new ModelRendererTurbo(this, 21, 20, textureX, textureY); // Box 25
		baseModel[26] = new ModelRendererTurbo(this, 21, 26, textureX, textureY); // Box 27
		baseModel[27] = new ModelRendererTurbo(this, 45, 7, textureX, textureY); // Box 25
		baseModel[28] = new ModelRendererTurbo(this, 9, 22, textureX, textureY); // Box 25
		baseModel[29] = new ModelRendererTurbo(this, 9, 22, textureX, textureY); // Box 25
		baseModel[30] = new ModelRendererTurbo(this, 4, 14, textureX, textureY); // Box 31
		baseModel[31] = new ModelRendererTurbo(this, 4, 14, textureX, textureY); // Box 31
		baseModel[32] = new ModelRendererTurbo(this, 195, 114, textureX, textureY); // Box 34
		baseModel[33] = new ModelRendererTurbo(this, 195, 113, textureX, textureY); // Box 34
		baseModel[34] = new ModelRendererTurbo(this, 154, 47, textureX, textureY); // EnergyMain
		baseModel[35] = new ModelRendererTurbo(this, 190, 21, textureX, textureY); // EnergyTop
		baseModel[36] = new ModelRendererTurbo(this, 152, 5, textureX, textureY); // EnergyTopper
		baseModel[37] = new ModelRendererTurbo(this, 202, 4, textureX, textureY); // EnergyToppest

		baseModel[0].addBox(0F, 0F, 0F, 48, 16, 32, 0F); // Base
		baseModel[0].setRotationPoint(0F, -16F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 16, 16, 80, 0F); // Base 2
		baseModel[1].setRotationPoint(0F, -32F, 0F);

		baseModel[2].addBox(0F, 0F, 0F, 16, 16, 16, 0F); // Base 4
		baseModel[2].setRotationPoint(16F, -32F, 0F);

		baseModel[3].addBox(0F, 0F, 0F, 16, 1, 16, 0F); // Base5
		baseModel[3].setRotationPoint(32F, -16F, 32F);

		baseModel[4].addBox(0F, 0F, 0F, 48, 16, 48, 0F); // Base
		baseModel[4].setRotationPoint(0F, -16F, 48F);

		baseModel[5].addBox(0F, 0F, 0F, 16, 16, 16, 0F); // Base6
		baseModel[5].setRotationPoint(32F, -32F, 80F);

		baseModel[6].addBox(0F, 0F, 0F, 7, 1, 7, 0F); // Box 8
		baseModel[6].setRotationPoint(40F, -17F, 24F);

		baseModel[7].addBox(0F, 0F, 0F, 7, 1, 7, 0F); // Box 8
		baseModel[7].setRotationPoint(40F, -17F, 49F);

		baseModel[8].addBox(0F, 0F, 0F, 5, 10, 5, 0F); // Box 10
		baseModel[8].setRotationPoint(41F, -27F, 25F);

		baseModel[9].addBox(0F, 0F, 0F, 5, 10, 5, 0F); // Box 10
		baseModel[9].setRotationPoint(41F, -27F, 50F);

		baseModel[10].addBox(0F, 0F, 0F, 5, 5, 20, 0F); // Box 12
		baseModel[10].setRotationPoint(41F, -32F, 30F);

		baseModel[11].addBox(0F, 0F, 0F, 5, 1, 5, 0F); // Box 8
		baseModel[11].setRotationPoint(33F, -17F, 49F);

		baseModel[12].addBox(0F, 0F, 0F, 3, 10, 3, 0F); // Box 10
		baseModel[12].setRotationPoint(34F, -27F, 50F);

		baseModel[13].addBox(0F, 0F, 0F, 3, 3, 40, 0F); // Box 12
		baseModel[13].setRotationPoint(34F, -30F, 10F);

		baseModel[14].addBox(0F, 0F, 0F, 1, 5, 5, 0F); // Box 15
		baseModel[14].setRotationPoint(32F, -31F, 6F);

		baseModel[15].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // Box 16
		baseModel[15].setRotationPoint(33F, -30F, 7F);

		baseModel[16].addBox(0F, 0F, 0F, 5, 1, 5, 0F); // Box 17
		baseModel[16].setRotationPoint(41F, -17F, 73F);

		baseModel[17].addBox(0F, 0F, 0F, 5, 1, 5, 0F); // Box 17
		baseModel[17].setRotationPoint(34F, -17F, 73F);

		baseModel[18].addBox(0F, 0F, 0F, 3, 18, 3, 0F); // Box 10
		baseModel[18].setRotationPoint(35F, -34F, 74F);

		baseModel[19].addBox(0F, 0F, 0F, 3, 18, 3, 0F); // Box 10
		baseModel[19].setRotationPoint(42F, -34F, 74F);

		baseModel[20].addBox(0F, 0F, 0F, 5, 5, 1, 0F); // Box 21
		baseModel[20].setRotationPoint(41F, -38F, 79F);

		baseModel[21].addBox(0F, 0F, 0F, 5, 5, 1, 0F); // Box 21
		baseModel[21].setRotationPoint(34F, -38F, 79F);

		baseModel[22].addBox(0F, 0F, 0F, 3, 3, 2, 0F); // Box 21
		baseModel[22].setRotationPoint(35F, -37F, 77F);

		baseModel[23].addBox(0F, 0F, 0F, 3, 3, 2, 0F); // Box 21
		baseModel[23].setRotationPoint(42F, -37F, 77F);

		baseModel[24].addShapeBox(0F, 0F, 0F, 5, 5, 5, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 25
		baseModel[24].setRotationPoint(41F, -32F, 25F);

		baseModel[25].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 25
		baseModel[25].setRotationPoint(34F, -30F, 50F);

		baseModel[26].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 27
		baseModel[26].setRotationPoint(34F, -30F, 7F);

		baseModel[27].addShapeBox(0F, 0F, 0F, 5, 5, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 25
		baseModel[27].setRotationPoint(41F, -32F, 50F);

		baseModel[28].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 25
		baseModel[28].setRotationPoint(35F, -37F, 74F);

		baseModel[29].addShapeBox(0F, 0F, 0F, 3, 3, 3, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 25
		baseModel[29].setRotationPoint(42F, -37F, 74F);

		baseModel[30].addBox(0F, 0F, 0F, 1, 2, 1, 0F); // Box 31
		baseModel[30].setRotationPoint(16F, -18F, 45F);

		baseModel[31].addBox(0F, 0F, 0F, 1, 2, 1, 0F); // Box 31
		baseModel[31].setRotationPoint(16F, -18F, 34F);

		baseModel[32].addBox(0F, 0F, 0F, 14, 10, 10, 0F); // Box 34
		baseModel[32].setRotationPoint(34F, -26F, 16F);
		baseModel[32].rotateAngleY = -1.25663706F;

		baseModel[33].addBox(0F, 0F, 0F, 14, 10, 10, 0F); // Box 34
		baseModel[33].setRotationPoint(6F, -26F, 96F);
		baseModel[33].rotateAngleY = -1.78023584F;

		baseModel[34].addBox(0F, 0F, 0F, 16, 7, 16, 0F); // EnergyMain
		baseModel[34].setRotationPoint(32F, -39F, 80F);

		baseModel[35].addTrapezoid(0F, 0F, 0F, 16, 1, 16, 0F, -1.00F, ModelRendererTurbo.MR_TOP); // EnergyTop
		baseModel[35].setRotationPoint(32F, -40F, 80F);

		baseModel[36].addBox(0F, 0F, 0F, 12, 7, 12, 0F); // EnergyTopper
		baseModel[36].setRotationPoint(34F, -47F, 82F);

		baseModel[37].addTrapezoid(0F, 0F, 0F, 10, 1, 10, 0F, 1.00F, ModelRendererTurbo.MR_BOTTOM); // EnergyToppest
		baseModel[37].setRotationPoint(35F, -48F, 83F);

		doorModel = new ModelRendererTurbo[1];
		doorModel[0] = new ModelRendererTurbo(this, 123, 144, textureX, textureY); // Box 33

		doorModel[0].addBox(-0.5F, -15F, -8F, 1, 16, 16, 0F); // Box 33
		doorModel[0].setRotationPoint(17F, -15.5F, 40F);

		parts.put("base", baseModel);
		parts.put("door", doorModel);

		flipAll();
	}

	@Override
	public void getBlockRotation(EnumFacing facing, boolean mirrored)
	{
		switch(facing)
		{
			case NORTH:
			{
				if(mirrored)
					GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?3f: -3f, 0f, mirrored?2f: 3f);
			}
			break;
			case SOUTH:
			{
				if(!mirrored)
					GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?2: -2f, 0f, mirrored?3: 2f);
			}
			break;
			case EAST:
			{
				GlStateManager.rotate(mirrored?90: 270F, 0F, 1F, 0F);
				GlStateManager.translate(mirrored?2: -2f, 0f, mirrored?2: 3f);
			}
			break;
			case WEST:
			{
				GlStateManager.rotate(mirrored?270: 90, 0, 1, 0);
				GlStateManager.translate(mirrored?3: -3f, 0f, mirrored?3: 2f);
			}
			break;
		}
	}
}
