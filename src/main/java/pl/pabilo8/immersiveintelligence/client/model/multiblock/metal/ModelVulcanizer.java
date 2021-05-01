package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 01.05.2021
 */
public class ModelVulcanizer extends ModelIIBase
{
	ModelRendererTurbo[] rotatoModel, castBoxFrontModel, castTopFrontModel, castDoorFrontModel, castBoxBackModel, castTopBackModel, castDoorBackModel;

	int textureX = 256;
	int textureY = 256;

	public ModelVulcanizer() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[45];
		baseModel[0] = new ModelRendererTurbo(this, 36, 208, textureX, textureY); // MAIN01
		baseModel[1] = new ModelRendererTurbo(this, 12, 12, textureX, textureY); // MAIN02
		baseModel[2] = new ModelRendererTurbo(this, 5, 110, textureX, textureY); // MAIN03
		baseModel[3] = new ModelRendererTurbo(this, 12, 12, textureX, textureY); // MAIN04
		baseModel[4] = new ModelRendererTurbo(this, 12, 12, textureX, textureY); // MAIN05
		baseModel[5] = new ModelRendererTurbo(this, 12, 12, textureX, textureY); // MAIN06
		baseModel[6] = new ModelRendererTurbo(this, 164, 182, textureX, textureY); // MAIN07
		baseModel[7] = new ModelRendererTurbo(this, 12, 12, textureX, textureY); // MAIN08
		baseModel[8] = new ModelRendererTurbo(this, 12, 12, textureX, textureY); // MAIN09
		baseModel[9] = new ModelRendererTurbo(this, 12, 12, textureX, textureY); // MAIN10
		baseModel[10] = new ModelRendererTurbo(this, 12, 12, textureX, textureY); // MAIN11
		baseModel[11] = new ModelRendererTurbo(this, 95, 101, textureX, textureY); // MAIN14
		baseModel[12] = new ModelRendererTurbo(this, 56, 71, textureX, textureY); // MAIN16
		baseModel[13] = new ModelRendererTurbo(this, 56, 101, textureX, textureY); // MAIN19
		baseModel[14] = new ModelRendererTurbo(this, 192, 115, textureX, textureY); // MAIN24
		baseModel[15] = new ModelRendererTurbo(this, 42, 70, textureX, textureY); // MAIN25
		baseModel[16] = new ModelRendererTurbo(this, 42, 70, textureX, textureY); // MAIN26
		baseModel[17] = new ModelRendererTurbo(this, 133, 94, textureX, textureY); // Box 66
		baseModel[18] = new ModelRendererTurbo(this, 133, 94, textureX, textureY); // Box 68
		baseModel[19] = new ModelRendererTurbo(this, 100, 182, textureX, textureY); // Box 69
		baseModel[20] = new ModelRendererTurbo(this, 100, 182, textureX, textureY); // Box 70
		baseModel[21] = new ModelRendererTurbo(this, 14, 8, textureX, textureY); // MAIN25
		baseModel[22] = new ModelRendererTurbo(this, 14, 8, textureX, textureY); // MAIN25
		baseModel[23] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // MAIN01
		baseModel[24] = new ModelRendererTurbo(this, 36, 208, textureX, textureY); // MAIN01
		baseModel[25] = new ModelRendererTurbo(this, 36, 208, textureX, textureY); // MAIN01
		baseModel[26] = new ModelRendererTurbo(this, 36, 208, textureX, textureY); // MAIN01
		baseModel[27] = new ModelRendererTurbo(this, 95, 120, textureX, textureY); // MAIN14
		baseModel[28] = new ModelRendererTurbo(this, 11, 119, textureX, textureY); // MAIN14
		baseModel[29] = new ModelRendererTurbo(this, 126, 131, textureX, textureY); // MAIN14
		baseModel[30] = new ModelRendererTurbo(this, 11, 107, textureX, textureY); // MAIN14
		baseModel[31] = new ModelRendererTurbo(this, 125, 152, textureX, textureY); // MAIN14
		baseModel[32] = new ModelRendererTurbo(this, 4, 182, textureX, textureY); // MAIN14
		baseModel[33] = new ModelRendererTurbo(this, 126, 131, textureX, textureY); // MAIN14
		baseModel[34] = new ModelRendererTurbo(this, 56, 101, textureX, textureY); // MAIN19
		baseModel[35] = new ModelRendererTurbo(this, 196, 59, textureX, textureY); // MAIN19
		baseModel[36] = new ModelRendererTurbo(this, 196, 59, textureX, textureY); // MAIN19
		baseModel[37] = new ModelRendererTurbo(this, 56, 101, textureX, textureY); // MAIN19
		baseModel[38] = new ModelRendererTurbo(this, 104, 192, textureX, textureY); // MAIN19
		baseModel[39] = new ModelRendererTurbo(this, 56, 101, textureX, textureY); // MAIN19
		baseModel[40] = new ModelRendererTurbo(this, 152, 136, textureX, textureY); // MAIN19
		baseModel[41] = new ModelRendererTurbo(this, 56, 81, textureX, textureY); // MAIN19
		baseModel[42] = new ModelRendererTurbo(this, 165, 71, textureX, textureY); // MAIN19
		baseModel[43] = new ModelRendererTurbo(this, 56, 81, textureX, textureY); // MAIN19
		baseModel[44] = new ModelRendererTurbo(this, 196, 51, textureX, textureY); // MAIN19

