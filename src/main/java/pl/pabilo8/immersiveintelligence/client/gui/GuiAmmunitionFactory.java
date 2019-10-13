package pl.pabilo8.immersiveintelligence.client.gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.TileEntityAmmunitionFactory;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.TileEntityPrintingPress;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerAmmunitionFactory;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerPrintingPress;

import java.awt.*;
import java.util.ArrayList;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.ammunitionFactory;
import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.printingPress;

/**
 * Created by Pabilo8 on 10-07-2019.
 */
public class GuiAmmunitionFactory extends GuiIEContainerBase
{
	public static final String texture_ammunition_factory = ImmersiveIntelligence.MODID+":textures/gui/ammunition_factory.png";
	public static final Color gunpowder_colour = new Color(0x525252);
	TileEntityAmmunitionFactory tile;

	public GuiAmmunitionFactory(InventoryPlayer inventoryPlayer, TileEntityAmmunitionFactory tile)
	{
		super(new ContainerAmmunitionFactory(inventoryPlayer, tile));
		this.ySize = 168;
		this.tile = tile;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRenderer.drawString(I18n.format("tile."+ImmersiveIntelligence.MODID+".metal_multiblock.ammunition_factory.name"), 8, 6, 0x0a0a0a);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(texture_ammunition_factory);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		int stored = (int)(47*(tile.getEnergyStored(null)/(float)tile.getMaxEnergyStored(null)));
		ClientUtils.drawGradientRect(guiLeft+112, guiTop+23+(47-stored), guiLeft+119, guiTop+70, 0xffb51500, 0xff600b00);

		float capacity = tile.tanks[0].getCapacity();
		int yy = guiTop+21+51;
		for(int i = tile.tanks[0].getFluidTypes()-1; i >= 0; i--)
		{
			FluidStack fs = tile.tanks[0].fluids.get(i);
			if(fs!=null&&fs.getFluid()!=null)
			{
				int fluidHeight = (int)(48*(fs.amount/capacity));
				yy -= fluidHeight;
				ClientUtils.drawRepeatedFluidSprite(fs, guiLeft+125, yy, 16, fluidHeight);
			}
		}

		ClientUtils.bindTexture(texture_ammunition_factory);
		drawTexturedModalRect(guiLeft+123, guiTop+21, 176, 0, 20, 51);


		if(tile.ingredientCount1 > 0)
		{
			float comp1 = Math.min((float)tile.ingredientCount1/(float)ammunitionFactory.componentCapacity, 1);
			Color c1 = new Color(BulletRegistry.INSTANCE.registeredComponents.get(tile.ingredient1).getColour());
			GlStateManager.color(c1.getRed()/255f, c1.getGreen()/255f, c1.getBlue()/255f, 1f);
			drawTexturedModalRect(guiLeft+8, guiTop+19+Math.round(16f*(1f-comp1)), 123, 22, 2, Math.round(16f*comp1));
		}

		if(tile.ingredientCount2 > 0)
		{
			float comp2 = Math.min((float)tile.ingredientCount2/(float)ammunitionFactory.componentCapacity, 1);
			Color c2 = new Color(BulletRegistry.INSTANCE.registeredComponents.get(tile.ingredient2).getColour());
			GlStateManager.color(c2.getRed()/255f, c2.getGreen()/255f, c2.getBlue()/255f, 1f);
			drawTexturedModalRect(guiLeft+8, guiTop+59+Math.round(16f*(1f-comp2)), 123, 22, 2, Math.round(16f*comp2));
		}

		if(tile.gunpowderCount > 0)
		{
			float gunpowder = Math.min((float)tile.gunpowderCount/(float)ammunitionFactory.componentCapacity, 1);
			GlStateManager.color(gunpowder_colour.getRed()/255f, gunpowder_colour.getGreen()/255f, gunpowder_colour.getBlue()/255f, 1f);
			drawTexturedModalRect(guiLeft+8, guiTop+39+Math.round(16f*(1f-gunpowder)), 123, 22, 2, Math.round(16f*gunpowder));
		}

		GlStateManager.color(1f, 1f, 1f, 1f);

		//if(tile.active)
		//	drawTexturedModalRect(guiLeft+31, guiTop+37, 176, 51, Math.round(55*(printingPress.printTime-tile.processTimeLeft)/printingPress.printTime), 20);

	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
		this.renderHoveredToolTip(mx, my);

		//Thanks Flaxbeard!
		ArrayList<String> tooltip = new ArrayList();

		if(mx >= guiLeft+125&&mx <= guiLeft+125+16&&my >= guiTop+21&&my <= guiTop+21+47)
		{
			float capacity = tile.tanks[0].getCapacity();
			int yy = guiTop+21+47;
			if(tile.tanks[0].getFluidTypes()==0)
				tooltip.add(I18n.format("gui.immersiveengineering.empty"));
			else
				for(int i = tile.tanks[0].getFluidTypes()-1; i >= 0; i--)
				{
					FluidStack fs = tile.tanks[0].fluids.get(i);
					if(fs!=null&&fs.getFluid()!=null)
					{
						int fluidHeight = (int)(47*(fs.amount/capacity));
						yy -= fluidHeight;
						if(my >= yy&&my < yy+fluidHeight)
							ClientUtils.addFluidTooltip(fs, tooltip, (int)capacity);
					}
				}
		}

		if(mx > guiLeft+112&&mx < guiLeft+119&&my > guiTop+23&&my < guiTop+70)
			tooltip.add(tile.getEnergyStored(null)+"/"+tile.getMaxEnergyStored(null)+" IF");

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}
}
