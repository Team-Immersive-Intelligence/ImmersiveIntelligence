package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.common.property.Properties;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.EnumMultiblockProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileMultiblockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.TernaryValue;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 20.06.2019
 */
public class BlockIIMetalMultiblock1 extends BlockIIMultiblock<MetalMultiblocks1>
{
	public BlockIIMetalMultiblock1()
	{
		super("metal_multiblock1", Material.IRON, PropertyEnum.create("type", MetalMultiblocks1.class),
				IEProperties.FACING_HORIZONTAL,
				IEProperties.BOOLEANS[0], IEProperties.BOOLEANS[1], IEProperties.CONNECTIONS, IEProperties.MULTIBLOCKSLAVE,
				Properties.AnimationProperty, IEProperties.DYNAMICRENDER, IOBJModelCallback.PROPERTY
		);
		setHardness(3.0F);
		setResistance(15.0F);

		addToTESRMap(MetalMultiblocks1.FUEL_STATION, MetalMultiblocks1.VEHICLE_WORKSHOP, MetalMultiblocks1.VULCANIZER);

		setSubBlockLayer(MetalMultiblocks1.COAGULATOR, BlockRenderLayer.CUTOUT);
		setSubBlockLayer(MetalMultiblocks1.FILLER, BlockRenderLayer.CUTOUT);
		setSubBlockLayer(MetalMultiblocks1.RADAR, BlockRenderLayer.CUTOUT);
		setSubBlockLayer(MetalMultiblocks1.FLAGPOLE, BlockRenderLayer.CUTOUT);
		setSubBlockLayer(MetalMultiblocks1.EMPLACEMENT, BlockRenderLayer.CUTOUT);
		setSubBlockLayer(MetalMultiblocks1.CHEMICAL_PAINTER, BlockRenderLayer.CUTOUT);
		setSubBlockLayer(MetalMultiblocks1.PROJECTILE_WORKSHOP, BlockRenderLayer.CUTOUT);
		setSubBlockLayer(MetalMultiblocks1.AMMUNITION_ASSEMBLER, BlockRenderLayer.CUTOUT);
		setSubBlockLayer(MetalMultiblocks1.HEAVY_AMMUNITION_ASSEMBLER, BlockRenderLayer.CUTOUT);
	}

	@Deprecated
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		switch(state.getValue(property))
		{
			case FUEL_STATION:
			case VEHICLE_WORKSHOP:
			case STRATEGIC_COMMAND_TABLE:
			case VULCANIZER:
				return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
			default:
				return EnumBlockRenderType.MODEL;
		}
	}

	public enum MetalMultiblocks1 implements IITileMultiblockEnum
	{
		@IIBlockProperties(needsCustomState = true)
		@EnumMultiblockProvider(multiblock = MultiblockRedstoneInterface.class, tile = TileEntityRedstoneDataInterface.class)
		REDSTONE_DATA_INTERFACE,

		@IIBlockProperties(needsCustomState = true)
		@EnumMultiblockProvider(multiblock = MultiblockFiller.class, tile = TileEntityFiller.class)
		FILLER,

		@IIBlockProperties(needsCustomState = true)
		@EnumMultiblockProvider(multiblock = MultiblockCoagulator.class, tile = TileEntityCoagulator.class)
		COAGULATOR,

		@IIBlockProperties(needsCustomState = true)
		@EnumMultiblockProvider(multiblock = MultiblockProjectileWorkshop.class, tile = TileEntityProjectileWorkshop.class)
		PROJECTILE_WORKSHOP,
		@IIBlockProperties(needsCustomState = true)
		@EnumMultiblockProvider(multiblock = MultiblockAmmunitionAssembler.class, tile = TileEntityAmmunitionAssembler.class)
		AMMUNITION_ASSEMBLER,

		@IIBlockProperties(needsCustomState = true)
		@EnumMultiblockProvider(multiblock = MultiblockFuelStation.class, tile = TileEntityFuelStation.class)
		FUEL_STATION,

		@IIBlockProperties(hidden = TernaryValue.TRUE)
		@EnumMultiblockProvider(multiblock = MultiblockVehicleWorkshop.class, tile = TileEntityVehicleWorkshop.class)
		VEHICLE_WORKSHOP,
		@IIBlockProperties(needsCustomState = true)
		@EnumMultiblockProvider(multiblock = MultiblockFlagpole.class, tile = TileEntityFlagpole.class)
		FLAGPOLE,
		@IIBlockProperties(needsCustomState = true)
		@EnumMultiblockProvider(multiblock = MultiblockRadar.class, tile = TileEntityRadar.class)
		RADAR,
		@IIBlockProperties(needsCustomState = true)
		@EnumMultiblockProvider(multiblock = MultiblockEmplacement.class, tile = TileEntityEmplacement.class)
		EMPLACEMENT,
		@IIBlockProperties(hidden = TernaryValue.TRUE)
		STRATEGIC_COMMAND_TABLE, //not implemented

		@IIBlockProperties(needsCustomState = true)
		@EnumMultiblockProvider(multiblock = MultiblockChemicalPainter.class, tile = TileEntityChemicalPainter.class)
		CHEMICAL_PAINTER,
		@EnumMultiblockProvider(multiblock = MultiblockVulcanizer.class, tile = TileEntityVulcanizer.class)
		VULCANIZER,

		@IIBlockProperties(needsCustomState = true)
		@EnumMultiblockProvider(multiblock = MultiblockHeavyAmmunitionAssembler.class, tile = TileEntityHeavyAmmunitionAssembler.class)
		HEAVY_AMMUNITION_ASSEMBLER,
	}
}
