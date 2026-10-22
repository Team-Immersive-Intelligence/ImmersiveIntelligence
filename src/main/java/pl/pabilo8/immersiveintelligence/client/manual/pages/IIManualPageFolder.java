package pl.pabilo8.immersiveintelligence.client.manual.pages;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.client.manual.IEManualInstance;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualInstance.ManualEntry;
import blusunrize.lib.manual.ManualPages;
import blusunrize.lib.manual.gui.GuiClickableList;
import blusunrize.lib.manual.gui.GuiManual;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Locale;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualEntry;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Registers folder entries and supplies navigation to their child entries.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 06.10.2026
 * @ii-approved 0.3.2
 * @since 06.08.2023
 */
public class IIManualPageFolder extends ManualPages
{
	private final String fullPath;
	@Getter
	private final String name;
	private final String category;
	private final String namespace;
	private final BiConsumer<String, ManualEntry> registerEntry;
	private final ArrayList<IIManualEntry> entries = new ArrayList<>();
	private final ArrayList<IIManualPageFolder> subFolders = new ArrayList<>();
	private final ManualEntry entry;
	private GuiClickableList menu;

	/**
	 * Creates a folder with the parent category and registration method.
	 */
	public IIManualPageFolder(ManualInstance manual, String name, @Nullable IIManualPageFolder parent)
	{
		this(manual, name, parent, parent==null?ManualHelper.CAT_UPDATE: parent.category,
				parent==null?ManualHelper.CAT_UPDATE: parent.namespace,
				parent==null?(category, entry) -> manual.manualContents.put(category, entry): parent.registerEntry);
	}

	/**
	 * Creates a root folder in the destination category.
	 */
	public IIManualPageFolder(ManualInstance manual, String folderName, String category)
	{
		this(manual, folderName, category, category, (storageCategory, entry) -> manual.manualContents.put(storageCategory, entry));
	}

	/**
	 * Creates a root folder with a registration method that tracks its entries.
	 */
	public IIManualPageFolder(ManualInstance manual, String folderName, String category, String namespace,
							  BiConsumer<String, ManualEntry> registerEntry)
	{
		this(manual, folderName, null, category, namespace, registerEntry);
	}

	private IIManualPageFolder(ManualInstance manual, String name, @Nullable IIManualPageFolder parent,
							   String category, String namespace, BiConsumer<String, ManualEntry> registerEntry)
	{
		super(manual, name);
		this.name = name;
		this.fullPath = (parent==null?"": parent.fullPath+"/")+name;
		this.category = category;
		this.namespace = namespace;
		this.registerEntry = registerEntry;
		this.entry = new ManualEntry("folder:"+namespace+"/"+category+"/"+fullPath, category, this);
		registerEntry.accept(parent==null?category: ManualHelper.CAT_UPDATE, entry);

		if(parent!=null)
			((IEManualInstance)manual).hideEntry(getEntryName());

		Locale locale = I18n.i18nLocale;
		if(locale!=null)
		{
			locale.properties.put("ie.manual.entry."+getEntryName()+".name", IIReference.CHARICON_FOLDER+" "+I18n.format("ie.manual.folder."+name));
			locale.properties.put("ie.manual.entry."+getEntryName()+".subtext", "");
		}
	}

	@Override
	public void initPage(GuiManual gui, int x, int y, List<GuiButton> pageButtons)
	{
		super.initPage(gui, x, y, pageButtons);

		List<String> allEntries = new ArrayList<>();
		allEntries.addAll(subFolders.stream().map(IIManualPageFolder::getEntryName).collect(Collectors.toList()));
		allEntries.addAll(entries.stream().map(ManualEntry::getName).collect(Collectors.toList()));

		pageButtons.add(menu = new GuiClickableList(gui, 0, x, y, 100, 168, 1f, 1, allEntries.toArray(new String[0]))
		{
			@Override
			public boolean mousePressed(Minecraft mc, int mx, int my)
			{
				if(!super.mousePressed(mc, mx, my))
					return false;
				gui.previousSelectedEntry.push(getEntryName());
				if(menu.selectedOption < subFolders.size())
					gui.setSelectedEntry(subFolders.get(menu.selectedOption).getEntryName());
				else
					gui.setSelectedEntry(entries.get(menu.selectedOption-subFolders.size()).getName());
				gui.page = 0;

				return true;
			}
		});
	}

	/**
	 * Registers a child entry and hides it from the update-news list.
	 */
	public void addEntry(IIManualEntry entry)
	{
		entries.add(entry);
		entry.setFolder(this);
		registerEntry.accept(ManualHelper.CAT_UPDATE, entry);
		((IEManualInstance)manual).hideEntry(entry.getName());
	}

	@Override
	public void renderPage(GuiManual gui, int x, int y, int mx, int my)
	{

	}

	@Override
	public boolean listForSearch(String searchTag)
	{
		//TODO: 07.08.2023 add search keywords from subfolders
		return false;
	}

	/**
	 * Gets the global entry ID for folder navigation.
	 */
	public String getEntryName()
	{
		return entry.getName();
	}

	/**
	 * Gets a child folder or creates it with this folder's registration method.
	 */
	@Nonnull
	public IIManualPageFolder getOrCreateSubFolder(String name)
	{
		//Find the existing folder.
		for(IIManualPageFolder entry : subFolders)
			if(entry.name.equals(name))
				return entry;

		IIManualPageFolder newFolder = new IIManualPageFolder(manual, name, this, category, namespace, registerEntry);
		subFolders.add(newFolder);

		return newFolder;
	}
}
