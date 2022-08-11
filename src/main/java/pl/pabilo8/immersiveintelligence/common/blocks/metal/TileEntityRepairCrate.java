package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.IEntitySpecialRepairable;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.client.render.metal_device.RepairCrateRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.IISounds;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.EffectCrates.repairCrateEnergyPerAction;

/**
 * @author Pabilo8
 * @since 06.07.2020
 */
public class TileEntityRepairCrate extends TileEntityEffectCrate implements ISoundTile
{
	public boolean repaired = false;
	public boolean shouldRepairArmor = true;
	public boolean shouldRepairVehicles = true;

	public TileEntityRepairCrate()
	{
		inventory = NonNullList.withSize(16, ItemStack.EMPTY);
		insertionHandler = new IEInventoryHandler(16, this);
	}

	@Override
	boolean isSupplied()
	{
		return ((shouldRepairArmor)||(shouldRepairVehicles))&&inventory.stream().anyMatch(stack -> !stack.isEmpty());
	}

	@Override
	void useSupplies()
	{
		for(ItemStack itemStack : inventory)
			if(!itemStack.isEmpty())
			{
				itemStack.shrink(1);
				break;
			}
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		shouldRepairArmor = nbt.getBoolean("shouldHeal");
		shouldRepairVehicles = nbt.getBoolean("shouldBoost");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setBoolean("shouldHeal", shouldRepairArmor);
		nbt.setBoolean("shouldBoost", shouldRepairVehicles);
	}

	@Override
	public void update()
	{
		super.update();
		ImmersiveEngineering.proxy.handleTileSound(IISounds.welding_mid, this, hasUpgrade(IIContent.UPGRADE_INSERTER)&&focusedEntity!=null, .5f, 1);
	}

	@Override
	boolean affectEntity(Entity entity, boolean upgraded)
	{
		if(!upgraded||(repairCrateEnergyPerAction <= energyStorage))
		{
			repaired = false;
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
			{
				energyStorage -= repairCrateEnergyPerAction;
			}
			return repaired;
		}
		return false;
	}

	@Override
	boolean checkEntity(Entity entity)
	{
		return entity instanceof IEntitySpecialRepairable||(entity instanceof EntityLivingBase);
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_REPAIR_CRATE.ordinal();
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return Utils.compareToOreName(stack, "plateSteel");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderWithUpgrades(MachineUpgrade... upgrades)
	{
		RepairCrateRenderer.renderWithUpgrade(upgrades);
	}

	@Override
	public boolean shoudlPlaySound(String sound)
	{
		return focusedEntity!=null;
	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		if(part==1)
			shouldRepairArmor = state;
		else if(part==2)
			shouldRepairVehicles = state;
		else
			super.onAnimationChangeClient(state, part);
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		if(part==1)
			shouldRepairArmor = state;
		else if(part==2)
			shouldRepairVehicles = state;
		else
			super.onAnimationChangeServer(state, part);
	}

	@Override
	protected NBTTagCompound makeSyncEntity()
	{
		if(hasUpgrade(IIContent.UPGRADE_INSERTER))
		{
			world.playSound(null, getPos(), focusedEntity!=null?IISounds.welding_start: IISounds.welding_end, SoundCategory.BLOCKS, 1f, 1f);
		}
		return super.makeSyncEntity();
	}
}
