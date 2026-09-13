package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;

import javax.annotation.Nullable;

/**
 * Applies hostile-only exposure around the point illuminated by an Emplacement light.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 08.09.2026
 */
public abstract class EmplacementWeaponLightBase extends EmplacementWeaponTurretBase
{
	@Override
	public final boolean canShoot(TileEntityEmplacement te)
	{
		return true;
	}

	@Override
	protected boolean shoot(TileEntityEmplacement te, TargetCoordinateReference target)
	{
		Vec3d targetPosition = target.supplyCoordinates();
		if(targetPosition!=null)
		{
			applyExposure(te, targetPosition);
			onTargetIlluminated(te, target.getEntity());
		}
		//A continuous light does not consume a Fire Mission shot.
		return false;
	}

	@Override
	protected void onIdleUpdate(TileEntityEmplacement te)
	{
		Vec3d origin = getAimOrigin(te);
		Vec3d end = origin.add(aim.getTarget(0).scale(getIlluminationRange()));
		RayTraceResult hit = te.getWorld().rayTraceBlocks(origin, end, false, true, false);
		applyExposure(te, hit==null||hit.hitVec==null?end: hit.hitVec);
	}

	private void applyExposure(TileEntityEmplacement te, Vec3d targetPosition)
	{
		OwnerIdentity owner = te.getOwnerIdentity();
		double radius = Math.max(0d, getExposureRadius());
		double radiusSq = radius*radius;
		AxisAlignedBB area = new AxisAlignedBB(targetPosition, targetPosition).grow(radius);
		for(EntityLivingBase entity : te.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, area,
				candidate -> candidate.isEntityAlive()&&owner.isHostile(candidate)
						&&candidate.getDistanceSq(targetPosition.x, targetPosition.y, targetPosition.z) <= radiusSq))
			entity.addPotionEffect(new PotionEffect(IIPotions.exposed, Math.max(1, getExposureDuration()),
					getExposureAmplifier(te, entity), false, true));
	}

	/**
	 * Gets the exposure amplifier for one illuminated entity.
	 */
	protected int getExposureAmplifier(TileEntityEmplacement te, EntityLivingBase entity)
	{
		return 1;
	}

	/**
	 * Applies weapon-specific effects to the tracked entity.
	 */
	protected void onTargetIlluminated(TileEntityEmplacement te, @Nullable Entity entity)
	{

	}

	/**
	 * @return maximum light reach in blocks
	 */
	protected abstract float getIlluminationRange();

	/**
	 * @return exposed-effect radius in blocks
	 */
	protected abstract float getExposureRadius();

	/**
	 * @return exposed-effect duration in ticks
	 */
	protected abstract int getExposureDuration();
}
