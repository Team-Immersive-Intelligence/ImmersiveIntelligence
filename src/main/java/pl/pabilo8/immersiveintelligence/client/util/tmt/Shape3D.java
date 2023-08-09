package pl.pabilo8.immersiveintelligence.client.util.tmt;

public class Shape3D
{
	public PositionTransformVertex[] vertices;
	public TexturedPolygon[] faces;
	public Shape3D(PositionTransformVertex[] verts, TexturedPolygon[] poly)
	{
		vertices = verts;
		faces = poly;
	}
}
