package pl.pabilo8.immersiveintelligence.common.item;

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
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.item.ItemIIPrintedPage.SubItems;
import pl.pabilo8.immersiveintelligence.common.util.IILib;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 09-07-2019
 */
public class ItemIIPrintedPage extends ItemIISubItemsBase<SubItems> implements IGuiItem
{
	public ItemIIPrintedPage()
	{
		super("printed_page", 64, SubItems.values());
	}

	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(IIUtils.getItalicString(I18n.format(stackToSub(stack).tooltip)));
	}

	public void setText(ItemStack stack, String text)
	{
		ItemNBTHelper.setString(stack, "text", text);
	}

	@Override
	public int getGuiID(ItemStack stack)
	{
		return stackToSub(stack).guiPage.ordinal();
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	@Nonnull
	public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, EntityPlayer player, @Nonnull EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		CommonProxy.openGuiForItem(player, hand==EnumHand.MAIN_HAND?EntityEquipmentSlot.MAINHAND: EntityEquipmentSlot.OFFHAND);
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	public enum SubItems implements IIItemEnum
	{
		@IIItemProperties(oreDict = "pageEmpty")
		BLANK(IIGuiList.GUI_PRINTED_PAGE_BLANK),
		@IIItemProperties(oreDict = {"pageText", "pageWritten"})
		TEXT(IIGuiList.GUI_PRINTED_PAGE_TEXT),
		@IIItemProperties(oreDict = {"pageCode", "pageWritten"})
		CODE(IIGuiList.GUI_PRINTED_PAGE_CODE),
		@IIItemProperties(oreDict = {"pageBlueprint", "pageWritten"})
		BLUEPRINT(IIGuiList.GUI_PRINTED_PAGE_BLUEPRINT);

		private final IIGuiList guiPage;
		private final String tooltip;

		SubItems(IIGuiList guiPage)
		{
			this.guiPage = guiPage;
			tooltip = IILib.DESCRIPTION_KEY+"printed_page."+getName();
		}
	}
}
