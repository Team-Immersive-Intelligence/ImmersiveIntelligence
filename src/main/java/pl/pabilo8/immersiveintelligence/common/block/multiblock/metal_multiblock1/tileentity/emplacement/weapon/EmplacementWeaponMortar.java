package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Autocannon;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.CPDS;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.FilteredItemHandler;

public class EmplacementWeaponMortar extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	public EmplacementWeaponMortar()
	{
		this.inventoryBase = NonNullList.withSize(12, ItemStack.EMPTY);
		this.inventoryPlatform = NonNullList.withSize(6, ItemStack.EMPTY);
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.visionAABB = this.visionAABB.grow(Autocannon.detectionRadius);
		this.attackAABB = this.attackAABB.grow(Autocannon.attackRadius);

		this.inventoryBaseHandler = new FilteredItemHandler(inventoryBase)
				.withFilter(this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(CPDS.yawRotateSpeed, CPDS.pitchRotateSpeed);
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
