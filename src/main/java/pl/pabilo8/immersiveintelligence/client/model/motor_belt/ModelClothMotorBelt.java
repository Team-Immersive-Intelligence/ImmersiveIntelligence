package pl.pabilo8.immersiveintelligence.client.model.motor_belt;

import pl.pabilo8.immersiveintelligence.api.rotary.IModelMotorBelt;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * Created by Pabilo8 on 07-06-2019.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class ModelClothMotorBelt extends ModelIIBase implements IModelMotorBelt
{
	int textureX = 32;
	int textureY = 16;

	public ModelClothMotorBelt() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[1];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0

		baseModel[0].addBox(-3F, -1F, -5F, 6, 1, 10, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, 0F, 0F);
		baseModel[0].rotateAngleY=1.57f;

		translateAll(0F, 0F, 0F);

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
		baseModel[0].rotateAngleZ = y;
	}
}
