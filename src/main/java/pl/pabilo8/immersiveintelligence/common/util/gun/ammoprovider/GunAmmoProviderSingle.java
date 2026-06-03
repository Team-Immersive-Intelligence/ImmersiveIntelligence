package pl.pabilo8.immersiveintelligence.common.util.gun.ammoprovider;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunAmmoProvider;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 26.05.2026
 */
public class GunAmmoProviderSingle<T extends IAmmoTypeItem<T, E>, E extends EntityAmmoBase<? super E>> extends GunAmmoProvider
{
	private final IAmmoTypeItem<T, E> acceptedAmmo;
	private ItemStack loadedBullet = ItemStack.EMPTY;
	private ItemStack pendingBullet = ItemStack.EMPTY;

	public GunAmmoProviderSingle(Entity gun, Supplier<Entity> operatorSupplier, IAmmoTypeItem<T, E> acceptedAmmo, float loadTime)
	{
		super(gun, operatorSupplier);
		this.acceptedAmmo = acceptedAmmo;
		this.maxReload = loadTime;
	}

	@Override
	protected void onLoadFinished()
	{
		if(!pendingBullet.isEmpty())
		{
			loadedBullet = pendingBullet.copy();
			pendingBullet = ItemStack.EMPTY;
		}
	}

	@Override
	protected void onUnloadFinished()
	{
		if(!loadedBullet.isEmpty())
		{
			IIUtils.giveOrDropStack(this.operatorSupplier.get(), loadedBullet);
			giveOrDrop(loadedBullet);
			loadedBullet = ItemStack.EMPTY;
		}
	}

	@Nonnull
	@Override
	protected ItemStack provideNextBullet()
	{
		if(loadedBullet.isEmpty())
			return ItemStack.EMPTY;
		ItemStack bullet = loadedBullet.copy();
		loadedBullet = ItemStack.EMPTY;
		loadingState = GunLoadingState.EMPTY;
		return bullet;
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getAmmoList()
	{
		return NonNullList.withSize(loadedBullet.isEmpty()?0: 1, loadedBullet);
	}

	@Override
	protected boolean canFindAmmo()
	{
		Entity player = operatorSupplier.get();
		if(player==null)
			return false;

		IItemHandler inv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(inv==null)
			return false;

		for(int i = 0; i < inv.getSlots(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty()&&stack.getItem()==acceptedAmmo)
			{
				pendingBullet = stack.copy();
				return true;
			}
		}
		return false;
	}

	//--- Persistence ---//

	@Nonnull
	public ItemStack getLoadedStack()
	{
		return loadedBullet.isEmpty()?ItemStack.EMPTY: loadedBullet.copy();
	}

	public void setLoadedStack(@Nonnull ItemStack stack)
	{
		if(stack.isEmpty()||stack.getItem()!=acceptedAmmo)
			loadedBullet = ItemStack.EMPTY;
		else
			loadedBullet = stack.copy();
	}

	//--- NBT ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.wrapNBT(super.serializeNBT())
				.withItemStack("loaded_bullet", this.loadedBullet)
				.unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		super.deserializeNBT(nbt);
		this.loadedBullet = new ItemStack(nbt.getCompoundTag("loaded_bullet"));
	}
}
