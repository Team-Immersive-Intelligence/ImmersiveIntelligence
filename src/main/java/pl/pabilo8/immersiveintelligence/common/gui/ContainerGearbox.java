package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityGearbox;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 17.05.2019
 */
public class ContainerGearbox extends ContainerIIBase<TileEntityGearbox>
{
	public ContainerGearbox(EntityPlayer player, TileEntityGearbox tile)
	{
		super(player, tile);
		this.addSlotArray(52+12-2, 29, 0, TileEntityGearbox.GEAR_SLOTS, 5, MotorGearSlot::new);

		this.addPlayerInventory(player.inventory, 8, 87);
	}
}