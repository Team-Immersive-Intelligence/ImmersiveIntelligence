package pl.pabilo8.immersiveintelligence.common.item.crafting.material;

import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialGem.MaterialsGem;
import pl.pabilo8.immersiveintelligence.common.util.IBatchOredictRegister;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

/**
 * @author Pabilo8
 * @since 2019-05-11
 */
@IBatchOredictRegister(oreDict = "gem")
public class ItemIIMaterialGem extends ItemIISubItemsBase<MaterialsGem>
{
	public ItemIIMaterialGem()
	{
		super("material_gem", 64, MaterialsGem.values());
	}

	@GeneratedItemModels(itemName = "material_gem", texturePath = "material/gem")
	public enum MaterialsGem implements IIItemEnum
	{
		FLUORITE,
		PHOSPHORUS
	}
}
