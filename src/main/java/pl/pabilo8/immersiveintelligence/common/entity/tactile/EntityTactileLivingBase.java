package pl.pabilo8.immersiveintelligence.common.entity.tactile;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Invisible living root for a collection of server-side AMT tactile parts.
 * <p>
 * The root gives projectiles and other entity-based systems a stable, faction-owned
 * living entity while collision and interaction remain delegated to the animated parts.
 * </p>
 */
public class EntityTactileLivingBase extends EntityLivingBase implements IEntityMultiPart, IEntityAdditionalSpawnData
{
	private static final EntityAMTTactile[] NO_PARTS = new EntityAMTTactile[0];
	private static final AxisAlignedBB EMPTY = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
	private static final String NBT_OWNER = "tactile_owner";
	private static final String NBT_NAME = "tactile_name";

	@Nullable
	private TactileManager manager;
	@Nonnull
	private OwnerIdentity ownerIdentity;
	private String tactileName = "";

	public EntityTactileLivingBase(World worldIn)
	{
		super(worldIn);
		setSize(0, 0);
		setInvisible(true);
		setEntityInvulnerable(true);
		setNoGravity(true);
		noClip = true;
		ownerIdentity = getNeutralIdentity();
	}

	EntityTactileLivingBase(TactileManager manager)
	{
		this(manager.getWorld());
		this.manager = manager;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		motionX = motionY = motionZ = 0;
		fallDistance = 0;

		if(!world.isRemote)
		{
			if(manager==null||!manager.owns(this))
			{
				setDead();
				return;
			}
			Vec3d position = manager.getLivingEntityPosition();
			setPosition(position.x, position.y, position.z);
		}
	}

	@Override
	public EntityAMTTactile[] getParts()
	{
		return manager==null?NO_PARTS: manager.getPartArray();
	}

	@Override
	public boolean attackEntityFromPart(@Nonnull MultiPartEntityPart part, @Nonnull DamageSource source, float amount)
	{
		return false;
	}

	@Override
	public World getWorld()
	{
		return world;
	}

	@Nonnull
	public OwnerIdentity getOwnerIdentity()
	{
		return ownerIdentity;
	}

	public void setOwnerIdentity(@Nullable OwnerIdentity ownerIdentity)
	{
		this.ownerIdentity = ownerIdentity==null?getNeutralIdentity(): ownerIdentity;
	}

	public void setTactileName(@Nullable String tactileName)
	{
		this.tactileName = tactileName==null?"": tactileName;
	}

	@Override
	public String getName()
	{
		if(hasCustomName())
			return super.getName();
		return tactileName.isEmpty()?super.getName(): I18n.translateToLocal(tactileName);
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}

	@Override
	public boolean canBePushed()
	{
		return false;
	}

	@Override
	protected void collideWithNearbyEntities()
	{

	}

	@Override
	public void applyEntityCollision(Entity entityIn)
	{

	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return false;
	}

	@Override
	public AxisAlignedBB getEntityBoundingBox()
	{
		return EMPTY.offset(posX, posY, posZ);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return null;
	}

	@Override
	public boolean writeToNBTOptional(NBTTagCompound compound)
	{
		return false;
	}

	@Override
	public boolean writeToNBTAtomically(NBTTagCompound compound)
	{
		return false;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		if(compound.hasKey(NBT_OWNER, 8))
			readOwner(compound.getString(NBT_OWNER));
		tactileName = compound.getString(NBT_NAME);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setString(NBT_OWNER, ownerIdentity.getUUID().toString());
		compound.setString(NBT_NAME, tactileName);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		ByteBufUtils.writeUTF8String(buffer, ownerIdentity.getUUID().toString());
		ByteBufUtils.writeUTF8String(buffer, tactileName);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		readOwner(ByteBufUtils.readUTF8String(additionalData));
		tactileName = ByteBufUtils.readUTF8String(additionalData);
	}

	private void readOwner(String uuid)
	{
		try
		{
			ownerIdentity = DiplomacyHandler.getInstance(world.isRemote).getIdentityByUUID(UUID.fromString(uuid));
		} catch(IllegalArgumentException ignored)
		{
			ownerIdentity = getNeutralIdentity();
		}
	}

	private OwnerIdentity getNeutralIdentity()
	{
		return DiplomacyHandler.getInstance(world.isRemote).getIdentityByUUID(DiplomacyHandler.NEUTRAL_UUID);
	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList()
	{
		return NonNullList.create();
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack)
	{

	}

	@Override
	public EnumHandSide getPrimaryHand()
	{
		return EnumHandSide.RIGHT;
	}
}
