package pl.pabilo8.immersiveintelligence.client.model.misc;

import pl.pabilo8.immersiveintelligence.client.model.BaseBlockModel;
import pl.pabilo8.immersiveintelligence.client.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.tmt.Shape2D;

/**
 * Created by Pabilo8 on 07-06-2019.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class ModelBullet extends BaseBlockModel
{
	int textureX = 64;
	int textureY = 32;

	public ModelBullet() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[8];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 1
		baseModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 1
		baseModel[2] = new ModelRendererTurbo(this, 36, 0, textureX, textureY); // Box 3
		baseModel[3] = new ModelRendererTurbo(this, 12, 14, textureX, textureY); // Box 3
		baseModel[4] = new ModelRendererTurbo(this, 2, 0, textureX, textureY); // Box 3
		baseModel[5] = new ModelRendererTurbo(this, 2, 0, textureX, textureY); // Box 3
		baseModel[6] = new ModelRendererTurbo(this, 2, 0, textureX, textureY); // Box 3
		baseModel[7] = new ModelRendererTurbo(this, 2, 0, textureX, textureY); // Box 3

		baseModel[0].addShape3D(4F, -4F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 1, 0, 1), new Coord2D(1, 0, 1, 0), new Coord2D(1, 8, 1, 8), new Coord2D(0, 7, 0, 7)}), 12, 1, 8, 18, 12, ModelRendererTurbo.MR_FRONT, new float[]{6, 2, 8, 2}); // Shape 1
		baseModel[0].setRotationPoint(8F, 0F, 8F);
		baseModel[0].rotateAngleX = -1.57079633F;

		baseModel[1].addShape3D(4F, -4F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 1, 0, 1), new Coord2D(1, 0, 1, 0), new Coord2D(1, 8, 1, 8), new Coord2D(0, 7, 0, 7)}), 12, 1, 8, 18, 12, ModelRendererTurbo.MR_FRONT, new float[]{6, 2, 8, 2}); // Shape 1
		baseModel[1].setRotationPoint(8F, 0F, 8F);
		baseModel[1].rotateAngleX = -1.57079633F;
		baseModel[1].rotateAngleY = 3.14159265F;

		baseModel[2].addBox(-3F, -12F, -4F, 6, 12, 8, 0F); // Box 3
		baseModel[2].setRotationPoint(8F, 0F, 8F);

		baseModel[3].addFlexTrapezoid(-3F, -16F, -3F, 6, 3, 6, 0F, -2.00F, -2.00F, -2.00F, -2.00F, -2.00F, -2.00F, ModelRendererTurbo.MR_TOP); // Box 3
		baseModel[3].setRotationPoint(8F, 0F, 8F);

		baseModel[4].addShapeBox(-3F, -13F, 3F, 6, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 1F, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 3
		baseModel[4].setRotationPoint(8F, 0F, 8F);

		baseModel[5].addShapeBox(-3F, -13F, 3F, 6, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 1F, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 3
		baseModel[5].setRotationPoint(8F, 0F, 8F);
		baseModel[5].rotateAngleY = 3.14159265F;

		baseModel[6].addShapeBox(-3F, -13F, 3F, 6, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 3
		baseModel[6].setRotationPoint(8F, 0F, 8F);
		baseModel[6].rotateAngleY = 1.57079633F;

		baseModel[7].addShapeBox(-3F, -13F, 3F, 6, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 3
		baseModel[7].setRotationPoint(8F, 0F, 8F);
		baseModel[7].rotateAngleY = -1.57079633F;

		flipAll();
	}
}
