package pl.pabilo8.immersiveintelligence.common.util.gun.ammoprovider;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandlerModifiable;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunAmmoProvider;

import javax.annotation.Nonnull;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Ammunition provider that consumes bullets from an item handler instead of a player's inventory.
 * Intended for emplacement platform inventories. It supports both direct bullet stacks and II bullet magazines.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 03.06.2026
 */
public class GunAmmoProviderItemHandler extends GunAmmoProvider
{
	private final IItemHandlerModifiable inventory;
	private final Predicate<ItemStack> acceptedAmmo;

	public GunAmmoProviderItemHandler(Entity gun, Supplier<Entity> operatorSupplier, IItemHandlerModifiable inventory,
									  Predicate<ItemStack> acceptedAmmo, float loadTime)
	{
		super(gun, operatorSupplier);
		this.inventory = inventory;
		this.acceptedAmmo = acceptedAmmo==null?stack -> true: acceptedAmmo;
		this.maxReload = loadTime;
	}

	@Override
	protected void onLoadFinished()
	{
		//No internal cache. The platform inventory itself is the loaded source.
	}

	@Override
	protected void onUnloadFinished()
	{
		//No unload behaviour: the ammo remains in the platform inventory.
	}

	@Override
	protected boolean canFindAmmo()
	{
		return hasAvailableAmmo();
	}

	public boolean hasAvailableAmmo()
	{
		return findDirectAmmoSlot() >= 0||findMagazineAmmoSlot() >= 0;
	}

	@Nonnull
	@Override
	protected ItemStack provideNextBullet()
	{
		int magazineSlot = findMagazineAmmoSlot();
		if(magazineSlot >= 0)
		{
			ItemStack magazine = inventory.getStackInSlot(magazineSlot).copy();
			NonNullList<ItemStack> bullets = IIContent.itemBulletMagazine.readInventory(magazine);

			for(int i = 0; i < bullets.size(); i++)
			{
				ItemStack bullet = bullets.get(i);
				if(!bullet.isEmpty()&&acceptedAmmo.test(bullet))
				{
					ItemStack provided = bullet.copy();
					provided.setCount(1);

					bullet.shrink(1);
					if(bullet.getCount() <= 0)
						bullets.set(i, ItemStack.EMPTY);

					IIContent.itemBulletMagazine.writeInventory(magazine, bullets);
					inventory.setStackInSlot(magazineSlot,
							IIContent.itemBulletMagazine.hasNoBullets(magazine)?ItemStack.EMPTY: magazine);

					if(!hasAvailableAmmo())
						loadingState = GunLoadingState.EMPTY;
					return provided;
				}
			}
		}

		int directSlot = findDirectAmmoSlot();
		if(directSlot >= 0)
		{
			ItemStack provided = inventory.extractItem(directSlot, 1, false);
			if(!hasAvailableAmmo())
				loadingState = GunLoadingState.EMPTY;
			return provided;
		}

		loadingState = GunLoadingState.EMPTY;
		return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getAmmoList()
	{
		NonNullList<ItemStack> list = NonNullList.create();
		for(int i = 0; i < inventory.getSlots(); i++)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if(stack.isEmpty())
				continue;
			if(isMagazineWithAmmo(stack)||acceptedAmmo.test(stack))
				list.add(stack.copy());
		}
		return list;
	}

	private int findDirectAmmoSlot()
	{
		for(int i = 0; i < inventory.getSlots(); i++)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if(!stack.isEmpty()&&acceptedAmmo.test(stack))
				return i;
		}
		return -1;
	}

	private int findMagazineAmmoSlot()
	{
		for(int i = 0; i < inventory.getSlots(); i++)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if(isMagazineWithAmmo(stack))
				return i;
		}
		return -1;
	}

	private boolean isMagazineWithAmmo(ItemStack stack)
	{
		if(stack.isEmpty()||stack.getItem()!=IIContent.itemBulletMagazine)
			return false;

		Magazines type = IIContent.itemBulletMagazine.stackToSub(stack);
		if(type==null)
			return false;

		NonNullList<ItemStack> bullets = IIContent.itemBulletMagazine.readInventory(stack);
		for(ItemStack bullet : bullets)
			if(!bullet.isEmpty()&&acceptedAmmo.test(bullet))
				return true;
		return false;
	}
}
