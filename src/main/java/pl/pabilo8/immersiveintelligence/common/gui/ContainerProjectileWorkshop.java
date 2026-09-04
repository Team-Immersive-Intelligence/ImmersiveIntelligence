package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 10.07.2019
 * @since 08.13.2025
 */
public class ContainerProjectileWorkshop extends ContainerIITileBase<TileEntityProjectileWorkshop>
{
	private static final int CORE_PLAYER_INVENTORY_X = 76;

	public Slot inputSlot, componentInputSlot;

	public ContainerProjectileWorkshop(EntityPlayer player, TileEntityProjectileWorkshop tile)
	{
		super(player, tile);

		if(tile.isUpgradeInstalled(IIContent.UPGRADE_CORE_FILLER))
		{
			inputSlot = addSlot(8+2+2, 45-8, MultiblockProjectileWorkshop.SLOT_INPUT);
			componentInputSlot = addSlot(176/2-9-1, 12+4, MultiblockProjectileWorkshop.SLOT_COMPONENT_INPUT);
			addPlayerInventory(player.inventory, 8+32+8+4-44, 141+8);
		}
		else
		{
			inputSlot = addSlot(8, 8+2, MultiblockProjectileWorkshop.SLOT_INPUT);
			addPlayerInventory(player.inventory, CORE_PLAYER_INVENTORY_X, 141+8);
		}

	}
}
