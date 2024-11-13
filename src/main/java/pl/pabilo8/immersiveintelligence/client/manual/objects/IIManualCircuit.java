package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.lib.manual.gui.GuiClickableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit.Circuits;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8
 * @since 02.11.2022
 */
public class IIManualCircuit extends IIManualObject
{
	@Nonnull
	private Circuits circuit;
	private GuiClickableList list;

	//--- Setup ---//

	public IIManualCircuit(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
	}

	@Override
	public void postInit(IIManualPage page)
	{
		super.postInit(page);
		this.circuit = dataSource.getEnum("circuit", Circuits.class);

		String[] functionNames = Arrays.stream(this.circuit.getFunctions())
				.map(name -> I18n.format("datasystem.immersiveintelligence.function."+name.toLowerCase()))
				.map(name -> "- "+name)
				.toArray(String[]::new);
		this.list = new GuiClickableList(gui, 0, x, y, width-6, height, 1f, 1, functionNames)
		{
			@Override
			public boolean mousePressed(Minecraft mc, int mx, int my)
			{
				boolean b = super.mousePressed(mc, mx, my);
				if(selectedOption==-1)
					return b;
				return b;
			}
		};
	}

	@Override
	protected int getDefaultHeight()
	{
		return 100;
	}

	//--- Rendering, Reaction ---//


	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		return list.mousePressed(mc, mouseX, mouseY);
	}

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		super.drawButton(mc, mx, my, partialTicks);
		list.drawButton(mc, mx, my, partialTicks);
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
