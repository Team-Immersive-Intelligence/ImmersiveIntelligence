package pl.pabilo8.immersiveintelligence.client.manual.objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.List;

/**
 * @author Pabilo8
 * @since 22.05.2022
 */
public class IIManualHorizontalLine extends IIManualObject
{
	//--- Setup ---//

	public IIManualHorizontalLine(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
	}

	//--- Rendering, Reaction ---//

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		GlStateManager.disableTexture2D();
		drawTexturedModalRect(x, y-2, 0, 0, width, 1);
		GlStateManager.enableTexture2D();
	}

	@Override
	protected int getDefaultHeight()
	{
		return 4;
	}

	@Override
	public void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button)
	{

	}

	@Override
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		return null;
	}
}
