package pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.backpack;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIBackpack.BackpackType;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemStackHandler;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIInventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Exposes the backpack inventory to players and item handlers.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 18.11.2026
 */
public class TileEntityStorageBackpack extends TileEntityBackpack implements IIIInventory, IIIGuiMultiblockTile
{
	private ItemStack inventoryStack = ItemStack.EMPTY;
	private IIItemStackHandler itemHandler;

	public TileEntityStorageBackpack()
	{
		super(BackpackType.STORAGE);
	}

	private IIItemStackHandler getItemHandler()
	{
		if(backpack!=inventoryStack)
		{
			itemHandler = (IIItemStackHandler)backpack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			itemHandler.setTile(this);
			inventoryStack = backpack;
		}
		return itemHandler;
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return getItemHandler().getContainedItems();
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return getItemHandler().isItemValid(slot, stack);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing side)
	{
		return capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY||super.hasCapability(capability, side);
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing side)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandler());
		return super.getCapability(capability, side);
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.BACKPACK_BLOCK;
	}

	@Override
	public TileEntity master()
	{
		return this;
	}
}
