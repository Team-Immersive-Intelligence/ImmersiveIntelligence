package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.client.manual.IEManualInstance;
import blusunrize.lib.manual.ManualInstance.ManualEntry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageFolder;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import java.util.LinkedHashSet;
import java.util.List;

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

	public static void cleanFolderEntries()
	{
		IEManualInstance manual = (IEManualInstance)ManualHelper.getManual();
		List<ManualEntry> remaining = manual.manualContents.get(ManualHelper.CAT_UPDATE);
		remaining.removeIf(entry -> entry.getPages().length!=1||!(entry.getPages()[0] instanceof IIManualPageFolder));

		//Well, one way or another...
		for(ManualEntry folder : remaining)
			manual.manualContents.remove(ManualHelper.CAT_UPDATE, folder);
	}

	protected final IIManualEntry addEntry(String name)
	{
		IIManualPageFolder folder = createSubFolder(name, null);
		IIManualEntry entry = new IIManualEntry(name, getCategory());

		if(folder==null)
			ManualHelper.getManual().manualContents.put(getCategory(), entry);
		else
			folder.addEntry(entry);

		return entry;
	}

	@Nullable
	private IIManualPageFolder createSubFolder(String fileName, @Nullable IIManualPageFolder folder)
	{
		//No folders or last folder
		if(!fileName.contains("/"))
			return folder;

		int i = fileName.indexOf("/");
		String folderName = fileName.substring(0, i);
		String remaining = fileName.substring(i+1);

		//Checking in root directory
		if(folder==null)
		{
			List<ManualEntry> manualEntries = ManualHelper.getManual().manualContents.get(getCategory());
			folder = manualEntries.stream()
					.filter(manualEntry -> manualEntry.getName().equals(folderName))
					.limit(1)
					.map(ManualEntry::getPages)
					.filter(pages -> pages.length > 0&&pages[0] instanceof IIManualPageFolder)
					.map(pages -> (IIManualPageFolder)pages[0])
					.findFirst().orElseGet(
							() -> new IIManualPageFolder(ManualHelper.getManual(), folderName, getCategory())
					);

			return createSubFolder(remaining, folder);
		}

		//Add the folder to root
		return folder.getOrCreateSubFolder(folderName);
	}

	protected final EasyNBT getSourceForItem(ItemStack stack)
	{
		return EasyNBT.newNBT().withItemStack("item", stack);
	}

	protected final EasyNBT getSourceForItems(ItemStack... stacks)
	{
		return EasyNBT.newNBT().withList("items", stacks);
	}

	protected final EasyNBT getSourceForBlueprint(String name)
	{
		return EasyNBT.newNBT().withString("blueprint", name);
	}
}
