package pl.pabilo8.immersiveintelligence.common.item.ammo;

import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casings;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

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
public class ItemIIAmmoCasing extends ItemIISubItemsBase<Casings>
{
	public ItemIIAmmoCasing()
	{
		super("ammo_casing", 64, Casings.values());
	}

	public enum Casings implements IIItemEnum
	{
		@IIItemProperties(stackSize = 1)
		ARTILLERY_8BCAL,
		@IIItemProperties(stackSize = 1)
		MORTAR_6BCAL,
		@IIItemProperties(stackSize = 1)
		LIGHT_ARTILLERY_6BCAL,

		@IIItemProperties(stackSize = 24)
		AUTOCANNON_3BCAL,
		@IIItemProperties(stackSize = 24)
		MG_2BCAL,
		@IIItemProperties(stackSize = 32)
		STG_1BCAL,
		@IIItemProperties(stackSize = 48)
		SMG_1BCAL,

		@IIItemProperties(stackSize = 1)
		NAVAL_MINE,
		TRIPMINE,
		TELLERMINE,
		@IIItemProperties(stackSize = 1)
		RADIO_EXPLOSIVES
	}
}
