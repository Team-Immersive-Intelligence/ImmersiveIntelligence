package pl.pabilo8.immersiveintelligence.common.blocks.wooden;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.blocks.wooden.BlockIIRubberLog.IIBlockTypesRubberLog;

import java.util.Locale;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class BlockIIRubberLog extends BlockIIBase<IIBlockTypesRubberLog>
{
	public BlockIIRubberLog()
	{
		super("rubber_log", Material.WOOD, PropertyEnum.create("type", IIBlockTypesRubberLog.class), ItemBlockIEBase.class, BlockLog.LOG_AXIS);
		this.setHardness(2.0F);
		this.setSoundType(SoundType.WOOD);
		lightOpacity = 0;

	}

	@Override
	protected IBlockState getInitDefaultState()
	{
		return super.getInitDefaultState().withProperty(property,IIBlockTypesRubberLog.RUBBER).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.fromFacingAxis(facing.getAxis()));
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(property, IIBlockTypesRubberLog.RUBBER).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.values()[MathHelper.clamp(meta,0,3)]);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(BlockLog.LOG_AXIS).ordinal();
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public boolean canSustainLeaves(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public boolean isWood(net.minecraft.world.IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}

	public enum IIBlockTypesRubberLog implements IStringSerializable, BlockIEBase.IBlockEnum
	{
		RUBBER;

		@Override
		public String getName()
		{
			return this.toString().toLowerCase(Locale.ENGLISH);
		}

		@Override
		public int getMeta()
		{
			return ordinal();
		}

		@Override
		public boolean listForCreative()
		{
			return true;
		}
	}
}