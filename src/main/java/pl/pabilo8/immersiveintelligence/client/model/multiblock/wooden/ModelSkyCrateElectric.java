package pl.pabilo8.immersiveintelligence.client.model.multiblock.wooden;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * Created by Pabilo8 on 07-06-2019.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class ModelSkyCrateElectric extends ModelIIBase
{
	int textureX = 64;
	int textureY = 32;

	public ModelSkyCrateElectric() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[36];
		baseModel[0] = new ModelRendererTurbo(this, 0, 5, textureX, textureY); // SideArm1
		baseModel[1] = new ModelRendererTurbo(this, 0, 5, textureX, textureY); // SideArm2
		baseModel[2] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // BottomArm
		baseModel[3] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // TopArm
		baseModel[4] = new ModelRendererTurbo(this, 6, 5, textureX, textureY); // TopHandle
		baseModel[5] = new ModelRendererTurbo(this, 6, 5, textureX, textureY); // TopHandle
		baseModel[6] = new ModelRendererTurbo(this, 16, 5, textureX, textureY); // TopHandle2
		baseModel[7] = new ModelRendererTurbo(this, 16, 5, textureX, textureY); // TopHandle2
		baseModel[8] = new ModelRendererTurbo(this, 16, 5, textureX, textureY); // TopHandle2
		baseModel[9] = new ModelRendererTurbo(this, 16, 5, textureX, textureY); // TopHandle2
		baseModel[10] = new ModelRendererTurbo(this, 10, 7, textureX, textureY); // TopHandle
		baseModel[11] = new ModelRendererTurbo(this, 10, 15, textureX, textureY); // TopHandle
		baseModel[12] = new ModelRendererTurbo(this, 10, 11, textureX, textureY); // TopHandle
		baseModel[13] = new ModelRendererTurbo(this, 19, 5, textureX, textureY); // BottomArm
		baseModel[14] = new ModelRendererTurbo(this, 19, 10, textureX, textureY); // SideArm2
		baseModel[15] = new ModelRendererTurbo(this, 19, 10, textureX, textureY); // SideArm2
		baseModel[16] = new ModelRendererTurbo(this, 10, 19, textureX, textureY); // SideArm2
		baseModel[17] = new ModelRendererTurbo(this, 10, 19, textureX, textureY); // SideArm2
		baseModel[18] = new ModelRendererTurbo(this, 26, 17, textureX, textureY); // SideArm2
		baseModel[19] = new ModelRendererTurbo(this, 26, 17, textureX, textureY); // SideArm2
		baseModel[20] = new ModelRendererTurbo(this, 26, 24, textureX, textureY); // SideArm2
		baseModel[21] = new ModelRendererTurbo(this, 26, 24, textureX, textureY); // SideArm2
		baseModel[22] = new ModelRendererTurbo(this, 38, 27, textureX, textureY); // SideArm2
		baseModel[23] = new ModelRendererTurbo(this, 46, 22, textureX, textureY); // SideArm2
		baseModel[24] = new ModelRendererTurbo(this, 46, 22, textureX, textureY); // SideArm2
		baseModel[25] = new ModelRendererTurbo(this, 38, 27, textureX, textureY); // SideArm2
		baseModel[26] = new ModelRendererTurbo(this, 37, 5, textureX, textureY); // SideArm2
		baseModel[27] = new ModelRendererTurbo(this, 37, 5, textureX, textureY); // SideArm2
		baseModel[28] = new ModelRendererTurbo(this, 41, 9, textureX, textureY); // BottomArm
		baseModel[29] = new ModelRendererTurbo(this, 41, 9, textureX, textureY); // BottomArm
		baseModel[30] = new ModelRendererTurbo(this, 40, 0, textureX, textureY); // BottomArm
		baseModel[31] = new ModelRendererTurbo(this, 43, 3, textureX, textureY); // BottomArm
		baseModel[32] = new ModelRendererTurbo(this, 44, 15, textureX, textureY); // BottomArm
		baseModel[33] = new ModelRendererTurbo(this, 40, 0, textureX, textureY); // BottomArm
		baseModel[34] = new ModelRendererTurbo(this, 43, 3, textureX, textureY); // BottomArm
		baseModel[35] = new ModelRendererTurbo(this, 44, 15, textureX, textureY); // BottomArm

		baseModel[0].addBox(-9F, 0F, -2F, 1, 16, 4, 0F); // SideArm1
		baseModel[0].setRotationPoint(0F, -16F, 0F);

		baseModel[1].addBox(-9F, 0F, -2F, 1, 16, 4, 0F); // SideArm2
		baseModel[1].setRotationPoint(0F, -16F, 0F);
		baseModel[1].rotateAngleY = 3.14159265F;

		baseModel[2].addBox(-9F, 0F, -2F, 18, 1, 4, 0F); // BottomArm
		baseModel[2].setRotationPoint(0F, -17F, 0F);

		baseModel[3].addBox(-9F, 0F, -2F, 18, 1, 4, 0F); // TopArm
		baseModel[3].setRotationPoint(0F, 0F, 0F);

		baseModel[4].addFlexTrapezoid(-2F, 0F, -1.5F, 4, 1, 1, 0F, -1.00F, -1.00F, 0.00F, 0F, 0F, 0F, ModelRendererTurbo.MR_TOP); // TopHandle
		baseModel[4].setRotationPoint(0F, -19F, 0F);

		baseModel[5].addFlexTrapezoid(-2F, 0F, 0.5F, 4, 1, 1, 0F, -1.00F, -1.00F, 0.00F, 0F, 0F, 0F, ModelRendererTurbo.MR_TOP); // TopHandle
		baseModel[5].setRotationPoint(0F, -19F, 0F);

		baseModel[6].addBox(-2F, 0F, -1.5F, 1, 1, 1, 0F); // TopHandle2
		baseModel[6].setRotationPoint(0F, -18F, 0F);

		baseModel[7].addBox(-2F, 0F, 0.5F, 1, 1, 1, 0F); // TopHandle2
		baseModel[7].setRotationPoint(0F, -18F, 0F);

		baseModel[8].addBox(1F, 0F, -1.5F, 1, 1, 1, 0F); // TopHandle2
		baseModel[8].setRotationPoint(0F, -18F, 0F);

		baseModel[9].addBox(1F, 0F, 0.5F, 1, 1, 1, 0F); // TopHandle2
		baseModel[9].setRotationPoint(0F, -18F, 0F);

		baseModel[10].addBox(-2F, 0F, -1.5F, 3, 1, 3, 0F); // TopHandle
		baseModel[10].setRotationPoint(0.5F, -21F, 0F);

		baseModel[11].addShapeBox(-2F, 0F, -1.5F, 3, 1, 3, 0F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TopHandle
		baseModel[11].setRotationPoint(0.5F, -22F, 0F);

		baseModel[12].addShapeBox(-2F, 0F, -1.5F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F); // TopHandle
		baseModel[12].setRotationPoint(0.5F, -20F, 0F);

		baseModel[13].addBox(-2F, 0F, -2F, 4, 1, 4, 0F); // BottomArm
		baseModel[13].setRotationPoint(0F, -16F, 0F);

		baseModel[14].addBox(-15F, 0F, -3F, 6, 1, 6, 0F); // SideArm2
		baseModel[14].setRotationPoint(0F, -2F, 0F);
		baseModel[14].rotateAngleY = 3.14159265F;

		baseModel[15].addBox(9F, 0F, -3F, 6, 1, 6, 0F); // SideArm2
		baseModel[15].setRotationPoint(0F, -2F, 0F);
		baseModel[15].rotateAngleY = 3.14159265F;

		baseModel[16].addBox(-14F, 0F, -3F, 4, 8, 4, 0F); // SideArm2
		baseModel[16].setRotationPoint(0F, -10F, -1F);
		baseModel[16].rotateAngleY = 3.14159265F;

		baseModel[17].addBox(10F, 0F, -3F, 4, 8, 4, 0F); // SideArm2
		baseModel[17].setRotationPoint(0F, -10F, -1F);
		baseModel[17].rotateAngleY = 3.14159265F;

		baseModel[18].addBox(-15F, 0F, -3F, 6, 1, 6, 0F); // SideArm2
		baseModel[18].setRotationPoint(0F, -11F, 0F);
		baseModel[18].rotateAngleY = 3.14159265F;

		baseModel[19].addBox(9F, 0F, -3F, 6, 1, 6, 0F); // SideArm2
		baseModel[19].setRotationPoint(0F, -11F, 0F);
		baseModel[19].rotateAngleY = 3.14159265F;

		baseModel[20].addBox(-14F, 0F, -2F, 4, 1, 4, 0F); // SideArm2
		baseModel[20].setRotationPoint(0F, -12F, 0F);
		baseModel[20].rotateAngleY = 3.14159265F;

		baseModel[21].addBox(10F, 0F, -2F, 4, 1, 4, 0F); // SideArm2
		baseModel[21].setRotationPoint(0F, -12F, 0F);
		baseModel[21].rotateAngleY = 3.14159265F;

		baseModel[22].addBox(-14F, 0F, -2F, 4, 1, 4, 0F); // SideArm2
		baseModel[22].setRotationPoint(0F, -14F, 0F);
		baseModel[22].rotateAngleY = 3.14159265F;

		baseModel[23].addBox(10F, 0F, -2F, 4, 1, 4, 0F); // SideArm2
		baseModel[23].setRotationPoint(0F, -14F, 0F);
		baseModel[23].rotateAngleY = 3.14159265F;

		baseModel[24].addBox(-14F, 0F, -2F, 4, 1, 4, 0F); // SideArm2
		baseModel[24].setRotationPoint(0F, -16F, 0F);
		baseModel[24].rotateAngleY = 3.14159265F;

		baseModel[25].addBox(10F, 0F, -2F, 4, 1, 4, 0F); // SideArm2
		baseModel[25].setRotationPoint(0F, -16F, 0F);
		baseModel[25].rotateAngleY = 3.14159265F;

		baseModel[26].addBox(-13F, 0F, -2F, 2, 6, 2, 0F); // SideArm2
		baseModel[26].setRotationPoint(0F, -17F, -1F);
		baseModel[26].rotateAngleY = 3.14159265F;

		baseModel[27].addBox(11F, 0F, -2F, 2, 6, 2, 0F); // SideArm2
		baseModel[27].setRotationPoint(0F, -17F, -1F);
		baseModel[27].rotateAngleY = 3.14159265F;

		baseModel[28].addBox(2F, 0F, -2F, 4, 2, 4, 0F); // BottomArm
		baseModel[28].setRotationPoint(0F, -19F, 0F);

		baseModel[29].addBox(-6F, 0F, -2F, 4, 2, 4, 0F); // BottomArm
		baseModel[29].setRotationPoint(0F, -19F, 0F);

		baseModel[30].addBox(-9F, 0F, -1F, 3, 1, 2, 0F); // BottomArm
		baseModel[30].setRotationPoint(0F, -18F, 0F);

		baseModel[31].addShapeBox(-10F, 0F, -1F, 1, 1, 2, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BottomArm
		baseModel[31].setRotationPoint(0F, -18F, 0F);

		baseModel[32].addBox(-10F, 0F, -1F, 1, 6, 2, 0F); // BottomArm
		baseModel[32].setRotationPoint(0F, -17F, 0F);

		baseModel[33].addBox(6F, 0F, -1F, 3, 1, 2, 0F); // BottomArm
		baseModel[33].setRotationPoint(0F, -18F, 0F);

		baseModel[34].addShapeBox(9F, 0F, -1F, 1, 1, 2, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // BottomArm
		baseModel[34].setRotationPoint(0F, -18F, 0F);

		baseModel[35].addBox(9F, 0F, -1F, 1, 6, 2, 0F); // BottomArm
		baseModel[35].setRotationPoint(0F, -17F, 0F);

		flipAll();
	}
}
