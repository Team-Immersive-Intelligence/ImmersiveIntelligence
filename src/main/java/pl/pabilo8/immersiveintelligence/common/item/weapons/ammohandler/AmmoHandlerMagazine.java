package pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIGunBase;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Handles magazine-based ammunition storage and reload sequences.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 10.08.2026
 * @since 20.02.2023
 */
public abstract class AmmoHandlerMagazine extends AmmoHandler
{
	private final IAmmoTypeItem<?, ?> validAmmo;
	private final String tag;

	public AmmoHandlerMagazine(ItemIIGunBase item, String tag, IAmmoTypeItem<?, ?> validAmmo)
	{
		super(item);
		this.tag = tag;
		this.validAmmo = validAmmo;
	}

	@Override
	public boolean canFire(ItemStack weapon, EasyNBT nbt)
	{
		return !IIContent.itemBulletMagazine.hasNoBullets(nbt.getItemStack(tag));
	}

	@Override
	public boolean isValidAmmo(ItemStack weapon, ItemStack ammo)
	{
		if(ammo.getItem()==IIContent.itemBulletMagazine)
		{
			Magazines magazine = IIContent.itemBulletMagazine.stackToSub(ammo);
			//If magazine is of valid type and has bullets
			return magazine.ammo==validAmmo&&isValidType(weapon, magazine)&&!IIContent.itemBulletMagazine.hasNoBullets(ammo);
		}
		return false;
	}

	protected boolean isValidType(ItemStack weapon, Magazines magazine)
	{
		return true;
	}

	@Override
	@Nonnull
	public ItemStack getNextAmmo(ItemStack weapon, EasyNBT nbt, boolean doTake)
	{
		ItemStack magazine = nbt.getItemStack(tag);
		ItemStack ammo = IIContent.itemBulletMagazine.takeBullet(magazine, doTake);

		//Save the magazine
		if(doTake)
			nbt.withItemStack(tag, magazine);

		//Return ammo extracted from magazine
		return ammo;
	}

	/**
	 * Gets the magazine currently installed in the weapon.
	 *
	 * @param nbt weapon NBT
	 * @return loaded magazine or an empty stack
	 */
	@Nonnull
	public ItemStack getLoadedMagazine(EasyNBT nbt)
	{
		return nbt.getItemStack(tag);
	}

	@Override
	public int reloadWeapon(ItemStack weapon, World world, Entity user, EasyNBT nbt, EasyNBT upgrades, int reloading)
	{
		ItemStack loaded = getLoadedMagazine(nbt), found;
		final int reloadTime = item.getReloadTime(weapon, loaded, upgrades);

		if(!loaded.isEmpty())
		{
			//Play unloading sound
			if(reloading==reloadTime/3)
				playSound(user, getUnloadSound(weapon, upgrades), SoundCategory.PLAYERS, 1f, 1f);

			//Progress reloading
			reloading += 1;
			if(reloading >= reloadTime)
			{
				if(!world.isRemote)
				{
					//Return the magazine through the active reload source
					IIContent.itemBulletMagazine.defaultize(loaded);
					item.returnReloadedMagazine(user, weapon, loaded);
				}
				//Mirror the visual weapon state on both sides. Item transfer stays server-side.
				nbt.without(tag);
				return 0; //Stop
			}
			return reloading; //Continue
		}

		//Return 0 if there is no ammunition to be loaded
		if((found = item.findAmmo(user, weapon)).isEmpty())
			return 0;
		markLoadedAmmo(nbt, found);

		if(reloading==reloadTime/1.5) //Play loading sound
			playSound(user, getReloadSound(weapon, upgrades), SoundCategory.PLAYERS, 1f, 1f);
		reloading++;

		//Reloading
		if(reloading >= reloadTime)
		{
			//Mirror the loaded magazine for the renderer on both sides.
			nbt.withItemStack(tag, found.copy());
			if(!world.isRemote)
				//Take away the item from the authoritative source.
				found.shrink(1);
			return 0;
		}

		return reloading;
	}

	public void markLoadedAmmo(EasyNBT nbt, ItemStack ammo)
	{

	}

	@Override
	public void addAmmoInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flag)
	{
		IIContent.itemBulletMagazine.addInformation(ItemNBTHelper.getItemStack(stack, tag), world, tooltip, flag);
	}

	@Override
	public NBTTagList getAmmoList(ItemStack stack)
	{
		return ItemNBTHelper.getTagCompound(ItemNBTHelper.getItemStack(stack, tag), "bullets").getTagList("dictionary", 10);
	}
}
