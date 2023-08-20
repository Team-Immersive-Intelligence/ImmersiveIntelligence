package pl.pabilo8.immersiveintelligence.common.item.crafting.material;

import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialBoule.MaterialsBoule;
import pl.pabilo8.immersiveintelligence.common.util.IBatchOredictRegister;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

/**
 * @author Pabilo8
 * @since 2019-05-11
 */
@IBatchOredictRegister(oreDict = "boule")
public class ItemIIMaterialBoule extends ItemIISubItemsBase<MaterialsBoule>
{
	public ItemIIMaterialBoule()
	{
		super("material_boule", 64, MaterialsBoule.values());
	}

	@GeneratedItemModels(itemName = "material_boule", texturePath = "material/dust")
	public enum MaterialsBoule implements IIItemEnum
	{
		SILICON
	}
}
