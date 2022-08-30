package pl.pabilo8.immersiveintelligence.client.model.metal_device;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 16-07-2019
 */
public class ModelSmallDataBuffer extends ModelIIBase
{
	int textureX = 64;
	int textureY = 64;

	public ModelSmallDataBuffer() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[12];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 38, 30, textureX, textureY); // Box 0
		baseModel[2] = new ModelRendererTurbo(this, 24, 19, textureX, textureY); // Box 0
		baseModel[3] = new ModelRendererTurbo(this, 0, 19, textureX, textureY); // Box 0
		baseModel[4] = new ModelRendererTurbo(this, 0, 48, textureX, textureY); // Box 0
		baseModel[5] = new ModelRendererTurbo(this, 47, 43, textureX, textureY); // Box 0
		baseModel[6] = new ModelRendererTurbo(this, 47, 43, textureX, textureY); // Box 0
		baseModel[7] = new ModelRendererTurbo(this, 24, 19, textureX, textureY); // Box 0
		baseModel[8] = new ModelRendererTurbo(this, 0, 31, textureX, textureY); // Box 0
		baseModel[9] = new ModelRendererTurbo(this, 0, 19, textureX, textureY); // Box 0
		baseModel[10] = new ModelRendererTurbo(this, 24, 43, textureX, textureY); // Box 0
		baseModel[11] = new ModelRendererTurbo(this, 54, 44, textureX, textureY); // Box 0

		baseModel[0].addBox(0F, 0F, 0F, 16, 3, 16, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, -3F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 12, 12, 1, 0F); // Box 0
		baseModel[1].setRotationPoint(2F, -15F, 15F);

		baseModel[2].addBox(0F, 0F, 0F, 8, 8, 1, 0F); // Box 0
		baseModel[2].setRotationPoint(4F, -13F, 14F);

		baseModel[3].addBox(0F, 0F, 0F, 10, 10, 2, 0F); // Box 0
		baseModel[3].setRotationPoint(3F, -14F, 3F);

		baseModel[4].addBox(0F, 0F, 0F, 10, 8, 2, 0F); // Box 0
		baseModel[4].setRotationPoint(3F, -14F, 0F);

		baseModel[5].addBox(0F, 0F, 0F, 2, 3, 1, 0F); // Box 0
		baseModel[5].setRotationPoint(5F, -6F, 0.5F);

		baseModel[6].addBox(0F, 0F, 0F, 2, 3, 1, 0F); // Box 0
		baseModel[6].setRotationPoint(9F, -6F, 0.5F);

		baseModel[7].addBox(0F, 0F, 0F, 8, 8, 1, 0F); // Box 0
		baseModel[7].setRotationPoint(4F, -13F, 2F);

		baseModel[8].addBox(0F, 0F, 0F, 1, 8, 9, 0F); // Box 0
		baseModel[8].setRotationPoint(13F, -11F, 4F);

		baseModel[9].addBox(0F, 0F, 0F, 10, 10, 2, 0F); // Box 0
		baseModel[9].setRotationPoint(3F, -14F, 12F);

		baseModel[10].addBox(0F, 0F, 0F, 8, 13, 7, 0F); // Box 0
		baseModel[10].setRotationPoint(4F, -16F, 5F);

		baseModel[11].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // Box 0
		baseModel[11].setRotationPoint(12F, -9F, 6F);


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
				GlStateManager.rotate(180F, 0F, 1F, 0F);
			}
			break;
			case SOUTH:
			{
				//GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(-1f, 0f, 1f);
			}
			break;
			case EAST:
			{
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-1f, 0f, 0f);
			}
			break;
			case WEST:
			{
				GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(0f, 0f, 1f);
			}
			break;
		}
	}
}
