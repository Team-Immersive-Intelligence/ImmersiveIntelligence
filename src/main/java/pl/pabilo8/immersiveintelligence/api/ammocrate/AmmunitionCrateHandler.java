package pl.pabilo8.immersiveintelligence.api.ammocrate;

import blusunrize.immersiveengineering.api.tool.BulletHandler;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IBulletContainer;
import blusunrize.immersiveengineering.common.items.ItemBullet;
import blusunrize.immersiveengineering.common.items.ItemRevolver;
import blusunrize.immersiveengineering.common.items.ItemSpeedloader;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.entity.mounted_weapon.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIAssaultRifle;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIGunBase;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIRifle;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIISubmachinegun;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandler;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandlerMagazine;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.gun.ammoprovider.GunAmmoProviderMagazine;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Predicate;

/**
 * Defines ammunition-crate modes and weapon reload handlers.
 *
 * @author Pabilo8(pabilo@iiteam.net)
 * @updated 11.08.2026
 * @since 10.08.2026
 */
public class AmmunitionCrateHandler
{
	public static final String KEY_CRATE_RELOAD = "ii_crate_reload";
	public static final String KEY_CRATE_RELOAD_PREVIEW = "ii_crate_reload_preview";

	private static final EnumMap<AmmunitionCrateMode, LinkedHashMap<Predicate<ItemStack>, ReloadHandler>> RELOAD_HANDLERS =
			new EnumMap<>(AmmunitionCrateMode.class);
	private static final Map<Entity, ReloadContext> RELOAD_CONTEXTS = new WeakHashMap<>();
	private static final Map<EntityMachinegun, MountedReloadSource> MOUNTED_RELOAD_SOURCES = new WeakHashMap<>();

	/**
	 * Registers default Immersive Intelligence ammunition crate modes.
	 */
	public static void init()
	{
		registerReloadHandler(AmmunitionCrateMode.REVOLVER,
				stack -> stack.getItem() instanceof ItemRevolver||stack.getItem() instanceof ItemSpeedloader,
				AmmunitionCrateHandler::reloadRevolver);

		registerGunHandler(AmmunitionCrateMode.SUBMACHINEGUN, stack -> stack.getItem() instanceof ItemIISubmachinegun);
		registerGunHandler(AmmunitionCrateMode.ASSAULT_RIFLE,
				stack -> stack.getItem() instanceof ItemIIAssaultRifle&&ItemNBTHelper.getInt(stack, ItemIIAssaultRifle.FIRE_MODE) < 2);
		registerGunHandler(AmmunitionCrateMode.RIFLE_BULLETS,
				stack -> stack.getItem() instanceof ItemIIRifle&&!((ItemIIRifle)stack.getItem()).hasIIUpgrade(stack, WeaponUpgrade.SEMI_AUTOMATIC));
		registerGunHandler(AmmunitionCrateMode.RIFLE_MAGAZINES,
				stack -> stack.getItem() instanceof ItemIIRifle&&((ItemIIRifle)stack.getItem()).hasIIUpgrade(stack, WeaponUpgrade.SEMI_AUTOMATIC));

		registerMagazineHandler(AmmunitionCrateMode.SUBMACHINEGUN, Magazines.SUBMACHINEGUN, Magazines.SUBMACHINEGUN_DRUM);
		registerMagazineHandler(AmmunitionCrateMode.ASSAULT_RIFLE, Magazines.ASSAULT_RIFLE);
		registerMagazineHandler(AmmunitionCrateMode.RIFLE_MAGAZINES, Magazines.RIFLE);
		registerMagazineHandler(AmmunitionCrateMode.MACHINEGUN_MAGAZINES, Magazines.MACHINEGUN);
	}

	/**
	 * Registers a right-click reload handler for a crate mode.
	 */
	public static void registerReloadHandler(AmmunitionCrateMode mode, Predicate<ItemStack> predicate, ReloadHandler handler)
	{
		RELOAD_HANDLERS.computeIfAbsent(mode, ignored -> new LinkedHashMap<>()).put(predicate, handler);
	}

