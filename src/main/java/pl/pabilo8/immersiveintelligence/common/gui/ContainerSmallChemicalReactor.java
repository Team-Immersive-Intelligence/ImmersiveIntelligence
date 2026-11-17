package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntitySmallChemicalReactor;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * @author Avalon (avalon@iiteam.net)
 * @since 17.11.2026
 */

public class ContainerSmallChemicalReactor extends ContainerIITileBase<TileEntitySmallChemicalReactor>
{
	public ContainerSmallChemicalReactor(EntityPlayer player, TileEntitySmallChemicalReactor tile)
	{
		super(player, tile);
		addPlayerInventory(player.inventory, 8, 87);
	}
}
