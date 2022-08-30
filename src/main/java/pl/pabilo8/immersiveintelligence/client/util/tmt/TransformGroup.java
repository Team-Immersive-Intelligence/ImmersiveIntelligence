package pl.pabilo8.immersiveintelligence.client.util.tmt;

import net.minecraft.util.math.Vec3d;

public abstract class TransformGroup
{
	public abstract double getWeight();

	public abstract Vec3d doTransformation(PositionTransformVertex vertex);
}
