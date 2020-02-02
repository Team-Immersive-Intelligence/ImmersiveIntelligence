package pl.pabilo8.immersiveintelligence.common.entity.bullets;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.common.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBullet;

import java.util.List;

/**
 * Created by Pabilo8 on 30-08-2019.
 * Yes, I stole this one from Flan's Mod too! (Thanks Flan!)
 * Also, I couldn't get the Immersive Engineering IEProjectile to work...
 * That's why I extend Entity
 */
public class EntityBullet extends Entity implements IEntityAdditionalSpawnData
{
	private static int fuse = 600; // Kill bullets after 30 seconds
	public EntityLivingBase owner;
	public ItemStack stack;
	public String name;
	public int colourCore = 0, colourPaint = 0, colourTrail = -1;
	private float size = 0f, penetrationPower = 0f, mass = 0f;
	@SideOnly(Side.CLIENT)
	private boolean playedFlybySound;

	public EntityBullet(World world)
	{
		super(world);
		setSize(0.5F, 0.5F);
		stack = ItemIIBullet.getAmmoStack(1, "artillery_8bCal", "CoreSteel", "TNT", "", 1f);
	}

	public EntityBullet(World world, double x, double y, double z, EntityLivingBase shooter, ItemStack stack)
	{
		this(world);
		owner = shooter;
		this.stack = stack.copy();
		setPosition(x, y, z);
		setParams();
		ItemIIBullet.getCasing(stack).doPuff(this);
		setEntityInvulnerable(false);
	}


	public float getSize()
	{
		return size;
	}

	public void setFuse(int fuse)
	{
		EntityBullet.fuse = fuse;
	}

	@Override
	protected void entityInit()
	{

	}

