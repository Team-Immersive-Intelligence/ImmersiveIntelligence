package pl.pabilo8.immersiveintelligence.client.gui.elements.data_editor;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 07.09.2021
 */
public class GuiDataEditorInteger extends GuiDataEditor<DataTypeInteger>
{
	private GuiButtonIE buttonInt, buttonHex, buttonBin;
	private GuiTextField valueEdit;
	private String valueLabel;
	private final FontRenderer renderer = ClientUtils.mc().fontRenderer;
	private int mode = 10;

	public GuiDataEditorInteger(int buttonId, DataTypeInteger dataType)
	{
		super(buttonId, dataType);
	}

	@Override
	public void init()
	{
		super.init();

		this.valueLabel = I18n.format(IIReference.DESCRIPTION_KEY+"variable_value");
		this.valueEdit = new GuiTextField(0, renderer,
				x+2, y+12, width-4, 20);
		this.valueEdit.setFocused(true);
		this.valueEdit.setText(dataType.toString());
		this.valueEdit.updateCursorCounter();

		this.buttonInt = addButton(new GuiButtonIE(1, x+2, y+32+2, 12, 12, "D",
				ImmersiveIntelligence.MODID+":textures/gui/data_input_machine_editing.png", 96, 234)
				.setHoverOffset(12, 0));
		this.buttonHex = addButton(new GuiButtonIE(1, x+2+12, y+32+2, 12, 12, "H",
				ImmersiveIntelligence.MODID+":textures/gui/data_input_machine_editing.png", 96, 234)
				.setHoverOffset(12, 0));
		this.buttonBin = addButton(new GuiButtonIE(1, x+2+24, y+32+2, 12, 12, "B",
				ImmersiveIntelligence.MODID+":textures/gui/data_input_machine_editing.png", 96, 234)
				.setHoverOffset(12, 0));
	}

	@Override
	public DataTypeInteger createType()
	{
		return new DataTypeInteger();
	}

	@Override
	public void update()
	{
		super.update();
		this.valueEdit.updateCursorCounter();
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		super.drawButton(mc, mouseX, mouseY, partialTicks);
		renderer.drawString(valueLabel, x+2, y+2, IIReference.COLOR_H1.getPackedRGB(), false);

		this.valueEdit.drawTextBox();
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		if(buttonInt.isMouseOver())
		{
			valueEdit.setText(Integer.toString(getFieldValue(), 10));
			mode = 10;
			return true;
		}
		else if(buttonHex.isMouseOver())
		{
			valueEdit.setText(Integer.toString(getFieldValue(), 16));
			mode = 16;
			return true;
		}
		else if(buttonBin.isMouseOver())
		{
			valueEdit.setText(Integer.toString(getFieldValue(), 2));
			mode = 2;
			return true;
		}
		else
		{
			this.valueEdit.mouseClicked(mouseX, mouseY, 0);
		}
		return super.mousePressed(mc, mouseX, mouseY);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException
	{
		this.valueEdit.textboxKeyTyped(typedChar, keyCode);
		//this.valueEdit.setValidator(s -> s.matches("^[\\d\\(\\)\\-+]+$"));
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public DataTypeInteger outputType()
	{
		dataType.value = getFieldValue();
		return dataType;
	}

	@Override
	public boolean isFocused()
	{
		return valueEdit.isFocused();
	}

	public int getFieldValue()
	{
		try
		{
			return Integer.parseInt(valueEdit.getText(), mode);
		} catch(NumberFormatException ignored)
		{
			return 0;
		}
	}

	@Override
	public void getTooltip(ArrayList<String> tooltip, int mx, int my)
	{
		// TODO: 19.01.2022 translation 
		if(buttonInt.isMouseOver())
			tooltip.add("Decimal");
		else if(buttonHex.isMouseOver())
			tooltip.add("Hexadecimal");
		else if(buttonBin.isMouseOver())
			tooltip.add("Binary");
	}
}
