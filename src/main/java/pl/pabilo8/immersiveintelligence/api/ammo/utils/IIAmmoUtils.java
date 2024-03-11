package pl.pabilo8.immersiveintelligence.api.ammo.utils;

import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import blusunrize.immersiveengineering.api.tool.RailgunHandler.RailgunProjectileProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.IIAmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.IIPenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.IIPenetrationRegistry.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Ammunition;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBlockDamageSync;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @since 14-03-2020
 */
public class IIAmmoUtils
{
	//--- Global Values ---//
	public static boolean ammoBreaksBlocks = Weapons.blockDamage;
	public static boolean ammoExplodesBlocks = Ammunition.blockDamage;
	public static boolean ammoRicochets = true;

	//--- Block Damage ---//

	public static void dealBlockDamage(World world, Vec3d direction, float bulletDamage, BlockPos pos, IPenetrationHandler pen)
	{
		if(!ammoBreaksBlocks)
			return;

		DamageBlockPos dimensionBlockPos = new DamageBlockPos(pos, world, pen.getIntegrity());
		float newHp = IIPenetrationRegistry.getBlockHitpoints(pen, pos, world)-(bulletDamage*pen.getReduction());
		if(newHp > 0)
		{
			List<DamageBlockPos> list = IIPenetrationRegistry.blockDamage.stream().filter(damageBlockPos -> damageBlockPos.equals(dimensionBlockPos)).collect(Collectors.toList());
			if(list.size() > 0)
				list.forEach(damageBlockPos -> damageBlockPos.damage = newHp);
			else
				IIPenetrationRegistry.blockDamage.add(new DamageBlockPos(dimensionBlockPos, newHp));

			IIPacketHandler.sendToClient(dimensionBlockPos, world,
					new MessageBlockDamageSync(new DamageBlockPos(dimensionBlockPos, newHp/(pen.getIntegrity()/pen.getReduction())), direction));
		}
		else if(newHp <= 0)
		{
			IIPenetrationRegistry.blockDamage.removeIf(damageBlockPos -> damageBlockPos.equals(dimensionBlockPos));
			world.getBlockState(pos).getBlock().breakBlock(world, pos, world.getBlockState(pos));
			world.destroyBlock(dimensionBlockPos, false);

			IIPacketHandler.sendToClient(dimensionBlockPos, world,
					new MessageBlockDamageSync(new DamageBlockPos(dimensionBlockPos, newHp/(pen.getIntegrity()/pen.getReduction())), direction));
		}
	}

	//--- Common Component Methods ---//

