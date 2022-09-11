package pl.pabilo8.immersiveintelligence.common.block.fortification;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import pl.pabilo8.immersiveintelligence.common.block.fortification.BlockIIWoodenChainFence.WoodenFortifications;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileProviderEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;

/**
 * @author Pabilo8
 * @since 29.08.2020
 */
public class BlockIIWoodenChainFence extends BlockIIFenceBase<WoodenFortifications>
{
	public BlockIIWoodenChainFence()
	{
		super("wooden_fortification", Material.WOOD, PropertyEnum.create("type", WoodenFortifications.class), ItemBlockIIBase::new);
		setHardness(3.0F);
		setResistance(15.0F);
	}

	public enum WoodenFortifications implements IITileProviderEnum
	{
		@IIBlockProperties(needsCustomState = true)
		WOODEN_STEEL_CHAIN_FENCE,
		@IIBlockProperties(needsCustomState = true)
		WOODEN_BRASS_CHAIN_FENCE
	}
}
