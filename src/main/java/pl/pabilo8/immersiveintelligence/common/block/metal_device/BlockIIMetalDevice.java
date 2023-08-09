package pl.pabilo8.immersiveintelligence.common.block.metal_device;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.*;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityLatexCollector;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityMetalCrate;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityMedicalCrate;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityRepairCrate;
import pl.pabilo8.immersiveintelligence.common.util.IILib;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.EnumTileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileProviderEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class BlockIIMetalDevice extends BlockIITileProvider<IIBlockTypes_MetalDevice>
{
	public BlockIIMetalDevice()
	{
		super("metal_device", Material.IRON, PropertyEnum.create("type", IIBlockTypes_MetalDevice.class), ItemBlockIIBase::new,
				IEProperties.FACING_HORIZONTAL, IEProperties.BOOLEANS[0], IEProperties.BOOLEANS[1],
				IEProperties.CONNECTIONS, IEProperties.DYNAMICRENDER, IOBJModelCallback.PROPERTY, Properties.AnimationProperty
		);

		setHardness(3.0F);
		setResistance(15.0F);

		setToolTypes(IILib.TOOL_HAMMER);

		setBlockLayer(BlockRenderLayer.CUTOUT_MIPPED);
		setSubBlockLayer(IIBlockTypes_MetalDevice.METAL_CRATE, BlockRenderLayer.CUTOUT);

		addToTESRMap(IIBlockTypes_MetalDevice.TIMED_BUFFER, IIBlockTypes_MetalDevice.REDSTONE_BUFFER,
				IIBlockTypes_MetalDevice.SMALL_DATA_BUFFER, IIBlockTypes_MetalDevice.DATA_MERGER);
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

	@SuppressWarnings("deprecation")
	@Nonnull
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

	public enum IIBlockTypes_MetalDevice implements IITileProviderEnum
	{
		@EnumTileProvider(tile = TileEntityMetalCrate.class)
		METAL_CRATE,
		@EnumTileProvider(tile = TileEntityAmmunitionCrate.class)
		AMMUNITION_CRATE,
		@EnumTileProvider(tile = TileEntitySmallDataBuffer.class)
		SMALL_DATA_BUFFER,
		@EnumTileProvider(tile = TileEntityTimedBuffer.class)
		TIMED_BUFFER,
		@EnumTileProvider(tile = TileEntityRedstoneBuffer.class)
		REDSTONE_BUFFER,
		@EnumTileProvider(tile = TileEntityPunchtapeReader.class)
		PUNCHTAPE_READER,
		@EnumTileProvider(tile = TileEntityDataRouter.class)
		DATA_ROUTER,
		@EnumTileProvider(tile = TileEntityDataMerger.class)
		DATA_MERGER,
		@EnumTileProvider(tile = TileEntityMedicalCrate.class)
		MEDIC_CRATE,
		@EnumTileProvider(tile = TileEntityRepairCrate.class)
		REPAIR_CRATE,
		@EnumTileProvider(tile = TileEntityLatexCollector.class)
		LATEX_COLLECTOR
	}


}