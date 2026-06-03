package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.IESlot.Output;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRedstoneDataInterface;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.06.2019
 */
public class ContainerRedstoneDataInterface extends ContainerIITileBase<TileEntityRedstoneDataInterface>
{
	public Slot punchtapeInput, punchtapeOutput;

	private ContainerRedstoneDataInterface(EntityPlayer player, TileEntityRedstoneDataInterface tile, boolean dataSection)
	{
		super(player, tile);

		punchtapeInput = addSlot(8, 12+4, dataSection?MultiblockRedstoneInterface.SLOT_PUNCHTAPE_DATA: MultiblockRedstoneInterface.SLOT_PUNCHTAPE_REDSTONE,
				FilteredDataInput::new);
		punchtapeOutput = addSlot(8, 72+32, MultiblockRedstoneInterface.SLOT_PUNCHTAPE_OUTPUT, Output::new);

		addPlayerInventory(player.inventory, 8+16, 141+8);
	}

	public static ContainerRedstoneDataInterface getDataGUI(EntityPlayer player, TileEntityRedstoneDataInterface tile)
	{
		return new ContainerRedstoneDataInterface(player, tile, true);
	}

	public static ContainerRedstoneDataInterface getRedstoneGUI(EntityPlayer player, TileEntityRedstoneDataInterface tile)
	{
		return new ContainerRedstoneDataInterface(player, tile, false);
	}
}
