package pl.pabilo8.immersiveintelligence.client.model.multiblock.wooden;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * Created by Pabilo8 on 07-06-2019.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class ModelSkyCrate extends ModelIIBase
{
	int textureX = 64;
	int textureY = 32;

	public ModelSkyCrate() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[13];
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

		flipAll();
	}
}