	/**
	 * Tests whether a stack can be stored in a physical crate slot.
	 */
	public static boolean isStackValid(AmmunitionCrateMode mode, int slot, ItemStack stack)
	{
		if(slot >= ContainerAmmunitionCrate.AMMUNITION_START
				&&slot < ContainerAmmunitionCrate.AMMUNITION_START+ContainerAmmunitionCrate.AMMUNITION_COUNT)
			return mode.isAmmunition(stack);

		if(slot >= ContainerAmmunitionCrate.SPENT_START
				&&slot < ContainerAmmunitionCrate.SPENT_START+ContainerAmmunitionCrate.SPENT_COUNT)
		{
			if(mode.hasSpentMagazineSlots()&&slot >= ContainerAmmunitionCrate.SPENT_MAGAZINE_START)
				return mode.isSpentMagazine(stack);
			return mode.isSpentCasing(stack);
		}

		if(slot >= ContainerAmmunitionCrate.REVOLVER_PATTERN_START&&slot < ContainerAmmunitionCrate.INVENTORY_SIZE)
			return mode==AmmunitionCrateMode.REVOLVER&&mode.isPatternAmmunition(stack);
		return false;
	}

	/**
	 * Tests whether the open crate can handle the held item.
	 */
	public static boolean canReload(AmmunitionCrateMode mode, ItemStack stack)
	{
		LinkedHashMap<Predicate<ItemStack>, ReloadHandler> handlers = RELOAD_HANDLERS.get(mode);
		return handlers!=null&&handlers.keySet().stream().anyMatch(predicate -> predicate.test(stack));
	}

	/**
	 * Starts a registered reload action for the held item.
	 */
	public static boolean reload(TileEntityAmmunitionCrate crate, EntityPlayer player, EnumHand hand)
	{
		ItemStack held = player.getHeldItem(hand);
		LinkedHashMap<Predicate<ItemStack>, ReloadHandler> handlers = RELOAD_HANDLERS.get(crate.mode);
		if(handlers==null)
			return false;

		for(Map.Entry<Predicate<ItemStack>, ReloadHandler> entry : handlers.entrySet())
			if(entry.getKey().test(held))
			{
				if(entry.getValue().reload(crate, player, hand, held))
				{
					player.addPotionEffect(new PotionEffect(IIPotions.wellSupplied, 100));
					return true;
				}
			}
		return false;
	}

	/**
	 * Tests whether the inserter can service an entity now.
	 */
	public static boolean canAutomate(TileEntityAmmunitionCrate crate, Entity entity)
	{
		if(entity instanceof EntityPlayer player)
			return canAutomatePlayer(crate, player);
		if(entity instanceof EntityMachinegun machinegun)
			return canAutomateMachinegun(crate, machinegun);
		return false;
	}

	/**
	 * Starts one inserter reload action.
	 */
	public static boolean reloadAutomatically(TileEntityAmmunitionCrate crate, Entity entity)
	{
		if(entity instanceof EntityPlayer player)
		{
			ItemStack weapon = player.getHeldItemMainhand();
			if(hasReloadContext(player, weapon)||!canAutomatePlayer(crate, player))
				return false;
			return reload(crate, player, EnumHand.MAIN_HAND);
		}
		if(entity instanceof EntityMachinegun machinegun)
			return reloadMountedMachinegun(crate, machinegun);
		return false;
	}

