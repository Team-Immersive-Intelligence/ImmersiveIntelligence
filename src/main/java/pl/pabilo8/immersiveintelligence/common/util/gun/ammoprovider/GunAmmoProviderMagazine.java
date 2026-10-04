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
 * @updated 03.10.2026
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
		if(magazineStack.isEmpty())
		{
			if(getBulletCount()==0)
				return;
			magazineStack = IIContent.itemBulletMagazine.getMagazine(magazineType);
		}

		IIContent.itemBulletMagazine.writeInventory(magazineStack, bullets);
		IIContent.itemBulletMagazine.defaultize(magazineStack);
	}

	/**
	 * Rebuilds the internal bullet list from a magazine stack.
	 *
	 * @param stack magazine to read
	 */
	private void rebuildBulletsFromMagazine(@Nonnull ItemStack stack)
	{
		clear();
		if(stack.isEmpty())
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
		rebuildMagazineFromBullets();
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
		if(!magazineStack.isEmpty()||getBulletCount()!=0)
			return false;

		ItemStack foundMagazine = AmmunitionCrateHandler.findMountedMagazine(gun, magazineType);
		boolean fromCrate = foundMagazine!=null&&!foundMagazine.isEmpty();
		if(!fromCrate)
			foundMagazine = findMagazineInHotbar(magazineType);

		if(foundMagazine==null||foundMagazine.isEmpty())
			return false;

		//Copy before extraction. Load only after the source releases the magazine.
		ItemStack loadedMagazine = foundMagazine.copy();
		boolean consumed = fromCrate?AmmunitionCrateHandler.consumeMountedMagazine(gun, foundMagazine):
				consumeMagazineFromHotbar(foundMagazine);
		if(!consumed)
			return false;

		setLoadedStack(loadedMagazine);
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

	/**
	 * Returns a copy of the loaded magazine.
	 */
	@Nonnull
	public ItemStack getLoadedStack()
	{
		return magazineStack.copy();
	}

	/**
	 * Restores a matching magazine and makes it available for firing or unloading.
	 */
	public void setLoadedStack(@Nonnull ItemStack stack)
	{
		magazineStack = !stack.isEmpty()&&stack.getItem()==IIContent.itemBulletMagazine
				&&IIContent.itemBulletMagazine.stackToSub(stack)==magazineType?stack.copy(): ItemStack.EMPTY;
		if(!magazineStack.isEmpty())
			magazineStack.setCount(1);
		rebuildBulletsFromMagazine(magazineStack);
		reload = 0;
		loadingState = magazineStack.isEmpty()?GunLoadingState.EMPTY: GunLoadingState.LOADED;
	}

	//--- NBT ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		rebuildMagazineFromBullets();
		NBTTagCompound nbt = super.serializeNBT();
		if(!magazineStack.isEmpty())
			nbt.setTag("magazine", magazineStack.serializeNBT());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("magazine"))
			setLoadedStack(new ItemStack(nbt.getCompoundTag("magazine")));
		else
			setLoadedStack(ItemStack.EMPTY);
		//Restore the saved reload state after the stack setter resets it.
		super.deserializeNBT(nbt);
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
			if(stack.getItem()==IIContent.itemBulletMagazine&&IIContent.itemBulletMagazine.stackToSub(stack)==expectedType
					&&!IIContent.itemBulletMagazine.hasNoBullets(stack))
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
