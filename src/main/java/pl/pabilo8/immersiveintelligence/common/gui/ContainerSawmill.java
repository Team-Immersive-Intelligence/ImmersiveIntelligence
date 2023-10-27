package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISawblade;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySawmill;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class ContainerSawmill extends ContainerIEBase<TileEntitySawmill>
{
	public ContainerSawmill(EntityPlayer player, TileEntitySawmill tile)
	{
		super(player.inventory, tile);
		//Input/Output Slots

		this.addSlotToContainer(new SawmillInput(this, this.inv, 0, 13, 36));
		this.addSlotToContainer(new Saw(tile, this, this.inv, 1, 48, 23));

		this.addSlotToContainer(new IESlot.Output(this, this.inv, 2, 86, 36));
		this.addSlotToContainer(new IESlot.Output(this, this.inv, 3, 108, 36));

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(player.inventory, j+i*9+9, 8+j*18, 86+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(player.inventory, i, 8+i*18, 144));
	}

	public static class SawmillInput extends IESlot
	{
		public SawmillInput(Container container, IInventory inv, int id, int x, int y)
		{
			super(container, inv, id, x, y);
		}

		/**
		 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
		 */
		@Override
		public boolean isItemValid(ItemStack itemStack)
		{
			return !itemStack.isEmpty()&&SawmillRecipe.isValidRecipeInput(itemStack);
		}
	}

	public static class Saw extends IESlot
	{
		TileEntitySawmill tile;

		public Saw(TileEntitySawmill tile, Container container, IInventory inv, int id, int x, int y)
		{
			super(container, inv, id, x, y);
			this.tile = tile;
		}

		@Override
		public boolean isItemValid(ItemStack itemStack)
		{
			return !itemStack.isEmpty()&&itemStack.getItem() instanceof ISawblade;
		}

		@Override
		public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
		{
			if(tile!=null&&tile.rotation.getRotationSpeed() > 0)
			{
				thePlayer.attackEntityFrom(DamageSource.GENERIC, tile.rotation.getTorque()/2.5f);
			}
			return super.onTake(thePlayer, stack);
		}
	}
}
