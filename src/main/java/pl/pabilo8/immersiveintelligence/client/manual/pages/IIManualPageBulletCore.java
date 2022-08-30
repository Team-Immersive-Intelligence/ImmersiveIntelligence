package pl.pabilo8.immersiveintelligence.client.manual.pages;

import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualUtils;
import blusunrize.lib.manual.gui.GuiManual;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoCore;

/**
 * @author Pabilo8
 * @since 07.08.2021
 */
public class IIManualPageBulletCore extends IIManualPageBulletComponent
{
	float damageMod, penMod, blastMod;

	public IIManualPageBulletCore(ManualInstance manual, IAmmoCore coreType)
	{
		super(manual, coreType.getName(), coreType.getMaterial().getExampleStack(), EnumComponentRole.GENERAL_PURPOSE, coreType.getDensity());
		this.text = "bullet_core."+name;
		damageMod = coreType.getDamageModifier();
		penMod = coreType.getPenetrationHardness();
		blastMod = coreType.getExplosionModifier();
	}

	@Override
	int renderInfo(GuiManual gui, int x, int y)
	{
		ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD+I18n.format("ie.manual.entry.bullet_components.dmg_mod", damageMod), x, y+20, 120, manual.getTextColour());
		ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD+I18n.format("ie.manual.entry.bullet_components.pen_mod", penMod), x, y+30, 120, manual.getTextColour());
		ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD+I18n.format("ie.manual.entry.bullet_components.blast_mod", blastMod), x, y+40, 120, manual.getTextColour());

		ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD+I18n.format("ie.manual.entry.bullet_components.density", density), x, y, 120, manual.getTextColour());

		return 50;
	}
}
