package pl.pabilo8.immersiveintelligence.common.world;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIRubberLog.RubberLogs;

import java.util.Random;

/**
 * @author Pabilo8
 * @since 20.04.2021
 */
public class IIWorldGenRubberTree extends WorldGenAbstractTree
{
	private static final IBlockState TRUNK = IIContent.blockRubberLog.getDefaultState().withProperty(BlockLog.LOG_AXIS, EnumAxis.Y);
	private static final IBlockState TRUNK_REBBUR = IIContent.blockRubberLog.getDefaultState().withProperty(IIContent.blockRubberLog.property, RubberLogs.REBBUR).withProperty(BlockLog.LOG_AXIS, EnumAxis.Y);
	private static final IBlockState LEAF = IIContent.blockRubberLeaves.getDefaultState().withProperty(BlockLeaves.DECAYABLE, true).withProperty(BlockLeaves.CHECK_DECAY, true);

	public IIWorldGenRubberTree()
	{
		super(false);
	}

	/**
	 * sets dirt at a specific location if it isn't already dirt
	 */
	protected void setDirtAt(World worldIn, BlockPos pos)
	{
		if(worldIn.getBlockState(pos).getBlock()!=Blocks.DIRT)
			this.setBlockAndNotifyAdequately(worldIn, pos, Blocks.DIRT.getDefaultState());
	}

	public boolean isReplaceable(World world, BlockPos pos)
	{
		net.minecraft.block.state.IBlockState state = world.getBlockState(pos);
		return state.getBlock().isAir(state, world, pos)||state.getBlock().isLeaves(state, world, pos)||state.getBlock().isWood(world, pos)||canGrowInto(state.getBlock())||state.getBlock()==IIContent.blockRubberSapling;
	}

	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		int i = rand.nextInt(5)+8;
		int j = i-rand.nextInt(2)-3;
		int k = i-j;
		int l = 1+rand.nextInt(k+1);

		if(position.getY() >= 1&&position.getY()+i+1 <= 256)
		{
			boolean flag = true;

			for(int i1 = position.getY(); i1 <= position.getY()+1+i&&flag; ++i1)
			{
				int j1;

				if(i1-position.getY() < j)
					j1 = 0;
				else
					j1 = l;

				BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

				for(int k1 = position.getX()-j1; k1 <= position.getX()+j1&&flag; ++k1)
					for(int l1 = position.getZ()-j1; l1 <= position.getZ()+j1&&flag; ++l1)
						if(i1 >= 0&&i1 < 256)
						{
							if(!this.isReplaceable(worldIn, blockpos$mutableblockpos.setPos(k1, i1, l1)))
								flag = false;
						}
						else
							flag = false;
			}

			if(!flag)
				return false;
			else
			{
				BlockPos down = position.down();
				IBlockState state = worldIn.getBlockState(down);
				boolean isSoil = state.getBlock().canSustainPlant(state, worldIn, down, net.minecraft.util.EnumFacing.UP, (net.minecraft.block.BlockSapling)Blocks.SAPLING);

				if(isSoil&&position.getY() < 256-i-1)
				{
					state.getBlock().onPlantGrow(state, worldIn, down, position);
					int k2 = 0;

					for(int l2 = position.getY()+i; l2 >= position.getY()+j; --l2)
					{
						for(int j3 = position.getX()-k2; j3 <= position.getX()+k2; ++j3)
						{
							int k3 = j3-position.getX();

							for(int i2 = position.getZ()-k2; i2 <= position.getZ()+k2; ++i2)
							{
								int j2 = i2-position.getZ();

								if(Math.abs(k3)!=k2||Math.abs(j2)!=k2||k2 <= 0)
								{
									BlockPos blockpos = new BlockPos(j3, l2, i2);
									state = worldIn.getBlockState(blockpos);

									if(state.getBlock().canBeReplacedByLeaves(state, worldIn, blockpos))
										this.setBlockAndNotifyAdequately(worldIn, blockpos, LEAF);
								}
							}
						}

						if(k2 >= 1&&l2==position.getY()+j+1)
							--k2;
						else if(k2 < l)
							++k2;
					}

					for(int i3 = 0; i3 < i-1; ++i3)
					{
						BlockPos upN = position.up(i3);
						state = worldIn.getBlockState(upN);

						if(state.getBlock().isAir(state, worldIn, upN)||state.getBlock().isLeaves(state, worldIn, upN))
							this.setBlockAndNotifyAdequately(worldIn, position.up(i3), i3==1?TRUNK_REBBUR: TRUNK);
					}

					return true;
				}
				else
					return false;
			}
		}
		else
			return false;
	}
}