package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Autocannon;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.CPDS;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;

public class EmplacementWeaponMortar extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	public EmplacementWeaponMortar()
	{
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.ammoFactory.setAmmo(IIContent.itemAmmoMortar);
		this.visionAABB = this.visionAABB.grow(Autocannon.detectionRadius);
		this.attackAABB = this.attackAABB.grow(Autocannon.attackRadius);

		setupItemHandlers(te, 12, 6, this.ammoFactory::isValidAmmo, this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(CPDS.yawRotateSpeed, CPDS.pitchRotateSpeed);
		this.ammoFactory.setUseArtilleryAngles(true);
	}

	@Override
	public String getName()
	{
		return "mortar";
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
