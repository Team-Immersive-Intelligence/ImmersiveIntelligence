package pl.pabilo8.immersiveintelligence.common.util.gui;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.gui.IESlot.FluidContainer;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.crafting.DataProgrammingRecipe;
import pl.pabilo8.immersiveintelligence.api.rotary.IMotorGear;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 23.09.2023
 */
public class ContainerIIBase<T extends TileEntityIEBase & IIEInventory> extends ContainerIEBase<T>
{
	public Slot[] playerInventory = new Slot[0];

	public ContainerIIBase(EntityPlayer player, T tile)
	{
		super(player.inventory, tile);
		this.slotCount = tile.getInventory().size();
		this.tile = tile;
	}

	public Slot[] addPlayerInventory(InventoryPlayer inventoryPlayer, int x, int y)
	{
		playerInventory = new Slot[36];
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(playerInventory[j+i*9] = new Slot(inventoryPlayer, j+i*9+9, x+j*18, y+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(playerInventory[i+27] = new Slot(inventoryPlayer, i, x+i*18, y+58));
		return playerInventory;
	}

	protected DefaultInputSlot addSlot(int x, int y, int index)
	{
		return addSlot(x, y, index, DefaultInputSlot::new);
	}

	@SuppressWarnings("unchecked")
	protected <SLOT extends Slot> SLOT addSlot(int x, int y, int index, SlotConstructor<SLOT> aNew)
	{
		return (SLOT)addSlotToContainer(aNew.construct(this, this.inv, index, x, y));
	}

	protected <SLOT extends Slot> Slot[] addSlotArray(int x, int y, int startIndex, int totalSlots, int slotsPerRow, SlotConstructor<SLOT> aNew)
	{
		return addSlotArray(x, y, startIndex, totalSlots, slotsPerRow, 18, aNew);
	}

	protected <SLOT extends Slot> Slot[] addSlotArray(int x, int y, int startIndex, int totalSlots, int slotsPerRow, int slotDistance, SlotConstructor<SLOT> aNew)
	{
		ArrayList<SLOT> slots = new ArrayList<>();
		for(int i = 0; i < totalSlots; i++)
			slots.add(this.addSlot(x+i%slotsPerRow*slotDistance, y+i/slotsPerRow*slotDistance, i+startIndex, aNew));
		return slots.toArray(new Slot[0]);
	}

	protected <SLOT extends Slot> Slot[] addVirtualSlots(int startIndex, int totalSlots, SlotConstructor<SLOT> aNew)
	{
		ArrayList<SLOT> slots = new ArrayList<>();
		for(int i = 0; i < totalSlots; i++)
			slots.add(this.addSlot(-32, -32, i+startIndex, aNew));
		return slots.toArray(new Slot[0]);
	}

	/**
	 * Functional interface for {@link Slot} constructors to create them in batch.
	 *
	 * @param <SLOT> The type of the slot to construct.
	 */
	@FunctionalInterface
	public interface SlotConstructor<SLOT extends Slot>
	{
		SLOT construct(Container container, IInventory inv, int id, int x, int y);
	}

	public static class IISlot extends IESlot
	{
		@Nullable
		private Predicate<ItemStack> filter = null;
		@Nullable
		private Runnable onChanged = null;

		public IISlot(Container container, IInventory inv, int id, int x, int y)
		{
			super(container, inv, id, x, y);
		}

		public IISlot withFilter(@Nullable Predicate<ItemStack> filter)
		{
			this.filter = filter;
			return this;
		}

		public IISlot withOnChanged(@Nullable Runnable onChanged)
		{
			this.onChanged = onChanged;
			return this;
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return filter==null||filter.test(stack);
		}

		@Override
		public void onSlotChanged()
		{
			super.onSlotChanged();
			if(onChanged!=null)
				onChanged.run();
		}
	}

	public class DefaultInputSlot extends IISlot
	{
		public DefaultInputSlot(Container container, IInventory inv, int id, int x, int y)
		{
			super(container, inv, id, x, y);
			withFilter(stack -> tile.isStackValid(getSlotIndex(), stack));
		}
	}

	public class CrateSlot extends IISlot
	{
		public CrateSlot(Container container, IInventory inv, int id, int x, int y)
		{
			super(container, inv, id, x, y);
			withFilter(IEApi::isAllowedInCrate);
		}
	}

	public static class FilteredDataInput extends IISlot
	{
		public FilteredDataInput(Container container, IInventory inv, int id, int x, int y)
		{
			super(container, inv, id, x, y);
			withFilter(stack -> DataProgrammingRecipe.streamRecipes(DataProgrammingRecipe.class)
					.anyMatch(r -> r.input.matchesItemStackIgnoringSize(stack)));
		}
	}

	public static class MotorGearSlot extends IISlot
	{
		public MotorGearSlot(Container container, IInventory inv, int id, int x, int y)
		{
			super(container, inv, id, x, y);
			withFilter(stack -> stack.getItem() instanceof IMotorGear);
		}

		@Override
		public int getSlotStackLimit()
		{
			return 1;
		}
	}

	public static SlotConstructor<FluidContainer> getFluidContainerSlot(SideConfig mode)
	{
		//Because fuck logic, that's why
		//-Blusunrize, allegedly
		int filter = mode==SideConfig.NONE?0: (mode==SideConfig.INPUT?2: 1);
		return (container, inv1, id, x, y) -> new FluidContainer(container, inv1, id, x, y, filter);
	}
}
