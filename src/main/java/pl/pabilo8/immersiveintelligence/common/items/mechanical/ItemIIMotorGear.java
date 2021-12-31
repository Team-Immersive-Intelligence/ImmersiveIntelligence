package pl.pabilo8.immersiveintelligence.common.items.mechanical;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.MechanicalDevices;
import pl.pabilo8.immersiveintelligence.api.rotary.IMotorGear;
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
		super("motor_gear", 64, MotorGear.getNames());
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add("Ratio:"+String.format("%.2f", getGearTorqueModifier(stack)));
	}

	@Override
	public float getGearTorqueModifier(ItemStack stack)
	{
		return MotorGear.valueOf(subNames[stack.getMetadata()].toUpperCase()).torqueMod;
	}

	enum MotorGear implements IStringSerializable
	{
		COPPER(MechanicalDevices.gear_torque_modifier[0]),
		BRASS(MechanicalDevices.gear_torque_modifier[1]),
		IRON(MechanicalDevices.gear_torque_modifier[2]),
		STEEL(MechanicalDevices.gear_torque_modifier[3]),
		TUNGSTEN(MechanicalDevices.gear_torque_modifier[4]);

		float torqueMod;

		MotorGear(float torqueMod)
		{
			this.torqueMod = torqueMod;
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
