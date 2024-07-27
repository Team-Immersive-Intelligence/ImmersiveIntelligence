package pl.pabilo8.immersiveintelligence.common.weaponsystem.ammunition;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.weaponsystem.IIWeaponBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 20.02.2023
 */
public abstract class AmmoHandlerSingle extends AmmoHandler
{
	private final IAmmoTypeItem validAmmo;
	private final String tag;

	public AmmoHandlerSingle(IIWeaponBase weapon, String tag, IAmmoTypeItem validAmmo)
	{
		super(weapon);
		this.tag = tag;
		this.validAmmo = validAmmo;
	}

	@Override
	public boolean canFire(ItemStack gun, EasyNBT nbt)
	{
		return !nbt.getItemStack(tag).isEmpty();
	}

	@Override
	public boolean isValidAmmo(ItemStack gun, ItemStack ammo)
	{
		return ammo.getItem()==validAmmo;
	}

	@Override
	@Nonnull
	public ItemStack getNextAmmo(ItemStack gun, EasyNBT nbt, boolean doTake)
	{
		ItemStack stack = nbt.getItemStack(tag);
		if(doTake)
			nbt.without(tag);
		return stack;
	}

	@Override
	public int reloadWeapon(ItemStack gun, World world, Entity user, EasyNBT nbt, EasyNBT upgrades, int reloading)
	{
		ItemStack found;

		//Return 0 if there is no ammunition to be loaded
		if((found = weapon.findAmmo(user, gun)).isEmpty())
			return 0;

		final int reloadTime = weapon.getReloadTime(gun, found, upgrades);
		if(reloading==0) //Play loading sound
			playSound(user, getReloadSound(gun, upgrades), SoundCategory.PLAYERS, 1f, 1f);
		reloading++;
		nbt.withItemStack("found", found);

		//Reloading
		if(reloading >= reloadTime)
		{
			if(!world.isRemote)
			{
				//Load ammo
				nbt.withItemStack(tag, found);
				//Take away the item from inventory
				found.shrink(1);
			}
			return 0;
		}


		return reloading;
	}

	@Override
	public void addAmmoInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flag)
	{
		tooltip.add("   "+TextFormatting.GOLD+ItemNBTHelper.getItemStack(stack, tag).getDisplayName());
	}

	@Override
	public NBTTagList getAmmoList(ItemStack stack)
	{
		NBTTagList list = new NBTTagList();
		list.appendTag(ItemNBTHelper.getTagCompound(stack, tag));
		return list;
	}
}
