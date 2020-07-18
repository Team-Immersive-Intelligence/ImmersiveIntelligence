package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;

import java.util.ArrayList;
import java.util.List;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.EffectCrates.energyDrain;
import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.EffectCrates.maxEnergyStored;

/**
 * @author Pabilo8
 * @since 06.07.2020
 */
public abstract class TileEntityEffectCrate extends TileEntityImmersiveConnectable implements IDirectionalTile, IBooleanAnimatedPartsBlock, ITickable, IUpgradableMachine
{
	public EnumFacing facing = EnumFacing.NORTH;
	public String name;
	public boolean open = false;
	public float lidAngle = 0;
	private ArrayList<MachineUpgrade> upgrades = new ArrayList<>();
	public int energyStorage = 0;

	@Override
	public EnumFacing getFacing()
	{
		return null;
	}

	@Override
	public void setFacing(EnumFacing facing)
	{
		this.facing = facing;
		if(facing.getAxis().isVertical())
			this.facing = EnumFacing.NORTH;
	}

	@Override
	public int getFacingLimitation()
	{
		return 2;
	}

	@Override
	public boolean mirrorFacingOnPlacement(EntityLivingBase placer)
	{
		return false;
	}

	@Override
	public boolean canHammerRotate(EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase entity)
	{
		return !entity.isSneaking();
	}

	@Override
	public boolean canRotate(EnumFacing axis)
	{
		return !axis.getAxis().isVertical();
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		if(nbt.hasKey("name"))
			this.name = nbt.getString("name");
		if(nbt.hasKey("open"))
			open = nbt.getBoolean("open");
		setFacing(EnumFacing.getFront(nbt.getInteger("facing")));
		getUpgradesFromNBT(nbt);
		energyStorage = nbt.getInteger("energyStorage");

	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		if(name!=null)
			nbt.setString("name", name);
		nbt.setBoolean("open", open);
		nbt.setInteger("facing", facing.getIndex());
		saveUpgradesToNBT(nbt);
		nbt.setInteger("energyStorage", energyStorage);

	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		open = state;
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		open = state;
	}

	@Override
	public void update()
	{
		updateLid();
		if(!world.isRemote&&hasUpgrade(CommonProxy.UPGRADE_INSERTER)&&isSupplied()&&world.getTotalWorldTime()%getEffectTime()==0)
		{
			//get all in range
			//effect
			List<Entity> entitiesWithinAABB = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(getPos()).grow(getRange()));
			entitiesWithinAABB.removeIf(this::checkEntity);
			if(entitiesWithinAABB.size() > 0)
			{
				entitiesWithinAABB.forEach(this::affectEntity);
				useSupplies();
			}
		}
	}

	@Override
	public boolean canFitUpgrade(MachineUpgrade upgrade)
	{
		return upgrade.equals(CommonProxy.UPGRADE_INSERTER);
	}

	@Override
	public boolean hasUpgrade(MachineUpgrade upgrade)
	{
		return upgrades.stream().anyMatch(machineUpgrade -> machineUpgrade.getName().equals(upgrade.getName()));
	}

	@Override
	public <T extends TileEntity & IUpgradableMachine> T getUpgradeMaster()
	{
		return (T)this;
	}

	@Override
	public void saveUpgradesToNBT(NBTTagCompound tag)
	{
		for(MachineUpgrade upgrade : upgrades)
			tag.setBoolean(upgrade.getName().toString(), true);
	}

	@Override
	public void getUpgradesFromNBT(NBTTagCompound tag)
	{
		upgrades.clear();
		upgrades.addAll(MachineUpgrade.getUpgradesFromNBT(tag));
	}

	@Override
	protected boolean canTakeLV()
	{
		return true;
	}

	@Override
	public boolean isEnergyOutput()
	{
		return true;
	}

	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset)
	{
		return hasUpgrade(CommonProxy.UPGRADE_INSERTER)&&super.canConnectCable(cableType, target, offset);
	}

	@Override
	public int outputEnergy(int amount, boolean simulate, int energyType)
	{
		if(amount > 0&&energyStorage < maxEnergyStored)
		{
			if(!simulate)
			{
				int rec = Math.min(maxEnergyStored-energyStorage, energyDrain);
				energyStorage += rec;
				return rec;
			}
			return Math.min(maxEnergyStored-energyStorage, energyDrain);
		}
		return 0;
	}

	private void updateLid()
	{
		if(open&&lidAngle < 1.5f)
			lidAngle = Math.min(lidAngle+0.2f, 1.5f);
		else if(!open&&lidAngle > 0f)
			lidAngle = Math.max(lidAngle-0.3f, 0f);
	}

	abstract boolean isSupplied();

	abstract void useSupplies();

	int getEffectTime()
	{
		return 20;
	}

	int getRange()
	{
		return 4;
	}

	abstract void affectEntity(Entity entity);

	abstract boolean checkEntity(Entity entity);
}
