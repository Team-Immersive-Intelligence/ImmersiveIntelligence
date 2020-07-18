package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.utils.IEntitySpecialRepairable;
import pl.pabilo8.immersiveintelligence.common.IIPotions;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.EffectCrates.repairCrateEnergyPerHeal;

/**
 * @author Pabilo8
 * @since 06.07.2020
 */
public class TileEntityRepairCrate extends TileEntityEffectCrate
{
	NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);

	public TileEntityRepairCrate()
	{

	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!descPacket)
			inventory = Utils.readInventory(nbt.getTagList("inventory", 10), inventory.size());
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		if(!descPacket)
			nbt.setTag("inventory", Utils.writeInventory(inventory));
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
		if(repairCrateEnergyPerHeal <= energyStorage)
		{
			boolean repaired = false;
			if(entity instanceof IEntitySpecialRepairable)
			{
				IEntitySpecialRepairable repairable = (IEntitySpecialRepairable)entity;
				if(repairable.canRepair())
					repaired = repairable.repair(2);
			}
			else if(entity instanceof EntityLivingBase)
			{
				if(!((EntityLivingBase)entity).isPotionActive(IIPotions.undergoing_repairs))
				{
					((EntityLivingBase)entity).addPotionEffect(new PotionEffect(IIPotions.undergoing_repairs, 60, 1, false, false));
					repaired = true;
				}
			}
			if(repaired)
				energyStorage -= repairCrateEnergyPerHeal;
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
