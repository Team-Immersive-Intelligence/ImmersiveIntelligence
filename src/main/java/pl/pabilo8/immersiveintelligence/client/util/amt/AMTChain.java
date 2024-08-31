package pl.pabilo8.immersiveintelligence.client.util.amt;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.rotary.MotorBeltType;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.render.mechanical_device.BeltModelStorage;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityMechanicalConnectable;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIVectorLine;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIModelHeader;

import java.util.ArrayList;

/**
 * AMT type for drawing chains
 *
 * @author Pabilo8
 * @since 02.08.2024
 */
public class AMTChain extends AMT
{
	//--- Builder values ---//
	private final ArrayList<Vec3d> nodes = new ArrayList<>();
	private AMTQuads segment;
	private IIVectorLine animPosition, animRotation;
	private float segmentLength, segmentOffset, progress = 0;
	private int segments;

	//--- Post-compilation ---//
	private boolean compiled;

	public AMTChain(String name, Vec3d originPos)
	{
		super(name, originPos);
	}

	public AMTChain(String name, IIModelHeader header)
	{
		this(name, header.getOffset(name));
	}

	public static AMTChain getChainForNetwork(TileEntityMechanicalConnectable startTE, Connection connection)
	{
		//Connection length
		Vec3d pointEnd = new Vec3d(connection.end).subtract(new Vec3d(connection.start));
		//Belts can be connected only on X or Z axis
		boolean mainAxisIsX = pointEnd.z==0, negative = false;
		TileEntityMechanicalConnectable endTE = (TileEntityMechanicalConnectable)startTE.getWorld().getTileEntity(connection.end);
		Vec2f end = new Vec2f(-(float)(mainAxisIsX?pointEnd.x: pointEnd.z), (float)pointEnd.y);
		assert endTE!=null;
		float angle = (float)((Math.atan2(end.x, end.y)*(180/Math.PI))-90);

		//Behold, Grand Wall of Fixes, because making a universal system is too hard of a task
		if(connection.vertical)
			angle += 180;
		else if(startTE.getFacing().getAxisDirection()==AxisDirection.NEGATIVE^mainAxisIsX)
		{
			end = new Vec2f(-end.x, end.y);
			angle += 180;
		}
		else
			angle = -angle;

		//Else ye perish
		assert connection.cableType instanceof MotorBeltType;
		AMTChain chain = new AMTChain("chain", Vec3d.ZERO)
				.withSegmentModel(BeltModelStorage.getModelForBelt(((MotorBeltType)connection.cableType)));

		//First half of first belt wheel
		for(float i = 0; i <= 90; i += 45)
		{
			Vec2f offset = new Vec2f(
					(float)(Math.cos(Math.toRadians(angle+i+180))*(startTE.getRadius()+1)/16d),
					(float)(Math.sin(Math.toRadians(angle+i+180))*(startTE.getRadius()+1)/16d)
			);
			chain.withNode(offset.x, offset.y, angle+i+90);
		}

		//Second belt wheel
		for(float i = 90; i <= 270; i += 45)
		{
			Vec2f offset = new Vec2f(
					(float)(Math.cos(Math.toRadians(angle+i-180))*(endTE.getRadius()+1)/16d)+end.x,
					(float)(Math.sin(Math.toRadians(angle+i-180))*(endTE.getRadius()+1)/16d)+end.y
			);
			chain.withNode(offset.x, offset.y, angle+i+90);
		}

		//Second half of first belt wheel
		for(float i = 270; i <= 360; i += 45)
		{
			Vec2f offset = new Vec2f(
					(float)(Math.cos(Math.toRadians(angle+i+180))*(startTE.getRadius()+1)/16d),
					(float)(Math.sin(Math.toRadians(angle+i+180))*(startTE.getRadius()+1)/16d)
			);
			chain.withNode(offset.x, offset.y, angle+i+90);
		}

		return chain;
	}

	public AMTChain withSegmentModel(AMTQuads segment)
	{
		this.segment = new AMTQuads("nucular_chargis", segment.originPos, segment.quads);
//		this.segment = segment;

		//find min and max points on X axis of the model
		float minX = Float.MAX_VALUE, maxX = -Float.MAX_VALUE;
		for(BakedQuad quad : segment.quads)
			for(Vec3d vertex : IIClientUtils.extractVertexPositions(quad))
			{
				if(vertex.x < minX)
					minX = (float)vertex.x;
				if(vertex.x > maxX)
					maxX = (float)vertex.x;
			}
		//the length of a single track segment
		segmentLength = maxX-minX;

		disposeOf();
		return this;
	}

	public AMTChain clearNodes()
	{
		nodes.clear();
		disposeOf();
		return this;
	}

	public AMTChain withNode(double x, double y, double dir)
	{
		nodes.add(new Vec3d(x, y, dir));
		disposeOf();
		return this;
	}

	public AMTChain withWheels(double x, double y, double radius1, double x2, double y2, double radius2)
	{
//		nodes.add(new Vec3d());

		disposeOf();
		return this;
	}

	public void setProgress(float progress)
	{
		this.progress = progress;
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		if(!compiled)
		{
			//Total distance used for time calculation later
			float totalDistance = 0;
			int size = nodes.size();

			//Parts from which the animation will be built
			Vec3d[] positions = new Vec3d[size+1];
			Vec3d[] rotations = new Vec3d[size+1];
			float[] times = new float[size+1];

			//Go through all nodes and build the animation
			for(int n = 0; n < size; n++)
			{
				//Current and next node
				Vec3d thisNode = nodes.get(n);
				Vec3d nextNode = nodes.get((int)IIMath.positiveModulo(n-1, nodes.size()));

				//Calculate distance and number of segments
				times[n] = (float)Math.sqrt(Math.pow(nextNode.x-thisNode.x, 2)+Math.pow(nextNode.y-thisNode.y, 2));
				positions[n] = new Vec3d(thisNode.x, thisNode.y, 0);
				rotations[n] = new Vec3d(0, 0, thisNode.z);
				totalDistance += times[n];
				times[n] = totalDistance;
			}

			positions[size] = positions[0];
			rotations[size] = rotations[0].scale(-1);
			times[size] = 1;

			if(totalDistance!=0)
				for(int i = size-1; i >= 0; i--)
					times[i] = (times[i]-times[0])/totalDistance;

			animPosition = new IIVectorLine(times, positions);
			animRotation = new IIVectorLine(times, rotations);
			segments = MathHelper.floor(totalDistance/segmentLength);
			segmentOffset = 1f/segments;

			compiled = true;
		}
		else
		{
			GlStateManager.translate(0.5, 0.5, 0.5);
			//Draw chain elements
			for(int i = 0; i < segments; i++)
			{
				Vec3d translate = animPosition.getForTime((progress+(i*segmentOffset))%1f);
				Vec3d rotate = animRotation.getForTime((progress+(i*segmentOffset))%1f);

				GlStateManager.pushMatrix();
				GlStateManager.translate(translate.x, translate.y, translate.z);
				GlStateManager.rotate((float)rotate.z, 0, 0, 1);

				segment.draw(tes, buf);
				GlStateManager.popMatrix();
			}
			/*for(Vec3d value : animPosition.values)
			{
				GlStateManager.pushMatrix();
				GlStateManager.translate(value.x, value.y, value.z);

				segment.draw(tes, buf);
				GlStateManager.popMatrix();
			}*/
		}
	}

	@Override
	public void disposeOf()
	{
		IIAnimationUtils.disposeOf(segment);
		compiled = false;
	}
}
