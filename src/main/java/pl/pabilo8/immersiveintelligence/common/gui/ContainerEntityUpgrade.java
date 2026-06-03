package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.api.upgrade.IUpgradableDevice;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIEntityBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 10.07.2019
 */
public class ContainerEntityUpgrade<E extends Entity & IIEInventory & IUpgradableDevice> extends ContainerIIEntityBase<E>
{
	public ContainerEntityUpgrade(EntityPlayer player, E tile)
	{
		super(player, tile);
		//Input/Output Slots

		addPlayerInventory(player.inventory, 9+32+16-8, 18+32+32+8-4+76-16+4+24+8);
	}
}
