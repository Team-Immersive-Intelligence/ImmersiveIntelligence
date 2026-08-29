package pl.pabilo8.immersiveintelligence.client.fx.utils;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.client.fx.factories.ParticleFactory;
import pl.pabilo8.immersiveintelligence.client.fx.particles.AbstractParticle;
import pl.pabilo8.immersiveintelligence.client.fx.utils.IIParticleUtils.PositionGenerator;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.util.List;

/**
 * Defines particles spawned from a parent particle.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 28.08.2026
 * @since 22.12.2024
 **/
public class ParticleOffspring<T extends AbstractParticle>
{
	private final String factoryName;
	private final PositionGenerator positionGenerator;
	private final float distance;
	private final float speed;
	private final float stretchLength;
	private final int minAmount;
	private final int randAmount;
	private final List<ParticleProperties> inheritedProperties;
	private boolean initialized = false;
	private ParticleFactory<T> particleFactory;

	/**
	 * Creates an offspring definition with a variable spawn count.
	 */
	public ParticleOffspring(String factoryName, PositionGenerator positionGenerator,
	                         float distance, float speed, float stretchLength,
	                         int minAmount, int maxAmount, List<ParticleProperties> inheritedProperties)
	{
		this.factoryName = factoryName;
		this.positionGenerator = positionGenerator;
		this.distance = distance;
		this.speed = speed;
		this.stretchLength = stretchLength;
		this.minAmount = Math.min(minAmount, maxAmount);
		this.randAmount = Math.max(minAmount, maxAmount)-minAmount;
		this.inheritedProperties = inheritedProperties;
	}

	/**
	 * Creates an offspring definition with a fixed spawn count.
	 */
	public ParticleOffspring(String factoryName, PositionGenerator positionGenerator,
	                         float distance, float speed, float stretchLength,
	                         int amount, List<ParticleProperties> inheritedProperties)
	{
		this(factoryName, positionGenerator, distance, speed, stretchLength, amount, amount, inheritedProperties);
	}

	/**
	 * Creates offspring with legacy parameter defaults.
	 * Speed defaults to distance and stretch defaults to zero.
	 */
	public ParticleOffspring(String factoryName, PositionGenerator positionGenerator,
	                         float distance, int minAmount, int maxAmount, List<ParticleProperties> inheritedProperties)
	{
		this(factoryName, positionGenerator, distance, distance, 0, minAmount, maxAmount, inheritedProperties);
	}

	/**
	 * Creates a fixed amount of offspring with legacy parameter defaults.
	 * Speed defaults to distance and stretch defaults to zero.
	 */
	public ParticleOffspring(String factoryName, PositionGenerator positionGenerator,
	                         float distance, int amount, List<ParticleProperties> inheritedProperties)
	{
		this(factoryName, positionGenerator, distance, distance, 0, amount, amount, inheritedProperties);
	}

	/**
	 * Resolves the particle factory used by this definition.
	 */
	@SuppressWarnings("unchecked")
	public void init()
	{
		this.particleFactory = (ParticleFactory<T>)ParticleRegistry.getParticle(factoryName);
		this.initialized = true;
	}

	/**
	 * Spawns offspring from the supplied parent particle.
	 *
	 * @param originParticle parent particle
	 */
	public <O extends AbstractParticle> void spawn(O originParticle)
	{
		if(!initialized)
			init();

		Vector3f originVector = (Vector3f)originParticle.getProperty(ParticleProperties.POSITION);
		Vec3d originPos = new Vec3d(originVector.x, originVector.y, originVector.z);
		Vector3f originMotion = (Vector3f)originParticle.getProperty(ParticleProperties.MOTION);
		Vec3d direction = new Vec3d(originMotion.x, originMotion.y, originMotion.z).normalize();

		int amount = minAmount+(randAmount > 0?Utils.RAND.nextInt(randAmount): 0);
		for(int i = 0; i < amount; i++)
		{
			Vec3d pos = positionGenerator.generatePosition(originPos, direction, i, distance, amount);
			Vec3d motion = positionGenerator.generateMotion(originPos, direction, pos, i, speed, amount);
			Vector2f rotation = positionGenerator.generateRotation(originPos, direction, i, distance, amount);
			Vec3d stretch = positionGenerator.generateStretch(originPos, direction, pos, motion, stretchLength);

			T spawn = particleFactory.spawn(pos, motion, rotation);
			if(!stretch.equals(Vec3d.ZERO)&&!stretch.equals(pos))
				spawn.setProperty(ParticleProperties.STRETCH, new Vector3f((float)stretch.x, (float)stretch.y, (float)stretch.z));
			for(ParticleProperties inheritedProperty : inheritedProperties)
				spawn.setProperty(inheritedProperty, originParticle.getProperty(inheritedProperty));
		}
	}
}
