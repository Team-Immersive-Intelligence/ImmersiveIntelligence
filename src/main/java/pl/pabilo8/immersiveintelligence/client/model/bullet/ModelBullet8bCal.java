package pl.pabilo8.immersiveintelligence.client.model.bullet;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * Created by Pabilo8 on 07-06-2019.
 * Created using SMP-Toolbox 2.0 (Old stuff, probably considered dead ^^)
 * Thanks to The Flan's Mod Team for the Turbo Model Thingy used in there
 */
public class ModelBullet8bCal extends ModelIIBase implements IBulletModel
{
	private static final String TEXTURE = "immersiveintelligence:textures/entity/bullets/8bcal_howitzer.png";
	int textureX = 32;
	int textureY = 32;

	ModelRendererTurbo[] casingModel, casingDetailedModel, paintModel;
	ModelRendererTurbo[] coreShapedModel, corePiercingModel, coreCanisterModel;

	public ModelBullet8bCal()
	{
		casingModel = new ModelRendererTurbo[3];
		casingModel[0] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Box 0
		casingModel[1] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Box 2
		casingModel[2] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Box 2

		casingModel[0].setFlipped(true);
		casingModel[0].addShapeBox(-4F, -3F, -4F, 8, 3, 8, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F); // Box 0
		casingModel[0].setRotationPoint(0F, 0F, 0F);

		casingModel[1].addTrapezoid(-4F, -13F, -4F, 8, 4, 8, 0F, -1.00F, ModelRendererTurbo.MR_TOP); // Box 2
		casingModel[1].setRotationPoint(0F, 0F, 0F);

		casingModel[2].addShapeBox(-4F, -9F, -4F, 8, 6, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		casingModel[2].setRotationPoint(0F, 0F, 0F);

		casingDetailedModel = new ModelRendererTurbo[9];
		casingDetailedModel[0] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Box 0
		casingDetailedModel[1] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Box 2
		casingDetailedModel[2] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Box 2
		casingDetailedModel[3] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Box 2
		casingDetailedModel[4] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Box 2
		casingDetailedModel[5] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Box 2
		casingDetailedModel[6] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Box 2
		casingDetailedModel[7] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Box 2
		casingDetailedModel[8] = new ModelRendererTurbo(this, 0, 14, textureX, textureY); // Box 2

		casingDetailedModel[0].setFlipped(true);
		casingDetailedModel[0].addShapeBox(-4F, -3F, -4F, 8, 3, 8, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, -3F, 0F); // Box 0
		casingDetailedModel[0].setRotationPoint(0F, 0F, 0F);

		casingDetailedModel[1].addShapeBox(-4F, -9F, -4F, 1, 6, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		casingDetailedModel[1].setRotationPoint(0F, 0F, 0F);

		casingDetailedModel[2].addShapeBox(-4.01F, -9F, -4F, 1, 6, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		casingDetailedModel[2].setRotationPoint(0F, 0F, 0F);
		casingDetailedModel[2].rotateAngleY = 3.14159265F;

		casingDetailedModel[3].addShapeBox(-4F, -9F, -4F, 1, 6, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		casingDetailedModel[3].setRotationPoint(0F, 0F, 0F);
		casingDetailedModel[3].rotateAngleY = 1.57079633F;

		casingDetailedModel[4].addShapeBox(-4.01F, -9F, -4.01F, 1, 6, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		casingDetailedModel[4].setRotationPoint(0F, 0F, 0F);
		casingDetailedModel[4].rotateAngleY = 4.71238898F;

		casingDetailedModel[5].addShapeBox(-4F, -13F, -4F, 1, 4, 8, 0F, -1F, 0F, -1F, 1F, 0F, -1F, 1F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		casingDetailedModel[5].setRotationPoint(0F, 0F, 0F);

		casingDetailedModel[6].addShapeBox(-4.01F, -13F, -4F, 1, 4, 8, 0F, -1F, 0F, -1F, 1F, 0F, -1F, 1F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		casingDetailedModel[6].setRotationPoint(0F, 0F, 0F);
		casingDetailedModel[6].rotateAngleY = 1.57079633F;

		casingDetailedModel[7].addShapeBox(-4.01F, -13F, -4.01F, 1, 4, 8, 0F, -1F, 0F, -1F, 1F, 0F, -1F, 1F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		casingDetailedModel[7].setRotationPoint(0F, 0F, 0F);
		casingDetailedModel[7].rotateAngleY = 3.14159265F;

		casingDetailedModel[8].addShapeBox(-4F, -13F, -4.01F, 1, 4, 8, 0F, -1F, 0F, -1F, 1F, 0F, -1F, 1F, 0F, -1F, -1F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 2
		casingDetailedModel[8].setRotationPoint(0F, 0F, 0F);
		casingDetailedModel[8].rotateAngleY = 4.71238898F;


		paintModel = new ModelRendererTurbo[4];
		paintModel[0] = new ModelRendererTurbo(this, 0, 28, textureX, textureY); // Box 3
		paintModel[1] = new ModelRendererTurbo(this, 0, 28, textureX, textureY); // Box 3
		paintModel[2] = new ModelRendererTurbo(this, 0, 20, textureX, textureY); // Box 3
		paintModel[3] = new ModelRendererTurbo(this, 0, 20, textureX, textureY); // Box 3

		paintModel[0].addShapeBox(-4.0f, -8.0f, -4.01f, 8, 4, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 3
		paintModel[0].setRotationPoint(0F, 0F, 0F);

		paintModel[1].addShapeBox(-4.0f, -8.0f, 4.01F, 8, 4, 0, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 3
		paintModel[1].setRotationPoint(0F, 0F, 0F);

		paintModel[2].addShapeBox(-4.01f, -8.0f, -4.0f, 0, 4, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 3
		paintModel[2].setRotationPoint(0F, 0F, 0F);

		paintModel[3].addShapeBox(4.01F, -8.0f, -4.0f, 0, 4, 8, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 3
		paintModel[3].setRotationPoint(0F, 0F, 0F);


		coreCanisterModel = new ModelRendererTurbo[2];
		coreCanisterModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 1
		coreCanisterModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 4

		coreCanisterModel[0].addShapeBox(-2.5f, -16.0f, -2.5f, 5, 3, 5, 0F, -1.0f, 0F, -1.0f, -1.0f, 0F, -1.0f, -1.0f, 0F, -1.0f, -1.0f, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		coreCanisterModel[0].setRotationPoint(0F, 0F, 0F);

		coreCanisterModel[1].addShapeBox(-2.5f, -13.01f, -2.5f, 5, 8, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		coreCanisterModel[1].setRotationPoint(0F, 0F, 0F);

		corePiercingModel = new ModelRendererTurbo[2];
		corePiercingModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 1
		corePiercingModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 4

		corePiercingModel[0].addShapeBox(-2.5f, -16.0f, -2.5f, 5, 3, 5, 0F, -2.0f, 0F, -2.0f, -2.0f, 0F, -2.0f, -2.0f, 0F, -2.0f, -2.0f, 0F, -2.0f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		corePiercingModel[0].setRotationPoint(0F, 0F, 0F);

		corePiercingModel[1].addShapeBox(-2.5f, -13.01f, -2.5f, 5, 8, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		corePiercingModel[1].setRotationPoint(0F, 0F, 0F);

		coreShapedModel = new ModelRendererTurbo[10];
		coreShapedModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 1
		coreShapedModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 4
		coreShapedModel[2] = new ModelRendererTurbo(this, 20, 0, textureX, textureY); // Box 1
		coreShapedModel[3] = new ModelRendererTurbo(this, 20, 0, textureX, textureY); // Box 1
		coreShapedModel[4] = new ModelRendererTurbo(this, 20, 7, textureX, textureY); // Box 1
		coreShapedModel[5] = new ModelRendererTurbo(this, 20, 7, textureX, textureY); // Box 1
		coreShapedModel[6] = new ModelRendererTurbo(this, 15, 0, textureX, textureY); // Box 1
		coreShapedModel[7] = new ModelRendererTurbo(this, 15, 0, textureX, textureY); // Box 1
		coreShapedModel[8] = new ModelRendererTurbo(this, 20, 0, textureX, textureY); // Box 1
		coreShapedModel[9] = new ModelRendererTurbo(this, 20, 0, textureX, textureY); // Box 1

		coreShapedModel[0].addBox(-2.5f, -14.0f, -2.5f, 5, 1, 5, 0F); // Box 1
		coreShapedModel[0].setRotationPoint(0F, 0F, 0F);

		coreShapedModel[1].addShapeBox(-2.5f, -13.01f, -2.5f, 5, 8, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		coreShapedModel[1].setRotationPoint(0F, 0F, 0F);

		coreShapedModel[2].addShapeBox(-2.5f, -16.0f, -2.5f, 1, 2, 5, 0F, -1.0f, 0F, -1.0f, 0F, 0F, -1.0f, 0F, 0F, -1.0f, -1.0f, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		coreShapedModel[2].setRotationPoint(0F, 0F, 0F);

		coreShapedModel[3].addShapeBox(1.5F, -16.0f, -2.5f, 1, 2, 5, 0F, 0F, 0F, -1.0f, -1.0f, 0F, -1.0f, -1.0f, 0F, -1.0f, 0F, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		coreShapedModel[3].setRotationPoint(0F, 0F, 0F);

		coreShapedModel[4].addShapeBox(-1.5f, -16.0f, -2.5f, 3, 2, 1, 0F, 0F, 0F, -1.0f, 0F, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		coreShapedModel[4].setRotationPoint(0F, 0F, 0F);

		coreShapedModel[5].addShapeBox(-1.5f, -16.0f, 1.5F, 3, 2, 1, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1.0f, 0F, 0F, -1.0f, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		coreShapedModel[5].setRotationPoint(0F, 0F, 0F);

		coreShapedModel[6].addBox(-1.5f, -16.0f, -1.5f, 1, 2, 3, 0F); // Box 1
		coreShapedModel[6].setRotationPoint(0F, 0F, 0F);

		coreShapedModel[7].addBox(0.5F, -16.0f, -1.5f, 1, 2, 3, 0F); // Box 1
		coreShapedModel[7].setRotationPoint(0F, 0F, 0F);

		coreShapedModel[8].addBox(-0.5f, -16.0f, -1.5f, 1, 2, 1, 0F); // Box 1
		coreShapedModel[8].setRotationPoint(0F, 0F, 0F);

		coreShapedModel[9].addBox(-0.5f, -16.0f, 0.5F, 1, 2, 1, 0F); // Box 1
		coreShapedModel[9].setRotationPoint(0F, 0F, 0F);

		parts.put("casing", casingModel);
		parts.put("casing_detailed", casingDetailedModel);
		parts.put("paint", paintModel);

		parts.put("core_shaped", coreShapedModel);
		parts.put("core_piercing", corePiercingModel);
		parts.put("core_canister", coreCanisterModel);

		flipAll();
	}

	@Override
	public void renderBulletUnused(int coreColour, EnumCoreTypes coreType, int paintColour)
	{
		renderCasingUndetailed(paintColour);
		renderCore(coreColour, coreType);
	}

	public void renderCasingUndetailed(int paintColour)
	{
		ClientUtils.bindTexture(TEXTURE);
		for(ModelRendererTurbo model : casingModel)
			model.render(0.0625f);

		if(paintColour!=-1)
		{
			float[] c = IIUtils.rgbIntToRGB(paintColour);
			GlStateManager.color(c[0], c[1], c[2]);
			for(ModelRendererTurbo model : paintModel)
				model.render(0.0625f);
			GlStateManager.color(1f, 1f, 1f, 1f);
		}
	}

	@Override
	public void renderCasing(float gunpowderPercentage, int paintColour)
	{
		ClientUtils.bindTexture(TEXTURE);
		for(ModelRendererTurbo model : casingDetailedModel)
			model.render(0.0625f);

		if(paintColour!=-1)
		{
			float[] c = IIUtils.rgbIntToRGB(paintColour);
			GlStateManager.color(c[0], c[1], c[2]);
			for(ModelRendererTurbo model : paintModel)
				model.render(0.0625f);
			GlStateManager.color(1f, 1f, 1f, 1f);
		}


		if(gunpowderPercentage > 0)
		{
			GlStateManager.pushMatrix();
			ClientUtils.bindTexture("minecraft:textures/blocks/concrete_powder_black.png");
			GlStateManager.translate(0, 0.2f+0.25*gunpowderPercentage,0);
			GlStateManager.rotate(90, 1f, 0f, 0f);
			ClientUtils.drawTexturedRect(-0.1875f, -0.1875f, 0.375f, 0.375f, 0.25f, 0.625f, 0.25f, 0.625f);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void renderCore(int coreColour, EnumCoreTypes coreType)
	{
		ClientUtils.bindTexture(TEXTURE);
		float[] c = IIUtils.rgbIntToRGB(coreColour);
		GlStateManager.color(c[0], c[1], c[2]);
		switch(coreType)
		{
			case SHAPED:
				for(ModelRendererTurbo model : coreShapedModel)
					model.render(0.0625f);
				break;
			case PIERCING:
				for(ModelRendererTurbo model : corePiercingModel)
					model.render(0.0625f);
				break;
			case CANISTER:
				for(ModelRendererTurbo model : coreCanisterModel)
					model.render(0.0625f);
				break;
		}
		GlStateManager.color(1f, 1f, 1f, 1f);
	}

	@Override
	public void reloadModels()
	{
		ModelBullet8bCal newModel = new ModelBullet8bCal();
		this.casingModel = newModel.casingModel;
		this.casingDetailedModel = newModel.casingDetailedModel;
		this.corePiercingModel = newModel.corePiercingModel;
		this.coreCanisterModel = newModel.coreCanisterModel;
		this.coreShapedModel = newModel.coreShapedModel;
		this.paintModel = newModel.paintModel;
	}
}
