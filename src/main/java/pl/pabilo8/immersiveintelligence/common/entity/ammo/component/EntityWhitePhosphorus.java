package pl.pabilo8.immersiveintelligence.common.entity.ammo.component;

import blusunrize.immersiveengineering.common.entities.EntityIEProjectile;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 26.10.2019
 */
public class EntityWhitePhosphorus extends EntityIEProjectile implements IEntityAdditionalSpawnData
{
	public EntityWhitePhosphorus(World world)
	{
		super(world);
		setTickLimit(8);
	}

	public EntityWhitePhosphorus(World world, double x, double y, double z, double ax, double ay, double az)
	{
		super(world, x, y, z, ax, ay, az);
		setTickLimit(8);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
	}

	@Override
	public double getGravity()
	{
		return 0.004F;
	}

	/**
	 * Gets called every tick from main Entity class
	 */
	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();
	}

	/**
	 * Sets entity to burn for x scatter of seconds, cannot lower scatter of existing fire.
	 */
	@Override
	public void setFire(int seconds)
	{

	}

	@Override
	public void onImpact(RayTraceResult mop)
	{
		if(!this.world.isRemote&&mop.typeOfHit!=Type.MISS)
		{
			//Ignore other white phosphorus
			if(mop.typeOfHit==Type.ENTITY&&mop.entityHit instanceof EntityWhitePhosphorus)
				return;
			//Set hit entity to fire
			BlockPos hitPos = new BlockPos(mop.hitVec);
			world.playSound(null, hitPos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.125f, 1f);
			world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(hitPos).grow(0.5f))
					.forEach(entityLivingBase -> entityLivingBase.setFire(40));
			setDead();
		}
	}

	@Override
	protected boolean allowFriendlyFire(EntityPlayer target)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender()
	{
		return 15;
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness()
	{
		return 15;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		buffer.writeDouble(posX);
		buffer.writeDouble(posY);
		buffer.writeDouble(posZ);
		buffer.writeDouble(motionX);
		buffer.writeDouble(motionY);
		buffer.writeDouble(motionZ);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		posX = additionalData.readDouble();
		posY = additionalData.readDouble();
		posZ = additionalData.readDouble();
		motionX = additionalData.readDouble();
		motionY = additionalData.readDouble();
		motionZ = additionalData.readDouble();
	}
}
