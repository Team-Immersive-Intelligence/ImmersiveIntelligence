package pl.pabilo8.immersiveintelligence.client.model.bullet;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 17.01.2021
 */
public class ModelRailgunGrenade extends ModelGrenade
{
	private static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/entity/bullets/railgun_grenade.png";

	public ModelRailgunGrenade()
	{
		super();
	}

	@Override
	public void renderCore(int coreColour, EnumCoreTypes coreType)
	{
		ClientUtils.bindTexture(TEXTURE);
		float[] c = Utils.rgbIntToRGB(coreColour);
		GlStateManager.color(c[0], c[1], c[2]);
		for(ModelRendererTurbo model : coreClassicModel)
			model.render(0.0625f);
	}

	@Override
	public void renderBulletUsed(int coreColour, EnumCoreTypes coreType, int paintColour)
	{
		renderCasing(1f, paintColour);
		renderCore(coreColour, coreType);
	}

	@Override
	public void renderCasing(float gunpowderPercentage, int paintColour)
	{
		ClientUtils.bindTexture(TEXTURE);
		for(ModelRendererTurbo model : baseModel)
			model.render(0.0625f);
		if(paintColour!=-1)
			for(ModelRendererTurbo model : paintModel)
				model.render(0.0625f);
	}
}
