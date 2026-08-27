package pl.pabilo8.immersiveintelligence.common.util.gun.ammoprovider;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandlerModifiable;
import pl.pabilo8.immersiveintelligence.common.IIContent;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Provides staged ammunition from magazines stored in an item handler.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.08.2026
 */
public class GunAmmoProviderMagazineItemHandler extends GunAmmoProviderItemHandler
{
	private final Predicate<ItemStack> acceptedMagazine;
	private final Function<ItemStack, ItemStack> spentMagazineOutput;
	private final BooleanSupplier clientSide;
	private int[] loadStages = {Integer.MAX_VALUE};
	private int[] mountedBySlot;
	private int loadStage;
	private int activeStages = 1;
	private boolean finalBatch;
	private boolean unloadBlocked;
	private int currentMagazineSlot = -1;

	public GunAmmoProviderMagazineItemHandler(Entity gun, Supplier<Entity> operatorSupplier, IItemHandlerModifiable inventory,
	                                          Predicate<ItemStack> acceptedMagazine, Predicate<ItemStack> acceptedAmmo,
	                                          Function<ItemStack, ItemStack> spentMagazineOutput, BooleanSupplier clientSide,
	                                          float loadTime)
	{
		super(gun, operatorSupplier, inventory, acceptedAmmo, loadTime);
		this.acceptedMagazine = acceptedMagazine==null?stack -> false: acceptedMagazine;
		this.spentMagazineOutput = spentMagazineOutput;
		this.clientSide = clientSide==null?() -> false: clientSide;
		this.mountedBySlot = new int[inventory.getSlots()];
	}

	/**
	 * Sets the number of magazines loaded by each reload animation stage.
	 */
	@Override
	public GunAmmoProviderMagazineItemHandler withLoadStages(int... loadStages)
	{
		if(loadStages==null||loadStages.length==0)
			this.loadStages = new int[]{Integer.MAX_VALUE};
		else
			this.loadStages = Arrays.stream(loadStages).map(value -> Math.max(1, value)).toArray();
		this.loadStage = Math.min(this.loadStage, this.loadStages.length-1);
		this.activeStages = Math.min(Math.max(1, this.activeStages), this.loadStages.length);
		return this;
	}

	/**
	 * Prepares unloading of spent magazines or loading of a new magazine set.
	 */
	@Override
	public void prepareReload()
	{
		if(!isEmpty())
			return;

		normalizeMountedSlots();
		this.currentMagazineSlot = -1;
		this.unloadBlocked = false;
		if(getMountedMagazineCount() > 0)
		{
			prepareUnloadSequence();
			//startReloading() changes a LOADED provider to the UNLOAD state.
			this.loadingState = GunLoadingState.LOADED;
		}
		else
			prepareLoadSequence();
	}

	@Override
	public void update()
	{
		normalizeMountedSlots();
		if(loadingState==GunLoadingState.LOADED&&!hasUsableMountedAmmo())
		{
			loadingState = GunLoadingState.EMPTY;
			currentMagazineSlot = -1;
			return;
		}
		if(loadingState!=GunLoadingState.LOAD&&loadingState!=GunLoadingState.UNLOAD)
			return;

		float stageDuration = getStageDuration();
		if(++reload < stageDuration)
			return;
		reload = 0;

		if(loadingState==GunLoadingState.LOAD)
			updateLoadStage();
		else
			updateUnloadStage();
	}

	private void updateLoadStage()
	{
		mountMagazines(getCurrentStageCapacity());
		if(loadStage+1 < activeStages&&getLoadableMagazineCount() > 0)
		{
			loadStage++;
			return;
		}

		loadingState = hasUsableMountedAmmo()?GunLoadingState.LOADED: GunLoadingState.EMPTY;
	}

	private void updateUnloadStage()
	{
		if(!unmountMagazines(getCurrentStageCapacity()))
		{
			reload = getStageDuration();
			return;
		}
		if(loadStage+1 < activeStages&&getMountedMagazineCount() > 0)
		{
			loadStage++;
			return;
		}

		finishUnloadSequence();
	}

	private void prepareLoadSequence()
	{
		int available = getLoadableMagazineCount();
		this.loadStage = 0;
		this.activeStages = getStageCountForUnits(Math.min(available, getTotalStageCapacity()));
		this.finalBatch = available <= getTotalStageCapacity();
		this.reload = 0;
	}

	private void prepareUnloadSequence()
	{
		this.loadStage = 0;
		this.activeStages = getStageCountForUnits(getMountedMagazineCount());
		this.reload = 0;
	}

	private void finishUnloadSequence()
	{
		this.currentMagazineSlot = -1;
		this.unloadBlocked = false;
		if(getLoadableMagazineCount() > 0)
		{
			prepareLoadSequence();
			this.loadingState = GunLoadingState.LOAD;
		}
		else
		{
			this.loadingState = GunLoadingState.EMPTY;
			this.loadStage = 0;
			this.activeStages = 1;
			this.finalBatch = false;
			this.reload = 0;
		}
	}

	@Override
	protected boolean canFindAmmo()
	{
		return getLoadableMagazineCount() > 0;
	}

