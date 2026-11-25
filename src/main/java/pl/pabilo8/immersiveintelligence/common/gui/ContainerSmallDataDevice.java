package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional;

/**
 * Supplies the player inventory below a small data device screen.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.10.2026
 */
public class ContainerSmallDataDevice<T extends TileEntityIIDirectional & IIEInventory> extends ContainerIITileBase<T>
{
	public ContainerSmallDataDevice(EntityPlayer player, T tile)
	{
		this(player, tile, 168);
	}

	public ContainerSmallDataDevice(EntityPlayer player, T tile, int deviceHeight)
	{
		super(player, tile);
		addPlayerInventory(player.inventory, 40, deviceHeight+14);
	}

	public ContainerSmallDataDevice(EntityPlayer player, T tile, boolean narrow)
	{
		super(player, tile);
		addPlayerInventory(player.inventory, narrow?8: 40, 182);
	}
}
