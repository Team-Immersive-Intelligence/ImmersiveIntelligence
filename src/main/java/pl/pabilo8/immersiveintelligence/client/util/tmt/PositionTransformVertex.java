package pl.pabilo8.immersiveintelligence.client.util.tmt;

import net.minecraft.util.math.Vec3d;

public class PositionTransformVertex extends PositionTextureVertex
{
	public PositionTransformVertex(float x, float y, float z, float u, float v)
	{
		this(new Vec3d(x, y, z), u, v);
	}

	public PositionTransformVertex(PositionTextureVertex vertex, float u, float v)
	{
		super(vertex, u, v);
		if(vertex instanceof PositionTransformVertex)
			neutralVector = ((PositionTransformVertex)vertex).neutralVector;
		else
			neutralVector = new Vec3d(vertex.vector3D.x, vertex.vector3D.y, vertex.vector3D.z);
	}

	public PositionTransformVertex(PositionTextureVertex vertex)
	{
		this(vertex, vertex.texturePositionX, vertex.texturePositionY);
	}

	public PositionTransformVertex(Vec3d vector, float u, float v)
	{
		super(vector, u, v);
		neutralVector = new Vec3d(vector.x, vector.y, vector.z);
	}

	public void setTransformation()
	{
		vector3D = new Vec3d(neutralVector.x, neutralVector.y, neutralVector.z);
	}

	public Vec3d neutralVector;

}
