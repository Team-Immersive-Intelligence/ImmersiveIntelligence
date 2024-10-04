package pl.pabilo8.immersiveintelligence.common.block.mines;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIITripwireConnector.IIBlockTypesTripWireConnector;
import pl.pabilo8.immersiveintelligence.common.block.mines.tileentity.TileEntityTripwireConnector;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.EnumTileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileProviderEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 05.02.2021
 */
public class BlockIITripwireConnector extends BlockIITileProvider<IIBlockTypesTripWireConnector>
{
	public enum IIBlockTypesTripWireConnector implements IITileProviderEnum
	{
		@EnumTileProvider(tile = TileEntityTripwireConnector.class)
		MAIN
	}

	public BlockIITripwireConnector()
	{
		super("tripwire_connector", Material.WOOD, PropertyEnum.create("type", IIBlockTypesTripWireConnector.class), ItemBlockIIBase::new,
				IOBJModelCallback.PROPERTY, IEProperties.BOOLEANS[0], IEProperties.BOOLEANS[1], IEProperties.CONNECTIONS);
		setLightOpacity(0);
		setCategory(IICategory.WARFARE);
	}

	@Nullable
	@Override
	public String getMappingsExtension(int meta, boolean itemBlock)
	{
		return null;
	}
}
