package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.BlockIIWoodenMultiblock.IIBlockTypes_WoodenMultiblock;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSawmill;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSkyCartStation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSkyCratePost;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySawmill;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCartStation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCratePost;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.util.IILib;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.EnumMultiblockProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileMultiblockEnum;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockConnectable;

import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 2019-06-05
 */
public class BlockIIWoodenMultiblock extends BlockIIMultiblock<IIBlockTypes_WoodenMultiblock>
{
	public BlockIIWoodenMultiblock()
	{
		super("wooden_multiblock", Material.WOOD, PropertyEnum.create("type", IIBlockTypes_WoodenMultiblock.class),
				IEProperties.FACING_HORIZONTAL, IEProperties.BOOLEANS[0], IEProperties.BOOLEANS[1], IEProperties.CONNECTIONS, IEProperties.MULTIBLOCKSLAVE, IOBJModelCallback.PROPERTY);
		setHardness(3.0F);
		setResistance(15.0F);

		addToTESRMap(IIBlockTypes_WoodenMultiblock.values());
	}

	public enum IIBlockTypes_WoodenMultiblock implements IITileMultiblockEnum
	{
		@EnumMultiblockProvider(tile = TileEntitySkyCratePost.class, multiblock = MultiblockSkyCratePost.class)
		SKYCRATE_POST,
		@EnumMultiblockProvider(tile = TileEntitySkyCrateStation.class, multiblock = MultiblockSkyCrateStation.class)
		SKYCRATE_STATION,
		@EnumMultiblockProvider(tile = TileEntitySawmill.class, multiblock = MultiblockSawmill.class)
		SAWMILL,
		@EnumMultiblockProvider(tile = TileEntitySkyCartStation.class, multiblock = MultiblockSkyCartStation.class)
		SKYCART_STATION
	}
}
