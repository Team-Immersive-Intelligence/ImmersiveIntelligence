package pl.pabilo8.immersiveintelligence.client.gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerAmmunitionCrate;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class GuiAmmunitionCrate extends GuiIEContainerBase
{
	public boolean upgraded;
	public GuiAmmunitionCrate(InventoryPlayer inventoryPlayer, TileEntityAmmunitionCrate tile)
	{
		super(new ContainerAmmunitionCrate(inventoryPlayer, tile));
		upgraded=tile.hasUpgrade(IIContent.UPGRADE_MG_LOADER);
		this.ySize = 222;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRenderer.drawString(I18n.format("tile."+ImmersiveIntelligence.MODID+".metal_device.ammunition_crate.name"), 8, 6, 0x0a0a0a);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/gui/ammunition_crate.png");
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		if(upgraded)
		{
			this.drawTexturedModalRect(guiLeft+176, guiTop, 176, 0, 49, 133);
			Utils.drawStringCentered(fontRenderer, I18n.format(CommonProxy.INFO_KEY+"machineupgrade.mg_loader.gui_tooltip"), guiLeft+176,guiTop+6,49,0,0x0a0a0a);
		}
	}
}
