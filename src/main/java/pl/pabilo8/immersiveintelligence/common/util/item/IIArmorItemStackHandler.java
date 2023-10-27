package pl.pabilo8.immersiveintelligence.common.util.item;

import blusunrize.immersiveengineering.common.util.EnergyHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @author BluSunrize
 * @since 09.07.2020
 * <p>
 * but actually
 */
public class IIArmorItemStackHandler extends ItemStackHandler implements ICapabilityProvider
{
	private boolean first = true;
	private ItemStack stack;
	EnergyHelper.ItemEnergyStorage energyStorage;

	public IIArmorItemStackHandler(ItemStack stack)
	{
		super();
		this.stack = stack;
		this.energyStorage = new EnergyHelper.ItemEnergyStorage(stack);
	}

	@Nullable
	private Runnable onChange = null;

	public void setTile(TileEntity tile)
	{
		onChange = tile==null?null: (tile::markDirty);
	}

	public void setInventoryForUpdate(IInventory inv)
	{
		onChange = inv==null?null: (inv::markDirty);
	}

	@Override
	protected void onContentsChanged(int slot)
	{
		super.onContentsChanged(slot);
		if(onChange!=null)
			onChange.run();
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability==CapabilityEnergy.ENERGY||capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(first)
		{
			int idealSize = ((ItemIIUpgradeableArmor)stack.getItem()).getSlotCount();
			NonNullList<ItemStack> newList = NonNullList.withSize(idealSize, ItemStack.EMPTY);
			for(int i = 0; i < Math.min(stacks.size(), idealSize); i++)
				newList.set(i, stacks.get(i));
			stacks = newList;
			stack = ItemStack.EMPTY;
			first = false;
		}
		if(capability==CapabilityEnergy.ENERGY)
			return (T)energyStorage;
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T)this;
		return null;
	}

	public NonNullList<ItemStack> getContainedItems()
	{
		return stacks;
	}
}
