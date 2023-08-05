package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerCrate;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityMetalCrate;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
@ChestContainer
public class ContainerMetalCrate extends ContainerCrate
{
	public ContainerMetalCrate(EntityPlayer player, TileEntityMetalCrate tile)
	{
		super(player.inventory, tile);
	}
}
