package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrintingPress;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrintingPress.PrintOrder;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerPrintingPress;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIMultiblockProcess;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @updated 13.12.2023
 * @since 10-07-2019
 */
public class GuiPrintingPress extends GuiIEContainerBase
{
	private static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/gui/printing_press.png";
	private final TileEntityPrintingPress tile;

	public GuiPrintingPress(EntityPlayer player, TileEntityPrintingPress tile)
	{
		super(new ContainerPrintingPress(player, tile));
		this.ySize = 176;
		this.tile = tile;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRenderer.drawString(net.minecraft.client.resources.I18n.format("tile."+ImmersiveIntelligence.MODID+".metal_multiblock.printing_press.name"), 8, 6, IIReference.COLOR_H1.getPackedRGB());
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(TEXTURE);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		IIClientUtils.drawPowerBar(guiLeft+112, guiTop+23, 7, 47, tile.getEnergyStored(null)/(float)tile.getMaxEnergyStored(null));

		float capacity = tile.tank.getCapacity();
		int yy = guiTop+21+51;
		for(int i = tile.tank.getFluidTypes()-1; i >= 0; i--)
		{
			FluidStack fs = tile.tank.fluids.get(i);
			if(fs!=null&&fs.getFluid()!=null)
			{
				int fluidHeight = (int)(48*(fs.amount/capacity));
				yy -= fluidHeight;
				ClientUtils.drawRepeatedFluidSprite(fs, guiLeft+125, yy, 16, fluidHeight);
			}
		}

		ClientUtils.bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft+123, guiTop+21, 176, 0, 20, 51);

		if(!tile.processQueue.isEmpty())
		{
			IIMultiblockProcess<PrintOrder> current = tile.processQueue.get(0);
			drawTexturedModalRect(guiLeft+31, guiTop+37, 176, 51,
					(int)(55*tile.getProductionProgress(current, partialTicks)), 20);
		}

	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
		this.renderHoveredToolTip(mx, my);

		//Thanks Flaxbeard!
		ArrayList<String> tooltip = new ArrayList<>();

		if(IIMath.isPointInRectangle(125, 21, 125+16, 21+47, mx-guiLeft, my-guiTop))
		{
			float capacity = tile.tank.getCapacity();
			int yy = guiTop+21+47;
			if(tile.tank.getFluidTypes()==0)
				tooltip.add(I18n.format("gui.immersiveengineering.empty"));
			else
				for(int i = tile.tank.getFluidTypes()-1; i >= 0; i--)
				{
					FluidStack fs = tile.tank.fluids.get(i);
					if(fs!=null&&fs.getFluid()!=null)
					{
						int fluidHeight = (int)(47*(fs.amount/capacity));
						yy -= fluidHeight;
						if(my >= yy&&my < yy+fluidHeight)
							ClientUtils.addFluidTooltip(fs, tooltip, (int)capacity);
					}
				}
		}

		if(IIMath.isPointInRectangle(112, 23, 119, 70, mx-guiLeft, my-guiTop))
			tooltip.add(IIUtils.getPowerLevelString(tile.energyStorage));

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}
}
