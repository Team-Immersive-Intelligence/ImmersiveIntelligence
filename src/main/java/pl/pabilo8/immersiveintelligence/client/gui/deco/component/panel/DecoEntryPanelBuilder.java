package pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel;

import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoFrame;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 25.02.2025
 **/
public class DecoEntryPanelBuilder<TYPE> extends DecoEntryPanel<TYPE>
{
	private final Map<String, Function<DecoEntryPanelBuilder<TYPE>, DecoComponent<?>>> components = new HashMap<>();
	private final Map<String, DecoComponent<?>> childrenMap = new HashMap<>();
	private final Map<String, DecoLabel> labels = new HashMap<>();
	private int paddingX, paddingY;
	private BiConsumer<TYPE, DecoEntryPanelBuilder<TYPE>> elementApplyMethod;

	public DecoEntryPanelBuilder()
	{
	}

	@Override
	protected void initializeChildren()
	{
		super.withPadding(paddingX, paddingY);
		childrenMap.clear();

		components.forEach((name, function) -> {
			DecoComponent<?> component = function.apply(this);
			this.addComponent(component);
			this.childrenMap.put(name, component);
		});
		for(DecoLabel label : labels.values())
			this.addLabel(label);
	}

	@Override
	protected void applyElementToChildren(TYPE type)
	{
		if(elementApplyMethod!=null)
			elementApplyMethod.accept(type, this);
	}

	@Override
	public DecoEntryPanelBuilder<TYPE> withElementTooltip(Function<TYPE, String> onTooltip)
	{
		super.withElementTooltip(onTooltip);
		return this;
	}

	//--- Settings ---//

	@Override
	@SuppressWarnings("unchecked")
	public DecoEntryPanelBuilder<TYPE> withSize(int width, int height)
	{
		return (DecoEntryPanelBuilder<TYPE>)super.withSize(width, height);
	}

	@Override
	@SuppressWarnings("unchecked")
	public DecoEntryPanelBuilder<TYPE> withWidth(int width)
	{
		return (DecoEntryPanelBuilder<TYPE>)super.withWidth(width);
	}

	@Override
	@SuppressWarnings("unchecked")
	public DecoEntryPanelBuilder<TYPE> withHeight(int height)
	{
		return (DecoEntryPanelBuilder<TYPE>)super.withHeight(height);
	}

	@Override
	@SuppressWarnings("unchecked")
	public DecoEntryPanelBuilder<TYPE> withTemplate(DecoComponentTemplate<DecoPanel> template)
	{
		return (DecoEntryPanelBuilder<TYPE>)super.withTemplate(template);
	}

	@Override
	public DecoEntryPanelBuilder<TYPE> withPadding(int x, int y)
	{
		this.paddingX = x;
		this.paddingY = y;
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public DecoEntryPanelBuilder<TYPE> withFrame(@Nullable DecoFrame frame)
	{
		return (DecoEntryPanelBuilder<TYPE>)super.withFrame(frame);
	}

	@Override
	@SuppressWarnings("unchecked")
	public DecoEntryPanelBuilder<TYPE> withBackgroundMask(ResLoc backgroundMask)
	{
		return (DecoEntryPanelBuilder<TYPE>)super.withBackgroundMask(backgroundMask);
	}

	@Override
	@SuppressWarnings("unchecked")
	public DecoEntryPanelBuilder<TYPE> withBackground(ResLoc background)
	{
		return (DecoEntryPanelBuilder<TYPE>)super.withBackground(background);
	}

	//--- Components ---//

	public DecoEntryPanelBuilder<TYPE> withComponent(String name, DecoComponent<?> component)
	{
		return withComponent(name, p -> component);
	}

	public DecoEntryPanelBuilder<TYPE> withComponent(Function<DecoEntryPanelBuilder<TYPE>, DecoComponent<?>> component)
	{
		return withComponent(getGenericComponentName(), component);
	}

	public DecoEntryPanelBuilder<TYPE> withComponent(String name, Function<DecoEntryPanelBuilder<TYPE>, DecoComponent<?>> component)
	{
		this.components.put(name, component);
		return this;
	}

	//--- Labels ---//

	public DecoEntryPanelBuilder<TYPE> withLabel(String name, DecoLabel label)
	{
		this.labels.put(name, label);
		return this;
	}

	//--- Type Update Event and Component/Label Accessors ---//

	public DecoEntryPanelBuilder<TYPE> withElementApplyMethod(BiConsumer<TYPE, DecoEntryPanelBuilder<TYPE>> method)
	{
		this.elementApplyMethod = method;
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T extends DecoComponent<? super T>> T component(String name, @SuppressWarnings("unused") Class<T> klass)
	{
		return (T)this.childrenMap.getOrDefault(name, null);
	}

	public DecoLabel label(String name)
	{
		return this.labels.getOrDefault(name, null);
	}

	//--- Utils ---//

	@Nonnull
	private String getGenericComponentName()
	{
		return String.valueOf(components.size());
	}

}
