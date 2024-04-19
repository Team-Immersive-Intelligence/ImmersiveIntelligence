package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity;


import net.minecraft.block.BlockContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class TileEntityNewspaperStand extends TileEntity
{
    private ItemStack[] items = new ItemStack[12]; // Array to hold 12 items

    // Method to add an item to the stand
    public void addItem(ItemStack stack) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                items[i] = stack.copy();
                markDirty();
                return;
            }
        }
    }

    // Method to remove an item from the stand
    public ItemStack removeItem() {
        for (int i = items.length - 1; i >= 0; i--) {
            if (items[i] != null) {
                ItemStack stack = items[i];
                items[i] = null;
                markDirty();
                return stack;
            }
        }
        return null; // If the stand is empty
    }
}


/*
public class TileEntityNewspaperStand extends BlockContainer {


    // Handle player interaction
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityNewspaperStand) {
            TileEntityNewspaperStand stand = (TileEntityNewspaperStand) tileEntity;
            ItemStack heldItem = playerIn.getHeldItem(hand);
            if (playerIn.isSneaking()) {
                // Shift-right-click to add item
                if (!heldItem.isEmpty()) {
                    stand.addItem(heldItem.copy());
                    if (!playerIn.capabilities.isCreativeMode) {
                        heldItem.shrink(1);
                    }
                    return true;
                }
            } else {
                // Right-click to remove item
                ItemStack item = stand.removeItem();
                if (item != null) {
                    if (!playerIn.inventory.addItemStackToInventory(item)) {
                        // If the player's inventory is full, drop the item
                        InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), item);
                    }
                    return true;
                }
            }
        }
        return false;
    }
    }
*/