	/**
	 * Gets the current inserter travel for the active reload action.
	 */
	public static float getAutomaticReloadProgress(Entity entity, float partialTicks)
	{
		if(entity instanceof EntityPlayer player)
		{
			ItemStack weapon = player.getHeldItemMainhand();
			if(weapon.getItem() instanceof ItemIIGunBase gun)
			{
				EasyNBT nbt = EasyNBT.wrapNBT(weapon);
				int reloading = nbt.getInt(ItemIIGunBase.RELOADING);
				if(reloading <= 0)
					return 0;

				AmmoHandler ammoHandler = gun.getAmmoHandler(weapon);
				ItemStack loaded = ammoHandler instanceof AmmoHandlerMagazine magazineHandler?
						magazineHandler.getLoadedMagazine(nbt): ItemStack.EMPTY;
				int reloadTime = Math.max(1, gun.getReloadTime(weapon, loaded, EasyNBT.wrapNBT(gun.getUpgrades(weapon))));
				return pingPong(MathHelper.clamp((reloading+partialTicks)/reloadTime, 0f, 1f));
			}
			if(weapon.getItem() instanceof ItemRevolver)
			{
				int maxReload = Math.max(1, Math.round(30*Tools.ammunitionCrateResupplyTime));
				return pingPong(MathHelper.clamp(ItemNBTHelper.getInt(weapon, "reload")/(float)maxReload, 0f, 1f));
			}
		}
		else if(entity instanceof EntityMachinegun machinegun)
		{
			GunAmmoProviderMagazine provider = getActiveMachinegunMagazine(machinegun);
			if(provider!=null&&provider.isReloading())
				return pingPong(provider.getLoadingProgress(partialTicks));
		}
		return 0;
	}

	/**
	 * Returns ammunition reserved for an active crate reload.
	 */
	public static ItemStack findReloadAmmunition(Entity entity, ItemStack target, Predicate<ItemStack> predicate)
	{
		ReloadContext context = getContext(entity, target);
		if(context==null)
			return entity.world.isRemote?getClientReloadPreview(target, predicate): ItemStack.EMPTY;

		if(!context.reserved.isEmpty())
		{
			if(predicate.test(context.reserved))
				return context.reserved;
			context.returnReserved();
		}

		context.reserved = context.crate.extractAmmunition(predicate);
		context.setPreview(context.reserved);
		return context.reserved;
	}

	/**
	 * Returns true if an entity currently reloads the specified item from a crate.
	 */
	public static boolean hasReloadContext(Entity entity, ItemStack target)
	{
		return getContext(entity, target)!=null;
	}

	/**
	 * Tests whether the entity has a server reload context or a synced client preview.
	 */
	public static boolean hasReloadSource(Entity entity, ItemStack target)
	{
		return hasReloadContext(entity, target)
				||(entity.world.isRemote&&EasyNBT.wrapNBT(target).getBoolean(KEY_CRATE_RELOAD));
	}

	/**
	 * Returns an unloaded magazine to the crate when possible.
	 */
	public static boolean returnReloadedMagazine(Entity entity, ItemStack target, ItemStack magazine)
	{
		ReloadContext context = getContext(entity, target);
		if(context==null||magazine.isEmpty())
			return false;

		context.deferReturnedMagazine(magazine);
		magazine.setCount(0);
		return true;
	}

	/**
	 * Tests whether a magazine-fed gun must continue after its unload stage.
	 */
	public static boolean shouldContinueGunReload(Entity entity, ItemStack weapon, AmmoHandler handler, EasyNBT nbt)
	{
		if(!(handler instanceof AmmoHandlerMagazine)||handler.canFire(weapon, nbt))
			return false;

		ReloadContext context = getContext(entity, weapon);
		if(context!=null)
			return context.crate.hasAmmunition(stack -> handler.isValidAmmo(weapon, stack));
		return entity.world.isRemote
				&&!getClientReloadPreview(weapon, stack -> handler.isValidAmmo(weapon, stack)).isEmpty();
	}

	/**
	 * Ends a crate-backed reload and returns reserved items.
	 */
	public static void finishReload(Entity entity, ItemStack target)
	{
		ReloadContext context = getContext(entity, target);
		if(context==null)
			return;
		context.finish();
		RELOAD_CONTEXTS.remove(entity);
	}

