package pl.pabilo8.immersiveintelligence.common.blocks.wooden;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleBlockDust;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
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
	public static final PropertyEnum<IIBlockTypesRubberLog> PROP = PropertyEnum.create("type", IIBlockTypesRubberLog.class);

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0625,0,0.0625,0.9375,1, 0.9375);

	public BlockIIRubberLog()
	{
		super("rubber_log", Material.WOOD, PROP, ItemBlockIEBase.class, BlockLog.LOG_AXIS);
		this.setHardness(2.0F);
		this.setSoundType(SoundType.WOOD);
		lightOpacity = 0;
		setMetaHidden(1);
		setMetaHidden(2);
	}

	@Override
	protected boolean normalBlockCheck(IBlockState state)
	{
		return state.getValue(PROP).getMeta()!=2;
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		return layer==(state.getValue(PROP)==IIBlockTypesRubberLog.STRIPPED?BlockRenderLayer.CUTOUT: BlockRenderLayer.SOLID);
	}

	@Override
	protected IBlockState getInitDefaultState()
	{
		return super.getInitDefaultState().withProperty(PROP, IIBlockTypesRubberLog.RAW).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.fromFacingAxis(facing.getAxis()));
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(PROP, IIBlockTypesRubberLog.values()[(int)Math.floor(meta/4f)]).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.values()[MathHelper.clamp(meta%3, 0, 3)]);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(BlockLog.LOG_AXIS).ordinal()+(state.getValue(PROP).getMeta()*4);
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

	public enum IIBlockTypesRubberStuff implements IStringSerializable, BlockIEBase.IBlockEnum
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

	public enum IIBlockTypesRubberLog implements IStringSerializable, BlockIEBase.IBlockEnum
	{
		RAW,
		REBBUR, //Carver reference, it's truly a great name xD
		STRIPPED;

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

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(state.getValue(PROP)==IIBlockTypesRubberLog.REBBUR)
		{
			ItemStack heldItem = player.getHeldItem(hand);
			if(heldItem.getItem().getToolClasses(heldItem).contains("axe"))
			{
				for(EnumFacing horizontal : EnumFacing.HORIZONTALS)
				{
					Vec3d vpos = new Vec3d(pos).addVector(0.5,0.65,0.5).add(new Vec3d(horizontal.getDirectionVec()).scale(0.5));
					for(int i = 0; i < 10; i++)
					{
						Vec3d vv = new Vec3d(horizontal.getDirectionVec()).addVector(0,-2,0).scale(Utils.RAND.nextDouble()*0.25);
						Vec3d voff = vpos.add(new Vec3d(horizontal.rotateY().getDirectionVec()).scale(i/10d));
						// new Vec3d(Utils.RAND.nextDouble(), 0.5, Utils.RAND.nextDouble()).addVector(0.5, 0, 0.5).scale(2)
						world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, voff.x, voff.y, voff.z, vv.x, vv.y, vv.z, getStateId(state));
					}
				}
				world.setBlockState(pos, state.withProperty(PROP, IIBlockTypesRubberLog.STRIPPED));
			}
		}
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return state.getValue(PROP)==IIBlockTypesRubberLog.STRIPPED?new AxisAlignedBB(0.0625,0,0.0625,0.9375,1, 0.9375):FULL_BLOCK_AABB;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.SOLID;
	}
}