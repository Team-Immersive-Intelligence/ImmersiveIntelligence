package pl.pabilo8.immersiveintelligence.common.item.crafting.material;

import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialNugget.MaterialsNugget;
import pl.pabilo8.immersiveintelligence.common.util.IBatchOredictRegister;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

/**
 * @author Pabilo8
 * @since 2019-05-11
 */
@IBatchOredictRegister(oreDict = "nugget")
public class ItemIIMaterialNugget extends ItemIISubItemsBase<MaterialsNugget>
{
	public ItemIIMaterialNugget()
	{
		super("material_nugget", 64, MaterialsNugget.values());
	}

	@GeneratedItemModels(itemName = "material_nugget", texturePath = "material/nugget")
	public enum MaterialsNugget implements IIItemEnum
	{
		ADVANCED_ELECTRONIC_ALLOY,
		BRASS,
		PLATINUM,
		TUNGSTEN,
		ZINC,
		SILICON,
		DURALUMINIUM
	}
}
