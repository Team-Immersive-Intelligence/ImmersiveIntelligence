package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoClipboardList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.clipboard.ClipboardEntry;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.clipboard.DecoClipboardUtils;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.TileEntityClipboard;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerClipboardTile;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIClipboard;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * DecoGUI editor for clipboard entries stored in the placed tile entity.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.09.2026
 */
@DecoTemplate(name = "clipboard_block", category = DecoGuiCategory.CLIPBOARD)
public class GuiClipboardBlock extends DecoTileGui<TileEntityClipboard, ContainerClipboardTile>
{
	private DecoClipboardList clipboardList;

	public GuiClipboardBlock(EntityPlayer player, TileEntityClipboard tile)
	{
		super(player, tile, IIGUI.CLIPBOARD_BLOCK);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 0, 176, 196)
				.withTitleBar(tile)
				.withNextLayer()
				.withBox(DecoTextures.BG_PAPER, DecoTextures.TEMPLATE_PAPER, 4, 4, 160+8, 172+8+8)

				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 196, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		addComponent((clipboardList = new DecoClipboardList(6, 6, ignored -> {
				}))
						.withSize(144+8+8+3, 156+8+16+8-2)
						.withEntries(DecoClipboardUtils.readEntries(tile.getEntries()))
						.withDropAction(this::entryFromCarriedStack)
		);
	}

	private ClipboardEntry entryFromCarriedStack()
	{
		ItemStack carried = playerContainer.getItemStack();
		return carried.isEmpty()?null: DecoClipboardUtils.createEntry(carried.copy());
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		return EasyNBT.newNBT().withTag(ItemIIClipboard.NBT_ENTRIES,
				DecoClipboardUtils.writeEntries(clipboardList.getEntries()));
	}
}
