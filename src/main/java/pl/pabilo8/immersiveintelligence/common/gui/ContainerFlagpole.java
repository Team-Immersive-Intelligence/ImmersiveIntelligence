package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFlagpole;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 27.12.2025
 */
public class ContainerFlagpole extends ContainerIIBase<TileEntityFlagpole>
{
	private ContainerFlagpole(EntityPlayer player, TileEntityFlagpole tile, boolean faction)
	{
		super(player, tile);
		addPlayerInventory(player.inventory, 40, faction?(166+32): 166);
	}

	public static Container getContainerForFlagpolePage(EntityPlayer player, TileEntityFlagpole tile)
	{
		return new ContainerFlagpole(player, tile, false);
	}

	public static Container getContainerForFactionPage(EntityPlayer player, TileEntityFlagpole tile)
	{
		return new ContainerFlagpole(player, tile, true);
	}
}
