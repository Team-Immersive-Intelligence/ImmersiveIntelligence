package pl.pabilo8.immersiveintelligence.common.util.gun.ammoprovider;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunAmmoProvider;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Provides staged direct ammunition from an item handler.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 21.08.2026
 * @since 03.06.2026
 */
public class GunAmmoProviderItemHandler extends GunAmmoProvider
{
	protected final IItemHandlerModifiable inventory;
	protected final Predicate<ItemStack> acceptedAmmo;
	private int[] loadStages = {Integer.MAX_VALUE};
	private int loadStage;
	private boolean finalBatch;
	private NonNullList<ItemStack> loadedAmmo = NonNullList.create();
	private NonNullList<ItemStack> loadingAmmo = NonNullList.create();

	public GunAmmoProviderItemHandler(Entity gun, Supplier<Entity> operatorSupplier, IItemHandlerModifiable inventory,
	                                  Predicate<ItemStack> acceptedAmmo, float loadTime)
	{
		super(gun, operatorSupplier);
		this.inventory = inventory;
		this.acceptedAmmo = acceptedAmmo==null?stack -> true: acceptedAmmo;
		this.maxReload = loadTime;
	}

	/**
	 * Sets the number of rounds loaded by each reload animation stage.
	 */
	public GunAmmoProviderItemHandler withLoadStages(int... loadStages)
	{
		if(loadStages==null||loadStages.length==0)
			this.loadStages = new int[]{Integer.MAX_VALUE};
		else
			this.loadStages = Arrays.stream(loadStages).map(value -> Math.max(1, value)).toArray();
		this.loadStage = Math.min(this.loadStage, this.loadStages.length-1);
		return this;
	}

	/**
	 * Prepares stage metadata before {@link #startReloading()} is called.
	 */
	public void prepareReload()
	{
		if(!isEmpty())
			return;
		this.loadStage = 0;
		this.loadedAmmo.clear();
		int availableAmmo = getAvailableAmmoCount();
		this.loadingAmmo = buildAmmoSnapshot(getTotalStageCapacity());
		this.finalBatch = availableAmmo <= getTotalStageCapacity();
	}

	@Override
	public void update()
	{
		if(loadingState==GunLoadingState.EMPTY||loadingState==GunLoadingState.LOADED)
			return;

		if(++reload < maxReload)
			return;

		reload = 0;
		switch(loadingState)
		{
			case LOAD:
				onLoadFinished();
				if(hasNextLoadStage())
					loadStage++;
				else
				{
					loadingState = loadedAmmo.isEmpty()?GunLoadingState.EMPTY: GunLoadingState.LOADED;
					loadingAmmo.clear();
				}
				break;
			case UNLOAD:
				onUnloadFinished();
				loadingState = GunLoadingState.EMPTY;
				break;
			default:
				break;
		}
	}

	@Override
	protected void onLoadFinished()
	{
		this.loadedAmmo = copyAmmoList(loadingAmmo, getCurrentStageCapacity());
	}

	@Override
	protected void onUnloadFinished()
	{
		this.loadedAmmo.clear();
		this.loadingAmmo.clear();
		this.loadStage = 0;
		this.finalBatch = false;
	}

	@Override
	protected boolean canFindAmmo()
	{
		return hasAvailableAmmo();
	}

	public boolean hasAvailableAmmo()
	{
		return getAvailableAmmoCount() > 0;
	}

