package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.DustUtils;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Filler;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFiller;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerFiller;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class GuiFiller extends GuiIEContainerBase
{
	public static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/gui/filler.png";
	TileEntityFiller tile;

	public GuiFiller(EntityPlayer player, TileEntityFiller tile)
	{
		super(new ContainerFiller(player, tile));
		this.ySize = 168;
		this.tile = tile;
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(TEXTURE);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		if(!tile.dustStorage.isEmpty())
		{
			int stored = (int)(60*(tile.dustStorage.amount/(float)Filler.dustCapacity));
			float[] rgb = DustUtils.getColor(tile.dustStorage).getFloatRGB();

			GlStateManager.color(rgb[0], rgb[1], rgb[2]);
			this.drawTexturedModalRect(guiLeft+56, guiTop+2+(60-stored), 176, 60-stored, 64, stored);
			GlStateManager.color(1f, 1f, 1f);
		}

		IIClientUtils.drawPowerBar(guiLeft+161-4, guiTop+24, 7, 47, tile.getEnergyStored(null)/(float)tile.getMaxEnergyStored(null));
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
		this.renderHoveredToolTip(mx, my);

		ArrayList<String> tooltip = new ArrayList<>();

		if(mx > guiLeft+161-4&&mx < guiLeft+168-4&&my > guiTop+24&&my < guiTop+71)
			tooltip.add(IIUtils.getPowerLevelString(tile.energyStorage));

		int stored = (int)(60*(tile.dustStorage.amount/(float)Filler.dustCapacity));
		if(mx > guiLeft+56&&mx < guiLeft+56+60&&my > guiTop+2+(60-stored)&&my < guiTop+2+(60-stored)+stored)
		{
			tooltip.add(DustUtils.getDustName(tile.dustStorage));
			tooltip.add(tile.dustStorage.amount+" mB");
		}

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}
}