	@Override
	public boolean hasAvailableAmmo()
	{
		normalizeMountedSlots();
		return getMountedMagazineCount() > 0||getLoadableMagazineCount() > 0;
	}

	/**
	 * @return the number of compatible reserve magazines in Platform storage
	 */
	@Override
	public int getAvailableAmmoCount()
	{
		normalizeMountedSlots();
		return getLoadableMagazineCount();
	}

	@Nonnull
	@Override
	protected ItemStack provideNextBullet()
	{
		normalizeMountedSlots();
		int slot = findLoadedMagazineSlot(true);
		if(slot < 0)
		{
			loadingState = GunLoadingState.EMPTY;
			return ItemStack.EMPTY;
		}

		ItemStack magazine = inventory.getStackInSlot(slot).copy();
		NonNullList<ItemStack> bullets = IIContent.itemBulletMagazine.readInventory(magazine);
		for(int bulletSlot = 0; bulletSlot < bullets.size(); bulletSlot++)
		{
			ItemStack bullet = bullets.get(bulletSlot);
			if(bullet.isEmpty()||!acceptedAmmo.test(bullet))
				continue;

			ItemStack provided = bullet.copy();
			provided.setCount(1);
			bullet.shrink(1);
			if(bullet.getCount() <= 0)
				bullets.set(bulletSlot, ItemStack.EMPTY);
			IIContent.itemBulletMagazine.writeInventory(magazine, bullets);
			inventory.setStackInSlot(slot, magazine);

			if(!isMagazineWithUsableAmmo(magazine))
				currentMagazineSlot = -1;
			if(!hasUsableMountedAmmo())
				loadingState = GunLoadingState.EMPTY;
			return provided;
		}

		currentMagazineSlot = -1;
		if(!hasUsableMountedAmmo())
			loadingState = GunLoadingState.EMPTY;
		return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getAmmoList()
	{
		normalizeMountedSlots();
		NonNullList<ItemStack> result = NonNullList.create();
		for(int slot = 0; slot < inventory.getSlots(); slot++)
			if(mountedBySlot[slot] > 0)
				result.add(inventory.getStackInSlot(slot).copy());
		return result;
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getRenderAmmoList()
	{
		return getAmmoList();
	}

	@Nonnull
	@Override
	public ItemStack peekLoadedAmmo()
	{
		normalizeMountedSlots();
		int slot = findLoadedMagazineSlot(true);
		if(slot < 0)
			return ItemStack.EMPTY;

		for(ItemStack bullet : IIContent.itemBulletMagazine.readInventory(inventory.getStackInSlot(slot)))
			if(!bullet.isEmpty()&&acceptedAmmo.test(bullet))
			{
				ItemStack result = bullet.copy();
				result.setCount(1);
				return result;
			}
		return ItemStack.EMPTY;
	}

	@Override
	public int getLoadedRoundCount()
	{
		normalizeMountedSlots();
		int count = 0;
		for(int slot = 0; slot < inventory.getSlots(); slot++)
		{
			if(mountedBySlot[slot] <= 0)
				continue;
			for(ItemStack bullet : IIContent.itemBulletMagazine.readInventory(inventory.getStackInSlot(slot)))
				if(!bullet.isEmpty()&&acceptedAmmo.test(bullet))
					count += bullet.getCount();
		}
		return count;
	}

	@Override
	public int getLoadStage()
	{
		return loadStage;
	}

	@Override
	public boolean isFinalBatch()
	{
		return finalBatch;
	}

	@Override
	public boolean isUnloadBlocked()
	{
		return unloadBlocked;
	}

	@Override
	public float getLoadingProgress(float partialTicks)
	{
		if(loadingState==GunLoadingState.LOADED)
			return 1f;
		if(loadingState==GunLoadingState.EMPTY)
			return 0f;

		float stageProgress = Math.max(0f, Math.min(1f, (reload+partialTicks)/getStageDuration()));
		float sequenceProgress = (loadStage+stageProgress)/Math.max(1f, activeStages);
		return loadingState==GunLoadingState.UNLOAD?1f-sequenceProgress: sequenceProgress;
	}

	private int mountMagazines(int requested)
	{
		int remaining = requested;
		int mounted = 0;
		for(int slot = 0; slot < inventory.getSlots()&&remaining > 0; slot++)
		{
			if(mountedBySlot[slot] > 0||!isMagazineWithUsableAmmo(inventory.getStackInSlot(slot)))
				continue;
			mountedBySlot[slot] = 1;
			mounted++;
			remaining--;
		}
		return mounted;
	}

	private boolean unmountMagazines(int requested)
	{
		int remaining = requested;
		this.unloadBlocked = false;
		for(int slot = 0; slot < inventory.getSlots()&&remaining > 0; slot++)
		{
			if(mountedBySlot[slot] <= 0)
				continue;
			if(clientSide.getAsBoolean())
			{
				mountedBySlot[slot] = 0;
				remaining--;
				continue;
			}

			ItemStack magazine = inventory.getStackInSlot(slot).copy();
			if(magazine.isEmpty())
			{
				mountedBySlot[slot] = 0;
				continue;
			}
			if(spentMagazineOutput==null)
			{
				this.unloadBlocked = true;
				return false;
			}

			ItemStack remainder = spentMagazineOutput.apply(magazine);
			if(!remainder.isEmpty())
			{
				this.unloadBlocked = true;
				return false;
			}

			inventory.setStackInSlot(slot, ItemStack.EMPTY);
			mountedBySlot[slot] = 0;
			remaining--;
		}
		return true;
	}

	private int getLoadableMagazineCount()
	{
		int count = 0;
		for(int slot = 0; slot < inventory.getSlots(); slot++)
			if(mountedBySlot[slot] <= 0&&isMagazineWithUsableAmmo(inventory.getStackInSlot(slot)))
				count++;
		return count;
	}

	private int getMountedMagazineCount()
	{
		int count = 0;
		for(int mounted : mountedBySlot)
			if(mounted > 0)
				count++;
		return count;
	}

	private boolean hasUsableMountedAmmo()
	{
		return findLoadedMagazineSlot(false) >= 0;
	}

	private int findLoadedMagazineSlot(boolean keepCurrent)
	{
		if(keepCurrent&&isLoadedMagazineUsable(currentMagazineSlot))
			return currentMagazineSlot;
		for(int slot = 0; slot < inventory.getSlots(); slot++)
			if(isLoadedMagazineUsable(slot))
			{
				if(keepCurrent)
					currentMagazineSlot = slot;
				return slot;
			}
		return -1;
	}

	private boolean isLoadedMagazineUsable(int slot)
	{
		return slot >= 0&&slot < inventory.getSlots()&&mountedBySlot[slot] > 0
				&&isMagazineWithUsableAmmo(inventory.getStackInSlot(slot));
	}

	private boolean isMagazineWithUsableAmmo(ItemStack stack)
	{
		if(stack.isEmpty()||stack.getItem()!=IIContent.itemBulletMagazine||!acceptedMagazine.test(stack))
			return false;
		for(ItemStack bullet : IIContent.itemBulletMagazine.readInventory(stack))
			if(!bullet.isEmpty()&&acceptedAmmo.test(bullet))
				return true;
		return false;
	}

	private void normalizeMountedSlots()
	{
		if(mountedBySlot.length!=inventory.getSlots())
			mountedBySlot = Arrays.copyOf(mountedBySlot, inventory.getSlots());
		for(int slot = 0; slot < mountedBySlot.length; slot++)
		{
			ItemStack stack = inventory.getStackInSlot(slot);
			if(stack.isEmpty()||stack.getItem()!=IIContent.itemBulletMagazine||!acceptedMagazine.test(stack))
				mountedBySlot[slot] = 0;
			else
				mountedBySlot[slot] = mountedBySlot[slot] > 0?1: 0;
		}
	}

	private int getStageCountForUnits(int units)
	{
		if(units <= 0)
			return 1;
		long capacity = 0;
		for(int i = 0; i < loadStages.length; i++)
		{
			capacity += loadStages[i];
			if(loadStages[i]==Integer.MAX_VALUE||capacity >= units)
				return i+1;
		}
		return loadStages.length;
	}

	private int getCurrentStageCapacity()
	{
		return loadStages[Math.min(loadStage, loadStages.length-1)];
	}

	private int getTotalStageCapacity()
	{
		long capacity = 0;
		for(int stage : loadStages)
		{
			capacity += stage;
			if(stage==Integer.MAX_VALUE||capacity >= Integer.MAX_VALUE)
				return Integer.MAX_VALUE;
		}
		return (int)capacity;
	}

	private float getStageDuration()
	{
		return Math.max(1f, maxReload/Math.max(1, activeStages));
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = super.serializeNBT();
		nbt.setInteger("magazine_load_stage", loadStage);
		nbt.setInteger("magazine_active_stages", activeStages);
		nbt.setBoolean("magazine_final_batch", finalBatch);
		nbt.setIntArray("mounted_magazines", mountedBySlot);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		super.deserializeNBT(nbt);
		this.loadStage = Math.max(0, Math.min(nbt.getInteger("magazine_load_stage"), loadStages.length-1));
		this.activeStages = Math.max(1, Math.min(
				nbt.hasKey("magazine_active_stages")?nbt.getInteger("magazine_active_stages"): 1,
				loadStages.length));
		this.finalBatch = nbt.getBoolean("magazine_final_batch");
		int[] mounted = nbt.getIntArray("mounted_magazines");
		this.mountedBySlot = mounted.length==0?new int[inventory.getSlots()]: mounted;
		normalizeMountedSlots();
		this.currentMagazineSlot = -1;
		this.unloadBlocked = false;

		//Old saves used the direct-ammunition provider. Recover mounted magazines from Platform storage.
		if(!nbt.hasKey("mounted_magazines")&&(isLoaded()||isReloading()))
		{
			int remaining = getTotalStageCapacity();
			for(int slot = 0; slot < inventory.getSlots()&&remaining > 0; slot++)
				if(isMagazineWithUsableAmmo(inventory.getStackInSlot(slot)))
				{
					mountedBySlot[slot] = 1;
					remaining--;
				}
		}
	}
}
