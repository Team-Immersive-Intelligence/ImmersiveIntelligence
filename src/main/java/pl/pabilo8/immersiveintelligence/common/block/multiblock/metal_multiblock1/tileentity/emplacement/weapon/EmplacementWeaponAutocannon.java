package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Autocannon;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;

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
		this.visionAABB = this.visionAABB.grow(Autocannon.detectionRadius);
		this.attackAABB = this.attackAABB.grow(Autocannon.attackRadius);
		this.aim.withAimSpeed(Autocannon.yawRotateSpeed, Autocannon.pitchRotateSpeed);

		setupItemHandlers(te, 18, 8, stack -> OreDictionary.itemMatches(stack,
				IIContent.itemBulletMagazine.getMagazine(Magazines.AUTOCANNON), false), stack -> OreDictionary.itemMatches(stack,
				IIContent.itemBulletMagazine.getMagazine(Magazines.AUTOCANNON), false));
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

	/*@Override
	public EmplacementHitboxEntity[] getCollisionBoxes()
	{
		if(entity==null)
			return new EmplacementHitboxEntity[0];

		//new Vec3d(0,0,0)
		ArrayList<EmplacementHitboxEntity> list = new ArrayList<>();
		list.add(new EmplacementHitboxEntity(entity, "baseBox", 1f, 1.5f,
				new Vec3d(0, 1, 0), Vec3d.ZERO, 4));
		list.add(new EmplacementHitboxEntity(entity, "ammoBox", 0.625f, 0.75f,
				new Vec3d(1, 0.625, 0), Vec3d.ZERO, 2));

		list.add(new EmplacementHitboxEntity(entity, "shieldRight", 0.75f, 2f,
				new Vec3d(-0.5, 1, -0.625), Vec3d.ZERO, 12));
		list.add(new EmplacementHitboxEntity(entity, "shieldLeft", 0.75f, 2f,
				new Vec3d(-0.5, 1, 0.625), Vec3d.ZERO, 12));

		list.add(new EmplacementHitboxEntity(entity, "barrelRight", 0.5f, 0.5f,
				new Vec3d(-0.5, 1.125, -0.625), new Vec3d(-0.5, 0, 0), 12));
		list.add(new EmplacementHitboxEntity(entity, "barrelRight", 0.5f, 0.5f,
				new Vec3d(-0.5, 1.125, -0.625), new Vec3d(-1, 0, 0), 12));

		list.add(new EmplacementHitboxEntity(entity, "barrelLeft", 0.5f, 0.5f,
				new Vec3d(-0.5, 1, 0.625), new Vec3d(-0.5, 0, 0), 12));
		list.add(new EmplacementHitboxEntity(entity, "barrelLeft", 0.5f, 0.5f,
				new Vec3d(-0.5, 1, 0.625), new Vec3d(-1, 0, 0), 12));

		return list.toArray(new EmplacementHitboxEntity[0]);
	}*/

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
