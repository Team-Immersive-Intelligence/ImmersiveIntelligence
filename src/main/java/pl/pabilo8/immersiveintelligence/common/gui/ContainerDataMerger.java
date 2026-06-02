package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMerger;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 02.06.2026
 * @since 30.06.2019
 */
public class ContainerDataMerger extends ContainerIITileBase<TileEntityDataMerger>
{
	public ContainerDataMerger(EntityPlayer player, TileEntityDataMerger tile)
	{
		super(player, tile);
		this.addPlayerInventory(player.inventory, 8, 86);
	}

}
