package pl.pabilo8.immersiveintelligence.common.block.stone;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.block.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.block.types.IIBlockTypes_StoneDecoration;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class BlockIISandbags extends BlockIITileProvider<IIBlockTypes_StoneDecoration>
{
	public static final PropertyInteger LEFT = PropertyInteger.create("left", 0, 2);
	public static final PropertyInteger RIGHT = PropertyInteger.create("right", 0, 2);
	public static final PropertyInteger CORNER = PropertyInteger.create("corner", 0, 2);

	public BlockIISandbags()
	{
		super("stone_decoration", Material.IRON, PropertyEnum.create("type", IIBlockTypes_StoneDecoration.class), ItemBlockIEBase.class, IEProperties.FACING_HORIZONTAL,
				LEFT, RIGHT, CORNER);
		setHardness(3.0F);
		setResistance(15.0F);
		lightOpacity = 0;
		this.setAllNotNormalBlock();
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
		return new TileEntitySandbags();
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
	public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing)
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

	@Nullable
	@Override
	public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EntityLiving entity)
	{
		return PathNodeType.FENCE;
	}
}