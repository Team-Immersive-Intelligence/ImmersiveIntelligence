package pl.pabilo8.immersiveintelligence.client.util.amt.parts;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.rotary.MotorBeltType;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.render.mechanical_device.BeltModelStorage;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityMechanicalConnectable;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIVectorLine;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * AMT type for drawing chains, tracks and motor belts.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 02.08.2024
 */
public class AMTChain extends AMT
{
	private static final double EPSILON = 1e-7;
	private static final double TWO_PI = Math.PI*2d;
	private static final double MAX_ARC_STEP = Math.PI/12d;
	private static final double MAX_CONTACT_ARC = Math.PI+1e-4d;

	private final ArrayList<ChainNode> nodes = new ArrayList<>();
	private final ArrayList<Wheel> wheels = new ArrayList<>();
	private final HashMap<Integer, PathCache> pathCache = new HashMap<>();
	private AMTQuads segment;
	private PathCache activePath;
	private float segmentLength, progress = 0;
	private Axis trackAxis = Axis.X;
	private AxisDirection axisDirection = AxisDirection.POSITIVE;

	public AMTChain(String name, Vec3d originPos)
	{
		super(name, originPos);
	}

	public AMTChain(String name, AMTModelHeader header)
	{
		this(name, header.getOffset(name));
	}

	public static AMTChain getChainForNetwork(TileEntityMechanicalConnectable startTE, Connection connection)
	{
		Vec3d delta = new Vec3d(connection.end).subtract(new Vec3d(connection.start));
		Axis axis = delta.z==0?Axis.X: Axis.Z;
		TileEntityMechanicalConnectable endTE = (TileEntityMechanicalConnectable)startTE.getWorld().getTileEntity(connection.end);
		assert endTE!=null;
		assert connection.cableType instanceof MotorBeltType;

		double end = -(axis==Axis.X?delta.x: delta.z);
		if(!connection.vertical&&(startTE.getFacing().getAxisDirection()==AxisDirection.NEGATIVE^axis==Axis.X))
			end = -end;

		AMTChain chain = new AMTChain("chain", Vec3d.ZERO)
				.withSegmentModel(BeltModelStorage.getModelForBelt((MotorBeltType)connection.cableType))
				.withTrackAxis(axis)
				.withAxisDirection(end < 0?AxisDirection.POSITIVE: AxisDirection.NEGATIVE);
		chain.withWheel(0, 0, (startTE.getRadius()+1)/16d, 0);
		chain.withWheel(end, delta.y, (endTE.getRadius()+1)/16d, 0);
		return chain;
	}

	//---Setters---//

	public AMTChain withSegmentModel(AMTQuads segment)
	{
		if(this.segment!=null&&this.segment!=segment)
			AMTUtils.disposeOf(this.segment);
		this.segment = new AMTQuads("nucular_chargis", segment.originPos, segment.quads);
		this.segmentLength = measureSegmentLength(segment);
		invalidatePath();
		return this;
	}

	public AMTChain clearNodes()
	{
		nodes.clear();
		wheels.clear();
		invalidatePath();
		return this;
	}

	public AMTChain withNode(double x, double y, double dir)
	{
		return withNode(new Vec3d(x, y, 0), dir);
	}

	public AMTChain withNode(double x, double y, double z, double dir)
	{
		return withNode(new Vec3d(x, y, z), dir);
	}

	public AMTChain withNode(Vec3d position, double dir)
	{
		nodes.add(ChainNode.link(new Vec2f((float)(trackAxis==Axis.X?position.x: position.z), (float)position.y), dir));
		invalidatePath();
		return this;
	}

	public AMTChain withWheel(Vec3d position, double radius, double maxCompression)
	{
		return withWheel((float)(trackAxis==Axis.X?position.x: position.z), (float)position.y, radius, maxCompression);
	}

	public AMTChain withWheel(double x, double y, double radius, double maxCompression)
	{
		addWheel(new Vec2f((float)x, (float)y), radius, maxCompression);
		return this;
	}

	public Wheel addWheel(Vec3d position, double radius, double maxCompression)
	{
		return addWheel(new Vec2f((float)(trackAxis==Axis.X?position.x: position.z), (float)position.y), radius, maxCompression);
	}

