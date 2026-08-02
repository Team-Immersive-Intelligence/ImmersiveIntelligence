package pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel;

import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoScrolledCollection;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoFrame;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRenderer;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 25.02.2025
 **/
public class DecoEntryPanelBuilder<TYPE> extends DecoEntryPanel<TYPE>
{
	private final Map<String, Function<DecoEntryPanelBuilder<TYPE>, DecoComponent<?>>> components = new LinkedHashMap<>();
	private final Map<String, Function<DecoEntryPanelBuilder<TYPE>, DecoLabel>> labelFactories = new LinkedHashMap<>();
	private final Map<String, DecoComponent<?>> childrenMap = new HashMap<>();
	private final Map<String, DecoLabel> labelsMap = new HashMap<>();
	private final Map<TYPE, DecoEntryPanelBuilder<TYPE>> panelCache = new HashMap<>();
	private final boolean cachedEntry;

	private int paddingX, paddingY;
	private int refreshInterval, displayTicks;
	private boolean layoutDirty;
	private BiConsumer<TYPE, DecoEntryPanelBuilder<TYPE>> elementApplyMethod;
	private Function<TYPE, String> elementTooltip;
	@Nullable
	private DecoFrame panelFrame;
	@Nullable
	private ResLoc panelBackground = DecoTextures.BG_PAPER;
	@Nullable
	private ResLoc panelBackgroundMask = DecoTextures.TEMPLATE_PAPER;

	public DecoEntryPanelBuilder()
	{
		this.cachedEntry = false;
	}

