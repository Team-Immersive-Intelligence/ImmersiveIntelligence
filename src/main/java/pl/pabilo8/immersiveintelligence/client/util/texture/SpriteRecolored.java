package pl.pabilo8.immersiveintelligence.client.util.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

/**
 * A recolored variant of a {@link TextureAtlasSprite} used mainly by Immersive Intelligence's paintable vehicles.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 24.11.2025
 */
public class SpriteRecolored extends TextureAtlasSprite
{
	/**
	 * The source sprite of this texture.
	 */
	private final ResourceLocation srcLocation;
	/**
	 * The color to apply
	 */
	private final IIColor color;

	public SpriteRecolored(ResourceLocation srcLocation, IIColor color)
	{
		super(ResLoc.root(srcLocation).with(srcLocation.getResourcePath(), "_", color.getHexRGB()).toString());
		this.srcLocation = srcLocation;
		this.color = color;
	}

	@Override
	public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location)
	{
		return true;
	}

	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return Collections.singletonList(srcLocation);
	}

	@Override
	public boolean load(IResourceManager manager, ResourceLocation location, Function<ResourceLocation, TextureAtlasSprite> textureGetter)
	{
		try
		{
			final TextureAtlasSprite sourceSprite = textureGetter.apply(srcLocation);

			if(sourceSprite==null)
			{
				IILogger.error("Failed to create a recolored sprite of {} as the source sprite wasn't able to be loaded!", srcLocation);
				return true;
			}

			// Copy dimensions from source sprite
			width = sourceSprite.getIconWidth();
			height = sourceSprite.getIconHeight();

			final int[][] pixels = new int[Minecraft.getMinecraft().gameSettings.mipmapLevels+1][];
			pixels[0] = new int[width*height];

			// Get the source sprite's pixel data
			final int[][] sourcePixels = sourceSprite.getFrameTextureData(0);

			if(sourcePixels==null||sourcePixels[0]==null)
			{
				IILogger.error("Source sprite has no pixel data: {}", srcLocation);
				return true;
			}

			// Recolor the pixels
			for(int p = 0; p < width*height; p++)
			{
				pixels[0][p] = recolorPixel(sourcePixels[0][p], color);
			}

			// Generate mipmaps for other levels if needed
			for(int level = 1; level < pixels.length; level++)
			{
				if(sourcePixels.length > level&&sourcePixels[level]!=null)
				{
					// Recolor existing mipmap levels from source
					pixels[level] = new int[sourcePixels[level].length];
					for(int p = 0; p < sourcePixels[level].length; p++)
					{
						pixels[level][p] = recolorPixel(sourcePixels[level][p], color);
					}
				}
			}

			this.clearFramesTextureData();
			this.framesTextureData.add(pixels);
			return false;
		} catch(Exception e)
		{
			IILogger.error("Failed to create a recolored sprite of {}: {}", srcLocation, e.toString());
			return true;
		}
	}

	/**
	 * Recolors a single pixel using the specified IIColor, with smooth blending based on grayness
	 */
	private int recolorPixel(int pixel, IIColor targetColor)
	{
		int alpha = (pixel>>24)&0xFF;

		if(alpha > 0)
		{
			// Extract original RGB components
			int r = (pixel>>16)&0xFF;
			int g = (pixel>>8)&0xFF;
			int b = pixel&0xFF;

			// Calculate how "gray" the pixel is
			int maxComponent = Math.max(r, Math.max(g, b));
			int minComponent = Math.min(r, Math.min(g, b));
			int componentRange = maxComponent-minComponent;

			// Define thresholds for blending
			int minGrayThreshold = 0;  // Definitely gray - no recoloring
			int maxGrayThreshold = 40;  // Definitely colored - full recoloring

			// Calculate blend factor (0 = gray, no recolor; 1 = colored, full recolor)
			float blendFactor;
			if(componentRange <= minGrayThreshold)
			{
				blendFactor = 0.0f; // Completely gray, no recoloring
			}
			else if(componentRange >= maxGrayThreshold)
			{
				blendFactor = 1.0f; // Definitely colored, full recoloring
			}
			else
			{
				// Smooth transition between thresholds
				blendFactor = (float)(componentRange-minGrayThreshold)/(maxGrayThreshold-minGrayThreshold);
			}

			// If completely gray, return original pixel
			if(blendFactor <= 0.0f)
			{
				return pixel;
			}

			// Calculate luminance for recoloring
			float luminance = (r*0.299f+g*0.587f+b*0.114f)/255.0f;

			// Apply target color while preserving luminance
			float[] targetRGB = targetColor.getFloatRGB();
			int recoloredR = (int)(targetRGB[0]*luminance*255);
			int recoloredG = (int)(targetRGB[1]*luminance*255);
			int recoloredB = (int)(targetRGB[2]*luminance*255);

			// Clamp recolored values
			recoloredR = Math.min(255, Math.max(0, recoloredR));
			recoloredG = Math.min(255, Math.max(0, recoloredG));
			recoloredB = Math.min(255, Math.max(0, recoloredB));

			// Blend between original and recolored based on grayness
			int finalR = (int)(r*(1-blendFactor)+recoloredR*blendFactor);
			int finalG = (int)(g*(1-blendFactor)+recoloredG*blendFactor);
			int finalB = (int)(b*(1-blendFactor)+recoloredB*blendFactor);

			// Clamp final values
			finalR = Math.min(255, Math.max(0, finalR));
			finalG = Math.min(255, Math.max(0, finalG));
			finalB = Math.min(255, Math.max(0, finalB));

			return (alpha<<24)|(finalR<<16)|(finalG<<8)|finalB;
		}
		else
		{
			return pixel; // Keep transparent pixels as-is
		}
	}
}
