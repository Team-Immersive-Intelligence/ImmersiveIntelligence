package pl.pabilo8.immersiveintelligence.common.block.rotary_device;

import blusunrize.immersiveengineering.api.IEProperties;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.BlockRenderLayer;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIMechanicalDevice.IIBlockTypes_MechanicalDevice;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityTransmissionBox;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityTransmissionBoxCreative;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.EnumTileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileProviderEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class BlockIIMechanicalDevice extends BlockIITileProvider<IIBlockTypes_MechanicalDevice>
{
	public enum IIBlockTypes_MechanicalDevice implements IITileProviderEnum
	{
		@EnumTileProvider(tile = TileEntityTransmissionBox.class)
		WOODEN_TRANSMISSION_BOX,
		@EnumTileProvider(tile = TileEntityTransmissionBoxCreative.class)
		CREATIVE_TRANSMISSION_BOX
	}

	public BlockIIMechanicalDevice()
	{
		super("mechanical_device", Material.WOOD, PropertyEnum.create("type", IIBlockTypes_MechanicalDevice.class), ItemBlockIIBase::new,
				IEProperties.FACING_ALL);
		setHardness(3.0F);
		setResistance(15.0F);
		setFullCube(true);
		setBlockLayer(BlockRenderLayer.CUTOUT_MIPPED);
		setToolTypes(IIReference.TOOL_HAMMER);
	}
}