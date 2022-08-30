package pl.pabilo8.immersiveintelligence.client.model.misc;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * @author Pabilo8
 * @since 17.05.2021
 */
public class ModelParachute extends ModelIIBase
{
	int textureX = 128;
	int textureY = 64;

	public ModelRendererTurbo[] linkThingyModel;

	public ModelParachute() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[24];
		baseModel[0] = new ModelRendererTurbo(this, 78, 46, textureX, textureY); // Box 30
		baseModel[1] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Shape 26
		baseModel[2] = new ModelRendererTurbo(this, 78, 46, textureX, textureY); // Box 30
		baseModel[3] = new ModelRendererTurbo(this, 78, 46, textureX, textureY); // Box 30
		baseModel[4] = new ModelRendererTurbo(this, 78, 46, textureX, textureY); // Box 30
		baseModel[5] = new ModelRendererTurbo(this, 80, 13, textureX, textureY); // Box 30
		baseModel[6] = new ModelRendererTurbo(this, 80, 13, textureX, textureY); // Box 30
		baseModel[7] = new ModelRendererTurbo(this, 80, 13, textureX, textureY); // Box 30
		baseModel[8] = new ModelRendererTurbo(this, 80, 13, textureX, textureY); // Box 30
		baseModel[9] = new ModelRendererTurbo(this, 0, 30, textureX, textureY); // Box 30
		baseModel[10] = new ModelRendererTurbo(this, 0, 30, textureX, textureY); // Box 30
		baseModel[11] = new ModelRendererTurbo(this, 0, 30, textureX, textureY); // Box 30
		baseModel[12] = new ModelRendererTurbo(this, 0, 30, textureX, textureY); // Box 30
		baseModel[13] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Shape 26
		baseModel[14] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Shape 26
		baseModel[15] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Shape 26
		baseModel[16] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 26
		baseModel[17] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 26
		baseModel[18] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 26
		baseModel[19] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Shape 26
		baseModel[20] = new ModelRendererTurbo(this, 0, 47, textureX, textureY); // Shape 26
		baseModel[21] = new ModelRendererTurbo(this, 0, 47, textureX, textureY); // Shape 26
		baseModel[22] = new ModelRendererTurbo(this, 0, 47, textureX, textureY); // Shape 26
		baseModel[23] = new ModelRendererTurbo(this, 0, 47, textureX, textureY); // Shape 26

