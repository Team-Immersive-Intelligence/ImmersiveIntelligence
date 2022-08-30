package pl.pabilo8.immersiveintelligence.client.model.metal_device;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 16-07-2019
 */
public class ModelRedstoneBuffer extends ModelIIBase
{
	int textureX = 64;
	int textureY = 64;

	public ModelRedstoneBuffer() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[12];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 24, 45, textureX, textureY); // Box 0
		baseModel[2] = new ModelRendererTurbo(this, 4, 37, textureX, textureY); // Box 0
		baseModel[3] = new ModelRendererTurbo(this, 0, 19, textureX, textureY); // Box 0
		baseModel[4] = new ModelRendererTurbo(this, 0, 46, textureX, textureY); // Box 0
		baseModel[5] = new ModelRendererTurbo(this, 22, 37, textureX, textureY); // Box 0
		baseModel[6] = new ModelRendererTurbo(this, 22, 37, textureX, textureY); // Box 0
		baseModel[7] = new ModelRendererTurbo(this, 4, 37, textureX, textureY); // Box 0
		baseModel[8] = new ModelRendererTurbo(this, 38, 19, textureX, textureY); // Box 0
		baseModel[9] = new ModelRendererTurbo(this, 28, 29, textureX, textureY); // Box 0
		baseModel[10] = new ModelRendererTurbo(this, 0, 37, textureX, textureY); // Box 0
		baseModel[11] = new ModelRendererTurbo(this, 0, 37, textureX, textureY); // Box 0

		baseModel[0].addBox(0F, 0F, 0F, 16, 3, 16, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, -3F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 12, 12, 4, 0F); // Box 0
		baseModel[1].setRotationPoint(2F, -15F, 12F);

		baseModel[2].addBox(0F, 0F, 0F, 8, 8, 1, 0F); // Box 0
		baseModel[2].setRotationPoint(4F, -13F, 11F);

		baseModel[3].addBox(0F, 0F, 0F, 10, 10, 8, 0F); // Box 0
		baseModel[3].setRotationPoint(3F, -14F, 3F);

		baseModel[4].addBox(0F, 0F, 0F, 10, 8, 2, 0F); // Box 0
		baseModel[4].setRotationPoint(3F, -14F, 0F);

		baseModel[5].addBox(0F, 0F, 0F, 2, 3, 1, 0F); // Box 0
		baseModel[5].setRotationPoint(5F, -6F, 0.5F);

		baseModel[6].addBox(0F, 0F, 0F, 2, 3, 1, 0F); // Box 0
		baseModel[6].setRotationPoint(9F, -6F, 0.5F);

		baseModel[7].addBox(0F, 0F, 0F, 8, 8, 1, 0F); // Box 0
		baseModel[7].setRotationPoint(4F, -13F, 2F);

		baseModel[8].addBox(0F, 0F, 0F, 1, 12, 6, 0F); // Box 0
		baseModel[8].setRotationPoint(13F, -15F, 4F);

		baseModel[9].addBox(0F, 0F, 0F, 1, 8, 8, 0F); // Box 0
		baseModel[9].setRotationPoint(14F, -13.5F, 3F);

		baseModel[10].addBox(-0.75F, 0F, 0F, 1, 6, 1, 0F); // Box 0
		baseModel[10].setRotationPoint(14F, -8F, 4.5F);
		baseModel[10].rotateAngleZ = 0.26179939F;

		baseModel[11].addBox(-0.75F, 0F, 0F, 1, 6, 1, 0F); // Box 0
		baseModel[11].setRotationPoint(14F, -8F, 8.5F);
		baseModel[11].rotateAngleZ = 0.26179939F;

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
