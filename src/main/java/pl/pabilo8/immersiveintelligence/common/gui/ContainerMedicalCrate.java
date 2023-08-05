package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityMedicalCrate;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class ContainerMedicalCrate extends ContainerIEBase<TileEntityMedicalCrate>
{
	public ContainerMedicalCrate(EntityPlayer player, TileEntityMedicalCrate tile)
	{
		//Normal bullet slots

		super(player.inventory, tile);
		int shift = tile.hasUpgrade(IIContent.UPGRADE_INSERTER)?0:27;

		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 0, 41+shift, 21, 2));
		this.addSlotToContainer(new IESlot.Output(this, this.inv, 1, 41+shift, 57));

		this.addSlotToContainer(new IESlot(this, this.inv, 2, 85+shift, 21){
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return TileEntityMedicalCrate.BOOST_POTION_ITEM.test(stack);
			}
		});
		this.addSlotToContainer(new IESlot.Output(this, this.inv, 3, 85+shift, 57));

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(player.inventory, j+i*9+9, 8+j*18, 87+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(player.inventory, i, 8+i*18, 145));
	}
}
