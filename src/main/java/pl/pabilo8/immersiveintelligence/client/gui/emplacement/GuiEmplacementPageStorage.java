package pl.pabilo8.immersiveintelligence.client.gui.emplacement;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.gui.elements.GuiLabelNoShadow;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement;

/**
 * @author Pabilo8
 * @since 16.07.2021
 */
public class GuiEmplacementPageStorage extends GuiEmplacement
{
	public GuiEmplacementPageStorage(InventoryPlayer inventoryPlayer, TileEntityEmplacement tile)
	{
		super(inventoryPlayer, tile, IIGuiList.GUI_EMPLACEMENT_STORAGE);
		title = I18n.format(CommonProxy.DESCRIPTION_KEY+"metal_multiblock1.emplacement.storage");
	}

	@Override
	public void initGui()
	{
		super.initGui();
		if(tile.currentWeapon!=null&&tile.currentWeapon.getBaseInventory().size() > 0)
		{
			addLabel(xSize/2, 8+16, 0x0a0a0a, I18n.format(CommonProxy.DESCRIPTION_KEY+"metal_multiblock1.emplacement.base_inventory"))
					.setCentered();
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mx, int my)
	{
		super.drawGuiContainerBackgroundLayer(partialTicks, mx, my);
		if(tile.currentWeapon!=null)
			tile.currentWeapon.renderStorageInventory(this, mx, my, partialTicks, true);
		if(tile.getInventory().size() > 0)
		{
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(SourceFactor.ONE, DestFactor.DST_COLOR);
			for(int i = 0; i < tile.getInventory().size(); i++)
				ClientUtils.drawSlot(guiLeft+8+((i%9)*18), guiTop+32+((int)Math.floor(i/(float)9)*18), 16, 16, 0x44);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableBlend();
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		if(tile.currentWeapon!=null)
			tile.currentWeapon.renderStorageInventory(this, mouseX, mouseY, 0, false);
	}
}
