package pl.pabilo8.immersiveintelligence.common.crafting;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.Config.IEConfig.Tools;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import lombok.RequiredArgsConstructor;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;
import pl.pabilo8.immersiveintelligence.common.item.tools.backpack.ItemIIBackpackBase;

import java.util.Arrays;

/**
 * Attaches a backpack to a chestplate or removes it from one.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.11.2026
 */
@RequiredArgsConstructor
public class RecipeBackpackArmor extends Impl<IRecipe> implements IRecipe
{
	private final ItemIIBackpackBase backpackItem;

	@Override
	public boolean matches(InventoryCrafting inventory, World world)
	{
		return !getCraftingResult(inventory).isEmpty();
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory)
	{
		ItemStack armor = ItemStack.EMPTY;
		ItemStack backpack = ItemStack.EMPTY;
		for(int slot = 0; slot < inventory.getSizeInventory(); slot++)
		{
			ItemStack stack = inventory.getStackInSlot(slot);
			if(stack.isEmpty())
				continue;
			if(backpack.isEmpty()&&stack.getItem()==backpackItem)
				backpack = stack;
			else if(armor.isEmpty()&&isValidArmor(stack))
				armor = stack;
			else
				return ItemStack.EMPTY;
		}

		if(armor.isEmpty())
			return ItemStack.EMPTY;
		ItemStack attached = ItemIIBackpackBase.getAttachedBackpack(armor);
		if(backpack.isEmpty()?attached.getItem()!=backpackItem:
				!attached.isEmpty()||ItemNBTHelper.hasKey(armor, Lib.NBT_Powerpack))
			return ItemStack.EMPTY;

		ItemStack output = armor.copy();
		output.setCount(1);
		ItemIIBackpackBase.setAttachedBackpack(output, backpack);
		return output;
	}

	@Override
	public boolean canFit(int width, int height)
	{
		return width*height >= 2;
	}

	@Override
	public boolean isDynamic()
	{
		return true;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventory)
	{
		NonNullList<ItemStack> remaining = NonNullList.withSize(inventory.getSizeInventory(), ItemStack.EMPTY);
		for(int slot = 0; slot < inventory.getSizeInventory(); slot++)
		{
			ItemStack attached = ItemIIBackpackBase.getAttachedBackpack(inventory.getStackInSlot(slot));
			if(attached.getItem()==backpackItem)
				remaining.set(slot, attached);
		}
		return remaining;
	}

	private boolean isValidArmor(ItemStack stack)
	{
		if(!(stack.getItem() instanceof ItemArmor)||((ItemArmor)stack.getItem()).armorType!=EntityEquipmentSlot.CHEST
				||stack.getItem() instanceof ItemIIBackpackBase||stack.getItem()==IEContent.itemPowerpack)
			return false;
		String name = stack.getItem().getRegistryName().toString();
		return Arrays.asList(Tools.powerpack_whitelist).contains(name)||!Arrays.asList(Tools.powerpack_blacklist).contains(name);
	}
}
