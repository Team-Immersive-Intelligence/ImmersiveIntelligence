package pl.pabilo8.immersiveintelligence.common.item;

import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;

/**
 * @author Avalon (avalon@iiteam.net)
 * @since 31.10.2026
 */
@IIItemProperties(category = IICategory.RESOURCES)
public class ItemIISolderIngot extends ItemIIBase
{
	public ItemIISolderIngot()
	{
		super("solder_ingot", 64);
	}
}
