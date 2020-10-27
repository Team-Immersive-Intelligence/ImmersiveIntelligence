package pl.pabilo8.immersiveintelligence.client.model.connector;

import pl.pabilo8.immersiveintelligence.client.model.ModelBlockBase;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * Created by Pabilo8 on 2019-06-01.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */

public class ModelDataSplitConnector extends ModelBlockBase
{
	int textureX = 64;
	int textureY = 32;

	public ModelDataSplitConnector() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[8];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BoxBotton
		baseModel[1] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // BoxMiddle
		baseModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BoxTop
		baseModel[3] = new ModelRendererTurbo(this, 0, 23, textureX, textureY); // BoxToppestTop
		baseModel[4] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // BoxMiddle
		baseModel[5] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BoxTop
		baseModel[6] = new ModelRendererTurbo(this, 0, 23, textureX, textureY); // BoxToppestTop
		baseModel[7] = new ModelRendererTurbo(this, 20, 9, textureX, textureY); // BoxBotton

		baseModel[0].addBox(-4F, 2F, -4F, 8, 1, 8, 0F); // BoxBotton
		baseModel[0].setRotationPoint(8F, -1F, 8F);

		baseModel[1].addBox(-3F, -3F, -7F, 6, 3, 6, 0F); // BoxMiddle
		baseModel[1].setRotationPoint(8F, -8F, 8F);
		baseModel[1].rotateAngleX = 0.43633231F;

		baseModel[2].addBox(-4F, -5F, -8F, 8, 2, 8, 0F); // BoxTop
		baseModel[2].setRotationPoint(8F, -8F, 8F);
		baseModel[2].rotateAngleX = 0.43633231F;

		baseModel[3].addTrapezoid(-2F, -7F, -6F, 4, 2, 4, 0F, 1.00F, ModelRendererTurbo.MR_BOTTOM); // BoxToppestTop
		baseModel[3].setRotationPoint(8F, -8F, 8F);
		baseModel[3].rotateAngleX = 0.43633231F;

		baseModel[4].addBox(-3F, -3F, 1F, 6, 3, 6, 0F); // BoxMiddle
		baseModel[4].setRotationPoint(8F, -8F, 8F);
		baseModel[4].rotateAngleX = -0.43633231F;

		baseModel[5].addBox(-4F, -5F, 0F, 8, 2, 8, 0F); // BoxTop
		baseModel[5].setRotationPoint(8F, -8F, 8F);
		baseModel[5].rotateAngleX = -0.43633231F;

		baseModel[6].addTrapezoid(-2F, -7F, 2F, 4, 2, 4, 0F, 1.00F, ModelRendererTurbo.MR_BOTTOM); // BoxToppestTop
		baseModel[6].setRotationPoint(8F, -8F, 8F);
		baseModel[6].rotateAngleX = -0.43633231F;

		baseModel[7].addBox(-4F, -7F, -7F, 8, 9, 14, 0F); // BoxBotton
		baseModel[7].setRotationPoint(8F, -1F, 8F);

		flipAll();
	}
}