	private DecoEntryPanelBuilder(DecoEntryPanelBuilder<TYPE> template)
	{
		this.cachedEntry = true;
		this.components.putAll(template.components);
		this.labelFactories.putAll(template.labelFactories);
		this.paddingX = template.paddingX;
		this.paddingY = template.paddingY;
		this.elementApplyMethod = template.elementApplyMethod;
		this.elementTooltip = template.elementTooltip;
		this.panelFrame = template.panelFrame;
		this.panelBackground = template.panelBackground;
		this.panelBackgroundMask = template.panelBackgroundMask;
		this.height = template.height;
		this.width = -1;

		super.withBackground(panelBackground);
		super.withBackgroundMask(panelBackgroundMask);
		super.withFrame(panelFrame);
		if(elementTooltip!=null)
			super.withElementTooltip(elementTooltip);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	protected void initializeChildren()
	{
		super.withPadding(paddingX, paddingY);
		childrenMap.clear();
		labelsMap.clear();

		components.forEach((name, function) -> {
			DecoComponent component = function.apply(this);
			this.addComponent(component);
			this.childrenMap.put(name, component);
		});
		labelFactories.forEach((name, function) -> {
			DecoLabel label = function.apply(this);
			this.addLabel(label);
			this.labelsMap.put(name, label);
		});
	}

	@Override
	protected void applyElementToChildren(TYPE type)
	{
		if(elementApplyMethod!=null)
			elementApplyMethod.accept(type, this);
	}

	@Override
	public int displayElement(TYPE type, int width, IIFontRenderer font, int mouseX, int mouseY, float partialTicks, boolean heightProbe)
	{
		if(cachedEntry)
			return super.displayElement(type, width, font, mouseX, mouseY, partialTicks, heightProbe);

		if(this.width!=width)
		{
			this.width = width;
			clearCache(false);
		}

		DecoEntryPanelBuilder<TYPE> panel = panelCache.get(type);
		if(panel==null)
		{
			panel = new DecoEntryPanelBuilder<>(this);
			panel.bindCollection(getCurrentList());
			panel.parentGui = getCurrentList()==null?getParentGui(): getCurrentList().getParentGui();
			panel.applyElement(type);
			panelCache.put(type, panel);
		}
		else
			panel.applyElement(type);

		return panel.displayElement(type, width, font, mouseX, mouseY, partialTicks, heightProbe);
	}

	@Override
	public void drawElementUpperLayer(TYPE type, int width, IIFontRenderer font, int mouseX, int mouseY, float partialTicks)
	{
		if(cachedEntry)
		{
			super.drawElementUpperLayer(type, width, font, mouseX, mouseY, partialTicks);
			return;
		}

		DecoEntryPanelBuilder<TYPE> panel = panelCache.get(type);
		if(panel!=null)
			panel.drawElementUpperLayer(type, width, font, mouseX, mouseY, partialTicks);
	}

	@Override
	public List<String> getTooltip()
	{
		if(cachedEntry)
			return super.getTooltip();

		for(DecoEntryPanelBuilder<TYPE> panel : panelCache.values())
			if(panel.isMouseOver())
				return panel.getTooltip();
		return Collections.emptyList();
	}

	@Override
	public boolean ownsComponent(DecoComponent<?> component)
	{
		return cachedEntry?this==component: panelCache.containsValue(component);
	}

	@Override
	public void bindCollection(DecoScrolledCollection<?, TYPE> collection)
	{
		super.bindCollection(collection);
		if(!cachedEntry)
			clearCache(false);
	}

	@Nullable
	@Override
	public DecoEntryPanel<TYPE> getElementPanel(TYPE type)
	{
		if(cachedEntry)
			return super.getElementPanel(type);
		return panelCache.get(type);
	}

	@Override
	public boolean onDisplayTick()
	{
		if(cachedEntry)
			return false;

		if(layoutDirty)
		{
			layoutDirty = false;
			return true;
		}
		if(refreshInterval <= 0||++displayTicks < refreshInterval)
			return false;

		clearCache(false);
		return true;
	}

	@Override
	public void onEntriesChanged(Collection<TYPE> entries)
	{
		if(cachedEntry||panelCache.isEmpty())
			return;

		Iterator<Map.Entry<TYPE, DecoEntryPanelBuilder<TYPE>>> iterator = panelCache.entrySet().iterator();
		while(iterator.hasNext())
		{
			Map.Entry<TYPE, DecoEntryPanelBuilder<TYPE>> cached = iterator.next();
			if(!entries.contains(cached.getKey()))
			{
				cleanupCachedPanel(cached.getValue());
				iterator.remove();
			}
		}
	}

	@Override
	public void cleanupDisplay()
	{
		if(cachedEntry)
			cleanup();
		else
			clearCache(false);
	}

	@Override
	public DecoEntryPanelBuilder<TYPE> withElementTooltip(Function<TYPE, String> onTooltip)
	{
		this.elementTooltip = onTooltip;
		super.withElementTooltip(onTooltip);
		invalidateDefinition();
		return this;
	}

	/**
	 * Clears all cached entry panels. Their component trees will be rebuilt lazily on the next layout or draw pass.
	 *
	 * @return this
	 */
	public DecoEntryPanelBuilder<TYPE> refreshCache()
	{
		clearCache(true);
		return this;
	}

	/**
	 * Sets a periodic cache refresh interval.
	 *
	 * @param displayTicks number of collection display passes between cache refreshes; zero disables periodic refresh
	 * @return this
	 */
	public DecoEntryPanelBuilder<TYPE> withRefreshInterval(int displayTicks)
	{
		this.refreshInterval = Math.max(0, displayTicks);
		this.displayTicks = 0;
		return this;
	}

	//--- Settings ---//

	@Override
	public DecoEntryPanelBuilder<TYPE> withSize(int width, int height)
	{
		return withWidth(width).withHeight(height);
	}

	@Override
	public DecoEntryPanelBuilder<TYPE> withWidth(int width)
	{
		super.withWidth(width);
		invalidateDefinition();
		return this;
	}

	@Override
	public DecoEntryPanelBuilder<TYPE> withHeight(int height)
	{
		super.withHeight(height);
		invalidateDefinition();
		return this;
	}

	@Override
	public DecoEntryPanelBuilder<TYPE> withTemplate(DecoComponentTemplate<DecoPanel> template)
	{
		super.withTemplate(template);
		invalidateDefinition();
		return this;
	}

	@Override
	public DecoEntryPanelBuilder<TYPE> withPadding(int x, int y)
	{
		this.paddingX = x;
		this.paddingY = y;
		invalidateDefinition();
		return this;
	}

	@Override
	public DecoEntryPanelBuilder<TYPE> withFrame(@Nullable DecoFrame frame)
	{
		this.panelFrame = frame;
		super.withFrame(frame);
		invalidateDefinition();
		return this;
	}

	@Override
	public DecoEntryPanelBuilder<TYPE> withBackgroundMask(@Nullable ResLoc backgroundMask)
	{
		this.panelBackgroundMask = backgroundMask;
		super.withBackgroundMask(backgroundMask);
		invalidateDefinition();
		return this;
	}

	@Override
	public DecoEntryPanelBuilder<TYPE> withBackground(@Nullable ResLoc background)
	{
		this.panelBackground = background;
		super.withBackground(background);
		invalidateDefinition();
		return this;
	}

	//--- Components ---//

	public DecoEntryPanelBuilder<TYPE> withComponent(String name, Supplier<? extends DecoComponent<?>> component)
	{
		return withComponent(name, p -> component.get());
	}

	public DecoEntryPanelBuilder<TYPE> withComponent(Function<DecoEntryPanelBuilder<TYPE>, DecoComponent<?>> component)
	{
		return withComponent(getGenericComponentName(), component);
	}

	public DecoEntryPanelBuilder<TYPE> withComponent(String name, Function<DecoEntryPanelBuilder<TYPE>, DecoComponent<?>> component)
	{
		this.components.put(name, component);
		invalidateDefinition();
		return this;
	}

	//--- Labels ---//

	public DecoEntryPanelBuilder<TYPE> withLabel(String name, Supplier<? extends DecoLabel> label)
	{
		return withLabel(name, p -> label.get());
	}

	public DecoEntryPanelBuilder<TYPE> withLabel(String name, Function<DecoEntryPanelBuilder<TYPE>, DecoLabel> label)
	{
		this.labelFactories.put(name, label);
		invalidateDefinition();
		return this;
	}

	//--- Type Update Event and Component/Label Accessors ---//

	public DecoEntryPanelBuilder<TYPE> withElementApplyMethod(BiConsumer<TYPE, DecoEntryPanelBuilder<TYPE>> method)
	{
		this.elementApplyMethod = method;
		invalidateDefinition();
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T extends DecoComponent<? super T>> T component(String name, @SuppressWarnings("unused") Class<T> klass)
	{
		return (T)this.childrenMap.getOrDefault(name, null);
	}

	public DecoLabel label(String name)
	{
		return this.labelsMap.getOrDefault(name, null);
	}

	//--- Utils ---//


	private void clearCache(boolean markLayoutDirty)
	{
		panelCache.values().forEach(this::cleanupCachedPanel);
		panelCache.clear();
		displayTicks = 0;
		layoutDirty |= markLayoutDirty;
	}

	private void cleanupCachedPanel(DecoEntryPanelBuilder<TYPE> panel)
	{
		if(panel.getParentGui()!=null)
			panel.getParentGui().releaseFocusWithin(panel);
		panel.cleanup();
	}

	private void invalidateDefinition()
	{
		if(panelCache!=null&&!cachedEntry&&!panelCache.isEmpty())
			refreshCache();
	}

	@Nonnull
	private String getGenericComponentName()
	{
		return String.valueOf(components.size());
	}
}
