package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.vehicle_workshop;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 12.04.2021
 */
public class ModelCrane extends ModelIIBase
{
	public ModelRendererTurbo[] craneMainModel, shaftModel, exhaustModel, grabberModel, armTopModel, armBottomModel, craneArmModel, craneArmShaftModel;
	int textureX = 64;
	int textureY = 64;

	public ModelCrane()
	{
		baseModel = new ModelRendererTurbo[1];
		baseModel[0] = new ModelRendererTurbo(this, 4, 17, textureX, textureY); // Box 0

		baseModel[0].addBox(-4F, 0F, -4F, 8, 1, 8, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, -1F, 0F);


		craneMainModel = new ModelRendererTurbo[11];
		craneMainModel[0] = new ModelRendererTurbo(this, 14, 7, textureX, textureY); // Box 0
		craneMainModel[1] = new ModelRendererTurbo(this, 12, 26, textureX, textureY); // Box 0
		craneMainModel[2] = new ModelRendererTurbo(this, 48, 35, textureX, textureY); // Box 0
		craneMainModel[3] = new ModelRendererTurbo(this, 32, 3, textureX, textureY); // Box 0
		craneMainModel[4] = new ModelRendererTurbo(this, 16, 27, textureX, textureY); // Box 0
		craneMainModel[5] = new ModelRendererTurbo(this, 53, 10, textureX, textureY); // Box 0
		craneMainModel[6] = new ModelRendererTurbo(this, 46, 6, textureX, textureY); // Box 0
		craneMainModel[7] = new ModelRendererTurbo(this, 52, 6, textureX, textureY); // Box 0
		craneMainModel[8] = new ModelRendererTurbo(this, 4, 26, textureX, textureY); // Box 0
		craneMainModel[9] = new ModelRendererTurbo(this, 16, 44, textureX, textureY); // Box 0
		craneMainModel[10] = new ModelRendererTurbo(this, 24, 44, textureX, textureY); // Box 0

		craneMainModel[0].addBox(-3F, 0F, -3F, 6, 4, 6, 0F); // Box 0
		craneMainModel[0].setRotationPoint(0F, -5F, 0F);

		craneMainModel[1].addBox(-3F, 0F, -5F, 6, 6, 12, 0F); // Box 0
		craneMainModel[1].setRotationPoint(0F, -32F, 0F);

		craneMainModel[2].addBox(-2F, 0F, -2F, 4, 21, 4, 0F); // Box 0
		craneMainModel[2].setRotationPoint(0F, -26F, 0F);

		craneMainModel[3].addBox(2F, 0F, -2F, 3, 6, 4, 0F); // Box 0
		craneMainModel[3].setRotationPoint(0F, -30F, 0F);

		craneMainModel[4].addBox(2F, 0F, -1.5F, 1, 8, 3, 0F); // Box 0
		craneMainModel[4].setRotationPoint(0F, -22F, 0F);

		craneMainModel[5].addShapeBox(2F, 0F, -1.5F, 2, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // Box 0
		craneMainModel[5].setRotationPoint(0F, -24F, 0F);

		craneMainModel[6].addShapeBox(2F, 0F, -2.5F, 1, 3, 4, 0F, 0F, 0F, 0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.5F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F); // Box 0
		craneMainModel[6].setRotationPoint(0F, -14F, 0F);

		craneMainModel[7].addBox(-3F, 0F, -3F, 5, 3, 1, 0F); // Box 0
		craneMainModel[7].setRotationPoint(0F, -14F, 0F);

		craneMainModel[8].addBox(-4F, 0F, -2F, 2, 4, 4, 0F); // Box 0
		craneMainModel[8].setRotationPoint(0F, -14.5F, 0F);

		craneMainModel[9].addBox(-7F, 0F, -5F, 4, 8, 8, 0F); // Box 0
		craneMainModel[9].setRotationPoint(0F, -32F, 0F);

		craneMainModel[10].addBox(-2F, 0F, -4F, 4, 4, 16, 0.25F); // Box 0
		craneMainModel[10].setRotationPoint(0F, -31F, 0F);


		shaftModel = new ModelRendererTurbo[1];
		shaftModel[0] = new ModelRendererTurbo(this, 36, 15, textureX, textureY); // Box 0

		shaftModel[0].addBox(-1F, 0F, -1F, 2, 21, 2, 0F); // Box 0
		shaftModel[0].setRotationPoint(0F, -26F, 0F);


		exhaustModel = new ModelRendererTurbo[5];
		exhaustModel[0] = new ModelRendererTurbo(this, 24, 1, textureX, textureY); // Box 0
		exhaustModel[1] = new ModelRendererTurbo(this, 7, 58, textureX, textureY); // Box 0
		exhaustModel[2] = new ModelRendererTurbo(this, 44, 13, textureX, textureY); // Box 0
		exhaustModel[3] = new ModelRendererTurbo(this, 2, 52, textureX, textureY); // Box 0
		exhaustModel[4] = new ModelRendererTurbo(this, 0, 34, textureX, textureY); // Box 0

		exhaustModel[0].addBox(-6.5F, 0F, -8F, 3, 3, 3, 0F); // Box 0
		exhaustModel[0].setRotationPoint(0F, -34.5F, 0F);

		exhaustModel[1].addShapeBox(-6.5F, 0F, -8F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		exhaustModel[1].setRotationPoint(0F, -31.5F, 0F);

		exhaustModel[2].addBox(-2.5F, 0F, -8F, 3, 7, 3, 0F); // Box 0
		exhaustModel[2].setRotationPoint(0F, -34.5F, 0F);

		exhaustModel[3].addShapeBox(-3.5F, 0F, -8F, 4, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F); // Box 0
		exhaustModel[3].setRotationPoint(0F, -27.5F, 0F);

		exhaustModel[4].addShapeBox(-6.5F, 0F, -8F, 3, 3, 3, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		exhaustModel[4].setRotationPoint(0F, -27.5F, 0F);


		grabberModel = new ModelRendererTurbo[5];
		grabberModel[0] = new ModelRendererTurbo(this, 0, 44, textureX, textureY); // Box 0
		grabberModel[1] = new ModelRendererTurbo(this, 56, 19, textureX, textureY); // Box 0
		grabberModel[2] = new ModelRendererTurbo(this, 56, 19, textureX, textureY); // Box 0
		grabberModel[3] = new ModelRendererTurbo(this, 56, 19, textureX, textureY); // Box 0
		grabberModel[4] = new ModelRendererTurbo(this, 56, 19, textureX, textureY); // Box 0

		grabberModel[0].addBox(-3F, 2F, -3F, 6, 2, 6, 0F); // Box 0
		grabberModel[0].setRotationPoint(0F, -23F, 25.5F);

		grabberModel[1].addBox(-1F, 1F, -3F, 2, 2, 2, 0F); // Box 0
		grabberModel[1].setRotationPoint(0F, -21F, 25.5F);
		grabberModel[1].rotateAngleX = -0.13962634F;
		grabberModel[1].rotateAngleY = -1.57079633F;

		grabberModel[2].addBox(-1F, 1F, 1F, 2, 2, 2, 0F); // Box 0
		grabberModel[2].setRotationPoint(0F, -21F, 25.5F);
		grabberModel[2].rotateAngleX = 0.13962634F;
		grabberModel[2].rotateAngleY = -1.57079633F;

		grabberModel[3].addBox(-1F, 1F, -3F, 2, 2, 2, 0F); // Box 0
		grabberModel[3].setRotationPoint(0F, -21F, 25.5F);
		grabberModel[3].rotateAngleX = -0.13962634F;
		grabberModel[3].rotateAngleY = -3.14159265F;

		grabberModel[4].addBox(-1F, 1F, 1F, 2, 2, 2, 0F); // Box 0
		grabberModel[4].setRotationPoint(0F, -21F, 25.5F);
		grabberModel[4].rotateAngleX = 0.13962634F;
		grabberModel[4].rotateAngleY = -3.14159265F;


		armTopModel = new ModelRendererTurbo[1];
		armTopModel[0] = new ModelRendererTurbo(this, 6, 4, textureX, textureY); // Box 0

		armTopModel[0].addBox(-0.5F, 0F, -0.5F, 1, 3, 1, 0F); // Box 0
		armTopModel[0].setRotationPoint(0F, -19F, 25.5F);


		armBottomModel = new ModelRendererTurbo[1];
		armBottomModel[0] = new ModelRendererTurbo(this, 6, 0, textureX, textureY); // Box 0

		armBottomModel[0].addBox(-0.5F, 0F, -0.5F, 1, 3, 1, 0F); // Box 0
		armBottomModel[0].setRotationPoint(0F, -16F, 25.5F);


		craneArmModel = new ModelRendererTurbo[2];
		craneArmModel[0] = new ModelRendererTurbo(this, 24, 44, textureX, textureY); // Box 0
		craneArmModel[1] = new ModelRendererTurbo(this, 44, 23, textureX, textureY); // Box 0

		craneArmModel[0].addBox(-2F, 0F, 7F, 4, 4, 16, 0F); // Box 0
		craneArmModel[0].setRotationPoint(0F, -31F, 0F);

		craneArmModel[1].addBox(-2.5F, 0F, 23F, 5, 7, 5, 0F); // Box 0
		craneArmModel[1].setRotationPoint(0F, -32F, 0F);


		craneArmShaftModel = new ModelRendererTurbo[1];
		craneArmShaftModel[0] = new ModelRendererTurbo(this, 36, 15, textureX, textureY); // Box 0

		craneArmShaftModel[0].addBox(-1F, 2F, -1F, 2, 21, 2, 0F); // Box 0
		craneArmShaftModel[0].setRotationPoint(0F, -29F, 0F);
		craneArmShaftModel[0].rotateAngleX = 1.57079633F;

		parts.put("base",baseModel);
		parts.put("craneMain",craneMainModel);
		parts.put("shaft",shaftModel);
		parts.put("exhaust",exhaustModel);
		parts.put("grabber",grabberModel);
		parts.put("armTop",armTopModel);
		parts.put("armBottom",armBottomModel);
		parts.put("craneArm",craneArmModel);
		parts.put("craneArmShaft",craneArmShaftModel);

		flipAll();
	}
}
