package pl.pabilo8.immersiveintelligence.client.util.texture;

import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static pl.pabilo8.immersiveintelligence.common.util.IIReference.RES_II;
import static pl.pabilo8.immersiveintelligence.common.util.IIReference.RES_MC;
import static pl.pabilo8.immersiveintelligence.common.util.ResLoc.EXT_PNG;

/**
 * Caches flag textures with flag masks for II banner patterns.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.09.2026
 */
@SideOnly(Side.CLIENT)
public final class FlagBannerTexturesCache extends BannerTextures.Cache
{
	private static final ResLoc BASE_TEXTURE = RES_MC.with("textures/entity/banner_base").withExtension(EXT_PNG);
	private static final ResLoc PATTERN_TEXTURES = RES_MC.with("textures/entity/banner/");
	private final LinkedHashMap<String, TextureEntry> textures = new LinkedHashMap<>();

	public FlagBannerTexturesCache()
	{
		super("II_FLAG", BASE_TEXTURE, PATTERN_TEXTURES.getResourcePath());
	}

	/**
	 * Gets a cached flag texture or builds it from the pattern and colour lists.
	 */
	@Nullable
	@Override
	public ResourceLocation getResourceLocation(String id, List<BannerPattern> patternList, List<EnumDyeColor> colorList)
	{
		if(id==null||id.isEmpty())
			return null;
		long now = System.currentTimeMillis();
		TextureEntry entry = textures.get(id);
		if(entry==null)
		{
			TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
			//Keep the vanilla cache limit and remove one unused texture when full.
			if(textures.size() >= 256)
			{
				Iterator<TextureEntry> iterator = textures.values().iterator();
				while(iterator.hasNext())
				{
					TextureEntry unused = iterator.next();
					if(now-unused.lastUseMillis > 5000L)
					{
						textureManager.deleteTexture(unused.texture);
						iterator.remove();
						break;
					}
				}
				if(textures.size() >= 256)
					return BannerTextures.BANNER_BASE_TEXTURE;
			}

			List<String> layers = patternList.stream().map(pattern -> {
				String name = pattern.getFileName();
				return PATTERN_TEXTURES.with(name, name.startsWith(ImmersiveIntelligence.MODID+"_")?"_flag": "")
						.withExtension(EXT_PNG).toString();
			}).collect(Collectors.toList());
			ResLoc texture = RES_II.with("flag/", id);
			textureManager.loadTexture(texture, new LayeredColorMaskTexture(BASE_TEXTURE, layers, new ArrayList<>(colorList)));
			entry = new TextureEntry(texture);
			textures.put(id, entry);
		}
		entry.lastUseMillis = now;
		return entry.texture;
	}

	/**
	 * Stores a flag texture and its last use time.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 30.09.2026
	 */
	@RequiredArgsConstructor
	private static class TextureEntry
	{
		private final ResLoc texture;
		private long lastUseMillis;
	}
}