	/**
	 * Finds a machinegun magazine from a direct or inserter crate source.
	 */
	@Nullable
	public static ItemStack findMountedMagazine(Entity gun, Magazines expected)
	{
		TileEntityAmmunitionCrate crate = getMountedCrate(gun, AmmunitionCrateMode.MACHINEGUN_MAGAZINES, true);
		if(crate==null)
			return null;
		return crate.findAmmunition(stack -> isMagazine(stack, expected)&&!IIContent.itemBulletMagazine.hasNoBullets(stack));
	}

	/**
	 * Removes a magazine that was loaded into a mounted machinegun.
	 */
	public static boolean consumeMountedMagazine(Entity gun, ItemStack magazine)
	{
		TileEntityAmmunitionCrate crate = getMountedCrate(gun, AmmunitionCrateMode.MACHINEGUN_MAGAZINES, false);
		boolean consumed = crate!=null&&crate.removeAmmunitionStack(magazine);
		finishMountedLoadSource(gun);
		return consumed;
	}

	/**
	 * Marks a crate-backed magazine change as dirty.
	 */
	public static void markMountedMagazineChanged(Entity gun)
	{
		TileEntityAmmunitionCrate crate = getMountedCrate(gun, AmmunitionCrateMode.MACHINEGUN_MAGAZINES, false);
		if(crate!=null)
			crate.markDirty();
		finishMountedLoadSource(gun);
	}

	/**
	 * Returns a mounted machinegun magazine to its Ammunition Crate.
	 */
	public static boolean returnMountedMagazine(Entity gun, Magazines expected, ItemStack magazine)
	{
		TileEntityAmmunitionCrate crate = getMountedCrate(gun, AmmunitionCrateMode.MACHINEGUN_MAGAZINES, false);
		if(crate==null||!isMagazine(magazine, expected))
			return false;
		return crate.insertReturnedMagazine(magazine.copy()).isEmpty();
	}

	/**
	 * Extracts one belt-fed machinegun round from an adjacent crate.
	 */
	public static ItemStack extractMountedBullet(Entity gun)
	{
		TileEntityAmmunitionCrate crate = getMountedCrate(gun, AmmunitionCrateMode.MACHINEGUN_BELT_FED, false);
		return crate==null?ItemStack.EMPTY: crate.extractAmmunition(AmmunitionCrateHandler::isMachinegunRound);
	}

	/**
	 * Tests whether an adjacent belt-fed crate has ammunition.
	 */
	public static boolean hasMountedBullet(Entity gun)
	{
		TileEntityAmmunitionCrate crate = getMountedCrate(gun, AmmunitionCrateMode.MACHINEGUN_BELT_FED, false);
		return crate!=null&&crate.hasAmmunition(AmmunitionCrateHandler::isMachinegunRound);
	}

	/**
	 * Gets belt-fed ammunition for rendering.
	 */
	public static NonNullList<ItemStack> getMountedAmmunition(Entity gun)
	{
		TileEntityAmmunitionCrate crate = getMountedCrate(gun, AmmunitionCrateMode.MACHINEGUN_BELT_FED, false);
		return crate==null?NonNullList.create(): crate.getAmmunitionStacks();
	}

	private static boolean canAutomatePlayer(TileEntityAmmunitionCrate crate, EntityPlayer player)
	{
		ItemStack weapon = player.getHeldItemMainhand();
		if(!canReload(crate.mode, weapon))
			return false;
		ReloadContext reloadContext = getContext(player, weapon);
		if(reloadContext!=null)
			return reloadContext.crate==crate;

		if(weapon.getItem() instanceof ItemIIGunBase gun)
		{
			AmmoHandler handler = gun.getAmmoHandler(weapon);
			EasyNBT nbt = EasyNBT.wrapNBT(weapon);
			if(handler.canFire(weapon, nbt))
				return false;

			if(handler instanceof AmmoHandlerMagazine magazineHandler&&!magazineHandler.getLoadedMagazine(nbt).isEmpty())
				return true;
			return crate.hasAmmunition(stack -> handler.isValidAmmo(weapon, stack));
		}
		return weapon.getItem() instanceof ItemRevolver&&needsRevolverReload(crate, weapon);
	}

