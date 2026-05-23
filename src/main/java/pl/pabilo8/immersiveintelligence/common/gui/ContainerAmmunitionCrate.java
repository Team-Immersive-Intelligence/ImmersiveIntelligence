package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.api.tool.BulletHandler;
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
	public Slot[] inputBullet, inputShell, inputRevolver, inputMG;

	public ContainerAmmunitionCrate(EntityPlayer player, TileEntityAmmunitionCrate tile)
	{
		super(player, tile);

		this.inputBullet = new Slot[20];
		this.inputShell = new Slot[9];
		this.inputRevolver = new Slot[8];
		this.inputMG = new Slot[12];

		//Normal bullet slots (20 slots, 4x5 grid)
		for(int i = 0; i < 20; i++)
			this.inputBullet[i] = addSlotToContainer(new Slot(this.inv, i, 8+(i%5)*18, 18+(i/5)*18)
			{
				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return stack.getItem() instanceof ItemBullet&&
							!(stack.isItemEqual(BulletHandler.emptyCasing)||stack.isItemEqual(BulletHandler.emptyShell));
				}
			});

		//Empty shell slots (9 slots in a row)
		for(int i = 0; i < 9; i++)
			this.inputShell[i] = addSlotToContainer(new Slot(this.inv, 20+i, 8+(i*18), 108)
			{
				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return false;
				}
			});

		//Revolver layout slots (8 slots, circular layout)
		this.inputRevolver[0] = addSlotToContainer(new GhostFilteredBullet(this, this.inv, 29, 125, 18));
		this.inputRevolver[1] = addSlotToContainer(new GhostFilteredBullet(this, this.inv, 30, 144, 26));
		this.inputRevolver[2] = addSlotToContainer(new GhostFilteredBullet(this, this.inv, 31, 152, 45));
		this.inputRevolver[3] = addSlotToContainer(new GhostFilteredBullet(this, this.inv, 32, 144, 64));
		this.inputRevolver[4] = addSlotToContainer(new GhostFilteredBullet(this, this.inv, 33, 125, 72));
		this.inputRevolver[5] = addSlotToContainer(new GhostFilteredBullet(this, this.inv, 34, 106, 64));
		this.inputRevolver[6] = addSlotToContainer(new GhostFilteredBullet(this, this.inv, 35, 98, 45));
		this.inputRevolver[7] = addSlotToContainer(new GhostFilteredBullet(this, this.inv, 36, 106, 26));

		//MG loader slots (if upgrade installed)
		if(tile.isUpgradeInstalled(IIContent.UPGRADE_MG_LOADER))
		{
			for(int i = 0; i < 12; i++)
				this.inputMG[i] = addSlotToContainer(new Slot(this.inv, i+30, 184+(i%2)*18, 18+(i/2)*18)
				{
					@Override
					public boolean isItemValid(ItemStack stack)
					{
						return stack.getItem() instanceof ItemIIAmmoMachinegun;
					}
				});
		}

		//Player inventory
		addPlayerInventory(player.inventory, 8, 98+45);
	}

	public static class GhostFilteredBullet extends IESlot.Ghost
	{
		public GhostFilteredBullet(Container container, IInventory inv, int id, int x, int y)
		{
			super(container, inv, id, x, y);
		}

		@Override
		public void putStack(ItemStack itemStack)
		{
			super.putStack(itemStack);
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return stack.getItem() instanceof ItemBullet&&
					!(stack.isItemEqual(BulletHandler.emptyCasing)||stack.isItemEqual(BulletHandler.emptyShell));
		}
	}
}
