package pl.pabilo8.immersiveintelligence.common.util;

import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageExplosion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector4f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Pabilo8
 * @updated 03.04.2024
 * @ii-approved 0.3.1
 * @since 25.12.2020
 */
//TODO: 04.04.2024 use blast resistance instead of hardness
public class IIExplosion extends Explosion
{
	/**
	 * The loss of energy for a explosion line trace
	 */
	private static final float LOSS = 0.3F*0.75F*5;
	@Nonnull
	private final Vec3d center, direction;
	private final ComponentEffectShape shape;
	private final float power;
	private final boolean doDrops;

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
	}

	@Override
	public void doExplosionA()
	{
		this.affectedBlockPositions.addAll(generateAffectedBlockPositions(false));
		float diameter = this.size*2.0F;
		int k1 = MathHelper.floor(this.x-(double)diameter-1.0D);
		int l1 = MathHelper.floor(this.x+(double)diameter+1.0D);
		int i2 = MathHelper.floor(this.y-(double)diameter-1.0D);
		int i1 = MathHelper.floor(this.y+(double)diameter+1.0D);
		int j2 = MathHelper.floor(this.z-(double)diameter-1.0D);
		int j1 = MathHelper.floor(this.z+(double)diameter+1.0D);
		List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB(k1, i2, j2, l1, i1, j1));
		ForgeEventFactory.onExplosionDetonate(this.world, this, list, diameter);
		Vec3d vec3d = new Vec3d(this.x, this.y, this.z);

		for(Entity entity : list)
			if(!entity.isDead&&!entity.isImmuneToExplosions())
			{
				double fragment = entity.getDistance(this.x, this.y, this.z)/(double)diameter;

				if(fragment <= 1.0D)
				{
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
						entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (float)((int)((reversed*reversed+reversed)/2.0D*7.0D*(double)diameter+1.0D)));
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
	}

	/**
	 * @return set of positions to be affected by this explosion
	 */
	public Set<BlockPos> generateAffectedBlockPositions(boolean getLastOnly)
	{
		Set<BlockPos> set;
		//Generate block positions based on shape
		switch(shape)
		{
			case LINE:
				set = generateLineBlockPos(getLastOnly);
				break;
			case CONE:
				set = generateConeBlockPos(getLastOnly, 1, 1f);
				break;
			case ORB:
				set = generateOrbBlockPos(getLastOnly, 1, 1f);
				break;
			default:
			case STAR:
				set = generateOrbBlockPos(getLastOnly, 0.35f, 0.9f);
				break;
		}
		return set;
	}

	//--- Explosion Shapes ---//

	/**
	 * Based on ICBM Classic explosion code.<br>
	 * Huge thanks to DarkGuardsman ^^
	 *
	 * @return blocks affected by an orb shaped explosion
	 */
	private Set<BlockPos> generateOrbBlockPos(boolean getLastOnly, float densityScale, float powerMultiplier)
	{
		ArrayList<BlockPos> set = new ArrayList<>();
		ArrayList<BlockPos> firsts = new ArrayList<>();
		//Steps per rotation
		final int steps = MathHelper.ceil(Math.PI*size*densityScale);
		float power, pitch, yaw;
		Vec3d current, direction;

		//REFACTOR: 03.04.2024 use matrix4
		for(int yawSlices = 0; yawSlices < 2*steps; yawSlices++)
			for(int pitchSlice = 0; pitchSlice < steps; pitchSlice++)
			{
				//Calculate power
				power = this.power*powerMultiplier-(this.size*world.rand.nextFloat()/2);
				//Get angles for rotation steps
				yaw = (float)((Math.PI/steps)*yawSlices);
				pitch = (float)((Math.PI/steps)*pitchSlice);

				//Figure out vector to move for trace (cut in half to improve trace skipping blocks)
				direction = new Vec3d(
						MathHelper.sin(pitch)*MathHelper.cos(yaw)*0.5,
						MathHelper.cos(pitch)*0.5,
						MathHelper.sin(pitch)*MathHelper.sin(yaw)*0.5
				);

				//Revert position to explosion center
				current = center;
				boolean atLeastOne = false;

				//Trace from start to end
				while(center.distanceTo(current) <= size&&power > 0)
				{
					//Consume power per loop
					power -= LOSS;

					//Convert double position to int position as block pos
					final BlockPos pos = new BlockPos(MathHelper.floor(current.x), MathHelper.floor(current.y), MathHelper.floor(current.z));

					//Stops from scanning the same position twice
					if(!set.contains(pos))
					{
						//Cannot destroy unloaded blocks
						if(!world.isBlockLoaded(pos))
							continue;
						if(canDestroyBlock(pos, power))
						{
							set.add(pos);
							atLeastOne = true;
						}
					}

					//Move forward
					current = current.add(direction);
				}

				//Add last block if it was not added before
				if(getLastOnly&&atLeastOne&&!firsts.contains(set.get(set.size()-1)))
					firsts.add(new BlockPos(set.get(set.size()-1)));
			}
		return Sets.newHashSet(getLastOnly&&!firsts.isEmpty()?firsts: set);
	}

	/**
	 * @return blocks affected by a line shaped explosion
	 */
	private Set<BlockPos> generateLineBlockPos(boolean getLastOnly)
	{
		ArrayList<BlockPos> set = new ArrayList<>();
		float power = this.power;
		for(float i = 0; i < size*1.25f; i += 0.5f, power -= LOSS)
		{
			BlockPos pos = new BlockPos(center.add(direction.scale(i)));
			if(!world.isBlockLoaded(pos))
				continue;
			if(canDestroyBlock(pos, power))
			{
				if(!set.contains(pos))
					set.add(pos);
			}
			else
				break;
		}
		return getLastOnly&&!set.isEmpty()?Collections.singleton(set.get(0)): Sets.newHashSet(set);
	}

	/**
	 * @return blocks affected by a cone shaped explosion
	 */
	private Set<BlockPos> generateConeBlockPos(boolean getLastOnly, float densityScale, float powerMultiplier)
	{
		ArrayList<BlockPos> set = new ArrayList<>();
		ArrayList<BlockPos> firsts = new ArrayList<>();

		//Steps per rotation
		final int steps = MathHelper.ceil(0.5f*size*densityScale);
		final float step = 0.5f/steps;
		float power;

		for(float pitch = -0.25f; pitch < 0.25f; pitch += step)
			for(float yaw = -0.25f; yaw < 0.25f; yaw += step)
			{
				Vec3d initial = rotateVector(direction, (float)(pitch*Math.PI), (float)(yaw*Math.PI));
				Vec3d current = this.center;
				power = this.power*powerMultiplier;

				while(this.center.distanceTo(current) <= size&&power > 0)
				{
					//Consume power per loop
					power -= LOSS;

					//Convert double position to int position as block pos
					final BlockPos pos = new BlockPos(MathHelper.floor(current.x), MathHelper.floor(current.y), MathHelper.floor(current.z));

					//Stops from scanning the same position twice
					if(!set.contains(pos))
					{
						//Cannot destroy unloaded blocks
						if(!world.isBlockLoaded(pos))
							continue;
						if(canDestroyBlock(pos, power))
							set.add(pos);
					}

					//Move forward
					current = current.add(initial);
				}
				if(getLastOnly&&!current.equals(center)&&!firsts.contains(new BlockPos(current)))
					firsts.add(new BlockPos(current));
			}
		return Sets.newHashSet(getLastOnly&&!firsts.isEmpty()?firsts: set);
	}

	public Vec3d rotateVector(Vec3d vec, float pitch, float yaw)
	{
		// Create rotation matrices for pitch and yaw
		Matrix4f pitchRotation = new Matrix4f();
		pitchRotation.setIdentity();
		pitchRotation.setRotation(new AxisAngle4f(1, 0, 0, pitch));
		Matrix4f yawRotation = new Matrix4f();
		yawRotation.setIdentity();
		yawRotation.setRotation(new AxisAngle4f(0, 1, 0, yaw));

		// Apply pitch and yaw rotations
		Vector4f rotatedVector = new Vector4f((float)vec.x, (float)vec.y, (float)vec.z, 1);
		pitchRotation.transform(rotatedVector);
		yawRotation.transform(rotatedVector);

		// Return the rotated vector
		return new Vec3d(rotatedVector.x, rotatedVector.y, rotatedVector.z).normalize();
	}

	@Override
	public void doExplosionB(boolean spawnParticles)
	{
		float pitch = (1.0F+(Utils.RAND.nextFloat()-Utils.RAND.nextFloat())*0.2F)*0.7F;
		//play explosion sound
		IIPacketHandler.playRangedSound(world, getPosition(),
				causesFire?IISounds.explosionIncendiary: IISounds.explosion,
				SoundCategory.NEUTRAL, (int)(72*size), 1f, pitch);

		if(spawnParticles)
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageExplosion(this.causesFire, this.damagesTerrain, this.size, this.power, center, direction, shape), IIPacketHandler.targetPointFromPos(getPos(), world, (int)(64+size)));

		if(this.damagesTerrain)
			for(BlockPos blockpos : this.affectedBlockPositions)
			{
				IBlockState iblockstate = this.world.getBlockState(blockpos);
				Block block = iblockstate.getBlock();

				if(iblockstate.getMaterial()!=Material.AIR)
				{
					if(doDrops&&block.canDropFromExplosion(this))
						block.dropBlockAsItemWithChance(this.world, blockpos, this.world.getBlockState(blockpos), 1.0F/this.size, 0);

					block.onBlockExploded(this.world, blockpos, this);
				}
			}

		if(this.causesFire)
			for(BlockPos blockpos1 : this.affectedBlockPositions)
				if(this.world.getBlockState(blockpos1).getMaterial()==Material.AIR&&this.world.getBlockState(blockpos1.down()).isFullBlock()&&this.random.nextInt(3)==0)
					this.world.setBlockState(blockpos1, Blocks.FIRE.getDefaultState());
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
		//Get block state from position
		IBlockState state = world.getBlockState(pos);

		//Ignore air blocks && Only break block that can be broken
		if(!state.getBlock().isAir(state, world, pos)&&power >= state.getBlock().getExplosionResistance(world, pos, exploder, this))
			return power > 0.0F&&(this.exploder==null||this.exploder.canExplosionDestroyBlock(this, this.world, pos, state, power));
		//Block cannot be destroyed
		return false;
	}

	private BlockPos getPos()
	{
		return new BlockPos(this.x, this.y, this.z);
	}

	public double getPower()
	{
		return power;
	}

	public double getSize()
	{
		return size;
	}
}
