package pl.pabilo8.immersiveintelligence.common.item.crafting.material;

import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialBoule.MaterialsBoule;
import pl.pabilo8.immersiveintelligence.common.util.IBatchOredictRegister;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 11.05.2019
 */
@IBatchOredictRegister(oreDict = "boule")
@IIItemProperties(category = IICategory.RESOURCES)
public class ItemIIMaterialBoule extends ItemIISubItemsBase<MaterialsBoule>
{
	public ItemIIMaterialBoule()
	{
		super("material_boule", 64, MaterialsBoule.values());
	}

	@GeneratedItemModels(itemName = "material_boule", texturePath = "material/boule")
	public enum MaterialsBoule implements IIItemEnum
	{
		SILICON
	}
}