	public Wheel addWheel(Vec2f position, double radius, double maxCompression)
	{
		ChainNode node = ChainNode.wheel(position, Math.max(0d, radius), Math.max(0d, maxCompression));
		nodes.add(node);
		Wheel wheel = new Wheel(node);
		wheels.add(wheel);
		invalidatePath();
		return wheel;
	}

	public AMTChain setWheelCompression(int wheelIndex, float compression)
	{
		if(wheelIndex >= 0&&wheelIndex < wheels.size())
			wheels.get(wheelIndex).setCompression(compression);
		return this;
	}

	public AMTChain withTrackAxis(Axis axis)
	{
		Axis resolved = axis==Axis.Z?Axis.Z: Axis.X;
		if(trackAxis!=resolved)
		{
			trackAxis = resolved;
			invalidatePath();
		}
		return this;
	}

	public AMTChain withAxisDirection(AxisDirection axisDirection)
	{
		AxisDirection resolved = axisDirection==null?AxisDirection.POSITIVE: axisDirection;
		if(this.axisDirection!=resolved)
		{
			this.axisDirection = resolved;
			invalidatePath();
		}
		return this;
	}

	public void setProgress(float progress)
	{
		this.progress = progress;
	}

	@Override
	public void applyProperties(EasyNBT nbt)
	{
		super.applyProperties(nbt);
		nbt.checkSetFloat("progress", this::setProgress);
		nbt.checkSetString("track_direction", axis -> withTrackAxis("z".equalsIgnoreCase(axis)?Axis.Z: Axis.X));
		nbt.checkSetString("axis_direction", direction -> withAxisDirection("negative".equalsIgnoreCase(direction)||"NEGATIVE".equals(direction)?AxisDirection.NEGATIVE: AxisDirection.POSITIVE));
		if(nbt.hasKey("nodes"))
		{
			clearNodes();
			nbt.streamList(NBTTagCompound.class, "nodes").forEach(tag -> {
				NBTTagList list = tag.getTagList("position", EasyNBT.TAG_DOUBLE);
				float x = (float)list.getDoubleAt(0);
				float y = (float)list.getDoubleAt(1);
				if("wheel".equalsIgnoreCase(tag.getString("type")))
				{
					Wheel wheel = addWheel(new Vec2f(x, y), tag.getDouble("radius")/16f, tag.getDouble("max_compression")/16f);
					if(tag.hasKey("compression"))
						wheel.setCompression(tag.getFloat("compression"));
				}
				else
					withNode(x, y, tag.getDouble("direction"));
			});
		}
	}

	//---Drawing---//

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		if(segment==null||nodes.size() < 2||segmentLength <= EPSILON)
			return;

		PathCache path = getPath();
		if(path==null||path.segments <= 0||path.position==null||path.rotation==null)
			return;

