package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidTank;
import pl.pabilo8.immersiveintelligence.api.utils.IEntitySpecialRepairable;
import pl.pabilo8.immersiveintelligence.common.IIPotions;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.EffectCrates.mediCrateEnergyPerHeal;
import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.EffectCrates.mediCrateTankSize;

/**
 * @author Pabilo8
 * @since 06.07.2020
 */
public class TileEntityMedicalCrate extends TileEntityEffectCrate
{
	FluidTank tank = new FluidTank(mediCrateTankSize);

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!descPacket)
			tank = tank.readFromNBT(nbt.getCompoundTag("tank"));
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		if(!descPacket)
			nbt.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
	}

	@Override
	boolean isSupplied()
	{
		return true;
	}

	@Override
	void useSupplies()
	{

	}

	@Override
	void affectEntity(Entity entity)
	{
		if(mediCrateEnergyPerHeal <= energyStorage)
		{
			boolean healed = false;
			if(entity instanceof IEntitySpecialRepairable)
			{
				IEntitySpecialRepairable repairable = (IEntitySpecialRepairable)entity;
				if(repairable.canRepair())
					healed = repairable.repair(2);
			}
			else if(entity instanceof EntityLivingBase)
			{
				if(!((EntityLivingBase)entity).isPotionActive(IIPotions.undergoing_repairs))
				{
					((EntityLivingBase)entity).addPotionEffect(new PotionEffect(IIPotions.undergoing_repairs, 60, 1, false, false));
					healed = true;
				}
			}
			if(healed)
				energyStorage -= mediCrateEnergyPerHeal;
		}
	}

	@Override
	boolean checkEntity(Entity entity)
	{
		return entity instanceof IEntitySpecialRepairable;
	}


	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return new Vec3d(0.5, 0.5, 0.5);
	}
}
