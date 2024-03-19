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
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.IIPenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.IIPenetrationRegistry.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.HitEffect;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenMaterialTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.MultipleRayTracer;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.MultipleRayTracer.MultipleTracerBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
	public static float DRAG = 0.01f;
	/**
	 * The gravity coefficient of the projectile, used for calculating the motion vector
	 */
	public static float GRAVITY = 0.15f;
	/**
	 * The slowmo multiplier for projectile motion
	 */
	public static float SLOWMO = 1;
	/**
	 * The threshold for minimal hardness difference proportion that makes a projectile ricochet.
	 *
	 * @implNote For example a projectile with hardness 3 and threshold 1.66 will need to hit a surface having hardness of at least 5 to ricochet
	 */
	private static final float RICOCHET_THRESHOLD = 1.66f;

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
	 * The mass of the bullet, used for calculating the motion vector
	 */
	protected float mass;

	//--- Penetration System Properties ---//
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
	/**
	 * Once true, the bullet will detonate at the end of the tick
	 */
	protected boolean markedForDetonation = false;

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

	public float getDamage(PenMaterialTypes materialHit)
	{
		return ammoType.getDamage()*coreType.getDamageMod(materialHit)*core.getDamageModifier();
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
		setMotion();

		//handle timed fuse
		if(fuseType==EnumFuseTypes.TIMED)
			if(fuseParameter-- <= 0)
				detonate();

		AxisAlignedBB aabb = this.getEntityBoundingBox()
				.grow(fuseType==EnumFuseTypes.PROXIMITY?fuseParameter: 0);
		if(world.isRemote)
			aabb.offset(this.getPositionVector().scale(-1));

		//TODO: 18.03.2024 make a new raytracer that parses entities dynamically at can be stopped at a point
		//Volumetric raytrace through the projectile's flight path
		MultipleRayTracer tracer = MultipleTracerBuilder.setPos(world,
						this.getPositionVector(), this.getNextPositionVector())
				.setAABB(aabb)
				.setFilters(this.ignoredEntities, this.ignoredPositions)
				.volumetricTrace();

		//When a bullet hits something, iteration stops and the motionInterrupt becomes its final motion vector
		Vec3d motionInterrupt = null;
		for(RayTraceResult hit : tracer)
		{
			if(markedForDetonation||motionInterrupt!=null)
				break;

			if(hit!=null)
			{
				//Proximity fuses don't use penetration logic
				if(fuseType==EnumFuseTypes.PROXIMITY||penetrationAbility <= 0)
				{
					detonate();
					break;
				}

				switch(hit.typeOfHit)
				{
					case BLOCK:
						if(handleBlockDamage(hit))
							motionInterrupt = hit.hitVec.subtract(posX, posY, posZ);
						break;
					case ENTITY:
						if(handleEntityDamage(hit))
							motionInterrupt = hit.hitVec.subtract(posX, posY, posZ);
						break;
				}
			}
		}

		if(motionInterrupt!=null)
		{
			motionX = motionInterrupt.x;
			motionY = motionInterrupt.y;
			motionZ = motionInterrupt.z;
		}

		posX += motionX;
		posY += motionY;
		posZ += motionZ;

		//set rotations based on motion
		Vec3d normalized = new Vec3d(motionX, motionY, motionZ).normalize();
		float motionXZ = MathHelper.sqrt(normalized.x*normalized.x+normalized.z*normalized.z);
		this.rotationYaw = (float)((Math.atan2(normalized.x, normalized.z)*180D)/Math.PI);
		this.rotationPitch = -(float)((Math.atan2(normalized.y, motionXZ)*180D)/Math.PI);

		//spawn trail
		if(world.isRemote&&!isDead)
			spawnTrailParticles();
		//detonate if marked
		if(markedForDetonation)
			finallyDetonate();
	}

	/**
	 * Updates the velocity and gravity motion of the projectile
	 */
	protected void updatePhysics()
	{
		velocity -= DRAG*velocity;
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
				component.getFirst().onEffect(world, pos, dir, multiplier, component.getSecond(), coreType, owner);
			setDead();
		}
	}

	private void setMotion()
	{
		this.motionX = baseMotion.x*velocity;
		this.motionY = baseMotion.y*velocity+gravityMotionY;
		this.motionZ = baseMotion.z*velocity;
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
		if(penetrationAbility <= 0)
			return false;

		IBlockState state = world.getBlockState(pos);
		IPenetrationHandler penHandler = IIPenetrationRegistry.getPenetrationHandler(state);
		PenMaterialTypes penType = penHandler.getPenetrationType();

		float ammoHardness = penetrationHardness*coreType.getPenMod(penType);
		float blockHardness = state.getBlockHardness(world, pos);

		//ricochet if the block is unbreakable or the projectile can't penetrate it
		if(blockHardness==-1||blockHardness > ammoHardness*RICOCHET_THRESHOLD)
		{
			onHitRicochet(hit, penHandler);
			return true;
		}
		else //go through and damage the block
		{
			onHitPenetrate(hit, penHandler);
			if(world.isRemote)
				return false;

			//don't damage fluids
			if(!state.getMaterial().isLiquid())
				IIAmmoUtils.dealBlockDamage(world, getDirection(), getDamage(penType), pos, penHandler);

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
		float armor = 0, toughness = 0;
		IPenetrationHandler penHandler = IIPenetrationRegistry.getPenetrationHandler(other);
		//Type of material penetrated
		PenMaterialTypes penType = penHandler.getPenetrationType();

		//Damage entity armor
		if(other instanceof EntityLivingBase)
		{
			armor = MathHelper.floor(((EntityLivingBase)other).getEntityAttribute(SharedMonsterAttributes.ARMOR).getAttributeValue())*ARMOR_FACTOR;
			toughness += MathHelper.floor(((EntityLivingBase)other).getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue())*TOUGHNESS_FACTOR;

			//Damage the hit other entity armour whether penetrated or not
			if(toughness > 0||armor > 0)
				IIAmmoUtils.breakArmour(other, (int)getDamage(penType));
		}

		//The damage actually dealt to the entity, uses only armor, as toughness is used for penetration hardness
		float damageReceived = Math.max(0, getDamage(penType)-armor*
				MathHelper.clamp(toughness-penetrationHardness, 0, 1)
		);

		//Ricochet off the entity
		if(toughness > penetrationHardness)
			onHitRicochet(hit, penHandler);
			//Can't damage the entity, but can penetrate it for some reason... which is weird, so just detonate the bullet and let's call it a day
		else if(damageReceived==0)
		{
			detonate();
			return true;
		}
		else //Penetrate and damage the entity
		{
			onHitPenetrate(hit, penHandler);
			//If entity can't be damaged, detonate the projectile
			if(!other.attackEntityFrom(IIDamageSources.causeBulletDamage(this, other), damageReceived))
			{
				detonate();
				return true;
			}
		}
		return false;
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

		//Reduce velocity
		velocity *= 0.6f;
		penetrationAbility -= handler.getReduction()*2;

		//If sideHit is missing, the hit surface should be a facing opposite to the bullet direction
		if(hit.sideHit==null)
			hit.sideHit = EnumFacing.getFacingFromVector((float)baseMotion.x, (float)baseMotion.y, (float)baseMotion.z).getOpposite();

		//Compute the ricochet direction based on the surface normal
		Vec3d surfaceNormal = new Vec3d(hit.sideHit.getDirectionVec());
		baseMotion = baseMotion.subtract(
				surfaceNormal.scale(2*baseMotion.dotProduct(surfaceNormal))
		).normalize();

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
	}

	@ParametersAreNonnullByDefault
	@Override
	public void setFromParameters(IAmmoType<?, EntityAmmoProjectile> ammoType, AmmoCore core, EnumCoreTypes coreType, EnumFuseTypes fuseType, int fuseParameter, List<Tuple<AmmoComponent, NBTTagCompound>> components)
	{
		super.setFromParameters(ammoType, core, coreType, fuseType, fuseParameter, components);
		this.velocity = ammoType.getDefaultVelocity()*SLOWMO;
		this.mass = ammoType.getCoreMass(core, components.stream().map(Tuple::getFirst).toArray(AmmoComponent[]::new));
		this.penetrationAbility = ammoType.getPenetrationDepth();
		this.penetrationHardness = core.getPenetrationHardness();
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
				.collect(Collectors.toCollection(ArrayList::new));
		this.ignoredPositions = nbt.streamList(NBTTagIntArray.class, "ignored_pos", EasyNBT.TAG_INT_ARRAY)
				.map(NBTTagIntArray::getIntArray)
				.filter(t -> t.length==3)
				.map(t -> new BlockPos(t[0], t[1], t[2]))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		EasyNBT nbt = EasyNBT.wrapNBT(compound);
		nbt.withVec3d("base_motion", baseMotion);
		if(ignoredEntities!=null)
			nbt.withList("ignored_entities", e -> new NBTTagInt(e.getEntityId()), ignoredEntities);
		if(ignoredPositions!=null)
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
		GRAVITY = 0.15f*SLOWMO;
		DRAG = 0.01f*SLOWMO;
	}

}
