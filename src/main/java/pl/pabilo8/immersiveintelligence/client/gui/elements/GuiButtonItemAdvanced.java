package pl.pabilo8.immersiveintelligence.client.gui.elements;

import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

/**
 * @author Pabilo8
 * @since 07-07-2019
 */
public class GuiButtonItemAdvanced extends GuiButtonIE
{
	ItemStack item;
	int offset_x, offset_y;

	public GuiButtonItemAdvanced(int buttonId, int x, int y, int w, int h, String texture, int u, int v, ItemStack stack, int offset_x, int offset_y)
	{
		super(buttonId, x, y, w, h, "", texture, u, v);
		this.item = stack;
		this.offset_x = offset_x;
		this.offset_y = offset_y;
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		super.drawButton(mc, mouseX, mouseY, partialTicks);

		if(!item.isEmpty())
		{
			this.zLevel = 200.0F;
			RenderItem itemRender = mc.getRenderItem();
			itemRender.zLevel = 200.0F;
			itemRender.renderItemAndEffectIntoGUI(item, x+1+offset_x, y+1+offset_y);
			this.zLevel = 0.0F;
			itemRender.zLevel = 0.0F;
			RenderHelper.disableStandardItemLighting();
		}
	}
}
