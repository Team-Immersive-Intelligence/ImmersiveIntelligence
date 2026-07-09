package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 10.07.2019
 * @since 08.13.2025
 */
public class ContainerAmmunitionAssembler extends ContainerIITileBase<TileEntityAmmunitionAssembler>
{
	public Slot casingSlot, coreSlot, outputSlot;

	public ContainerAmmunitionAssembler(EntityPlayer player, TileEntityAmmunitionAssembler tile)
	{
		super(player, tile);

		coreSlot = addSlot(24+8, 20-10+8, MultiblockAmmunitionAssembler.SLOT_CORE);
		casingSlot = addSlot(24+8, 60-10-4, MultiblockAmmunitionAssembler.SLOT_CASING);
		outputSlot = addSlot(24+8+64+8+2+1-48-2-4+64+2, 28+4+4+4+1-16+8, MultiblockAmmunitionAssembler.SLOT_OUTPUT);

		addPlayerInventory(player.inventory, 8, 87+24);

	}
}
