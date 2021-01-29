package pl.pabilo8.immersiveintelligence.client.model.metal_device;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 30.08.2020
 * Yes, it exists
 */
public class ModelDebugger extends ModelIIBase
{
	int textureX = 64;
	int textureY = 64;

	public ModelRendererTurbo[] machineBoxModel, projectorModel, deskStuffModel;

	public ModelDebugger() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[5];
		baseModel[0] = new ModelRendererTurbo(this, 18, 30, textureX, textureY); // Box 0
		baseModel[1] = new ModelRendererTurbo(this, 18, 30, textureX, textureY); // Box 0
		baseModel[2] = new ModelRendererTurbo(this, 18, 30, textureX, textureY); // Box 0
		baseModel[3] = new ModelRendererTurbo(this, 18, 30, textureX, textureY); // Box 0
		baseModel[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 4

		baseModel[0].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 0
		baseModel[0].setRotationPoint(12F, -6F, 12F);
		baseModel[0].rotateAngleX = 0.17453293F;
		baseModel[0].rotateAngleZ = 0.17453293F;

		baseModel[1].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 0
		baseModel[1].setRotationPoint(12F, -6F, 4F);
		baseModel[1].rotateAngleX = 0.17453293F;
		baseModel[1].rotateAngleY = -1.57079633F;
		baseModel[1].rotateAngleZ = 0.17453293F;

		baseModel[2].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 0
		baseModel[2].setRotationPoint(4F, -6F, 12F);
		baseModel[2].rotateAngleX = 0.17453293F;
		baseModel[2].rotateAngleY = -4.71238898F;
		baseModel[2].rotateAngleZ = 0.17453293F;

		baseModel[3].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Box 0
		baseModel[3].setRotationPoint(4F, -6F, 4F);
		baseModel[3].rotateAngleX = 0.17453293F;
		baseModel[3].rotateAngleY = -3.14159265F;
		baseModel[3].rotateAngleZ = 0.17453293F;

		baseModel[4].addBox(0F, 0F, 0F, 16, 2, 16, 0F); // Box 4
		baseModel[4].setRotationPoint(0F, -8F, 0F);


		machineBoxModel = new ModelRendererTurbo[8];
		machineBoxModel[0] = new ModelRendererTurbo(this, 0, 18, textureX, textureY); // Box 5
		machineBoxModel[1] = new ModelRendererTurbo(this, 16, 39, textureX, textureY); // Box 5
		machineBoxModel[2] = new ModelRendererTurbo(this, 0, 39, textureX, textureY); // Box 5
		machineBoxModel[3] = new ModelRendererTurbo(this, 26, 30, textureX, textureY); // Box 5
		machineBoxModel[4] = new ModelRendererTurbo(this, 5, 41, textureX, textureY); // Box 5
		machineBoxModel[5] = new ModelRendererTurbo(this, 6, 39, textureX, textureY); // Box 5
		machineBoxModel[6] = new ModelRendererTurbo(this, 6, 39, textureX, textureY); // Box 5
		machineBoxModel[7] = new ModelRendererTurbo(this, 5, 41, textureX, textureY); // Box 5

		machineBoxModel[0].addBox(0F, 0F, 0F, 16, 6, 6, 0F); // Box 5
		machineBoxModel[0].setRotationPoint(0F, -14F, 10F);

		machineBoxModel[1].addBox(0F, 0F, 0F, 4, 1, 4, 0F); // Box 5
		machineBoxModel[1].setRotationPoint(6F, -15F, 11F);

		machineBoxModel[2].addBox(0F, 0F, 0F, 2, 1, 2, 0F); // Box 5
		machineBoxModel[2].setRotationPoint(7F, -16F, 12F);

		machineBoxModel[3].addBox(0F, 0F, 0F, 4, 4, 1, 0F); // Box 5
		machineBoxModel[3].setRotationPoint(12F, -14F, 9F);

		machineBoxModel[4].addShapeBox(0F, 0F, 0F, 1, 1, 4, 0F, 0F, 0F, -1F, -1F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 5
		machineBoxModel[4].setRotationPoint(9F, -16F, 11F);

		machineBoxModel[5].addShapeBox(0F, 0F, 0F, 2, 1, 1, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 5
		machineBoxModel[5].setRotationPoint(7F, -16F, 11F);

		machineBoxModel[6].addShapeBox(0F, 0F, 0F, 2, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 5
		machineBoxModel[6].setRotationPoint(7F, -16F, 14F);

		machineBoxModel[7].addShapeBox(0F, 0F, 0F, 1, 1, 4, 0F, -1F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 5
		machineBoxModel[7].setRotationPoint(6F, -16F, 11F);

		projectorModel = new ModelRendererTurbo[11];
		projectorModel[0] = new ModelRendererTurbo(this, 0, 30, textureX, textureY); // Box 5
		projectorModel[1] = new ModelRendererTurbo(this, 40, 26, textureX, textureY); // Box 5
		projectorModel[2] = new ModelRendererTurbo(this, 40, 26, textureX, textureY); // Box 5
		projectorModel[3] = new ModelRendererTurbo(this, 54, 18, textureX, textureY); // Box 5
		projectorModel[4] = new ModelRendererTurbo(this, 44, 18, textureX, textureY); // Box 5
		projectorModel[5] = new ModelRendererTurbo(this, 38, 18, textureX, textureY); // Box 5
		projectorModel[6] = new ModelRendererTurbo(this, 51, 32, textureX, textureY); // Box 5
		projectorModel[7] = new ModelRendererTurbo(this, 46, 28, textureX, textureY); // Box 5
		projectorModel[8] = new ModelRendererTurbo(this, 46, 26, textureX, textureY); // Box 5
		projectorModel[9] = new ModelRendererTurbo(this, 48, 0, textureX, textureY); // Box 5
		projectorModel[10] = new ModelRendererTurbo(this, 48, 10, textureX, textureY); // Box 5

		projectorModel[0].addBox(0F, -3.5F, -2.5F, 5, 5, 4, 0F); // Box 5
		projectorModel[0].setRotationPoint(12F, -17F, 7F);
		projectorModel[0].rotateAngleZ = 0.95993109F;

		projectorModel[1].addBox(-1F, -3F, -2F, 1, 4, 4, 0F); // Box 5
		projectorModel[1].setRotationPoint(12F, -17F, 6F);
		projectorModel[1].rotateAngleZ = 0.95993109F;

		projectorModel[2].addBox(-3F, -3F, -2F, 1, 4, 4, 0F); // Box 5
		projectorModel[2].setRotationPoint(12F, -17F, 6F);
		projectorModel[2].rotateAngleZ = 0.95993109F;

		projectorModel[3].addBox(-2F, -2.5F, -1.5F, 1, 3, 3, 0F); // Box 5
		projectorModel[3].setRotationPoint(12F, -17F, 6F);
		projectorModel[3].rotateAngleZ = 0.95993109F;

		projectorModel[4].addBox(0F, 0F, 0F, 2, 2, 6, 0F); // Box 5
		projectorModel[4].setRotationPoint(14F, -20F, 3F);

		projectorModel[5].addBox(0F, 0F, 0F, 2, 2, 4, 0F); // Box 5
		projectorModel[5].setRotationPoint(13F, -13F, 5F);

		projectorModel[6].addBox(0F, 0F, 0F, 2, 6, 2, 0F); // Box 5
		projectorModel[6].setRotationPoint(14F, -19F, 5F);
		projectorModel[6].rotateAngleZ = -0.15707963F;

		projectorModel[7].addBox(0F, -3.5F, -2.5F, 5, 1, 1, 0F); // Box 5
		projectorModel[7].setRotationPoint(12F, -17F, 6F);
		projectorModel[7].rotateAngleZ = 0.95993109F;

		projectorModel[8].addBox(0F, 0.5F, -2.5F, 5, 1, 1, 0F); // Box 5
		projectorModel[8].setRotationPoint(12F, -17F, 6F);
		projectorModel[8].rotateAngleZ = 0.95993109F;

		projectorModel[9].addBox(5F, -3.5F, -3.5F, 2, 5, 5, 0F); // Box 5
		projectorModel[9].setRotationPoint(12F, -17F, 7F);
		projectorModel[9].rotateAngleZ = 0.95993109F;

		projectorModel[10].addBox(0F, -2.5F, -2.5F, 1, 3, 1, 0F); // Box 5
		projectorModel[10].setRotationPoint(12F, -17F, 6F);
		projectorModel[10].rotateAngleZ = 0.95993109F;


		deskStuffModel = new ModelRendererTurbo[6];
		deskStuffModel[0] = new ModelRendererTurbo(this, 24, 41, textureX, textureY); // Box 5
		deskStuffModel[1] = new ModelRendererTurbo(this, 28, 39, textureX, textureY); // Box 5
		deskStuffModel[2] = new ModelRendererTurbo(this, 4, 0, textureX, textureY); // Box 15
		deskStuffModel[3] = new ModelRendererTurbo(this, 34, 35, textureX, textureY); // Box 15
		deskStuffModel[4] = new ModelRendererTurbo(this, 30, 36, textureX, textureY); // Box 15
		deskStuffModel[5] = new ModelRendererTurbo(this, 26, 35, textureX, textureY); // Box 15

		deskStuffModel[0].addBox(0F, 0F, -8F, 8, 1, 8, 0F); // Box 5
		deskStuffModel[0].setRotationPoint(4F, -10F, 10F);
		deskStuffModel[0].rotateAngleX = 0.13962634F;

		deskStuffModel[1].addBox(0F, -1F, -8F, 8, 1, 1, 0F); // Box 5
		deskStuffModel[1].setRotationPoint(4F, -10F, 10F);
		deskStuffModel[1].rotateAngleX = 0.13962634F;

		deskStuffModel[2].addBox(0F, 0F, 0F, 3, 3, 3, 0F); // Box 15
		deskStuffModel[2].setRotationPoint(1F, -11F, 6F);

		deskStuffModel[3].addBox(0F, -3F, 0F, 1, 3, 1, 0F); // Box 15
		deskStuffModel[3].setRotationPoint(2.5F, -11F, 6.5F);
		deskStuffModel[3].rotateAngleY = -0.26179939F;
		deskStuffModel[3].rotateAngleZ = -0.27925268F;

		deskStuffModel[4].addBox(-0.5F, -2F, 0F, 1, 2, 1, 0F); // Box 15
		deskStuffModel[4].setRotationPoint(1.5F, -11F, 7.5F);
		deskStuffModel[4].rotateAngleY = -2.30383461F;
		deskStuffModel[4].rotateAngleZ = -0.27925268F;

		deskStuffModel[5].addBox(-0.5F, -2F, 0F, 1, 3, 1, 0F); // Box 15
		deskStuffModel[5].setRotationPoint(2.5F, -11.5F, 8.5F);
		deskStuffModel[5].rotateAngleX = 0.31415927F;
		deskStuffModel[5].rotateAngleY = -2.30383461F;
		deskStuffModel[5].rotateAngleZ = 0.27925268F;

		parts.put("base", baseModel);
		parts.put("machineBox", machineBoxModel);
		parts.put("projector", projectorModel);
		parts.put("deskStuff", deskStuffModel);

		flipAll();
	}
}
