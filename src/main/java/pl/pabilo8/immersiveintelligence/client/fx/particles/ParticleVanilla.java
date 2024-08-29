package pl.pabilo8.immersiveintelligence.client.fx.particles;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.builder.ParticleBuilder;
import pl.pabilo8.immersiveintelligence.client.fx.prefab.IProgrammableParticle;
import pl.pabilo8.immersiveintelligence.client.fx.prefab.IScalableParticle;
import pl.pabilo8.immersiveintelligence.client.fx.prefab.ITexturedParticle;
import pl.pabilo8.immersiveintelligence.client.fx.utils.IIParticleProperties;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.function.BiConsumer;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 13.05.2024
 */
public class ParticleVanilla extends IIParticle implements IProgrammableParticle<ParticleVanilla>, ITexturedParticle, IScalableParticle
{
	private IIColor color = IIColor.WHITE;
	private double size = 1, scale = 1;
	/**
	 * Programmable animation for the particle
	 */
	private BiConsumer<ParticleVanilla, Float> program = null;
	/**
	 * Texture shift amount
	 */
	private int textureShift;
	/**
	 * Texture array
	 */
	private TextureAtlasSprite[] textures = new TextureAtlasSprite[0];

	/**
	 * Extending constructors should be passed as a parameter to {@link ParticleBuilder}
	 *
	 * @param world
	 * @param pos
	 */
	public ParticleVanilla(World world, Vec3d pos)
	{
		super(world, pos);
	}

	//--- Properties ---//

	@Override
	public void setProperties(EasyNBT properties)
	{
		super.setProperties(properties);
		properties.checkSetDouble(IIParticleProperties.SIZE, s -> size = s);
		properties.checkSetDouble(IIParticleProperties.SCALE, s -> scale = s);
		properties.checkSetInt(IIParticleProperties.TEXTURE_SHIFT, s -> textureShift = s);
		properties.checkSetColor(IIParticleProperties.COLOR, c -> color = c);
	}

	@Override
	public EasyNBT getProperties(EasyNBT eNBT)
	{
		return super.getProperties(eNBT)
				.withDouble(IIParticleProperties.SIZE, size)
				.withDouble(IIParticleProperties.SCALE, scale)
				.withInt(IIParticleProperties.TEXTURE_SHIFT, textureShift)
				.withColor(IIParticleProperties.COLOR, color);
	}

	//--- Rendering ---//

	@Override
	public void render(BufferBuilder buffer, float partialTicks, float x, float xz, float z, float yz, float xy)
	{
		//Execute program
		if(program!=null)
			program.accept(this, partialTicks);

		//Get scale for position camera transform
		double fSize = this.size*this.scale;

		//Get brightness
		int i = this.getBrightnessForRender();
		int j = i>>16&65535;
		int k = i&65535;

		//Calculate the position camera transform, so it's relative to the viewpoint
		Vec3d[] avec3d = new Vec3d[]{
				new Vec3d(-x*fSize-xy*fSize, -z*fSize, -yz*fSize-xz*fSize),
				new Vec3d(-x*fSize+xy*fSize, z*fSize, -yz*fSize+xz*fSize),
				new Vec3d(x*fSize+xy*fSize, z*fSize, yz*fSize+xz*fSize),
				new Vec3d(x*fSize-xy*fSize, -z*fSize, yz*fSize-xz*fSize)
		};

		//Get UV values
		TextureAtlasSprite texture = textures[textureShift%textures.length];
		float u = texture.getMinU();
		float v = texture.getMinV();
		float uu = texture.getMaxU();
		float vv = texture.getMaxV();

		//Put into buffer
		buffer.pos(posX+avec3d[0].x, posY+avec3d[0].y, posZ+avec3d[0].z)
				.tex(uu, vv)
				.color(color.red, color.green, color.blue, color.alpha)
				.lightmap(j, k).endVertex();
		buffer.pos(posX+avec3d[1].x, posY+avec3d[1].y, posZ+avec3d[1].z)
				.tex(uu, v)
				.color(color.red, color.green, color.blue, color.alpha)
				.lightmap(j, k).endVertex();
		buffer.pos(posX+avec3d[2].x, posY+avec3d[2].y, posZ+avec3d[2].z)
				.tex(u, v)
				.color(color.red, color.green, color.blue, color.alpha)
				.lightmap(j, k).endVertex();
		buffer.pos(posX+avec3d[3].x, posY+avec3d[3].y, posZ+avec3d[3].z)
				.tex(u, vv)
				.color(color.red, color.green, color.blue, color.alpha)
				.lightmap(j, k).endVertex();
	}

	//--- ITexturedParticle ---//

	@Override
	public void retexture(int textureID, ResLoc textureLocation)
	{
		if(textures.length > textureID)
			textures[textureID] = ClientUtils.getSprite(textureLocation);
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
	public void setColor(IIColor color)
	{
		this.color = color;
	}

	@Override
	public IIColor getColor()
	{
		return color;
	}

	//--- IResizableParticle ---//

	@Override
	public void setSize(double size)
	{
		this.size = size;
	}

	@Override
	public double getSize()
	{
		return size;
	}

	@Override
	public void setProgram(BiConsumer<ParticleVanilla, Float> program)
	{
		this.program = program;
	}

	//--- Utils ---//

	private int getBrightnessForRender()
	{
		BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
		return this.world.isBlockLoaded(blockpos)?this.world.getCombinedLight(blockpos, 0): 0;
	}

	@Override
	public Vec3d getScale()
	{
		return new Vec3d(scale, scale, scale);
	}

	@Override
	public void setScale(Vec3d scale)
	{
		this.scale = scale.x*scale.y*scale.z;
	}
}