		baseModel[0].addShapeBox(0F, 0F, 0F, 16, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 30
		baseModel[0].setRotationPoint(8F, -120F, 27F);
		baseModel[0].rotateAngleX = 1.57079633F;
		baseModel[0].rotateAngleY = -3.14159265F;

		baseModel[1].setFlipped(true);
		baseModel[1].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 8, 0, 8), new Coord2D(8, 16, 8, 16), new Coord2D(16, 16, 16, 16), new Coord2D(16, 0, 16, 0), new Coord2D(0, 0, 0, 0) }), 1, 16, 16, 60, 1, ModelRendererTurbo.MR_FRONT, new float[] {8 ,16 ,16 ,8 ,12}); // Shape 26
		baseModel[1].setRotationPoint(-8F, -120F, 27F);
		baseModel[1].rotateAngleX = -1.57079633F;
		baseModel[1].rotateAngleY = 1.57079633F;

		baseModel[2].addShapeBox(0F, 0F, 0F, 16, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 30
		baseModel[2].setRotationPoint(24F, -120F, -5F);
		baseModel[2].rotateAngleX = 1.57079633F;
		baseModel[2].rotateAngleY = -4.71238898F;

		baseModel[3].addShapeBox(0F, 0F, 0F, 16, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 30
		baseModel[3].setRotationPoint(-24F, -120F, 11F);
		baseModel[3].rotateAngleX = 1.57079633F;
		baseModel[3].rotateAngleY = -1.57079633F;

		baseModel[4].addShapeBox(0F, 0F, 0F, 16, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 30
		baseModel[4].setRotationPoint(-8F, -120F, -21F);
		baseModel[4].rotateAngleX = 1.57079633F;

		baseModel[5].addShapeBox(0F, -24F, 0F, 16, 32, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 30
		baseModel[5].setRotationPoint(-8F, -121F, -21F);
		baseModel[5].rotateAngleX = -1.57079633F;
		baseModel[5].rotateAngleY = -1.57079633F;
		baseModel[5].rotateAngleZ = -0.61086524F;

		baseModel[6].addShapeBox(0F, -24F, 0F, 16, 32, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 30
		baseModel[6].setRotationPoint(-24F, -121F, 11F);
		baseModel[6].rotateAngleX = -1.57079633F;
		baseModel[6].rotateAngleY = -3.14159265F;
		baseModel[6].rotateAngleZ = -0.61086524F;

		baseModel[7].addShapeBox(0F, -24F, 0F, 16, 32, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 30
		baseModel[7].setRotationPoint(8F, -121F, 27F);
		baseModel[7].rotateAngleX = -1.57079633F;
		baseModel[7].rotateAngleY = -4.71238898F;
		baseModel[7].rotateAngleZ = -0.61086524F;

		baseModel[8].addShapeBox(0F, -24F, 0F, 16, 32, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 30
		baseModel[8].setRotationPoint(24F, -121F, -5F);
		baseModel[8].rotateAngleX = -1.57079633F;
		baseModel[8].rotateAngleY = -6.28318531F;
		baseModel[8].rotateAngleZ = -0.61086524F;

		baseModel[9].addShapeBox(-8F, -12.62F, -19.9F, 32, 1, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 30
		baseModel[9].setRotationPoint(-8F, -110F, -21F);
		baseModel[9].rotateAngleX = 1.13446401F;

		baseModel[10].addShapeBox(-8F, -12.62F, -19.9F, 32, 1, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 30
		baseModel[10].setRotationPoint(-24F, -110F, 11F);
		baseModel[10].rotateAngleX = 1.13446401F;
		baseModel[10].rotateAngleY = -1.57079633F;

		baseModel[11].addShapeBox(-8F, -12.62F, -19.9F, 32, 1, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 30
		baseModel[11].setRotationPoint(8F, -110F, 27F);
		baseModel[11].rotateAngleX = 1.13446401F;
		baseModel[11].rotateAngleY = -3.14159265F;

		baseModel[12].addShapeBox(-8F, -12.62F, -19.9F, 32, 1, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 30
		baseModel[12].setRotationPoint(24F, -110F, -5F);
		baseModel[12].rotateAngleX = 1.13446401F;
		baseModel[12].rotateAngleY = -4.71238898F;

		baseModel[13].setFlipped(true);
		baseModel[13].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 8, 0, 8), new Coord2D(8, 16, 8, 16), new Coord2D(16, 16, 16, 16), new Coord2D(16, 0, 16, 0), new Coord2D(0, 0, 0, 0) }), 1, 16, 16, 60, 1, ModelRendererTurbo.MR_FRONT, new float[] {8 ,16 ,16 ,8 ,12}); // Shape 26
		baseModel[13].setRotationPoint(24F, -120F, 11F);
		baseModel[13].rotateAngleX = -1.57079633F;

		baseModel[14].setFlipped(true);
		baseModel[14].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 8, 0, 8), new Coord2D(8, 16, 8, 16), new Coord2D(16, 16, 16, 16), new Coord2D(16, 0, 16, 0), new Coord2D(0, 0, 0, 0) }), 1, 16, 16, 60, 1, ModelRendererTurbo.MR_FRONT, new float[] {8 ,16 ,16 ,8 ,12}); // Shape 26
		baseModel[14].setRotationPoint(8F, -120F, -21F);
		baseModel[14].rotateAngleX = -1.57079633F;
		baseModel[14].rotateAngleY = -1.57079633F;

		baseModel[15].setFlipped(true);
		baseModel[15].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 8, 0, 8), new Coord2D(8, 16, 8, 16), new Coord2D(16, 16, 16, 16), new Coord2D(16, 0, 16, 0), new Coord2D(0, 0, 0, 0) }), 1, 16, 16, 60, 1, ModelRendererTurbo.MR_FRONT, new float[] {8 ,16 ,16 ,8 ,12}); // Shape 26
		baseModel[15].setRotationPoint(-24F, -120F, -5F);
		baseModel[15].rotateAngleX = -1.57079633F;
		baseModel[15].rotateAngleY = 3.14159265F;

		baseModel[16].setFlipped(true);
		baseModel[16].addShape3D(0F, 0F, -1.12F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(9, 13, 9, 13), new Coord2D(21, 13, 21, 13), new Coord2D(30, 0, 30, 0) }), 1, 30, 13, 74, 1, ModelRendererTurbo.MR_FRONT, new float[] {30 ,16 ,12 ,16}); // Shape 26
		baseModel[16].setRotationPoint(37F, -112F, -13F);
		baseModel[16].rotateAngleX = -0.78539816F;
		baseModel[16].rotateAngleY = 0.78539816F;

		baseModel[17].setFlipped(true);
		baseModel[17].addShape3D(0F, 0F, -1.12F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(9, 13, 9, 13), new Coord2D(21, 13, 21, 13), new Coord2D(30, 0, 30, 0) }), 1, 30, 13, 74, 1, ModelRendererTurbo.MR_FRONT, new float[] {30 ,16 ,12 ,16}); // Shape 26
		baseModel[17].setRotationPoint(16F, -112F, 40F);
		baseModel[17].rotateAngleX = -0.78539816F;
		baseModel[17].rotateAngleY = 2.35619449F;

		baseModel[18].setFlipped(true);
		baseModel[18].addShape3D(0F, 0F, -1.12F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(9, 13, 9, 13), new Coord2D(21, 13, 21, 13), new Coord2D(30, 0, 30, 0) }), 1, 30, 13, 74, 1, ModelRendererTurbo.MR_FRONT, new float[] {30 ,16 ,12 ,16}); // Shape 26
		baseModel[18].setRotationPoint(-37F, -112F, 19F);
		baseModel[18].rotateAngleX = -0.78539816F;
		baseModel[18].rotateAngleY = -2.35619449F;

		baseModel[19].setFlipped(true);
		baseModel[19].addShape3D(0F, 0F, -1.12F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(9, 13, 9, 13), new Coord2D(21, 13, 21, 13), new Coord2D(30, 0, 30, 0) }), 1, 30, 13, 74, 1, ModelRendererTurbo.MR_FRONT, new float[] {30 ,16 ,12 ,16}); // Shape 26
		baseModel[19].setRotationPoint(-16F, -112F, -34F);
		baseModel[19].rotateAngleX = -0.78539816F;
		baseModel[19].rotateAngleY = -0.78539816F;

		baseModel[20].setFlipped(true);
		baseModel[20].addShape3D(0F, 0F, 4.5F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(4, 16, 4, 16), new Coord2D(34, 16, 34, 16), new Coord2D(39, 0, 39, 0) }), 1, 39, 16, 103, 1, ModelRendererTurbo.MR_FRONT, new float[] {39 ,17 ,30 ,17}); // Shape 26
		baseModel[20].setRotationPoint(40F, -95F, -10F);
		baseModel[20].rotateAngleX = -0.31415927F;
		baseModel[20].rotateAngleY = -5.49778714F;

		baseModel[21].setFlipped(true);
		baseModel[21].addShape3D(0F, 0F, 4.5F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(4, 16, 4, 16), new Coord2D(34, 16, 34, 16), new Coord2D(39, 0, 39, 0) }), 1, 39, 16, 103, 1, ModelRendererTurbo.MR_FRONT, new float[] {39 ,17 ,30 ,17}); // Shape 26
		baseModel[21].setRotationPoint(13F, -95F, 43F);
		baseModel[21].rotateAngleX = -0.31415927F;
		baseModel[21].rotateAngleY = -3.92699082F;

		baseModel[22].setFlipped(true);
		baseModel[22].addShape3D(0F, 0F, 4.5F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(4, 16, 4, 16), new Coord2D(34, 16, 34, 16), new Coord2D(39, 0, 39, 0) }), 1, 39, 16, 103, 1, ModelRendererTurbo.MR_FRONT, new float[] {39 ,17 ,30 ,17}); // Shape 26
		baseModel[22].setRotationPoint(-13F, -95F, -37F);
		baseModel[22].rotateAngleX = -0.31415927F;
		baseModel[22].rotateAngleY = -0.78539816F;

		baseModel[23].setFlipped(true);
		baseModel[23].addShape3D(0F, 0F, 4.5F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(4, 16, 4, 16), new Coord2D(34, 16, 34, 16), new Coord2D(39, 0, 39, 0) }), 1, 39, 16, 103, 1, ModelRendererTurbo.MR_FRONT, new float[] {39 ,17 ,30 ,17}); // Shape 26
		baseModel[23].setRotationPoint(-40F, -95F, 16F);
		baseModel[23].rotateAngleX = -0.31415927F;
		baseModel[23].rotateAngleY = -2.37364778F;


		linkThingyModel = new ModelRendererTurbo[2];
		linkThingyModel[0] = new ModelRendererTurbo(this, 32, 25, textureX, textureY); // Box 29
		linkThingyModel[1] = new ModelRendererTurbo(this, 32, 25, textureX, textureY); // Box 30

		linkThingyModel[0].addShapeBox(0F, 0F, 0F, 2, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 29
		linkThingyModel[0].setRotationPoint(-8F, -31F, -2F);

		linkThingyModel[1].addShapeBox(0F, 0F, 0F, 2, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 30
		linkThingyModel[1].setRotationPoint(6F, -31F, -2F);


		parts.put("base",baseModel);
		parts.put("linkThingy",linkThingyModel);

		flipAll();
	}
}
