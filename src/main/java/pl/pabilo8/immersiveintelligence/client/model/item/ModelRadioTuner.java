package pl.pabilo8.immersiveintelligence.client.model.item;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * Created by Pabilo8 on 07-06-2019.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class ModelRadioTuner extends ModelIIBase
{
	int textureX = 64;
	int textureY = 32;
	public ModelRendererTurbo[] sliderModel1, sliderModel2;

	public ModelRadioTuner() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[22];
		baseModel[0] = new ModelRendererTurbo(this, 16, 12, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 1
		baseModel[2] = new ModelRendererTurbo(this, 60, 0, textureX, textureY); // Box 0
		baseModel[3] = new ModelRendererTurbo(this, 52, 0, textureX, textureY); // Box 0
		baseModel[4] = new ModelRendererTurbo(this, 2, 12, textureX, textureY); // Box 0
		baseModel[5] = new ModelRendererTurbo(this, 6, 12, textureX, textureY); // Box 0
		baseModel[6] = new ModelRendererTurbo(this, 36, 0, textureX, textureY); // Box 0
		baseModel[7] = new ModelRendererTurbo(this, 44, 8, textureX, textureY); // Box 0
		baseModel[8] = new ModelRendererTurbo(this, 10, 12, textureX, textureY); // Box 0
		baseModel[9] = new ModelRendererTurbo(this, 10, 14, textureX, textureY); // Box 0
		baseModel[10] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Box 0
		baseModel[11] = new ModelRendererTurbo(this, 24, 16, textureX, textureY); // Box 0
		baseModel[12] = new ModelRendererTurbo(this, 16, 12, textureX, textureY); // Box 0
		baseModel[13] = new ModelRendererTurbo(this, 24, 6, textureX, textureY); // Box 0
		baseModel[14] = new ModelRendererTurbo(this, 24, 4, textureX, textureY); // Box 0
		baseModel[15] = new ModelRendererTurbo(this, 24, 20, textureX, textureY); // Box 0
		baseModel[16] = new ModelRendererTurbo(this, 16, 20, textureX, textureY); // Box 0
		baseModel[17] = new ModelRendererTurbo(this, 32, 20, textureX, textureY); // Box 0
		baseModel[18] = new ModelRendererTurbo(this, 28, 20, textureX, textureY); // Box 0
		baseModel[19] = new ModelRendererTurbo(this, 20, 20, textureX, textureY); // Box 0
		baseModel[20] = new ModelRendererTurbo(this, 12, 16, textureX, textureY); // Box 0
		baseModel[21] = new ModelRendererTurbo(this, 16, 16, textureX, textureY); // Box 0

		baseModel[0].addShapeBox(0F, 0F, 0F, 7, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F); // Box 0
		baseModel[0].setRotationPoint(5F, -11.5F, 7F);

		baseModel[1].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 1, 0, 1), new Coord2D(1, 0, 1, 0), new Coord2D(12, 0, 12, 0), new Coord2D(12, 10, 12, 10), new Coord2D(1, 10, 1, 10), new Coord2D(0, 9, 0, 9)}), 2, 12, 10, 44, 2, ModelRendererTurbo.MR_FRONT, new float[]{8, 2, 11, 10, 11, 2}); // Shape 1
		baseModel[1].setRotationPoint(16F, -3F, 7F);

		baseModel[2].addShapeBox(0F, 0F, 0F, 1, 8, 1, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[2].setRotationPoint(3.5F, -12F, 5.5F);

		baseModel[3].addBox(0F, 0F, 0F, 2, 9, 2, 0F); // Box 0
		baseModel[3].setRotationPoint(0.5F, -12.5F, 5F);

		baseModel[4].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[4].setRotationPoint(2.5F, -11F, 5.5F);

		baseModel[5].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[5].setRotationPoint(2.5F, -6F, 5.5F);

		baseModel[6].addBox(0F, 0F, 0F, 7, 7, 1, 0F); // Box 0
		baseModel[6].setRotationPoint(8F, -11.5F, 4F);

		baseModel[7].addBox(0F, 0F, 0F, 3, 1, 1, 0F); // Box 0
		baseModel[7].setRotationPoint(8F, -10.5F, 3F);

		baseModel[8].addShapeBox(0F, 0F, 0F, 2, 1, 1, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[8].setRotationPoint(9F, -12.5F, 4F);

		baseModel[9].addShapeBox(0F, 0F, 0F, 2, 1, 1, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[9].setRotationPoint(12F, -12.5F, 4F);

		baseModel[10].addBox(0F, 0F, 0F, 3, 9, 3, 0F); // Box 0
		baseModel[10].setRotationPoint(5F, -13.5F, 2F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[11].setRotationPoint(5F, -14.5F, 2F);

		baseModel[12].addShapeBox(0F, 0F, 0F, 7, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F); // Box 0
		baseModel[12].setRotationPoint(5F, -7.5F, 7F);

		baseModel[13].addBox(0F, 0F, 0F, 3, 1, 1, 0F); // Box 0
		baseModel[13].setRotationPoint(8F, -8.5F, 3F);

		baseModel[14].addBox(0F, 0F, 0F, 5, 1, 1, 0F); // Box 0
		baseModel[14].setRotationPoint(8F, -6.5F, 3F);

		baseModel[15].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[15].setRotationPoint(13F, -6.5F, 3F);

		baseModel[16].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 0
		baseModel[16].setRotationPoint(13F, -9.5F, 3F);

		baseModel[17].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[17].setRotationPoint(13F, -10.5F, 3F);

		baseModel[18].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[18].setRotationPoint(11F, -10.5F, 3F);

		baseModel[19].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[19].setRotationPoint(11F, -8.5F, 3F);

		baseModel[20].addBox(0F, 0F, 0F, 1, 9, 1, 0F); // Box 0
		baseModel[20].setRotationPoint(6F, -23.5F, 3F);

		baseModel[21].addShapeBox(0F, -2F, 0F, 2, 2, 2, 0F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F); // Box 0
		baseModel[21].setRotationPoint(5.5F, -23.25F, 2.5F);


		sliderModel1 = new ModelRendererTurbo[1];
		sliderModel1[0] = new ModelRendererTurbo(this, 28, 0, textureX, textureY); // Box 0

		sliderModel1[0].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 0
		sliderModel1[0].setRotationPoint(10F, -11.5F, 7.5F);


		sliderModel2 = new ModelRendererTurbo[1];
		sliderModel2[0] = new ModelRendererTurbo(this, 24, 0, textureX, textureY); // Box 0

		sliderModel2[0].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 0
		sliderModel2[0].setRotationPoint(10F, -7.5F, 7.5F);

		parts.put("base", baseModel);
		parts.put("sliderModel1", sliderModel1);
		parts.put("sliderModel2", sliderModel2);
		flipAll();
	}
}
