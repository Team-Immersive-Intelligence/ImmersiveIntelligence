package pl.pabilo8.immersiveintelligence.common.item.crafting.material;

import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialIngot.MaterialsIngot;
import pl.pabilo8.immersiveintelligence.common.util.IBatchOredictRegister;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

/**
 * @author Pabilo8
 * @since 2019-05-11
 */
@IBatchOredictRegister(oreDict = "ingot")
public class ItemIIMaterialIngot extends ItemIISubItemsBase<MaterialsIngot>
{
	public ItemIIMaterialIngot()
	{
		super("material_ingot", 64, MaterialsIngot.values());
	}

	public enum MaterialsIngot implements IIItemEnum
	{
		ADVANCED_ELECTRONIC_ALLOY,
		BRASS,
		PLATINUM,
		TUNGSTEN,
		ZINC,
		MAGNET,
		SILICON,
		DURALUMINIUM
	}
}
