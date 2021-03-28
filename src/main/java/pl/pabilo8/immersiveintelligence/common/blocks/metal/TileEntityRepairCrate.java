package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.IEntitySpecialRepairable;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.client.render.metal_device.MedicalCrateRenderer;
import pl.pabilo8.immersiveintelligence.client.render.metal_device.RepairCrateRenderer;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IIPotions;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.EffectCrates.repairCrateEnergyPerAction;

/**
 * @author Pabilo8
 * @since 06.07.2020
 */
public class TileEntityRepairCrate extends TileEntityEffectCrate
{
	public TileEntityRepairCrate()
	{
		inventory = NonNullList.withSize(9, ItemStack.EMPTY);
		insertionHandler = new IEInventoryHandler(9, this);
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
	void affectEntity(Entity entity, boolean upgraded)
	{
		if(!upgraded||(repairCrateEnergyPerAction <= energyStorage))
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
					((EntityLivingBase)entity).addPotionEffect(new PotionEffect(IIPotions.undergoing_repairs, upgraded?200: 400, upgraded?1: 0, true, true));
					repaired = true;
				}
			}
			if(!upgraded&&repaired)
				energyStorage -= repairCrateEnergyPerAction;
		}
	}

	@Override
	boolean checkEntity(Entity entity)
	{
		return entity instanceof IEntitySpecialRepairable||(entity instanceof EntityLivingBase);
	}


	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return new Vec3d(0.5, 0.5, 0.5);
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_REPAIR_CRATE.ordinal();
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderWithUpgrades(MachineUpgrade... upgrades)
	{
		RepairCrateRenderer.renderWithUpgrade(upgrades);
	}
}
