package pl.pabilo8.immersiveintelligence.client.gui;

import blusunrize.immersiveengineering.client.ClientProxy;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;

/**
 * @author Pabilo8
 * @since 09-07-2019
 */
public class GuiPrintedPage extends GuiScreen
{
	private static final String page_texture = ImmersiveIntelligence.MODID+":textures/gui/printed_page.png";

	private final String text;

	public GuiPrintedPage(EntityPlayer player, ItemStack book)
	{

		text = ItemNBTHelper.getString(book, "text");

	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(page_texture);
		int pageImageWidth = 149;
		int i = (this.width-pageImageWidth)/2;
		int pageImageHeight = 196;
		int j = (this.height-pageImageHeight)/2;
		this.drawTexturedModalRect(i, j, 0, 0, 192, 192);


		String text_processed = text.replace("<br>", "\n");
		text_processed = text_processed.replace("\\n", "\n");

		ClientProxy.itemFont.drawSplitString(text_processed, i+8, j+24, pageImageWidth-8, Utils.COLOR_H1);
	}
}
