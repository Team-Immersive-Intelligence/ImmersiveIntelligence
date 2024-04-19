package pl.pabilo8.immersiveintelligence.common.block.metal_device;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityNewspaperStand;

public class BlockIINewspaperStand extends BlockContainer {

    public BlockIINewspaperStand() {
        super(Material.IRON); // Set the material of your block
        this.setHardness(4.0f); // Set the hardness of the block
        this.setResistance(10.0f); // Set the resistance of the block
        this.setCreativeTab(IIContent.II_CREATIVE_TAB);
        // Set other properties as needed
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityNewspaperStand(); // Return an instance of your custom TileEntity
    }

    // Handle player interaction
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        // Your interaction logic goes here (see previous example)
        return true; // Return true to indicate that interaction was handled
    }

    // You may need to override other methods related to rendering if you have custom models or textures
}