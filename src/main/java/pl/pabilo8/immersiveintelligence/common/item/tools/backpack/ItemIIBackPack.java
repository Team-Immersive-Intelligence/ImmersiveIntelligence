package pl.pabilo8.immersiveintelligence.common.item.tools.backpack;


import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IGuiItem;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
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
 * @since 13.11.2026
 */
@IIItemProperties(category = IICategory.TOOLS)
@GeneratedItemModels(itemName = "backpack", texturePath = "backpack/backpack")
public class ItemIIBackPack extends ItemIIBackpackBase implements IInventoryItem, IGuiItem
{
	private static final String NBT_FREE_SLOTS = "free_slots";

	public ItemIIBackPack()
	{
		super("backpack", "backpack");
	}

	@Override
	public boolean handleWearingPlayerInteraction(ItemStack stack, EntityPlayer wearer, EntityPlayer visitor, EnumHand hand)
	{
		if(visitor.world.isRemote)
			return true;
		if(visitor.isSneaking())
		{
			IItemHandler inventory = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(inventory==null)
				return false;
			visitor.setHeldItem(hand, ItemHandlerHelper.insertItemStacked(inventory, visitor.getHeldItem(hand), false));
		}
		else
		{
			WornBackpack worn = getWornBackpack(wearer);
			if(worn==null)
				return false;
			openWornBackpack(visitor, worn);
		}
		return true;
	}

	@Override
	public boolean onBackpackAction(EntityPlayer player, WornBackpack worn)
	{
		openWornBackpack(player, worn);
		return true;
	}

	private void openWornBackpack(EntityPlayer player, WornBackpack worn)
	{
		if(worn.wearer instanceof EntityPlayer wearer)
			CommonProxy.openGuiForItem(player, wearer, EntityEquipmentSlot.CHEST);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String[] getBackpackOverlayText(ItemStack stack)
	{
		int free = ItemNBTHelper.hasKey(stack, NBT_FREE_SLOTS)?ItemNBTHelper.getInt(stack, NBT_FREE_SLOTS): countFreeSlots(stack);
		return new String[]{stack.getDisplayName(), I18n.format(IIReference.DESCRIPTION_KEY+"backpack_free_slots", free, getSlotCount())};
	}

	@Override
	public boolean onWornTick(EntityLivingBase wearer, ItemStack stack)
	{
		super.onWornTick(wearer, stack);
		if(wearer.world.isRemote||wearer.ticksExisted%10!=0)
			return false;
		int free = countFreeSlots(stack);
		if(ItemNBTHelper.hasKey(stack, NBT_FREE_SLOTS)&&ItemNBTHelper.getInt(stack, NBT_FREE_SLOTS)==free)
			return false;
		//Share the slot count through equipment NBT without sending the inventory capability.
		ItemNBTHelper.setInt(stack, NBT_FREE_SLOTS, free);
		return true;
	}

	private int countFreeSlots(ItemStack stack)
	{
		IItemHandler inventory = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		int free = 0;
		if(inventory!=null)
			for(int slot = 0; slot < inventory.getSlots(); slot++)
				if(inventory.getStackInSlot(slot).isEmpty())
					free++;
		return free;
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
