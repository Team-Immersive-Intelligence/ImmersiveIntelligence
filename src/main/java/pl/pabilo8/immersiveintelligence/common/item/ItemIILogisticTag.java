package pl.pabilo8.immersiveintelligence.common.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.LogisticTag;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nullable;
import java.util.List;

/**
 * An item meant as a carrier for {@link LogisticTag Logistics Tags} used in the II logistics system.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 26.01.2026
 */
@IIItemProperties(category = IICategory.LOGISTICS, stackSize = 1)
@GeneratedItemModels(itemName = "logistic_tag", type = ItemModelType.ITEM_SIMPLE)
public class ItemIILogisticTag extends ItemIIBase
{
	public ItemIILogisticTag()
	{
		super("logistic_tag", 1);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag tooltipFlag)
	{
		LogisticTag tag = getLogisticsTag(stack);
		//It's the only item that should display that it has no logi tag
		if(tag==null)
			tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"logistic_tag.none"));
	}

	/**
	 * Returns an ItemStack of this item with the given {@link LogisticTag Logistics Tag} applied to it.
	 *
	 * @param logiTag The LogisticsTag to apply.
	 * @param amount  The amount of items in the stack.
	 * @return An ItemStack with the Logistics Tag applied.
	 */
	public ItemStack getStack(LogisticTag logiTag, int amount)
	{
		ItemStack stack = getStack(amount);
		return logiTag.applyToStack(stack);
	}

	@Nullable
	public LogisticTag getLogisticsTag(ItemStack stack)
	{
		return LogisticTag.getLogisticsTagFromStack(stack);
	}
}
