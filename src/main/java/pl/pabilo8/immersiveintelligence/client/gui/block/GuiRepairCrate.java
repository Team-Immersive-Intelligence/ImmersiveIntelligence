package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.EffectCrates;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityRepairCrate;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerRepairCrate;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.IILib;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class GuiRepairCrate extends GuiIEContainerBase
{
	private static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/gui/repair_crate.png";
	private final TileEntityRepairCrate tile;
	GuiButtonState buttonHealing = null, buttonBoost = null;

	public GuiRepairCrate(EntityPlayer player, TileEntityRepairCrate tile)
	{
		super(new ContainerRepairCrate(player, tile));
		this.tile = tile;
		this.ySize = 168;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		addButton(buttonHealing = new GuiButtonState(0, guiLeft+111, guiTop+2, 28, 24, "", tile.shouldRepairArmor, TEXTURE, 176, 75, 0));
		addButton(buttonBoost = new GuiButtonState(1, guiLeft+111, guiTop+24, 28, 24, "", tile.shouldRepairVehicles, TEXTURE, 176, 51, 0));

		boolean upgraded = tile.hasUpgrade(IIContent.UPGRADE_INSERTER);
		buttonHealing.visible = upgraded;
		buttonBoost.visible = upgraded;
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button==buttonHealing)
		{
			tile.shouldRepairArmor = !tile.shouldRepairArmor;
			buttonHealing.state = tile.shouldRepairArmor;
			IIPacketHandler.sendToServer(new MessageBooleanAnimatedPartsSync(tile.shouldRepairArmor, 1, tile.getPos()));
		}
		else if(button==buttonBoost)
		{
			tile.shouldRepairVehicles = !tile.shouldRepairVehicles;
			buttonBoost.state = tile.shouldRepairVehicles;
			IIPacketHandler.sendToServer(new MessageBooleanAnimatedPartsSync(tile.shouldRepairVehicles, 2, tile.getPos()));
		}
	}


	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		boolean upgraded = tile.hasUpgrade(IIContent.UPGRADE_INSERTER);

		if(upgraded)
		{
			ArrayList<String> tooltip = new ArrayList<>();

			if(IIUtils.isPointInRectangle(buttonHealing.x, buttonHealing.y, buttonHealing.x+buttonHealing.width, buttonHealing.y+buttonHealing.height, mouseX, mouseY))
				tooltip.add(I18n.format(IILib.DESCRIPTION_KEY+"repair_crate.repair_armor"));
			else if(IIUtils.isPointInRectangle(buttonBoost.x, buttonBoost.y, buttonBoost.x+buttonBoost.width, buttonBoost.y+buttonBoost.height, mouseX, mouseY))
				tooltip.add(I18n.format(IILib.DESCRIPTION_KEY+"repair_crate.repair_vehicles"));

			if(IIUtils.isPointInRectangle(guiLeft+153, guiTop+24, guiLeft+153+7, guiTop+24+47, mouseX, mouseY))
				tooltip.add(tile.energyStorage+"/"+EffectCrates.maxEnergyStored+" IF");

			if(!tooltip.isEmpty())
			{
				ClientUtils.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer, guiLeft+xSize, -1);
				RenderHelper.enableGUIStandardItemLighting();
			}
		}

	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		boolean upgraded = tile.hasUpgrade(IIContent.UPGRADE_INSERTER);
		int ww = upgraded?9: 36;

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(TEXTURE);
		this.drawTexturedModalRect(guiLeft, guiTop+79, 0, 79, xSize, ySize-79);
		this.drawTexturedModalRect(guiLeft+ww, guiTop, 9, 0, upgraded?167: 102, 79);

		IIClientUtils.drawPowerBar(guiLeft+153, guiTop+24, 7, 47, tile.energyStorage/(float)EffectCrates.maxEnergyStored);
	}
}
