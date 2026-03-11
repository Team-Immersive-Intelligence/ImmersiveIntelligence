package pl.pabilo8.immersiveintelligence.client.model;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import pl.pabilo8.immersiveintelligence.client.util.texture.SpriteRecolored;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Graphics;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * Handles {@link pl.pabilo8.immersiveintelligence.client.util.texture.SpriteRecolored recolorable texture variants}, requires manual texture path registration.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 24.11.2025
 */
public class TextureRecoloringRegistry
{
	private static final HashMap<ResourceLocation, RecolorableTexture> RECOLORABLE_TEXTURES = new HashMap<>();

	/**
	 * Registers a texture to be recolored with multiple color variants
	 *
	 * @param baseTexture The original texture to recolor
	 */
	public static void registerRecolorableTexture(ResourceLocation baseTexture)
	{
		RECOLORABLE_TEXTURES.putIfAbsent(baseTexture, new RecolorableTexture(baseTexture));
	}

	public static void onTextureStitch(TextureStitchEvent.Pre event)
	{
		TextureMap textureMap = event.getMap();

		RECOLORABLE_TEXTURES.forEach((baseLocation, texture) -> {
			// Generate colors distributed across the hue spectrum
			IILogger.info("Registering texture variant of {}", baseLocation);
			for(int i = 0; i < Graphics.dynamiclyColoredTextureVariants; i++)
			{
				float hue = (float)i/(float)Graphics.dynamiclyColoredTextureVariants;
				IIColor color = IIColor.fromHSV(hue, 0.35f, 0.85f);

				// Create the recolored sprite
				SpriteRecolored recoloredSprite = new SpriteRecolored(baseLocation, color);
				textureMap.setTextureEntry(recoloredSprite);
				texture.recoloredVariants.put(color, recoloredSprite);
			}
		});
	}

	/**
	 * Helper method to get the sprite for a recolored texture.
	 *
	 * @param base  The base texture
	 * @param color The color variant
	 * @return The sprite for the recolored texture, or null, if it does not exist
	 * @implNote Does not approximate colors, use the exact same as one registered
	 */
	@Nullable
	public static SpriteRecolored getRecoloredTextureSprite(ResourceLocation base, IIColor color)
	{
		RecolorableTexture texture = RECOLORABLE_TEXTURES.get(base);
		if(texture==null)
			return null;
		return texture.recoloredVariants.get(color);
	}

	private static class RecolorableTexture
	{
		public final ResourceLocation baseTexture;
		public final HashMap<IIColor, SpriteRecolored> recoloredVariants = new HashMap<>();

		public RecolorableTexture(ResourceLocation baseTexture)
		{
			this.baseTexture = baseTexture;
		}
	}
}
