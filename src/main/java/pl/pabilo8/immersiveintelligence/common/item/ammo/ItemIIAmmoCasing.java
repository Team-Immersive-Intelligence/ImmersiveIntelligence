package pl.pabilo8.immersiveintelligence.common.item.ammo;

import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casing;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.GeneratedSubItemModel;

/**
 * @author Pabilo8
 * @since 08.08.2021
 *
 * <p>
 * Bullet System mk.4 changes how ammo items works (again!).
 * Cores and Bullets are one item, as they both use the same nbt tags.
 * Casings are optional items which are used only in crafting and have no impact on the actual bullet.
 * Some items like grenades and revolver bullets use existing items as casings, i.e. sticks, IE empty casings.
 * This item provides only the missing ones.
 * </p>
 */
@IIItemProperties(category = IICategory.RESOURCES)
public class ItemIIAmmoCasing extends ItemIISubItemsBase<Casing>
{
	public ItemIIAmmoCasing()
	{
		super("ammo_casing", 64, Casing.values());
		setSortedSubItems(
				Casing.ARTILLERY_8BCAL, Casing.MEDIUM_ARTILLERY_6BCAL, Casing.LIGHT_ARTILLERY_6BCAL,
				Casing.LIGHT_GUN_4BCAL,
				Casing.ROCKET_6BCAL, Casing.HEAVY_ROCKET_10BCAL, Casing.GUIDED_MISSILE_6BCAL,
				Casing.MORTAR_6BCAL,
				Casing.AUTOCANNON_3BCAL, Casing.MG_2BCAL, Casing.STG_1BCAL, Casing.SMG_1BCAL,
				Casing.NAVAL_MINE, Casing.TRIPMINE, Casing.TELLERMINE, Casing.RADIO_EXPLOSIVES
		);
	}

	@GeneratedItemModels(itemName = "ammo_casing", texturePath = "bullets/casings")
	public enum Casing implements IIItemEnum
	{
		@IIItemProperties(stackSize = 1)
		ARTILLERY_8BCAL,
		@IIItemProperties(stackSize = 1)
		MORTAR_6BCAL,
		@IIItemProperties(stackSize = 1)
		LIGHT_ARTILLERY_6BCAL,

		@IIItemProperties(stackSize = 24)
		AUTOCANNON_3BCAL,
		@GeneratedSubItemModel(customTexturePath = "bullets/casings/machinegun_2bcal")
		@IIItemProperties(stackSize = 24)
		MG_2BCAL,
		@GeneratedSubItemModel(customTexturePath = "bullets/casings/assault_rifle_1bcal")
		@IIItemProperties(stackSize = 32)
		STG_1BCAL,
		@GeneratedSubItemModel(customTexturePath = "bullets/casings/submachinegun_1bcal")
		@IIItemProperties(stackSize = 48)
		SMG_1BCAL,

		@IIItemProperties(stackSize = 1)
		NAVAL_MINE,
		TRIPMINE,
		TELLERMINE,
		@IIItemProperties(stackSize = 1)
		RADIO_EXPLOSIVES,

		@IIItemProperties(stackSize = 1)
		MEDIUM_ARTILLERY_6BCAL,
		@IIItemProperties(stackSize = 1)
		LIGHT_GUN_4BCAL,

		@IIItemProperties(stackSize = 1)
		ROCKET_6BCAL,
		@IIItemProperties(stackSize = 1)
		HEAVY_ROCKET_10BCAL,
		@IIItemProperties(stackSize = 1)
		GUIDED_MISSILE_6BCAL
	}
}