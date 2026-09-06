package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter.TargetEntityProperties;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter.TargetEntityType;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomaticStatus;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Lazily reads and caches properties for one target candidate.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.08.2026
 */
public class TargetEvaluationContext
{
	private static final int REGISTRY_ID = 1;
	private static final int ENTITY_TYPE = 1<<1;
	private static final int NAME = 1<<2;
	private static final int HEALTH = 1<<3;
	private static final int MAX_HEALTH = 1<<4;
	private static final int DISTANCE = 1<<5;
	private static final int ON_GROUND = 1<<6;
	private static final int IN_WATER = 1<<7;
	private static final int ON_FIRE = 1<<8;
	private static final int DIPLOMACY = 1<<9;

	@Nullable
	private World world;
	@Nullable
	private OwnerIdentity ownerIdentity;
	private Vec3d origin = Vec3d.ZERO;
	private Entity rawEntity;
	private Entity entity;
	private int computed;

	@Nullable
	private ResourceLocation registryId;
	private int entityTypeMask;
	private String name;
	private double health, maxHealth, distanceSq;
	private boolean onGround, inWater, onFire;
	private DiplomaticStatus diplomaticStatus;

	/**
	 * Sets the candidate and clears all lazily computed values.
	 */
	public TargetEvaluationContext reset(@Nullable World world, @Nullable OwnerIdentity ownerIdentity,
	                                     @Nonnull Vec3d origin, @Nonnull Entity rawEntity)
	{
		return resetNormalized(world, ownerIdentity, origin, rawEntity, TargetEntityProperties.normalize(rawEntity));
	}

	/**
	 * Sets an already normalised candidate and clears all lazily computed values.
	 */
	public TargetEvaluationContext resetNormalized(@Nullable World world, @Nullable OwnerIdentity ownerIdentity,
	                                               @Nonnull Vec3d origin, @Nonnull Entity rawEntity,
	                                               @Nonnull Entity normalizedEntity)
	{
		this.world = world;
		this.ownerIdentity = ownerIdentity;
		this.origin = origin;
		this.rawEntity = rawEntity;
		this.entity = normalizedEntity;
		this.computed = 0;
		this.registryId = null;
		this.name = "";
		return this;
	}

	@Nonnull
	public Entity getRawEntity()
	{
		return rawEntity;
	}

	@Nonnull
	public Entity getEntity()
	{
		return entity;
	}

	@Nullable
	public ResourceLocation getRegistryId()
	{
		if((computed&REGISTRY_ID)==0)
		{
			computed |= REGISTRY_ID;
			registryId = EntityList.getKey(entity);
		}
		return registryId;
	}

	@Nullable
	public String getRegistryModId()
	{
		ResourceLocation id = getRegistryId();
		return id==null?null: id.getResourceDomain();
	}

	public int getEntityTypeMask()
	{
		if((computed&ENTITY_TYPE)==0)
		{
			computed |= ENTITY_TYPE;
			entityTypeMask = TargetEntityType.getTypeMask(entity);
		}
		return entityTypeMask;
	}

	@Nonnull
	public String getName()
	{
		if((computed&NAME)==0)
		{
			computed |= NAME;
			name = entity.getName();
		}
		return name;
	}

	public double getHealth()
	{
		if((computed&HEALTH)==0)
		{
			computed |= HEALTH;
			health = TargetEntityProperties.getHealth(entity);
		}
		return health;
	}

	public double getMaxHealth()
	{
		if((computed&MAX_HEALTH)==0)
		{
			computed |= MAX_HEALTH;
			maxHealth = TargetEntityProperties.getMaxHealth(entity);
		}
		return maxHealth;
	}

	public double getDistanceSq()
	{
		if((computed&DISTANCE)==0)
		{
			computed |= DISTANCE;
			double dx = entity.posX-origin.x;
			double dy = entity.posY+entity.height*0.5-origin.y;
			double dz = entity.posZ-origin.z;
			distanceSq = dx*dx+dy*dy+dz*dz;
		}
		return distanceSq;
	}

	public boolean isOnGround()
	{
		if((computed&ON_GROUND)==0)
		{
			computed |= ON_GROUND;
			onGround = entity.onGround;
		}
		return onGround;
	}

	public boolean isInWater()
	{
		if((computed&IN_WATER)==0)
		{
			computed |= IN_WATER;
			inWater = entity.isInWater();
		}
		return inWater;
	}

	public boolean isOnFire()
	{
		if((computed&ON_FIRE)==0)
		{
			computed |= ON_FIRE;
			onFire = entity.isBurning();
		}
		return onFire;
	}

	@Nonnull
	public DiplomaticStatus getDiplomaticStatus()
	{
		if((computed&DIPLOMACY)==0)
		{
			computed |= DIPLOMACY;
			diplomaticStatus = ownerIdentity!=null&&entity instanceof EntityLivingBase?
					ownerIdentity.getRelationTowards((EntityLivingBase)entity): DiplomaticStatus.NEUTRAL;
		}
		return diplomaticStatus;
	}

	@Nullable
	public World getWorld()
	{
		return world;
	}
}
