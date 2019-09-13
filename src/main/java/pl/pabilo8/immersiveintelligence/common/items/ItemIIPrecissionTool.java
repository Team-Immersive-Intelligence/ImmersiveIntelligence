package pl.pabilo8.immersiveintelligence.common.items;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.IPrecissionTool;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecissionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.PrecissionAssemblerRenderer;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.tools;

/**
 * Created by Pabilo8 on 19-08-2019.
 */
public class ItemIIPrecissionTool extends ItemIIBase implements IPrecissionTool
{
	public ItemIIPrecissionTool()
	{
		super("precission_tool", 1, "precission_buzzsaw", "precission_drill", "precission_inserter", "precission_solderer", "precission_welder");
		PrecissionAssemblerRecipe.registerToolType("buzzsaw", this);
		PrecissionAssemblerRecipe.registerToolType("drill", this);
		PrecissionAssemblerRecipe.registerToolType("inserter", this);
		PrecissionAssemblerRecipe.registerToolType("solderer", this);
		PrecissionAssemblerRecipe.registerToolType("welder", this);
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		super.onCreated(stack, worldIn, playerIn);
		ItemNBTHelper.setInt(stack, "damage", getPrecissionToolMaxDamage(stack));
	}

	@Override
	public String getPrecissionToolType(ItemStack stack)
	{
		//uses "welder" instead "precission_welder" for shorter crafttweaker scripts ^^
		return subNames[stack.getMetadata()].replaceAll("precission_", "");
	}

	@Override
	public void damagePrecissionTool(ItemStack stack, int amount)
	{
		if(!ItemNBTHelper.hasKey(stack, "damage"))
			ItemNBTHelper.setInt(stack, "damage", getPrecissionToolMaxDamage(stack));

		ItemNBTHelper.setInt(stack, "damage", getPrecissionToolDamage(stack)-amount);

		if(getPrecissionToolDamage(stack) < 0)
			stack.setCount(0);
	}

	@Override
	public int getPrecissionToolDamage(ItemStack stack)
	{
		return ItemNBTHelper.getInt(stack, "damage");
	}

	@Override
	public int getPrecissionToolMaxDamage(ItemStack stack)
	{
		switch(subNames[stack.getMetadata()])
		{
			case "precission_buzzsaw":
			{
				return tools.precission_tool_buzzsaw_durability;
			}
			case "precission_drill":
			{
				return tools.precission_tool_drill_durability;
			}
			case "precission_inserter":
			{
				return tools.precission_tool_inserter_durability;
			}
			case "precission_solderer":
			{
				return tools.precission_tool_solderer_durability;
			}
			case "precission_welder":
			{
				return tools.precission_tool_welder_durability;
			}
		}
		return -1;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return ItemNBTHelper.hasKey(stack, "damage")&&((double)getPrecissionToolDamage(stack)/(double)getPrecissionToolMaxDamage(stack))!=1f;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return 1d-((double)getPrecissionToolDamage(stack)/(double)getPrecissionToolMaxDamage(stack));
	}

	@Override
	public int getWorkTime(String tool_name)
	{
		switch(tool_name)
		{
			case "buzzsaw":
			{
				return tools.precission_tool_buzzsaw_usage_time;
			}
			case "drill":
			{
				return tools.precission_tool_drill_usage_time;
			}
			case "inserter":
			{
				return tools.precission_tool_inserter_usage_time;
			}
			case "solderer":
			{
				return tools.precission_tool_solderer_usage_time;
			}
			case "welder":
			{
				return tools.precission_tool_welder_usage_time;
			}
		}
		return -1;
	}

	@Override
	public ItemStack getToolPresentationStack(String tool_name)
	{
		return new ItemStack(this, 1, getMetaBySubname("precission_"+tool_name));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInMachine(ItemStack stack, float progress, float angle, float maxProgress, ItemStack renderedStack)
	{
		switch(subNames[stack.getMetadata()])
		{
			case "precission_buzzsaw":
			{
				PrecissionAssemblerRenderer.modelBuzzsaw.renderProgress(progress, angle, maxProgress);
				return;
			}
			case "precission_drill":
			{
				PrecissionAssemblerRenderer.modelDrill.renderProgress(progress, angle, maxProgress);
				return;
			}
			case "precission_inserter":
			{
				PrecissionAssemblerRenderer.modelInserter.renderProgress(progress, angle, maxProgress, renderedStack);
				return;
			}
			case "precission_solderer":
			{
				PrecissionAssemblerRenderer.modelSolderer.renderProgress(progress, angle, maxProgress);
				return;
			}
			case "precission_welder":
			{
				PrecissionAssemblerRenderer.modelWelder.renderProgress(progress, angle, maxProgress);
				return;
			}
		}
	}


}
