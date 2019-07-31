package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
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
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDevice;

import java.util.Arrays;

/**
 * Created by Pabilo8 on 2019-05-17.
 */
public class BlockIIMetalDevice extends BlockIITileProvider<IIBlockTypes_MetalDevice>
{
	public BlockIIMetalDevice()
	{
		super("metal_device", Material.IRON, PropertyEnum.create("type", IIBlockTypes_MetalDevice.class), ItemBlockIEBase.class, IEProperties.FACING_HORIZONTAL, IEProperties.MULTIBLOCKSLAVE, IEProperties.BOOLEANS[0], IEProperties.BOOLEANS[1], IOBJModelCallback.PROPERTY);
		setHardness(3.0F);
		setResistance(15.0F);
		lightOpacity = 0;
		this.setAllNotNormalBlock();

		tesrMap.put(IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta(), IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getName());
		tesrMap.put(IIBlockTypes_MetalDevice.TIMED_BUFFER.getMeta(), IIBlockTypes_MetalDevice.TIMED_BUFFER.getName());
		tesrMap.put(IIBlockTypes_MetalDevice.REDSTONE_BUFFER.getMeta(), IIBlockTypes_MetalDevice.REDSTONE_BUFFER.getName());
	}

	@Override
	public boolean useCustomStateMapper()
	{
		return true;
	}

	@Override
	public String getCustomStateMapping(int meta, boolean itemBlock)
	{
		return null;
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
			case DATA_DEBUGGER:
			{
				return new TileEntityDataDebugger();
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
		if(getMetaFromState(state)==IIBlockTypes_MetalDevice.METAL_CRATE.getMeta())
		{
			return EnumBlockRenderType.MODEL;
		}
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return getMetaFromState(state)!=IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta();
	}
}