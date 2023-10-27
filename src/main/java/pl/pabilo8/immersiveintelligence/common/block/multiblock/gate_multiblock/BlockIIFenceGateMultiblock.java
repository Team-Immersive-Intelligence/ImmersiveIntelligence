package pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock;

import blusunrize.immersiveengineering.api.IEProperties;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.BlockIIFenceGateMultiblock.IIBlockTypes_FenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockAluminiumChainFenceGate.TileEntityAluminiumChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockAluminiumFenceGate.TileEntityAluminiumFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockSteelChainFenceGate.TileEntitySteelChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockSteelFenceGate.TileEntitySteelFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockWoodenChainFenceGate.TileEntityWoodenChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockWoodenFenceGate.TileEntityWoodenFenceGate;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.EnumMultiblockProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileMultiblockEnum;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;

/**
 * @author Pabilo8
 * @since 23.12.2021
 */
public class BlockIIFenceGateMultiblock extends BlockIIMultiblock<IIBlockTypes_FenceGate>
{
	// TODO: 05.09.2022 higher hardness and resistance for metal gates
	public enum IIBlockTypes_FenceGate implements IITileMultiblockEnum
	{
		@EnumMultiblockProvider(multiblock = MultiblockWoodenFenceGate.class, tile = TileEntityWoodenFenceGate.class)
		WOODEN,
		@EnumMultiblockProvider(multiblock = MultiblockWoodenChainFenceGate.class, tile = TileEntityWoodenChainFenceGate.class)
		WOODEN_CHAIN,
		@EnumMultiblockProvider(multiblock = MultiblockSteelFenceGate.class, tile = TileEntitySteelFenceGate.class)
		STEEL,
		@EnumMultiblockProvider(multiblock = MultiblockSteelChainFenceGate.class, tile = TileEntitySteelChainFenceGate.class)
		STEEL_CHAIN,
		@EnumMultiblockProvider(multiblock = MultiblockAluminiumFenceGate.class, tile = TileEntityAluminiumFenceGate.class)
		ALUMINIUM,
		@EnumMultiblockProvider(multiblock = MultiblockAluminiumChainFenceGate.class, tile = TileEntityAluminiumChainFenceGate.class)
		ALUMINIUM_CHAIN
	}

	public BlockIIFenceGateMultiblock()
	{
		super("gate_multiblock", Material.IRON, PropertyEnum.create("type", IIBlockTypes_FenceGate.class),
				IEProperties.FACING_HORIZONTAL, IEProperties.DYNAMICRENDER, IEProperties.BOOLEANS[0]);
		setHardness(3.0F);
		setResistance(15.0F);

		setSubMaterial(IIBlockTypes_FenceGate.WOODEN, Material.WOOD);
		setSubMaterial(IIBlockTypes_FenceGate.WOODEN_CHAIN, Material.WOOD);
	}
}
