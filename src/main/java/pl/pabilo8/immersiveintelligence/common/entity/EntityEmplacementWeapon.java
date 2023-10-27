package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import it.unimi.dsi.fastutil.doubles.DoubleComparators;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Team;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.api.utils.IEntitySpecialRepairable;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityEmplacement.EmplacementWeapon;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;

/**
 * Yes, "living", as in: able to attract enemies towards it
 *
 * @author Pabilo8
 * @since 17.07.2021
 */
public class EntityEmplacementWeapon extends EntityLivingBase implements IEntityMultiPart, IEntitySpecialRepairable
{
	EmplacementWeapon parent = null;
	public EmplacementHitboxEntity[] partArray = new EmplacementHitboxEntity[0];
	public AxisAlignedBB aabb = new AxisAlignedBB(-1.5, 0, -1.5, 1.5, 3, 1.5);

	public EntityEmplacementWeapon(World worldIn)
	{
		super(worldIn);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
	}

	@Override
	public void onUpdate()
	{
		if(!this.world.isRemote)
		{
			this.setFlag(6, this.isGlowing());
		}
		this.onEntityUpdate();

		if(!world.isRemote&&ticksExisted > 40&&getParent()==null)
			setDead();

		if(getParent()!=null)
		{
			parent.syncWithEntity(this);
			rotateCollisionBoxes();
		}
	}

	@Override
	public Entity[] getParts()
	{
		return partArray;
	}

