package pl.pabilo8.immersiveintelligence.client.model.metal_device;

import pl.pabilo8.immersiveintelligence.client.model.ModelBlockBase;
import pl.pabilo8.immersiveintelligence.client.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.tmt.Shape2D;

/**
 * @author Pabilo8
 * @since 04.09.2020
 */
public class ModelMediCrate extends ModelBlockBase
{
	int textureX = 64;
	int textureY = 32;

	public ModelRendererTurbo[] lidModel;

	public ModelMediCrate() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[26];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // MainBox
		baseModel[1] = new ModelRendererTurbo(this, 16, 18, textureX, textureY); // BorderBox
		baseModel[2] = new ModelRendererTurbo(this, 16, 18, textureX, textureY); // BorderBox
		baseModel[3] = new ModelRendererTurbo(this, 40, 1, textureX, textureY); // BorderBox
		baseModel[4] = new ModelRendererTurbo(this, 40, 1, textureX, textureY); // BorderBox
		baseModel[5] = new ModelRendererTurbo(this, 48, 0, textureX, textureY); // Holder
		baseModel[6] = new ModelRendererTurbo(this, 48, 0, textureX, textureY); // Holder
		baseModel[7] = new ModelRendererTurbo(this, 40, 2, textureX, textureY); // Holder2
		baseModel[8] = new ModelRendererTurbo(this, 40, 0, textureX, textureY); // Holder2
		baseModel[9] = new ModelRendererTurbo(this, 40, 0, textureX, textureY); // Holder2
		baseModel[10] = new ModelRendererTurbo(this, 40, 2, textureX, textureY); // Holder2
		baseModel[11] = new ModelRendererTurbo(this, 48, 2, textureX, textureY); // Shape 13
		baseModel[12] = new ModelRendererTurbo(this, 48, 2, textureX, textureY); // Shape 13
		baseModel[13] = new ModelRendererTurbo(this, 6, 18, textureX, textureY); // MainBox
		baseModel[14] = new ModelRendererTurbo(this, 6, 18, textureX, textureY); // MainBox
		baseModel[15] = new ModelRendererTurbo(this, 0, 13, textureX, textureY); // MainBox
		baseModel[16] = new ModelRendererTurbo(this, 30, 12, textureX, textureY); // MainBox
		baseModel[17] = new ModelRendererTurbo(this, 51, 5, textureX, textureY); // Box 19
		baseModel[18] = new ModelRendererTurbo(this, 51, 5, textureX, textureY); // Box 19
		baseModel[19] = new ModelRendererTurbo(this, 50, 17, textureX, textureY); // Box 19
		baseModel[20] = new ModelRendererTurbo(this, 50, 17, textureX, textureY); // Box 19
		baseModel[21] = new ModelRendererTurbo(this, 58, 15, textureX, textureY); // Box 19
		baseModel[22] = new ModelRendererTurbo(this, 2, 18, textureX, textureY); // Box 19
		baseModel[23] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 19
		baseModel[24] = new ModelRendererTurbo(this, 40, 4, textureX, textureY); // Holder2
		baseModel[25] = new ModelRendererTurbo(this, 40, 4, textureX, textureY); // Holder2

		baseModel[0].addBox(0F, 0F, 0F, 16, 4, 8, 0F); // MainBox
		baseModel[0].setRotationPoint(0F, -4F, 4F);

		baseModel[1].addBox(0F, 0F, 0F, 16, 1, 1, 0F); // BorderBox
		baseModel[1].setRotationPoint(0F, -9F, 4F);

		baseModel[2].addBox(0F, 0F, 0F, 16, 1, 1, 0F); // BorderBox
		baseModel[2].setRotationPoint(0F, -9F, 11F);

		baseModel[3].addBox(0F, 0F, 0F, 1, 1, 6, 0F); // BorderBox
		baseModel[3].setRotationPoint(0F, -9F, 5F);

		baseModel[4].addBox(0F, 0F, 0F, 1, 1, 6, 0F); // BorderBox
		baseModel[4].setRotationPoint(15F, -9F, 5F);

		baseModel[5].addBox(0F, 0F, 0F, 6, 1, 1, 0F); // Holder
		baseModel[5].setRotationPoint(5F, -7F, 11.5F);

		baseModel[6].addBox(0F, 0F, 0F, 6, 1, 1, 0F); // Holder
		baseModel[6].setRotationPoint(5F, -7F, 3.5F);

		baseModel[7].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Holder2
		baseModel[7].setRotationPoint(5F, -6F, 3.5F);

		baseModel[8].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Holder2
		baseModel[8].setRotationPoint(10F, -6F, 3.5F);

		baseModel[9].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Holder2
		baseModel[9].setRotationPoint(5F, -6F, 11.5F);

