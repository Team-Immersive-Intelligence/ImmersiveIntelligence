package pl.pabilo8.immersiveintelligence.client.model.misc;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.client.model.BaseBlockModel;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.tmt.Shape2D;

/**
 * Created by Pabilo8 on 07-06-2019.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class ModelBullet extends BaseBlockModel implements IBulletModel
{
	private static String texture = ImmersiveIntelligence.MODID+":textures/entity/bullet.png";
	int textureX = 64;
	int textureY = 32;
	ModelRendererTurbo[] coreModel;

	public ModelBullet() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[9];
		baseModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Casing
		baseModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Casing
		baseModel[2] = new ModelRendererTurbo(this, 30, 7, textureX, textureY); // Casing
		baseModel[3] = new ModelRendererTurbo(this, 2, 0, textureX, textureY); // Casing
		baseModel[4] = new ModelRendererTurbo(this, 2, 0, textureX, textureY); // Casing
		baseModel[5] = new ModelRendererTurbo(this, 2, 0, textureX, textureY); // Casing
		baseModel[6] = new ModelRendererTurbo(this, 2, 0, textureX, textureY); // Casing
		baseModel[7] = new ModelRendererTurbo(this, 30, 7, textureX, textureY); // Casing
		baseModel[8] = new ModelRendererTurbo(this, 12, 14, textureX, textureY); // Casing

		baseModel[0].addShape3D(4F, -4F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 1, 0, 1), new Coord2D(1, 0, 1, 0), new Coord2D(1, 8, 1, 8), new Coord2D(0, 7, 0, 7)}), 12, 1, 8, 18, 12, ModelRendererTurbo.MR_FRONT, new float[]{6, 2, 8, 2}); // Casing
		baseModel[0].setRotationPoint(0F, 0F, 0F);
		baseModel[0].rotateAngleX = -1.57079633F;

		baseModel[1].addShape3D(4F, -4F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 1, 0, 1), new Coord2D(1, 0, 1, 0), new Coord2D(1, 8, 1, 8), new Coord2D(0, 7, 0, 7)}), 12, 1, 8, 18, 12, ModelRendererTurbo.MR_FRONT, new float[]{6, 2, 8, 2}); // Casing
		baseModel[1].setRotationPoint(0F, 0F, 0F);
		baseModel[1].rotateAngleX = -1.57079633F;
		baseModel[1].rotateAngleY = -3.14159265F;

		baseModel[2].addBox(-3F, -12F, -4F, 6, 12, 1, 0F); // Casing
		baseModel[2].setRotationPoint(0F, 0F, 0F);

		baseModel[3].addShapeBox(-3F, -13F, 3F, 6, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 1F, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Casing
		baseModel[3].setRotationPoint(0F, 0F, 0F);

		baseModel[4].addShapeBox(-3F, -13F, 3F, 6, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 1F, 0F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Casing
		baseModel[4].setRotationPoint(0F, 0F, 0F);
		baseModel[4].rotateAngleY = -3.14159265F;

		baseModel[5].addShapeBox(-3F, -13F, 3F, 6, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Casing
		baseModel[5].setRotationPoint(0F, 0F, 0F);
		baseModel[5].rotateAngleY = -1.57079633F;

		baseModel[6].addShapeBox(-3F, -13F, 3F, 6, 1, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Casing
		baseModel[6].setRotationPoint(0F, 0F, 0F);
		baseModel[6].rotateAngleY = 1.57079633F;

		baseModel[7].addBox(-3F, -12F, 3F, 6, 12, 1, 0F); // Casing
		baseModel[7].setRotationPoint(0F, 0F, 0F);

		baseModel[8].addBox(-3F, -2F, -3F, 6, 1, 6, 0F); // Casing
		baseModel[8].setRotationPoint(0F, 0F, 0F);


		coreModel = new ModelRendererTurbo[2];
		coreModel[0] = new ModelRendererTurbo(this, 15, 23, textureX, textureY); // Core
		coreModel[1] = new ModelRendererTurbo(this, 39, 20, textureX, textureY); // Core

		coreModel[0].addFlexTrapezoid(-3F, -16F, -3F, 6, 3, 6, 0F, -2.00F, -2.00F, -2.00F, -2.00F, -2.00F, -2.00F, ModelRendererTurbo.MR_TOP); // Core
		coreModel[0].setRotationPoint(0F, 0F, 0F);

		coreModel[1].addBox(-3F, -12F, -3F, 6, 6, 6, 0F); // Core
		coreModel[1].setRotationPoint(0F, 0F, 0F);

		coreModel[0].mirror = true;
		coreModel[0].flip = true;
		parts.put("base", baseModel);
		parts.put("core", coreModel);
		flipAll();
	}

	@Override
	public void renderBullet(int coreColour, int paintColour)
	{
		renderCasing(true, coreColour, 0);
	}

	@Override
	public void renderCasing(boolean core, int coreColour, float gunpowderPercentage)
	{
		ClientUtils.bindTexture(texture);
		for(ModelRendererTurbo model : baseModel)
			model.render(0.0625f);

		if(core)
			renderCore(coreColour);
		else if(gunpowderPercentage > 0)
		{
			GlStateManager.pushMatrix();
			ClientUtils.bindTexture("minecraft:textures/blocks/concrete_powder_black.png");
			GlStateManager.translate(0.3125f, 0.25*gunpowderPercentage, -0.6875f);
			GlStateManager.rotate(90, 1f, 0f, 0f);
			ClientUtils.drawTexturedRect(0f, 0f, 6/16f, 6/16f, 0f, 6/16f, 0f, 6/16f);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void renderCore(int coreColour)
	{
		ClientUtils.bindTexture(texture);
		float[] c = Utils.rgbIntToRGB(coreColour);
		GlStateManager.color(c[0], c[1], c[2]);
		for(ModelRendererTurbo model : coreModel)
			model.render(0.0625f);
		GlStateManager.color(1f, 1f, 1f, 1f);
	}

	@Override
	public void getConveyorOffset()
	{
		GlStateManager.translate(0, 0.035, 0);
	}
}
