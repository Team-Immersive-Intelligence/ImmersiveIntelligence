package pl.pabilo8.immersiveintelligence.common.block.wooden;

import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.block.types.IIBlockTypesCharredLog;

/**
 * @author Pabilo8
 * @since 08.12.2021
 */
public class BlockIICharredLog extends BlockIIBase<IIBlockTypesCharredLog>
{
	public static final PropertyEnum<IIBlockTypesCharredLog> PROP = PropertyEnum.create("type", IIBlockTypesCharredLog.class);

	public BlockIICharredLog()
	{
		super("charred_log", Material.WOOD, PROP, ItemBlockIEBase.class, BlockLog.LOG_AXIS);
		this.setHardness(2.0F);
		this.setSoundType(SoundType.WOOD);
		lightOpacity = 0;
	}

	@Override
	protected IBlockState getInitDefaultState()
	{
		return super.getInitDefaultState().withProperty(PROP, IIBlockTypesCharredLog.MAIN).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return this.getDefaultState().withProperty(BlockLog.LOG_AXIS, EnumAxis.fromFacingAxis(facing.getAxis()));
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(PROP, IIBlockTypesCharredLog.values()[(int)Math.floor(meta/4f)]).withProperty(BlockLog.LOG_AXIS, EnumAxis.values()[MathHelper.clamp(meta%3, 0, 3)]);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(BlockLog.LOG_AXIS).ordinal()+(state.getValue(PROP).getMeta()*4);
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public boolean isWood(IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.SOLID;
	}
}