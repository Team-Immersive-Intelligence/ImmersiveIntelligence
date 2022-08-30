package pl.pabilo8.immersiveintelligence.client.model.metal_device;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

import static pl.pabilo8.immersiveintelligence.client.IIClientUtils.drawRope;

/**
 * @author Pabilo8
 * @since 17-07-2019
 */
public class ModelCraneElectric extends ModelIIBase
{
	public ModelRendererTurbo[] craneMainModel, shaftModel, grabberModel, armTopModel, armBottomModel, craneArmModel, craneArmShaftModel;
	int textureX = 64;
	int textureY = 64;

	public ModelCraneElectric() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[1];
		baseModel[0] = new ModelRendererTurbo(this, 4, 17, textureX, textureY); // Box 0

		baseModel[0].addBox(-4F, 0F, -4F, 8, 1, 8, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, -1F, 0F);


		craneMainModel = new ModelRendererTurbo[13];
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
		craneMainModel[10] = new ModelRendererTurbo(this, 2, 11, textureX, textureY); // Box 0
		craneMainModel[11] = new ModelRendererTurbo(this, 2, 11, textureX, textureY); // Box 0
		craneMainModel[12] = new ModelRendererTurbo(this, 4, 56, textureX, textureY); // Box 0

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

		craneMainModel[10].addBox(-3F, 0F, -5F, 5, 5, 1, 0F); // Box 0
		craneMainModel[10].setRotationPoint(0.5F, -31.5F, -1F);

		craneMainModel[11].addBox(-3F, 0F, -5F, 5, 5, 1, 0F); // Box 0
		craneMainModel[11].setRotationPoint(0.5F, -31.5F, -6F);

		craneMainModel[12].addBox(-3F, 0F, -5F, 4, 4, 4, 0F); // Box 0
		craneMainModel[12].setRotationPoint(1F, -31F, -5F);


		shaftModel = new ModelRendererTurbo[1];
		shaftModel[0] = new ModelRendererTurbo(this, 36, 15, textureX, textureY); // Box 0

		shaftModel[0].addBox(-1F, 0F, -1F, 2, 21, 2, 0F); // Box 0
		shaftModel[0].setRotationPoint(0F, -26F, 0F);


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

		parts.put("base", baseModel);
		parts.put("craneMain", craneMainModel);
		parts.put("shaft", shaftModel);
		parts.put("grabber", grabberModel);
		parts.put("armTop", armTopModel);
		parts.put("armBottom", armBottomModel);
		parts.put("craneArm", craneArmModel);
		parts.put("craneArmShaft", craneArmShaftModel);

		flipAll();
	}

	public void renderCrane(ResourceLocation res, float yaw, float distance, float drop, float grabProgress, Runnable function)
	{
		GlStateManager.pushMatrix();
		ClientUtils.mc().getTextureManager().bindTexture(res);

		GlStateManager.translate(0, 0.0625, 0);
		for(ModelRendererTurbo mod : baseModel)
			mod.render(0.0625f);


		GlStateManager.rotate(180+yaw, 0, 1, 0);

		for(ModelRendererTurbo mod : craneMainModel)
			mod.render(0.0625f);
		for(ModelRendererTurbo mod : shaftModel)
			mod.render(0.0625f);

		GlStateManager.translate(0, 0, 1f-distance);

		for(ModelRendererTurbo mod : craneArmModel)
			mod.render(0.0625f);
		for(ModelRendererTurbo mod : craneArmShaftModel)
			mod.render(0.0625f);

		GlStateManager.translate(0, -drop, 0);

		// TODO: 01.11.2021 grabProgress
		for(ModelRendererTurbo mod : grabberModel)
			mod.render(0.0625f);

		GlStateManager.translate(0, 1.25+0.0625, 0);

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, -0.75, -distance-0.625);
		function.run();
		GlStateManager.popMatrix();

		ClientUtils.mc().getTextureManager().bindTexture(new ResourceLocation("immersiveengineering:textures/blocks/wire.png"));
		GlStateManager.disableCull();
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		drawRope(buffer, 0, 0, -1.59375, 0, drop+0.25, -1.59375, 0.0625, 0);
		drawRope(buffer, 0, 0, -1.59375, 0, drop+0.25, -1.59375, 0, 0.0625);
		Tessellator.getInstance().draw();
		GlStateManager.enableCull();

		GlStateManager.popMatrix();
	}
}
