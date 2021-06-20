package pl.pabilo8.immersiveintelligence.client.gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal.MultiblockProcess;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.VulcanizerRecipe;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityVulcanizer;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerElectrolyzer;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerVulcanizer;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class GuiVulcanizer extends GuiIEContainerBase
{
	public static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/gui/vulcanizer.png";
	TileEntityVulcanizer tile;

	public GuiVulcanizer(InventoryPlayer inventoryPlayer, TileEntityVulcanizer tile)
	{
		super(new ContainerVulcanizer(inventoryPlayer, tile));
		this.ySize = 168;
		this.tile = tile;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		//this.fontRenderer.drawString(I18n.format("tile."+ImmersiveIntelligence.MODID+".metal_multiblock1.vulcanizer.name"), 8, 6, 0x0a0a0a);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		float rotato=0;
		float prog1=0, prog2=0;
		ItemStack s1=ItemStack.EMPTY, s2=ItemStack.EMPTY;
		if(tile.processQueue.size() > 0)
		{
			MultiblockProcess<VulcanizerRecipe> process0 = tile.processQueue.get(0);
			prog1=process0.processTick/(float)process0.maxTicks;
			s1=process0.recipe.output;
			double processTime = ((process0.processTick+f)/(double)process0.maxTicks);
			if(processTime<0.78)
				rotato = 0;
			else if(processTime < 0.84)
				rotato = Math.min((float)((processTime-0.78)/0.05), 1);
			else
				rotato = 1;

			if(tile.processQueue.size()>1)
			{
				MultiblockProcess<VulcanizerRecipe> process1 = tile.processQueue.get(1);
				s2= process1.recipe.output;
				prog2= process1.processTick/(float)process1.maxTicks;

			}
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(TEXTURE);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		GlStateManager.pushMatrix();
		GlStateManager.translate(guiLeft+85,guiTop+39,0);
		GlStateManager.rotate(rotato*180, 0, 0, 1);

		this.drawTexturedModalRect(-32, -24, 0, 168, 64, 48);

		GlStateManager.pushMatrix();
		GlStateManager.translate(-36,0,0);
		GlStateManager.rotate(-rotato*180,0,0,1);
		this.drawTexturedModalRect(-12, -14, 64, 168, 24, 23);
		int o = (int)(16*prog1);
		ClientUtils.drawGradientRect(8, -9+(16-o), 10, 7, 0xffb51500, 0xff600b00);

		mc.getRenderItem().renderItemIntoGUI(s1, -10,-9);

		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		ClientUtils.bindTexture(TEXTURE);
		GlStateManager.translate(36,0,0);
		GlStateManager.rotate(-rotato*180,0,0,1);
		this.drawTexturedModalRect(-12, -14, 64, 168, 24, 23);
		o = (int)(16*prog2);
		ClientUtils.drawGradientRect(8, -9+(16-o), 10, 7, 0xffb51500, 0xff600b00);
		mc.getRenderItem().renderItemIntoGUI(s2, -10,-9);

		GlStateManager.popMatrix();


		GlStateManager.popMatrix();


		int stored = (int)(47*(tile.getEnergyStored(null)/(float)tile.getMaxEnergyStored(null)));
		ClientUtils.drawGradientRect(guiLeft+157, guiTop+24+(47-stored), guiLeft+164, guiTop+71, 0xffb51500, 0xff600b00);
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
		this.renderHoveredToolTip(mx, my);

		ArrayList<String> tooltip = new ArrayList();

		if(mx > guiLeft+157&&mx < guiLeft+164&&my > guiTop+24&&my < guiTop+71)
			tooltip.add(tile.getEnergyStored(null)+"/"+tile.getMaxEnergyStored(null)+" IF");

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}
}
