package pl.pabilo8.immersiveintelligence.common.block.simple;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;


/**
 * @author Pabilo8
 * @since 01.09.2020
 */
public class BlockIIHMXDynamite extends BlockIITileProvider<BlockIIHMXDynamite.IIBlockTypes_HMXDynamite> {

	public BlockIIHMXDynamite() {
		super("hmx_dynamite", Material.WOOD, PropertyEnum.create("type", IIBlockTypes_HMXDynamite.class), ItemBlockIIBase::new);
		setFullCube(true);
		setHardness(3.0F);
		setResistance(10.0F);
		setCategory(IICategory.WARFARE);
		setToolTypes(IIReference.TOOL_WIRECUTTER);
		setBlockLayer(BlockRenderLayer.CUTOUT_MIPPED);
		setLightOpacity(0);

	}

	public enum IIBlockTypes_HMXDynamite implements IIBlockEnum, IIBlockInterfaces.IITileProviderEnum {
		@IIBlockProperties(oreDict = {"explosiveHMX", "explosiveOctogen"})
		MAIN
	}

	@Override
	@Nonnull
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	@SuppressWarnings("deprecation")
	@Nonnull
	@ParametersAreNonnullByDefault
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.SOLID;
	}

}



