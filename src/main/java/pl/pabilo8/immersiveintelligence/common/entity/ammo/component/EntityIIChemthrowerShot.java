package pl.pabilo8.immersiveintelligence.common.entity.ammo.component;

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
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.FactoryTracer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Expansion of the IE's Chemthrower.
 * Originally created due to forced Albedo compat on IE's side.
 *
 * @author Pabilo8
 * @since 08.07.2023
 * @author Avalon
 * @since 15.12.2024
 */
@net.minecraftforge.fml.common.Optional.Interface(iface = "com.elytradev.mirage.lighting.IEntityLightEventConsumer", modid = "mirage")
public class EntityIIChemthrowerShot extends EntityIEProjectile implements IEntityLightEventConsumer {
	private static final double SIZE = 0.5;

	protected Set<Entity> hitEntities = new HashSet<>();
	protected Set<BlockPos> hitPos = new HashSet<>();

	protected final FactoryTracer flightTracer = FactoryTracer.create(new AxisAlignedBB(-SIZE, -SIZE, -SIZE, SIZE, SIZE, SIZE))
			.setFilters(this.hitEntities, this.hitPos);

	private FluidStack fluid;
	private static final DataParameter<Optional<FluidStack>> dataMarker_fluid = EntityDataManager.createKey(EntityIIChemthrowerShot.class, IEFluid.OPTIONAL_FLUID_STACK);

	public EntityIIChemthrowerShot(World world) {
		super(world);
		hitEntities.add(this);
	}

	public EntityIIChemthrowerShot(World world, double x, double y, double z, double ax, double ay, double az, FluidStack fluid) {
		super(world, x, y, z, ax, ay, az);
		this.fluid = fluid;
		this.setFluidSynced();
	}

	public EntityIIChemthrowerShot(World world, EntityLivingBase living, double ax, double ay, double az, FluidStack fluid) {
		super(world, living, ax, ay, az);
		this.fluid = fluid;
		this.setFluidSynced();
	}

	public EntityIIChemthrowerShot withShooters(Entity... entities) {
		if (entities.length > 0)
			this.hitEntities.addAll(Arrays.asList(entities));
		return this;
	}

	public EntityIIChemthrowerShot withShooters(Collection<BlockPos> blockPos) {
		if (!blockPos.isEmpty())
			this.hitPos.addAll(blockPos);
		return this;
	}

	public EntityIIChemthrowerShot withShooters(BlockPos... blockPos) {
		if (blockPos.length > 0)
			this.hitPos.addAll(Arrays.asList(blockPos));
		return this;
	}

	public EntityIIChemthrowerShot withMotion(double x, double y, double z) {
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		return this;
	}

	public EntityIIChemthrowerShot withMotion(Vec3d vecDir) {
		return withMotion(vecDir.x, vecDir.y, vecDir.z);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(dataMarker_fluid, Optional.absent());
	}

	public void setFluidSynced() {
		if (this.getFluid() != null)
			this.dataManager.set(dataMarker_fluid, Optional.of(this.getFluid()));
	}

	public FluidStack getFluidSynced() {
		return this.dataManager.get(dataMarker_fluid).orNull();
	}

	public FluidStack getFluid() {
		return fluid;
	}

	@Override
	public double getGravity() {
		if (getFluid() != null) {
			FluidStack fluidStack = getFluid();
			boolean isGas = fluidStack.getFluid().isGaseous(fluidStack) || ChemthrowerHandler.isGas(fluidStack.getFluid());
			return (isGas ? 0.025f : 0.05F) * (fluidStack.getFluid().getDensity(fluidStack) < 0 ? -1 : 1);
		}
		return super.getGravity();
	}

	@Override
	public void onUpdate() {
		if (this.getShooter() == null && this.world.isRemote)
			this.shootingEntity = getShooterSynced();
		this.onEntityUpdate();

		if (!inGround) {
			flightTracer.stepTrace(world, this.getPositionVector(),
					this.getPositionVector().addVector(motionX, motionY, motionZ),
					hit -> {
						if (hit == null || hit.typeOfHit == null) {
							System.out.println("Invalid RayTraceResult: " + hit);
							return false;
						}

						onImpact(hit);

						switch (hit.typeOfHit) {
							case BLOCK:
								return hitPos.add(hit.getBlockPos());
							case ENTITY:
								if (hit.entityHit != null) {
									return onEntityHit(hit.entityHit);
								}
								break;
							default:
								System.out.println("Unhandled hit type: " + hit.typeOfHit);
								break;
						}
						return false;
					}
			);
		} else {
			handleInGroundState();
		}

		updatePositionAndMotion();
	}

