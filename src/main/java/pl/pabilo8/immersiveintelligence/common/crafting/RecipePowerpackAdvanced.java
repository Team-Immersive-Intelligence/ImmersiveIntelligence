package pl.pabilo8.immersiveintelligence.common.crafting;

import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import pl.pabilo8.immersiveintelligence.common.IIContent;

/**
 * @author Pabilo8
 * @since 15.04.2021
 */
public class RecipePowerpackAdvanced extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		ItemStack powerpack = ItemStack.EMPTY;
		ItemStack armor = ItemStack.EMPTY;
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stackInSlot = inv.getStackInSlot(i);
			if(!stackInSlot.isEmpty())
				if(powerpack.isEmpty()&&IIContent.itemAdvancedPowerPack.equals(stackInSlot.getItem()))
					powerpack = stackInSlot;
				else if(armor.isEmpty()&&isValidArmor(stackInSlot))
					armor = stackInSlot;
				else
					return false;
		}
		if(!powerpack.isEmpty()&&(!armor.isEmpty()||!ItemNBTHelper.hasKey(armor, IIContent.NBT_AdvancedPowerpack)))
			return true;
		else return !armor.isEmpty()&&ItemNBTHelper.hasKey(armor, IIContent.NBT_AdvancedPowerpack)&&powerpack.isEmpty();
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack powerpack = ItemStack.EMPTY;
		ItemStack armor = ItemStack.EMPTY;

		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stackInSlot = inv.getStackInSlot(i);
			if(!stackInSlot.isEmpty())
				if(powerpack.isEmpty()&&IIContent.itemAdvancedPowerPack.equals(stackInSlot.getItem()))
					powerpack = stackInSlot;
				else if(armor.isEmpty()&&isValidArmor(stackInSlot))
					armor = stackInSlot;
		}

		if(!powerpack.isEmpty())
		{
			ItemStack output;
			if(!armor.isEmpty())
			{
				output = armor.copy();
				ItemNBTHelper.setItemStack(output, IIContent.NBT_AdvancedPowerpack, powerpack.copy());
			}
			else
				output = powerpack.copy();
			return output;
		}
		else if(!armor.isEmpty()&&ItemNBTHelper.hasKey(armor, IIContent.NBT_AdvancedPowerpack))
		{
			ItemStack output = armor.copy();
			ItemNBTHelper.remove(output, IIContent.NBT_AdvancedPowerpack);
			return output;
		}
		return ItemStack.EMPTY;
	}

	/**
	 * Used to determine if this recipe can fit in a grid of the given width/height
	 */
	@Override
	public boolean canFit(int width, int height)
	{
		return width >= 2&&height >= 2;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return new ItemStack(IIContent.itemAdvancedPowerPack, 1, 0);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		NonNullList<ItemStack> remaining = ForgeHooks.defaultRecipeGetRemainingItems(inv);
		for(int i = 0; i < remaining.size(); i++)
		{
			ItemStack stackInSlot = inv.getStackInSlot(i);
			if(!stackInSlot.isEmpty()&&ItemNBTHelper.hasKey(stackInSlot, IIContent.NBT_AdvancedPowerpack))
				remaining.set(i, ItemNBTHelper.getItemStack(stackInSlot, IIContent.NBT_AdvancedPowerpack));
		}
		return remaining;
	}

	private boolean isValidArmor(ItemStack stack)
	{
		if(!(stack.getItem() instanceof ItemArmor)||((ItemArmor)stack.getItem()).armorType!=EntityEquipmentSlot.CHEST)
			return false;
		if(stack.getItem()==IEContent.itemPowerpack)
			return false;
		if(stack.getItem()==IIContent.itemAdvancedPowerPack)
			return false;
		String regName = stack.getItem().getRegistryName().toString();
		for(String s : Config.IEConfig.Tools.powerpack_whitelist)
			if(regName.equals(s))
				return true;
		for(String s : Config.IEConfig.Tools.powerpack_blacklist)
			if(regName.equals(s))
				return false;
		return true;
	}
}