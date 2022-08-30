package pl.pabilo8.immersiveintelligence.client.model.bullet;

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
 * @since 21.11.2020
 */
public class ModelBullet3bCal extends ModelIIBase implements IBulletModel
{
	private static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/entity/bullets/3bcal_autocannon.png";
	int textureX = 32;
	int textureY = 16;

	ModelRendererTurbo[] casingModel, casingDetailedModel, paintModel;
	ModelRendererTurbo[] coreSoftpointModel, corePiercingModel, coreCanisterModel;

	public ModelBullet3bCal()
	{
		casingModel = new ModelRendererTurbo[2];
		casingModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		casingModel[1] = new ModelRendererTurbo(this, 9, 9, textureX, textureY); // Box 2

		casingModel[0].addShapeBox(-1.5F, -3F, -1.5F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		casingModel[0].setRotationPoint(0F, 0F, 0F);

		casingModel[1].addTrapezoid(-1.5F, -5F, -1.5F, 3, 2, 3, 0F, -0.20F, ModelRendererTurbo.MR_TOP); // Box 2
		casingModel[1].setRotationPoint(0F, 0F, 0F);

		casingDetailedModel = new ModelRendererTurbo[9];
		casingDetailedModel[0] = new ModelRendererTurbo(this, 24, 11, textureX, textureY); // Box 3
		casingDetailedModel[1] = new ModelRendererTurbo(this, 24, 11, textureX, textureY); // Box 3
		casingDetailedModel[2] = new ModelRendererTurbo(this, 24, 8, textureX, textureY); // Box 3
		casingDetailedModel[3] = new ModelRendererTurbo(this, 24, 8, textureX, textureY); // Box 3
		casingDetailedModel[4] = new ModelRendererTurbo(this, 24, 3, textureX, textureY); // Box 3
		casingDetailedModel[5] = new ModelRendererTurbo(this, 24, 3, textureX, textureY); // Box 3
		casingDetailedModel[6] = new ModelRendererTurbo(this, 20, 0, textureX, textureY); // Box 3
		casingDetailedModel[7] = new ModelRendererTurbo(this, 20, 0, textureX, textureY); // Box 3
		casingDetailedModel[8] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0

		casingDetailedModel[0].addShapeBox(-1.5F, -3F, -1.5F, 1, 2, 3, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F); // Box 3
		casingDetailedModel[0].setRotationPoint(0F, 0F, 0F);

		casingDetailedModel[1].addShapeBox(0.5F, -3F, -1.5F, 1, 2, 3, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F); // Box 3
		casingDetailedModel[1].setRotationPoint(0F, 0F, 0F);

		casingDetailedModel[2].addShapeBox(-1.5F, -3F, -1.51F, 3, 2, 1, 0F, -0.01F, 0F, 0F, -0.01F, 0F, 0F, -0.01F, 0F, -0.5F, -0.01F, 0F, -0.5F, -0.01F, 0F, 0F, -0.01F, 0F, 0F, -0.01F, 0F, -0.5F, -0.01F, 0F, -0.5F); // Box 3
		casingDetailedModel[2].setRotationPoint(0F, 0F, 0F);

		casingDetailedModel[3].addShapeBox(-1.5F, -3F, 0.49F, 3, 2, 1, 0F, -0.01F, 0F, -0.5F, -0.01F, 0F, -0.5F, -0.01F, 0F, 0F, -0.01F, 0F, 0F, -0.01F, 0F, -0.5F, -0.01F, 0F, -0.5F, -0.01F, 0F, 0F, -0.01F, 0F, 0F); // Box 3
		casingDetailedModel[3].setRotationPoint(0F, 0F, 0F);

		casingDetailedModel[4].addShapeBox(-1.5F, -5F, -1.5F, 1, 2, 3, 0F, -0.25F, 0F, -0.25F, -0.5F, 0F, -0.25F, -0.5F, 0F, -0.25F, -0.25F, 0F, -0.25F, 0F, 0F, 0F, -0.5F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F); // Box 3
		casingDetailedModel[4].setRotationPoint(0F, 0F, 0F);

		casingDetailedModel[5].addShapeBox(0.5F, -5F, -1.5F, 1, 2, 3, 0F, -0.5F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.5F, 0F, -0.25F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F); // Box 3
		casingDetailedModel[5].setRotationPoint(0F, 0F, 0F);

		casingDetailedModel[6].addShapeBox(-1.5F, -5F, -1.51F, 3, 2, 1, 0F, -0.26F, 0F, -0.25F, -0.26F, 0F, -0.25F, -0.26F, 0F, -0.5F, -0.26F, 0F, -0.5F, -0.01F, 0F, 0F, -0.01F, 0F, 0F, -0.01F, 0F, -0.5F, -0.01F, 0F, -0.5F); // Box 3
		casingDetailedModel[6].setRotationPoint(0F, 0F, 0F);

		casingDetailedModel[7].addShapeBox(-1.5F, -5F, 0.49F, 3, 2, 1, 0F, -0.26F, 0F, -0.5F, -0.26F, 0F, -0.5F, -0.26F, 0F, -0.25F, -0.26F, 0F, -0.25F, -0.01F, 0F, -0.5F, -0.01F, 0F, -0.5F, -0.01F, 0F, 0F, -0.01F, 0F, 0F); // Box 3
		casingDetailedModel[7].setRotationPoint(0F, 0F, 0F);

		casingDetailedModel[8].addShapeBox(-1.5F, -1F, -1.5F, 3, 1, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		casingDetailedModel[8].setRotationPoint(0F, 0F, 0F);


		paintModel = new ModelRendererTurbo[1];
		paintModel[0] = new ModelRendererTurbo(this, 9, 5, textureX, textureY); // Box 3

		paintModel[0].addShapeBox(-1.5F, -3F, -1.5F, 3, 1, 3, 0F, 0.05F, 0F, 0.05F, 0.05F, 0F, 0.05F, 0.05F, 0F, 0.05F, 0.05F, 0F, 0.05F, 0.05F, 0F, 0.05F, 0.05F, 0F, 0.05F, 0.05F, 0F, 0.05F, 0.05F, 0F, 0.05F); // Box 3
		paintModel[0].setRotationPoint(0F, 0F, 0F);


		coreCanisterModel = new ModelRendererTurbo[2];
		coreCanisterModel[0] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // Box 1
		coreCanisterModel[1] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // Box 4

		coreCanisterModel[0].addShapeBox(-1F, -8F, -1F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		coreCanisterModel[0].setRotationPoint(0F, 0F, 0F);

		coreCanisterModel[1].addShapeBox(-1F, -5.01F, -1F, 2, 5, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		coreCanisterModel[1].setRotationPoint(0F, 0F, 0F);

		corePiercingModel = new ModelRendererTurbo[2];
		corePiercingModel[0] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // Box 1
		corePiercingModel[1] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // Box 4

		corePiercingModel[0].addTrapezoid(-1F, -8F, -1F, 2, 3, 2, 0F, -0.80F, ModelRendererTurbo.MR_TOP); // Box 1
		corePiercingModel[0].setRotationPoint(0F, 0F, 0F);

		corePiercingModel[1].addShapeBox(-1F, -5.01F, -1F, 2, 5, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		corePiercingModel[1].setRotationPoint(0F, 0F, 0F);

		coreSoftpointModel = new ModelRendererTurbo[3];
		coreSoftpointModel[0] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // Box 1
		coreSoftpointModel[1] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // Box 4
		coreSoftpointModel[2] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // Box 1

		coreSoftpointModel[0].addShapeBox(-1F, -7F, -1F, 2, 1, 2, 0F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, -0.5F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		coreSoftpointModel[0].setRotationPoint(0F, 0F, 0F);

		coreSoftpointModel[1].addShapeBox(-1F, -5.01F, -1F, 2, 5, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		coreSoftpointModel[1].setRotationPoint(0F, 0F, 0F);

		coreSoftpointModel[2].addBox(-1F, -6F, -1F, 2, 1, 2, 0F); // Box 1
		coreSoftpointModel[2].setRotationPoint(0F, 0F, 0F);

		parts.put("casing", casingModel);
		parts.put("casing_detailed", casingDetailedModel);
		parts.put("paint", paintModel);

		parts.put("core_softpoint", coreSoftpointModel);
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
			GlStateManager.translate(0, 0.0625f+0.2*gunpowderPercentage,0);
			GlStateManager.rotate(90, 1f, 0f, 0f);
			ClientUtils.drawTexturedRect(-1/16f, -1/16f, 2/16f, 2/16f, 4/16f, 6/16f, 4/16f, 6/16f);
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
			case SOFTPOINT:
				for(ModelRendererTurbo model : coreSoftpointModel)
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
		ModelBullet3bCal newModel = new ModelBullet3bCal();
		this.baseModel = newModel.baseModel;
		this.casingModel = newModel.casingModel;
		this.casingDetailedModel = newModel.casingDetailedModel;
		this.corePiercingModel = newModel.corePiercingModel;
		this.coreCanisterModel = newModel.coreCanisterModel;
		this.coreSoftpointModel = newModel.coreSoftpointModel;
		this.paintModel = newModel.paintModel;
	}
}
