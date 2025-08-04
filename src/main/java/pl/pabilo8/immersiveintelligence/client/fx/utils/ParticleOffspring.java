package pl.pabilo8.immersiveintelligence.client.fx.utils;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.client.fx.factories.ParticleFactory;
import pl.pabilo8.immersiveintelligence.client.fx.particles.AbstractParticle;
import pl.pabilo8.immersiveintelligence.client.fx.utils.IIParticleUtils.PositionGenerator;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 22.12.2024
 **/
public class ParticleOffspring<T extends AbstractParticle>
{
	private final String factoryName;
	private final PositionGenerator positionGenerator;
	private final float size;
	private final int minAmount;
	private final int randAmount;
	private boolean initialized = false;
	private ParticleFactory<T> particleFactory;

	public ParticleOffspring(String factoryName, PositionGenerator positionGenerator,
							 float size, int minAmount, int maxAmount)
	{
		this.factoryName = factoryName;
		this.positionGenerator = positionGenerator;
		this.size = size;
		this.minAmount = Math.min(minAmount, maxAmount);
		this.randAmount = Math.max(minAmount, maxAmount)-minAmount;
	}

	public ParticleOffspring(String factoryName, PositionGenerator positionGenerator,
							 float size, int amount)
	{
		this(factoryName, positionGenerator, size, amount, amount);
	}

	@SuppressWarnings("unchecked")
	public void init()
	{
		this.particleFactory = (ParticleFactory<T>)ParticleRegistry.getParticle(factoryName);
		this.initialized = true;
	}

	public <O extends AbstractParticle> void spawn(O originParticle)
	{
		if(!initialized)
			init();

		Vector3f originVector = (Vector3f)originParticle.getProperty(ParticleProperties.POSITION);
		Vec3d originPos = new Vec3d(originVector.x, originVector.y, originVector.z);

		int amount = minAmount+(randAmount > 0?Utils.RAND.nextInt(randAmount): 0);
		for(int i = 0; i < amount; i++)
		{
			Vec3d pos = positionGenerator.generatePosition(originPos, i, size, amount);
			Vec3d motion = positionGenerator.generateMotion(originPos, i, size, amount);
			Vector2f rotation = positionGenerator.generateRotation(originPos, i, size, amount);

			particleFactory.spawn(pos, motion, rotation);
		}
	}
}

