package pl.pabilo8.immersiveintelligence.common.items;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecissionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 25-06-2019
 */
public class ItemIIAssemblyScheme extends ItemIIBase
{
	public ItemIIAssemblyScheme()
	{
		super("assembly_scheme", 8);
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		ItemStack s = ItemNBTHelper.getItemStack(stack, "recipeItem");
		list.add(I18n.format(CommonProxy.description_key+"assembly_scheme.used_to_create", s.getDisplayName()+(s.getCount() > 1?" x"+s.getCount(): "")));
		list.add(I18n.format(CommonProxy.description_key+"assembly_scheme.items_created", ItemNBTHelper.getInt(stack, "createdItems")));

		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			list.add(I18n.format(CommonProxy.description_key+"assembly_scheme.materials"));
			PrecissionAssemblerRecipe recipe = getRecipeForStack(stack);
			if(recipe!=null)
			{
				for(IngredientStack ingredient : recipe.inputs)
					list.add(ingredient.getExampleStack().getDisplayName()+(ingredient.inputSize > 1?" x"+ingredient.inputSize: ""));
			}
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)||Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
		{
			list.add(I18n.format(CommonProxy.description_key+"assembly_scheme.tools"));
			PrecissionAssemblerRecipe recipe = getRecipeForStack(stack);
			if(recipe!=null)
			{
				for(String tool : recipe.tools)
					list.add(PrecissionAssemblerRecipe.getExampleToolStack(tool).getDisplayName());
			}
		}
		else
		{
			list.add(I18n.format(CommonProxy.description_key+"assembly_scheme.info_hold1"));
			list.add(I18n.format(CommonProxy.description_key+"assembly_scheme.info_hold2"));
		}

	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		super.onCreated(stack, worldIn, playerIn);
		if(!ItemNBTHelper.hasKey(stack, "createdItems"))
			ItemNBTHelper.setInt(stack, "createdItems", 0);
	}

	public ItemStack getStackForRecipe(PrecissionAssemblerRecipe recipe)
	{
		ItemStack stack = new ItemStack(this);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("recipeItem", recipe.output.serializeNBT());
		stack.setTagCompound(tag);
		return stack;
	}

	public ItemStack getProducedStack(ItemStack scheme)
	{
		if(ItemNBTHelper.hasKey(scheme, "recipeItem"))
			return new ItemStack(ItemNBTHelper.getTagCompound(scheme, "recipeItem"));
		return ItemStack.EMPTY;
	}

	@Nullable
	public PrecissionAssemblerRecipe getRecipeForStack(ItemStack stack)
	{
		NBTTagCompound tag = ItemNBTHelper.getTagCompound(stack, "recipeItem");
		ItemStack recipe_stack = new ItemStack(tag);

		for(PrecissionAssemblerRecipe recipe : PrecissionAssemblerRecipe.recipeList)
		{
			if(recipe.output.isItemEqual(recipe_stack))
				return recipe;
		}

		return null;
	}

	public void increaseCreatedItems(ItemStack stack, int amount)
	{
		ItemNBTHelper.setInt(stack, "createdItems", ItemNBTHelper.getInt(stack, "createdItems")+amount);
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		if(this.isInCreativeTab(tab))
			for(PrecissionAssemblerRecipe recipe : PrecissionAssemblerRecipe.recipeList)
			{
				ItemStack stack = new ItemStack(this);
				ItemNBTHelper.setTagCompound(stack, "recipeItem", recipe.output.serializeNBT());
				list.add(stack);
			}
	}
}
