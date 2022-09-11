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
public class ModelAdvancedRadioTuner extends ModelIIBase
{
	public ModelRendererTurbo[] gaugeModel1, gaugeModel2, gaugeModel3;
	int textureX = 64;
	int textureY = 32;

	public ModelAdvancedRadioTuner() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[28];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 1
		baseModel[1] = new ModelRendererTurbo(this, 60, 0, textureX, textureY); // Box 0
		baseModel[2] = new ModelRendererTurbo(this, 52, 0, textureX, textureY); // Box 0
		baseModel[3] = new ModelRendererTurbo(this, 2, 12, textureX, textureY); // Box 0
		baseModel[4] = new ModelRendererTurbo(this, 6, 12, textureX, textureY); // Box 0
		baseModel[5] = new ModelRendererTurbo(this, 36, 0, textureX, textureY); // Box 0
		baseModel[6] = new ModelRendererTurbo(this, 44, 8, textureX, textureY); // Box 0
		baseModel[7] = new ModelRendererTurbo(this, 10, 12, textureX, textureY); // Box 0
		baseModel[8] = new ModelRendererTurbo(this, 10, 14, textureX, textureY); // Box 0
		baseModel[9] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Box 0
		baseModel[10] = new ModelRendererTurbo(this, 24, 16, textureX, textureY); // Box 0
		baseModel[11] = new ModelRendererTurbo(this, 24, 6, textureX, textureY); // Box 0
		baseModel[12] = new ModelRendererTurbo(this, 24, 20, textureX, textureY); // Box 0
		baseModel[13] = new ModelRendererTurbo(this, 16, 20, textureX, textureY); // Box 0
		baseModel[14] = new ModelRendererTurbo(this, 32, 20, textureX, textureY); // Box 0
		baseModel[15] = new ModelRendererTurbo(this, 28, 20, textureX, textureY); // Box 0
		baseModel[16] = new ModelRendererTurbo(this, 20, 20, textureX, textureY); // Box 0
		baseModel[17] = new ModelRendererTurbo(this, 12, 16, textureX, textureY); // Box 0
		baseModel[18] = new ModelRendererTurbo(this, 16, 16, textureX, textureY); // Box 0
		baseModel[19] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Box 0
		baseModel[20] = new ModelRendererTurbo(this, 24, 16, textureX, textureY); // Box 0
		baseModel[21] = new ModelRendererTurbo(this, 12, 16, textureX, textureY); // Box 0
		baseModel[22] = new ModelRendererTurbo(this, 16, 16, textureX, textureY); // Box 0
		baseModel[23] = new ModelRendererTurbo(this, 24, 20, textureX, textureY); // Box 0
		baseModel[24] = new ModelRendererTurbo(this, 44, 10, textureX, textureY); // Box 0
		baseModel[25] = new ModelRendererTurbo(this, 28, 20, textureX, textureY); // Box 0
		baseModel[26] = new ModelRendererTurbo(this, 37, 12, textureX, textureY); // Box 0
		baseModel[27] = new ModelRendererTurbo(this, 47, 12, textureX, textureY); // Box 0

