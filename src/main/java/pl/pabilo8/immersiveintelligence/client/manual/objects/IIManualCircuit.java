package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.lib.manual.gui.GuiClickableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit.Circuits;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
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
					return false;
				gui.previousSelectedEntry.push(gui.getSelectedEntry());
				gui.setSelectedEntry(circuit.getName());
				gui.page = selectedOption;
				gui.initGui();
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
		if(IIMath.isPointInRectangle(this.x+list.width, this.y, this.x+list.width+8, this.y+list.height, mouseX, mouseY))
		{
			int maxOffset = ReflectionHelper.getPrivateValue(GuiClickableList.class, list, "maxOffset");
			int clickedOffset = (int)MathHelper.clamp((float)(mouseY-this.y)/(list.height)*1.25*maxOffset, 0, maxOffset);
			ReflectionHelper.setPrivateValue(GuiClickableList.class, list, clickedOffset, "offset");
			return true;
		}

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
		this.mousePressed(ClientUtils.mc(), lastX, lastY);
	}

	@Override
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		return null;
	}
}
