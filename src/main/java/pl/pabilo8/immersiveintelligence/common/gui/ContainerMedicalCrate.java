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

	public Slot inputSlot, inputFluidSlot, outputSlot, outputSlo2;

	public ContainerMedicalCrate(EntityPlayer player, TileEntityMedicalCrate tile)
	{
		//Normal bullet slots

		super(player, tile);
		int shift = tile.isUpgradeInstalled(IIContent.UPGRADE_INSERTER)?0: 27;


		inputFluidSlot = this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 0, 41, 21, 2));
		outputSlot = this.addSlotToContainer(new IESlot.Output(this, this.inv, 1, 41, 57));

		inputSlot = this.addSlotToContainer(new IESlot.Output(this, this.inv, 2, 85, 21)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return TileEntityMedicalCrate.BOOST_POTION_ITEM.test(stack);
			}
		});

		outputSlo2 = this.addSlotToContainer(new IESlot.Output(this, this.inv, 3, 85, 57));

		addPlayerInventory(player.inventory, 8, 141);
	}
}
