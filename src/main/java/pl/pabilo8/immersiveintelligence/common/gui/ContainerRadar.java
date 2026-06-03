package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRadar;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 10.07.2019
 */
public class ContainerRadar extends ContainerIITileBase<TileEntityRadar>
{
	public ContainerRadar(EntityPlayer player, TileEntityRadar tile)
	{
		super(player, tile);
		addPlayerInventory(player.inventory, 40, 166);
	}
}
