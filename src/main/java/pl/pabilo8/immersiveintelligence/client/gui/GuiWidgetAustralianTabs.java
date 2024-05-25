package pl.pabilo8.immersiveintelligence.client.gui;

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
	private int lastMouseX, lastMouseY;
	private IICategory hoveredTab;

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
		this.drawTexturedModalRect(x+26, y-2, 0, 0, gui.xSize, 16);

		hoveredTab = IICategory.NULL;
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
				this.drawTexturedModalRect(x, y, 0, 136, 28, 24);
			else
				this.drawTexturedModalRect(x, y, hovered?28: 56, 136, 28, 24);

			//Draw icon
			this.drawTexturedModalRect(x+3, y, 84, 136, 24, 24);
			y += 22;
		}

		//Draw tooltip
		if(hoveredTab!=IICategory.NULL)
		{
			lastMouseX = mouseX;
			lastMouseY = mouseY;
			IIContent.II_CREATIVE_TAB.setHoveringText(this);
		}
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

	public void drawHovered()
	{
		gui.drawHoveringText(I18n.format("itemGroup.ii."+hoveredTab.getName()), lastMouseX, lastMouseY);
	}
}
