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
public class ModelBullet2bCal extends ModelIIBase implements IBulletModel
{
	private static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/entity/bullets/2bcal_machinegun.png";
	int textureX = 32;
	int textureY = 16;

	ModelRendererTurbo[] casingModel, paintModel;
	ModelRendererTurbo[] coreSoftpointModel, corePiercingModel;

	public ModelBullet2bCal()
	{
		casingModel = new ModelRendererTurbo[2];
		casingModel[0] = new ModelRendererTurbo(this, 0, 9, textureX, textureY); // Box 0
		casingModel[1] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0

		casingModel[0].addBox(-1F, -3F, -1F, 2, 3, 2, 0F); // Box 0
		casingModel[0].setRotationPoint(0F, 0F, 0F);

		casingModel[1].addShapeBox(-1F, -5F, -1F, 2, 2, 2, 0F, -0.25F, -0.75F, -0.25F, -0.25F, -0.75F, -0.25F, -0.25F, -0.75F, -0.25F, -0.25F, -0.75F, -0.25F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		casingModel[1].setRotationPoint(0F, 0F, 0F);


		paintModel = new ModelRendererTurbo[1];
		paintModel[0] = new ModelRendererTurbo(this, 8, 0, textureX, textureY); // Box 0

		paintModel[0].addShapeBox(-1F, -3F, -1F, 2, 2, 2, 0F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F); // Box 0
		paintModel[0].setRotationPoint(0F, 0F, 0F);


		coreSoftpointModel = new ModelRendererTurbo[2];
		coreSoftpointModel[0] = new ModelRendererTurbo(this, 0, 4, textureX, textureY); // Box 0
		coreSoftpointModel[1] = new ModelRendererTurbo(this, 8, 4, textureX, textureY); // Box 0

		coreSoftpointModel[0].addShapeBox(-1F, -5F, -1F, 2, 2, 2, 0F, -0.5F, -0.5F, -0.5F, -0.5F, -0.5F, -0.5F, -0.5F, -0.5F, -0.5F, -0.5F, -0.5F, -0.5F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F); // Box 0
		coreSoftpointModel[0].setRotationPoint(0F, 0F, 0F);

		coreSoftpointModel[1].addShapeBox(-1F, -3F, -1F, 2, 3, 2, 0F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F); // Box 0
		coreSoftpointModel[1].setRotationPoint(0F, 0F, 0F);


		corePiercingModel = new ModelRendererTurbo[2];
		corePiercingModel[0] = new ModelRendererTurbo(this, 0, 4, textureX, textureY); // Box 0
		corePiercingModel[1] = new ModelRendererTurbo(this, 8, 4, textureX, textureY); // Box 0

		corePiercingModel[0].addShapeBox(-1F, -6F, -1F, 2, 3, 2, 0F, -0.625F, 0F, -0.625F, -0.625F, 0F, -0.625F, -0.625F, 0F, -0.625F, -0.625F, 0F, -0.625F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F); // Box 0
		corePiercingModel[0].setRotationPoint(0F, 0F, 0F);

		corePiercingModel[1].addShapeBox(-1F, -3F, -1F, 2, 3, 2, 0F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F); // Box 0
		corePiercingModel[1].setRotationPoint(0F, 0F, 0F);


		parts.put("casing", casingModel);
		parts.put("paint", paintModel);

		parts.put("core_softpoint", coreSoftpointModel);
		parts.put("core_piercing", corePiercingModel);

		flipAll();
	}

	@Override
	public void renderCasing(float gunpowderPercentage, int paintColour)
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

		/*
		if(gunpowderPercentage > 0)
		{
			GlStateManager.pushMatrix();
			ClientUtils.bindTexture("minecraft:textures/blocks/concrete_powder_black.png");
			GlStateManager.translate(0.3125f, 0.25*gunpowderPercentage, -0.6875f);
			GlStateManager.rotate(90, 1f, 0f, 0f);
			ClientUtils.drawTexturedRect(0f, 0f, 6/16f, 6/16f, 0f, 6/16f, 0f, 6/16f);
			GlStateManager.popMatrix();
		}
		 */
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
		}
		GlStateManager.color(1f, 1f, 1f, 1f);
	}

	@Override
	public void reloadModels()
	{
		ModelBullet2bCal newModel = new ModelBullet2bCal();
		this.baseModel = newModel.baseModel;
		this.casingModel = newModel.casingModel;
		this.corePiercingModel = newModel.corePiercingModel;
		this.coreSoftpointModel = newModel.coreSoftpointModel;
		this.paintModel = newModel.paintModel;
	}
}
