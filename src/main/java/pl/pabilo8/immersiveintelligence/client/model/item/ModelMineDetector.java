package pl.pabilo8.immersiveintelligence.client.model.item;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * @author Pabilo8
 * @since 28.01.2021
 */
public class ModelMineDetector extends ModelIIBase
{
	int textureX = 32;
	int textureY = 32;

	public ModelRendererTurbo[] poleModel;

	public ModelMineDetector() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[8];
		baseModel[0] = new ModelRendererTurbo(this, 4, 25, textureX, textureY); // Box 4
		baseModel[1] = new ModelRendererTurbo(this, 16, 13, textureX, textureY); // Box 9
		baseModel[2] = new ModelRendererTurbo(this, 13, 20, textureX, textureY); // Box 14
		baseModel[3] = new ModelRendererTurbo(this, 4, 4, textureX, textureY); // Shape 20
		baseModel[4] = new ModelRendererTurbo(this, 13, 20, textureX, textureY); // Box 14
		baseModel[5] = new ModelRendererTurbo(this, 18, 23, textureX, textureY); // Box 14
		baseModel[6] = new ModelRendererTurbo(this, 24, 25, textureX, textureY); // Box 14
		baseModel[7] = new ModelRendererTurbo(this, 4, 4, textureX, textureY); // Shape 20

		baseModel[0].addShapeBox(0F, 0F, 0F, 7, 1, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		baseModel[0].setRotationPoint(-21.5F, 6F, -2.5F);

		baseModel[1].addShapeBox(0F, 0F, 0F, 4, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 9
		baseModel[1].setRotationPoint(-19F, 5F, -1F);

		baseModel[2].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 14
		baseModel[2].setRotationPoint(-21F, 5F, -2F);

		baseModel[3].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(5, 0, 5, 0), new Coord2D(6, 1, 6, 1), new Coord2D(6, 7, 6, 7), new Coord2D(5, 8, 5, 8), new Coord2D(0, 8, 0, 8)}), 1, 6, 8, 28, 1, ModelRendererTurbo.MR_FRONT, new float[]{8, 5, 2, 6, 2, 5}); // Shape 20
		baseModel[3].setRotationPoint(-18F, 7F, 4.5F);
		baseModel[3].rotateAngleX = 1.57079633F;

		baseModel[4].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 14
		baseModel[4].setRotationPoint(-21F, 5F, 1F);

		baseModel[5].addShapeBox(1F, 0F, 0F, 3, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 14
		baseModel[5].setRotationPoint(-20.5F, 4F, -1.5F);

		baseModel[6].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 14
		baseModel[6].setRotationPoint(-20.5F, 4F, -1.5F);

		baseModel[7].addShape3D(0F, -8F, -2F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(5, 0, 5, 0), new Coord2D(6, 1, 6, 1), new Coord2D(6, 7, 6, 7), new Coord2D(5, 8, 5, 8), new Coord2D(0, 8, 0, 8)}), 1, 6, 8, 28, 1, ModelRendererTurbo.MR_FRONT, new float[]{8, 5, 2, 6, 2, 5}); // Shape 20
		baseModel[7].setRotationPoint(-18F, 9F, 4.5F);
		baseModel[7].rotateAngleX = 1.57079633F;
		baseModel[7].rotateAngleY = -3.14159265F;

		poleModel = new ModelRendererTurbo[10];
		poleModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 1
		poleModel[1] = new ModelRendererTurbo(this, 4, 25, textureX, textureY); // Box 16
		poleModel[2] = new ModelRendererTurbo(this, 28, 27, textureX, textureY); // Box 17
		poleModel[3] = new ModelRendererTurbo(this, 28, 25, textureX, textureY); // Box 18
		poleModel[4] = new ModelRendererTurbo(this, 19, 18, textureX, textureY); // Box 19
		poleModel[5] = new ModelRendererTurbo(this, 4, 18, textureX, textureY); // Box 21
		poleModel[6] = new ModelRendererTurbo(this, 24, 27, textureX, textureY); // Box 14
		poleModel[7] = new ModelRendererTurbo(this, 4, 13, textureX, textureY); // Box 14
		poleModel[8] = new ModelRendererTurbo(this, 4, 13, textureX, textureY); // Box 21
		poleModel[9] = new ModelRendererTurbo(this, 4, 13, textureX, textureY); // Box 14

		poleModel[0].addShapeBox(0F, -30F, 0F, 1, 31, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		poleModel[0].setRotationPoint(-17F, 5F, 0F);
		poleModel[0].rotateAngleZ = -0.95993109F;

		poleModel[1].addShapeBox(-2F, -30F, 0F, 1, 4, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 16
		poleModel[1].setRotationPoint(-17F, 5F, -0.5F);
		poleModel[1].rotateAngleZ = -0.95993109F;

		poleModel[2].addShapeBox(-1F, -27.2F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 17
		poleModel[2].setRotationPoint(-17F, 5F, 0F);
		poleModel[2].rotateAngleZ = -0.95993109F;

		poleModel[3].addShapeBox(-1F, -29.8F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 18
		poleModel[3].setRotationPoint(-17F, 5F, 0F);
		poleModel[3].rotateAngleZ = -0.95993109F;

		poleModel[4].addShapeBox(0F, -31F, 0F, 4, 2, 2, 0F, 0.875F, -1F, 0F, -2F, 3F, 0F, -2F, 3F, 0F, 0.875F, -1F, 0F, -1F, 0F, 0F, -0.25F, -3.875F, 0F, -0.25F, -3.875F, 0F, -1F, 0F, 0F); // Box 19
		poleModel[4].setRotationPoint(-17F, 5F, -0.5F);
		poleModel[4].rotateAngleZ = -0.95993109F;

		poleModel[5].addShapeBox(-2F, -4F, 0F, 2, 2, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 21
		poleModel[5].setRotationPoint(-17F, 5F, -3F);
		poleModel[5].rotateAngleZ = -0.95993109F;

		poleModel[6].addShapeBox(0F, -3F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 14
		poleModel[6].setRotationPoint(-17F, 5F, -1.5F);
		poleModel[6].rotateAngleZ = -0.95993109F;

		poleModel[7].addShapeBox(0F, -2F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0.5F, 0F, 0F); // Box 14
		poleModel[7].setRotationPoint(-17F, 5F, -1.5F);
		poleModel[7].rotateAngleZ = -0.95993109F;

		poleModel[8].addShapeBox(-6F, -3F, 0F, 4, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 21
		poleModel[8].setRotationPoint(-17F, 5F, -3F);
		poleModel[8].rotateAngleZ = -0.95993109F;

		poleModel[9].addShapeBox(-0.5F, -1F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, 0F, 0F, 0F); // Box 14
		poleModel[9].setRotationPoint(-17F, 5F, -1.5F);
		poleModel[9].rotateAngleZ = -0.95993109F;

		parts.put("base", baseModel);
		parts.put("pole", poleModel);
		translateAll(17,0,-0.5f);

		flipAll();
	}
}
