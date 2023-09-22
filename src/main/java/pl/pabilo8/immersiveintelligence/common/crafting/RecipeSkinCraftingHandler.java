package pl.pabilo8.immersiveintelligence.common.crafting;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISkinnable;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler.IISpecialSkin;

/**
 * @author Pabilo8
 * @since 07.08.2021
 */
public class RecipeSkinCraftingHandler extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		return new SkinResult(inv).canCraft;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		return new SkinResult(inv).output;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		return new SkinResult(inv).remaining;
	}

	private static class SkinResult
	{
		private final boolean canCraft;
		private final NonNullList<ItemStack> remaining;
		private final ItemStack output;

		private ISkinnable skinnable;
		private ItemStack item;
		private ItemStack manual;
		int manualStack = 0;

		public SkinResult(InventoryCrafting inv)
		{
			this.manual = ItemStack.EMPTY;
			this.canCraft = process(inv);

			if(canCraft)
			{
				remaining = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
				remaining.set(manualStack, manual.copy());
				String last = ItemNBTHelper.getString(manual, "lastSkin");
				ItemStack op = item.copy();
				skinnable.applySkinnableSkin(op, last);
				output = op;
			}
			else
			{
				remaining = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
				output = ItemStack.EMPTY;
			}
		}

		private boolean process(InventoryCrafting inv)
		{
			manual = ItemStack.EMPTY;
			item = ItemStack.EMPTY;
			skinnable = null;

			for(int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack stack = inv.getStackInSlot(i);
				if(!stack.isEmpty())
				{
					if(stack.getItem()==IEContent.itemTool&&stack.getItemDamage()==3)
					{
						if(manual.isEmpty()&&ItemNBTHelper.hasKey(stack, "lastSkin"))
						{
							manual = stack;
							manualStack = i;
						}
						else
							return false;
					}
					else if(stack.getItem() instanceof ISkinnable)
					{
						if(item.isEmpty())
						{
							item = stack;
							skinnable = ((ISkinnable)stack.getItem());
						}
					}
					else
						return false;
				}
			}

			boolean result = !manual.isEmpty()&&skinnable!=null;
			if(result)
			{
				String sessionID = Minecraft.getMinecraft().getSession().getSessionID(); // Result: token:FML:X where X is the UUID
				String uuid = sessionID.substring(sessionID.lastIndexOf(':')+1), skinName = ItemNBTHelper.getString(manual, "lastSkin");
				if(IISkinHandler.isValidSkin(skinName))
				{
					IISpecialSkin skin = IISkinHandler.getSkin(skinName);
					boolean eligible = false, doesApply = skin.doesApply(skinnable.getSkinnableName());

					for(String id : skin.uuid)
					{
						if(id.replace("-", "").equals(uuid))
						{
							eligible = true;
							break;
						}
					}

					if(!eligible||!doesApply) return false;
				}
				else
				{
					return false;
				}
			}

			return result;
		}
	}

	@Override
	public boolean canFit(int width, int height)
	{
		return width >= 2||height >= 2;
	}

}
