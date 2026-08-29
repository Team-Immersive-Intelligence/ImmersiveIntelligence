package pl.pabilo8.immersiveintelligence.client.fx.particles;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.factories.ParticleFactory;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 13.05.2024
 */
public class ParticleVanilla extends AbstractParticle
{
	private IIColor color = IIColor.WHITE;
	private float size = 1, scale = 1;
	/**
	 * Texture shift amount
	 */
	private int textureShift;
	/**
	 * Texture array
	 */
	private TextureAtlasSprite[] textureSprites = new TextureAtlasSprite[0];
	private ResourceLocation[] textures = new ResourceLocation[0];

	/**
	 * Extending constructors should be passed as a parameter to {@link ParticleFactory}
	 *
	 * @param world The world
	 * @param pos   The position
	 */
	public ParticleVanilla(World world, Vec3d pos)
	{
		super(world, pos);
	}

	//--- Properties ---//

	@Nonnull
	@Override
	public Object getProperty(ParticleProperties key)
	{
		return switch(key)
		{
			case COLOR -> color;
			case RED -> color.red;
			case GREEN -> color.green;
			case BLUE -> color.blue;
			case ALPHA -> color.alpha;
			case SIZE -> size;
			case SCALE -> scale;
			case TEXTURE_SHIFT -> textureShift;
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
			case SIZE:
				size = (float)value;
				break;
			case SCALE:
				scale = (float)value;
				break;

			case COLOR:
				color = (IIColor)value;
				break;
			case RED:
				color = color.withRed((float)value);
				break;
			case GREEN:
				color = color.withGreen((float)value);
				break;
			case BLUE:
				color = color.withBlue((float)value);
				break;
			case ALPHA:
				color = color.withAlpha((float)value);
				break;

			case TEXTURE_SHIFT:
				textureShift = (int)value;
				break;
			case TEXTURES:
				setTextureSprites((ResourceLocation[])value);
				break;
			case TEXTURES_COUNT:
				break; //do nothing
			default:
				super.setProperty(key, value);
				break;
		}
	}


	//--- Rendering ---//

	@Override
	public void preRender(float partialTicks, float x, float xz, float z, float yz, float xy)
	{
		programs.forEach(p -> p.onParticleRender(this, partialTicks));

		this.matrix.setIdentity();
		this.matrix.translate(
				pos.x+motion.x*interpTicks-interPos.x,
				pos.y+motion.y*interpTicks-interPos.y,
				pos.z+motion.z*interpTicks-interPos.z
		);
		//TODO: 23.12.2024 use matrix instead of calculations in render


	}

	@Override
	public void render(BufferBuilder buffer, float partialTicks, float x, float xz, float z, float yz, float xy)
	{
		double fSize = this.size*this.scale;

		//Get brightness
		int i = this.getBrightnessForRender();
		int j = i>>16&65535;
		int k = i&65535;

		//Calculate the position camera transform, so it's relative to the viewpoint
		Vec3d[] avec3d = new Vec3d[]{
				new Vec3d(-x*fSize-yz*fSize, -xz*fSize, -z*fSize-xy*fSize),
				new Vec3d(-x*fSize+yz*fSize, xz*fSize, -z*fSize+xy*fSize),
				new Vec3d(x*fSize+yz*fSize, xz*fSize, z*fSize+xy*fSize),
				new Vec3d(x*fSize-yz*fSize, -xz*fSize, z*fSize-xy*fSize)
		};


		//Get UV values
		TextureAtlasSprite texture = textureSprites[textureShift%textureSprites.length];
		float u = texture.getMinU();
		float v = texture.getMinV();
		float uu = texture.getMaxU();
		float vv = texture.getMaxV();


		float posX = (float)(pos.x+motion.x*interpTicks-interPos.x);
		float posY = (float)(pos.y+motion.y*interpTicks-interPos.y);
		float posZ = (float)(pos.z+motion.z*interpTicks-interPos.z);

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


	//--- Utils ---//

	public void setTextureSprites(ResourceLocation[] textureSprites)
	{
		this.textures = textureSprites;
		this.textureSprites = new TextureAtlasSprite[textureSprites.length];
		for(int i = 0; i < textureSprites.length; i++)
			this.textureSprites[i] = ClientUtils.getSprite(textureSprites[i]);
	}

	private int getBrightnessForRender()
	{
		BlockPos blockPos = new BlockPos(this.pos.x, this.pos.y, this.pos.z);
		return this.world.isBlockLoaded(blockPos)?this.world.getCombinedLight(blockPos, 0): 0;
	}
}
