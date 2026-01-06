package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.api.upgrade.IUpgradableDevice;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 10.07.2019
 */
public class ContainerUpgrade<T extends TileEntityIEBase & IIEInventory & IUpgradableDevice> extends ContainerIIBase<T>
{
	public ContainerUpgrade(EntityPlayer player, T tile)
	{
		super(player, tile);
		//Input/Output Slots

		addPlayerInventory(player.inventory, 9+32+16-8, 18+32+32+8-4+76-16+4+24);
	}
}
