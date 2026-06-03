package pl.pabilo8.immersiveintelligence.common.util.gui;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.gui.IESlot.FluidContainer;
import blusunrize.immersiveengineering.common.gui.IESlot.ICallbackContainer;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import pl.pabilo8.immersiveintelligence.api.crafting.DataProgrammingRecipe;
import pl.pabilo8.immersiveintelligence.api.rotary.IMotorGear;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Common Immersive Intelligence container base for TileEntity, Entity and ItemStack backed GUIs.
 * <p>
 * It deliberately extends vanilla {@link Container} directly. Concrete specialisations are responsible
 * for defining their backing object and interaction rules.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 23.09.2023
 */
public abstract class ContainerIIBase extends Container implements ICallbackContainer
{
	protected final InventoryPlayer inventoryPlayer;
	@Nullable
	public IInventory inv;
	public int slotCount;
	public Slot[] playerInventory = new Slot[0];

	protected ContainerIIBase(InventoryPlayer inventoryPlayer)
	{
		this.inventoryPlayer = inventoryPlayer;
	}

	protected ContainerIIBase(InventoryPlayer inventoryPlayer, @Nullable IInventory inv, int slotCount)
	{
		this.inventoryPlayer = inventoryPlayer;
		this.inv = inv;
		this.slotCount = slotCount;
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

	protected Slot addSlot(int x, int y, int index)
	{
		if(inv==null)
			throw new IllegalStateException(getClass().getSimpleName()+" has no IInventory-backed inventory");
		return addSlot(x, y, index, DefaultInputSlot::new);
	}

	@SuppressWarnings("unchecked")
	protected <SLOT extends Slot> SLOT addSlot(int x, int y, int index, SlotConstructor<SLOT> aNew)
	{
		if(inv==null)
			throw new IllegalStateException(getClass().getSimpleName()+" has no IInventory-backed inventory");
		return (SLOT)addSlotToContainer(aNew.construct(this, this.inv, index, x, y));
	}

	protected Slot[] addSlotArray(int x, int y, int startIndex, int totalSlots, int slotsPerRow)
	{
		return addSlotArray(x, y, startIndex, totalSlots, slotsPerRow, 18);
	}

	protected Slot[] addSlotArray(int x, int y, int startIndex, int totalSlots, int slotsPerRow, int slotDistance)
	{
		ArrayList<Slot> slots = new ArrayList<>();
		for(int i = 0; i < totalSlots; i++)
			slots.add(this.addSlot(x+i%slotsPerRow*slotDistance, y+i/slotsPerRow*slotDistance, i+startIndex));
		return slots.toArray(new Slot[0]);
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

	protected Slot[] addVirtualSlots(int startIndex, int totalSlots)
	{
		ArrayList<Slot> slots = new ArrayList<>();
		for(int i = 0; i < totalSlots; i++)
			slots.add(this.addSlot(-32, -32, i+startIndex));
		return slots.toArray(new Slot[0]);
	}

	protected <SLOT extends Slot> Slot[] addVirtualSlots(int startIndex, int totalSlots, SlotConstructor<SLOT> aNew)
	{
		ArrayList<SLOT> slots = new ArrayList<>();
		for(int i = 0; i < totalSlots; i++)
			slots.add(this.addSlot(-32, -32, i+startIndex, aNew));
		return slots.toArray(new Slot[0]);
	}

	protected boolean isStackValid(int slot, ItemStack stack)
	{
		return inv==null||inv.isItemValidForSlot(slot, stack);
	}

	protected int getSlotLimit(int slot)
	{
		return inv==null?64: inv.getInventoryStackLimit();
	}

	@Override
	public boolean canInsert(ItemStack stack, int slotNumer, Slot slotObject)
	{
		return true;
	}

	@Override
	public boolean canTake(ItemStack stack, int slotNumer, Slot slotObject)
	{
		return true;
	}

	@Nonnull
	@Override
	public ItemStack slotClick(int id, int button, ClickType clickType, EntityPlayer player)
	{
		Slot slot = id < 0||id >= this.inventorySlots.size()?null: this.inventorySlots.get(id);
		if(!(slot instanceof IESlot.Ghost))
			return super.slotClick(id, button, clickType, player);

		ItemStack stack = ItemStack.EMPTY;
		ItemStack stackSlot = slot.getStack();
		if(!stackSlot.isEmpty())
			stack = stackSlot.copy();

		if(button==2)
			slot.putStack(ItemStack.EMPTY);
		else if(button==0||button==1)
		{
			ItemStack stackHeld = player.inventory.getItemStack();
			if(stackSlot.isEmpty())
			{
				if(!stackHeld.isEmpty()&&slot.isItemValid(stackHeld))
					slot.putStack(Utils.copyStackWithAmount(stackHeld, 1));
			}
			else if(stackHeld.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else if(slot.isItemValid(stackHeld))
				slot.putStack(Utils.copyStackWithAmount(stackHeld, 1));
		}
		else if(button==5)
		{
			ItemStack stackHeld = player.inventory.getItemStack();
			if(!slot.getHasStack()&&!stackHeld.isEmpty()&&slot.isItemValid(stackHeld))
				slot.putStack(Utils.copyStackWithAmount(stackHeld, 1));
		}
		return stack;
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot)
	{
		ItemStack stack = ItemStack.EMPTY;
		Slot slotObject = this.inventorySlots.get(slot);
		if(slotObject!=null&&slotObject.getHasStack())
		{
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();
			if(slot < slotCount)
			{
				if(!this.mergeItemStack(stackInSlot, slotCount, this.inventorySlots.size(), true))
					return ItemStack.EMPTY;
			}
			else if(!this.mergeItemStack(stackInSlot, 0, slotCount, false))
				return ItemStack.EMPTY;

			if(stackInSlot.isEmpty())
				slotObject.putStack(ItemStack.EMPTY);
			else
				slotObject.onSlotChanged();

			if(stackInSlot.getCount()==stack.getCount())
				return ItemStack.EMPTY;
			slotObject.onTake(player, stackInSlot);
		}
		return stack;
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
		if(inv!=null)
			this.inv.closeInventory(playerIn);
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
			withFilter(stack -> ContainerIIBase.this.isStackValid(getSlotIndex(), stack));
		}

		@Override
		public int getSlotStackLimit()
		{
			return ContainerIIBase.this.getSlotLimit(getSlotIndex());
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

	protected static class InventoryIIEInventory<T extends IIEInventory> implements IInventory
	{
		protected final T source;
		protected final Supplier<ITextComponent> displayName;
		protected final Predicate<EntityPlayer> usabilityCheck;

		public InventoryIIEInventory(T source, Supplier<ITextComponent> displayName, Predicate<EntityPlayer> usabilityCheck)
		{
			this.source = source;
			this.displayName = displayName;
			this.usabilityCheck = usabilityCheck;
		}

		@Override
		public int getSizeInventory()
		{
			return source.getInventory().size();
		}

		@Override
		public boolean isEmpty()
		{
			for(ItemStack stack : source.getInventory())
				if(!stack.isEmpty())
					return false;
			return true;
		}

		@Nonnull
		@Override
		public ItemStack getStackInSlot(int index)
		{
			return source.getInventory().get(index);
		}

		@Nonnull
		@Override
		public ItemStack decrStackSize(int index, int count)
		{
			ItemStack stack = ItemStackHelper.getAndSplit(source.getInventory(), index, count);
			if(!stack.isEmpty())
			{
				source.doGraphicalUpdates(index);
				markDirty();
			}
			return stack;
		}

		@Nonnull
		@Override
		public ItemStack removeStackFromSlot(int index)
		{
			ItemStack stack = ItemStackHelper.getAndRemove(source.getInventory(), index);
			if(!stack.isEmpty())
			{
				source.doGraphicalUpdates(index);
				markDirty();
			}
			return stack;
		}

		@Override
		public void setInventorySlotContents(int index, @Nonnull ItemStack stack)
		{
			source.getInventory().set(index, stack);
			if(stack.getCount() > getInventoryStackLimit())
				stack.setCount(getInventoryStackLimit());
			source.doGraphicalUpdates(index);
			markDirty();
		}

		@Override
		public int getInventoryStackLimit()
		{
			return 64;
		}

		@Override
		public void markDirty()
		{

		}

		@Override
		public boolean isUsableByPlayer(@Nonnull EntityPlayer player)
		{
			return usabilityCheck.test(player);
		}

		@Override
		public void openInventory(@Nonnull EntityPlayer player)
		{

		}

		@Override
		public void closeInventory(@Nonnull EntityPlayer player)
		{

		}

		@Override
		public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack)
		{
			return source.isStackValid(index, stack);
		}

		@Override
		public int getField(int id)
		{
			return 0;
		}

		@Override
		public void setField(int id, int value)
		{

		}

		@Override
		public int getFieldCount()
		{
			return 0;
		}

		@Override
		public void clear()
		{
			NonNullList<ItemStack> inventory = source.getInventory();
			for(int i = 0; i < inventory.size(); i++)
				setInventorySlotContents(i, ItemStack.EMPTY);
		}

		@Override
		public String getName()
		{
			return getDisplayName().getUnformattedText();
		}

		@Override
		public boolean hasCustomName()
		{
			return false;
		}

		@Nonnull
		@Override
		public ITextComponent getDisplayName()
		{
			ITextComponent name = displayName.get();
			return name==null?new TextComponentString(""): name;
		}
	}
}
