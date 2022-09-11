package pl.pabilo8.immersiveintelligence.common.block.simple;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IColouredBlock;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IColouredItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIRubberLeaves.RubberStuff;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class BlockIIRubberLeaves extends BlockIIBase<RubberStuff> implements IShearable, IColouredBlock, IColouredItem
{
	protected boolean leavesFancy;
	int[] surroundings;

	public enum RubberStuff implements IIBlockEnum
	{
		RUBBER
	}

	public BlockIIRubberLeaves()
	{
		super("rubber_leaves", PropertyEnum.create("type", RubberStuff.class), Material.LEAVES, ItemBlockIILeaves::new,
				BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE);
		this.setTickRandomly(true);
		this.setHardness(0.2F);
		this.setLightOpacity(1);
		lightOpacity = 0;
	}

	@Override
	@Nonnull
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(property, RubberStuff.RUBBER).withProperty(BlockLeaves.CHECK_DECAY, meta==1).withProperty(BlockLeaves.DECAYABLE, meta==1);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		if(state==null||enumValues==null||!this.equals(state.getBlock())) return 0;
		return state.getValue(BlockLeaves.DECAYABLE)?1: 0;
	}

	@Override
	protected IBlockState getInitDefaultState()
	{
		return super.getInitDefaultState().withProperty(BlockLeaves.CHECK_DECAY, false).withProperty(BlockLeaves.DECAYABLE, false);
	}

	@Override
	public boolean canIEBlockBePlaced(World world, BlockPos pos, IBlockState newState, EnumFacing side, float hitX, float hitY, float hitZ, EntityPlayer player, ItemStack stack)
	{
		return true;
	}

	public void breakBlock(World worldIn, BlockPos pos, @Nonnull IBlockState state)
	{
		int k = pos.getX();
		int l = pos.getY();
		int i1 = pos.getZ();

		if(worldIn.isAreaLoaded(new BlockPos(k-2, l-2, i1-2), new BlockPos(k+2, l+2, i1+2)))
			for(int j1 = -1; j1 <= 1; ++j1)
				for(int k1 = -1; k1 <= 1; ++k1)
					for(int l1 = -1; l1 <= 1; ++l1)
					{
						BlockPos blockpos = pos.add(j1, k1, l1);
						IBlockState iblockstate = worldIn.getBlockState(blockpos);

						if(iblockstate.getBlock().isLeaves(iblockstate, worldIn, blockpos))
							iblockstate.getBlock().beginLeavesDecay(iblockstate, worldIn, blockpos);
					}
	}

	public void updateTick(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand)
	{
		if(!worldIn.isRemote)
			if(state.getValue(BlockLeaves.CHECK_DECAY)&&state.getValue(BlockLeaves.DECAYABLE))
			{
				int k = pos.getX();
				int l = pos.getY();
				int i1 = pos.getZ();

				if(this.surroundings==null)
					this.surroundings = new int[32768];

				if(!worldIn.isAreaLoaded(pos, 1))
					return; // Forge: prevent decaying leaves from updating neighbors and loading unloaded chunks
				if(worldIn.isAreaLoaded(pos, 6)) // Forge: extend range from 5 to 6 to account for neighbor checks in world.markAndNotifyBlock -> world.updateObservingBlocksAt
				{
					BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

					for(int i2 = -4; i2 <= 4; ++i2)
						for(int j2 = -4; j2 <= 4; ++j2)
							for(int k2 = -4; k2 <= 4; ++k2)
							{
								IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos.setPos(k+i2, l+j2, i1+k2));
								Block block = iblockstate.getBlock();

								final int index = (i2+16)*1024+(j2+16)*32+k2+16;
								if(!block.canSustainLeaves(iblockstate, worldIn, blockpos$mutableblockpos.setPos(k+i2, l+j2, i1+k2)))
									if(block.isLeaves(iblockstate, worldIn, blockpos$mutableblockpos.setPos(k+i2, l+j2, i1+k2)))
										this.surroundings[index] = -2;
									else
										this.surroundings[index] = -1;
								else
									this.surroundings[index] = 0;
							}

					for(int i3 = 1; i3 <= 4; ++i3)
						for(int j3 = -4; j3 <= 4; ++j3)
							for(int k3 = -4; k3 <= 4; ++k3)
								for(int l3 = -4; l3 <= 4; ++l3)
									if(this.surroundings[(j3+16)*1024+(k3+16)*32+l3+16]==i3-1)
									{
										if(this.surroundings[(j3+16-1)*1024+(k3+16)*32+l3+16]==-2)
											this.surroundings[(j3+16-1)*1024+(k3+16)*32+l3+16] = i3;

										if(this.surroundings[(j3+16+1)*1024+(k3+16)*32+l3+16]==-2)
											this.surroundings[(j3+16+1)*1024+(k3+16)*32+l3+16] = i3;

										if(this.surroundings[(j3+16)*1024+(k3+16-1)*32+l3+16]==-2)
											this.surroundings[(j3+16)*1024+(k3+16-1)*32+l3+16] = i3;

										if(this.surroundings[(j3+16)*1024+(k3+16+1)*32+l3+16]==-2)
											this.surroundings[(j3+16)*1024+(k3+16+1)*32+l3+16] = i3;

										if(this.surroundings[(j3+16)*1024+(k3+16)*32+(l3+16-1)]==-2)
											this.surroundings[(j3+16)*1024+(k3+16)*32+(l3+16-1)] = i3;

										if(this.surroundings[(j3+16)*1024+(k3+16)*32+l3+16+1]==-2)
											this.surroundings[(j3+16)*1024+(k3+16)*32+l3+16+1] = i3;
									}
				}

				int l2 = this.surroundings[16912];

				if(l2 >= 0)
					worldIn.setBlockState(pos, state.withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE), 4);
				else
					this.destroy(worldIn, pos);
			}
	}

	private void destroy(@Nonnull World worldIn, @Nonnull BlockPos pos)
	{
		this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
		worldIn.setBlockToAir(pos);
	}

	@SideOnly(Side.CLIENT)
	@SuppressWarnings("deprecation")
	public void randomDisplayTick(@Nonnull IBlockState stateIn, World worldIn, BlockPos pos, @Nonnull Random rand)
	{
		if(worldIn.isRainingAt(pos.up())&&!worldIn.getBlockState(pos.down()).isTopSolid()&&rand.nextInt(15)==1)
		{
			double d0 = (float)pos.getX()+rand.nextFloat();
			double d1 = (double)pos.getY()-0.05D;
			double d2 = (float)pos.getZ()+rand.nextFloat();
			worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(@Nonnull Random random)
	{
		return random.nextInt(20)==0?1: 0;
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	@Nonnull
	public Item getItemDropped(@Nonnull IBlockState state, @Nonnull Random rand, int fortune)
	{
		return Item.getItemFromBlock(IIContent.blockRubberSapling);
	}

	/**
	 * Spawns this Block's drops into the World as EntityItems.
	 */
	public void dropBlockAsItemWithChance(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, float chance, int fortune)
	{
		super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	public boolean isOpaqueCube(@Nonnull IBlockState state)
	{
		return !this.leavesFancy;
	}

	@SideOnly(Side.CLIENT)
	@Nonnull
	public BlockRenderLayer getBlockLayer()
	{
		this.leavesFancy = Minecraft.getMinecraft().gameSettings.fancyGraphics;

		return this.leavesFancy?BlockRenderLayer.CUTOUT_MIPPED: BlockRenderLayer.SOLID;
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		return layer==getBlockLayer();
	}

	@SuppressWarnings("deprecation")
	public boolean causesSuffocation(@Nonnull IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isLeaves(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos)
	{
		return true;
	}

	@Override
	public void beginLeavesDecay(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos)
	{
		if(!(Boolean)state.getValue(BlockLeaves.CHECK_DECAY))
			world.setBlockState(pos, state.withProperty(BlockLeaves.CHECK_DECAY, true), 4);
	}

	@Override
	public void getDrops(@Nonnull NonNullList<ItemStack> drops, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune)
	{
		Random rand = world instanceof World?((World)world).rand: new Random();
		//drop chance
		int chance = 20;

		if(fortune > 0)
		{
			chance -= 2<<fortune;
			if(chance < 30) chance = 30;
		}

		if(rand.nextInt(chance)==0)
		{
			ItemStack drop = new ItemStack(getItemDropped(state, rand, fortune), 1);
			if(!drop.isEmpty())
				drops.add(drop);
		}
		this.captureDrops(true);
		drops.addAll(this.captureDrops(false));
	}


	@SideOnly(Side.CLIENT)
	@SuppressWarnings("deprecation")
	public boolean shouldSideBeRendered(@Nonnull IBlockState blockState, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, @Nonnull EnumFacing side)
	{
		return (this.leavesFancy||blockAccess.getBlockState(pos.offset(side)).getBlock()!=this)&&super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@Override
	public boolean isShearable(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Nonnull
	@Override
	public List<ItemStack> onSheared(@Nonnull ItemStack itemStack, IBlockAccess iBlockAccess, BlockPos blockPos, int i)
	{
		return NonNullList.withSize(1, new ItemStack(this, 1));
	}

	@Override
	public boolean hasCustomBlockColours()
	{
		return true;
	}

	@Override
	public int getRenderColour(@Nonnull IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex)
	{
		return worldIn!=null&&pos!=null?BiomeColorHelper.getFoliageColorAtPos(worldIn, pos): ColorizerFoliage.getFoliageColorBasic();
	}


	public static class ItemBlockIILeaves extends ItemBlockIIBase implements IColouredItem
	{
		public ItemBlockIILeaves(BlockIIBase b)
		{
			super(b);
		}

		@Override
		public boolean hasCustomItemColours()
		{
			return true;
		}

		@Override
		public int getColourForIEItem(ItemStack stack, int pass)
		{
			return ColorizerFoliage.getFoliageColorBasic();
		}
	}
}