	private static boolean canAutomateMachinegun(TileEntityAmmunitionCrate crate, EntityMachinegun machinegun)
	{
		if(crate.mode!=AmmunitionCrateMode.MACHINEGUN_MAGAZINES||machinegun.upgrades.contains(WeaponUpgrade.BELT_FED_LOADER))
			return false;

		GunAmmoProviderMagazine provider = getActiveMachinegunMagazine(machinegun);
		if(provider==null)
			return false;
		if(provider.isReloading())
		{
			MountedReloadSource source = MOUNTED_RELOAD_SOURCES.get(machinegun);
			return source!=null&&source.crate==crate;
		}
		if(provider.isLoaded())
		{
			ItemStack loaded = provider.getLoadedStack();
			boolean emptyMagazine = !loaded.isEmpty()&&IIContent.itemBulletMagazine.hasNoBullets(loaded);
			if(!emptyMagazine)
				clearMountedReloadSource(machinegun, crate);
			return emptyMagazine;
		}
		return provider.isEmpty()&&crate.hasAmmunition(stack -> isLiveMagazine(stack, Magazines.MACHINEGUN));
	}

	private static boolean reloadMountedMachinegun(TileEntityAmmunitionCrate crate, EntityMachinegun machinegun)
	{
		if(!canAutomateMachinegun(crate, machinegun))
			return false;

		GunAmmoProviderMagazine provider = getActiveMachinegunMagazine(machinegun);
		if(provider==null||provider.isReloading())
			return false;

		MountedReloadSource source = MOUNTED_RELOAD_SOURCES.computeIfAbsent(machinegun, ignored -> new MountedReloadSource());
		source.crate = crate;
		source.loadingAuthorized = provider.isEmpty();
		boolean started = provider.startReloading();
		if(started)
			machinegun.updateEntityForEvent(SyncEvents.ENTITY_CUSTOM1);
		return started;
	}

	@Nullable
	private static GunAmmoProviderMagazine getActiveMachinegunMagazine(EntityMachinegun machinegun)
	{
		if(machinegun.upgrades.contains(WeaponUpgrade.BELT_FED_LOADER))
			return null;
		return machinegun.upgrades.contains(WeaponUpgrade.SECOND_MAGAZINE)?machinegun.loadingMagazine2: machinegun.loadingMagazine1;
	}

	private static boolean needsRevolverReload(TileEntityAmmunitionCrate crate, ItemStack weapon)
	{
		IItemHandler bulletHandler = weapon.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(bulletHandler==null)
			return false;

		int chambers = Math.min(ContainerAmmunitionCrate.REVOLVER_PATTERN_COUNT, bulletHandler.getSlots());
		for(int chamber = 0; chamber < chambers; chamber++)
		{
			ItemStack required = crate.getInventory().get(ContainerAmmunitionCrate.REVOLVER_PATTERN_START+chamber);
			if(required.isEmpty())
				continue;
			ItemStack loaded = bulletHandler.getStackInSlot(chamber);
			if((loaded.isEmpty()||isRevolverCasing(loaded)||!stacksMatch(loaded, required))
					&&crate.hasAmmunition(stack -> stacksMatch(stack, required)))
				return true;
		}
		return false;
	}

	@Nullable
	private static ReloadContext getContext(Entity entity, ItemStack target)
	{
		ReloadContext context = RELOAD_CONTEXTS.get(entity);
		return context!=null&&context.matches(target)?context: null;
	}

	private static void beginReload(TileEntityAmmunitionCrate crate, Entity entity, ItemStack target, Predicate<ItemStack> ammunition)
	{
		ReloadContext previous = RELOAD_CONTEXTS.remove(entity);
		if(previous!=null)
			previous.finish();

		ReloadContext context = new ReloadContext(crate, entity, target);
		context.setPreview(crate.findAmmunition(ammunition));
		RELOAD_CONTEXTS.put(entity, context);
	}

