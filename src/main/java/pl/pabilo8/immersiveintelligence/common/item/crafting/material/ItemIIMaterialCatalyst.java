package pl.pabilo8.immersiveintelligence.common.item.crafting.material;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialCatalyst.MaterialsCatalyst;
import pl.pabilo8.immersiveintelligence.common.util.IBatchOredictRegister;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
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
 * @since 11.05.2019
 */
@IBatchOredictRegister(oreDict = "catalyst")
@IIItemProperties(category = IICategory.RESOURCES)
public class ItemIIMaterialCatalyst extends ItemIISubItemsBase<MaterialsCatalyst>
{
	public ItemIIMaterialCatalyst()
	{
		super("material_catalyst", 1, MaterialsCatalyst.values());
	}

	public String getToolID(ItemStack stack)
	{
		return getSubNames()[stackToSub(stack).ordinal()];
	}

	public int getToolMaxDamage(ItemStack stack)
	{
		return stackToSub(stack).durability;
	}

	public int getToolDamage(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "damage"))
			return getToolMaxDamage(stack);
		return ItemNBTHelper.getInt(stack, "damage");
	}

	public void damageTool(ItemStack stack, int amount)
	{
		if(!ItemNBTHelper.hasKey(stack, "damage"))
			ItemNBTHelper.setInt(stack, "damage", getToolMaxDamage(stack));

		ItemNBTHelper.setInt(stack, "damage", getToolDamage(stack)-amount);

		if(getToolDamage(stack) < 0)
			stack.setCount(0);
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

	@GeneratedItemModels(itemName = "material_catalyst", texturePath = "material/catalyst")
	public enum MaterialsCatalyst implements IIItemEnum
	{
		COPPER(Tools.catalystCopperDurability),
		IRON(Tools.catalystIronDurability),
		NICKEL(Tools.catalystNickelDurability),
		PLATINUM(Tools.catalystPlatinumDurability);

		private final int durability;

		MaterialsCatalyst(int durability)
		{
			this.durability = durability;
		}
	}
}
