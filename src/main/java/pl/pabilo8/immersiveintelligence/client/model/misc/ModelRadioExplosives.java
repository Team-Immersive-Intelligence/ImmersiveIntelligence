package pl.pabilo8.immersiveintelligence.client.model.misc;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 29.01.2021
 */
public class ModelRadioExplosives extends ModelIIBase implements IBulletModel
{
	int textureX = 64;
	int textureY = 32;
	public static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/blocks/mine/radio_explosives.png";

	public ModelRendererTurbo[] satchelTopModel, explosivesModel;

	public ModelRadioExplosives() //Same as Filename
	{
		satchelTopModel = new ModelRendererTurbo[8];
		satchelTopModel[0] = new ModelRendererTurbo(this, 24, 19, textureX, textureY); // Box 1
		satchelTopModel[1] = new ModelRendererTurbo(this, 54, 26, textureX, textureY); // Box 2
		satchelTopModel[2] = new ModelRendererTurbo(this, 22, 0, textureX, textureY); // Box 3
		satchelTopModel[3] = new ModelRendererTurbo(this, 52, 21, textureX, textureY); // Box 6
		satchelTopModel[4] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 1
		satchelTopModel[5] = new ModelRendererTurbo(this, 54, 26, textureX, textureY); // Box 2
		satchelTopModel[6] = new ModelRendererTurbo(this, 22, 0, textureX, textureY); // Box 3
		satchelTopModel[7] = new ModelRendererTurbo(this, 52, 21, textureX, textureY); // Box 6

		satchelTopModel[0].addShapeBox(0F, 0F, 0F, 8, 1, 12, 0F, 0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.5F, 0F, 0F, 0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.5F, 0F, 0F); // Box 1
		satchelTopModel[0].setRotationPoint(-2.5F, -10F, -6F);

		satchelTopModel[1].addShapeBox(0F, 0F, 0F, 1, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		satchelTopModel[1].setRotationPoint(-3.5F, -9.5F, 2F);

		satchelTopModel[2].addShapeBox(0F, 0F, 0F, 9, 1, 2, 0F, 0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.5F, 0F, 0F, 0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.5F, 0F, 0F); // Box 3
		satchelTopModel[2].setRotationPoint(-3F, -10.5F, 2F);

		satchelTopModel[3].addShapeBox(0F, 0F, 0F, 1, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 6
		satchelTopModel[3].setRotationPoint(-4.5F, -7.5F, 1.5F);

		satchelTopModel[4].addShapeBox(0F, 0F, 0F, 1, 3, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		satchelTopModel[4].setRotationPoint(-3F, -9F, -5F);

		satchelTopModel[5].addShapeBox(0F, 0F, 0F, 1, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		satchelTopModel[5].setRotationPoint(-3.5F, -9.5F, -4F);

		satchelTopModel[6].addShapeBox(0F, 0F, 0F, 9, 1, 2, 0F, 0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.5F, 0F, 0F, 0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0.5F, 0F, 0F); // Box 3
		satchelTopModel[6].setRotationPoint(-3F, -10.5F, -4F);

		satchelTopModel[7].addShapeBox(0F, 0F, 0F, 1, 2, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 6
		satchelTopModel[7].setRotationPoint(-4.5F, -7.5F, -4.5F);

		baseModel = new ModelRendererTurbo[14];
		baseModel[0] = new ModelRendererTurbo(this, 24, 3, textureX, textureY); // Box 4
		baseModel[1] = new ModelRendererTurbo(this, 48, 2, textureX, textureY); // Box 7
		baseModel[2] = new ModelRendererTurbo(this, 52, 15, textureX, textureY); // Box 2
		baseModel[3] = new ModelRendererTurbo(this, 24, 19, textureX, textureY); // Box 1
		baseModel[4] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Box 1
		baseModel[5] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Box 1
		baseModel[6] = new ModelRendererTurbo(this, 36, 0, textureX, textureY); // Box 1
		baseModel[7] = new ModelRendererTurbo(this, 0, 23, textureX, textureY); // Box 1
		baseModel[8] = new ModelRendererTurbo(this, 58, 11, textureX, textureY); // Box 2
		baseModel[9] = new ModelRendererTurbo(this, 24, 3, textureX, textureY); // Box 4
		baseModel[10] = new ModelRendererTurbo(this, 52, 15, textureX, textureY); // Box 2
		baseModel[11] = new ModelRendererTurbo(this, 58, 11, textureX, textureY); // Box 2
		baseModel[12] = new ModelRendererTurbo(this, 60, 20, textureX, textureY); // Box 7
		baseModel[13] = new ModelRendererTurbo(this, 26, 6, textureX, textureY); // Box 7

		baseModel[0].addShapeBox(0F, 0F, 0F, 8, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		baseModel[0].setRotationPoint(-2F, -0.5F, 2F);

		baseModel[1].addShapeBox(0F, 0F, 0F, 4, 6, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 7
		baseModel[1].setRotationPoint(-0.5F, -6F, 5.5F);

		baseModel[2].addShapeBox(0F, 0F, 0F, 1, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[2].setRotationPoint(5F, -9.5F, 2F);

		baseModel[3].addShapeBox(0F, 0F, 0F, 8, 1, 12, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		baseModel[3].setRotationPoint(-2.5F, -1F, -6F);

		baseModel[4].addShapeBox(0F, 0F, 0F, 8, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		baseModel[4].setRotationPoint(-2.5F, -9F, -6F);

		baseModel[5].addShapeBox(0F, 0F, 0F, 8, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		baseModel[5].setRotationPoint(-2.5F, -9F, 5F);

		baseModel[6].addShapeBox(0F, 0F, 0F, 1, 5, 10, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		baseModel[6].setRotationPoint(-2.5F, -6F, -5F);

		baseModel[7].addShapeBox(0F, 0F, 0F, 10, 8, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		baseModel[7].setRotationPoint(5.5F, -9F, -5F);
		baseModel[7].rotateAngleY = 1.57079633F;

		baseModel[8].addShapeBox(0F, 0F, 0F, 1, 7, 2, 0F, 0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[8].setRotationPoint(-3F, -6.5F, 2F);

		baseModel[9].addShapeBox(0F, 0F, 0F, 8, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		baseModel[9].setRotationPoint(-2F, -0.5F, -4F);

		baseModel[10].addShapeBox(0F, 0F, 0F, 1, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[10].setRotationPoint(5F, -9.5F, -4F);

		baseModel[11].addShapeBox(0F, 0F, 0F, 1, 7, 2, 0F, 0.5F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[11].setRotationPoint(-3F, -6.5F, -4F);

		baseModel[12].addShapeBox(0F, 0F, 0F, 1, 10, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 7
		baseModel[12].setRotationPoint(2F, -16F, 6F);

		baseModel[13].addShapeBox(0F, 0F, 0F, 4, 4, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 7
		baseModel[13].setRotationPoint(-0.5F, -5F, 7.5F);


		explosivesModel = new ModelRendererTurbo[4];
		explosivesModel[0] = new ModelRendererTurbo(this, 24, 11, textureX, textureY); // Box 1
		explosivesModel[1] = new ModelRendererTurbo(this, 24, 21, textureX, textureY); // Box 1
		explosivesModel[2] = new ModelRendererTurbo(this, 24, 11, textureX, textureY); // Box 1
		explosivesModel[3] = new ModelRendererTurbo(this, 24, 21, textureX, textureY); // Box 1

		explosivesModel[0].addShapeBox(0F, 0F, 0F, 3, 7, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		explosivesModel[0].setRotationPoint(-0.5F, -8F, -5F);

		explosivesModel[1].addShapeBox(0F, 0F, 0F, 3, 7, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		explosivesModel[1].setRotationPoint(1.5F, -8F, -2F);

		explosivesModel[2].addShapeBox(0F, 0F, 0F, 3, 7, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		explosivesModel[2].setRotationPoint(-1.5F, -8F, 0F);

		explosivesModel[3].addShapeBox(0F, 0F, 0F, 3, 7, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		explosivesModel[3].setRotationPoint(1.5F, -8F, 2F);

		parts.put("satchelTop",satchelTopModel);
		parts.put("base",baseModel);
		parts.put("explosives", explosivesModel);

		translateAll(0f,0,0);

		flipAll();
	}

	@Override
	public void renderCasing(float topAngle, int paintColour)
	{
		ClientUtils.bindTexture(TEXTURE);
		for(ModelRendererTurbo mod : satchelTopModel)
			mod.render();
		for(ModelRendererTurbo mod : baseModel)
			mod.render();
	}

	@Override
	public void renderCore(int coreColour, EnumCoreTypes coreType)
	{
		ClientUtils.bindTexture(TEXTURE);
		float[] c = IIUtils.rgbIntToRGB(coreColour);
		GlStateManager.color(c[0], c[1], c[2]);
		for(ModelRendererTurbo mod : explosivesModel)
			mod.render();
	}

	public void reloadModels()
	{
		ModelRadioExplosives newModel = new ModelRadioExplosives();
		this.satchelTopModel = newModel.satchelTopModel;
		this.baseModel = newModel.baseModel;
		this.explosivesModel = newModel.explosivesModel;
	}
}
