package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler.ChemthrowerEffect;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler.ChemthrowerEffect_Potion;
import blusunrize.immersiveengineering.common.entities.EntityIEProjectile;
import blusunrize.immersiveengineering.common.util.IEFluid;
import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.IEntityLightEventConsumer;
import com.elytradev.mirage.lighting.Light;
import com.google.common.base.Optional;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.MultipleRayTracer;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.MultipleRayTracer.MultipleTracerBuilder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Expansion of the IE's Chemthrower.
 * Originally created due to forced Albedo compat on IE's side.
 *
 * @author Pabilo8
 * @since 08.07.2023
 */
@net.minecraftforge.fml.common.Optional.Interface(iface = "com.elytradev.mirage.lighting.IEntityLightEventConsumer", modid = "mirage")
public class EntityIIChemthrowerShot extends EntityIEProjectile implements IEntityLightEventConsumer
{
	private final ArrayList<Entity> hitEntities = new ArrayList<>();
	private final ArrayList<BlockPos> hitPos = new ArrayList<>();

	private FluidStack fluid;
	private static final DataParameter<Optional<FluidStack>> dataMarker_fluid = EntityDataManager.createKey(EntityIIChemthrowerShot.class, IEFluid.OPTIONAL_FLUID_STACK);

	public EntityIIChemthrowerShot(World world)
	{
		super(world);
	}

	public EntityIIChemthrowerShot(World world, double x, double y, double z, double ax, double ay, double az, FluidStack fluid)
	{
		super(world, x, y, z, ax, ay, az);
		this.fluid = fluid;
		this.setFluidSynced();
	}

	public EntityIIChemthrowerShot(World world, EntityLivingBase living, double ax, double ay, double az, FluidStack fluid)
	{
		super(world, living, ax, ay, az);
		this.fluid = fluid;
		this.setFluidSynced();
	}

	public EntityIIChemthrowerShot withShooters(Entity... entities)
	{
		if(entities.length > 0)
			this.hitEntities.addAll(Arrays.asList(entities));
		return this;
	}

	public EntityIIChemthrowerShot withShooters(BlockPos... entities)
	{
		if(entities.length > 0)
			this.hitPos.addAll(Arrays.asList(entities));
		return this;
	}

	public EntityIIChemthrowerShot withMotion(double x, double y, double z)
	{
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		return this;
	}

	public EntityIIChemthrowerShot withMotion(Vec3d vecDir)
	{
		return withMotion(vecDir.x, vecDir.y, vecDir.z);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(dataMarker_fluid, Optional.absent());
	}

	public void setFluidSynced()
	{
		if(this.getFluid()!=null)
			this.dataManager.set(dataMarker_fluid, Optional.of(this.getFluid()));
	}

	public FluidStack getFluidSynced()
	{
		return this.dataManager.get(dataMarker_fluid).orNull();
	}

	public FluidStack getFluid()
	{
		return fluid;
	}

	@Override
	public double getGravity()
	{
		if(getFluid()!=null)
		{
			FluidStack fluidStack = getFluid();
			boolean isGas = fluidStack.getFluid().isGaseous(fluidStack)||ChemthrowerHandler.isGas(fluidStack.getFluid());
			return (isGas?.025f: .05F)*(fluidStack.getFluid().getDensity(fluidStack) < 0?-1: 1);
		}
		return super.getGravity();
	}

	@Override
	public boolean canIgnite()
	{
		return ChemthrowerHandler.isFlammable(getFluid()==null?null: getFluid().getFluid());
	}

	@Override
	public void onUpdate()
	{
		if(this.getShooter()==null&&this.world.isRemote)
			this.shootingEntity = getShooterSynced();
		this.onEntityUpdate();

		if(!inGround)
		{
			Vec3d nextPosition = getPositionVector().addVector(motionX, motionY, motionZ);
			MultipleRayTracer tracer = MultipleTracerBuilder.setPos(world, this.getPositionVector(), nextPosition)
					.setAABB(this.getEntityBoundingBox())
					.setFilters(this.hitEntities, this.hitPos)
					.volumetricTrace();

			for(RayTraceResult hit : tracer)
			{
				onImpact(hit);
				switch(hit.typeOfHit)
				{
					case BLOCK:
					{
						if(hitPos.contains(hit.getBlockPos()))
							continue;
					}
					break;
					case ENTITY:
					{
						if(hitEntities.contains(hit.entityHit))
							continue;
						//TODO: 10.07.2023 armor check
						if(fire > 0)
							hit.entityHit.setFire(fire);

						break;
					}
					default:
					case MISS:
						break;
				}
			}
		}
		else
		{
			IBlockState state = this.world.getBlockState(getPosition());
			Block block = state.getBlock();

			if(block==this.inBlock&&block.getMetaFromState(state)==this.inMeta)
			{
				++this.ticksInGround;
				if(this.ticksInGround >= getMaxTicksInGround())
					this.setDead();
			}
			else
			{
				this.inGround = false;
				this.motionX *= this.rand.nextFloat()*0.2F;
				this.motionY *= this.rand.nextFloat()*0.2F;
				this.motionZ *= this.rand.nextFloat()*0.2F;
				this.ticksInGround = 0;
				this.ticksInAir = 0;
			}
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;

		this.prevRotationYaw = rotationYaw;
		this.prevRotationPitch = rotationPitch;

		Vec3d normalized = new Vec3d(motionX, motionY, motionZ).normalize();
		float motionXZ = MathHelper.sqrt(normalized.x*normalized.x+normalized.z*normalized.z);
		this.rotationYaw = (float)((Math.atan2(normalized.x, normalized.z)*180D)/3.1415927410125732D);
		this.rotationPitch = -(float)((Math.atan2(normalized.y, motionXZ)*180D)/3.1415927410125732D);

		if(this.isInWater())
		{
			for(int j = 0; j < 4; ++j)
			{
				float f3 = 0.25F;
				this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX-this.motionX*(double)f3, this.posY-this.motionY*(double)f3, this.posZ-this.motionZ*(double)f3, this.motionX, this.motionY, this.motionZ);
			}
			this.motionX *= 0.2;
			this.motionY *= 0.2;
			this.motionZ *= 0.2;
		}

		this.motionY -= getGravity();
		this.setPosition(this.posX, this.posY, this.posZ);
		this.doBlockCollisions();
	}

