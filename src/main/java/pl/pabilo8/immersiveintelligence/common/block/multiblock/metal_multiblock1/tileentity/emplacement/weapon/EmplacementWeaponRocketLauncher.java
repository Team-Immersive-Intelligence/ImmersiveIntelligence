package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Autocannon;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.gun.ChillingState;

/**
 * Implements the two-stage, six-round Rocket Launcher Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 17.08.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponRocketLauncher extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		if(this.chillingState==null)
			this.chillingState = new ChillingState(240, 320, 80);
		this.ammoFactory.setAmmo(IIContent.itemAmmoRocketLight);
		this.visionAABB = this.visionAABB.grow(Autocannon.detectionRadius);
		this.attackAABB = this.attackAABB.grow(Autocannon.attackRadius);
		setupItemHandlers(te, 16, 0, 16, 0,
				this.ammoFactory::isValidAmmo, this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(2f, 1f);
		this.aim.withPitchLimit(-90, 90);
		this.ammoFactory.setUseArtilleryAngles(true);
		this.rotateAfterFiring = false;
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
		return new int[]{4, 4};
	}

	@Override
	protected Float getLoadingYaw()
	{
		return 180f;
	}

	@Override
	protected Float getLoadingPitch()
	{
		return 0f;
	}

	@Override
	protected Float getHidingYaw()
	{
		return 0f;
	}

	@Override
	protected Float getHidingPitch()
	{
		return 0f;
	}

	@Override
	public String getName()
	{
		return "rocket_launcher";
	}

	@Override
	public int getShotDelay()
	{
		return 20;
	}

	@Override
	public int getReloadDelay()
	{
		return 280;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return 1024;
	}

	@Override
	public int getMaxHealth()
	{
		return 300;
	}
}
