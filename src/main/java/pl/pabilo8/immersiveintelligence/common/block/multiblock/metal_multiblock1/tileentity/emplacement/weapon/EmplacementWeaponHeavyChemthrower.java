package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.HeavyChemthrower;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement.EmplacementStateNeeds;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon.EmplacementHitboxEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class EmplacementWeaponHeavyChemthrower extends EmplacementWeaponTurretBase
{
	boolean shouldIgnite = false;
	FluidTank tank = new FluidTank(HeavyChemthrower.tankCapacity);
	SidedFluidHandler fluidHandler = new SidedFluidHandler(this);

	public EmplacementWeaponHeavyChemthrower()
	{

	}

	@Override
	public void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.visionAABB = this.visionAABB.grow(HeavyChemthrower.detectionRadius);
		this.attackAABB = this.attackAABB.grow(HeavyChemthrower.attackRadius);
	}

	@Override
	public String getName()
	{
		return "heavy_chemthrower";
	}

	@Override
	public EmplacementStateNeeds onUpdate(TileEntityEmplacement te)
	{
		if(shootDelay > 0)
			shootDelay--;

		return super.onUpdate(te);
	}

	@Override
	public boolean canShoot(TileEntityEmplacement te)
	{
		return false;
	}

	@Override
	public float getYawTurnSpeed()
	{
		return HeavyChemthrower.yawRotateSpeed;
	}

	@Override
	public float getPitchTurnSpeed()
	{
		return HeavyChemthrower.pitchRotateSpeed;
	}

	@Override
	public float getShotDelay()
	{
		return HeavyChemthrower.sprayTime;
	}

	@Override
	public float getReloadDelay()
	{
		return 0;
	}

	@Override
	public float getSetupDelay()
	{
		return HeavyChemthrower.setupTime;
	}

	@Nullable
	@Override
	public IFluidHandler getBaseFluidHandler()
	{
		return fluidHandler;
	}

	@Override
	public void syncWithEntity(EntityEmplacementWeapon entity)
	{
		super.syncWithEntity(entity);
		if(entity==this.entity)
		{
			entity.aabb = new AxisAlignedBB(-3, 0, -3, 3, 3, 3);
			if((setupDelay!=0&&setupDelay!=HeavyChemthrower.setupTime)&&entity.ticksExisted%20==0)
			{
				entity.partArray = getCollisionBoxes();
			}
		}
	}

	@Override
	public EmplacementHitboxEntity[] getCollisionBoxes()
	{
		if(entity==null)
			return new EmplacementHitboxEntity[0];

		//new Vec3d(0,0,0)

		float t = this.setupDelay/(float)HeavyChemthrower.setupTime;

		ArrayList<EmplacementHitboxEntity> list = new ArrayList<>();
		list.add(new EmplacementHitboxEntity(entity, "baseBox", 1f, 1.25f,
				new Vec3d(0, 0.5, 0), Vec3d.ZERO, 12));
		list.add(new EmplacementHitboxEntity(entity, "baseBoxTop", 0.5f, 0.25f,
				new Vec3d(-0.25, 1.25, 0.25), Vec3d.ZERO, 12));
		list.add(new EmplacementHitboxEntity(entity, "baseBoxTop", 0.5f, 0.25f,
				new Vec3d(-0.25, 1.25, -0.25), Vec3d.ZERO, 12));

		//Increase amount of B A R R E L S
		list.add(new EmplacementHitboxEntity(entity, "barrelLeft", 0.5f, 1,
				new Vec3d(0.75, 0.8125, 0.5), Vec3d.ZERO, 4));
		list.add(new EmplacementHitboxEntity(entity, "barrelRight", 0.5f, 1,
				new Vec3d(0.75, 0.8125, -0.5), Vec3d.ZERO, 4));

		for(float f = -0.25f; f <= 0.25f; f += 0.5f)
		{
			list.add(new EmplacementHitboxEntity(entity, "gunBarrelLeft", 0.3125f, 0.3125f,
					new Vec3d(0, 0.8125, -0.25f), new Vec3d(-0.8125, 0, 0), 12));

			if(t > 0.35f)
				list.add(new EmplacementHitboxEntity(entity, "gunBarrelLeft", 0.3125f, 0.3125f,
						new Vec3d(0, 0.8125, f), new Vec3d(-1.125, 0, 0), 12));
			if(t > 0.5f)
				list.add(new EmplacementHitboxEntity(entity, "gunBarrelLeft", 0.3125f, 0.3125f,
						new Vec3d(0, 0.8125, f), new Vec3d(-1.4375, 0, 0), 12));
			if(t > 0.65f)
				list.add(new EmplacementHitboxEntity(entity, "gunBarrelLeft", 0.3125f, 0.3125f,
						new Vec3d(0, 0.8125, f), new Vec3d(-1.75, 0, 0), 12));
			if(t > 0.75f)
				list.add(new EmplacementHitboxEntity(entity, "gunBarrelLeft", 0.3125f, 0.3125f,
						new Vec3d(0, 0.8125, f), new Vec3d(-2.0625, 0, 0), 12));
			if(t > 0.9f)
				list.add(new EmplacementHitboxEntity(entity, "gunBarrelLeft", 0.425f, 0.425f,
						new Vec3d(0, 0.8125, f), new Vec3d(-2.487500011920929, 0, 0), 12));
		}

		return list.toArray(new EmplacementHitboxEntity[0]);
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return HeavyChemthrower.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return HeavyChemthrower.maxHealth;
	}

	private double getStackMass()
	{
		if(tank.getFluid()!=null)
			return (tank.getFluid().getFluid().isGaseous()?0.025F: 0.05F)*(float)(tank.getFluid().getFluid().getDensity(tank.getFluid()) < 0?-1: 1);
		else
			return 0;
	}

	static class SidedFluidHandler implements IFluidHandler
	{
		EmplacementWeaponHeavyChemthrower barrel;

		SidedFluidHandler(EmplacementWeaponHeavyChemthrower barrel)
		{
			this.barrel = barrel;
		}

		@Override
		public int fill(FluidStack resource, boolean doFill)
		{
			if(resource==null)
				return 0;
			return barrel.tank.fill(resource, doFill);
		}

		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain)
		{
			if(resource==null)
				return null;
			return this.drain(resource.amount, doDrain);
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain)
		{
			return barrel.tank.drain(maxDrain, doDrain);
		}

		@Override
		public IFluidTankProperties[] getTankProperties()
		{
			return barrel.tank.getTankProperties();
		}
	}

	//--- NBT ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = super.serializeNBT();
		nbt.setInteger("setupDelay", setupDelay);
		nbt.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		super.deserializeNBT(nbt);
		setupDelay = nbt.getInteger("setupDelay");
		tank.readFromNBT(nbt.getCompoundTag("tank"));
	}
}
