package pl.pabilo8.immersiveintelligence.common.util;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.common.EventHandler;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageExplosion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.IntStream;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 03.04.2024
 * @ii-approved 0.3.1
 * @since 25.12.2020
 */
public class IIExplosion extends Explosion
{
	/**
	 * The loss of energy for a explosion line trace
	 */
	private static final float LOSS = 0.3F*0.75F*5;
	private static final float TRACE_STEP = 0.5F;
	private static final double TRACE_STEP_D = TRACE_STEP;
	private static final double ZERO_DIRECTION_EPSILON = 1.0E-7D;
	private static final float PARALLEL_TRACE_SIZE_THRESHOLD = 56f;
	private static final int PARALLEL_TRACE_RAY_THRESHOLD = 8192;
	private static final float PARTICLE_SURFACE_SAMPLE_SIZE_THRESHOLD = 8f;
	private static final int MAX_PARTICLE_SURFACE_SAMPLES = 256;

	private static final long POS_X_MASK = 0x3FFFFFFL;
	private static final long POS_Y_MASK = 0xFFFL;
	private static final long POS_Z_MASK = 0x3FFFFFFL;
	private static final int POS_Y_SHIFT = 26;
	private static final int POS_X_SHIFT = 38;

	@Nonnull
	private final Vec3d center, direction;
	private final ComponentEffectShape shape;
	private final float power;
	private final boolean doDrops;
	private int delay;

	public IIExplosion(World world, @Nonnull Entity exploder,
	                   Vec3d position, @Nullable Vec3d direction,
	                   float size, float power, ComponentEffectShape shape,
	                   boolean flaming, boolean damagesTerrain, boolean doDrops
	)
	{
		super(world, exploder, position.x, position.y, position.z, size, flaming, damagesTerrain);
		this.center = new Vec3d(x, y, z);
		this.direction = direction==null?Vec3d.ZERO: direction;
		this.shape = shape;
		this.power = power;
		this.doDrops = doDrops;
		this.delay = 2;
	}

	/**
	 * @param positions all block positions
	 * @param facing    facing to check for
	 * @return positions with highest axis value indicated by the facing
	 */
	public static Set<BlockPos> getTopBlocks(Set<BlockPos> positions, EnumFacing facing)
	{
		EnumFacing.Axis axis = facing.getAxis();
		EnumFacing.AxisDirection direction = facing.getAxisDirection();

		//Group positions by the other two axes, using a primitive long key to avoid Tuple churn.
		Map<Long, BlockPos> groupedPositions = new HashMap<>(positions.size());
		for(BlockPos pos : positions)
		{
			int primary = getAxisValue(pos, axis);
			int secondary1 = axis==EnumFacing.Axis.X?pos.getY(): pos.getX();
			int secondary2 = axis==EnumFacing.Axis.Z?pos.getY(): pos.getZ();
			long key = packTwoInts(secondary1, secondary2);

			BlockPos previous = groupedPositions.get(key);
			if(previous==null||
					(direction==EnumFacing.AxisDirection.POSITIVE&&primary > getAxisValue(previous, axis))||
					(direction==EnumFacing.AxisDirection.NEGATIVE&&primary < getAxisValue(previous, axis)))
				groupedPositions.put(key, pos);
		}

		return new HashSet<>(groupedPositions.values());
	}

	@Override
	public void doExplosionA()
	{
		this.affectedBlockPositions.addAll(generateAffectedBlockPositions());

		float diameter = this.size*2.0F;
		double diameterSq = diameter*diameter;
		int k1 = MathHelper.floor(this.x-(double)diameter-1.0D);
		int l1 = MathHelper.floor(this.x+(double)diameter+1.0D);
		int i2 = MathHelper.floor(this.y-(double)diameter-1.0D);
		int i1 = MathHelper.floor(this.y+(double)diameter+1.0D);
		int j2 = MathHelper.floor(this.z-(double)diameter-1.0D);
		int j1 = MathHelper.floor(this.z+(double)diameter+1.0D);

		List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB(k1, i2, j2, l1, i1, j1));
		ForgeEventFactory.onExplosionDetonate(this.world, this, list, diameter);
		Vec3d vec3d = new Vec3d(this.x, this.y, this.z);
		DamageSource explosionDamage = DamageSource.causeExplosionDamage(this);

