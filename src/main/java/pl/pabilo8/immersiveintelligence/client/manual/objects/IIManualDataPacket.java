package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringUtils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Displays the contents of a Data Packet similar to how a Debugger would do it.
 *
 * @author Pabilo8
 * @since 03.11.2022
 */
public class IIManualDataPacket extends IIManualObject
{
	DataPacket packet;
	List<String> text;
	boolean obstructed;

	//--- Setup ---//

	public IIManualDataPacket(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
	}

	@Override
	public void postInit(IIManualPage page)
	{
		super.postInit(page);

		packet = new DataPacket().fromNBT(dataSource.getCompound("data"));
		text = compilePacketString();
		obstructed = false;
	}

	private List<String> compilePacketString()
	{
		//gets variables in format l:{Value:0}
		return minimizeArrays(
				packet.variables.entrySet().stream()
						.map(entry -> String.format("%s %s = %s",
								entry.getValue().getTypeColor().getHexCol(entry.getValue().getName()),
								entry.getKey(),
								entry.getValue().toString().replace(
												"\n", "\n"+StringUtils.repeat(' ', (entry.getValue().getName().length()+7)))
										.trim()
						))
						.map(s -> s.split("\n"))
						.toArray(String[][]::new)
		);
	}

	/**
	 * Joins 2d string arrays into a single dimension one
	 */
	private List<String> minimizeArrays(String[][] array)
	{
		ArrayList<String> joined = new ArrayList<>();
		for(String[] strings : array)
			joined.addAll(Arrays.asList(strings));
		joined.sort(Collections.reverseOrder());
		return joined;
	}

	@Override
	protected int getDefaultHeight()
	{
		return 22;
	}

	//--- Rendering, Reaction ---//

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		super.drawButton(mc, mx, my, partialTicks);
		ClientUtils.drawColouredRect(x-2, y, width, height, 0xaa000000);
		ClientUtils.drawColouredRect(x+width, y, 2, height, 0xaa000000);

		for(int i = 0; i < height; i += 4)
			ClientUtils.drawColouredRect(x+width-2, y+i, 2, 2, 0xbb000000);

		boolean unicodeFlag = manual.fontRenderer.getUnicodeFlag();

		manual.fontRenderer.setUnicodeFlag(true);
		int yy = y-7;
		for(String s : text)
			manual.fontRenderer.drawString(s, x, yy += manual.fontRenderer.FONT_HEIGHT, 0xffffff, true);
		manual.fontRenderer.setUnicodeFlag(unicodeFlag);
	}

	@Override
	public void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button)
	{

	}

	@Override
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		return (hovered&&obstructed)?text: null;
	}
}
