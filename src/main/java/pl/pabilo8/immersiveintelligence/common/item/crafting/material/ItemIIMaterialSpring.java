package pl.pabilo8.immersiveintelligence.common.item.crafting.material;

import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialSpring.MaterialsSpring;
import pl.pabilo8.immersiveintelligence.common.util.IBatchOredictRegister;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

/**
 * @author Pabilo8
 * @since 2019-05-11
 */
@IBatchOredictRegister(oreDict = "spring")
public class ItemIIMaterialSpring extends ItemIISubItemsBase<MaterialsSpring>
{
	public ItemIIMaterialSpring()
	{
		super("material_spring", 64, MaterialsSpring.values());
	}

	@GeneratedItemModels(itemName = "material_spring", texturePath = "material/spring")
	public enum MaterialsSpring implements IIItemEnum
	{
		BRASS,
		IRON,
		STEEL
	}
}
