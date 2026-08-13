package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate;

import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.api.ammocrate.AmmunitionCrateHandler;
import pl.pabilo8.immersiveintelligence.api.ammocrate.AmmunitionCrateMode;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeTier;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

import static pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.EffectCrates.ammoCrateEnergyPerAction;

/**
 * Stores mode-specific ammunition and reloads compatible weapons.
 *
 * @author Pabilo8(pabilo@iiteam.net)
 * @updated 13.08.2026
 * @since 17.05.2019
 */
public class TileEntityAmmunitionCrate extends TileEntityEffectCrate
{
	static
	{
		UpgradeTechTree.getTreeFor(TileEntityAmmunitionCrate.class)
				.withUpgrade(IIContent.UPGRADE_INSERTER, UpgradeTier.TIER_1);
	}

	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE, SyncEvents.TILE_DROP_AS_ITEM})
	public AmmunitionCrateMode mode = AmmunitionCrateMode.REVOLVER;

	public TileEntityAmmunitionCrate()
	{
		super(NonNullList.withSize(ContainerAmmunitionCrate.INVENTORY_SIZE, ItemStack.EMPTY), IEInventoryHandler::new);
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.AMMUNITION_CRATE;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		if(slot >= ContainerAmmunitionCrate.SPENT_START&&slot < ContainerAmmunitionCrate.SPENT_START+ContainerAmmunitionCrate.SPENT_COUNT)
		{
			if(hasSpentMagazineSlots()&&slot >= ContainerAmmunitionCrate.SPENT_MAGAZINE_START)
				return mode.isSpentMagazine(stack);
			return mode.isSpentCasing(stack);
		}
		return AmmunitionCrateHandler.isStackValid(mode, slot, stack);
	}

	@Override
	public void receiveMessageFromClient(@Nonnull NBTTagCompound message)
	{
		AmmunitionCrateMode previousMode = mode;
		super.receiveMessageFromClient(message);
		if(previousMode==mode||world==null||world.isRemote)
			return;
		dropInvalidModeContents();
		updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(isUpgradeInstalled(IIContent.UPGRADE_INSERTER))
		{
			if(lid.getState())
				setLidState(false);
			return false;
		}

		if(player.isSneaking())
		{
			setLidState(!lid.getState());
			return true;
		}

		if(!lid.isFullyOpened())
			return false;

		if(world.isRemote)
			return canDepositHeldItem(heldItem)||AmmunitionCrateHandler.canReload(mode, heldItem);
		if(depositHeldItem(player, hand))
			return true;
		return AmmunitionCrateHandler.reload(this, player, hand);
	}

	private boolean canDepositHeldItem(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		return mode.isSpentCasing(stack)||mode.isSpentMagazine(stack)
				||(stack.getItem()==IIContent.itemBulletMagazine&&mode.isAmmunition(stack));
	}

	private boolean depositHeldItem(EntityPlayer player, EnumHand hand)
	{
		ItemStack held = player.getHeldItem(hand);
		if(held.isEmpty())
			return false;

		ItemStack remaining;
		if(mode.isSpentCasing(held))
			remaining = insertSpentCasing(held.copy());
		else if(mode.isSpentMagazine(held))
			remaining = insertReturnedMagazine(held.copy());
		else if(held.getItem()==IIContent.itemBulletMagazine&&mode.isAmmunition(held))
			remaining = insertAmmunition(held.copy());
		else
			return false;

		if(remaining.getCount()==held.getCount())
			return false;
		player.setHeldItem(hand, remaining);
		return true;
	}

	/**
	 * @return number of storage slots available for ammunition
	 */
	public int getAmmunitionSlotCount()
	{
		return ContainerAmmunitionCrate.AMMUNITION_COUNT;
	}

	/**
	 * @return true if this mode has a separate spent-magazine section
	 */
	public boolean hasSpentMagazineSlots()
	{
		return mode!=AmmunitionCrateMode.REVOLVER&&mode.hasSpentMagazineSlots();
	}

	/**
	 * Finds an ammunition stack without extracting it.
	 */
	public ItemStack findAmmunition(Predicate<ItemStack> predicate)
	{
		for(int slot = ContainerAmmunitionCrate.AMMUNITION_START; slot < ContainerAmmunitionCrate.AMMUNITION_START+getAmmunitionSlotCount(); slot++)
		{
			ItemStack stack = inventory.get(slot);
			if(!stack.isEmpty()&&predicate.test(stack))
				return stack;
		}
		return ItemStack.EMPTY;
	}

	/**
	 * @return true if matching ammunition is stored
	 */
	public boolean hasAmmunition(Predicate<ItemStack> predicate)
	{
		return !findAmmunition(predicate).isEmpty();
	}

	/**
	 * Extracts one matching ammunition item.
	 */
	public ItemStack extractAmmunition(Predicate<ItemStack> predicate)
	{
		for(int slot = ContainerAmmunitionCrate.AMMUNITION_START; slot < ContainerAmmunitionCrate.AMMUNITION_START+getAmmunitionSlotCount(); slot++)
		{
			ItemStack stack = inventory.get(slot);
			if(!stack.isEmpty()&&predicate.test(stack))
			{
				ItemStack extracted = insertionHandler.extractItem(slot, 1, false);
				if(!extracted.isEmpty())
					markDirty();
				return extracted;
			}
		}
		return ItemStack.EMPTY;
	}

	/**
	 * Removes a specific ammunition stack from storage.
	 */
	public boolean removeAmmunitionStack(ItemStack stack)
	{
		for(int slot = ContainerAmmunitionCrate.AMMUNITION_START; slot < ContainerAmmunitionCrate.AMMUNITION_START+getAmmunitionSlotCount(); slot++)
			if(inventory.get(slot)==stack)
			{
				ItemStack extracted = insertionHandler.extractItem(slot, 1, false);
				if(!extracted.isEmpty())
					markDirty();
				return !extracted.isEmpty();
			}
		return false;
	}

	/**
	 * Inserts a stack into the ammunition section.
	 */
	public ItemStack insertAmmunition(ItemStack stack)
	{
		return insertIntoRange(stack, ContainerAmmunitionCrate.AMMUNITION_START, ContainerAmmunitionCrate.AMMUNITION_START+getAmmunitionSlotCount());
	}

	/**
	 * Inserts a stack into the spent-casing section.
	 */
	public ItemStack insertSpentCasing(ItemStack stack)
	{
		int end = hasSpentMagazineSlots()?ContainerAmmunitionCrate.SPENT_MAGAZINE_START: ContainerAmmunitionCrate.SPENT_START+ContainerAmmunitionCrate.SPENT_COUNT;
		return insertIntoRange(stack, ContainerAmmunitionCrate.SPENT_START, end);
	}

	/**
	 * Inserts an unloaded magazine into the correct section.
	 */
	public ItemStack insertReturnedMagazine(ItemStack stack)
	{
		if(stack.isEmpty())
			return ItemStack.EMPTY;

		if(hasSpentMagazineSlots()&&isStackValid(ContainerAmmunitionCrate.SPENT_MAGAZINE_START, stack))
			return insertIntoRange(stack, ContainerAmmunitionCrate.SPENT_MAGAZINE_START, ContainerAmmunitionCrate.SPENT_START+ContainerAmmunitionCrate.SPENT_COUNT);
		return insertAmmunition(stack);
	}

	/**
	 * Gets copies of ammunition stacks for rendering.
	 */
	public NonNullList<ItemStack> getAmmunitionStacks()
	{
		NonNullList<ItemStack> stacks = NonNullList.create();
		for(int slot = ContainerAmmunitionCrate.AMMUNITION_START; slot < ContainerAmmunitionCrate.AMMUNITION_START+getAmmunitionSlotCount(); slot++)
			if(!inventory.get(slot).isEmpty())
				stacks.add(inventory.get(slot).copy());
		return stacks;
	}

	/**
	 * Tests whether an entity is inside the inserter service area.
	 */
	public boolean isInserterTargetInRange(Entity entity)
	{
		AxisAlignedBB range = new AxisAlignedBB(getPos()).offset(0.5, 0.5, 0.5).grow(getRange());
		return entity!=null&&entity.getEntityBoundingBox().intersects(range);
	}

	private ItemStack insertIntoRange(ItemStack stack, int start, int end)
	{
		int initialCount = stack.getCount();
		ItemStack remaining = stack;
		for(int slot = start; slot < end&&!remaining.isEmpty(); slot++)
			remaining = insertionHandler.insertItem(slot, remaining, false);
		if(remaining.getCount()!=initialCount)
			markDirty();
		return remaining;
	}

	@Override
	public void update()
	{
		super.update();
		if(world!=null&&!world.isRemote&&world.getTotalWorldTime()%10==0)
			outputSpentItems();
	}

	private void dropInvalidModeContents()
	{
		for(int slot = 0; slot < ContainerAmmunitionCrate.REVOLVER_PATTERN_START; slot++)
		{
			ItemStack stack = inventory.get(slot);
			if(stack.isEmpty()||isStackValid(slot, stack))
				continue;

			inventory.set(slot, ItemStack.EMPTY);
			Utils.dropStackAtPos(world, pos, stack);
		}
	}

	private void outputSpentItems()
	{
		TileEntity tile = world.getTileEntity(pos.down());
		if(tile==null||!tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP))
			return;

		IItemHandler output = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		if(output==null)
			return;

		boolean changed = false;
		for(int slot = ContainerAmmunitionCrate.SPENT_START; slot < ContainerAmmunitionCrate.SPENT_START+ContainerAmmunitionCrate.SPENT_COUNT; slot++)
		{
			ItemStack stack = inventory.get(slot);
			if(stack.isEmpty()||!isStackValid(slot, stack))
				continue;

			ItemStack remaining = ItemHandlerHelper.insertItem(output, stack.copy(), false);
			if(remaining.getCount()==stack.getCount())
				continue;

			inventory.set(slot, remaining);
			changed = true;
		}

		if(changed)
			markDirty();
	}

	@Override
	public float calculateInserterAnimation(float partialTicks)
	{
		Entity focused = focusedEntity.get();
		if(focused==null)
			return 0;
		return AmmunitionCrateHandler.getAutomaticReloadProgress(focused, partialTicks);
	}

	@Override
	boolean isSupplied()
	{
		//The inserter must also be able to remove an empty magazine when no replacement is available.
		return true;
	}

	@Override
	void useSupplies()
	{
	}

	@Override
	boolean affectEntity(Entity entity, boolean upgraded)
	{
		if(!upgraded||energyStorage.getEnergyStored() < ammoCrateEnergyPerAction)
			return false;

		boolean started = AmmunitionCrateHandler.reloadAutomatically(this, entity);
		if(started)
			consumeEnergy(ammoCrateEnergyPerAction);
		return started;
	}

	@Override
	boolean checkEntity(Entity entity)
	{
		return AmmunitionCrateHandler.canAutomate(this, entity);
	}
}
