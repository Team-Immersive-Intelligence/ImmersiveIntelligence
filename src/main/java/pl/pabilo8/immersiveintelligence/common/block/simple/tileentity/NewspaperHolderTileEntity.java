package pl.pabilo8.immersiveintelligence.common.block.simple.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class NewspaperHolderTileEntity extends TileEntity {

    private ItemStack[] itemStacks = new ItemStack[12]; // Array to store items, adjust size as needed

    public ItemStack getItemStackInSlot(int slotIndex) {
        return itemStacks[slotIndex];
    }

    public void setItemStackInSlot(int slotIndex, ItemStack itemStack) {
        itemStacks[slotIndex] = itemStack;
        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        for (int i = 0; i < itemStacks.length; i++) {
            if (!itemStacks[i].isEmpty()) {
                compound.setTag("itemStack_" + i, itemStacks[i].serializeNBT());
            }
        }
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        for (int i = 0; i < itemStacks.length; i++) {
            String key = "itemStack_" + i;
            if (compound.hasKey(key)) {
                itemStacks[i] = new ItemStack(compound.getCompoundTag(key));
            } else {
                itemStacks[i] = ItemStack.EMPTY;
            }
        }
    }
}