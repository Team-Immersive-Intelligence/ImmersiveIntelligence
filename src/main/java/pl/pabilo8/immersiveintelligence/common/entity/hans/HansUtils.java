package pl.pabilo8.immersiveintelligence.common.entity.hans;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.NonNullList;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.gate.TileEntityGateBase;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.EntityParachute;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.hand_weapon.*;
import pl.pabilo8.immersiveintelligence.common.items.armor.ItemIIArmorUpgrade.ArmorUpgrades;
import pl.pabilo8.immersiveintelligence.common.items.armor.ItemIIUpgradeableArmor;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIWeaponUpgrade.WeaponUpgrades;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Function;

/**
 * @author Pabilo8
 * @since 12.05.2021
 */
public class HansUtils
{
	public static final HashMap<Item, Function<EntityHans, AIHansHandWeapon>> WEAPON_MAP = new HashMap<>();

	public static void init()
	{
		WEAPON_MAP.put(IEContent.itemRevolver, AIHansRevolver::new);
		WEAPON_MAP.put(IIContent.itemSubmachinegun, AIHansSubmachinegun::new);
		WEAPON_MAP.put(IEContent.itemRailgun, AIHansRailgun::new);
		WEAPON_MAP.put(IEContent.itemChemthrower, AIHansChemthrower::new);
		WEAPON_MAP.put(IIContent.itemBinoculars, AIHansBinoculars::new);
		WEAPON_MAP.put(IIContent.itemGrenade, AIHansGrenade::new);
	}

	@Nullable
	public static AIHansHandWeapon getHandWeaponTask(EntityHans hans)
	{
		return WEAPON_MAP.entrySet()
				.stream()
				.filter(entry -> entry.getKey()==hans.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem())
				.findFirst()
				.map(Entry::getValue)
				.map(fun -> fun.apply(hans))
				.orElse(null);
	}

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

	public static void setParachute(Entity entity)
	{
		EntityParachute para = new EntityParachute(entity.getEntityWorld());
		para.setPosition(entity.posX, entity.posY, entity.posZ);
		entity.world.spawnEntity(para);
		entity.startRiding(para);
	}

	public static String getGermanTimeName(long time)
	{
		if(time < 11500)
			return "Morgen";
		else if(time < 18000)
			return "Tag";
		else
			return "Abend";
	}

	static PathNodeType getDoorNode(IBlockState state)
	{
		boolean open = state.getValue(BlockDoor.OPEN);
		if(!open)
			return state.getMaterial()==Material.WOOD?PathNodeType.DOOR_WOOD_CLOSED: PathNodeType.DOOR_IRON_CLOSED;
		return PathNodeType.DOOR_OPEN;
	}

	static PathNodeType getGateNode(TileEntityGateBase<?> te)
	{
		return te.isDoorPart()?(te.open?PathNodeType.DOOR_OPEN: PathNodeType.DOOR_WOOD_CLOSED): PathNodeType.BLOCKED;
	}

	static PathNodeType getFenceGateNode(IBlockState state)
	{
		boolean open = state.getValue(BlockFenceGate.OPEN);
		if(!open)
			return state.getMaterial()==Material.WOOD?PathNodeType.DOOR_WOOD_CLOSED: PathNodeType.DOOR_IRON_CLOSED;
		return PathNodeType.DOOR_OPEN;
	}
}
