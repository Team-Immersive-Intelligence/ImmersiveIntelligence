package pl.pabilo8.immersiveintelligence.client.gui.deco.component.widget;

import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 09.06.2025
 */
public abstract class GuiComponentWidgetBase<W extends GuiComponentWidgetBase<? super W>> extends DecoPanel
{
	public GuiComponentWidgetBase()
	{
		super(0, 0);
	}

	public abstract DecoTab provideTab();
}
