package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityWoodenCrate;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 17.05.2019
 */
@ChestContainer
public class ContainerIICrate<T extends TileEntityWoodenCrate> extends ContainerIITileBase<T>
{
	public ContainerIICrate(EntityPlayer player, T tile)
	{
		super(player, tile);
		//Tile Inventory
		addSlotArray(8, 14, 0, tile.getInventory().size(), 9, DefaultInputSlot::new);
		//Player Inventory
		addPlayerInventory(player.inventory, 8, 87);
	}
}
