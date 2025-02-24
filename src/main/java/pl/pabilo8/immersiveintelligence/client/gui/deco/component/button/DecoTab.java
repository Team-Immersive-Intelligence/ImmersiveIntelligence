package pl.pabilo8.immersiveintelligence.client.gui.deco.component.button;

import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * A standard inventory tab of the Deco GUI system
 *
 * @author Pabilo8
 * @since 12.01.2025
 */
public class DecoTab extends DecoButton
{
	public DecoTab()
	{
		super(0, 0);
		this.backgroundLocation = IIReference.RES_TEXTURES_DECO_COMPONENT_TAB;
		padding = new int[]{6, 1, 4, 3};
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

	public DecoTab withLink(IIGuiList link)
	{
		this.withOnPressed((gui, mx, my) -> {
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
