package pl.pabilo8.immersiveintelligence.client.model.multiblock.wooden;

import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.BaseBlockModel;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * Created by Pabilo8 on 2019-06-01.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class ModelSkyCrateStation extends BaseBlockModel
{
	int textureX = 128;
	int textureY = 64;

	public ModelSkyCrateStation() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[16];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // PostBase
		baseModel[1] = new ModelRendererTurbo(this, 48, 1, textureX, textureY); // PostBaseStand1
		baseModel[2] = new ModelRendererTurbo(this, 48, 1, textureX, textureY); // PostBaseStand2
		baseModel[3] = new ModelRendererTurbo(this, 115, 1, textureX, textureY); // PostPole1
		baseModel[4] = new ModelRendererTurbo(this, 115, 1, textureX, textureY); // PostPole2
		baseModel[5] = new ModelRendererTurbo(this, 0, 18, textureX, textureY); // PostTop
		baseModel[6] = new ModelRendererTurbo(this, 0, 37, textureX, textureY); // PostTopMiddle
		baseModel[7] = new ModelRendererTurbo(this, 22, 18, textureX, textureY); // PostBaseArm1
		baseModel[8] = new ModelRendererTurbo(this, 22, 18, textureX, textureY); // PostBaseArm2
		baseModel[9] = new ModelRendererTurbo(this, 42, 18, textureX, textureY); // PostTopBox
		baseModel[10] = new ModelRendererTurbo(this, 0, 18, textureX, textureY); // PostHook1
		baseModel[11] = new ModelRendererTurbo(this, 0, 18, textureX, textureY); // PostHook2
		baseModel[12] = new ModelRendererTurbo(this, 0, 18, textureX, textureY); // PostHook3
		baseModel[13] = new ModelRendererTurbo(this, 97, 11, textureX, textureY); // PostSidePlate1
		baseModel[14] = new ModelRendererTurbo(this, 97, 11, textureX, textureY); // PostSidePlate2
		baseModel[15] = new ModelRendererTurbo(this, 22, 23, textureX, textureY); // PostTopHookUnder

		baseModel[0].addBox(-16F, 0F, -8F, 16, 2, 16, 0F); // PostBase
		baseModel[0].setRotationPoint(16F, -2F, 8F);

		baseModel[1].addBox(-16F, 0F, -6F, 16, 3, 3, 0F); // PostBaseStand1
		baseModel[1].setRotationPoint(16F, -5F, 8F);

		baseModel[2].addBox(-16F, 0F, 3F, 16, 3, 3, 0F); // PostBaseStand2
		baseModel[2].setRotationPoint(16F, -5F, 8F);

		baseModel[3].addBox(-3F, 0F, 3F, 3, 40, 3, 0F); // PostPole1
		baseModel[3].setRotationPoint(16F, -45F, 8F);

		baseModel[4].addBox(-3F, 0F, -6F, 3, 40, 3, 0F); // PostPole2
		baseModel[4].setRotationPoint(16F, -45F, 8F);

		baseModel[5].addBox(-3F, 0F, -8F, 3, 3, 16, 0F); // PostTop
		baseModel[5].setRotationPoint(16F, -48F, 8F);

		baseModel[6].addBox(-16F, 0F, -3F, 32, 2, 6, 0F); // PostTopMiddle
		baseModel[6].setRotationPoint(16F, -45F, 8F);

		baseModel[7].addBox(-14.5F, 0F, -5.5F, 11, 3, 2, 0F); // PostBaseArm1
		baseModel[7].setRotationPoint(16F, -13F, 8F);
		baseModel[7].rotateAngleZ = -0.62831853F;

		baseModel[8].addBox(-14.5F, 0F, 3.5F, 11, 3, 2, 0F); // PostBaseArm2
		baseModel[8].setRotationPoint(16F, -13F, 8F);
		baseModel[8].rotateAngleZ = -0.62831853F;

		baseModel[9].addBox(-14F, 0F, -4F, 10, 6, 8, 0F); // PostTopBox
		baseModel[9].setRotationPoint(16F, -43F, 8F);

		baseModel[10].addFlexTrapezoid(0F, 0F, -2F, 1, 1, 4, 0F, 0.00F, 0.00F, -1.00F, -1.00F, -1.00F, -1.00F, ModelRendererTurbo.MR_BOTTOM); // PostHook1
		baseModel[10].setRotationPoint(16F, -42F, 8F);

		baseModel[11].addBox(0F, 0F, -2F, 1, 1, 1, 0F); // PostHook2
		baseModel[11].setRotationPoint(16F, -43F, 8F);

		baseModel[12].addBox(0F, 0F, 1F, 1, 1, 1, 0F); // PostHook3
		baseModel[12].setRotationPoint(16F, -43F, 8F);

		baseModel[13].addBox(-5F, 0F, 6F, 8, 32, 1, 0F); // PostSidePlate1
		baseModel[13].setRotationPoint(16F, -41F, 8F);

		baseModel[14].addBox(-2F, 0F, 6F, 8, 32, 1, 0F); // PostSidePlate2
		baseModel[14].setRotationPoint(16F, -41F, 8F);
		baseModel[14].rotateAngleY = 3.14159265F;

		baseModel[15].addBox(-2F, 0F, -2.5F, 4, 1, 5, 0F); // PostTopHookUnder
		baseModel[15].setRotationPoint(16F, -43.5F, 8F);

		flipAll();
	}

	@Override
	public void getTranslation(EnumFacing facing, BaseBlockModel model)
	{
		switch(facing)
		{
			case NORTH:
			{
				model.translateAll(-8f, 0f, -8f);
			}
			break;
			case SOUTH:
			{
				model.translateAll(-8f, 0f, 8f);
			}
			break;
			case EAST:
			{
				model.translateAll(-16f, 0f, 0f);
			}
			break;
			case WEST:
			{
				model.translateAll(0f, 0f, 0f);
			}
			break;
		}
	}
}
