package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityGearbox;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerGearbox;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class GuiGearbox extends GuiIEContainerBase
{
	public static final String texture_skycrate_station = ImmersiveIntelligence.MODID+":textures/gui/wooden_gearbox.png";
	TileEntityGearbox tile;

	public GuiGearbox(EntityPlayer player, TileEntityGearbox tile)
	{
		super(new ContainerGearbox(player, tile));
		this.ySize = 176;
		this.tile = tile;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		IIClientUtils.drawStringCentered(fontRenderer, I18n.format("tile."+ImmersiveIntelligence.MODID+".gearbox.wooden_gearbox.name"), 0, 0, getXSize(), 6, 0xd99747);
		fontRenderer.drawString((RotaryUtils.getGearEffectiveness(tile.getInventory(), tile.getEfficiencyMultiplier())*100)+"%", 76, 47, 0xd99747);
		fontRenderer.drawString(String.format("%.2f", RotaryUtils.getGearTorqueRatio(tile.getInventory())), 76, 59, 0xd99747);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(texture_skycrate_station);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		RotaryUtils.renderEnergyBars(guiLeft+148, guiTop+20, tile.rotation,0,100);
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
		this.renderHoveredToolTip(mx, my);

		//Thanks Flaxbeard!
		ArrayList<String> tooltip = new ArrayList<>();

		RotaryUtils.renderEnergyTooltip(tooltip, mx, my, guiLeft+148, guiTop+20, tile.rotation);

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}
}
