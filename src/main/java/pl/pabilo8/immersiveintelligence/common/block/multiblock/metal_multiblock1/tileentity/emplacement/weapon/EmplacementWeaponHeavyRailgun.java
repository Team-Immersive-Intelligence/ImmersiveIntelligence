package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.HeavyRailgun;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIRailgunOverride;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

/**
 * To Blu:
 * I do as I promised, I promised to not add a railgun turret
 * so I added a Heavy Railgun emplacement
 */
public class EmplacementWeaponHeavyRailgun extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	public EmplacementWeaponHeavyRailgun()
	{
		this.setup = new MultiblockInteractablePart(HeavyRailgun.setupTime);
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.ammoFactory.setAmmo(IIContent.itemRailgunGrenade);
		this.visionAABB = this.visionAABB.grow(HeavyRailgun.detectionRadius);
		this.attackAABB = this.attackAABB.grow(HeavyRailgun.attackRadius);
		setupItemHandlers(te, 18, 6, ItemIIRailgunOverride::isAmmo, ItemIIRailgunOverride::isAmmo);
		this.aim.withAimSpeed(HeavyRailgun.yawRotateSpeed, HeavyRailgun.pitchRotateSpeed);
	}

	@Override
	public String getName()
	{
		return "heavy_railgun";
	}

	@Override
	public int getShotDelay()
	{
		return HeavyRailgun.shotFireTime;
	}

	@Override
	public int getReloadDelay()
	{
		return HeavyRailgun.reloadTime;
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

		list.add(new EmplacementHitboxEntity(entity, "shieldRight", 0.75f, 2f,
				new Vec3d(-0.5, 1, -0.625), Vec3d.ZERO, 14));
		list.add(new EmplacementHitboxEntity(entity, "shieldRightBack", 0.75f, 2f,
				new Vec3d(0, 1, -0.625), Vec3d.ZERO, 14));
		list.add(new EmplacementHitboxEntity(entity, "shieldMiddle", 0.75f, 0.5f,
				new Vec3d(-0.5, 0, -0.625), Vec3d.ZERO, 14));
		list.add(new EmplacementHitboxEntity(entity, "shieldLeft", 0.75f, 2f,
				new Vec3d(-0.5, 1, 0.625), Vec3d.ZERO, 14));

		list.add(new EmplacementHitboxEntity(entity, "barrel", 0.625f, 0.625f,
				new Vec3d(-0.5, 1.5, 0), new Vec3d(-0.625f, 0, 0), 12));
		list.add(new EmplacementHitboxEntity(entity, "barrel", 0.625f, 0.625f,
				new Vec3d(-0.5, 1.5, 0), new Vec3d(-1.25, 0, 0), 12));
		list.add(new EmplacementHitboxEntity(entity, "barrel", 0.625f, 0.625f,
				new Vec3d(-0.5, 1.5, 0), new Vec3d(-1.875, 0, 0), 12));

		return list.toArray(new EmplacementHitboxEntity[0]);
	}*/

	@Override
	public int getEnergyUpkeepCost()
	{
		return HeavyRailgun.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return HeavyRailgun.maxHealth;
	}
}
