package pl.pabilo8.immersiveintelligence.client.util.amt;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticle;
import pl.pabilo8.immersiveintelligence.client.fx.particles.ParticleGunfire;

import java.util.function.Consumer;

/**
 * AMT type for drawing particle effects (gunshots, smoke, etc.)
 *
 * @author Pabilo8
 * @since 21.08.2022
 */
public class AMTParticle extends AMT
{
	private IIParticle particle;

	public AMTParticle(String name, Vec3d originPos)
	{
		super(name, originPos);
	}

	public AMTParticle(String name, IIModelHeader header)
	{
		this(name, header.getOffset(name));
	}

	public AMTParticle setParticle(IIParticle particle)
	{
		return setParticle(particle, part -> {
		});
	}

	public AMTParticle setParticle(IIParticle particle, Consumer<IIParticle> config)
	{
		this.particle = particle;
		config.accept(particle);
		return this;
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		if(particle==null||particle.getAge()==0)
			return;

		if(particle instanceof ParticleGunfire)
		{
			drawGunshot(buf);
			return;
		}

		//Uses the custom property value for age
		particle.setAge((int)(property*particle.getMaxAge()));
		particle.setWorld(ClientUtils.mc().world);

		GlStateManager.translate(originPos.x, originPos.y-0.5, originPos.z);
		GlStateManager.rotate(90, 0, 1, 0);

		//Set up BufferBuilder with the particle stage
		particle.getDrawStage().prepareRender(buf);

		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
		GlStateManager.disableCull();

		//Render
		particle.renderParticle(buf, ClientUtils.mc().player,
				ClientUtils.mc().getRenderPartialTicks(),
				ActiveRenderInfo.getRotationX(),
				ActiveRenderInfo.getRotationXZ(),
				ActiveRenderInfo.getRotationZ(),
				ActiveRenderInfo.getRotationYZ(),
				ActiveRenderInfo.getRotationXY()
		);
		tes.draw();
		particle.getDrawStage().clear();
	}

	/**
	 * A very temporary solution, pls don't howitzize
	 */
	private void drawGunshot(BufferBuilder buf)
	{
		GlStateManager.translate(originPos.x, originPos.y-0.125, originPos.z);
		GlStateManager.rotate(90, 0, 1, 0);
		GlStateManager.disableCull();

		TextureAtlasSprite tex = ParticleGunfire.TEXTURES[(int)(property*7)];
		float u = tex.getMinU(), v = tex.getInterpolatedV(8), uu = tex.getInterpolatedU(8), vv = tex.getMaxV();
		float scale = 1.5f;

		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_CONSTANT_ALPHA);
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.disableLighting();

		GlStateManager.scale(scale, scale, scale);

		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		IIClientUtils.drawFace(buf, 0.385, -0.5, -0.5+0.03125, 0.385, 0.5, 0.5+0.03125,
				u, uu, v, vv);
		u = tex.getInterpolatedU(8);
		uu = tex.getMaxU();
		IIClientUtils.drawFace(buf, 0.125, -0.5, -0.5, 0.125, 0.5, 0.5,
				u, uu, v, vv);

		Tessellator.getInstance().draw();

		//set texture to the "beam" part
		u = tex.getMinU();
		uu = tex.getMaxU();
		v = tex.getMinV();
		vv = tex.getInterpolatedV(8);

		GlStateManager.translate(0, 0, 0.03125);
		for(int i = 0; i < 4; i++)
		{
			buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			IIClientUtils.drawFace(buf, 0, -0.5, 0, 2, 0.5, 0,
					u, uu, v, vv);
			Tessellator.getInstance().draw();
			GlStateManager.rotate(45, 1, 0, 0);
		}

		GlStateManager.enableCull();
		GlStateManager.depthMask(true);
		GlStateManager.enableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
	}

	@Override
	public void disposeOf()
	{

	}
}
