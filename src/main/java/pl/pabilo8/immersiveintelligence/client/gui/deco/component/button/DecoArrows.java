package pl.pabilo8.immersiveintelligence.client.gui.deco.component.button;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoTextBasedComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;

import java.util.function.Consumer;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 01.08.2025
 */
public class DecoArrows extends DecoTextBasedComponent<DecoArrows>
{
	private Consumer<Boolean> onArrow;

	public DecoArrows(int x, int y)
	{
		super(x, y);
		withSize(8, 16);
		withBackground(DecoTextures.COMPONENT_ARROWS);
		withOnPressed(this::onArrowsPressed);
	}

	public DecoArrows withOnArrow(Consumer<Boolean> onArrow)
	{
		this.onArrow = onArrow;
		return this;
	}

	private boolean onArrowsPressed(DecoArrows gui, MouseButton mouseButton, int mouseX, int mouseY)
	{
		if(onArrow==null)
			return false;

		if(mouseY >= y&&mouseY < y+8)
			onArrow.accept(true);
		else if(mouseX >= y+height-8&&mouseX < y+height)
			onArrow.accept(false);
		return true;
	}

	@Override
	protected boolean initialize()
	{
		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		bindAtlas();
		TextureAtlasSprite arrows = ClientUtils.getSprite(backgroundLocation);

		IIDrawUtils.startTexturedColored()
				.drawTexColorRect(
						x, y, 8, 6, backgroundColor,
						arrows.getMinU(), arrows.getInterpolatedU(8), arrows.getMinV(), arrows.getInterpolatedV(6)
				)
				.drawTexColorRect(
						x, y+height-6, 8, 6, backgroundColor,
						arrows.getMinU(), arrows.getInterpolatedU(8), arrows.getInterpolatedV(6), arrows.getInterpolatedV(12)
				)
				.finish();
	}

	@Override
	public void cleanup()
	{

	}

}
