package pl.pabilo8.immersiveintelligence.common.block.metal_device;

import blusunrize.immersiveengineering.api.IEProperties;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice1.IIBlockTypes_MetalDevice1;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityCO2Filter;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.EnumTileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileProviderEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;

/**
 * @author Pabilo8
 * @since 24.05.2021
 */
public class BlockIIMetalDevice1 extends BlockIITileProvider<IIBlockTypes_MetalDevice1>
{
	public enum IIBlockTypes_MetalDevice1 implements IITileProviderEnum
	{
		@EnumTileProvider(tile = TileEntityCO2Filter.class)
		@IIBlockProperties(needsCustomState = true, renderLayer = BlockRenderLayer.CUTOUT)
		CO2_FILTER
	}

	public BlockIIMetalDevice1()
	{
		super("metal_device1", Material.IRON, PropertyEnum.create("type", IIBlockTypes_MetalDevice1.class), ItemBlockIIBase::new,
				IEProperties.FACING_ALL, IEProperties.MULTIBLOCKSLAVE, IEProperties.BOOLEANS[0]);

		setHardness(3.0F);
		setResistance(15.0F);
		setLightOpacity(0);
		setToolTypes(IIReference.TOOL_HAMMER);
		setBlockLayer(BlockRenderLayer.CUTOUT_MIPPED);

		this.setMetaMobilityFlag(IIBlockTypes_MetalDevice1.CO2_FILTER, EnumPushReaction.BLOCK);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		IBlockState actualState = super.getActualState(state, world, pos);
		TileEntity tile = world.getTileEntity(pos);

		if(tile instanceof TileEntityCO2Filter)
			actualState = actualState.withProperty(IEProperties.BOOLEANS[0], ((TileEntityCO2Filter)tile).subBlockID==1);
		if(tile instanceof TileEntityCO2Filter)
			actualState = actualState.withProperty(IEProperties.MULTIBLOCKSLAVE, ((TileEntityCO2Filter)tile).isDummy());

		return actualState;
	}

	@Override
	public boolean canIEBlockBePlaced(World world, BlockPos pos, IBlockState newState, EnumFacing side, float hitX, float hitY, float hitZ, EntityPlayer player, ItemStack stack)
	{
		if(stack.getItemDamage()==IIBlockTypes_MetalDevice1.CO2_FILTER.getMeta())
		{
			for(int hh = 1; hh <= 2; hh++)
			{
				BlockPos pos2 = pos.add(0, hh, 0);
				if(world.isOutsideBuildHeight(pos2)||!world.getBlockState(pos2).getBlock().isReplaceable(world, pos2))
					return false;
			}
		}
		return true;
	}
}
