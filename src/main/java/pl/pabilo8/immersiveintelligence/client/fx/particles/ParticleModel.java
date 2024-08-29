package pl.pabilo8.immersiveintelligence.client.fx.particles;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.builder.ParticleModelBuilder;
import pl.pabilo8.immersiveintelligence.client.fx.prefab.IProgrammableParticle;
import pl.pabilo8.immersiveintelligence.client.fx.prefab.ParticleAbstractModel;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.BiConsumer;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 12.04.2024
 */
public class ParticleModel extends ParticleAbstractModel implements IProgrammableParticle<ParticleModel>
{
	/**
	 * Programmable animation for the particle
	 */
	private BiConsumer<ParticleModel, Float> program;

	@Nullable
	ParticleModelBuilder.ParticleModel model = null;
	TextureAtlasSprite[] textures = new TextureAtlasSprite[0];
	int textureShift = 0;
	//for rotation transforms
	Matrix4 mat = new Matrix4();

	public ParticleModel(World world, Vec3d pos)
	{
		super(world, pos);
	}

	@Override
	public void setModel(@Nullable ParticleModelBuilder.ParticleModel particleModel)
	{
		this.model = particleModel;
		if(model!=null)
			setTextures(Arrays.copyOf(particleModel.textures, particleModel.textures.length));
	}

	@Override
	public void setTextures(TextureAtlasSprite[] textures)
	{
		this.textures = textures;
	}

	@Override
	public void setTextureShift(int textureShift)
	{
		this.textureShift = textureShift;
	}

	@Override
	public float getTexturesCount()
	{
		return textures.length;
	}

	@Override
	public void retexture(int textureID, ResLoc textureLocation)
	{
		if(textureID >= 0&&textureID < textures.length)
			textures[textureID] = ClientUtils.getSprite(textureLocation);
	}

	@Override
	public <T extends ParticleAbstractModel> void retextureModel(T otherParticle)
	{
		if(otherParticle instanceof ParticleModel)
			this.textures = ((ParticleModel)otherParticle).textures;
	}

	@Override
	protected void notifyUpdateRotation()
	{
		this.mat = new Matrix4()
				.setIdentity()
				.rotate(-Math.atan2(rotationX, rotationZ), 0, 1, 0)
				.rotate(Math.asin(rotationY), 1, 0, 0);
	}

	@Override
	public void render(BufferBuilder buffer, float partialTicks, float x, float xz, float z, float yz, float xy)
	{
		if(model==null)
			return;

		if(program!=null)
			program.accept(this, partialTicks);

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

			Vec3d pos = getRenderPosition().add(mat.apply(getScaledVector(model.positions[i]))); //
			Vec2f uv = model.uv[i];
			TextureAtlasSprite texture = textures[(model.tex[i]+textureShift)%textures.length];

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
		BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
		return this.world.isBlockLoaded(blockpos)?this.world.getCombinedLight(blockpos, 0): 0;
	}

	@Override
	public void setProgram(BiConsumer<ParticleModel, Float> program)
	{
		this.program = this.program==null?program: this.program.andThen(program);
	}

	private Vec3d getScaledVector(Vec3d vec)
	{
		return new Vec3d(vec.x*scale.x, vec.y*scale.y, vec.z*scale.z);
	}
}
