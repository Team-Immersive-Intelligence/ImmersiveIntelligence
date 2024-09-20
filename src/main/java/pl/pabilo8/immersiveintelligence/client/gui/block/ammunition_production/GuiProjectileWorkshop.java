package pl.pabilo8.immersiveintelligence.client.gui.block.ammunition_production;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonDropdownList;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class GuiProjectileWorkshop extends GuiAmmunitionBase<TileEntityProjectileWorkshop>
{
	GuiButtonDropdownList typeList = null, bulletList = null;
	private GuiTextField valueEdit;
	ItemStack exampleStack = ItemStack.EMPTY;
	boolean hasFillerUpgrade;
	int coreIconID = 0;

	public GuiProjectileWorkshop(EntityPlayer player, TileEntityProjectileWorkshop tile)
	{
		super(player, tile, ContainerProjectileWorkshop::new);
		IIPacketHandler.sendToServer(
				new MessageBooleanAnimatedPartsSync(true, blusunrize.immersiveengineering.common.util.Utils.RAND.nextInt(2), tile.getPos()));
		hasFillerUpgrade = tile.hasUpgrade(IIContent.UPGRADE_CORE_FILLER);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		if(!hasFillerUpgrade)
		{
			addLabel(guiLeft+122, guiTop+5+5, IIReference.COLOR_H1, "Core:");
			addLabel(guiLeft+122, guiTop+5+32-10+5, IIReference.COLOR_H1, "Type:");

			//Ammo types
			String[] names = AmmoRegistry.getAllAmmoItems().stream().map(IAmmoTypeItem::getName).toArray(String[]::new);
			bulletList = new GuiButtonDropdownList(buttonList.size(), guiLeft+122, guiTop+20-6, 136, 12, 6, names);
			bulletList.setTranslationFunc(s -> I18n.format("item.immersiveintelligence."+s.toLowerCase()+".bullet.name"));

			//Core types
			String[] cores = Arrays.stream(tile.producedAmmo.getAllowedCoreTypes()).map(CoreType::getName).toArray(String[]::new);
			typeList = new GuiButtonDropdownList(buttonList.size(), guiLeft+122, guiTop+20+32-8-7, 136, 12, 3, cores);
			typeList.setTranslationFunc(s -> I18n.format(IIReference.DESCRIPTION_KEY+"bullet_core_type."+s));
			typeList.selectedEntry = Arrays.asList(cores).indexOf(tile.coreType.getName());
			addButton(typeList);

			bulletList.selectedEntry = Arrays.asList(names).indexOf(tile.producedAmmo.getName());
			addButton(bulletList);

			IAmmoTypeItem<?, ?> bullet = AmmoRegistry.getAmmoItem(bulletList.getEntry(bulletList.selectedEntry));
			exampleStack = bullet==null?ItemStack.EMPTY:
					bullet.getAmmoCoreStack(IIContent.ammoCoreBrass, CoreType.v(typeList.getEntry(typeList.selectedEntry)));
			coreIconID = tile.coreType.ordinal();
		}
		else
		{
			valueEdit = new GuiTextField(buttonList.size(), fontRenderer, guiLeft+122, guiTop+20+32-8-7+11, 72, 12);
			this.valueEdit.setFocused(false);
			this.valueEdit.setText(String.valueOf(tile.fillAmount));
			this.valueEdit.updateCursorCounter();

			addLabel(guiLeft+122, guiTop+5+5+22+11, IIReference.COLOR_H1, "Insert Amount:");
		}

		addLabel(guiLeft+1, guiTop+8, 118, 0, IIReference.COLOR_H1, I18n.format("tile.immersiveintelligence.metal_multiblock1.projectile_workshop.name")).setCentered();
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(!hasFillerUpgrade||!this.valueEdit.textboxKeyTyped(typedChar, keyCode))
			super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if(!hasFillerUpgrade||!this.valueEdit.mouseClicked(mouseX, mouseY, mouseButton))
			super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		if(!hasFillerUpgrade)
		{
			if(button==typeList)
			{
				sendList("core_type", typeList.getEntry(typeList.selectedEntry));
			}
			else if(button==bulletList)
			{
				sendList("produced_bullet", bulletList.getEntry(bulletList.selectedEntry));

				IAmmoTypeItem<?, ?> bullet = AmmoRegistry.getAmmoItem(bulletList.getEntry(bulletList.selectedEntry));
				String selectedType = typeList.getEntry(typeList.selectedEntry);

				int id = typeList.id;
				buttonList.remove(typeList);

				//reset
				String[] cores = Arrays.stream(bullet.getAllowedCoreTypes()).map(CoreType::getName).toArray(String[]::new);
				typeList = new GuiButtonDropdownList(id, guiLeft+122, guiTop+20+32-8-7, 72, 12, 3, cores);
				typeList.setTranslationFunc(s -> I18n.format(IIReference.DESCRIPTION_KEY+"bullet_core_type."+s));
				typeList.selectedEntry = Math.max(Arrays.asList(cores).indexOf(selectedType), 0);
				this.buttonList.add(id, typeList);

				sendList("core_type", typeList.getEntry(typeList.selectedEntry));

			}

			coreIconID = CoreType.v(typeList.getEntry(typeList.selectedEntry)).ordinal();

			IAmmoTypeItem<?, ?> bullet = AmmoRegistry.getAmmoItem(bulletList.getEntry(bulletList.selectedEntry));
			exampleStack = bullet==null?ItemStack.EMPTY:
					bullet.getAmmoCoreStack(IIContent.ammoCoreBrass, CoreType.v(typeList.getEntry(typeList.selectedEntry)));
		}
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		IIClientUtils.bindTexture(TEXTURE);
		if(hasFillerUpgrade)
		{
			drawTexturedModalRect(guiLeft+6, guiTop+9+32, 224, 0, 20, 23); //in core
			drawTexturedModalRect(guiLeft+6+44-8, guiTop+15, 220, 0, 24, 23); //in component
			drawTexturedModalRect(guiLeft+6+64+21, guiTop+9+32, 224, 23, 20, 23); //out
			drawTexturedModalRect(guiLeft+6+44+22-4, guiTop+15+3, 62, 230, 49, 20); //tank back

			drawTexturedModalRect(guiLeft+6+22, guiTop+9+30, 123, 176, 62, 34); //progress back

			if(tile.currentProcess!=null)
				drawTexturedModalRect(guiLeft+6+22, guiTop+9+30, 0, 176,
						(int)(62*tile.getProductionProgress(tile.currentProcess, partialTicks)), 34); //progress top

			GlStateManager.enableBlend();
			ClientUtils.handleGuiTank(tile.tanksFiller, guiLeft+6+44+22-4, guiTop+15+3, 49, 20, 62, 210, 49, 20, mouseX, mouseY, TEXTURE.toString(), null);
			GlStateManager.disableBlend();

			AmmoComponent component = tile.componentInside.getComponent();
			if(component!=null)
			{
				IIColor color = tile.componentInside.getColor();
				IIClientUtils.drawGradientBar(guiLeft+6+44-6, guiTop+20, 2, 16, color, color, tile.componentInside.getAmountPercentage());

				IIClientUtils.drawStringCentered(fontRenderer, tile.componentInside.getTranslatedName(), guiLeft+122, guiTop+5, 71, 0, IIReference.COLOR_H1.getPackedRGB());
				fontRenderer.drawString(TextFormatting.ITALIC+I18n.format(IIReference.DESCRIPTION_KEY+"bullet_type."+component.getRole().getName())+TextFormatting.RESET,
						guiLeft+122, guiTop+5+11, IIReference.COLOR_H2.getPackedRGB());
			}

			RenderHelper.enableGUIStandardItemLighting();
			if(tile.currentProcess!=null)
				itemRender.renderItemIntoGUI(tile.currentProcess.recipe.getEffect(), guiLeft+6+64+21+2, guiTop+9+32+4);
			RenderHelper.disableStandardItemLighting();

			valueEdit.drawTextBox();
		}
		else
		{
			drawTexturedModalRect(guiLeft+6, guiTop+29+6, 224, 0, 20, 23); //in
			drawTexturedModalRect(guiLeft+6+64+21, guiTop+29+6, 224, 23, 20, 23); //out

			drawTexturedModalRect(guiLeft+6+22, guiTop+10+6, 123, 210, 62, 41); //progress back

			if(tile.currentProcess!=null)
			{
				drawTexturedModalRect(guiLeft+6+22, guiTop+10+6, 0, 210,
						(int)(62*tile.getProductionProgress(tile.currentProcess, partialTicks)), 41); //progress top
			}

			IIClientUtils.bindTexture(TEXTURE_ICONS);
			drawTexturedModalRect(guiLeft+122+16, guiTop+5+48, 16*coreIconID, 30, 16, 16); //fuse icon

			RenderHelper.enableGUIStandardItemLighting();
			if(tile.currentProcess!=null)
				itemRender.renderItemIntoGUI(tile.currentProcess.recipe.getEffect(), guiLeft+6+64+21+2, guiTop+29+6+4);
			itemRender.renderItemIntoGUI(exampleStack, guiLeft+122, guiTop+5+48);
			RenderHelper.disableStandardItemLighting();
		}
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();

		if(hasFillerUpgrade)
		{
			try
			{
				IIPacketHandler.sendToServer(new MessageIITileSync(tile,
						EasyNBT.newNBT().withInt("fill_amount", Integer.parseInt(valueEdit.getText()))
				));
			} catch(NumberFormatException ignored) {}
		}

		IIPacketHandler.sendToServer(new MessageBooleanAnimatedPartsSync(false, 0, tile.getPos()));
		IIPacketHandler.sendToServer(new MessageBooleanAnimatedPartsSync(false, 1, tile.getPos()));
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
	}

	@Override
	ArrayList<String> drawTooltip(int mx, int my, ArrayList<String> tooltip)
	{
		if(tile.currentProcess!=null)
		{
			if(hasFillerUpgrade?
					isPointInRegion(6+64+21+2, 9+32+4, 16, 16, mx, my):
					isPointInRegion(6+64+21+2, 29+6+4, 16, 16, mx, my))
				tooltip.addAll(tile.currentProcess.recipe.getEffect().getTooltip(ClientUtils.mc().player, mc.gameSettings.advancedItemTooltips?TooltipFlags.ADVANCED: TooltipFlags.NORMAL));
		}
		if(hasFillerUpgrade)
			ClientUtils.handleGuiTank(tile.tanksFiller, guiLeft+6+44+22-4, guiTop+15+3, 49, 20, 62, 210, 49, 20, mx, my, TEXTURE.toString(), tooltip);
		return super.drawTooltip(mx, my, tooltip);
	}
}
