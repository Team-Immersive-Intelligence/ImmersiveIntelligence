package pl.pabilo8.immersiveintelligence.common.entity.ammo.component;

import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler.ChemthrowerEffect;
import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.IEntityLightEventConsumer;
import com.elytradev.mirage.lighting.Light;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.PenetrationCache;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.fx.particles.AbstractParticle;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Overrides.Chemthrower;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.entity.ISyncNBTEntity;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.FactoryTracer;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.util.*;

/**
 * Expansion of the IE's Chemthrower.
 * Originally created due to forced Albedo compat on IE's side.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @updated 14.08.2026
 * @since 08.07.2023
 */
@Interface(iface = "com.elytradev.mirage.lighting.IEntityLightEventConsumer", modid = "mirage")
public class EntityIIChemthrowerShot extends Entity implements ISyncNBTEntity<EntityIIChemthrowerShot>, IEntityLightEventConsumer
{
	private final Set<Entity> ignoredEntities = new HashSet<>();
	private final Set<BlockPos> ignoredPositions = new HashSet<>();
	private final FactoryTracer flightTracer = FactoryTracer.create(
			new AxisAlignedBB(-Chemthrower.shotEntitySize, -Chemthrower.shotEntitySize, -Chemthrower.shotEntitySize, Chemthrower.shotEntitySize, Chemthrower.shotEntitySize, Chemthrower.shotEntitySize)
	).setFilters(ignoredEntities, ignoredPositions);

	@Getter
	@SyncNBT(events = {SyncEvents.ENTITY_CUSTOM1}, nullable = true)
	public FluidStack fluidStack;
	@SyncNBT(events = {SyncEvents.ENTITY_CUSTOM1})
	public int tickLimit = Chemthrower.chemthrowerShotLifetime;
	@SyncNBT(nullable = true)
	public UUID shooterUUID;
	@SyncNBT(events = {SyncEvents.ENTITY_CUSTOM1, SyncEvents.ENTITY_COLLISION})
	public boolean ignited;
	@SyncNBT(events = {SyncEvents.ENTITY_COLLISION})
	public boolean stopped;
	@Getter
	@SyncNBT(events = {SyncEvents.ENTITY_COLLISION})
	public Vec3d endPosition = Vec3d.ZERO;
	@Getter
	private EntityLivingBase shooter;
	private int stoppedTicks;

	public EntityIIChemthrowerShot(World world)
	{
		super(world);
		setSize(Chemthrower.shotEntitySize, Chemthrower.shotEntitySize);
		ignoredEntities.add(this);
	}

	public EntityIIChemthrowerShot(World world, double x, double y, double z,
	                               double lookX, double lookY, double lookZ, FluidStack fluidStack)
	{
		this(world);
		setLocationAndAngles(x, y, z, rotationYaw, rotationPitch);
		setPosition(x, y, z);
		withMotion(lookX, lookY, lookZ);
		this.fluidStack = fluidStack==null?null: fluidStack.copy();
	}

	public EntityIIChemthrowerShot(World world, EntityLivingBase shooter,
	                               double lookX, double lookY, double lookZ, FluidStack fluidStack)
	{
		this(world, shooter.posX, shooter.posY+shooter.getEyeHeight(), shooter.posZ, lookX, lookY, lookZ, fluidStack);
		rotationYaw = shooter.rotationYaw;
		rotationPitch = shooter.rotationPitch;
		this.shooter = shooter;
		shooterUUID = shooter.getUniqueID();
		ignoredEntities.add(shooter);
	}

	/**
	 * Adds entities that the server collision trace must ignore.
	 */
	public EntityIIChemthrowerShot withShooters(Entity... entities)
	{
		ignoredEntities.addAll(Arrays.asList(entities));
		return this;
	}

	/**
	 * Adds block positions that the server collision trace must ignore.
	 */
	public EntityIIChemthrowerShot withShooters(Collection<BlockPos> blockPos)
	{
		ignoredPositions.addAll(blockPos);
		return this;
	}