	public static ItemStack getClientReloadPreview(ItemStack target, Predicate<ItemStack> predicate)
	{
		EasyNBT nbt = EasyNBT.wrapNBT(target);
		if(!nbt.getBoolean(KEY_CRATE_RELOAD))
			return ItemStack.EMPTY;
		ItemStack preview = nbt.getItemStack(KEY_CRATE_RELOAD_PREVIEW);
		return !preview.isEmpty()&&predicate.test(preview)?preview: ItemStack.EMPTY;
	}

	public static void registerGunHandler(AmmunitionCrateMode mode, Predicate<ItemStack> predicate)
	{
		registerReloadHandler(mode, predicate, (crate, player, hand, stack) -> {
			ItemIIGunBase gun = (ItemIIGunBase)stack.getItem();
			AmmoHandler handler = gun.getAmmoHandler(stack);
			beginReload(crate, player, stack, ammo -> handler.isValidAmmo(stack, ammo));
			gun.startReload(stack);
			return true;
		});
	}

	public static void registerMagazineHandler(AmmunitionCrateMode mode, Magazines... magazines)
	{
		registerReloadHandler(mode,
				stack -> stack.getItem()==IIContent.itemBulletMagazine&&isMagazine(stack, magazines),
				(crate, player, hand, stack) -> {
					Magazines magazine = IIContent.itemBulletMagazine.stackToSub(stack);
					beginReload(crate, player, stack,
							ammo -> ammo.getItem()==magazine.ammo&&!magazine.ammo.isBulletCore(ammo));
					((ItemIIBulletMagazine)stack.getItem()).startReload(stack);
					return true;
				});
	}

	static boolean reloadRevolver(TileEntityAmmunitionCrate crate, EntityPlayer player, EnumHand hand, ItemStack held)
	{
		if(!(held.getItem() instanceof IBulletContainer))
			return false;

		if(held.getItem() instanceof ItemRevolver)
			ItemNBTHelper.setInt(held, "reload", Math.round(30*Tools.ammunitionCrateResupplyTime));

		IItemHandler bulletHandler = held.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(bulletHandler==null)
			return false;

		int chambers = Math.min(ContainerAmmunitionCrate.REVOLVER_PATTERN_COUNT, bulletHandler.getSlots());
		for(int chamber = 0; chamber < chambers; chamber++)
		{
			ItemStack unloaded = bulletHandler.extractItem(chamber, 1, false);
			if(unloaded.isEmpty())
				continue;

			ItemStack remainder = isRevolverCasing(unloaded)?crate.insertSpentCasing(unloaded): crate.insertAmmunition(unloaded);
			giveBack(player, remainder);
		}

		for(int chamber = 0; chamber < chambers; chamber++)
		{
			ItemStack required = crate.getInventory().get(ContainerAmmunitionCrate.REVOLVER_PATTERN_START+chamber);
			if(required.isEmpty())
				continue;

			ItemStack round = crate.extractAmmunition(stack -> stacksMatch(stack, required));
			if(round.isEmpty())
				continue;

			ItemStack remainder = bulletHandler.insertItem(chamber, round, false);
			if(!remainder.isEmpty())
				giveBack(player, crate.insertAmmunition(remainder));
		}

		crate.markDirty();
		return true;
	}

	static void giveBack(EntityPlayer player, ItemStack stack)
	{
		if(stack.isEmpty())
			return;
		if(!player.inventory.addItemStackToInventory(stack))
			player.dropItem(stack, false);
	}

