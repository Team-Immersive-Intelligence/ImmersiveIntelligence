package pl.pabilo8.immersiveintelligence.client.util.amt.parts;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for {@link AMTQuads} that can append faces by 3D and UV coordinates.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.02.2026
 *
 */
public class AMTQuadsBuilder
{
	private static final VertexFormat FORMAT = DefaultVertexFormats.ITEM;

	private final List<BakedQuad> quads = new ArrayList<>();
	private final TextureAtlasSprite sprite;
	private boolean diffuseLighting = true;

	public AMTQuadsBuilder(@Nullable TextureAtlasSprite sprite)
	{
		this.sprite = sprite;
	}

	public AMTQuadsBuilder setDiffuseLighting(boolean diffuseLighting)
	{
		this.diffuseLighting = diffuseLighting;
		return this;
	}

	/**
	 * Adds a single axis-aligned face.
	 *
	 * @param face which face to create (determines plane and winding)
	 * @param xyz1 one corner in model space
	 * @param xyz2 opposite corner in model space
	 * @param uv1  UV min (pixels, not normalized)
	 * @param uv2  UV max (pixels, not normalized)
	 */
	public AMTQuadsBuilder withFace(EnumFacing face, Vec3d xyz1, Vec3d xyz2, Vec2f uv1, Vec2f uv2)
	{
		quads.add(makeFace(face, xyz1.scale(0.0625), xyz2.scale(0.0625), uv1, uv2, 16f, 16f));
		return this;
	}

	/**
	 * Adds a single axis-aligned face with explicit texture size for proper UV normalization.
	 */
	public AMTQuadsBuilder withFace(EnumFacing face, Vec3d xyz1, Vec3d xyz2, Vec2f uv1, Vec2f uv2, float texW, float texH)
	{
		quads.add(makeFace(face, xyz1.scale(0.0625), xyz2.scale(0.0625), uv1, uv2, texW, texH));
		return this;
	}

	/**
	 * Adds a box (6 faces) with Minecraft-style UV unfolding inside the UV rectangle [uvStart..uvEnd].
	 * <p>
	 * UV layout inside the rectangle (in pixels):
	 * - Middle row: LEFT | FRONT | RIGHT | BACK  (each: dz x dy)
	 * - Top row (above FRONT): UP (dz x dx)
	 * - Bottom row (below FRONT): DOWN (dz x dx)
	 *
	 * @param xyz1    min corner
	 * @param xyz2    max corner
	 * @param uvStart UV start (pixels)
	 * @param uvEnd   UV end (pixels)
	 * @param texW    texture width (pixels)
	 * @param texH    texture height (pixels)
	 */
	public AMTQuadsBuilder withBox(Vec3d xyz1, Vec3d xyz2, Vec2f uvStart, Vec2f uvEnd, float texW, float texH)
	{
		//Positions
		double x1 = Math.min(xyz1.x, xyz2.x), x2 = Math.max(xyz1.x, xyz2.x);
		double y1 = Math.min(xyz1.y, xyz2.y), y2 = Math.max(xyz1.y, xyz2.y);
		double z1 = Math.min(xyz1.z, xyz2.z), z2 = Math.max(xyz1.z, xyz2.z);

		//Sizes and UV base
		float dx = (float)(x2-x1);
		float dy = (float)(y2-y1);
		float dz = (float)(z2-z1);
		float u0 = uvStart.x, v0 = uvStart.y;

		//Horizontal spans (middle row): LEFT(dz) FRONT(dx) RIGHT(dz) BACK(dx)
		float uFront0 = u0+dz;
		float uRight0 = uFront0+dx;
		float uBack0 = uRight0+dz;

		//Vertical spans
		float vMid0 = v0+dz;
		float vBot0 = vMid0+dy;

		//Build faces with per-face UVs (pixel coords)
		return this
				//FRONT (+Z): x by y
				.withFace(EnumFacing.SOUTH,
						new Vec3d(x1, y1, z2), new Vec3d(x2, y2, z2),
						new Vec2f(uFront0, vMid0), new Vec2f(uFront0+dx, vMid0+dy),
						texW, texH)
				//BACK (-Z): x by y (on BACK slot)
				.withFace(EnumFacing.NORTH,
						new Vec3d(x2, y1, z1), new Vec3d(x1, y2, z1),
						new Vec2f(uBack0, vMid0), new Vec2f(uBack0+dx, vMid0+dy),
						texW, texH)
				//LEFT (-X): z by y
				.withFace(EnumFacing.WEST,
						new Vec3d(x1, y1, z1), new Vec3d(x1, y2, z2),
						new Vec2f(u0, vMid0), new Vec2f(u0+dz, vMid0+dy),
						texW, texH)
				//RIGHT (+X): z by y
				.withFace(EnumFacing.EAST,
						new Vec3d(x2, y1, z2), new Vec3d(x2, y2, z1),
						new Vec2f(uRight0, vMid0), new Vec2f(uRight0+dz, vMid0+dy),
						texW, texH)
				//UP (+Y): x by z, placed in top row over FRONT, size dx by dz
				.withFace(EnumFacing.UP,
						new Vec3d(x1, y2, z2), new Vec3d(x2, y2, z1),
						new Vec2f(uFront0, v0), new Vec2f(uFront0+dx, v0+dz),
						texW, texH)
				//DOWN (-Y): x by z, placed in bottom row under FRONT
				.withFace(EnumFacing.DOWN,
						new Vec3d(x1, y1, z1), new Vec3d(x2, y1, z2),
						new Vec2f(uFront0, vBot0), new Vec2f(uFront0+dx, vBot0+dz),
						texW, texH);
	}

