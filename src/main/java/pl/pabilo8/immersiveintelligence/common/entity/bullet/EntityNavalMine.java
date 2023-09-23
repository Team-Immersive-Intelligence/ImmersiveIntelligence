package pl.pabilo8.immersiveintelligence.common.entity.bullet;

import blusunrize.immersiveengineering.common.util.FakePlayerUtil;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Mines;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageEntityNBTSync;

/**
 * @author Pabilo8
 * @since 09.02.2021
 */
public class EntityNavalMine extends EntityBullet
{
	public int maxLength = 5;
	boolean isNotRiding = false;
	private static final DataParameter<Integer> dataMarkerMaxLength = EntityDataManager.createKey(EntityNavalMine.class, DataSerializers.VARINT);

	public EntityNavalMine(World worldIn)
	{
		super(worldIn);
	}

	@Override
	public void handleStatusUpdate(byte id)
	{
		super.handleStatusUpdate(id);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataManager.register(dataMarkerMaxLength, 0);
	}

	@Override
	protected void fromStack(ItemStack stack)
	{
		super.fromStack(stack);
		if(ItemNBTHelper.hasKey(stack, "length"))
			this.maxLength = ItemNBTHelper.getInt(stack, "length");
		else
			this.maxLength = 5;
	}

	public EntityNavalMine(World worldIn, ItemStack stack, double x, double y, double z)
	{
		super(worldIn);
		fromStack(stack);
		this.setPosition(x, y, z);
		this.force = 0;
		this.initialForce = 0;
	}

	@Override
	public void onUpdate()
	{
		onUpdateSuper();

		if(inWater)
		{
			//trick
			isNotRiding = true;
			if(getRidingEntity() instanceof EntityNavalMineAnchor)
				getRidingEntity().noClip = true;
			move(MoverType.SELF, 0, 0.0125f, 0);
			if(getRidingEntity() instanceof EntityNavalMineAnchor)
				getRidingEntity().noClip = false;
			isNotRiding = false;
		}

		if(!world.isRemote)
			world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(0.35f), input -> !(input instanceof EntityNavalMineAnchor||input instanceof EntityNavalMine)).forEach(this::applyEntityCollision);
		else if(dataManager.isDirty())
			maxLength = dataManager.get(dataMarkerMaxLength);

		if(!world.isRemote&&ticksExisted==1)
		{
			if(this.shooter==null)
				this.shooter = FakePlayerUtil.getFakePlayer(world);

			NBTTagCompound nbt = new NBTTagCompound();
			writeEntityToNBT(nbt);
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageEntityNBTSync(this, nbt), IIPacketHandler.targetPointFromEntity(this, 32));
		}
		else if(world.isRemote&&!wasSynced)
			return;

		if(isDead)
			return;

		if(world.isRemote)
		{
			for(int j = 0; j < components.length; j++)
			{
				IAmmoComponent c = components[j];
				if(c.hasTrail())
					c.spawnParticleTrail(this, componentNBT[j]);
			}
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		setMaxLength(compound.getInteger("maxLength"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setInteger("maxLength", maxLength);
	}

	public void setMaxLength(int length)
	{
		maxLength = length;
		dataManager.set(dataMarkerMaxLength, maxLength);
	}

	@Override
	public void applyEntityCollision(Entity entityIn)
	{
		//for less annoyance
		if(entityIn instanceof EntitySquid)
			return;

		super.applyEntityCollision(entityIn);
		if(ticksExisted > Mines.navalMineArmTime)
		{
			this.performEffect(new RayTraceResult(this));
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return getEntityBoundingBox().grow(0.3f);
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	@Override
	public void setDead()
	{
		super.setDead();
		if(isRiding())
			getRidingEntity().setDead();
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		if(isRiding())
		{
			double diff = Math.min(maxLength, Math.abs(getRidingEntity().posY-this.posY));
			return this.getCollisionBoundingBox().expand(0, diff, 0);
		}
		else
			return super.getRenderBoundingBox();
	}

	@Override
	public boolean isRiding()
	{
		if(isNotRiding)
			return false;
		return super.isRiding();
	}
}
