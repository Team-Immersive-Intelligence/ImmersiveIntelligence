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
 * Draws a textured crossed ribbon between POSITION and STRETCH.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.08.2026
 */
public class ParticleRibbon extends ParticleVanilla
{
	private Vector3f endPoint = new Vector3f(1, 1, 1);
	private TextureAtlasSprite[] ribbonSprites = new TextureAtlasSprite[0];

	/**
	 * Creates a ribbon particle.
	 *
	 * @param world particle world
	 * @param pos   ribbon start point
	 */
	public ParticleRibbon(World world, Vec3d pos)
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
		this.ribbonSprites = new TextureAtlasSprite[textureSprites.length];
		for(int i = 0; i < textureSprites.length; i++)
			this.ribbonSprites[i] = ClientUtils.getSprite(textureSprites[i]);
	}

	@Override
	public void render(BufferBuilder buffer, float partialTicks, float x, float xz, float z, float yz, float xy)
	{
		if(ribbonSprites.length==0)
			return;

		float diameter = (float)getProperty(ParticleProperties.SIZE)*(float)getProperty(ParticleProperties.SCALE);
		if(diameter <= 0)
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
		Vec3d side1 = direction.crossProduct(reference).normalize().scale(diameter*0.5d);
		Vec3d side2 = side1.crossProduct(direction).normalize().scale(diameter*0.5d);
		int segments = Math.max(1, MathHelper.ceil(length/diameter));

		int textureShift = (int)getProperty(ParticleProperties.TEXTURE_SHIFT);
		TextureAtlasSprite texture = ribbonSprites[Math.floorMod(textureShift, ribbonSprites.length)];
		IIColor color = (IIColor)getProperty(ParticleProperties.COLOR);
		int brightness = getRibbonBrightness();
		int lightU = brightness>>16&65535;
		int lightV = brightness&65535;

		for(int i = 0; i < segments; i++)
		{
			double segmentStartDistance = i*diameter;
			double segmentEndDistance = Math.min((i+1)*diameter, length);
			Vec3d segmentStart = start.add(direction.scale(segmentStartDistance));
			Vec3d segmentEnd = start.add(direction.scale(segmentEndDistance));
			addQuad(buffer, segmentStart, segmentEnd, side1, texture, color, lightU, lightV);
			addQuad(buffer, segmentStart, segmentEnd, side2, texture, color, lightU, lightV);
		}
	}

	private void addQuad(BufferBuilder buffer, Vec3d start, Vec3d end, Vec3d side,
	                     TextureAtlasSprite texture, IIColor color, int lightU, int lightV)
	{
		Vec3d a = start.add(side);
		Vec3d b = end.add(side);
		Vec3d c = end.subtract(side);
		Vec3d d = start.subtract(side);

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

	private int getRibbonBrightness()
	{
		BlockPos blockPos = new BlockPos(pos.x, pos.y, pos.z);
		return world.isBlockLoaded(blockPos)?world.getCombinedLight(blockPos, 0): 0;
	}
}
