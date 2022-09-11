package pl.pabilo8.immersiveintelligence.common.block.fortification;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import pl.pabilo8.immersiveintelligence.common.block.fortification.BlockIIMetalChainFence.MetalFortifications;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileProviderEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;

/**
 * @author Pabilo8
 * @since 29.08.2020
 */
public class BlockIIMetalChainFence extends BlockIIFenceBase<MetalFortifications>
{
	public BlockIIMetalChainFence()
	{
		super("metal_fortification", Material.IRON, PropertyEnum.create("type", MetalFortifications.class), ItemBlockIIBase::new);
		setHardness(3.0F);
		setResistance(15.0F);
	}

	public enum MetalFortifications implements IITileProviderEnum
	{
		@IIBlockProperties(needsCustomState = true)
		STEEL_CHAIN_FENCE,
		@IIBlockProperties(needsCustomState = true)
		BRASS_CHAIN_FENCE,
		@IIBlockProperties(needsCustomState = true)
		ALUMINIUM_CHAIN_FENCE
	}
}
