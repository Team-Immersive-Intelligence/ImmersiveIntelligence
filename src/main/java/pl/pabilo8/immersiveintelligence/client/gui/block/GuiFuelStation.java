package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.FuelStation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFuelStation;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerFuelStation;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class GuiFuelStation extends GuiIEContainerBase
{
	public static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/gui/fuel_station.png";
	TileEntityFuelStation tile;

	public GuiFuelStation(EntityPlayer player, TileEntityFuelStation tile)
	{
		super(new ContainerFuelStation(player, tile));
		this.ySize = 168;
		this.tile = tile;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{

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

		//Thanks, Flaxbeard!
		int yy = guiTop + 63;
		for(int i = this.tile.tanks[0].getFluidTypes() - 1; i >= 0; --i) {
			FluidStack fs = this.tile.tanks[0].fluids.get(i);
			if (fs != null && fs.getFluid() != null) {
				int fluidHeight = (int)(60f * ((float)fs.amount /FuelStation.fluidCapacity));
				yy -= fluidHeight;
				ClientUtils.drawRepeatedFluidSprite(fs, (float)(guiLeft + 63), (float)yy, 52f, (float)fluidHeight);
			}
		}

		IIClientUtils.drawPowerBar(guiLeft+137, guiTop+22, 7,46,tile.getEnergyStored(null)/(float)tile.getMaxEnergyStored(null));
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
		this.renderHoveredToolTip(mx, my);
		ArrayList<String> tooltip = new ArrayList<>();

		if(mx >= guiLeft+63&&mx <= guiLeft+63+52&&my >= guiTop+3&&my <= guiTop+63)
		{
			float capacity = tile.tanks[0].getCapacity();
			if(tile.tanks[0].getFluidTypes()==0)
				tooltip.add(I18n.format("gui.immersiveengineering.empty"));
			else
			{
				int fluidUpToNow = 0;
				int lastY = 0;
				int myRelative = guiTop+63-my;
				for(int i = tile.tanks[0].getFluidTypes()-1; i >= 0; i--)
				{
					FluidStack fs = tile.tanks[0].fluids.get(i);
					if(fs!=null&&fs.getFluid()!=null)
					{
						fluidUpToNow += fs.amount;
						int newY = (int)(60*(fluidUpToNow/capacity));
						if(myRelative >= lastY&&myRelative < newY)
						{
							ClientUtils.addFluidTooltip(fs, tooltip, (int)capacity);
							break;
						}
						lastY = newY;
					}
				}
			}
		}

		if(mx > guiLeft+137&&mx < guiLeft+144&&my > guiTop+22&&my < guiTop+68)
			tooltip.add(IIUtils.getPowerLevelString(tile));

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}
}
