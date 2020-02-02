package pl.pabilo8.immersiveintelligence.client.model.connector;

import pl.pabilo8.immersiveintelligence.client.model.BaseBlockModel;
import pl.pabilo8.immersiveintelligence.client.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.tmt.Shape2D;

/**
 * Created by Pabilo8 on 2019-06-01.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class ModelWheel extends BaseBlockModel
{
	public ModelRendererTurbo[] wheel;

	int textureX = 32;
	int textureY = 32;

	public ModelWheel() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[5];
		baseModel[0] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // MainBox
		baseModel[1] = new ModelRendererTurbo(this, 18, 14, textureX, textureY); // MainBox
		baseModel[2] = new ModelRendererTurbo(this, 16, 4, textureX, textureY); // MainBox
		baseModel[3] = new ModelRendererTurbo(this, 16, 0, textureX, textureY); // MainBox
		baseModel[4] = new ModelRendererTurbo(this, 18, 19, textureX, textureY); // MainBox

		baseModel[0].addBox(0F, 0F, 0F, 8, 8, 1, 0F); // MainBox
		baseModel[0].setRotationPoint(4F, -12F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // MainBox
		baseModel[1].setRotationPoint(6F, -10F, 11F);

		baseModel[2].addBox(0F, 0F, 0F, 3, 3, 1, 0F); // MainBox
		baseModel[2].setRotationPoint(6.5F, -9.5F, 4F);

		baseModel[3].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // MainBox
		baseModel[3].setRotationPoint(7F, -9F, 2F);

		baseModel[4].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // MainBox
		baseModel[4].setRotationPoint(6F, -10F, 1F);


		wheel = new ModelRendererTurbo[2];
		wheel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		wheel[1] = new ModelRendererTurbo(this, 24, 5, textureX, textureY); // MainBox

		wheel[0].addShape3D(4F, -4F, -3F, new Shape2D(new Coord2D[]{new Coord2D(2, 0, 2, 0), new Coord2D(6, 0, 6, 0), new Coord2D(8, 2, 8, 2), new Coord2D(8, 6, 8, 6), new Coord2D(6, 8, 6, 8), new Coord2D(2, 8, 2, 8), new Coord2D(0, 6, 0, 6), new Coord2D(0, 2, 0, 2)}), 6, 8, 8, 28, 6, ModelRendererTurbo.MR_FRONT, new float[]{3, 4, 3, 4, 3, 4, 3, 4}); // Shape 5
		wheel[0].setRotationPoint(0F, -0F, 8F);

		wheel[1].addBox(-1F, -1F, 0F, 2, 2, 1, 0F); // MainBox
		wheel[1].setRotationPoint(0F, -0F, 12F);

		parts.put("base", baseModel);
		parts.put("wheel", wheel);

		flipAll();
	}
}
