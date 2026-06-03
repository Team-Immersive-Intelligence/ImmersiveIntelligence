package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityVulcanizer;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 10.07.2019
 * @since 08.12.2025
 */
public class ContainerVulcanizer extends ContainerIITileBase<TileEntityVulcanizer>
{
	public Slot[] slotInput;

	public ContainerVulcanizer(EntityPlayer player, TileEntityVulcanizer tile)
	{
		super(player, tile);
		slotInput = addSlotArray(6+2, 19-4-8+2, 0, 3, 1, 22, IISlot::new);

		addPlayerInventory(player.inventory, 8, 87);
	}
}
