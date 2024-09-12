package pl.pabilo8.immersiveintelligence.client.gui.elements.data_editor;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8
 * @since 07.09.2021
 */
public class GuiDataEditorBoolean extends GuiDataEditor<DataTypeBoolean>
{
	private static final String TEX = ImmersiveIntelligence.MODID+":textures/gui/data_input_machine_editing.png";
	private boolean editedstate = false;
	private GuiButtonState buttonTrue, buttonFalse;
	private String valueLabel;
	private final FontRenderer renderer = ClientUtils.mc().fontRenderer;

	public GuiDataEditorBoolean(int buttonId, DataTypeBoolean dataType)
	{
		super(buttonId, dataType);
	}

	@Override
	public void init()
	{
		Minecraft mc = Minecraft.getMinecraft();

		this.valueLabel = I18n.format(IIReference.DESCRIPTION_KEY+"variable_value");
		editedstate = dataType.value;

		buttonTrue = addButton(new GuiButtonState(0, x+16, y+12, 48, 12, I18n.format(IIReference.DATA_KEY+"datatype.boolean.true"), editedstate, TEX, 0, 234, 0));
		buttonFalse = addButton(new GuiButtonState(1, x+48+16, y+12, 48, 12, I18n.format(IIReference.DATA_KEY+"datatype.boolean.false"), !editedstate, TEX, 0, 234, 0));

		buttonTrue.setHoverOffset(48, 0);
		buttonFalse.setHoverOffset(48, 0);
		buttonTrue.textOffset = new int[]{(buttonTrue.width/2)-(mc.fontRenderer.getStringWidth(buttonTrue.displayString)/2), buttonTrue.height/2-4};
		buttonFalse.textOffset = new int[]{(buttonFalse.width/2)-(mc.fontRenderer.getStringWidth(buttonFalse.displayString)/2), buttonFalse.height/2-4};
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		super.drawButton(mc, mouseX, mouseY, partialTicks);
		renderer.drawString(valueLabel, x+2, y+2, IIReference.COLOR_H1.getPackedRGB(), false);

	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		if(buttonTrue.isMouseOver())
		{
			buttonTrue.mousePressed(mc, mouseX, mouseY);
			editedstate = true;
			buttonTrue.state = true;
			buttonFalse.state = false;
			return true;
		}
		else if(buttonFalse.isMouseOver())
		{
			buttonFalse.mousePressed(mc, mouseX, mouseY);
			editedstate = false;
			buttonTrue.state = false;
			buttonFalse.state = true;
			return true;
		}

		return super.mousePressed(mc, mouseX, mouseY);
	}

	@Override
	public DataTypeBoolean createType()
	{
		return new DataTypeBoolean();
	}

	@Override
	public DataTypeBoolean outputType()
	{
		dataType.value = editedstate;
		return dataType;
	}
}
