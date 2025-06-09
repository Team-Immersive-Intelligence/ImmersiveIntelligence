package pl.pabilo8.immersiveintelligence.client.fx.particles;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.factories.ParticleModelFactory.ParticleModel;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 07.04.2024
 */
public abstract class ParticleAbstractModel extends AbstractParticle
{
	protected float size, scale;
	int textureShift = 0;
	protected IIColor color = IIColor.WHITE;

	public ParticleAbstractModel(World world, Vec3d pos)
	{
		super(world, pos);
	}

	@Nonnull
	@Override
	public Object getProperty(ParticleProperties key)
	{
		switch(key)
		{
			case SIZE:
				return size;
			case SCALE:
				return scale;
			case COLOR:
				return color;
			case RED:
				return color.red;
			case GREEN:
				return color.green;
			case BLUE:
				return color.blue;
			case ALPHA:
				return color.alpha;
			case TEXTURE_SHIFT:
				return textureShift;
			default:
				return super.getProperty(key);
		}
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
			default:
				super.setProperty(key, value);
		}
	}

	//--- Model ---//

	/**
	 * Sets the 3D model of the particle
	 */
	public abstract void setModel(ParticleModel particleModel);

	/**
	 * Sets the texture of the particle
	 */
	public abstract void retexture(int textureID, ResLoc textureLocation);

	/**
	 * Sets the texture of the particle to one used by another particle
	 */
	public abstract <T extends ParticleAbstractModel> void retextureModel(T otherParticle);
}
