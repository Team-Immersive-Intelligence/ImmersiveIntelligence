package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityInserterBase;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 20.01.2026
 */
public class ContainerInserter extends ContainerIITileBase<TileEntityInserterBase>
{
	public ContainerInserter(EntityPlayer player, TileEntityInserterBase tile)
	{
		super(player, tile);
		addPlayerInventory(player.inventory, 8+32, 86+64+16+8);
	}
}
