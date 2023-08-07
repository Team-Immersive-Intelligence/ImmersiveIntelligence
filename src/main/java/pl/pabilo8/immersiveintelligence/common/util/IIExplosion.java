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
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageExplosion;

import java.util.List;
import java.util.Set;

/**
 * @author Pabilo8
 * @since 25.12.2020
 */
public class IIExplosion extends Explosion
{
	/**
	 * The loss of energy for a explosion line trace
	 */
	private static final float LOSS = 0.3F*0.75F*5;
	private final float power;
	private final boolean doDrops;

	public IIExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, float power, boolean flaming, boolean damagesTerrain, boolean doDrops)
	{
		super(worldIn, entityIn, x, y, z, size, flaming, damagesTerrain);
		this.power = power;
		this.doDrops = doDrops;
	}

	public IIExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, float power, boolean flaming, boolean damagesTerrain)
	{
		this(worldIn, entityIn, x, y, z, size, power, flaming, damagesTerrain, true);
	}

	public IIExplosion(World worldIn, Entity entityIn, Vec3d pos, float size, float power, boolean flaming, boolean damagesTerrain, boolean doDrops)
	{
		this(worldIn, entityIn, pos.x, pos.y, pos.z, size, power, flaming, damagesTerrain, doDrops);
	}

	public IIExplosion(World worldIn, Entity entityIn, Vec3d pos, float size, float power, boolean flaming, boolean damagesTerrain)
	{
		this(worldIn, entityIn, pos.x, pos.y, pos.z, size, power, flaming, damagesTerrain);
	}

	@Override
	public void doExplosionA()
	{
		this.affectedBlockPositions.addAll(generateAffectedBlockPositions());
		float f3 = this.size*2.0F;
		int k1 = MathHelper.floor(this.x-(double)f3-1.0D);
		int l1 = MathHelper.floor(this.x+(double)f3+1.0D);
		int i2 = MathHelper.floor(this.y-(double)f3-1.0D);
		int i1 = MathHelper.floor(this.y+(double)f3+1.0D);
		int j2 = MathHelper.floor(this.z-(double)f3-1.0D);
		int j1 = MathHelper.floor(this.z+(double)f3+1.0D);
		List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB(k1, i2, j2, l1, i1, j1));
		net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.world, this, list, f3);
		Vec3d vec3d = new Vec3d(this.x, this.y, this.z);

		for(Entity entity : list)
			if(!entity.isDead&&!entity.isImmuneToExplosions())
			{
				double d12 = entity.getDistance(this.x, this.y, this.z)/(double)f3;

				if(d12 <= 1.0D)
				{
					double d5 = entity.posX-this.x;
					double d7 = entity.posY+(double)entity.getEyeHeight()-this.y;
					double d9 = entity.posZ-this.z;
					double d13 = MathHelper.sqrt(d5*d5+d7*d7+d9*d9);

					if(d13!=0.0D)
					{
						d5 = d5/d13;
						d7 = d7/d13;
						d9 = d9/d13;
						double d14 = this.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
						double d10 = (1.0D-d12)*d14;
						entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (float)((int)((d10*d10+d10)/2.0D*7.0D*(double)f3+1.0D)));
						double d11 = d10;

						if(entity instanceof EntityLivingBase)
							d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase)entity, d10);

						entity.motionX += d5*d11;
						entity.motionY += d7*d11;
						entity.motionZ += d9*d11;

						if(entity instanceof EntityPlayer)
						{
							EntityPlayer entityplayer = (EntityPlayer)entity;

							if(!entityplayer.isSpectator()&&(!entityplayer.isCreative()||!entityplayer.capabilities.isFlying))
								this.playerKnockbackMap.put(entityplayer, new Vec3d(d5*d10, d7*d10, d9*d10));
						}
					}
				}
			}
	}

	/**
	 * Based on ICBM Classic explosion code.<br>
	 * Huge thanks to DarkGuardsman ^^
	 *
	 * @return set of positions to be affected by explosion
	 */
	public Set<BlockPos> generateAffectedBlockPositions()
	{
		float power, yaw, pitch;
		Set<BlockPos> set = Sets.newHashSet();

		//Steps per rotation
		final int steps = MathHelper.ceil(Math.PI*size);
		//Block center, currently scanned position, scan direction
		Vec3d center = getPosition(), current, direction;


		final int lineDensityScale = 2;
		for(int yawSlices = 0; yawSlices < lineDensityScale*steps; yawSlices++)
			for(int pitchSlice = 0; pitchSlice < steps; pitchSlice++)
			{
				//Calculate power
				power = this.power-(this.size*world.rand.nextFloat()/2);
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

						//Get block state and block from position
						final IBlockState state = world.getBlockState(pos);
						final Block block = state.getBlock();

						//Ignore air blocks && Only break block that can be broken
						if(!block.isAir(state, world, pos)&&state.getBlockHardness(world, pos) >= 0)
						{
							//Check if block can be destroyed
							if(power > 0.0F&&(this.exploder==null||this.exploder.canExplosionDestroyBlock(this, this.world, pos, state, power)))
								set.add(pos);
						}
					}

					//Move forward
					current = current.add(direction);
				}
			}

		return set;
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
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageExplosion(this.causesFire, this.damagesTerrain, this.size, this.power, getPosition()), IIPacketHandler.targetPointFromPos(getPos(), world, (int)(64+size)));

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
		if(!net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, this))
		{
			doExplosionA();
			doExplosionB(spawnParticles);
		}
		return this;
	}

	private BlockPos getPos()
	{
		return new BlockPos(this.x, this.y, this.z);
	}
}
