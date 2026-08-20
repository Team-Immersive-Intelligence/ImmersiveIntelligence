package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Autocannon;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;

/**
 * Implements the single-round Guided Missile Launcher Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 17.08.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponGuidedMissileLauncher extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	public EmplacementWeaponGuidedMissileLauncher()
	{
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.ammoFactory.setAmmo(IIContent.itemAmmoGuidedMissile);
		this.visionAABB = this.visionAABB.grow(Autocannon.detectionRadius);
		this.attackAABB = this.attackAABB.grow(Autocannon.attackRadius);
		setupItemHandlers(te, 8, 0, 4, 0,
				this.ammoFactory::isValidAmmo, this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(2.5f, 5f);
	}

	@Override
	protected boolean isSpentCasing(ItemStack stack)
	{
		return false;
	}

	@Override
	protected boolean storesSpentCasings()
	{
		return false;
	}

	@Override
	protected int[] getReloadStages()
	{
		return new int[]{1};
	}

	@Override
	public String getName()
	{
		return "guided_missile_launcher";
	}

	@Override
	public int getShotDelay()
	{
		return 10;
	}

	@Override
	public int getReloadDelay()
	{
		return 160;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return 1024;
	}

	@Override
	public int getMaxHealth()
	{
		return 250;
	}
}
