package pl.pabilo8.immersiveintelligence.common.items.mechanical;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.MechanicalDevices;
import pl.pabilo8.immersiveintelligence.api.rotary.IMotorGear;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Pabilo8
 * @since 27-12-2019
 */
public class ItemIIMotorGear extends ItemIIBase implements IMotorGear
{
	public ItemIIMotorGear()
	{
		super("motor_gear", 16, MotorGear.getNames());
	}

	@Override
	public int getGearDurability(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "gear_damage"))
			ItemNBTHelper.setInt(stack, "gear_damage", 0);
		return ItemNBTHelper.getInt(stack, "gear_damage");
	}

	@Override
	public void damageGear(ItemStack stack, int amount)
	{
		int current = getGearDurability(stack)+amount;
		if(current <= getGearMaxDurability(stack))
			ItemNBTHelper.setInt(stack, "gear_damage", current);
		else
		{
			ItemNBTHelper.remove(stack, "gear_damage");
			stack.shrink(1);
		}
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return getGearDurability(stack) > 0;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return (double)getGearDurability(stack)/(double)getGearMaxDurability(stack);
	}

	@Override
	public int getGearMaxDurability(ItemStack stack)
	{
		return MotorGear.valueOf(subNames[stack.getMetadata()].toUpperCase()).maxDurability;
	}

	@Override
	public int getGearMinTorque(ItemStack stack)
	{
		return MotorGear.valueOf(subNames[stack.getMetadata()].toUpperCase()).minRPM;
	}

	@Override
	public int getGearMaxRPM(ItemStack stack)
	{
		return MotorGear.valueOf(subNames[stack.getMetadata()].toUpperCase()).maxRPM;
	}

	@Override
	public int getGearMaxTorque(ItemStack stack)
	{
		return MotorGear.valueOf(subNames[stack.getMetadata()].toUpperCase()).maxTorque;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if(flagIn.isAdvanced())
		{
			tooltip.add(I18n.format(CommonProxy.INFO_KEY+"gear_durability", getGearMaxDurability(stack)-getGearDurability(stack), getGearMaxDurability(stack)));
		}
	}

	enum MotorGear implements IStringSerializable
	{
		COPPER(MechanicalDevices.gear_min_rpm[0], MechanicalDevices.gear_max_rpm[0], MechanicalDevices.gear_max_torque[0], MechanicalDevices.gear_max_durability[0]),
		BRASS(MechanicalDevices.gear_min_rpm[1], MechanicalDevices.gear_max_rpm[1], MechanicalDevices.gear_max_torque[1], MechanicalDevices.gear_max_durability[1]),
		IRON(MechanicalDevices.gear_min_rpm[2], MechanicalDevices.gear_max_rpm[2], MechanicalDevices.gear_max_torque[2], MechanicalDevices.gear_max_durability[2]),
		STEEL(MechanicalDevices.gear_min_rpm[3], MechanicalDevices.gear_max_rpm[3], MechanicalDevices.gear_max_torque[3], MechanicalDevices.gear_max_durability[3]),
		TUNGSTEN(MechanicalDevices.gear_min_rpm[4], MechanicalDevices.gear_max_rpm[4], MechanicalDevices.gear_max_torque[4], MechanicalDevices.gear_max_durability[4]);

		int minRPM, maxRPM, maxTorque, maxDurability;

		MotorGear(int minRPM, int maxRPM, int maxTorque, int maxDurability)
		{
			this.minRPM = minRPM;
			this.maxRPM = maxRPM;
			this.maxTorque = maxTorque;
			this.maxDurability = maxDurability;
		}

		public static String[] getNames()
		{
			ArrayList<String> list = new ArrayList<String>();
			String[] a = new String[list.size()];
			for(MotorGear belt : values())
				list.add(belt.getName());
			return list.toArray(a);
		}

		@Override
		public String getName()
		{
			return this.toString().toLowerCase(Locale.ENGLISH);
		}
	}

}
