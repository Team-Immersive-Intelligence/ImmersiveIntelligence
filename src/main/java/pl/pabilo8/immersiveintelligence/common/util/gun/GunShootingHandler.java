package pl.pabilo8.immersiveintelligence.common.util.gun;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessagePlayIISound;
import pl.pabilo8.immersiveintelligence.common.util.sound.AdvancedSounds;

/**
 * Used to store and calculate gun recoil and overheating.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 16.08.2026
 * @since 15.05.2026
 */
public class GunShootingHandler implements INBTSerializable<NBTBase>
{
	private float shotDelay, maxShotDelay;
	private GunRecoil recoil = null;
	private AmmoFactory<? extends EntityAmmoProjectile> ammoFactory = null;
	private GunAmmoProvider ammoProvider = null;
	private NBTTagCompound pendingAmmoProviderNBT = null;
	//Sounds
	private AdvancedSounds.RangedSound sound = null;
	private SoundEvent soundDryFire = null;
	private int shootSoundRange = 1;

	public GunShootingHandler()
	{

	}

	public void update()
	{
		//Decrease shot delay
		shotDelay = Math.max(0, shotDelay-1);
		//Update recoil and ammo provider
		if(this.recoil!=null)
			this.recoil.update();
		if(this.ammoProvider!=null)
			this.ammoProvider.update();
	}

	public boolean fire()
	{
		//Check basic conditions
		if(!canShoot()||ammoProvider==null||ammoFactory==null)
			return false;
		World world = ammoFactory.getWorld();

		//Cannot fire when the provider is still reloading
		if(ammoProvider.isReloading())
			return false;

		//Load ammo from provider
		ItemStack firedStack = ammoProvider.provideAmmo();
		boolean fired = false;
		if(!world.isRemote)
		{
			if(!firedStack.isEmpty())
			{
				ammoFactory.setStack(firedStack);
				EntityAmmoProjectile projectile = ammoFactory.create();
				fired = projectile!=null;
				if(fired&&sound!=null)
					IIPacketHandler.sendToAllClients(new MessagePlayIISound(sound, SoundCategory.BLOCKS, shootSoundRange, ammoFactory.getPos(), 1f, 1f));
			}
			else if(soundDryFire!=null)
				world.playSound(null, new BlockPos(ammoFactory.getPos()), soundDryFire, SoundCategory.BLOCKS, 1f, 1f);
		}
		else
			fired = !firedStack.isEmpty();

		if(!fired)
			return false;

		//Reset shot delay
		this.shotDelay = maxShotDelay;
		//Add recoil and gun overheat
		if(recoil!=null)
		{
			recoil.addRecoil();
			if(recoil.isOverheated()&&!world.isRemote)
				world.playSound(null, new BlockPos(ammoFactory.getPos()), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1f);
		}
		return true;
	}

	public boolean startReloading()
	{
		return ammoProvider!=null&&ammoProvider.startReloading();
	}

	//--- With ---//

	public GunShootingHandler withMaxShotDelay(float maxShotDelay)
	{
		this.maxShotDelay = maxShotDelay;
		return this;
	}

	public GunShootingHandler withRecoilHandler(GunRecoil recoil)
	{
		this.recoil = recoil;
		return this;
	}

	public GunShootingHandler withAmmoFactory(AmmoFactory<? extends EntityAmmoProjectile> ammoFactory)
	{
		this.ammoFactory = ammoFactory;
		return this;
	}

	public GunShootingHandler withAmmoProvider(GunAmmoProvider provider)
	{
		this.ammoProvider = provider;
		if(this.ammoProvider!=null&&pendingAmmoProviderNBT!=null)
		{
			this.ammoProvider.deserializeNBT(pendingAmmoProviderNBT);
			pendingAmmoProviderNBT = null;
		}
		return this;
	}

	public GunShootingHandler withShootSound(AdvancedSounds.RangedSound sound, int range)
	{
		this.sound = sound;
		this.shootSoundRange = range;
		return this;
	}

	public GunShootingHandler withDryFireSound(SoundEvent soundDryFire)
	{
		this.soundDryFire = soundDryFire;
		return this;
	}

	//--- Getters ---//

	public float getShotDelay(float partialTicks)
	{
		return Math.max(0, shotDelay-partialTicks);
	}

	public boolean canShoot()
	{
		return shotDelay <= 0&&(recoil==null||!recoil.isOverheated());
	}

	public float getLoadingProgress(float partialTicks)
	{
		if(ammoProvider==null)
			return 0;
		return ammoProvider.getLoadingProgress(partialTicks);
	}

	//--- NBT ---//

	@Override
	public NBTBase serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat("shot_delay", shotDelay);
		if(ammoProvider!=null)
			nbt.setTag("ammo_provider", ammoProvider.serializeNBT());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTBase nbt)
	{
		//Compatibility with the old scalar shot-delay format.
		if(nbt instanceof NBTTagFloat)
		{
			this.shotDelay = ((NBTTagFloat)nbt).getFloat();
			this.pendingAmmoProviderNBT = null;
			return;
		}
		if(!(nbt instanceof NBTTagCompound))
			return;

		NBTTagCompound compound = (NBTTagCompound)nbt;
		this.shotDelay = compound.getFloat("shot_delay");
		if(compound.hasKey("ammo_provider"))
		{
			NBTTagCompound providerNBT = compound.getCompoundTag("ammo_provider");
			if(ammoProvider!=null)
				ammoProvider.deserializeNBT(providerNBT);
			else
				pendingAmmoProviderNBT = providerNBT.copy();
		}
		else
			pendingAmmoProviderNBT = null;
	}
}
