package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Autocannon;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;

public class EmplacementWeaponLightHowitzer extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	public EmplacementWeaponLightHowitzer()
	{
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.ammoFactory.setAmmo(IIContent.itemAmmoLightArtillery);
		this.visionAABB = this.visionAABB.grow(Autocannon.detectionRadius);
		this.attackAABB = this.attackAABB.grow(Autocannon.attackRadius);

		setupItemHandlers(te, 12, 6, this.ammoFactory::isValidAmmo, this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(3.5f, 3.5f);
		this.ammoFactory.setUseArtilleryAngles(false);
	}

	@Override
	public String getName()
	{
		return "light_howitzer";
	}

	@Override
	public int getShotDelay()
	{
		return 10;
	}

	@Override
	public int getReloadDelay()
	{
		return 56;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return 512;
	}

	@Override
	public int getMaxHealth()
	{
		return 350;
	}
}
