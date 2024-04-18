package pl.pabilo8.immersiveintelligence.common.block.simple;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.math.AxisAlignedBB;





import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.NewspaperHolderTileEntity;

public class BlockIINewspaperStand extends BlockContainer {

    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 3, 1); // One block wide, three blocks tall


    public BlockIINewspaperStand() {
        super(Material.IRON);
        setUnlocalizedName("newspaper_holder_block");
        setRegistryName("newspaper_holder_block");
        setCreativeTab(IIContent.II_CREATIVE_TAB); // Add it to a creative tab for testing
    }


    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new NewspaperHolderTileEntity();
    }


    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof NewspaperHolderTileEntity) {
                NewspaperHolderTileEntity holder = (NewspaperHolderTileEntity) tileEntity;
                int slotIndex = getSlotIndexFromHit(hitX, hitY, hitZ, facing);
                ItemStack heldItem = player.getHeldItem(hand);
                ItemStack currentStack = holder.getItemStackInSlot(slotIndex);

                if (!heldItem.isEmpty()) {
                    // Insert item
                    if (currentStack.isEmpty()) {
                        holder.setItemStackInSlot(slotIndex, heldItem.copy());
                        if (!player.isCreative()) {
                            heldItem.shrink(1); // Decrease stack size if not in creative mode
                        }
                    }
                } else {
                    // Remove item
                    if (!currentStack.isEmpty()) {
                        player.setHeldItem(hand, currentStack.copy());
                        holder.setItemStackInSlot(slotIndex, ItemStack.EMPTY);
                    }
                }
                return true;
            }
        }
        return false;
    }


    private int getSlotIndexFromHit(float hitX, float hitY, float hitZ, EnumFacing facing) {
        // Assuming your block's width, height, and depth are 1 unit each
        int segments = 3;
        int segmentIndex;

        switch (facing) {
            case NORTH:
                segmentIndex = (int) (hitX * segments);
                break;
            case SOUTH:
                segmentIndex = segments - (int) (hitX * segments) - 1;
                break;
            case WEST:
                segmentIndex = (int) (hitZ * segments);
                break;
            case EAST:
                segmentIndex = segments - (int) (hitZ * segments) - 1;
                break;
            default:
                segmentIndex = 0;
                break;
        }

        return segmentIndex;
    }


    // Override getBoundingBox to specify the collision box
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOX;


    }
}