	/**
	 * Adds block positions that the server collision trace must ignore.
	 */
	public EntityIIChemthrowerShot withShooters(BlockPos... blockPos)
	{
		ignoredPositions.addAll(Arrays.asList(blockPos));
		return this;
	}

	/**
	 * Sets projectile motion.
	 */
	public EntityIIChemthrowerShot withMotion(double x, double y, double z)
	{
		motionX = x;
		motionY = y;
		motionZ = z;
		return this;
	}

	/**
	 * Sets projectile motion.
	 */
	public EntityIIChemthrowerShot withMotion(Vec3d vecDir)
	{
		return withMotion(vecDir.x, vecDir.y, vecDir.z);
	}

	/**
	 * Sets the maximum projectile lifetime.
	 */
	public EntityIIChemthrowerShot withTickLimit(int limit)
	{
		tickLimit = Math.max(1, limit);
		if(!world.isRemote&&ticksExisted > 0)
			updateEntityForEvent(SyncEvents.ENTITY_CUSTOM1);
		return this;
	}

	//--- NBT ---//

	@Override
	@SideOnly(Side.CLIENT)
	public void receiveNBTMessageClient(NBTTagCompound nbt)
	{
		ISyncNBTEntity.super.receiveNBTMessageClient(nbt);
		if(stopped)
			applyStopState();
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		ISyncNBTEntity.super.readEntityFromNBT(compound);
		if(stopped)
			applyStopState();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		ISyncNBTEntity.super.writeEntityToNBT(compound);
	}

	//--- Update ---//

	@Override
	protected void entityInit()
	{

	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if(isDead)
			return;

		if(stopped)
		{
			//Stop the projectile on client if it's stopped on server
			applyStopState();
			if(!world.isRemote&&++stoppedTicks >= Chemthrower.shotRemainingTicks)
				setDead();
			return;
		}


		if(world.isRemote)
		{
			//Update on client, no colision simulation
			Vec3d start = getPositionVector();
			moveProjectile();
			spawnTraceParticle(start, getPositionVector());
			return;
		}

		//Server-side update
		resolveShooter();
		if(ticksExisted >= tickLimit)
		{
			stopAt(getPositionVector());
			return;
		}

		Vec3d start = getPositionVector();
		Vec3d end = start.addVector(motionX, motionY, motionZ);
		//Trace the collision path and react
		RayTraceResult collision = flightTracer.stepTrace(world, start, end, this::handleTraceHit);
		//Stop projectile on first valid hit
		if(collision==null&&!stopped)
			moveProjectile();

		//Ignite the block projectile is landed at
		updateIgnition();
	}

	private boolean handleTraceHit(RayTraceResult hit)
	{
		if(hit==null||hit.typeOfHit==null||hit.typeOfHit==Type.MISS)
			return false;

		return switch(hit.typeOfHit)
		{
			case ENTITY -> handleEntityCollision(hit);
			case BLOCK -> handleBlockCollision(hit);
			default -> false;
		};
	}

	private boolean handleEntityCollision(RayTraceResult hit)
	{
		Entity target = hit.entityHit;
		if(target==null||ignoredEntities.contains(target))
			return false;

		if(shooter instanceof EntityPlayer&&target instanceof EntityPlayer&&
				!((EntityPlayer)shooter).canAttackPlayer((EntityPlayer)target))
		{
			ignoredEntities.add(target);
			return false;
		}

		if(!ignited&&canIgnite()&&target.isBurning())
			setFire(3);
		handleFluidImpact(hit, fluidStack);
		stopAt(getHitPosition(hit));
		return true;
	}

	private boolean handleBlockCollision(RayTraceResult hit)
	{
		BlockPos pos = hit.getBlockPos();
		if(ignoredPositions.contains(pos))
			return false;

		IBlockState state = world.getBlockState(pos);
		handleFluidImpact(hit, fluidStack);
		if(state.getCollisionBoundingBox(world, pos)==Block.NULL_AABB)
			return false;

		Vec3d hitPosition = getHitPosition(hit);
		Vec3d direction = new Vec3d(motionX, motionY, motionZ);
		double length = direction.lengthVector();
		if(length > 1e-6)
			hitPosition = hitPosition.subtract(direction.scale(0.05d/length));

		stopAt(hitPosition);
		if(state.getMaterial()!=Material.AIR)
			state.getBlock().onEntityCollidedWithBlock(world, pos, state, this);
		return true;
	}