		for(Entity entity : list)
			if(!entity.isDead&&!entity.isImmuneToExplosions())
			{
				double distanceSq = entity.getDistanceSq(this.x, this.y, this.z);
				if(distanceSq > diameterSq)
					continue;

				double fragment = MathHelper.sqrt(distanceSq)/(double)diameter;
				double xDiff = entity.posX-this.x;
				double yDiff = entity.posY+(double)entity.getEyeHeight()-this.y;
				double zDiff = entity.posZ-this.z;
				double dist = MathHelper.sqrt(xDiff*xDiff+yDiff*yDiff+zDiff*zDiff);

				if(dist!=0.0D)
				{
					xDiff = xDiff/dist;
					yDiff = yDiff/dist;
					zDiff = zDiff/dist;
					double blockDensity = this.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
					double reversed = (1.0D-fragment)*blockDensity;
					entity.attackEntityFrom(explosionDamage,
							(float)((int)((reversed*reversed+reversed)/2.0D*15.0D*power/2f*(double)diameter+1.0D)));
					double reversedTmp = reversed;

					if(entity instanceof EntityLivingBase)
						reversedTmp = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase)entity, reversed);

					entity.motionX += xDiff*reversedTmp;
					entity.motionY += yDiff*reversedTmp;
					entity.motionZ += zDiff*reversedTmp;

