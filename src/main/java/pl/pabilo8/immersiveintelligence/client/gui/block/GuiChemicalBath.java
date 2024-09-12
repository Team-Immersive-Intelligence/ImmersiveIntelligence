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
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ChemicalBath;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityChemicalBath;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerChemicalBath;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class GuiChemicalBath extends GuiIEContainerBase
{
	public static final String texture_chemical_bath = ImmersiveIntelligence.MODID+":textures/gui/chemical_bath.png";
	TileEntityChemicalBath tile;

	public GuiChemicalBath(EntityPlayer player, TileEntityChemicalBath tile)
	{
		super(new ContainerChemicalBath(player, tile));
		this.ySize = 168;
		this.tile = tile;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRenderer.drawString(I18n.format("tile."+ImmersiveIntelligence.MODID+".metal_multiblock.chemical_bath.name"), 8, 8, IIReference.COLOR_H1.getPackedRGB());
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(texture_chemical_bath);

		//Draw the tank background

		this.drawTexturedModalRect(guiLeft+32, guiTop+39, 0, 168, 102, 32);

		FluidStack fluid = tile.tanks[0].getFluid();
		if(fluid!=null&&fluid.amount > 0)
		{
			//Draw fluid inside the tank
			float tfluid = 1f-(fluid.amount/(float)tile.tanks[0].getCapacity());
			ClientUtils.drawRepeatedFluidSprite(fluid, guiLeft+32, guiTop+39+(32*tfluid), 102, 32f*(1f-tfluid));
		}

		ClientUtils.bindTexture(texture_chemical_bath);

		//Draw normal background

		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		IIClientUtils.drawPowerBar(guiLeft+161, guiTop+24, 7, 47, tile.getEnergyStored(null)/(float)tile.getMaxEnergyStored(null));

		if(tile.active)
		{
			float progress5 = Math.min(1f, tile.processTime/(tile.processTimeMax*0.5f));
			float progress10 = progress5==1?(((tile.processTime/((float)tile.processTimeMax))-0.5f)/0.5f): 0f;
			this.drawTexturedModalRect(guiLeft+16, guiTop+58, 0, 200, Math.round(19f*progress5), 12);
			this.drawTexturedModalRect(guiLeft+131, guiTop+57, 19, 200, Math.round(21f*progress10), 12);

		}
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
		this.renderHoveredToolTip(mx, my);

		//Thanks Flaxbeard!
		ArrayList<String> tooltip = new ArrayList<>();


		if(isPointInRegion(161, 24, 7, 47, mx, my))
			tooltip.add(IIUtils.getPowerLevelString(tile));

		FluidStack fluid = tile.tanks[0].getFluid();
		if(fluid!=null&&fluid.amount > 0)
		{
			float tfluid = 1f-(fluid.amount/(float)tile.tanks[0].getCapacity());
			if(isPointInRegion(32, 39+(int)(32*tfluid), 102, (int)(32*(1f-tfluid)), mx, my))
			{
				if(!(
						IIMath.isPointInTriangle(30, 57, 30, 70, 43, 70, mx-guiLeft, my-guiTop)||
								IIMath.isPointInTriangle(122, 70, 135, 70, 135, 57, mx-guiLeft, my-guiTop)))
					ClientUtils.addFluidTooltip(fluid, tooltip, ChemicalBath.fluidCapacity);
			}
		}

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}
}
