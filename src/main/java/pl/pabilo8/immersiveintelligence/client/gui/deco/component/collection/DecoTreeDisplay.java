package pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection;

import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 08.09.2025
 */
public class DecoTreeDisplay extends DecoPanel
{
	public DecoTreeDisplay(int x, int y)
	{
		super(x, y);
		withBackground(DecoTextures.GUI_BG_DARK);
	}
}
