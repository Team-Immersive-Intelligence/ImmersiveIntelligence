package pl.pabilo8.immersiveintelligence.client.fx.particles;

import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Draws an additive burst of glow rays (like an ender dragon explosion).
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 06.08.2026
 */
public class ParticleGlow extends AbstractParticle
{
	private static final int RAY_COUNT = 60;
	private static final long RAY_SEED = 432L;

	private IIColor color = IIColor.WHITE;
	private IIColor edgeColor = IIColor.fromPackedRGB(0xFF7F37);
	private float size = 1f;
	private float scale = 1f;

	/**
	 * Creates an additive glow particle.
	 *
	 * @param world particle world
	 * @param pos   particle position
	 */
	public ParticleGlow(World world, Vec3d pos)
	{
		super(world, pos);
	}

	@Nonnull
	@Override
	public Object getProperty(ParticleProperties key)
	{
		return switch(key)
		{
			case COLOR -> color;
			case COLOR_SECONDARY -> edgeColor;
			case RED -> color.red;
			case GREEN -> color.green;
			case BLUE -> color.blue;
			case ALPHA -> color.alpha;
			case SIZE -> size;
			case SCALE -> scale;
			default -> super.getProperty(key);
		};
	}

	@Override
	public void setProperty(ParticleProperties key, Object value)
	{
		switch(key)
		{
			case COLOR:
				color = (IIColor)value;
				break;
			case COLOR_SECONDARY:
				edgeColor = (IIColor)value;
				break;
			case RED:
				color = color.withRed((float)value);
				break;
			case GREEN:
				color = color.withGreen((float)value);
				break;
			case BLUE:
				color = color.withBlue((float)value);
				break;
			case ALPHA:
				color = color.withAlpha((float)value);
				break;
			case SIZE:
				size = (float)value;
				break;
			case SCALE:
				scale = (float)value;
				break;
			default:
				super.setProperty(key, value);
				break;
		}
	}

	@Override
	public void preRender(float partialTicks, float x, float xz, float z, float yz, float xy)
	{
		super.preRender(partialTicks, x, xz, z, yz, xy);
		float age = lifeTime+partialTicks;
		matrix.translate(0, -1d+Math.min(1f, age)*12f*size/200f, 0);
	}

	@Override
	public void render(BufferBuilder buffer, float partialTicks, float x, float xz, float z, float yz, float xy)
	{
		float age = lifeTime+partialTicks;
		float safeLifetime = Math.max(1f, maxLifeTime);
		float fadeIn = MathHelper.clamp(age/(safeLifetime*0.1f), 0f, 1f);
		float fadeOut = MathHelper.clamp((safeLifetime-age)/(safeLifetime*0.05f), 0f, 1f);
		float visibleScale = size*scale*Math.min(fadeIn, fadeOut);
		if(visibleScale <= 0f)
			return;

		float spin = getProgress(partialTicks)*25f;
		Random random = new Random(RAY_SEED);
		Matrix4 rayMatrix = matrix.copy();
		for(int i = 0; i < RAY_COUNT; i++)
		{
			rayMatrix.rotate(Math.toRadians(random.nextFloat()*180f), 1, 0, 0);
			rayMatrix.rotate(Math.toRadians(random.nextFloat()*360f), 0, 1, 0);
			rayMatrix.rotate(Math.toRadians(random.nextFloat()*180f), 1, 0, 0);
			rayMatrix.rotate(Math.toRadians(random.nextFloat()*360f), 0, 1, 0);
			rayMatrix.rotate(Math.toRadians(random.nextFloat()*360f+spin), 0, 0, 1);

			double length = random.nextFloat()*35f*visibleScale;
			double radius = random.nextFloat()*15f*visibleScale;
			Vec3d origin = rayMatrix.apply(Vec3d.ZERO);
			Vec3d first = rayMatrix.apply(new Vec3d(-0.8660254037844386*radius, length, -0.5*radius));
			Vec3d second = rayMatrix.apply(new Vec3d(0.8660254037844386*radius, length, -0.5*radius));
			Vec3d third = rayMatrix.apply(new Vec3d(0, length, radius));

			addTriangle(buffer, origin, first, second);
			addTriangle(buffer, origin, second, third);
			addTriangle(buffer, origin, third, first);
		}
	}

	private void addTriangle(BufferBuilder buffer, Vec3d origin, Vec3d first, Vec3d second)
	{
		buffer.pos(origin.x, origin.y, origin.z)
				.color(color.red, color.green, color.blue, color.alpha)
				.endVertex();
		buffer.pos(first.x, first.y, first.z)
				.color(edgeColor.red, edgeColor.green, edgeColor.blue, 0)
				.endVertex();
		buffer.pos(second.x, second.y, second.z)
				.color(edgeColor.red, edgeColor.green, edgeColor.blue, 0)
				.endVertex();
		buffer.pos(second.x, second.y, second.z)
				.color(edgeColor.red, edgeColor.green, edgeColor.blue, 0)
				.endVertex();
	}
}
