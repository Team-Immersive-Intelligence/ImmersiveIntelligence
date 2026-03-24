package pl.pabilo8.immersiveintelligence.common.util.block;

import blusunrize.immersiveengineering.common.blocks.TileEntityIESlab;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockIISlab<E extends Enum<E> & IIBlockEnum> extends BlockIIBase<E>
{
	public static final PropertyInteger prop_SlabType = PropertyInteger.create("slabtype", 0, 2);

	public BlockIISlab(BlockIIBase<E> base)
	{
		super(base.name+"_slab", base.property, base.materials[0], ItemBlockIISlabs::new, prop_SlabType);

		//Copy properties from base block
		System.arraycopy(base.hidden, 0, this.hidden, 0, base.enumValues.length);
		System.arraycopy(base.fullCubes, 0, this.fullCubes, 0, base.enumValues.length);
		System.arraycopy(base.category, 0, this.category, 0, base.enumValues.length);
		System.arraycopy(base.stackAmounts, 0, this.stackAmounts, 0, base.enumValues.length);
		System.arraycopy(base.opaqueness, 0, this.opaqueness, 0, base.enumValues.length);
		System.arraycopy(base.hardness, 0, this.hardness, 0, base.enumValues.length);
		System.arraycopy(base.blastResistance, 0, this.blastResistance, 0, base.enumValues.length);
		System.arraycopy(base.description, 0, this.description, 0, base.enumValues.length);
		System.arraycopy(base.materials, 0, this.materials, 0, base.enumValues.length);
		System.arraycopy(base.soundTypes, 0, this.soundTypes, 0, base.enumValues.length);
	}

	/**
	 * Get the actual Block state of this Block at the given position. This applies properties not visible in the metadata,
	 * such as fence connections.
	 */
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getActualState(state, world, pos);
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityIESlab)
			return state.withProperty(prop_SlabType, ((TileEntityIESlab)tile).slabType);
		return state;
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityIESlab();
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
	}

	/**
	 * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
	 * Block.removedByPlayer
	 */
	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity tile, ItemStack stack)
	{
		if(tile instanceof TileEntityIESlab&&!player.capabilities.isCreativeMode)
		{
			spawnAsEntity(world, pos, new ItemStack(this, ((TileEntityIESlab)tile).slabType==2?2: 1, this.getMetaFromState(state)));
			return;
		}
		super.harvestBlock(world, player, pos, state, tile, stack);
	}

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityIESlab)
		{
			int type = ((TileEntityIESlab)te).slabType;
			if(type==0)
				return side==EnumFacing.DOWN;
			else if(type==1)
				return side==EnumFacing.UP;
		}
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullBlock(@Nonnull IBlockState state)
	{
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(@Nonnull IBlockState state)
	{
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(@Nonnull IBlockState state)
	{
		return false;
	}

	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityIESlab)
		{
			//only double slabs should block light
			if(((TileEntityIESlab)te).slabType==2)
				return super.getLightOpacity(state, world, pos);
			return 0;
		}
		return 0;
	}

	/**
	 * Get the geometry of the queried face at the given position and state. This is used to decide whether things like
	 * buttons are allowed to be placed on the face, or how glass panes connect to the face, among other things.
	 * <p>
	 * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED}, which represents something that does
	 * not fit the other descriptions and will generally cause other things not to connect to the face.
	 *
	 * @return an approximation of the form of the given face
	 * @deprecated call via {@link IBlockState#getBlockFaceShape(IBlockAccess, BlockPos, EnumFacing)} whenever possible.
	 * Implementing/overriding is fine.
	 */
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityIESlab)
		{
			int type = ((TileEntityIESlab)te).slabType;
			if(type==2)
				return BlockFaceShape.SOLID;
			else if((type==0&&side==EnumFacing.DOWN)||(type==1&&side==EnumFacing.UP))
				return BlockFaceShape.SOLID;
			else
				return BlockFaceShape.UNDEFINED;
		}
		return BlockFaceShape.SOLID;
	}

	/**
	 * @deprecated call via {@link IBlockState#getBoundingBox(IBlockAccess, BlockPos)} whenever possible.
	 * Implementing/overriding is fine.
	 */
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityIESlab)
		{
			int type = ((TileEntityIESlab)te).slabType;
			if(type==0)
				return new AxisAlignedBB(0, 0, 0, 1, .5f, 1);
			else if(type==1)
				return new AxisAlignedBB(0, .5f, 0, 1, 1, 1);
			else
				return FULL_BLOCK_AABB;
		}
		else
			return new AxisAlignedBB(0, 0, 0, 1, .5f, 1);
	}
}
