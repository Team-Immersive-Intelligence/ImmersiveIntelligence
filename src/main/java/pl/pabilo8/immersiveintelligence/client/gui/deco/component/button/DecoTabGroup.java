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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A group of DecoTabs displayed in a row or column, depending on alignment.
 * <p>
 * Tabs added with a panel or selection action are managed tabs: the group owns their selected state,
 * switches associated panels and can be selected programmatically. Plain {@link #withTab(DecoTab)}
 * tabs retain their own press handlers and are only laid out by the group.
 * </p>
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 30.07.2026
 * @since 24.09.2025
 */
public class DecoTabGroup extends DecoComponent<DecoTabGroup>
{
	private final List<DecoTab> tabs = new ArrayList<>();
	private final Map<DecoTab, DecoPanel> tabPanels = new LinkedHashMap<>();
	private final Map<DecoTab, Runnable> tabActions = new LinkedHashMap<>();
	private ResourceLocation background = DecoTextures.COMPONENT_TAB;
	private boolean horizontal;
	private int spacing = 0;
	private int fixedTabWidth = -1;
	@Nullable
	private DecoTab selectedTab;

	public DecoTabGroup(int x, int y)
	{
		super(x, y);
	}

	public DecoTabGroup withHorizontalAlignment(boolean horizontal)
	{
		this.horizontal = horizontal;
		if(this.background==DecoTextures.COMPONENT_TAB)
			this.background = DecoTextures.COMPONENT_TAB_VERTICAL;
		this.initialized = false;
		return this;
	}

	public DecoTabGroup withBackground(ResourceLocation background)
	{
		this.background = background;
		this.initialized = false;
		return this;
	}

	public DecoTabGroup withSpacing(int spacing)
	{
		this.spacing = spacing;
		this.initialized = false;
		return this;
	}

	/**
	 * Forces each horizontally arranged tab to use the supplied width instead of its packed text width.
	 */
	public DecoTabGroup withTabWidth(int width)
	{
		this.fixedTabWidth = Math.max(1, width);
		if(horizontal)
			tabs.forEach(tab -> tab.withWidth(this.fixedTabWidth));
		this.initialized = false;
		return this;
	}

	/**
	 * Adds a layout-only tab. Its existing press handler and selected state remain caller-managed.
	 */
	public DecoTabGroup withTab(DecoTab tab)
	{
		tab.withSize(24, 24)
				.withPadding(5, 3, 5, 2)
				.pack();
		if(horizontal)
		{
			if(fixedTabWidth > 0)
				tab.withWidth(fixedTabWidth);
			tab.withHeight(this.height);
		}
		else
			tab.withSize(this.width, tab.height);

		tabs.add(tab);
		children.add(tab);
		this.initialized = false;
		return this;
	}

	/**
	 * Adds a managed tab that runs an action when selected.
	 */
	public DecoTabGroup withTab(DecoTab tab, Runnable onSelected)
	{
		return withManagedTab(tab, null, onSelected);
	}

	/**
	 * Adds a tab associated with a panel. The first managed tab is selected by default;
	 * pressing another tab hides the previous panel and displays the selected one.
	 *
	 * @param tab   tab used to select the panel
	 * @param panel panel controlled by the tab
	 * @return this
	 */
	public DecoTabGroup withTab(DecoTab tab, DecoPanel panel)
	{
		return withManagedTab(tab, panel, null);
	}

	/**
	 * Adds a panel-backed managed tab and runs an additional action after it is selected.
	 */
	public DecoTabGroup withTab(DecoTab tab, DecoPanel panel, Runnable onSelected)
	{
		return withManagedTab(tab, panel, onSelected);
	}

	private DecoTabGroup withManagedTab(DecoTab tab, @Nullable DecoPanel panel, @Nullable Runnable onSelected)
	{
		withTab(tab);
		if(panel!=null)
			tabPanels.put(tab, panel);
		if(onSelected!=null)
			tabActions.put(tab, onSelected);

		tab.withOnPressed((gui, button, mouseX, mouseY) -> {
			if(button!=MouseButton.LEFT)
				return false;
			selectTab(tab, true);
			return true;
		});

		if(selectedTab==null)
			selectTab(tab, false);
		else
		{
			tab.withSelected(false);
			if(panel!=null)
				panel.setActive(false);
		}
		return this;
	}

	/**
	 * Selects a managed tab and runs its selection action.
	 */
	public boolean selectTab(DecoTab tab)
	{
		return selectTab(tab, true);
	}

	/**
	 * Selects a managed tab.
	 *
	 * @param tab       tab to select
	 * @param runAction whether its optional selection action should be executed
	 * @return true when the tab belongs to this group and is managed
	 */
	public boolean selectTab(DecoTab tab, boolean runAction)
	{
		if(tab==null||(!tabPanels.containsKey(tab)&&!tabActions.containsKey(tab)))
			return false;

		selectedTab = tab;
		for(DecoTab groupedTab : tabs)
			if(tabPanels.containsKey(groupedTab)||tabActions.containsKey(groupedTab))
				groupedTab.withSelected(groupedTab==tab);
		tabPanels.forEach((groupedTab, panel) ->
				panel.setActive(groupedTab==tab));

		if(runAction)
		{
			Runnable action = tabActions.get(tab);
			if(action!=null)
				action.run();
		}
		return true;
	}

	public boolean selectTab(int tabIndex, boolean runAction)
	{
		if(tabIndex < 0||tabIndex >= tabs.size())
			return false;
		return selectTab(tabs.get(tabIndex), runAction);
	}

	@Nullable
	public DecoTab getSelectedTab()
	{
		return selectedTab;
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
