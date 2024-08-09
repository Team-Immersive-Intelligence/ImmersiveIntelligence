package pl.pabilo8.immersiveintelligence.common.entity.ammo.types;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.HitEffect;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.PenetrationCache;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.FactoryTracer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 02.02.2024
 */
public class EntityAmmoProjectile extends EntityAmmoBase<EntityAmmoProjectile>
{
	//--- Constants ---//
	/**
	 * Tick limit for the projectile, used for decay
	 */
	public static int MAX_TICKS = 600;
	/**
	 * The drag coefficient of the projectile, used for calculating the motion vector
	 */
	public static float DRAG;
	/**
	 * The gravity coefficient of the projectile, used for calculating the motion vector
	 */
	public static float GRAVITY;
	/**
	 * The slowmo multiplier for projectile motion
	 */
	public static float SLOWMO;

	static
	{
		setSlowmo(1);
	}

	/**
	 * Modifier for enemy armor effectiveness, used for calculating damage
	 */
	public static final float ARMOR_FACTOR = 0.3f;
	/**
	 * Modifier for enemy armor toughness, used for calculating penetration
	 */
	public static final float TOUGHNESS_FACTOR = 0.45f;

	//--- Properties ---//
	/**
	 * The base motion of the bullet, used for calculating the motion vector
	 */
	@SyncNBT(events = SyncEvents.ENTITY_COLLISION)
	public Vec3d baseMotion = Vec3d.ZERO;
	/**
	 * Gravity motion of the bullet, used for calculating the motion vector
	 */
	@SyncNBT(events = SyncEvents.ENTITY_COLLISION)
	public double gravityMotionY = 0;
	/**
	 * The velocity of the bullet, in blocks per tick
	 */
	@SyncNBT(events = SyncEvents.ENTITY_COLLISION)
	public float velocity;
	/**
	 * The mass of the bullet, used for calculating the motion vector
	 */
	protected double mass;

	//--- Penetration System Properties ---//
	/**
	 * The penetration ability of the projectile, in blocks<br>
	 * The penetration hardness of a projectile, uses block hardness values
	 */
	@SyncNBT(events = SyncEvents.ENTITY_COLLISION)
	public float penetrationDepth;
	@SyncNBT(events = SyncEvents.ENTITY_COLLISION)
	public PenetrationHardness penetrationHardness;

	/**
	 * Blocks and Entities to ignore during hit detection
	 */
	protected Set<Entity> ignoredEntities = new HashSet<>();
	protected Set<BlockPos> ignoredPositions = new HashSet<>();
	/**
	 * Raytracer used for the projectile
	 */
	@Nonnull
	protected FactoryTracer flightTracer;

	/**
	 * Once true, the bullet will detonate at the end of the tick
	 */
	protected boolean markedForDetonation = false;
	private float velocityModifier;

	public EntityAmmoProjectile(World world)
	{
		super(world);
	}

	@Override
	protected boolean shouldDecay()
	{
		return ticksExisted > MAX_TICKS||posY < 0;
	}

	//--- Getters ---//

	@Override
	@Nonnull
	protected Vec3d getDirection()
	{
		return baseMotion;
	}

	public float getDamage()
	{
		return ammoType.getDamage()*coreType.getDamageMod()*core.getDamageModifier()*velocityModifier;
	}

	public float getVelocity()
	{
		return velocity;
	}

	//--- Update ---//

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		//Yep, that's it, that's the entire motion code
		updatePhysics();
		updateVisibleMotion();

		//handle timed fuse
		if(fuseType==FuseType.TIMED)
			if(fuseParameter-- <= 0)
				detonate();

		//do movement specific to the projectile type
		doProjectileMotion();

		//set rotations based on motion
		doRotations();