	/**
	 * Gets called every tick from main Entity class
	 */
	@Override
	public void onEntityUpdate()
	{
		if(this.getFluid()==null&&this.world.isRemote)
			this.fluid = getFluidSynced();
		IBlockState state = world.getBlockState(new BlockPos(posX, posY, posZ));
		Block b = state.getBlock();
		if(b!=null&&this.canIgnite()&&(state.getMaterial()==Material.FIRE||state.getMaterial()==Material.LAVA))
			this.setFire(6);
		super.onEntityUpdate();
	}

	/**
	 * Sets entity to burn for x amount of seconds, cannot lower amount of existing fire.
	 */
	@Override
	public void setFire(int seconds)
	{
		if(!canIgnite())
			return;
		super.setFire(seconds);
	}

	@Override
	public void onImpact(RayTraceResult mop)
	{
		//TODO: 10.07.2023 improve
		if(!this.world.isRemote&&getFluid()!=null)
		{
			FluidStack fluidStack = getFluid();
			Fluid fluid = fluidStack.getFluid();
			ChemthrowerEffect effect = ChemthrowerHandler.getEffect(fluid);
			boolean fire = fluid.getTemperature(fluidStack) > 1000;
			if(effect!=null)
			{
				ItemStack thrower = ItemStack.EMPTY;
				EntityPlayer shooter = (EntityPlayer)this.getShooter();
				if(shooter!=null)
					thrower = shooter.getHeldItem(EnumHand.MAIN_HAND);

				if(mop.typeOfHit==Type.ENTITY&&mop.entityHit instanceof EntityLivingBase)
					effect.applyToEntity((EntityLivingBase)mop.entityHit, shooter, thrower, fluidStack);
				else if(mop.typeOfHit==Type.BLOCK)
					effect.applyToBlock(world, mop, shooter, thrower, fluidStack);
			}
			else if(mop.entityHit!=null&&fluid.getTemperature(fluidStack) > 500)
			{
				int tempDiff = fluid.getTemperature(fluidStack)-300;
				int damage = Math.abs(tempDiff)/500;
				if(mop.entityHit.attackEntityFrom(DamageSource.LAVA, damage))
					mop.entityHit.hurtResistantTime = (int)(mop.entityHit.hurtResistantTime*.75);
			}
			if(mop.entityHit!=null)
			{
				int f = this.isBurning()?this.fire: fire?3: 0;
				if(f > 0)
				{
					mop.entityHit.setFire(f);
					if(mop.entityHit.attackEntityFrom(DamageSource.IN_FIRE, 2))
						mop.entityHit.hurtResistantTime = (int)(mop.entityHit.hurtResistantTime*.75);
				}
			}
		}
	}

	@Override
	protected boolean allowFriendlyFire(EntityPlayer target)
	{
		FluidStack fluidStack = getFluid();
		if(fluidStack!=null)
		{
			ChemthrowerEffect effect = ChemthrowerHandler.getEffect(fluidStack.getFluid());
			return effect instanceof ChemthrowerEffect_Potion&&!((ChemthrowerEffect_Potion)effect).getIsNegative();
		}
		return false;
	}


	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender()
	{
		FluidStack fluidStack = getFluid();
		if(fluidStack!=null)
		{
			int light = this.isBurning()?15: fluidStack.getFluid().getLuminosity(fluidStack);
			int superBrightness = super.getBrightnessForRender();
			light = (superBrightness&(0xff<<20))|(light<<4);
			if(light > 0)
				return Math.max(light, superBrightness);
		}
		return super.getBrightnessForRender();
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness()
	{
		FluidStack fluidStack = getFluid();
		if(fluidStack!=null)
		{
			int light = this.isBurning()?15: fluidStack.getFluid().getLuminosity(fluidStack);
			if(light > 0)
				return Math.max(light, super.getBrightness());
		}
		return super.getBrightness();
	}

	@Override
	@SideOnly(Side.CLIENT)
	@net.minecraftforge.fml.common.Optional.Method(modid = "mirage")
	public void gatherLights(GatherLightsEvent gatherLightsEvent, Entity entity)
	{
		//TODO: 09.07.2023 colored lighting
		FluidStack fluidStack = getFluid();
		if(fluidStack!=null)
		{
			int light = this.isBurning()?15: fluidStack.getFluid().getLuminosity(fluidStack);
			if(light > 0)
				gatherLightsEvent.add(Light.builder().pos(this).radius(.05f*light).color(1, 1, 1).build());
		}
	}
}