	private void setParams()
	{
		if(!stack.isEmpty()&&stack.getItem() instanceof ItemIIBullet)
		{
			this.mass = ItemIIBullet.getMass(stack);

			float first_pen = 0f, second_pen = 0f, core_pen = 0f;
			penetrationPower = ItemIIBullet.getCasing(stack).getPenetration();
			if(ItemIIBullet.hasCore(stack))
				core_pen = ItemIIBullet.getCore(stack).getPenetrationModifier(new NBTTagCompound());
			if(ItemIIBullet.hasFirstComponent(stack))
				first_pen = ItemIIBullet.getFirstComponent(stack).getPenetrationModifier(ItemIIBullet.getFirstComponentNBT(stack));
			if(ItemIIBullet.hasSecondComponent(stack))
				second_pen = ItemIIBullet.getSecondComponent(stack).getPenetrationModifier(ItemIIBullet.getSecondComponentNBT(stack));
			penetrationPower = core_pen+first_pen+second_pen;
			penetrationPower *= ItemIIBullet.getCasing(stack).getPenetration();
			size = ItemIIBullet.getCasing(stack).getSize();
			name = ItemIIBullet.getCasing(stack).getName();
			colourCore = ItemIIBullet.getCore(stack).getColour();
			colourPaint = ItemIIBullet.getColour(stack);
			colourTrail = ItemIIBullet.getTrailColour(stack);
			this.setSize(.625f*size, .625f*size);
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return getEntityBoundingBox();
	}

	@Override
	public void setVelocity(double d, double d1, double d2)
	{
		motionX = d;
		motionY = d1;
		motionZ = d2;
		if(prevRotationPitch==0.0F&&prevRotationYaw==0.0F)
		{
			float f = MathHelper.sqrt(d*d+d2*d2);
			prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2)*180D)/3.1415927410125732D);
			prevRotationPitch = rotationPitch = (float)((Math.atan2(d1, f)*180D)/3.1415927410125732D);
			setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
		}
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if(ticksExisted > fuse)
		{
			setDead();
		}

		if(isDead)
			return;

		if(world.isRemote)
			onUpdateClient();

		boolean canMove = true;


		Vec3d currentPos = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d nextPos = new Vec3d(this.posX+this.motionX, this.posY+this.motionY, this.posZ+this.motionZ);
		RayTraceResult mop = this.world.rayTraceBlocks(currentPos, nextPos, false, true, false);

		if(mop==null||mop.entityHit==null)
		{
			Entity entity = null;
			List list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1), (e) -> e.canBeCollidedWith());
			double d0 = 0.0D;
			for(int i = 0; i < list.size(); ++i)
			{
				Entity entity1 = (Entity)list.get(i);
				if(entity1.canBeCollidedWith()&&(this.ticksExisted > 1))
				{
					float f = 0.3F;
					AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow((double)f, (double)f, (double)f);
					RayTraceResult movingobjectposition1 = axisalignedbb.calculateIntercept(currentPos, nextPos);

					if(movingobjectposition1!=null)
					{
						double d1 = currentPos.distanceTo(movingobjectposition1.hitVec);
						if(d1 < d0||d0==0.0D)
						{
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}
			if(entity!=null)
				mop = new RayTraceResult(entity);
		}

		if(mop!=null)
		{

			canMove = false;
			ImmersiveIntelligence.logger.info(mop.typeOfHit==Type.ENTITY);
			if(onImpact(mop))
			{
				setDead();
			}
		}

		// Movement dampening variables
		float drag = 0.99F;
		float gravity_part = 0.02F;
		// If the stack is in water, spawn particles and increase the drag
		if(isInWater())
		{
			for(int i = 0; i < 4; i++)
			{
				float bubbleMotion = 0.25F;
				world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX-motionX*bubbleMotion,
						posY-motionY*bubbleMotion, posZ-motionZ*bubbleMotion, motionX, motionY, motionZ);
			}
			drag = 0.8F;
		}
		motionX *= drag;
		motionY *= drag;
		motionZ *= drag;
		motionY -= gravity_part*this.mass;

		if(colourTrail!=-1)
		{
			float[] colors = Utils.rgbIntToRGB(colourTrail);
			ImmersiveEngineering.proxy.spawnRedstoneFX(world, posX, posY, posZ, 0, 0, 0, size*4, colors[0], colors[1], colors[2]);
		}

		// Apply motion
		if(canMove)
		{
			posX += motionX;
			posY += motionY;
			posZ += motionZ;
		}

		setPosition(posX, posY, posZ);

		// Recalculate the angles from the new motion
		float motionXZ = MathHelper.sqrt(motionX*motionX+motionZ*motionZ);
		rotationYaw = (float)((Math.atan2(motionX, motionZ)*180D)/3.1415927410125732D);
		rotationPitch = (float)((Math.atan2(motionY, motionXZ)*180D)/3.1415927410125732D);

		rotationPitch = prevRotationPitch+(rotationPitch-prevRotationPitch)*0.2F;
		rotationYaw = prevRotationYaw+(rotationYaw-prevRotationYaw)*0.2F;

		// Temporary fire glitch fix
		if(world.isRemote)
			extinguish();
	}

	@SideOnly(Side.CLIENT)
	private void onUpdateClient()
	{
		if(getDistanceSq(ClientUtils.mc().player.getPosition()) < size*15&&!playedFlybySound)
		{
			playedFlybySound = true;
			//TODO: Play flyby sound
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag)
	{
		tag.setTag("stack", stack.serializeNBT());
		if(owner==null)
			tag.setString("owner", "null");
		else
			tag.setString("owner", owner.getName());
		tag.setString("name", name);
		tag.setInteger("colourCore", colourCore);
		tag.setInteger("colourPaint", colourPaint);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag)
	{
		String ownerName = tag.getString("owner");
		if(tag.hasKey("stack"))
			stack = new ItemStack(tag.getCompoundTag("stack"));
		if(ownerName!=null&&!ownerName.equals("null"))
			owner = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(ownerName);
		name = tag.getString("name");
		colourCore = tag.getInteger("colourCore");
		colourPaint = tag.getInteger("colourPaint");
	}

	@Override
	public void writeSpawnData(ByteBuf data)
	{
		setParams();
		data.writeDouble(motionX);
		data.writeDouble(motionY);
		data.writeDouble(motionZ);
		ByteBufUtils.writeItemStack(data, stack);
		if(owner==null)
			ByteBufUtils.writeUTF8String(data, "null");
		else
			ByteBufUtils.writeUTF8String(data, owner.getName());

		ByteBufUtils.writeUTF8String(data, name);
		data.writeInt(colourCore);
		data.writeInt(colourPaint);
	}

	@Override
	public void readSpawnData(ByteBuf data)
	{
		try
		{
			motionX = data.readDouble();
			motionY = data.readDouble();
			motionZ = data.readDouble();
			stack = ByteBufUtils.readItemStack(data);
			String name = ByteBufUtils.readUTF8String(data);
			for(Object obj : world.loadedEntityList)
			{
				if(obj!=null&&((Entity)obj).getName().equals(name))
				{
					owner = (EntityLivingBase)obj;
					break;
				}
			}
			this.name = ByteBufUtils.readUTF8String(data);
			colourCore = data.readInt();
			colourPaint = data.readInt();
			ImmersiveIntelligence.logger.info("Read bullet data on "+(world.isRemote?"Client": "Server")+", name: "+name);
		} catch(Exception e)
		{
			ImmersiveIntelligence.logger.error("Failed to read stack owner from server.");
			super.setDead();
			ImmersiveIntelligence.logger.throwing(e);
		}
	}

	@Override
	public boolean isBurning()
	{
		return false;
	}

	@Override
	public boolean canBePushed()
	{
		return false;
	}

	public boolean onImpact(RayTraceResult mop)
	{
		if(!this.world.isRemote&&!stack.isEmpty()&&stack.getItem() instanceof ItemIIBullet)
		{
			//Simulate the penetration
			float damage = ItemIIBullet.getCasing(stack).getDamage()*(ItemIIBullet.hasCore(stack)?ItemIIBullet.getCore(stack).getDamageModifier(null): 1f);
			float first_dmg = ItemIIBullet.hasFirstComponent(stack)?ItemIIBullet.getFirstComponent(stack).getDamageModifier(ItemIIBullet.getFirstComponentNBT(stack)): 0f;
			float second_dmg = ItemIIBullet.hasSecondComponent(stack)?ItemIIBullet.getSecondComponent(stack).getDamageModifier(ItemIIBullet.getSecondComponentNBT(stack)): 0f;
			damage += first_dmg+second_dmg;

			if(this.penetrationPower > 0)
			{
				if(mop.typeOfHit==Type.BLOCK)
				{
					BlockPos pos = mop.getBlockPos();
					float hardness = world.getBlockState(pos).getBlockHardness(world, pos);

					ImmersiveIntelligence.logger.info("hardness: "+hardness);
					ImmersiveIntelligence.logger.info("penetration: "+penetrationPower);

					//Ricochet
					if(penetrationPower!=0&&hardness > 0&&!world.getBlockState(pos).getMaterial().isLiquid()&&hardness/4 >= penetrationPower*4)
					{
						ImmersiveIntelligence.logger.info("Ricochet!");
						penetrationPower /= 4;
						if(penetrationPower < 0.5f)
							penetrationPower = 0;
						motionX *= -0.65f;
						motionY *= -0.65f;
						motionZ *= -0.65f;
					}
					//Penetration
					else if(hardness > 0&&!world.getBlockState(pos).getMaterial().isLiquid()&&penetrationPower >= hardness)
					{
						world.destroyBlock(pos, false);
						penetrationPower = Math.max(0f, penetrationPower-hardness);
					}
					//Non-penetrating hit
					else
					{
						this.penetrationPower = 0f;
					}
					ImmersiveIntelligence.logger.info(hardness);
				}

				if(mop.entityHit!=null)
				{
					if(mop.entityHit.attackEntityFrom(IIDamageSources.causeBulletDamage(this, this.owner), damage))
						mop.entityHit.hurtResistantTime = 0;
					Vec3d nextPos = new Vec3d(this.posX+this.motionX, this.posY+this.motionY, this.posZ+this.motionZ);
					penetrationPower = Math.max(0f, penetrationPower-damage/3f);
					moveToBlockPosAndAngles(new BlockPos(nextPos), rotationYaw, rotationPitch);
				}
			}
			else
			{
				if(ItemIIBullet.hasFirstComponent(stack))
					ItemIIBullet.getFirstComponent(stack).onExplosion(ItemIIBullet.getCore(stack).getExplosionModifier()*ItemIIBullet.getCasing(stack).getComponentCapacity()*ItemIIBullet.getFirstComponentQuantity(stack), ItemIIBullet.getFirstComponentNBT(stack), world, getPosition(), this);
				if(ItemIIBullet.hasSecondComponent(stack))
				{
					ItemIIBullet.getSecondComponent(stack).onExplosion(ItemIIBullet.getCore(stack).getExplosionModifier()*ItemIIBullet.getCasing(stack).getComponentCapacity()*ItemIIBullet.getSecondComponentQuantity(stack), ItemIIBullet.getSecondComponentNBT(stack), world, getPosition(), this);
				}
				return true;
			}
		}
		return false;
	}
}
