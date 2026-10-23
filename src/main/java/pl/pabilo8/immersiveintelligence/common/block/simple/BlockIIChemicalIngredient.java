package pl.pabilo8.immersiveintelligence.common.block.simple;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIChemicalIngredient.ChemicalIngredientTypes;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;

/**
 * Decorative industrial glowstone block.
 */
public class BlockIIChemicalIngredient extends BlockIIBase<ChemicalIngredientTypes>
{
	public BlockIIChemicalIngredient()
	{
		super("chemical_ingredient", PropertyEnum.create("type", ChemicalIngredientTypes.class), Material.GLASS, ItemBlockIIBase::new);
		setHardness(0.3F);
		setResistance(1.5F);
		setLightLevel(1.0F);
		setFullCube(true);
		setCategory(IICategory.RESOURCES);
	}

	public enum ChemicalIngredientTypes implements IIBlockEnum
	{
		INDUSTRIAL_GLOWSTONE;
	}
}
