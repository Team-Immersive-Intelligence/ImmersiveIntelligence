package pl.pabilo8.immersiveintelligence.client.manual.pages;

import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualUtils;
import blusunrize.lib.manual.gui.GuiManual;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;

/**
 * @author Pabilo8
 * @since 07.08.2021
 */
public class IIManualPageBulletCore extends IIManualPageBulletComponent
{
	private final AmmoCore core;

	public IIManualPageBulletCore(ManualInstance manual, AmmoCore ammoCore)
	{
		super(manual, ammoCore.getName(), ammoCore.getMaterial().getExampleStack(), ComponentRole.GENERAL_PURPOSE, ammoCore.getDensity());
		core = ammoCore;
		this.text = "bullet_core."+name;
	}

	@Override
	int renderInfo(GuiManual gui, int x, int y)
	{
		//TODO: 27.05.2024 reimplement
//		ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD+I18n.format("ie.manual.entry.bullet_components.dmg_mod", damageMod), x-12, y+20, 140, manual.getTextColour());
//		ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD+I18n.format("ie.manual.entry.bullet_components.pen_mod", penMod), x-12, y+30, 140, manual.getTextColour());
//		ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD+I18n.format("ie.manual.entry.bullet_components.blast_mod", blastMod), x-12, y+40, 140, manual.getTextColour());

		ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD+I18n.format("ie.manual.entry.bullet_components.density", density), x, y, 120, manual.getTextColour());

		return 50;
	}
}