	@Nullable
	static TileEntityAmmunitionCrate getMountedCrate(Entity gun, AmmunitionCrateMode mode, boolean loading)
	{
		if(!(gun instanceof EntityMachinegun machinegun))
			return null;

		if(mode==AmmunitionCrateMode.MACHINEGUN_MAGAZINES)
		{
			MountedReloadSource source = MOUNTED_RELOAD_SOURCES.get(machinegun);
			TileEntityAmmunitionCrate inserterCrate = source==null?null: source.crate;
			if(inserterCrate!=null)
			{
				if(!inserterCrate.isInvalid()
						&&inserterCrate.mode==mode
						&&inserterCrate.isUpgradeInstalled(IIContent.UPGRADE_INSERTER)
						&&inserterCrate.isInserterTargetInRange(machinegun)
						&&(!loading||source.loadingAuthorized))
					return inserterCrate;
				MOUNTED_RELOAD_SOURCES.remove(machinegun);
			}
		}

		EnumFacing rear = EnumFacing.fromAngle(machinegun.aim.getCenterYaw()).getOpposite();
		TileEntity tile = gun.world.getTileEntity(gun.getPosition().offset(rear).down());
		if(!(tile instanceof TileEntityAmmunitionCrate crate)||crate.mode!=mode||!crate.lid.isFullyOpened())
			return null;

		//The inserter occupies the direct belt feed path.
		if(mode==AmmunitionCrateMode.MACHINEGUN_BELT_FED&&crate.isUpgradeInstalled(IIContent.UPGRADE_INSERTER))
			return null;
		return crate;
	}

	static void finishMountedLoadSource(Entity gun)
	{
		if(gun instanceof EntityMachinegun machinegun)
		{
			MountedReloadSource source = MOUNTED_RELOAD_SOURCES.get(machinegun);
			if(source!=null)
				source.loadingAuthorized = false;
		}
	}

	static void clearMountedReloadSource(EntityMachinegun machinegun, TileEntityAmmunitionCrate crate)
	{
		MountedReloadSource source = MOUNTED_RELOAD_SOURCES.get(machinegun);
		if(source!=null&&source.crate==crate)
			MOUNTED_RELOAD_SOURCES.remove(machinegun);
	}

	static Predicate<ItemStack> casingPredicate(IAmmoTypeItem<?, ?> ammo)
	{
		ItemStack casing = ammo.getCasingStack(1);
		return stack -> stacksMatch(stack, casing);
	}

	static boolean isLooseRound(ItemStack stack, IAmmoTypeItem<?, ?> ammo)
	{
		return stack.getItem()==ammo&&!ammo.isBulletCore(stack);
	}

	static boolean isMachinegunRound(ItemStack stack)
	{
		return isLooseRound(stack, IIContent.itemAmmoMachinegun);
	}

	static boolean isLiveMagazine(ItemStack stack, Magazines... magazines)
	{
		return isMagazine(stack, magazines)&&!IIContent.itemBulletMagazine.hasNoBullets(stack);
	}

	static boolean isSpentMagazine(ItemStack stack, Magazines... magazines)
	{
		return isMagazine(stack, magazines)&&IIContent.itemBulletMagazine.hasNoBullets(stack);
	}

	static boolean isMagazine(ItemStack stack, Magazines... magazines)
	{
		if(stack.getItem()!=IIContent.itemBulletMagazine)
			return false;
		Magazines type = IIContent.itemBulletMagazine.stackToSub(stack);
		for(Magazines magazine : magazines)
			if(type==magazine)
				return true;
		return false;
	}

	static boolean isRevolverRound(ItemStack stack)
	{
		return stack.getItem() instanceof ItemBullet
				&&!stack.isItemEqual(BulletHandler.emptyCasing)
				&&!stack.isItemEqual(BulletHandler.emptyShell);
	}

	static boolean isRevolverCasing(ItemStack stack)
	{
		return stack.isItemEqual(BulletHandler.emptyCasing)||stack.isItemEqual(BulletHandler.emptyShell);
	}

	static boolean stacksMatch(ItemStack first, ItemStack second)
	{
		return !first.isEmpty()&&!second.isEmpty()
				&&first.isItemEqual(second)
				&&ItemStack.areItemStackTagsEqual(first, second);
	}

	static float pingPong(float progress)
	{
		return 1f-Math.abs(MathHelper.clamp(progress, 0f, 1f)*2f-1f);
	}

}
