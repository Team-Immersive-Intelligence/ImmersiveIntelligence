package pl.pabilo8.immersiveintelligence.common.entity.bullets;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.client.ClientUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationHelper;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.HitEffect;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.common.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBullet;

import java.util.List;
import java.util.Map.Entry;
import java.util.function.Predicate;

/**
 * Created by Pabilo8 on 30-08-2019.
 * Yes, I stole this one from Flan's Mod too! (Thanks Flan!)
 * Major update on 08-03-2020.
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
		setEntityInvulnerable(false);
	}


	public float getSize()
	{
		return size = ItemIIBullet.getCasing(this.stack).getSize();
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
			List list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1), Entity::canBeCollidedWith);
			double d0 = 0.0D;
			for(int i = 0; i < list.size(); ++i)
			{
				Entity entity1 = (Entity)list.get(i);
				if(entity1.canBeCollidedWith()&&(this.ticksExisted > 1))
				{
					float f = 0.3F;
					AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(f, f, f);
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
		//The higher the velocity, the lower the penetration loss
		//Penetrating up is harder than down
		penetrationPower *= 0.95*(motionY > 0?-1: 1);
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

			if(world.isRemote)
				ItemIIBullet.getCasing(stack).doPuff(this);
		} catch(Exception e)
		{
			super.setDead();
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

			if(this.penetrationPower > 0)
			{
				if(mop.typeOfHit==Type.BLOCK)
				{
					BlockPos pos = mop.getBlockPos();
					IPenetrationHandler pen = null;
					for(Entry<Predicate<IBlockState>, IPenetrationHandler> e : PenetrationRegistry.registeredBlocks.entrySet())
					{
						if(e.getKey().test(world.getBlockState(pos)))
						{
							pen = e.getValue();
							break;
						}
					}
					if(pen==null)
					{
						for(Entry<Predicate<Material>, IPenetrationHandler> e : PenetrationRegistry.registeredMaterials.entrySet())
						{
							if(e.getKey().test(world.getBlockState(pos).getMaterial()))
							{
								pen = e.getValue();
								break;
							}
						}
					}

					float hardness = world.getBlockState(pos).getBlockHardness(world, pos);
					float density = pen.getDensity();
					float width = 1;

					AxisAlignedBB aabb = world.getBlockState(pos).getBoundingBox(world, pos);
					switch(EnumFacing.getFacingFromVector((float)motionX, (float)motionY, (float)motionZ))
					{
						case NORTH:
						case SOUTH:
							width = (float)Math.abs(aabb.maxX-aabb.minX);
							break;
						case EAST:
						case WEST:
							width = (float)Math.abs(aabb.maxZ-aabb.minZ);
							break;
						case UP:
						case DOWN:
							width = (float)Math.abs(aabb.maxY-aabb.minY);
							break;
					}


					float hp = pen.getIntegrity()/pen.getDensity();

					boolean done = false;
					DimensionBlockPos blockHitPos = new DimensionBlockPos(pos, world);
					for(Entry<DimensionBlockPos, Float> p : PenetrationRegistry.blockDamage.entrySet())
					{
						if(p.getKey().equals(blockHitPos))
						{
							blockHitPos = p.getKey();
							hp = p.getValue();
							done = true;
							break;
						}
					}
					if(!done)
						PenetrationRegistry.blockDamage.put(blockHitPos, hp);

					float penFraction = penetrationPower/(hardness*width*density);

					//Over Penetration
					if(penFraction > 1)
					{

						PenetrationHelper.dealBlockDamage(world, size*50, blockHitPos, hp, pen);
						penetrationPower -= hardness*width*density;
						float supressionRadius = ItemIIBullet.getCasing(stack).getSupressionRadius();
						int suppressionPower = ItemIIBullet.getCasing(stack).getSuppressionPower();
						PenetrationHelper.supress(world, posX, posY, posZ, supressionRadius*(1f-penFraction), suppressionPower);
						playHitSound(HitEffect.PENETRATION, world, pos, pen);

					}
					//Ricochet
					else if(penetrationPower > 0.01f&&penFraction < 0.125f&&density >= 1f)
					{
						penetrationPower = 0.01f;

						motionX *= -0.125f;
						motionZ *= -0.125f;
						motionY *= -0.25f;
						double newPitch = (90-Math.abs(rotationPitch));

						if(rotationPitch < 0)
							rotationPitch -= 2*newPitch;
						else if(rotationPitch > 0)
							rotationPitch += 2*newPitch;

						float supressionRadius = ItemIIBullet.getCasing(stack).getSupressionRadius();
						int suppressionPower = ItemIIBullet.getCasing(stack).getSuppressionPower();
						PenetrationHelper.supress(world, posX, posY, posZ, supressionRadius, suppressionPower);

						playHitSound(HitEffect.PARTIAL_PENETRATION, world, pos, pen);
						//TODO: Ricochet Sound

					}
					//Regular Penetration
					else
					{
						PenetrationHelper.dealBlockDamage(world, size*penFraction*50, blockHitPos, hp, pen);
						playHitSound(HitEffect.PENETRATION, world, pos, pen);
						penetrationPower = 0;
					}

				}

				if(mop.entityHit!=null)
				{
					boolean headshot = false;

					float core_damage = ItemIIBullet.getCasing(stack).getDamage()*(ItemIIBullet.hasCore(stack)?ItemIIBullet.getCore(stack).getDamageModifier(null): 1f);
					float first_dmg = ItemIIBullet.hasFirstComponent(stack)?ItemIIBullet.getFirstComponent(stack).getDamageModifier(ItemIIBullet.getFirstComponentNBT(stack)): 0f;
					float second_dmg = ItemIIBullet.hasSecondComponent(stack)?ItemIIBullet.getSecondComponent(stack).getDamageModifier(ItemIIBullet.getSecondComponentNBT(stack)): 0f;

					if(mop.entityHit instanceof EntityLivingBase)
					{
						headshot = blusunrize.immersiveengineering.common.util.Utils.isVecInEntityHead((EntityLivingBase)mop.entityHit, new Vec3d(posX, posY, posZ));

						IAttributeInstance armor = ((EntityLivingBase)mop.entityHit).getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS);
						PenetrationHelper.breakArmour(mop.entityHit, (int)Math.ceil((core_damage*0.125f)/armor.getAttributeValue()));

					}

					float damage = (float)(core_damage*(1+first_dmg+second_dmg)*(headshot?1.25: 1));

					if(mop.entityHit.attackEntityFrom(IIDamageSources.causeBulletDamage(this, this.owner), damage))
						mop.entityHit.hurtResistantTime = 0;
					Vec3d nextPos = new Vec3d(this.posX+this.motionX, this.posY+this.motionY, this.posZ+this.motionZ);


					penetrationPower = Math.max(0f, penetrationPower-damage/3f);
					moveToBlockPosAndAngles(new BlockPos(nextPos), rotationYaw, rotationPitch);
				}
			}
			if(penetrationPower <= 0)
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

	private void playHitSound(HitEffect effect, World world, BlockPos pos, IPenetrationHandler handler)
	{
		IBlockState state = world.getBlockState(pos);
		SoundEvent event = handler.getSpecialSound(effect);
		if(event==null)
		{
			SoundType type = state.getBlock().getSoundType(state, world, pos, this);
			event = effect==HitEffect.PENETRATION?type.getBreakSound(): type.getStepSound();
		}
		world.getMinecraftServer().getPlayerList().sendToAllNearExcept(null, posX, posY, posZ, 24D, this.world.provider.getDimension(), new SPacketSoundEffect(event, SoundCategory.BLOCKS, posX, posY, posZ, 1f, 1f));
	}
}
