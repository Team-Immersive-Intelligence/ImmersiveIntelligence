package pl.pabilo8.immersiveintelligence.client.gui.deco.component;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nullable;

/**
 * A class for displaying a single image in the Deco GUI system
 * with a default width and height of 16x16.
 *
 * @since 10.01.2025
 */
public class DecoImage extends GuiComponentDecoBase<DecoImage>
{
	@Nullable
	private ResourceLocation imageLocation;
	private boolean usesBlockAtlas;
	private TextureAtlasSprite sprite;

	public DecoImage(int x, int y)
	{
		super(x, y);
	}

	public DecoImage withImageLocation(@Nullable ResourceLocation imageLocation)
	{
		return withImageLocation(imageLocation, false);
	}

	public DecoImage withImageLocation(@Nullable ResourceLocation imageLocation, boolean usesBlockAtlas)
	{
		this.imageLocation = imageLocation;
		this.usesBlockAtlas = usesBlockAtlas;
		return this;
	}

	@Override
	protected boolean initialize()
	{
		if(imageLocation!=null)
		{
			sprite = ClientUtils.getSprite(imageLocation);
			return true;
		}
		return false;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		GlStateManager.enableBlend();
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();
		if(usesBlockAtlas)
		{
			bindAtlas();
			draw.drawTexColorRect(x, y, width, height, IIColor.WHITE, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
		}
		else
		{
			IIClientUtils.bindTexture(imageLocation);
			draw.drawTexColorRect(x, y, width, height, IIColor.WHITE, 0, 1, 0, 1);
		}
		draw.finish();
		GlStateManager.disableBlend();
	}

	@Override
	public void cleanup()
	{

	}
}