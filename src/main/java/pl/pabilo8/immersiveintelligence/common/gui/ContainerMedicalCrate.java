package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityMedicalCrate;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 17.05.2019
 * @since 08.14.2025
 */
public class ContainerMedicalCrate extends ContainerIIBase<TileEntityMedicalCrate>
{

	public Slot inputSlot, inputFluidSlot, outputSlot, outputSlot2;

	public ContainerMedicalCrate(EntityPlayer player, TileEntityMedicalCrate tile)
	{
		//Normal bullet slots

		super(player, tile);
		int shift = tile.isUpgradeInstalled(IIContent.UPGRADE_INSERTER)?0: 27;


		inputFluidSlot = this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 0, 68, 19, 2));
		outputSlot = this.addSlotToContainer(new IESlot.Output(this, this.inv, 1, 68, 55));

		inputSlot = this.addSlotToContainer(new IESlot.Output(this, this.inv, 2, 112, 19)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return TileEntityMedicalCrate.BOOST_POTION_ITEM.test(stack);
			}
		});

		outputSlot2 = this.addSlotToContainer(new IESlot.Output(this, this.inv, 3, 112, 55));

		addPlayerInventory(player.inventory, 8, 87);
	}
}
