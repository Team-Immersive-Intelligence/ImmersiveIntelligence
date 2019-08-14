package pl.pabilo8.immersiveintelligence.client.gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.TileEntityChemicalBath;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerChemicalBath;

import java.util.ArrayList;

/**
 * Created by Pabilo8 on 10-07-2019.
 */
public class GuiChemicalBath extends GuiIEContainerBase
{
	public static final String texture_chemical_bath = ImmersiveIntelligence.MODID+":textures/gui/chemical_bath.png";
	TileEntityChemicalBath tile;

	public GuiChemicalBath(InventoryPlayer inventoryPlayer, TileEntityChemicalBath tile)
	{
		super(new ContainerChemicalBath(inventoryPlayer, tile));
		this.ySize = 168;
		this.tile = tile;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRenderer.drawString(I18n.format("tile."+ImmersiveIntelligence.MODID+".metal_multiblock.chemical_bath.name"), 8, 6, 0x0a0a0a);
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

		if(tile.tanks[0].getFluidAmount() > 0)
		{
			//Draw fluid inside the tank
			float tfluid = 1f-(((float)tile.tanks[0].getFluidAmount())/tile.tanks[0].getCapacity());
			ClientUtils.drawRepeatedFluidSprite(tile.tanks[0].getFluid(), guiLeft+32, guiTop+39+(32*tfluid), 102, 32f);
		}

		ClientUtils.bindTexture(texture_chemical_bath);

		//Draw normal background

		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		int stored = (int)(47*(tile.getEnergyStored(null)/(float)tile.getMaxEnergyStored(null)));
		ClientUtils.drawGradientRect(guiLeft+161, guiTop+24+(47-stored), guiLeft+168, guiTop+71, 0xffb51500, 0xff600b00);

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
		ArrayList<String> tooltip = new ArrayList();

		if(mx > guiLeft+161&&mx < guiLeft+168&&my > guiTop+24&&my < guiTop+71)
			tooltip.add(tile.getEnergyStored(null)+"/"+tile.getMaxEnergyStored(null)+" IF");

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}
}
