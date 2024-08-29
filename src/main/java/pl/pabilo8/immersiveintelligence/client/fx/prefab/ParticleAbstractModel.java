package pl.pabilo8.immersiveintelligence.client.fx.prefab;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.builder.ParticleModelBuilder.ParticleModel;
import pl.pabilo8.immersiveintelligence.client.fx.particles.IIParticle;
import pl.pabilo8.immersiveintelligence.client.fx.utils.IIParticleProperties;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 07.04.2024
 */
public abstract class ParticleAbstractModel extends IIParticle implements ITexturedParticle, IScalableParticle
{
	protected double size;
	protected Vec3d scale = IIUtils.ONE;
	protected IIColor color = IIColor.WHITE;

	public ParticleAbstractModel(World world, Vec3d pos)
	{
		super(world, pos);
	}

	//--- NBT Overrides ---//

	@Override
	public void setProperties(EasyNBT properties)
	{
		super.setProperties(properties);
		properties.checkSetDouble(IIParticleProperties.SIZE, this::setSize);
		properties.checkSetColor(IIParticleProperties.COLOR, this::setColor);
		properties.checkSetVec3D(IIParticleProperties.SCALE, this::setScale);
	}

	@Override
	public EasyNBT getProperties(EasyNBT eNBT)
	{
		return super.getProperties(eNBT)
				.withDouble(IIParticleProperties.SIZE, size)
				.withColor(IIParticleProperties.COLOR, getColor())
				.withVec3d(IIParticleProperties.SCALE, getScale());
	}


	//--- Display Scale ---//

	@Override
	public Vec3d getScale()
	{
		return scale;
	}

	@Override
	public void setScale(Vec3d scale)
	{
		this.scale = scale;
	}

	//--- Size ---//

	@Override
	public double getSize()
	{
		return size;
	}

	@Override
	public void setSize(double size)
	{
		this.size = size;
	}

	//--- Color ---//

	/**
	 * Sets the color of the particle
	 */
	public void setColor(IIColor color)
	{
		this.color = color;
	}

	/**
	 * @return color of this particle
	 */
	public IIColor getColor()
	{
		return color;
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
