package pl.pabilo8.immersiveintelligence.common.entity.hans;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.EntityParachute;
import pl.pabilo8.immersiveintelligence.common.items.armor.ItemIIUpgradeableArmor;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIArmorUpgrade.ArmorUpgrades;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIWeaponUpgrade.WeaponUpgrades;

/**
 * @author Pabilo8
 * @since 12.05.2021
 */
public class HansUtils
{
	private static void setArmor(EntityHans hans, EntityEquipmentSlot slot, ItemIIUpgradeableArmor item, ArmorUpgrades... armorUpgrades)
	{
		ItemStack stack = new ItemStack(item);
		int i = item.getSlotCount();
		NonNullList<ItemStack> upgrades = NonNullList.withSize(i, ItemStack.EMPTY);
		for(ArmorUpgrades upgrade : armorUpgrades)
		{
			upgrades.set(--i, new ItemStack(IIContent.itemArmorUpgrade, 1, upgrade.ordinal()));
			if(i==0)
				break;
		}
		item.setContainedItems(stack, upgrades);
		item.recalculateUpgrades(stack);
		item.finishUpgradeRecalculation(stack);
		hans.setItemStackToSlot(slot, stack);
	}

	public static void setHelmet(EntityHans hans, ArmorUpgrades... armorUpgrades)
	{
		setArmor(hans, EntityEquipmentSlot.HEAD, IIContent.itemLightEngineerHelmet, armorUpgrades);
	}

	public static void setSubmachinegun(EntityHans hans, ItemStack magazine, WeaponUpgrades... weaponUpgrades)
	{
		ItemStack stack = new ItemStack(IIContent.itemSubmachinegun);
		ItemNBTHelper.setItemStack(stack, "magazine", magazine);
		int i = IIContent.itemSubmachinegun.getSlotCount(stack);
		NonNullList<ItemStack> upgrades = NonNullList.withSize(i, ItemStack.EMPTY);
		for(WeaponUpgrades upgrade : weaponUpgrades)
		{
			upgrades.set(--i, new ItemStack(IIContent.itemWeaponUpgrade, 1, upgrade.ordinal()));
			if(i==0)
				break;
		}
		IIContent.itemSubmachinegun.setContainedItems(stack, upgrades);
		IIContent.itemSubmachinegun.recalculateUpgrades(stack);
		IIContent.itemSubmachinegun.finishUpgradeRecalculation(stack);
		hans.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
	}

	public static void setParachute(EntityHans hans)
	{
		EntityParachute para = new EntityParachute(hans.getEntityWorld());
		para.setPosition(hans.posX,hans.posY,hans.posZ);
		hans.world.spawnEntity(para);
		hans.startRiding(para);
	}
}
