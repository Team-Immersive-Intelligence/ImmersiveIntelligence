package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class ContainerUpgrade extends ContainerIEBase
{
	public <T extends TileEntity&IUpgradableMachine>ContainerUpgrade(EntityPlayer player, T tile)
	{
		super(player.inventory, tile);
		//Input/Output Slots

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(player.inventory, j+i*9+9, 8+j*18, 87+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(player.inventory, i, 8+i*18, 145));
	}
}
