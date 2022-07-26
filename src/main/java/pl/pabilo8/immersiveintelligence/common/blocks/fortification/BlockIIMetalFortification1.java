package pl.pabilo8.immersiveintelligence.common.blocks.fortification;

import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalFortification1;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 29.08.2020
 */
public class BlockIIMetalFortification1 extends BlockIIBase<IIBlockTypes_MetalFortification1>
{
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.5D, 1.0D);

	public BlockIIMetalFortification1()
	{
		super("metal_fortification1", Material.IRON, PropertyEnum.create("type", IIBlockTypes_MetalFortification1.class), ItemBlockIEBase.class);
		setHardness(3.0F);
		setResistance(15.0F);
		this.setBlockLayer(BlockRenderLayer.CUTOUT_MIPPED);
		this.setAllNotNormalBlock();
		lightOpacity = 0;
	}

	@Override
	public boolean allowHammerHarvest(IBlockState state)
	{
		return true;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB;
	}
}
