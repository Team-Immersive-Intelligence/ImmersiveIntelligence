package pl.pabilo8.immersiveintelligence.client.gui.elements.buttons;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author Pabilo8
 * @since 07-07-2019
 */
public class GuiButtonItemAdvanced extends GuiButtonIE
{
	ItemStack item;
	int offset_x, offset_y;

	// TODO: 12.09.2021 remove
	public GuiButtonItemAdvanced(int buttonId, int x, int y, int w, int h, String texture, int u, int v, ItemStack stack, int offset_x, int offset_y)
	{
		this(buttonId, x, y, w, h, new ResourceLocation(texture), u, v, stack, offset_x, offset_y);
	}

	public GuiButtonItemAdvanced(int buttonId, int x, int y, int w, int h, ResourceLocation texture, int u, int v, ItemStack stack, int offset_x, int offset_y)
	{
		super(buttonId, x, y, w, h, stack.getDisplayName(), texture.toString(), u, v);
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
		if(this.visible)
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = canClick(mc, mouseX, mouseY);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			this.drawTexturedModalRect(x, y, texU, texV, width, height);
			this.mouseDragged(mc, mouseX, mouseY);
			if(!item.isEmpty())
			{
				GlStateManager.pushMatrix();
				this.zLevel = 200.0F;
				RenderItem itemRender = mc.getRenderItem();
				itemRender.zLevel = 200.0F;
				itemRender.renderItemAndEffectIntoGUI(item, x+1+offset_x, y+1+offset_y);
				this.zLevel = 0.0F;
				itemRender.zLevel = 0.0F;
				RenderHelper.disableStandardItemLighting();
				GlStateManager.popMatrix();
			}
		}
	}
}
