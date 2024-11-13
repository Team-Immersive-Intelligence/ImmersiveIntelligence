package pl.pabilo8.immersiveintelligence.client.manual.pages;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.client.manual.IEManualInstance;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualInstance.ManualEntry;
import blusunrize.lib.manual.ManualPages;
import blusunrize.lib.manual.gui.GuiClickableList;
import blusunrize.lib.manual.gui.GuiManual;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Locale;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @since 06.08.2023
 */
public class IIManualPageFolder extends ManualPages
{
	private final String fullPath;
	private final String name;
	private ManualEntry entry = null;
	private final ArrayList<IIManualEntry> entries = new ArrayList<>();
	private final ArrayList<IIManualPageFolder> subFolders = new ArrayList<>();

	private GuiClickableList menu;

	//Subfolder constructor
	public IIManualPageFolder(ManualInstance manual, String name, @Nullable IIManualPageFolder parent)
	{
		super(manual, name);
		this.name = name;
		this.fullPath = (parent==null?"": parent.fullPath+"/")+name;

		//Root folder already has a category assigned to it
		if(parent!=null)
		{
			//Put to manual silently
			ManualHelper.getManual().manualContents.put(ManualHelper.CAT_UPDATE, entry = new ManualEntry("folder:"+fullPath, ManualHelper.CAT_UPDATE, this));
			((IEManualInstance)ManualHelper.getManual()).hideEntry(name);
		}

		//TODO: 07.08.2023 somehow convince IE's FontRenderer to render II symbols, or fully replace it with II's renderer
		Locale locale = I18n.i18nLocale;
		if(I18n.i18nLocale!=null)
		{
			locale.properties.put("ie.manual.entry."+name+".name", "\u2348 "+I18n.format("ie.manual.folder."+name)); //title
			locale.properties.put("ie.manual.entry."+name+".subtitle", ""); //subtitle
		}

	}

	//Root folder constructor
	public IIManualPageFolder(ManualInstance manual, String folderName, String category)
	{
		this(manual, folderName, (IIManualPageFolder)null);
		ManualHelper.getManual().manualContents.put(category, entry = new ManualEntry(name, ManualHelper.CAT_UPDATE, this));
	}

	@Override
	public void initPage(GuiManual gui, int x, int y, List<GuiButton> pageButtons)
	{
		super.initPage(gui, x, y, pageButtons);

		List<String> allEntries = new ArrayList<>();
		allEntries.addAll(subFolders.stream().map(n -> n.name).collect(Collectors.toList()));
		allEntries.addAll(entries.stream().map(ManualEntry::getName).collect(Collectors.toList()));

		pageButtons.add(menu = new GuiClickableList(gui, 0, x, y, 100, 168, 1f, 1, allEntries.toArray(new String[0]))
		{
			@Override
			public boolean mousePressed(Minecraft mc, int mx, int my)
			{
				boolean b = super.mousePressed(mc, mx, my);
				if(menu.selectedOption==-1)
					return b;
				if(menu.selectedOption < subFolders.size())
					gui.setSelectedEntry("folder:"+subFolders.get(menu.selectedOption).fullPath);
				else
					gui.setSelectedEntry(entries.get(menu.selectedOption-subFolders.size()).getName());

				return b;
			}
		});
	}

	public void addEntry(IIManualEntry entry)
	{
		entries.add(entry);
		entry.setFolder(this);
		ManualHelper.getManual().manualContents.put(ManualHelper.CAT_UPDATE, entry);
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

	public String getName()
	{
		return name;
	}

	@Nonnull
	public IIManualPageFolder getOrCreateSubFolder(String name)
	{
		//Get existing folder
		for(IIManualPageFolder entry : subFolders)
			if(entry.name.equals(name))
				return entry;

		//Create a new folder and add it as a subfolder
		IIManualPageFolder newFolder = new IIManualPageFolder(manual, name, this);
		subFolders.add(newFolder);

		return newFolder;
	}
}
