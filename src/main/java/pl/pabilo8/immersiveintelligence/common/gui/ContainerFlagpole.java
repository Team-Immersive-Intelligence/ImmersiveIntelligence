package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFlagpole;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 27.12.2025
 */
public class ContainerFlagpole extends ContainerIIBase<TileEntityFlagpole>
{
	public ContainerFlagpole(EntityPlayer player, TileEntityFlagpole tile)
	{
		super(player, tile);
		addPlayerInventory(player.inventory, 40, 166);
	}
}
