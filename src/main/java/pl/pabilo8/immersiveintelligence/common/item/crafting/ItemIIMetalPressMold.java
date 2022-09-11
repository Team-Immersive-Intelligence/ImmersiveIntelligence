package pl.pabilo8.immersiveintelligence.common.item.crafting;

import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIMetalPressMold.PressMolds;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

/**
 * @author Pabilo8
 * @since 2019-05-11
 */
public class ItemIIMetalPressMold extends ItemIISubItemsBase<PressMolds>
{
	public ItemIIMetalPressMold()
	{
		super("press_mold", 1, PressMolds.values());
	}

	public enum PressMolds implements IIItemEnum
	{
		HOWITZER,
		LIGHT_HOWITZER,
		MORTAR,
		AUTOCANNON,
		MACHINEGUN,
		ASSAULT_RIFLE,
		SUBMACHINEGUN,
		NAVAL_MINE,
		TRIPMINE,
		TELLERMINE
	}
}
