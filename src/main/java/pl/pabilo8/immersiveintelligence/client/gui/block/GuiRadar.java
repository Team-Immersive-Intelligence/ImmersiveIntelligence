package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.gui.ITabbedGui;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRadar;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerRadar;

/**
 * @author Pabilo8
 * @since 28.04.2023
 */
public class GuiRadar extends GuiIEContainerBase implements ITabbedGui
{
	public static final ResourceLocation TEXTURE = new ResourceLocation("immersiveintelligence:textures/gui/radar.png");

	public GuiRadar(EntityPlayer player, TileEntityRadar tile)
	{
		super(new ContainerRadar(player, tile));
		xSize = 229;
		ySize = 255;
	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		bindTexture();
		this.drawTexturedModalRect(guiLeft, guiTop, 13, 0, 229, 165);
		this.drawTexturedModalRect(guiLeft+26, guiTop+165, 39, 165, 175, 90);

	}

	protected void bindTexture()
	{
		mc.getTextureManager().bindTexture(TEXTURE);
	}
}
