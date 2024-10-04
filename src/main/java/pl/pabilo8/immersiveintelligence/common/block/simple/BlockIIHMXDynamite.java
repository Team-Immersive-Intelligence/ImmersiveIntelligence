package pl.pabilo8.immersiveintelligence.common.block.simple;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIHMXDynamite.HMX_Explosives;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHMXDynamitePrimed;
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
public class BlockIIHMXDynamite extends BlockIIBase<HMX_Explosives>
{
	public BlockIIHMXDynamite()
	{
		super("hmx_dynamite", PropertyEnum.create("type", HMX_Explosives.class), Material.TNT, ItemBlockIIBase::new, BlockLog.LOG_AXIS);
		this.setHardness(3.0F);
		this.setResistance(25F);
		setCategory(IICategory.RESOURCES);
	}

	public enum HMX_Explosives implements IIBlockEnum
	{
		@IIBlockProperties(oreDict = {"explosive_HMX", "explosive_hexamine"})
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
				.withProperty(property, HMX_Explosives.MAIN)
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
				.withProperty(property, HMX_Explosives.values()[(int)Math.floor(meta/4f)])
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

	// Make the block explode when right-clicked with a flint and steel or powered by redstone

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		// If player has a flint and steel or fire charge, ignite the dynamite
		if(!world.isRemote)
		{
			ItemStack heldItem = player.getHeldItem(hand);
			if(heldItem.getItem()==Items.FLINT_AND_STEEL||heldItem.getItem()==Items.FIRE_CHARGE)
			{
				world.setBlockToAir(pos); // Remove the block
				ignite(world, pos, player); // Trigger the explosion
				return true;
			}
		}
		return false;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		// If powered by redstone, ignite the dynamite
		if(world.isBlockPowered(pos))
		{
			ignite(world, pos, null); // Trigger the explosion if powered by redstone
			world.setBlockToAir(pos); // Remove the block
		}
	}

	// Method to ignite the block and spawn an exploding entity
	private void ignite(World world, BlockPos pos, @Nullable EntityLivingBase igniter)
	{
		if(!world.isRemote)
		{
			EntityHMXDynamitePrimed dynamite = new EntityHMXDynamitePrimed(world, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5, igniter);
			world.spawnEntity(dynamite);
			world.playSound(null, dynamite.posX, dynamite.posY, dynamite.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}
}


