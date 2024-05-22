package pl.pabilo8.immersiveintelligence.client.gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IICreativeTab;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;

/**
 * Displays category sub tabs in {@link IICreativeTab}
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author GabrielV (gabriel@iiteam.net)
 * @ii-approved 0.3.1
 * @since 21.05.2024
 */
public class GuiWidgetAustralianTabs extends GuiButton
{
	private final GuiContainerCreative gui;

	public GuiWidgetAustralianTabs(int x, int y, GuiContainerCreative gui)
	{
		super(1108, x, y, "");
		this.gui = gui;
		this.width = 28;
		this.height = (IICategory.values().length-1)*24;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		visible = gui.getSelectedTabIndex()==IIContent.II_CREATIVE_TAB.getTabIndex();
		if(!visible)
			return;
		GlStateManager.disableLighting();
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		GlStateManager.enableBlend();

		mc.getTextureManager().bindTexture(IICreativeTab.selectedCategory.getCreativeTabTexture());
		this.drawTexturedModalRect(x+28, y, 0, 0, gui.xSize, 16);

		IICategory hoveredTab = IICategory.NULL;
		int y = this.y;
		for(IICategory subTab : IICategory.values())
		{
			if(subTab==IICategory.NULL)
				continue;
			mc.getTextureManager().bindTexture(subTab.getCreativeTabTexture());

			//Whether the current sub tab is hovered over
			hovered = mouseX >= x&&mouseY >= y&&mouseX < x+28&&mouseY < y+24;
			if(hovered)
				hoveredTab = subTab;

			//Draw tab
			if(subTab==IICreativeTab.selectedCategory)
				this.drawTexturedModalRect(x, y, hovered?0: 28, 136, 28, 24);
			else
				this.drawTexturedModalRect(x, y, hovered?84: 56, 136, 28, 24);

			//Draw icon
			ClientUtils.bindAtlas();
			this.drawTexturedModalRect(x+7, y+3, ClientUtils.getSprite(subTab.getCreativeIconTexture()), 16, 16);
			y += 22;
		}

		//Draw tooltip
		if(hoveredTab!=IICategory.NULL)
			gui.drawHoveringText(I18n.format("itemGroup.ii."+hoveredTab.getName()), mouseX, mouseY);
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		if(super.mousePressed(mc, mouseX, mouseY))
		{
			IICreativeTab.selectedCategory = IICategory.values()[MathHelper.clamp(MathHelper.floor((mouseY-y)/22f)+1, 1, IICategory.values().length-1)];
			playPressSound(mc.getSoundHandler());
			gui.setCurrentCreativeTab(IIContent.II_CREATIVE_TAB);
			return false;
		}
		return false;
	}
}
