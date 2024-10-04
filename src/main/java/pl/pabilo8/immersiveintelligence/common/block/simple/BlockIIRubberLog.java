package pl.pabilo8.immersiveintelligence.common.block.simple;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIRubberLog.RubberLogs;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.TernaryValue;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class BlockIIRubberLog extends BlockIIBase<RubberLogs>
{
	public BlockIIRubberLog()
	{
		super("rubber_log", PropertyEnum.create("type", RubberLogs.class), Material.WOOD, ItemBlockIIBase::new, BlockLog.LOG_AXIS);
		this.setHardness(2.0F);
		this.setResistance(1F);
		setCategory(IICategory.RESOURCES);

		setBlockLayer(BlockRenderLayer.SOLID);
		setSubBlockLayer(RubberLogs.STRIPPED, BlockRenderLayer.CUTOUT);
	}

	public enum RubberLogs implements IIBlockEnum
	{
		RAW,
		@IIBlockProperties(hidden = TernaryValue.TRUE)
		REBBUR, //Carver reference, it's truly a great name xD
		@IIBlockProperties(hidden = TernaryValue.TRUE, fullCube = TernaryValue.FALSE)
		STRIPPED;
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
		return super.getInitDefaultState().withProperty(property, RubberLogs.RAW).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return this.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.fromFacingAxis(facing.getAxis()));
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(property, RubberLogs.values()[(int)Math.floor(meta/4f)]).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.values()[MathHelper.clamp(meta%3, 0, 3)]);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		if(state==null||enumValues==null||!this.equals(state.getBlock())) return 0;
		return state.getValue(BlockLog.LOG_AXIS).ordinal()+(state.getValue(property).getMeta()*4);
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

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(state.getValue(property)==RubberLogs.REBBUR)
		{
			ItemStack heldItem = player.getHeldItem(hand);
			if(heldItem.getItem().getToolClasses(heldItem).contains("axe"))
			{
				for(EnumFacing horizontal : EnumFacing.HORIZONTALS)
				{
					Vec3d vpos = new Vec3d(pos).addVector(0.5, 0.65, 0.5).add(new Vec3d(horizontal.getDirectionVec()).scale(0.5));
					for(int i = 0; i < 10; i++)
					{
						Vec3d vv = new Vec3d(horizontal.getDirectionVec()).addVector(0, -2, 0).scale(Utils.RAND.nextDouble()*0.25);
						Vec3d voff = vpos.add(new Vec3d(horizontal.rotateY().getDirectionVec()).scale(i/10d));
						// new Vec3d(Utils.RAND.nextDouble(), 0.5, Utils.RAND.nextDouble()).addVector(0.5, 0, 0.5).scale(2)
						world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, voff.x, voff.y, voff.z, vv.x, vv.y, vv.z, getStateId(state));
					}
				}
				world.setBlockState(pos, state.withProperty(property, RubberLogs.STRIPPED));
			}
		}
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return state.getValue(property)==RubberLogs.STRIPPED?new AxisAlignedBB(0.0625, 0, 0.0625, 0.9375, 1, 0.9375): FULL_BLOCK_AABB;
	}
}