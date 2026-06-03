package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Machinegun;
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
		this.visionAABB = this.visionAABB.grow(Machinegun.detectionRadius);
		this.attackAABB = this.attackAABB.grow(Machinegun.attackRadius);

		setupItemHandlers(te, 20, 12, this.ammoFactory::isValidAmmo, this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(Machinegun.yawRotateSpeed, Machinegun.pitchRotateSpeed)
				.withPitchLimit(-35, 65);
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

	/*@Override
	public EmplacementHitboxEntity[] getCollisionBoxes()
	{
		if(entity==null)
			return new EmplacementHitboxEntity[0];

		//new Vec3d(0,0,0)
		ArrayList<EmplacementHitboxEntity> list = new ArrayList<>();
		list.add(new EmplacementHitboxEntity(entity, "baseBox", 1f, 1.25f,
				new Vec3d(0, 0.75, 0), Vec3d.ZERO, 4));

		list.add(new EmplacementHitboxEntity(entity, "baseSandbags", 0.75f, 0.75f,
				new Vec3d(0.75, 0.5, 0), Vec3d.ZERO, 4));
		list.add(new EmplacementHitboxEntity(entity, "baseSandbags", 0.75f, 0.75f,
				new Vec3d(0.75, 0.5, 0.75), Vec3d.ZERO, 4));
		list.add(new EmplacementHitboxEntity(entity, "baseSandbags", 0.75f, 0.75f,
				new Vec3d(0.75, 0.5, -0.75), Vec3d.ZERO, 4));

		list.add(new EmplacementHitboxEntity(entity, "baseSandbags", 0.75f, 0.75f,
				new Vec3d(0, 0.5, 0.75), Vec3d.ZERO, 4));
		list.add(new EmplacementHitboxEntity(entity, "baseSandbags", 0.75f, 0.75f,
				new Vec3d(0, 0.5, -0.75), Vec3d.ZERO, 4));

		list.add(new EmplacementHitboxEntity(entity, "baseSandbags", 0.75f, 0.75f,
				new Vec3d(-0.75, 0.5, 0), Vec3d.ZERO, 4));
		list.add(new EmplacementHitboxEntity(entity, "baseSandbags", 0.75f, 0.75f,
				new Vec3d(-0.75, 0.5, 0.75), Vec3d.ZERO, 4));
		list.add(new EmplacementHitboxEntity(entity, "baseSandbags", 0.75f, 0.75f,
				new Vec3d(-0.75, 0.5, -0.75), Vec3d.ZERO, 4));


		list.add(new EmplacementHitboxEntity(entity, "barrel", 0.45f, 0.45f,
				new Vec3d(-0.5, 1, 0), new Vec3d(-0.625f, 0, 0), 12));
		list.add(new EmplacementHitboxEntity(entity, "barrel", 0.45f, 0.45f,
				new Vec3d(-0.5, 1, 0), new Vec3d(-1.25, 0, 0), 12));

		return list.toArray(new EmplacementHitboxEntity[0]);
	}*/

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
