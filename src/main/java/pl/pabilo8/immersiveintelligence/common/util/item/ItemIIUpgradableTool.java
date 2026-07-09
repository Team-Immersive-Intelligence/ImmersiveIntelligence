package pl.pabilo8.immersiveintelligence.common.util.item;

import blusunrize.immersiveengineering.common.items.ItemUpgradeableTool;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 27.01.2023
 */
public abstract class ItemIIUpgradableTool extends ItemUpgradeableTool
{
	public ItemIIUpgradableTool(String name, int stackSize, String upgradeType, String... subNames)
	{
		super(name, stackSize, upgradeType, subNames);
	}

	/**
	 * @param stack   stack to check
	 * @param upgrade upgrade
	 * @return whether the stack has this upgrade installed
	 */
	public boolean hasIIUpgrade(ItemStack stack, IIItemEnum upgrade)
	{
		return getUpgrades(stack).hasKey(upgrade.getName());
	}

	/**
	 * @param stack   stack to check
	 * @param upgrade list of upgrades
	 * @return whether the stack has <b>ANY</b> of the upgrades listed
	 */
	public boolean hasIIUpgrade(ItemStack stack, IIItemEnum... upgrade)
	{
		NBTTagCompound upgrades = getUpgrades(stack);
		for(IIItemEnum e : upgrade)
			if(upgrades.hasKey(e.getName()))
				return true;
		return false;
	}

	/**
	 * @param stack   stack to check
	 * @param upgrade list of upgrades
	 * @return whether the stack has <b>ALL</b> of the upgrades listed
	 */
	public boolean hasIIUpgrades(ItemStack stack, IIItemEnum... upgrade)
	{
		NBTTagCompound upgrades = getUpgrades(stack);
		for(IIItemEnum e : upgrade)
			if(!upgrades.hasKey(e.getName()))
				return false;
		return true;
	}

	/**
	 * @param stack   stack to check
	 * @param upgrade upgrade type
	 * @return list of all installed upgrades of the given type
	 */
	public <E extends Enum<E> & IIItemEnum> EnumSet<E> listUpgrades(ItemStack stack, Class<E> upgrade)
	{
		NBTTagCompound upgrades = getUpgrades(stack);
		List<E> collect = Arrays.stream(upgrade.getEnumConstants())
				.filter(enumConstant -> upgrades.hasKey(enumConstant.getName()))
				.collect(Collectors.toList());
		return collect.isEmpty()?EnumSet.noneOf(upgrade): EnumSet.copyOf(collect);
	}


}
