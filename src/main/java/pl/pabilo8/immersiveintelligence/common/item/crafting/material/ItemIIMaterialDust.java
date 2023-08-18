package pl.pabilo8.immersiveintelligence.common.item.crafting.material;

import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialDust.MaterialsDust;
import pl.pabilo8.immersiveintelligence.common.util.IBatchOredictRegister;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

/**
 * @author Pabilo8
 * @since 2019-05-11
 */
@IBatchOredictRegister(oreDict = "dust")
public class ItemIIMaterialDust extends ItemIISubItemsBase<MaterialsDust>
{
	public ItemIIMaterialDust()
	{
		super("material_dust", 64, MaterialsDust.values());
	}

	@GeneratedItemModels(itemName = "material_dust", texturePath = "material/dust")
	public enum MaterialsDust implements IIItemEnum
	{
		ADVANCED_ELECTRONIC_ALLOY,
		BRASS,
		PLATINUM,
		TUNGSTEN,
		ZINC,
		SILICON,
		FLUORITE,
		QUARTZ,
		QUARTZ_DIRTY,
		PHOSPHORUS,
		DURALUMINIUM
	}
}
