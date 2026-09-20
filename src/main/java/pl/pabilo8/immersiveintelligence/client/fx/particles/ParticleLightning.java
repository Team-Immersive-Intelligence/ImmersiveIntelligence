package pl.pabilo8.immersiveintelligence.client.fx.particles;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nonnull;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Draws an animated lightning between POSITION and STRETCH.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 06.08.2026
 */
public class ParticleLightning extends AbstractParticle
{
	private static final int SUB_POINT_COUNT = 12;
	private static final int BASE_ANIMATION_DELAY = 4;

	private final List<Vec3d> subPoints = new ArrayList<>();
	private final Random random = new Random();
	private IIColor color = IIColor.WHITE;
	private Vector3f endPoint = new Vector3f(1, 1, 1);
	private int animationTimer;

	/**
	 * Creates an animated lightning particle.
	 *
	 * @param world particle world
	 * @param pos   lightning start point
	 */
	public ParticleLightning(World world, Vec3d pos)
	{
		super(world, pos);
		random.setSeed(getLightningSeed());
	}

	@Override
	public void onUpdate()
	{
		if(subPoints.isEmpty()||animationTimer <= 0)
			createLightning();
		else
			animationTimer--;
		super.onUpdate();
	}

	@Nonnull
	@Override
	public Object getProperty(ParticleProperties key)
	{
		return switch(key)
		{
			case COLOR -> color;
			case RED -> color.red;
			case GREEN -> color.green;
			case BLUE -> color.blue;
			case ALPHA -> color.alpha;
			case STRETCH -> endPoint;
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
			case STRETCH:
				endPoint = new Vector3f((Vector3f)value);
				random.setSeed(getLightningSeed());
				subPoints.clear();
				animationTimer = 0;
				break;
			default:
				super.setProperty(key, value);
				break;
		}
	}

	@Override
	public void render(BufferBuilder buffer, float partialTicks, float x, float xz, float z, float yz, float xy)
	{
		if(subPoints.isEmpty())
			createLightning();
		renderStrip(buffer, color);
	}

	private long getLightningSeed()
	{
		long seed = Double.doubleToLongBits(pos.x);
		seed = 31*seed+Double.doubleToLongBits(pos.y);
		seed = 31*seed+Double.doubleToLongBits(pos.z);
		seed = 31*seed+Float.floatToIntBits(endPoint.x);
		seed = 31*seed+Float.floatToIntBits(endPoint.y);
		return 31*seed+Float.floatToIntBits(endPoint.z);
	}

	private void createLightning()
	{
		subPoints.clear();
		Vec3d start = new Vec3d(pos.x, pos.y, pos.z);
		Vec3d end = new Vec3d(endPoint.x, endPoint.y, endPoint.z);
		Vec3d distance = end.subtract(start);

		for(int i = 0; i < SUB_POINT_COUNT; i++)
		{
			Vec3d sub = start.addVector(
					distance.x/SUB_POINT_COUNT*i,
					distance.y/SUB_POINT_COUNT*i,
					distance.z/SUB_POINT_COUNT*i
			);
			double fixedPointDistance = (i-SUB_POINT_COUNT/2d)/(SUB_POINT_COUNT/2d);
			double modifier = 1d-0.75d*Math.abs(fixedPointDistance);
			double offsetX = (random.nextDouble()-0.5d)*modifier;
			double offsetY = (random.nextDouble()-0.5d)*modifier;
			double offsetZ = (random.nextDouble()-0.5d)*modifier;

			if(fixedPointDistance < 0)
			{
				offsetY += 0.75d*modifier*(0.75d+fixedPointDistance);
				offsetX = sub.x-start.x < 0?-Math.abs(offsetX): Math.abs(offsetX);
				offsetZ = sub.z-start.z < 0?-Math.abs(offsetZ): Math.abs(offsetZ);
			}
			else
			{
				offsetY = Math.min(end.y+(1d-fixedPointDistance)*-Math.signum(distance.y), offsetY);
				offsetX = Math.abs(offsetX)*(end.x-sub.x);
				offsetZ = Math.abs(offsetZ)*(end.z-sub.z);
			}
			subPoints.add(sub.addVector(offsetX, offsetY, offsetZ));
		}
		animationTimer = BASE_ANIMATION_DELAY+random.nextInt(5)-2;
	}

	private void renderStrip(BufferBuilder buffer, IIColor stripColor)
	{
		Vec3d start = new Vec3d(pos.x-interPos.x, pos.y-interPos.y, pos.z-interPos.z);
		Vec3d end = new Vec3d(endPoint.x-interPos.x, endPoint.y-interPos.y, endPoint.z-interPos.z);

		//Transparent duplicate endpoints separate particles in the shared GL_LINE_STRIP buffer.
		addVertex(buffer, start, stripColor, 0);
		addVertex(buffer, start, stripColor, stripColor.alpha);
		for(Vec3d point : subPoints)
			addVertex(buffer, point.subtract(interPos), stripColor, stripColor.alpha);
		addVertex(buffer, end, stripColor, stripColor.alpha);
		addVertex(buffer, end, stripColor, 0);
	}

	private void addVertex(BufferBuilder buffer, Vec3d point, IIColor stripColor, int alpha)
	{
		buffer.pos(point.x, point.y, point.z)
				.color(stripColor.red, stripColor.green, stripColor.blue, alpha)
				.endVertex();
	}
}
