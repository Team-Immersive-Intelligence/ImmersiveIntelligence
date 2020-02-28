package pl.pabilo8.immersiveintelligence.client.gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityPacker;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerPacker;

import java.util.ArrayList;

/**
 * Created by Pabilo8 on 10-07-2019.
 */
public class GuiPacker extends GuiIEContainerBase
{
	public static final String texture_packer = ImmersiveIntelligence.MODID+":textures/gui/packer.png";
	TileEntityPacker tile;

	public GuiPacker(InventoryPlayer inventoryPlayer, TileEntityPacker tile)
	{
		super(new ContainerPacker(inventoryPlayer, tile));
		this.ySize = 184;
		this.tile = tile;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		Utils.drawStringCentered(fontRenderer, I18n.format("tile."+ImmersiveIntelligence.MODID+".metal_multiblock.packer.name"), 0, 4, getXSize(), 6, 0x0a0a0a);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(texture_packer);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		int stored = (int)(47*(tile.getEnergyStored(null)/(float)tile.getMaxEnergyStored(null)));
		ClientUtils.drawGradientRect(guiLeft+157, guiTop+26+(47-stored), guiLeft+164, guiTop+74, 0xffb51500, 0xff600b00);

	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
		this.renderHoveredToolTip(mx, my);

		//Thanks Flaxbeard!
		ArrayList<String> tooltip = new ArrayList();

		if(mx > guiLeft+157&&mx < guiLeft+164&&my > guiTop+26&&my < guiTop+74)
			tooltip.add(tile.getEnergyStored(null)+"/"+tile.getMaxEnergyStored(null)+" IF");

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}
}
