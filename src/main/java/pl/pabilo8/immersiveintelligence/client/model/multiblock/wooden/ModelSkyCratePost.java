package pl.pabilo8.immersiveintelligence.client.model.multiblock.wooden;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * Created by Pabilo8 on 2019-06-01.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class ModelSkyCratePost extends ModelIIBase
{
	int textureX = 128;
	int textureY = 64;

	public ModelSkyCratePost() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[16];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Import PostBase
		baseModel[1] = new ModelRendererTurbo(this, 48, 1, textureX, textureY); // Import PostBaseStand1
		baseModel[2] = new ModelRendererTurbo(this, 48, 1, textureX, textureY); // Import PostBaseStand2
		baseModel[3] = new ModelRendererTurbo(this, 115, 1, textureX, textureY); // Import PostPole1
		baseModel[4] = new ModelRendererTurbo(this, 115, 1, textureX, textureY); // Import PostPole2
		baseModel[5] = new ModelRendererTurbo(this, 0, 18, textureX, textureY); // Import PostTop
		baseModel[6] = new ModelRendererTurbo(this, 0, 37, textureX, textureY); // Import PostTopMiddle
		baseModel[7] = new ModelRendererTurbo(this, 22, 18, textureX, textureY); // Import PostBaseArm1
		baseModel[8] = new ModelRendererTurbo(this, 22, 18, textureX, textureY); // Import PostBaseArm2
		baseModel[9] = new ModelRendererTurbo(this, 42, 18, textureX, textureY); // Import PostTopBox
		baseModel[10] = new ModelRendererTurbo(this, 0, 18, textureX, textureY); // Import PostHook1
		baseModel[11] = new ModelRendererTurbo(this, 0, 18, textureX, textureY); // Import PostHook2
		baseModel[12] = new ModelRendererTurbo(this, 0, 18, textureX, textureY); // Import PostHook3
		baseModel[13] = new ModelRendererTurbo(this, 97, 11, textureX, textureY); // Import PostSidePlate1
		baseModel[14] = new ModelRendererTurbo(this, 97, 11, textureX, textureY); // Import PostSidePlate2
		baseModel[15] = new ModelRendererTurbo(this, 22, 23, textureX, textureY); // Import PostTopHookUnder

		baseModel[0].addBox(-16F, 0F, -8F, 16, 2, 16, 0F); // Import PostBase
		baseModel[0].setRotationPoint(16F, -2F, 8F);

		baseModel[1].addBox(-16F, 0F, -6F, 16, 3, 3, 0F); // Import PostBaseStand1
		baseModel[1].setRotationPoint(16F, -5F, 8F);

		baseModel[2].addBox(-16F, 0F, 3F, 16, 3, 3, 0F); // Import PostBaseStand2
		baseModel[2].setRotationPoint(16F, -5F, 8F);

		baseModel[3].addBox(-3F, 0F, 3F, 3, 40, 3, 0F); // Import PostPole1
		baseModel[3].setRotationPoint(16F, -45F, 8F);

		baseModel[4].addBox(-3F, 0F, -6F, 3, 40, 3, 0F); // Import PostPole2
		baseModel[4].setRotationPoint(16F, -45F, 8F);

		baseModel[5].addBox(-3F, 0F, -8F, 3, 3, 16, 0F); // Import PostTop
		baseModel[5].setRotationPoint(16F, -48F, 8F);

		baseModel[6].addBox(-16F, 0F, -3F, 32, 2, 6, 0F); // Import PostTopMiddle
		baseModel[6].setRotationPoint(16F, -45F, 8F);

		baseModel[7].addBox(-14.5F, 0F, -5.5F, 11, 3, 2, 0F); // Import PostBaseArm1
		baseModel[7].setRotationPoint(16F, -13F, 8F);
		baseModel[7].rotateAngleZ = 0.62831853F;

		baseModel[8].addBox(-14.5F, 0F, 3.5F, 11, 3, 2, 0F); // Import PostBaseArm2
		baseModel[8].setRotationPoint(16F, -13F, 8F);
		baseModel[8].rotateAngleZ = 0.62831853F;

		baseModel[9].addBox(-14F, 0F, -4F, 10, 6, 8, 0F); // Import PostTopBox
		baseModel[9].setRotationPoint(16F, -43F, 8F);

		baseModel[10].addShapeBox(-0.5F, 0F, -2F, 1, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F); // Import PostHook1
		baseModel[10].setRotationPoint(24.5F, -41.5F, 8F);
		baseModel[10].rotateAngleY = 1.57079633F;

		baseModel[11].addBox(0F, 0F, -2.5F, 1, 1, 1, 0F); // Import PostHook2
		baseModel[11].setRotationPoint(25.5F, -42.5F, 10F);

		baseModel[12].addBox(0F, 0F, 0.5F, 1, 1, 1, 0F); // Import PostHook3
		baseModel[12].setRotationPoint(22.5F, -42.5F, 7F);

		baseModel[13].addBox(-5F, 0F, 6F, 8, 32, 1, 0F); // Import PostSidePlate1
		baseModel[13].setRotationPoint(16F, -41F, 8F);

		baseModel[14].addBox(-2F, 0F, 6F, 8, 32, 1, 0F); // Import PostSidePlate2
		baseModel[14].setRotationPoint(16F, -41F, 8F);
		baseModel[14].rotateAngleY = -3.14159265F;

		baseModel[15].addBox(6F, 0F, -2.5F, 5, 1, 5, 0F); // Import PostTopHookUnder
		baseModel[15].setRotationPoint(16F, -43.5F, 8F);

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
