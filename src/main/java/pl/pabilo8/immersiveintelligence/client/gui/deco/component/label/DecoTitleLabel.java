package pl.pabilo8.immersiveintelligence.client.gui.deco.component.label;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nonnull;

/**
 * A label with a background drawn behind it.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 12.01.2025
 **/
public class DecoTitleLabel extends DecoLabel
{
	protected ResLoc backgroundLocation;

	/**
	 * @param fontRenderer The font renderer to use
	 * @param x            The x position of the label
	 * @param y            The y position of the label
	 */
	public DecoTitleLabel(FontRenderer fontRenderer, int x, int y)
	{
		super(fontRenderer, x, y);
	}

	public DecoTitleLabel withBackgroundLocation(ResLoc backgroundLocation)
	{
		this.backgroundLocation = backgroundLocation;
		return this;
	}

	@Override
	public void drawLabel(@Nonnull Minecraft mc, int mouseX, int mouseY)
	{
		//Draw background
		TextureAtlasSprite sprite = ClientUtils.getSprite(backgroundLocation);
		ClientUtils.bindAtlas();
		IIDrawUtils.startTexturedColored()
				.drawTexColorRect(x-8, y-4, 16, 16, IIColor.WHITE,
						sprite.getMinU(), sprite.getInterpolatedU(8),
						sprite.getMinV(), sprite.getInterpolatedV(8)
				)
				.drawConnectedColorRect(x+8, y-4, width-16, 16, IIColor.WHITE,
						16, 16,
						sprite.getInterpolatedU(4), sprite.getInterpolatedU(12),
						sprite.getMinV(), sprite.getInterpolatedV(8)
				)
				.drawTexColorRect(x+width-8, y-4, 16, 16, IIColor.WHITE,
						sprite.getInterpolatedU(8), sprite.getMaxU(),
						sprite.getMinV(), sprite.getInterpolatedV(8)
				)
				.finish();

		//Draw rest of the label
		super.drawLabel(mc, mouseX, mouseY);
	}
}
