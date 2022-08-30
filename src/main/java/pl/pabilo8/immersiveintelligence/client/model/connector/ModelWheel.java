package pl.pabilo8.immersiveintelligence.client.model.connector;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * Created by Pabilo8 on 2019-06-01.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class ModelWheel extends ModelIIBase
{
	public ModelRendererTurbo[] wheel;

	int textureX = 32;
	int textureY = 16;

	public ModelWheel() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[6];
		baseModel[0] = new ModelRendererTurbo(this, 14, 5, textureX, textureY); // MainBox
		baseModel[1] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // MainBox
		baseModel[2] = new ModelRendererTurbo(this, 22, 0, textureX, textureY); // MainBox
		baseModel[3] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // MainBox
		baseModel[4] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // MainBox
		baseModel[5] = new ModelRendererTurbo(this, 14, 5, textureX, textureY); // MainBox

		baseModel[0].addShapeBox(0F, 0F, 0F, 8, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MainBox
		baseModel[0].setRotationPoint(4F, -12F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // MainBox
		baseModel[1].setRotationPoint(6F, -10F, 9F);

		baseModel[2].addBox(0F, 0F, 0F, 3, 3, 2, 0F); // MainBox
		baseModel[2].setRotationPoint(6.5F, -9.5F, 2F);

		baseModel[3].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // MainBox
		baseModel[3].setRotationPoint(6F, -10F, 1F);

		baseModel[4].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // MainBox
		baseModel[4].setRotationPoint(6F, -10F, 4F);

		baseModel[5].setFlipped(true);
		baseModel[5].addShapeBox(0F, 0F, 0F, 8, 4, 1, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F, 0F, -4F, 0F); // MainBox
		baseModel[5].setRotationPoint(4F, -8F, 0F);


		wheel = new ModelRendererTurbo[3];
		wheel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5
		wheel[1] = new ModelRendererTurbo(this, 18, 12, textureX, textureY); // MainBox
		wheel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 5

		wheel[0].addShape3D(6F, -6F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(6, 0, 6, 0), new Coord2D(6, 12, 6, 12), new Coord2D(2, 12, 2, 12), new Coord2D(0, 10, 0, 10), new Coord2D(0, 2, 0, 2) }), 4, 6, 12, 34, 4, ModelRendererTurbo.MR_FRONT, new float[] {3 ,8 ,3 ,4 ,12 ,4}); // Shape 5
		wheel[0].setRotationPoint(0, 0, 9F);

		wheel[1].addBox(-1F, -1F, 0F, 2, 2, 1, 0F); // MainBox
		wheel[1].setRotationPoint(0, 0, 10F);

		wheel[2].addShape3D(6F, -6F, 0F, new Shape2D(new Coord2D[] { new Coord2D(2, 0, 2, 0), new Coord2D(6, 0, 6, 0), new Coord2D(6, 12, 6, 12), new Coord2D(2, 12, 2, 12), new Coord2D(0, 10, 0, 10), new Coord2D(0, 2, 0, 2) }), 4, 6, 12, 34, 4, ModelRendererTurbo.MR_FRONT, new float[] {3 ,8 ,3 ,4 ,12 ,4}); // Shape 5
		wheel[2].setRotationPoint(0, 0, 9F);
		wheel[2].rotateAngleZ = 3.14159265F;

		parts.put("base", baseModel);
		parts.put("wheel", wheel);

		flipAll();
	}
}
