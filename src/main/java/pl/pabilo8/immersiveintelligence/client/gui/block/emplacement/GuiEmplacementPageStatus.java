package pl.pabilo8.immersiveintelligence.client.gui.block.emplacement;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.elements.GuiReactiveList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonSwitch;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiSliderII;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeapon.MachineUpgradeEmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.io.IOException;

/**
 * @author Pabilo8
 * @since 16.07.2021
 */
public class GuiEmplacementPageStatus extends GuiEmplacement
{
	GuiButtonSwitch switchRSControl, switchDataControl, switchSendTarget;
	GuiSliderII sliderRepair;

	public GuiEmplacementPageStatus(EntityPlayer player, TileEntityEmplacement tile)
	{
		super(player, tile, IIGuiList.GUI_EMPLACEMENT_STATUS);
		title = I18n.format(IIReference.DESCRIPTION_KEY+"metal_multiblock1.emplacement.status");
	}

	@Override
	public void initGui()
	{
		super.initGui();

		addLabel(8, 24, 96, 0, IIColor.WHITE, tile.currentWeapon!=null?(
				I18n.format("machineupgrade.immersiveintelligence."+tile.currentWeapon.getName())):
				I18n.format(IIReference.DESCRIPTION_KEY+"metal_multiblock1.emplacement.weapon_none")).setCentered();
		addLabel(8, 86, IIReference.COLOR_H1, I18n.format(IIReference.DESCRIPTION_KEY+"metal_multiblock1.emplacement.upgrades"));
		//
		GuiReactiveList upgradeList = new GuiReactiveList(this, buttonList.size(), guiLeft+11, guiTop+86+6+1, 124, 54,
				tile.getUpgrades().stream().filter(upgrade -> !(upgrade instanceof MachineUpgradeEmplacementWeapon)).map(MachineUpgrade::getName).toArray(String[]::new))
				//"Heavy Barrel","Ballistic Circuitry","High-Quality Bearings")
				.setTranslationFunc(s -> I18n.format("machineupgrade.immersiveintelligence."+s))
				.setFormatting(0.75f, true);
		addButton(upgradeList);

		addLabel(112, 22, 93, 0, IIReference.COLOR_H1,
				I18n.format(IIReference.DESCRIPTION_KEY+"metal_multiblock1.emplacement.settings")).setCentered();

		switchRSControl = addSwitch(112, 28, 80, IIReference.COLOR_H1, COLOR_IN, COLOR_OUT, tile.redstoneControl,
				I18n.format(IIReference.DESCRIPTION_KEY+"metal_multiblock1.emplacement.redstone_control"));
		switchDataControl = addSwitch(112,
				28+switchRSControl.getTextHeight(fontRenderer),
				80, IIReference.COLOR_H1, COLOR_IN, COLOR_OUT, tile.dataControl,
				I18n.format(IIReference.DESCRIPTION_KEY+"metal_multiblock1.emplacement.data_control"));
		switchSendTarget = addSwitch(112,
				28+switchRSControl.getTextHeight(fontRenderer)+switchDataControl.getTextHeight(fontRenderer),
				80, IIReference.COLOR_H1, COLOR_IN, COLOR_OUT, tile.sendAttackSignal,
				I18n.format(IIReference.DESCRIPTION_KEY+"metal_multiblock1.emplacement.send_attack_signal"));

		sliderRepair = addSlider(116,
				28+switchRSControl.getTextHeight(fontRenderer)+switchDataControl.getTextHeight(fontRenderer)+switchSendTarget.getTextHeight(fontRenderer)
						+fontRenderer.getWordWrappedHeight(I18n.format(IIReference.DESCRIPTION_KEY+"metal_multiblock1.emplacement.auto_repair_threshold"), 70),
				80, IIReference.COLOR_H1, tile.autoRepairAmount,
				I18n.format(IIReference.DESCRIPTION_KEY+"metal_multiblock1.emplacement.auto_repair_threshold"));

	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		if(button instanceof GuiButtonSwitch)
		{
			syncDataToServer();
		}
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		syncDataToServer();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mx, int my)
	{
		super.drawGuiContainerBackgroundLayer(partialTicks, mx, my);
		bindTexture();
		drawTexturedModalRect(guiLeft+8+96, guiTop+16, 205, 9, 6, 130);

		draw3DRotato(guiLeft+8, guiTop+16, partialTicks);
		ClientUtils.drawColouredRect(guiLeft+8, guiTop+86+4, 96, 54, 0x4f000000);

	}

	private void draw3DRotato(int x, int y, float partialTicks)
	{
		//ClientUtils.drawGradientRect(x, y, x+86, y+76, 0xff000000,0xff000000);
		ClientUtils.drawColouredRect(x, y, 96, 76-12, 0xff000000);
		GlStateManager.pushMatrix();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.translate(x+47, y+65-12, 0);
		GlStateManager.rotate(-15, 1, 0, 0);
		GlStateManager.scale(-22, -22, -1);
		GlStateManager.rotate(360*(((mc.world.getTotalWorldTime()%120)+partialTicks)/120f), 0, 1, 0);
		tile.renderWithUpgrades(tile.getUpgrades().toArray(new MachineUpgrade[0]));
		GlStateManager.disableLighting();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popMatrix();
	}

	@Override
	protected void syncDataToServer()
	{
		super.syncDataToServer();

		IIPacketHandler.sendToServer(new MessageIITileSync(this.tile, EasyNBT.newNBT()
				.withBoolean("redstoneControl", switchRSControl.state)
				.withBoolean("dataControl", switchDataControl.state)
				.withBoolean("sendAttackSignal", switchSendTarget.state)
				.withFloat("autoRepairAmount", (float)sliderRepair.sliderValue)
		));
	}
}
