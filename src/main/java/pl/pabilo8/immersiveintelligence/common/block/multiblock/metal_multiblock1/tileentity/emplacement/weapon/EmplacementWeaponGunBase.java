package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
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
 * @updated 31.08.2026
 * @since 04.09.2025
 */
public abstract class EmplacementWeaponGunBase<A extends EntityAmmoBase<A>> extends EmplacementWeaponTurretBase
{

	/**
	 * Used to fire ammo for the weapon.
	 */
	protected AmmoFactory<A> ammoFactory;
	@Nullable
	protected FilteredEmplacementInventoryHandler baseAmmoHandler, baseCasingHandler;
	@Nullable
	protected FilteredEmplacementInventoryHandler platformAmmoHandler, platformCasingHandler;
	/**
	 * Allows target tracking during a post-shot reload. Heavy weapons can disable this to hold the loading pose.
	 */
	protected boolean rotateAfterFiring = true;
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_RELOAD)
	public float lastFiringYaw = 0, lastFiringPitch = 0;
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_RELOAD)
	public boolean hasLastFiringAngles = false;
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_RELOAD)
	public boolean reloadAfterFiring = false;
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_RELOAD)
	public int fireTimeCounter = 0;
	private boolean returnToLastFiringAngles = false;
	private int itemTransferTicker = 0;
	private transient int clientFireTimeCounter = 0;
	private transient boolean fireTimeCounterChanged = false;

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
		if(te.getWorld().isRemote)
			this.clientFireTimeCounter = this.fireTimeCounter;
	}

	@Override
	public void onPlatformUpdate(TileEntityEmplacement te)
	{
		ensureShootingComponents();
		if(platformAmmoProvider!=null)
		{
			boolean wasReloading = platformAmmoProvider.isReloading();
			boolean wasUnloading = platformAmmoProvider.isUnloading();
			int previousStage = platformAmmoProvider.getLoadStage();
			gunHandler.update();

			boolean reloadFinished = wasReloading&&!platformAmmoProvider.isReloading();
			if(reloadFinished&&reloadAfterFiring&&hasLastFiringAngles)
				returnToLastFiringAngles = true;
			if(reloadFinished)
				reloadAfterFiring = false;
			if(previousStage!=platformAmmoProvider.getLoadStage()
					||wasUnloading!=platformAmmoProvider.isUnloading()||reloadFinished)
				syncWithClient(te, SyncEvents.WEAPON_RELOAD);
		}
		else
			gunHandler.update();

		super.onPlatformUpdate(te);
	}

	@Override
	public EmplacementStateNeeds onUpdate(TileEntityEmplacement te, EmplacementStateNeeds baseNeeds, TargetCoordinateReference currentTarget)
	{
		if(te.getOwnerIdentity()!=null)
			ammoFactory.setOwner(te.getOwnerIdentity().getFirstResponsibleMember(te.getWorld()));

		ensureShootingComponents();
		if(currentTarget!=null)
			returnToLastFiringAngles = false;

		if(platformAmmoProvider!=null)
		{
			boolean readyForLoading = te.door.getState()&&te.door.isFullyOpened()&&(setup==null||setup.isFullyOpened())
					&&(!reloadAfterFiring||gunHandler.getShotDelay(0)==0);
			boolean wantsReload = platformAmmoProvider.isEmpty()&&platformAmmoProvider.hasAvailableAmmo();

			if(readyForLoading&&(wantsReload||(platformAmmoProvider.isReloading()&&!rotateAfterFiring)))
			{
				Float loadingYaw = getLoadingYaw();
				Float loadingPitch = getLoadingPitch();
				setAimTargetAngles(te, loadingYaw, loadingPitch);
				if(wantsReload&&isAtAngles(loadingYaw, loadingPitch, 1.5f))
				{
					platformAmmoProvider.prepareReload();
					if(gunHandler.startReloading())
						syncWithClient(te, SyncEvents.WEAPON_RELOAD);
				}
			}
		}

		if(returnToLastFiringAngles&&currentTarget==null)
		{
			setAimTargetAngles(te, lastFiringYaw, lastFiringPitch);
			if(isAtAngles(lastFiringYaw, lastFiringPitch, 1.5f))
				returnToLastFiringAngles = false;
		}

		return super.onUpdate(te, baseNeeds, currentTarget);
	}

	@Override
	public void onClientUpdate(TileEntityEmplacement te)
	{
		this.fireTimeCounterChanged = this.clientFireTimeCounter!=this.fireTimeCounter;
		this.clientFireTimeCounter = this.fireTimeCounter;
		ensureShootingComponents();
		gunHandler.update();
		super.onClientUpdate(te);
	}

	@Override
	protected boolean canChill(TileEntityEmplacement te)
	{
		ensureShootingComponents();
		return super.canChill(te)&&gunHandler.canShoot()&&!returnToLastFiringAngles&&aim.isAimed(1.5f)
				&&(platformAmmoProvider==null||!platformAmmoProvider.isReloading());
	}

	protected void ensureShootingComponents()
	{
		if(platformAmmoProvider==null&&platformAmmoHandler!=null)
		{
			platformAmmoProvider = createPlatformAmmoProvider().withLoadStages(getReloadStages());
			//noinspection unchecked
			gunHandler.withAmmoProvider(platformAmmoProvider)
					.withAmmoFactory((AmmoFactory<? extends EntityAmmoProjectile>)ammoFactory)
					.withRecoilHandler(recoil)
					.withMaxShotDelay(getShotDelay());
		}
	}

	/**
	 * Creates the ammunition provider used by the Platform inventory.
	 *
	 * @return ammunition provider
	 */
	protected GunAmmoProviderItemHandler createPlatformAmmoProvider()
	{
		return new GunAmmoProviderItemHandler(null, () -> null, platformAmmoHandler,
				ammoFactory::isValidAmmo, getReloadDelay());
	}

	/**
	 * @return round counts loaded by successive reload animation stages
	 */
	protected int[] getReloadStages()
	{
		return new int[]{Integer.MAX_VALUE};
	}

	/**
	 * @return number of visual firing animation variants
	 */
	public int getFireAnimationVariants()
	{
		return 1;
	}

	/**
	 * @return true when the synced fire counter changed during this client tick
	 */
	public boolean didFireAnimationAdvance()
	{
		return fireTimeCounterChanged;
	}

	/**
	 * Gets the world-space offset added to the Emplacement weapon centre for aiming and firing.
	 *
	 * @return weapon origin offset
	 */
	public Vec3d getWeaponOffset()
	{
		return Vec3d.ZERO;
	}

	@Override
	protected Vec3d getAimOrigin(TileEntityEmplacement te)
	{
		return te.getWeaponCenter().add(getWeaponOffset());
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
		return null;
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
		if(platformAmmoProvider==null||returnToLastFiringAngles)
			return false;
		return platformAmmoProvider.isLoaded()||(rotateAfterFiring&&reloadAfterFiring&&platformAmmoProvider.isReloading());
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

		ammoFactory.setPositionAndVelocity(getAimOrigin(te), this.aim, 0.25f, 1f)
				.setShooterAndGun(null, baseEntity)
				.setIgnoredEntities(te.tactileHandler.getEntities());
		boolean fired = gunHandler.fire();
		if(fired)
		{
			fireTimeCounter++;
			lastFiringYaw = aim.getRelativeYaw(0);
			lastFiringPitch = aim.getPitch(0);
			hasLastFiringAngles = true;
			reloadAfterFiring = platformAmmoProvider.isEmpty();
			if(storesSpentCasings()&&storeSpentCasing(firedAmmo))
				te.updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
		}
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
		return !casing.isEmpty()&&storePlatformSpentItem(casing).isEmpty();
	}

	/**
	 * Stores a spent item in the Platform output inventory.
	 *
	 * @param stack spent item
	 * @return item that could not be stored
	 */
	@Nonnull
	protected ItemStack storePlatformSpentItem(ItemStack stack)
	{
		ItemStack remainder = stack.copy();
		for(int slot = 0; platformCasingHandler!=null&&slot < platformCasingHandler.getSlots()&&!remainder.isEmpty(); slot++)
			remainder = platformCasingHandler.insertInternal(slot, remainder, false);
		return remainder;
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
			if(++itemTransferTicker < getItemTransferSpeed())
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
		return platformAmmoProvider.isUnloadBlocked()
				||platformAmmoProvider.isEmpty()&&!platformAmmoProvider.hasAvailableAmmo()
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
			ItemStack candidate = source.extractInternal(sourceSlot, 1, true);
			if(candidate.isEmpty())
				continue;
			for(int targetSlot = 0; targetSlot < target.getSlots(); targetSlot++)
			{
				if(!target.insertInternal(targetSlot, candidate, true).isEmpty())
					continue;
				if(simulate)
					return true;

				ItemStack extracted = source.extractInternal(sourceSlot, 1, false);
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
		return platformAmmoProvider==null?NonNullList.create(): platformAmmoProvider.getRenderAmmoList();
	}

	/**
	 * @return ammunition present inside the weapon
	 */
	@Nonnull
	public NonNullList<ItemStack> getAllAmmo()
	{
		ensureShootingComponents();
		return platformAmmoProvider==null?NonNullList.create(): platformAmmoProvider.getAmmoList();
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

	/**
	 * @return true while the weapon unloads ammunition
	 */
	public boolean isUnloading()
	{
		ensureShootingComponents();
		return platformAmmoProvider!=null&&platformAmmoProvider.isUnloading();
	}

	/**
	 * @return forward 0-1 progress for the active load or unload animation
	 */
	public float getReloadAnimationProgress(float partialTicks)
	{
		float progress = getReloadProgress(partialTicks);
		return isUnloading()?1f-progress: progress;
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
	public IItemHandler getBaseItemHandler(boolean input)
	{
		return input?baseAmmoHandler: baseCasingHandler;
	}

	@Nullable
	@Override
	public IItemHandler getPlatformItemHandler(boolean input)
	{
		return input?platformAmmoHandler: platformCasingHandler;
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
	 * Creates separate ammo and casing views for Base and Platform storage.
	 */
	protected final void setupItemHandlers(TileEntityEmplacement te,
	                                       int baseAmmoSlots, int baseCasingSlots, int platformAmmoSlots, int platformCasingSlots,
	                                       Predicate<ItemStack> baseFilter, Predicate<ItemStack> platformFilter)
	{
		this.baseAmmoHandler = createHandler(te, baseAmmoSlots, 0, baseFilter, true, false);
		this.baseCasingHandler = createHandler(te, baseCasingSlots, baseAmmoSlots, this::isSpentCasing, false, true);

		int baseSlots = baseAmmoSlots+baseCasingSlots;
		this.platformAmmoHandler = createHandler(te, platformAmmoSlots, baseSlots, platformFilter, true, true);
		this.platformCasingHandler = createHandler(te, platformCasingSlots, baseSlots+platformAmmoSlots, this::isSpentCasing, false, true);

		this.platformAmmoProvider = null;
		ensureShootingComponents();
	}

	@Nullable
	private FilteredEmplacementInventoryHandler createHandler(TileEntityEmplacement te, int slots, int offset,
	                                                          Predicate<ItemStack> filter, boolean externalInsert,
	                                                          boolean externalExtract)
	{
		return slots > 0?new FilteredEmplacementInventoryHandler(slots, te, offset, filter, externalInsert, externalExtract): null;
	}

	/**
	 * Adds weapon filters and separate external and internal access to IE's tile inventory handler.
	 */
	protected static class FilteredEmplacementInventoryHandler extends IEInventoryHandler
	{
		private final Predicate<ItemStack> filter;
		private final int slotOffset;
		private final boolean externalInsert, externalExtract;

		public FilteredEmplacementInventoryHandler(int slots, TileEntityEmplacement inventory, int slotOffset,
		                                           Predicate<ItemStack> filter, boolean externalInsert, boolean externalExtract)
		{
			super(slots, inventory, slotOffset, true, true);
			this.filter = filter==null?stack -> true: filter;
			this.slotOffset = slotOffset;
			this.externalInsert = externalInsert;
			this.externalExtract = externalExtract;
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
		{
			if(!externalInsert||!accepts(stack))
				return stack;
			return super.insertItem(slot, stack, simulate);
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate)
		{
			return externalExtract?super.extractItem(slot, amount, simulate): ItemStack.EMPTY;
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

		public ItemStack extractInternal(int slot, int amount, boolean simulate)
		{
			return super.extractItem(slot, amount, simulate);
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
