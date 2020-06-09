package pl.pabilo8.immersiveintelligence.common.items;

import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IGuiItem;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Pabilo8 on 09-07-2019.
 */
public class ItemIIPrintedPage extends ItemIIBase implements IGuiItem
{
	public ItemIIPrintedPage()
	{
		super("printed_page", 64, "blank", "text", "code", "blueprint");
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(I18n.format(CommonProxy.description_key+"printed_page."+getSubNames()[stack.getMetadata()]));
	}

	public void setText(ItemStack stack, String text)
	{
		ItemNBTHelper.setString(stack, "text", text);
	}

	@Override
	public int getGuiID(ItemStack stack)
	{
		//TODO: Add a switch when needed
		return IIGuiList.GUI_PRINTED_PAGE_BLANK.ordinal();
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		ImmersiveIntelligence.proxy.openGuiForItem(player, hand==EnumHand.MAIN_HAND?EntityEquipmentSlot.MAINHAND: EntityEquipmentSlot.OFFHAND);
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}
}
