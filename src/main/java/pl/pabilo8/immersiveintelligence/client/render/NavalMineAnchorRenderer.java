package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelNavalMineAnchor;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityNavalMineAnchor;

/**
 * @author Pabilo8
 * @since 21.01.2021
 */
public class NavalMineAnchorRenderer extends Render<EntityNavalMineAnchor> implements IReloadableModelContainer<NavalMineAnchorRenderer>
{
	public static ModelNavalMineAnchor model = new ModelNavalMineAnchor();
	public static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/entity/bullets/naval_mine_anchor.png";

	public NavalMineAnchorRenderer(RenderManager renderManager)
	{
		super(renderManager);
		subscribeToList("naval_mine_anchor");
	}

	@Override
	public void doRender(EntityNavalMineAnchor entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableLighting();

		ClientUtils.bindTexture(TEXTURE);

		GlStateManager.disableLighting();
		RenderHelper.enableStandardItemLighting();
		for(ModelRendererTurbo mod : model.baseModel)
			mod.render();

		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	@Override
	public void reloadModels()
	{
		model = new ModelNavalMineAnchor();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityNavalMineAnchor entity)
	{
		return null;
	}
}
