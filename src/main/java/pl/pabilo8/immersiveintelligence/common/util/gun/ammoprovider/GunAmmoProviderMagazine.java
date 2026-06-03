package pl.pabilo8.immersiveintelligence.common.util.gun.ammoprovider;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunAmmoProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.function.Supplier;

/**
 * Magazine-fed ammunition provider.
 * Stores bullets internally as a list for fast access, and keeps a magazine ItemStack
 * for simple persistence and external representation.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 26.05.2026
 */
public class GunAmmoProviderMagazine extends GunAmmoProvider
{
	private final Magazines magazineType;
	private final NonNullList<ItemStack> bullets;
	private ItemStack magazineStack = ItemStack.EMPTY;

	public GunAmmoProviderMagazine(Entity gun, Supplier<Entity> operatorSupplier, Magazines type, float loadTime)
	{
		super(gun, operatorSupplier);
		this.magazineType = type;
		this.bullets = NonNullList.withSize(type.capacity, ItemStack.EMPTY);
		this.maxReload = loadTime;
	}

	//--- Bullet list and Magazine stack sync ---//

	/**
	 * Rebuilds the magazine ItemStack from the current bullet list.
	 * Should be called after any modification to the bullet list.
	 */
	private void rebuildMagazineFromBullets()
	{
		if(getBulletCount()==0)
		{
			magazineStack = ItemStack.EMPTY;
			return;
		}

		//Build a list of only non‑empty bullets (up to capacity)
		NonNullList<ItemStack> bulletList = NonNullList.create();
		for(ItemStack b : bullets)
			if(!b.isEmpty())
				bulletList.add(b);

		magazineStack = IIContent.itemBulletMagazine.getMagazine(magazineType);
		IIContent.itemBulletMagazine.writeInventory(magazineStack, bulletList);
	}

	/**
	 * Rebuilds the bullet list from the given magazine stack.
	 * Clears the current bullets and refills from the magazine.
	 *
	 * @param stack the magazine ItemStack to load from
	 */
	private void rebuildBulletsFromMagazine(@Nonnull ItemStack stack)
	{
		clear();
		if(stack.isEmpty()||stack.getItem()!=IIContent.itemBulletMagazine)
			return;

		Magazines type = IIContent.itemBulletMagazine.stackToSub(stack);
		if(type!=magazineType)
			return;

		NonNullList<ItemStack> magazineBullets = IIContent.itemBulletMagazine.readInventory(stack);
		int copyLimit = Math.min(magazineType.capacity, magazineBullets.size());
		for(int i = 0; i < copyLimit; i++)
			bullets.set(i, magazineBullets.get(i).copy());
	}

	//--- Implementation ---//

	@Override
	protected void onLoadFinished()
	{
		//Bullets have already been added during canFindAmmo().
		//Create the magazine stack from the bullets.
		rebuildMagazineFromBullets();
	}

	@Override
	protected void onUnloadFinished()
	{
		if(getBulletCount() > 0)
		{
			//Drop or give the magazine stack (which reflects the current bullets)
			giveOrDrop(magazineStack);
			clear();
			magazineStack = ItemStack.EMPTY;
		}
	}

	@Nonnull
	@Override
	protected ItemStack provideNextBullet()
	{
		if(getBulletCount()==0)
			return ItemStack.EMPTY;

		//Find the first non‑empty bullet
		for(int i = 0; i < bullets.size(); i++)
		{
			ItemStack bullet = bullets.get(i);
			if(!bullet.isEmpty())
			{
				bullets.set(i, ItemStack.EMPTY);
				rebuildMagazineFromBullets(); //Keep magazine stack in sync
				return bullet.copy();
			}
		}
		return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getAmmoList()
	{
		return bullets;
	}

	@Override
	protected boolean canFindAmmo()
	{
		//Check if full
		if(getBulletCount() >= magazineType.capacity)
			return false;

		//Try to find a matching magazine in the operator's hotbar
		ItemStack foundMagazine = findMagazineInHotbar(magazineType);
		if(foundMagazine!=null&&!foundMagazine.isEmpty())
		{
			int needed = magazineType.capacity-getBulletCount();
			NonNullList<ItemStack> magazineBullets = IIContent.itemBulletMagazine.readInventory(foundMagazine);
			int taken = 0;

			//Transfer bullets from the found magazine into our internal list
			for(int i = 0; i < magazineBullets.size()&&taken < needed; i++)
			{
				ItemStack bullet = magazineBullets.get(i);
				if(!bullet.isEmpty())
				{
					for(int j = 0; j < bullets.size(); j++)
					{
						if(bullets.get(j).isEmpty())
						{
							bullets.set(j, bullet.copy());
							taken++;
							break;
						}
					}
				}
			}

			if(taken > 0)
			{
				//Update the found magazine (remove transferred bullets)
				IIContent.itemBulletMagazine.writeInventory(foundMagazine, magazineBullets);

				//If the magazine becomes empty, remove it from the operator's inventory
				if(IIContent.itemBulletMagazine.hasNoBullets(foundMagazine))
					consumeMagazineFromHotbar(foundMagazine);

				return true;
			}
		}
		return false;
	}

	//--- Utils ---//

	private void clear()
	{
		Collections.fill(bullets, ItemStack.EMPTY);
	}

	private int getBulletCount()
	{
		int count = 0;
		for(ItemStack b : bullets)
			if(!b.isEmpty())
				count++;
		return count;
	}

	//--- Persistence (for saving/loading to originStack) ---//

	@Nonnull
	public ItemStack getLoadedStack()
	{
		return magazineStack;
	}

	public void setLoadedStack(@Nonnull ItemStack stack)
	{
		clear();
		magazineStack = stack.isEmpty()?ItemStack.EMPTY: stack.copy();
		rebuildBulletsFromMagazine(magazineStack);
	}

	//--- NBT ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = super.serializeNBT();
		if(!magazineStack.isEmpty())
			nbt.setTag("magazine", magazineStack.serializeNBT());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		super.deserializeNBT(nbt);
		if(nbt.hasKey("magazine"))
			setLoadedStack(new ItemStack(nbt.getCompoundTag("magazine")));
		else
			setLoadedStack(ItemStack.EMPTY);
	}

	//--- Hotbar interaction helpers (copied from your existing code) ---//

	@Nullable
	protected ItemStack findMagazineInHotbar(Magazines expectedType)
	{
		Entity player = operatorSupplier.get();
		if(player==null)
			return null;

		IItemHandler inv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(inv==null)
			return null;

		for(int i = 0; i < 9; i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.getItem()==IIContent.itemBulletMagazine&&
					IIContent.itemBulletMagazine.stackToSub(stack)==expectedType)
				return stack;
		}
		return null;
	}

	protected boolean consumeMagazineFromHotbar(ItemStack magazine)
	{
		Entity player = operatorSupplier.get();
		if(player==null)
			return false;

		IItemHandler inv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(inv==null)
			return false;

		for(int i = 0; i < 9; i++)
			if(inv.getStackInSlot(i)==magazine)
			{
				ItemStack extracted = inv.extractItem(i, 1, false);
				return !extracted.isEmpty();
			}
		return false;
	}
}
