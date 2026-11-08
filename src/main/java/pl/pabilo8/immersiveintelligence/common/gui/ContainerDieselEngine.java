package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntityDieselEngine;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * Provides the Diesel Engine controls and player inventory.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.10.2026
 */
public class ContainerDieselEngine extends ContainerIITileBase<TileEntityDieselEngine>
{
	public ContainerDieselEngine(EntityPlayer player, TileEntityDieselEngine tile)
	{
		super(player, tile);
		addPlayerInventory(player.inventory, 8, 87);
	}
}
