package pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIGunBase;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Internal ammunition storage handler for {@link pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIGunBase}
 *
 * @author Pabilo8
 * @since 20.02.2023
 */
public abstract class AmmoHandler
{
	protected final ItemIIGunBase item;

	public AmmoHandler(ItemIIGunBase item)
	{
		this.item = item;
	}

	public abstract boolean canFire(ItemStack weapon, EasyNBT nbt);

	/**
	 * @param weapon weapon itemstack
	 * @param ammo   ammo itemstack to be checked
	 * @return true when it's a valid magazine or bullet
	 */
	public abstract boolean isValidAmmo(ItemStack weapon, ItemStack ammo);

	@Nonnull
	public abstract ItemStack getNextAmmo(ItemStack weapon, EasyNBT nbt, boolean doTake);


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
