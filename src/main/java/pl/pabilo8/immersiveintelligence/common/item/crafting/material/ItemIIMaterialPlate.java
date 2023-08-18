package pl.pabilo8.immersiveintelligence.common.item.crafting.material;

import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialPlate.MaterialsPlate;
import pl.pabilo8.immersiveintelligence.common.util.IBatchOredictRegister;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

/**
 * @author Pabilo8
 * @since 2019-05-11
 */
@IBatchOredictRegister(oreDict = "plate")
public class ItemIIMaterialPlate extends ItemIISubItemsBase<MaterialsPlate>
{
	public ItemIIMaterialPlate()
	{
		super("material_plate", 64, MaterialsPlate.values());
	}

	@GeneratedItemModels(itemName = "material_plate", texturePath = "material/plate")
	public enum MaterialsPlate implements IIItemEnum
	{
		ADVANCED_ELECTRONIC_ALLOY,
		BRASS,
		PLATINUM,
		TUNGSTEN,
		ZINC,
		SILICON,
		RUBBER_RAW,
		DURALUMINIUM
	}
}
