package pl.pabilo8.immersiveintelligence.common.blocks.stone;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_StoneDecoration;

/**
 * Created by Pabilo8 on 2019-05-17.
 */
public class BlockIIStoneDecoration extends BlockIITileProvider<IIBlockTypes_StoneDecoration>
{
	public BlockIIStoneDecoration()
	{
		super("stone_decoration", Material.IRON, PropertyEnum.create("type", IIBlockTypes_StoneDecoration.class), ItemBlockIEBase.class, IEProperties.FACING_HORIZONTAL, IEProperties.MULTIBLOCKSLAVE);
		setHardness(3.0F);
		setResistance(15.0F);
		lightOpacity = 0;
		this.setAllNotNormalBlock();

		tesrMap.put(IIBlockTypes_StoneDecoration.SANDBAGS.getMeta(), IIBlockTypes_StoneDecoration.SANDBAGS.getName());

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
	public TileEntity createBasicTE(World world, IIBlockTypes_StoneDecoration type)
	{
		if(type==IIBlockTypes_StoneDecoration.SANDBAGS)
			return new TileEntitySandbags();
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

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, world, pos, blockIn, fromPos);
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntitySandbags)
		{
			((TileEntitySandbags)te).cornerFacing = ((TileEntitySandbags)te).getCornerFacing();
		}
	}

	@Deprecated
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return false;
	}
}