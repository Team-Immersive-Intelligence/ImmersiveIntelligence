package pl.pabilo8.immersiveintelligence.common.entity.ammo.types.naval_mine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Mines;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoMine;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIINavalMine;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 02.02.2024
 * @ii-approved 0.3.1
 * @since 09.02.2021
 */
public class EntityNavalMine extends EntityAmmoMine
{
	private static final DataParameter<Integer> dataMarkerMaxLength = EntityDataManager.createKey(EntityNavalMine.class, DataSerializers.VARINT);
	public int maxLength = 5;
	boolean isNotRiding = false;

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

	@Override
	public void setFromStack(@Nonnull ItemStack stack)
	{
		super.setFromStack(stack);
		EasyNBT.wrapNBT(stack).checkSetInt(ItemIINavalMine.NBT_CHAIN_LENGTH, f -> this.maxLength = f, 5);
	}

	// Then modify the onUpdate method as follows:
	@Override
	public void onUpdate()
	{
		super.onUpdate();

		Entity riding = getRidingEntity();
		if(ticksExisted < 4||!(riding instanceof EntityNavalMineAnchor))
			return;

		IBlockState state = world.getBlockState(getPosition());
		IBlockState stateAbove = world.getBlockState(new BlockPos(this.posX, Math.ceil(this.posY), this.posZ));
		boolean inLiquid = state.getMaterial().isLiquid();
		boolean aboveIsLiquid = stateAbove.getMaterial().isLiquid();

		//stabilize at liquid/air boundary to prevent bobbing
		if(inLiquid)
		{
			//prevent bobbing when between air and water
			if(!aboveIsLiquid)
			{
				this.motionY = 0;
				this.setPosition(this.posX, this.posY, this.posZ);
			}
			else
			{
				//stay afloat
				riding.noClip = true;
				isNotRiding = true;
				move(MoverType.SELF, 0, 0.0125f, 0);
				isNotRiding = false;
				riding.noClip = false;
			}
		}
		//follow the anchor cart, try to stay on top
		else if(riding.posY+riding.height < this.posY)
			move(MoverType.SELF, 0, -0.2f, 0);
		else
			//stay on top of the anchor cart
			this.posY = riding.posY+riding.height;

		if(!world.isRemote)
			world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(0.35f), input -> input!=riding&&input!=this)
					.forEach(this::applyEntityCollision);
		else if(dataManager.isDirty())
			maxLength = dataManager.get(dataMarkerMaxLength);
	}

	@Override
	@Nonnull
	protected Vec3d getDirection()
	{
		return Vec3d.ZERO;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		setMaxLength(compound.getInteger("maxLength"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
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
	public void applyEntityCollision(Entity other)
	{
		//for less annoyance
		if(other instanceof EntitySquid||other.noClip)
			return;

		super.applyEntityCollision(other);
		if(ticksExisted > Mines.navalMineArmTime)
			detonate();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return getEntityBoundingBox().grow(0.3f);
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

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		AmmoComponent[] components = this.components.stream().map(Tuple::getFirst).toArray(AmmoComponent[]::new);
		NBTTagCompound[] componentTags = this.components.stream().map(Tuple::getSecond).toArray(NBTTagCompound[]::new);

		ItemStack ammoStack = IIContent.itemNavalMine.getAmmoStack(this.core, this.coreType, this.fuseType, components);
		IIContent.itemNavalMine.setComponentNBT(ammoStack, componentTags);

		return ammoStack;
	}
}
