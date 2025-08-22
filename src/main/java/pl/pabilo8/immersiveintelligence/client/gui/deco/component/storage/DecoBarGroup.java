package pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoBase;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A group of DecoBars that can be drawn together.
 * This class allows for the management of multiple DecoBar instances,
 */
public class DecoBarGroup extends GuiComponentDecoBase<DecoBarGroup>
{
	private final List<DecoBar> bars = new ArrayList<>();
	protected ResLoc backgroundLocation, iconBackgroundLocation;

	public DecoBarGroup(int x, int y)
	{
		super(x, y);
		withBackgroundLocation(DecoTextures.RES_TEXTURES_DECO_COMPONENT_FRAME, DecoTextures.RES_TEXTURES_DECO_BAR_ICON_BACKGROUND);
	}

	public DecoBarGroup withBackgroundLocation(ResLoc backgroundLocation, ResLoc iconBackgroundLocation)
	{
		this.backgroundLocation = backgroundLocation;
		this.iconBackgroundLocation = iconBackgroundLocation;
		return this;
	}

	public DecoBarGroup withBar(Consumer<DecoBar> bar)
	{
		DecoBar newBar = new DecoBar(0, 0);
		bar.accept(newBar);
		bars.add(newBar);
		return this;
	}

	@Override
	protected boolean initialize()
	{
		if(!bars.isEmpty())
		{
			withSize(
					6+bars.stream().mapToInt(b -> b.width).sum(),
					bars.stream().mapToInt(b -> b.height).max().getAsInt()
			);
			int offset = 0;
			for(DecoBar bar : bars)
			{
				bar.x = x+offset;
				bar.y = y;
				bar.withBackgroundLocation(backgroundLocation, iconBackgroundLocation);
				offset += bar.width;
			}
			return true;
		}

		return false;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		//Bind the atlas texture for drawing
		bindAtlas();
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();

		//Draw bar backgrounds
		for(DecoBar bar : bars)
			bar.drawBarBackground(draw);

		//Draw common icon background
		TextureAtlasSprite iconBgSprite = ClientUtils.getSprite(iconBackgroundLocation);
		draw.drawConnectedColorRect(
				x-3, y-9, width, 18, IIColor.WHITE,
				32, 32, 4, 4,
				iconBgSprite.getMinU(), iconBgSprite.getMaxU(),
				iconBgSprite.getMinV(), iconBgSprite.getMaxV()
		);

		//Draw icons
		for(DecoBar bar : bars)
			bar.drawIcon(draw, false);

		draw.finish();

		//Disable textures for drawing the gradient bars
		GlStateManager.disableTexture2D();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);

		//Draw gradient bars
		draw = IIDrawUtils.startColored();
		for(DecoBar bar : bars)
			bar.drawBarGradient(draw);
		draw.finish();


		//Re-enable textures
		GlStateManager.enableTexture2D();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();


	}

	@Override
	public List<String> getTooltip()
	{
		List<String> tooltip = super.getTooltip();
		for(DecoBar bar : bars)
			tooltip.addAll(bar.getTooltip());
		return tooltip;
	}

	@Override
	public void cleanup()
	{
		bars.forEach(GuiComponentDecoBase::cleanup);
	}
}
