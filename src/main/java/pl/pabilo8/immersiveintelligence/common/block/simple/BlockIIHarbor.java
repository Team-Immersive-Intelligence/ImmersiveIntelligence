package pl.pabilo8.immersiveintelligence.common.block.simple;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIHarbor.HarborType;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;

/**
 * Connected harbor floor pieces. The saved block state contains only {@link HarborType};
 * the support model and quay-to-land contact are derived from neighbouring blocks.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.07.2026
 */
public class BlockIIHarbor extends BlockIIBase<HarborType>
{
	public static final PropertyBool CONNECTED = PropertyBool.create("connected");
	public static final PropertyEnum<HarborConnection> SUPPORT = PropertyEnum.create("support", HarborConnection.class);
	public static final PropertyBool SOLID_NEIGHBOR = PropertyBool.create("solid_neighbor");

	public BlockIIHarbor()
	{
		super("harbor", PropertyEnum.create("type", HarborType.class), Material.WOOD, ItemBlockIIBase::new,
				CONNECTED, SUPPORT, SOLID_NEIGHBOR);
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
				.withProperty(CONNECTED, false)
				.withProperty(SUPPORT, HarborConnection.CENTER)
				.withProperty(SOLID_NEIGHBOR, false);
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getActualState(state, world, pos);
		boolean connected = hasHarborNeighbor(world, pos);
		HarborConnection connection = getConnection(world, pos);
		boolean solidNeighbor = state.getValue(property)==HarborType.WOODEN_QUAY
				&&connection!=HarborConnection.CENTER
				&&hasSolidNeighbor(world, pos);
		return state
				.withProperty(CONNECTED, connected)
				.withProperty(SUPPORT, connection)
				.withProperty(SOLID_NEIGHBOR, solidNeighbor);
	}

	private boolean hasHarborNeighbor(IBlockAccess world, BlockPos pos)
	{
		for(EnumFacing facing : EnumFacing.HORIZONTALS)
			if(isHarbor(world, pos.offset(facing)))
				return true;
		return false;
	}

	private HarborConnection getConnection(IBlockAccess world, BlockPos pos)
	{
		int x = 0;
		int z = 0;

		if(!isHarbor(world, pos.north())) z--;
		if(!isHarbor(world, pos.south())) z++;
		if(!isHarbor(world, pos.west())) x--;
		if(!isHarbor(world, pos.east())) x++;

		return HarborConnection.fromOffset(Integer.signum(x), Integer.signum(z));
	}

	private boolean isHarbor(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos).getBlock()==this;
	}

	private boolean hasSolidNeighbor(IBlockAccess world, BlockPos pos)
	{
		for(EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			BlockPos neighborPos = pos.offset(facing);
			IBlockState neighbor = world.getBlockState(neighborPos);
			if(neighbor.getBlock()!=this&&neighbor.isSideSolid(world, neighborPos, facing.getOpposite()))
				return true;
		}
		return false;
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
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side)
	{
		return side==EnumFacing.UP?BlockFaceShape.SOLID: BlockFaceShape.UNDEFINED;
	}

	@Override
	@ParametersAreNonnullByDefault
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	public enum HarborType implements IIBlockEnum
	{
		@IIBlockProperties(needsCustomState = true)
		WOODEN_PIER,
		@IIBlockProperties(needsCustomState = true)
		WOODEN_QUAY
	}

	/**
	 * Direction of the exposed edge of a connected harbor surface.
	 * Opposite exposed edges cancel into {@link #CENTER}; this keeps lines and isolated blocks deterministic
	 * while preserving all nine useful states for rectangular placements.
	 */
	public enum HarborConnection implements IStringSerializable
	{
		CENTER(0, 0),
		N(0, -1),
		NE(1, -1),
		E(1, 0),
		SE(1, 1),
		S(0, 1),
		SW(-1, 1),
		W(-1, 0),
		NW(-1, -1);

		private final int x;
		private final int z;

		HarborConnection(int x, int z)
		{
			this.x = x;
			this.z = z;
		}

		public static HarborConnection fromOffset(int x, int z)
		{
			for(HarborConnection connection : values())
				if(connection.x==x&&connection.z==z)
					return connection;
			return CENTER;
		}

		@Override
		public String getName()
		{
			return name().toLowerCase(Locale.ENGLISH);
		}
	}
}
