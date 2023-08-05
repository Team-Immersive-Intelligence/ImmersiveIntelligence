package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerCrate;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.TileEntitySmallCrate;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
@ChestContainer
public class ContainerSmallCrate extends ContainerCrate
{
	public ContainerSmallCrate(EntityPlayer player, TileEntitySmallCrate tile)
	{
		super(player.inventory, tile);
	}
}