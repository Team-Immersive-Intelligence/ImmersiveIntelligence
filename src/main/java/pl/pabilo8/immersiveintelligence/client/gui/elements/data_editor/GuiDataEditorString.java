package pl.pabilo8.immersiveintelligence.client.gui.elements.data_editor;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.client.gui.elements.GuiMultiLineTextField;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.io.IOException;

/**
 * @author Pabilo8
 * @since 07.09.2021
 */
public class GuiDataEditorString extends GuiDataEditor<DataTypeString>
{
	private GuiTextField valueEdit;
	private String valueLabel;
	private final FontRenderer renderer = ClientUtils.mc().fontRenderer;

	public GuiDataEditorString(int buttonId, DataTypeString dataType)
	{
		super(buttonId, dataType);
	}

	@Override
	public void init()
	{
		super.init();

		this.valueLabel = I18n.format(IIReference.DESCRIPTION_KEY+"variable_value");
		this.valueEdit = new GuiMultiLineTextField(0, renderer,
				x+2, y+12, width-4, height-20);
		this.valueEdit.setFocused(true);
		this.valueEdit.setText(dataType.toString());
		this.valueEdit.updateCursorCounter();
		this.valueEdit.setMaxStringLength(512);
	}

	@Override
	public DataTypeString createType()
	{
		return new DataTypeString();
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

		this.valueEdit.mouseClicked(mouseX, mouseY, 0);
		return super.mousePressed(mc, mouseX, mouseY);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException
	{
		this.valueEdit.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public DataTypeString outputType()
	{
		dataType.value = this.valueEdit.getText();
		return dataType;
	}

	@Override
	public boolean isFocused()
	{
		return valueEdit.isFocused();
	}
}
