package pl.pabilo8.immersiveintelligence.client.model.bullet;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Grenade;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
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
public class ModelGrenade extends ModelIIBase implements IBulletModel
{
	private static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/entity/bullets/grenade.png";
	int textureX = 32;
	int textureY = 32;
	ModelRendererTurbo[] coreModel, coreClassicModel, coreMoreClassicModel, paintModel;

	public ModelGrenade() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[1];
		baseModel[0] = new ModelRendererTurbo(this, 8, 15, textureX, textureY); // Box 0

		baseModel[0].addShapeBox(-1F, -11F, -1F, 2, 11, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		baseModel[0].setRotationPoint(0F, 0F, 0F);

		coreModel = new ModelRendererTurbo[1];
		coreModel[0] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 0

		coreModel[0].addShapeBox(-2.5F, -18F, -2.5F, 5, 7, 5, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		coreModel[0].setRotationPoint(0F, 0F, 0F);


		coreClassicModel = new ModelRendererTurbo[1];
		coreClassicModel[0] = new ModelRendererTurbo(this, 16, 22, textureX, textureY); // Box 0

		coreClassicModel[0].addShapeBox(-2F, -17F, -2F, 4, 6, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		coreClassicModel[0].setRotationPoint(0F, 0F, 0F);


		coreMoreClassicModel = new ModelRendererTurbo[1];
		coreMoreClassicModel[0] = new ModelRendererTurbo(this, 16, 12, textureX, textureY); // Box 0

		coreMoreClassicModel[0].addShapeBox(-2F, -17F, -2F, 4, 6, 4, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 0
		coreMoreClassicModel[0].setRotationPoint(0F, 0F, 0F);


		paintModel = new ModelRendererTurbo[2];
		paintModel[0] = new ModelRendererTurbo(this, 8, 12, textureX, textureY); // Box 0
		paintModel[1] = new ModelRendererTurbo(this, 0, 12, textureX, textureY); // Box 0

		paintModel[0].addShapeBox(-1F, -1F, -1F, 2, 1, 2, 0F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F, 0.01F); // Box 0
		paintModel[0].setRotationPoint(0F, 0F, 0F);

		paintModel[1].addShapeBox(-1F, -8F, -1F, 2, 4, 2, 0F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F, 0.01F, 0F, 0.01F); // Box 0
		paintModel[1].setRotationPoint(0F, 0F, 0F);

		parts.put("base", baseModel);
		parts.put("core", coreModel);
		parts.put("core_classic", coreClassicModel);
		parts.put("core_more_classic", coreMoreClassicModel);
		parts.put("paint", paintModel);
		flipAll();
	}

	@Override
	public void renderBulletUsed(int coreColour, EnumCoreTypes coreType, int paintColour)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(0,-0.5,-0.125 );
		renderCasing(1f, paintColour);
		renderCore(coreColour, coreType);
		GlStateManager.popMatrix();
	}

	@Override
	public void renderCasing(float gunpowderPercentage, int paintColour)
	{
		ClientUtils.bindTexture(TEXTURE);
		for(ModelRendererTurbo model : baseModel)
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
	public void renderCore(int coreColour, EnumCoreTypes coreType)
	{
		ClientUtils.bindTexture(TEXTURE);
		switch(Grenade.classicGrenades)
		{
			default:
			case 0:
			{
				float[] c = IIUtils.rgbIntToRGB(coreColour);
				GlStateManager.color(c[0], c[1], c[2]);
				for(ModelRendererTurbo model : coreModel)
					model.render(0.0625f);
			}
			break;
			case 1:
			{
				float[] c = IIUtils.rgbIntToRGB(coreColour);
				GlStateManager.color(c[0], c[1], c[2]);
				for(ModelRendererTurbo model : coreClassicModel)
					model.render(0.0625f);
			}
			break;
			case 2:
			{
				for(ModelRendererTurbo model : coreMoreClassicModel)
					model.render(0.0625f);
			}
			break;
		}
	}

	@Override
	public void reloadModels()
	{
		ModelGrenade newModel = new ModelGrenade();
		this.baseModel = newModel.baseModel;
		this.coreModel = newModel.coreModel;
		this.coreClassicModel = newModel.coreClassicModel;
		this.coreMoreClassicModel = newModel.coreMoreClassicModel;
		this.paintModel = newModel.paintModel;
	}
}
