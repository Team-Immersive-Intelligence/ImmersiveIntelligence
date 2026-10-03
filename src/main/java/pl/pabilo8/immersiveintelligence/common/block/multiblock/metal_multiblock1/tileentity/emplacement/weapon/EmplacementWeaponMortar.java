package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Mortar;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageParticleEffect;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;
import pl.pabilo8.immersiveintelligence.common.util.gun.ChillingState;

import javax.annotation.Nullable;

/**
 * Implements the single-round Mortar Emplacement weapon without spent casings.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.09.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponMortar extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	public EmplacementWeaponMortar()
	{

	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.chillingState = new ChillingState(Mortar.minimumIdleTime,
				Mortar.idleAnimationInterval, Mortar.idleAnimationDuration);
		this.ammoFactory.setAmmo(IIContent.itemAmmoMortar);
		this.visionAABB = this.visionAABB.grow(Mortar.detectionRadius);
		this.attackAABB = this.attackAABB.grow(Mortar.attackRadius);

		setupItemHandlers(te, 12, 0, 4, 0, this.ammoFactory::isValidAmmo, this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(Mortar.yawRotateSpeed, Mortar.pitchRotateSpeed)
				.withYawLimit(Mortar.minYaw, Mortar.maxYaw)
				.withPitchLimit(Mortar.minPitch, Mortar.maxPitch);
		this.gunHandler.withShootSound(IISounds.howitzerShot, 55);
		this.rotateAfterFiring = false;
	}

	@Override
	public Vec3d getWeaponOffset()
	{
		return new Vec3d(0, 0.625f, 0);
	}

	@Override
	protected void configureProjectile(TileEntityEmplacement te, EntityAmmoProjectile projectile, TargetCoordinateReference target)
	{
		super.configureProjectile(te, projectile, target);
		IIPacketHandler.sendToClient(new MessageParticleEffect("ammo/gunfire_mortar", te.getWorld(), te.tactileHandler.getPosition("fire"),
				aim.getTarget(0).scale(0.01), aim.getYaw(0), aim.getPitch(0), EasyNBT.newNBT()));
	}

	@Override
	protected int[] getReloadStages()
	{
		return new int[]{1};
	}

	@Override
	protected boolean storesSpentCasings()
	{
		return false;
	}

	@Nullable
	@Override
	protected Float getLoadingYaw()
	{
		return Mortar.loadingYaw;
	}

	@Override
	protected Float getLoadingPitch()
	{
		return Mortar.loadingPitch;
	}

	@Nullable
	@Override
	protected Float getHidingYaw()
	{
		return Mortar.hidingYaw;
	}

	@Nullable
	@Override
	protected Float getHidingPitch()
	{
		return Mortar.hidingPitch;
	}

	@Override
	public String getName()
	{
		return "mortar";
	}

	@Override
	public boolean isArtilleryWeapon()
	{
		return true;
	}

	@Override
	public boolean usesBallisticFireByDefault()
	{
		return true;
	}

	@Override
	public int getShotDelay()
	{
		return Mortar.shotFireTime;
	}

	@Override
	public int getReloadDelay()
	{
		return Mortar.reloadTime;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return Mortar.energyUpkeepCost;
	}

	@Override
	public int getArmorForPart(String partName)
	{
		if(partName.startsWith("barrel"))
			return 20;
		return switch(partName)
		{
			case "turret_child1", "turret_child2", "turret_child3" -> 1;
			case "base" -> 12;
			default -> 8;
		};
	}

	@Override
	public int getMaxHealth()
	{
		return Mortar.maxHealth;
	}
}
