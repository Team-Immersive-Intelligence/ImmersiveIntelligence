package pl.pabilo8.immersiveintelligence.common.block.fortification;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockWall;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.block.BlockIIBase;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 29.08.2020
 */
public abstract class BlockIIFenceBase<E extends Enum<E> & BlockIEBase.IBlockEnum> extends BlockIIBase<E> implements ITileEntityProvider
{
	public static final PropertyBool FORCED_POST = PropertyBool.create("forced_post");

	public BlockIIFenceBase(String name, Material material, PropertyEnum<E> mainProperty, Class<? extends ItemBlockIEBase> itemBlock, Object... additionalProperties)
	{
		super(name, material, mainProperty, itemBlock, BlockWall.NORTH, BlockWall.SOUTH, BlockWall.WEST, BlockWall.EAST, BlockWall.UP, FORCED_POST, additionalProperties);
	}

	@Override
	protected IBlockState getInitDefaultState()
	{
		return super.getInitDefaultState().withProperty(FORCED_POST, false);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityChainFence();
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

	@Override
	public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing)
	{
		IBlockState connector = world.getBlockState(pos.offset(facing));
		return connector.getBlock() instanceof BlockIIFenceBase;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side)
	{
		return side!=EnumFacing.UP&&side!=EnumFacing.DOWN?BlockFaceShape.MIDDLE_POLE: BlockFaceShape.CENTER;
	}

	/**
	 * Get the actual Block state of this Block at the given position. This applies properties not visible in the metadata,
	 * such as fence connections.
	 */
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getActualState(state, world, pos);

		//manual but works
		boolean north = Utils.canFenceConnectTo(world, pos, EnumFacing.NORTH, blockMaterial);
		boolean south = Utils.canFenceConnectTo(world, pos, EnumFacing.SOUTH, blockMaterial);
		boolean east = Utils.canFenceConnectTo(world, pos, EnumFacing.EAST, blockMaterial);
		boolean west = Utils.canFenceConnectTo(world, pos, EnumFacing.WEST, blockMaterial);

		boolean forced = false;
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityChainFence)
			forced = ((TileEntityChainFence)te).hasPost;

		boolean none = !north&&!south&&!east&&!west;
		boolean corner = (north&&east)||(north&&west)||(south&&east)||(south&&west);
		boolean contacts = ((north?1: 0)+(south?1: 0)+(east?1: 0)+(west?1: 0))==2;

		if(none||corner||!contacts)
			forced = false;

		return state
				.withProperty(BlockWall.NORTH, north)
				.withProperty(BlockWall.SOUTH, south)
				.withProperty(BlockWall.EAST, east)
				.withProperty(BlockWall.WEST, west)
				.withProperty(FORCED_POST, forced)
				.withProperty(BlockWall.UP, forced||none||corner||!contacts);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
	{
		state = state.getActualState(worldIn, pos);

		addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockFence.PILLAR_AABB);
		if(state.getValue(BlockFence.NORTH))
			addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockFence.NORTH_AABB);
		if(state.getValue(BlockFence.EAST))
			addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockFence.EAST_AABB);
		if(state.getValue(BlockFence.SOUTH))
			addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockFence.SOUTH_AABB);
		if(state.getValue(BlockFence.WEST))
			addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockFence.WEST_AABB);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return new AxisAlignedBB(Utils.canFenceConnectTo(world, pos, EnumFacing.WEST, blockMaterial)?0: .375f, 0, Utils.canFenceConnectTo(world, pos, EnumFacing.NORTH, blockMaterial)?0: .375f, Utils.canFenceConnectTo(world, pos, EnumFacing.EAST, blockMaterial)?1: .625f, 1f, Utils.canFenceConnectTo(world, pos, EnumFacing.SOUTH, blockMaterial)?1: .625f);
	}

	@Nullable
	@Override
	public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EntityLiving entity)
	{
		return PathNodeType.FENCE;
	}

	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityChainFence&&Utils.isHammer(player.getHeldItem(hand)))
		{
			((TileEntityChainFence)te).hasPost = !((TileEntityChainFence)te).hasPost;
			IBlockState newstate = getActualState(state.withProperty(FORCED_POST, ((TileEntityChainFence)te).hasPost), world, pos);
			world.setBlockState(pos, newstate);
			world.addBlockEvent(pos, newstate.getBlock(), 255, 0);
			return true;
		}
		return false;
	}
}
