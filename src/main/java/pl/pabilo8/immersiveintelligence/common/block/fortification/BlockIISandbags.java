package pl.pabilo8.immersiveintelligence.common.block.fortification;

import blusunrize.immersiveengineering.api.IEProperties;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import pl.pabilo8.immersiveintelligence.common.block.fortification.BlockIISandbags.IIBlockTypes_Sandbags;
import pl.pabilo8.immersiveintelligence.common.block.fortification.tileentity.TileEntitySandbags;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.EnumTileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileProviderEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class BlockIISandbags extends BlockIITileProvider<IIBlockTypes_Sandbags>
{
	public static final PropertyInteger LEFT = PropertyInteger.create("left", 0, 2);
	public static final PropertyInteger RIGHT = PropertyInteger.create("right", 0, 2);
	public static final PropertyInteger CORNER = PropertyInteger.create("corner", 0, 2);

	public BlockIISandbags()
	{
		super("stone_decoration", Material.SAND, PropertyEnum.create("type", IIBlockTypes_Sandbags.class), ItemBlockIIBase::new, IEProperties.FACING_HORIZONTAL,
				LEFT, RIGHT, CORNER);
		setHardness(4.0F);
		setResistance(55.0F);

		setBlockLayer(BlockRenderLayer.CUTOUT_MIPPED);
		setLightOpacity(0);

		setToolTypes(IIReference.TOOL_HAMMER);
	}

	public enum IIBlockTypes_Sandbags implements IITileProviderEnum
	{
		@EnumTileProvider(tile = TileEntitySandbags.class)
		SANDBAGS
	}

	@Override
	protected IBlockState getInitDefaultState()
	{
		return super.getInitDefaultState()
				.withProperty(LEFT, 0)
				.withProperty(RIGHT, 0)
				.withProperty(CORNER, 0);
	}

	@Override
	public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, @Nonnull EnumFacing facing)
	{
		IBlockState connector = world.getBlockState(pos.offset(facing));
		return connector.getBlock() instanceof BlockIISandbags;
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

		EnumFacing facing = state.getValue(IEProperties.FACING_HORIZONTAL);


		//manual but works
		boolean left = world.getBlockState(pos.offset(facing.rotateYCCW())).getBlock() instanceof BlockIISandbags;//Utils.canFenceConnectTo(world, pos, EnumFacing.NORTH, blockMaterial);
		boolean right = world.getBlockState(pos.offset(facing.rotateY())).getBlock() instanceof BlockIISandbags;//Utils.canFenceConnectTo(world, pos, EnumFacing.SOUTH, blockMaterial);

		TileEntity te = world.getTileEntity(pos.offset(facing.getOpposite()));

		int corner = 0;
		if(te instanceof TileEntitySandbags)
		{
			EnumFacing other = ((TileEntitySandbags)te).facing;
			corner = other==facing.rotateY()?1: (other==facing.rotateYCCW()?2: 0);
		}

		return state
				.withProperty(LEFT, corner!=0?0: (left?2: 1))
				.withProperty(RIGHT, corner!=0?0: (right?2: 1))
				.withProperty(CORNER, corner);
	}

	@Override
	@ParametersAreNonnullByDefault
	public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EntityLiving entity)
	{
		return PathNodeType.FENCE;
	}
}