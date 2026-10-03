package pl.pabilo8.immersiveintelligence.common.block.simple;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;

/**
 * Decorative industrial glowstone block.
 */
public class BlockIIIndustrialGlowstone extends BlockIIBase<BlockIIIndustrialGlowstone.Types>
{
	public BlockIIIndustrialGlowstone()
	{
		super("industrial_glowstone", PropertyEnum.create("type", Types.class), Material.GLASS, ItemBlockIIBase::new);
		setHardness(0.3F);
		setResistance(1.5F);
		setLightLevel(1.0F);
		setFullCube(true);
		setCategory(IICategory.RESOURCES);
	}

	public enum Types implements IIBlockEnum
	{
		INDUSTRIAL;
	}
}
