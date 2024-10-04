package pl.pabilo8.immersiveintelligence.common.block.simple;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
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
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIICharredLog.CharredLogs;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Pabilo8
 * @since 08.12.2021
 */
public class BlockIICharredLog extends BlockIIBase<CharredLogs>
{
	public BlockIICharredLog()
	{
		super("charred_log", PropertyEnum.create("type", CharredLogs.class), Material.WOOD, ItemBlockIIBase::new, BlockLog.LOG_AXIS);
		this.setHardness(2.0F);
		this.setResistance(1F);
		setCategory(IICategory.RESOURCES);
	}

	public enum CharredLogs implements IIBlockEnum
	{
		@IIBlockProperties(oreDict = {"logWood", "logCharred"})
		MAIN
	}

	@Nullable
	@Override
	public String getMappingsExtension(int meta, boolean itemBlock)
	{
		return null;
	}

	@Override
	protected IBlockState getInitDefaultState()
	{
		return super.getInitDefaultState()
				.withProperty(property, CharredLogs.MAIN)
				.withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE);
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return this.getDefaultState()
				.withProperty(BlockLog.LOG_AXIS, EnumAxis.fromFacingAxis(facing.getAxis()));
	}

	@Override
	@Nonnull
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState()
				.withProperty(property, CharredLogs.values()[(int)Math.floor(meta/4f)])
				.withProperty(BlockLog.LOG_AXIS, EnumAxis.values()[MathHelper.clamp(meta%3, 0, 3)]);
	}

	@Override
	public int getMetaFromState(@Nullable IBlockState state)
	{
		if(state==null||enumValues==null||!this.equals(state.getBlock())) return 0;
		return state.getValue(BlockLog.LOG_AXIS).ordinal()+(state.getValue(property).getMeta()*4);
	}

	@Override
	@Nonnull
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	@ParametersAreNonnullByDefault
	public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}

	@Override
	@ParametersAreNonnullByDefault
	public boolean isWood(IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return state.getValue(property).getMeta()*4;
	}

	@Override
	@SuppressWarnings("deprecation")
	@Nonnull
	@ParametersAreNonnullByDefault
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.SOLID;
	}
}