package pl.pabilo8.immersiveintelligence.common.weaponsystem.ammunition;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.weaponsystem.IIWeaponBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AmmoHandler
{
	/**
	 * Gun item
	 */
	protected final IIWeaponBase weapon;

	public AmmoHandler(IIWeaponBase weapon)
	{
		this.weapon = weapon;
	}

	public abstract boolean canFire(ItemStack gun, EasyNBT nbt);

	/**
	 * @param gun Weapon's ItemStack
	 * @param ammo - Ammo's ItemStack to be checked
	 * @return true when it's a valid magazine or bullet
	 */
	public abstract boolean isValidAmmo(ItemStack gun, ItemStack ammo);

	@Nonnull
	public abstract ItemStack getNextAmmo(ItemStack gun, EasyNBT nbt, boolean doTake);

	/**
	 * Reloads the weapon with ammunition
	 *
	 * @param weapon    weapon stack
	 * @param world
	 * @param user      user of the weapon
	 * @param nbt       stack's tag
	 * @param upgrades  upgrade tag
	 * @param reloading ticks
	 * @return
	 */
	public abstract int reloadWeapon(ItemStack weapon, World world, Entity user, EasyNBT nbt, EasyNBT upgrades, int reloading);

	public abstract void addAmmoInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flag);

	public abstract NBTTagList getAmmoList(ItemStack stack);

	//--- Client Methods ---//

	/**
	 * @param weapon the gun ItemStack
	 * @param nbt    upgrade tag
	 * @return sound played when unloading ammo from the gun
	 */
	@Nullable
	protected abstract SoundEvent getUnloadSound(ItemStack weapon, EasyNBT nbt);

	/**
	 * @param weapon the gun ItemStack
	 * @param nbt    upgrade tag
	 * @return sound played when loading ammo into the gun
	 */
	@Nullable
	protected abstract SoundEvent getReloadSound(ItemStack weapon, EasyNBT nbt);

	//--- Utility Methods ---//

	protected void playSound(@Nullable Entity user, @Nullable SoundEvent sound, SoundCategory category, float volume, float pitch)
	{
		if(user!=null&&sound!=null)
			user.world.playSound(null, user.posX, user.posY, user.posZ, sound, category, volume, pitch);
	}
}
