package pl.pabilo8.immersiveintelligence.common.entity.ammo.naval_mine;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Mines;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;

/**
 * @author Pabilo8
 * @since 09.02.2021
 */
public class EntityNavalMine extends EntityAmmoBase
{
	public int maxLength = 5;
	boolean isNotRiding = false;
	private static final DataParameter<Integer> dataMarkerMaxLength = EntityDataManager.createKey(EntityNavalMine.class, DataSerializers.VARINT);

	public EntityNavalMine(World worldIn)
	{
		super(worldIn);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataManager.register(dataMarkerMaxLength, 0);
	}

	//TODO: 02.01.2024 reimplement
/*	@Override
	protected void fromStack(ItemStack stack)
	{
		super.fromStack(stack);
		if(ItemNBTHelper.hasKey(stack, "length"))
			this.maxLength = ItemNBTHelper.getInt(stack, "length");
		else
			this.maxLength = 5;
	}*/

	@Override
	public void onUpdate()
	{
		super.onUpdate();

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
			setDead();
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
