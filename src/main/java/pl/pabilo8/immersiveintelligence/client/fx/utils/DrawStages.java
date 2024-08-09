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
 * @author Pabilo8
 * @since 06.04.2024
 */
public enum DrawStages implements ISerializableEnum
{
	/**
	 * Normal particles render just like minecraft's default particles
	 */
	VANILLA(GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP, false, false, ParticleSystem.PARTICLE_TEXTURES),
	/**
	 * Normal particles, but uses additive blending
	 */
	VANILLA_ADDITIVE(GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP, false, false, ParticleSystem.PARTICLE_TEXTURES),

	/**
	 * Tracer particles are rendered on their background using additive blending and no texture
	 */
	COLOR_ONLY_ADDITIVE(DestFactor.ONE, DefaultVertexFormats.POSITION_COLOR, false, false, null),

	/**
	 * Uses the default texture map, use sprites with it
	 */
	CUSTOM(GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP, false, false, TextureMap.LOCATION_BLOCKS_TEXTURE),
	/**
	 * Same as CUSTOM, but uses additive blending
	 */
	CUSTOM_ADDITIVE(DestFactor.ONE_MINUS_CONSTANT_ALPHA, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP, false, false, TextureMap.LOCATION_BLOCKS_TEXTURE),
	/**
	 * Same as CUSTOM, but applies a noise shader during rendering
	 */
	CUSTOM_SMOKE_NOISE_SHADER(CUSTOM, Shaders.NOISE, partialTicks -> new float[]{partialTicks}),
//	CUSTOM_SMOKE_NOISE_SHADER(CUSTOM, null, partialTicks -> new float[0]),
	/**
	 * Same as CUSTOM, but with normal maps, use with solid 3D models
	 */
	CUSTOM_SOLID(GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, ParticleUtils.PARTICLE_SOLID, false, true, TextureMap.LOCATION_BLOCKS_TEXTURE);

	final DestFactor destFactor;
	final VertexFormat vertexFormat;
	public final boolean renderThroughBlocks, applyLighting;
	final ResourceLocation textureRes;
	final Shaders shader;
	final Function<Float, float[]> shaderParameters;
	public final boolean requiresNormals;

	DrawStages(DestFactor destFactor, VertexFormat vertexFormat, boolean renderThroughBlocks, boolean applyLighting, @Nullable ResourceLocation textureRes)
	{
		this.destFactor = destFactor;
		this.vertexFormat = vertexFormat;
		this.renderThroughBlocks = renderThroughBlocks;
		this.applyLighting = applyLighting;
		this.textureRes = textureRes;
		this.shader = null;
		this.shaderParameters = partialTicks -> new float[0];
		this.requiresNormals = vertexFormat.getElements().stream()
				.anyMatch(element -> element.getUsage()==EnumUsage.NORMAL);
	}

	/**
	 * Copy constructor adding a shader
	 *
	 * @param other            DrawStages to copy
	 * @param shader           shader to use
	 * @param shaderParameters parameters for the shader
	 */
	DrawStages(DrawStages other, Shaders shader, Function<Float, float[]> shaderParameters)
	{
		this.destFactor = other.destFactor;
		this.vertexFormat = other.vertexFormat;
		this.renderThroughBlocks = other.renderThroughBlocks;
		this.applyLighting = other.applyLighting;
		this.textureRes = other.textureRes;
		this.requiresNormals = other.requiresNormals;
		this.shader = shader;
		this.shaderParameters = shaderParameters;
	}

	public void prepareRender(BufferBuilder buffer, float partialTicks)
	{
		if(textureRes!=null)
			Minecraft.getMinecraft().renderEngine.bindTexture(textureRes);
		else
			GlStateManager.disableTexture2D();

		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, destFactor);
		if(this.renderThroughBlocks)
			GlStateManager.disableDepth();
		if(shader!=null)
			ShaderUtil.useShader(shader, shaderParameters.apply(partialTicks));
		if(applyLighting)
		{
			GlStateManager.enableLighting();

		}

		buffer.begin(GL11.GL_QUADS, vertexFormat);
	}

	public void clear()
	{
		if(this.renderThroughBlocks)
			GlStateManager.enableDepth();
		if(textureRes==null)
			GlStateManager.enableTexture2D();
		if(shader!=null)
			ShaderUtil.releaseShader();
		if(applyLighting)
			GlStateManager.disableLighting();

		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
	}

}
