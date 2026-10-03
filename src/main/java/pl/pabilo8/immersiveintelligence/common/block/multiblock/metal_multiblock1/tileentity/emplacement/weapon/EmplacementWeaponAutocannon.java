package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Autocannon;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.util.gun.ChillingState;
import pl.pabilo8.immersiveintelligence.common.util.gun.ammoprovider.GunAmmoProviderItemHandler;
import pl.pabilo8.immersiveintelligence.common.util.gun.ammoprovider.GunAmmoProviderMagazineItemHandler;

import javax.annotation.Nullable;

/**
 * Implements the four-barrel Autocannon Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 21.08.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponAutocannon extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	public EmplacementWeaponAutocannon()
	{
		super();
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.ammoFactory.setAmmo(IIContent.itemAmmoAutocannon);
		this.visionAABB = this.visionAABB.grow(Autocannon.detectionRadius);
		this.attackAABB = this.attackAABB.grow(Autocannon.attackRadius);
		this.chillingState = new ChillingState(Autocannon.minimumIdleTime,
				Autocannon.idleAnimationInterval, Autocannon.idleAnimationDuration);
		this.aim.withAimSpeed(Autocannon.yawRotateSpeed, Autocannon.pitchRotateSpeed)
				.withYawLimit(Autocannon.minYaw, Autocannon.maxYaw)
				.withPitchLimit(Autocannon.minPitch, Autocannon.maxPitch);

		setupItemHandlers(te, 8, 4, 8, 16, this::isMagazine, this::isMagazine);

		this.rotateAfterFiring = true;
		this.gunHandler.withShootSound(IISounds.autocannonShot, 55)
				.withDryFireSound(IISounds.machinegunShotDry);
	}

	@Override
	protected GunAmmoProviderItemHandler createPlatformAmmoProvider()
	{
		return new GunAmmoProviderMagazineItemHandler(null, () -> null, platformAmmoHandler, this::isMagazine,
				ammoFactory::isValidAmmo, this::storePlatformSpentItem, () -> ammoFactory.getWorld().isRemote, getReloadDelay());
	}

	@Override
	protected int[] getReloadStages()
	{
		return new int[]{1, 1, 1, 1};
	}

	@Override
	public int getFireAnimationVariants()
	{
		return 4;
	}

	@Override
	protected int getItemTransferSpeed()
	{
		return Autocannon.itemTransferInterval;
	}

	@Override
	protected boolean isSpentCasing(ItemStack stack)
	{
		return super.isSpentCasing(stack)||isMagazine(stack);
	}

	private boolean isMagazine(ItemStack stack)
	{
		return OreDictionary.itemMatches(stack, IIContent.itemBulletMagazine.getMagazine(Magazines.AUTOCANNON), false);
	}

	@Override
	public String getName()
	{
		return "autocannon";
	}

	@Override
	public int getShotDelay()
	{
		return Autocannon.bulletFireTime;
	}

	@Override
	public int getReloadDelay()
	{
		return Autocannon.reloadTime;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return Autocannon.energyUpkeepCost;
	}

	@Override
	public int getArmorForPart(String partName)
	{
		if(partName.startsWith("cannon"))
			return 20;
		return switch(partName)
		{
			case "turret_child0", "turret_child1" -> 1;
			case "turret_child2", "turret_child3", "turret_child4", "turret_child5",
				 "turret_child6", "turret_child7" -> 24;
			case "base" -> 12;
			default -> 8;
		};
	}

	@Override
	public int getMaxHealth()
	{
		return Autocannon.maxHealth;
	}

	@Nullable
	@Override
	protected Float getLoadingPitch()
	{
		return 0f;
	}
}
