package pl.pabilo8.immersiveintelligence.common.gui;

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
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 01.07.2025
 * @ii-approved 0.3.1
 * @since 10.07.2019
 */
public class ContainerSawmill extends ContainerIIBase<TileEntitySawmill>
{
	public final Slot slotInput, slotSaw, slotOutput, slotOutputTrash;

	public ContainerSawmill(EntityPlayer player, TileEntitySawmill tile)
	{
		super(player, tile);

		this.slotInput = addSlotToContainer(new SawmillInputSlot(this, this.inv, 0, 13+8, 36));
		this.slotSaw = addSlotToContainer(new SawSlot(tile, this, this.inv, 1, 48+8, 23));

		this.slotOutput = addSlotToContainer(new IESlot.Output(this, this.inv, 2, 86+8, 36));
		this.slotOutputTrash = addSlotToContainer(new IESlot.Output(this, this.inv, 3, 108+8, 36));

		addPlayerInventory(player.inventory, 8, 86);
	}

	public static class SawmillInputSlot extends IESlot
	{
		public SawmillInputSlot(Container container, IInventory inv, int id, int x, int y)
		{
			super(container, inv, id, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack itemStack)
		{
			return !itemStack.isEmpty()&&SawmillRecipe.streamRecipes(SawmillRecipe.class)
					.anyMatch(r -> r.itemInput.matchesItemStackIgnoringSize(itemStack));
		}
	}

	public static class SawSlot extends IESlot
	{
		private final TileEntitySawmill tile;

		public SawSlot(TileEntitySawmill tile, Container container, IInventory inv, int id, int x, int y)
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
