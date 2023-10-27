package pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
public abstract class AmmoHandlerList extends AmmoHandler
{
	private final IAmmo validAmmo;
	private final String tag;
	private final int size;

	public AmmoHandlerList(ItemIIGunBase item, String tag, IAmmo validAmmo, int size)
	{
		super(item);
		this.tag = tag;
		this.validAmmo = validAmmo;
		this.size = size;
	}

	@Override
	public boolean canFire(ItemStack weapon, EasyNBT nbt)
	{
		return !nbt.getList(tag, EasyNBT.TAG_COMPOUND).hasNoTags();
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
		NBTTagList list = nbt.getList(tag, EasyNBT.TAG_COMPOUND);
		if(doTake)
			return new ItemStack(((NBTTagCompound)list.removeTag(0)));
		return new ItemStack(list.getCompoundTagAt(0));
	}

	@Override
	public int reloadWeapon(ItemStack weapon, World world, Entity user, EasyNBT nbt, EasyNBT upgrades, int reloading)
	{
		ItemStack found;

		//Return 0 if there is no ammunition to be loaded
		if((found = item.findAmmo(user, weapon)).isEmpty())
			return 0;
		NBTTagList list = nbt.getList(tag, EasyNBT.TAG_COMPOUND);
		//Max amount loaded
		if(list.tagCount() >= size)
			return 0;

		final int reloadTime = item.getReloadTime(weapon, found, upgrades);
		// TODO: 21.02.2023
		nbt.withItemStack("found", found);

		//Play loading sound
		if(reloading==1)
			playSound(user, getStartLoadingSound(weapon, upgrades), SoundCategory.PLAYERS, 1f, 1f);
			//Play bullet loading sound
		else if(reloading==reloadTime/3)
			playSound(user, getReloadSound(weapon, upgrades), SoundCategory.PLAYERS, 1f, 1f);
		reloading++;

		boolean finish = reloading >= reloadTime;

		//Reloading
		if(finish||(list.tagCount() < size-1&&reloading >= reloadTime/1.5))
		{
			//Get the current stack
			ItemStack copy = found.copy();
			copy.setCount(1);
			list.appendTag(copy.serializeNBT());

			//Take away the item from inventory
			found.shrink(1);
			nbt.withTag(tag, list);

			//Continue loading, until there's no space (or user has no bullets)
			if(list.tagCount() < size)
				return (reloading/3);
		}

		if(finish)
		{
			if(!world.isRemote)
				playSound(user, getFinishLoadingSound(weapon, upgrades), SoundCategory.PLAYERS, 1f, 1f);
			return 0;
		}

		return reloading;
	}

	@Override
	public void addAmmoInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flag)
	{
		NBTTagList list = ItemNBTHelper.getTag(stack).getTagList(tag, EasyNBT.TAG_COMPOUND);
		for(NBTBase nbt : list)
			tooltip.add("   "+TextFormatting.GOLD+new ItemStack(((NBTTagCompound)nbt)).getDisplayName());
	}

	@Override
	public NBTTagList getAmmoList(ItemStack stack)
	{
		return ItemNBTHelper.getTag(stack).getTagList(tag, EasyNBT.TAG_COMPOUND);
	}

	//--- Client ---//

	@Nullable
	protected abstract SoundEvent getFinishLoadingSound(ItemStack weapon, EasyNBT nbt);

	@Nullable
	protected abstract SoundEvent getStartLoadingSound(ItemStack weapon, EasyNBT nbt);

	@SideOnly(Side.CLIENT)
	@Nullable
	@Override
	protected SoundEvent getUnloadSound(ItemStack weapon, EasyNBT nbt)
	{
		return null;
	}
}
