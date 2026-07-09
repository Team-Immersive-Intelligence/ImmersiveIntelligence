package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.HeavyChemthrower;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

import javax.annotation.Nullable;

public class EmplacementWeaponHeavyChemthrower extends EmplacementWeaponTurretBase
{
	private FluidTank tank = new FluidTank(HeavyChemthrower.tankCapacity);
	private SidedFluidHandler fluidHandler = new SidedFluidHandler(this);
	private boolean shouldIgnite = false;

	public EmplacementWeaponHeavyChemthrower()
	{
		this.setup = new MultiblockInteractablePart(HeavyChemthrower.setupTime);
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.visionAABB = this.visionAABB.grow(HeavyChemthrower.detectionRadius);
		this.attackAABB = this.attackAABB.grow(HeavyChemthrower.attackRadius);
		this.aim.withAimSpeed(HeavyChemthrower.yawRotateSpeed, HeavyChemthrower.pitchRotateSpeed);
		this.setup = new MultiblockInteractablePart(HeavyChemthrower.setupTime);
	}

	@Override
	public String getName()
	{
		return "heavy_chemthrower";
	}

	@Override
	public boolean canShoot(TileEntityEmplacement te)
	{
		return false;
	}

	@Override
	public int getShotDelay()
	{
		return HeavyChemthrower.sprayTime;
	}

	@Override
	public int getReloadDelay()
	{
		return 0;
	}

	@Nullable
	@Override
	public IFluidHandler getBaseFluidHandler()
	{
		return fluidHandler;
	}

	/*@Override
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
	}*/

	@Override
	public int getEnergyUpkeepCost()
	{
		return HeavyChemthrower.energyUpkeepCost;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initializeGUI(DecoPanel panelBase, DecoPanel panelPlatform)
	{


		panelPlatform.addComponent(
				new DecoFluidTank(4, 4+2)
						.withFluidTank(tank)
						.withHeight(panelPlatform.height-8)
		);
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

	@Override
	public boolean handleDataCommand(DataPacket packet)
	{
		return super.handleDataCommand(packet);
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
		nbt.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		super.deserializeNBT(nbt);
		tank.readFromNBT(nbt.getCompoundTag("tank"));
	}
}
