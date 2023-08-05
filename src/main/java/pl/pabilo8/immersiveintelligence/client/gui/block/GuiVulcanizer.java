package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal.MultiblockProcess;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.api.crafting.VulcanizerRecipe;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityVulcanizer;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerVulcanizer;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class GuiVulcanizer extends GuiIEContainerBase
{
	public static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/gui/vulcanizer.png";
	public TileEntityVulcanizer tile;

	public GuiVulcanizer(EntityPlayer player, TileEntityVulcanizer tile)
	{
		super(new ContainerVulcanizer(player, tile));
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
		IIClientUtils.drawPowerBar(8, -9,2,16,prog1);

		mc.getRenderItem().renderItemIntoGUI(s1, -10,-9);

		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		ClientUtils.bindTexture(TEXTURE);
		GlStateManager.translate(36,0,0);
		GlStateManager.rotate(-rotato*180,0,0,1);
		this.drawTexturedModalRect(-12, -14, 64, 168, 24, 23);
		IIClientUtils.drawPowerBar(8, -9,2,16,prog2);
		mc.getRenderItem().renderItemIntoGUI(s2, -10,-9);

		GlStateManager.popMatrix();


		GlStateManager.popMatrix();

		IIClientUtils.drawPowerBar(guiLeft+157, guiTop+24, 7,47,tile.getEnergyStored(null)/(float)tile.getMaxEnergyStored(null));

	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
		this.renderHoveredToolTip(mx, my);

		ArrayList<String> tooltip = new ArrayList<>();

		if(mx > guiLeft+157&&mx < guiLeft+164&&my > guiTop+24&&my < guiTop+71)
			tooltip.add(IIUtils.getPowerLevelString(tile));
		ItemStack previewedItem = getPreviewedItem(mx, my);
		if(previewedItem!=null)
			tooltip.addAll(previewedItem.getTooltip(mc.player, mc.gameSettings.advancedItemTooltips?TooltipFlags.ADVANCED: TooltipFlags.NORMAL));
		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}

	@Nullable
	public ItemStack getPreviewedItem(int mouseX, int mouseY)
	{
		if(tile.processQueue.size() > 0)
		{
			MultiblockProcess<VulcanizerRecipe> process0 = tile.processQueue.get(0);
			float rotato;
			double processTime = ((process0.processTick)/(double)process0.maxTicks);
			if(processTime < 0.78)
				rotato = 0;
			else if(processTime < 0.84)
				rotato = Math.min((float)((processTime-0.78)/0.05), 1);
			else
				rotato = 1;
			double angle = rotato*180f;

			{
				float xx = (float)(guiLeft+86+Math.cos(Math.toRadians(angle))*-36), yy = (float)(guiTop+39+Math.sin(Math.toRadians(angle))*-36);
				if(IIUtils.isPointInRectangle(xx-8, yy-8, xx+8, yy+8, mouseX, mouseY))
					return tile.processQueue.get(0).recipe.output;
			}
			if(tile.processQueue.size()>1)
			{
				angle = MathHelper.wrapDegrees(180+angle);
				float xx = (float)(guiLeft+86+Math.cos(Math.toRadians(angle))*-36), yy = (float)(guiTop+39+Math.sin(Math.toRadians(angle))*-36);
				if(IIUtils.isPointInRectangle(xx-8, yy-8, xx+8, yy+8, mouseX, mouseY))
					return tile.processQueue.get(1).recipe.output;
			}
		}
		return null;
	}
}
