package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataRouter;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 02.06.2026
 */
public class ContainerDataRouter extends ContainerIITileBase<TileEntityDataRouter>
{
	private ContainerDataRouter(EntityPlayer player, TileEntityDataRouter tile, boolean isEditMode)
	{
		super(player, tile);
		this.addPlayerInventory(player.inventory, isEditMode?40: 8, isEditMode?184+16-2+12-2: 176-2);
	}

	public static ContainerDataRouter getMainGui(EntityPlayer player, TileEntityDataRouter tile)
	{
		return new ContainerDataRouter(player, tile, false);
	}

	public static ContainerDataRouter getEditGui(EntityPlayer player, TileEntityDataRouter tile)
	{
		return new ContainerDataRouter(player, tile, true);
	}
}
