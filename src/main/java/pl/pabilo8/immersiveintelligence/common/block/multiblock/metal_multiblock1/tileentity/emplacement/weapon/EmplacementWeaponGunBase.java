package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
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
 * Implements Platform ammunition loading, casing storage, and Base servicing for Emplacement guns.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 17.08.2026
 * @since 04.09.2025
 */
public abstract class EmplacementWeaponGunBase<A extends EntityAmmoBase<A>> extends EmplacementWeaponTurretBase
{
	private static final int ITEM_TRANSFER_INTERVAL = 15;

	/**
	 * Used to fire ammo for the weapon.
	 */
	protected AmmoFactory<A> ammoFactory;
	@Nullable
	protected FilteredEmplacementInventoryHandler baseAmmoHandler, baseCasingHandler;
	@Nullable
	protected FilteredEmplacementInventoryHandler platformAmmoHandler, platformCasingHandler;
	@Nullable
	private IItemHandler baseItemHandler, platformItemHandler;
	private int itemTransferTicker = 0;

	@SyncNBT(time = 0, events = SyncEvents.WEAPON_RELOAD)
	public GunShootingHandler gunHandler = new GunShootingHandler();
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_RELOAD)
	public GunRecoil recoil = new GunRecoil();
	@Nullable
	protected GunAmmoProviderItemHandler platformAmmoProvider;

	/**
	 * Called after the weapon is installed or loaded from NBT.
	 */
	@Override
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
		if(platformAmmoProvider!=null)
		{
			boolean wasReloading = platformAmmoProvider.isReloading();
			int previousStage = platformAmmoProvider.getLoadStage();
			boolean readyForLoading = te.door.getState()&&te.door.isFullyOpened()&&(setup==null||setup.isFullyOpened());
			boolean wantsReload = platformAmmoProvider.isEmpty()&&platformAmmoProvider.hasAvailableAmmo();

			if(readyForLoading&&(wantsReload||platformAmmoProvider.isReloading()))
			{
				Float loadingYaw = getLoadingYaw();
				Float loadingPitch = getLoadingPitch();
				setAimTargetAngles(te, loadingYaw, loadingPitch);
				if(!te.getWorld().isRemote&&wantsReload&&isAtAngles(loadingYaw, loadingPitch, 1.5f))
				{
					platformAmmoProvider.prepareReload();
					if(gunHandler.startReloading())
						syncWithClient(te, SyncEvents.WEAPON_RELOAD);
				}
			}

			gunHandler.update();
			if(!te.getWorld().isRemote&&(previousStage!=platformAmmoProvider.getLoadStage()
					||(wasReloading&&!platformAmmoProvider.isReloading())))
				syncWithClient(te, SyncEvents.WEAPON_RELOAD);
		}
		else
			gunHandler.update();

		return super.onUpdate(te, baseNeeds, currentTarget);
	}

	protected void ensureShootingComponents()
	{
		if(platformAmmoProvider==null&&platformAmmoHandler!=null)
		{
			platformAmmoProvider = new GunAmmoProviderItemHandler(null, () -> null, platformAmmoHandler,
					ammoFactory::isValidAmmo, getReloadDelay()).withLoadStages(getReloadStages());
			//noinspection unchecked
			gunHandler.withAmmoProvider(platformAmmoProvider)
					.withAmmoFactory((AmmoFactory<? extends EntityAmmoProjectile>)ammoFactory)
					.withRecoilHandler(recoil)
					.withMaxShotDelay(getShotDelay());
		}
	}

	/**
	 * @return round counts loaded by successive reload animation stages
	 */
	protected int[] getReloadStages()
	{
		return new int[]{Integer.MAX_VALUE};
	}

	/**
	 * @return required local yaw for loading, or null when any yaw is valid
	 */
	@Nullable
	protected Float getLoadingYaw()
	{
		return null;
	}

	/**
	 * @return required pitch for loading, or null when any pitch is valid
	 */
	@Nullable
	protected Float getLoadingPitch()
	{
		return getHidingPitch();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initializeGUI(DecoPanel panelBase, DecoPanel panelPlatform)
	{

	}

	@Override
	protected boolean canTrackTarget(TileEntityEmplacement te)
	{
		ensureShootingComponents();
		return platformAmmoProvider!=null&&platformAmmoProvider.isLoaded()&&!platformAmmoProvider.isReloading();
	}

	@Override
	public boolean canShoot(TileEntityEmplacement te)
	{
		ensureShootingComponents();
		return gunHandler.canShoot()&&platformAmmoProvider!=null&&platformAmmoProvider.isLoaded()
				&&canStoreSpentCasing(platformAmmoProvider.peekLoadedAmmo());
	}

	@Override
	protected boolean shoot(TileEntityEmplacement te, TargetCoordinateReference target)
	{
		ensureShootingComponents();
		if(te.getWorld().isRemote||platformAmmoProvider==null||!platformAmmoProvider.isLoaded())
			return false;

		ItemStack firedAmmo = platformAmmoProvider.peekLoadedAmmo();
		if(baseEntity!=null)
			blusunrize.immersiveengineering.common.util.Utils.attractEnemies(baseEntity, 24);

		ammoFactory.setPositionAndVelocity(te.getWeaponCenter(), this.aim, 0.25f, 1f)
				.setShooterAndGun(null, baseEntity)
				.setIgnoredEntities(te.tactileHandler.getEntities());
		boolean fired = gunHandler.fire();
		if(fired&&storesSpentCasings()&&storeSpentCasing(firedAmmo))
			te.updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
		return fired;
	}

	/**
	 * @return true when fired rounds produce casings stored by this weapon
	 */
	protected boolean storesSpentCasings()
	{
		return true;
	}

	private boolean storeSpentCasing(ItemStack firedAmmo)
	{
		ItemStack casing = getSpentCasing(firedAmmo);
		if(casing.isEmpty())
			return false;
		for(int slot = 0; platformCasingHandler!=null&&slot < platformCasingHandler.getSlots()&&!casing.isEmpty(); slot++)
			casing = platformCasingHandler.insertInternal(slot, casing, false);
		return casing.isEmpty();
	}

	private boolean canStoreSpentCasing(ItemStack firedAmmo)
	{
		if(!storesSpentCasings())
			return true;
		ItemStack casing = getSpentCasing(firedAmmo);
		if(casing.isEmpty())
			return true;
		for(int slot = 0; platformCasingHandler!=null&&slot < platformCasingHandler.getSlots(); slot++)
			if(platformCasingHandler.insertInternal(slot, casing, true).isEmpty())
				return true;
		return false;
	}

	@Nonnull
	private ItemStack getSpentCasing(ItemStack firedAmmo)
	{
		if(firedAmmo.isEmpty())
			return ItemStack.EMPTY;
		IAmmoTypeItem<?, ?> ammoType = AmmoRegistry.getAmmoItem(firedAmmo);
		return ammoType==null?ItemStack.EMPTY: ammoType.getCasingStack(1);
	}

	@Override
	protected boolean handleSupplyService(TileEntityEmplacement te)
	{
		ensureShootingComponents();
		if(!te.getWorld().isRemote&&(te.door.getState()||!te.door.isFullyClosed()))
			itemTransferTicker = 0;

		return updateResupplyState(te, this::requiresPlatformResupply, this::hasPendingBaseService, () -> {
			if(++itemTransferTicker < ITEM_TRANSFER_INTERVAL)
				return;
			itemTransferTicker = 0;

			//One physical item moves per service interval. Casings have priority over new ammunition.
			boolean changed = transferOneItem(platformCasingHandler, baseCasingHandler, false)
					||transferOneItem(baseAmmoHandler, platformAmmoHandler, false);
			if(changed)
				te.updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
		});
	}

	private boolean requiresPlatformResupply()
	{
		if(platformAmmoProvider==null)
			return false;
		return platformAmmoProvider.isEmpty()&&!platformAmmoProvider.hasAvailableAmmo()
				||platformAmmoProvider.isLoaded()&&!canStoreSpentCasing(platformAmmoProvider.peekLoadedAmmo());
	}

	private boolean hasPendingBaseService()
	{
		return hasAnyItem(platformCasingHandler)||transferOneItem(baseAmmoHandler, platformAmmoHandler, true);
	}

	private boolean hasAnyItem(@Nullable FilteredEmplacementInventoryHandler handler)
	{
		if(handler==null)
			return false;
		for(int slot = 0; slot < handler.getSlots(); slot++)
			if(!handler.getStackInSlot(slot).isEmpty())
				return true;
		return false;
	}

	private boolean transferOneItem(@Nullable FilteredEmplacementInventoryHandler source,
	                                @Nullable FilteredEmplacementInventoryHandler target, boolean simulate)
	{
		if(source==null||target==null)
			return false;
		for(int sourceSlot = 0; sourceSlot < source.getSlots(); sourceSlot++)
		{
			ItemStack candidate = source.extractItem(sourceSlot, 1, true);
			if(candidate.isEmpty())
				continue;
			for(int targetSlot = 0; targetSlot < target.getSlots(); targetSlot++)
			{
				if(!target.insertInternal(targetSlot, candidate, true).isEmpty())
					continue;
				if(simulate)
					return true;

				ItemStack extracted = source.extractItem(sourceSlot, 1, false);
				ItemStack remainder = target.insertInternal(targetSlot, extracted, false);
				if(!remainder.isEmpty())
					source.insertInternal(sourceSlot, remainder, false);
				return remainder.isEmpty();
			}
		}
		return false;
	}

	protected boolean isSpentCasing(ItemStack stack)
	{
		return ammoFactory.isValidCasing(stack);
	}

	@Nonnull
	@Override
	protected ItemStack extractBaseCasing(int amount)
	{
		if(baseCasingHandler==null||amount <= 0)
			return ItemStack.EMPTY;
		for(int slot = 0; slot < baseCasingHandler.getSlots(); slot++)
		{
			ItemStack casing = baseCasingHandler.extractItem(slot, amount, false);
			if(!casing.isEmpty())
				return casing;
		}
		return ItemStack.EMPTY;
	}


	/**
	 * @return ammunition already loaded into the weapon
	 */
	@Nonnull
	public NonNullList<ItemStack> getLoadedAmmo()
	{
		ensureShootingComponents();
		return platformAmmoProvider==null?NonNullList.create(): platformAmmoProvider.getLoadedAmmoList();
	}

	/**
	 * @return ammunition that must be rendered during loading or while loaded
	 */
	@Nonnull
	public NonNullList<ItemStack> getRenderAmmo()
	{
		ensureShootingComponents();
		return platformAmmoProvider==null?NonNullList.create(): platformAmmoProvider.getRenderAmmoList();
	}

	public int getLoadedRoundCount()
	{
		ensureShootingComponents();
		return platformAmmoProvider==null?0: platformAmmoProvider.getLoadedRoundCount();
	}

	public int getReloadStage()
	{
		ensureShootingComponents();
		return platformAmmoProvider==null?0: platformAmmoProvider.getLoadStage();
	}

	public boolean isFinalReloadBatch()
	{
		ensureShootingComponents();
		return platformAmmoProvider!=null&&platformAmmoProvider.isFinalBatch();
	}

	public boolean isReloading()
	{
		ensureShootingComponents();
		return platformAmmoProvider!=null&&platformAmmoProvider.isReloading();
	}

	public boolean isLoaded()
	{
		ensureShootingComponents();
		return platformAmmoProvider!=null&&platformAmmoProvider.isLoaded();
	}

	public float getReloadProgress(float partialTicks)
	{
		ensureShootingComponents();
		return platformAmmoProvider==null?0: platformAmmoProvider.getLoadingProgress(partialTicks);
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
	public IItemHandler getBaseItemHandler()
	{
		return baseItemHandler;
	}

	@Nullable
	@Override
	public IItemHandler getPlatformItemHandler()
	{
		return platformItemHandler;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return (baseAmmoHandler!=null&&baseAmmoHandler.acceptsAbsoluteSlot(slot, stack))
				||(baseCasingHandler!=null&&baseCasingHandler.acceptsAbsoluteSlot(slot, stack))
				||(platformAmmoHandler!=null&&platformAmmoHandler.acceptsAbsoluteSlot(slot, stack))
				||(platformCasingHandler!=null&&platformCasingHandler.acceptsAbsoluteSlot(slot, stack));
	}

	/**
	 * Creates separate ammo and casing views while preserving two combined GUI inventories.
	 */
	protected final void setupItemHandlers(TileEntityEmplacement te,
	                                       int baseAmmoSlots, int baseCasingSlots, int platformAmmoSlots, int platformCasingSlots,
	                                       Predicate<ItemStack> baseFilter, Predicate<ItemStack> platformFilter)
	{
		this.baseAmmoHandler = createHandler(te, baseAmmoSlots, 0, baseFilter, true);
		this.baseCasingHandler = createHandler(te, baseCasingSlots, baseAmmoSlots, this::isSpentCasing, false);

		int baseSlots = baseAmmoSlots+baseCasingSlots;
		this.platformAmmoHandler = createHandler(te, platformAmmoSlots, baseSlots, platformFilter, true);
		this.platformCasingHandler = createHandler(te, platformCasingSlots, baseSlots+platformAmmoSlots, this::isSpentCasing, false);

		this.baseItemHandler = combineHandlers(baseAmmoHandler, baseCasingHandler);
		this.platformItemHandler = combineHandlers(platformAmmoHandler, platformCasingHandler);
		this.platformAmmoProvider = null;
		ensureShootingComponents();
	}

	@Nullable
	private FilteredEmplacementInventoryHandler createHandler(TileEntityEmplacement te, int slots, int offset,
	                                                          Predicate<ItemStack> filter, boolean externalInsert)
	{
		return slots > 0?new FilteredEmplacementInventoryHandler(slots, te, offset, filter, externalInsert): null;
	}

	@Nullable
	private IItemHandler combineHandlers(@Nullable FilteredEmplacementInventoryHandler primary,
	                                     @Nullable FilteredEmplacementInventoryHandler secondary)
	{
		if(primary==null)
			return secondary;
		if(secondary==null)
			return primary;
		return new CombinedInvWrapper(primary, secondary);
	}

	/**
	 * Adds weapon filters and controlled internal insertion to IE's tile inventory handler.
	 */
	protected static class FilteredEmplacementInventoryHandler extends IEInventoryHandler
	{
		private final Predicate<ItemStack> filter;
		private final int slotOffset;
		private final boolean externalInsert;

		public FilteredEmplacementInventoryHandler(int slots, TileEntityEmplacement inventory, int slotOffset,
		                                           Predicate<ItemStack> filter, boolean externalInsert)
		{
			super(slots, inventory, slotOffset, true, true);
			this.filter = filter==null?stack -> true: filter;
			this.slotOffset = slotOffset;
			this.externalInsert = externalInsert;
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
		{
			if(!externalInsert||!accepts(stack))
				return stack;
			return super.insertItem(slot, stack, simulate);
		}

		@Override
		public void setStackInSlot(int slot, @Nonnull ItemStack stack)
		{
			if(!stack.isEmpty()&&(!externalInsert||!filter.test(stack)))
				return;
			super.setStackInSlot(slot, stack);
		}

		public ItemStack insertInternal(int slot, ItemStack stack, boolean simulate)
		{
			return accepts(stack)?super.insertItem(slot, stack, simulate): stack;
		}

		public boolean accepts(ItemStack stack)
		{
			return !stack.isEmpty()&&filter.test(stack);
		}

		public boolean acceptsAbsoluteSlot(int absoluteSlot, ItemStack stack)
		{
			int localSlot = absoluteSlot-slotOffset;
			return localSlot >= 0&&localSlot < getSlots()&&accepts(stack);
		}
	}
}