	public AMTQuads build(String name, Vec3d originPos)
	{
		return new AMTQuads(name, originPos, quads.toArray(new BakedQuad[0]));
	}

	//--- Internals ---

	private BakedQuad makeFace(EnumFacing face, Vec3d a, Vec3d b, Vec2f uvA, Vec2f uvB, float texW, float texH)
	{
		//normalize xyz
		double x1 = Math.min(a.x, b.x), x2 = Math.max(a.x, b.x);
		double y1 = Math.min(a.y, b.y), y2 = Math.max(a.y, b.y);
		double z1 = Math.min(a.z, b.z), z2 = Math.max(a.z, b.z);

		//normalize uv (pixels)
		float u0 = Math.min(uvA.x, uvB.x);
		float v0 = Math.min(uvA.y, uvB.y);
		float u1 = Math.max(uvA.x, uvB.x);
		float v1 = Math.max(uvA.y, uvB.y);

		//convert to atlas-interpolated UVs
		float iu0 = sprite!=null?sprite.getInterpolatedU(u0/texW*16f): (u0/texW);
		float iv0 = sprite!=null?sprite.getInterpolatedV(v0/texH*16f): (v0/texH);
		float iu1 = sprite!=null?sprite.getInterpolatedU(u1/texW*16f): (u1/texW);
		float iv1 = sprite!=null?sprite.getInterpolatedV(v1/texH*16f): (v1/texH);

		//4 vertices, ITEM format is 7 ints per vertex in 1.12 (pos,color,uv,light,normal) but we fill minimally:
		//pos (3 floats), color (int), uv (2 floats), normal (3 bytes packed) are handled via int packing below.
		int[] data = new int[FORMAT.getIntegerSize()*4];

		//choose plane + winding per face (consistent outward normals)
		switch(face)
		{
			case UP:    //y = y2, z2->z1 to keep winding
				putVertex(data, 0, (float)x1, (float)y2, -(float)z2, iu0, iv0, face);
				putVertex(data, 1, (float)x1, (float)y2, -(float)z1, iu0, iv1, face);
				putVertex(data, 2, (float)x2, (float)y2, -(float)z1, iu1, iv1, face);
				putVertex(data, 3, (float)x2, (float)y2, -(float)z2, iu1, iv0, face);
				break;
			case DOWN:  //y = y1
				putVertex(data, 0, (float)x1, (float)y1, -(float)z1, iu0, iv0, face);
				putVertex(data, 1, (float)x1, (float)y1, -(float)z2, iu0, iv1, face);
				putVertex(data, 2, (float)x2, (float)y1, -(float)z2, iu1, iv1, face);
				putVertex(data, 3, (float)x2, (float)y1, -(float)z1, iu1, iv0, face);
				break;
			case NORTH: //z = z1
				putVertex(data, 0, (float)x2, (float)y1, -(float)z1, iu0, iv1, face);
				putVertex(data, 1, (float)x2, (float)y2, -(float)z1, iu0, iv0, face);
				putVertex(data, 2, (float)x1, (float)y2, -(float)z1, iu1, iv0, face);
				putVertex(data, 3, (float)x1, (float)y1, -(float)z1, iu1, iv1, face);
				break;
			case SOUTH: //z = z2
				putVertex(data, 0, (float)x1, (float)y1, -(float)z2, iu0, iv1, face);
				putVertex(data, 1, (float)x1, (float)y2, -(float)z2, iu0, iv0, face);
				putVertex(data, 2, (float)x2, (float)y2, -(float)z2, iu1, iv0, face);
				putVertex(data, 3, (float)x2, (float)y1, -(float)z2, iu1, iv1, face);
				break;
			case WEST:  //x = x1
				putVertex(data, 0, (float)x1, (float)y1, -(float)z1, iu0, iv1, face);
				putVertex(data, 1, (float)x1, (float)y2, -(float)z1, iu0, iv0, face);
				putVertex(data, 2, (float)x1, (float)y2, -(float)z2, iu1, iv0, face);
				putVertex(data, 3, (float)x1, (float)y1, -(float)z2, iu1, iv1, face);
				break;
			case EAST:  //x = x2
			default:
				putVertex(data, 0, (float)x2, (float)y1, -(float)z2, iu0, iv1, face);
				putVertex(data, 1, (float)x2, (float)y2, -(float)z2, iu0, iv0, face);
				putVertex(data, 2, (float)x2, (float)y2, -(float)z1, iu1, iv0, face);
				putVertex(data, 3, (float)x2, (float)y1, -(float)z1, iu1, iv1, face);
				break;
		}

		//noinspection DataFlowIssue
		return new BakedQuad(data, -1, face, sprite, diffuseLighting, FORMAT);
	}

	private void putVertex(int[] data, int vert, float x, float y, float z, float u, float v, EnumFacing face)
	{
		int stride = FORMAT.getIntegerSize();
		int i = vert*stride;

		//Position (3 floats)
		data[i] = Float.floatToRawIntBits(x);
		data[i+1] = Float.floatToRawIntBits(y);
		data[i+2] = Float.floatToRawIntBits(z);

		//Color (white, RGBA packed as int; BufferBuilder will multiply if needed)
		data[i+3] = 0xFFFFFFFF;

		//UV (2 floats)
		data[i+4] = Float.floatToRawIntBits(u);
		data[i+5] = Float.floatToRawIntBits(v);

		//Normal packed into int (xyz as signed bytes in low 24 bits) in 1.12 item format
		int nx = face.getFrontOffsetX();
		int ny = face.getFrontOffsetY();
		int nz = face.getFrontOffsetZ();
		data[i+6] = (nx&0xFF)|((ny&0xFF)<<8)|((nz&0xFF)<<16);
	}
}