	private void handleInGroundState() {
		IBlockState state = this.world.getBlockState(getPosition());
		Block block = state.getBlock();

		if (block == this.inBlock && block.getMetaFromState(state) == this.inMeta) {
			++this.ticksInGround;
			if (this.ticksInGround >= getMaxTicksInGround())
				this.setDead();
		} else {
			this.inGround = false;
			this.motionX *= this.rand.nextFloat() * 0.2F;
			this.motionY *= this.rand.nextFloat() * 0.2F;
			this.motionZ *= this.rand.nextFloat() * 0.2F;
			this.ticksInGround = 0;
			this.ticksInAir = 0;
		}
	}

	private void updatePositionAndMotion() {
		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;

		this.motionY -= getGravity();
		this.setPosition(this.posX, this.posY, this.posZ);
		this.doBlockCollisions();
	}

	private boolean onEntityHit(Entity hit) {
		if (hit == null) {
			System.out.println("Attempted to process a null entity.");
			return false;
		}

		if (!hitEntities.add(hit)) return false;

		if (fire > 0) hit.setFire(fire);
		return true;
	}

	@Override
	public void onImpact(RayTraceResult mop) {
		if (mop == null || mop.typeOfHit == null) {
			System.out.println("RayTraceResult is null or invalid.");
			return;
		}

		if (!this.world.isRemote && getFluid() != null) {
			FluidStack fluidStack = getFluid();
			if (fluidStack != null && fluidStack.getFluid() != null) {
				handleFluidImpact(mop, fluidStack);
			} else {
				System.out.println("FluidStack or Fluid is null.");
			}
		} else {
			System.out.println("World is client-side or fluid is null.");
		}
	}

	@Override
	protected boolean allowFriendlyFire(EntityPlayer target)
	{
		return false;
	}

	private void handleFluidImpact(RayTraceResult mop, FluidStack fluidStack) {
		Fluid fluid = fluidStack.getFluid();
		ChemthrowerEffect effect = ChemthrowerHandler.getEffect(fluid);

		boolean fire = fluid.getTemperature(fluidStack) > 1000;

		if (effect != null) {
			applyChemthrowerEffect(mop, fluidStack, fluid, effect);
		} else if (mop.entityHit != null && fluid.getTemperature(fluidStack) > 500) {
			int tempDiff = fluid.getTemperature(fluidStack) - 300;
			int damage = Math.abs(tempDiff) / 500;
			if (mop.entityHit.attackEntityFrom(DamageSource.LAVA, damage)) {
				mop.entityHit.hurtResistantTime = (int) (mop.entityHit.hurtResistantTime * 0.75);
			}
		}

		if (mop.entityHit != null) {
			int f = this.isBurning() ? this.fire : fire ? 3 : 0;
			if (f > 0) {
				mop.entityHit.setFire(f);
				if (mop.entityHit.attackEntityFrom(DamageSource.IN_FIRE, 2)) {
					mop.entityHit.hurtResistantTime = (int) (mop.entityHit.hurtResistantTime * 0.75);
				}
			}
		}
	}

	private void applyChemthrowerEffect(RayTraceResult mop, FluidStack fluidStack, Fluid fluid, ChemthrowerEffect effect) {
		ItemStack thrower = ItemStack.EMPTY;
		EntityPlayer shooter = (EntityPlayer) this.getShooter();
		if (shooter != null) {
			thrower = shooter.getHeldItem(EnumHand.MAIN_HAND);
		}

		if (mop.typeOfHit == Type.ENTITY && mop.entityHit instanceof EntityLivingBase) {
			effect.applyToEntity((EntityLivingBase) mop.entityHit, shooter, thrower, fluidStack);
		} else if (mop.typeOfHit == Type.BLOCK) {
			effect.applyToBlock(world, mop, shooter, thrower, fluidStack);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender() {
		FluidStack fluidStack = getFluid();
		if (fluidStack != null) {
			int light = this.isBurning() ? 15 : fluidStack.getFluid().getLuminosity(fluidStack);
			int superBrightness = super.getBrightnessForRender();
			light = (superBrightness & (0xff << 20)) | (light << 4);
			if (light > 0)
				return Math.max(light, superBrightness);
		}
		return super.getBrightnessForRender();
	}

	@Override
	public float getBrightness() {
		FluidStack fluidStack = getFluid();
		if (fluidStack != null) {
			int light = this.isBurning() ? 15 : fluidStack.getFluid().getLuminosity(fluidStack);
			if (light > 0)
				return Math.max(light, super.getBrightness());
		}
		return super.getBrightness();
	}

	@Override
	@SideOnly(Side.CLIENT)
	@net.minecraftforge.fml.common.Optional.Method(modid = "mirage")
	public void gatherLights(GatherLightsEvent gatherLightsEvent, Entity entity) {
		FluidStack fluidStack = getFluid();
		if (fluidStack != null) {
			int light = this.isBurning() ? 15 : fluidStack.getFluid().getLuminosity(fluidStack);
			if (light > 0)
				gatherLightsEvent.add(Light.builder().pos(this).radius(.05f * light).color(1, 1, 1).build());
		}
	}
}