package pl.pabilo8.immersiveintelligence.client.model.metal_device;

import pl.pabilo8.immersiveintelligence.client.model.BaseBlockModel;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 15-06-2019
 */
public class ModelAlarmSiren extends BaseBlockModel
{
	int textureX = 32;
	int textureY = 64;

	public ModelAlarmSiren()
	{
		baseModel = new ModelRendererTurbo[14];
		baseModel[0] = new ModelRendererTurbo(this, 16, 16, textureX, textureY); // MainBox
		baseModel[1] = new ModelRendererTurbo(this, 0, 16, textureX, textureY); // TopBox
		baseModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BackSpeaker
		baseModel[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // FrontSpeaker
		baseModel[4] = new ModelRendererTurbo(this, 0, 21, textureX, textureY); // BackSpeakerDot
		baseModel[5] = new ModelRendererTurbo(this, 0, 21, textureX, textureY); // FrontSpeakerDot
		baseModel[6] = new ModelRendererTurbo(this, 10, 35, textureX, textureY); // BackSpeakerFrame
		baseModel[7] = new ModelRendererTurbo(this, 10, 35, textureX, textureY); // BackSpeakerFrame
		baseModel[8] = new ModelRendererTurbo(this, 12, 21, textureX, textureY); // BackSpeakerFrame
		baseModel[9] = new ModelRendererTurbo(this, 12, 21, textureX, textureY); // BackSpeakerFrame
		baseModel[10] = new ModelRendererTurbo(this, 10, 35, textureX, textureY); // BackSpeakerFrame
		baseModel[11] = new ModelRendererTurbo(this, 10, 35, textureX, textureY); // BackSpeakerFrame
		baseModel[12] = new ModelRendererTurbo(this, 12, 21, textureX, textureY); // BackSpeakerFrame
		baseModel[13] = new ModelRendererTurbo(this, 12, 21, textureX, textureY); // BackSpeakerFrame

		baseModel[0].addBox(0F, 0F, 0F, 4, 15, 4, 0F); // MainBox
		baseModel[0].setRotationPoint(6F, -15F, 6F);

		baseModel[1].addTrapezoid(0F, 0F, 0F, 4, 1, 4, 0F, -1.00F, ModelRendererTurbo.MR_TOP); // TopBox
		baseModel[1].setRotationPoint(6F, -16F, 6F);

		baseModel[2].addTrapezoid(-5F, 0F, 2F, 10, 10, 6, 0F, -2.00F, ModelRendererTurbo.MR_FRONT); // BackSpeaker
		baseModel[2].setRotationPoint(8F, -13F, 8F);

		baseModel[3].addTrapezoid(-5F, 0F, 2F, 10, 10, 6, 0F, -2.00F, ModelRendererTurbo.MR_FRONT); // FrontSpeaker
		baseModel[3].setRotationPoint(8F, -13F, 8F);
		baseModel[3].rotateAngleY = 3.14159265F;

		baseModel[4].addTrapezoid(-1F, 4F, 8F, 2, 2, 2, 0F, 0.50F, ModelRendererTurbo.MR_FRONT); // BackSpeakerDot
		baseModel[4].setRotationPoint(8F, -13F, 8F);

		baseModel[5].addTrapezoid(-1F, 4F, 8F, 2, 2, 2, 0F, 0.50F, ModelRendererTurbo.MR_FRONT); // FrontSpeakerDot
		baseModel[5].setRotationPoint(8F, -13F, 8F);
		baseModel[5].rotateAngleY = 3.14159265F;

		baseModel[6].addBox(-5F, 0F, 8F, 10, 1, 1, 0F); // BackSpeakerFrame
		baseModel[6].setRotationPoint(8F, -13F, 8F);

		baseModel[7].addBox(-5F, 9F, 8F, 10, 1, 1, 0F); // BackSpeakerFrame
		baseModel[7].setRotationPoint(8F, -13F, 8F);

		baseModel[8].addBox(-5F, 1F, 8F, 1, 8, 1, 0F); // BackSpeakerFrame
		baseModel[8].setRotationPoint(8F, -13F, 8F);

		baseModel[9].addBox(4F, 1F, 8F, 1, 8, 1, 0F); // BackSpeakerFrame
		baseModel[9].setRotationPoint(8F, -13F, 8F);

		baseModel[10].addBox(-5F, 0F, 8F, 10, 1, 1, 0F); // BackSpeakerFrame
		baseModel[10].setRotationPoint(8F, -13F, 8F);
		baseModel[10].rotateAngleY = 3.14159265F;

		baseModel[11].addBox(-5F, 9F, 8F, 10, 1, 1, 0F); // BackSpeakerFrame
		baseModel[11].setRotationPoint(8F, -13F, 8F);
		baseModel[11].rotateAngleY = 3.14159265F;

		baseModel[12].addBox(-5F, 1F, 8F, 1, 8, 1, 0F); // BackSpeakerFrame
		baseModel[12].setRotationPoint(8F, -13F, 8F);
		baseModel[12].rotateAngleY = 3.14159265F;

		baseModel[13].addBox(4F, 1F, 8F, 1, 8, 1, 0F); // BackSpeakerFrame
		baseModel[13].setRotationPoint(8F, -13F, 8F);
		baseModel[13].rotateAngleY = 3.14159265F;

		flipAll();
	}

}
