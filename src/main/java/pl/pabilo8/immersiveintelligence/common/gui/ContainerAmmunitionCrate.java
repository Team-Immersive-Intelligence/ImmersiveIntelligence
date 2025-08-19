package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.api.tool.BulletHandler;
import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.items.ItemBullet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.item.ammo.gun.ItemIIAmmoMachinegun;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 17.05.2019
 * @since 08.18.2025
 */
public class ContainerAmmunitionCrate extends ContainerIIBase<TileEntityAmmunitionCrate>
{

	public Slot[] slotsInputbullet, slotsInputshell, slotsInputrevolver, slotsInputmg;

	public ContainerAmmunitionCrate(EntityPlayer player, TileEntityAmmunitionCrate tile)
	{
		//Normal bullet slots

		super(player, tile);
		for(int i = 0; i < 20; i++)

			this.slotsInputbullet[0] = addSlotToContainer(new Slot(this.inv, i, 8+(i%5)*18, 18+(i/5)*18)
			{
				/**
				 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
				 */
				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return stack.getItem() instanceof ItemBullet&&!(stack.isItemEqual(BulletHandler.emptyCasing)||stack.isItemEqual(BulletHandler.emptyShell));
				}
			});

		//Empty shell slots

		for(int i = 0; i < 9; i++)
			this.slotsInputshell[0] = addSlotToContainer(new Slot(this.inv, 20+i, 8+(i*18), 108)
			{
				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return false;
				}
			});

		//Revolver Layout Slots
		this.slotsInputrevolver[0] = addSlotToContainer(new GhostFilteredBullet(this, this.inv, 29, 125, 18));
		this.slotsInputrevolver[0] = addSlotToContainer(new GhostFilteredBullet(this, this.inv, 30, 144, 26));
		this.slotsInputrevolver[0] = addSlotToContainer(new GhostFilteredBullet(this, this.inv, 31, 152, 45));
		this.slotsInputrevolver[0] = addSlotToContainer(new GhostFilteredBullet(this, this.inv, 32, 144, 64));
		this.slotsInputrevolver[0] = addSlotToContainer(new GhostFilteredBullet(this, this.inv, 33, 125, 72));
		this.slotsInputrevolver[0] = addSlotToContainer(new GhostFilteredBullet(this, this.inv, 34, 106, 64));
		this.slotsInputrevolver[0] = addSlotToContainer(new GhostFilteredBullet(this, this.inv, 35, 98, 45));
		this.slotsInputrevolver[0] = addSlotToContainer(new GhostFilteredBullet(this, this.inv, 36, 106, 26));

		boolean mg = tile.hasUpgrade(IIContent.UPGRADE_MG_LOADER);
		if(mg)
		{
			for(int i = 0; i < 12; i++)
				this.slotsInputmg[0] = addSlotToContainer(new Slot(this.inv, i+30, 184+(i%2)*18, 18+(i/2)*18)
				{
					/**
					 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
					 */
					@Override
					public boolean isItemValid(ItemStack stack)
					{
						return stack.getItem() instanceof ItemIIAmmoMachinegun;
					}
				});
		}

		addPlayerInventory(player.inventory, 8, 141);
	}

	public static class GhostFilteredBullet extends IESlot.Ghost
	{
		public GhostFilteredBullet(Container container, IInventory inv, int id, int x, int y)
		{
			super(container, inv, id, x, y);
		}

		/**
		 * Helper method to put a stack in the slot.
		 */
		@Override
		public void putStack(ItemStack itemStack)
		{
			super.putStack(itemStack);
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return stack.getItem() instanceof ItemBullet&&!(stack.isItemEqual(BulletHandler.emptyCasing)||stack.isItemEqual(BulletHandler.emptyShell));
		}
	}
}
