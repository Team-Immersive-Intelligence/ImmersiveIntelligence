package pl.pabilo8.immersiveintelligence.common.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IGuiItem;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.item.ItemIIPrintedPage.PageType;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.07.2019
 */
@IIItemProperties(category = IICategory.ELECTRONICS)
public class ItemIIPrintedPage extends ItemIISubItemsBase<PageType> implements IGuiItem
{
	public ItemIIPrintedPage()
	{
		super("printed_page", 64, PageType.values());
	}

	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(IIStringUtil.getItalicString(I18n.format(stackToSub(stack).tooltip)));

		if (stack.hasTagCompound())
		{
			NBTTagCompound nbttagcompound = stack.getTagCompound();
			String s = nbttagcompound.getString("author");
			if (!StringUtils.isNullOrEmpty(s))
				tooltip.add(TextFormatting.GRAY + net.minecraft.util.text.translation.I18n.translateToLocalFormatted("book.byAuthor", new Object[]{s}));
		}
	}

	@Override
	@Nonnull
	public String getItemStackDisplayName(ItemStack stack) {
	if (stack.hasTagCompound())
	{
		NBTTagCompound nbttagcompound = stack.getTagCompound();
		String titleString = nbttagcompound.getString("title");
		if (!StringUtils.isNullOrEmpty(titleString)) return titleString;
	}

	return super.getItemStackDisplayName(stack);
}

	@Override
	public IIGUI getGUI(ItemStack stack)
	{
		return stackToSub(stack).gui;
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	@Nonnull
	public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, EntityPlayer player, @Nonnull EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		switch (stackToSub(stack))
		{
			case BLANK:
				return new ActionResult<>(EnumActionResult.PASS, stack);
			case TEXT:
			case CODE:
			case BLUEPRINT:
			case LETTER_OPENED:
			case BOUND_PAGES:
			case BOOK:
			case NEWSPAPER:
			{
				CommonProxy.openGuiForItem(player, hand);
				return new ActionResult<>(EnumActionResult.SUCCESS, stack);
			}
			case LETTER:
			{
				stack.setItemDamage(PageType.LETTER_OPENED.getMeta());
				player.setHeldItem(hand, stack);
				return new ActionResult<>(EnumActionResult.SUCCESS, stack);
			}
		}

		return new ActionResult<>(EnumActionResult.FAIL, stack);
	}

	@GeneratedItemModels(itemName = "printed_page")
	public enum PageType implements IIItemEnum
	{
		// Special case for blank paper
		@IIItemProperties(oreDict = {"pageEmpty", "paper"})
		BLANK(IIGUI.PRINTED_PAGE_BLANK),

		// All single Page item variants
		@IIItemProperties(oreDict = {"pageText", "pageWritten"})
		TEXT(IIGUI.PRINTED_PAGE_TEXT),
		@IIItemProperties(oreDict = {"pageCode", "pageWritten"})
		CODE(IIGUI.PRINTED_PAGE_CODE),
		@IIItemProperties(oreDict = {"pageBlueprint", "pageWritten"})
		BLUEPRINT(IIGUI.PRINTED_PAGE_BLUEPRINT),
		@IIItemProperties(oreDict = {"pageLetterOpen", "pageWritten"})
		LETTER_OPENED(IIGUI.PRINTED_PAGE_BLANK),

		// All multi Page variants
		@IIItemProperties(oreDict = "pageBinded")
		BOUND_PAGES(IIGUI.PRINTED_PAGE_BOUND),
		@IIItemProperties(oreDict = "pageNewspaper")
		NEWSPAPER(IIGUI.PRINTED_PAGE_NEWSPAPER),
		@IIItemProperties(oreDict = {"pageBook", "book", "bookPrinted"})
		BOOK(IIGUI.PRINTED_PAGE_BLANK),

		// All page containers
		@IIItemProperties(oreDict = {"pageLetter", "letter"})
		LETTER(IIGUI.PRINTED_PAGE_BLANK);

		private final IIGUI gui;
		private final String tooltip;

		PageType(IIGUI gui)
		{
			this.gui = gui;
			tooltip = IIReference.DESCRIPTION_KEY+"printed_page."+getName();
		}

		public static PageType fromStack(ItemStack stack)
		{
			try
			{
				return PageType.values()[stack.getMetadata()];
			} catch (Exception e)
			{
				return PageType.values()[0];
			}
		}

		public boolean useHandSpecialRender()
		{
			switch (this)
			{
				case TEXT:
				case CODE:
				case BLUEPRINT:
				case LETTER_OPENED:
				case BOUND_PAGES:
				case NEWSPAPER:
					return true;
				case BOOK:
				case LETTER:
				case BLANK:
				default:
					return false;
			}
		}

		public int getDisplayedPages()
		{
			switch (this)
			{
				case TEXT:
				case CODE:
				case BLUEPRINT:
				case LETTER_OPENED:
				case BOUND_PAGES:
					return 1;
				case NEWSPAPER:
					return 3;
				case BOOK:
					return 2;
				case LETTER:
				case BLANK:
				default:
					return 0;
			}
		}

		/**
		 * Returns the name of the actual file within the printed_page directory
		 * @param stack the item stack, where the metadata is taken from
		 * @return a String for the filename
		 */
		public String getUITextureName(ItemStack stack)
		{
			switch (this)
			{
				case TEXT:
				case CODE:
				case BLUEPRINT:
				case LETTER_OPENED:
					return "page.png";
				case BOUND_PAGES:
					return "bound.png";
				case NEWSPAPER:
					return "newspaper.png";
				case BOOK:
					return "book.png";
				case LETTER:
				case BLANK:
				default:
					return "";
			}
		}
	}
}