		GlStateManager.translate(0.5, 0.5, 0.5);
		for(int i = 0; i < path.segments; i++)
			drawSegment(tes, buf, path, i);
	}

	@Override
	protected AMT renamedCopy(String newName)
	{
		AMTChain clone = new AMTChain(newName, originPos);
		for(ChainNode node : nodes)
		{
			ChainNode copied = node.copy();
			clone.nodes.add(copied);
			if(copied.wheel)
				clone.wheels.add(clone.new Wheel(copied));
		}
		if(segment!=null)
			clone.segment = new AMTQuads("nucular_chargis", segment.originPos, segment.quads);
		clone.segmentLength = segmentLength;
		clone.progress = progress;
		clone.trackAxis = trackAxis;
		clone.axisDirection = axisDirection;
		return clone;
	}

	@Override
	public void disposeOf()
	{
		if(segment!=null)
			AMTUtils.disposeOf(segment);
		segment = null;
		invalidatePath();
	}

	//---Internal Utils---//

	private void drawSegment(Tessellator tes, BufferBuilder buf, PathCache path, int index)
	{
		float time = progress+(index*path.segmentOffset);
		time -= MathHelper.floor(time);
		Vec3d translate = path.position.getForTime(time);
		Vec3d rotate = path.rotation.getForTime(time);

		GlStateManager.pushMatrix();
		GlStateManager.translate(translate.x, translate.y, translate.z);
		if(trackAxis==Axis.Z)
		{
			GlStateManager.rotate((float)rotate.y, 0, 1, 0);
			GlStateManager.rotate((float)-rotate.x, 1, 0, 0);
		}
		else
			GlStateManager.rotate((float)rotate.z, 0, 0, 1);
		segment.draw(tes, buf);
		GlStateManager.popMatrix();
	}

	private PathCache getPath()
	{
		int hash = pathHash();
		if(activePath!=null&&activePath.hash==hash)
			return activePath;

		activePath = pathCache.get(hash);
		if(activePath==null)
		{
			activePath = hasWheels()?compileWheelPath(hash): compileLegacyPath(hash);
			if(activePath!=null)
				pathCache.put(hash, activePath);
		}
		return activePath;
	}

	private int pathHash()
	{
		int hash = Objects.hash(trackAxis, axisDirection, nodes.size(), Math.round(segmentLength*1000000f));
		for(ChainNode node : nodes)
			hash = 31*hash+Objects.hash(node.position.x, node.position.y, node.direction, node.radius, node.maxCompression, node.wheel, node.compressionBucket());
		return hash;
	}

	private boolean hasWheels()
	{
		for(ChainNode node : nodes)
			if(node.wheel)
				return true;
		return false;
	}

	private PathCache compileLegacyPath(int hash)
	{
		float totalDistance = 0;
		int size = nodes.size();
		Vec3d[] positions = new Vec3d[size+1];
		Vec3d[] rotations = new Vec3d[size+1];
		float[] times = new float[size+1];

		for(int n = 0; n < size; n++)
		{
			ChainNode node = nodes.get(n);
			ChainNode next = nodes.get((int)IIMath.positiveModulo(n-1, size));
			times[n] = distance(node.horizontal(), node.currentY(), next.horizontal(), next.currentY());
			positions[n] = renderPosition(node.horizontal(), node.currentY());
			rotations[n] = renderRotation(node.direction);
			totalDistance += times[n];
			times[n] = totalDistance;
		}
		if(totalDistance <= EPSILON)
			return null;

		positions[size] = positions[0];
		rotations[size] = rotations[0].scale(-1);
		times[size] = 1;
		for(int i = size-1; i >= 0; i--)
			times[i] = (times[i]-times[0])/totalDistance;
		return finishCompilation(hash, totalDistance, times, positions, rotations);
	}

	private PathCache compileWheelPath(int hash)
	{
		PathSolution solution = solveWheelEnvelope(nodes);
		if(solution==null||solution.nodes.size() < 2)
			return null;

		ArrayList<PathPoint> path = new ArrayList<>();
		for(int i = 0; i < solution.nodes.size(); i++)
		{
			ChainNode node = solution.nodes.get(i);
			if(!node.wheel||node.radius <= EPSILON)
			{
				addPathPoint(path, new PathPoint(node.horizontal(), node.currentY(), node.direction));
				continue;
			}
			appendWheelArc(path, node, solution.previousTangent(i).end, solution.tangents[i].start, solution.winding, solution.nodes.size() <= 2?TWO_PI+EPSILON: MAX_CONTACT_ARC);
		}
		return compilePathPoints(hash, path);
	}

	private PathSolution solveWheelEnvelope(List<ChainNode> sourceNodes)
	{
		ArrayList<ChainNode> working = canonicalizeWheelOrder(sourceNodes);
		for(int iteration = 0; iteration < Math.max(1, working.size()*2)&&working.size() >= 2; iteration++)
		{
			int winding = -1, invalidEdge = -1;
			Tangent[] tangents = new Tangent[working.size()];
			for(int i = 0; i < working.size(); i++)
			{
				tangents[i] = calculateExternalTangent(working.get(i), working.get((i+1)%working.size()), -winding);
				if(!tangents[i].valid||(working.size() > 2&&tangents[i].spanLength <= EPSILON))
				{
					invalidEdge = i;
					break;
				}
			}

			if(invalidEdge >= 0)
			{
				if(working.size() <= 2)
					return null;
				working.remove(chooseDegenerateNode(working, invalidEdge));
				working = canonicalizeWheelOrder(working);
				continue;
			}

			int rejectedWheel = findOverwrappedWheel(working, tangents, winding);
			if(rejectedWheel >= 0)
			{
				working.remove(rejectedWheel);
				working = canonicalizeWheelOrder(working);
				continue;
			}
			return new PathSolution(working, tangents, winding);
		}
		return null;
	}

	private int findOverwrappedWheel(ArrayList<ChainNode> working, Tangent[] tangents, int winding)
	{
		if(working.size() <= 2)
			return -1;

		int rejectedWheel = -1;
		double rejectedSweep = MAX_CONTACT_ARC;
		for(int i = 0; i < working.size(); i++)
		{
			double sweep = wheelSweep(working, tangents, i, winding);
			if(sweep > rejectedSweep)
			{
				rejectedWheel = i;
				rejectedSweep = sweep;
			}
		}

		//Prefer keeping the first wheel stable unless another wheel is just as invalid.
		if(rejectedWheel==0)
			for(int i = 1; i < working.size(); i++)
			{
				double sweep = wheelSweep(working, tangents, i, winding);
				if(sweep > MAX_CONTACT_ARC&&sweep >= rejectedSweep-1e-4d)
				{
					rejectedWheel = i;
					rejectedSweep = sweep;
				}
			}
		return rejectedWheel;
	}

	private double wheelSweep(ArrayList<ChainNode> nodes, Tangent[] tangents, int index, int winding)
	{
		ChainNode node = nodes.get(index);
		return !node.wheel||node.radius <= EPSILON?0: Math.abs(wheelArc(node, tangents[(index-1+nodes.size())%nodes.size()].end, tangents[index].start, winding)[1]);
	}

	private ArrayList<ChainNode> canonicalizeWheelOrder(List<ChainNode> sourceNodes)
	{
		ArrayList<ChainNode> ordered = rotateToFirstWheel(sourceNodes);
		if(ordered.size() <= 2)
			return ordered;

		double first = ordered.get(0).horizontal();
		int forwardSign = axisDirection==AxisDirection.POSITIVE?1: -1;
		for(int i = 1; i < ordered.size(); i++)
		{
			double delta = ordered.get(i).horizontal()-first;
			if(Math.abs(delta) <= EPSILON)
				continue;
			return delta*forwardSign > EPSILON?reverseKeepingFirst(ordered): ordered;
		}
		return ordered;
	}

	private static ArrayList<ChainNode> rotateToFirstWheel(List<ChainNode> sourceNodes)
	{
		ArrayList<ChainNode> ordered = new ArrayList<>(sourceNodes);
		for(int i = 0; i < ordered.size(); i++)
			if(ordered.get(i).wheel)
				return rotate(ordered, i);
		return ordered;
	}

	private static ArrayList<ChainNode> rotate(ArrayList<ChainNode> source, int offset)
	{
		if(offset <= 0)
			return source;
		ArrayList<ChainNode> rotated = new ArrayList<>(source.size());
		for(int i = 0; i < source.size(); i++)
			rotated.add(source.get((offset+i)%source.size()));
		return rotated;
	}

	private static ArrayList<ChainNode> reverseKeepingFirst(ArrayList<ChainNode> source)
	{
		ArrayList<ChainNode> reversed = new ArrayList<>(source.size());
		reversed.add(source.get(0));
		for(int i = source.size()-1; i >= 1; i--)
			reversed.add(source.get(i));
		return reversed;
	}

	private static int chooseDegenerateNode(List<ChainNode> nodes, int edgeIndex)
	{
		int nextIndex = (edgeIndex+1)%nodes.size();
		ChainNode first = nodes.get(edgeIndex), second = nodes.get(nextIndex);
		if(!first.wheel&&!second.wheel) return nextIndex;
		if(!first.wheel) return edgeIndex;
		if(!second.wheel) return nextIndex;
		if(edgeIndex==0) return nextIndex;
		if(nextIndex==0) return edgeIndex;
		return Math.abs(first.radius-second.radius) > EPSILON?(first.radius < second.radius?edgeIndex: nextIndex): nextIndex;
	}

	private void appendWheelArc(List<PathPoint> path, ChainNode wheel, Vec2f incoming, Vec2f outgoing, int winding, double maxContactArc)
	{
		double[] arc = wheelArc(wheel, incoming, outgoing, winding);
		if(Math.abs(arc[1]) <= EPSILON||Math.abs(arc[1]) > maxContactArc)
			return;

		int steps = arcSteps(arc[1], wheel.radius);
		for(int step = 0; step <= steps; step++)
		{
			double angle = arc[0]+arc[1]*(step/(double)steps);
			double tangentX = -Math.sin(angle)*winding;
			double tangentY = Math.cos(angle)*winding;
			addPathPoint(path, new PathPoint(
					wheel.horizontal()+Math.cos(angle)*wheel.radius,
					wheel.currentY()+Math.sin(angle)*wheel.radius,
					Math.toDegrees(Math.atan2(tangentY, tangentX))
			));
		}
	}

	private int arcSteps(double sweep, double radius)
	{
		double arcLength = Math.abs(sweep)*radius;
		int angleSteps = (int)Math.ceil(Math.abs(sweep)/MAX_ARC_STEP);
		int lengthSteps = (int)Math.ceil(arcLength/Math.max(segmentLength*0.5d, 1d/64d));
		return MathHelper.clamp(Math.max(angleSteps, lengthSteps), 2, 96);
	}

	private double[] wheelArc(ChainNode wheel, Vec2f incoming, Vec2f outgoing, int winding)
	{
		double startAngle = Math.atan2(incoming.y-wheel.currentY(), incoming.x-wheel.horizontal());
		double endAngle = Math.atan2(outgoing.y-wheel.currentY(), outgoing.x-wheel.horizontal());
		double delta = endAngle-startAngle;
		if(winding > 0)
			while(delta < 0)
				delta += TWO_PI;
		else
			while(delta > 0)
				delta -= TWO_PI;
		return new double[]{startAngle, delta};
	}

	private PathCache compilePathPoints(int hash, List<PathPoint> path)
	{
		if(path.size() < 2)
			return null;

		double totalDistance = pathDistance(path);
		if(totalDistance <= EPSILON)
			return null;

		Vec3d[] positions = new Vec3d[path.size()+1];
		Vec3d[] rotations = new Vec3d[path.size()+1];
		float[] times = new float[path.size()+1];
		double travelled = 0;
		for(int i = 0; i < path.size(); i++)
		{
			PathPoint point = path.get(i);
			positions[i] = renderPosition(point.x, point.y);
			rotations[i] = renderRotation(point.direction);
			times[i] = (float)(travelled/totalDistance);
			PathPoint next = path.get((i+1)%path.size());
			travelled += distance(point.x, point.y, next.x, next.y);
		}
		positions[path.size()] = positions[0];
		rotations[path.size()] = rotations[0];
		times[path.size()] = 1f;
		return finishCompilation(hash, (float)totalDistance, times, positions, rotations);
	}

	private static double pathDistance(List<PathPoint> path)
	{
		double totalDistance = 0;
		for(int i = 0; i < path.size(); i++)
			totalDistance += distance(path.get(i).x, path.get(i).y, path.get((i+1)%path.size()).x, path.get((i+1)%path.size()).y);
		return totalDistance;
	}

	private Vec3d renderPosition(double horizontal, double y)
	{
		return new Vec3d(horizontal, y, 0);
	}

	private Vec3d renderRotation(double direction)
	{
		return new Vec3d(0, 0, direction);
	}

	private PathCache finishCompilation(int hash, float totalDistance, float[] times, Vec3d[] positions, Vec3d[] rotations)
	{
		int segments = Math.max(1, MathHelper.floor(totalDistance/segmentLength));
		return new PathCache(hash, new IIVectorLine(times, positions), new IIVectorLine(times, rotations), segments, 1f/segments);
	}

	private Tangent calculateExternalTangent(ChainNode a, ChainNode b, int side)
	{
		double ax = a.horizontal(), ay = a.currentY(), bx = b.horizontal(), by = b.currentY();
		double dx = bx-ax, dy = by-ay, distanceSquared = dx*dx+dy*dy, radiusDifference = a.radius-b.radius;
		double tangentSquared = distanceSquared-radiusDifference*radiusDifference;
		if(distanceSquared <= EPSILON||tangentSquared < -EPSILON)
			return Tangent.invalid();

		double tangentLength = Math.sqrt(Math.max(0, tangentSquared));
		double nx = (dx*radiusDifference-dy*tangentLength*side)/distanceSquared;
		double ny = (dy*radiusDifference+dx*tangentLength*side)/distanceSquared;
		return new Tangent(new Vec2f((float)(ax+nx*a.radius), (float)(ay+ny*a.radius)), new Vec2f((float)(bx+nx*b.radius), (float)(by+ny*b.radius)), tangentLength, true);
	}


	private static Vec3d readPathPosition(NBTTagCompound tag)
	{
		return new Vec3d(tag.getDouble("x")/16f, tag.getDouble("y")/16f, tag.getDouble("z")/16f);
	}

	private void invalidatePath()
	{
		activePath = null;
		pathCache.clear();
	}

	private static void addPathPoint(List<PathPoint> path, PathPoint point)
	{
		if(!path.isEmpty())
		{
			PathPoint previous = path.get(path.size()-1);
			if(distance(previous.x, previous.y, point.x, point.y) <= EPSILON)
			{
				previous.direction = point.direction;
				return;
			}
		}
		path.add(point);
	}

	private static float distance(double ax, double ay, double bx, double by)
	{
		double dx = bx-ax, dy = by-ay;
		return (float)Math.sqrt(dx*dx+dy*dy);
	}

	private static float measureSegmentLength(AMTQuads segment)
	{
		float minX = Float.MAX_VALUE, maxX = -Float.MAX_VALUE;
		for(BakedQuad quad : segment.quads)
			for(Vec3d vertex : IIClientUtils.extractVertexPositions(quad))
			{
				if(vertex.x < minX) minX = (float)vertex.x;
				if(vertex.x > maxX) maxX = (float)vertex.x;
			}
		return maxX-minX;
	}

	public final class Wheel
	{
		private final ChainNode node;

		private Wheel(ChainNode node)
		{
			this.node = node;
		}

		public float getCompression()
		{
			return node.compression;
		}

		public Wheel setCompression(float compression)
		{
			int oldBucket = node.compressionBucket();
			node.compression = MathHelper.clamp(compression, 0f, 1f);
			if(oldBucket!=node.compressionBucket())
				activePath = null;
			return this;
		}

		public double getRadius()
		{
			return node.radius;
		}

		public double getMaxCompression()
		{
			return node.maxCompression;
		}
	}

	private static final class ChainNode
	{
		private final Vec2f position;
		private final double direction, radius, maxCompression;
		private final boolean wheel;
		private float compression;

		private ChainNode(Vec2f position, double direction, double radius, double maxCompression, boolean wheel)
		{
			this.position = position;
			this.direction = direction;
			this.radius = radius;
			this.maxCompression = maxCompression;
			this.wheel = wheel;
		}

		private static ChainNode link(Vec2f position, double direction)
		{
			return new ChainNode(position, direction, 0, 0, false);
		}

		private static ChainNode wheel(Vec2f position, double radius, double maxCompression)
		{
			return new ChainNode(position, 0, radius, maxCompression, true);
		}

		private double horizontal()
		{
			return position.x;
		}

		private double currentY()
		{
			return position.y+(wheel?maxCompression*compression: 0);
		}

		private int compressionBucket()
		{
			return wheel?(int)Math.round(maxCompression*compression*4d): 0;
		}

		private ChainNode copy()
		{
			ChainNode copy = new ChainNode(position, direction, radius, maxCompression, wheel);
			copy.compression = compression;
			return copy;
		}
	}

	private static final class Tangent
	{
		private final Vec2f start, end;
		private final double spanLength;
		private final boolean valid;

		private Tangent(Vec2f start, Vec2f end, double spanLength, boolean valid)
		{
			this.start = start;
			this.end = end;
			this.spanLength = spanLength;
			this.valid = valid;
		}

		private static Tangent invalid()
		{
			return new Tangent(null, null, 0, false);
		}
	}

	private static final class PathSolution
	{
		private final ArrayList<ChainNode> nodes;
		private final Tangent[] tangents;
		private final int winding;

		private PathSolution(ArrayList<ChainNode> nodes, Tangent[] tangents, int winding)
		{
			this.nodes = nodes;
			this.tangents = tangents;
			this.winding = winding;
		}

		private Tangent previousTangent(int index)
		{
			return tangents[(index-1+nodes.size())%nodes.size()];
		}
	}

	private static final class PathPoint
	{
		private final double x, y;
		private double direction;

		private PathPoint(double x, double y, double direction)
		{
			this.x = x;
			this.y = y;
			this.direction = direction;
		}
	}

	private static final class PathCache
	{
		private final int hash, segments;
		private final IIVectorLine position, rotation;
		private final float segmentOffset;

		private PathCache(int hash, IIVectorLine position, IIVectorLine rotation, int segments, float segmentOffset)
		{
			this.hash = hash;
			this.position = position;
			this.rotation = rotation;
			this.segments = segments;
			this.segmentOffset = segmentOffset;
		}
	}
}
