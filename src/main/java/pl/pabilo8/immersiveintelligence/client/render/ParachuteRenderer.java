package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelParachute;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.entity.EntityParachute;

/**
 * @author Pabilo8
 * @since 21.01.2021
 */
public class ParachuteRenderer extends Render<EntityParachute> implements IReloadableModelContainer<ParachuteRenderer>
{
	public static ModelParachute model = new ModelParachute();
	private static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/entity/parachute.png";
	private static final ResourceLocation TEXTURE_WHITE = new ResourceLocation("immersiveengineering", "textures/items/white.png");

	public ParachuteRenderer(RenderManager renderManager)
	{
		super(renderManager);
		subscribeToList("parachute");
	}

	@Override
	public void doRender(EntityParachute entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y-5.5, z);
		GlStateManager.rotate(entityYaw,0,1,0);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableLighting();

		GlStateManager.disableLighting();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.pushMatrix();
		double tt = 1d-Math.max((entity.time-partialTicks)/20d,0);
		GlStateManager.scale(tt,1,tt);

		Entity controllingPassenger = entity.getControllingPassenger();
		if(controllingPassenger!=null)
		{
			GlStateManager.disableCull();
			GlStateManager.pushMatrix();
			ClientUtils.mc().getTextureManager().bindTexture(new ResourceLocation("immersiveengineering:textures/blocks/wire.png"));

			GlStateManager.scale(0.0625f, 0.0625f, 0.0625f);
			GlStateManager.translate(0, 14, 0);

			GlStateManager.color(150f/255f, 126f/255f, 109f/255f);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

			drawRope(buffer,-5,-8,0,-7 ,16,0,1,0);
			drawRope(buffer,-5,-8,0,-7 ,16,0,0,1);

			drawRope(buffer,5,-8,0,7 ,16,0,1,0);
			drawRope(buffer,5,-8,0,7 ,16,0,0,1);
			//drawRope(buffer,-7,-8,0, -7,16,0,0,1);

			drawRope(buffer,-7,16,0, -43,83,-20,1,0);
			drawRope(buffer,-7,16,0, -43,83,-20,0,1);
			drawRope(buffer,-7,16,0, -43,83,12,1,0);
			drawRope(buffer,-7,16,0, -43,83,12,0,1);

			drawRope(buffer,-7,16,0, -16,83,-46,1,0);
			drawRope(buffer,-7,16,0, -16,83,-46,0,1);
			drawRope(buffer,-7,16,0, -16,83,40,1,0);
			drawRope(buffer,-7,16,0, -16,83,40,0,1);

			drawRope(buffer,7,16,0, 43,83,-20,1,0);
			drawRope(buffer,7,16,0, 43,83,-20,0,1);
			drawRope(buffer,7,16,0, 43,83,12,1,0);
			drawRope(buffer,7,16,0, 43,83,12,0,1);

			drawRope(buffer,7,16,0, 16,83,-46,1,0);
			drawRope(buffer,7,16,0, 16,83,-46,0,1);
			drawRope(buffer,7,16,0, 16,83,40,1,0);
			drawRope(buffer,7,16,0, 16,83,40,0,1);

			tessellator.draw();

			GlStateManager.enableCull();
			GlStateManager.color(1f,1f,1f);

			GlStateManager.popMatrix();
		}
		ClientUtils.mc().getTextureManager().bindTexture(new ResourceLocation(TEXTURE));

		for(ModelRendererTurbo mod : model.baseModel)
			mod.render();
		GlStateManager.popMatrix();
		for(ModelRendererTurbo mod : model.linkThingyModel)
			mod.render();

		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	@Override
	public void reloadModels()
	{
		model = new ModelParachute();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityParachute entity)
	{
		return null;
	}

	public static void drawRope(BufferBuilder buff, double x, double y, double z, double xx, double yy, double zz,int xdiff, int zdiff)
	{
		buff.pos(x+xdiff, y, z-zdiff).tex(0f, 0f).endVertex();
		buff.pos(xx+xdiff, yy, zz-zdiff).tex(0f, 1f).endVertex();
		buff.pos(xx-xdiff, yy, zz+zdiff).tex(0.125f, 1f).endVertex();
		buff.pos(x-xdiff, y, z+zdiff).tex(0.125f, 0f).endVertex();
	}
}
