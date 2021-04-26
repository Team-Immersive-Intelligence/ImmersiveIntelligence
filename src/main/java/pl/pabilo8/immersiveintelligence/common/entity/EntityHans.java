package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.hans_tasks.AIHansHowitzer;
import pl.pabilo8.immersiveintelligence.common.entity.hans_tasks.AIHansSubmachinegun;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.ItemIIBulletMagazine;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIWeaponUpgrade.WeaponUpgrades;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 11.01.2021
 */
public class EntityHans extends EntityCreature implements INpc
{
	public boolean hasAmmo = true;

	public final NonNullList<ItemStack> mainInventory = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);

	private final net.minecraftforge.items.IItemHandlerModifiable handHandler = new net.minecraftforge.items.wrapper.EntityHandsInvWrapper(this);
	private final net.minecraftforge.items.IItemHandlerModifiable armorHandler = new net.minecraftforge.items.wrapper.EntityArmorInvWrapper(this);
	private final net.minecraftforge.items.IItemHandlerModifiable invHandler = new ItemStackHandler(this.mainInventory);
	private final net.minecraftforge.items.IItemHandler joinedHandler = new net.minecraftforge.items.wrapper.CombinedInvWrapper(armorHandler, handHandler, invHandler);

	public EntityHans(World worldIn)
	{
		super(worldIn);
		this.enablePersistence();
	}

	public void equipItems(int id)
	{
		ItemStack helmet = new ItemStack(IIContent.itemLightEngineerHelmet);
		if(id < 2)
		{
			ItemStack stack = new ItemStack(IIContent.itemSubmachinegun);
			NonNullList<ItemStack> upgrades = NonNullList.withSize(2, ItemStack.EMPTY);
			upgrades.set(0, new ItemStack(IIContent.itemWeaponUpgrade, 1, WeaponUpgrades.SUPPRESSOR.ordinal()));
			upgrades.set(1, new ItemStack(IIContent.itemWeaponUpgrade, 1, WeaponUpgrades.BOTTOM_LOADING.ordinal()));
			IIContent.itemSubmachinegun.setContainedItems(stack, upgrades);
			IIContent.itemSubmachinegun.recalculateUpgrades(stack);
			IIContent.itemSubmachinegun.finishUpgradeRecalculation(stack);
			ItemStack magazine = ItemIIBulletMagazine.getMagazine(id==0?"submachinegun": "submachinegun_drum", IIContent.itemAmmoSubmachinegun.getBulletWithParams("core_brass", "piercing"));
			ItemNBTHelper.setItemStack(stack, "magazine", magazine);
			mainInventory.set(0, magazine.copy());
			mainInventory.set(1, magazine.copy());
			mainInventory.set(2, magazine.copy());

			setHeldItem(EnumHand.MAIN_HAND, stack);

			/*
			upgrades = NonNullList.withSize(3, ItemStack.EMPTY);
			upgrades.set(0, new ItemStack(IIContent.itemArmorUpgrade, 1, ArmorUpgrades.GASMASK.ordinal()));
			IIContent.itemLightEngineerHelmet.setContainedItems(helmet, upgrades);
			IIContent.itemLightEngineerHelmet.recalculateUpgrades(helmet);
			IIContent.itemLightEngineerHelmet.finishUpgradeRecalculation(helmet);
			 */
		}
		else if(id==8)
		{
			ItemStack stack = new ItemStack(IIContent.itemBinoculars);
			setHeldItem(EnumHand.MAIN_HAND, stack);
			setSneaking(true);
		}

		setItemStackToSlot(EntityEquipmentSlot.HEAD, helmet);

	}

	@Override
	protected void initEntityAI()
	{
		super.initEntityAI();

		//Attack mobs
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityMob.class, true,true));
		//Attack players and Hanses with different team, stay neutral on default
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityHans.class, 40, true, true,
				input -> input!=null&&input.getTeam()!=this.getTeam()
		));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, 40, true, true,
				input -> input!=null&&input.getTeam()!=this.getTeam()
		));

		this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
		this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityGasCloud.class, 8.0F, 0.6D, 0.6D));
		this.tasks.addTask(3, new AIHansSubmachinegun(this, 1f, 6, 15));
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1f, true));

		/*
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityHans.class, 3.0F));
		this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 0.3D, 1f));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		 */

		this.tasks.addTask(0, new EntityAISwimming(this));

		//this.tasks.addTask(4, new EntityAIAvoidEntity<>(this, EntityLivingBase.class, avEntity-> this.hasAmmunition()&&avEntity!=null&&avEntity.getRevengeTarget()==this, 8.0F, 0.6D, 0.6D));


	}

	@Override
	public boolean startRiding(Entity entityIn)
	{
		boolean b = super.startRiding(entityIn);
		if(b)
		{
			if(entityIn instanceof EntityVehicleSeat)
			{
				if(entityIn.getRidingEntity() instanceof EntityFieldHowitzer)
				{
					tasks.addTask(1, new AIHansHowitzer((EntityFieldHowitzer)entityIn.getRidingEntity(), ((EntityVehicleSeat)entityIn).seatID));
				}
			}
		}
		return b;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if(facing==null) return (T)joinedHandler;
			else if(facing.getAxis().isVertical()) return (T)handHandler;
			else if(facing.getAxis().isHorizontal()) return (T)armorHandler;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		readInventory(compound.getTagList("npc_inventory", 10));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setTag("npc_inventory", Utils.writeInventory(mainInventory));
	}

	private void readInventory(NBTTagList npc_inventory)
	{
		int max = npc_inventory.tagCount();
		for(int i = 0; i < max; i++)
		{
			NBTTagCompound itemTag = npc_inventory.getCompoundTagAt(i);
			int slot = itemTag.getByte("Slot")&255;
			if(slot >= 0&&slot < this.mainInventory.size())
				this.mainInventory.set(slot, new ItemStack(itemTag));
		}
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20f);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24f);
	}

	public boolean attackEntityAsMob(Entity entityIn)
	{
		this.world.setEntityState(this, (byte)4);
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(7+this.rand.nextInt(15)));

		if(flag)
		{
			entityIn.motionY += 0.4000000059604645D;
			this.applyEnchantments(this, entityIn);
		}

		this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, 1.0F, 1.0F);
		return flag;
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		return hand==EnumHand.MAIN_HAND&&player.getPositionVector().distanceTo(this.getPositionVector()) <= 1d;
	}

	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand)
	{
		if(hand==EnumHand.OFF_HAND)
			return EnumActionResult.FAIL;
		if(!world.isRemote)
		{
			faceEntity(player, 360f, 360f);

			ItemStack heldItem = player.getHeldItem(hand);
			if(heldItem.isEmpty())
			{
				player.sendMessage(new TextComponentString(
						String.format("Guten %1$s, %2$s!",
								getGermanTimeName(world.getWorldTime()),
								player.getName()
						)));
			}
			else if(IIContent.itemSubmachinegun.isAmmo(heldItem, getActiveItemStack()))
			{
				int i = 0;
				while(i < mainInventory.size()&&!heldItem.isEmpty())
				{
					heldItem = invHandler.insertItem(i, heldItem, true);
					i++;
				}
				if(heldItem.isEmpty())
					player.sendMessage(new TextComponentString("Danke für diese neue Patronen. Meine Maschinenpistole war sehr hungrig!"));
				else
					player.sendMessage(new TextComponentString("Danke für diese neue Patronen, aber habe ich sie genug."));
			}
			player.setHeldItem(EnumHand.MAIN_HAND, heldItem);
			player.swingArm(hand);

		}
		return EnumActionResult.PASS;
	}

	private String getGermanTimeName(long time)
	{
		if(time < 11500)
			return "Morgen";
		else if(time < 18000)
			return "Tag";
		else
			return "Abend";
	}

	public boolean isValidTarget(Entity entity)
	{
		return entity instanceof EntityZombie||((entity instanceof EntityPlayer||entity instanceof EntityHans)&&entity.getTeam()!=this.getTeam());
	}
}
