package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 10.07.2019
 * @since 08.13.2025
 */
public class ContainerAmmunitionAssembler extends ContainerIIBase<TileEntityAmmunitionAssembler>
{
	public Slot inputSlot, outputSlot;

	public ContainerAmmunitionAssembler(EntityPlayer player, TileEntityAmmunitionAssembler tile)
	{
		super(player, tile);

		inputSlot = addSlot(8, 20-10, 0);
		outputSlot = addSlot(8, 60-10, 1);

		addPlayerInventory(player.inventory, 8, 87);

	}
}
