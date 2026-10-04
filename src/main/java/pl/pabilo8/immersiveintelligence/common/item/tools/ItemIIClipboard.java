package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.api.IEProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IGuiItem;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemStackHandler;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemStackHandler.IInventoryItem;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nullable;

/**
 * Portable clipboard that stores typed Deco clipboard entries and places its matching block.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.09.2026
 */
@GeneratedItemModels(itemName = "clipboard", type = ItemModelType.ITEM_SIMPLE_TOOL, texturePath = "tools/clipboard")
@IIItemProperties(category = IICategory.TOOLS)
public class ItemIIClipboard extends ItemIIBase implements IInventoryItem, IGuiItem
{
	public static final String NBT_ENTRIES = "entries";
	public static final int MAX_ENTRIES = 64;

	public ItemIIClipboard()
	{
		super("clipboard", 1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if(player.isSneaking())
			return super.onItemRightClick(world, player, hand);
		CommonProxy.openGuiForItem(player, hand);
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
									  float hitX, float hitY, float hitZ)
	{
		if(!player.isSneaking())
			return EnumActionResult.PASS;

		ItemStack stack = player.getHeldItem(hand);
		IBlockState clicked = world.getBlockState(pos);
		BlockPos target = clicked.getBlock().isReplaceable(world, pos)?pos: pos.offset(facing);
		if(!player.canPlayerEdit(target, facing, stack)
				||!world.mayPlace(IIContent.blockClipboard, target, false, facing, player))
			return EnumActionResult.FAIL;

		if(!world.isRemote)
		{
			IBlockState placed = IIContent.blockClipboard.getDefaultState()
					.withProperty(IEProperties.FACING_ALL, facing);
			if(!world.setBlockState(target, placed, 3))
				return EnumActionResult.FAIL;
			IIContent.blockClipboard.onIEBlockPlacedBy(world, target, placed, facing, hitX, hitY, hitZ, player, stack);
			world.playSound(null, target, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1f, 0.8f);
			if(!player.capabilities.isCreativeMode)
				stack.shrink(1);
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	public IIGUI getGUI(ItemStack stack)
	{
		return IIGUI.CLIPBOARD_ITEM;
	}

	public static NBTTagList getEntries(ItemStack stack)
	{
		return stack.hasTagCompound()?sanitiseEntries(stack.getTagCompound().getTagList(NBT_ENTRIES, 10)): new NBTTagList();
	}

	public static void setEntries(ItemStack stack, NBTTagList entries)
	{
		if(stack.isEmpty())
			return;
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt==null)
			stack.setTagCompound(nbt = new NBTTagCompound());
		nbt.setTag(NBT_ENTRIES, sanitiseEntries(entries));
	}

	public static NBTTagList sanitiseEntries(NBTTagList entries)
	{
		NBTTagList result = new NBTTagList();
		for(int i = 0; i < Math.min(entries.tagCount(), MAX_ENTRIES); i++)
		{
			NBTTagCompound entry = entries.getCompoundTagAt(i);
			if(entry.hasKey("type", 8)&&entry.getString("type").length() <= 64&&entry.hasKey("value", 10))
				result.appendTag(entry.copy());
		}
		return result;
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return stack.isEmpty()?null: new IIItemStackHandler(stack);
	}

	@Override
	public int getSlotCount()
	{
		return 0;
	}
}
