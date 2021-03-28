package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.client.render.metal_device.MedicalCrateRenderer;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IIPotions;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.EffectCrates.*;

/**
 * @author Pabilo8
 * @since 06.07.2020
 */
public class TileEntityMedicalCrate extends TileEntityEffectCrate
{
	public TileEntityMedicalCrate()
	{
		inventory = NonNullList.withSize(9, ItemStack.EMPTY);
		insertionHandler = new IEInventoryHandler(9, this);
	}

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
	void affectEntity(Entity entity, boolean upgraded)
	{
		if(!upgraded||(repairCrateEnergyPerAction <= energyStorage))
		{
			boolean healed = false;
			if(entity instanceof EntityLivingBase)
			{
				if(!((EntityLivingBase)entity).isPotionActive(IIPotions.medical_treatment))
				{
					//upgraded: 10s / 6s effect, non: 20s, 10s effect
					((EntityLivingBase)entity).addPotionEffect(new PotionEffect(IIPotions.medical_treatment, upgraded?200: 400, upgraded?1: 0, true, true));
					healed = true;
				}
			}
			if(!upgraded&&healed)
				energyStorage -= mediCrateEnergyPerAction;
		}
	}

	@Override
	boolean checkEntity(Entity entity)
	{
		return entity instanceof EntityLivingBase;
	}


	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return new Vec3d(0.5, 0.5, 0.5);
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_MEDICRATE.ordinal();
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderWithUpgrades(MachineUpgrade... upgrades)
	{
		MedicalCrateRenderer.renderWithUpgrade(upgrades);
	}
}
