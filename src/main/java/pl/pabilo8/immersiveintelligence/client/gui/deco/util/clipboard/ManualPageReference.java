package pl.pabilo8.immersiveintelligence.client.gui.deco.util.clipboard;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.gui.GuiManual;
import net.minecraft.client.Minecraft;

/**
 * Stable link target for a page of the Engineer's Manual.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.09.2026
 */
public class ManualPageReference
{
	public final String entry;
	public final int page;
	public final String title;

	public ManualPageReference(String entry, int page, String title)
	{
		this.entry = entry;
		this.page = Math.max(0, page);
		this.title = title;
	}

	public void open()
	{
		GuiManual gui = ManualHelper.getManual().getGui();
		if(gui==null)
			gui = new GuiManual(ManualHelper.getManual(), ManualHelper.getManual().texture);
		gui.setSelectedEntry(entry);
		gui.page = page;
		Minecraft.getMinecraft().displayGuiScreen(gui);
	}
}
