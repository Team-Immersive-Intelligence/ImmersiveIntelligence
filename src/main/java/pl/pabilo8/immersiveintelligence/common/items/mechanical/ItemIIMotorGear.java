package pl.pabilo8.immersiveintelligence.common.items.mechanical;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
		super("motor_gear", 64, MotorGear.getNames());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		float mod = getGearTorqueModifier(stack);
		//speed : torque
		float speed = mod < 1&&mod!=0?1f/mod: 1;
		float torque = mod >= 1?mod: 1;

		tooltip.add(I18n.format(CommonProxy.INFO_KEY+"gear_ratio",
				speed==(int)speed?String.valueOf((int)speed): String.valueOf(speed),
				torque==(int)torque?String.valueOf((int)torque): String.valueOf(torque)
		));
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

		final float torqueMod;

		MotorGear(float torqueMod)
		{
			this.torqueMod = torqueMod;
		}

		public static String[] getNames()
		{
			ArrayList<String> list = new ArrayList<>();
			for(MotorGear belt : values())
				list.add(belt.getName());
			return list.toArray(new String[0]);
		}

		@Override
		public String getName()
		{
			return this.toString().toLowerCase(Locale.ENGLISH);
		}
	}

}
