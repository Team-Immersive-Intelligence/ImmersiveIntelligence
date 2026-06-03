package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement.EmplacementStateNeeds;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.function.Predicate;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 30.12.2025
 * @since 04.09.2025
 */
public abstract class EmplacementWeaponGunBase<A extends EntityAmmoBase<A>> extends EmplacementWeaponTurretBase
{
	/**
	 * Used to fire ammo for the weapon
	 */
	protected AmmoFactory<A> ammoFactory;
	@Nullable
	protected IEInventoryHandler inventoryBaseHandler;
	@Nullable
	protected IEInventoryHandler inventoryPlatformHandler;

	/**
	 * Called after the weapon is installed or loaded from NBT
	 * Initialize sight AABB here
	 */
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);

		this.ammoFactory = new AmmoFactory<A>(te.getWorld()).setIgnoredBlocks(te.getMultiblockBlocks());
		if(!te.getWorld().isRemote&&te.tactileHandler!=null)
			ammoFactory.setShooterAndGun(null, baseEntity)
					.setIgnoredEntities(te.tactileHandler.getEntities());

		this.aim.withAimCorrectionFunction(ammoFactory::getAnglePrediction);
	}

	@Override
	public EmplacementStateNeeds onUpdate(TileEntityEmplacement te, EmplacementStateNeeds baseNeeds, TargetCoordinateReference currentTarget)
	{
		if(!te.getWorld().isRemote&&te.getOwnerIdentity()!=null)
			ammoFactory.setOwner(te.getOwnerIdentity().getFirstResponsibleMember(te.getWorld()));
		return super.onUpdate(te, baseNeeds, currentTarget);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initializeGUI(DecoPanel panelBase, DecoPanel panelPlatform)
	{

	}

	@Override
	public boolean canShoot(TileEntityEmplacement te)
	{
		return false;
	}

	@Override
	public void setDead()
	{
		super.setDead();
		this.ammoFactory
				.setShooterAndGun(null, null)
				.setIgnoredEntities(Collections.emptyList());
	}

	@Nullable
	@Override
	public IEInventoryHandler getBaseItemHandler()
	{
		return inventoryBaseHandler;
	}

	@Nullable
	@Override
	public IEInventoryHandler getPlatformItemHandler()
	{
		return inventoryPlatformHandler;
	}

	/**
	 * Creates tile-backed IE inventory handlers for weapons that do not require item filtering.
	 */
	protected final void setupItemHandlers(TileEntityEmplacement te, int baseSlots, int platformSlots)
	{
		setupItemHandlers(te, baseSlots, platformSlots, stack -> true, stack -> true);
	}

	/**
	 * Creates tile-backed IE inventory handlers for this weapon.
	 *
	 * @param te             Emplacement tile entity
	 * @param baseSlots      slots reserved at the start of the tile inventory
	 * @param platformSlots  slots reserved directly after the base section
	 * @param baseFilter     filter for base-section insertion
	 * @param platformFilter filter for platform-section insertion
	 */
	protected final void setupItemHandlers(TileEntityEmplacement te, int baseSlots, int platformSlots,
										   Predicate<ItemStack> baseFilter, Predicate<ItemStack> platformFilter)
	{
		baseSlots = Math.max(0, Math.min(baseSlots, te.inventory.size()));
		platformSlots = Math.max(0, Math.min(platformSlots, te.inventory.size()-baseSlots));

		this.inventoryBaseHandler = baseSlots > 0?
				new FilteredEmplacementInventoryHandler(baseSlots, te, 0, baseFilter): null;
		this.inventoryPlatformHandler = platformSlots > 0?
				new FilteredEmplacementInventoryHandler(platformSlots, te, baseSlots, platformFilter): null;
	}

	/**
	 * IE's inventory handler already performs the normal tile inventory write/read logic;
	 * this small extension only adds a per-weapon stack filter before delegating to it.
	 */
	protected static class FilteredEmplacementInventoryHandler extends IEInventoryHandler
	{
		private final Predicate<ItemStack> filter;

		public FilteredEmplacementInventoryHandler(int slots, TileEntityEmplacement inventory, int slotOffset, Predicate<ItemStack> filter)
		{
			super(slots, inventory, slotOffset, true, true);
			this.filter = filter==null?stack -> true: filter;
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
		{
			if(!accepts(stack))
				return stack;
			return super.insertItem(slot, stack, simulate);
		}

		@Override
		public void setStackInSlot(int slot, @Nonnull ItemStack stack)
		{
			if(!stack.isEmpty()&&!filter.test(stack))
				return;
			super.setStackInSlot(slot, stack);
		}

		public boolean accepts(ItemStack stack)
		{
			return !stack.isEmpty()&&filter.test(stack);
		}
	}
}
