package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
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
import org.lwjgl.util.vector.Vector3f;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBullet;

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
	public int colorCore = 0, colorPaint = 0;
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
	}

	public void setFuse(int fuse)
	{
		this.fuse = fuse;
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
			colorCore = ItemIIBullet.getCore(stack).getColour();
			colorPaint = ItemIIBullet.getColour(stack);
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

		Vector3f origin = new Vector3f((float)posX, (float)posY, (float)posZ);
		Vector3f motion = new Vector3f((float)motionX, (float)motionY, (float)motionZ);

		boolean canMove = true;


		Vec3d currentPos = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d nextPos = new Vec3d(this.posX+this.motionX, this.posY+this.motionY, this.posZ+this.motionZ);
		RayTraceResult mop = this.world.rayTraceBlocks(currentPos, nextPos, false, true, false);

		if(mop!=null)
		{
			canMove = false;
			ImmersiveIntelligence.logger.info("NIIIEE");
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
		if(getDistanceSq(Minecraft.getMinecraft().player.getPosition()) < size*15&&!playedFlybySound)
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
		tag.setInteger("colorCore", colorCore);
		tag.setInteger("colorPaint", colorPaint);
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
		colorCore = tag.getInteger("colorCore");
		colorPaint = tag.getInteger("colorPaint");
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
		data.writeInt(colorCore);
		data.writeInt(colorPaint);
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
			colorCore = data.readInt();
			colorPaint = data.readInt();
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

			float damage = ItemIIBullet.getCasing(stack).getDamage();
			float first_dmg = ItemIIBullet.hasFirstComponent(stack)?ItemIIBullet.getFirstComponent(stack).getDamageModifier(ItemIIBullet.getFirstComponentNBT(stack)): 0f;
			float second_dmg = ItemIIBullet.hasSecondComponent(stack)?ItemIIBullet.getSecondComponent(stack).getDamageModifier(ItemIIBullet.getSecondComponentNBT(stack)): 0f;
			damage += first_dmg+second_dmg;

			if(this.penetrationPower > 0)
			{
				if(mop.typeOfHit==Type.BLOCK)
				{
					BlockPos pos = mop.getBlockPos();
					float hardness = world.getBlockState(pos).getBlockHardness(world, pos);

					if(hardness > 0&&!world.getBlockState(pos).getMaterial().isLiquid()&&penetrationPower >= hardness)
					{
						world.destroyBlock(pos, false);
						penetrationPower = Math.max(0f, penetrationPower-hardness);
					}
					else
					{
						this.penetrationPower = 0f;
					}
					ImmersiveIntelligence.logger.info(hardness);
				}

				if(mop.entityHit!=null)
				{
					mop.entityHit.attackEntityFrom(IIDamageSources.causeBulletDamage(this, this.owner), damage);
					Vec3d nextPos = new Vec3d(this.posX+this.motionX, this.posY+this.motionY, this.posZ+this.motionZ);
					penetrationPower = Math.max(0f, penetrationPower-damage/3f);
					moveToBlockPosAndAngles(new BlockPos(nextPos), rotationYaw, rotationPitch);
				}
			}
			else
			{
				ImmersiveIntelligence.logger.info("otak!");
				if(ItemIIBullet.hasFirstComponent(stack))
					ItemIIBullet.getFirstComponent(stack).onExplosion(ItemIIBullet.getCore(stack).getExplosionModifier()*ItemIIBullet.getCasing(stack).getComponentCapacity()*ItemIIBullet.getFirstComponentQuantity(stack), ItemIIBullet.getFirstComponentNBT(stack), world, getPosition(), this);
				if(ItemIIBullet.hasSecondComponent(stack))
				{
					ImmersiveIntelligence.logger.info("hasSecond!");
					ItemIIBullet.getSecondComponent(stack).onExplosion(ItemIIBullet.getCore(stack).getExplosionModifier()*ItemIIBullet.getCasing(stack).getComponentCapacity()*ItemIIBullet.getSecondComponentQuantity(stack), ItemIIBullet.getSecondComponentNBT(stack), world, getPosition(), this);
				}
				return true;
			}
		}
		return false;
	}
}
