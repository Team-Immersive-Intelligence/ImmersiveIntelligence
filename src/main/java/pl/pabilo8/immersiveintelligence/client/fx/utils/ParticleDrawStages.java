package pl.pabilo8.immersiveintelligence.client.fx.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil.Shaders;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 06.04.2024
 */
public enum ParticleDrawStages implements ISerializableEnum
{
	/**
	 * Normal particles render just like minecraft's default particles
	 */
	VANILLA(GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP,
			false, false, ParticleSystem.PARTICLE_TEXTURES),
	/**
	 * Normal particles, but uses additive blending
	 */
	VANILLA_ADDITIVE(GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP,
			false, false, ParticleSystem.PARTICLE_TEXTURES),

	/**
	 * Tracer particles are rendered on their background using additive blending and no texture
	 */
	COLOR_ONLY_ADDITIVE(DestFactor.ONE, DefaultVertexFormats.POSITION_COLOR, false, false, null),

	/**
	 * Uses the default texture map, use sprites with it
	 */
	CUSTOM(GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP,
			false, false, TextureMap.LOCATION_BLOCKS_TEXTURE),
	/**
	 * Same as CUSTOM, but uses additive blending
	 */
	CUSTOM_ADDITIVE(DestFactor.ONE_MINUS_CONSTANT_ALPHA, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP,
			false, false, TextureMap.LOCATION_BLOCKS_TEXTURE),
	/**
	 * Same as CUSTOM, but applies a noise shader during rendering
	 */
	CUSTOM_SMOKE_NOISE_SHADER(GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP,
			false, false, TextureMap.LOCATION_BLOCKS_TEXTURE,
			Shaders.NOISE_NO_LIGHTMAP, partialTicks -> new float[]{partialTicks}
	),
//	CUSTOM_SMOKE_NOISE_SHADER(CUSTOM, null, partialTicks -> new float[0]),
	/**
	 * Same as CUSTOM, but with normal maps, use with solid 3D models
	 */
	CUSTOM_SOLID(GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, IIParticleUtils.PARTICLE_SOLID,
			false, true, TextureMap.LOCATION_BLOCKS_TEXTURE),
	/**
	 * Color-only triangles with additive blending.
	 */
	COLOR_ONLY_ADDITIVE_TRIANGLES(DestFactor.ONE, DefaultVertexFormats.POSITION_COLOR,
			false, false, null, GL11.GL_TRIANGLES, true),
	/**
	 * Wide color-only line strips with additive blending.
	 */
	COLOR_ONLY_LINE_STRIP(DestFactor.ONE, DefaultVertexFormats.POSITION_COLOR,
			false, false, null, GL11.GL_LINE_STRIP, false, 4f),
	/**
	 * Thin color-only line strips with additive blending.
	 */
	COLOR_ONLY_LINE_STRIP_THIN(DestFactor.ONE, DefaultVertexFormats.POSITION_COLOR,
			false, false, null, GL11.GL_LINE_STRIP, false, 1f);

	public final boolean renderThroughBlocks, applyLighting;
	public final boolean requiresNormals;
	final DestFactor destFactor;
	final VertexFormat vertexFormat;
	final ResourceLocation textureRes;
	final int drawMode;
	final boolean smoothShading;
	final float lineWidth;
	final Shaders shader;
	final Function<Float, float[]> shaderParameters;

	ParticleDrawStages(DestFactor destFactor, VertexFormat vertexFormat, boolean renderThroughBlocks, boolean applyLighting, @Nullable ResourceLocation textureRes)
	{
		this(destFactor, vertexFormat, renderThroughBlocks, applyLighting, textureRes, GL11.GL_QUADS, false, 1f);
	}

	ParticleDrawStages(DestFactor destFactor, VertexFormat vertexFormat, boolean renderThroughBlocks, boolean applyLighting,
	                   @Nullable ResourceLocation textureRes, int drawMode, boolean smoothShading)
	{
		this(destFactor, vertexFormat, renderThroughBlocks, applyLighting, textureRes, drawMode, smoothShading, 1f);
	}

	ParticleDrawStages(DestFactor destFactor, VertexFormat vertexFormat, boolean renderThroughBlocks, boolean applyLighting,
	                   @Nullable ResourceLocation textureRes, int drawMode, boolean smoothShading, float lineWidth)
	{
		this.destFactor = destFactor;
		this.vertexFormat = vertexFormat;
		this.renderThroughBlocks = renderThroughBlocks;
		this.applyLighting = applyLighting;
		this.textureRes = textureRes;
		this.drawMode = drawMode;
		this.smoothShading = smoothShading;
		this.lineWidth = lineWidth;
		this.shader = null;
		this.shaderParameters = partialTicks -> new float[0];
		this.requiresNormals = vertexFormat.getElements().stream()
				.anyMatch(element -> element.getUsage()==EnumUsage.NORMAL);
	}

	ParticleDrawStages(DestFactor destFactor, VertexFormat vertexFormat, boolean renderThroughBlocks, boolean applyLighting, @Nullable ResourceLocation textureRes,
	                   Shaders shader, Function<Float, float[]> shaderParameters)
	{
		this.destFactor = destFactor;
		this.vertexFormat = vertexFormat;
		this.renderThroughBlocks = renderThroughBlocks;
		this.applyLighting = applyLighting;
		this.textureRes = textureRes;
		this.drawMode = GL11.GL_QUADS;
		this.smoothShading = false;
		this.lineWidth = 1f;
		this.requiresNormals = vertexFormat.getElements().stream()
				.anyMatch(element -> element.getUsage()==EnumUsage.NORMAL);
		this.shader = shader;
		this.shaderParameters = shaderParameters;
	}

	public void prepareRender(BufferBuilder buffer, float partialTicks)
	{
		if(textureRes!=null)
			Minecraft.getMinecraft().renderEngine.bindTexture(textureRes);
		else
			GlStateManager.disableTexture2D();

		if(applyLighting)
			GlStateManager.enableLighting();

		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, destFactor);
		if(this.renderThroughBlocks)
			GlStateManager.disableDepth();
		else
			GlStateManager.enableDepth();

		if(shader!=null)
			ShaderUtil.useShader(shader, shaderParameters.apply(partialTicks));

		if(smoothShading)
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
		if(lineWidth!=1f)
			GL11.glLineWidth(lineWidth);
		buffer.begin(drawMode, vertexFormat);
	}

	public void clear()
	{
		if(this.renderThroughBlocks)
			GlStateManager.enableDepth();
		if(textureRes==null)
			GlStateManager.enableTexture2D();
		if(shader!=null)
			ShaderUtil.releaseShader();
		if(smoothShading)
			GlStateManager.shadeModel(GL11.GL_FLAT);
		if(lineWidth!=1f)
			GL11.glLineWidth(1f);
		if(applyLighting)
			GlStateManager.disableLighting();

		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
	}

}
