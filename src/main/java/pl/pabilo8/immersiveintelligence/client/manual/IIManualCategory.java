package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.client.manual.IEManualInstance;
import blusunrize.lib.manual.IManualPage;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualInstance.ManualEntry;
import lombok.RequiredArgsConstructor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageFolder;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Registers manual entries and removes its own entries before a reload.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 06.10.2026
 * @ii-approved 0.3.2
 * @since 18.01.2020
 */
public abstract class IIManualCategory
{
	private final List<ManualRegistration> registrations = new ArrayList<>();
	private final Map<String, IIManualPageFolder> rootFolders = new LinkedHashMap<>();
	private ManualInstance registeredManual;

	/**
	 * Gets the resource directory and the default destination category.
	 */
	public abstract String getCategory();

	/**
	 * Removes owned entries. Overrides must call this method before they add entries.
	 */
	public void addPages()
	{
		clearEntries();
		registeredManual = ManualHelper.getManual();
	}

	protected final IIManualEntry addEntry(String name)
	{
		return addEntry(name, getCategory());
	}

	/**
	 * Adds a Markdown entry to the destination category with this provider's resources.
	 */
	protected final IIManualEntry addEntry(String name, String targetCategory)
	{
		registerCategory(targetCategory);
		IIManualPageFolder folder = createSubFolder(name, targetCategory, null);
		IIManualEntry entry = new IIManualEntry(name, targetCategory, getCategory());

		if(folder==null)
			registerEntry(targetCategory, entry);
		else
			folder.addEntry(entry);

		return entry;
	}

	/**
	 * Adds an entry with pages that do not use Markdown.
	 */
	protected final ManualEntry addEntry(String name, String targetCategory, IManualPage... pages)
	{
		registerCategory(targetCategory);
		ManualEntry entry = new ManualEntry(name, targetCategory, pages);
		registerEntry(targetCategory, entry);
		return entry;
	}

	private void clearEntries()
	{
		if(registeredManual!=null)
			for(ManualRegistration registration : registrations)
				registeredManual.manualContents.remove(registration.category, registration.entry);
		registrations.clear();
		rootFolders.clear();
	}

	private void registerEntry(String category, ManualEntry entry)
	{
		getManual().manualContents.put(category, entry);
		registrations.add(new ManualRegistration(category, entry));
	}

	private ManualInstance getManual()
	{
		ManualInstance manual = ManualHelper.getManual();
		if(registeredManual!=manual)
		{
			clearEntries();
			registeredManual = manual;
		}
		return manual;
	}

	@SuppressWarnings({"deprecation", "unchecked"})
	private void registerCategory(String category)
	{
		IEManualInstance manual = (IEManualInstance)getManual();
		((Set<String>)ReflectionHelper.getPrivateValue(IEManualInstance.class, manual, "categorySet")).add(category);
	}

	@Nullable
	private IIManualPageFolder createSubFolder(String fileName, String targetCategory, @Nullable IIManualPageFolder folder)
	{
		//The last path segment is the entry file.
		if(!fileName.contains("/"))
			return folder;

		int i = fileName.indexOf("/");
		String folderName = fileName.substring(0, i);
		String remaining = fileName.substring(i+1);

		if(folder==null)
			folder = rootFolders.computeIfAbsent(targetCategory+"/"+folderName,
					key -> new IIManualPageFolder(getManual(), folderName, targetCategory, getCategory(), this::registerEntry));
		else
			folder = folder.getOrCreateSubFolder(folderName);

		return createSubFolder(remaining, targetCategory, folder);
	}

	protected final EasyNBT getSourceForItem(ItemStack stack)
	{
		return EasyNBT.newNBT().withItemStack("item", stack);
	}

	protected final EasyNBT getSourceForItems(ItemStack... stacks)
	{
		return EasyNBT.newNBT().withList("items", stacks);
	}

	protected final EasyNBT getSourceForRecipe(Class<? extends IIMultiblockRecipe> klass, Object mainHashObject, Object... hashObjects)
	{
		String recipeName = IIMultiblockRecipe.generateRecipeName(mainHashObject, hashObjects);
		return getSourceForRecipe(klass, recipeName);
	}

	protected final EasyNBT getSourceForRecipe(Class<? extends IIMultiblockRecipe> klass, String recipeName)
	{
		String recipeClassName = IIMultiblockRecipe.getRecipeClassName(klass);
		return EasyNBT.newNBT().withString("type", recipeClassName).withString("recipe", recipeName);
	}

	protected final EasyNBT getSourceForBlueprint(String name)
	{
		return EasyNBT.newNBT().withString("blueprint", name);
	}

	/**
	 * Stores the entry instance and its registration key for cleanup.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 06.10.2026
	 */
	@RequiredArgsConstructor
	private static final class ManualRegistration
	{
		private final String category;
		private final ManualEntry entry;
	}
}
