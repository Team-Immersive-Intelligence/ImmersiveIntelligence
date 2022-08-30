package pl.pabilo8.immersiveintelligence.client.model.weapon.emplacement;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

public class ModelInfraredObserver extends ModelIIBase
{
	int textureX = 128;
	int textureY = 128;

	public ModelRendererTurbo[] observerModel, hatchModel, lensModel;

	public ModelInfraredObserver(boolean doOffsets) //Same as Filename
	{
		baseModel = new ModelRendererTurbo[53];
		baseModel[0] = new ModelRendererTurbo(this, 120, 68, textureX, textureY); // PLATFORM04
		baseModel[1] = new ModelRendererTurbo(this, 36, 36, textureX, textureY); // PLATFORM07
		baseModel[2] = new ModelRendererTurbo(this, 52, 14, textureX, textureY); // Radio01
		baseModel[3] = new ModelRendererTurbo(this, 4, 47, textureX, textureY); // Radio02
		baseModel[4] = new ModelRendererTurbo(this, 0, 101, textureX, textureY); // Radio03
		baseModel[5] = new ModelRendererTurbo(this, 73, 7, textureX, textureY); // Radio04
		baseModel[6] = new ModelRendererTurbo(this, 73, 7, textureX, textureY); // Radio05
		baseModel[7] = new ModelRendererTurbo(this, 56, 100, textureX, textureY); // Radio06
		baseModel[8] = new ModelRendererTurbo(this, 4, 90, textureX, textureY); // Radio07
		baseModel[9] = new ModelRendererTurbo(this, 20, 84, textureX, textureY); // Radio08
		baseModel[10] = new ModelRendererTurbo(this, 20, 84, textureX, textureY); // Radio09
		baseModel[11] = new ModelRendererTurbo(this, 56, 100, textureX, textureY); // Radio10
		baseModel[12] = new ModelRendererTurbo(this, 56, 100, textureX, textureY); // Radio11
		baseModel[13] = new ModelRendererTurbo(this, 2, 61, textureX, textureY); // Radio12
		baseModel[14] = new ModelRendererTurbo(this, 0, 75, textureX, textureY); // Radio13
		baseModel[15] = new ModelRendererTurbo(this, 118, 13, textureX, textureY); // PLATFORM08
		baseModel[16] = new ModelRendererTurbo(this, 30, 34, textureX, textureY); // Radio14
		baseModel[17] = new ModelRendererTurbo(this, 56, 100, textureX, textureY); // Radio15
		baseModel[18] = new ModelRendererTurbo(this, 56, 100, textureX, textureY); // Radio16
		baseModel[19] = new ModelRendererTurbo(this, 3, 82, textureX, textureY); // Radio17
		baseModel[20] = new ModelRendererTurbo(this, 25, 4, textureX, textureY); // Radio18
		baseModel[21] = new ModelRendererTurbo(this, 2, 58, textureX, textureY); // Radio19
		baseModel[22] = new ModelRendererTurbo(this, 2, 58, textureX, textureY); // Radio20
		baseModel[23] = new ModelRendererTurbo(this, 2, 58, textureX, textureY); // Radio21
		baseModel[24] = new ModelRendererTurbo(this, 56, 100, textureX, textureY); // Radio22
		baseModel[25] = new ModelRendererTurbo(this, 56, 100, textureX, textureY); // Radio23
		baseModel[26] = new ModelRendererTurbo(this, 56, 100, textureX, textureY); // Radio24
		baseModel[27] = new ModelRendererTurbo(this, 48, 38, textureX, textureY); // Radio25
		baseModel[28] = new ModelRendererTurbo(this, 2, 68, textureX, textureY); // Radio26
		baseModel[29] = new ModelRendererTurbo(this, 2, 72, textureX, textureY); // Radio27
		baseModel[30] = new ModelRendererTurbo(this, 48, 38, textureX, textureY); // Radio28
		baseModel[31] = new ModelRendererTurbo(this, 1, 89, textureX, textureY); // Radio29
		baseModel[32] = new ModelRendererTurbo(this, 16, 107, textureX, textureY); // Radio32
		baseModel[33] = new ModelRendererTurbo(this, 39, 73, textureX, textureY); // Radio34
		baseModel[34] = new ModelRendererTurbo(this, 2, 58, textureX, textureY); // Radio35
		baseModel[35] = new ModelRendererTurbo(this, 118, 13, textureX, textureY); // PLATFORM08
		baseModel[36] = new ModelRendererTurbo(this, 120, 68, textureX, textureY); // PLATFORM04
		baseModel[37] = new ModelRendererTurbo(this, 11, 86, textureX, textureY); // Radio26
		baseModel[38] = new ModelRendererTurbo(this, 2, 79, textureX, textureY); // Radio26
		baseModel[39] = new ModelRendererTurbo(this, 30, 65, textureX, textureY); // Radio34
		baseModel[40] = new ModelRendererTurbo(this, 36, 67, textureX, textureY); // Radio34
		baseModel[41] = new ModelRendererTurbo(this, 10, 53, textureX, textureY); // Radio34
		baseModel[42] = new ModelRendererTurbo(this, 56, 97, textureX, textureY); // Radio34
		baseModel[43] = new ModelRendererTurbo(this, 36, 62, textureX, textureY); // Radio34
		baseModel[44] = new ModelRendererTurbo(this, 44, 66, textureX, textureY); // Radio34
		baseModel[45] = new ModelRendererTurbo(this, 30, 71, textureX, textureY); // Radio35
		baseModel[46] = new ModelRendererTurbo(this, 3, 86, textureX, textureY); // Radio26
		baseModel[47] = new ModelRendererTurbo(this, 19, 86, textureX, textureY); // Radio19
		baseModel[48] = new ModelRendererTurbo(this, 19, 86, textureX, textureY); // Radio20
		baseModel[49] = new ModelRendererTurbo(this, 16, 107, textureX, textureY); // Radio21
		baseModel[50] = new ModelRendererTurbo(this, 16, 107, textureX, textureY); // Radio21
		baseModel[51] = new ModelRendererTurbo(this, 10, 60, textureX, textureY); // Shape 77
		baseModel[52] = new ModelRendererTurbo(this, 10, 60, textureX, textureY); // Shape 77

		baseModel[0].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // PLATFORM04
		baseModel[0].setRotationPoint(-13.5F, -25F, -4F);

		baseModel[1].addBox(0F, 0F, 0F, 26, 6, 20, 0F); // PLATFORM07
		baseModel[1].setRotationPoint(-14F, -7F, -12.5F);

		baseModel[2].addBox(0F, 0F, 0F, 13, 17, 5, 0F); // Radio01
		baseModel[2].setRotationPoint(-3.5F, -18F, 10.5F);

		baseModel[3].addBox(0F, 0F, 0F, 3, 1, 3, 0F); // Radio02
		baseModel[3].setRotationPoint(5.5F, -19F, 11.5F);

		baseModel[4].addBox(0F, 0F, 0F, 1, 26, 1, 0F); // Radio03
		baseModel[4].setRotationPoint(6.5F, -45F, 12.5F);

		baseModel[5].addBox(0F, 0F, 0F, 6, 6, 1, 0F); // Radio04
		baseModel[5].setRotationPoint(2.5F, -17F, 15.5F);

		baseModel[6].addBox(0F, 0F, 0F, 6, 6, 1, 0F); // Radio05
		baseModel[6].setRotationPoint(2.5F, -10F, 15.5F);

		baseModel[7].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // Radio06
		baseModel[7].setRotationPoint(9.49F, -16F, 11.5F);

		baseModel[8].addBox(0F, 0F, 0F, 5, 9, 5, 0F); // Radio07
		baseModel[8].setRotationPoint(-9.5F, -12F, 10.5F);

		baseModel[9].addBox(0F, 0F, 0F, 7, 2, 7, 0F); // Radio08
		baseModel[9].setRotationPoint(-10.5F, -3F, 9.5F);

		baseModel[10].addBox(0F, 0F, 0F, 7, 2, 7, 0F); // Radio09
		baseModel[10].setRotationPoint(-10.5F, -14F, 9.5F);

		baseModel[11].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // Radio10
		baseModel[11].setRotationPoint(-9.5F, -14F, 11.5F);
		baseModel[11].rotateAngleZ = 1.57079633F;

		baseModel[12].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // Radio11
		baseModel[12].setRotationPoint(-4.5F, -17.5F, 11.5F);

		baseModel[13].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio12
		baseModel[13].setRotationPoint(-9F, -17F, 12F);

		baseModel[14].addBox(0F, 0F, 0F, 3, 2, 2, 0F); // Radio13
		baseModel[14].setRotationPoint(-7F, -17F, 12F);

		baseModel[15].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // PLATFORM08
		baseModel[15].setRotationPoint(-11.5F, -25.5F, -4.5F);

		baseModel[16].addBox(0F, 0F, 0F, 3, 12, 10, 0F); // Radio14
		baseModel[16].setRotationPoint(-16.5F, -13F, 5.5F);

		baseModel[17].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // Radio15
		baseModel[17].setRotationPoint(-14F, -4F, 7F);

		baseModel[18].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // Radio16
		baseModel[18].setRotationPoint(-14F, -8F, 7F);

		baseModel[19].addBox(0F, 0F, 0F, 10, 2, 2, 0F); // Radio17
		baseModel[19].setRotationPoint(-13.5F, -7.5F, 7.5F);

		baseModel[20].addBox(0F, 0F, 0F, 14, 2, 2, 0F); // Radio18
		baseModel[20].setRotationPoint(-13.5F, -3.5F, 7.5F);

		baseModel[21].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // Radio19
		baseModel[21].setRotationPoint(0.5F, -3.5F, 9.5F);

		baseModel[22].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // Radio20
		baseModel[22].setRotationPoint(-3.5F, -7.5F, 9.5F);

		baseModel[23].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // Radio21
		baseModel[23].setRotationPoint(5F, -5.5F, 8.5F);

		baseModel[24].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // Radio22
		baseModel[24].setRotationPoint(11.5F, -5.5F, 1.5F);

		baseModel[25].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // Radio23
		baseModel[25].setRotationPoint(11.5F, -5.5F, -2.5F);

		baseModel[26].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // Radio24
		baseModel[26].setRotationPoint(11.5F, -5.5F, -6.5F);

		baseModel[27].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio25
		baseModel[27].setRotationPoint(12.5F, -5F, -6F);

		baseModel[28].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio26
		baseModel[28].setRotationPoint(12.5F, -4F, -10F);

		baseModel[29].addBox(0F, 0F, 0F, 2, 1, 2, 0F); // Radio27
		baseModel[29].setRotationPoint(12.5F, -2F, -10F);

		baseModel[30].addShapeBox(0F, 0F, 0F, 2, 4, 2, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio28
		baseModel[30].setRotationPoint(12.5F, -5F, -2F);

		baseModel[31].addShapeBox(0F, 0F, 0F, 2, 4, 2, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio29
		baseModel[31].setRotationPoint(12.5F, -5F, 2F);

		baseModel[32].addBox(0F, 0F, 0F, 3, 3, 1, 0F); // Radio32
		baseModel[32].setRotationPoint(8F, -6F, 7.5F);

		baseModel[33].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // Radio34
		baseModel[33].setRotationPoint(9.5F, -5.5F, 11F);

		baseModel[34].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // Radio35
		baseModel[34].setRotationPoint(8.5F, -5.5F, 8F);

		baseModel[35].addBox(0F, 0F, 0F, 1, 4, 4, 0F); // PLATFORM08
		baseModel[35].setRotationPoint(9.5F, -25.5F, -4.5F);

		baseModel[36].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // PLATFORM04
		baseModel[36].setRotationPoint(11.5F, -25F, -4F);

		baseModel[37].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // Radio26
		baseModel[37].setRotationPoint(12.5F, -4F, -8F);

		baseModel[38].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio26
		baseModel[38].setRotationPoint(12.5F, -4F, -6F);

		baseModel[39].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F); // Radio34
		baseModel[39].setRotationPoint(9.5F, -5.5F, 13F);

		baseModel[40].addBox(0F, 0F, 0F, 2, 4, 2, 0F); // Radio34
		baseModel[40].setRotationPoint(9.5F, -9.5F, 13F);

		baseModel[41].addShapeBox(0F, 0F, 0F, 2, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio34
		baseModel[41].setRotationPoint(9.5F, -11.5F, 12F);

		baseModel[42].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio34
		baseModel[42].setRotationPoint(9.5F, -15.5F, 12F);

		baseModel[43].addBox(0F, 0F, 0F, 2, 3, 2, 0F); // Radio34
		baseModel[43].setRotationPoint(9.5F, -14.5F, 12F);

		baseModel[44].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio34
		baseModel[44].setRotationPoint(9.5F, -11.5F, 13F);

		baseModel[45].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, -1F, 0F, 0F); // Radio35
		baseModel[45].setRotationPoint(8.5F, -5.5F, 9F);

		baseModel[46].addShapeBox(0F, 0F, 0F, 2, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F); // Radio26
		baseModel[46].setRotationPoint(12.5F, -3F, -6F);

		baseModel[47].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio19
		baseModel[47].setRotationPoint(0.5F, -3.5F, 7.5F);

		baseModel[48].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio20
		baseModel[48].setRotationPoint(-3.5F, -7.5F, 7.5F);

		baseModel[49].addBox(0F, 0F, 0F, 3, 3, 1, 0F); // Radio21
		baseModel[49].setRotationPoint(4.5F, -6F, 9.5F);

		baseModel[50].addBox(0F, 0F, 0F, 3, 3, 1, 0F); // Radio21
		baseModel[50].setRotationPoint(4.5F, -6F, 7.5F);

		baseModel[51].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(10, 0, 10, 0), new Coord2D(10, 16, 10, 16), new Coord2D(8, 20, 8, 20), new Coord2D(2, 20, 2, 20), new Coord2D(0, 16, 0, 16) }), 2, 10, 20, 58, 2, ModelRendererTurbo.MR_FRONT, new float[] {16 ,5 ,6 ,5 ,16 ,10}); // Shape 77
		baseModel[51].setRotationPoint(-13F, -7F, 2.5F);
		baseModel[51].rotateAngleY = 1.57079633F;

		baseModel[52].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(10, 0, 10, 0), new Coord2D(10, 16, 10, 16), new Coord2D(8, 20, 8, 20), new Coord2D(2, 20, 2, 20), new Coord2D(0, 16, 0, 16) }), 2, 10, 20, 58, 2, ModelRendererTurbo.MR_FRONT, new float[] {16 ,5 ,6 ,5 ,16 ,10}); // Shape 77
		baseModel[52].setRotationPoint(10F, -7F, 2.5F);
		baseModel[52].rotateAngleY = 1.57079633F;


		observerModel = new ModelRendererTurbo[28];
		observerModel[0] = new ModelRendererTurbo(this, 48, 62, textureX, textureY); // EYEBOX01
		observerModel[1] = new ModelRendererTurbo(this, 88, 11, textureX, textureY); // EYEBOX02
		observerModel[2] = new ModelRendererTurbo(this, 108, 65, textureX, textureY); // EYEBOX03
		observerModel[3] = new ModelRendererTurbo(this, 4, 55, textureX, textureY); // EYEBOX04
		observerModel[4] = new ModelRendererTurbo(this, 112, 36, textureX, textureY); // EYEBOX05
		observerModel[5] = new ModelRendererTurbo(this, 112, 36, textureX, textureY); // EYEBOX06
		observerModel[6] = new ModelRendererTurbo(this, 4, 107, textureX, textureY); // EYEBOX07
		observerModel[7] = new ModelRendererTurbo(this, 48, 106, textureX, textureY); // EYEBOX08
		observerModel[8] = new ModelRendererTurbo(this, 48, 106, textureX, textureY); // EYEBOX09
		observerModel[9] = new ModelRendererTurbo(this, 16, 112, textureX, textureY); // EYEBOX10
		observerModel[10] = new ModelRendererTurbo(this, 22, 35, textureX, textureY); // EYEBOX11
		observerModel[11] = new ModelRendererTurbo(this, 37, 8, textureX, textureY); // LENSES02
		observerModel[12] = new ModelRendererTurbo(this, 112, 50, textureX, textureY); // EYEBOX11
		observerModel[13] = new ModelRendererTurbo(this, 19, 52, textureX, textureY); // LENSES01
		observerModel[14] = new ModelRendererTurbo(this, 18, 50, textureX, textureY); // LENSES01
		observerModel[15] = new ModelRendererTurbo(this, 15, 52, textureX, textureY); // LENSES01
		observerModel[16] = new ModelRendererTurbo(this, 19, 52, textureX, textureY); // LENSES01
		observerModel[17] = new ModelRendererTurbo(this, 56, 69, textureX, textureY); // LENSES01
		observerModel[18] = new ModelRendererTurbo(this, 56, 69, textureX, textureY); // LENSES01
		observerModel[19] = new ModelRendererTurbo(this, 52, 68, textureX, textureY); // LENSES01
		observerModel[20] = new ModelRendererTurbo(this, 52, 68, textureX, textureY); // LENSES01
		observerModel[21] = new ModelRendererTurbo(this, 14, 8, textureX, textureY); // EYEBOX02
		observerModel[22] = new ModelRendererTurbo(this, 14, 8, textureX, textureY); // EYEBOX02
		observerModel[23] = new ModelRendererTurbo(this, 47, 4, textureX, textureY); // EYEBOX02
		observerModel[24] = new ModelRendererTurbo(this, 6, 35, textureX, textureY); // EYEBOX02
		observerModel[25] = new ModelRendererTurbo(this, 24, 26, textureX, textureY); // EYEBOX02
		observerModel[26] = new ModelRendererTurbo(this, 36, 18, textureX, textureY); // EYEBOX02
		observerModel[27] = new ModelRendererTurbo(this, 47, 4, textureX, textureY); // EYEBOX02

		observerModel[0].addBox(0F, 0F, 0F, 20, 15, 20, 0F); // EYEBOX01
		observerModel[0].setRotationPoint(-10.5F, -33F, -12.5F);

		observerModel[1].addBox(0F, 0F, 0F, 10, 15, 10, 0F); // EYEBOX02
		observerModel[1].setRotationPoint(-0.5F, -33F, -22.5F);

		observerModel[2].addBox(0F, 0F, 0F, 3, 14, 3, 0F); // EYEBOX03
		observerModel[2].setRotationPoint(3F, -34F, -20.5F);
		observerModel[2].rotateAngleX = 1.57079633F;

		observerModel[3].addBox(0F, 0F, 0F, 2, 2, 1, 0F); // EYEBOX04
		observerModel[3].setRotationPoint(3.5F, -36.5F, -21F);

		observerModel[4].addBox(0F, 0F, 0F, 1, 5, 2, 0F); // EYEBOX05
		observerModel[4].setRotationPoint(4F, -37.5F, -17.5F);

		observerModel[5].addBox(0F, 0F, 0F, 1, 5, 2, 0F); // EYEBOX06
		observerModel[5].setRotationPoint(4F, -37.5F, -10.5F);

		observerModel[6].addBox(0F, 0F, 0F, 5, 20, 1, 0F); // EYEBOX07
		observerModel[6].setRotationPoint(8.5F, -18F, -12.5F);
		observerModel[6].rotateAngleX = 1.57079633F;
		observerModel[6].rotateAngleZ = -1.57079633F;

		observerModel[7].addBox(0F, 0F, 0F, 8, 5, 1, 0F); // EYEBOX08
		observerModel[7].setRotationPoint(0.5F, -18F, -12.5F);

		observerModel[8].addBox(0F, 0F, 0F, 8, 5, 1, 0F); // EYEBOX09
		observerModel[8].setRotationPoint(0.5F, -18F, 6.5F);

		observerModel[9].addBox(0F, 0F, 0F, 20, 11, 5, 0F); // EYEBOX10
		observerModel[9].setRotationPoint(-10.5F, -13F, 7.5F);
		observerModel[9].rotateAngleX = 1.57079633F;
		observerModel[9].rotateAngleY = -1.57079633F;

		observerModel[10].addBox(0F, 0F, 0F, 7, 7, 2, 0F); // EYEBOX11
		observerModel[10].setRotationPoint(1F, -31.5F, -24.5F);

		observerModel[11].addBox(0F, 0F, 0F, 6, 6, 4, 0F); // LENSES02
		observerModel[11].setRotationPoint(-8.5F, -31.5F, -16.5F);

		observerModel[12].addBox(0F, 0F, 0F, 5, 5, 1, 0F); // EYEBOX11
		observerModel[12].setRotationPoint(2F, -30.5F, -25.5F);

		observerModel[13].addBox(0F, 0F, 0F, 2, 1, 1, 0F); // LENSES01
		observerModel[13].setRotationPoint(1F, -31.5F, -25.5F);

		observerModel[14].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // LENSES01
		observerModel[14].setRotationPoint(1F, -30.5F, -25.5F);

		observerModel[15].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // LENSES01
		observerModel[15].setRotationPoint(7F, -30.5F, -25.5F);

		observerModel[16].addBox(0F, 0F, 0F, 2, 1, 1, 0F); // LENSES01
		observerModel[16].setRotationPoint(6F, -31.5F, -25.5F);

		observerModel[17].addBox(0F, 0F, 0F, 2, 1, 1, 0F); // LENSES01
		observerModel[17].setRotationPoint(1F, -25.5F, -25.5F);

		observerModel[18].addBox(0F, 0F, 0F, 2, 1, 1, 0F); // LENSES01
		observerModel[18].setRotationPoint(6F, -25.5F, -25.5F);

		observerModel[19].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // LENSES01
		observerModel[19].setRotationPoint(1F, -26.5F, -25.5F);

		observerModel[20].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // LENSES01
		observerModel[20].setRotationPoint(7F, -26.5F, -25.5F);

		observerModel[21].addBox(0F, 0F, 0F, 1, 8, 10, 0F); // EYEBOX02
		observerModel[21].setRotationPoint(-1.48F, -26F, -12.48F);

		observerModel[22].addBox(0F, 0F, 0F, 1, 8, 10, 0F); // EYEBOX02
		observerModel[22].setRotationPoint(-10.48F, -26F, -12.48F);

		observerModel[23].addBox(0F, 0F, 0F, 8, 0, 10, 0F); // EYEBOX02
		observerModel[23].setRotationPoint(-9.49F, -26F, -12.49F);

		observerModel[24].addBox(0F, 0F, 0F, 4, 5, 4, 0F); // EYEBOX02
		observerModel[24].setRotationPoint(-8.48F, -23F, -11.28F);

		observerModel[25].addBox(0F, 0F, 0F, 4, 5, 4, 0F); // EYEBOX02
		observerModel[25].setRotationPoint(-5.48F, -23F, -7.48F);

		observerModel[26].addBox(0F, 0F, 0F, 8, 10, 0, 0F); // EYEBOX02
		observerModel[26].setRotationPoint(-9.5F, -26F, -2.5F);

		observerModel[27].addBox(0F, 0F, 0F, 8, 0, 10, 0F); // EYEBOX02
		observerModel[27].setRotationPoint(-9.49F, -18.01F, -12.49F);


		hatchModel = new ModelRendererTurbo[1];
		hatchModel[0] = new ModelRendererTurbo(this, 30, 93, textureX, textureY); // DOORCAMERA01

		hatchModel[0].addBox(0F, 0F, 0F, 8, 18, 1, 0F); // DOORCAMERA01
		hatchModel[0].setRotationPoint(0.5F, -14F, 6.5F);
		hatchModel[0].rotateAngleX = -1.57079633F;


		lensModel = new ModelRendererTurbo[9];
		lensModel[0] = new ModelRendererTurbo(this, 14, 46, textureX, textureY); // LENSES01
		lensModel[1] = new ModelRendererTurbo(this, 14, 44, textureX, textureY); // LENSES01
		lensModel[2] = new ModelRendererTurbo(this, 0, 46, textureX, textureY); // LENSES01
		lensModel[3] = new ModelRendererTurbo(this, 0, 52, textureX, textureY); // LENSES01
		lensModel[4] = new ModelRendererTurbo(this, 58, 64, textureX, textureY); // LENSES01
		lensModel[5] = new ModelRendererTurbo(this, 4, 51, textureX, textureY); // EYEBOX11
		lensModel[6] = new ModelRendererTurbo(this, 4, 51, textureX, textureY); // EYEBOX11
		lensModel[7] = new ModelRendererTurbo(this, 41, 82, textureX, textureY); // EYEBOX11
		lensModel[8] = new ModelRendererTurbo(this, 41, 82, textureX, textureY); // EYEBOX11

		lensModel[0].addBox(0F, 0F, 0F, 7, 1, 1, 0F); // LENSES01
		lensModel[0].setRotationPoint(1F, -31.5F, -26.5F);

		lensModel[1].addBox(0F, 0F, 0F, 7, 1, 1, 0F); // LENSES01
		lensModel[1].setRotationPoint(1F, -25.5F, -26.5F);

		lensModel[2].addBox(0F, 0F, 0F, 1, 5, 1, 0F); // LENSES01
		lensModel[2].setRotationPoint(1F, -30.5F, -26.5F);

		lensModel[3].addBox(0F, 0F, 0F, 1, 5, 1, 0F); // LENSES01
		lensModel[3].setRotationPoint(7F, -30.5F, -26.5F);

		lensModel[4].addBox(0F, 0F, 0F, 5, 5, 0, 0F); // LENSES01
		lensModel[4].setRotationPoint(2F, -30.5F, -26F);

		lensModel[5].addBox(0F, 0F, 0F, 3, 1, 1, 0F); // EYEBOX11
		lensModel[5].setRotationPoint(3F, -31.5F, -25.5F);

		lensModel[6].addBox(0F, 0F, 0F, 3, 1, 1, 0F); // EYEBOX11
		lensModel[6].setRotationPoint(3F, -25.5F, -25.5F);

		lensModel[7].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // EYEBOX11
		lensModel[7].setRotationPoint(1F, -29.5F, -25.5F);

		lensModel[8].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // EYEBOX11
		lensModel[8].setRotationPoint(7F, -29.5F, -25.5F);

		parts.put("base",  baseModel);
		parts.put("observer", observerModel);
		parts.put("hatch", hatchModel);
		parts.put("lens", lensModel);

		translateAll(0.5f,1,0);
		if(doOffsets)
		{
			translate(observerModel,0,24,3);
			translate(hatchModel,0,24,3);
			translate(lensModel,0,24,3);
		}

		flipAll();
	}
}
