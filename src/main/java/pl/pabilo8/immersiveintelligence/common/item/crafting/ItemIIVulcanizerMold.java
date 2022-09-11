package pl.pabilo8.immersiveintelligence.common.item.crafting;

import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIVulcanizerMold.VulcanizerMolds;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

/**
 * @author Pabilo8
 * @since 01.05.2021
 */
public class ItemIIVulcanizerMold extends ItemIISubItemsBase<VulcanizerMolds>
{
	public ItemIIVulcanizerMold()
	{
		super("vulcanizer_mold", 64, VulcanizerMolds.values());
	}

	public enum VulcanizerMolds implements IIItemEnum
	{
		BELT,
		TIRE
	}
}
