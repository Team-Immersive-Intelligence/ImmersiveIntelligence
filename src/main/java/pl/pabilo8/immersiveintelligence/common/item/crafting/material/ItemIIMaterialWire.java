package pl.pabilo8.immersiveintelligence.common.item.crafting.material;

import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialWire.MaterialsWire;
import pl.pabilo8.immersiveintelligence.common.util.IBatchOredictRegister;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

/**
 * @author Pabilo8
 * @since 2019-05-11
 */
@IBatchOredictRegister(oreDict = "wire")
public class ItemIIMaterialWire extends ItemIISubItemsBase<MaterialsWire>
{
	public ItemIIMaterialWire()
	{
		super("material_wire", 64, MaterialsWire.values());
	}

	@GeneratedItemModels(itemName = "material_wire", texturePath = "material/wire")
	public enum MaterialsWire implements IIItemEnum
	{
		TUNGSTEN,
		BRASS
	}
}
