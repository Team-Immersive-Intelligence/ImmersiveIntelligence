package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDevice;

/**
 * Created by Pabilo8 on 2019-05-17.
 */
public class BlockIIMetalDevice extends BlockIITileProvider<IIBlockTypes_MetalDevice>
{
	public BlockIIMetalDevice()
	{
		super("metal_device", Material.IRON, PropertyEnum.create("type", IIBlockTypes_MetalDevice.class), ItemBlockIEBase.class, IEProperties.FACING_HORIZONTAL, IEProperties.MULTIBLOCKSLAVE);
		setHardness(3.0F);
		setResistance(15.0F);
		lightOpacity = 0;
		this.setNotNormalBlock(IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta());
		this.setMetaBlockLayer(IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta(), BlockRenderLayer.CUTOUT);

		tesrMap.put(IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta(), IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getName());
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
	public TileEntity createBasicTE(World world, IIBlockTypes_MetalDevice type)
	{
		switch(type)
		{
			case METAL_CRATE:
			{
				return new TileEntityMetalCrate();
			}
			case AMMUNITION_CRATE:
			{
				return new TileEntityAmmunitionCrate();
			}
			case DATA_PRINTER:
			{
				return new TileEntityDataPrinter();
			}
			case DATA_SENDER:
			{
				return new TileEntityDataSender();
			}
		}
		return null;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getActualState(state, world, pos);
		return state;
	}

	@Override
	public boolean allowHammerHarvest(IBlockState state)
	{
		return true;
	}

	@Override
	public boolean canIEBlockBePlaced(World world, BlockPos pos, IBlockState newState, EnumFacing side, float hitX, float hitY, float hitZ, EntityPlayer player, ItemStack stack)
	{
		return true;
	}

	@Deprecated
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		if(getMetaFromState(state)==IIBlockTypes_MetalDevice.METAL_CRATE.getMeta())
		{
			return EnumBlockRenderType.MODEL;
		}
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return getMetaFromState(state)!=IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta();
	}
}