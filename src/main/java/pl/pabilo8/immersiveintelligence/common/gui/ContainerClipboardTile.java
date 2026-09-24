package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.TileEntityClipboard;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * Inventory bridge for the placed Engineer's Clipboard.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.09.2026
 */
public class ContainerClipboardTile extends ContainerIITileBase<TileEntityClipboard>
{
	public ContainerClipboardTile(EntityPlayer player, TileEntityClipboard tile)
	{
		super(player, tile);
		addPlayerInventory(player.inventory, 8, 216-8);
	}
}
