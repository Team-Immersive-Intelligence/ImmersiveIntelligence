package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.hans_tasks.AIHansHowitzer;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.ItemIIBulletMagazine;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIWeaponUpgrade.WeaponUpgrades;

/**
 * @author Pabilo8
 * @since 11.01.2021
 */
public class EntityHans extends EntityCreature implements IRangedAttackMob
{
	public EntityHans(World worldIn)
	{
		super(worldIn);
		equipItems(2);
		this.enablePersistence();
	}

	public void equipItems(int id)
	{
		setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(IIContent.itemLightEngineerHelmet));
		if(id < 2)
		{
			ItemStack stack = new ItemStack(IIContent.itemSubmachinegun);
			NonNullList<ItemStack> upgrades = NonNullList.withSize(2, ItemStack.EMPTY);
			upgrades.set(0, new ItemStack(IIContent.itemWeaponUpgrade, 1, WeaponUpgrades.SUPPRESSOR.ordinal()));
			upgrades.set(1, new ItemStack(IIContent.itemWeaponUpgrade, 1, WeaponUpgrades.BOTTOM_LOADING.ordinal()));
			IIContent.itemSubmachinegun.setContainedItems(stack, upgrades);
			IIContent.itemSubmachinegun.recalculateUpgrades(stack);
			IIContent.itemSubmachinegun.finishUpgradeRecalculation(stack);
			ItemStack magazine = ItemIIBulletMagazine.getMagazine(id==0?"submachinegun": "submachinegun_drum", IIContent.itemAmmoSubmachinegun.getBulletWithParams("core_brass", "piercing", "white_phosphorus"));
			ItemNBTHelper.setItemStack(stack, "magazine", magazine);
			setHeldItem(EnumHand.MAIN_HAND, stack);
		}
		else if(id==8)
		{
			ItemStack stack = new ItemStack(IIContent.itemBinoculars);
			setHeldItem(EnumHand.MAIN_HAND, stack);
			setSneaking(true);
		}
	}

	@Override
	protected void initEntityAI()
	{
		super.initEntityAI();
		this.tasks.addTask(0, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(1, new EntityAILookIdle(this));
		this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 0.3D, 1f));
		this.tasks.addTask(4, new EntityAIAvoidEntity<>(this, EntityGasCloud.class, 8.0F, 0.6D, 0.6D));
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
	{

	}

	@Override
	public void setSwingingArms(boolean swingingArms)
	{

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
}
