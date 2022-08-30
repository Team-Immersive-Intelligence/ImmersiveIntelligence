package pl.pabilo8.immersiveintelligence.common.block.fortification;

import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import pl.pabilo8.immersiveintelligence.common.block.types.IIBlockTypes_WoodenFortification;

/**
 * @author Pabilo8
 * @since 29.08.2020
 */
public class BlockIIWoodenFortification extends BlockIIFenceBase<IIBlockTypes_WoodenFortification>
{
	public BlockIIWoodenFortification()
	{
		super("wooden_fortification", Material.WOOD, PropertyEnum.create("type", IIBlockTypes_WoodenFortification.class), ItemBlockIEBase.class, BlockFence.NORTH, BlockFence.SOUTH, BlockFence.WEST, BlockFence.EAST);
		setHardness(3.0F);
		setResistance(15.0F);
		this.setBlockLayer(BlockRenderLayer.CUTOUT_MIPPED);
		this.setAllNotNormalBlock();
		lightOpacity = 0;
	}

	@Override
	public boolean useCustomStateMapper()
	{
		return true;
	}

	@Override
	public String getCustomStateMapping(int meta, boolean itemBlock)
	{
		return IIBlockTypes_WoodenFortification.values()[meta].getName();
	}

	@Override
	public boolean allowHammerHarvest(IBlockState state)
	{
		return true;
	}
}
