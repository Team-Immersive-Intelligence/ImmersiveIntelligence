package pl.pabilo8.immersiveintelligence.common.block.rotary_device;

import blusunrize.immersiveengineering.api.IEProperties;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.BlockRenderLayer;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityGearbox;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIGearbox.IIBlockTypes_Gearbox;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.EnumTileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileProviderEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class BlockIIGearbox extends BlockIITileProvider<IIBlockTypes_Gearbox>
{
	public enum IIBlockTypes_Gearbox implements IITileProviderEnum
	{
		@EnumTileProvider(tile = TileEntityGearbox.class)
		WOODEN_GEARBOX
	}

	public BlockIIGearbox()
	{
		super("gearbox", Material.IRON, PropertyEnum.create("type", IIBlockTypes_Gearbox.class), ItemBlockIIBase::new,
				IEProperties.MULTIBLOCKSLAVE, IEProperties.SIDECONFIG[0], IEProperties.SIDECONFIG[1], IEProperties.SIDECONFIG[2],
				IEProperties.SIDECONFIG[3], IEProperties.SIDECONFIG[4], IEProperties.SIDECONFIG[5]);
		setHardness(3.0F);
		setResistance(15.0F);
		setLightOpacity(0);
		setBlockLayer(BlockRenderLayer.CUTOUT);
		setFullCube(true);
		setToolTypes(IIReference.TOOL_HAMMER);
	}
}