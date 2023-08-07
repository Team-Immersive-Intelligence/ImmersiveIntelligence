package pl.pabilo8.immersiveintelligence.client.fx;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author Pabilo8
 * @author EpicSquid
 * <p>
 * Drawing Stages partially stolen from LadyLib's Particles
 * @since 23.12.2020
 * <p>
 * Based on Embers' Particle Renderer
 */
public class ParticleRenderer
{
	public static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation("textures/particle/particles.png");

	private final Map<DrawingStages, Queue<IIParticle>> particles = new HashMap<>();
	private static final int MAX_PARTICLES = 2000;
	private static final int MAX_DRAWN_PARTICLES = 1000;

	public void updateParticles()
	{
		if(ClientUtils.mc().world==null)
		{
			particles.clear();
			return;
		}

		int count = 0;
		// go through every particle indiscriminately
		for(Queue<IIParticle> particleQueue : particles.values())
		{
			for(Iterator<IIParticle> iterator = particleQueue.iterator(); iterator.hasNext(); )
			{
				// particles cost a lot less to update than to render so we can update more of them
				if(++count > MAX_PARTICLES)
					break;
				IIParticle particle = iterator.next();
				particle.onUpdate();
				if(!particle.isAlive())
					iterator.remove();
			}
		}
	}

	public void renderParticles(float partialTicks)
	{

		float x = ActiveRenderInfo.getRotationX();
		float z = ActiveRenderInfo.getRotationZ();
		float yz = ActiveRenderInfo.getRotationYZ();
		float xy = ActiveRenderInfo.getRotationXY();
		float xz = ActiveRenderInfo.getRotationXZ();
		EntityPlayer player = Minecraft.getMinecraft().player;

		if(player!=null)
		{
			updateParticleFields(partialTicks, player);

			GlStateManager.pushMatrix();

			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
			GlStateManager.disableCull();

			GlStateManager.depthMask(false);

			Tessellator tess = Tessellator.getInstance();
			BufferBuilder buffer = tess.getBuffer();

			drawParticles:
			for(Map.Entry<DrawingStages, Queue<IIParticle>> particleStage : particles.entrySet())
			{
				int particleCount = 0;
				particleStage.getKey().prepareRender(buffer);
				for(IIParticle particle : particleStage.getValue())
				{
					if(++particleCount > MAX_DRAWN_PARTICLES)
					{
						tess.draw();
						particleStage.getKey().clear();
						break drawParticles;
					}
					particle.renderParticle(buffer, player, partialTicks, x, xz, z, yz, xy);
				}
				tess.draw();
				particleStage.getKey().clear();
			}

			GlStateManager.enableCull();
			GlStateManager.depthMask(true);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableBlend();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
			GlStateManager.popMatrix();
		}

	}

	private static void updateParticleFields(float partialTicks, EntityPlayer player)
	{
		Particle.interpPosX = player.lastTickPosX+(player.posX-player.lastTickPosX)*partialTicks;
		Particle.interpPosY = player.lastTickPosY+(player.posY-player.lastTickPosY)*partialTicks;
		Particle.interpPosZ = player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*partialTicks;
		Particle.cameraViewDir = player.getLook(partialTicks);
	}

	public void addEffect(IIParticle p)
	{
		if(particles.values().stream().mapToInt(Collection::size).sum() < MAX_PARTICLES)
		{
			particles.computeIfAbsent(p.getDrawStage(), i -> new ArrayDeque<>()).add(p);
		}
	}

	public void reload()
	{
		particles.clear();
	}

	public enum DrawingStages
	{
		//Normal particles render just like minecraft's default particles
		NORMAL(GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP, false, PARTICLE_TEXTURES),
		//Uses the default texture map, use sprites with it
		CUSTOM(GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP, false, TextureMap.LOCATION_BLOCKS_TEXTURE),
		//Same as CUSTOM, but uses additive blending
		CUSTOM_ADDITIVE(DestFactor.ONE_MINUS_CONSTANT_ALPHA, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP, false, TextureMap.LOCATION_BLOCKS_TEXTURE),
		//Tracer particles are rendered on their background using additive blending and no texture
		TRACER(GlStateManager.DestFactor.ONE, DefaultVertexFormats.POSITION_COLOR, false, null);

		final GlStateManager.DestFactor destFactor;
		final VertexFormat vertexFormat;
		final boolean renderThroughBlocks;
		final ResourceLocation textureRes;

		DrawingStages(DestFactor destFactor, VertexFormat vertexFormat, boolean renderThroughBlocks, @Nullable ResourceLocation textureRes)
		{
			this.destFactor = destFactor;
			this.vertexFormat = vertexFormat;
			this.renderThroughBlocks = renderThroughBlocks;
			this.textureRes = textureRes;
		}

		public void prepareRender(BufferBuilder buffer)
		{
			if(textureRes!=null)
				Minecraft.getMinecraft().renderEngine.bindTexture(textureRes);
			else
				GlStateManager.disableTexture2D();

			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, destFactor);
			if(this.renderThroughBlocks)
				GlStateManager.disableDepth();

			buffer.begin(GL11.GL_QUADS, vertexFormat);
		}

		public void clear()
		{
			if(this.renderThroughBlocks)
				GlStateManager.enableDepth();
			if(textureRes==null)
				GlStateManager.enableTexture2D();
		}

	}
}
