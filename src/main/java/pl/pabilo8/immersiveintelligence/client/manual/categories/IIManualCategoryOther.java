package pl.pabilo8.immersiveintelligence.client.manual.categories;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualCategory;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author fastdelaspeed
 * @since 20-02-2025
 */
public class IIManualCategoryOther extends IIManualCategory
{
	public static IIManualCategoryOther INSTANCE = new IIManualCategoryOther();

	@Override
	public String getCategory()
	{
		return IIReference.CAT_OTHER;
	}

	@Override
	public void addPages()
	{
		super.addPages();
		addEntry("electric_tools")
				.addSource("e_hammer", getSourceForItem(new ItemStack(IIContent.itemHammer)))
				.addSource("e_wrench", getSourceForItem(new ItemStack(IIContent.itemElectricWrench)))
				.addSource("e_cutter", getSourceForItem(new ItemStack(IIContent.itemWirecutter)
				));

		addEntry("lighter")
				.addSource("lighter", getSourceForItem(new ItemStack(IIContent.itemLighter)
				));

		addEntry("measuring_cup")
				.addSource("m_cup", getSourceForItem(new ItemStack(IIContent.itemMeasuringCup)
				));
	}
}