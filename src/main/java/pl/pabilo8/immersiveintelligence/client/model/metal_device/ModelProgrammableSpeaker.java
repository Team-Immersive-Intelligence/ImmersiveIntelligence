package pl.pabilo8.immersiveintelligence.client.model.metal_device;

import pl.pabilo8.immersiveintelligence.client.model.ModelBlockBase;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 15-06-2019
 */
public class ModelProgrammableSpeaker extends ModelBlockBase
{
	int textureX = 32;
	int textureY = 64;

	public ModelProgrammableSpeaker()
	{
		baseModel = new ModelRendererTurbo[26];
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
		baseModel[14] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BackSpeaker
		baseModel[15] = new ModelRendererTurbo(this, 0, 21, textureX, textureY); // BackSpeakerDot
		baseModel[16] = new ModelRendererTurbo(this, 10, 35, textureX, textureY); // BackSpeakerFrame
		baseModel[17] = new ModelRendererTurbo(this, 10, 35, textureX, textureY); // BackSpeakerFrame
		baseModel[18] = new ModelRendererTurbo(this, 12, 21, textureX, textureY); // BackSpeakerFrame
		baseModel[19] = new ModelRendererTurbo(this, 12, 21, textureX, textureY); // BackSpeakerFrame
		baseModel[20] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BackSpeaker
		baseModel[21] = new ModelRendererTurbo(this, 0, 21, textureX, textureY); // BackSpeakerDot
		baseModel[22] = new ModelRendererTurbo(this, 10, 35, textureX, textureY); // BackSpeakerFrame
		baseModel[23] = new ModelRendererTurbo(this, 10, 35, textureX, textureY); // BackSpeakerFrame
		baseModel[24] = new ModelRendererTurbo(this, 12, 21, textureX, textureY); // BackSpeakerFrame
		baseModel[25] = new ModelRendererTurbo(this, 12, 21, textureX, textureY); // BackSpeakerFrame

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

		baseModel[14].addTrapezoid(-5F, 0F, 2F, 10, 10, 6, 0F, -2.00F, ModelRendererTurbo.MR_FRONT); // BackSpeaker
		baseModel[14].setRotationPoint(8F, -13F, 8F);
		baseModel[14].rotateAngleY = 1.57079633F;

		baseModel[15].addTrapezoid(-1F, 4F, 8F, 2, 2, 2, 0F, 0.50F, ModelRendererTurbo.MR_FRONT); // BackSpeakerDot
		baseModel[15].setRotationPoint(8F, -13F, 8F);
		baseModel[15].rotateAngleY = 1.57079633F;

		baseModel[16].addBox(-5F, 0F, 8F, 10, 1, 1, 0F); // BackSpeakerFrame
		baseModel[16].setRotationPoint(8F, -13F, 8F);
		baseModel[16].rotateAngleY = 1.57079633F;

		baseModel[17].addBox(-5F, 9F, 8F, 10, 1, 1, 0F); // BackSpeakerFrame
		baseModel[17].setRotationPoint(8F, -13F, 8F);
		baseModel[17].rotateAngleY = 1.57079633F;

		baseModel[18].addBox(-5F, 1F, 8F, 1, 8, 1, 0F); // BackSpeakerFrame
		baseModel[18].setRotationPoint(8F, -13F, 8F);
		baseModel[18].rotateAngleY = 1.57079633F;

		baseModel[19].addBox(4F, 1F, 8F, 1, 8, 1, 0F); // BackSpeakerFrame
		baseModel[19].setRotationPoint(8F, -13F, 8F);
		baseModel[19].rotateAngleY = 1.57079633F;

		baseModel[20].addTrapezoid(-5F, 0F, 2F, 10, 10, 6, 0F, -2.00F, ModelRendererTurbo.MR_FRONT); // BackSpeaker
		baseModel[20].setRotationPoint(8F, -13F, 8F);
		baseModel[20].rotateAngleY = -1.57079633F;

		baseModel[21].addTrapezoid(-1F, 4F, 8F, 2, 2, 2, 0F, 0.50F, ModelRendererTurbo.MR_FRONT); // BackSpeakerDot
		baseModel[21].setRotationPoint(8F, -13F, 8F);
		baseModel[21].rotateAngleY = -1.57079633F;

		baseModel[22].addBox(-5F, 0F, 8F, 10, 1, 1, 0F); // BackSpeakerFrame
		baseModel[22].setRotationPoint(8F, -13F, 8F);
		baseModel[22].rotateAngleY = -1.57079633F;

		baseModel[23].addBox(-5F, 9F, 8F, 10, 1, 1, 0F); // BackSpeakerFrame
		baseModel[23].setRotationPoint(8F, -13F, 8F);
		baseModel[23].rotateAngleY = -1.57079633F;

		baseModel[24].addBox(-5F, 1F, 8F, 1, 8, 1, 0F); // BackSpeakerFrame
		baseModel[24].setRotationPoint(8F, -13F, 8F);
		baseModel[24].rotateAngleY = -1.57079633F;

		baseModel[25].addBox(4F, 1F, 8F, 1, 8, 1, 0F); // BackSpeakerFrame
		baseModel[25].setRotationPoint(8F, -13F, 8F);
		baseModel[25].rotateAngleY = -1.57079633F;

		flipAll();
	}

}
