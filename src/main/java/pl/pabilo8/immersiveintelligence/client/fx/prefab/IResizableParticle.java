package pl.pabilo8.immersiveintelligence.client.fx.prefab;

/**
 * @author Pabilo8
 * @since 18.05.2024
 */
public interface IResizableParticle
{
	//--- Size ---//

	/**
	 * Sets the size of the particle
	 *
	 * @param size size of the particle
	 */
	void setSize(double size);

	/**
	 * @return size of the particle
	 */
	double getSize();
}
