package pl.pabilo8.immersiveintelligence.common.block.simple;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIHarborSupport.HarborSupportType;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Vertical harbor support. A column can add a concrete foot where it meets solid ground
 * and a top fitting where it directly supports a harbor floor.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.07.2026
 */
public class BlockIIHarborSupport extends BlockIIBase<HarborSupportType>
{
	public static final PropertyBool BOTTOM = PropertyBool.create("bottom");
	public static final PropertyBool TOP = PropertyBool.create("top");
	private static final AxisAlignedBB SUPPORT_AABB = new AxisAlignedBB(0.25, 0, 0.25, 0.75, 1, 0.75);

	public BlockIIHarborSupport()
	{
		super("harbor_support", PropertyEnum.create("type", HarborSupportType.class), Material.WOOD,
				ItemBlockIIBase::new, BOTTOM, TOP);
		setHardness(3.0F);
		setResistance(15.0F);
		setCategory(IICategory.RESOURCES);
		setFullCube(false);
		setLightOpacity(0);
		setBlockLayer(BlockRenderLayer.CUTOUT_MIPPED);
		setHarvestLevel("axe", 0);
	}

	@Override
	protected IBlockState getInitDefaultState()
	{
		return super.getInitDefaultState()
				.withProperty(BOTTOM, false)
				.withProperty(TOP, false);
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getActualState(state, world, pos);

		BlockPos belowPos = pos.down();
		IBlockState below = world.getBlockState(belowPos);
		boolean bottom = below.getBlock()!=this&&below.isSideSolid(world, belowPos, EnumFacing.UP);
		boolean top = world.getBlockState(pos.up()).getBlock() instanceof BlockIIHarbor;

		return state
				.withProperty(BOTTOM, bottom)
				.withProperty(TOP, top);
	}

	@Override
	public String getMappingsExtension(int meta, boolean itemBlock)
	{
		String extension = super.getMappingsExtension(meta, itemBlock);
		return itemBlock&&extension!=null?extension+"_item": extension;
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return SUPPORT_AABB;
	}

	@Nullable
	@Override
	@ParametersAreNonnullByDefault
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return SUPPORT_AABB;
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side)
	{
		return side.getAxis()==EnumFacing.Axis.Y?BlockFaceShape.CENTER_BIG: BlockFaceShape.MIDDLE_POLE;
	}

	@Override
	@ParametersAreNonnullByDefault
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	public enum HarborSupportType implements IIBlockEnum
	{
		@IIBlockProperties(needsCustomState = true)
		WOODEN_PIER_SUPPORT
	}
}
