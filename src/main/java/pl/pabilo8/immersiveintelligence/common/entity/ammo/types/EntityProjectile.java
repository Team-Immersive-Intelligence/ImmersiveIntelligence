package pl.pabilo8.immersiveintelligence.common.entity.ammo.types;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.api.ammo.IIPenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.IIPenetrationRegistry.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.HitEffect;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenMaterialTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.MultipleRayTracer;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.MultipleRayTracer.MultipleTracerBuilder;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.ArrayList;
import java.util.Collection;

public class EntityProjectile extends EntityAmmoBase
{
	//--- Constants ---//
	/**
	 * Tick limit for the projectile, used for decay
	 */
	public static final int MAX_TICKS = 600;
	/**
	 * The drag coefficient of the projectile, used for calculating the motion vector
	 */
	public static final float DRAG = 0.01f;
	/**
	 * The gravity coefficient of the projectile, used for calculating the motion vector
	 */
	public static final float GRAVITY = 0.15f;

	//--- Properties ---//
	/**
	 * The base motion of the bullet, used for calculating the motion vector
	 */
	protected Vec3d baseMotion = Vec3d.ZERO;
	/**
	 * Gravity motion of the bullet, used for calculating the motion vector
	 */
	protected double gravityMotionY = 0;

	/**
	 * The velocity of the bullet, in blocks per tick
	 */
	protected float velocity;
	/**
	 * The penetration ability of the projectile, in blocks<br>
	 * The penetration hardness of a projectile, uses block hardness values
	 */
	protected float penetrationAbility, penetrationHardness;

	/**
	 * Blocks and Entities to ignore during hit detection
	 */
	protected ArrayList<Entity> ignoredEntities = new ArrayList<>();
	protected ArrayList<BlockPos> ignoredPositions = new ArrayList<>();

	public EntityProjectile(World world)
	{
		super(world);
	}

	/**
	 * @param owner The owner of this bullet, used for statistics and hit detection
	 */
	@Override
	public void setOwner(@Nullable Entity owner)
	{
		super.setOwner(owner);
		this.ignoredEntities.add(owner);
	}

	@Override
	protected boolean shouldDecay()
	{
		return ticksExisted > MAX_TICKS||posY < 0;
	}

	@Override
	protected Vec3d getDirection()
	{
		return baseMotion;
	}

	//--- Update ---//


	@Override
	public void onUpdate()
	{
		super.onUpdate();

		//Yep, that's it, that's the entire motion code
		gravityMotionY -= GRAVITY*this.mass*DEV_SLOMO;
		gravityMotionY *= 1d-DRAG;
		setMotion();

		//Volumetric raytrace through the projectile's flight path
		MultipleRayTracer tracer = MultipleTracerBuilder.setPos(world,
						this.getPositionVector(),
						this.getNextPositionVector())
				.setAABB(this.getEntityBoundingBox()
						.grow(fuseType==EnumFuseTypes.PROXIMITY?fuseParameter: 0)
						.offset(this.getPositionVector().scale(-1)))
				.setFilters(this.ignoredEntities, this.ignoredPositions)
				.volumetricTrace();
		for(RayTraceResult hit : tracer)
			if(hit!=null)
				switch(hit.typeOfHit)
				{
					case BLOCK:
						handleBlockDamage(hit);
						break;
					case ENTITY:
						handleEntityDamage(hit);
						break;
				}

		//TODO: 03.01.2024 clear penetration cluster
//		PenetrationCluster cluster = new PenetrationCluster();

//		setMotion();
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		setPosition(posX, posY, posZ);

		if(fuseType==EnumFuseTypes.TIMED)
			if(fuseParameter-- <= 0)
				detonate();


		if(world.isRemote)
			if(penetrationHardness!=0)
			{
				Vec3d normalized = new Vec3d(motionX, motionY, motionZ).normalize();
				float motionXZ = MathHelper.sqrt(normalized.x*normalized.x+normalized.z*normalized.z);
				this.rotationYaw = (float)((Math.atan2(normalized.x, normalized.z)*180D)/3.1415927410125732D);
				this.rotationPitch = -(float)((Math.atan2(normalized.y, motionXZ)*180D)/3.1415927410125732D);

				for(int j = 0; j < components.length; j++)
				{
					IAmmoComponent c = components[j];
					c.spawnParticleTrail(this, componentNBT[j]);
				}
			}

	}

