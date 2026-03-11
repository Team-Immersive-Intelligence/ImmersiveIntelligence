package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 22.05.2022
 */
public class IIManualWIPNotice extends IIManualObject
{
	public static final ResLoc TEXTURE_WIP_WARNING = IIReference.RES_TEXTURES_MANUAL.with("wip_content", ResLoc.EXT_PNG);
	private final boolean brief;

	public IIManualWIPNotice(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
		this.brief = nbt.getBoolean("brief");
		this.height = brief?24: 52;
	}

	//--- Rendering, Reaction ---//

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		super.drawButton(mc, mx, my, partialTicks);

		ClientUtils.drawColouredRect(x-2, y, width+2, 20, 0x99000000);
		if(!brief)
			ClientUtils.drawColouredRect(x-2, y+20, width+2, height-20, 0x44000000);
		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.enableBlend();

		IIClientUtils.bindTexture(TEXTURE_WIP_WARNING);
		IIDrawUtils.startTextured()
				.drawTexRect(x, y+2, 16, 16, 0, 1, 0, 1)
				.finish();

		manual.fontRenderer.setUnicodeFlag(true);
		manual.fontRenderer.drawSplitString(TextFormatting.ITALIC+I18n.format("ie.manual.entry.wip_warning0"), x+18, y+2, 100, manual.getHighlightColour());
		if(!brief)
			manual.fontRenderer.drawSplitString(TextFormatting.ITALIC+I18n.format("ie.manual.entry.wip_warning1"), x+2, y+2+20, 116, manual.getTextColour());
	}

	@Override
	protected int getDefaultHeight()
	{
		return 52;
	}

	@Override
	public void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button)
	{

	}

	@Override
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		if(hovered)
			return Collections.emptyList();
		return null;
	}
}
