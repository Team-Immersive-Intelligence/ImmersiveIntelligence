package pl.pabilo8.immersiveintelligence.common.util;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualInstance.ManualEntry;
import blusunrize.lib.manual.ManualPages;
import com.google.gson.*;
import net.minecraft.item.EnumRarity;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageContributorSkin;
import pl.pabilo8.immersiveintelligence.common.IILogger;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * @author Pabilo8
 * @since 30-06-2020
 */
public class CustomSkinHandler
{
	//Ordered by name
	public static final LinkedHashMap<String, SpecialSkin> specialSkins = new LinkedHashMap<>();
	//For each UUID
	public static final LinkedHashMap<String, ArrayList<SpecialSkin>> specialSkinsByUUID = new LinkedHashMap<>();

	public static void getManualPages()
	{
		ManualHelper.getManual().manualContents.removeAll("contributor_skins");
		ArrayList<ManualPages> skin_pages = new ArrayList<>();
		skin_pages.add(new ManualPages.Text(ManualHelper.getManual(), "contributor_skins"));
		for(SpecialSkin skin : CustomSkinHandler.specialSkins.values())
			skin_pages.add(new IIManualPageContributorSkin(ManualHelper.getManual(), skin));

		ManualEntry contributor_skins = ManualHelper.getManual().getEntry("contributor_skins");
		if(contributor_skins==null)
		{
			contributor_skins = new ManualEntry("contributor_skins", IILib.CAT_WARFARE);
			ManualHelper.getManual().manualContents.put(IILib.CAT_WARFARE, contributor_skins);
		}

		contributor_skins.setPages(skin_pages.toArray(new ManualPages[]{}));
	}

	public static class SpecialSkin
	{
		public final String name;
		public final String[] uuid;
		public final String[] appliesTo;
		public final List<String> mods;
		public int textColor = 0xffffff;
		public EnumRarity rarity = EnumRarity.UNCOMMON;

		public SpecialSkin(String name, String[] uuid, String[] appliesTo, List<String> mods)
		{
			this.name = name;
			this.uuid = uuid;
			this.appliesTo = appliesTo;
			this.mods = mods;
		}

		//Couldn't do it in the constructor, because it spitted an error
		void parseAdditionals()
		{
			//lambdas are love, lambdas are life
			Optional<String> optional = mods.stream().filter(s -> s.contains("text_color=")).findFirst();
			optional.ifPresent(s -> this.textColor = Integer.parseInt(s.substring(11), 16));
			mods.removeIf(s -> s.contains("text_color="));

			optional = mods.stream().filter(s -> s.contains("rarity=")).findFirst();
			//You know lambdas, huh? I'm wondering if it's slower or faster than a regular for loop, looks nice though
			optional.map(s -> s.substring(7).replace('/', ':')).ifPresent(substring -> Arrays.stream(EnumRarity.values()).filter(rarity -> rarity.rarityName.equals(substring)).forEach(rarity -> this.rarity = rarity));
			mods.removeIf(s -> s.contains("rarity="));
		}
	}

	/**
	 * @author BluSunrize (original), Pabilo8 (correct json implementation)
	 * @since 0.2.0
	 * <p>
	 * Special thanks to:
	 * Blu and Hazard - authors of the awesome mod Immersive Engineering (also, thanks Malte)
	 * Flan and GaryCXJk - for maintaining and letting me use their Turbo Model Thigy lib
	 * Fex Calo - who also offered me use of his updated TMT version (I rejected, because of use terms, sry)
	 * Flaxbeard - LOTS of code rightfully stolen from him, best IE addon dev, Glory to Flaxbeard!
	 * Choroman - ex-modeller of II and PixelWar,
	 * Krystiano - ex-newbie-modeller-in-training
	 * Kuruma - author of Immersive Energy,
	 * Muddykat and JStocke - members of Immersive Geology Team,
	 * Kingcavespider1 - the first pull-request creator
	 * Franz - wannabe modeller of II, retired
	 * Carver - a helpful idea giver, now a modeller
	 * Royal Jelly and MasterEnderman - the first stargivers on GitHub
	 * Blackdragon - a Youtuber, tutorial maker and a helper for the community
	 * And of course, thanks to all non mentioned II contributors,
	 * no matter the size of contribution, you all deserve a reward ^^
	 * - Pabilo8, 30-06-2020
	 * </p>
	 */
	public static class ThreadContributorSpecialsDownloader extends Thread
	{
		public static ThreadContributorSpecialsDownloader activeThread;

		public ThreadContributorSpecialsDownloader()
		{
			setName("Immersive Intelligence Contributors Thread");
			setDaemon(true);
			start();
			activeThread = this;
		}

		@Override
		public void run()
		{
			Gson gson = new Gson();
			try
			{
				IILogger.info("Attempting to download II special skin list from GitHub");
				URL url = new URL("https://raw.githubusercontent.com/Pabilo8/ImmersiveIntelligence/master/contributor_skins.json");
				specialSkins.clear();
				specialSkinsByUUID.clear();
				JsonStreamParser parser = new JsonStreamParser(new InputStreamReader(url.openStream()));
				JsonArray array = parser.next().getAsJsonArray();
				for(JsonElement jsonElement : array)
				{
					try
					{
						//Non-automatic parsing because of lots of (file cache?) errors
						JsonObject jsonObject = jsonElement.getAsJsonObject();

						String name = jsonObject.get("name").getAsString();

						ArrayList<String> uuid = new ArrayList<>(), appliesTo = new ArrayList<>(), mods = new ArrayList<>();
						jsonObject.get("uuid").getAsJsonArray().iterator().forEachRemaining(jsonElement1 -> uuid.add(jsonElement1.getAsString()));
						jsonObject.get("appliesTo").getAsJsonArray().iterator().forEachRemaining(jsonElement1 -> appliesTo.add(jsonElement1.getAsString()));
						jsonObject.get("mods").getAsJsonArray().iterator().forEachRemaining(jsonElement1 -> mods.add(jsonElement1.getAsString()));

						SpecialSkin skin = new SpecialSkin(name, uuid.toArray(new String[]{}), appliesTo.toArray(new String[]{}), mods);

						specialSkins.put(skin.name, skin);

						if(skin.uuid!=null)
							for(String id : skin.uuid)
								if(specialSkinsByUUID.containsKey(id))
									specialSkinsByUUID.get(id).add(skin);
								else
									specialSkinsByUUID.put(id, new ArrayList<>(Collections.singleton(skin)));

					} catch(Exception excepParse)
					{
						IILogger.warn(excepParse);
					}
				}
				specialSkins.values().forEach(SpecialSkin::parseAdditionals);
				StringBuilder builder = new StringBuilder("Loaded skins: ");
				specialSkins.values().forEach(specialSkin -> builder.append(specialSkin.name).append(", "));
				IILogger.info(builder.delete(builder.lastIndexOf(", "), builder.length()).toString());

			} catch(Exception e)
			{
				IILogger.info("Could not load contributor special skin list.");
				e.printStackTrace();
			}

		}
	}


}
