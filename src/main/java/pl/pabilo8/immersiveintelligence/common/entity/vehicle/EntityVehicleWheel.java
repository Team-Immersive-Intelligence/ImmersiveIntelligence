package pl.pabilo8.immersiveintelligence.common.entity.vehicle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;

/**
 * @author Pabilo8
 * @since 13.07.2020
 */
public class EntityVehicleWheel extends EntityVehiclePart
{
	public int wheelTraverse = 0;
	public EntityVehicleWheel(IVehicleMultiPart parent, String partName, Vec3d offset, AxisAlignedBB aabb)
	{
		super(parent, partName, offset, aabb);
		stepHeight = 0.75f;
	}

	public void travel(float strafe, float vertical, float forward, float gravity, double moveSpeed)
	{
		if(!this.isInWater())
		{
			if(!this.isInLava())
			{
				float f6 = 0.91F;
				BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(this.posX, this.getEntityBoundingBox().minY-1.0D, this.posZ);

				if(this.onGround)
				{
					IBlockState underState = this.world.getBlockState(blockpos$pooledmutableblockpos);
					f6 = underState.getBlock().getSlipperiness(underState, this.world, blockpos$pooledmutableblockpos, this)*0.91F;
				}

				float f7 = 0.16277136F/(f6*f6*f6);
				float f8;

				if(this.onGround)
				{
					f8 = (float)(moveSpeed*f7);
				}
				else
				{
					f8 = gravity;
				}

				this.moveRelative(strafe, vertical, forward, f8);
				f6 = 0.91F;

				if(this.onGround)
				{
					IBlockState underState = this.world.getBlockState(blockpos$pooledmutableblockpos.setPos(this.posX, this.getEntityBoundingBox().minY-1.0D, this.posZ));
					f6 = underState.getBlock().getSlipperiness(underState, this.world, blockpos$pooledmutableblockpos, this)*0.91F;
				}

				this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

				blockpos$pooledmutableblockpos.setPos(this.posX, 0.0D, this.posZ);

				if(!this.world.isRemote||this.world.isBlockLoaded(blockpos$pooledmutableblockpos)&&this.world.getChunkFromBlockCoords(blockpos$pooledmutableblockpos).isLoaded())
				{
					if(!this.hasNoGravity())
					{
						this.motionY -= 0.08D;
					}
				}
				else if(this.posY > 0.0D)
				{
					this.motionY = -0.1D;
				}
				else
				{
					this.motionY = 0.0D;
				}

				this.motionY *= 0.9800000190734863D;
				this.motionX *= f6;
				this.motionZ *= f6;
				blockpos$pooledmutableblockpos.release();

			}
			else
			{
				double d4 = this.posY;
				this.moveRelative(strafe, vertical, forward, 0.02F);
				this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
				this.motionX *= 0.5D;
				this.motionY *= 0.5D;
				this.motionZ *= 0.5D;

				if(!this.hasNoGravity())
				{
					this.motionY -= 0.02D;
				}

				if(this.collidedHorizontally&&this.isOffsetPositionInLiquid(this.motionX, this.motionY+0.6000000238418579D-this.posY+d4, this.motionZ))
				{
					this.motionY = 0.30000001192092896D;
				}
			}
		}
		else
		{
			double d0 = this.posY;
			float f1 = this.getWaterSlowDown();
			float f2 = 0.02F;
			float f3 = 1;

			if(!this.onGround)
			{
				f3 *= 0.5F;
			}

			if(f3 > 0.0F)
			{
				f1 += (0.54600006F-f1)*f3/3.0F;
				f2 += (moveSpeed-f2)*f3/3.0F;
			}

			this.moveRelative(strafe, vertical, forward, f2);
			this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
			this.motionX *= f1;
			this.motionY *= 0.800000011920929D;
			this.motionZ *= f1;

			if(!this.hasNoGravity())
			{
				this.motionY -= 0.02D;
			}

			if(this.collidedHorizontally&&this.isOffsetPositionInLiquid(this.motionX, this.motionY+0.6000000238418579D-this.posY+d0, this.motionZ))
			{
				this.motionY = 0.30000001192092896D;
			}
		}
	}

	private float getWaterSlowDown()
	{
		return 0.25f;
	}
}
