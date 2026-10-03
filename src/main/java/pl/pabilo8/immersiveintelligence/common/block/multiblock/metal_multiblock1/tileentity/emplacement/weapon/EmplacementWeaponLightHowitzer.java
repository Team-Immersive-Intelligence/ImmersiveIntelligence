package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.LightHowitzer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageParticleEffect;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;
import pl.pabilo8.immersiveintelligence.common.util.gun.ChillingState;

/**
 * Implements the single-round Light Howitzer Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.09.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponLightHowitzer extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	public EmplacementWeaponLightHowitzer()
	{

	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.chillingState = new ChillingState(LightHowitzer.minimumIdleTime,
				LightHowitzer.idleAnimationInterval, LightHowitzer.idleAnimationDuration);
		this.ammoFactory.setAmmo(IIContent.itemAmmoLightArtillery);
		this.visionAABB = this.visionAABB.grow(LightHowitzer.detectionRadius);
		this.attackAABB = this.attackAABB.grow(LightHowitzer.attackRadius);

		setupItemHandlers(te, 8, 8, 4, 4,
				this.ammoFactory::isValidAmmo, this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(LightHowitzer.yawRotateSpeed, LightHowitzer.pitchRotateSpeed)
				.withYawLimit(LightHowitzer.minYaw, LightHowitzer.maxYaw)
				.withPitchLimit(LightHowitzer.minPitch, LightHowitzer.maxPitch);
		this.gunHandler.withShootSound(IISounds.howitzerShot, 55);
		this.rotateAfterFiring = false;
	}

	@Override
	protected void configureProjectile(TileEntityEmplacement te, EntityAmmoProjectile projectile, TargetCoordinateReference target)
	{
		super.configureProjectile(te, projectile, target);
		IIPacketHandler.sendToClient(new MessageParticleEffect("ammo/gunfire_howitzer", te.getWorld(), te.tactileHandler.getPosition("fire"),
				aim.getTarget(0).scale(0.01), aim.getYaw(0), aim.getPitch(0), EasyNBT.newNBT()));
	}

	@Override
	public Vec3d getWeaponOffset()
	{
		return new Vec3d(0, 0.625f, 0);
	}

	@Override
	protected int[] getReloadStages()
	{
		return new int[]{1};
	}

	@Override
	protected Float getLoadingYaw()
	{
		return LightHowitzer.loadingYaw;
	}

	@Override
	protected Float getLoadingPitch()
	{
		return LightHowitzer.loadingPitch;
	}

	@Override
	public String getName()
	{
		return "light_howitzer";
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
		return LightHowitzer.shotFireTime;
	}

	@Override
	public int getReloadDelay()
	{
		return LightHowitzer.reloadTime;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return LightHowitzer.energyUpkeepCost;
	}

	@Override
	public int getArmorForPart(String partName)
	{
		if(partName.startsWith("barrel"))
			return 20;
		return switch(partName)
		{
			case "turret_child5", "turret_child6" -> 1;
			case "turret_child1", "turret_child2", "turret_child3", "turret_child4" -> 24;
			case "base" -> 12;
			default -> partName.startsWith("gun_block")?16: 8;
		};
	}

	@Override
	public int getMaxHealth()
	{
		return LightHowitzer.maxHealth;
	}
}
