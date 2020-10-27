package pl.pabilo8.immersiveintelligence.common.blocks.fortification;

import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalFortification;

/**
 * @author Pabilo8
 * @since 29.08.2020
 */
public class BlockIIMetalFortification extends BlockIIFenceBase<IIBlockTypes_MetalFortification>
{
	public BlockIIMetalFortification()
	{
		super("metal_fortification", Material.IRON, PropertyEnum.create("type", IIBlockTypes_MetalFortification.class), ItemBlockIEBase.class);
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
		return IIBlockTypes_MetalFortification.values()[meta].getName();
	}

	@Override
	public boolean allowHammerHarvest(IBlockState state)
	{
		return true;
	}


}
