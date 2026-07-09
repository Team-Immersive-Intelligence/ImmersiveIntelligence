package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCartStation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 17.05.2019
 * @since 08.14.2025
 */
public class ContainerSkycrateStation extends ContainerIITileBase<TileEntitySkyCrateStation>
{

	public Slot inputSlot;

	public ContainerSkycrateStation(EntityPlayer player, TileEntitySkyCrateStation tile)
	{
		super(player, tile);
		this.addSlotArray(52+12-2, 32, 0, TileEntitySkyCartStation.GEAR_SLOTS, 5, MotorGearSlot::new);

		this.addPlayerInventory(player.inventory, 8, 87);

	}
}

