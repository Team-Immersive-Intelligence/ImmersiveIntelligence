package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDevice;

import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class BlockIIMetalDevice extends BlockIITileProvider<IIBlockTypes_MetalDevice>
{
	public BlockIIMetalDevice()
	{
		super("metal_device", Material.IRON, PropertyEnum.create("type", IIBlockTypes_MetalDevice.class), ItemBlockIEBase.class,
				IEProperties.FACING_HORIZONTAL, IEProperties.BOOLEANS[0], IEProperties.BOOLEANS[1],
				IEProperties.DYNAMICRENDER, IOBJModelCallback.PROPERTY, Properties.AnimationProperty
		);
		setHardness(3.0F);
		setResistance(15.0F);
		lightOpacity = 0;
		this.setAllNotNormalBlock();

		tesrMap.put(IIBlockTypes_MetalDevice.TIMED_BUFFER.getMeta(), IIBlockTypes_MetalDevice.TIMED_BUFFER.getName());
		tesrMap.put(IIBlockTypes_MetalDevice.REDSTONE_BUFFER.getMeta(), IIBlockTypes_MetalDevice.REDSTONE_BUFFER.getName());
		tesrMap.put(IIBlockTypes_MetalDevice.SMALL_DATA_BUFFER.getMeta(), IIBlockTypes_MetalDevice.SMALL_DATA_BUFFER.getName());
		tesrMap.put(IIBlockTypes_MetalDevice.DATA_MERGER.getMeta(), IIBlockTypes_MetalDevice.DATA_MERGER.getName());
	}

	@Override
	public boolean useCustomStateMapper()
	{
		return true;
	}

	@Override
	public String getCustomStateMapping(int meta, boolean itemBlock)
	{
		switch(IIBlockTypes_MetalDevice.values()[meta])
		{
			default:
				return null;

			case AMMUNITION_CRATE:
			case MEDIC_CRATE:
			case REPAIR_CRATE:
				return IIBlockTypes_MetalDevice.values()[meta].getName();
		}
	}

	@Override
	public TileEntity createBasicTE(World world, IIBlockTypes_MetalDevice type)
	{
		switch(type)
		{
			case METAL_CRATE:
			{
				return new TileEntityMetalCrate();
			}
			case AMMUNITION_CRATE:
			{
				return new TileEntityAmmunitionCrate();
			}

			case TIMED_BUFFER:
			{
				return new TileEntityTimedBuffer();
			}
			case REDSTONE_BUFFER:
			{
				return new TileEntityRedstoneBuffer();
			}
			case PUNCHTAPE_READER:
			{
				return new TileEntityPunchtapeReader();
			}
			case SMALL_DATA_BUFFER:
			{
				return new TileEntitySmallDataBuffer();
			}
			case DATA_MERGER:
			{
				return new TileEntityDataMerger();
			}
			case DATA_ROUTER:
			{
				return new TileEntityDataRouter();
			}
			case MEDIC_CRATE:
			{
				return new TileEntityMedicalCrate();
			}
			case REPAIR_CRATE:
			{
				return new TileEntityRepairCrate();
			}
			case LATEX_COLLECTOR:
			{
				return new TileEntityLatexCollector();
			}
		}
		return null;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		BlockStateContainer base = super.createBlockState();
		IUnlistedProperty[] unlisted = (base instanceof ExtendedBlockState)?((ExtendedBlockState)base).getUnlistedProperties().toArray(new IUnlistedProperty[0]): new IUnlistedProperty[0];
		unlisted = Arrays.copyOf(unlisted, unlisted.length+1);
		unlisted[unlisted.length-1] = IEProperties.CONNECTIONS;
		return new ExtendedBlockState(this, base.getProperties().toArray(new IProperty[0]), unlisted);
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getExtendedState(state, world, pos);
		if(state instanceof IExtendedBlockState)
		{
			IExtendedBlockState ext = (IExtendedBlockState)state;
			TileEntity te = world.getTileEntity(pos);
			if(!(te instanceof TileEntityImmersiveConnectable))
				return state;
			state = ext.withProperty(IEProperties.CONNECTIONS, ((TileEntityImmersiveConnectable)te).genConnBlockstate());
		}
		return state;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getActualState(state, world, pos);
		return state;
	}

	@Override
	public boolean allowHammerHarvest(IBlockState state)
	{
		return true;
	}

	@Override
	public boolean canIEBlockBePlaced(World world, BlockPos pos, IBlockState newState, EnumFacing side, float hitX, float hitY, float hitZ, EntityPlayer player, ItemStack stack)
	{
		return true;
	}

	@Deprecated
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		switch(state.getValue(property))
		{
			case METAL_CRATE:
			case DATA_ROUTER:
			case LATEX_COLLECTOR:
			case PUNCHTAPE_READER:
			case AMMUNITION_CRATE:
			case MEDIC_CRATE:
			case REPAIR_CRATE:
				return EnumBlockRenderType.MODEL;
			default:
				return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
		}

	}

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return getMetaFromState(state)!=IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta();
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, world, pos, blockIn, fromPos);
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityLatexCollector)
		{
			TileEntityLatexCollector connector = (TileEntityLatexCollector)te;
			if(world.isAirBlock(pos.offset((connector.facing))))
			{
				this.dropBlockAsItem(connector.getWorld(), pos, world.getBlockState(pos), 0);
				connector.getWorld().setBlockToAir(pos);
			}
		}
	}
}