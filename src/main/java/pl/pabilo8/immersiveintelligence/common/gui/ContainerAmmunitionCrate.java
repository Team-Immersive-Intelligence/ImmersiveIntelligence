package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.ammocrate.AmmunitionCrateHandler;
import pl.pabilo8.immersiveintelligence.api.ammocrate.AmmunitionCrateMode;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Arranges the fixed Ammunition Crate inventory according to its active loading mode.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon(avalon@iiteam.net)
 * @updated 13.08.2026
 * @since 17.05.2019
 */
public class ContainerAmmunitionCrate extends ContainerIITileBase<TileEntityAmmunitionCrate>
{
	//--- Static Fields ---//
	public static final int INVENTORY_SIZE = 50;
	public static final int AMMUNITION_START = 0;
	public static final int AMMUNITION_COUNT = 18;
	public static final int SPENT_START = 18;
	public static final int SPENT_COUNT = 20;
	public static final int RESERVED_START = SPENT_START+SPENT_COUNT;
	public static final int SPENT_CASING_COUNT = 10;
	public static final int SPENT_MAGAZINE_START = SPENT_START+SPENT_CASING_COUNT;
	public static final int SPENT_MAGAZINE_COUNT = 10;
	public static final int RESERVED_COUNT = 4;
	public static final int REVOLVER_PATTERN_START = RESERVED_START+RESERVED_COUNT;
	public static final int REVOLVER_PATTERN_COUNT = 8;

	//--- Slots ---//
	public Slot[] inputAmmunition = new Slot[0];
	public Slot[] spentCasings = new Slot[0];
	public Slot[] spentMagazines = new Slot[0];
	public Slot[] revolverPattern = new Slot[0];

	public ContainerAmmunitionCrate(EntityPlayer player, TileEntityAmmunitionCrate tile)
	{
		super(player, tile);
		refreshSlots(tile.mode);
	}

	/**
	 * Rebuilds slot positions without changing their physical inventory indices.
	 *
	 * @param mode crate mode to display
	 */
	public void refreshSlots(AmmunitionCrateMode mode)
	{
		inventorySlots.clear();
		inventoryItemStacks.clear();

		List<Slot> ammunition = new ArrayList<>();
		List<Slot> casings = new ArrayList<>();
		List<Slot> magazines = new ArrayList<>();
		List<Slot> pattern = new ArrayList<>();

		//Slots 0..17 are ammunition storage. Keep the Revolver layout left-aligned for its drum pattern.
		int ammunitionX = mode==AmmunitionCrateMode.REVOLVER?8: 58;
		for(int slot = AMMUNITION_START;
		    slot < AMMUNITION_START+AMMUNITION_COUNT; slot++)
		{
			int displayIndex = slot-AMMUNITION_START;
			ammunition.add(addSlotToContainer(new AmmunitionSlot(inv, slot,
					ammunitionX+(displayIndex%6)*18, 40+(displayIndex/6)*18)));
		}

		//Slots 18..37 hold spent output. Magazine modes use two 5x2 sections.
		boolean separateMagazines = mode!=AmmunitionCrateMode.REVOLVER&&mode.hasSpentMagazineSlots();
		for(int slot = SPENT_START;
		    slot < SPENT_START+SPENT_COUNT; slot++)
		{
			if(separateMagazines&&slot >= SPENT_MAGAZINE_START)
			{
				int displayIndex = slot-SPENT_MAGAZINE_START;
				magazines.add(addSlotToContainer(new SpentSlot(inv, slot,
						123+(displayIndex%5)*18, 116+(displayIndex/5)*18)));
			}
			else
			{
				int displayIndex = slot-SPENT_START;
				int columns = separateMagazines?5: 10;
				int startX = separateMagazines?11: (mode==AmmunitionCrateMode.REVOLVER?8: 22);
				casings.add(addSlotToContainer(new SpentSlot(inv, slot,
						startX+(displayIndex%columns)*18, 116+(displayIndex/columns)*18)));
			}
		}

		//Slots 38..41 remain hidden to keep the physical inventory and container indices stable at 50 slots.
		for(int slot = RESERVED_START;
		    slot < RESERVED_START+RESERVED_COUNT; slot++)
			addSlotToContainer(new ReservedSlot(inv, slot));

		//Slots 42..49 are permanently reserved for the Revolver chamber pattern.
		int[][] chamberPositions = {
				{160, 40-4}, {180, 48-4}, {188, 67-4}, {180, 86-4},
				{160, 94-4}, {140, 86-4}, {132, 67-4}, {140, 48-4}
		};
		for(int chamber = 0; chamber < REVOLVER_PATTERN_COUNT; chamber++)
		{
			int slot = REVOLVER_PATTERN_START+chamber;
			GhostFilteredBullet patternSlot = new GhostFilteredBullet(this, inv, tile, slot,
					mode==AmmunitionCrateMode.REVOLVER?chamberPositions[chamber][0]: -32,
					mode==AmmunitionCrateMode.REVOLVER?chamberPositions[chamber][1]: -32);
			addSlotToContainer(patternSlot);
			if(mode==AmmunitionCrateMode.REVOLVER)
				pattern.add(patternSlot);
		}

		inputAmmunition = ammunition.toArray(new Slot[0]);
		spentCasings = casings.toArray(new Slot[0]);
		spentMagazines = magazines.toArray(new Slot[0]);
		revolverPattern = pattern.toArray(new Slot[0]);

		addPlayerInventory(inventoryPlayer, 32, 168);
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber)
	{
		if(slotNumber < 0||slotNumber >= inventorySlots.size())
			return ItemStack.EMPTY;
		if(slotNumber >= REVOLVER_PATTERN_START
				&&slotNumber < INVENTORY_SIZE)
			return ItemStack.EMPTY;

		Slot slot = inventorySlots.get(slotNumber);
		if(slot==null||!slot.getHasStack())
			return ItemStack.EMPTY;

		ItemStack source = slot.getStack();
		ItemStack original = source.copy();
		if(slotNumber < slotCount)
		{
			if(!mergeItemStack(source, slotCount, inventorySlots.size(), true))
				return ItemStack.EMPTY;
		}
		else if(!mergeItemStack(source, AMMUNITION_START, SPENT_START+SPENT_COUNT, false))
			return ItemStack.EMPTY;

		if(source.isEmpty())
			slot.putStack(ItemStack.EMPTY);
		else
			slot.onSlotChanged();

		if(source.getCount()==original.getCount())
			return ItemStack.EMPTY;

		slot.onTake(player, source);
		return original;
	}

	private class AmmunitionSlot extends Slot
	{
		private AmmunitionSlot(IInventory inventory, int index, int x, int y)
		{
			super(inventory, index, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return tile.isStackValid(getSlotIndex(), stack);
		}
	}

	private static class ReservedSlot extends Slot
	{
		private ReservedSlot(IInventory inventory, int index)
		{
			super(inventory, index, -32, -32);
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return false;
		}
	}

	private class SpentSlot extends Slot
	{
		private SpentSlot(IInventory inventory, int index, int x, int y)
		{
			super(inventory, index, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return tile.isStackValid(getSlotIndex(), stack);
		}
	}

	public static class GhostFilteredBullet extends IESlot.Ghost
	{
		private final TileEntityAmmunitionCrate tile;

		public GhostFilteredBullet(Container container, IInventory inventory, TileEntityAmmunitionCrate tile, int id, int x, int y)
		{
			super(container, inventory, id, x, y);
			this.tile = tile;
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return AmmunitionCrateHandler.isStackValid(tile.mode, getSlotIndex(), stack);
		}
	}
}
