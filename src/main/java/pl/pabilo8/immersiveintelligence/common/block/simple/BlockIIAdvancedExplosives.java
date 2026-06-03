package pl.pabilo8.immersiveintelligence.common.block.simple;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIAdvancedExplosives.HMX_Explosives;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @updated 03.11.2026
 * @since 08.12.2021
 */
public class BlockIIAdvancedExplosives extends BlockIIBase<HMX_Explosives>
{
	public BlockIIAdvancedExplosives()
	{
		super("advanced_explosives", PropertyEnum.create("type", HMX_Explosives.class), Material.TNT, ItemBlockIIBase::new);
		this.setHardness(3.0F);
		this.setResistance(25F);
		setCategory(IICategory.WARFARE);
		this.setTickRandomly(true);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		//TODO: 05.01.2025 II lighter
		if(!world.isRemote)
		{
			ItemStack heldItem = player.getHeldItem(hand);
			if(heldItem.getItem()==Items.FLINT_AND_STEEL||heldItem.getItem()==Items.FIRE_CHARGE)
			{
				world.setBlockToAir(pos); //Remove the block
				explode(world, pos, state, player); //Trigger the explosion
				return true;
			}
		}
		return false;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		if(!world.isRemote)
		{
			//If powered by redstone, detonate
			if(world.isBlockPowered(pos))
			{
				explode(world, pos, state, null);
				world.setBlockToAir(pos);
				return;
			}
			//If fire is used, detonate
			if(isAdjacentToFire(world, pos))
			{
				explode(world, pos, state, null);
				world.setBlockToAir(pos);
			}
		}
	}

	@Override
	public void randomTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		if(!world.isRemote&&isAdjacentToFire(world, pos))
		{
			explode(world, pos, state, null);
			world.setBlockToAir(pos);
		}
	}


	//checks if fire is adject to the block
	private boolean isAdjacentToFire(World world, BlockPos pos)
	{
		for(EnumFacing facing : EnumFacing.VALUES)
		{
			IBlockState neighbor = world.getBlockState(pos.offset(facing));
			if(neighbor.getBlock()==Blocks.FIRE||neighbor.getMaterial()==Material.FIRE)
				return true;
		}
		return false;
	}

	//Can catch on fire/ firespread

	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return true;
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 100;
	}


	public void explode(World world, BlockPos pos, @Nullable EntityLivingBase igniter)
	{
		if(!world.isRemote)
		{
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock()==this)
				explode(world, pos, state, igniter);
		}
	}

	public void explode(World world, BlockPos pos, IBlockState state, @Nullable EntityLivingBase igniter)
	{
		if(!world.isRemote)
		{
			HMX_Explosives type = state.getValue(this.property);
			AmmoComponent component;
			switch(type)
			{
				case RDX:
					component = IIContent.ammoComponentRDX;
					break;
				case HMX:
					component = IIContent.ammoComponentHMX;
					break;
				case WHITE_PHOSPHORUS:
					component = IIContent.ammoComponentWhitePhosphorus;
					break;
				default:
					return; //Nie wolno, nie można, nie potrzeba nam tego
			}
			component.onEffect(world, new Vec3d(pos), new Vec3d(0, -1, 0), ComponentEffectShape.ORB, new NBTTagCompound(), 1.0f, 1.0f, igniter);
		}
	}

	public enum HMX_Explosives implements IIBlockEnum
	{
		@IIBlockProperties(oreDict = {"explosiveRDX", "explosiveHexogen"}, needsCustomState = true)
		RDX,
		@IIBlockProperties(oreDict = {"explosiveHMX", "explosiveHexamine"}, needsCustomState = true)
		HMX,
		@IIBlockProperties(oreDict = {"explosiveWhitePhosphorus"}, needsCustomState = true)
		WHITE_PHOSPHORUS
	}
}


