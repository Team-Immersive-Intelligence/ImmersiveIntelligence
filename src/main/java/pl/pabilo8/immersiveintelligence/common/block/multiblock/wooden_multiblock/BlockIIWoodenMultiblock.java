package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.common.property.Properties;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.BlockIIWoodenMultiblock.WoodenMultiblocks;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSawmill;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSkyCartStation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSkyCratePost;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySawmill;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCartStation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCratePost;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.EnumMultiblockProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileMultiblockEnum;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;

/**
 * @author Pabilo8
 * @since 2019-06-05
 */
public class BlockIIWoodenMultiblock extends BlockIIMultiblock<WoodenMultiblocks>
{
	public BlockIIWoodenMultiblock()
	{
		super("wooden_multiblock", Material.WOOD, PropertyEnum.create("type", WoodenMultiblocks.class),
				IEProperties.FACING_HORIZONTAL, IEProperties.BOOLEANS[0], IEProperties.BOOLEANS[1], IEProperties.CONNECTIONS,
				IEProperties.MULTIBLOCKSLAVE, IEProperties.DYNAMICRENDER, IOBJModelCallback.PROPERTY, Properties.AnimationProperty);
		setHardness(3.0F);
		setResistance(15.0F);

		addToTESRMap(WoodenMultiblocks.SKYCART_STATION, WoodenMultiblocks.SKYCRATE_STATION, WoodenMultiblocks.SKYCRATE_POST);
	}

	public enum WoodenMultiblocks implements IITileMultiblockEnum
	{
		@EnumMultiblockProvider(tile = TileEntitySkyCratePost.class, multiblock = MultiblockSkyCratePost.class)
		SKYCRATE_POST,
		@EnumMultiblockProvider(tile = TileEntitySkyCrateStation.class, multiblock = MultiblockSkyCrateStation.class)
		SKYCRATE_STATION,
		@EnumMultiblockProvider(tile = TileEntitySawmill.class, multiblock = MultiblockSawmill.class)
		@IIBlockProperties(needsCustomState = true, renderLayer = BlockRenderLayer.CUTOUT)
		SAWMILL,
		@EnumMultiblockProvider(tile = TileEntitySkyCartStation.class, multiblock = MultiblockSkyCartStation.class)
		SKYCART_STATION
	}
}