		baseModel[0].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 1, 0, 1), new Coord2D(1, 0, 1, 0), new Coord2D(12, 0, 12, 0), new Coord2D(12, 10, 12, 10), new Coord2D(1, 10, 1, 10), new Coord2D(0, 9, 0, 9)}), 2, 12, 10, 44, 2, ModelRendererTurbo.MR_FRONT, new float[]{8, 2, 11, 10, 11, 2}); // Shape 1
		baseModel[0].setRotationPoint(16F, -3F, 7F);

		baseModel[1].addShapeBox(0F, 0F, 0F, 1, 8, 1, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[1].setRotationPoint(3.5F, -12F, 5.5F);

		baseModel[2].addBox(0F, 0F, 0F, 2, 9, 2, 0F); // Box 0
		baseModel[2].setRotationPoint(0.5F, -12.5F, 5F);

		baseModel[3].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[3].setRotationPoint(2.5F, -11F, 5.5F);

		baseModel[4].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[4].setRotationPoint(2.5F, -6F, 5.5F);

		baseModel[5].addBox(0F, 0F, 0F, 7, 7, 1, 0F); // Box 0
		baseModel[5].setRotationPoint(7F, -11.5F, 4F);

		baseModel[6].addBox(0F, 0F, 0F, 3, 1, 1, 0F); // Box 0
		baseModel[6].setRotationPoint(7F, -10.5F, 3F);

		baseModel[7].addShapeBox(0F, 0F, 0F, 2, 1, 1, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[7].setRotationPoint(8F, -12.5F, 4F);

		baseModel[8].addShapeBox(0F, 0F, 0F, 2, 1, 1, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[8].setRotationPoint(11F, -12.5F, 4F);

		baseModel[9].addBox(0F, 0F, 0F, 3, 9, 3, 0F); // Box 0
		baseModel[9].setRotationPoint(4F, -13.5F, 2F);

		baseModel[10].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[10].setRotationPoint(4F, -14.5F, 2F);

		baseModel[11].addBox(0F, 0F, 0F, 3, 1, 1, 0F); // Box 0
		baseModel[11].setRotationPoint(7F, -8.5F, 3F);

		baseModel[12].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F); // Box 0
		baseModel[12].setRotationPoint(12F, -6.5F, 3F);

		baseModel[13].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 0
		baseModel[13].setRotationPoint(12F, -9.5F, 3F);

		baseModel[14].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[14].setRotationPoint(12F, -10.5F, 3F);

		baseModel[15].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[15].setRotationPoint(10F, -10.5F, 3F);

		baseModel[16].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[16].setRotationPoint(10F, -8.5F, 3F);

		baseModel[17].addBox(0F, 0F, 0F, 1, 9, 1, 0F); // Box 0
		baseModel[17].setRotationPoint(5F, -23.5F, 3F);

		baseModel[18].addShapeBox(0F, -2F, 0F, 2, 2, 2, 0F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F); // Box 0
		baseModel[18].setRotationPoint(4.5F, -23.25F, 2.5F);

		baseModel[19].addBox(0F, 0F, 0F, 3, 9, 3, 0F); // Box 0
		baseModel[19].setRotationPoint(14F, -13.5F, 2F);

		baseModel[20].addShapeBox(0F, 0F, 0F, 3, 1, 3, 0F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[20].setRotationPoint(14F, -14.5F, 2F);

		baseModel[21].addBox(0F, 0F, 0F, 1, 9, 1, 0F); // Box 0
		baseModel[21].setRotationPoint(15F, -20.5F, 3F);

		baseModel[22].addShapeBox(0F, -2F, 0F, 2, 2, 2, 0F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F, -0.25F); // Box 0
		baseModel[22].setRotationPoint(14.5F, -20.25F, 2.5F);

		baseModel[23].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[23].setRotationPoint(13F, -6.5F, 3F);

		baseModel[24].addBox(0F, 0F, 0F, 3, 1, 1, 0F); // Box 0
		baseModel[24].setRotationPoint(7F, -6.5F, 3F);

		baseModel[25].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[25].setRotationPoint(10F, -6.5F, 3F);

		baseModel[26].addShapeBox(0F, 0F, 0F, 4, 6, 1, 0F, -1F, -1.5F, -0.25F, -1F, -1.5F, -0.25F, -1F, -1.5F, -0.25F, -1F, -1.5F, -0.25F, -1F, -1.5F, -0.25F, -1F, -1.5F, -0.25F, -1F, -1.5F, -0.25F, -1F, -1.5F, -0.25F); // Box 0
		baseModel[26].setRotationPoint(15F, -13F, 5F);

		baseModel[27].addShapeBox(0F, 0F, 0F, 4, 6, 1, 0F, -1F, -1.5F, -0.25F, -1F, -1.5F, -0.25F, -1F, -1.5F, -0.25F, -1F, -1.5F, -0.25F, -1F, -1.5F, -0.25F, -1F, -1.5F, -0.25F, -1F, -1.5F, -0.25F, -1F, -1.5F, -0.25F); // Box 0
		baseModel[27].setRotationPoint(15F, -9F, 5F);


		gaugeModel1 = new ModelRendererTurbo[1];
		gaugeModel1[0] = new ModelRendererTurbo(this, 16, 12, textureX, textureY); // Box 0
		gaugeModel1[0].addShapeBox(-1.5F, -1.5F, 0F, 3, 3, 1, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F); // Box 0

		gaugeModel2 = new ModelRendererTurbo[1];
		gaugeModel2[0] = new ModelRendererTurbo(this, 16, 12, textureX, textureY); // Box 0
		gaugeModel2[0].addShapeBox(-1.5F, -1.5F, 0F, 3, 3, 1, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F); // Box 0


		gaugeModel3 = new ModelRendererTurbo[1];
		gaugeModel3[0] = new ModelRendererTurbo(this, 16, 12, textureX, textureY); // Box 0
		gaugeModel3[0].addShapeBox(-1.5F, -1.5F, 0F, 3, 3, 1, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F); // Box 0

		parts.put("base", baseModel);
		parts.put("gaugeModel1", gaugeModel1);
		parts.put("gaugeModel2", gaugeModel2);
		parts.put("gaugeModel3", gaugeModel3);
		flipAll();
	}
}
