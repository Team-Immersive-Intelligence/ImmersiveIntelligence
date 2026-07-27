package pl.pabilo8.immersiveintelligence.client.gui.deco.component.button;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A group of DecoTabs displayed in a row or column, depending on alignment.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 27.07.2026
 * @since 24.09.2025
 */
public class DecoTabGroup extends DecoComponent<DecoTabGroup>
{
	private final List<DecoTab> tabs = new ArrayList<>();
	private final Map<DecoTab, DecoPanel> tabPanels = new LinkedHashMap<>();
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
		tab.withSize(24, 24)
				.withPadding(5, 3, 5, 2)
				.pack();
		if(horizontal)
			tab.withSize(tab.width, this.height);
		else
			tab.withSize(this.width, tab.height);

		tabs.add(tab);
		children.add(tab);
		return this;
	}

	/**
	 * Adds a tab associated with a panel. The first pair is selected by default;
	 * pressing another tab hides the previous panel and displays the selected one.
	 *
	 * @param tab   tab used to select the panel
	 * @param panel panel controlled by the tab
	 * @return this
	 */
	public DecoTabGroup withTab(DecoTab tab, DecoPanel panel)
	{
		withTab(tab);
		tabPanels.put(tab, panel);

		boolean selected = tabPanels.size()==1;
		tab.withSelected(selected)
				.withOnPressed((gui, button, mouseX, mouseY) -> {
					if(button!=MouseButton.LEFT)
						return false;
					selectTab(tab);
					return true;
				});
		panel.enabled = panel.visible = selected;
		return this;
	}

	private void selectTab(DecoTab selectedTab)
	{
		this.tabPanels.forEach((tab, panel) -> {
			boolean selected = tab==selectedTab;
			tab.withSelected(selected);
			panel.enabled = panel.visible = selected;
		});
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
			this.width = tabs.isEmpty()?0: offsetX-x-spacing;
			this.height = maxH;
		}
		else
		{
			this.width = maxW;
			this.height = tabs.isEmpty()?0: offsetY-y-spacing;
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
