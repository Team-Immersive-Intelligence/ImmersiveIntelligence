package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 22.05.2022
 */
public class IIManualTable extends IIManualObject
{
	String[][] table = null;
	boolean frame;
	int textHeight;
	int[] bars;

	//--- Setup ---//

	public IIManualTable(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
	}

	@Override
	public void postInit(IIManualPage page)
	{
		super.postInit(page);

		//lambda superiority mk.1
		table = dataSource
				//get "list" tag from source
				.getList("table", EasyNBT.TAG_LIST).tagList.stream()
				.map(t -> ((NBTTagList)t))
				.map(t -> t.tagList.stream().map(NBTBase::toString).toArray(String[]::new))
				.toArray(String[][]::new);

		frame = !dataSource.hasKey("frame")||dataSource.getBoolean("frame");

		textHeight = manual.fontRenderer.FONT_HEIGHT+6;
		if(table!=null)
			calculateBars();
	}

	//--- Rendering, Reaction ---//

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		super.drawButton(mc, mx, my, partialTicks);

		if(table==null)
			return;

		manual.fontRenderer.setUnicodeFlag(true);
		int col = manual.getHighlightColour()|0xff000000;
		gui.drawGradientRect(x, y+textHeight-2, x+120, y+textHeight-1, col, col);
		int[] textOff = new int[bars!=null?bars.length: 0];

		if(bars!=null)
		{
			int xx = x;
			for(int i = 0; i < bars.length; i++)
			{
				xx += bars[i]+4;
				xx += 4;
				textOff[i] = xx;
			}
		}

		int yOff = 0;
		for(String[] line : table)
			if(line!=null)
			{
				int height = 0;
				for(int j = 0; j < line.length; j++)
					if(line[j]!=null)
					{
						int xx = textOff.length > 0&&j > 0?textOff[j-1]: x;
						int w = Math.max(10, 120-(j > 0?textOff[j-1]-x: 0));
						ManualUtils.drawSplitString(manual.fontRenderer, line[j], xx, y+textHeight+yOff, w, manual.getTextColour());
						int l = manual.fontRenderer.listFormattedStringToWidth(line[j], w).size();
						if(l > height)
							height = l;
					}

				if(frame)
				{
					float scale = .5f;
					GlStateManager.scale(1, scale, 1);
					int barHeight = (int)((y+textHeight+yOff+height*manual.fontRenderer.FONT_HEIGHT)/scale);
					gui.drawGradientRect(x, barHeight, x+120, barHeight+1,
							manual.getTextColour()|0xff000000, manual.getTextColour()|0xff000000);
					GlStateManager.scale(1, 1/scale, 1);
				}

				yOff += height*(manual.fontRenderer.FONT_HEIGHT+1);
			}

		gui.drawGradientRect(x, y+textHeight-4, x-3, y+textHeight+yOff, manual.getHighlightColour(), manual.getHighlightColour());

		if(bars!=null)
			for(int i = 0; i < bars.length; i++)
				gui.drawGradientRect(textOff[i]-4, y+textHeight-4, textOff[i]-3, y+textHeight+yOff, col, col);
	}

	@Override
	protected int getDefaultHeight()
	{
		return 36;
	}

	@Override
	public void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button)
	{

	}

	@Nullable
	@Override
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		return null;
	}

	//--- Private Methods ---//

	private void calculateBars()
	{
		bars = new int[1];
		for(String[] strings : table)
		{
			if(strings.length-1 > bars.length)
			{
				int[] newBars = new int[strings.length-1];
				System.arraycopy(bars, 0, newBars, 0, bars.length);
				bars = newBars;
			}

			for(int j = 0; j < strings.length-1; j++)
			{
				int rw = manual.fontRenderer.getStringWidth(strings[j]);
				if(rw > bars[j])
					bars[j] = rw;
			}
		}
	}
}
