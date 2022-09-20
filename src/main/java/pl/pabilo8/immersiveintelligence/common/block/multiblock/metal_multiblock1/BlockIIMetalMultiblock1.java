package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockChemicalPainter;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityChemicalPainter;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.*;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.EnumMultiblockProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileMultiblockEnum;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;

/**
 * @author Pabilo8
 * @since 20-06-2019
 */
public class BlockIIMetalMultiblock1 extends BlockIIMultiblock<MetalMultiblocks1>
{
	public BlockIIMetalMultiblock1()
	{
		super("metal_multiblock1", Material.IRON, PropertyEnum.create("type", MetalMultiblocks1.class),
				IEProperties.FACING_HORIZONTAL,
				IEProperties.BOOLEANS[0], IEProperties.BOOLEANS[1], IEProperties.CONNECTIONS, IEProperties.MULTIBLOCKSLAVE, IOBJModelCallback.PROPERTY);
		setHardness(3.0F);
		setResistance(15.0F);

		addToTESRMap(MetalMultiblocks1.EMPLACEMENT, MetalMultiblocks1.FLAGPOLE, MetalMultiblocks1.FUEL_STATION,
				MetalMultiblocks1.VEHICLE_WORKSHOP, MetalMultiblocks1.RADAR, MetalMultiblocks1.FILLER,
				MetalMultiblocks1.VULCANIZER, MetalMultiblocks1.CHEMICAL_PAINTER, MetalMultiblocks1.COAGULATOR,
				MetalMultiblocks1.PROJECTILE_WORKSHOP, MetalMultiblocks1.AMMUNITION_WORKSHOP
		);
	}

	public enum MetalMultiblocks1 implements IITileMultiblockEnum
	{
		@EnumMultiblockProvider(multiblock = MultiblockRedstoneInterface.class, tile = TileEntityRedstoneInterface.class)
		REDSTONE_DATA_INTERFACE,
		@EnumMultiblockProvider(multiblock = MultiblockFiller.class, tile = TileEntityFiller.class)
		FILLER,
		@EnumMultiblockProvider(multiblock = MultiblockCoagulator.class, tile = TileEntityCoagulator.class)
		COAGULATOR,
		@EnumMultiblockProvider(multiblock = MultiblockProjectileWorkshop.class, tile = TileEntityProjectileWorkshop.class)
		PROJECTILE_WORKSHOP,
		@EnumMultiblockProvider(multiblock = MultiblockAmmunitionWorkshop.class, tile = TileEntityAmmunitionWorkshop.class)
		AMMUNITION_WORKSHOP,
		@EnumMultiblockProvider(multiblock = MultiblockFuelStation.class, tile = TileEntityFuelStation.class)
		FUEL_STATION,
		@EnumMultiblockProvider(multiblock = MultiblockVehicleWorkshop.class, tile = TileEntityVehicleWorkshop.class)
		VEHICLE_WORKSHOP,
		@EnumMultiblockProvider(multiblock = MultiblockFlagpole.class, tile = TileEntityFlagpole.class)
		FLAGPOLE,
		@EnumMultiblockProvider(multiblock = MultiblockRadar.class, tile = TileEntityRadar.class)
		RADAR,
		@EnumMultiblockProvider(multiblock = MultiblockEmplacement.class, tile = TileEntityEmplacement.class)
		EMPLACEMENT,
		STRATEGIC_COMMAND_TABLE, //not implemented
		@EnumMultiblockProvider(multiblock = MultiblockChemicalPainter.class, tile = TileEntityChemicalPainter.class)
		CHEMICAL_PAINTER,
		@EnumMultiblockProvider(multiblock = MultiblockVulcanizer.class, tile = TileEntityVulcanizer.class)
		VULCANIZER
	}

	@Deprecated
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		switch(state.getValue(property))
		{
			case REDSTONE_DATA_INTERFACE:
				return EnumBlockRenderType.MODEL;
			default:
				return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
		}
	}
}
