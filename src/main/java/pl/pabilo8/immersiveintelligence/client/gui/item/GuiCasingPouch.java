package pl.pabilo8.immersiveintelligence.client.gui.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoItemGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoItemStackDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerCasingPouch;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casing;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 25.09.2023
 */
@DecoTemplate(name = "casing_pouch", category = DecoGuiCategory.GENERIC_TILE)
public class GuiCasingPouch extends DecoItemGui<ContainerCasingPouch>
{
	public GuiCasingPouch(EntityPlayer player, ItemStack heldStack, EnumHand hand)
	{
		super(player, heldStack, hand, IIGUI.CASING_POUCH);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL_ROUGH, DecoTextures.TEMPLATE_ROUND, 0, 0, 176, 5*16, IIColor.fromPackedRGB(0x8dae77))
				.withStandaloneFrame(6, 4, 176-12, 5*16-10, DecoTextures.FRAME_CORNERS_BRASS, 4, true)
				.withTitleBar(itemStack)
				.withInventorySlots(SlotStyle.VANILLA, container.slotsCasing)
				.withInventorySlots(SlotStyle.VANILLA, container.slotsMagazine)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 5*16, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		DecoPanel infoPanel = addComponent(new DecoPanel(64*2+8-2, 14)
				.withSize(20, 16*4-6)
				.withBackground(DecoTextures.BG_PAPER)
				.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
		);
		infoPanel.addComponent(new DecoItemStackDisplay(2, 2)
				.withStack(IIContent.itemAmmoCasing.getStack(Casing.MG_2BCAL))
				.withOnTooltip(null)
		);
		infoPanel.addComponent(new DecoItemStackDisplay(2, 20)
				.withStack(IIContent.itemAmmoCasing.getStack(Casing.MG_2BCAL))
				.withOnTooltip(null)
		);
		infoPanel.addComponent(new DecoItemStackDisplay(2, 19+19+1)
				.withStack(IIContent.itemBulletMagazine.getStack(Magazines.MACHINEGUN))
				.withOnTooltip(null)
		);
	}
}
