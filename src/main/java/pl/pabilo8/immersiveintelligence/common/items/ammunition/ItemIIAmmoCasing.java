package pl.pabilo8.immersiveintelligence.common.items.ammunition;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;

/**
 * @author Pabilo8
 * @since 08.08.2021
 *
 * <p>
 *     Bullet System mk.4 changes how ammo items works (again!).
 *     Cores and Bullets are one item, as they both use the same nbt tags.
 *     Casings are optional items which are used only in crafting and have no impact on the actual bullet.
 *     Some items like grenades and revolver bullets use existing items as casings, i.e. sticks, IE empty casings.
 *     This item provides only the missing ones.
 * </p>
 */
public class ItemIIAmmoCasing extends ItemIIBase
{
	public ItemIIAmmoCasing()
	{
		super("ammo_casing", 64,
				"artillery_8bcal",
				"mortar_6bCal",
				"light_artillery_6bcal",
				"autocannon_3bcal",
				"mg_2bcal",
				"stg_1bcal",
				"smg_1bcal",
				"naval_mine",
				"tripmine",
				"tellermine",
				"radio_explosives"
		);
	}

	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		switch(stack.getMetadata())
		{
			case 0:
				return IIContent.itemAmmoArtillery.getItemStackLimit(stack);
			case 1:
				return IIContent.itemAmmoMortar.getItemStackLimit(stack);
			case 2:
				return IIContent.itemAmmoLightArtillery.getItemStackLimit(stack);
			case 3:
				return IIContent.itemAmmoAutocannon.getItemStackLimit(stack);
			case 4:
				return IIContent.itemAmmoMachinegun.getItemStackLimit(stack);
			case 5:
				return IIContent.itemAmmoAssaultRifle.getItemStackLimit(stack);
			case 6:
				return IIContent.itemAmmoSubmachinegun.getItemStackLimit(stack);
			case 7:
				return IIContent.itemNavalMine.getItemStackLimit(stack);
			case 8:
			case 9:
				return 64;
			case 10:
				return 1;
		}
		return super.getItemStackLimit(stack);
	}
}
