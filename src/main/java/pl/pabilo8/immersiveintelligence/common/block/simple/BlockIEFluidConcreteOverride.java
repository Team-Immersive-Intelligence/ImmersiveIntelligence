package pl.pabilo8.immersiveintelligence.common.block.simple;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockIEFluidConcrete;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
import blusunrize.immersiveengineering.common.util.IEPotions;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Random;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 11.01.2026
 */
public class BlockIEFluidConcreteOverride extends BlockIEFluidConcrete
{
	public BlockIEFluidConcreteOverride()
	{
		super("fluidConcrete", IEContent.fluidConcrete, Material.WATER);
		IEContent.registeredIEBlocks.removeIf(item -> item instanceof BlockIEFluidConcrete);
		IEContent.registeredIEBlocks.add(this);
		this.setQuantaPerBlock(8);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if(!isSourceBlock(world, pos)&&ForgeEventFactory.canCreateFluidSource(world, pos, state, false))
		{
			int adjacentSourceBlocks =
					(isSourceBlock(world, pos.north())?1: 0)+
							(isSourceBlock(world, pos.south())?1: 0)+
							(isSourceBlock(world, pos.east())?1: 0)+
							(isSourceBlock(world, pos.west())?1: 0);
			if(adjacentSourceBlocks >= 2&&(world.getBlockState(pos.up(densityDir)).getMaterial().isSolid()||isSourceBlock(world, pos.up(densityDir))))
				world.setBlockState(pos, state.withProperty(LEVEL, 0));
		}

		int level = state.getValue(LEVEL);
		int timer = state.getValue(IEProperties.INT_16);
		int quantaRemaining = quantaPerBlock-level;
		int expQuanta = -101;

		// Increase drying time (was effectively up to 14 ticks depending on depth)
		final int hardenAfter = 480;

		if(timer >= Math.min(hardenAfter, quantaRemaining))
		{
			final IBlockState solidState =
					(level >= 6)
							?IEContent.blockStoneDecoration.getStateFromMeta(BlockTypes_StoneDecoration.CONCRETE.getMeta())
							: IEContent.blockStoneDecorationSlabs.getStateFromMeta(BlockTypes_StoneDecoration.CONCRETE.getMeta());

			world.setBlockState(pos, solidState);
			for(EntityLivingBase living : world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos, pos.add(1, 1, 1))))
				living.addPotionEffect(new PotionEffect(IEPotions.concreteFeet, Integer.MAX_VALUE));
			return;
		}
		else
		{
			state = state.withProperty(IEProperties.INT_16, Math.min(15, timer+1));
			world.setBlockState(pos, state);

		}

		// check adjacent block levels if non-source
		if(quantaRemaining < quantaPerBlock)
		{
			if(world.getBlockState(pos.add(0, -densityDir, 0)).getBlock()==this||
					world.getBlockState(pos.add(-1, -densityDir, 0)).getBlock()==this||
					world.getBlockState(pos.add(1, -densityDir, 0)).getBlock()==this||
					world.getBlockState(pos.add(0, -densityDir, -1)).getBlock()==this||
					world.getBlockState(pos.add(0, -densityDir, 1)).getBlock()==this)
			{
				expQuanta = quantaPerBlock-1;
			}
			else
			{
				int maxQuanta = -100;
				maxQuanta = getLargerQuanta(world, pos.add(-1, 0, 0), maxQuanta);
				maxQuanta = getLargerQuanta(world, pos.add(1, 0, 0), maxQuanta);
				maxQuanta = getLargerQuanta(world, pos.add(0, 0, -1), maxQuanta);
				maxQuanta = getLargerQuanta(world, pos.add(0, 0, 1), maxQuanta);

				expQuanta = maxQuanta-1;
			}

			int total = level;
			int blocks = 1;
			for(EnumFacing f : EnumFacing.HORIZONTALS)
			{
				IBlockState otherState = world.getBlockState(pos.offset(f));
				if(otherState.getBlock()==this)
				{
					blocks++;
					total += otherState.getValue(LEVEL);
				}
			}
			int newEvenQuanta = (int)Math.ceil(total/(float)blocks);
			for(EnumFacing f : EnumFacing.HORIZONTALS)
			{
				IBlockState otherState = world.getBlockState(pos.offset(f));
				if(otherState.getBlock()==this)
					world.setBlockState(pos.offset(f), otherState.withProperty(LEVEL, newEvenQuanta));

			}

			// decay calculation
			if(expQuanta!=quantaRemaining)
			{
				quantaRemaining = expQuanta;

				if(expQuanta <= 0)
					world.setBlockToAir(pos);
				else
				{
					world.setBlockState(pos, state.withProperty(LEVEL, quantaPerBlock-expQuanta), 2);
					world.scheduleUpdate(pos, this, tickRate);
					world.notifyNeighborsOfStateChange(pos, this, true);
				}
			}
		}
		// This is a "source" block, set meta to zero, and send a server only update
		else if(quantaRemaining >= quantaPerBlock)
			world.setBlockState(pos, this.getDefaultState(), 2);

		// Flow vertically if possible
		if(canDisplace(world, pos.up(densityDir)))
		{
			flowIntoBlockRet(world, pos.up(densityDir), 1, timer);
			return;
		}

		// Flow outward if possible
		int flowMeta = quantaPerBlock-quantaRemaining+1;
		if(flowMeta >= quantaPerBlock)
		{
			world.setBlockState(pos, state.withProperty(IEProperties.INT_16, Math.min(15, timer+1)));
			world.scheduleUpdate(pos, this, tickRate);
			return;
		}

		if(isSourceBlock(world, pos)||!isFlowingVertically(world, pos))
		{
			if(world.getBlockState(pos.down(densityDir)).getBlock()==this)
				flowMeta = 1;
			boolean flowTo[] = getOptimalFlowDirections(world, pos);
			boolean hasFlown = false;
			if(flowTo[0])
				hasFlown |= flowIntoBlockRet(world, pos.add(-1, 0, 0), flowMeta, timer);
			if(flowTo[1])
				hasFlown |= flowIntoBlockRet(world, pos.add(1, 0, 0), flowMeta, timer);
			if(flowTo[2])
				hasFlown |= flowIntoBlockRet(world, pos.add(0, 0, -1), flowMeta, timer);
			if(flowTo[3])
				hasFlown |= flowIntoBlockRet(world, pos.add(0, 0, 1), flowMeta, timer);

			if(!hasFlown)
			{
				world.setBlockState(pos, state.withProperty(IEProperties.INT_16, Math.min(15, timer+1)));
				world.scheduleUpdate(pos, this, tickRate);
			}
		}
	}

	protected boolean flowIntoBlockRet(World world, BlockPos pos, int meta, int harden)
	{
		if(meta < 0)
			return false;
		if(displaceIfPossible(world, pos))
		{
			world.setBlockState(pos, this.getBlockState().getBaseState().withProperty(LEVEL, meta).withProperty(IEProperties.INT_16, harden), 3);
			return true;
		}
		return false;
	}
}