	private Vec3d getHitPosition(RayTraceResult hit)
	{
		return hit.hitVec==null?getPositionVector(): hit.hitVec;
	}

	private void stopAt(Vec3d position)
	{
		if(stopped)
			return;

		stopped = true;
		endPosition = position;
		applyStopState();
		updateEntityForEvent(SyncEvents.ENTITY_COLLISION);
	}

	private void applyStopState()
	{
		setPosition(endPosition.x, endPosition.y, endPosition.z);
		motionX = 0;
		motionY = 0;
		motionZ = 0;
	}

	private void moveProjectile()
	{
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		updateRotation();

		float decay = 0.99f;
		if(isInWater())
			decay *= 0.8f;
		motionX *= decay;
		motionY *= decay;
		motionZ *= decay;
		motionY -= getGravity();

		setPosition(posX, posY, posZ);
	}

	private void updateRotation()
	{
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
		float[] rotation = IIMath.getRotationFromVector(motionX, motionY, motionZ);
		this.rotationYaw = MathHelper.wrapDegrees(rotation[0]);
		this.rotationPitch = rotation[1];
	}

	private void resolveShooter()
	{
		if(shooter!=null||shooterUUID==null)
			return;

		for(Entity entity : world.loadedEntityList)
			if(entity instanceof EntityLivingBase&&shooterUUID.equals(entity.getUniqueID()))
			{
				shooter = (EntityLivingBase)entity;
				ignoredEntities.add(entity);
				return;
			}
	}

	private void updateIgnition()
	{
		if(!canIgnite()||ignited)
			return;

		Material material = world.getBlockState(new BlockPos(posX, posY, posZ)).getMaterial();
		if(material==Material.FIRE||material==Material.LAVA)
			setFire(6);
	}

	/**
	 * Gets the gravity applied to the projectile.
	 */
	public double getGravity()
	{
		if(fluidStack==null||fluidStack.getFluid()==null)
			return 0.05f;

		Fluid fluid = fluidStack.getFluid();
		boolean gas = fluid.isGaseous(fluidStack)||ChemthrowerHandler.isGas(fluid);
		return (gas?0.025f: 0.05f)*(fluid.getDensity(fluidStack) < 0?-1: 1);
	}

	/**
	 * Checks if the projectile fluid can ignite.
	 */
	public boolean canIgnite()
	{
		return ChemthrowerHandler.isFlammable(fluidStack==null?null: fluidStack.getFluid());
	}

	@Override
	public void setFire(int seconds)
	{
		if(!canIgnite())
			return;

		super.setFire(seconds);
		if(world.isRemote)
			return;

		boolean changed = !ignited;
		ignited = true;
		if(changed&&ticksExisted > 0)
			updateEntityForEvent(SyncEvents.ENTITY_CUSTOM1);
	}

	private void handleFluidImpact(RayTraceResult hit, FluidStack fluidStack)
	{
		if(fluidStack==null||fluidStack.getFluid()==null)
			return;

		Fluid fluid = fluidStack.getFluid();
		ChemthrowerEffect effect = ChemthrowerHandler.getEffect(fluid);
		boolean hotFluid = fluid.getTemperature(fluidStack) > 1000;

		if(effect!=null)
			applyChemthrowerEffect(hit, fluidStack, effect);
		else if(hit.entityHit!=null&&fluid.getTemperature(fluidStack) > 500)
		{
			int damage = Math.abs(fluid.getTemperature(fluidStack)-300)/500;
			if(hit.entityHit.attackEntityFrom(DamageSource.LAVA, damage))
				hit.entityHit.hurtResistantTime = (int)(hit.entityHit.hurtResistantTime*0.75f);
		}

		if(hit.typeOfHit==Type.BLOCK&&ignited)
			applyBurningBlockImpact(hit);

		if(hit.entityHit!=null)
		{
			int fireTime = ignited?fire: hotFluid?3: 0;
			if(fireTime > 0)
			{
				hit.entityHit.setFire(fireTime);
				if(hit.entityHit.attackEntityFrom(DamageSource.IN_FIRE, 2))
					hit.entityHit.hurtResistantTime = (int)(hit.entityHit.hurtResistantTime*0.75f);
			}
		}
	}

