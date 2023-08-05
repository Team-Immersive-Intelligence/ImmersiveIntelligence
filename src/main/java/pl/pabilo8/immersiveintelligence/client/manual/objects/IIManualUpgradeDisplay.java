package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8
 * @since 22.05.2022
 */
public class IIManualUpgradeDisplay extends IIManualObject
{
	MachineUpgrade upgrade;

	public IIManualUpgradeDisplay(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
	}

	@Override
	public void postInit(IIManualPage page)
	{
		super.postInit(page);

		//set the upgrade
		upgrade = MachineUpgrade.getUpgradeByID(dataSource.getString("upgrade"));
		if(upgrade==null)
			height = 0;
	}

	//--- Rendering, Reaction ---//

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		super.drawButton(mc, mx, my, partialTicks);

		if(upgrade==null)
			return;

		ClientUtils.drawColouredRect(x-2, y, width+2, height, 0xaa000000);
		GlStateManager.color(1f,1f,1f,1f);

		mc.getTextureManager().bindTexture(upgrade.getIcon());
		GlStateManager.enableBlend();
		ClientUtils.drawTexturedRect(x, y+2, 16, 16, 0d, 1d, 0d, 1d);

		manual.fontRenderer.setUnicodeFlag(true);
		manual.fontRenderer.drawSplitString(TextFormatting.ITALIC+"Upgrade applied using a wrench.", x+18, y+2, 100,manual.getHighlightColour());
	}

	@Override
	protected int getDefaultHeight()
	{
		return 20;
	}

	@Override
	public void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button)
	{

	}

	@Override
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		// TODO: 01.12.2022 link
		if(hovered)
			return Collections.singletonList("");
		return null;
	}
}
