package pl.pabilo8.immersiveintelligence.common.util.gun.ammoprovider;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.ammocrate.AmmunitionCrateHandler;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunAmmoProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.function.Supplier;

/**
 * Supplies a mounted weapon from magazines in the operator inventory or an Ammunition Crate.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 10.08.2026
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
	 * Rebuilds the magazine stack after the bullet list changes.
	 */
	private void rebuildMagazineFromBullets()
	{
		if(getBulletCount()==0)
		{
			//Keep the physical magazine after its last round is fired.
			if(!magazineStack.isEmpty())
				magazineStack = IIContent.itemBulletMagazine.getMagazine(magazineType);
			return;
		}

		NonNullList<ItemStack> bulletList = NonNullList.create();
		for(ItemStack bullet : bullets)
			if(!bullet.isEmpty())
				bulletList.add(bullet);


		magazineStack = IIContent.itemBulletMagazine.getMagazine(magazineType);
		IIContent.itemBulletMagazine.writeInventory(magazineStack, bulletList);
	}

	/**
	 * Rebuilds the internal bullet list from a magazine stack.
	 *
	 * @param stack magazine to read
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
		rebuildMagazineFromBullets();
	}

	@Override
	protected void onUnloadFinished()
	{
		if(!magazineStack.isEmpty())
		{
			if(!AmmunitionCrateHandler.returnMountedMagazine(gun, magazineType, magazineStack))
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

		for(int i = 0; i < bullets.size(); i++)
		{
			ItemStack bullet = bullets.get(i);
			if(!bullet.isEmpty())
			{
				bullets.set(i, ItemStack.EMPTY);
				rebuildMagazineFromBullets();
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
		if(getBulletCount() >= magazineType.capacity)
			return false;

		//Try to find a matching magazine in the operator's hotbar
		ItemStack foundMagazine = AmmunitionCrateHandler.findMountedMagazine(gun, magazineType);
		boolean fromCrate = foundMagazine!=null&&!foundMagazine.isEmpty();
		if(!fromCrate)
			foundMagazine = findMagazineInHotbar(magazineType);

		if(foundMagazine==null||foundMagazine.isEmpty())
			return false;

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
		}
		else
			return false;

		IIContent.itemBulletMagazine.writeInventory(foundMagazine, magazineBullets);
		if(IIContent.itemBulletMagazine.hasNoBullets(foundMagazine))
		{
			if(fromCrate)
				AmmunitionCrateHandler.consumeMountedMagazine(gun, foundMagazine);
			else
				consumeMagazineFromHotbar(foundMagazine);
		}
		else if(fromCrate)
			AmmunitionCrateHandler.markMountedMagazineChanged(gun);

		return true;
	}

	//--- Utils ---//

	private void clear()
	{
		Collections.fill(bullets, ItemStack.EMPTY);
	}

	private int getBulletCount()
	{
		int count = 0;
		for(ItemStack bullet : bullets)
			if(!bullet.isEmpty())
				count++;
		return count;
	}

	//--- Persistence ---//

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

	//--- Hotbar interaction ---//


	@Nullable
	protected ItemStack findMagazineInHotbar(Magazines expectedType)
	{
		Entity player = operatorSupplier.get();
		if(player==null)
			return null;

		IItemHandler inventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(inventory==null)
			return null;

		for(int i = 0; i < 9; i++)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if(stack.getItem()==IIContent.itemBulletMagazine&&IIContent.itemBulletMagazine.stackToSub(stack)==expectedType)
				return stack;
		}
		return null;
	}

	protected boolean consumeMagazineFromHotbar(ItemStack magazine)
	{
		Entity player = operatorSupplier.get();
		if(player==null)
			return false;


		IItemHandler inventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(inventory==null)
			return false;

		for(int i = 0; i < 9; i++)
			if(inventory.getStackInSlot(i)==magazine)
			{
				ItemStack extracted = inventory.extractItem(i, 1, false);
				return !extracted.isEmpty();
			}
		return false;
	}
}
