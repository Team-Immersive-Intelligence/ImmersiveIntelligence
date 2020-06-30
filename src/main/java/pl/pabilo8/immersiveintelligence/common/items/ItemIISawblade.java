package pl.pabilo8.immersiveintelligence.common.items;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.api.utils.ISawblade;

/**
 * @author Pabilo8
 * @since 19-08-2019
 */
public class ItemIISawblade extends ItemIIBase implements ISawblade
{
	public ItemIISawblade()
	{
		super("sawblade", 1, "iron", "steel", "tungsten");
		SawmillRecipe.registerSawblade("iron", this);
		SawmillRecipe.registerSawblade("steel", this);
		SawmillRecipe.registerSawblade("tungsten", this);
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		super.onCreated(stack, worldIn, playerIn);
		ItemNBTHelper.setInt(stack, "damage", getSawbladeMaxDamage(stack));
	}

	@Override
	public String getMaterialName(ItemStack stack)
	{
		return subNames[stack.getMetadata()];
	}

	@Override
	public void damageSawblade(ItemStack stack, int amount)
	{
		if(!ItemNBTHelper.hasKey(stack, "damage"))
			ItemNBTHelper.setInt(stack, "damage", getSawbladeMaxDamage(stack));

		ItemNBTHelper.setInt(stack, "damage", getSawbladeDamage(stack)-amount);

		if(getSawbladeDamage(stack) < 0)
			stack.setCount(0);
	}

	@Override
	public int getSawbladeDamage(ItemStack stack)
	{
		return ItemNBTHelper.getInt(stack, "damage");
	}

	@Override
	public int getSawbladeMaxDamage(ItemStack stack)
	{
		switch(subNames[stack.getMetadata()])
		{
			case "iron":
			{
				return Tools.sawblade_iron_durability;
			}
			case "steel":
			{
				return Tools.sawblade_steel_durability;
			}
			case "tungsten":
			{
				return Tools.sawblade_tungsten_durability;
			}
		}
		return -1;
	}

	@Override
	public int getHardness(ItemStack stack)
	{
		switch(subNames[stack.getMetadata()])
		{
			/*
			case "bronze":
			{
				return 1;
			}
			 */
			case "iron":
			{
				return 2;
			}
			case "steel":
			{
				return 3;
			}
			case "tungsten":
			{
				return 4;
			}
			/*
			case "titanium":
			{
				return 5;
			}
			 */
		}
		return -1;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return ItemNBTHelper.hasKey(stack, "damage")&&((double)getSawbladeDamage(stack)/(double)getSawbladeDamage(stack))!=1f;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return 1d-((double)getSawbladeDamage(stack)/(double)getSawbladeDamage(stack));
	}

	@Override
	public ItemStack getToolPresentationStack(String tool_name)
	{
		return new ItemStack(this, 1, getMetaBySubname(tool_name));
	}

	public int getSawbladeDisplayMeta(ItemStack stack)
	{
		switch(subNames[stack.getMetadata()])
		{
			/*
			case "bronze":
			{
				return ClientProxy.model_saw_bronze;
			}
			 */
			case "iron":
			{
				return 8;
			}
			case "steel":
			{
				return 9;
			}
			case "tungsten":
			{
				return 10;
			}
			/*
			case "titanium":
			{
				return ClientProxy.model_saw_titanium;
			}
			 */
		}
		return 0;
	}

}
