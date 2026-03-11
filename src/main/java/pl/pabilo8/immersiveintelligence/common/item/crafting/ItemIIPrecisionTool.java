package pl.pabilo8.immersiveintelligence.common.item.crafting;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecisionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IPrecisionTool;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIPrecisionTool.PrecisionTools;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 19.08.2019
 */
@IIItemProperties(category = IICategory.TOOLS)
public class ItemIIPrecisionTool extends ItemIISubItemsBase<PrecisionTools> implements IPrecisionTool
{
	public ItemIIPrecisionTool()
	{
		super("precission_tool", 1, PrecisionTools.values());

		for(PrecisionTools e : getSubItems())
			PrecisionAssemblerRecipe.registerToolType(e.getName(), this);
	}

	@Override
	@ParametersAreNonnullByDefault
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		super.onCreated(stack, worldIn, playerIn);
		ItemNBTHelper.setInt(stack, "damage", getToolMaxDamage(stack));
	}

	@Override
	public String getToolID(ItemStack stack)
	{
		return getSubNames()[stackToSub(stack).ordinal()];
	}

	@Override
	public void damageTool(ItemStack stack, int amount)
	{
		if(!ItemNBTHelper.hasKey(stack, "damage"))
			ItemNBTHelper.setInt(stack, "damage", getToolMaxDamage(stack));

		ItemNBTHelper.setInt(stack, "damage", getToolDamage(stack)-amount);

		if(getToolDamage(stack) < 0)
			stack.setCount(0);
	}

	@Override
	public int getToolDamage(ItemStack stack)
	{
		return ItemNBTHelper.hasKey(stack, "damage")?ItemNBTHelper.getInt(stack, "damage"): getToolMaxDamage(stack);
	}

	@Override
	public int getToolMaxDamage(ItemStack stack)
	{
		return stackToSub(stack).durability;
	}

	@Override
	@ParametersAreNonnullByDefault
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(I18n.format(IIReference.INFO_KEY_TOOL_DURABILITY, TextFormatting.GOLD.toString()+getToolDamage(stack)+TextFormatting.GRAY, TextFormatting.GOLD.toString()+getToolMaxDamage(stack)+TextFormatting.GRAY));
	}

	@Override
	public boolean showDurabilityBar(@Nonnull ItemStack stack)
	{
		return ItemNBTHelper.hasKey(stack, "damage")&&((double)getToolDamage(stack)/(double)getToolMaxDamage(stack))!=1f;
	}

	@Override
	public double getDurabilityForDisplay(@Nonnull ItemStack stack)
	{
		return 1d-((double)getToolDamage(stack)/(double)getToolMaxDamage(stack));
	}

	@Override
	public int getWorkTime(String toolName)
	{
		return nameToSub(toolName).usageTime;
	}

	@Override
	@Nonnull
	public ItemStack getToolPresentationStack(@Nonnull String toolName)
	{
		return getStack(nameToSub(toolName), 1);
	}

	@Nonnull
	@Override
	public ResLoc getToolModelRes(String toolName)
	{
		return IIReference.RES_BLOCK_MODEL.with("multiblock/precision_assembler/tools/", toolName, ResLoc.EXT_OBJ);
	}

	@GeneratedItemModels(itemName = "precission_tool", texturePath = "precision_tool")
	public enum PrecisionTools implements IIItemEnum
	{
		BUZZSAW(Tools.precisionToolBuzzsawDurability, Tools.precisionToolBuzzsawUsageTime),
		DRILL(Tools.precisionToolDrillDurability, Tools.precisionToolDrillUsageTime),
		INSERTER(Tools.precisionToolInserterDurability, Tools.precisionToolInserterUsageTime),
		SOLDERER(Tools.precisionToolSoldererDurability, Tools.precisionToolSoldererUsageTime),
		WELDER(Tools.precisionToolWelderDurability, Tools.precisionToolWelderUsageTime),
		HAMMER(Tools.precisionToolHammerDurability, Tools.precisionToolHammerUsageTime);

		private final int durability;
		private final int usageTime;

		PrecisionTools(int durability, int usageTime)
		{
			this.durability = durability;
			this.usageTime = usageTime;
		}
	}
}
