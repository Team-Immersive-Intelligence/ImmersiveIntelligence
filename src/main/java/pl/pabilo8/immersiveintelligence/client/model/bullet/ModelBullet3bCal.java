package pl.pabilo8.immersiveintelligence.client.model.bullet;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.ModelBlockBase;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 21.11.2020
 */
public class ModelBullet3bCal extends ModelBlockBase implements IBulletModel
{
	private static String TEXTURE = ImmersiveIntelligence.MODID+":textures/entity/bullets/3bcal_autocannon.png";
	int textureX = 32;
	int textureY = 16;

	ModelRendererTurbo[] casingModel, paintModel;
	ModelRendererTurbo[] coreSoftpointModel, corePiercingModel, coreCanisterModel;

	public ModelBullet3bCal()
	{
		casingModel = new ModelRendererTurbo[2];
		casingModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0
		casingModel[1] = new ModelRendererTurbo(this, 9, 9, textureX, textureY); // Box 2

		casingModel[0].addShapeBox(0F, 3F, 0F, 3, 3, 3, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		casingModel[0].setRotationPoint(0F, 0F, 0F);

		casingModel[1].addTrapezoid(0F, 1F, 0F, 3, 2, 3, 0F, -0.20F, ModelRendererTurbo.MR_TOP); // Box 2
		casingModel[1].setRotationPoint(0F, 0F, 0F);

		paintModel = new ModelRendererTurbo[1];
		paintModel[0] = new ModelRendererTurbo(this, 9, 5, textureX, textureY); // Box 3

		paintModel[0].addShapeBox(0F, 3F, 0F, 3, 1, 3, 0F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F); // Box 3
		paintModel[0].setRotationPoint(0F, 0F, 0F);

		corePiercingModel = new ModelRendererTurbo[2];
		corePiercingModel[0] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // Box 1
		corePiercingModel[1] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // Box 4

		corePiercingModel[0].addTrapezoid(0.5F, -2F, 0.5F, 2, 3, 2, 0F, -0.80F, ModelRendererTurbo.MR_TOP); // Box 1
		corePiercingModel[0].setRotationPoint(0F, 0F, 0F);

		corePiercingModel[1].addShapeBox(0.5F, 0.99F, 0.5F, 2, 5, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		corePiercingModel[1].setRotationPoint(0F, 0F, 0F);


		coreCanisterModel = new ModelRendererTurbo[2];
		coreCanisterModel[0] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // Box 1
		coreCanisterModel[1] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // Box 4

		coreCanisterModel[0].addShapeBox(0.5F, -2F, 0.5F, 2, 3, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 1
		coreCanisterModel[0].setRotationPoint(0F, 0F, 0F);

		coreCanisterModel[1].addShapeBox(0.5F, 0.99F, 0.5F, 2, 5, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		coreCanisterModel[1].setRotationPoint(0F, 0F, 0F);


		coreSoftpointModel = new ModelRendererTurbo[2];
		coreSoftpointModel[0] = new ModelRendererTurbo(this, 12, 0, textureX, textureY); // Box 1
		coreSoftpointModel[1] = new ModelRendererTurbo(this, 0, 7, textureX, textureY); // Box 4

		coreSoftpointModel[0].addTrapezoid(0.5F, 0F, 0.5F, 2, 1, 2, 0F, -0.50F, ModelRendererTurbo.MR_TOP); // Box 1
		coreSoftpointModel[0].setRotationPoint(0F, 0F, 0F);

		coreSoftpointModel[1].addShapeBox(0.5F, 0.99F, 0.5F, 2, 5, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		coreSoftpointModel[1].setRotationPoint(0F, 0F, 0F);

		parts.put("casing", casingModel);
		parts.put("paint", paintModel);

		parts.put("core_softpoint", coreSoftpointModel);
		parts.put("core_piercing", corePiercingModel);
		parts.put("core_canister", coreCanisterModel);

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
			float[] c = Utils.rgbIntToRGB(paintColour);
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
		float[] c = Utils.rgbIntToRGB(coreColour);
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
}
