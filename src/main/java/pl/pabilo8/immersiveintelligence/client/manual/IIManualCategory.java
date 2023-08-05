package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.client.manual.IEManualInstance;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.LinkedHashSet;

/**
 * @author Pabilo8
 * @since 18-01-2020
 */
public abstract class IIManualCategory
{
	public abstract String getCategory();

	/**
	 * Well, maybe it is deprecated...<br>
	 * And, well, maybe it is unchecked...<br>
	 * But doing it by adding a page and then removing it would make it even more messy
	 */
	@SuppressWarnings({"deprecation", "unchecked"})
	public void addPages()
	{
		IEManualInstance manual = (IEManualInstance)ManualHelper.getManual();
		manual.manualContents.removeAll(getCategory());
		((LinkedHashSet<String>)ReflectionHelper.getPrivateValue(IEManualInstance.class, manual, "categorySet")).add(getCategory());
	}

	protected final IIManualEntry addEntry(String name)
	{
		IIManualEntry entry = new IIManualEntry(name, getCategory());
		ManualHelper.getManual().manualContents.put(getCategory(), entry);
		return entry;
	}

	protected final EasyNBT getSourceForItem(ItemStack stack)
	{
		return EasyNBT.newNBT().withItemStack("item", stack);
	}

	protected final EasyNBT getSourceForItems(ItemStack... stacks)
	{
		return EasyNBT.newNBT().withList("items", stacks);
	}
}
