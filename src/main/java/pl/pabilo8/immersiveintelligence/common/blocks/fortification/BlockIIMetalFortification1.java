package pl.pabilo8.immersiveintelligence.common.blocks.fortification;

import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalFortification1;

/**
 * @author Pabilo8
 * @since 29.08.2020
 */
public class BlockIIMetalFortification1 extends BlockIITileProvider<IIBlockTypes_MetalFortification1>
{
	public BlockIIMetalFortification1()
	{
		super("metal_fortification1", Material.IRON, PropertyEnum.create("type", IIBlockTypes_MetalFortification1.class), ItemBlockIEBase.class);
		setHardness(3.0F);
		setResistance(15.0F);
		this.setBlockLayer(BlockRenderLayer.CUTOUT_MIPPED);
		this.setAllNotNormalBlock();
		lightOpacity = 0;
		addToTESRMap(IIBlockTypes_MetalFortification1.TANK_TRAP);
	}

	@Override
	public boolean useCustomStateMapper()
	{
		return false;
	}

	@Override
	public String getCustomStateMapping(int meta, boolean itemBlock)
	{
		return null;
	}

	@Override
	public boolean allowHammerHarvest(IBlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createBasicTE(World worldIn, IIBlockTypes_MetalFortification1 type)
	{
		return new TileEntityTankTrap();
	}
}
