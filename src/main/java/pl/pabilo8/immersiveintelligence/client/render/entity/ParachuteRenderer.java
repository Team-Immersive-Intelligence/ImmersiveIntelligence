package pl.pabilo8.immersiveintelligence.client.render.entity;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIEntityRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIEntityRenderer.RegisteredEntityRenderer;
import pl.pabilo8.immersiveintelligence.common.entity.EntityParachute;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * Displays a recoverable parachute after landing.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.11.2026
 */
@RegisteredEntityRenderer(clazz = EntityParachute.class, name = "fallen_parachute")
public class ParachuteRenderer extends IIEntityRenderer<EntityParachute>
{
	private static final ResLoc MODEL = IIReference.RES_II.with("models/entity/fallen_parachute").withExtension(ResLoc.EXT_OBJ);
	private AMTModel model;
	private IIAnimationCompiledMap showup;

	public ParachuteRenderer(RenderManager manager)
	{
		super(manager);
	}

	@Override
	public void draw(EntityParachute entity, BufferBuilder buffer, float partialTicks, Tessellator tessellator)
	{
		if(!entity.hasLanded())
			return;
		this.model.defaultize();
		this.showup.apply(AMTUtils.getAnimationProgress(entity.getLandingAge(), 60, false, partialTicks));
		GlStateManager.rotate(-entity.rotationYaw, 0, 1, 0);
		GlStateManager.disableCull();
		this.model.render(tessellator, buffer);
		GlStateManager.enableCull();
	}

	@Override
	public void compileModels()
	{
		this.model = new AMTModel(DefaultVertexFormats.BLOCK, MODEL);
		this.showup = IIAnimationCompiledMap.create(model, IIReference.RES_II.with("backpack/fallen_parachute_showup"));
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		AMTLoader.preloadTexturesFromOBJ(MODEL, map);
	}

	@Override
	protected void nullifyModels()
	{
		this.model = AMTUtils.disposeOf(this.model);
		this.showup = null;
	}
}
