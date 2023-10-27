package pl.pabilo8.immersiveintelligence.client.gui.block.ammunition_production;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityAmmunitionWorkshop;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerAmmunitionWorkshop;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class GuiAmmunitionWorkshop extends GuiAmmunitionBase<TileEntityAmmunitionWorkshop>
{
	private GuiTextField valueEdit;
	HashMap<GuiButtonState, EnumFuseTypes> fuseButtons = new HashMap<>();

	public GuiAmmunitionWorkshop(EntityPlayer player, TileEntityAmmunitionWorkshop tile)
	{
		super(player, tile, ContainerAmmunitionWorkshop::new);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		labelList.clear();
		buttonList.clear();
		fuseButtons.clear();

		addLabel(guiLeft+1, guiTop+8, 118, 0, IIReference.COLOR_H1, I18n.format("tile.immersiveintelligence.metal_multiblock1.ammunition_workshop.name")).setCentered();

		int i = 0;
		for(EnumFuseTypes fuse : EnumFuseTypes.values())
		{
			fuseButtons.put(
					addButton(new GuiButtonState(buttonList.size(),
							guiLeft+122+(i%3)*21, guiTop+5+5+4+(int)(Math.floor(i/3f)*20), 20, 20,
							"", tile.fuse==fuse, TEXTURE.toString(), 220, 46, 1)),
					fuse);
			i++;
		}

		switch(tile.fuse)
		{
			case TIMED:
			case PROXIMITY:
			{
				this.valueEdit = new GuiTextField(buttonList.size(), fontRenderer, guiLeft+122, guiTop+20+32-8-7+11, 72, 12);
				this.valueEdit.setFocused(false);
				this.valueEdit.setText(String.valueOf(tile.fuseConfig));
				this.valueEdit.updateCursorCounter();
			}
			break;
			case CONTACT:
				this.valueEdit = null;
				break;
		}

		addLabel(guiLeft+122, guiTop+5+5, IIReference.COLOR_H1, "Fuse:");
		addLabel(guiLeft+122, guiTop+5+5+32, IIReference.COLOR_H1, "Parameters:");
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(this.valueEdit==null||!this.valueEdit.textboxKeyTyped(typedChar, keyCode))
			super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if(this.valueEdit==null||!this.valueEdit.mouseClicked(mouseX, mouseY, mouseButton))
			super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		if(button instanceof GuiButtonState)
		{
			tile.fuse = fuseButtons.get(button);
			initGui();
		}
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		super.drawGuiContainerBackgroundLayer(f, mx, my);
		IIClientUtils.bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft+6, guiTop+9+6, 224, 0, 20, 23); //in top
		drawTexturedModalRect(guiLeft+6, guiTop+9+38+2+6, 224, 0, 20, 23); //in bottom
		drawTexturedModalRect(guiLeft+6+64+21, guiTop+29+6, 224, 23, 20, 23); //out

		drawTexturedModalRect(guiLeft+6+22, guiTop+9+16+6, 185, 176, 61, 34); //progress back

		if(tile.processTimeMax!=0)
			drawTexturedModalRect(guiLeft+6+22, guiTop+9+16+6, 62, 176, (int)(61*(tile.processTime/(float)tile.processTimeMax)), 34); //progress top

		RenderHelper.enableGUIStandardItemLighting();
		itemRender.renderItemIntoGUI(tile.effect, guiLeft+6+64+21+2, guiTop+29+6+4);
		RenderHelper.disableStandardItemLighting();

		if(valueEdit!=null)
			valueEdit.drawTextBox();
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
	}

	@Override
	ArrayList<String> drawTooltip(int mx, int my, ArrayList<String> tooltip)
	{
		IIClientUtils.bindTexture(TEXTURE);

		GlStateManager.color(1f, 1f, 1f, 1f);
		RenderHelper.disableStandardItemLighting();
		for(int i = 0; i < EnumFuseTypes.values().length; i++)
			drawTexturedModalRect(guiLeft+122+2+(i%3)*21, guiTop+5+5+4+2+(int)Math.floor(i/20f), 221+(i%2)*16, 86+(int)(Math.floor(i/2f)*16), 16, 16);

		if(!tile.effect.isEmpty())
		{
			if(isPointInRegion(6+64+21+2, 29+6+4, 16, 16, mx, my))
				tooltip.addAll(tile.effect.getTooltip(ClientUtils.mc().player, mc.gameSettings.advancedItemTooltips?TooltipFlags.ADVANCED: TooltipFlags.NORMAL));
		}
		fuseButtons.forEach((button, enumFuseTypes) -> {
			if(button.isMouseOver())
				tooltip.add(enumFuseTypes.name());
		});

		return super.drawTooltip(mx, my, tooltip);
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();

		if(valueEdit!=null)
			valueEdit.setText(String.valueOf(tile.fuseConfig));

		IIPacketHandler.sendToServer(new MessageIITileSync(tile, EasyNBT.newNBT()
				.withString("fuse", tile.fuse.getName())
				.conditionally(valueEdit!=null, e -> e.withInt("fuse_config", Integer.parseInt(valueEdit.getText())))
		));
	}
}
