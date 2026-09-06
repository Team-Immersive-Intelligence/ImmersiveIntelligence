package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Autocannon;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.util.gun.ammoprovider.GunAmmoProviderItemHandler;
import pl.pabilo8.immersiveintelligence.common.util.gun.ammoprovider.GunAmmoProviderMagazineItemHandler;

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
		this.aim.withAimSpeed(Autocannon.yawRotateSpeed, Autocannon.pitchRotateSpeed);

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
		return 2;
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
	public int getMaxHealth()
	{
		return Autocannon.maxHealth;
	}


}
