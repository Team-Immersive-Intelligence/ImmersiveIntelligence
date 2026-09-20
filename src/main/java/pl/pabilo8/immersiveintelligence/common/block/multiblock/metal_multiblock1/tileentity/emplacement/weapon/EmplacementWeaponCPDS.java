package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.CPDS;
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
 * Implements the CPDS Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.09.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponCPDS extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	public EmplacementWeaponCPDS()
	{
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.ammoFactory.setAmmo(IIContent.itemAmmoMachinegun);
		this.visionAABB = this.visionAABB.grow(CPDS.detectionRadius);
		this.attackAABB = this.attackAABB.grow(CPDS.attackRadius);
		this.chillingState = new ChillingState(200, 240, 144);
		setupItemHandlers(te, 8, 4, 4, 4+22, this::isMagazine, this::isMagazine);
		this.aim.withAimSpeed(CPDS.yawRotateSpeed, CPDS.pitchRotateSpeed)
				.withPitchLimit(-90, 68.5f);

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
		return new int[]{1};
	}

	@Override
	public int getFireAnimationVariants()
	{
		return 8;
	}

	@Override
	protected int getItemTransferSpeed()
	{
		return CPDS.itemTransferInterval;
	}

	@Override
	protected boolean isSpentCasing(ItemStack stack)
	{
		return super.isSpentCasing(stack)||isMagazine(stack);
	}

	private boolean isMagazine(ItemStack stack)
	{
		return OreDictionary.itemMatches(stack, IIContent.itemBulletMagazine.getMagazine(Magazines.CPDS_DRUM), false);
	}

	@Override
	public String getName()
	{
		return "cpds";
	}

	@Override
	public int getShotDelay()
	{
		return CPDS.bulletFireTime;
	}

	@Override
	public int getReloadDelay()
	{
		return CPDS.reloadTime;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return CPDS.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return CPDS.maxHealth;
	}

	@Nullable
	@Override
	protected Float getLoadingPitch()
	{
		return 0f;
	}

	@Nullable
	@Override
	protected Float getHidingPitch()
	{
		return -90f;
	}
}