					if(entity instanceof EntityPlayer)
					{
						EntityPlayer entityplayer = (EntityPlayer)entity;

						if(!entityplayer.isSpectator()&&(!entityplayer.isCreative()||!entityplayer.capabilities.isFlying))
							this.playerKnockbackMap.put(entityplayer, new Vec3d(xDiff*reversed, yDiff*reversed, zDiff*reversed));
					}
				}
			}
	}

	/**
	 * @return set of positions to be affected by this explosion
	 */
	public Set<BlockPos> generateAffectedBlockPositions()
	{
		switch(shape)
		{
			case LINE:
				return generateLineBlockPos();
			case CONE:
				return generateConeBlockPos(1, 1f);
			case ORB:
				return generateOrbBlockPos(1, 1f);
			default:
			case STAR:
				return generateOrbBlockPos(0.35f, 0.9f);
		}
	}

	//--- Explosion Shapes ---//

	/**
	 * Based on ICBM Classic explosion code.<br>
	 * Huge thanks to DarkGuardsman ^^
	 *
	 * @return blocks affected by an orb shaped explosion
	 */
	private Set<BlockPos> generateOrbBlockPos(float densityScale, float powerMultiplier)
	{
		final int steps = Math.max(1, MathHelper.ceil((float)Math.PI*size*densityScale));
		final int maxTraceSteps = MathHelper.floor(size/TRACE_STEP);
		final float basePower = this.power*powerMultiplier;
		final float yawStep = (float)Math.PI/steps;
		final float pitchStep = (float)Math.PI/steps;

		final int yawCount = 2*steps;
		final int rayCount = yawCount*steps;

		if(shouldUseParallelTracing(rayCount))
			return generateOrbBlockPosParallel(steps, maxTraceSteps, basePower, yawStep, pitchStep);

		BlockTraceCollector collector = new BlockTraceCollector(estimateCollectorCapacity());

		for(int yawSlice = 0; yawSlice < yawCount; yawSlice++)
		{
			float yaw = yawStep*yawSlice;
			float cosYaw = MathHelper.cos(yaw);
			float sinYaw = MathHelper.sin(yaw);

			for(int pitchSlice = 0; pitchSlice < steps; pitchSlice++)
			{
				float pitch = pitchStep*pitchSlice;
				float sinPitch = MathHelper.sin(pitch);

				//Figure out vector to move for trace. The 0.5 step keeps the old ray density.
				double stepX = sinPitch*cosYaw*TRACE_STEP_D;
				double stepY = MathHelper.cos(pitch)*TRACE_STEP_D;
				double stepZ = sinPitch*sinYaw*TRACE_STEP_D;
				float rayPower = basePower-(this.size*world.rand.nextFloat()*0.5F);

				traceRay(collector, stepX, stepY, stepZ, maxTraceSteps, rayPower);
			}
		}

		return collector.toBlockPosSet();
	}

	/**
	 * @return blocks affected by a line shaped explosion
	 */
	private Set<BlockPos> generateLineBlockPos()
	{
		BlockTraceCollector collector = new BlockTraceCollector(Math.max(16, MathHelper.ceil(size*1.25f/TRACE_STEP)));
		Vec3d dir = getSafeDirection();
		double stepX = dir.x*TRACE_STEP_D;
		double stepY = dir.y*TRACE_STEP_D;
		double stepZ = dir.z*TRACE_STEP_D;
		int maxTraceSteps = MathHelper.floor(size*1.25f/TRACE_STEP);

		double currentX = center.x;
		double currentY = center.y;
		double currentZ = center.z;
		float tracePower = this.power;

		for(int i = 0; i <= maxTraceSteps&&tracePower > 0; i++, tracePower -= LOSS)
		{
			ExposionTraceResult result = collector.tryAdd(MathHelper.floor(currentX), MathHelper.floor(currentY), MathHelper.floor(currentZ), tracePower);
			if(result==ExposionTraceResult.BLOCKED)
				break;

			currentX += stepX;
			currentY += stepY;
			currentZ += stepZ;
		}

		return collector.toBlockPosSet();
	}

	/**
	 * @return blocks affected by a cone shaped explosion
	 */
	private Set<BlockPos> generateConeBlockPos(float densityScale, float powerMultiplier)
	{
		final int steps = Math.max(1, MathHelper.ceil(0.5f*size*densityScale));
		final int maxTraceSteps = MathHelper.floor(size);
		final float step = 0.5f/steps;
		final float basePower = this.power*powerMultiplier;
		final Vec3d dir = getSafeDirection();

		final int rayCount = steps*steps;
		if(shouldUseParallelTracing(rayCount))
			return generateConeBlockPosParallel(steps, maxTraceSteps, step, basePower, dir);

		BlockTraceCollector collector = new BlockTraceCollector(estimateCollectorCapacity()/2);

		for(float pitch = -0.25f; pitch < 0.25f; pitch += step)
			for(float yaw = -0.25f; yaw < 0.25f; yaw += step)
			{
				Vec3d initial = rotateVector(dir, (float)(pitch*Math.PI), (float)(yaw*Math.PI));
				traceRay(collector, initial.x, initial.y, initial.z, maxTraceSteps, basePower);
			}

		return collector.toBlockPosSet();
	}

	private Set<BlockPos> generateOrbBlockPosParallel(final int steps, final int maxTraceSteps, final float basePower,
	                                                  final float yawStep, final float pitchStep)
	{
		final int pitchCount = steps;
		final int rayCount = 2*steps*pitchCount;
		final float[] randomLosses = createOrbRandomLosses(rayCount);

		BlockPowerCollector collector = IntStream.range(0, rayCount).parallel().collect(
				this::createParallelCollector,
				(localCollector, rayIndex) -> {
					int yawSlice = rayIndex/pitchCount;
					int pitchSlice = rayIndex-yawSlice*pitchCount;

					float yaw = yawStep*yawSlice;
					float pitch = pitchStep*pitchSlice;
					float sinPitch = MathHelper.sin(pitch);

					double stepX = sinPitch*MathHelper.cos(yaw)*TRACE_STEP_D;
					double stepY = MathHelper.cos(pitch)*TRACE_STEP_D;
					double stepZ = sinPitch*MathHelper.sin(yaw)*TRACE_STEP_D;
					float rayPower = basePower-randomLosses[rayIndex];

					traceRayCandidates(localCollector, stepX, stepY, stepZ, maxTraceSteps, rayPower);
				},
				BlockPowerCollector::mergeFrom
		);

		return validateCandidateBlocks(collector);
	}

	private Set<BlockPos> generateConeBlockPosParallel(final int steps, final int maxTraceSteps, final float step,
	                                                   final float basePower, final Vec3d dir)
	{
		final int rayCount = steps*steps;

		BlockPowerCollector collector = IntStream.range(0, rayCount).parallel().collect(
				this::createParallelCollector,
				(localCollector, rayIndex) -> {
					int pitchIndex = rayIndex/steps;
					int yawIndex = rayIndex-pitchIndex*steps;

					float pitch = -0.25f+step*pitchIndex;
					float yaw = -0.25f+step*yawIndex;
					Vec3d initial = rotateVector(dir, (float)(pitch*Math.PI), (float)(yaw*Math.PI));

					traceRayCandidates(localCollector, initial.x, initial.y, initial.z, maxTraceSteps, basePower);
				},
				BlockPowerCollector::mergeFrom
		);

		return validateCandidateBlocks(collector);
	}

	private Vec3d rotateVector(Vec3d vec, float pitch, float yaw)
	{
		float cosPitch = MathHelper.cos(pitch);
		float sinPitch = MathHelper.sin(pitch);
		float cosYaw = MathHelper.cos(yaw);
		float sinYaw = MathHelper.sin(yaw);

		double xPitch = vec.x;
		double yPitch = vec.y*cosPitch-vec.z*sinPitch;
		double zPitch = vec.y*sinPitch+vec.z*cosPitch;

		double xYaw = xPitch*cosYaw+zPitch*sinYaw;
		double zYaw = -xPitch*sinYaw+zPitch*cosYaw;

		return normaliseOrZero(xYaw, yPitch, zYaw);
	}

	private void traceRay(BlockTraceCollector collector, double stepX, double stepY, double stepZ, int maxTraceSteps, float initialPower)
	{
		double currentX = center.x;
		double currentY = center.y;
		double currentZ = center.z;
		float tracePower = initialPower;

		for(int i = 0; i <= maxTraceSteps&&tracePower > 0; i++)
		{
			//Keep the old order: consume power before testing the block at the current step.
			tracePower -= LOSS;
			collector.tryAdd(MathHelper.floor(currentX), MathHelper.floor(currentY), MathHelper.floor(currentZ), tracePower);

			currentX += stepX;
			currentY += stepY;
			currentZ += stepZ;
		}
	}

	private void traceRayCandidates(BlockPowerCollector collector, double stepX, double stepY, double stepZ, int maxTraceSteps, float initialPower)
	{
		double currentX = center.x;
		double currentY = center.y;
		double currentZ = center.z;
		float tracePower = initialPower;

		for(int i = 0; i <= maxTraceSteps&&tracePower > 0; i++)
		{
			tracePower -= LOSS;
			if(tracePower > 0.0F)
				collector.record(MathHelper.floor(currentX), MathHelper.floor(currentY), MathHelper.floor(currentZ), tracePower);

			currentX += stepX;
			currentY += stepY;
			currentZ += stepZ;
		}
	}

	@Override
	public void doExplosionB(boolean spawnParticles)
	{
		float pitch = (1.0F+(Utils.RAND.nextFloat()-Utils.RAND.nextFloat())*0.2F)*0.7F;
		//play explosion sound
		IIPacketHandler.playRangedSound(world, getPosition(),
				causesFire?IISounds.explosionIncendiary: IISounds.explosion,
				SoundCategory.NEUTRAL, (int)(72+6*size), 1f, pitch);

		if(spawnParticles)
			IIPacketHandler.sendToClient(MessageExplosion.createExplosionMessage(
					this.world, this.causesFire, this.damagesTerrain, this.size, this.power,
					center, direction, shape,
					this.size > PARTICLE_SURFACE_SAMPLE_SIZE_THRESHOLD?
							getParticleEffectBlocks(MAX_PARTICLE_SURFACE_SAMPLES): Collections.emptyList()
			));

		EventHandler.pendingExplosions.add(this);
	}

	public boolean explodeBlocks()
	{
		if((delay--) >= 0)
			return false;

		if(this.damagesTerrain)
			for(BlockPos pos : this.affectedBlockPositions)
			{
				IBlockState iblockstate = this.world.getBlockState(pos);
				Block block = iblockstate.getBlock();

				if(iblockstate.getMaterial()!=Material.AIR)
				{
					if(doDrops&&block.canDropFromExplosion(this))
						block.dropBlockAsItemWithChance(this.world, pos, iblockstate, 1.0F/this.size, 0);

					block.onBlockExploded(this.world, pos, this);
				}
			}

		if(this.causesFire)
		{
			BlockPos.MutableBlockPos below = new BlockPos.MutableBlockPos();

			for(BlockPos blockpos1 : this.affectedBlockPositions)
			{
				IBlockState state = this.world.getBlockState(blockpos1);
				if(state.getMaterial()!=Material.AIR)
					continue;

				below.setPos(blockpos1.getX(), blockpos1.getY()-1, blockpos1.getZ());
				if(this.world.getBlockState(below).isFullBlock()&&this.random.nextInt(3)==0)
					this.world.setBlockState(blockpos1, Blocks.FIRE.getDefaultState());
			}
		}

		return true;
	}

	public IIExplosion doExplosion()
	{
		return doExplosion(true);
	}

	public IIExplosion doExplosion(boolean spawnParticles)
	{
		if(!ForgeEventFactory.onExplosionStart(world, this))
		{
			doExplosionA();
			doExplosionB(spawnParticles);
		}
		return this;
	}

	//--- Utilities ---//

	private boolean canDestroyBlock(BlockPos pos, float power)
	{
		if(power <= 0.0F)
			return false;

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		//Ignore air blocks && Only break block that can be broken
		return !block.isAir(state, world, pos)&&
				power >= block.getExplosionResistance(world, pos, exploder, this)&&
				(this.exploder==null||this.exploder.canExplosionDestroyBlock(this, this.world, pos, state, power));
	}

	private Vec3d getSafeDirection()
	{
		return normaliseOrZero(direction.x, direction.y, direction.z);
	}

	private static Vec3d normaliseOrZero(double x, double y, double z)
	{
		double lengthSq = x*x+y*y+z*z;
		if(lengthSq <= ZERO_DIRECTION_EPSILON)
			return Vec3d.ZERO;

		double invLength = MathHelper.fastInvSqrt(lengthSq);
		return new Vec3d(x*invLength, y*invLength, z*invLength);
	}

	private int estimateCollectorCapacity()
	{
		//A rough volume-based starting capacity. It is intentionally conservative, because HashMap resizing hurts less than allocating far too much for small explosions.
		return Math.max(64, MathHelper.floor(size*size*size*0.35F));
	}

	private boolean shouldUseParallelTracing(int rayCount)
	{
		return size > PARALLEL_TRACE_SIZE_THRESHOLD&&
				rayCount >= PARALLEL_TRACE_RAY_THRESHOLD&&
				Runtime.getRuntime().availableProcessors() > 1;
	}

	private BlockPowerCollector createParallelCollector()
	{
		int processors = Math.max(1, Runtime.getRuntime().availableProcessors());
		int capacity = estimateCollectorCapacity()/Math.max(4, processors*4);
		return new BlockPowerCollector(Math.max(256, Math.min(16384, capacity)));
	}

	private float[] createOrbRandomLosses(int rayCount)
	{
		float[] losses = new float[rayCount];
		for(int i = 0; i < rayCount; i++)
			losses[i] = this.size*world.rand.nextFloat()*0.5F;
		return losses;
	}

	private Set<BlockPos> validateCandidateBlocks(BlockPowerCollector collector)
	{
		Set<BlockPos> positions = new LinkedHashSet<>(collector.size());
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

		for(Map.Entry<Long, Float> entry : collector.strongestPower.entrySet())
		{
			long packed = entry.getKey();
			mutablePos.setPos(unpackX(packed), unpackY(packed), unpackZ(packed));

			if(!world.isBlockLoaded(mutablePos))
				continue;
			if(canDestroyBlock(mutablePos, entry.getValue()))
				positions.add(new BlockPos(mutablePos.getX(), mutablePos.getY(), mutablePos.getZ()));
		}

		return positions;
	}

	private static int getAxisValue(BlockPos pos, EnumFacing.Axis axis)
	{
		return axis==EnumFacing.Axis.X?pos.getX(): (axis==EnumFacing.Axis.Y?pos.getY(): pos.getZ());
	}

	private static long packTwoInts(int a, int b)
	{
		return ((long)a<<32)^(b&0xFFFFFFFFL);
	}

	private static long packPos(int x, int y, int z)
	{
		return ((long)x&POS_X_MASK)<<POS_X_SHIFT|
				((long)y&POS_Y_MASK)<<POS_Y_SHIFT|
				((long)z&POS_Z_MASK);
	}

	private static int unpackX(long packed)
	{
		return (int)(packed>>POS_X_SHIFT);
	}

	private static int unpackY(long packed)
	{
		int y = (int)((packed>>POS_Y_SHIFT)&POS_Y_MASK);
		return y >= 2048?y-4096: y;
	}

	private static int unpackZ(long packed)
	{
		return (int)(packed<<38>>38);
	}

	private BlockPos getPos()
	{
		return new BlockPos(this.x, this.y, this.z);
	}

	/**
	 * Creates a bounded, evenly distributed sample of the actual affected surface for client particles.
	 * The positions come from {@link #affectedBlockPositions}, so dust and debris cannot be placed
	 * beyond the blocks selected by the server-side explosion calculation.
	 */
	public List<BlockPos> getParticleEffectBlocks(int maxSamples)
	{
		if(maxSamples <= 0||this.affectedBlockPositions.isEmpty())
			return Collections.emptyList();

		double directionLengthSq = direction.x*direction.x+direction.y*direction.y+direction.z*direction.z;
		Vec3d visualDirection = directionLengthSq <= ZERO_DIRECTION_EPSILON?
				new Vec3d(0, 1, 0): direction.scale(-1).normalize();
		EnumFacing facing = EnumFacing.getFacingFromVector(
				(float)visualDirection.x,
				(float)visualDirection.y,
				(float)visualDirection.z
		);
		EnumFacing.Axis axis = facing.getAxis();
		EnumFacing.AxisDirection axisDirection = facing.getAxisDirection();

		int minSecondary1 = Integer.MAX_VALUE;
		int minSecondary2 = Integer.MAX_VALUE;
		int maxSecondary1 = Integer.MIN_VALUE;
		int maxSecondary2 = Integer.MIN_VALUE;

		for(BlockPos blockPos : this.affectedBlockPositions)
		{
			int secondary1 = axis==EnumFacing.Axis.X?blockPos.getY(): blockPos.getX();
			int secondary2 = axis==EnumFacing.Axis.Z?blockPos.getY(): blockPos.getZ();
			minSecondary1 = Math.min(minSecondary1, secondary1);
			minSecondary2 = Math.min(minSecondary2, secondary2);
			maxSecondary1 = Math.max(maxSecondary1, secondary1);
			maxSecondary2 = Math.max(maxSecondary2, secondary2);
		}

		long projectedArea = (long)(maxSecondary1-minSecondary1+1)*(maxSecondary2-minSecondary2+1);
		int bucketSize = Math.max(1, MathHelper.ceil((float)Math.sqrt(projectedArea/(double)maxSamples)));
		Map<Long, BlockPos> surfaceBuckets = new LinkedHashMap<>(maxSamples);

		for(BlockPos blockPos : this.affectedBlockPositions)
		{
			int primary = getAxisValue(blockPos, axis);
			int secondary1 = axis==EnumFacing.Axis.X?blockPos.getY(): blockPos.getX();
			int secondary2 = axis==EnumFacing.Axis.Z?blockPos.getY(): blockPos.getZ();
			long bucketKey = packTwoInts(
					Math.floorDiv(secondary1, bucketSize),
					Math.floorDiv(secondary2, bucketSize)
			);

			BlockPos previous = surfaceBuckets.get(bucketKey);
			if(previous==null||
					(axisDirection==EnumFacing.AxisDirection.POSITIVE&&primary > getAxisValue(previous, axis))||
					(axisDirection==EnumFacing.AxisDirection.NEGATIVE&&primary < getAxisValue(previous, axis)))
				surfaceBuckets.put(bucketKey, blockPos);
		}

		List<BlockPos> surface = new ArrayList<>(surfaceBuckets.values());
		if(surface.size() <= maxSamples)
			return surface;

		List<BlockPos> reduced = new ArrayList<>(maxSamples);
		float stride = surface.size()/(float)maxSamples;
		for(int i = 0; i < maxSamples; i++)
			reduced.add(surface.get(Math.min(surface.size()-1, MathHelper.floor(i*stride))));
		return reduced;
	}

	public double getPower()
	{
		return power;
	}

	public double getSize()
	{
		return size;
	}

	public ComponentEffectShape getShape()
	{
		return shape;
	}

	//--- Helper classes ---//

	private static class BlockPowerCollector
	{
		private final Map<Long, Float> strongestPower;

		private BlockPowerCollector(int expectedSize)
		{
			this.strongestPower = new HashMap<>(Math.max(16, expectedSize));
		}

		private void record(int x, int y, int z, float power)
		{
			long packed = packPos(x, y, z);
			Float previousPower = strongestPower.get(packed);
			if(previousPower==null||previousPower < power)
				strongestPower.put(packed, power);
		}

		private void mergeFrom(BlockPowerCollector other)
		{
			for(Map.Entry<Long, Float> entry : other.strongestPower.entrySet())
			{
				Float previousPower = strongestPower.get(entry.getKey());
				if(previousPower==null||previousPower < entry.getValue())
					strongestPower.put(entry.getKey(), entry.getValue());
			}
		}

		private int size()
		{
			return strongestPower.size();
		}
	}

	private class BlockTraceCollector
	{
		private final Set<Long> affectedBlocks;
		private final Map<Long, Float> strongestTestedPower;
		private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

		private BlockTraceCollector(int expectedSize)
		{
			int capacity = Math.max(16, expectedSize);
			this.affectedBlocks = new LinkedHashSet<>(capacity);
			this.strongestTestedPower = new HashMap<>(capacity);
		}

		private ExposionTraceResult tryAdd(int x, int y, int z, float power)
		{
			if(power <= 0.0F)
				return ExposionTraceResult.KNOWN;

			long packed = packPos(x, y, z);
			if(affectedBlocks.contains(packed))
				return ExposionTraceResult.KNOWN;

			Float previousPower = strongestTestedPower.get(packed);
			if(previousPower!=null&&previousPower >= power)
				return ExposionTraceResult.KNOWN;

			strongestTestedPower.put(packed, power);
			mutablePos.setPos(x, y, z);

			if(!world.isBlockLoaded(mutablePos))
				return ExposionTraceResult.UNLOADED;

			if(canDestroyBlock(mutablePos, power))
			{
				affectedBlocks.add(packed);
				return ExposionTraceResult.ADDED;
			}

			return ExposionTraceResult.BLOCKED;
		}

		private Set<BlockPos> toBlockPosSet()
		{
			Set<BlockPos> positions = new LinkedHashSet<>(affectedBlocks.size());
			for(Long packed : affectedBlocks)
				positions.add(BlockPos.fromLong(packed));
			return positions;
		}
	}

	private enum ExposionTraceResult
	{
		KNOWN,
		ADDED,
		UNLOADED,
		BLOCKED
	}
}
