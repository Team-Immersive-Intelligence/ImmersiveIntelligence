package pl.pabilo8.immersiveintelligence.client.fx.prefab;

import pl.pabilo8.immersiveintelligence.client.fx.particles.IIParticle;

import java.util.function.BiConsumer;

/**
 * @author Pabilo8
 * @since 17.04.2024
 */
public interface IProgrammableParticle<T extends IIParticle>
{
	/**
	 * Sets a program for the particle that will be executed every tick
	 */
	void setProgram(BiConsumer<T, Float> program);
}
