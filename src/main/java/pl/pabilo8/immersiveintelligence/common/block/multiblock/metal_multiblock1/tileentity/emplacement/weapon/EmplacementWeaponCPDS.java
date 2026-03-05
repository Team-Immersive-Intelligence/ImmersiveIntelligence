package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.CPDS;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.FilteredItemHandler;

/**
 * CPDS Q&A
 * <p>
 * Q: Is CPDS a real life thing?
 * A: Not really, it's based on CIWS, but in II it performs a counter-projectile role with anti-aircraft as secondary task
 * <p>
 * Q: What does CPDS stand for?
 * A: Counter-Projectile Defense System
 * <p>
 * Q: Why gatling?
 * A: It's the most high-tech II can get ^^ Historically, gatling guns were used since the US Civil War, so yes, they existed in interwar/ww2
 * Decided to choose it because of the unique design
 * <p>
 * Q: Isn't it OP? it's 8 barrels
 * A: Yes, but it costs a lot
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 */
public class EmplacementWeaponCPDS extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	public EmplacementWeaponCPDS()
	{
		this.inventoryPlatform = NonNullList.withSize(3, ItemStack.EMPTY);
		this.inventoryBase = NonNullList.withSize(8, ItemStack.EMPTY);
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.visionAABB = this.visionAABB.grow(CPDS.detectionRadius);
		this.attackAABB = this.attackAABB.grow(CPDS.attackRadius);
		this.inventoryBaseHandler = new FilteredItemHandler(inventoryBase)
				.withFilter(stack -> OreDictionary.itemMatches(stack,
						IIContent.itemBulletMagazine.getMagazine(Magazines.CPDS_DRUM), false));
		this.aim.withAimSpeed(CPDS.yawRotateSpeed, CPDS.pitchRotateSpeed);
	}

	@Override
	public String getName()
	{
		return "cpds";
	}

	@Override
	public int getShotDelay()
	{
		return 0;
	}

	@Override
	public int getReloadDelay()
	{
		return CPDS.reloadTime;
	}

	/*@Override
	public EmplacementHitboxEntity[] getCollisionBoxes()
	{
		if(entity==null)
			return new EmplacementHitboxEntity[0];

		//new Vec3d(0,0,0)
		ArrayList<EmplacementHitboxEntity> list = new ArrayList<>();
		list.add(new EmplacementHitboxEntity(entity, "baseBox", 2f, 0.75f,
				new Vec3d(0, 0.75, 0), Vec3d.ZERO, 12));
		list.add(new EmplacementHitboxEntity(entity, "topBox", 1.75f, 0.75f+0.5f,
				new Vec3d(0.25, 0.75+0.5, 0), Vec3d.ZERO, 12));
		list.add(new EmplacementHitboxEntity(entity, "camera", 0.75f, 0.75f,
				new Vec3d(-0.125, 2.25, -0.625), Vec3d.ZERO, 6));

		list.add(new EmplacementHitboxEntity(entity, "barrel1", 0.5f, 0.5f,
				new Vec3d(0, 1, 0), new Vec3d(-1.25, 0, -0.25), 20));
		list.add(new EmplacementHitboxEntity(entity, "barrel2", 0.5f, 0.5f,
				new Vec3d(0, 1, 0), new Vec3d(-1.75, 0, -0.25), 20));
		list.add(new EmplacementHitboxEntity(entity, "barrel3", 0.5f, 0.5f,
				new Vec3d(0, 1, 0), new Vec3d(-2.25, 0, -0.25), 20));


		return list.toArray(new EmplacementHitboxEntity[0]);
	}*/

	@Override
	public int getEnergyUpkeepCost()
	{
		return CPDS.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return CPDS.maxHealth;
	}

}
