package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.lib.manual.ManualInstance.ManualEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.Locale;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageFolder;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @since 21.03.2022
 */
public class IIManualEntry extends ManualEntry
{
	private final LinkedHashMap<String, String> texts = new LinkedHashMap<>();
	private final LinkedHashMap<String, EasyNBT> dataSources = new LinkedHashMap<>();
	private final String fullFilePath;
	private IIManualPageFolder folder;

	public IIManualEntry(String name, String category)
	{
		super(name.contains("/")?name.substring(name.lastIndexOf("/")+1): name, category);
		this.fullFilePath = name;
		loadTexts(true);
	}

	@SideOnly(Side.CLIENT)
	public void loadTexts(boolean updateMeta)
	{
		Language lang = ClientUtils.mc().getLanguageManager().getCurrentLanguage();
		boolean nonEnglish = !lang.getLanguageCode().equalsIgnoreCase("en_us");

		IResource stateFile = tryGetFile(lang.getLanguageCode().toLowerCase());
		if(nonEnglish&&stateFile==null)
			stateFile = tryGetFile("en_us");

		if(stateFile!=null)
		{
			texts.clear();
			InputStream inputStream = stateFile.getInputStream();
			String[] sections = new BufferedReader(new InputStreamReader(inputStream))
					.lines()
					.map(s -> s.startsWith("#")?s.replaceFirst("#", "\t"): s) //replace section markers with tab (so it won't mess up links)
					.collect(Collectors.joining("\n")) //join entire text to a single string
					.split("\t"); //separate sections
			IOUtils.closeQuietly(inputStream);
			System.arraycopy(sections, 1, sections, 0, sections.length-1);

			for(String s : sections)
			{
				String[] lines = s.trim().split("\n"); //separate with newline, for iteration

				if(lines.length==0)
					continue;
				lines[0] = lines[0].trim();

				if(lines[0].startsWith("meta")) //metadata
				{
					if(updateMeta&&lines.length > 1)
					{
						//you wish you know how much I struggled to get this to work... xD
						Locale locale = I18n.i18nLocale;
						if(I18n.i18nLocale!=null)
						{
							locale.properties.put("ie.manual.entry."+getName()+".name", lines[1]); //title
							if(lines.length > 2)
								locale.properties.put("ie.manual.entry."+getName()+".subtext", lines[2]); //subtitle
						}
					}
				}
				else //process section
				{
					StringBuilder builder = new StringBuilder();
					for(int i = 1; i < lines.length; i++)
						builder.append(lines[i]
								.replace("%SECTION%", lines[0]) //replace %SECTION% with section name
								.replace("%PAGE%", getName()) //replace %SECTION% with page name
						).append("\n"); //add all lines aside page id

					texts.put(lines[0], builder.toString());
				}
			}

		}
		else
			IILogger.error("Could not load manual page for %s", getName());

		if(updateMeta)
			setPages(texts.keySet()
					.stream()
					.map(IIManualPage::new)
					.peek(IIManualPage::setManual)
					.peek(p -> p.setParent(this))
					.toArray(IIManualPage[]::new)
			);
	}

	@Nullable
	private IResource tryGetFile(String language)
	{
		try
		{
			return Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(ImmersiveIntelligence.MODID,
					String.format("ie_manual/%s/%s/%s.md", language, getCategory(), fullFilePath)));
		} catch(IOException ignored) {}
		return null;
	}

	/**
	 * Adds a Data Source to the page
	 *
	 * @param name   source id
	 * @param source source tag compound
	 */
	//TODO: 08.08.2023 Adding sources from json files
	public IIManualEntry addSource(String name, EasyNBT source)
	{
		dataSources.put(name, source);
		return this;
	}

	/**
	 * Gets a Data Source from the page
	 *
	 * @param name source id
	 * @return source tag compound
	 */
	@Nullable
	public EasyNBT getSource(String name)
	{
		return dataSources.getOrDefault(name, null);
	}

	public String fetchPage(String text)
	{
		loadTexts(false);
		return texts.getOrDefault(text, null);
	}

	public int getSubPageID(String name)
	{
		int i = 0;
		for(String s : texts.keySet())
		{
			if(name.equals(s))
				return i;
			i++;
		}
		return -1;
	}

	public void setFolder(IIManualPageFolder folder)
	{
		this.folder = folder;
	}

	public IIManualPageFolder getFolder()
	{
		return folder;
	}
}
