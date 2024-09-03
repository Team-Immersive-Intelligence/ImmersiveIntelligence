package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import blusunrize.lib.manual.ManualUtils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerUpgrade;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBeginMachineUpgrade;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class GuiUpgrade extends GuiIEContainerBase
{
	public static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/gui/upgrade_lowtier.png";
	IUpgradableMachine upgradableMachine;
	TileEntity tileEntity;
	List<MachineUpgrade> upgrades;
	MachineUpgrade previewed = null;
	public boolean info = false;
	public boolean previewInstalled = false;
	GuiButtonIE buttonInfo = null, buttonUpgrade = null, buttonQuit = null;
	private String textUpgradeMachine, textInfo, textUpgrade, textRemove, textBack;

	public <T extends TileEntity & IUpgradableMachine> GuiUpgrade(EntityPlayer player, T tile)
	{
		super(new ContainerUpgrade(player, tile));
		this.ySize = 168;
		this.upgradableMachine = tile;
		this.tileEntity = tile;
		upgrades = MachineUpgrade.getMatchingUpgrades(upgradableMachine);
		for(MachineUpgrade upgrade : upgradableMachine.getUpgrades())
			if(!upgrades.contains(upgrade))
				upgrades.add(upgrade);
	}

	public void initGui()
	{
		super.initGui();
		boolean g = buttonInfo!=null&&buttonInfo.visible;
		boolean h = buttonUpgrade!=null&&buttonUpgrade.visible;
		boolean j = buttonQuit!=null&&buttonQuit.visible;

		textUpgradeMachine = I18n.format(IIReference.DESCRIPTION_KEY+"upgrade_gui.title");
		textInfo = I18n.format(IIReference.DESCRIPTION_KEY+"upgrade_gui.info");
		textUpgrade = I18n.format(IIReference.DESCRIPTION_KEY+"upgrade_gui.install");
		textRemove = I18n.format(IIReference.DESCRIPTION_KEY+"upgrade_gui.remove");
		textBack = I18n.format(IIReference.DESCRIPTION_KEY+"upgrade_gui.back");

		addButton(buttonInfo = new GuiButtonIE(0, guiLeft+106, guiTop+23, 52, 12, textInfo, TEXTURE, 49, 168));
		addButton(buttonUpgrade = new GuiButtonIE(1, guiLeft+106, guiTop+38, 52, 12, textUpgrade, TEXTURE, 49, 180));
		addButton(buttonQuit = new GuiButtonIE(2, guiLeft+106, guiTop+53, 52, 12, textBack, TEXTURE, 49, 192));

		buttonInfo.visible = g;
		buttonUpgrade.visible = h;
		buttonQuit.visible = j;

		if(upgradableMachine.getInstallProgress() > 0)
		{
			previewed = upgradableMachine.getCurrentlyInstalled();
			buttonList.forEach(guiButton -> guiButton.visible = false);
		}
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		IIClientUtils.drawStringCentered(fontRenderer, textUpgradeMachine, 0, -14, getXSize()+4, 6, 0xd99747);
		//fontRenderer.drawString((RotaryUtils.getGearEffectiveness(tile.getInventory(), tile.getEfficiencyMultiplier(), 3)*100)+"%", 76, 47, 0xd99747);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(button.id==0)
			info ^= true;
		else if(button.id==1)
		{
			IIPacketHandler.sendToServer(new MessageBeginMachineUpgrade(tileEntity, previewed.getName(), mc.player, !previewInstalled));
			mc.player.closeScreen();
		}
		else if(button.id==2)
		{
			previewed = null;
			info = false;
			buttonList.forEach(guiButton -> guiButton.visible = false);
		}
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(TEXTURE);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		int ww = ((fontRenderer.getStringWidth(textUpgradeMachine)-4)/2);
		this.drawTexturedModalRect(guiLeft+(xSize/2-ww)-4, guiTop-11, 0, 168, 12, 11);
		for(int w = 11; w < ww*2; w += 12)
			this.drawTexturedModalRect(guiLeft+(xSize/2-ww)+w-4, guiTop-11, 12, 168, Math.min(12, (ww*2)-w), 11);
		this.drawTexturedModalRect(guiLeft+(xSize/2+ww)-4, guiTop-11, 37, 168, 12, 11);

		if(this.previewed!=null&&info)
		{
			this.drawTexturedModalRect(guiLeft+xSize, guiTop+4, 176, 36, 80, 128);
		}

	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if(mouseButton==0&&previewed==null&&IIMath.isPointInRectangle(guiLeft+99, guiTop+8, guiLeft+99+66, guiTop+8+56, mouseX, mouseY))
		{
			int xx = mouseX-(guiLeft+100);
			int yy = mouseY-(guiTop+9);
			int id = (int)(Math.floor(xx/20f)+(3*Math.floor(yy/20f)));
			if(id > -1&&id < upgrades.size())
			{
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				previewed = upgrades.get(id);
				previewInstalled = upgradableMachine.hasUpgrade(previewed);
				buttonUpgrade.displayString = previewInstalled?textRemove: textUpgrade;
			}
		}
		else
			super.mouseClicked(mouseX, mouseY, mouseButton);

		buttonList.forEach(guiButton -> guiButton.visible = (previewed!=null));
		buttonUpgrade.enabled = hasItemsForUpgrade(previewed);
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
		//this.renderHoveredToolTip(mx, my);

		GlStateManager.pushMatrix();
		GlStateManager.color(1f, 1f, 1f, 1f);
		//GuiInventory.drawEntityOnScreen(guiLeft+40,guiTop+40,32,mx,my, mc.player);
		GlStateManager.translate(guiLeft+51, guiTop+70, 0);
		GlStateManager.rotate(-15, 1, 0, 0);
		GlStateManager.scale(-24, -24, -1);
		GlStateManager.rotate(360*(((mc.world.getTotalWorldTime()%120)+partial)/120f), 0, 1, 0);
		ArrayList<MachineUpgrade> upgrades = new ArrayList<>(upgradableMachine.getUpgrades());
		if(previewed!=null&&!upgrades.contains(previewed))
			upgrades.add(previewed);
		upgradableMachine.renderWithUpgrades(upgrades.toArray(new MachineUpgrade[0]));
		GlStateManager.disableLighting();
		GlStateManager.popMatrix();

		ArrayList<String> tooltip = new ArrayList<>();
		if(previewed==null)
		{
			int i = 0;
			for(MachineUpgrade upgrade : this.upgrades)
			{
				int xx = (i%3)*20, yy = (int)Math.floor(i/3f)*20;
				ClientUtils.bindTexture(TEXTURE);
				drawTexturedModalRect(guiLeft+100+xx, guiTop+9+yy, upgradableMachine.hasUpgrade(upgrade)?121: 101, 168, 20, 20);
				if(IIMath.isPointInRectangle(guiLeft+100+xx, guiTop+9+yy, guiLeft+100+xx+16, guiTop+9+yy+16, mx, my))
					tooltip.add(getUpgradeNameTranslation(upgrade));
				i++;
			}
			i = 0;
			for(MachineUpgrade upgrade : this.upgrades)
			{
				int xx = (i%3)*20, yy = (int)Math.floor(i/3f)*20;
				GlStateManager.color(1f, 1f, 1f, 1f);
				mc.getTextureManager().bindTexture(upgrade.getIcon());
				ClientUtils.drawTexturedRect(guiLeft+102+xx, guiTop+11+yy, 16, 16, 0d, 1d, 0d, 1d);
				i++;
			}

		}
		else
		{
			drawRect(guiLeft+99, guiTop+8, guiLeft+99+66, guiTop+8+56, 0xdf0e1c34);
			IIClientUtils.drawStringCentered(fontRenderer, getUpgradeNameTranslation(previewed), guiLeft+99, guiTop+8, 66, 1, 0xffffff);
		}

		//RotaryUtils.renderEnergyTooltip(tooltip, mx, my, guiLeft+148, guiTop+20, tile.rotation);

		ClientUtils.bindTexture(TEXTURE);
		this.drawTexturedModalRect(guiLeft+8, guiTop+5, 176, 0, 15, 15);
		this.drawTexturedModalRect(guiLeft+79, guiTop+5, 191, 0, 15, 15);
		this.drawTexturedModalRect(guiLeft+8, guiTop+66, 176, 15, 15, 15);
		this.drawTexturedModalRect(guiLeft+79, guiTop+66, 191, 15, 15, 15);

		drawTexturedModalRect(guiLeft+152, guiTop+1, 0, 179, 16, 11);

		if(info)
		{
			RenderHelper.enableGUIStandardItemLighting();
			int yy = (int)(Math.floor(previewed.getRequiredStacks().size()/4f)*18);
			for(int i = 0; i < previewed.getRequiredStacks().size(); i++)
			{
				int x = (i%4)*18;
				int y = (int)(Math.floor(i/4f)*18);
				ItemStack stack = previewed.getRequiredStacks().get(i).getExampleStack();
				stack.setCount(previewed.getRequiredStacks().get(i).inputSize);
				//drawString(fontRenderer,stack.getDisplayName(),guiLeft+xSize+x, guiTop+4-yy+y,0xffffff);
				mc.getRenderItem().renderItemAndEffectIntoGUI(stack, guiLeft+xSize+x+2, guiTop+112-yy+y);
				mc.getRenderItem().renderItemOverlayIntoGUI(fontRenderer, stack, guiLeft+xSize+x+2, guiTop+112-yy+y, null);
				if(IIMath.isPointInRectangle(guiLeft+xSize+x+2, guiTop+112-yy+y, guiLeft+xSize+x+18, guiTop+128-yy+y, mx, my))
					tooltip.addAll(stack.getTooltip(mc.player, mc.gameSettings.advancedItemTooltips?TooltipFlags.ADVANCED: TooltipFlags.NORMAL));
			}

			boolean uni = fontRenderer.getUnicodeFlag();
			fontRenderer.setUnicodeFlag(true);
			fontRenderer.drawSplitString(getUpgradeDescTranslation(previewed), guiLeft+xSize+2, guiTop+8, 76, IIReference.COLOR_H1.getPackedRGB());
			fontRenderer.setUnicodeFlag(uni);
		}

		for(GuiButton guiButton : buttonList)
		{
			guiButton.drawButton(mc, mx, my, partial);
		}

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}

	private String getUpgradeNameTranslation(MachineUpgrade upgrade)
	{
		return I18n.format("machineupgrade.immersiveintelligence."+upgrade.getName());
	}

	private String getUpgradeDescTranslation(MachineUpgrade upgrade)
	{
		return I18n.format("machineupgrade.immersiveintelligence."+upgrade.getName()+".desc");
	}

	public boolean hasItemsForUpgrade(MachineUpgrade upgrade)
	{
		if(upgrade==null)
			return false;

		if(mc.player.isCreative())
			return true;

		for(IngredientStack requiredStack : upgrade.getRequiredStacks())
		{
			int reqSize = requiredStack.inputSize;
			for(int slot = 0; slot < ManualUtils.mc().player.inventory.getSizeInventory(); slot++)
			{
				ItemStack inSlot = ManualUtils.mc().player.inventory.getStackInSlot(slot);
				if(!inSlot.isEmpty()&&requiredStack.matchesItemStackIgnoringSize(inSlot))
					if((reqSize -= inSlot.getCount()) <= 0)
						break;
			}

			if(reqSize > 0)
				return false;
		}
		return true;
	}

	@Nullable
	public ItemStack getPreviewedItem(int mouseX, int mouseY)
	{
		if(previewed!=null&&info)
		{
			int yy = (int)(Math.floor(previewed.getRequiredStacks().size()/4f)*18);
			for(int i = 0; i < previewed.getRequiredStacks().size(); i++)
			{
				int x = (i%4)*18;
				int y = (int)(Math.floor(i/4f)*18);
				if(IIMath.isPointInRectangle(guiLeft+xSize+x+2, guiTop+112-yy+y, guiLeft+xSize+x+18, guiTop+128-yy+y, mouseX, mouseY))
					return previewed.getRequiredStacks().get(i).getExampleStack();
			}
		}
		return null;
	}
}
