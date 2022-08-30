package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * @author Pabilo8
 * @since 01.05.2021
 */
public class ModelVulcanizer extends ModelIIBase
{
	public ModelRendererTurbo[] rotatoModel, castTopModel, castBoxModel, castDoorModel, castHeatingModel, rollerModel;

	int textureX = 256;
	int textureY = 256;

	public ModelVulcanizer() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[68];
		baseModel[0] = new ModelRendererTurbo(this, 36, 208, textureX, textureY); // MAIN01
		baseModel[1] = new ModelRendererTurbo(this, 12, 12, textureX, textureY); // MAIN02
		baseModel[2] = new ModelRendererTurbo(this, 5, 110, textureX, textureY); // MAIN03
		baseModel[3] = new ModelRendererTurbo(this, 12, 12, textureX, textureY); // MAIN04
		baseModel[4] = new ModelRendererTurbo(this, 12, 12, textureX, textureY); // MAIN05
		baseModel[5] = new ModelRendererTurbo(this, 12, 12, textureX, textureY); // MAIN06
		baseModel[6] = new ModelRendererTurbo(this, 192, 178, textureX, textureY); // MAIN07
		baseModel[7] = new ModelRendererTurbo(this, 12, 12, textureX, textureY); // MAIN08
		baseModel[8] = new ModelRendererTurbo(this, 12, 12, textureX, textureY); // MAIN09
		baseModel[9] = new ModelRendererTurbo(this, 12, 12, textureX, textureY); // MAIN10
		baseModel[10] = new ModelRendererTurbo(this, 12, 12, textureX, textureY); // MAIN11
		baseModel[11] = new ModelRendererTurbo(this, 95, 101, textureX, textureY); // MAIN14
		baseModel[12] = new ModelRendererTurbo(this, 56, 71, textureX, textureY); // MAIN16
		baseModel[13] = new ModelRendererTurbo(this, 56, 101, textureX, textureY); // MAIN19
		baseModel[14] = new ModelRendererTurbo(this, 173, 115, textureX, textureY); // MAIN24
		baseModel[15] = new ModelRendererTurbo(this, 72, 101, textureX, textureY); // MAIN25
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
		baseModel[35] = new ModelRendererTurbo(this, 178, 83, textureX, textureY); // MAIN19
		baseModel[36] = new ModelRendererTurbo(this, 178, 83, textureX, textureY); // MAIN19
		baseModel[37] = new ModelRendererTurbo(this, 56, 101, textureX, textureY); // MAIN19
		baseModel[38] = new ModelRendererTurbo(this, 104, 192, textureX, textureY); // MAIN19
		baseModel[39] = new ModelRendererTurbo(this, 56, 101, textureX, textureY); // MAIN19
		baseModel[40] = new ModelRendererTurbo(this, 152, 136, textureX, textureY); // MAIN19
		baseModel[41] = new ModelRendererTurbo(this, 56, 81, textureX, textureY); // MAIN19
		baseModel[42] = new ModelRendererTurbo(this, 165, 71, textureX, textureY); // MAIN19
		baseModel[43] = new ModelRendererTurbo(this, 56, 81, textureX, textureY); // MAIN19
		baseModel[44] = new ModelRendererTurbo(this, 196, 51, textureX, textureY); // MAIN19
		baseModel[45] = new ModelRendererTurbo(this, 165, 93, textureX, textureY); // MAIN07
		baseModel[46] = new ModelRendererTurbo(this, 42, 70, textureX, textureY); // MAIN26
		baseModel[47] = new ModelRendererTurbo(this, 14, 8, textureX, textureY); // MAIN25
		baseModel[48] = new ModelRendererTurbo(this, 165, 93, textureX, textureY); // MAIN07
		baseModel[49] = new ModelRendererTurbo(this, 165, 93, textureX, textureY); // MAIN07
		baseModel[50] = new ModelRendererTurbo(this, 211, 66, textureX, textureY); // MAIN07
		baseModel[51] = new ModelRendererTurbo(this, 95, 127, textureX, textureY); // MAIN14
		baseModel[52] = new ModelRendererTurbo(this, 95, 127, textureX, textureY); // MAIN14
		baseModel[53] = new ModelRendererTurbo(this, 95, 127, textureX, textureY); // MAIN14
		baseModel[54] = new ModelRendererTurbo(this, 95, 127, textureX, textureY); // MAIN14
		baseModel[55] = new ModelRendererTurbo(this, 2, 198, textureX, textureY); // ProductionAxle5
		baseModel[56] = new ModelRendererTurbo(this, 2, 198, textureX, textureY); // ProductionAxle5
		baseModel[57] = new ModelRendererTurbo(this, 2, 198, textureX, textureY); // ProductionAxle5
		baseModel[58] = new ModelRendererTurbo(this, 2, 198, textureX, textureY); // ProductionAxle5
		baseModel[59] = new ModelRendererTurbo(this, 2, 198, textureX, textureY); // ProductionAxle5
		baseModel[60] = new ModelRendererTurbo(this, 2, 198, textureX, textureY); // ProductionAxle5
		baseModel[61] = new ModelRendererTurbo(this, 2, 198, textureX, textureY); // ProductionAxle5
		baseModel[62] = new ModelRendererTurbo(this, 2, 198, textureX, textureY); // ProductionAxle5
		baseModel[63] = new ModelRendererTurbo(this, 2, 198, textureX, textureY); // ProductionAxle5
		baseModel[64] = new ModelRendererTurbo(this, 2, 198, textureX, textureY); // ProductionAxle5
		baseModel[65] = new ModelRendererTurbo(this, 165, 237, textureX, textureY); // MAIN07
		baseModel[66] = new ModelRendererTurbo(this, 165, 237, textureX, textureY); // MAIN07
		baseModel[67] = new ModelRendererTurbo(this, 142, 211, textureX, textureY); // MAIN03

