package pl.pabilo8.immersiveintelligence.common.item.crafting.material;

import net.minecraftforge.fml.common.Loader;
import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialRod.MaterialsRod;
import pl.pabilo8.immersiveintelligence.common.util.IBatchOredictRegister;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

/**
 * @author Pabilo8
 * @since 2019-05-11
 */
@IBatchOredictRegister(oreDict = "stick")
public class ItemIIMaterialRod extends ItemIISubItemsBase<MaterialsRod>
{
	public ItemIIMaterialRod()
	{
		super("material_rod", 64, MaterialsRod.values());

		if(Loader.isModLoaded("immersiveposts"))
			setMetaUnhidden(MaterialsRod.ZINC, MaterialsRod.PLATINUM);
	}

	@GeneratedItemModels(itemName = "material_rod", texturePath = "material/rod")
	public enum MaterialsRod implements IIItemEnum
	{
		BRASS,
		TUNGSTEN,
		ZINC,
		PLATINUM,
		DURALUMINIUM
	}
}
