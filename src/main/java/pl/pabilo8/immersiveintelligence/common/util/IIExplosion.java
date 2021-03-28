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
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageExplosion;

import java.util.List;
import java.util.Set;

/**
 * @author Pabilo8
 * @since 25.12.2020
 */
public class IIExplosion extends Explosion
{
	private final float power;

	public IIExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, float power, boolean flaming, boolean damagesTerrain)
	{
		super(worldIn, entityIn, x, y, z, size, flaming, damagesTerrain);
		this.power = power;
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
		{
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
						{
							d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase)entity, d10);
						}

						entity.motionX += d5*d11;
						entity.motionY += d7*d11;
						entity.motionZ += d9*d11;

						if(entity instanceof EntityPlayer)
						{
							EntityPlayer entityplayer = (EntityPlayer)entity;

							if(!entityplayer.isSpectator()&&(!entityplayer.isCreative()||!entityplayer.capabilities.isFlying))
							{
								this.playerKnockbackMap.put(entityplayer, new Vec3d(d5*d10, d7*d10, d9*d10));
							}
						}
					}
				}
			}
		}
	}

	public Set<BlockPos> generateAffectedBlockPositions()
	{
		Set<BlockPos> set = Sets.newHashSet();

		for(int j = 0; j < 16; ++j)
		{
			for(int k = 0; k < 16; ++k)
			{
				for(int l = 0; l < 16; ++l)
				{
					if(j==0||j==15||k==0||k==15||l==0||l==15)
					{
						double d0 = (float)j/15.0F*2.0F-1.0F;
						double d1 = (float)k/15.0F*2.0F-1.0F;
						double d2 = (float)l/15.0F*2.0F-1.0F;
						double d3 = Math.sqrt(d0*d0+d1*d1+d2*d2);
						d0 = d0/d3;
						d1 = d1/d3;
						d2 = d2/d3;
						float f = this.size*(0.7F+this.world.rand.nextFloat()*0.6F);
						double d4 = this.x;
						double d6 = this.y;
						double d8 = this.z;

						while(f > 0.0F)
						{
							BlockPos blockpos = new BlockPos(d4, d6, d8);
							IBlockState iblockstate = this.world.getBlockState(blockpos);

							if(iblockstate.getMaterial()!=Material.AIR)
							{
								float f2 = this.exploder!=null?this.exploder.getExplosionResistance(this, this.world, blockpos, iblockstate): iblockstate.getBlock().getExplosionResistance(world, blockpos, null, this);
								f -= (f2+0.3F)*0.3F;

								if(f > 0.0F&&(this.exploder==null||this.exploder.canExplosionDestroyBlock(this, this.world, blockpos, iblockstate, f)))
								{
									set.add(blockpos);
								}

							}
							d4 += d0*0.30000001192092896D;
							d6 += d1*0.30000001192092896D;
							d8 += d2*0.30000001192092896D;
							f -= 1;
						}
					}
				}
			}
		}
		return set;
	}

	@Override
	public void doExplosionB(boolean spawnParticles)
	{
		this.world.playSound(this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 4.0F, (1.0F+(Utils.RAND.nextFloat()-Utils.RAND.nextFloat())*0.2F)*0.7F, true);

		if(spawnParticles)
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageExplosion(this.causesFire, this.damagesTerrain, this.size, this.power, this.x, this.y, this.z), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromPos(getPos(), world, (int)(32+size)));


		if(this.damagesTerrain)
		{
			for(BlockPos blockpos : this.affectedBlockPositions)
			{
				IBlockState iblockstate = this.world.getBlockState(blockpos);
				Block block = iblockstate.getBlock();

				if(iblockstate.getMaterial()!=Material.AIR)
				{
					if(block.canDropFromExplosion(this))
					{
						block.dropBlockAsItemWithChance(this.world, blockpos, this.world.getBlockState(blockpos), 1.0F/this.size, 0);
					}

					block.onBlockExploded(this.world, blockpos, this);
				}
			}
		}

		if(this.causesFire)
		{
			for(BlockPos blockpos1 : this.affectedBlockPositions)
			{
				if(this.world.getBlockState(blockpos1).getMaterial()==Material.AIR&&this.world.getBlockState(blockpos1.down()).isFullBlock()&&this.random.nextInt(3)==0)
				{
					this.world.setBlockState(blockpos1, Blocks.FIRE.getDefaultState());
				}
			}
		}
	}

	private BlockPos getPos()
	{
		return new BlockPos(this.x, this.y, this.z);
	}
}
