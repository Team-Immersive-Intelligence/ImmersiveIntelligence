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
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Overrides;
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

	/**
	 * Replaces the default IE manual entry with a new markdown-based one.
	 *
	 * @param entryName name of the existing entry
	 * @param category  category
	 * @return new manual entry
	 */
	protected final IIManualEntry replaceDefaultEntry(String entryName, String category)
	{
		return replaceDefaultEntry(entryName, entryName, category);
	}

	/**
	 * Replaces the default IE manual entry with a new markdown-based one.
	 *
	 * @param entryName     name of the existing entry
	 * @param replacingName name of the entry to add as the replacement
	 * @param category      category
	 * @return new manual entry
	 */
	protected final IIManualEntry replaceDefaultEntry(String entryName, String replacingName, String category)
	{
		if(!Overrides.enableManualOverride)
			return null;
		removeEntry(entryName, category);
		return addEntry(replacingName, category);
	}

	/**
	 * Moves a manual entry to another name and category.
	 *
	 * @param sourceName          name of the entry to move
	 * @param sourceCategory      category to remove the entry from
	 * @param destinationName     destination entry name
	 * @param destinationCategory category to add the entry to
	 * @return the moved manual entry, or null if no matching entry was found
	 */
	@Nullable
	protected final ManualEntry moveEntry(String sourceName, String sourceCategory, String destinationName, String destinationCategory)
	{
		if(!Overrides.enableManualOverride)
			return null;
		registerCategory(destinationCategory);
		//Remove old entry
		ManualEntry sourceEntry = removeEntry(sourceName, sourceCategory);

		//Register a new entry
		if(sourceEntry!=null)
		{
			IIManualPageFolder folder = createSubFolder(destinationName, destinationCategory, null);
			ManualEntry destinationEntry = new ManualEntry(destinationName, destinationCategory, sourceEntry.getPages());

			if(folder==null)
				registerEntry(destinationCategory, destinationEntry);
			else
				folder.addEntry(destinationEntry);

			/*Locale locale = I18n.i18nLocale;
			if(I18n.i18nLocale!=null)
			{
				//title
				locale.properties.put("ie.manual.entry."+destinationName+".name", I18n.format("ie.manual.entry."+sourceName+".name"));
				//subtitle
				locale.properties.put("ie.manual.entry."+destinationName+".subtext", I18n.format("ie.manual.entry."+sourceName+".subtext"));
			}*/
			ReflectionHelper.setPrivateValue(ManualEntry.class, destinationEntry, sourceName, "name");

			return destinationEntry;
		}
		return null;
	}

	/**
	 * Removes the first manual entry with the specified name from a category.
	 *
	 * @param entryName entry name
	 * @param category  category name
	 */
	@Nullable
	protected final ManualEntry removeEntry(String entryName, String category)
	{
		if(!Overrides.enableManualOverride)
			return null;
		Optional<ManualEntry> first = registeredManual.manualContents.get(category)
				.stream()
				.filter(manualEntry -> manualEntry.getName().equals(entryName))
				.findFirst();

		if(first.isPresent())
		{
			ManualEntry found = first.get();
			registeredManual.manualContents.remove(category, found);
			return found;
		}
		return null;
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
