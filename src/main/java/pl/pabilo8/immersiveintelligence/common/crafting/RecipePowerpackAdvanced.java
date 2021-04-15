package pl.pabilo8.immersiveintelligence.common.crafting;

import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IColouredItem;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.Lists;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.items.tools.ItemIIAdvancedPowerPack;

import java.util.List;

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
		List<ItemStack> list = Lists.newArrayList();
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stackInSlot = inv.getStackInSlot(i);
			if(!stackInSlot.isEmpty())
				if(powerpack.isEmpty()&&IIContent.itemAdvancedPowerPack.equals(stackInSlot.getItem()))
					powerpack = stackInSlot;
				else if(armor.isEmpty()&&isValidArmor(stackInSlot))
					armor = stackInSlot;
				else if(Utils.isDye(stackInSlot))
					list.add(stackInSlot);
				else
					return false;
		}
		if(!powerpack.isEmpty()&&(!armor.isEmpty()||!list.isEmpty())&&!ItemNBTHelper.hasKey(armor, IIContent.NBT_AdvancedPowerpack))
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

		int[] colourArray = new int[3];
		int j = 0;
		int totalColourSets = 0;
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stackInSlot = inv.getStackInSlot(i);
			if(!stackInSlot.isEmpty())
				if(powerpack.isEmpty()&&IIContent.itemAdvancedPowerPack.equals(stackInSlot.getItem()))
				{
					powerpack = stackInSlot;
					int colour = IIContent.itemAdvancedPowerPack.getColor(powerpack);
					float r = (float)(colour >> 16&255)/255.0F;
					float g = (float)(colour >> 8&255)/255.0F;
					float b = (float)(colour&255)/255.0F;
					j = (int)((float)j+Math.max(r, Math.max(g, b))*255.0F);
					colourArray[0] = (int)((float)colourArray[0]+r*255.0F);
					colourArray[1] = (int)((float)colourArray[1]+g*255.0F);
					colourArray[2] = (int)((float)colourArray[2]+b*255.0F);
					++totalColourSets;
				}
				else if(Utils.isDye(stackInSlot))
				{
					float[] afloat = EntitySheep.getDyeRgb(EnumDyeColor.byDyeDamage(Utils.getDye(stackInSlot)));
					int r = (int)(afloat[0]*255.0F);
					int g = (int)(afloat[1]*255.0F);
					int b = (int)(afloat[2]*255.0F);
					j += Math.max(r, Math.max(g, b));
					colourArray[0] += r;
					colourArray[1] += g;
					colourArray[2] += b;
					++totalColourSets;
				}
				else if(armor.isEmpty()&&stackInSlot.getItem() instanceof ItemArmor&&((ItemArmor)stackInSlot.getItem()).armorType==EntityEquipmentSlot.HEAD&&!IIContent.itemAdvancedPowerPack.equals(stackInSlot.getItem()))
					armor = stackInSlot;
		}

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
			if(totalColourSets > 1)
			{
				int r = colourArray[0]/totalColourSets;
				int g = colourArray[1]/totalColourSets;
				int b = colourArray[2]/totalColourSets;
				float colourMod = (float)j/(float)totalColourSets;
				float highestColour = (float)Math.max(r, Math.max(g, b));
				r = (int)((float)r*colourMod/highestColour);
				g = (int)((float)g*colourMod/highestColour);
				b = (int)((float)b*colourMod/highestColour);
				int newColour = (r<<8)+g;
				newColour = (newColour<<8)+b;
				ItemNBTHelper.setInt(powerpack, ItemIIAdvancedPowerPack.NBT_Colour, newColour);
			}
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