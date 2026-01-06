package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Machinegun;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon.EmplacementHitboxEntity;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.FilteredItemHandler;

import java.util.ArrayList;

/**
 * *reloads the gun* [H] E A V Y  M A C H I N E G U N<br>
 * <s>Rawket Lawnchair!</s>
 */
public class EmplacementWeaponMachinegun extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	public EmplacementWeaponMachinegun()
	{
		super();
		this.inventoryBase = NonNullList.withSize(36, ItemStack.EMPTY);
		this.inventoryPlatform = NonNullList.withSize(12, ItemStack.EMPTY);
		this.inventoryPlatformHandler = new FilteredItemHandler(inventoryPlatform)
				.withFilter(stack -> stack.getItem()==IIContent.itemAmmoMachinegun);
	}

	@Override
	public void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.visionAABB = this.visionAABB.grow(Machinegun.detectionRadius);
		this.attackAABB = this.attackAABB.grow(Machinegun.attackRadius);
	}

	@Override
	public String getName()
	{
		return "machinegun";
	}

	@Override
	public float getYawTurnSpeed()
	{
		return Machinegun.yawRotateSpeed;
	}

	@Override
	public float getPitchTurnSpeed()
	{
		return Machinegun.pitchRotateSpeed;
	}

	@Override
	public float getPitchUpperLimit()
	{
		return 65;
	}

	@Override
	public float getPitchLowerLimit()
	{
		return -35;
	}

	@Override
	public float getShotDelay()
	{
		return 2;
	}

	@Override
	public float getReloadDelay()
	{
		return Machinegun.reloadTime;
	}

	@Override
	public float getSetupDelay()
	{
		return Machinegun.setupTime;
	}

	@Override
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
