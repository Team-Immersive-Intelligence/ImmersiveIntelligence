package pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIGunBase;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 20.02.2023
 */
public abstract class AmmoHandlerSingle extends AmmoHandler
{
	private final IAmmo validAmmo;
	private final String tag;

	public AmmoHandlerSingle(ItemIIGunBase item, String tag, IAmmo validAmmo)
	{
		super(item);
		this.tag = tag;
		this.validAmmo = validAmmo;
	}

	@Override
	public boolean canFire(ItemStack weapon, EasyNBT nbt)
	{
		return !nbt.getItemStack(tag).isEmpty();
	}

	@Override
	public boolean isValidAmmo(ItemStack weapon, ItemStack ammo)
	{
		return ammo.getItem()==validAmmo;
	}

	@Override
	@Nonnull
	public ItemStack getNextAmmo(ItemStack weapon, EasyNBT nbt, boolean doTake)
	{
		ItemStack stack = nbt.getItemStack(tag);
		if(doTake)
			nbt.without(tag);
		return stack;
	}

	@Override
	public int reloadWeapon(ItemStack weapon, World world, Entity user, EasyNBT nbt, EasyNBT upgrades, int reloading)
	{
		ItemStack found;

		//Return 0 if there is no ammunition to be loaded
		if((found = item.findAmmo(user, weapon)).isEmpty())
			return 0;

		final int reloadTime = item.getReloadTime(weapon, found, upgrades);
		if(reloading==0) //Play loading sound
			playSound(user, getReloadSound(weapon, upgrades), SoundCategory.PLAYERS, 1f, 1f);
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
