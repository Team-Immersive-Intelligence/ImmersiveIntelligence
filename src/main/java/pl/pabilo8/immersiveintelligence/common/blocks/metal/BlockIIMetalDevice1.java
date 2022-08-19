package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
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
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDevice1;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 24.05.2021
 */
public class BlockIIMetalDevice1 extends BlockIITileProvider<IIBlockTypes_MetalDevice1>
{
	public BlockIIMetalDevice1()
	{
		super("metal_device1", Material.IRON, PropertyEnum.create("type", IIBlockTypes_MetalDevice1.class), ItemBlockIEBase.class, IEProperties.FACING_ALL, IEProperties.MULTIBLOCKSLAVE, IEProperties.BOOLEANS[0]);
		this.setHardness(3.0F);
		this.setResistance(15.0F);
		lightOpacity = 0;
		this.setAllNotNormalBlock();

		setMetaBlockLayer(IIBlockTypes_MetalDevice1.CO2_FILTER.getMeta(), BlockRenderLayer.CUTOUT);
		//this.setMetaLightOpacity(IIBlockTypes_MetalDevice1.CO2_FILTER.getMeta(), 255);
		this.setMetaMobilityFlag(IIBlockTypes_MetalDevice1.CO2_FILTER.getMeta(), EnumPushReaction.BLOCK);


	}

	@Override
	public boolean useCustomStateMapper()
	{
		return true;
	}

	@Override
	public String getCustomStateMapping(int meta, boolean itemBlock)
	{
		if(IIBlockTypes_MetalDevice1.values()[meta]==IIBlockTypes_MetalDevice1.CO2_FILTER)
			return "co2_filter";
		return null;
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getExtendedState(state, world, pos);

		return state;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		IBlockState actualState = super.getActualState(state, world, pos);
		TileEntity tile = world.getTileEntity(pos);

		if(tile instanceof TileEntityCO2Filter)
			actualState = actualState.withProperty(IEProperties.BOOLEANS[0], ((TileEntityCO2Filter)tile).dummy==1);
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

	@Nullable
	@Override
	public TileEntity createBasicTE(World worldIn, IIBlockTypes_MetalDevice1 type)
	{
		if(type==IIBlockTypes_MetalDevice1.CO2_FILTER)
		{
			return new TileEntityCO2Filter();
		}
		return null;
	}

	@Override
	public boolean allowHammerHarvest(IBlockState state)
	{
		return true;
	}
}