		baseModel[0].addShapeBox(0F, 0F, 0F, 32, 16, 32, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN01
		baseModel[0].setRotationPoint(-49F, 7F, -48F);

		baseModel[1].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN02
		baseModel[1].setRotationPoint(-47F, 4F, 44F);

		baseModel[2].addShapeBox(0F, 0F, 0F, 30, 42, 30, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN03
		baseModel[2].setRotationPoint(-48F, -38F, 17F);

		baseModel[3].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN04
		baseModel[3].setRotationPoint(-21F, 4F, 44F);

		baseModel[4].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN05
		baseModel[4].setRotationPoint(-21F, 4F, 18F);

		baseModel[5].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN06
		baseModel[5].setRotationPoint(-47F, 4F, 18F);

		baseModel[6].addShapeBox(0F, 0F, 0F, 16, 42, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN07
		baseModel[6].setRotationPoint(-1F, -38F, 31F);

		baseModel[7].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN08
		baseModel[7].setRotationPoint(1F, 4F, 44F);

		baseModel[8].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN09
		baseModel[8].setRotationPoint(1F, 4F, 17F);

		baseModel[9].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN10
		baseModel[9].setRotationPoint(11F, 4F, 44F);

		baseModel[10].addShapeBox(0F, 0F, 0F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN11
		baseModel[10].setRotationPoint(11F, 4F, 17F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 16, 3, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN14
		baseModel[11].setRotationPoint(-41F, 4F, 25F);

		baseModel[12].addShapeBox(0F, 0F, 0F, 22, 8, 22, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN16
		baseModel[12].setRotationPoint(-28F, -1F, -32F);

		baseModel[13].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN19
		baseModel[13].setRotationPoint(-14F, 3F, -42F);

		baseModel[14].addBox(0F, 0F, 0F, 18, 1, 18, 0F); // MAIN24
		baseModel[14].setRotationPoint(-26F, -2F, -30F);

		baseModel[15].addShapeBox(0F, 0F, 0F, 12, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN25
		baseModel[15].setRotationPoint(1F, -34F, 47F);

		baseModel[16].addShapeBox(0F, 0F, 0F, 8, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN26
		baseModel[16].setRotationPoint(-45F, -21F, 47F);

		baseModel[17].addShapeBox(0F, 0F, 0F, 3, 3, 26, 0F, 0F, 0F, 0F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, 0F); // Box 66
		baseModel[17].setRotationPoint(-7F, -3F, -34F);

		baseModel[18].setFlipped(true);
		baseModel[18].addShapeBox(0F, 0F, 0F, 3, 3, 26, 0F, -3F, 0F, 0F, -3F, 0F, -3F, -3F, 0F, -3F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, -3F, -3F, 0F, -3F, -3F, 0F, 0F); // Box 68
		baseModel[18].setRotationPoint(-30F, -3F, -34F);

		baseModel[19].addShapeBox(0F, 0F, 0F, 20, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 69
		baseModel[19].setRotationPoint(-27F, -3F, -34F);

		baseModel[20].addShapeBox(0F, 0F, 0F, 20, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 70
		baseModel[20].setRotationPoint(-27F, -3F, -11F);

		baseModel[21].addShapeBox(0F, 0F, 0F, 4, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN25
		baseModel[21].setRotationPoint(5F, -37F, 47F);

		baseModel[22].addShapeBox(0F, 0F, 0F, 4, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN25
		baseModel[22].setRotationPoint(-43F, -24F, 47F);

		baseModel[23].addShapeBox(0F, 0F, 0F, 64, 16, 32, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN01
		baseModel[23].setRotationPoint(-49F, 7F, 16F);

		baseModel[24].addShapeBox(0F, 0F, 0F, 32, 16, 32, 0F, -32F, 0F, -32F, -32F, 0F, -32F, -32F, 0F, -32F, -32F, 0F, -32F, -32F, 0F, -32F, -32F, 0F, -32F, -32F, 0F, -32F, -32F, 0F, -32F); // MAIN01
		baseModel[24].setRotationPoint(-17F, 7F, -16F);

		baseModel[25].setFlipped(true);
		baseModel[25].addShapeBox(0F, 0F, 0F, 32, 16, 32, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F); // MAIN01
		baseModel[25].setRotationPoint(-17F, 7F, -48F);

		baseModel[26].setFlipped(true);
		baseModel[26].addShapeBox(0F, 0F, 0F, 32, 16, 32, 0F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F, 0F, 0F, -32F); // MAIN01
		baseModel[26].setRotationPoint(-49F, 7F, -16F);

		baseModel[27].addShapeBox(0F, 0F, 0F, 12, 3, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN14
		baseModel[27].setRotationPoint(2F, 4F, 22F);

		baseModel[28].addShapeBox(0F, 0F, 0F, 6, 6, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN14
		baseModel[28].setRotationPoint(-12F, 1F, 26F);

		baseModel[29].addShapeBox(0F, 0F, 0F, 6, 6, 15, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN14
		baseModel[29].setRotationPoint(-12F, 1F, 11F);

		baseModel[30].addShapeBox(0F, 0F, 0F, 6, 6, 6, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN14
		baseModel[30].setRotationPoint(-12F, 1F, 5F);

		baseModel[31].addShapeBox(0F, 0F, 0F, 4, 6, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN14
		baseModel[31].setRotationPoint(-16F, 1F, 5F);

		baseModel[32].addShapeBox(0F, 0F, 0F, 6, 6, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // MAIN14
		baseModel[32].setRotationPoint(-22F, 1F, 5F);

		baseModel[33].addShapeBox(0F, 0F, 0F, 6, 6, 15, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN14
		baseModel[33].setRotationPoint(-22F, 1F, -10F);

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

		baseModel[45].addShapeBox(0F, 0F, 0F, 15, 6, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN07
		baseModel[45].setRotationPoint(0F, -2F, 15F);

		baseModel[46].addShapeBox(0F, 0F, 0F, 8, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN26
		baseModel[46].setRotationPoint(3F, -21F, 47F);

		baseModel[47].addShapeBox(0F, 0F, 0F, 4, 3, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN25
		baseModel[47].setRotationPoint(5F, -24F, 47F);

		baseModel[48].addShapeBox(0F, 0F, 0F, 15, 6, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN07
		baseModel[48].setRotationPoint(0F, -38F, 15F);

		baseModel[49].addShapeBox(0F, 0F, 0F, 15, 6, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN07
		baseModel[49].setRotationPoint(0F, -20F, 15F);

		baseModel[50].addShapeBox(0F, 0F, 0F, 16, 42, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN07
		baseModel[50].setRotationPoint(0F, -38F, 15F);
		baseModel[50].rotateAngleY = 1.57079633F;

		baseModel[51].addShapeBox(0F, 0F, 0F, 12, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN14
		baseModel[51].setRotationPoint(2F, -3F, 17F);

		baseModel[52].addShapeBox(0F, 0F, 0F, 12, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN14
		baseModel[52].setRotationPoint(2F, -14F, 25F);

		baseModel[53].addShapeBox(0F, 0F, 0F, 12, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN14
		baseModel[53].setRotationPoint(2F, -21F, 17F);

		baseModel[54].addShapeBox(0F, 0F, 0F, 12, 1, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN14
		baseModel[54].setRotationPoint(2F, -32F, 26F);

		baseModel[55].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle5
		baseModel[55].setRotationPoint(0F, -25F, 24F);

		baseModel[56].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle5
		baseModel[56].setRotationPoint(13F, -25F, 25F);

		baseModel[57].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle5
		baseModel[57].setRotationPoint(0F, -27F, 17F);

		baseModel[58].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle5
		baseModel[58].setRotationPoint(13F, -27F, 18F);

		baseModel[59].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle5
		baseModel[59].setRotationPoint(0F, 3F, 18F);

		baseModel[60].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle5
		baseModel[60].setRotationPoint(13F, 3F, 18F);

		baseModel[61].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle5
		baseModel[61].setRotationPoint(0F, -7F, 24F);

		baseModel[62].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle5
		baseModel[62].setRotationPoint(13F, -7F, 25F);

		baseModel[63].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle5
		baseModel[63].setRotationPoint(0F, -9F, 17F);

		baseModel[64].addBox(0F, -1F, -1F, 1, 2, 2, 0F); // ProductionAxle5
		baseModel[64].setRotationPoint(13F, -9F, 18F);

		baseModel[65].addShapeBox(0F, 0F, 0F, 16, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN07
		baseModel[65].setRotationPoint(15F, -29F, 15F);
		baseModel[65].rotateAngleY = 1.57079633F;

		baseModel[66].addShapeBox(0F, 0F, 0F, 16, 6, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN07
		baseModel[66].setRotationPoint(15F, -11F, 15F);
		baseModel[66].rotateAngleY = 1.57079633F;

		baseModel[67].addShapeBox(0F, 0F, 0F, 24, 16, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // MAIN03
		baseModel[67].setRotationPoint(-48F, -27F, 20F);
		baseModel[67].rotateAngleY = 1.57079633F;

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

		castTopModel = new ModelRendererTurbo[27];
		castTopModel[0] = new ModelRendererTurbo(this, 154, 156, textureX, textureY); // TOP_TWO01
		castTopModel[1] = new ModelRendererTurbo(this, 124, 188, textureX, textureY); // TOP_TWO02
		castTopModel[2] = new ModelRendererTurbo(this, 22, 182, textureX, textureY); // TOP_TWO03
		castTopModel[3] = new ModelRendererTurbo(this, 22, 182, textureX, textureY); // TOP_TWO04
		castTopModel[4] = new ModelRendererTurbo(this, 22, 182, textureX, textureY); // TOP_TWO05
		castTopModel[5] = new ModelRendererTurbo(this, 64, 70, textureX, textureY); // TOP_TWO06
		castTopModel[6] = new ModelRendererTurbo(this, 64, 70, textureX, textureY); // TOP_TWO07
		castTopModel[7] = new ModelRendererTurbo(this, 64, 70, textureX, textureY); // TOP_TWO08
		castTopModel[8] = new ModelRendererTurbo(this, 68, 184, textureX, textureY); // TOP_TWO09
		castTopModel[9] = new ModelRendererTurbo(this, 212, 0, textureX, textureY); // TOP_TWO10
		castTopModel[10] = new ModelRendererTurbo(this, 8, 25, textureX, textureY); // TOP_TWO11
		castTopModel[11] = new ModelRendererTurbo(this, 0, 16, textureX, textureY); // TOP_TWO12
		castTopModel[12] = new ModelRendererTurbo(this, 8, 25, textureX, textureY); // TOP_TWO13
		castTopModel[13] = new ModelRendererTurbo(this, 8, 25, textureX, textureY); // TOP_TWO14
		castTopModel[14] = new ModelRendererTurbo(this, 0, 16, textureX, textureY); // TOP_TWO17
		castTopModel[15] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // TOP_TWO18
		castTopModel[16] = new ModelRendererTurbo(this, 0, 16, textureX, textureY); // TOP_TWO19
		castTopModel[17] = new ModelRendererTurbo(this, 0, 8, textureX, textureY); // TOP_TWO18
		castTopModel[18] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // TOP_TWO18
		castTopModel[19] = new ModelRendererTurbo(this, 0, 8, textureX, textureY); // TOP_TWO18
		castTopModel[20] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // TOP_TWO18
		castTopModel[21] = new ModelRendererTurbo(this, 0, 8, textureX, textureY); // TOP_TWO18
		castTopModel[22] = new ModelRendererTurbo(this, 22, 182, textureX, textureY); // TOP_TWO06
		castTopModel[23] = new ModelRendererTurbo(this, 22, 182, textureX, textureY); // TOP_TWO07
		castTopModel[24] = new ModelRendererTurbo(this, 22, 182, textureX, textureY); // TOP_TWO08
		castTopModel[25] = new ModelRendererTurbo(this, 125, 228, textureX, textureY); // TOP_TWO01
		castTopModel[26] = new ModelRendererTurbo(this, 125, 228, textureX, textureY); // TOP_TWO02

		castTopModel[0].addShapeBox(0F, 0F, 0F, 12, 48, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO01
		castTopModel[0].setRotationPoint(-41F, -28F, -45F);
		castTopModel[0].rotateAngleX = 1.57079633F;

		castTopModel[1].addShapeBox(0F, 0F, 0F, 5, 3, 8, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F); // TOP_TWO02
		castTopModel[1].setRotationPoint(-29F, -35F, -25F);

		castTopModel[2].addShapeBox(0F, 0F, 0F, 10, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO03
		castTopModel[2].setRotationPoint(-33F, -37F, -27F);
		castTopModel[2].rotateAngleZ = 0.33161256F;

		castTopModel[3].addShapeBox(0F, 0F, 0F, 10, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO04
		castTopModel[3].setRotationPoint(-33F, -37F, -22F);
		castTopModel[3].rotateAngleZ = 0.33161256F;

		castTopModel[4].addShapeBox(0F, 0F, 0F, 10, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO05
		castTopModel[4].setRotationPoint(-33F, -37F, -17F);
		castTopModel[4].rotateAngleZ = 0.33161256F;

		castTopModel[5].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO06
		castTopModel[5].setRotationPoint(-34F, -37F, -17.01F);

		castTopModel[6].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO07
		castTopModel[6].setRotationPoint(-34F, -37F, -22.01F);

		castTopModel[7].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO08
		castTopModel[7].setRotationPoint(-34F, -37F, -27.01F);

		castTopModel[8].addShapeBox(0F, 0F, 0F, 8, 3, 16, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO09
		castTopModel[8].setRotationPoint(-39F, -28F, -28F);

		castTopModel[9].addShapeBox(0F, 0F, 0F, 20, 48, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO10
		castTopModel[9].setRotationPoint(-49F, -23F, -45F);
		castTopModel[9].rotateAngleX = 1.57079633F;

		castTopModel[10].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // TOP_TWO11
		castTopModel[10].setRotationPoint(-48F, -26F, -38F);

		castTopModel[11].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO12
		castTopModel[11].setRotationPoint(-47F, -30F, -23F);

		castTopModel[12].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // TOP_TWO13
		castTopModel[12].setRotationPoint(-48F, -26F, -24F);

		castTopModel[13].addBox(0F, 0F, 0F, 6, 1, 6, 0F); // TOP_TWO14
		castTopModel[13].setRotationPoint(-48F, -26F, -10F);

		castTopModel[14].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO17
		castTopModel[14].setRotationPoint(-47F, -30F, -9F);

		castTopModel[15].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO18
		castTopModel[15].setRotationPoint(-47F, -34F, -37F);

		castTopModel[16].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO19
		castTopModel[16].setRotationPoint(-47F, -30F, -37F);

		castTopModel[17].addShapeBox(0F, 0F, 0F, 2, 4, 4, 0F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F); // TOP_TWO18
		castTopModel[17].setRotationPoint(-43F, -34F, -37F);

		castTopModel[18].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO18
		castTopModel[18].setRotationPoint(-47F, -34F, -23F);

		castTopModel[19].addShapeBox(0F, 0F, 0F, 2, 4, 4, 0F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F); // TOP_TWO18
		castTopModel[19].setRotationPoint(-43F, -34F, -23F);

		castTopModel[20].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO18
		castTopModel[20].setRotationPoint(-47F, -34F, -9F);

		castTopModel[21].addShapeBox(0F, 0F, 0F, 2, 4, 4, 0F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F, 0F, -4F, -4F); // TOP_TWO18
		castTopModel[21].setRotationPoint(-43F, -34F, -9F);


		castTopModel[22].addShapeBox(0F, 0F, 0F, 10, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO03
		castTopModel[22].setRotationPoint(-23F, -37F, -27F);
		castTopModel[22].rotateAngleZ=0.000001f;
		castTopModel[22].hasOffset=true;

		castTopModel[23].addShapeBox(0F, 0F, 0F, 10, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO04
		castTopModel[23].setRotationPoint(-23F, -37F, -22F);
		castTopModel[23].rotateAngleZ=0.000001f;
		castTopModel[23].hasOffset=true;

		castTopModel[24].addShapeBox(0F, 0F, 0F, 10, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO05
		castTopModel[24].setRotationPoint(-23F, -37F, -17F);
		castTopModel[24].rotateAngleZ=0.000001f;
		castTopModel[24].hasOffset=true;

		castTopModel[25].addShapeBox(0F, 0F, 0F, 12, 0, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO01
		castTopModel[25].setRotationPoint(-41F, -27.5F, -44.8F);
		castTopModel[25].rotateAngleX = 1.57079633F;

		castTopModel[26].setFlipped(true);
		castTopModel[26].addShapeBox(0F, 0F, 0F, 12, 0, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO01
		castTopModel[26].setRotationPoint(-41F, -27.5F, 2.8F);
		castTopModel[26].rotateAngleX = 1.57079633F;

		castBoxModel = new ModelRendererTurbo[10];
		castBoxModel[0] = new ModelRendererTurbo(this, 0, 48, textureX, textureY); // DOOR01
		castBoxModel[1] = new ModelRendererTurbo(this, 8, 8, textureX, textureY); // DOOR02
		castBoxModel[2] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // DOOR03
		castBoxModel[3] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // DOOR06
		castBoxModel[4] = new ModelRendererTurbo(this, 8, 8, textureX, textureY); // DOOR07
		castBoxModel[5] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // DOOR09
		castBoxModel[6] = new ModelRendererTurbo(this, 8, 8, textureX, textureY); // DOOR10
		castBoxModel[7] = new ModelRendererTurbo(this, 26, 8, textureX, textureY); // DOOR04
		castBoxModel[8] = new ModelRendererTurbo(this, 26, 8, textureX, textureY); // DOOR05
		castBoxModel[9] = new ModelRendererTurbo(this, 26, 8, textureX, textureY); // DOOR08

		castBoxModel[0].addShapeBox(0F, 0F, 0F, 48, 21, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR01
		castBoxModel[0].setRotationPoint(-48F, -23F, -45F);
		castBoxModel[0].rotateAngleY = 1.57079633F;

		castBoxModel[1].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR02
		castBoxModel[1].setRotationPoint(-50F, -10F, -40F);

		castBoxModel[2].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR03
		castBoxModel[2].setRotationPoint(-50F, -19F, -40F);

		castBoxModel[3].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR06
		castBoxModel[3].setRotationPoint(-50F, -19F, -4F);

		castBoxModel[4].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR07
		castBoxModel[4].setRotationPoint(-50F, -10F, -4F);

		castBoxModel[5].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR09
		castBoxModel[5].setRotationPoint(-50F, -19F, -22F);

		castBoxModel[6].addShapeBox(0F, 0F, 0F, 1, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // DOOR10
		castBoxModel[6].setRotationPoint(-50F, -10F, -22F);

		castBoxModel[7].addShapeBox(0F, 0F, 0F, 1, 11, 2, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // DOOR04
		castBoxModel[7].setRotationPoint(-51F, -19F, -40F);

		castBoxModel[8].addShapeBox(0F, 0F, 0F, 1, 11, 2, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // DOOR05
		castBoxModel[8].setRotationPoint(-51F, -19F, -4F);

		castBoxModel[9].addShapeBox(0F, 0F, 0F, 1, 11, 2, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // DOOR08
		castBoxModel[9].setRotationPoint(-51F, -19F, -22F);


		castDoorModel = new ModelRendererTurbo[4];
		castDoorModel[0] = new ModelRendererTurbo(this, 98, 48, textureX, textureY); // BAWX01
		castDoorModel[1] = new ModelRendererTurbo(this, 28, 191, textureX, textureY); // BAWX02
		castDoorModel[2] = new ModelRendererTurbo(this, 192, 156, textureX, textureY); // BAWX03
		castDoorModel[3] = new ModelRendererTurbo(this, 192, 134, textureX, textureY); // Box 103

		castDoorModel[0].addShapeBox(0F, 0F, 0F, 48, 22, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BAWX01
		castDoorModel[0].setRotationPoint(-29F, -23F, -45F);
		castDoorModel[0].rotateAngleY = 1.57079633F;

		castDoorModel[1].addShapeBox(0F, 0F, 0F, 19, 48, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BAWX02
		castDoorModel[1].setRotationPoint(-49F, -1F, -45F);
		castDoorModel[1].rotateAngleX = 1.57079633F;

		castDoorModel[2].addShapeBox(0F, 0F, 0F, 18, 21, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BAWX03
		castDoorModel[2].setRotationPoint(-48F, -23F, -45F);

		castDoorModel[3].addShapeBox(0F, 0F, 0F, 18, 21, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 103
		castDoorModel[3].setRotationPoint(-48F, -23F, 2F);

		rollerModel = new ModelRendererTurbo[4];
		rollerModel[0] = new ModelRendererTurbo(this, 4, 216, textureX, textureY); // ProductionRoller3
		rollerModel[1] = new ModelRendererTurbo(this, 4, 216, textureX, textureY); // ProductionRoller3
		rollerModel[2] = new ModelRendererTurbo(this, 4, 216, textureX, textureY); // ProductionRoller3
		rollerModel[3] = new ModelRendererTurbo(this, 4, 216, textureX, textureY); // ProductionRoller3

		//rollerModel[0].setFlipped(true);
		rollerModel[0].addShape3D(3F, -3F, 0F, new Shape2D(new Coord2D[]{new Coord2D(1, 0, 1, 0), new Coord2D(5, 0, 5, 0), new Coord2D(6, 1, 6, 1), new Coord2D(6, 5, 6, 5), new Coord2D(5, 6, 5, 6), new Coord2D(1, 6, 1, 6), new Coord2D(0, 5, 0, 5), new Coord2D(0, 1, 0, 1)}), 12, 6, 6, 24, 12, ModelRendererTurbo.MR_FRONT, new float[]{2, 4, 2, 4, 2, 4, 2, 4}); // ProductionRoller3
		rollerModel[0].setRotationPoint(1F, -25F, 25F);
		rollerModel[0].rotateAngleY = 1.57079633F;

		//rollerModel[1].setFlipped(true);
		rollerModel[1].addShape3D(3F, -3F, 0F, new Shape2D(new Coord2D[]{new Coord2D(1, 0, 1, 0), new Coord2D(5, 0, 5, 0), new Coord2D(6, 1, 6, 1), new Coord2D(6, 5, 6, 5), new Coord2D(5, 6, 5, 6), new Coord2D(1, 6, 1, 6), new Coord2D(0, 5, 0, 5), new Coord2D(0, 1, 0, 1)}), 12, 6, 6, 24, 12, ModelRendererTurbo.MR_FRONT, new float[]{2, 4, 2, 4, 2, 4, 2, 4}); // ProductionRoller3
		rollerModel[1].setRotationPoint(1F, -27F, 18F);
		rollerModel[1].rotateAngleY = 1.57079633F;

		//rollerModel[2].setFlipped(true);
		rollerModel[2].addShape3D(3F, -3F, 0F, new Shape2D(new Coord2D[]{new Coord2D(1, 0, 1, 0), new Coord2D(5, 0, 5, 0), new Coord2D(6, 1, 6, 1), new Coord2D(6, 5, 6, 5), new Coord2D(5, 6, 5, 6), new Coord2D(1, 6, 1, 6), new Coord2D(0, 5, 0, 5), new Coord2D(0, 1, 0, 1)}), 12, 6, 6, 24, 12, ModelRendererTurbo.MR_FRONT, new float[]{2, 4, 2, 4, 2, 4, 2, 4}); // ProductionRoller3
		rollerModel[2].setRotationPoint(1F, -7F, 25F);
		rollerModel[2].rotateAngleY = 1.57079633F;

		//rollerModel[3].setFlipped(true);
		rollerModel[3].addShape3D(3F, -3F, 0F, new Shape2D(new Coord2D[]{new Coord2D(1, 0, 1, 0), new Coord2D(5, 0, 5, 0), new Coord2D(6, 1, 6, 1), new Coord2D(6, 5, 6, 5), new Coord2D(5, 6, 5, 6), new Coord2D(1, 6, 1, 6), new Coord2D(0, 5, 0, 5), new Coord2D(0, 1, 0, 1)}), 12, 6, 6, 24, 12, ModelRendererTurbo.MR_FRONT, new float[]{2, 4, 2, 4, 2, 4, 2, 4}); // ProductionRoller3
		rollerModel[3].setRotationPoint(1F, -9F, 18F);
		rollerModel[3].rotateAngleY = 1.57079633F;

		castHeatingModel = new ModelRendererTurbo[2];
		castHeatingModel[0] = new ModelRendererTurbo(this, 125, 233, textureX, textureY); // TOP_TWO01
		castHeatingModel[1] = new ModelRendererTurbo(this, 125, 233, textureX, textureY); // TOP_TWO02

		castHeatingModel[0].addShapeBox(0F, 0F, 0F, 12, 0, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO01
		castHeatingModel[0].setRotationPoint(-41F, -28F, -44.9F);
		castHeatingModel[0].rotateAngleX = 1.57079633F;

		castHeatingModel[1].addShapeBox(0F, 0F, 0F, 12, 0, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO01
		castHeatingModel[1].setRotationPoint(-41F, -28F, 2.9F);
		castHeatingModel[1].rotateAngleX = 1.57079633F;

		parts.put("base", baseModel);
		parts.put("rotato", rotatoModel);
		parts.put("castBoxBack", castTopModel);
		parts.put("castTopBack", castBoxModel);
		parts.put("castDoorBack", castDoorModel);
		parts.put("castHeating", castHeatingModel);
		parts.put("roller", rollerModel);

		flipAll();

		translateAll(1, 7, 0);
		translate(rotatoModel, 24-7f, 0, -28F+7f);
		//translate(castBoxModel, 24-7f, 0, );
		translate(castTopModel, 24-7f, 0, -28F+7f);
		translate(castDoorModel, 24-7f, 0, -28F+7f);
		translate(castHeatingModel, 24-7f, 0, -28F+7f);

		translate(castBoxModel,47.5f, -8.5f-21, -28F+7f);
	}

	@Override
	public void getBlockRotation(EnumFacing facing, boolean mirrored)
	{
		switch(facing)
		{
			case NORTH:
			{

				if(!mirrored)
				{
					GlStateManager.rotate(180F, 0F, 1F, 0F);
					GlStateManager.translate(-2f, 0f, -1f);
				}
				else
					GlStateManager.translate(2f, 0f, 0f);

				//GlStateManager.translate(mirrored?-1f: 0f, -1f, 0f);
			}
			break;
			case SOUTH:
			{
				if(mirrored)
				{
					GlStateManager.rotate(180F, 0F, 1F, 0F);
					GlStateManager.translate(1f, 0f, -1f);
				}
				else
					GlStateManager.translate(-1f, 0f, 0f);

				//GlStateManager.translate(mirrored?0f: -1f, -1f, mirrored?-1f: 1f);
			}
			break;
			case EAST:
			{
				GlStateManager.rotate(mirrored?270f: 90F, 0F, 1F, 0F);
				if(!mirrored)
					GlStateManager.translate(-2f, 0f, 0f);
				else
					GlStateManager.translate(2f, 0f, -1f);
			}
			break;
			case WEST:
			{

				GlStateManager.rotate(mirrored?90f: 270F, 0F, 1F, 0F);
				if(!mirrored)
					GlStateManager.translate(-1f, 0f, -1f);
				else
					GlStateManager.translate(1f, 0f, 0f);
				//GlStateManager.translate(mirrored?-1f: 0f, -1f, mirrored?-1f: 1f);

			}
			break;

		}

	}
}