	protected void handleBlockDamage(RayTraceResult hit)
	{
		BlockPos pos = hit.getBlockPos();
		ignoredPositions.add(pos);

		if(!world.isRemote)
		{
			IPenetrationHandler penetrationHandler = IIPenetrationRegistry.getPenetrationHandler(world.getBlockState(pos));
			PenMaterialTypes penType = penetrationHandler.getPenetrationType();
			float pen = penetrationHardness*coreType.getPenMod(penType);
			float dmg = baseDamage*coreType.getDamageMod(penType)/4f;
			float hardness = world.getBlockState(pos).getBlockHardness(world, pos);

			if(hardness < 0)
				pen = -1;

			if(pen > hardness/penetrationHandler.getReduction())
			{
				//thanks lgmrszd
				if(!world.getBlockState(pos).getMaterial().isLiquid())
					if(!world.isRemote)
					{
						IIAmmoUtils.dealBlockDamage(world, getBaseMotion(), dmg, pos, penetrationHandler);
						IISounds.playHitSound(HitEffect.IMPACT, world, hit.getBlockPos(), penetrationHandler);
					}

				penetrationHardness *= ((hardness*16f)/pen);
				force *= 0.85f;
			}
			else if(pen > 0)
			{
				if(!world.isRemote)
				{
					if(!world.getBlockState(pos).getMaterial().isLiquid())
					{
						IIAmmoUtils.dealBlockDamage(world, getBaseMotion(), dmg*(hardness/penetrationHandler.getReduction()), pos, penetrationHandler);
						playHitSound(HitEffect.IMPACT, world, hit.getBlockPos(), penetrationHandler);
					}
					if(fuse==-1)
						performEffect(hit);
				}

				stopAtPoint(hit);
				penetrationHardness = 0;
				break penloop;
			}
			else
			{
				//can't ricochet if penetrates multiple blocks
				if(pen!=-1&&force > hardness&&penetrationHandler.getPenetrationType().canRicochetOff()&&i==0)
				{
					ricochet(hardness/2f, pos);
					if(!world.isRemote)
						playHitSound(HitEffect.RICOCHET, world, hit.getBlockPos(), penetrationHandler);
				}
				else
				{
					if(!world.isRemote)
						if(fuse==-1)
							performEffect(hit);
					stopAtPoint(hit);

				}
				break penloop;
			}
		}
	}

