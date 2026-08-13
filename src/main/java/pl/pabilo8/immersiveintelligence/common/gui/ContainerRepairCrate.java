package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityRepairCrate;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @updated 13.08.2026
 * @ii-approved 0.3.1
 * @since 17.05.2019
 */
public class ContainerRepairCrate extends ContainerIITileBase<TileEntityRepairCrate>
{
	public Slot[] slots;

	public ContainerRepairCrate(EntityPlayer player, TileEntityRepairCrate tile)
	{
		super(player, tile);

		//Machine slots
		this.slots = addSlotArray(6+32+8+8, 4+6, 0, tile.inventory.size(), 4);

		//Player inventory
		boolean upgrade = tile.isUpgradeInstalled(IIContent.UPGRADE_INSERTER);
		addPlayerInventory(player.inventory, 8, 100+8+(upgrade?24: 0));
	}
}