		baseModel[10].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Holder2
		baseModel[10].setRotationPoint(10F, -6F, 11.5F);

		baseModel[11].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(1, 0, 1, 0), new Coord2D(5, 0, 5, 0), new Coord2D(6, 1, 6, 1), new Coord2D(0, 1, 0, 1)}), 1, 6, 1, 14, 1, ModelRendererTurbo.MR_FRONT, new float[]{2, 6, 2, 4}); // Shape 13
		baseModel[11].setRotationPoint(11F, -4F, 12.5F);

		baseModel[12].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(1, 0, 1, 0), new Coord2D(5, 0, 5, 0), new Coord2D(6, 1, 6, 1), new Coord2D(0, 1, 0, 1)}), 1, 6, 1, 14, 1, ModelRendererTurbo.MR_FRONT, new float[]{2, 6, 2, 4}); // Shape 13
		baseModel[12].setRotationPoint(11F, -4F, 4.5F);

		baseModel[13].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // MainBox
		baseModel[13].setRotationPoint(0F, -8F, 4F);

		baseModel[14].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // MainBox
		baseModel[14].setRotationPoint(15F, -8F, 4F);

		baseModel[15].addBox(0F, 0F, 0F, 14, 4, 1, 0F); // MainBox
		baseModel[15].setRotationPoint(1F, -8F, 4F);

		baseModel[16].addBox(0F, 0F, 0F, 14, 4, 1, 0F); // MainBox
		baseModel[16].setRotationPoint(1F, -8F, 11F);

		baseModel[17].addBox(-1.5F, -3F, -1.5F, 3, 3, 3, 0F); // Box 19
		baseModel[17].setRotationPoint(2F, -4F, 6F);
		baseModel[17].rotateAngleX = -0.27925268F;
		baseModel[17].rotateAngleY = -0.57595865F;

		baseModel[18].addBox(-1.5F, -3F, -1.5F, 3, 3, 3, 0F); // Box 19
		baseModel[18].setRotationPoint(3F, -4F, 9.5F);
		baseModel[18].rotateAngleX = -0.01745329F;
		baseModel[18].rotateAngleY = -0.52359878F;

		baseModel[19].addBox(-1.5F, -3F, -1.5F, 1, 3, 3, 0F); // Box 19
		baseModel[19].setRotationPoint(7F, -4F, 8.5F);
		baseModel[19].rotateAngleX = -0.01745329F;
		baseModel[19].rotateAngleY = -0.13962634F;

		baseModel[20].addBox(0.5F, -3F, -1.5F, 1, 3, 3, 0F); // Box 19
		baseModel[20].setRotationPoint(7F, -4F, 8.5F);
		baseModel[20].rotateAngleX = -0.01745329F;
		baseModel[20].rotateAngleY = -0.13962634F;

		baseModel[21].addBox(-0.5F, -2.5F, -1F, 1, 2, 2, 0F); // Box 19
		baseModel[21].setRotationPoint(7F, -4F, 8.5F);
		baseModel[21].rotateAngleX = -0.01745329F;
		baseModel[21].rotateAngleY = -0.13962634F;

		baseModel[22].addBox(1.5F, -2.5F, -1F, 4, 2, 2, 0F); // Box 19
		baseModel[22].setRotationPoint(7F, -4F, 8.5F);
		baseModel[22].rotateAngleX = -0.01745329F;
		baseModel[22].rotateAngleY = -0.13962634F;

		baseModel[23].addBox(5F, -1.5F, -0.5F, 3, 1, 1, 0F); // Box 19
		baseModel[23].setRotationPoint(7F, -4F, 8.5F);
		baseModel[23].rotateAngleX = -0.01745329F;
		baseModel[23].rotateAngleY = -0.13962634F;

		baseModel[24].addBox(0F, 0F, 0F, 1, 1, 2, 0F); // Holder2
		baseModel[24].setRotationPoint(-0.5F, -9F, 9F);

		baseModel[25].addBox(0F, 0F, 0F, 1, 1, 2, 0F); // Holder2
		baseModel[25].setRotationPoint(-0.5F, -9F, 5F);


		lidModel = new ModelRendererTurbo[2];
		lidModel[0] = new ModelRendererTurbo(this, 16, 23, textureX, textureY); // BorderBox
		lidModel[1] = new ModelRendererTurbo(this, 0, 26, textureX, textureY); // BorderBoxHandle

		lidModel[0].addBox(-1F, -1F, -4F, 16, 1, 8, 0F); // BorderBox
		lidModel[1].addBox(14.5F, -0.5F, -2F, 1, 2, 4, 0F); // BorderBoxHandle

		parts.put("base", baseModel);
		parts.put("lid", lidModel);

		flipAll();
	}
}
