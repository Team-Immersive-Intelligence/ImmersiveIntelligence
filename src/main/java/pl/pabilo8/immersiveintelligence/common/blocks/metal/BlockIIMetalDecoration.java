package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDecoration;

/**
 * Created by Pabilo8 on 2019-05-17.
 */
public class BlockIIMetalDecoration extends BlockIIBase<IIBlockTypes_MetalDecoration>
{
	public BlockIIMetalDecoration()
	{
		super("metal_decoration", Material.IRON, PropertyEnum.create("type", IIBlockTypes_MetalDecoration.class), ItemBlockIEBase.class);
		setHardness(3.0F);
		setResistance(15.0F);
		lightOpacity = 0;
		setBlockLayer(BlockRenderLayer.CUTOUT_MIPPED);
	}

	@Override
	public boolean allowHammerHarvest(IBlockState state)
	{
		return true;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Deprecated
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
}