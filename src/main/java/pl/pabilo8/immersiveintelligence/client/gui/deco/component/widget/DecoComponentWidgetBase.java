package pl.pabilo8.immersiveintelligence.client.gui.deco.component.widget;

import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 09.06.2025
 */
@ParametersAreNonnullByDefault
public abstract class DecoComponentWidgetBase<W extends DecoComponentWidgetBase<? super W>> extends DecoPanel
{
	public DecoComponentWidgetBase()
	{
		super(0, 0);
	}

	public abstract String getName();

	public abstract DecoTab provideTab();

	public int getWidgetWidth()
	{
		return width;
	}
}
