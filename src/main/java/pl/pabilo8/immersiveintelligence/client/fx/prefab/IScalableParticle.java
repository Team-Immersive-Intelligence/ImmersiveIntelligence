package pl.pabilo8.immersiveintelligence.client.fx.prefab;

import net.minecraft.util.math.Vec3d;

/**
 * @author Pabilo8
 * @since 18.05.2024
 */
public interface IScalableParticle extends IResizableParticle
{
	//--- Scale ---//

	/**
	 * @return display scale of the particle
	 */
	Vec3d getScale();

	/**
	 * Sets the display scale of the particle
	 *
	 * @param scale scale of the particle
	 */
	void setScale(Vec3d scale);

}
