package pl.pabilo8.immersiveintelligence.client.model.motor_belt;

import pl.pabilo8.immersiveintelligence.api.rotary.IModelMotorBelt;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * Created by Pabilo8 on 07-06-2019.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class ModelSteelMotorBelt extends ModelIIBase implements IModelMotorBelt
{
	int textureX = 64;
	int textureY = 32;

	public ModelSteelMotorBelt() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[25];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[2] = new ModelRendererTurbo(this, 6, 0, textureX, textureY); // Box 0
		baseModel[3] = new ModelRendererTurbo(this, 0, 4, textureX, textureY); // Box 0
		baseModel[4] = new ModelRendererTurbo(this, 8, 2, textureX, textureY); // Box 0
		baseModel[5] = new ModelRendererTurbo(this, 4, 2, textureX, textureY); // Box 0
		baseModel[6] = new ModelRendererTurbo(this, 0, 2, textureX, textureY); // Box 0
		baseModel[7] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[8] = new ModelRendererTurbo(this, 6, 0, textureX, textureY); // Box 0
		baseModel[9] = new ModelRendererTurbo(this, 0, 4, textureX, textureY); // Box 0
		baseModel[10] = new ModelRendererTurbo(this, 8, 2, textureX, textureY); // Box 0
		baseModel[11] = new ModelRendererTurbo(this, 4, 2, textureX, textureY); // Box 0
		baseModel[12] = new ModelRendererTurbo(this, 0, 2, textureX, textureY); // Box 0
		baseModel[13] = new ModelRendererTurbo(this, 0, 18, textureX, textureY); // Box 0
		baseModel[14] = new ModelRendererTurbo(this, 0, 18, textureX, textureY); // Box 0
		baseModel[15] = new ModelRendererTurbo(this, 32, 0, textureX, textureY); // Box 0
		baseModel[16] = new ModelRendererTurbo(this, 32, 0, textureX, textureY); // Box 0
		baseModel[17] = new ModelRendererTurbo(this, 34, 22, textureX, textureY); // Box 0
		baseModel[18] = new ModelRendererTurbo(this, 34, 22, textureX, textureY); // Box 0
		baseModel[19] = new ModelRendererTurbo(this, 12, 17, textureX, textureY); // Box 0
		baseModel[20] = new ModelRendererTurbo(this, 12, 17, textureX, textureY); // Box 0
		baseModel[21] = new ModelRendererTurbo(this, 40, 9, textureX, textureY); // Box 0
		baseModel[22] = new ModelRendererTurbo(this, 40, 9, textureX, textureY); // Box 0
		baseModel[23] = new ModelRendererTurbo(this, 24, 23, textureX, textureY); // Box 0
		baseModel[24] = new ModelRendererTurbo(this, 24, 23, textureX, textureY); // Box 0

		baseModel[0].addBox(-4F, 0F, -8F, 8, 1, 16, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, 0F, 0F);
		baseModel[0].rotateAngleY = 1.57f;

		baseModel[1].addBox(-3F, 1F, -5F, 2, 1, 1, 0F); // Box 0
		baseModel[1].setRotationPoint(0F, 0F, 0F);
		baseModel[1].rotateAngleY = 1.57f;

		baseModel[2].addBox(1F, 1F, -5F, 2, 1, 1, 0F); // Box 0
		baseModel[2].setRotationPoint(0F, 0F, 0F);
		baseModel[2].rotateAngleY = 1.57f;

		baseModel[3].addShapeBox(-4F, 1F, -5F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // Box 0
		baseModel[3].setRotationPoint(0F, 0F, 0F);
		baseModel[3].rotateAngleY = 1.57f;

		baseModel[4].addShapeBox(0F, 1F, -5F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // Box 0
		baseModel[4].setRotationPoint(0F, 0F, 0F);
		baseModel[4].rotateAngleY = 1.57f;

		baseModel[5].addShapeBox(-1F, 1F, -5F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[5].setRotationPoint(0F, 0F, 0F);
		baseModel[5].rotateAngleY = 1.57f;

		baseModel[6].addShapeBox(3F, 1F, -5F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[6].setRotationPoint(0F, 0F, 0F);
		baseModel[6].rotateAngleY = 1.57f;

		baseModel[7].addBox(-3F, 1F, 4F, 2, 1, 1, 0F); // Box 0
		baseModel[7].setRotationPoint(0F, 0F, 0F);
		baseModel[7].rotateAngleY = 1.57f;

		baseModel[8].addBox(1F, 1F, 4F, 2, 1, 1, 0F); // Box 0
		baseModel[8].setRotationPoint(0F, 0F, 0F);
		baseModel[8].rotateAngleY = 1.57f;

		baseModel[9].addShapeBox(-4F, 1F, 4F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // Box 0
		baseModel[9].setRotationPoint(0F, 0F, 0F);
		baseModel[9].rotateAngleY = 1.57f;

		baseModel[10].addShapeBox(0F, 1F, 4F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // Box 0
		baseModel[10].setRotationPoint(0F, 0F, 0F);
		baseModel[10].rotateAngleY = 1.57f;

		baseModel[11].addShapeBox(-1F, 1F, 4F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[11].setRotationPoint(0F, 0F, 0F);
		baseModel[11].rotateAngleY = 1.57f;

		baseModel[12].addShapeBox(3F, 1F, 4F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[12].setRotationPoint(0F, 0F, 0F);
		baseModel[12].rotateAngleY = 1.57f;

		baseModel[13].addBox(-3F, -1F, -8F, 2, 1, 8, 0F); // Box 0
		baseModel[13].setRotationPoint(0F, 0F, 0F);
		baseModel[13].rotateAngleY = 1.57f;

		baseModel[14].addBox(1F, -1F, -8F, 2, 1, 8, 0F); // Box 0
		baseModel[14].setRotationPoint(0F, 0F, 0F);
		baseModel[14].rotateAngleY = 1.57f;

		baseModel[15].addShapeBox(-4F, -1F, -8F, 1, 1, 8, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[15].setRotationPoint(0F, 0F, 0F);
		baseModel[15].rotateAngleY = 1.57f;

		baseModel[16].addShapeBox(0F, -1F, -8F, 1, 1, 8, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[16].setRotationPoint(0F, 0F, 0F);
		baseModel[16].rotateAngleY = 1.57f;

		baseModel[17].addShapeBox(-1F, -1F, -8F, 1, 1, 8, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[17].setRotationPoint(0F, 0F, 0F);
		baseModel[17].rotateAngleY = 1.57f;

		baseModel[18].addShapeBox(3F, -1F, -8F, 1, 1, 8, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[18].setRotationPoint(0F, 0F, 0F);
		baseModel[18].rotateAngleY = 1.57f;

		baseModel[19].addBox(-3F, -1F, 0F, 2, 1, 8, 0F); // Box 0
		baseModel[19].setRotationPoint(0F, 0F, 0F);
		baseModel[19].rotateAngleY = 1.57f;

		baseModel[20].addBox(1F, -1F, 0F, 2, 1, 8, 0F); // Box 0
		baseModel[20].setRotationPoint(0F, 0F, 0F);
		baseModel[20].rotateAngleY = 1.57f;

		baseModel[21].addShapeBox(-4F, -1F, 0F, 1, 1, 8, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[21].setRotationPoint(0F, 0F, 0F);
		baseModel[21].rotateAngleY = 1.57f;

		baseModel[22].addShapeBox(0F, -1F, 0F, 1, 1, 8, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[22].setRotationPoint(0F, 0F, 0F);
		baseModel[22].rotateAngleY = 1.57f;

		baseModel[23].addShapeBox(-1F, -1F, 0F, 1, 1, 8, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[23].setRotationPoint(0F, 0F, 0F);
		baseModel[23].rotateAngleY = 1.57f;

		baseModel[24].addShapeBox(3F, -1F, 0F, 1, 1, 8, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[24].setRotationPoint(0F, 0F, 0F);
		baseModel[24].rotateAngleY = 1.57f;

		flipAll();
	}

	@Override
	public void renderBelt()
	{
		render();
	}

	@Override
	public void setRotation(float y)
	{
		for(ModelRendererTurbo mod : baseModel)
			mod.rotateAngleZ = y;
	}
}
