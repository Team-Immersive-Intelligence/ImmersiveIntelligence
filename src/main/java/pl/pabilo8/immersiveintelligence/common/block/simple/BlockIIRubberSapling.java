package pl.pabilo8.immersiveintelligence.common.block.simple;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIRubberLeaves.RubberStuff;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.world.IIWorldGen;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class BlockIIRubberSapling extends BlockIIBase<RubberStuff> implements IGrowable, IPlantable
{
	private static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

	public BlockIIRubberSapling()
	{
		super("rubber_sapling", PropertyEnum.create("type", RubberStuff.class), Material.PLANTS, ItemBlockIIBase::new,
				BlockSapling.STAGE);
		setBlockLayer(BlockRenderLayer.CUTOUT_MIPPED);
		setLightOpacity(0);
		setFullCube(false);
		setCategory(IICategory.RESOURCES);
	}

	@Nullable
	@Override
	public String getMappingsExtension(int meta, boolean itemBlock)
	{
		return super.getMappingsExtension(meta, itemBlock);
	}

	@Override
	protected IBlockState getInitDefaultState()
	{
		return super.getInitDefaultState().withProperty(property, RubberStuff.RUBBER).withProperty(BlockSapling.STAGE, 0);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(property, RubberStuff.RUBBER).withProperty(BlockSapling.STAGE, meta);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		if(state==null||enumValues==null||!this.equals(state.getBlock())) return 0;
		return state.getValue(BlockSapling.STAGE);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return SAPLING_AABB;
	}

	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if(!worldIn.isRemote)
		{
			super.updateTick(worldIn, pos, state, rand);
			this.checkAndDropBlock(worldIn, pos, state);

			if(!worldIn.isAreaLoaded(pos, 1))
				return; // Forge: prevent loading unloaded chunks when checking neighbor's light
			if(worldIn.getLightFromNeighbors(pos.up()) >= 9&&rand.nextInt(7)==0)
			{
				this.grow(worldIn, pos, state, rand);
			}
		}
	}

	public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if(state.getValue(BlockSapling.STAGE)==0)
		{
			worldIn.setBlockState(pos, state.cycleProperty(BlockSapling.STAGE), 4);
		}
		else
		{
			this.generateTree(worldIn, pos, state, rand);
		}
	}

	public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if(!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;
		{
			IIWorldGen.worldGenRubberTree.generate(worldIn, rand, pos);
			worldIn.setBlockState(pos, IIContent.blockRubberLog.getDefaultState().withProperty(BlockLog.LOG_AXIS, EnumAxis.Y), 1);
		}
	}

	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{
		return true;
	}

	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		return (double)worldIn.rand.nextFloat() < 0.45D;
	}

	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		this.grow(worldIn, pos, state, rand);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		IBlockState soil = worldIn.getBlockState(pos.down());
		return super.canPlaceBlockAt(worldIn, pos)&&soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
	}

	/**
	 * Return true if the block can sustain a Bush
	 */
	protected boolean canSustainBush(IBlockState state)
	{
		return state.getBlock()==Blocks.GRASS||state.getBlock()==Blocks.DIRT||state.getBlock()==Blocks.FARMLAND;
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 * block, etc.
	 */
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		this.checkAndDropBlock(worldIn, pos, state);
	}

	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if(!this.canBlockStay(worldIn, pos, state))
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		if(state.getBlock()==this) //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
		{
			IBlockState soil = worldIn.getBlockState(pos.down());
			return soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
		}
		return this.canSustainBush(worldIn.getBlockState(pos.down()));
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess iBlockAccess, BlockPos blockPos)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public IBlockState getPlant(net.minecraft.world.IBlockAccess world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock()!=this) return getDefaultState();
		return state;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}
}