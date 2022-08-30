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
public class ModelTachometer extends ModelIIBase
{
	int textureX = 64;
	int textureY = 16;
	public ModelRendererTurbo[] gaugeModel;

	public ModelTachometer() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[13];
		baseModel[0] = new ModelRendererTurbo(this, 20, 0, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 1
		baseModel[2] = new ModelRendererTurbo(this, 60, 0, textureX, textureY); // Box 0
		baseModel[3] = new ModelRendererTurbo(this, 52, 0, textureX, textureY); // Box 0
		baseModel[4] = new ModelRendererTurbo(this, 2, 12, textureX, textureY); // Box 0
		baseModel[5] = new ModelRendererTurbo(this, 6, 12, textureX, textureY); // Box 0
		baseModel[6] = new ModelRendererTurbo(this, 36, 0, textureX, textureY); // Box 0
		baseModel[7] = new ModelRendererTurbo(this, 40, 8, textureX, textureY); // Box 0
		baseModel[8] = new ModelRendererTurbo(this, 38, 12, textureX, textureY); // Box 0
		baseModel[9] = new ModelRendererTurbo(this, 44, 12, textureX, textureY); // Box 0
		baseModel[10] = new ModelRendererTurbo(this, 16, 12, textureX, textureY); // Box 0
		baseModel[11] = new ModelRendererTurbo(this, 10, 12, textureX, textureY); // Box 0
		baseModel[12] = new ModelRendererTurbo(this, 10, 14, textureX, textureY); // Box 0

		baseModel[0].addBox(0F, 0F, 0F, 7, 7, 1, 0F); // Box 0
		baseModel[0].setRotationPoint(5F, -11.5F, 7F);

		baseModel[1].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 1, 0, 1), new Coord2D(1, 0, 1, 0), new Coord2D(10, 0, 10, 0), new Coord2D(10, 10, 10, 10), new Coord2D(1, 10, 1, 10), new Coord2D(0, 9, 0, 9)}), 2, 10, 10, 40, 2, ModelRendererTurbo.MR_FRONT, new float[]{8, 2, 9, 10, 9, 2}); // Shape 1
		baseModel[1].setRotationPoint(14F, -3F, 7F);

		baseModel[2].addShapeBox(0F, 0F, 0F, 1, 8, 1, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[2].setRotationPoint(3.5F, -12F, 5.5F);

		baseModel[3].addBox(0F, 0F, 0F, 2, 9, 2, 0F); // Box 0
		baseModel[3].setRotationPoint(0.5F, -12.5F, 5F);

		baseModel[4].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[4].setRotationPoint(2.5F, -11F, 5.5F);

		baseModel[5].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[5].setRotationPoint(2.5F, -6F, 5.5F);

		baseModel[6].addBox(0F, 0F, 0F, 7, 7, 1, 0F); // Box 0
		baseModel[6].setRotationPoint(5F, -11.5F, 4F);

		baseModel[7].addBox(0F, 0F, 0F, 3, 2, 2, 0F); // Box 0
		baseModel[7].setRotationPoint(7F, -9.5F, 2F);

		baseModel[8].addBox(0F, 0F, 0F, 1, 2, 2, 0F); // Box 0
		baseModel[8].setRotationPoint(5F, -9.5F, 2F);

		baseModel[9].addBox(0F, 0F, 0F, 1, 2, 2, 0F); // Box 0
		baseModel[9].setRotationPoint(11F, -9.5F, 2F);

		baseModel[10].addBox(0F, 0F, 0F, 11, 1, 1, 0F); // Box 0
		baseModel[10].setRotationPoint(3F, -9F, 2.5F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 2, 1, 1, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[11].setRotationPoint(6F, -12.5F, 4F);

		baseModel[12].addShapeBox(0F, 0F, 0F, 2, 1, 1, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[12].setRotationPoint(9F, -12.5F, 4F);

		gaugeModel = new ModelRendererTurbo[1];
		gaugeModel[0] = new ModelRendererTurbo(this, 52, 11, textureX, textureY); // Box 0

		gaugeModel[0].addBox(-0.5F, -0.5F, 0F, 4, 1, 1, 0F); // Box 0
		gaugeModel[0].setRotationPoint(8.5F, -7.5F, 7.5F);
		gaugeModel[0].rotateAngleZ = -0.76794487F;

		parts.put("base", baseModel);
		parts.put("gauge", gaugeModel);
		flipAll();
	}
}
