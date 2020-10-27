package pl.pabilo8.immersiveintelligence.client.model.misc;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.ModelBlockBase;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * Created by Pabilo8 on 07-06-2019.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class ModelGrenade extends ModelBlockBase implements IBulletModel
{
	private static String texture = ImmersiveIntelligence.MODID+":textures/entity/grenade.png";
	int textureX = 32;
	int textureY = 32;
	ModelRendererTurbo[] coreModel;

	public ModelGrenade() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[1];
		baseModel[0] = new ModelRendererTurbo(this, 0, 10, textureX, textureY); // Box 0

		baseModel[0].addBox(-1F, -1F, -12F, 2, 2, 12, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, 0F, 0F);
		baseModel[0].rotateAngleX = -1.57079633F;

		coreModel = new ModelRendererTurbo[1];
		coreModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 1

		coreModel[0].addShapeBox(-2F, -2F, -18F, 4, 4, 6, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F); // Box 1
		coreModel[0].setRotationPoint(0F, 0F, 0F);
		coreModel[0].rotateAngleX = -1.57079633F;

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
	}

	@Override
	public void renderCore(int coreColour)
	{
		ClientUtils.bindTexture(texture);
		//I don't know if grenades should be colored by core colour
		//float[] c = Utils.rgbIntToRGB(coreColour);
		//GlStateManager.color(c[0],c[1],c[2]);
		for(ModelRendererTurbo model : coreModel)
			model.render(0.0625f);
		//GlStateManager.color(1f,1f,1f,1f);
	}

	@Override
	public void getConveyorOffset()
	{
		GlStateManager.rotate(90, 0f, 1f, 0f);
		GlStateManager.translate(0f, 0.125f, 0f);
		GlStateManager.translate(0.5f, 0f, 1f);
		GlStateManager.rotate(-45, 0f, 1f, 0f);
		GlStateManager.translate(0f, 0f, -0.45f);
	}
}
