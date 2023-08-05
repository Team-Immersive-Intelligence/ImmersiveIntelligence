package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.lib.manual.ManualUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants.NBT;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.List;

/**
 * @author Pabilo8
 * @since 22.05.2022
 */
public class IIManualItemDisplay extends IIManualObject
{
	NonNullList<ItemStack> stacks;
	ItemStack highlighted = ItemStack.EMPTY;

	public IIManualItemDisplay(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
	}

	@Override
	public void postInit(IIManualPage page)
	{
		super.postInit(page);

		//create default
		stacks = NonNullList.create();
		//add a single item
		dataSource.checkSetCompound("item", nbt -> stacks.add(new ItemStack(nbt)));
		//add an item list
		if(dataSource.hasKey("items"))
			dataSource.streamList(NBTTagCompound.class, "items", NBT.TAG_COMPOUND)
					.map(ItemStack::new)
					.forEach(stacks::add);

	}

	//--- Rendering, Reaction ---//

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		super.drawButton(mc, mx, my, partialTicks);

		GlStateManager.enableRescaleNormal();
		RenderHelper.enableGUIStandardItemLighting();
		highlighted = ItemStack.EMPTY;
		int length = stacks.size();
		if(length > 0)
		{
			float scale = length > 8?1f: length > 3?1.5f: 2f;
			int line0 = (int)(7.5/scale);
			int line1 = line0-1;
			int lineSum = line0+line1;
			int lines = (length/lineSum*2)+(length%lineSum/line0)+(length%lineSum%line0 > 0?1: 0);
			float equalPerLine = length/(float)lines;
			line1 = (int)Math.floor(equalPerLine);
			line0 = (int)Math.ceil(equalPerLine);
			lineSum = line0+line1;
			int lastLines = length%lineSum;
			int lastLine = lastLines==line0?line0: lastLines==0?line1: lastLines%line0;
			GlStateManager.scale(scale, scale, scale);

			for(int line = 0; line < lines; line++)
			{
				int perLine = line==lines-1?lastLine: line%2==0?line0: line1;
				if(line==0&&perLine > length)
					perLine = length;
				int w2 = perLine*(int)(18*scale)/2;
				for(int i = 0; i < perLine; i++)
				{
					int item = line/2*lineSum+line%2*line0+i;
					if(item >= length)
						break;
					int xx = x+60-w2+(int)(i*18*scale);
					int yy = y+(lines < 2?4: 0)+line*(int)(18*scale);
					ManualUtils.renderItem().renderItemAndEffectIntoGUI(stacks.get(item), (int)(xx/scale), (int)(yy/scale));
					if(mx >= xx&&mx < xx+(16*scale)&&my >= yy&&my < yy+(16*scale))
						highlighted = stacks.get(item);
				}
			}
			GlStateManager.scale(1/scale, 1/scale, 1/scale);
		}
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();

	}

	@Override
	protected int getDefaultHeight()
	{
		return 33;
	}

	@Override
	public void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button)
	{

	}

	@Override
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		if(!highlighted.isEmpty())
			return this.gui.getItemToolTip(highlighted);
		return null;
	}
}
