package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.lib.manual.ManualUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8
 * @since 22.05.2022
 */
public class IIManualImage extends IIManualObject
{
	ResourceLocation res = null;
	String alt = "", tooltip = "";
	float[] uv = new float[]{0, 0, 1, 1};
	boolean frame = false;

	//--- Setup ---//

	public IIManualImage(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
	}

	@Override
	public void postInit(IIManualPage page)
	{
		super.postInit(page);

		if(dataSource.hasKey("img"))
		{
			//image
			dataSource.checkSetString("img", s -> res = new ResourceLocation(s+".png"));

			if(res!=null&&dataSource.hasKey("uv", EasyNBT.TAG_LIST))
			{
				NBTTagList uv = dataSource.getList("uv", EasyNBT.TAG_FLOAT);
				this.uv = new float[]{uv.getFloatAt(0), uv.getFloatAt(1), uv.getFloatAt(2), uv.getFloatAt(3)};
			}

			dataSource.checkSetBoolean("frame", b -> frame = b);
		}

		dataSource.checkSetString("alt", b -> alt = b);
		dataSource.checkSetString("tooltip", b -> tooltip = b);
	}

	//--- Rendering, Reaction ---//

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		super.drawButton(mc, mx, my, partialTicks);

		if(res!=null)
		{
			if(frame)
			{
				gui.drawGradientRect(x-2, y-2, x+width+2, y+height+2, 0xffeaa74c, 0xfff6b059);
				gui.drawGradientRect(x-1, y-1, x+width+1, y+height+1, 0xffc68e46, 0xffbe8844);
			}

			IIClientUtils.bindTexture(res);
			GlStateManager.color(1, 1, 1, 1);
			GlStateManager.enableBlend();
			ManualUtils.drawTexturedRect(x, y, width, height, uv[0], uv[2], uv[1], uv[3]);
		}
		else if(!alt.isEmpty())
		{
			drawString(manual.fontRenderer, alt, x, y, 0xffeaa74c);
		}

	}

	@Override
	protected int getDefaultHeight()
	{
		return height;
	}

	@Override
	public void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button)
	{

	}

	@Override
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		if(hovered&&!tooltip.isEmpty())
			return Collections.singletonList(tooltip);
		return null;
	}
}