	@Override
	public AxisAlignedBB getEntityBoundingBox()
	{
		return aabb.offset(posX, posY, posZ);
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return null;
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBox(Entity entityIn)
	{
		return null;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		Team t = null;
		String name = "emplacement";
		if(parent!=null)
		{
			name = parent.getName();
			TileEntity tileEntity = world.getTileEntity(getPosition());
			if(tileEntity instanceof TileEntityEmplacement)
			{
				TileEntityEmplacement master = ((TileEntityEmplacement)tileEntity).master();
				if(master!=null)
				{
					EntityPlayer player = world.getPlayerEntityByName(master.owner);
					if(player!=null)
						t = player.getTeam();
				}
			}
		}

		TextComponentTranslation textComponent = new TextComponentTranslation("machineupgrade.immersiveintelligence."+name);
		if(t!=null)
			textComponent.getStyle().setColor(t.getColor());
		textComponent.getStyle().setHoverEvent(this.getHoverEvent());
		textComponent.getStyle().setInsertion(this.getCachedUniqueIdString());
		return textComponent;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(parent==null)
			return false;

		if((source instanceof ElectricDamageSource||source==IEDamageSources.acid))
		{
			parent.applyDamage(amount*1.1f);
			return true;
		}

		if(source.damageType.equals("bullet")) //immersive vehicles(tm)
		{
			DamageSource temp_source = new DamageSource("bullet").setProjectile().setDamageBypassesArmor();
			//attack random part
			return attackEntityFromPart(partArray[blusunrize.immersiveengineering.common.util.Utils.RAND.nextInt(partArray.length)], source, amount*0.5f);
		}

		if(source.isFireDamage()||source.isMagicDamage())
			return false;

		//If attacked by a mob, player hits don't apply here
		if(source.getImmediateSource() instanceof EntityLivingBase)
		{
			Entity immediateSource = source.getImmediateSource();

			//Get nearest hitbox
			Optional<EmplacementHitboxEntity> hitbox = Arrays.stream(partArray)
					.min((o1, o2) -> DoubleComparators.NATURAL_COMPARATOR.compare(o1.getDistance(immediateSource), o2.getDistance(immediateSource)));
			if(hitbox.isPresent())
			{
				return attackEntityFromPart(hitbox.get(), source, amount);
			}
		}

		return false;
	}

	@Override
	public EnumHandSide getPrimaryHand()
	{
		return null;
	}

	@Override
	public float getCollisionBorderSize()
	{
		return 0;
	}

	private void rotateCollisionBoxes()
	{
		if(partArray.length==0)
			partArray = parent.getCollisionBoxes();

		if(partArray.length==0)
			return;

		TileEntity te = world.getTileEntity(getPosition());
		if(!(te instanceof TileEntityEmplacement))
			return;
		TileEntityEmplacement t = ((TileEntityEmplacement)te).master();
		if(t==null)
			return;

		float f = MathHelper.clamp(t.progress/(float)Emplacement.lidTime, 0f, 1f);
		float turretHeight;
		if(f <= 0.65)
		{
			turretHeight = (f-0.25f)/0.4f;
		}
		else if(f <= 0.85)
		{
			turretHeight = 1;
		}
		else if(f!=1)
		{
			turretHeight = 1f-(0.35f*((f-0.85f)/0.15f));
		}
		else
			turretHeight = 0.65f;

		Vec3d thisPos = getPositionVector().addVector(0, 4.5f*turretHeight-3, 0);

		Vec3d pos_main_x = IIUtils.offsetPosDirection(1f, Math.toRadians(rotationYaw), 0);
		Vec3d pos_main_z = IIUtils.offsetPosDirection(1f, Math.toRadians(rotationYaw-90), 0);

		Vec3d pos_gun_x = IIUtils.offsetPosDirection(1f, Math.toRadians(rotationYaw), Math.toRadians(rotationPitch));
		Vec3d pos_gun_z = IIUtils.offsetPosDirection(1f, Math.toRadians(rotationYaw-90), Math.toRadians(rotationPitch-90));


		for(EmplacementHitboxEntity part : partArray)
		{
			part.updatePart(thisPos, pos_main_x, pos_main_z, pos_gun_x, pos_gun_z);
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		if(world.isRemote)
			getParent();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
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
	public World getWorld()
	{
		return world;
	}

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float damage)
	{
		if(part instanceof EmplacementHitboxEntity)
		{
			int armor = ((EmplacementHitboxEntity)part).armor;
			if(armor-damage > 0)
			{
				world.playSound(null, getPosition(), IISounds.hitMetal.getSoundImpact(), SoundCategory.BLOCKS, 1.5f, armor/damage*0.95f);
				return false;
			}
			parent.applyDamage(damage-armor);
			world.playSound(null, getPosition(), IISounds.hitMetal.getSoundImpact(), SoundCategory.BLOCKS, 1.5f, 0.95f);
			return true;
		}
		world.playSound(null, getPosition(), IISounds.hitMetal.getSoundRicochet(), SoundCategory.BLOCKS, 1.5f, 0.65f);
		return false;
	}

	@Nullable
	public EmplacementWeapon getParent()
	{
		if(parent!=null)
			return parent;
		TileEntity tileEntity = world.getTileEntity(getPosition());
		if(tileEntity instanceof TileEntityEmplacement)
		{
			TileEntityEmplacement master = ((TileEntityEmplacement)tileEntity).master();
			if(master!=null&&master.currentWeapon!=null)
			{
				if(master.currentWeapon.entity==null||master.currentWeapon.entity==this)
					return parent = master.currentWeapon;
				else
					setDead();
			}
		}
		return null;
	}

	@Override
	public boolean canRepair()
	{
		if(parent==null)
			return false;

		return parent.getHealth()!=parent.getMaxHealth();
	}

	@Override
	public boolean repair(int repairPoints)
	{
		if(parent==null)
			return false;

		int maxAmount = MathHelper.clamp(repairPoints, 0, parent.getMaxHealth()-parent.getHealth());
		parent.applyDamage(-maxAmount);
		return true;
	}

	@Override
	public int getRepairCost()
	{
		if(parent==null)
			return 0;

		return 4;
	}

	public static class EmplacementHitboxEntity extends MultiPartEntityPart
	{
		protected final EntityEmplacementWeapon parentExt;
		private final Vec3d mainOffset, gunOffset;
		protected final AxisAlignedBB aabb;
		protected final int armor;

		public EmplacementHitboxEntity(EntityEmplacementWeapon parent, String partName, float width, float height, Vec3d mainOffset, Vec3d gunOffset, int armor)
		{
			super(parent, partName, width, height);
			this.parentExt = parent;
			this.mainOffset = mainOffset;
			this.gunOffset = gunOffset;
			this.aabb = new AxisAlignedBB(-width/2, -height/2, -width/2, width/2, height/2, width/2);
			this.armor = armor;
		}

		public void updatePart(Vec3d pPos, Vec3d posMainX, Vec3d posMainZ, Vec3d posGunX, Vec3d posGunZ)
		{
			posMainX = posMainX.scale(mainOffset.x).addVector(0, mainOffset.y, 0);
			posMainZ = posMainZ.scale(mainOffset.z);

			posGunX = posGunX.scale(gunOffset.x);
			posGunZ = posGunZ.scale(gunOffset.z);

			setLocationAndAngles(pPos.x+posMainX.x+posMainZ.x+posGunX.x+posGunZ.x, pPos.y+posMainX.y+posGunX.y+posMainZ.y+posGunZ.y, pPos.z+posMainX.z+posMainZ.z+posGunX.z+posGunZ.z, 0, 0);
			setEntityBoundingBox(aabb.offset(this.posX, this.posY, this.posZ));
			onUpdate();
		}

		@Override
		public void applyEntityCollision(Entity entityIn)
		{
			//disable collisions for entities of the same multipart
			if(entityIn!=parentExt&&parentExt!=null&&Arrays.stream(parentExt.getParts()).noneMatch(entity -> entity==entityIn))
				super.applyEntityCollision(entityIn);
		}

		@Override
		public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
		{
			return true;
		}

		@Override
		public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand)
		{
			if(world.isRemote&&parentExt!=null&&parentExt.parent!=null)
			{
				TileEntity tileEntity = world.getTileEntity(parentExt.getPosition());
				if(tileEntity instanceof TileEntityEmplacement)
				{
					ClientUtils.mc().playerController.processRightClickBlock(ClientUtils.mc().player, ClientUtils.mc().world, parentExt.getPosition(),
							EnumFacing.getDirectionFromEntityLiving(parentExt.getPosition(), player), vec, hand);
					return EnumActionResult.SUCCESS;
				}
			}
			return super.applyPlayerInteraction(player, vec, hand);
		}

		@Override
		public AxisAlignedBB getCollisionBoundingBox()
		{
			return getEntityBoundingBox();
		}

		@Override
		public AxisAlignedBB getCollisionBox(Entity entityIn)
		{
			return getEntityBoundingBox();
		}

		@Override
		public boolean canRenderOnFire()
		{
			//handled by parent
			return false;
		}

		@Override
		public boolean attackEntityFrom(DamageSource source, float amount)
		{
			if(source.damageType.equals("iiShrapnel")||source.damageType.equals("iiShrapnelNoShooter"))
				return false;
			if((source instanceof ElectricDamageSource||source==IEDamageSources.acid))
			{
				return parent.attackEntityFromPart(this, source, amount);
			}
			return super.attackEntityFrom(source, amount);
		}
	}
}
