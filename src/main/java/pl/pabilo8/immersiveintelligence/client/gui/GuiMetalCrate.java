package pl.pabilo8.immersiveintelligence.client.gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityMetalCrate;

/**
 * Created by Pabilo8 on 2019-05-17.
 */
public class GuiMetalCrate extends blusunrize.immersiveengineering.client.gui.GuiCrate
{
	public GuiMetalCrate(InventoryPlayer inventoryPlayer, TileEntityMetalCrate tile)
	{
		super(inventoryPlayer, tile);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/gui/metal_crate.png");
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
}
