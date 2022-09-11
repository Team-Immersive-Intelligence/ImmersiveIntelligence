package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.IIBlockTypes_MetalMultiblock0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.*;
import pl.pabilo8.immersiveintelligence.common.util.IILib;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.EnumMultiblockProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileMultiblockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.TernaryValue;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockConnectable;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 20-06-2019
 */
public class BlockIIMetalMultiblock0 extends BlockIIMultiblock<IIBlockTypes_MetalMultiblock0>
{
	public BlockIIMetalMultiblock0()
	{
		super("metal_multiblock", Material.IRON, PropertyEnum.create("type", IIBlockTypes_MetalMultiblock0.class),
				IEProperties.FACING_HORIZONTAL,
				IEProperties.BOOLEANS[0], IEProperties.BOOLEANS[1], IEProperties.MULTIBLOCKSLAVE, IEProperties.DYNAMICRENDER,
				IOBJModelCallback.PROPERTY, Properties.AnimationProperty);
		setHardness(3.0F);
		setResistance(15.0F);
		setLightOpacity(0);
		setFullCube(false);
		setBlockLayer(BlockRenderLayer.CUTOUT);
		setToolTypes(IILib.TOOL_HAMMER);

		addToTESRMap(
				IIBlockTypes_MetalMultiblock0.PRINTING_PRESS, IIBlockTypes_MetalMultiblock0.RADIO_STATION,
				IIBlockTypes_MetalMultiblock0.AMMUNITION_FACTORY, IIBlockTypes_MetalMultiblock0.ELECTROLYZER,
				IIBlockTypes_MetalMultiblock0.CHEMICAL_BATH, IIBlockTypes_MetalMultiblock0.PRECISSION_ASSEMBLER,
				IIBlockTypes_MetalMultiblock0.PACKER
		);
	}

	public enum IIBlockTypes_MetalMultiblock0 implements IITileMultiblockEnum
	{
		@EnumMultiblockProvider(tile = TileEntityRadioStation.class, multiblock = MultiblockRadioStation.class)
		RADIO_STATION,
		@EnumMultiblockProvider(tile = TileEntityPrintingPress.class, multiblock = MultiblockPrintingPress.class)
		PRINTING_PRESS,
		@EnumMultiblockProvider(tile = TileEntityDataInputMachine.class, multiblock = MultiblockDataInputMachine.class)
		DATA_INPUT_MACHINE,
		@EnumMultiblockProvider(tile = TileEntityArithmeticLogicMachine.class, multiblock = MultiblockArithmeticLogicMachine.class)
		ARITHMETIC_LOGIC_MACHINE,
		@EnumMultiblockProvider(tile = TileEntityChemicalBath.class, multiblock = MultiblockChemicalBath.class)
		@IIBlockProperties(needsCustomState = true)
		CHEMICAL_BATH,
		@EnumMultiblockProvider(tile = TileEntityElectrolyzer.class, multiblock = MultiblockElectrolyzer.class)
		@IIBlockProperties(needsCustomState = true)
		ELECTROLYZER,
		@EnumMultiblockProvider(tile = TileEntityPrecissionAssembler.class, multiblock = MultiblockPrecissionAssembler.class)
		@IIBlockProperties(needsCustomState = true)
		PRECISSION_ASSEMBLER,
		@EnumMultiblockProvider(tile = TileEntityBallisticComputer.class, multiblock = MultiblockBallisticComputer.class)
		@IIBlockProperties(needsCustomState = true)
		BALLISTIC_COMPUTER,
		@EnumMultiblockProvider(tile = TileEntityArtilleryHowitzer.class, multiblock = MultiblockArtilleryHowitzer.class)
		@IIBlockProperties(needsCustomState = true)
		ARTILLERY_HOWITZER,

		PERISCOPE, //not implemented

		@EnumMultiblockProvider(tile = TileEntityConveyorScanner.class, multiblock = MultiblockConveyorScanner.class)
		CONVEYOR_SCANNER,

		@EnumMultiblockProvider(tile = TileEntityPacker.class, multiblock = MultiblockPacker.class)
		AMMUNITION_FACTORY, //deprecated
		@IIBlockProperties(hidden = TernaryValue.TRUE)
		@EnumMultiblockProvider(tile = TileEntityPacker.class, multiblock = MultiblockPacker.class)
		PACKER_OLD, //deprecated

		@EnumMultiblockProvider(tile = TileEntityPacker.class, multiblock = MultiblockPacker.class)
		@IIBlockProperties(needsCustomState = true)
		PACKER,

		RAILWAY_PACKER //not implemented
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(@Nonnull IBlockState state)
	{
		switch(state.getValue(property))
		{
			case BALLISTIC_COMPUTER:
			case ARTILLERY_HOWITZER:
				return EnumBlockRenderType.MODEL;
			default:
				return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
		}
	}
}
