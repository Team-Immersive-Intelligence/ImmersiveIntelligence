package pl.pabilo8.immersiveintelligence.client.fx.particles;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.factories.ParticleModelFactory;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 12.04.2024
 */
public class ParticleAMTModel extends ParticleAbstractModel
{
	@Nullable
	private ParticleModelFactory.ParticleModel model = null;
	private TextureAtlasSprite[] textureSprites = new TextureAtlasSprite[0];
	private ResourceLocation[] textures = new ResourceLocation[0];

	public ParticleAMTModel(World world, Vec3d pos)
	{
		super(world, pos);
	}

	@Nonnull
	@Override
	public Object getProperty(ParticleProperties key)
	{
		return switch(key)
		{
			case TEXTURES -> textures;
			case TEXTURES_COUNT -> textures.length;
			default -> super.getProperty(key);
		};
	}

	@Override
	public void setProperty(ParticleProperties key, Object value)
	{
		switch(key)
		{
			case TEXTURES ->
			{
				this.textures = (ResourceLocation[])value;
				this.textureSprites = Arrays.stream(this.textures)
						.map(ClientUtils::getSprite)
						.toArray(TextureAtlasSprite[]::new);
			}
			case TEXTURES_COUNT -> {}
			default -> super.setProperty(key, value);
		}
	}

	@Override
	public void preRender(float partialTicks, float x, float xz, float z, float yz, float xy)
	{
		if(model==null)
			return;
		super.preRender(partialTicks, x, xz, z, yz, xy);
		matrix.scale(size*scale*stretch.x, size*scale*stretch.y, size*scale*stretch.z);
	}

	@Override
	public void setModel(@Nullable ParticleModelFactory.ParticleModel particleModel)
	{
		this.model = particleModel;
		if(model!=null)
		{
			this.textures = Arrays.copyOf(particleModel.textures, particleModel.textures.length);
			this.textureSprites = Arrays.copyOf(particleModel.textureSprites, particleModel.textureSprites.length);
		}
	}

	@Override
	public void retexture(int textureID, ResLoc textureLocation)
	{
		if(textureID >= 0&&textureID < textureSprites.length)
		{
			textures[textureID] = textureLocation;
			textureSprites[textureID] = ClientUtils.getSprite(textureLocation);
		}
	}

	@Override
	public <T extends ParticleAbstractModel> void retextureModel(T otherParticle)
	{
		if(otherParticle instanceof ParticleAMTModel)
		{
			this.textures = ((ParticleAMTModel)otherParticle).textures;
			this.textureSprites = ((ParticleAMTModel)otherParticle).textureSprites;
		}
	}

	@Override
	public void render(BufferBuilder buffer, float partialTicks, float x, float xz, float z, float yz, float xy)
	{
		if(model==null)
			return;

		boolean normals = getDrawStage().requiresNormals;
		int lightMapX = 16, lightMapY = 16;
		if(drawStage.applyLighting)
		{
			int i = this.getBrightnessForRender();
			lightMapX = i>>16&65535;
			lightMapY = i&65535;
		}

		for(int i = 0; i < model.elementsCount; i++)
		{
			Vec3d pos = matrix.apply(model.positions[i]);
			Vec2f uv = model.uv[i];
			TextureAtlasSprite texture = textureSprites[(model.tex[i]+textureShift)%textureSprites.length];

			buffer.pos(pos.x, pos.y, pos.z)
					.tex(texture.getInterpolatedU(uv.x*16), texture.getInterpolatedV(16-uv.y*16))
					.color(color.red, color.green, color.blue, color.alpha)
					.lightmap(lightMapX, lightMapY);
			if(normals)
			{
				Vec3d normal = model.normals[i];//mat.apply(model.normals[i]);
				buffer.normal((float)normal.x, (float)normal.y, (float)normal.z);
			}
			buffer.endVertex();
		}
	}

	private int getBrightnessForRender()
	{
		BlockPos blockPos = new BlockPos(this.pos.x, this.pos.y, this.pos.z);
		return this.world.isBlockLoaded(blockPos)?this.world.getCombinedLight(blockPos, 0): 0;
	}
}
