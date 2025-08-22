package pl.pabilo8.immersiveintelligence.client.gui.deco.component.button;

import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IILogger;

/**
 * A standard inventory tab of the Deco GUI system
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 12.01.2025
 */
public class DecoTab extends DecoButton
{
	public DecoTab()
	{
		super(0, 0);
		withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_TAB);
		withPadding(6, 1, 4, 3);
	}

	@Deprecated
	public DecoTab(int buttonId, int x, int y, int w, int h, int u, int v, ResourceLocation texture, String hoverText)
	{
		this();
		this.x = x;
		this.y = y;
		withSize(w, h);
		withIcon(texture);
		withTranslatedTooltip(hoverText);
	}

	public DecoTab withLink(IIGUI link)
	{
		this.withOnPressed((gui, mouseButton, mx, my) -> {
			if(mouseButton!=MouseButton.LEFT)
				return false;

			DecoGui<?, ?> parent = gui.getParentGui();
			if(parent==null)
				IILogger.error("DecoTab: No parent gui found, cannot link to "+link);
			else
				parent.changeGUI(link);
			return true;
		});
		return this;
	}
}
