package pl.pabilo8.immersiveintelligence.client.render;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;

/**
 * @author Pabilo8
 * @since 11.01.2021
 * <p>
 * Zhe Renderer for zhe Hans
 */
public class HansRenderer extends RenderBiped<EntityHans>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/entity/hans.png");

	public HansRenderer(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelPlayer(0.0f, false), 0.5F);
		this.addLayer(new LayerBipedArmor(this));
		this.addLayer(new LayerHeldItem(this));
	}

	@Override
	protected void preRenderCallback(EntityHans entitylivingbaseIn, float partialTickTime)
	{
		float f = 0.9375F;
		GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityHans entity)
	{
		return TEXTURE;
	}

	@Override
	protected boolean canRenderName(EntityHans entity)
	{
		return true;
	}

	@Override
	protected void renderEntityName(EntityHans entityIn, double x, double y, double z, String name, double distanceSq)
	{
		super.renderEntityName(entityIn, x, y, z, "Hans", distanceSq);
	}
}
