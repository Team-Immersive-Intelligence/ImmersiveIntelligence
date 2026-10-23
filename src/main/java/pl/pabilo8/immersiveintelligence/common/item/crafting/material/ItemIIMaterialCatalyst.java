package pl.pabilo8.immersiveintelligence.common.item.crafting.material;

import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialCatalyst.MaterialsCatalyst;
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
@IBatchOredictRegister(oreDict = "catalyst")
@IIItemProperties(category = IICategory.RESOURCES)
public class ItemIIMaterialCatalyst extends ItemIISubItemsBase<MaterialsCatalyst>
{
	public ItemIIMaterialCatalyst()
	{
		super("material_catalyst", 64, MaterialsCatalyst.values());
	}

	@GeneratedItemModels(itemName = "material_catalyst", texturePath = "material/catalyst")
	public enum MaterialsCatalyst implements IIItemEnum
	{
		COPPER,
		IRON,
		NICKEL,
		PLATINUM
	}
}
