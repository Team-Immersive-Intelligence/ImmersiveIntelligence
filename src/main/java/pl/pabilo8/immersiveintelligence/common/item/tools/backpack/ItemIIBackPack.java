package pl.pabilo8.immersiveintelligence.common.item.tools.backpack;

import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IGuiItem;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemStackHandler;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemStackHandler.IInventoryItem;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Stores items in a portable inventory.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 04.11.2026
 * @since 23.01.2021
 */
@IIItemProperties(category = IICategory.TOOLS)
@GeneratedItemModels(itemName = "backpack", texturePath = "backpack/backpack")
public class ItemIIBackPack extends ItemIIBackpackBase implements IInventoryItem, IGuiItem
{
	public ItemIIBackPack()
	{
		super("backpack");
	}

	@Override
	public int getSlotCount()
	{
		return 18;
	}

	@Override
	public IIGUI getGUI(ItemStack stack)
	{
		return IIGUI.BACKPACK;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if(player.isSneaking())
			return super.onItemRightClick(world, player, hand);
		if(!world.isRemote)
			CommonProxy.openGuiForItem(player, hand);
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return new IIItemStackHandler(stack)
		{
			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack item, boolean simulate)
			{
				return isItemValid(slot, item)?super.insertItem(slot, item, simulate): item;
			}

			@Override
			public boolean isItemValid(int slot, @Nonnull ItemStack item)
			{
				return !(Block.getBlockFromItem(item.getItem()) instanceof BlockShulkerBox)&&!(item.getItem() instanceof ItemIIBackPack)
						&&!(getAttachedBackpack(item).getItem() instanceof ItemIIBackPack);
			}
		};
	}
}
