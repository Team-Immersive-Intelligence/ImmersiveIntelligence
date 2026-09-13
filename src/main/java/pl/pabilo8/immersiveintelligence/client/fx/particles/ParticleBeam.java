package pl.pabilo8.immersiveintelligence.client.fx.particles;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nonnull;
import javax.vecmath.Vector3f;

/**
 * Draws a four-sided beam between POSITION and STRETCH.
 * SIZE controls the start diameter and SCALE controls the end diameter.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.09.2026
 */
public class ParticleBeam extends ParticleVanilla
{
	private Vector3f endPoint = new Vector3f(1, 1, 1);
	private TextureAtlasSprite[] beamSprites = new TextureAtlasSprite[0];

	public ParticleBeam(World world, Vec3d pos)
	{
		super(world, pos);
	}

	@Override
	public void onUpdate()
	{
		Vector3f previousStart = new Vector3f(pos);
		super.onUpdate();
		endPoint.add(new Vector3f(
				pos.x-previousStart.x,
				pos.y-previousStart.y,
				pos.z-previousStart.z
		));
	}

	@Nonnull
	@Override
	public Object getProperty(ParticleProperties key)
	{
		if(key==ParticleProperties.STRETCH)
			return endPoint;
		return super.getProperty(key);
	}

	@Override
	public void setProperty(ParticleProperties key, Object value)
	{
		if(key==ParticleProperties.STRETCH)
			endPoint = new Vector3f((Vector3f)value);
		else
			super.setProperty(key, value);
	}

	@Override
	public void setTextureSprites(ResourceLocation[] textureSprites)
	{
		super.setTextureSprites(textureSprites);
		this.beamSprites = new TextureAtlasSprite[textureSprites.length];
		for(int i = 0; i < textureSprites.length; i++)
			this.beamSprites[i] = ClientUtils.getSprite(textureSprites[i]);
	}

	@Override
	public void render(BufferBuilder buffer, float partialTicks, float x, float xz, float z, float yz, float xy)
	{
		if(beamSprites.length==0)
			return;

		float startDiameter = Math.max(0, (float)getProperty(ParticleProperties.SIZE));
		float endDiameter = Math.max(0, (float)getProperty(ParticleProperties.SCALE));
		float largestDiameter = Math.max(startDiameter, endDiameter);
		if(largestDiameter <= 0)
			return;

		Vec3d interpolation = new Vec3d(motion.x*interpTicks, motion.y*interpTicks, motion.z*interpTicks);
		Vec3d start = new Vec3d(pos.x, pos.y, pos.z).add(interpolation).subtract(interPos);
		Vec3d end = new Vec3d(endPoint.x, endPoint.y, endPoint.z).add(interpolation).subtract(interPos);
		Vec3d distance = end.subtract(start);
		double length = distance.lengthVector();
		if(length <= 1.0E-7D)
			return;

		Vec3d direction = distance.scale(1d/length);
		Vec3d reference = Math.abs(direction.y) < 0.999d?new Vec3d(0, 1, 0): new Vec3d(1, 0, 0);
		Vec3d side1 = direction.crossProduct(reference).normalize();
		Vec3d side2 = side1.crossProduct(direction).normalize();
		int segments = Math.max(1, MathHelper.ceil(length/largestDiameter));

		int textureShift = (int)getProperty(ParticleProperties.TEXTURE_SHIFT);
		TextureAtlasSprite texture = beamSprites[Math.floorMod(textureShift, beamSprites.length)];
		IIColor color = (IIColor)getProperty(ParticleProperties.COLOR);
		int brightness = getBeamBrightness();
		int lightU = brightness>>16&65535;
		int lightV = brightness&65535;

		for(int segment = 0; segment < segments; segment++)
		{
			double startProgress = segment/(double)segments;
			double endProgress = (segment+1d)/segments;
			Vec3d segmentStart = start.add(direction.scale(length*startProgress));
			Vec3d segmentEnd = start.add(direction.scale(length*endProgress));
			double startRadius = MathHelper.clampedLerp(startDiameter, endDiameter, startProgress)*0.5d;
			double endRadius = MathHelper.clampedLerp(startDiameter, endDiameter, endProgress)*0.5d;
			Vec3d[] startCorners = getCorners(segmentStart, side1, side2, startRadius);
			Vec3d[] endCorners = getCorners(segmentEnd, side1, side2, endRadius);

			for(int face = 0; face < 4; face++)
				addFace(buffer, startCorners[face], endCorners[face], endCorners[(face+1)%4],
						startCorners[(face+1)%4], texture, color, lightU, lightV);
		}
	}

	private Vec3d[] getCorners(Vec3d center, Vec3d side1, Vec3d side2, double radius)
	{
		Vec3d horizontal = side1.scale(radius);
		Vec3d vertical = side2.scale(radius);
		return new Vec3d[]{
				center.add(horizontal).add(vertical),
				center.subtract(horizontal).add(vertical),
				center.subtract(horizontal).subtract(vertical),
				center.add(horizontal).subtract(vertical)
		};
	}

	private void addFace(BufferBuilder buffer, Vec3d a, Vec3d b, Vec3d c, Vec3d d,
						 TextureAtlasSprite texture, IIColor color, int lightU, int lightV)
	{
		addVertex(buffer, a, texture.getMinU(), texture.getMinV(), color, lightU, lightV);
		addVertex(buffer, b, texture.getMinU(), texture.getMaxV(), color, lightU, lightV);
		addVertex(buffer, c, texture.getMaxU(), texture.getMaxV(), color, lightU, lightV);
		addVertex(buffer, d, texture.getMaxU(), texture.getMinV(), color, lightU, lightV);
	}

	private void addVertex(BufferBuilder buffer, Vec3d point, float u, float v, IIColor color, int lightU, int lightV)
	{
		buffer.pos(point.x, point.y, point.z)
				.tex(u, v)
				.color(color.red, color.green, color.blue, color.alpha)
				.lightmap(lightU, lightV)
				.endVertex();
	}

	private int getBeamBrightness()
	{
		BlockPos blockPos = new BlockPos(pos.x, pos.y, pos.z);
		return world.isBlockLoaded(blockPos)?world.getCombinedLight(blockPos, 0): 0;
	}
}
