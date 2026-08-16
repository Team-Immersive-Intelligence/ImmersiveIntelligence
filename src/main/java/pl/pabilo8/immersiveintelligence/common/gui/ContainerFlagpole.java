package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFlagpole;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * Provides the shared inventory container for all flagpole GUI pages.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 14.08.2026
 * @ii-approved 0.3.1
 * @since 27.12.2025
 */
public class ContainerFlagpole extends ContainerIITileBase<TileEntityFlagpole>
{
	private ContainerFlagpole(EntityPlayer player, TileEntityFlagpole tile)
	{
		super(player, tile);
		addPlayerInventory(player.inventory, 40, 166);
	}

	public static Container getContainerForFlagpolePage(EntityPlayer player, TileEntityFlagpole tile)
	{
		return new ContainerFlagpole(player, tile);
	}

	public static Container getContainerForFactionPage(EntityPlayer player, TileEntityFlagpole tile)
	{
		return new ContainerFlagpole(player, tile);
	}

	public static Container getContainerForConfigPage(EntityPlayer player, TileEntityFlagpole tile)
	{
		return new ContainerFlagpole(player, tile);
	}
}
