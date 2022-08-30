package pl.pabilo8.immersiveintelligence.client.model.misc;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;

/**
 * @author Pabilo8
 * @since 29.01.2021
 */
public class ModelTellermine extends ModelIIBase implements IBulletModel
{
	int textureX = 32;
	int textureY = 32;
	public static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/blocks/mine/tellermine.png";

	public ModelRendererTurbo[] coreModel;

	public ModelTellermine() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[10];
		baseModel[0] = new ModelRendererTurbo(this, 0, 18, textureX, textureY); // Shape 1
		baseModel[1] = new ModelRendererTurbo(this, 12, 18, textureX, textureY); // Box 2
		baseModel[2] = new ModelRendererTurbo(this, 12, 23, textureX, textureY); // Box 2
		baseModel[3] = new ModelRendererTurbo(this, 12, 20, textureX, textureY); // Box 2
		baseModel[4] = new ModelRendererTurbo(this, 12, 20, textureX, textureY); // Box 2
		baseModel[5] = new ModelRendererTurbo(this, 12, 18, textureX, textureY); // Box 2
		baseModel[6] = new ModelRendererTurbo(this, 12, 23, textureX, textureY); // Box 2
		baseModel[7] = new ModelRendererTurbo(this, 12, 20, textureX, textureY); // Box 2
		baseModel[8] = new ModelRendererTurbo(this, 12, 20, textureX, textureY); // Box 2
		baseModel[9] = new ModelRendererTurbo(this, 0, 18, textureX, textureY); // Shape 1

		baseModel[0].addShape3D(0F, -6F, -2F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(4, 0, 4, 0), new Coord2D(6, 2, 6, 2), new Coord2D(6, 10, 6, 10), new Coord2D(4, 12, 4, 12), new Coord2D(0, 12, 0, 12) }), 2, 6, 12, 34, 2, ModelRendererTurbo.MR_FRONT, new float[] {12 ,4 ,3 ,8 ,3 ,4}); // Shape 1
		baseModel[0].setRotationPoint(0F, 0F, 0F);
		baseModel[0].rotateAngleX = 1.57079633F;

		baseModel[1].addShapeBox(1F, -3F, -2F, 4, 1, 1, 0F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F); // Box 2
		baseModel[1].setRotationPoint(-3F, 0F, -4F);

		baseModel[2].addBox(0F, -0.65F, -2.25F, 2, 1, 1, 0F); // Box 2
		baseModel[2].setRotationPoint(-1F, -2.5F, -5.5F);
		baseModel[2].rotateAngleX = 0.45378561F;

		baseModel[3].addShapeBox(0F, -0.65F, -2.25F, 1, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[3].setRotationPoint(1F, -2.5F, -5.5F);
		baseModel[3].rotateAngleX = 0.45378561F;

		baseModel[4].addShapeBox(0F, -0.65F, -2.25F, 1, 1, 2, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[4].setRotationPoint(-2F, -2.5F, -5.5F);
		baseModel[4].rotateAngleX = 0.45378561F;

		baseModel[5].addShapeBox(1F, -3F, -2F, 4, 1, 1, 0F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F); // Box 2
		baseModel[5].setRotationPoint(-3F, 0F, 7F);

		baseModel[6].addBox(0F, -0.35F, -2.25F, 2, 1, 1, 0F); // Box 2
		baseModel[6].setRotationPoint(-1F, -2.5F, 5.5F);
		baseModel[6].rotateAngleX = -3.47320521F;

		baseModel[7].addShapeBox(0F, -0.35F, -2.25F, 1, 1, 2, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[7].setRotationPoint(1F, -2.5F, 5.5F);
		baseModel[7].rotateAngleX = -3.47320521F;

		baseModel[8].addShapeBox(0F, -0.35F, -2.25F, 1, 1, 2, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		baseModel[8].setRotationPoint(-2F, -2.5F, 5.5F);
		baseModel[8].rotateAngleX = -3.47320521F;

		baseModel[9].addShape3D(0F, -6F, -2F, new Shape2D(new Coord2D[] { new Coord2D(0, 0, 0, 0), new Coord2D(4, 0, 4, 0), new Coord2D(6, 2, 6, 2), new Coord2D(6, 10, 6, 10), new Coord2D(4, 12, 4, 12), new Coord2D(0, 12, 0, 12) }), 2, 6, 12, 34, 2, ModelRendererTurbo.MR_FRONT, new float[] {12 ,4 ,3 ,8 ,3 ,4}); // Shape 1
		baseModel[9].setRotationPoint(0F, 0F, 0F);
		baseModel[9].rotateAngleX = 1.57079633F;
		baseModel[9].rotateAngleY = -3.14159265F;


		coreModel = new ModelRendererTurbo[1];
		coreModel[0] = new ModelRendererTurbo(this, 0, 8, textureX, textureY); // Box 2

		coreModel[0].addBox(0F, -3F, 0F, 8, 2, 8, 0F); // Box 2
		coreModel[0].setRotationPoint(-4F, 0F, -4F);

		parts.put("base",baseModel);
		parts.put("core",coreModel);

		translateAll(0f,1f,0.5f);

		flipAll();
	}

	@Override
	public void renderCasing(float gunpowderPercentage, int paintColour)
	{
		ClientUtils.bindTexture(TEXTURE);
		for(ModelRendererTurbo mod : baseModel)
			mod.render();
	}

	@Override
	public void renderCore(int coreColour, EnumCoreTypes coreType)
	{
		ClientUtils.bindTexture(TEXTURE);
		float[] c = IIUtils.rgbIntToRGB(coreColour);
		GlStateManager.color(c[0], c[1], c[2]);
		for(ModelRendererTurbo mod : coreModel)
			mod.render();
	}

	public void reloadModels()
	{
		ModelTellermine newModel = new ModelTellermine();
		this.baseModel = newModel.baseModel;
		this.coreModel = newModel.coreModel;
	}
}
