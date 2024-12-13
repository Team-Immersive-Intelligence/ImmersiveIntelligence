package pl.pabilo8.immersiveintelligence.client.gui.elements.data_editor;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 07.09.2021
 */
public class GuiDataEditorItemStack extends GuiDataEditor<DataTypeItemStack>
{
	private GuiTextField metaEdit, countEdit;

	// TODO: 27.12.2021 implement it in a way extending Slot while making handling it not as painful as it would be using the "default method"
	private ItemStack scanned;
	private String valueLabel;
	private final FontRenderer renderer = ClientUtils.mc().fontRenderer;
	private final RenderItem renderItem = ClientUtils.mc().getRenderItem();

	public GuiDataEditorItemStack(int buttonId, DataTypeItemStack dataType)
	{
		super(buttonId, dataType);
		scanned = dataType.value;

	}

	@Override
	public void init()
	{
		super.init();

		this.valueLabel = I18n.format(IIReference.DESCRIPTION_KEY+"variable_value");

		this.metaEdit = new GuiTextField(0, renderer,
				x+40, y+2+20+10, width-42, 12);
		this.metaEdit.setFocused(false);
		this.metaEdit.setText(String.valueOf(scanned.getMetadata()));
		this.metaEdit.updateCursorCounter();

		this.countEdit = new GuiTextField(0, renderer,
				x+40, y+32+16, width-42, 12);
		this.countEdit.setFocused(false);
		this.countEdit.setText(String.valueOf(scanned.getCount()));
		this.countEdit.updateCursorCounter();
	}

	@Override
	public DataTypeItemStack createType()
	{
		return new DataTypeItemStack();
	}

	@Override
	public void update()
	{
		super.update();
		this.metaEdit.updateCursorCounter();
		this.countEdit.updateCursorCounter();
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		super.drawButton(mc, mouseX, mouseY, partialTicks);

		ClientUtils.bindTexture("immersiveintelligence:textures/gui/emplacement_icons.png");
		drawTexturedModalRect(x+(width/2)-9, y+7, 0, 50, 18, 18);

		//renderer.drawString(valueLabel, x+2, y+2, IIReference.COLOR_H1.getPackedRGB(), false);

		//renderer.drawString(scanned.getDisplayName(), x+2, y+2+10+10, Lib.COLOUR_I_ImmersiveOrange, false);
		renderer.drawString("Item:", x+2, y+12, IIReference.COLOR_H1.getPackedRGB(), false);
		renderer.drawString("Meta:", x+2, y+2+20+12, IIReference.COLOR_H1.getPackedRGB(), false);
		renderer.drawString("Count:", x+2, y+32+16+2, IIReference.COLOR_H1.getPackedRGB(), false);

		this.countEdit.drawTextBox();
		this.metaEdit.drawTextBox();

		GlStateManager.pushMatrix();
		RenderHelper.enableGUIStandardItemLighting();
		renderItem.renderItemIntoGUI(scanned, x+(width/2)-8, y+8);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popMatrix();

	}

	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException
	{
		this.metaEdit.textboxKeyTyped(typedChar, keyCode);
		this.countEdit.textboxKeyTyped(typedChar, keyCode);
		//this.valueEdit.setValidator(s -> s.matches("^[\\d\\(\\)\\-+]+$"));
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public boolean isFocused()
	{
		return metaEdit.isFocused()||countEdit.isFocused();
	}


	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		if(IIMath.isPointInRectangle(x+(width/2)-8, y+10, x+(width/2)-8+16, y+10+16, mouseX, mouseY))
		{
			ItemStack underMouse = mc.player.inventory.getItemStack();
			this.scanned = underMouse.copy();
			this.init();
			return true;
		}
		else
		{
			this.metaEdit.mouseClicked(mouseX, mouseY, 0);
			this.countEdit.mouseClicked(mouseX, mouseY, 0);
			return super.mousePressed(mc, mouseX, mouseY);
		}
	}

	@Override
	public DataTypeItemStack outputType()
	{
		scanned.setCount(getFieldValue(this.countEdit, scanned.getCount()));
		scanned.setItemDamage(getFieldValue(this.metaEdit, scanned.getMetadata()));

		dataType.value = scanned.copy();


		return dataType;
	}

	public int getFieldValue(GuiTextField field, int defaultValue)
	{
		try
		{
			return Integer.parseInt(field.getText());
		} catch(NumberFormatException ignored)
		{
			return defaultValue;
		}
	}

	@Override
	public void getTooltip(ArrayList<String> tooltip, int mx, int my)
	{
		if(!scanned.isEmpty()&&IIMath.isPointInRectangle(x+(width/2)-8, y+10, x+(width/2)-8+16, y+10+16, mx, my))
			tooltip.addAll(scanned.getTooltip(ClientUtils.mc().player, ClientUtils.mc().gameSettings.advancedItemTooltips?TooltipFlags.ADVANCED: TooltipFlags.NORMAL));

	}
}
