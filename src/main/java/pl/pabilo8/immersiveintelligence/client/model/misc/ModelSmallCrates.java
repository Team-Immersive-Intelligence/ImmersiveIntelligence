package pl.pabilo8.immersiveintelligence.client.model.misc;

import pl.pabilo8.immersiveintelligence.client.model.ModelBlockBase;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * Created by Pabilo8 on 07-06-2019.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class ModelSmallCrates extends ModelBlockBase
{
	int textureX = 64;
	int textureY = 32;

	public ModelRendererTurbo[] boxCrateModel;
	public ModelRendererTurbo[] wideCrateModel;
	public ModelRendererTurbo[] cubeCrateModel;

	public ModelSmallCrates() //Same as Filename
	{
		boxCrateModel = new ModelRendererTurbo[1];
		boxCrateModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BoxWooden1

		boxCrateModel[0].addBox(0F, 0F, 0F, 14, 10, 10, 0F); // BoxWooden1
		boxCrateModel[0].setRotationPoint(1F, -10F, 3F);
		boxCrateModel[0].rotateAngleY = -0.01745329F;

		cubeCrateModel = new ModelRendererTurbo[1];
		cubeCrateModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BoxMetal1

		cubeCrateModel[0].addBox(0F, 0F, 0F, 14, 14, 14, 0F); // BoxMetal1
		cubeCrateModel[0].setRotationPoint(1F, -14F, 1F);

		wideCrateModel = new ModelRendererTurbo[1];
		wideCrateModel[0] = new ModelRendererTurbo(this, 0, 1, textureX, textureY); // BoxWooden1

		wideCrateModel[0].addBox(0F, 0F, 0F, 14, 5, 10, 0F); // BoxWooden1
		wideCrateModel[0].setRotationPoint(1F, -5F, 3F);

		parts.put("box_crate", boxCrateModel);
		parts.put("wide_crate", wideCrateModel);
		parts.put("cube_crate", cubeCrateModel);
		flipAll();
	}
}
