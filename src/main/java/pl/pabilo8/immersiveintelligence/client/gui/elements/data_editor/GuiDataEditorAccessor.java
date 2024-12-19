package pl.pabilo8.immersiveintelligence.client.gui.elements.data_editor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeAccessor;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonDataLetterList;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonDataLetterList.ArrowsAlignment;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8
 * @since 07.09.2021
 */
public class GuiDataEditorAccessor extends GuiDataEditor<DataTypeAccessor>
{
	private GuiButtonDataLetterList buttonVariable;
	private char c;

	public GuiDataEditorAccessor(int buttonId, DataTypeAccessor dataType)
	{
		super(buttonId, dataType);
		c = dataType.variable;
	}

	@Override
	public void init()
	{
		super.init();

		this.buttonVariable = addButton(new GuiButtonDataLetterList(buttonList.size(), x+width-130, y+20, false, c, ArrowsAlignment.LEFT));
	}

	@Override
	public DataTypeAccessor createType()
	{
		return new DataTypeAccessor();
	}

	@Override
	public void update()
	{

		super.update();
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		super.drawButton(mc, mouseX, mouseY, partialTicks);
		mc.fontRenderer.drawString(I18n.format(IIReference.DESCRIPTION_KEY+"variable"), x+2, y+10, 0x0a0a0a, false);

	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		if(buttonVariable.mousePressed(mc, mouseX, mouseY))
		{
			c = buttonVariable.selectedEntry;
			return true;
		}
		return super.mousePressed(mc, mouseX, mouseY);
	}

	@Override
	public DataTypeAccessor outputType()
	{
		dataType.variable = c;
		return dataType;
	}

}
