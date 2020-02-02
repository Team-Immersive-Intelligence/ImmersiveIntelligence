package pl.pabilo8.immersiveintelligence.common.blocks.rotary;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MechanicalDevice;

/**
 * Created by Pabilo8 on 2019-05-17.
 */
public class BlockIIMechanicalDevice extends BlockIITileProvider<IIBlockTypes_MechanicalDevice>
{
	public BlockIIMechanicalDevice()
	{
		super("mechanical_device", Material.IRON, PropertyEnum.create("type", IIBlockTypes_MechanicalDevice.class), ItemBlockIEBase.class, IEProperties.FACING_ALL);
		setHardness(3.0F);
		setResistance(15.0F);
		lightOpacity = 0;
		this.setAllNotNormalBlock();
		setBlockLayer(BlockRenderLayer.CUTOUT_MIPPED);
	}

	@Override
	public boolean useCustomStateMapper()
	{
		return true;
	}

	@Override
	public String getCustomStateMapping(int meta, boolean itemBlock)
	{
		return null;
	}

	@Override
	public TileEntity createBasicTE(World world, IIBlockTypes_MechanicalDevice type)
	{
		switch(type)
		{
			case WOODEN_TRANSMISSION_BOX:
				return new TileEntityTransmissionBox();
			case CREATIVE_TRANSMISSION_BOX:
				return new TileEntityTransmissionBoxCreative();
		}
		return null;
	}

	@Override
	public boolean allowHammerHarvest(IBlockState state)
	{
		return true;
	}

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return true;
	}
}