		baseModel[0].addShapeBox(0F, 0F, 0F, 32, 16, 32, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN01
		baseModel[0].setRotationPoint(-49F, 7F, -48F);

		baseModel[1].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN02
		baseModel[1].setRotationPoint(-47F, 4F, 42F);

		baseModel[2].addShapeBox(0F, 0F, 0F, 30, 42, 30, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN03
		baseModel[2].setRotationPoint(-48F, -38F, 15F);

		baseModel[3].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN04
		baseModel[3].setRotationPoint(-21F, 4F, 42F);

		baseModel[4].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN05
		baseModel[4].setRotationPoint(-21F, 4F, 16F);

		baseModel[5].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN06
		baseModel[5].setRotationPoint(-47F, 4F, 16F);

		baseModel[6].addShapeBox(0F, 0F, 0F, 16, 42, 30, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN07
		baseModel[6].setRotationPoint(-1F, -38F, 16F);

		baseModel[7].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN08
		baseModel[7].setRotationPoint(1F, 4F, 43F);

		baseModel[8].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN09
		baseModel[8].setRotationPoint(1F, 4F, 17F);

		baseModel[9].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN10
		baseModel[9].setRotationPoint(11F, 4F, 43F);

		baseModel[10].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN11
		baseModel[10].setRotationPoint(11F, 4F, 17F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 16, 3, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN14
		baseModel[11].setRotationPoint(-41F, 4F, 23F);

		baseModel[12].addShapeBox(0F, 0F, 0F, 22, 8, 22, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN16
		baseModel[12].setRotationPoint(-28F, -1F, -32F);

		baseModel[13].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN19
		baseModel[13].setRotationPoint(-14F, 3F, -42F);

		baseModel[14].addBox(0F, 0F, 0F, 18, 1, 18, 0F); // MAIN24
		baseModel[14].setRotationPoint(-26F, -2F, -30F);

		baseModel[15].addShapeBox(0F, 0F, 0F, 10, 10, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN25
		baseModel[15].setRotationPoint(2F, -29F, 46F);

		baseModel[16].addShapeBox(0F, 0F, 0F, 10, 10, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN26
		baseModel[16].setRotationPoint(-38F, -29F, 46F);

		baseModel[17].addShapeBox(0F, 0F, 0F, 3, 3, 26, 0F, 0F, 0F, 0F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, 0F); // Box 66
		baseModel[17].setRotationPoint(-7F, -3F, -34F);

		baseModel[18].addShapeBox(0F, 0F, 0F, 3, 3, 26, 0F, 0F, 0F, -3F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -3F); // Box 68
		baseModel[18].setRotationPoint(-30F, -3F, -34F);

		baseModel[19].addShapeBox(0F, 0F, 0F, 20, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 69
		baseModel[19].setRotationPoint(-27F, -3F, -34F);

		baseModel[20].addShapeBox(0F, 0F, 0F, 20, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 70
		baseModel[20].setRotationPoint(-27F, -3F, -11F);

		baseModel[21].addShapeBox(0F, 0F, 0F, 4, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN25
		baseModel[21].setRotationPoint(5F, -32F, 46F);

		baseModel[22].addShapeBox(0F, 0F, 0F, 4, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN25
		baseModel[22].setRotationPoint(-35F, -32F, 46F);

		baseModel[23].addShapeBox(0F, 0F, 0F, 64, 16, 32, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN01
		baseModel[23].setRotationPoint(-49F, 7F, 16F);

		baseModel[24].addShapeBox(0F, 0F, 0F, 32, 16, 32, 0F, -32F, 0F, -32F, -32F, 0F, -32F, -32F, 0F, -32F, -32F, 0F, -32F, -32F, 0F, -32F, -32F, 0F, -32F, -32F, 0F, -32F, -32F, 0F, -32F); // MAIN01
		baseModel[24].setRotationPoint(-17F, 7F, -16F);

		baseModel[25].addShapeBox(0F, 0F, 0F, 32, 16, 32, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F); // MAIN01
		baseModel[25].setRotationPoint(-17F, 7F, -48F);

		baseModel[26].addShapeBox(0F, 0F, 0F, 32, 16, 32, 0F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F); // MAIN01
		baseModel[26].setRotationPoint(-49F, 7F, -16F);

		baseModel[27].addShapeBox(0F, 0F, 0F, 8, 3, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN14
		baseModel[27].setRotationPoint(4F, 4F, 28F);

		baseModel[28].addShapeBox(0F, 0F, 0F, 6, 6, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN14
		baseModel[28].setRotationPoint(-12F, 1F, 27F);

		baseModel[29].addShapeBox(0F, 0F, 0F, 6, 6, 15, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN14
		baseModel[29].setRotationPoint(-12F, 1F, 12F);

		baseModel[30].addShapeBox(0F, 0F, 0F, 6, 6, 6, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN14
		baseModel[30].setRotationPoint(-12F, 1F, 6F);

		baseModel[31].addShapeBox(0F, 0F, 0F, 4, 6, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN14
		baseModel[31].setRotationPoint(-16F, 1F, 6F);

		baseModel[32].addShapeBox(0F, 0F, 0F, 6, 6, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // MAIN14
		baseModel[32].setRotationPoint(-22F, 1F, 6F);

		baseModel[33].addShapeBox(0F, 0F, 0F, 6, 6, 15, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN14
		baseModel[33].setRotationPoint(-22F, 1F, -9F);

		baseModel[34].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN19
		baseModel[34].setRotationPoint(-24F, 3F, -42F);

		baseModel[35].addShapeBox(0F, 0F, 0F, 4, 4, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN19
		baseModel[35].setRotationPoint(-14F, 3F, -38F);

		baseModel[36].addShapeBox(0F, 0F, 0F, 4, 4, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN19
		baseModel[36].setRotationPoint(-24F, 3F, -38F);

		baseModel[37].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN19
		baseModel[37].setRotationPoint(-2F, 3F, -42F);

		baseModel[38].addShapeBox(0F, 0F, 0F, 4, 4, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN19
		baseModel[38].setRotationPoint(-2F, 3F, -38F);

		baseModel[39].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN19
		baseModel[39].setRotationPoint(6F, 3F, -38F);

		baseModel[40].addShapeBox(0F, 0F, 0F, 4, 4, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN19
		baseModel[40].setRotationPoint(6F, 3F, -34F);

		baseModel[41].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // MAIN19
		baseModel[41].setRotationPoint(-2F, 3F, -26F);

		baseModel[42].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN19
		baseModel[42].setRotationPoint(-6F, 3F, -26F);

		baseModel[43].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // MAIN19
		baseModel[43].setRotationPoint(6F, 3F, -18F);

		baseModel[44].addShapeBox(0F, 0F, 0F, 12, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN19
		baseModel[44].setRotationPoint(-6F, 3F, -18F);


		rotatoModel = new ModelRendererTurbo[3];
		rotatoModel[0] = new ModelRendererTurbo(this, 0, 70, textureX, textureY); // ROTATO01
		rotatoModel[1] = new ModelRendererTurbo(this, 122, 71, textureX, textureY); // ROTATO02
		rotatoModel[2] = new ModelRendererTurbo(this, 160, 0, textureX, textureY); // ROTATO03

		rotatoModel[0].addShapeBox(0F, 0F, 0F, 14, 23, 14, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // ROTATO01
		rotatoModel[0].setRotationPoint(-24F, -25F, -28F);

		rotatoModel[1].addShapeBox(0F, 0F, 0F, 14, 8, 14, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // ROTATO02
		rotatoModel[1].setRotationPoint(-24F, -41F, -28F);

		rotatoModel[2].addShapeBox(0F, 0F, 0F, 12, 8, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // ROTATO03
		rotatoModel[2].setRotationPoint(-23F, -33F, -27F);


		castBoxFrontModel = new ModelRendererTurbo[4];
		castBoxFrontModel[0] = new ModelRendererTurbo(this, 98, 48, textureX, textureY); // BAWXIS01
		castBoxFrontModel[1] = new ModelRendererTurbo(this, 28, 191, textureX, textureY); // BAWXIS02
		castBoxFrontModel[2] = new ModelRendererTurbo(this, 192, 156, textureX, textureY); // BAWXIS03
		castBoxFrontModel[3] = new ModelRendererTurbo(this, 192, 134, textureX, textureY); // BAWXIS04

		castBoxFrontModel[0].addShapeBox(0F, 0F, 0F, 48, 22, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BAWXIS01
		castBoxFrontModel[0].setRotationPoint(-4F, -23F, -45F);
		castBoxFrontModel[0].rotateAngleY = 1.57079633F;

		castBoxFrontModel[1].addShapeBox(0F, 0F, 0F, 19, 48, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BAWXIS02
		castBoxFrontModel[1].setRotationPoint(-4F, -1F, -45F);
		castBoxFrontModel[1].rotateAngleX = 1.57079633F;

		castBoxFrontModel[2].addShapeBox(0F, 0F, 0F, 18, 21, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BAWXIS03
		castBoxFrontModel[2].setRotationPoint(-4F, -23F, -45F);

		castBoxFrontModel[3].addShapeBox(0F, 0F, 0F, 18, 21, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BAWXIS04
		castBoxFrontModel[3].setRotationPoint(-4F, -23F, 2F);


		castTopFrontModel = new ModelRendererTurbo[16];
		castTopFrontModel[0] = new ModelRendererTurbo(this, 155, 156, textureX, textureY); // TOP_ONE01
		castTopFrontModel[1] = new ModelRendererTurbo(this, 124, 188, textureX, textureY); // TOP_ONE02
		castTopFrontModel[2] = new ModelRendererTurbo(this, 22, 182, textureX, textureY); // TOP_ONE03
		castTopFrontModel[3] = new ModelRendererTurbo(this, 22, 182, textureX, textureY); // TOP_ONE04
		castTopFrontModel[4] = new ModelRendererTurbo(this, 22, 182, textureX, textureY); // TOP_ONE05
		castTopFrontModel[5] = new ModelRendererTurbo(this, 64, 70, textureX, textureY); // TOP_ONE06
		castTopFrontModel[6] = new ModelRendererTurbo(this, 64, 70, textureX, textureY); // TOP_ONE07
		castTopFrontModel[7] = new ModelRendererTurbo(this, 64, 70, textureX, textureY); // TOP_ONE08
		castTopFrontModel[8] = new ModelRendererTurbo(this, 68, 184, textureX, textureY); // TOP_ONE09
		castTopFrontModel[9] = new ModelRendererTurbo(this, 212, 0, textureX, textureY); // TOP_ONE10
		castTopFrontModel[10] = new ModelRendererTurbo(this, 8, 25, textureX, textureY); // TOP_ONE11
		castTopFrontModel[11] = new ModelRendererTurbo(this, 0, 16, textureX, textureY); // TOP_ONE12
		castTopFrontModel[12] = new ModelRendererTurbo(this, 8, 25, textureX, textureY); // TOP_ONE13
		castTopFrontModel[13] = new ModelRendererTurbo(this, 8, 25, textureX, textureY); // TOP_ONE14
		castTopFrontModel[14] = new ModelRendererTurbo(this, 0, 16, textureX, textureY); // TOP_ONE17
		castTopFrontModel[15] = new ModelRendererTurbo(this, 0, 16, textureX, textureY); // TOP_ONE19

		castTopFrontModel[0].addShapeBox(0F, 0F, 0F, 12, 48, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_ONE01
		castTopFrontModel[0].setRotationPoint(-5F, -28F, -45F);
		castTopFrontModel[0].rotateAngleX = 1.57079633F;

		castTopFrontModel[1].addShapeBox(0F, 0F, 0F, 5, 3, 8, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // TOP_ONE02
		castTopFrontModel[1].setRotationPoint(-10F, -35F, -25F);

		castTopFrontModel[2].addShapeBox(0F, 0F, 0F, 10, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_ONE03
		castTopFrontModel[2].setRotationPoint(-10F, -40F, -27F);
		castTopFrontModel[2].rotateAngleZ = -0.33161256F;

		castTopFrontModel[3].addShapeBox(0F, 0F, 0F, 10, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_ONE04
		castTopFrontModel[3].setRotationPoint(-10F, -40F, -22F);
		castTopFrontModel[3].rotateAngleZ = -0.33161256F;

		castTopFrontModel[4].addShapeBox(0F, 0F, 0F, 10, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_ONE05
		castTopFrontModel[4].setRotationPoint(-10F, -40F, -17F);
		castTopFrontModel[4].rotateAngleZ = -0.33161256F;

		castTopFrontModel[5].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_ONE06
		castTopFrontModel[5].setRotationPoint(-2F, -37F, -17.01F);

		castTopFrontModel[6].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_ONE07
		castTopFrontModel[6].setRotationPoint(-2F, -37F, -22.01F);

		castTopFrontModel[7].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_ONE08
		castTopFrontModel[7].setRotationPoint(-2F, -37F, -27.01F);

		castTopFrontModel[8].addShapeBox(0F, 0F, 0F, 8, 3, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_ONE09
		castTopFrontModel[8].setRotationPoint(-3F, -28F, -28F);

		castTopFrontModel[9].addShapeBox(0F, 0F, 0F, 20, 48, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_ONE10
		castTopFrontModel[9].setRotationPoint(-5F, -23F, -45F);
		castTopFrontModel[9].rotateAngleX = 1.57079633F;

		castTopFrontModel[10].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // TOP_ONE11
		castTopFrontModel[10].setRotationPoint(8F, -26F, -38F);

		castTopFrontModel[11].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_ONE12
		castTopFrontModel[11].setRotationPoint(9F, -30F, -23F);

		castTopFrontModel[12].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // TOP_ONE13
		castTopFrontModel[12].setRotationPoint(8F, -26F, -24F);

		castTopFrontModel[13].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // TOP_ONE14
		castTopFrontModel[13].setRotationPoint(8F, -26F, -10F);

		castTopFrontModel[14].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_ONE17
		castTopFrontModel[14].setRotationPoint(9F, -30F, -9F);

		castTopFrontModel[15].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_ONE19
		castTopFrontModel[15].setRotationPoint(9F, -30F, -37F);


		castDoorFrontModel = new ModelRendererTurbo[13];
		castDoorFrontModel[0] = new ModelRendererTurbo(this, 0, 48, textureX, textureY); // DOOR01
		castDoorFrontModel[1] = new ModelRendererTurbo(this, 8, 8, textureX, textureY); // DOOR02
		castDoorFrontModel[2] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // DOOR03
		castDoorFrontModel[3] = new ModelRendererTurbo(this, 26, 8, textureX, textureY); // DOOR04
		castDoorFrontModel[4] = new ModelRendererTurbo(this, 26, 8, textureX, textureY); // DOOR05
		castDoorFrontModel[5] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // DOOR06
		castDoorFrontModel[6] = new ModelRendererTurbo(this, 8, 8, textureX, textureY); // DOOR07
		castDoorFrontModel[7] = new ModelRendererTurbo(this, 26, 8, textureX, textureY); // DOOR08
		castDoorFrontModel[8] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // DOOR09
		castDoorFrontModel[9] = new ModelRendererTurbo(this, 8, 8, textureX, textureY); // DOOR10
		castDoorFrontModel[10] = new ModelRendererTurbo(this, 26, 8, textureX, textureY); // DOOR04
		castDoorFrontModel[11] = new ModelRendererTurbo(this, 26, 8, textureX, textureY); // DOOR05
		castDoorFrontModel[12] = new ModelRendererTurbo(this, 26, 8, textureX, textureY); // DOOR08

		castDoorFrontModel[0].addShapeBox(0F, 0F, 0F, 48, 21, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR01
		castDoorFrontModel[0].setRotationPoint(15F, -23F, -45F);
		castDoorFrontModel[0].rotateAngleY = 1.57079633F;

		castDoorFrontModel[1].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR02
		castDoorFrontModel[1].setRotationPoint(15F, -10F, -40F);

		castDoorFrontModel[2].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR03
		castDoorFrontModel[2].setRotationPoint(15F, -19F, -40F);

		castDoorFrontModel[3].addShapeBox(0F, 0F, 0F, 1, 11, 2, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F); // DOOR04
		castDoorFrontModel[3].setRotationPoint(16F, -19F, -40F);

		castDoorFrontModel[4].addShapeBox(0F, 0F, 0F, 1, 11, 2, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F); // DOOR05
		castDoorFrontModel[4].setRotationPoint(16F, -19F, -4F);

		castDoorFrontModel[5].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR06
		castDoorFrontModel[5].setRotationPoint(15F, -19F, -4F);

		castDoorFrontModel[6].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR07
		castDoorFrontModel[6].setRotationPoint(15F, -10F, -4F);

		castDoorFrontModel[7].addShapeBox(0F, 0F, 0F, 1, 11, 2, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F); // DOOR08
		castDoorFrontModel[7].setRotationPoint(16F, -19F, -22F);

		castDoorFrontModel[8].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR09
		castDoorFrontModel[8].setRotationPoint(15F, -19F, -22F);

		castDoorFrontModel[9].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR10
		castDoorFrontModel[9].setRotationPoint(15F, -10F, -22F);

		castDoorFrontModel[10].addShapeBox(0F, 0F, 0F, 1, 11, 2, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // DOOR04
		castDoorFrontModel[10].setRotationPoint(-51F, -19F, -40F);

		castDoorFrontModel[11].addShapeBox(0F, 0F, 0F, 1, 11, 2, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // DOOR05
		castDoorFrontModel[11].setRotationPoint(-51F, -19F, -4F);

		castDoorFrontModel[12].addShapeBox(0F, 0F, 0F, 1, 11, 2, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // DOOR08
		castDoorFrontModel[12].setRotationPoint(-51F, -19F, -22F);


		castBoxBackModel = new ModelRendererTurbo[28];
		castBoxBackModel[0] = new ModelRendererTurbo(this, 155, 156, textureX, textureY); // TOP_TWO01
		castBoxBackModel[1] = new ModelRendererTurbo(this, 124, 188, textureX, textureY); // TOP_TWO02
		castBoxBackModel[2] = new ModelRendererTurbo(this, 22, 182, textureX, textureY); // TOP_TWO03
		castBoxBackModel[3] = new ModelRendererTurbo(this, 22, 182, textureX, textureY); // TOP_TWO04
		castBoxBackModel[4] = new ModelRendererTurbo(this, 22, 182, textureX, textureY); // TOP_TWO05
		castBoxBackModel[5] = new ModelRendererTurbo(this, 64, 70, textureX, textureY); // TOP_TWO06
		castBoxBackModel[6] = new ModelRendererTurbo(this, 64, 70, textureX, textureY); // TOP_TWO07
		castBoxBackModel[7] = new ModelRendererTurbo(this, 64, 70, textureX, textureY); // TOP_TWO08
		castBoxBackModel[8] = new ModelRendererTurbo(this, 68, 184, textureX, textureY); // TOP_TWO09
		castBoxBackModel[9] = new ModelRendererTurbo(this, 212, 0, textureX, textureY); // TOP_TWO10
		castBoxBackModel[10] = new ModelRendererTurbo(this, 8, 25, textureX, textureY); // TOP_TWO11
		castBoxBackModel[11] = new ModelRendererTurbo(this, 0, 16, textureX, textureY); // TOP_TWO12
		castBoxBackModel[12] = new ModelRendererTurbo(this, 8, 25, textureX, textureY); // TOP_TWO13
		castBoxBackModel[13] = new ModelRendererTurbo(this, 8, 25, textureX, textureY); // TOP_TWO14
		castBoxBackModel[14] = new ModelRendererTurbo(this, 0, 16, textureX, textureY); // TOP_TWO17
		castBoxBackModel[15] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // TOP_TWO18
		castBoxBackModel[16] = new ModelRendererTurbo(this, 0, 16, textureX, textureY); // TOP_TWO19
		castBoxBackModel[17] = new ModelRendererTurbo(this, 0, 8, textureX, textureY); // TOP_TWO18
		castBoxBackModel[18] = new ModelRendererTurbo(this, 16, 0, textureX, textureY); // TOP_TWO18
		castBoxBackModel[19] = new ModelRendererTurbo(this, 0, 8, textureX, textureY); // TOP_TWO18
		castBoxBackModel[20] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // TOP_TWO18
		castBoxBackModel[21] = new ModelRendererTurbo(this, 0, 8, textureX, textureY); // TOP_TWO18
		castBoxBackModel[22] = new ModelRendererTurbo(this, 16, 0, textureX, textureY); // TOP_TWO18
		castBoxBackModel[23] = new ModelRendererTurbo(this, 0, 8, textureX, textureY); // TOP_TWO18
		castBoxBackModel[24] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // TOP_TWO18
		castBoxBackModel[25] = new ModelRendererTurbo(this, 0, 8, textureX, textureY); // TOP_TWO18
		castBoxBackModel[26] = new ModelRendererTurbo(this, 16, 0, textureX, textureY); // TOP_TWO18
		castBoxBackModel[27] = new ModelRendererTurbo(this, 0, 8, textureX, textureY); // TOP_TWO18

		castBoxBackModel[0].addShapeBox(0F, 0F, 0F, 12, 48, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO01
		castBoxBackModel[0].setRotationPoint(-41F, -28F, -45F);
		castBoxBackModel[0].rotateAngleX = 1.57079633F;

		castBoxBackModel[1].addShapeBox(0F, 0F, 0F, 5, 3, 8, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F); // TOP_TWO02
		castBoxBackModel[1].setRotationPoint(-29F, -35F, -25F);

		castBoxBackModel[2].addShapeBox(0F, 0F, 0F, 10, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO03
		castBoxBackModel[2].setRotationPoint(-33F, -37F, -27F);
		castBoxBackModel[2].rotateAngleZ = 0.33161256F;

		castBoxBackModel[3].addShapeBox(0F, 0F, 0F, 10, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO04
		castBoxBackModel[3].setRotationPoint(-33F, -37F, -22F);
		castBoxBackModel[3].rotateAngleZ = 0.33161256F;

		castBoxBackModel[4].addShapeBox(0F, 0F, 0F, 10, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO05
		castBoxBackModel[4].setRotationPoint(-33F, -37F, -17F);
		castBoxBackModel[4].rotateAngleZ = 0.33161256F;

		castBoxBackModel[5].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO06
		castBoxBackModel[5].setRotationPoint(-34F, -37F, -17.01F);

		castBoxBackModel[6].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO07
		castBoxBackModel[6].setRotationPoint(-34F, -37F, -22.01F);

		castBoxBackModel[7].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO08
		castBoxBackModel[7].setRotationPoint(-34F, -37F, -27.01F);

		castBoxBackModel[8].addShapeBox(0F, 0F, 0F, 8, 3, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO09
		castBoxBackModel[8].setRotationPoint(-39F, -28F, -28F);

		castBoxBackModel[9].addShapeBox(0F, 0F, 0F, 20, 48, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO10
		castBoxBackModel[9].setRotationPoint(-49F, -23F, -45F);
		castBoxBackModel[9].rotateAngleX = 1.57079633F;

		castBoxBackModel[10].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // TOP_TWO11
		castBoxBackModel[10].setRotationPoint(-48F, -26F, -38F);

		castBoxBackModel[11].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO12
		castBoxBackModel[11].setRotationPoint(-47F, -30F, -23F);

		castBoxBackModel[12].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // TOP_TWO13
		castBoxBackModel[12].setRotationPoint(-48F, -26F, -24F);

		castBoxBackModel[13].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // TOP_TWO14
		castBoxBackModel[13].setRotationPoint(-48F, -26F, -10F);

		castBoxBackModel[14].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO17
		castBoxBackModel[14].setRotationPoint(-47F, -30F, -9F);

		castBoxBackModel[15].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO18
		castBoxBackModel[15].setRotationPoint(-47F, -34F, -37F);

		castBoxBackModel[16].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO19
		castBoxBackModel[16].setRotationPoint(-47F, -30F, -37F);

		castBoxBackModel[17].addBox(0F, 0F, 0F, 2, 4, 4, 0F); // TOP_TWO18
		castBoxBackModel[17].setRotationPoint(-43F, -34F, -37F);

		castBoxBackModel[18].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO18
		castBoxBackModel[18].setRotationPoint(9F, -34F, -37F);

		castBoxBackModel[19].addBox(0F, 0F, 0F, 2, 4, 4, 0F); // TOP_TWO18
		castBoxBackModel[19].setRotationPoint(7F, -34F, -37F);

		castBoxBackModel[20].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO18
		castBoxBackModel[20].setRotationPoint(-47F, -34F, -23F);

		castBoxBackModel[21].addBox(0F, 0F, 0F, 2, 4, 4, 0F); // TOP_TWO18
		castBoxBackModel[21].setRotationPoint(-43F, -34F, -23F);

		castBoxBackModel[22].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO18
		castBoxBackModel[22].setRotationPoint(9F, -34F, -23F);

		castBoxBackModel[23].addBox(0F, 0F, 0F, 2, 4, 4, 0F); // TOP_TWO18
		castBoxBackModel[23].setRotationPoint(7F, -34F, -23F);

		castBoxBackModel[24].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO18
		castBoxBackModel[24].setRotationPoint(-47F, -34F, -9F);

		castBoxBackModel[25].addBox(0F, 0F, 0F, 2, 4, 4, 0F); // TOP_TWO18
		castBoxBackModel[25].setRotationPoint(-43F, -34F, -9F);

		castBoxBackModel[26].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO18
		castBoxBackModel[26].setRotationPoint(9F, -34F, -9F);

		castBoxBackModel[27].addBox(0F, 0F, 0F, 2, 4, 4, 0F); // TOP_TWO18
		castBoxBackModel[27].setRotationPoint(7F, -34F, -9F);


		castTopBackModel = new ModelRendererTurbo[7];
		castTopBackModel[0] = new ModelRendererTurbo(this, 0, 48, textureX, textureY); // DOOR01
		castTopBackModel[1] = new ModelRendererTurbo(this, 8, 8, textureX, textureY); // DOOR02
		castTopBackModel[2] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // DOOR03
		castTopBackModel[3] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // DOOR06
		castTopBackModel[4] = new ModelRendererTurbo(this, 8, 8, textureX, textureY); // DOOR07
		castTopBackModel[5] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // DOOR09
		castTopBackModel[6] = new ModelRendererTurbo(this, 8, 8, textureX, textureY); // DOOR10

		castTopBackModel[0].addShapeBox(0F, 0F, 0F, 48, 21, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR01
		castTopBackModel[0].setRotationPoint(-48F, -23F, -45F);
		castTopBackModel[0].rotateAngleY = 1.57079633F;

		castTopBackModel[1].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR02
		castTopBackModel[1].setRotationPoint(-50F, -10F, -40F);

		castTopBackModel[2].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR03
		castTopBackModel[2].setRotationPoint(-50F, -19F, -40F);

		castTopBackModel[3].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR06
		castTopBackModel[3].setRotationPoint(-50F, -19F, -4F);

		castTopBackModel[4].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR07
		castTopBackModel[4].setRotationPoint(-50F, -10F, -4F);

		castTopBackModel[5].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR09
		castTopBackModel[5].setRotationPoint(-50F, -19F, -22F);

		castTopBackModel[6].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR10
		castTopBackModel[6].setRotationPoint(-50F, -10F, -22F);


		castDoorBackModel = new ModelRendererTurbo[4];
		castDoorBackModel[0] = new ModelRendererTurbo(this, 98, 48, textureX, textureY); // BAWX01
		castDoorBackModel[1] = new ModelRendererTurbo(this, 28, 191, textureX, textureY); // BAWX02
		castDoorBackModel[2] = new ModelRendererTurbo(this, 192, 156, textureX, textureY); // BAWX03
		castDoorBackModel[3] = new ModelRendererTurbo(this, 192, 134, textureX, textureY); // Box 103

		castDoorBackModel[0].addShapeBox(0F, 0F, 0F, 48, 22, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BAWX01
		castDoorBackModel[0].setRotationPoint(-29F, -23F, -45F);
		castDoorBackModel[0].rotateAngleY = 1.57079633F;

		castDoorBackModel[1].addShapeBox(0F, 0F, 0F, 19, 48, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BAWX02
		castDoorBackModel[1].setRotationPoint(-49F, -1F, -45F);
		castDoorBackModel[1].rotateAngleX = 1.57079633F;

		castDoorBackModel[2].addShapeBox(0F, 0F, 0F, 18, 21, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BAWX03
		castDoorBackModel[2].setRotationPoint(-48F, -23F, -45F);

		castDoorBackModel[3].addShapeBox(0F, 0F, 0F, 18, 21, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 103
		castDoorBackModel[3].setRotationPoint(-48F, -23F, 2F);



		translateAll(0F, 0F, 0F);


		flipAll();
	}
}
