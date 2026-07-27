package pl.pabilo8.immersiveintelligence.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nullable;

/**
 * Inventory button opening the faction invitation list.
 *
 * @author Pabilo8
 * @since 22.07.2026
 */
public class GuiButtonFactionInvitations extends GuiButton
{
	@Nullable
	private final GuiContainerCreative creative;

	public GuiButtonFactionInvitations(int x, int y, @Nullable GuiContainerCreative creative)
	{
		super(1109, x, y, 16, 16, "");
		this.creative = creative;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		//Only show in inventory tab in creative mode
		if(creative!=null)
			visible = creative.getSelectedTabIndex()==CreativeTabs.INVENTORY.getTabIndex();

		if(visible)
		{
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.translate(0, 0, 1000);
			GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
			GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			IIClientUtils.bindAtlas();
			this.hovered = mouseX >= this.x&&mouseY >= this.y&&mouseX < this.x+this.width&&mouseY < this.y+this.height;

			//Hover animation
			IIDrawUtils.startTextured()
					.drawTexSprite(x, y, width, height, isMouseOver()?DecoTextures.ICON_INVENTORY_FACTION_INVITES_ACTIVE: DecoTextures.ICON_INVENTORY_FACTION_INVITES)
					.finish();
			if(isMouseOver())
				drawCenteredString(IIClientUtils.fontRegular, I18n.format(IIReference.GUI_TOOLTIP_KEY+"button.factions"), x+8, y+12, IIColor.WHITE.getPackedRGB());

			GlStateManager.popMatrix();

		}
	}
}
