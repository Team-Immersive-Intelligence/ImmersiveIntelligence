package pl.pabilo8.immersiveintelligence.client.util.font;

import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * @author Pabilo8
 * @since 27.09.2022
 */
public class IIFontRendererCustomGlyphs extends IIFontRenderer
{
	public IIFontRendererCustomGlyphs(ResourceLocation res)
	{
		super(res);
	}

	@Nonnull
	public ResourceLocation getGlyphLocation()
	{
		return new ResourceLocation(this.locationFontTexture.getResourceDomain(), this.locationFontTexture.getResourcePath().replace(".png", ".bin"));
	}

	@Override
	protected void readGlyphSizes()
	{
		IResource iresource = null;
		try
		{
			iresource = getResource(getGlyphLocation());
			iresource.getInputStream().read(this.glyphWidth);
		} catch(IOException ioexception)
		{
			super.readGlyphSizes();
		} finally {IOUtils.closeQuietly(iresource);}
	}
}
