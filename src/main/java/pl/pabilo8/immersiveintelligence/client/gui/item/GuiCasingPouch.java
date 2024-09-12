package pl.pabilo8.immersiveintelligence.client.gui.item;

import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerCasingPouch;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8
 * @since 25.09.2023
 */
public class GuiCasingPouch extends GuiIEContainerBase
{
	private static final ResLoc texture = IIReference.RES_TEXTURES_GUI.with("casing_pouch").withExtension(ResLoc.EXT_PNG);
	private final ItemStack pouch;

	public GuiCasingPouch(EntityPlayer player, ItemStack heldStack, EnumHand hand)
	{
		super(new ContainerCasingPouch(player, heldStack, hand));
		this.pouch = heldStack;
		xSize = 176;
		ySize = 173;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		IIClientUtils.drawStringCentered(this.fontRenderer, this.pouch.getDisplayName(), 0, 6, xSize, 0, 0xffc596);
//		this.fontRenderer.drawString(this.pouch.getDisplayName(), 8, 6, 0xffc596);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		IIClientUtils.bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

	}
}