	public static void suppress(World world, double posX, double posY, double posZ, float supressionRadius, int suppressionPower)
	{
		List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX, posY, posZ, posX, posY, posZ).grow(supressionRadius));
		for(EntityLivingBase entity : entities)
		{
			PotionEffect effect = entity.getActivePotionEffect(IIPotions.suppression);
			if(effect==null)
				effect = new PotionEffect(IIPotions.suppression, 120, suppressionPower, false, false);
			else
			{
				effect.duration = 10;
				effect.combine(new PotionEffect(IIPotions.suppression, 120, Math.min(255, effect.getAmplifier()+suppressionPower)));
			}
			entity.addPotionEffect(effect);
		}
	}

	public static void breakArmour(Entity entity, int damageToArmour)
	{
		if(entity instanceof EntityLivingBase)
		{
			EntityLivingBase ent = (EntityLivingBase)entity;
			PotionEffect effect = ent.getActivePotionEffect(IIPotions.brokenArmor);
			if(effect==null)
				effect = new PotionEffect(IIPotions.brokenArmor, 60, damageToArmour, false, false);
			else
			{
				effect.duration = 10;
				effect.combine(new PotionEffect(IIPotions.brokenArmor, 60, Math.min(255, effect.getAmplifier()+damageToArmour)));
			}
			for(ItemStack stack : ent.getArmorInventoryList())
				stack.damageItem(damageToArmour, ent);

			ent.addPotionEffect(effect);
		}
	}

	//--- Ballistic Calculation ---//

	/**
	 * Overload for {@link #calculateBallisticAngle(double, double, float, double, double, double)} with default drag and gravity meant for entity-based guns
	 *
	 * @param posShooter position of the shooter
	 * @param posTarget  position of the target
	 * @param ammoStack  ammo stack
	 * @param precision  precision of the calculation
	 * @return optimal ballistic shooting angle
	 */
	public static float calculateBallisticAngle(Vec3d posShooter, Vec3d posTarget, ItemStack ammoStack, float precision)
	{
		Vec3d dist = posShooter.subtract(posTarget);
		IAmmoTypeItem<?, ?> ammoItem = IIAmmoRegistry.getAmmoItem(ammoStack);

		if(ammoItem==null)
			return 0;

		return calculateBallisticAngle(new Vec3d(dist.x, 0, dist.z).distanceTo(Vec3d.ZERO),
				dist.y,
				ammoItem.getDefaultVelocity(),
				EntityAmmoProjectile.GRAVITY*ammoItem.getMass(ammoStack),
				1f-EntityAmmoProjectile.DRAG,
				precision
		);
	}

	//TODO: 15.02.2024 use an equation instead of simulating the trajectory

	/**
	 * Pitch calculation for artillery stolen from Pneumaticcraft. Huge thanks to desht and MineMaarten for this amazing code!
	 * <a href="https://github.com/TeamPneumatic/pnc-repressurized/blob/master/src/main/java/me/desht/pneumaticcraft/common/tileentity/TileEntityAirCannon.java">https://github.com/TeamPneumatic/pnc-repressurized/blob/master/src/main/java/me/desht/pneumaticcraft/common/tileentity/TileEntityAirCannon.java</a>
	 *
	 * @param distance       distance to target
	 * @param height         height difference between the gun and target
	 * @param force          speed (blocks/s) of the bullet
	 * @param gravity        gravity of the bullet
	 * @param drag           drag factor of the bullet
	 * @param anglePrecision precision with which the angle will be searched, the lower the number, the higher the precision
	 * @return optimal ballistic shooting angle
	 * @author desht
	 * @author MineMaarten
	 */
	public static float calculateBallisticAngle(double distance, double height, float force, double gravity, double drag, double anglePrecision)
	{
		double bestAngle = 0;
		double bestDistance = Float.MAX_VALUE;
		if(gravity==0D)
			return 90F-(float)(Math.atan(height/distance)*180F/Math.PI);
		/*
			simulate the trajectory for angles from 45 to 90 degrees,
			returning the angle which lands the projectile closest to the target distance
		*/
		for(double i = Math.PI*anglePrecision; i < Math.PI*0.5D; i += anglePrecision)
		{
			double motionX = MathHelper.cos((float)i)*force;// calculate the x component of the vector
			double motionY = MathHelper.sin((float)i)*force;// calculate the y component of the vector
			double posX = 0;
			double posY = 0;
			while(posY > height||motionY > 0)
			{
				// simulate movement, until we reach the y-level required
				motionX *= drag;
				motionY *= drag;
				motionY -= gravity;
				posX += motionX;
				posY += motionY;
			}
			double distanceToTarget = Math.abs(distance-posX);
			if(distanceToTarget < bestDistance)
			{
				bestDistance = distanceToTarget;
				bestAngle = i;
			}
		}

		return 90F-(float)(bestAngle*180D/Math.PI);
	}

	//TODO: 15.02.2024 check out optimized version
	/*
	//Optimized version
	public static float calculateBallisticAngle(double distance, double height, float force, double gravity, double drag, double anglePrecision) {
    double lowerBound = Math.PI * anglePrecision;
    double upperBound = Math.PI * 0.5D;
    double bestAngle = 0;
    double bestDistance = Double.MAX_VALUE;

    while (Math.abs(upperBound - lowerBound) > anglePrecision) {
        double midPoint = (lowerBound + upperBound) / 2;
        double motionX = MathHelper.cos((float)midPoint) * force;
        double motionY = MathHelper.sin((float)midPoint) * force;
        double posX = 0;
        double posY = 0;

        while (posY > height || motionY > 0) {
            motionX *= drag;
            motionY *= drag;
            motionY -= gravity;
            posX += motionX;
            posY += motionY;
        }

        double distanceToTarget = Math.abs(distance - posX);
        if (distanceToTarget < bestDistance) {
            bestDistance = distanceToTarget;
            bestAngle = midPoint;
        }

        if (posX < distance) {
            lowerBound = midPoint;
        } else {
            upperBound = midPoint;
        }
    }

    return 90F - (float)(bestAngle * 180D / Math.PI);
}
	 */

	public static float getDirectFireAngle(float initialVelocity, float mass, Vec3d toTarget)
	{
		/*float force = initialVelocity;
		double dist = toTarget.distanceTo(new Vec3d(0, toTarget.y, 0));
		double gravityMotionY = 0, motionY = 0, baseMotionY = toTarget.normalize().y, baseMotionYC;

		while(dist > 0)
		{
			force -= EntityAmmoProjectile.DRAG*force;
			gravityMotionY -= EntityAmmoProjectile.GRAVITY*mass;
			baseMotionYC = baseMotionY*(force/(initialVelocity));
			motionY += (baseMotionYC+gravityMotionY);
			dist -= force;
		}

		toTarget = toTarget.addVector(0, motionY-baseMotionY, 0).normalize();*/

		return (float)Math.toDegrees(calculateFireAngle(initialVelocity,
				mass*EntityAmmoProjectile.GRAVITY,
				toTarget.distanceTo(new Vec3d(0, toTarget.y, 0)),
				toTarget.y
		));


//		return (float)Math.toDegrees((Math.atan2(toTarget.y, toTarget.distanceTo(new Vec3d(0, toTarget.y, 0)))));
	}

	public static float calculateFireAngle(double initialVelocity, double gravity, double distance, double heightDifference)
	{
		double velocity2 = Math.pow(initialVelocity, 2);
		double gravity2 = Math.pow(gravity, 2);
		double distance2 = Math.pow(distance, 2);
		double underRoot = Math.pow(initialVelocity, 4)-gravity*(gravity2*distance2+2*heightDifference*velocity2);

		//No real solutions, the target is out of reach
		if(underRoot < 0)
			return Float.NaN;

		double positive = Math.atan((velocity2+Math.sqrt(underRoot))/(gravity*distance));
		double negative = Math.atan((velocity2-Math.sqrt(underRoot))/(gravity*distance));

		//Return the smaller angle (for the faster trajectory)
		return (float)Math.min(positive, negative);
	}

	public static float getIEDirectRailgunAngle(ItemStack ammo, Vec3d toTarget)
	{
		RailgunProjectileProperties p = RailgunHandler.getProjectileProperties(ammo);
		if(p!=null)
		{
			float force = 20;
			float gravity = (float)p.gravity;

			double gravityMotionY = 0, motionY = 0, baseMotionY = toTarget.normalize().y, baseMotionYC = baseMotionY;
			double dist = toTarget.distanceTo(new Vec3d(0, toTarget.y, 0));
			while(dist > 0)
			{
				dist -= force;
				force *= 0.99;
				baseMotionYC *= 0.99f;
				gravityMotionY -= gravity/force;
				motionY += (baseMotionYC+gravityMotionY);
			}

			toTarget = toTarget.addVector(0, motionY-baseMotionY, 0).normalize();
		}

		return (float)Math.toDegrees((Math.atan2(toTarget.y, toTarget.distanceTo(new Vec3d(0, toTarget.y, 0)))));
	}

	//TODO: 15.02.2024 implement on emplacements
	public static float[] getInterceptionAngles(Vec3d shooterPos, Vec3d shooterVel, Vec3d targetPos, Vec3d targetVel, float projectileSpeed, float mass)
	{
		//Calculate relative position and velocity
		Vec3d relPos = targetPos.subtract(shooterPos);
		Vec3d relVel = targetVel.subtract(shooterVel);

		//Calculate the time it would take for a projectile to reach the target
		double a = relVel.lengthSquared()-projectileSpeed*projectileSpeed;
		double b = 2*relPos.dotProduct(relVel);
		double c = relPos.lengthSquared();
		double discriminant = b*b-4*a*c;

		//No real number roots for the equation
		//TODO: 15.02.2024 return direct aim values without interception
		if(discriminant < 0)
			return new float[]{0, 0};

		double t1 = (-b+Math.sqrt(discriminant))/(2*a);
		double t2 = (-b-Math.sqrt(discriminant))/(2*a);
		double t = Math.min(t1, t2);

		//Predict the future position of the target
		Vec3d futurePos = targetPos.add(targetVel.scale(t));

		//Calculate the direction vector from the shooter to the predicted position of the target
		Vec3d dir = futurePos.subtract(shooterPos).normalize();

		//Convert this direction vector into pitch and yaw angles
		float yaw = (float)((Math.atan2(dir.z, dir.x)*180D)/Math.PI);

		//Calculate the correction for the pitch angle
		float pitch = getDirectFireAngle(projectileSpeed, mass, shooterPos.subtract(futurePos));

		return new float[]{yaw, pitch};
	}

}