	/**
	 * @return the number of compatible physical rounds in Platform storage.
	 */
	public int getAvailableAmmoCount()
	{
		int count = 0;
		for(int i = 0; i < inventory.getSlots(); i++)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if(isDirectAmmo(stack))
				count += stack.getCount();
		}
		return count;
	}

	@Nonnull
	@Override
	protected ItemStack provideNextBullet()
	{
		if(loadedAmmo.isEmpty())
		{
			resetLoadedState();
			return ItemStack.EMPTY;
		}

		ItemStack expected = peekLoadedAmmo();
		ItemStack provided = extractPhysicalRound(expected);
		if(provided.isEmpty())
		{
			resetLoadedState();
			return ItemStack.EMPTY;
		}

		removeLoadedRound(provided);
		if(getLoadedRoundCount() <= 0)
			resetLoadedState();
		return provided;
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getAmmoList()
	{
		return copyAmmoList(loadedAmmo.isEmpty()?loadingAmmo: loadedAmmo);
	}

	/**
	 * @return ammunition that is loaded or visible during the active load stage.
	 */
	@Nonnull
	public NonNullList<ItemStack> getRenderAmmoList()
	{
		if(loadingState==GunLoadingState.LOAD)
			return copyAmmoList(loadingAmmo, getCurrentStageCapacity());
		return copyAmmoList(loadedAmmo, loadedAmmo.size());
	}

	/**
	 * @return one loaded round for firing metadata, or an empty stack.
	 */
	@Nonnull
	public ItemStack peekLoadedAmmo()
	{
		for(ItemStack stack : loadedAmmo)
			if(!stack.isEmpty())
			{
				ItemStack result = stack.copy();
				result.setCount(1);
				return result;
			}
		return ItemStack.EMPTY;
	}

	public int getLoadedRoundCount()
	{
		int count = 0;
		for(ItemStack stack : loadedAmmo)
			count += stack.getCount();
		return count;
	}

	public int getLoadStage()
	{
		return loadStage;
	}

	public boolean isFinalBatch()
	{
		return finalBatch;
	}

	/**
	 * @return true while this provider unloads ammunition
	 */
	public boolean isUnloading()
	{
		return loadingState==GunLoadingState.UNLOAD;
	}

	/**
	 * @return true when unloading cannot move its spent item to the output
	 */
	public boolean isUnloadBlocked()
	{
		return false;
	}

	private boolean hasNextLoadStage()
	{
		return loadStage+1 < loadStages.length&&getAmmoCount(loadingAmmo) > getCurrentStageCapacity();
	}

	private int getCurrentStageCapacity()
	{
		long total = 0;
		for(int i = 0; i <= loadStage&&i < loadStages.length; i++)
		{
			total += loadStages[i];
			if(total >= Integer.MAX_VALUE)
				return Integer.MAX_VALUE;
		}
		return (int)total;
	}

	private int getTotalStageCapacity()
	{
		long total = 0;
		for(int stage : loadStages)
		{
			total += stage;
			if(total >= Integer.MAX_VALUE)
				return Integer.MAX_VALUE;
		}
		return (int)total;
	}

	private NonNullList<ItemStack> buildAmmoSnapshot(int limit)
	{
		NonNullList<ItemStack> result = NonNullList.create();
		int remaining = limit;
		for(int i = 0; i < inventory.getSlots()&&remaining > 0; i++)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if(isDirectAmmo(stack))
				remaining -= addToSnapshot(result, stack, remaining);
		}
		return result;
	}

	private int addToSnapshot(NonNullList<ItemStack> result, ItemStack source, int remaining)
	{
		int amount = Math.min(source.getCount(), remaining);
		if(amount <= 0)
			return 0;

		ItemStack added = source.copy();
		added.setCount(amount);
		//Keep physical stack boundaries. Large logical stacks overflow ItemStack's NBT count field.
		result.add(added);
		return amount;
	}

	@Nonnull
	private ItemStack extractPhysicalRound(ItemStack expected)
	{
		if(expected.isEmpty())
			return ItemStack.EMPTY;

		for(int slot = 0; slot < inventory.getSlots(); slot++)
		{
			ItemStack stack = inventory.getStackInSlot(slot);
			if(isDirectAmmo(stack)&&ItemHandlerHelper.canItemStacksStack(stack, expected))
				return inventory.extractItem(slot, 1, false);
		}
		return ItemStack.EMPTY;
	}

	private boolean isDirectAmmo(ItemStack stack)
	{
		return !stack.isEmpty()&&AmmoRegistry.getAmmoItem(stack)!=null&&acceptedAmmo.test(stack);
	}

	private void removeLoadedRound(ItemStack fired)
	{
		for(int i = 0; i < loadedAmmo.size(); i++)
		{
			ItemStack stack = loadedAmmo.get(i);
			if(!stack.isEmpty()&&ItemHandlerHelper.canItemStacksStack(stack, fired))
			{
				stack.shrink(1);
				if(stack.getCount() <= 0)
					loadedAmmo.remove(i);
				return;
			}
		}
	}

	private void resetLoadedState()
	{
		this.loadedAmmo.clear();
		this.loadingAmmo.clear();
		this.loadingState = GunLoadingState.EMPTY;
		this.reload = 0;
		this.loadStage = 0;
		this.finalBatch = false;
	}


	private NonNullList<ItemStack> copyAmmoList(NonNullList<ItemStack> source)
	{
		return copyAmmoList(source, Integer.MAX_VALUE);
	}

	private NonNullList<ItemStack> copyAmmoList(NonNullList<ItemStack> source, int limit)
	{
		NonNullList<ItemStack> copy = NonNullList.create();
		int remaining = limit;
		for(ItemStack stack : source)
		{
			if(remaining <= 0)
				break;
			if(stack.isEmpty())
				continue;
			ItemStack added = stack.copy();
			added.setCount(Math.min(stack.getCount(), remaining));
			copy.add(added);
			remaining -= added.getCount();
		}
		return copy;
	}

	private int getAmmoCount(NonNullList<ItemStack> ammo)
	{
		int count = 0;
		for(ItemStack stack : ammo)
			count += stack.getCount();
		return count;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = super.serializeNBT();
		nbt.setInteger("load_stage", loadStage);
		nbt.setBoolean("final_batch", finalBatch);
		NBTTagList list = new NBTTagList();
		for(ItemStack stack : loadedAmmo)
			if(!stack.isEmpty())
				list.appendTag(stack.serializeNBT());
		nbt.setTag("loaded_ammo", list);
		NBTTagList loadingList = new NBTTagList();
		for(ItemStack stack : loadingAmmo)
			if(!stack.isEmpty())
				loadingList.appendTag(stack.serializeNBT());
		nbt.setTag("loading_ammo", loadingList);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		super.deserializeNBT(nbt);
		this.loadStage = Math.max(0, Math.min(nbt.getInteger("load_stage"), loadStages.length-1));
		this.finalBatch = nbt.hasKey("final_batch")?nbt.getBoolean("final_batch"):
				getAvailableAmmoCount() <= getTotalStageCapacity();
		this.loadedAmmo.clear();
		NBTTagList list = nbt.getTagList("loaded_ammo", 10);
		for(int i = 0; i < list.tagCount(); i++)
		{
			ItemStack stack = new ItemStack(list.getCompoundTagAt(i));
			if(!stack.isEmpty())
				this.loadedAmmo.add(stack);
		}

		this.loadingAmmo.clear();
		NBTTagList loadingList = nbt.getTagList("loading_ammo", 10);
		for(int i = 0; i < loadingList.tagCount(); i++)
		{
			ItemStack stack = new ItemStack(loadingList.getCompoundTagAt(i));
			if(!stack.isEmpty())
				this.loadingAmmo.add(stack);
		}

		//Old saves had no logical cache. Rebuild it from the physical Platform inventory.
		if(isLoaded()&&loadedAmmo.isEmpty())
			this.loadedAmmo = buildAmmoSnapshot(getCurrentStageCapacity());
		if(isReloading()&&loadingAmmo.isEmpty())
			this.loadingAmmo = buildAmmoSnapshot(getTotalStageCapacity());
	}
}
