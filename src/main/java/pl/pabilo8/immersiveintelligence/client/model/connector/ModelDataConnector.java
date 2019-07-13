package pl.pabilo8.immersiveintelligence.client.model.connector;

import pl.pabilo8.immersiveintelligence.client.model.BaseBlockModel;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * Created by Pabilo8 on 2019-06-01.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */

public class ModelDataConnector extends BaseBlockModel
{
	int textureX = 32;
	int textureY = 32;

	public ModelDataConnector()
	{
		baseModel = new ModelRendererTurbo[4];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BoxBotton
		baseModel[1] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // BoxMiddle
		baseModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BoxTop
		baseModel[3] = new ModelRendererTurbo(this, 0, 22, textureX, textureY); // BoxToppestTop

		baseModel[0].addBox(-4F, 2F, -4F, 8, 6, 8, 0F); // BoxBotton
		baseModel[0].setRotationPoint(8F, -8F, 8F);

		baseModel[1].addBox(-3F, 0F, -3F, 6, 2, 6, 0F); // BoxMiddle
		baseModel[1].setRotationPoint(8F, -8F, 8F);

		baseModel[2].addBox(-4F, -2F, -4F, 8, 2, 8, 0F); // BoxTop
		baseModel[2].setRotationPoint(8F, -8F, 8F);

		baseModel[3].addTrapezoid(-2F, -4F, -2F, 4, 2, 4, 0F, 1.00F, ModelRendererTurbo.MR_BOTTOM); // BoxToppestTop
		baseModel[3].setRotationPoint(8F, -8F, 8F);

		flipAll();
	}
}