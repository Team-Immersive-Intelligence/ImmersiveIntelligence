package pl.pabilo8.immersiveintelligence.client.model.multiblock.wooden;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.model.BaseBlockModel;
import pl.pabilo8.immersiveintelligence.client.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.tmt.Shape2D;

/**
 * Created by Pabilo8 on 2020-04-13.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class ModelSawmill extends BaseBlockModel
{
	public ModelRendererTurbo[] baseTransparentModel, inserterBaseModel, inserterMovingPartModel, axleModel, sawdustModel;
	int textureX = 256;
	int textureY = 128;

	public ModelSawmill() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[82];
		baseModel[0] = new ModelRendererTurbo(this, 136, 42, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 102, 35, textureX, textureY); // Box 0
		baseModel[2] = new ModelRendererTurbo(this, 102, 35, textureX, textureY); // Box 0
		baseModel[3] = new ModelRendererTurbo(this, 152, 0, textureX, textureY); // Box 0
		baseModel[4] = new ModelRendererTurbo(this, 72, 45, textureX, textureY); // Box 0
		baseModel[5] = new ModelRendererTurbo(this, 0, 55, textureX, textureY); // Box 0
		baseModel[6] = new ModelRendererTurbo(this, 0, 55, textureX, textureY); // Box 0
		baseModel[7] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[8] = new ModelRendererTurbo(this, 0, 116, textureX, textureY); // Box 0
		baseModel[9] = new ModelRendererTurbo(this, 0, 55, textureX, textureY); // Box 0
		baseModel[10] = new ModelRendererTurbo(this, 0, 55, textureX, textureY); // Box 0
		baseModel[11] = new ModelRendererTurbo(this, 138, 13, textureX, textureY); // Box 0
		baseModel[12] = new ModelRendererTurbo(this, 54, 19, textureX, textureY); // Box 0
		baseModel[13] = new ModelRendererTurbo(this, 152, 23, textureX, textureY); // Box 0
		baseModel[14] = new ModelRendererTurbo(this, 4, 5, textureX, textureY); // Box 0
		baseModel[15] = new ModelRendererTurbo(this, 125, 42, textureX, textureY); // Box 0
		baseModel[16] = new ModelRendererTurbo(this, 130, 27, textureX, textureY); // Box 0
		baseModel[17] = new ModelRendererTurbo(this, 128, 12, textureX, textureY); // Box 0
		baseModel[18] = new ModelRendererTurbo(this, 130, 1, textureX, textureY); // Box 0
		baseModel[19] = new ModelRendererTurbo(this, 125, 26, textureX, textureY); // Box 0
		baseModel[20] = new ModelRendererTurbo(this, 125, 26, textureX, textureY); // Box 0
		baseModel[21] = new ModelRendererTurbo(this, 90, 35, textureX, textureY); // Box 0
		baseModel[22] = new ModelRendererTurbo(this, 90, 35, textureX, textureY); // Box 0
		baseModel[23] = new ModelRendererTurbo(this, 104, 82, textureX, textureY); // Box 0
		baseModel[24] = new ModelRendererTurbo(this, 24, 107, textureX, textureY); // Box 0
		baseModel[25] = new ModelRendererTurbo(this, 72, 62, textureX, textureY); // Box 0
		baseModel[26] = new ModelRendererTurbo(this, 101, 75, textureX, textureY); // Box 0
		baseModel[27] = new ModelRendererTurbo(this, 92, 77, textureX, textureY); // Box 0
		baseModel[28] = new ModelRendererTurbo(this, 120, 33, textureX, textureY); // Box 0
		baseModel[29] = new ModelRendererTurbo(this, 112, 1, textureX, textureY); // Box 0
		baseModel[30] = new ModelRendererTurbo(this, 108, 117, textureX, textureY); // Box 0
		baseModel[31] = new ModelRendererTurbo(this, 100, 29, textureX, textureY); // Box 0
		baseModel[32] = new ModelRendererTurbo(this, 138, 23, textureX, textureY); // Box 0
		baseModel[33] = new ModelRendererTurbo(this, 102, 108, textureX, textureY); // Box 0
		baseModel[34] = new ModelRendererTurbo(this, 104, 101, textureX, textureY); // Box 0
		baseModel[35] = new ModelRendererTurbo(this, 0, 88, textureX, textureY); // Box 0
		baseModel[36] = new ModelRendererTurbo(this, 94, 75, textureX, textureY); // Box 0
		baseModel[37] = new ModelRendererTurbo(this, 84, 75, textureX, textureY); // Box 0
		baseModel[38] = new ModelRendererTurbo(this, 94, 106, textureX, textureY); // Box 0
		baseModel[39] = new ModelRendererTurbo(this, 114, 23, textureX, textureY); // Box 0
		baseModel[40] = new ModelRendererTurbo(this, 114, 19, textureX, textureY); // Box 0
		baseModel[41] = new ModelRendererTurbo(this, 106, 43, textureX, textureY); // Box 0
		baseModel[42] = new ModelRendererTurbo(this, 114, 29, textureX, textureY); // Box 0
		baseModel[43] = new ModelRendererTurbo(this, 107, 38, textureX, textureY); // Box 0
		baseModel[44] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		baseModel[45] = new ModelRendererTurbo(this, 0, 10, textureX, textureY); // Box 0
		baseModel[46] = new ModelRendererTurbo(this, 11, 8, textureX, textureY); // Box 0
		baseModel[47] = new ModelRendererTurbo(this, 11, 6, textureX, textureY); // Box 0
		baseModel[48] = new ModelRendererTurbo(this, 11, 4, textureX, textureY); // Box 0
		baseModel[49] = new ModelRendererTurbo(this, 72, 75, textureX, textureY); // Box 0
		baseModel[50] = new ModelRendererTurbo(this, 0, 79, textureX, textureY); // Box 0
		baseModel[51] = new ModelRendererTurbo(this, 90, 35, textureX, textureY); // Box 0
		baseModel[52] = new ModelRendererTurbo(this, 90, 35, textureX, textureY); // Box 0
		baseModel[53] = new ModelRendererTurbo(this, 24, 107, textureX, textureY); // Box 0
		baseModel[54] = new ModelRendererTurbo(this, 125, 26, textureX, textureY); // Box 0
		baseModel[55] = new ModelRendererTurbo(this, 125, 26, textureX, textureY); // Box 0
		baseModel[56] = new ModelRendererTurbo(this, 104, 82, textureX, textureY); // Box 0
		baseModel[57] = new ModelRendererTurbo(this, 24, 96, textureX, textureY); // Box 0
		baseModel[58] = new ModelRendererTurbo(this, 64, 79, textureX, textureY); // Box 0
		baseModel[59] = new ModelRendererTurbo(this, 0, 96, textureX, textureY); // Box 0
		baseModel[60] = new ModelRendererTurbo(this, 0, 27, textureX, textureY); // Box 0
		baseModel[61] = new ModelRendererTurbo(this, 54, 34, textureX, textureY); // Box 0
		baseModel[62] = new ModelRendererTurbo(this, 74, 30, textureX, textureY); // Box 0
		baseModel[63] = new ModelRendererTurbo(this, 40, 110, textureX, textureY); // Box 0
		baseModel[64] = new ModelRendererTurbo(this, 2, 125, textureX, textureY); // Box 0
		baseModel[65] = new ModelRendererTurbo(this, 54, 104, textureX, textureY); // Box 0
		baseModel[66] = new ModelRendererTurbo(this, 8, 2, textureX, textureY); // Box 0
		baseModel[67] = new ModelRendererTurbo(this, 12, 2, textureX, textureY); // Box 0
		baseModel[68] = new ModelRendererTurbo(this, 14, 19, textureX, textureY); // Box 0
		baseModel[69] = new ModelRendererTurbo(this, 42, 19, textureX, textureY); // Box 0
		baseModel[70] = new ModelRendererTurbo(this, 54, 29, textureX, textureY); // Shape 95
		baseModel[71] = new ModelRendererTurbo(this, 42, 19, textureX, textureY); // Box 0
		baseModel[72] = new ModelRendererTurbo(this, 36, 19, textureX, textureY); // Box 0
		baseModel[73] = new ModelRendererTurbo(this, 12, 68, textureX, textureY); // Box 0
		baseModel[74] = new ModelRendererTurbo(this, 12, 79, textureX, textureY); // Box 0
		baseModel[75] = new ModelRendererTurbo(this, 12, 79, textureX, textureY); // Box 0
		baseModel[76] = new ModelRendererTurbo(this, 0, 84, textureX, textureY); // Box 0
		baseModel[77] = new ModelRendererTurbo(this, 12, 84, textureX, textureY); // Box 0
		baseModel[78] = new ModelRendererTurbo(this, 4, 84, textureX, textureY); // Box 0
		baseModel[79] = new ModelRendererTurbo(this, 4, 86, textureX, textureY); // Box 0
		baseModel[80] = new ModelRendererTurbo(this, 52, 19, textureX, textureY); // Box 0
		baseModel[81] = new ModelRendererTurbo(this, 48, 19, textureX, textureY); // Box 0

		baseModel[0].addBox(0F, 0F, 0F, 16, 1, 16, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, -1F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 1, 11, 16, 0F); // Box 0
		baseModel[1].setRotationPoint(0F, -12F, 0F);

		baseModel[2].addBox(0F, 0F, 0F, 1, 11, 16, 0F); // Box 0
		baseModel[2].setRotationPoint(15F, -12F, 0F);

		baseModel[3].addBox(0F, 0F, 0F, 14, 11, 1, 0F); // Box 0
		baseModel[3].setRotationPoint(1F, -12F, 15F);

		baseModel[4].addBox(0F, 0F, 0F, 14, 16, 1, 0F); // Box 0
		baseModel[4].setRotationPoint(1F, -12F, 15F);
		baseModel[4].rotateAngleX = -0.80285146F;

		baseModel[5].addBox(0F, 0F, 0F, 3, 13, 3, 0F); // Box 0
		baseModel[5].setRotationPoint(17F, -13F, -15F);

		baseModel[6].addBox(0F, 0F, 0F, 3, 13, 3, 0F); // Box 0
		baseModel[6].setRotationPoint(17F, -13F, 12F);

		baseModel[7].addBox(0F, 0F, 0F, 48, 3, 16, 0F); // Box 0
		baseModel[7].setRotationPoint(16F, -16F, -16F);

		baseModel[8].addBox(0F, 0F, 0F, 48, 3, 6, 0F); // Box 0
		baseModel[8].setRotationPoint(16F, -16F, 10F);

		baseModel[9].addBox(0F, 0F, 0F, 3, 13, 3, 0F); // Box 0
		baseModel[9].setRotationPoint(60F, -13F, -15F);

		baseModel[10].addBox(0F, 0F, 0F, 3, 13, 3, 0F); // Box 0
		baseModel[10].setRotationPoint(60F, -13F, 12F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 26, 2, 8, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 2F, 0F); // Box 0
		baseModel[11].setRotationPoint(14F, -16F, 2F);

		baseModel[12].addBox(0F, 0F, 0F, 26, 2, 8, 0F); // Box 0
		baseModel[12].setRotationPoint(40F, -16F, 2F);

		baseModel[13].addBox(0F, 0F, 0F, 2, 3, 6, 0F); // Box 0
		baseModel[13].setRotationPoint(14F, -16F, -16F);

		baseModel[14].addBox(0F, 0F, 0F, 1, 3, 5, 0F); // Box 0
		baseModel[14].setRotationPoint(15F, -16F, -5F);

		baseModel[15].addBox(0F, 0F, 0F, 2, 3, 5, 0F); // Box 0
		baseModel[15].setRotationPoint(64F, -16F, -10F);

		baseModel[16].addBox(0F, 0F, 0F, 1, 3, 5, 0F); // Box 0
		baseModel[16].setRotationPoint(64F, -16F, -5F);

		baseModel[17].addBox(0F, 3F, 0F, 8, 8, 1, 0F); // Box 0
		baseModel[17].setRotationPoint(36F, -16F, 15F);

		baseModel[18].addBox(0F, 3F, 0F, 6, 6, 5, 0F); // Box 0
		baseModel[18].setRotationPoint(37F, -16F, 10F);

		baseModel[19].addShapeBox(0F, 0F, 0F, 2, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F); // Box 0
		baseModel[19].setRotationPoint(60.5F, -13F, -12F);

		baseModel[20].addShapeBox(0F, 0F, 0F, 2, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[20].setRotationPoint(60.5F, -13F, 9F);

		baseModel[21].addShapeBox(0F, 0F, 0F, 3, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F); // Box 0
		baseModel[21].setRotationPoint(57F, -13F, 12.5F);

		baseModel[22].addShapeBox(0F, 0F, 0F, 3, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[22].setRotationPoint(20F, -13F, 12.5F);

		baseModel[23].addBox(0F, 0F, 0F, 2, 1, 18, 0F); // Box 0
		baseModel[23].setRotationPoint(60.5F, -13F, -9F);

		baseModel[24].addBox(0F, 0F, 0F, 34, 1, 2, 0F); // Box 0
		baseModel[24].setRotationPoint(23F, -13F, 12.5F);

		baseModel[25].addBox(0F, 0F, 0F, 17, 0, 13, 0F); // Box 0
		baseModel[25].setRotationPoint(15F, -16.05F, -16F);
		baseModel[25].flip = true;

		baseModel[26].addBox(0F, 0F, 0F, 17, 0, 5, 0F); // Box 0
		baseModel[26].setRotationPoint(15F, -16.05F, -16F);
		baseModel[26].rotateAngleX = -1.6406095F;
		baseModel[26].flip = true;

		baseModel[27].addBox(-2F, 0F, 0F, 2, 0, 5, 0F); // Box 0
		baseModel[27].setRotationPoint(16F, -16.05F, -10F);
		baseModel[27].rotateAngleZ = 0.61086524F;
		baseModel[27].flip = true;

		baseModel[28].addBox(0F, 3F, 0F, 4, 4, 1, 0F); // Box 0
		baseModel[28].setRotationPoint(38F, -16F, 9F);

		baseModel[29].addBox(0F, 3F, 0F, 2, 2, 7, 0F); // Box 0
		baseModel[29].setRotationPoint(39F, -15F, 2F);

		baseModel[30].addBox(0F, 3F, 0F, 2, 2, 7, 0F); // Box 0
		baseModel[30].setRotationPoint(39F, -15F, -7F);

		baseModel[31].addBox(0F, 3F, 0F, 4, 4, 3, 0F); // Box 0
		baseModel[31].setRotationPoint(38F, -16F, -10F);

		baseModel[32].addBox(0F, 3F, 0F, 2, 2, 1, 0F); // Box 0
		baseModel[32].setRotationPoint(39F, -15F, -11F);

		baseModel[33].addShapeBox(0F, 0F, 0F, 12, 1, 8, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[33].setRotationPoint(18F, -17F, -13F);
		baseModel[33].rotateAngleY = 0.41887902F;

		baseModel[34].addBox(0F, 0F, 0F, 8, 1, 6, 0F); // Box 0
		baseModel[34].setRotationPoint(52F, -17F, 11F);

		baseModel[35].addBox(0F, 0F, 0F, 4, 3, 4, 0F); // Box 0
		baseModel[35].setRotationPoint(54F, -20F, 13F);

		baseModel[36].addBox(0F, 0F, 0F, 4, 1, 1, 0F); // Box 0
		baseModel[36].setRotationPoint(54F, -21F, 16F);

		baseModel[37].addShapeBox(0F, 0F, 0F, 4, 1, 1, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[37].setRotationPoint(54F, -21F, 15F);

		baseModel[38].addBox(0F, 0F, 0F, 4, 1, 2, 0F); // Box 0
		baseModel[38].setRotationPoint(54F, -21F, 18F);

		baseModel[39].addShapeBox(0F, 0F, 0F, 4, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[39].setRotationPoint(54F, -21F, 20F);

		baseModel[40].addBox(0F, 0F, 0F, 4, 1, 3, 0F); // Box 0
		baseModel[40].setRotationPoint(54F, -20F, 19F);

		baseModel[41].addBox(0F, 0F, 0F, 3, 2, 3, 0F); // Box 0
		baseModel[41].setRotationPoint(54.5F, -19F, 19F);

		baseModel[42].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // Box 0
		baseModel[42].setRotationPoint(55F, -19F, 17F);

		baseModel[43].addBox(0F, 0F, 0F, 2, 2, 3, 0F); // Box 0
		baseModel[43].setRotationPoint(55F, -19F, 10F);

		baseModel[44].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[44].setRotationPoint(55.5F, -19F, 22F);

		baseModel[45].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[45].setRotationPoint(58.5F, -18F, 13F);

		baseModel[46].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[46].setRotationPoint(52.5F, -18F, 13F);

		baseModel[47].addBox(2.5F, -0.5F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[47].setRotationPoint(56F, -18.5F, 22F);
		baseModel[47].rotateAngleZ = 0.52359878F;

		baseModel[48].addBox(-3.5F, -0.5F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[48].setRotationPoint(56F, -18.5F, 22F);
		baseModel[48].rotateAngleZ = 0.52359878F;

		baseModel[49].addShapeBox(-2.5F, -0.5F, 0F, 5, 1, 1, 0F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F); // Box 0
		baseModel[49].setRotationPoint(56F, -18.5F, 22F);
		baseModel[49].rotateAngleZ = 0.52359878F;

		baseModel[50].addBox(0F, 0F, 0F, 16, 1, 16, 0F); // Box 0
		baseModel[50].setRotationPoint(48F, -17F, -16F);

		baseModel[51].addShapeBox(0F, 0F, 0F, 3, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F); // Box 0
		baseModel[51].setRotationPoint(57F, -13F, -14.5F);

		baseModel[52].addShapeBox(0F, 0F, 0F, 3, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[52].setRotationPoint(20F, -13F, -14.5F);

		baseModel[53].addBox(0F, 0F, 0F, 34, 1, 2, 0F); // Box 0
		baseModel[53].setRotationPoint(23F, -13F, -14.5F);

		baseModel[54].addShapeBox(0F, 0F, 0F, 2, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F); // Box 0
		baseModel[54].setRotationPoint(17.5F, -13F, -12F);

		baseModel[55].addShapeBox(0F, 0F, 0F, 2, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -2F, 0F, 0F, -2F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[55].setRotationPoint(17.5F, -13F, 9F);

		baseModel[56].addBox(0F, 0F, 0F, 2, 1, 18, 0F); // Box 0
		baseModel[56].setRotationPoint(17.5F, -13F, -9F);

		baseModel[57].addBox(0F, 0F, 0F, 10, 1, 10, 0F); // Box 0
		baseModel[57].setRotationPoint(48F, -13F, -12.5F);

		baseModel[58].addBox(0F, 0F, 0F, 8, 8, 12, 0F); // Box 0
		baseModel[58].setRotationPoint(55F, -8F, 2F);
		baseModel[58].rotateAngleY = 0.55850536F;

		baseModel[59].addBox(0F, 0F, 0F, 4, 4, 16, 0F); // Box 0
		baseModel[59].setRotationPoint(51F, -12F, -6F);

		baseModel[60].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[60].setRotationPoint(51F, -12F, -10F);

		baseModel[61].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 0
		baseModel[61].setRotationPoint(43F, -12F, 10F);

		baseModel[62].addShapeBox(0F, 0F, 0F, 4, 4, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[62].setRotationPoint(51F, -12F, 10F);

		baseModel[63].addBox(0F, 0F, 0F, 28, 1, 5, 0F); // Box 0
		baseModel[63].setRotationPoint(34F, -18F, -12F);

		baseModel[64].addBox(0F, 0F, 0F, 26, 1, 1, 0F); // Box 0
		baseModel[64].setRotationPoint(34F, -19F, -12F);

		baseModel[65].addBox(0F, 0F, 0F, 26, 1, 1, 0F); // Box 0
		baseModel[65].setRotationPoint(34F, -19F, -8F);

		baseModel[66].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[66].setRotationPoint(60.5F, -19F, -8.5F);

		baseModel[67].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[67].setRotationPoint(60.5F, -19F, -11.5F);

		baseModel[68].addBox(0F, 0F, 0F, 1, 1, 4, 0F); // Box 0
		baseModel[68].setRotationPoint(22F, -17F, -13F);
		baseModel[68].rotateAngleX = 0.06981317F;
		baseModel[68].rotateAngleY = 0.17453293F;

		baseModel[69].addBox(0F, 0F, 0F, 1, 4, 2, 0F); // Box 0
		baseModel[69].setRotationPoint(25F, -21F, -12F);

		baseModel[70].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(1, 0, 1, 0), new Coord2D(3, 0, 3, 0), new Coord2D(4, 1, 4, 1), new Coord2D(4, 3, 4, 3), new Coord2D(3, 4, 3, 4), new Coord2D(1, 4, 1, 4), new Coord2D(0, 3, 0, 3), new Coord2D(0, 1, 0, 1)}), 1, 4, 4, 16, 1, ModelRendererTurbo.MR_FRONT, new float[]{2, 2, 2, 2, 2, 2, 2, 2}); // Shape 95
		baseModel[70].setRotationPoint(29F, -17F, -9F);
		baseModel[70].rotateAngleX = 1.57079633F;

		baseModel[71].addBox(0F, 0F, 0F, 1, 4, 2, 0F); // Box 0
		baseModel[71].setRotationPoint(28F, -21F, -12F);

		baseModel[72].addBox(0F, 0F, 0F, 2, 4, 1, 0F); // Box 0
		baseModel[72].setRotationPoint(26F, -21F, -10F);

		baseModel[73].addBox(0F, 0F, 0F, 2, 4, 1, 0F); // Box 0
		baseModel[73].setRotationPoint(26F, -21F, -13F);

		baseModel[74].addShapeBox(0F, 0F, 0F, 1, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[74].setRotationPoint(28F, -21F, -10F);

		baseModel[75].addShapeBox(0F, 0F, 0F, 1, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // Box 0
		baseModel[75].setRotationPoint(25F, -21F, -10F);

		baseModel[76].addShapeBox(0F, 0F, 0F, 1, 4, 1, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[76].setRotationPoint(28F, -21F, -13F);

		baseModel[77].addShapeBox(0F, 0F, 0F, 1, 4, 1, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[77].setRotationPoint(25F, -21F, -13F);

		baseModel[78].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[78].setRotationPoint(28.5F, -20F, -11.5F);

		baseModel[79].addBox(0F, 0F, 0F, 1, 1, 1, 0F); // Box 0
		baseModel[79].setRotationPoint(28.5F, -18F, -11.5F);

		baseModel[80].addShapeBox(0F, 0F, 0F, 1, 3, 1, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[80].setRotationPoint(29.5F, -20F, -11.5F);

		baseModel[81].addBox(0F, -5F, 0F, 1, 5, 1, 0F); // Box 0
		baseModel[81].setRotationPoint(26.5F, -17F, -11.5F);
		baseModel[81].rotateAngleY = -1.13446401F;
		baseModel[81].rotateAngleZ = 0.29670597F;

		baseTransparentModel = new ModelRendererTurbo[10];
		baseTransparentModel[0] = new ModelRendererTurbo(this, 0, 35, textureX, textureY); // Box 0
		baseTransparentModel[1] = new ModelRendererTurbo(this, 24, 19, textureX, textureY); // Box 0
		baseTransparentModel[2] = new ModelRendererTurbo(this, 0, 2, textureX, textureY); // Box 0
		baseTransparentModel[3] = new ModelRendererTurbo(this, 0, 35, textureX, textureY); // Box 0
		baseTransparentModel[4] = new ModelRendererTurbo(this, 24, 19, textureX, textureY); // Box 0
		baseTransparentModel[5] = new ModelRendererTurbo(this, 0, 2, textureX, textureY); // Box 0
		baseTransparentModel[6] = new ModelRendererTurbo(this, 119, 65, textureX, textureY); // Box 0
		baseTransparentModel[7] = new ModelRendererTurbo(this, 147, 70, textureX, textureY); // Box 0
		baseTransparentModel[8] = new ModelRendererTurbo(this, 119, 70, textureX, textureY); // Box 0
		baseTransparentModel[9] = new ModelRendererTurbo(this, 147, 65, textureX, textureY); // Box 0

		baseTransparentModel[0].addBox(-1.5F, 4F, -1.5F, 3, 7, 3, 0F); // Box 0
		baseTransparentModel[0].setRotationPoint(18F, -11F, 10F);
		baseTransparentModel[0].rotateAngleZ = 0.15707963F;

		baseTransparentModel[1].addShapeBox(-1.5F, 2F, -1.5F, 3, 2, 3, 0F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseTransparentModel[1].setRotationPoint(18F, -11F, 10F);
		baseTransparentModel[1].rotateAngleZ = 0.15707963F;

		baseTransparentModel[2].addBox(-0.5F, 0F, -0.5F, 1, 2, 1, 0F); // Box 0
		baseTransparentModel[2].setRotationPoint(18F, -11F, 10F);
		baseTransparentModel[2].rotateAngleZ = 0.15707963F;

		baseTransparentModel[3].addBox(-1.5F, 4F, -1.5F, 3, 7, 3, 0F); // Box 0
		baseTransparentModel[3].setRotationPoint(19F, -11F, 8F);
		baseTransparentModel[3].rotateAngleY = -1.57079633F;
		baseTransparentModel[3].rotateAngleZ = 0.15707963F;

		baseTransparentModel[4].addShapeBox(-1.5F, 2F, -1.5F, 3, 2, 3, 0F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseTransparentModel[4].setRotationPoint(19F, -11F, 8F);
		baseTransparentModel[4].rotateAngleY = -1.57079633F;
		baseTransparentModel[4].rotateAngleZ = 0.15707963F;

		baseTransparentModel[5].addBox(-0.5F, 0F, -0.5F, 1, 2, 1, 0F); // Box 0
		baseTransparentModel[5].setRotationPoint(19F, -11F, 8F);
		baseTransparentModel[5].rotateAngleY = -1.57079633F;
		baseTransparentModel[5].rotateAngleZ = 0.15707963F;

		baseTransparentModel[6].addBox(0F, 0F, 0F, 14, 5, 0, 0F); // Box 0
		baseTransparentModel[6].setRotationPoint(1F, -12.05F, 15F);
		baseTransparentModel[6].rotateAngleX = -0.95993109F;

		baseTransparentModel[7].addBox(0F, 4.5F, 0.5F, 14, 5, 0, 0F); // Box 0
		baseTransparentModel[7].setRotationPoint(1F, -11.55F, 15F);
		baseTransparentModel[7].rotateAngleX = -1.04719755F;

		baseTransparentModel[8].addBox(0F, 9.5F, 1F, 14, 5, 0, 0F); // Box 0
		baseTransparentModel[8].setRotationPoint(1F, -12.05F, 15F);
		baseTransparentModel[8].rotateAngleX = -0.95993109F;

		baseTransparentModel[9].addBox(0F, 0F, 0F, 14, 5, 0, 0F); // Box 0
		baseTransparentModel[9].setRotationPoint(1F, -2.25F, 4.5F);
		baseTransparentModel[9].rotateAngleX = -1.29154365F;

		sawdustModel = new ModelRendererTurbo[14];
		sawdustModel[0] = new ModelRendererTurbo(this, 0, 55, textureX, textureY); // SawDust
		sawdustModel[1] = new ModelRendererTurbo(this, 0, 27, textureX, textureY); // SawDust
		sawdustModel[2] = new ModelRendererTurbo(this, 126, 85, textureX, textureY); // Box 0
		sawdustModel[3] = new ModelRendererTurbo(this, 126, 90, textureX, textureY); // Box 0
		sawdustModel[4] = new ModelRendererTurbo(this, 126, 95, textureX, textureY); // Box 0
		sawdustModel[5] = new ModelRendererTurbo(this, 126, 85, textureX, textureY); // Box 0
		sawdustModel[6] = new ModelRendererTurbo(this, 126, 90, textureX, textureY); // Box 0
		sawdustModel[7] = new ModelRendererTurbo(this, 126, 95, textureX, textureY); // Box 0
		sawdustModel[8] = new ModelRendererTurbo(this, 126, 85, textureX, textureY); // Box 0
		sawdustModel[9] = new ModelRendererTurbo(this, 126, 90, textureX, textureY); // Box 0
		sawdustModel[10] = new ModelRendererTurbo(this, 126, 95, textureX, textureY); // Box 0
		sawdustModel[11] = new ModelRendererTurbo(this, 126, 85, textureX, textureY); // Box 0
		sawdustModel[12] = new ModelRendererTurbo(this, 126, 90, textureX, textureY); // Box 0
		sawdustModel[13] = new ModelRendererTurbo(this, 126, 95, textureX, textureY); // Box 0


		//sawdustModel[13].setRotationPoint X: 38F

		sawdustModel[0].addBox(-12F, 0F, -12F, 24, 0, 24, 0F); // SawDust
		sawdustModel[0].setRotationPoint(0F, -0.05F, 0F);
		sawdustModel[0].flip = true;

		sawdustModel[1].addShapeBox(-9F, 0F, -9F, 18, 10, 18, 0F, -5F, 0F, -5F, -5F, 0F, -5F, -5F, 0F, -5F, -5F, 0F, -5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // SawDust
		sawdustModel[1].setRotationPoint(0F, -10F, 0F);

		sawdustModel[2].addBox(-7F, 0F, -5F, 14, 5, 0, 0F); // Box 0
		sawdustModel[2].setRotationPoint(0F, -7.05F, 0F);
		sawdustModel[2].rotateAngleX = -0.6981317F;

		sawdustModel[3].addBox(-7F, 0F, -7F, 14, 5, 0, 0F); // Box 0
		sawdustModel[3].setRotationPoint(0F, -3.05F, 0F);
		sawdustModel[3].rotateAngleX = -0.6981317F;

		sawdustModel[4].addBox(-7F, 2F, -7.5F, 14, 5, 0, 0F); // Box 0
		sawdustModel[4].setRotationPoint(0F, -1.05F, 0F);
		sawdustModel[4].rotateAngleX = -0.6981317F;

		sawdustModel[5].addBox(-7F, 0F, -5F, 14, 5, 0, 0F); // Box 0
		sawdustModel[5].setRotationPoint(0F, -7.05F, 0F);
		sawdustModel[5].rotateAngleX = -0.6981317F;
		sawdustModel[5].rotateAngleY = 1.57079633F;

		sawdustModel[6].addBox(-7F, 0F, -7F, 14, 5, 0, 0F); // Box 0
		sawdustModel[6].setRotationPoint(0F, -3.05F, 0F);
		sawdustModel[6].rotateAngleX = -0.6981317F;
		sawdustModel[6].rotateAngleY = 1.57079633F;

		sawdustModel[7].addBox(-7F, 2F, -7.5F, 14, 5, 0, 0F); // Box 0
		sawdustModel[7].setRotationPoint(0F, -1.05F, 0F);
		sawdustModel[7].rotateAngleX = -0.6981317F;
		sawdustModel[7].rotateAngleY = 1.57079633F;

		sawdustModel[8].addBox(-7F, 0F, -5F, 14, 5, 0, 0F); // Box 0
		sawdustModel[8].setRotationPoint(0F, -7.05F, 0F);
		sawdustModel[8].rotateAngleX = -0.6981317F;
		sawdustModel[8].rotateAngleY = -1.57079633F;

		sawdustModel[9].addBox(-7F, 0F, -7F, 14, 5, 0, 0F); // Box 0
		sawdustModel[9].setRotationPoint(0F, -3.05F, 0F);
		sawdustModel[9].rotateAngleX = -0.6981317F;
		sawdustModel[9].rotateAngleY = -1.57079633F;

		sawdustModel[10].addBox(-7F, 2F, -7.5F, 14, 5, 0, 0F); // Box 0
		sawdustModel[10].setRotationPoint(0F, -1.05F, 0F);
		sawdustModel[10].rotateAngleX = -0.6981317F;
		sawdustModel[10].rotateAngleY = -1.57079633F;

		sawdustModel[11].addBox(-7F, 0F, -5F, 14, 5, 0, 0F); // Box 0
		sawdustModel[11].setRotationPoint(0F, -7.05F, 0F);
		sawdustModel[11].rotateAngleX = -0.6981317F;
		sawdustModel[11].rotateAngleY = -3.14159265F;

		sawdustModel[12].addBox(-7F, 0F, -7F, 14, 5, 0, 0F); // Box 0
		sawdustModel[12].setRotationPoint(0F, -3.05F, 0F);
		sawdustModel[12].rotateAngleX = -0.6981317F;
		sawdustModel[12].rotateAngleY = -3.14159265F;

		sawdustModel[13].addBox(-7F, 2F, -7.5F, 14, 5, 0, 0F); // Box 0
		sawdustModel[13].setRotationPoint(0F, -1.05F, 0F);
		sawdustModel[13].rotateAngleX = -0.6981317F;
		sawdustModel[13].rotateAngleY = -3.14159265F;


		inserterBaseModel = new ModelRendererTurbo[3];
		inserterBaseModel[0] = new ModelRendererTurbo(this, 0, 71, textureX, textureY); // Box 0
		inserterBaseModel[1] = new ModelRendererTurbo(this, 12, 55, textureX, textureY); // Box 0
		inserterBaseModel[2] = new ModelRendererTurbo(this, 0, 80, textureX, textureY); // Box 0

		inserterBaseModel[0].addBox(0F, 0F, 0F, 4, 3, 3, 0F); // Box 0
		inserterBaseModel[0].setRotationPoint(54F, -21F, -11F);

		inserterBaseModel[1].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // Box 0
		inserterBaseModel[1].setRotationPoint(55F, -21F, -8F);

		inserterBaseModel[2].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // Box 0
		inserterBaseModel[2].setRotationPoint(55F, -21F, -13F);


		//axleModel[0].setRotationPoint 40F, -11F
		axleModel = new ModelRendererTurbo[2];
		axleModel[0] = new ModelRendererTurbo(this, 122, 26, textureX, textureY); // Box 0
		axleModel[1] = new ModelRendererTurbo(this, 122, 26, textureX, textureY); // Box 0

		axleModel[0].addBox(-0.5F, -0.5F, 0F, 1, 1, 2, 0F); // Box 0
		axleModel[0].setRotationPoint(0F, 0F, -13F);

		axleModel[1].addBox(-0.5F, -0.5F, 0F, 1, 1, 2, 0F); // Box 0


		inserterMovingPartModel = new ModelRendererTurbo[6];
		inserterMovingPartModel[0] = new ModelRendererTurbo(this, 88, 83, textureX, textureY); // Box 0
		inserterMovingPartModel[1] = new ModelRendererTurbo(this, 78, 40, textureX, textureY); // Box 0
		inserterMovingPartModel[2] = new ModelRendererTurbo(this, 0, 19, textureX, textureY); // Box 0
		inserterMovingPartModel[3] = new ModelRendererTurbo(this, 0, 19, textureX, textureY); // Box 0
		inserterMovingPartModel[4] = new ModelRendererTurbo(this, 8, 0, textureX, textureY); // Box 0
		inserterMovingPartModel[5] = new ModelRendererTurbo(this, 8, 0, textureX, textureY); // Box 0

		inserterMovingPartModel[0].addBox(0F, 0F, 0F, 1, 1, 16, 0F); // Box 0
		inserterMovingPartModel[0].setRotationPoint(55.5F, -20.5F, -15F);

		inserterMovingPartModel[1].addBox(0F, 0F, 0F, 10, 1, 1, 0F); // Box 0
		inserterMovingPartModel[1].setRotationPoint(51F, -20.5F, 1F);

		inserterMovingPartModel[2].addBox(0F, 0F, 0F, 1, 1, 3, 0F); // Box 0
		inserterMovingPartModel[2].setRotationPoint(51F, -20.5F, 2F);

		inserterMovingPartModel[3].addBox(0F, 0F, 0F, 1, 1, 3, 0F); // Box 0
		inserterMovingPartModel[3].setRotationPoint(60F, -20.5F, 2F);

		inserterMovingPartModel[4].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F); // Box 0
		inserterMovingPartModel[4].setRotationPoint(51F, -20.5F, 5F);

		inserterMovingPartModel[5].addShapeBox(0F, 0F, 0F, 1, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // Box 0
		inserterMovingPartModel[5].setRotationPoint(60F, -20.5F, 5F);

		parts.put("base", baseModel);
		parts.put("baseTransparent", baseTransparentModel);
		parts.put("sawdust", sawdustModel);
		parts.put("inserterBase", inserterBaseModel);
		parts.put("inserterMovingPart", inserterMovingPartModel);
		parts.put("axle", axleModel);

		flipAll();
	}

	@Override
	public void getBlockRotation(EnumFacing facing, BaseBlockModel model)
	{
		switch(facing)
		{
			case EAST:
			{
				GlStateManager.translate(-1f, 0f, -1f);
			}
			break;
			case WEST:
			{
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(-4f, 0f, 0f);

			}
			break;
			case NORTH:
			{
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-2f, 0f, 1f);

			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(-3f, 0f, -2f);
			}
			break;
		}
	}
}
