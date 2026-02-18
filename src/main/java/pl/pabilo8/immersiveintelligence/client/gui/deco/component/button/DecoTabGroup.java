package pl.pabilo8.immersiveintelligence.client.gui.deco.component.button;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.ArrayList;
import java.util.List;

/**
 * A group of DecoTabs displayed in a row or column, depending on alignment.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.09.2025
 */
public class DecoTabGroup extends DecoComponent<DecoTabGroup>
{
	private final List<DecoTab> tabs = new ArrayList<>();
	private ResourceLocation background = DecoTextures.COMPONENT_TAB;
	private boolean horizontal;
	private int spacing = 0;

	public DecoTabGroup(int x, int y)
	{
		super(x, y);
	}

	public DecoTabGroup withHorizontalAlignment(boolean horizontal)
	{
		this.horizontal = horizontal;
		if(this.background==DecoTextures.COMPONENT_TAB)
			this.background = DecoTextures.COMPONENT_TAB_VERTICAL;
		return this;
	}

	public DecoTabGroup withBackground(ResourceLocation background)
	{
		this.background = background;
		return this;
	}

	public DecoTabGroup withSpacing(int spacing)
	{
		this.spacing = spacing;
		return this;
	}

	public DecoTabGroup withTab(DecoTab tab)
	{
		tab.withSize(24, 24).pack();
		if(horizontal)
			tab.withSize(tab.width, this.height);
		else
			tab.withSize(this.width, tab.height);

		tabs.add(tab);
		children.add(tab);
		return this;
	}

	@Override
	protected boolean initialize()
	{
		int offsetX = x, offsetY = y;
		int maxW = 0, maxH = 0;
		for(DecoTab tab : tabs)
		{
			tab.withPosition(offsetX, offsetY).withBackground(ResLoc.of(this.background));
			if(horizontal)
			{
				offsetX += tab.width+spacing;
				maxH = Math.max(maxH, tab.height);
			}
			else
			{
				offsetY += tab.height+spacing;
				maxW = Math.max(maxW, tab.width);
			}
		}
		if(horizontal)
		{
			this.width = offsetX-x-spacing+(tabs.isEmpty()?0: tabs.get(tabs.size()-1).width);
			this.height = maxH;
		}
		else
		{
			this.width = maxW;
			this.height = offsetY-y-spacing+(tabs.isEmpty()?0: tabs.get(tabs.size()-1).height);
		}
		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		bindAtlas();
		TextureAtlasSprite sprite = ClientUtils.getSprite(background);
		IIDrawUtils.startTextured()
				.drawTexColorRect(0, 0, 64, 64, IIColor.WHITE,
						sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV())
				.finish();
	}

	@Override
	public List<String> getTooltip()
	{
		for(DecoTab tab : tabs)
			if(tab.isMouseOver())
				return tab.getTooltip();
		return super.getTooltip();
	}

	@Override
	public void cleanup()
	{
		tabs.forEach(DecoTab::cleanup);
	}
}
