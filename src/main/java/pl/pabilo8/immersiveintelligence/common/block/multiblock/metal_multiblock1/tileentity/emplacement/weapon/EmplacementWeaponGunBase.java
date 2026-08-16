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
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunRecoil;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunShootingHandler;
import pl.pabilo8.immersiveintelligence.common.util.gun.ammoprovider.GunAmmoProviderItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.function.Predicate;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 16.08.2026
 * @since 04.09.2025
 */
public abstract class EmplacementWeaponGunBase<A extends EntityAmmoBase<A>> extends EmplacementWeaponTurretBase
{
	/**
	 * Used to fire ammo for the weapon.
	 */
	protected AmmoFactory<A> ammoFactory;
	@Nullable
	protected IEInventoryHandler inventoryBaseHandler;
	@Nullable
	protected IEInventoryHandler inventoryPlatformHandler;

	@SyncNBT(time = 0, events = SyncEvents.WEAPON_RELOAD)
	public GunShootingHandler gunHandler = new GunShootingHandler();
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_RELOAD)
	public GunRecoil recoil = new GunRecoil();
	@Nullable
	protected GunAmmoProviderItemHandler platformAmmoProvider;

	/**
	 * Called after the weapon is installed or loaded from NBT.
	 * Initialize sight AABB here.
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

		ensureShootingComponents();
		boolean wasReloading = platformAmmoProvider!=null&&platformAmmoProvider.isReloading();
		gunHandler.update();
		if(!te.getWorld().isRemote&&wasReloading&&platformAmmoProvider!=null&&!platformAmmoProvider.isReloading())
			syncWithClient(te, SyncEvents.WEAPON_RELOAD);

		return super.onUpdate(te, baseNeeds, currentTarget);
	}

	protected void ensureShootingComponents()
	{
		if(platformAmmoProvider==null&&inventoryPlatformHandler!=null)
		{
			platformAmmoProvider = new GunAmmoProviderItemHandler(null, () -> null, inventoryPlatformHandler,
					ammoFactory::isValidAmmo, getReloadDelay());
			//noinspection unchecked
			gunHandler.withAmmoProvider(platformAmmoProvider)
					.withAmmoFactory((AmmoFactory<? extends EntityAmmoProjectile>)ammoFactory)
					.withRecoilHandler(recoil)
					.withMaxShotDelay(getShotDelay());
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initializeGUI(DecoPanel panelBase, DecoPanel panelPlatform)
	{

	}

	@Override
	public boolean canShoot(TileEntityEmplacement te)
	{
		ensureShootingComponents();
		return gunHandler.canShoot()&&platformAmmoProvider!=null
				&&(platformAmmoProvider.isLoaded()||platformAmmoProvider.hasAvailableAmmo());
	}

	@Override
	protected boolean shoot(TileEntityEmplacement te, TargetCoordinateReference target)
	{
		ensureShootingComponents();
		if(te.getWorld().isRemote||platformAmmoProvider==null)
			return false;

		if(platformAmmoProvider.isEmpty()&&platformAmmoProvider.hasAvailableAmmo())
		{
			if(gunHandler.startReloading())
				syncWithClient(te, SyncEvents.WEAPON_RELOAD);
		}
		if(!platformAmmoProvider.isLoaded())
			return false;

		if(baseEntity!=null)
			blusunrize.immersiveengineering.common.util.Utils.attractEnemies(baseEntity, 24);

		ammoFactory.setPositionAndVelocity(te.getWeaponCenter(), this.aim, 0.25f, 1f);
		return gunHandler.fire();
	}

	/**
	 * The weapon should be brought down when the platform has no available shot.
	 */
	@Override
	public boolean needsSupply(TileEntityEmplacement te)
	{
		ensureShootingComponents();
		return platformAmmoProvider!=null&&!platformAmmoProvider.isLoaded()&&!platformAmmoProvider.hasAvailableAmmo();
	}

	@Override
	public boolean needsRestock(TileEntityEmplacement te)
	{
		if(inventoryBaseHandler==null||inventoryPlatformHandler==null)
			return false;

		for(int baseSlot = 0; baseSlot < inventoryBaseHandler.getSlots(); baseSlot++)
		{
			ItemStack available = inventoryBaseHandler.getStackInSlot(baseSlot);
			if(available.isEmpty())
				continue;

			for(int platformSlot = 0; platformSlot < inventoryPlatformHandler.getSlots(); platformSlot++)
				if(inventoryPlatformHandler.insertItem(platformSlot, available, true).getCount() < available.getCount())
					return true;
		}
		return false;
	}

	@Override
	public boolean restockFromBase(TileEntityEmplacement te)
	{
		if(inventoryBaseHandler==null||inventoryPlatformHandler==null)
			return false;

		boolean changed = false;
		for(int baseSlot = 0; baseSlot < inventoryBaseHandler.getSlots(); baseSlot++)
		{
			ItemStack available = inventoryBaseHandler.getStackInSlot(baseSlot);
			if(available.isEmpty())
				continue;

			ItemStack moving = available.copy();
			for(int platformSlot = 0; platformSlot < inventoryPlatformHandler.getSlots()&&!moving.isEmpty(); platformSlot++)
				moving = inventoryPlatformHandler.insertItem(platformSlot, moving, false);

			int moved = available.getCount()-moving.getCount();
			if(moved > 0)
			{
				inventoryBaseHandler.extractItem(baseSlot, moved, false);
				changed = true;
			}

		}
		return changed;
	}


	@Override
	public void setDead()
	{
		super.setDead();
		if(this.ammoFactory!=null)
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
		this.platformAmmoProvider = null;
		ensureShootingComponents();
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