	private void applyChemthrowerEffect(RayTraceResult hit, FluidStack fluidStack, ChemthrowerEffect effect)
	{
		EntityPlayer shooter = this.shooter instanceof EntityPlayer?(EntityPlayer)this.shooter: null;
		ItemStack thrower = shooter==null?ItemStack.EMPTY: shooter.getHeldItem(EnumHand.MAIN_HAND);

		if(hit.typeOfHit==Type.ENTITY&&hit.entityHit instanceof EntityLivingBase)
			effect.applyToEntity((EntityLivingBase)hit.entityHit, shooter, thrower, fluidStack);
		else if(hit.typeOfHit==Type.BLOCK)
			effect.applyToBlock(world, hit, shooter, thrower, fluidStack);
	}

	private void applyBurningBlockImpact(RayTraceResult hit)
	{
		BlockPos pos = hit.getBlockPos();
		IBlockState state = world.getBlockState(pos);
		IPenetrationHandler penetration = PenetrationRegistry.getPenetrationHandler(state);
		if(PenetrationCache.dealBlockBurnDamage(world, Chemthrower.shotBlockBurnDamage, pos, penetration))
			return;

		EnumFacing side = hit.sideHit==null?EnumFacing.UP: hit.sideHit;
		BlockPos firePos = pos.offset(side);
		if(!world.isOutsideBuildHeight(firePos)&&world.isAirBlock(firePos)&&Blocks.FIRE.canPlaceBlockAt(world, firePos))
			world.setBlockState(firePos, Blocks.FIRE.getDefaultState(), 3);
	}

	@SideOnly(Side.CLIENT)
	private void spawnTraceParticle(Vec3d start, Vec3d end)
	{
		if(fluidStack==null||fluidStack.getFluid()==null)
			return;

		Fluid fluidStack = this.fluidStack.getFluid();
		boolean gas = (fluidStack.isGaseous(this.fluidStack)||ChemthrowerHandler.isGas(fluidStack));
		String particleName = gas?"gas": "fluid";
		IIColor color = IIClientUtils.getFluidTextureColor(this.fluidStack);
		if(ignited)
			particleName = ticksExisted < 3?particleName+"_fire": (gas?"fire_gas": "fire");

		AbstractParticle particle = ParticleRegistry.spawnParticle("chemthrower/"+particleName, start, new Vec3d(motionX, motionY, motionZ), new Vector2f());
		if(particle!=null)
			particle.withProperty(ParticleProperties.STRETCH, new Vector3f((float)end.x, (float)end.y, (float)end.z))
					.withProperty(ParticleProperties.COLOR, color)
					.withProperty(ParticleProperties.SIZE, (gas?2.5f: 0.75f)*MathHelper.clamp(ticksExisted/6f, 0.35f, 1f));
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean canRenderOnFire()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isInRangeToRender3d(double x, double y, double z)
	{
		return false;
	}

	//--- Mirage compat ---//

	@Override
	public float getBrightness()
	{
		if(fluidStack!=null)
		{
			int light = ignited?15: fluidStack.getFluid().getLuminosity(fluidStack);
			if(light > 0)
				return Math.max(light, super.getBrightness());
		}
		return super.getBrightness();
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Method(modid = "mirage")
	public void gatherLights(GatherLightsEvent gatherLightsEvent, Entity entity)
	{
		if(fluidStack==null)
			return;

		int light = ignited?15: fluidStack.getFluid().getLuminosity(fluidStack);
		if(light > 0)
			gatherLightsEvent.add(Light.builder().pos(this).radius(0.05f*light).color(1, 1, 1).build());
	}
}