		//spawn trail
		if(world.isRemote&&!isDead)
			spawnTrailParticles();
		//detonate if marked
		if(markedForDetonation)
			finallyDetonate();
	}

	/**
	 * Sets the rotation of the projectile based on its motion vector
	 */
	protected void doRotations()
	{
		Vec3d normalized = new Vec3d(motionX, motionY, motionZ).normalize();
		float motionXZ = MathHelper.sqrt(normalized.x*normalized.x+normalized.z*normalized.z);
		this.rotationYaw = (float)((Math.atan2(normalized.x, normalized.z)*180D)/Math.PI);
		this.rotationPitch = -(float)((Math.atan2(normalized.y, motionXZ)*180D)/Math.PI);
	}

	/**
	 * Updates the motion of the projectile
	 */
	protected void doProjectileMotion()
	{
		//if velocity is fully lost, perform a raytracer based motion or a simple gravity motion with collision check
		if(velocity > 0)
		{
			//Volumetric raytrace through the projectile's flight path
			RayTraceResult traceResult = flightTracer.stepTrace(world, getPositionVector(), getNextPositionVector(), hit -> {
				if(hit!=null)
				{
					//Proximity fuses don't use penetration logic
					if(shouldDetonateAfterContact())
					{
						detonate();
						return true;
					}

					//But other fuses do
					switch(hit.typeOfHit)
					{
						case BLOCK:
							return handleBlockDamage(hit);
						case ENTITY:
							return handleEntityDamage(hit);
					}
				}
				return false;
			});

			//When a bullet hits something, iteration stops and the motionInterrupt becomes its final motion vector
			if(traceResult!=null)
			{
				Vec3d motionInterrupt = traceResult.hitVec.subtract(posX, posY, posZ);
				motionX = motionInterrupt.x-Math.signum(motionInterrupt.x)*width;
				motionY = motionInterrupt.y-Math.signum(motionInterrupt.y)*height;
				motionZ = motionInterrupt.z-Math.signum(motionInterrupt.z)*width;
			}
		}

		//Finalize the motion
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
	}

	/**
	 * @return Whether the projectile should detonate after hitting a target
	 */
	protected boolean shouldDetonateAfterContact()
	{
		return fuseType==FuseType.PROXIMITY||penetrationDepth <= 0;
	}

	/**
	 * Updates the velocity and gravity motion of the projectile
	 */
	protected void updatePhysics()
	{
		velocity -= DRAG*velocity;
		if(velocity < 0.00001)
			velocity = 0;
		gravityMotionY -= GRAVITY*this.mass*SLOWMO;
		gravityMotionY *= 1d-DRAG;
	}

	@Override
	public void detonate()
	{
		markedForDetonation = true;
	}

	protected void finallyDetonate()
	{
		if(!world.isRemote)
		{
			//Get the values
			Vec3d dir = getDirection();
			Vec3d pos = new Vec3d(posX, posY, posZ);
			float multiplier = getComponentMultiplier();

			//Get the NBT of the bullet
			NBTTagCompound tag = new NBTTagCompound();
			writeEntityToNBT(tag);

			//Call the effect method on all components
			for(Tuple<AmmoComponent, NBTTagCompound> component : components)
				component.getFirst().onEffect(world, pos, dir,
						coreType.getEffectShape(), component.getSecond(), ammoType.getComponentMultiplier(), multiplier, owner);
			setDead();
		}
	}

	protected final void updateVisibleMotion()
	{
		this.motionX = baseMotion.x*velocity;
		this.motionY = baseMotion.y*velocity+gravityMotionY;
		this.motionZ = baseMotion.z*velocity;
//		markVelocityChanged();
	}

	protected void spawnTrailParticles()
	{
		for(Tuple<AmmoComponent, NBTTagCompound> component : components)
			component.getFirst().spawnParticleTrail(this, component.getSecond());
	}

	/**
	 * Attemps to damage a hit block
	 *
	 * @param hit hit result
	 * @return whether movement direction should be changed
	 */
	protected boolean handleBlockDamage(RayTraceResult hit)
	{
		BlockPos pos = hit.getBlockPos();
		ignoredPositions.add(pos);

		//Only calculate on server side
		if(penetrationDepth <= 0)
			return false;

		IBlockState state = world.getBlockState(pos);
		IPenetrationHandler penHandler = PenetrationRegistry.getPenetrationHandler(state);
		PenetrationHardness blockHardness = penHandler.getPenetrationHardness();
		boolean canPenetrate = penetrationHardness.compareTo(blockHardness) > 0;

		//ricochet if the block is unbreakable or the projectile can't penetrate it
		if(blockHardness!=PenetrationHardness.BEDROCK&&penHandler.canRicochet()&&!canPenetrate)
		{
			onHitRicochet(hit, penHandler);
			return true;
		}
		else //go through and damage the block
		{
			onHitPenetrate(hit, penHandler);
			if(!canPenetrate)
			{
				detonate();
				return true;
			}

			if(world.isRemote)
				return false;

			//don't damage fluids
			if(!state.getMaterial().isLiquid())
				PenetrationCache.dealBlockDamage(world, getDirection(), getDamage(), pos, penHandler);

		}
		return false;
	}

	/**
	 * Attemps to damage a hit entity
	 *
	 * @param hit hit result
	 * @return whether movement direction should be changed
	 */
	protected boolean handleEntityDamage(RayTraceResult hit)
	{
		ignoredEntities.add(hit.entityHit);
		if(hit.entityHit==this) //can't touch this
			return false;
		Entity other = hit.entityHit;

		//Shoot down another bullet
		if(other instanceof EntityAmmoBase)
		{
			if(((EntityAmmoBase<?>)other).getOwner()==this.owner)
				return false;
			((EntityAmmoBase<?>)other).detonate();
			this.detonate();
			return true;
		}

		//Collide with any other entity
		IPenetrationHandler penHandler = PenetrationRegistry.getPenetrationHandler(other);

		//Damage entity armor
		if(other instanceof EntityLivingBase)
		{
			float armor = MathHelper.floor(((EntityLivingBase)other).getEntityAttribute(SharedMonsterAttributes.ARMOR).getAttributeValue())*ARMOR_FACTOR;
			//Damage the other entity armour whether penetrated or not
			if(armor > 0)
				IIAmmoUtils.breakArmour(other, (int)getDamage());
		}
		//Ricochet off the entity
		if(penHandler.getPenetrationHardness().compareTo(penetrationHardness) > 0)
			onHitRicochet(hit, penHandler);

		onHitPenetrate(hit, penHandler);
		//If entity can't be damaged, detonate the projectile
		if(!other.attackEntityFrom(IIDamageSources.causeBulletDamage(this, other), getDamage()))
		{
			detonate();
			return true;
		}
		other.hurtResistantTime = 0;
		if(other instanceof EntityLivingBase)
			((EntityLivingBase)other).maxHurtTime = 0;

		return false;
	}

	/**
	 * @return the position the projectile will be at in the next tick
	 */
	public final Vec3d getNextPositionVector()
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
		penetrationDepth -= handler.getThickness();

		//Play ricochet sound
		SoundEvent sound = handler.getSpecialSound(HitEffect.IMPACT);
		if(sound!=null)
			playSound(sound, 0.5f, 1f);
	}

	/**
	 * Called when the projectile ricochets off a block or entity
	 *
	 * @param handler Block or entity's penetration handler
	 */
	protected void onHitRicochet(RayTraceResult hit, IPenetrationHandler handler)
	{
		//If ricochets are disabled, end the bullet's lifecycle
		if(!IIAmmoUtils.ammoRicochets)
		{
			detonate();
			return;
		}

		//Set the position to the hit position
		this.posX = hit.hitVec.x;
		this.posY = hit.hitVec.y;
		this.posZ = hit.hitVec.z;

		//Reduce velocity
		velocity *= 0.6f;
		penetrationDepth -= handler.getThickness()*2;
		gravityMotionY = 0;

		if(velocity <= 0)
			return;

		//If sideHit is missing, the hit surface should be a facing opposite to the bullet direction
//		if(hit.sideHit==null)
		hit.sideHit = EnumFacing.getFacingFromVector((float)baseMotion.x, (float)baseMotion.y, (float)baseMotion.z).getOpposite();

		//Compute the ricochet direction based on the surface normal
		Vec3d surfaceNormal = new Vec3d(hit.sideHit.getDirectionVec());
		baseMotion = baseMotion.subtract(
				surfaceNormal.scale(2*baseMotion.dotProduct(surfaceNormal))
		).normalize();
		updateEntityForEvent(SyncEvents.ENTITY_COLLISION);

		//Clear the lists
		ignoredEntities.clear();
		ignoredPositions.clear();

		//Play impact sound
		SoundEvent sound = handler.getSpecialSound(HitEffect.RICOCHET);
		if(sound!=null)
			playSound(sound, 0.5f, 1f);
	}

	//--- Setters ---//

	/**
	 * @param owner The owner of this bullet, used for statistics and hit detection
	 */
	@Override
	public void setOwner(@Nullable Entity owner)
	{
		super.setOwner(owner);
		this.ignoredEntities.add(owner);
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
		this.velocityModifier = velocityModifier;
		markVelocityChanged();
	}

	@ParametersAreNonnullByDefault
	@Override
	public void setFromParameters(IAmmoType<?, EntityAmmoProjectile> ammoType, AmmoCore core, CoreType coreType, FuseType fuseType, int fuseParameter, List<Tuple<AmmoComponent, NBTTagCompound>> components)
	{
		super.setFromParameters(ammoType, core, coreType, fuseType, fuseParameter, components);
		this.velocity = ammoType.getVelocity()*SLOWMO;
		this.mass = ammoType.getCoreMass(core, components.stream().map(Tuple::getFirst).toArray(AmmoComponent[]::new));
		this.penetrationDepth = IIAmmoUtils.getCombinedDepth(ammoType, coreType);
		this.penetrationHardness = IIAmmoUtils.getCombinedHardness(core, coreType);
		this.flightTracer = FactoryTracer.create(aabb.grow(0.125)).setFilters(ignoredEntities, ignoredPositions);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		EasyNBT nbt = EasyNBT.wrapNBT(compound);

		this.baseMotion = nbt.getVec3d("base_motion");
		this.ignoredEntities = nbt.streamList(NBTTagInt.class, "ignored_entities", EasyNBT.TAG_INT)
				.map(NBTTagInt::getInt)
				.map(world::getEntityByID)
				.collect(Collectors.toSet());
		this.ignoredPositions = nbt.streamList(NBTTagIntArray.class, "ignored_pos", EasyNBT.TAG_INT_ARRAY)
				.map(NBTTagIntArray::getIntArray)
				.filter(t -> t.length==3)
				.map(t -> new BlockPos(t[0], t[1], t[2]))
				.collect(Collectors.toSet());
		this.flightTracer.setFilters(ignoredEntities, ignoredPositions);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		EasyNBT nbt = EasyNBT.wrapNBT(compound);
		nbt.withVec3d("base_motion", baseMotion);
		if(ignoredEntities!=null&&!ignoredEntities.isEmpty())
			nbt.withList("ignored_entities", e -> new NBTTagInt(e.getEntityId()), ignoredEntities);
		if(ignoredPositions!=null&&!ignoredPositions.isEmpty())
			nbt.withList("ignored_pos", e -> new NBTTagIntArray(new int[]{e.getX(), e.getY(), e.getZ()}), ignoredPositions);
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

	//--- Static Methods ---//

	/**
	 * Sets the slowmo coefficient for all projectiles
	 *
	 * @param newSlowmo The new slowmo coefficient
	 */
	public static void setSlowmo(float newSlowmo)
	{
		//Apply new values
		SLOWMO = newSlowmo;
		GRAVITY = 0.1f*SLOWMO;
		DRAG = 0.01f*SLOWMO;
	}

}
