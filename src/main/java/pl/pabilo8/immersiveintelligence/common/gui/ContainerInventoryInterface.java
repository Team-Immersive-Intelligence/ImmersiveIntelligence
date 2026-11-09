package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityInventoryInterface;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * Supplies the player inventory beneath the rule list and editor.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 27.10.2026
 */
public class ContainerInventoryInterface extends ContainerIITileBase<TileEntityInventoryInterface>
{
	private ContainerInventoryInterface(EntityPlayer player, TileEntityInventoryInterface tile, int width, int height)
	{
		super(player, tile);
		addPlayerInventory(player.inventory, (width-176)/2+8, height+14);
	}

	public static ContainerInventoryInterface getMainGui(EntityPlayer player, TileEntityInventoryInterface tile)
	{
		return new ContainerInventoryInterface(player, tile, 320, 144);
	}

	public static ContainerInventoryInterface getEditGui(EntityPlayer player, TileEntityInventoryInterface tile)
	{
		return new ContainerInventoryInterface(player, tile, 240, 176);
	}

	public static ContainerInventoryInterface getOutputGui(EntityPlayer player, TileEntityInventoryInterface tile)
	{
		return new ContainerInventoryInterface(player, tile, 240, 144);
	}
}
