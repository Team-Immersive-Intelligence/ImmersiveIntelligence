package pl.pabilo8.immersiveintelligence.common.util.item;

import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
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
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author BluSunrize
 * @since 09.07.2020
 * <p>
 * but actually
 */
public class IIArmorItemStackHandler extends ItemStackHandler implements ICapabilityProvider
{
	EnergyHelper.ItemEnergyStorage energyStorage;
	private boolean first = true;
	private ItemStack stack;
	@Nullable
	private Runnable onChange = null;

	public IIArmorItemStackHandler(ItemStack stack)
	{
		super();
		this.stack = stack;
		this.energyStorage = new EnergyHelper.ItemEnergyStorage(stack);
	}

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
		//set inventory to the itemstack so it survives being removed from the workbench
		if(this.stack!=null && !this.stack.isEmpty())
		{
			NBTTagList list = Utils.writeInventory(this.stacks);
			ItemNBTHelper.getTag(this.stack).setTag("Inv", list);
		}
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
			//If itemstack already had a saved inventory, load it. Otherwise keep items as is
			if(ItemNBTHelper.hasKey(this.stack, "Inv"))
			{
				NBTTagList list = ItemNBTHelper.getTag(this.stack).getTagList("Inv", 10);
				NonNullList<ItemStack> inv = Utils.readInventory(list, idealSize);
				for(int i = 0; i < Math.min(inv.size(), idealSize); i++)
					newList.set(i, inv.get(i));
			}
			else
			{
				for(int i = 0; i < Math.min(stacks.size(), idealSize); i++)
					newList.set(i, stacks.get(i));
			}
			stacks = newList;
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
