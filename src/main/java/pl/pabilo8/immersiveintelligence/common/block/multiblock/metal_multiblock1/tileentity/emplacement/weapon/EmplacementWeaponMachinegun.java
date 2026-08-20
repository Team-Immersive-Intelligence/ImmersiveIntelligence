package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Machinegun;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

/**
 * *reloads the gun* [H] E A V Y  M A C H I N E G U N<br>
 * <s>Rawket Lawnchair!</s>
 */
public class EmplacementWeaponMachinegun extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	public EmplacementWeaponMachinegun()
	{
		super();
		this.setup = new MultiblockInteractablePart(Machinegun.setupTime);
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.ammoFactory.setAmmo(IIContent.itemAmmoMachinegun);
		this.visionAABB = this.visionAABB.grow(Machinegun.detectionRadius);
		this.attackAABB = this.attackAABB.grow(Machinegun.attackRadius);

		setupItemHandlers(te, 8, 8, 4, 4,
				this.ammoFactory::isValidAmmo, this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(Machinegun.yawRotateSpeed, Machinegun.pitchRotateSpeed)
				.withPitchLimit(-35, 65);

		this.gunHandler.withShootSound(IISounds.machinegunShot, 55)
				.withDryFireSound(IISounds.machinegunShotDry);
	}

	@Override
	public String getName()
	{
		return "machinegun";
	}

	@Override
	public int getShotDelay()
	{
		return 2;
	}

	@Override
	public int getReloadDelay()
	{
		return Machinegun.reloadTime;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return Machinegun.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return Machinegun.maxHealth;
	}
}