	protected void handleEntityDamage(RayTraceResult hit)
	{
		ignoredEntities.add(hit.entityHit);
		if(hit.entityHit==this) //can't touch this
			return;
		Entity e = hit.entityHit;

		if(e instanceof EntityAmmoBase)
		{
			if(((EntityAmmoBase)e).owner==this.owner)
				return;

			//you are already dead
			e.setDead();
			this.setDead();
			//nani?
			((EntityAmmoBase)e).detonate();
			this.detonate();
		}
		else
		{
			int armor = 0, toughness = 1;
			if(e instanceof EntityLivingBase)
			{
				armor = MathHelper.floor(((EntityLivingBase)e).getEntityAttribute(SharedMonsterAttributes.ARMOR).getAttributeValue());
				toughness += MathHelper.floor(((EntityLivingBase)e).getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
			}
			//TODO: 03.01.2024 calculate armor values

			PenMaterialTypes penType = (toughness > 0||armor > 0)?PenMaterialTypes.METAL: PenMaterialTypes.FLESH;
			float pen = penetrationHardness*coreType.getPenMod(penType)*Math.min(force, 1.15f);
			float dmg = baseDamage*coreType.getDamageMod(penType);

			//Damage armour
			IIAmmoUtils.breakArmour(e, (int)(baseDamage*coreType.getPenMod(penType)/8f));

			//bullet penetrated the entity
			if(pen > toughness)
			{
				//armor not counted in
				if(!world.isRemote)
				{
					IIAmmoUtils.breakArmour(e, (int)(baseDamage*coreType.getPenMod(penType)/6f));
					e.hurtResistantTime = 0;
					if(!e.attackEntityFrom(IIDamageSources.causeBulletDamage(this, this.shooter, e), dmg))
						performEffect(hit);
				}
				stopAtPoint(hit);
				penetrationHardness *= ((toughness*4f)/pen);
				force *= 0.85f;
			}
			//bullet stopped in entity
			else if(pen > 0)
			{
				if(!world.isRemote)
				{
					float depth = (pen-(toughness*6f))/pen;

					IIAmmoUtils.breakArmour(e, (int)(baseDamage*coreType.getPenMod(penType)/8f));
					e.hurtResistantTime = 0;
					if(!e.attackEntityFrom(IIDamageSources.causeBulletDamage(this, this.shooter, e), dmg))
						performEffect(hit);
					penetrationHardness = 0;
					if(fuse==-1)
						performEffect(hit);
				}
				stopAtPoint(hit);
				break penloop;
			}
			//bullet ricocheted off entity
			else
			{
				if(force > toughness*1.5f&&penType.canRicochetOff()&&i==0)
				{
					ricochet(toughness/2f, hit.getBlockPos());
					hitEntities.add(hit.entityHit);
				}
				else if(!world.isRemote)
					if(fuse==-1)
						performEffect(hit);
				stopAtPoint(hit);
				onHitRicochet();
			}
		}
	}

	public Vec3d getNextPositionVector()
	{
		return getPositionVector().addVector(motionX, motionY, motionZ);
	}

	//--- Builder ---//

	/**
	 * Called when the projectile goes through a block or entity
	 *
	 * @param handler Block or entity's penetration handler
	 */
	protected void onHitPenetrate(RayTraceResult hit, IPenetrationHandler handler)
	{
		//Reduce velocity
		velocity *= 0.9f;
		penetrationAbility -= handler.getReduction();
	}

	/**
	 * Called when the projectile ricochets off a block or entity
	 *
	 * @param handler Block or entity's penetration handler
	 */
	protected void onHitRicochet(RayTraceResult hit, IPenetrationHandler handler)
	{
		//Reduce velocity
		velocity *= 0.6f;
		penetrationAbility -= handler.getReduction();

		//Compute the ricochet direction based on the surface normal
		Vec3d surfaceNormal = new Vec3d(hit.sideHit.getDirectionVec());
		baseMotion = baseMotion.subtract(
				surfaceNormal.scale(2*baseMotion.dotProduct(surfaceNormal))
		).normalize();

		//Clear the lists
		ignoredEntities.clear();
		ignoredPositions.clear();
	}

	/**
	 * Sets the position and velocity of the projectile
	 *
	 * @param pos              The starting position
	 * @param dir              The direction
	 * @param velocityModifier The velocity modifier
	 */
	public void setPositionAndVelocity(Vec3d pos, Vec3d dir, float velocityModifier)
	{
		this.posX = pos.x;
		this.posY = pos.y;
		this.posZ = pos.z;
		this.baseMotion = dir.normalize();
		this.velocity *= velocityModifier;
	}

	/**
	 * @param ignoredBlocks   The blocks to ignore during hit detection
	 * @param ignoredEntities The entities to ignore during hit detection
	 */
	@ParametersAreNullableByDefault
	public void setIgnored(Collection<BlockPos> ignoredBlocks, Collection<Entity> ignoredEntities)
	{
		if(ignoredBlocks!=null)
			this.ignoredPositions.addAll(ignoredBlocks);
		if(ignoredEntities!=null)
			this.ignoredEntities.addAll(ignoredEntities);
	}
}
