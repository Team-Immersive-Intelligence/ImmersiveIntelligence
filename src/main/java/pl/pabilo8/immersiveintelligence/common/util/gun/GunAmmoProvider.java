package pl.pabilo8.immersiveintelligence.common.util.gun;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * Abstract ammunition provider for mounted weapons.
 * Handles loading/unloading from player inventory or external sources.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 23.05.2026
 */
public abstract class GunAmmoProvider implements INBTSerializable<NBTTagCompound>
{
	//Context
	protected final Entity gun;
	protected final Supplier<Entity> operatorSupplier;

	//State
	protected GunLoadingState loadingState = GunLoadingState.EMPTY;
	protected float reload = 0, maxReload = 0;
	protected GunAmmoProvider other = null;

	//Sounds
	protected SoundEvent loadSound = null;
	protected SoundEvent unloadSound = null;

	protected GunAmmoProvider(Entity gun, Supplier<Entity> operatorSupplier)
	{
		this.gun = gun;
		this.operatorSupplier = operatorSupplier;
	}

	public void update()
	{
		if(loadingState==GunLoadingState.EMPTY||loadingState==GunLoadingState.LOADED)
			return;

		if(++reload >= maxReload)
		{
			switch(loadingState)
			{
				case LOAD:
					onLoadFinished();
					loadingState = GunLoadingState.LOADED;
					break;
				case UNLOAD:
					onUnloadFinished();
					loadingState = GunLoadingState.EMPTY;
					break;
				default:
					break;
			}
			reload = 0;
		}
	}

	/**
	 * Starts the reload sequence.
	 */
	public final boolean startReloading()
	{
		if(loadingState==GunLoadingState.EMPTY)
		{
			if(canFindAmmo())
			{
				loadingState = GunLoadingState.LOAD;
				reload = 0;
				if(loadSound!=null)
					gun.world.playSound(null, gun.getPosition(), loadSound, SoundCategory.BLOCKS, 1f, 1f);
				return true;
			}
		}
		else if(loadingState==GunLoadingState.LOADED)
		{
			loadingState = GunLoadingState.UNLOAD;
			reload = 0;
			if(loadSound!=null)
				gun.world.playSound(null, gun.getPosition(), unloadSound, SoundCategory.BLOCKS, 1f, 1f);
			return true;
		}
		return false;
	}

	//--- Sounds ---//

	public GunAmmoProvider withLoadingSound(SoundEvent sound)
	{
		this.loadSound = sound;
		return this;
	}

	public GunAmmoProvider withUnloadingSound(SoundEvent sound)
	{
		this.unloadSound = sound;
		return this;
	}

	public GunAmmoProvider withSounds(SoundEvent load, SoundEvent unload)
	{
		this.loadSound = load;
		this.unloadSound = unload;
		return this;
	}

	public GunAmmoProvider withSecondaryProvider(GunAmmoProvider other)
	{
		this.other = other;
		return this;
	}

	//--- Core methods ---//

	protected abstract void onLoadFinished();

	protected abstract void onUnloadFinished();

	protected abstract boolean canFindAmmo();

	@Nonnull
	protected abstract ItemStack provideNextBullet();

	@Nonnull
	public abstract NonNullList<ItemStack> getAmmoList();

	@Nonnull
	public final ItemStack provideAmmo()
	{
		if(loadingState!=GunLoadingState.LOADED)
			return ItemStack.EMPTY;
		ItemStack bullet = provideNextBullet();
		if(bullet.isEmpty()&&other!=null)
			return other.provideAmmo();
		return bullet;
	}

	protected final void giveOrDrop(ItemStack ammoStack)
	{
		Entity entity = operatorSupplier.get();
		//No operator, drop at gun position
		if(entity==null)
			Utils.dropStackAtPos(gun.world, gun.getPosition(), ammoStack);
		else
			//Try to give the item to the operator, preferably into their ammo pouch, drop when that's not possible
			IIUtils.giveOrDropCasingStack(entity, ammoStack);
	}

	//--- Getters ---//

	public boolean isReloading()
	{
		return loadingState==GunLoadingState.LOAD||loadingState==GunLoadingState.UNLOAD;
	}

	public float getLoadingProgress(float partialTicks)
	{
		switch(loadingState)
		{
			case LOADED:
				return 1f;
			case LOAD:
				return (reload+partialTicks)/Math.max(1, maxReload);
			case UNLOAD:
				return 1f-((reload+partialTicks)/Math.max(1, maxReload));
			case EMPTY:
			default:
				return 0f;
		}
	}

	//--- NBT ---//
	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT()
				.withFloat("reload", reload)
				.withInt("state", loadingState.ordinal())
				.unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		this.reload = nbt.getFloat("reload");
		this.loadingState = GunLoadingState.values()[nbt.getInteger("state")%GunLoadingState.values().length];
	}

	public enum GunLoadingState implements ISerializableEnum
	{
		EMPTY,
		LOAD,
		LOADED,
		UNLOAD
	}

}
